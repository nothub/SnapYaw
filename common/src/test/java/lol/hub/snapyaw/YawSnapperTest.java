package lol.hub.snapyaw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YawSnapperTest {

    private static final float TICK_SECONDS = 1f / 20f;

    @Test
    void staysUnchangedOutsideTriggerRange() {
        assertEquals(84.9f, YawSnapper.apply(84.9f, TICK_SECONDS), 1e-4f);
    }

    @Test
    void staysUnchangedExactlyAtCardinal() {
        assertEquals(90f, YawSnapper.apply(90f, TICK_SECONDS), 1e-4f);
    }

    @Test
    void movesTowardNearestCardinalFromBelow() {
        float result = YawSnapper.apply(85f, TICK_SECONDS);

        assertEquals(85f + (5f / 3f) * TICK_SECONDS, result, 1e-4f);
    }

    @Test
    void movesTowardNearestCardinalFromAbove() {
        float result = YawSnapper.apply(95f, TICK_SECONDS);

        assertEquals(95f - (5f / 3f) * TICK_SECONDS, result, 1e-4f);
    }

    @Test
    void handlesWraparoundAcrossZero() {
        // -2 degrees is 2 degrees short of the 0/360 cardinal.
        float result = YawSnapper.apply(-2f, TICK_SECONDS);

        assertEquals(-2f + (5f / 3f) * TICK_SECONDS, result, 1e-4f);
    }

    @Test
    void handlesWraparoundAboveThreeSixty() {
        // 362 degrees is 2 degrees past the 360/0 cardinal.
        float result = YawSnapper.apply(362f, TICK_SECONDS);

        assertEquals(362f - (5f / 3f) * TICK_SECONDS, result, 1e-4f);
    }

    @Test
    void snapsExactlyToTargetInsteadOfOvershootingWhenStepWouldPassIt() {
        // Only 0.01 degrees left to travel -- a full tick's worth of
        // movement would overshoot past the cardinal without clamping.
        float result = YawSnapper.apply(89.99f, TICK_SECONDS);

        assertEquals(90f, result, 1e-4f);
    }

    // Pins down the 3-second worst-case contract: entering the trigger
    // range at exactly its edge (5 degrees off) reaches the cardinal in
    // exactly 3 seconds of accumulated ticks, not longer.
    @Test
    void reachesTargetInExactlyThreeSecondsFromTheEdgeOfTheRange() {
        float yaw = 85f;
        for (int tick = 0; tick < 60; tick++) {
            yaw = YawSnapper.apply(yaw, TICK_SECONDS);
        }

        assertEquals(90f, yaw, 1e-3f);
    }

}
