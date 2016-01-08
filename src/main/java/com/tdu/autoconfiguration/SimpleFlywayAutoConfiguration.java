package com.tdu.autoconfiguration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

@Configuration
@AutoConfigureBefore({FlywayAutoConfiguration.class })
@EnableConfigurationProperties(FlywayProperties.class)
public class SimpleFlywayAutoConfiguration {
	private static final String FLY_TABLE = "T_FLYWAYSCHEMA_VERSION";

	@Autowired
	private FlywayProperties properties = new FlywayProperties();

	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	@Autowired(required = false)
	private DataSource dataSource;

	@Autowired(required = false)
	@FlywayDataSource
	private DataSource flywayDataSource;

	@Autowired(required = false)
	private FlywayMigrationStrategy migrationStrategy;

	@PostConstruct
	public void checkLocationExists() {
		if (this.properties.isCheckLocation()) {
			Assert.state(!this.properties.getLocations().isEmpty(), "Migration script locations not configured");
			boolean exists = hasAtLeastOneLocation();
			Assert.state(exists, "Cannot find migrations location in: " + this.properties.getLocations()
					+ " (please add migrations or check your Flyway configuration)");
		}
	}

	private boolean hasAtLeastOneLocation() {
		for (String location : this.properties.getLocations()) {
			if (this.resourceLoader.getResource(location).exists()) {
				return true;
			}
		}
		return false;
	}

	@Bean
	@ConfigurationProperties(prefix = "flyway")
	public Flyway flyway() {
		Flyway flyway = new Flyway();
		if (this.properties.isCreateDataSource()) {
			flyway.setDataSource(this.properties.getUrl(), this.properties.getUser(), this.properties.getPassword(),
					this.properties.getInitSqls().toArray(new String[0]));
		} else if (this.flywayDataSource != null) {
			flyway.setDataSource(this.flywayDataSource);
		} else {
			flyway.setDataSource(this.dataSource);
		}
		//flyway.setLocations( this.properties.getLocations().toArray(new String[]{}));
		flyway.setSqlMigrationPrefix("V_");
		flyway.setBaselineOnMigrate(true);
		flyway.setEncoding("UTF-8");
		flyway.setIgnoreFailedFutureMigration(true);
		flyway.setTable(FLY_TABLE);
		return flyway;
	}

	@Bean
	@ConditionalOnMissingBean
	public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
		return new FlywayMigrationInitializer(flyway, this.migrationStrategy);
	}

}
