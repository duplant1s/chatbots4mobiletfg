package upc.edu.gessi.tfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upc.edu.gessi.tfg.models.App;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@SpringBootApplication
public class Chatbots4mobiletfgApplication {

	public static void main(String[] args) {
		SpringApplication.run(Chatbots4mobiletfgApplication.class, args);
	}

}
