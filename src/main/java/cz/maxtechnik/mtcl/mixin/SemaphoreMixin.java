package cz.maxtechnik.mtcl.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(targets="com.railwayteam.railways.content.semaphore.SemaphoreRenderer", remap=false)
public class SemaphoreMixin{
	@WrapOperation(
			method="renderSafe*",
			at=@At(
					value="INVOKE",
					target="Lnet/minecraft/client/renderer/RenderType;solid()Lnet/minecraft/client/renderer/RenderType;",
					ordinal=1
			),
			remap=false
	)
	private RenderType mtcl$makeSemaphoreLampTranslucent(Operation<RenderType> original){
		return RenderType.translucent();
	}
}