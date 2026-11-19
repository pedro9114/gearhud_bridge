package dev.gearhud.bridge;

import dev.gearhud.bridge.client.GearClientState;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GearHudPayload(int gear, int forward, int reverse) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<GearHudPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(GearHudBridge.MODID, "gear"));

    public static final StreamCodec<RegistryFriendlyByteBuf, GearHudPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, GearHudPayload::gear,
            ByteBufCodecs.VAR_INT, GearHudPayload::forward,
            ByteBufCodecs.VAR_INT, GearHudPayload::reverse,
            GearHudPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(GearHudPayload p, IPayloadContext ctx) {
        ctx.enqueueWork(() -> GearClientState.set(p.gear(), p.forward(), p.reverse()));
    }
}
