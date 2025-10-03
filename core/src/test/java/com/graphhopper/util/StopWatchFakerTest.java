package com.graphhopper.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import com.github.javafaker.Faker;

class StopWatchFakerTest {

    @Test
    void testRandomSleepWithFaker() throws InterruptedException {
        Faker faker = new Faker();
        int sleepTime = faker.number().numberBetween(5, 20); // génère un temps aléatoire entre 5 et 20 ms

        StopWatch sw = StopWatch.started();
        Thread.sleep(sleepTime); // pause aléatoire
        sw.stop();

        double elapsed = sw.getMillisDouble();

        // Vérifie que le temps mesuré est cohérent
        assertTrue(elapsed >= sleepTime - 2, 
            "Expected at least " + (sleepTime - 2) + "ms, but got " + elapsed);
    }
}
