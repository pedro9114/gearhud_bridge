package dev.gearhud.bridge.mixin;

import dev.gearhud.bridge.GearAccess;
import dev.gearhud.bridge.GearHudBridge;
import java.util.List;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.heartroadcafe.compactgearshift.block.GearshiftBlockEntity", remap = false)
public abstract class GearshiftBlockEntityMixin implements GearAccess {
    @Shadow private int currentGear;
    @Shadow @Final private List<Float> forwardRatios;
    @Shadow @Final private List<Float> reverseRatios;

    @Inject(method = "setGear", at = @At("TAIL"))
    private void bridge$afterSetGear(int gear, CallbackInfo ci) {
        GearHudBridge.changed((BlockEntity) (Object) this);
    }

    @Inject(method = "applyGearConfiguration", at = @At("TAIL"))
    private void bridge$afterConfig(List<Float> forward, List<Float> reverse, CallbackInfo ci) {
        GearHudBridge.changed((BlockEntity) (Object) this);
    }

    @Override public int bridge$gear() { return currentGear; }
    @Override public int bridge$forward() { return forwardRatios.size(); }
    @Override public int bridge$reverse() { return reverseRatios.size(); }
}
