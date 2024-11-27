package G2.SafeSpace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the SafeSpace application.
 * <p>
 * This class contains the main method which serves as the entry point to start the Spring Boot application.
 * The {@link SpringBootApplication} annotation marks this class as the configuration class for the Spring Boot application,
 * and it enables component scanning, auto-configuration, and property support.
 * </p>
 *
 * <p>
 * The main method calls {@link SpringApplication#run(Class, String...)} to launch the application.
 * </p>
 */
@SpringBootApplication
public class SafeSpaceApplication {

	/**
	 * The main method that starts the SafeSpace Spring Boot application.
	 * <p>
	 * This method initializes the Spring context and starts the application.
	 * </p>
	 *
	 * @param args Command-line arguments passed to the application at startup.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SafeSpaceApplication.class, args);
	}

}
