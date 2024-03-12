package blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

//INfo for API  documentation


@Configuration
public class SwaggerConfig {
	@Bean
	 OpenAPI openAPI() {
		
		return new OpenAPI().info(new Info().title("Blog Application Api")
				.description("Blog Api Developed By Rishav Raj").version("1.0").contact(new Contact().name("Rishav Raj")
						.url("https://www.instagram.com/rishu_no_baka?igsh=NXAwcDYzZ2FnZnc3")
						.email("rishav3330@gmail.com")).license(new License().name("Dark")));
				
	}

}
