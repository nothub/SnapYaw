package lol.hub.snapyaw.fabric;

import lol.hub.snapyaw.SnapYawCore;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class Main implements ClientModInitializer {

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("snapyaw", "keys"));
    private static final SnapYawCore CORE = new SnapYawCore(CATEGORY);

    @Override
    public void onInitializeClient() {
        KeyMappingHelper.registerKeyMapping(CORE.toggleKey);
        KeyMappingHelper.registerKeyMapping(CORE.pressKey);

        ClientTickEvents.END_CLIENT_TICK.register(CORE::onTick);
    }

}
