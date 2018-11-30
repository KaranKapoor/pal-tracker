package io.pivotal.pal.tracker.controllers;

import io.pivotal.pal.tracker.controllers.WelcomeController;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class WelcomeControllerTest {

    @Test
    public void itSaysHello() throws Exception {
        WelcomeController controller = new WelcomeController("A welcome message");

        assertThat(controller.sayHello()).isEqualTo("A welcome message");
    }
}
