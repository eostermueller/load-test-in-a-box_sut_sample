package com.github.eostermueller.tjp2.rest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @stolenFrom: https://www.concretepage.com/spring-5/extendwith-springextension-spring-test
 * @author erikostermueller
 *
 */
@SpringBootTest
//(webEnvironment=WebEnvironment.RANDOM_PORT)
public class WorkloadRESTIntegrationTest {
    @Autowired
    protected WebApplicationContext webAppContext;
 
    private MockMvc mockMvc;
 
    @BeforeEach
    public void setup() {
        mockMvc = webAppContextSetup(webAppContext).build();
    }
    @Test
    public void webAppContextTest() throws Exception {
    	assertTrue(webAppContext.getServletContext() instanceof MockServletContext);
    }
    @Test
    public void canPutAndGetWorkload() throws Exception {
    	System.out.println("putAndGet#1");
        URI uri = UriComponentsBuilder.fromUriString("/traffic/workload")
        		.build().encode().toUri();
    	System.out.println("putAndGet#2");
        mockMvc.perform(
        			   put(uri)
        				.contentType(MediaType.APPLICATION_JSON_VALUE)
        				.accept(MediaType.APPLICATION_JSON)
        				.characterEncoding("UTF-8")
        				.content( putRq() )
        		)
        .andDo( MockMvcResultHandlers.print() )
        .andExpect( status().isOk() )
        .andExpect(jsonPath("$.status").value(100))
        .andExpect(jsonPath("$.message").value(0))
        ;
    	System.out.println("putAndGet#3");
    	
        mockMvc.perform(
	 			   get(uri)
	 				.contentType(MediaType.APPLICATION_JSON_VALUE)
	 				.accept(MediaType.APPLICATION_JSON)
	 				.characterEncoding("UTF-8")
	 		)
		 .andDo( MockMvcResultHandlers.print() )
		 .andExpect( status().isOk() )
         .andExpect(jsonPath("$.result.useCases", hasSize(5)))
         .andExpect(jsonPath("$.result.useCases[*].method.name", hasSize(5)))
         .andExpect(jsonPath("$.status").value(100))
         .andExpect(jsonPath("$.message").value(0))
		 ;
    	System.out.println("putAndGet#4");
        
    }
	private String putRq() {
		//return "{\"processingUnits\":[{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"Reuse same Transformers from pool\"}]},\"useCaseName\":\"xsltTransform\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.xslt.XsltProcessor\",\"name\":\"pooledTransformerXslt\"},\"selected\":true},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"Reinstantiate Transformer every time\"}]},\"useCaseName\":\"xsltTransform\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.xslt.XsltProcessor\",\"name\":\"unPooledTransformerXslt\"},\"selected\":false}],\"name\":\"xsltTransform\"}";
		return "{\"useCases\":[{\"processingUnits\":[{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - table-based Random - 10 items, 10 iterations\"}]},\"useCaseName\":\"busyOptimizedUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomTableInt_10_10_optimizedUuid\"},\"selected\":true},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - reuse Random - 10 items, 10 iterations\"}]},\"useCaseName\":\"busyOptimizedUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomNextInt_10_10_optimizedUuid\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - table-based Random - 1000 items, 1000 iterations\"}]},\"useCaseName\":\"busyOptimizedUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomTableInt_1000_1000_optimizedUuid\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - threadLocal Random - 1000 items, 1000 iterations\"}]},\"useCaseName\":\"busyOptimizedUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomThreadLocalInt_1000_1000_optimizedUuid\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - reuse Random - 1000 items, 1000 iterations\"}]},\"useCaseName\":\"busyOptimizedUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomNextInt_1000_1000_optimizedUuid\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - threadLocal Random - 10 items, 10 iterations\"}]},\"useCaseName\":\"busyOptimizedUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomThreadLocalInt_10_10_optimizedUuid\"},\"selected\":false}],\"name\":\"busyOptimizedUuid\"},{\"processingUnits\":[{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - threadLocal Random - 10 items, 10 iterations\"}]},\"useCaseName\":\"busySlowUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomThreadLocalInt_10_10\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - threadLocal Random - 1000 items, 1000 iterations\"}]},\"useCaseName\":\"busySlowUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomThreadLocalInt_1000_1000\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - table-based Random - 10 items, 10 iterations\"}]},\"useCaseName\":\"busySlowUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomTableInt_10_10\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - reuse Random - 10 items, 10 iterations\"}]},\"useCaseName\":\"busySlowUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomNextInt_10_10\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - reuse Random - 1000 items, 1000 iterations\"}]},\"useCaseName\":\"busySlowUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomNextInt_1000_1000\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"busy - table-based Random - 1000 items, 1000 iterations\"}]},\"useCaseName\":\"busySlowUuid\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.BusyProcessor\",\"name\":\"randomTableInt_1000_1000\"},\"selected\":false}],\"name\":\"busySlowUuid\"},{\"processingUnits\":[{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"every rq adds 10mb that stays in memory for no more than 60 seconds\"}]},\"useCaseName\":\"memStress\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.MemStress\",\"name\":\"memStress_10mb_lasts_60sec\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"every rq adds 100k that stays in memory for no more than 60 sec\"}]},\"useCaseName\":\"memStress\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.MemStress\",\"name\":\"memStress_100k_lasts_60sec\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"every rq adds 10k that stays in memory for no more than 5 min\"}]},\"useCaseName\":\"memStress\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.MemStress\",\"name\":\"memStress_10k_lasts_5min\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"every rq adds 1mb that stays in memory for no more than 60 seconds\"}]},\"useCaseName\":\"memStress\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.MemStress\",\"name\":\"memStress_1mb_lasts_60sec\"},\"selected\":false}],\"name\":\"memStress\"},{\"processingUnits\":[{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"sleep ms 100\"}]},\"useCaseName\":\"sleep\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.SleepDelay\",\"name\":\"simulateSlowCode_sleepMilliseconds_100\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"sleep ms 1\"}]},\"useCaseName\":\"sleep\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.SleepDelay\",\"name\":\"simulateSlowCode_sleepMilliseconds_1\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"sync sleep ms 10\"}]},\"useCaseName\":\"sleep\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.SleepDelay\",\"name\":\"simulateSynchronizedSlowCode_sleepMilliseconds_10\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"sleep ms 10\"}]},\"useCaseName\":\"sleep\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.SleepDelay\",\"name\":\"simulateSlowCode_sleepMilliseconds_10\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"sleep ms 1000\"}]},\"useCaseName\":\"sleep\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.SleepDelay\",\"name\":\"simulateSlowCode_sleepMilliseconds_1000\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"sync sleep ms 1000\"}]},\"useCaseName\":\"sleep\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.SleepDelay\",\"name\":\"simulateSynchronizedSlowCode_sleepMilliseconds_1000\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"sync sleep ms 1\"}]},\"useCaseName\":\"sleep\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.SleepDelay\",\"name\":\"simulateSynchronizedSlowCode_sleepMilliseconds_1\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"sync sleep ms 100\"}]},\"useCaseName\":\"sleep\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.SleepDelay\",\"name\":\"simulateSynchronizedSlowCode_sleepMilliseconds_100\"},\"selected\":false}],\"name\":\"sleep\"},{\"processingUnits\":[{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"Reuse same Transformers from pool\"}]},\"useCaseName\":\"xsltTransform\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.xslt.XsltProcessor\",\"name\":\"pooledTransformerXslt\"},\"selected\":false},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"Reinstantiate Transformer every time\"}]},\"useCaseName\":\"xsltTransform\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.xslt.XsltProcessor\",\"name\":\"unPooledTransformerXslt\"},\"selected\":false}],\"name\":\"xsltTransform\"}]}\n"; 
	}
    
    
}