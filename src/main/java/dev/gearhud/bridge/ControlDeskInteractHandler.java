package dev.gearhud.bridge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = GearHudBridge.MODID)
public class ControlDeskInteractHandler {

    private static final ResourceLocation CONTROL_DESK_ID =
            ResourceLocation.fromNamespaceAndPath("aeroworks", "copycat_control_desk");

    private static final int SEARCH_RADIUS = 16;

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        BlockPos clickedPos = event.getPos();
        BlockState state = level.getBlockState(clickedPos);
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());

        if (!CONTROL_DESK_ID.equals(blockId)) return;

        BlockEntity nearestGearshift = findNearestGearshiftBox(level, clickedPos, SEARCH_RADIUS);
        if (nearestGearshift != null) {
            GearHudBridge.claim(nearestGearshift, player);
        }
    }

    @SubscribeEvent
    public static void onMountEntity(EntityMountEvent event) {
        if (!event.isMounting()) return;
        if (!(event.getEntityMounting() instanceof ServerPlayer player)) return;

        Level level = player.level();
        if (level.isClientSide()) return;

        BlockPos seatPos = event.getEntityBeingMounted().blockPosition();
        BlockEntity nearestGearshift = findNearestGearshiftBox(level, seatPos, SEARCH_RADIUS);

        if (nearestGearshift != null) {
            GearHudBridge.claim(nearestGearshift, player);
        }
    }

    private static BlockEntity findNearestGearshiftBox(Level level, BlockPos origin, int radius) {
        BlockEntity closest = null;
        double closestDistSq = Double.MAX_VALUE;

        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-radius, -radius, -radius),
                origin.offset(radius, radius, radius))) {

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof GearAccess) {
                double distSq = pos.distSqr(origin);
                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    closest = be;
                }
            }
        }
        return closest;
    }
}
