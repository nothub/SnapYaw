package lol.hub.snapyaw;

public final class YawSnapper {

    private static final float PRESS_MAX_DISTANCE_DEGREES = 45f;
    private static final float PRESS_MIN_DISTANCE_DEGREES = 5f;
    private static final float PRESS_MIN_DURATION_SECONDS = 0.25f;
    private static final float PRESS_MAX_DURATION_SECONDS = 0.75f;

    private YawSnapper() {
    }

    public static PressSnap startPressSnap(float yaw) {
        return new PressSnap(yaw);
    }

    public static boolean isAtCardinal(float yaw) {
        return distanceToNearestCardinal(yaw) == 0f;
    }

    // Positive implies nearest cardinal is ahead of yaw.
    static float distanceToNearestCardinal(float yaw) {
        float remainder = yaw % 90f;
        if (remainder < 0) remainder += 90f;
        return remainder <= 45f ? -remainder : 90f - remainder;
    }

    private static float pressSnapDurationSeconds(float distance) {
        float clamped = Math.clamp(distance, PRESS_MIN_DISTANCE_DEGREES, PRESS_MAX_DISTANCE_DEGREES);
        float t = (clamped - PRESS_MIN_DISTANCE_DEGREES) / (PRESS_MAX_DISTANCE_DEGREES - PRESS_MIN_DISTANCE_DEGREES);
        float inv = 1f - t;
        float eased = 1f - inv * inv;
        return PRESS_MIN_DURATION_SECONDS + eased * (PRESS_MAX_DURATION_SECONDS - PRESS_MIN_DURATION_SECONDS);
    }

    public static final class PressSnap {

        private final float startYaw;
        private final float targetYaw;
        private final float durationSeconds;
        private float elapsedSeconds = 0f;

        private PressSnap(float startYaw) {
            this.startYaw = startYaw;
            float distance = distanceToNearestCardinal(startYaw);
            this.targetYaw = startYaw + distance;
            this.durationSeconds = pressSnapDurationSeconds(Math.abs(distance));
        }

        public float advance(float deltaSeconds) {
            elapsedSeconds += deltaSeconds;
            float t = Math.min(1f, elapsedSeconds / durationSeconds);
            float inv = 1f - t;
            float eased = 1f - inv * inv;
            return startYaw + (targetYaw - startYaw) * eased;
        }

        public boolean isFinished() {
            return elapsedSeconds >= durationSeconds;
        }

    }

}
