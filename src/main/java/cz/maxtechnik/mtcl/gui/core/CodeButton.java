package cz.maxtechnik.mtcl.gui.core;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.maxtechnik.mtcl.MtclMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
public class CodeButton extends Button{
	private WidgetSprites texture;
	private Component message;
	public enum Texture{
		COPY(new WidgetSprites(ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/code/copy.png"),ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/code/copy_highlighted.png"))),
		ALL(new WidgetSprites(ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/code/all.png"),ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/code/all_highlighted.png"))),
		CHANGES(new WidgetSprites(ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/code/changes.png"),ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/code/changes_highlighted.png"))),
		GIVE(new WidgetSprites(ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/code/give.png"),ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/code/give_highlighted.png")));
		final WidgetSprites sprites;
		Texture(WidgetSprites sprites){
			this.sprites=sprites;
		}
	}
	@FunctionalInterface
	public interface OnPress{
		void onPress(CodeButton button);
	}
	public CodeButton(int x,int y,Component message,Texture texture,CodeButton.OnPress onPress){
		super(x,y,20,18,message,button->{if(onPress!=null) onPress.onPress((CodeButton)button);});
		this.texture=texture.sprites;
		this.message=message;
	}
	public void setMessage(@NotNull Component value){
		this.message=value;
	}
	public void setTexture(Texture value){
		this.texture=value.sprites;
	}
	@Override
	protected void renderWidget(GuiGraphics gui,int mouseX,int mouseY,float partialTick){
		gui.setColor(1F,1F,1F,this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		ResourceLocation sprite=texture.get(true,isHovered);
		gui.blit(sprite,getX(),getY(),0,0,20,18,20,18);
		if(isHovered) gui.renderTooltip(Minecraft.getInstance().font,message,mouseX,mouseY);
		RenderSystem.disableBlend();
	}
}
