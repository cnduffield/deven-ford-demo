package com.ford.deven.openshiftdemo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ford.cloudnative.base.app.web.exception.handler.ExceptionHandlerConfiguration;
import com.ford.deven.openshiftdemo.hello.api.HelloRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(ExceptionHandlerConfiguration.class)
public class WebSecurityConfigurationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	/***********************************************************************************************
	 * ENDPOINTS: Actuator
	 ***********************************************************************************************/

	@Test
	public void should_allowActuatorInfoEndpoint_withoutAuthentication() {
		assertAccess(get("/actuator/info"));
	}

	@Test
	public void should_allowActuatorHealthEndpoint_withoutAuthentication() {
		assertAccess(get("/actuator/health"));
	}

	@Test
	public void should_notAllowSensitiveActuatorEndpoints_withoutAuthentication() {
		assertNoAccess(get("/actuator/env"));
		assertNoAccess(get("/actuator/dump"));
		assertNoAccess(get("/actuator/trace"));
		assertNoAccess(get("/actuator/metrics"));
		assertNoAccess(get("/actuator/configprops"));
	}

	/***********************************************************************************************
	 * ENDPOINTS: Swagger
	 ***********************************************************************************************/

	@Test
	public void should_allowSwaggerEndpoints_withoutAuthentication() {
		assertAccess(get("/swagger-ui.html"));
		assertAccess(get("/v2/api-docs"));
	}

	/***********************************************************************************************
	 * ENDPOINTS: Other
	 ***********************************************************************************************/

	@Test
	public void should_notAllowOtherEndpoints_withoutAuthentication() {
		assertNoAccess(get("/other-does-not-exist"));
	}

	/***********************************************************************************************
	 * ENDPOINTS: hello feature
	 ***********************************************************************************************/

	@Test
	public void should_allowGetHelloApi_withoutAuthentication() {
		assertAccess(get("/api/v1/hello"));
	}

	@Test
	public void should_allowPostHelloApi_withoutAuthentication() {
		assertAccess(post("/api/v1/hello", HelloRequest.builder().build()));
	}

	//////////////////// Helper Methods

	private ResponseEntity<?> get(String urlPath) {
		return restTemplate.getForEntity(urlPath, String.class);
	}

	private ResponseEntity<?> post(String urlPath, Object post) {
		return restTemplate.postForEntity(urlPath, post, post.getClass());
	}

	private void assertAccess(ResponseEntity<?> entity) {
		assertThat(entity.getStatusCode()).isNotIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN, HttpStatus.NOT_FOUND);
	}

	private void assertNoAccess(ResponseEntity<?> entity) {
		assertThat(entity.getStatusCode()).isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN, HttpStatus.NOT_FOUND);
	}
}
