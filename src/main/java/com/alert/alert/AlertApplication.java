package com.alert.alert;

import com.alert.alert.faker.FakerGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class AlertApplication implements CommandLineRunner {

    private final FakerGenerator fakerGenerator;

    public AlertApplication(FakerGenerator fakerGenerator) {
        this.fakerGenerator = fakerGenerator;
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(AlertApplication.class, args);
    }

    @Override
    public void run(String... args) {
        fakerGenerator.generateFake(50);
    }
}
