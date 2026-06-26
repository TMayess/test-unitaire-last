package com.example.exo14.bdd;

import com.example.exo14.Exo14Application;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = Exo14Application.class)
public class CucumberSpringConfig {
}
