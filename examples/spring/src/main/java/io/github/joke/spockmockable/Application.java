package io.github.joke.spockmockable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class Application {

    public static void main(String... args) {
        run(Application.class, args);
    }

}
