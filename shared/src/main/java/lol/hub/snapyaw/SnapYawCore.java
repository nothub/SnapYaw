package lol.hub.snapyaw;

import net.minecraft.client.Minecraft;

public final class SnapYawCore {

    // Both loaders' client tick events fire at the fixed 20 Hz game tick
    // rate, so the elapsed time per call is constant rather than measured.
    private static final float TICK_SECONDS = 1f / 20f;

    public void onTick(Minecraft mc) {
        if (mc.level == null || mc.player == null) return;

        mc.player.setYRot(YawSnapper.apply(mc.player.getYRot(), TICK_SECONDS));
    }

}
