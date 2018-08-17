package aegirdynamics.com.joystickserver;

import aegirdynamics.com.joystickserver.NMEA.NMEAController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JoystickserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoystickserverApplication.class, args);
        new NMEAController();
    }
}
