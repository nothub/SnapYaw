package lol.hub.snapyaw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YawSnapperTest {

    private static final float TICK_SECONDS = 1f / 20f;

    @Test
    void pressSnapMovesTowardNearestCardinalRegardlessOfDistance() {
        YawSnapper.PressSnap snap = YawSnapper.startPressSnap(84f);

        float result = snap.advance(TICK_SECONDS);

        assertEquals(true, result > 84f);
        assertEquals(true, result < 90f);
    }

    @Test
    void pressSnapSnapsExactlyToTargetInsteadOfOvershooting() {
        // Below the 5-degree floor, so it still takes the minimum duration (5 ticks), never less.
        YawSnapper.PressSnap snap = YawSnapper.startPressSnap(89.99f);
        float result = 89.99f;
        for (int tick = 0; tick < 5; tick++) {
            result = snap.advance(TICK_SECONDS);
        }

        assertEquals(90f, result, 1e-4f);
        assertEquals(true, snap.isFinished());
    }

    @Test
    void pressSnapReachesTargetInExactlyThreeQuartersOfASecondFromTheWorstCaseDistance() {
        YawSnapper.PressSnap snap = YawSnapper.startPressSnap(45f);
        float yaw = 45f;
        for (int tick = 0; tick < 14; tick++) {
            yaw = snap.advance(TICK_SECONDS);
            assertEquals(false, snap.isFinished());
        }
        yaw = snap.advance(TICK_SECONDS);

        assertEquals(0f, yaw, 1e-3f);
        assertEquals(true, snap.isFinished());
    }

    @Test
    void pressSnapReachesTargetInExactlyTheMinimumDurationFromFiveDegrees() {
        YawSnapper.PressSnap snap = YawSnapper.startPressSnap(85f);
        float yaw = 85f;
        for (int tick = 0; tick < 4; tick++) {
            yaw = snap.advance(TICK_SECONDS);
            assertEquals(false, snap.isFinished());
        }
        yaw = snap.advance(TICK_SECONDS);

        assertEquals(90f, yaw, 1e-3f);
        assertEquals(true, snap.isFinished());
    }

    @Test
    void pressSnapCoversMoreThanHalfTheDistanceByHalfTheDurationBecauseItEasesOut() {
        // 65 is 25 degrees off 90 -- halfway between the 5-degree floor and 45-degree worst case, giving a
        // duration of 0.625s (12.5 ticks). Tick 8 lands past half of that.
        YawSnapper.PressSnap snap = YawSnapper.startPressSnap(65f);
        float yaw = 65f;
        for (int tick = 0; tick < 8; tick++) {
            yaw = snap.advance(TICK_SECONDS);
        }

        assertEquals(true, yaw - 65f > 12.5f);
    }

    @Test
    void isAtCardinalTrueOnlyExactlyOnCardinal() {
        assertEquals(true, YawSnapper.isAtCardinal(90f));
        assertEquals(false, YawSnapper.isAtCardinal(89.99f));
    }

}
