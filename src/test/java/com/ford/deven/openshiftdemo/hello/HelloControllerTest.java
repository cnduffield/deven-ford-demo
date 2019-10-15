package com.ford.deven.openshiftdemo.hello;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.ford.deven.openshiftdemo.hello.api.HelloRequest;
import com.ford.deven.openshiftdemo.hello.api.HelloResponse;

@RunWith(MockitoJUnitRunner.class)
public class HelloControllerTest {

	HelloController helloController;

	@Before
	public void setup() {
		helloController = new HelloController();
	}

	@Test
	public void testHello() throws Exception {
		HelloResponse response = helloController.hello();

		assertThat(response.getError()).isNull();
		assertThat(response.getResult()).isNotNull();

		String greeting = response.getResult().getGreeting();
		assertThat(greeting).isEqualTo("Hello");
	}

	@Test
	public void testHelloName() throws Exception {
		HelloRequest helloRequest = HelloRequest.builder().name("NAME").build();
		HelloResponse response = helloController.helloName(helloRequest);

		assertThat(response.getError()).isNull();
		assertThat(response.getResult()).isNotNull();

		String greeting = response.getResult().getGreeting();
		assertThat(greeting).isEqualTo("Hello NAME");
	}

}
