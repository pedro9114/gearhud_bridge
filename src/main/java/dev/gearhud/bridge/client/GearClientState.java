package dev.gearhud.bridge.client;

import dev.gearhud.bridge.GearHudBridge;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = GearHudBridge.MODID, value = Dist.CLIENT)
public final class GearClientState {
    private static volatile boolean known = false;
    private static volatile int gear = 0;

    private GearClientState() {}

    public static void set(int g, int forward, int reverse) {
        gear = g;
        known = true;
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut e) {
        known = false;
    }

    /** Texto que reemplaza a la "D" del HUD. 0 = N, positivo = 1..10, negativo = R1..R10. */
    public static String label(String fallback) {
        Minecraft mc = Minecraft.getInstance();
        if (!known || mc.player == null || mc.player.getVehicle() == null) return fallback;
        int g = gear;
        if (g == 0) return "N";
        if (g > 0) return Integer.toString(g);
        return "R" + (-g);
    }
}
