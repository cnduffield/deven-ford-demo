package com.ford.deven.openshiftdemo.acceptance.hello;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ford.cloudnative.base.test.acceptance.AcceptanceTestUtil;
import com.ford.deven.openshiftdemo.hello.api.HelloRequest;
import com.ford.deven.openshiftdemo.hello.api.HelloResponse;

public class HelloAcceptanceTest {

	RestTemplate restTemplate;

	@Before
	public void setup() {
		restTemplate = AcceptanceTestUtil.restTemplateBuilder().disableErrorHandler().build();
	}

	@Test
	public void testGetHelloEndpoint() throws Exception {
		ResponseEntity<HelloResponse> response = restTemplate.getForEntity("/api/v1/hello", HelloResponse.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getResult().getGreeting()).isEqualTo("Hello");
	}

	@Test
	public void testPostHelloEndpoint() throws Exception {
		HelloRequest helloRequest = HelloRequest.builder().name("NAME").build();
		ResponseEntity<HelloResponse> response = restTemplate.postForEntity("/api/v1/hello", helloRequest, HelloResponse.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getResult().getGreeting()).isEqualTo("Hello NAME");
	}

}
