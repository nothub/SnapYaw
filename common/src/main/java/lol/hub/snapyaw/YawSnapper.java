package lol.hub.snapyaw;

public final class YawSnapper {

    private static final float TRIGGER_RANGE_DEGREES = 5f;
    private static final float SNAP_DURATION_SECONDS = 3f;
    private static final float MAX_DEGREES_PER_SECOND = TRIGGER_RANGE_DEGREES / SNAP_DURATION_SECONDS;

    private YawSnapper() {
    }

    // Called once per tick with the elapsed time since the last call. Pulls
    // yaw toward the nearest cardinal direction at a constant angular speed
    // once within TRIGGER_RANGE_DEGREES of it, so the worst case (entering
    // the range at exactly the edge) takes exactly SNAP_DURATION_SECONDS to
    // reach the target, and closer entries take proportionally less.
    public static float apply(float yaw, float deltaSeconds) {
        float distance = distanceToNearestCardinal(yaw);
        if (Math.abs(distance) > TRIGGER_RANGE_DEGREES) return yaw;

        float maxStep = MAX_DEGREES_PER_SECOND * deltaSeconds;
        float step = Math.max(-maxStep, Math.min(maxStep, distance));
        return yaw + step;
    }

    // Signed shortest angle from yaw to the nearest multiple of 90 degrees,
    // magnitude at most 45 degrees. Positive means the nearest cardinal is
    // ahead of yaw (adding the result to yaw reaches it).
    static float distanceToNearestCardinal(float yaw) {
        float remainder = yaw % 90f;
        if (remainder < 0) remainder += 90f;
        return remainder <= 45f ? -remainder : 90f - remainder;
    }

}
