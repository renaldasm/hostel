package hostel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;

@SpringBootApplication
public class Application {

	@Autowired
	MessageSource messageSource;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
