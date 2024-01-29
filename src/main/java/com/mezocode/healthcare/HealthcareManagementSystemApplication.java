package com.mezocode.healthcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class HealthcareManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthcareManagementSystemApplication.class, args);
    }

}
