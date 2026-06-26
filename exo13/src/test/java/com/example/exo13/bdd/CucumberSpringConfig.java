package com.example.exo13.bdd;

import com.example.exo13.Exo13Application;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = Exo13Application.class)
public class CucumberSpringConfig {
}
