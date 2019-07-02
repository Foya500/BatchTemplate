package com.foya.ebill.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import javax.sql.DataSource;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Base64;


@Configuration
public class DataSourceConfiguration implements TransactionManagementConfigurer {

    @Value("${spring.datasource.oracle.password_encode}")
    private String password_encode;
    @Bean
    @ConfigurationProperties("spring.datasource.oracle")
    DataSource dataSource() throws SQLException, UnsupportedEncodingException {
        return DataSourceBuilder.create()
        		.password(new String(Base64.getDecoder().decode(this.password_encode), "UTF-8")).build();
    }
	@Bean
	public JdbcTemplate jdbcTemplate() throws SQLException, UnsupportedEncodingException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
		return jdbcTemplate;
	}

	@Bean
	public PlatformTransactionManager txManager() throws SQLException, UnsupportedEncodingException {
		return new DataSourceTransactionManager(dataSource());
	}

	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
			try {
				return txManager();
			} catch (SQLException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return null;
	}
}
