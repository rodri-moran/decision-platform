package com.rodrigomoran.decisionplatform.rules_service;

import org.springframework.boot.SpringApplication;

public class TestRulesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(RulesServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
