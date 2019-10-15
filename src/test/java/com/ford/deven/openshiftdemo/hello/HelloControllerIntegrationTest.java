package com.ford.deven.openshiftdemo.hello;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ford.cloudnative.base.app.web.exception.handler.ExceptionHandlerConfiguration;
import com.ford.cloudnative.base.test.response.BaseBodyErrorResultMatchers;
import com.ford.deven.openshiftdemo.hello.api.HelloRequest;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { HelloController.class }, secure = false)
@Import(ExceptionHandlerConfiguration.class)
public class HelloControllerIntegrationTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RequestMappingHandlerAdapter adapter;

	/***********************************************************************************************
	 * ENDPOINT: GET /api/v1/hello
	 ***********************************************************************************************/

	@Test
	public void should_return200ReponseWithFieldsPopulated_forHelloGreetingsEndpoint() throws Exception {
		jsonGet("/api/v1/hello")
			.andExpect(status().isOk())
			// controller unit tests test completeness of response object; we only smoke test here that a JSON response is returned
			.andExpect(jsonPath("$.result.greeting").value("Hello"));
	}

	/***********************************************************************************************
	 * ENDPOINT: POST /api/v1/hello
	 ***********************************************************************************************/

	@Test
	public void should_return200ReponseWithFieldsPopulated_forHelloNameGreetingsEndpoint() throws Exception {
		HelloRequest helloRequest = HelloRequest.builder().name("NAME").build();

		jsonPost("/api/v1/hello", helloRequest)
			.andExpect(status().isOk())
			// controller unit tests test completeness of response object; we only smoke test here that a JSON response is returned
			.andExpect(jsonPath("$.result.greeting").value("Hello NAME"));
	}

	@Test
	public void should_validateNameField_forHelloNameGreetingsEndpoint() throws Exception {
		HelloRequest helloRequest = HelloRequest.builder().build();

		jsonPost("/api/v1/hello", helloRequest)
			.andExpect(status().isBadRequest())
			.andExpect(BaseBodyErrorResultMatchers.dataErrorWithCode("name", "NotNull").exists())
			;
	}

	//////////////////// Helper Methods

	ResultActions jsonGet(String url) throws Exception {
		return this.mockMvc.perform(MockMvcRequestBuilders.get(url))
				.andDo(MockMvcResultHandlers.print());
	}

	ResultActions jsonPost(String url, Object entity) throws Exception {
		return
			this.mockMvc.perform(MockMvcRequestBuilders
				.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(entity))
			)
			.andDo(MockMvcResultHandlers.print());
	}
}
