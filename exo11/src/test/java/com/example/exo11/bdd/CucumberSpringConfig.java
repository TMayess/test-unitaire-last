package com.example.exo11.bdd;

import com.example.exo11.SupportTicketsApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
@CucumberContextConfiguration
@SpringBootTest(classes = SupportTicketsApplication.class)
@AutoConfigureMockMvc
public class CucumberSpringConfig {
}
