package cz.maxtechnik.mtcl.gui.core;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.maxtechnik.mtcl.MtclMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
public class Slot extends Button{
	private final ItemStack itemStack;
	public Slot(int x,int y,ItemStack itemStack,OnPress onPress){
		super(x,y,18,18,Component.empty(),onPress,DEFAULT_NARRATION);
		this.itemStack=itemStack;
	}
	@Override
	public void onClick(double mouseX,double mouseY){
		super.onClick(mouseX,mouseY);
		this.setFocused(false);
		System.out.println("neco");
	}
	@Override
	protected void renderWidget(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTick){
		RenderSystem.setShaderColor(1,1,1,1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		gui.blit(ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/slot.png"),this.getX(),this.getY(),0,0,18,18,18,18);
		gui.pose().pushPose();
		gui.renderItem(itemStack,this.getX()+1,this.getY()+1);
		gui.renderItemDecorations(Minecraft.getInstance().font,itemStack,this.getX()+1,this.getY()+1);
		gui.pose().popPose();
		if(this.isHovered()){
			gui.pose().pushPose();
			gui.pose().translate(0,0,300);
			gui.fill(this.getX()+1,this.getY()+1,this.getX()+this.getWidth()-1,this.getY()+this.getHeight()-1,0x80FFFFFF);
			gui.renderTooltip(Minecraft.getInstance().font,itemStack,mouseX,mouseY);
			gui.pose().popPose();
		}
		RenderSystem.disableBlend();
	}
}
