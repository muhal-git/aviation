package com.example.aviation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.example.aviation.config.DatabaseProperties;

@SpringBootApplication
@EnableConfigurationProperties({DatabaseProperties.class})
@EnableAspectJAutoProxy
public class AviationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AviationApplication.class, args);
	}

}
