package com.github.eostermueller.tjp2.rest;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.eostermueller.havoc.rest.ApiResponse;
import com.github.eostermueller.havoc.rest.Status;
import com.github.eostermueller.havoc.workload.DefaultFactory;
import com.github.eostermueller.havoc.workload.HavocException;
import com.github.eostermueller.havoc.workload.OnlyStringAndLongAndIntAreAllowedParameterTypes;
import com.github.eostermueller.havoc.workload.engine.Workload;
import com.github.eostermueller.havoc.workload.engine.WorkloadBuilder;
import com.github.eostermueller.havoc.workload.engine.WorkloadInvocationException;
import com.github.eostermueller.havoc.workload.model.HavocLibrary;
import com.github.eostermueller.havoc.workload.model.UseCases;
import com.github.eostermueller.havoc.workload.model.WorkloadSpecRq;
import com.github.eostermueller.havoc.workload.model.json.SerializaionUtil;

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
	@Bean
	@RequestMapping(
		    value = "/execute",
		    method = RequestMethod.GET)	
	public ApiResponse execute() throws HavocException, WorkloadInvocationException {
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
	@Bean
	@RequestMapping(
		    value = "/workload", 		    		    
		    method = RequestMethod.PUT)	
	public ApiResponse updateWorkload(
			@RequestBody String js0n
			) throws HavocException, WorkloadInvocationException, OnlyStringAndLongAndIntAreAllowedParameterTypes {
		
		ApiResponse apiResponse = new ApiResponse( System.nanoTime() );
		

		SerializaionUtil util = DefaultFactory.getFactory().createSerializationUtil();
		
		WorkloadSpecRq rq = util.unmmarshalWorkloadUpdateRq(js0n);

		LOGGER.debug("rq.getProcessingUnits().size(): " + rq.getProcessingUnits().size() );
		WorkloadBuilder workloadBuilder = DefaultFactory.getFactory().createWorkloadBuilder();
		
		Workload w = workloadBuilder.createWorkload(rq);
		LOGGER.debug("punits" + w.size());
		
		w.setVerboseState(rq);
		
		DefaultFactory.getFactory().setWorkloadSingleton(w);
		
		apiResponse.setStatus(Status.SUCCESS);
		
		return apiResponse;
	}
	@CrossOrigin(origins = "http://localhost:8090")
	@Bean
	@RequestMapping(
		    value = "/workload", 		    		    
		    method = RequestMethod.GET)	
	public ApiResponse getWorkload(
			) throws HavocException, WorkloadInvocationException, OnlyStringAndLongAndIntAreAllowedParameterTypes {
		
		ApiResponse apiResponse = new ApiResponse( System.nanoTime() );

		Workload workload = DefaultFactory.getFactory().getWorkloadSingleton();
		if (workload!=null) {
			apiResponse.setResult( workload.getVerboseState() );
			apiResponse.setStatus(Status.SUCCESS);
		} else {
			//Perhaps ui has not started, or just not selected anything yet?
			apiResponse.setStatus(Status.FAILURE);
		}
		
		return apiResponse;
	}
	
	@CrossOrigin(origins = "http://localhost:8090")
	@Bean
	@RequestMapping(
		    value = "/useCases", 		    		    
		    method = RequestMethod.GET)	
	public String useCases() throws HavocException, WorkloadInvocationException, OnlyStringAndLongAndIntAreAllowedParameterTypes {
		
		long nanoStart = System.nanoTime();
		UseCases useCases = HavocLibrary.scan();  

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
