package com.tdu.autoconfiguration;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MyBatisAutoConfiguration {

	@Autowired
	private MybatisProperties properties;
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	private static final Logger log = LoggerFactory.getLogger(MyBatisAutoConfiguration.class);

	@Autowired
	DataSource dataSource;

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		log.info("create mybatis sessionfactory");
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		if (StringUtils.hasText(this.properties.getConfig())) {
			factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfig()));
		} else {
			factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
			factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());
			factory.setMapperLocations(this.properties.getMapperLocations());
		}
		return factory.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.SIMPLE);
	}

	/**
	 * 默认扫描当前默认包
	 * @author tangdu
	 *
	 */
	public static class AutoConfiguredMapperScannerRegistrar
			implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {
		private BeanFactory beanFactory;
		private ResourceLoader resourceLoader;

		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
				BeanDefinitionRegistry registry) {
			ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
			List<String> pkgs;
			try {
				pkgs = AutoConfigurationPackages.get(this.beanFactory);
				for (String pkg : pkgs) {
					log.debug("Found MyBatis auto-configuration package '" + pkg + "'");
				}
				if (this.resourceLoader != null) {
					scanner.setResourceLoader(this.resourceLoader);
				}
				scanner.registerFilters();
				scanner.doScan(pkgs.toArray(new String[pkgs.size()]));
			} catch (IllegalStateException ex) {
				log.debug("Could not determine auto-configuration " + "package, automatic mapper scanning disabled.");
			}
		}

		@Override
		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
			this.beanFactory = beanFactory;
		}

		@Override
		public void setResourceLoader(ResourceLoader resourceLoader) {
			this.resourceLoader = resourceLoader;
		}
	}

	@Configuration
	@Import({ AutoConfiguredMapperScannerRegistrar.class })
	@ConditionalOnMissingBean(MapperFactoryBean.class)
	public static class MapperScannerRegistrarNotFoundConfiguration {
		@PostConstruct
		public void afterPropertiesSet() {
			log.debug(String.format("No %s found.", MapperFactoryBean.class.getName()));
		}
	}

}
