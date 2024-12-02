package com.avalanches.stepdefinitions;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = TesteConfig.class)  // Your configuration class
public class CucumberSpringConfiguration {
}