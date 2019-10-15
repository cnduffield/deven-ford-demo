package com.ford.deven.openshiftdemo.acceptance;

import static com.ford.cloudnative.base.test.json.JsonUtil.jsonContent;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ford.cloudnative.base.test.acceptance.AcceptanceTestUtil;

public class WebSecurityAcceptanceTest {

	RestTemplate restTemplate;

	@Before
	public void setup() {
		restTemplate = AcceptanceTestUtil.restTemplateBuilder().disableErrorHandler().build();
	}

	@Test
	public void testActuatorInfoEndpointIsAccessible() throws Exception {
		ResponseEntity<String> response = restTemplate.getForEntity("/actuator/info", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		JsonContent<?> jsonContent = jsonContent(response.getBody());
		assertThat(jsonContent).hasJsonPathStringValue("$.app.name");
		assertThat(jsonContent).extractingJsonPathStringValue("$.app.name").isNotBlank();
	}

}
