package dev.gearhud.bridge.mixin;

import dev.gearhud.bridge.client.GearClientState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Pseudo
@Mixin(targets = "com.horizon.speedhud.client.HorizonHud", remap = false)
public abstract class HorizonHudMixin {
    /** Reemplaza el texto fijo "D" de BetterSpeedHUD. */
    @ModifyConstant(method = "draw", constant = @Constant(stringValue = "D"))
    private static String bridge$gearLabel(String original) {
        return GearClientState.label(original);
    }
}
