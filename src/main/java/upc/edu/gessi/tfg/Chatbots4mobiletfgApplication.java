package upc.edu.gessi.tfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;

@SpringBootApplication
public class Chatbots4mobiletfgApplication {

	public static void main(String[] args) {
		SpringApplication.run(Chatbots4mobiletfgApplication.class, args);
	}

	@Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info().title("Extended Knowledge Base API"));
    }

}
