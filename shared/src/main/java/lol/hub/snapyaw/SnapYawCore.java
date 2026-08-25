package lol.hub.snapyaw;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class SnapYawCore {

    // Both loaders' client tick events fire at the fixed 20 Hz game tick
    // rate, so the elapsed time per call is constant rather than measured.
    private static final float TICK_SECONDS = 1f / 20f;

    public final KeyMapping toggleKey;

    private boolean enabled = true;

    public SnapYawCore(KeyMapping.Category category) {
        toggleKey = new KeyMapping("key.snapyaw.toggle", GLFW.GLFW_KEY_KP_1, category);
    }

    public void onTick(Minecraft mc) {
        if (mc.level == null || mc.player == null) return;

        if (toggleKey.consumeClick()) enabled = !enabled;
        if (!enabled) return;

        mc.player.setYRot(YawSnapper.apply(mc.player.getYRot(), TICK_SECONDS));
    }

}
