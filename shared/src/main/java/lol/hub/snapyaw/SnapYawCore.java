package lol.hub.snapyaw;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class SnapYawCore {

    public final KeyMapping toggleKey;
    public final KeyMapping pressKey;

    private boolean enabled = true;
    private boolean pressSnapping = false;

    public SnapYawCore(KeyMapping.Category category) {
        toggleKey = new KeyMapping("key.snapyaw.toggle", GLFW.GLFW_KEY_KP_1, category);
        pressKey = new KeyMapping("key.snapyaw.press", GLFW.GLFW_KEY_KP_2, category);
    }

    public void onTick(Minecraft mc) {
        if (mc.level == null || mc.player == null) return;

        if (toggleKey.consumeClick()) enabled = !enabled;
        if (pressKey.consumeClick()) pressSnapping = true;
        if (!enabled && !pressSnapping) return;

        // The server can run at a tick rate other than 20 (e.g. via /tickrate), synced to the client via ClientboundTickingStatePacket.
        // A client tick event represents one tick of simulated time, not one unit of wall-clock time -- this is the correct
        // per-tick duration for that tick, not a substitute for measuring real elapsed time.
        float deltaSeconds = mc.level.tickRateManager().millisecondsPerTick() / 1000f;

        if (enabled) mc.player.setYRot(YawSnapper.apply(mc.player.getYRot(), deltaSeconds));

        if (pressSnapping) {
            float yaw = YawSnapper.applyPressSnap(mc.player.getYRot(), deltaSeconds);
            mc.player.setYRot(yaw);
            if (YawSnapper.isAtCardinal(yaw)) pressSnapping = false;
        }
    }

}
