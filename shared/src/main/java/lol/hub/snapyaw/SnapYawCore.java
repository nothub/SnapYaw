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

        // Read the level's actual tick rate instead of assuming 20 -- the
        // server can run at a different rate (e.g. via /tickrate), synced to
        // the client via ClientboundTickingStatePacket. A single client tick
        // event still represents exactly one such tick's worth of simulated
        // time, however long it took in wall-clock time to arrive, so this
        // is not a substitute for measuring real elapsed time -- it's the
        // correct per-tick duration for a fixed-timestep tick callback.
        float deltaSeconds = mc.level.tickRateManager().millisecondsPerTick() / 1000f;
        mc.player.setYRot(YawSnapper.apply(mc.player.getYRot(), deltaSeconds));
    }

}
