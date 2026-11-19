package dev.gearhud.bridge;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(GearHudBridge.MODID)
public class GearHudBridge {
    public static final String MODID = "gearhud_bridge";

    /** Gearshift -> jugador que ve su marcha en el HUD. */
    private static final Map<BlockEntity, UUID> OWNERS = Collections.synchronizedMap(new WeakHashMap<>());

    public GearHudBridge(IEventBus modBus) {
        modBus.addListener(GearHudBridge::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1").optional();
        registrar.playToClient(GearHudPayload.TYPE, GearHudPayload.STREAM_CODEC, GearHudPayload::handle);
    }

    /** El jugador abrió la GUI de este Gearshift: pasa a ser quien ve la marcha. */
    public static void claim(BlockEntity be, ServerPlayer player) {
        if (!(be instanceof GearAccess)) return;
        synchronized (OWNERS) {
            Iterator<Map.Entry<BlockEntity, UUID>> it = OWNERS.entrySet().iterator();
            while (it.hasNext()) {
                if (player.getUUID().equals(it.next().getValue())) it.remove();
            }
            OWNERS.put(be, player.getUUID());
        }
        send(be, player);
    }

    /** Llamado desde el mixin cuando cambia la marcha o la configuración. */
    public static void changed(BlockEntity be) {
        if (!(be instanceof GearAccess) || be.getLevel() == null || be.getLevel().isClientSide) return;
        UUID id = OWNERS.get(be);
        if (id == null) return;
        MinecraftServer server = be.getLevel().getServer();
        if (server == null) return;
        ServerPlayer player = server.getPlayerList().getPlayer(id);
        if (player != null) send(be, player);
    }

    private static void send(BlockEntity be, ServerPlayer player) {
        GearAccess a = (GearAccess) be;
        PacketDistributor.sendToPlayer(player, new GearHudPayload(a.bridge$gear(), a.bridge$forward(), a.bridge$reverse()));
    }
}
