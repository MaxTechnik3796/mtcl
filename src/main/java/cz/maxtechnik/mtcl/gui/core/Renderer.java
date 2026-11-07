package cz.maxtechnik.mtcl.gui.core;

import cz.maxtechnik.mtcl.MtclMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
public class Renderer{
	private static final ResourceLocation TEXTURE=ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/item_info.png");
	protected static void render(GuiGraphics gui,int x,int y,int width,int height,int offsetX,int offsetY,int cornerSize){
		gui.blit(TEXTURE,x,y,offsetX,offsetY,cornerSize,cornerSize,256,256);
		gui.blit(TEXTURE,x+width-(cornerSize+1),y,offsetX+cornerSize+1,offsetY,cornerSize,cornerSize,256,256);
		gui.blit(TEXTURE,x,y+height-(cornerSize+1),offsetX,offsetY+cornerSize+1,cornerSize,cornerSize,256,256);
		gui.blit(TEXTURE,x+width-(cornerSize+1),y+height-(cornerSize+1),offsetX+cornerSize+1,offsetY+cornerSize+1,8,8,256,256);
		if(width>cornerSize*2-1)
			for(int i=0;i<width-cornerSize*2-1;i++){
				gui.blit(TEXTURE,x+cornerSize+i,y,offsetX+cornerSize,offsetY,1,cornerSize,256,256);
				gui.blit(TEXTURE,x+cornerSize+i,y+height-(cornerSize+1),offsetX+cornerSize,offsetY+cornerSize+1,1,cornerSize,256,256);
			}
		if(height>cornerSize*2-1)
			for(int i=0;i<height-cornerSize*2-1;i++){
				gui.blit(TEXTURE,x,y+cornerSize+i,offsetX,offsetY+cornerSize,cornerSize,1,256,256);
				gui.blit(TEXTURE,x+width-(cornerSize+1),y+cornerSize+i,offsetX+cornerSize+1,offsetY+cornerSize,cornerSize,1,256,256);
			}
		if(width>cornerSize*2-1&&height>cornerSize*2-1)
			for(int i=0;i<width-cornerSize*2-1;i++)
				for(int j=0;j<height-cornerSize*2-1;j++)
					gui.blit(TEXTURE,x+cornerSize+i,y+cornerSize+j,offsetX+cornerSize,offsetY+cornerSize,1,1,256,256);
	}
}