package cz.maxtechnik.mtcl.mixin;

import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
	@Inject(
			method = "getPriorityFogFunction",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void mtcl$cancelMobEffectFog(Entity entity, float partialTick, CallbackInfoReturnable<?> cir) {
		cir.setReturnValue(null);
	}
}