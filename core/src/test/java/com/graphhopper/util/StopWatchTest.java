package com.graphhopper.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class StopWatchTest {

    @Test
    void testToStringWithName() {
        StopWatch sw = new StopWatch("customName");
        String result = sw.toString();
        assertTrue(result.contains("customName"));
        assertTrue(result.contains("time:"));
    }

    @Test
    void testGetTimeStringZero() {
        StopWatch sw = new StopWatch();
        assertEquals("0ns", sw.getTimeString());
    }

    @Test
    void testGetMillisDoubleAfterSleep() throws InterruptedException {
        StopWatch sw = StopWatch.started(); // démarre directement
        Thread.sleep(10); // attend 10 ms
        sw.stop();

        double millis = sw.getMillisDouble();

        // On vérifie que le temps écoulé est au moins 10 ms (avec une petite marge)
        assertTrue(millis >= 9.0, "Expected at least ~10 ms, but got " + millis);
    }

}
