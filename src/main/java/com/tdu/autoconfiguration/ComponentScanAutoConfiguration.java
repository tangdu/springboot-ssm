package com.tdu.autoconfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
	basePackages={
		"com.tdu.service",
		"com.tdu.web",
		"com.tdu.task"
	}
)
public class ComponentScanAutoConfiguration {

}
