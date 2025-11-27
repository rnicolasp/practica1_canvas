    package com.paint.servlets;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    import javax.sql.DataSource;

    @SpringBootApplication
    @Configuration
    public class epaintApplication implements WebMvcConfigurer {

        @Value("$(spring.datasource.url")
        String datasource;

        public static void main(String[] args) {
            SpringApplication.run(epaintApplication.class, args);
        }

        @Autowired
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }
