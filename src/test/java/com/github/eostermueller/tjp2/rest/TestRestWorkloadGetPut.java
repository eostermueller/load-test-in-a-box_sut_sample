package com.github.eostermueller.tjp2.rest;


import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * com.github.eostermueller.tjp2.rest.WorkloadController
 * @author erikostermueller
 * TRY:
 * https://www.concretepage.com/spring-5/webappconfiguration-example-spring-test
 * https://stackoverflow.com/questions/49096577/how-to-test-spring-5-controllers-with-junit5
 *
 */
public class TestRestWorkloadGetPut extends TjpJUnitRestTest {
	//@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

//	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		assertNotNull(this.mockMvc);
	}

//	@Test
	public void testPutWorkload() throws Exception {
		  MockHttpServletRequestBuilder builder =
	              MockMvcRequestBuilders.put("/workload/")
	                                    .contentType(MediaType.APPLICATION_JSON_VALUE)
	                                    .accept(MediaType.APPLICATION_JSON)
	                                    .characterEncoding("UTF-8")
	                                    .content( putRq() );

		  this.mockMvc.perform(builder)
          .andExpect(MockMvcResultMatchers.status().isOk())
//          .andExpect(MockMvcResultMatchers.content()
//                                          .string("Article created."))
          .andDo(MockMvcResultHandlers.print());		  
	}
		
		/*
		 * 
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.name").value("emp1"))
				.andExpect(jsonPath("$.designation").value("manager"))
				.andExpect(jsonPath("$.empId").value("1"))
				.andExpect(jsonPath("$.salary").value(3000));		 * 
		 */

	private String putRq() {
		return "{\"processingUnits\":[{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"Reuse same Transformers from pool\"}]},\"useCaseName\":\"xsltTransform\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.xslt.XsltProcessor\",\"name\":\"pooledTransformerXslt\"},\"selected\":true},{\"descriptor\":{\"messages\":[{\"locale\":\"en_US\",\"message\":\"Reinstantiate Transformer every time\"}]},\"useCaseName\":\"xsltTransform\",\"method\":{\"parameters\":[],\"declaringClassName\":\"com.github.eostermueller.tjp2.xslt.XsltProcessor\",\"name\":\"unPooledTransformerXslt\"},\"selected\":false}],\"name\":\"xsltTransform\"}";
	}

}

