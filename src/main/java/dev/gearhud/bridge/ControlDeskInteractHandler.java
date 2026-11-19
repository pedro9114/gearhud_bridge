package dev.gearhud.bridge;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = GearHudBridge.MODID)
public class ControlDeskInteractHandler {

    private static final ResourceLocation CONTROL_DESK_ID =
            ResourceLocation.fromNamespaceAndPath("aeroworks", "copycat_control_desk");

    private static final int SEARCH_RADIUS = 16;

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        BlockPos clickedPos = event.getPos();
        BlockState state = level.getBlockState(clickedPos);
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());

        if (!CONTROL_DESK_ID.equals(blockId)) return;

        BlockEntity nearestGearshift = findConnectedGearshift(level, clickedPos, SEARCH_RADIUS);
        if (nearestGearshift != null) {
            GearHudBridge.claim(nearestGearshift, player);
        }
    }

    private static BlockEntity findConnectedGearshift(Level level, BlockPos origin, int maxDistance) {
        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(origin);
        visited.add(origin);

        int maxDistSq = maxDistance * maxDistance;

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();

            BlockEntity be = level.getBlockEntity(current);
            if (be instanceof GearAccess) {
                return be;
            }

            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.relative(dir);
                if (!visited.contains(neighbor) && neighbor.distSqr(origin) <= maxDistSq) {
                    visited.add(neighbor);
                    if (!level.getBlockState(neighbor).isAir()) {
                        queue.add(neighbor);
                    }
                }
            }
        }
        return null;
    }
}
