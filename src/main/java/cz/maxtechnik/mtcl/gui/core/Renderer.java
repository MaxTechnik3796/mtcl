package cz.maxtechnik.mtcl.gui.core;

import cz.maxtechnik.mtcl.MtclMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
public class Renderer{
	public enum Texture{
		CLASSIC(ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/background/classic.png"),256,4),
		TEXT(ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/background/text.png"),256,8),
		SLOT(ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/background/slot.png"),256,1),
		BOARD(ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/background/board.png"),256,4);
		final ResourceLocation texture;
		final int size;
		final int cornerSize;
		Texture(ResourceLocation texture,int size,int cornerSize){
			this.texture=texture;
			this.size=size;
			this.cornerSize=cornerSize;
		}
	}
	protected static void render(GuiGraphics gui,Texture texture,int x,int y,int width,int height){
		if(width>texture.size||height>texture.size) return;
		//TOP CORNER:
		gui.blit(texture.texture,x,y,0,0,width-texture.cornerSize,height-texture.cornerSize,texture.size,texture.size);
		//VERTICAL:
		gui.blit(texture.texture,x+width-texture.cornerSize,y,texture.size-texture.cornerSize,0,texture.cornerSize,height-texture.cornerSize,texture.size,texture.size);
		//HORIZONTAL:
		gui.blit(texture.texture,x,y+height-texture.cornerSize,0,texture.size-texture.cornerSize,width-texture.cornerSize,texture.cornerSize,texture.size,texture.size);
		//BOTTOM CORNER:
		gui.blit(texture.texture,x+width-texture.cornerSize,y+height-texture.cornerSize,texture.size-texture.cornerSize,texture.size-texture.cornerSize,texture.cornerSize,texture.cornerSize,texture.size,texture.size);
	}
}