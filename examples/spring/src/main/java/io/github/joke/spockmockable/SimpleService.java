package io.github.joke.spockmockable;

import org.springframework.stereotype.Service;

@Service
final public class SimpleService {

    final public String getName() {
        return SimpleService.class.getCanonicalName();
    }

}
