package dev.gearhud.bridge.mixin;

import dev.gearhud.bridge.GearHudBridge;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "com.heartroadcafe.compactgearshift.block.GearshiftBlock", remap = false)
public abstract class GearshiftBlockMixin {
    @Inject(method = "useWithoutItem", at = @At("HEAD"))
    private void bridge$claim(BlockState state, Level level, BlockPos pos, Player player,
                              BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            GearHudBridge.claim(level.getBlockEntity(pos), sp);
        }
    }
}
