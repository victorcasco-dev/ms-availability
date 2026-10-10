package com.reservas.availability;

import com.reservas.availability.config.PaginationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.liquibase.autoconfigure.LiquibaseAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
@EnableDiscoveryClient
@EntityScan(basePackages = "com.reservas.commons.entity")
@EnableConfigurationProperties(PaginationProperties.class)
public class MsAvailabilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAvailabilityApplication.class, args);
	}
}
