package com.ford.deven.openshiftdemo.hello;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ford.deven.openshiftdemo.hello.api.HelloRequest;
import com.ford.deven.openshiftdemo.hello.api.HelloResponse;
import com.ford.deven.openshiftdemo.hello.api.HelloResponse.HelloResponseResult;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api/v1/hello")
public class HelloController {

	@ApiOperation(value = "Hello Message", notes = "Returns a hello greeting")
	@GetMapping
	public HelloResponse hello() {
		HelloResponseResult result = HelloResponseResult.builder().greeting("Hello").build();

		return HelloResponse.result(result, HelloResponse.class);
	}

	@ApiOperation(value = "Customized Hello Message", notes = "Returns hello greeting with your name")
	@PostMapping
	public HelloResponse helloName(@RequestBody @Valid HelloRequest helloRequest) {
		HelloResponseResult result = HelloResponseResult.builder().greeting("Hello " + helloRequest.getName()).build();

		return HelloResponse.result(result, HelloResponse.class);
	}

}
