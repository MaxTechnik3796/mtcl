package cz.maxtechnik.mtcl.mixin.non_blindness;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Inject(
			method = "doesMobEffectBlockSky",
			at = @At("HEAD"),
			cancellable = true
	)
	private void mtcl$forceSkyRendering(Camera camera, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}
}