package com.rodrigomoran.decisionplatform.decision_service;

import org.springframework.boot.SpringApplication;

public class TestDecisionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(DecisionServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
