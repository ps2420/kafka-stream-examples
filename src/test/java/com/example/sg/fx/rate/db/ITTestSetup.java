package com.example.sg.fx.rate.db;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

import com.example.sg.fx.rate.db.FXRateDBRunner;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = { "com.dbs.sg.fx.rate.db" }, excludeFilters = {
		@Filter(type = FilterType.ASSIGNABLE_TYPE, value = { FXRateDBRunner.class }) })
public class ITTestSetup {

}
