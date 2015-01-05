package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.Console;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.servlet.MultipartConfigElement;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	@Bean
	MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("12MB");
		factory.setMaxRequestSize("12MB");
		return factory.createMultipartConfig();
	}

	public static void main(String[] args) {
		ApplicationContext ac = new FileSystemXmlApplicationContext(
				"src/main/resources/static/hsql_config.xml");

		DataSource dataSource = (DataSource) ac.getBean("dataSource");
		

		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate
				.update("CREATE TABLE customer (first_name varchar(100),id int GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) NOT NULL)");
		jdbcTemplate.update("INSERT INTO customer(first_name) VALUES('tobi')");
		SqlRowSet resultSet = jdbcTemplate.queryForRowSet("SELECT * FROM customer");
		System.out.print(resultSet.toString());
		// (new String[] {
		// "INSERT INTO customer(id,first_name) VALUES(3, 'tobi')",
		// "update customer set first_name = 'FN#'",
		// "delete from customer where id > 2" });

		SpringApplication.run(Application.class, args);
	}
}