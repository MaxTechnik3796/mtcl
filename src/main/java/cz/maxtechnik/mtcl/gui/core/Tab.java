package cz.maxtechnik.mtcl.gui.core;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.maxtechnik.mtcl.MtclMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
public class Tab extends Button{
	private final ItemStack icon;
	private boolean pressed=false;
	public Tab(int x,int y,Component text,ItemStack icon,OnPress onPress){
		super(x,y,32,28,text,onPress,DEFAULT_NARRATION);
		this.icon=icon;
	}
	public void press(boolean value){
		pressed=value;
	}
	@Override
	protected void renderWidget(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTick){
		RenderSystem.setShaderColor(1,1,1,1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		gui.blit(pressed?ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/tab_selected.png"):ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/tab_unselected.png"),this.getX(),this.getY(),0,0,32,28,32,28);
		gui.pose().pushPose();
		gui.renderItem(icon,this.getX()+14,this.getY()+6);
		gui.pose().popPose();
		RenderSystem.disableBlend();
	}
}
