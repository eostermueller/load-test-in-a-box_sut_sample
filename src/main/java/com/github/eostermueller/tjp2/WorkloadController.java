package com.github.eostermueller.tjp2;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.eostermueller.havoc.workload.DefaultFactory;
import com.github.eostermueller.havoc.workload.HavocException;
import com.github.eostermueller.havoc.workload.OnlyStringAndLongAndIntAreAllowedParameterTypes;
import com.github.eostermueller.havoc.workload.engine.Workload;
import com.github.eostermueller.havoc.workload.engine.WorkloadInvocationException;
import com.github.eostermueller.havoc.workload.model.HavocLibrary;
import com.github.eostermueller.havoc.workload.model.UseCases;
import com.github.eostermueller.havoc.workload.model.json.SerializaionUtil;

@RequestMapping("/workload")
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
	public String workload() throws HavocException, WorkloadInvocationException {
		LOGGER.error("in /workload");
		
		Workload workload = DefaultFactory.getFactory().getWorkloadSingleton();
		
		long nanoStart = System.nanoTime();
		workload.execute();
		long nanoStop = System.nanoTime();
		StringBuilder sb = new StringBuilder();
		
		sb
		.append("{")
		.append("  \"workload\": {")
		.append("     \"size\": \"" + String.valueOf(workload.size()) + "\",")
		.append("     \"ns\": \"" + String.valueOf(nanoStop-nanoStart ) + "\",")
		.append("     }")
		.append("}");
		
		return sb.toString();
	}
	
	@CrossOrigin(origins = "http://localhost:8090")
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
