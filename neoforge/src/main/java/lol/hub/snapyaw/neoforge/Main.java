package lol.hub.snapyaw.neoforge;

import lol.hub.snapyaw.SnapYawCore;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = "snapyaw", dist = {Dist.CLIENT})
public class Main {

    private static final SnapYawCore CORE = new SnapYawCore();

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.addListener(Main::onClientTick);
    }

    // PlayerTickEvent.Post fires once per Player entity tick, which on an
    // integrated server means once on the client/render thread (the local
    // player) AND once on the separate server thread (the server-side
    // player). Use the client-only tick event instead: fires exactly once
    // per client tick, same as Fabric's ClientTickEvents.END_CLIENT_TICK.
    private static void onClientTick(ClientTickEvent.Post event) {
        CORE.onTick(Minecraft.getInstance());
    }

}
