package lol.hub.snapyaw;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class SnapYawCore {

    public final KeyMapping key;

    private YawSnapper.PressSnap pressSnap;

    public SnapYawCore(KeyMapping.Category category) {
        key = new KeyMapping("key.snapyaw.key", GLFW.GLFW_KEY_KP_2, category);
    }

    public void onTick(Minecraft mc) {
        if (mc.level == null || mc.player == null) return;

        if (key.consumeClick()) pressSnap = YawSnapper.startPressSnap(mc.player.getYRot());
        if (pressSnap == null) return;

        float deltaSec = mc.level.tickRateManager().millisecondsPerTick() / 1000f;
        mc.player.setYRot(pressSnap.advance(deltaSec));
        if (pressSnap.isFinished()) pressSnap = null;
    }

}
