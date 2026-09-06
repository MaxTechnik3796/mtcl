package cz.maxtechnik.mtcl.mixin;

import net.neoforged.neoforge.client.event.RenderArmEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(targets="com.simibubi.create.content.equipment.armor.NetheriteBacktankFirstPersonRenderer", remap=false)
public class NetheriteBacktankFirstPersonRendererMixin{
	@Inject(method="onRenderPlayerHand", at=@At("HEAD"), cancellable=true)
	private static void disableBacktankGlove(RenderArmEvent event,CallbackInfo ci){
		ci.cancel();
	}
}