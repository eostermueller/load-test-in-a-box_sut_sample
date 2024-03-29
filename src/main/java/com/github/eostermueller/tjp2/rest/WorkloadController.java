package com.github.eostermueller.tjp2.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.eostermueller.snail4j.workload.AliasManager;
import com.github.eostermueller.snail4j.workload.ApiResponse;
import com.github.eostermueller.snail4j.workload.DefaultFactory;
import com.github.eostermueller.snail4j.workload.OnlyStringAndLongAndIntAreAllowedParameterTypes;
import com.github.eostermueller.snail4j.workload.Snail4jWorkloadException;
import com.github.eostermueller.snail4j.workload.Status;
import com.github.eostermueller.snail4j.workload.crypto.DecryptionException;
import com.github.eostermueller.snail4j.workload.crypto.WorkloadCrypto;
import com.github.eostermueller.snail4j.workload.engine.Workload;
import com.github.eostermueller.snail4j.workload.engine.WorkloadBuilder;
import com.github.eostermueller.snail4j.workload.engine.WorkloadInvocationException;
import com.github.eostermueller.snail4j.workload.markdown.MarkdownLoader;
import com.github.eostermueller.snail4j.workload.markdown.ParentMarkdownFile;
import com.github.eostermueller.snail4j.workload.model.UseCase;
import com.github.eostermueller.snail4j.workload.model.UseCases;
import com.github.eostermueller.snail4j.workload.model.WorkloadLibrary;
import com.github.eostermueller.snail4j.workload.model.json.SerializaionUtil;

@RequestMapping("/traffic")
@RestController
@EnableAutoConfiguration
@Configuration
@EnableWebMvc
public class WorkloadController implements WebMvcConfigurer {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@CrossOrigin 
	@RequestMapping(
		    value = "/execute",
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)	
	public ApiResponse execute() throws Snail4jWorkloadException, WorkloadInvocationException {
		ApiResponse apiResponse = new ApiResponse( System.nanoTime() );

		LOGGER.debug("in /workload");
		
		Workload workload = DefaultFactory.getFactory().getWorkloadSingleton();
		
		if (workload==null) {
			apiResponse.setStatus(Status.FAILURE);
		} else {
			workload.execute();
			apiResponse.setStatus(Status.SUCCESS);
		}
		
		return apiResponse;
	}
	@CrossOrigin 
	@RequestMapping(
		    value = "/markdown",
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)	
	public ApiResponse markdownList() throws Snail4jWorkloadException, WorkloadInvocationException, URISyntaxException, IOException {
		ApiResponse apiResponse = new ApiResponse( System.nanoTime() );
		
		MarkdownLoader loader = DefaultFactory.getFactory().createMarkdownLoader();
		
		loader.setLocator( DefaultFactory.getFactory().createMarkdownLocator() );
		
		List<ParentMarkdownFile> allParentMdFiles = loader.getMarkdownFiles();

//		SerializaionUtil util = DefaultFactory.getFactory().createSerializationUtil();

		apiResponse.setResult(allParentMdFiles);
		apiResponse.setNanoStop(System.nanoTime());
		apiResponse.setStatus(Status.SUCCESS);
		return apiResponse;
	}
	
	@CrossOrigin 
	@ResponseStatus(value = HttpStatus.OK)
    @PutMapping(value="/workload", consumes = MediaType.APPLICATION_JSON_VALUE)	
	public ApiResponse updateWorkload(
			@RequestBody String json
			) throws Snail4jWorkloadException, WorkloadInvocationException, OnlyStringAndLongAndIntAreAllowedParameterTypes, DecryptionException {
		
		ApiResponse apiResponse = new ApiResponse( System.nanoTime() );
		String aliasStatus = "<uninitialized>";
		

		SerializaionUtil util = DefaultFactory.getFactory().createSerializationUtil();
		
		LOGGER.debug("input js0n " + json );
		UseCases rq = util.unmmarshalUseCases(json);
		
			
		if (rq.getAlias()!=null && rq.getAlias().length() > 0) {
			AliasManager aliasManager = DefaultFactory.getFactory().getAliasManager();
			String encryptedKey = aliasManager.resolve(rq.getAlias());
			if (encryptedKey!=null && encryptedKey.length() > 0) {
				rq.setEncryptedKey(encryptedKey);
				aliasStatus = "alias " + rq.getAlias() + " successfully resolved";
			} else {
				apiResponse.setStatus(Status.FAILURE_ALIAS_DOES_NOT_EXIST);
				aliasStatus = String.format("Alias [%s] does not exist", rq.getAlias() );
				apiResponse.setMessage( aliasStatus );
			}
		}
		
		if (!apiResponse.isFailure()) {
			if (rq.getEncryptedKey()!=null && rq.getEncryptedKey().length()>0) {
				
				WorkloadCrypto wd = DefaultFactory.getFactory().getWorkloadCrypto();
				
				String plainTextWorkloadJson = wd.getDecryptedWorkload(DefaultFactory.getConfigLocation(), rq.getEncryptedKey() );
				rq = util.unmmarshalUseCases(plainTextWorkloadJson);
			}

			WorkloadBuilder workloadBuilder = DefaultFactory.getFactory().createWorkloadBuilder();
			
			try {
				Workload w = workloadBuilder.createWorkload(rq);
				
				if (w != null) {
					w.setVerboseState(rq);
					DefaultFactory.getFactory().setWorkloadSingleton(w);
					apiResponse.setStatus(Status.SUCCESS);
				} else {
					apiResponse.setStatus(Status.FAILURE_WORKLOAD_CREATION);
				}
			} catch (Exception e) {
				apiResponse.setStatus(Status.FAILURE_WORKLOAD_CREATION_EXCEPTION);
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				apiResponse.setMessage(
						String.format("Exception creating workload.  Message [%s] Stacktrace [%s]", 
						e.getMessage(),
						sw.toString()
						));
			}
		}
		
		apiResponse.setNanoStop( System.nanoTime() );
		
		return apiResponse;
	}
	
	@CrossOrigin 
	@RequestMapping(
		    value = "/workload", 		    		    
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)	
	public ApiResponse getWorkload(
			) throws Snail4jWorkloadException, WorkloadInvocationException, OnlyStringAndLongAndIntAreAllowedParameterTypes {

		ApiResponse apiResponse = new ApiResponse( System.nanoTime() );
		apiResponse.setStatus(Status.FAILURE); //assume the worst
		
		Workload workload = DefaultFactory.getFactory().getWorkloadSingleton();
		apiResponse.setStatus(Status.SUCCESS);
		if (workload!=null) {
			apiResponse.setResult( workload.getVerboseState() );
			LOGGER.debug("GET#3");
		}
		
		apiResponse.setNanoStop( System.nanoTime() );
		LOGGER.debug("getWorkload() apiResponse: " + apiResponse.toString() );
		return apiResponse;
	}
	@CrossOrigin 
	@RequestMapping(
		    value = "/encryptedWorkload", 		    		    
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)	
	public ApiResponse getEncryptedWorkload(
			) throws Snail4jWorkloadException, WorkloadInvocationException, OnlyStringAndLongAndIntAreAllowedParameterTypes {

		ApiResponse apiResponse = new ApiResponse( System.nanoTime() );
		apiResponse.setStatus(Status.FAILURE); //assume the worst
		
		Workload workload = DefaultFactory.getFactory().getWorkloadSingleton();
		apiResponse.setStatus(Status.SUCCESS);
		String encryptedWorkload = null;
		if (workload!=null) {
			
			WorkloadCrypto crypto = DefaultFactory.getFactory().getWorkloadCrypto();
			
			SerializaionUtil serialization = com.github.eostermueller.snail4j.workload.DefaultFactory.getFactory().createSerializationUtil();
		
			String json = serialization.marshalUseCases((UseCases) workload.getVerboseState());
			
			encryptedWorkload = crypto.getEncryptedWorkload(DefaultFactory.getConfigLocation(), json);
			
			apiResponse.setResult( encryptedWorkload );
			LOGGER.debug("GET#3");
		}
		
		apiResponse.setNanoStop( System.nanoTime() );
		LOGGER.debug("getWorkload() apiResponse: " + apiResponse.toString() );
		return apiResponse;
	}
	@CrossOrigin 
	@RequestMapping(
		    value = "/useCases", 		    		    
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)	
	public String useCases(
			@RequestParam("useCaseSearchCriteria") String useCaseSearchCriteria
			) throws Snail4jWorkloadException, WorkloadInvocationException, OnlyStringAndLongAndIntAreAllowedParameterTypes {
		
		long nanoStart = System.nanoTime();
		LOGGER.debug("About to call ClassGraph to search for [" + useCaseSearchCriteria + "]");
		UseCases useCases = WorkloadLibrary.scan(useCaseSearchCriteria);
		LOGGER.debug("Finished call ClassGraph");
		Comparator<UseCase> c = new Comparator<UseCase>() {
			@Override
			public int compare(UseCase o1, UseCase o2) {
				return o1.getName().compareToIgnoreCase( o2.getName() );
			}
		};
		
		LOGGER.debug("Before UseCase sort");
		useCases.sort(c);
		LOGGER.debug("After UseCase sort");

		long nanoStop = System.nanoTime();
		SerializaionUtil util = DefaultFactory.getFactory().createSerializationUtil();
		String json = util.marshalUseCases(useCases);
		LOGGER.debug("leaving WorkloadController.useCases() nano duration[" + (nanoStop-nanoStart) + "]");
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
