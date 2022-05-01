package io.github.joke.spockmockable.tests;

import org.springframework.stereotype.Service;

@Service
final public class SimpleService {

    final public String getName() {
        return SimpleService.class.getCanonicalName();
    }

}
