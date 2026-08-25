package lol.hub.snapyaw.fabric;

import lol.hub.snapyaw.SnapYawCore;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Main implements ClientModInitializer {

    private static final SnapYawCore CORE = new SnapYawCore();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(CORE::onTick);
    }

}
