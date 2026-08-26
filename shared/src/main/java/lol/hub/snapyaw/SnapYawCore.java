package lol.hub.snapyaw;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class SnapYawCore {

    public final KeyMapping toggleKey;

    private boolean enabled = true;

    public SnapYawCore(KeyMapping.Category category) {
        toggleKey = new KeyMapping("key.snapyaw.toggle", GLFW.GLFW_KEY_KP_1, category);
    }

    public void onTick(Minecraft mc) {
        if (mc.level == null || mc.player == null) return;

        if (toggleKey.consumeClick()) enabled = !enabled;
        if (!enabled) return;

        // The server can run at a tick rate other than 20 (e.g. via /tickrate), synced to the client via ClientboundTickingStatePacket.
        // A client tick event represents one tick of simulated time, not one unit of wall-clock time -- this is the correct
        // per-tick duration for that tick, not a substitute for measuring real elapsed time.
        float deltaSeconds = mc.level.tickRateManager().millisecondsPerTick() / 1000f;
        mc.player.setYRot(YawSnapper.apply(mc.player.getYRot(), deltaSeconds));
    }

}
