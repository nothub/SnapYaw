package lol.hub.snapyaw;

public final class YawSnapper {

    private static final float TRIGGER_RANGE_DEGREES = 5f;
    private static final float SNAP_DURATION_SECONDS = 3f;
    private static final float MAX_DEGREES_PER_SECOND = TRIGGER_RANGE_DEGREES / SNAP_DURATION_SECONDS;

    // A cardinal is at most 45 degrees away from any yaw, so that's the worst case for a press-triggered snap.
    private static final float PRESS_MAX_DISTANCE_DEGREES = 45f;
    private static final float PRESS_SNAP_DURATION_SECONDS = 1.2f;
    private static final float PRESS_MAX_DEGREES_PER_SECOND = PRESS_MAX_DISTANCE_DEGREES / PRESS_SNAP_DURATION_SECONDS;

    private YawSnapper() {
    }

    // Call once per tick with the elapsed time since the last call.
    // Pulls yaw toward the nearest cardinal at a constant angular speed once within TRIGGER_RANGE_DEGREES.
    // Worst case is entering at the edge of the range: reaches the target in exactly SNAP_DURATION_SECONDS. Closer entries take less time.
    public static float apply(float yaw, float deltaSeconds) {
        float distance = distanceToNearestCardinal(yaw);
        if (Math.abs(distance) > TRIGGER_RANGE_DEGREES) return yaw;

        return step(yaw, distance, MAX_DEGREES_PER_SECOND, deltaSeconds);
    }

    // Call once per tick, starting the tick the press-to-use key is pressed, regardless of distance to the
    // nearest cardinal -- unlike apply(), there's no trigger range. Keep calling until isAtCardinal() is true;
    // the caller commits to finishing the snap once started, even if the key is released early.
    // Worst case (45 degrees away) reaches the target in exactly PRESS_SNAP_DURATION_SECONDS.
    public static float applyPressSnap(float yaw, float deltaSeconds) {
        float distance = distanceToNearestCardinal(yaw);
        return step(yaw, distance, PRESS_MAX_DEGREES_PER_SECOND, deltaSeconds);
    }

    public static boolean isAtCardinal(float yaw) {
        return distanceToNearestCardinal(yaw) == 0f;
    }

    private static float step(float yaw, float distance, float maxDegreesPerSecond, float deltaSeconds) {
        float maxStep = maxDegreesPerSecond * deltaSeconds;
        float step = Math.max(-maxStep, Math.min(maxStep, distance));
        return yaw + step;
    }

    // Signed shortest angle from yaw to the nearest multiple of 90 degrees, magnitude at most 45.
    // Positive means the nearest cardinal is ahead of yaw: adding the result to yaw reaches it.
    static float distanceToNearestCardinal(float yaw) {
        float remainder = yaw % 90f;
        if (remainder < 0) remainder += 90f;
        return remainder <= 45f ? -remainder : 90f - remainder;
    }

}
