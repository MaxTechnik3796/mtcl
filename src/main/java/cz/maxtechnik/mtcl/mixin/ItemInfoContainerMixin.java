package cz.maxtechnik.mtcl.mixin;

import cz.maxtechnik.mtcl.gui.ItemInfo;
import cz.maxtechnik.mtcl.init.other.MtclModKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(AbstractContainerScreen.class)
public class ItemInfoContainerMixin{
	@Inject(method="keyPressed", at=@At("HEAD"), cancellable=true)
	private void onKeyPressed(int keyCode,int scanCode,int modifiers,CallbackInfoReturnable<Boolean> cir){
		AbstractContainerScreen<?> self=(AbstractContainerScreen<?>)(Object)this;
		if(MtclModKeys.ITEM_INFO.matches(keyCode,scanCode)){
			Minecraft mc=Minecraft.getInstance();
			ItemStack hoveredStack=self.getMenu().getCarried();
			if(hoveredStack.isEmpty()){
				Slot slot=self.getSlotUnderMouse();
				if(slot!=null&&!slot.getItem().isEmpty()) hoveredStack=slot.getItem();
			}
			if(!hoveredStack.isEmpty()){
				mc.setScreen(new ItemInfo(hoveredStack,self));
				cir.setReturnValue(true);
			}
		}
	}
}