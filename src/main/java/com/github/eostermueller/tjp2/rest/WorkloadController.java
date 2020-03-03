package com.github.eostermueller.tjp2.rest;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.eostermueller.snail4j.workload.ApiResponse;
import com.github.eostermueller.snail4j.workload.DefaultFactory;
import com.github.eostermueller.snail4j.workload.Snail4jWorkloadException;
import com.github.eostermueller.snail4j.workload.OnlyStringAndLongAndIntAreAllowedParameterTypes;
import com.github.eostermueller.snail4j.workload.Status;
import com.github.eostermueller.snail4j.workload.engine.Workload;
import com.github.eostermueller.snail4j.workload.engine.WorkloadBuilder;
import com.github.eostermueller.snail4j.workload.engine.WorkloadInvocationException;
import com.github.eostermueller.snail4j.workload.model.Snail4jLibrary;
import com.github.eostermueller.snail4j.workload.model.UseCases;
import com.github.eostermueller.snail4j.workload.model.json.SerializaionUtil;

@RequestMapping("/traffic")
@RestController
@EnableAutoConfiguration
@Configuration
@EnableWebMvc
public class WorkloadController implements WebMvcConfigurer {
	@Autowired
	private ResourceLoader resourceLoader;
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	

	@CrossOrigin(origins = "http://localhost:8090")
	@RequestMapping(
		    value = "/execute",
		    method = RequestMethod.GET)	
	public ApiResponse execute() throws Snail4jWorkloadException, WorkloadInvocationException {
		ApiResponse apiResponse = new ApiResponse( System.nanoTime() );

		LOGGER.error("in /workload");
		
		Workload workload = DefaultFactory.getFactory().getWorkloadSingleton();
		
		if (workload==null) {
			apiResponse.setStatus(Status.FAILURE);
		} else {
			workload.execute();
			apiResponse.setStatus(Status.SUCCESS);
		}
		
		return apiResponse;
	}

	@CrossOrigin(origins = "http://localhost:8090")
	@RequestMapping(
		    value = "/workload", 		    		    
		    method = RequestMethod.PUT)	
	public ApiResponse updateWorkload(
			@RequestBody String js0n
			) throws Snail4jWorkloadException, WorkloadInvocationException, OnlyStringAndLongAndIntAreAllowedParameterTypes {
		
		ApiResponse apiResponse = new ApiResponse( System.nanoTime() );
		

		SerializaionUtil util = DefaultFactory.getFactory().createSerializationUtil();
		
		LOGGER.debug("input js0n " + js0n );
		UseCases rq = util.unmmarshalUseCases(js0n);

		LOGGER.debug("rq.getProcessingUnits().size(): " + rq.getUseCases().size() );
		WorkloadBuilder workloadBuilder = DefaultFactory.getFactory().createWorkloadBuilder();
		
		Workload w = workloadBuilder.createWorkload(rq);
		LOGGER.debug("punits" + w.size());
		
		w.setVerboseState(rq);
		LOGGER.debug("PUT#1");
		
		DefaultFactory.getFactory().setWorkloadSingleton(w);
		LOGGER.debug("PUT#2");
		
		apiResponse.setStatus(Status.SUCCESS);
		LOGGER.debug("PUT#3");
		
		return apiResponse;
	}
	@CrossOrigin(origins = "http://localhost:8090")
	@RequestMapping(
		    value = "/workload", 		    		    
		    method = RequestMethod.GET)	
	public ApiResponse getWorkload(
			) throws Snail4jWorkloadException, WorkloadInvocationException, OnlyStringAndLongAndIntAreAllowedParameterTypes {
		LOGGER.debug("GET#1");

		ApiResponse apiResponse = new ApiResponse( System.nanoTime() );
		apiResponse.setStatus(Status.FAILURE); //assume the worst
		
		LOGGER.debug("GET#2");

		Workload workload = DefaultFactory.getFactory().getWorkloadSingleton();
		apiResponse.setStatus(Status.SUCCESS);
		if (workload!=null) {
			apiResponse.setResult( workload.getVerboseState() );
			LOGGER.debug("GET#3");
		}
		
		apiResponse.setNanoStop( System.nanoTime() );
		return apiResponse;
	}
	@CrossOrigin(origins = "http://localhost:8090")
	@RequestMapping(
		    value = "/useCases", 		    		    
		    method = RequestMethod.GET)	
	public String useCases() throws Snail4jWorkloadException, WorkloadInvocationException, OnlyStringAndLongAndIntAreAllowedParameterTypes {
		
		long nanoStart = System.nanoTime();
		UseCases useCases = Snail4jLibrary.scan();  

		long nanoStop = System.nanoTime();
		SerializaionUtil util = DefaultFactory.getFactory().createSerializationUtil();
		String json = util.marshalUseCases(useCases);
		
		return json;
	}
	
    public static void main(String[] args) throws Exception {
        
		ApplicationContext ac = SpringApplication.run(WorkloadController.class, args);
        ServletContext sc = null;
                 if (ac instanceof WebApplicationContext) {
                         WebApplicationContext wac = (WebApplicationContext)ac;
                         sc = wac.getServletContext();
                 }
        
    }
}
