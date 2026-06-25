package com.example.exo12.bdd;

import com.example.exo12.RoomsReservationsApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = RoomsReservationsApplication.class)
@AutoConfigureMockMvc
public class CucumberSpringConfig {
}
