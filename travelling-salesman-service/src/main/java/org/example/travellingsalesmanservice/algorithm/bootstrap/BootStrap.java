package org.example.travellingsalesmanservice.algorithm.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BootStrap implements CommandLineRunner {
    @Override
    public void run(String... args) {
        log.info("java.version: " + System.getProperty("java.version"));
    }
}
