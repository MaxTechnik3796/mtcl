package cz.maxtechnik.mtcl.gui.core;

import net.minecraft.client.gui.GuiGraphics;
public class Window{
	public static void create(GuiGraphics gui,Renderer.Texture type,int x,int y,int width,int height){
		Renderer.render(gui,type,x,y,width,height);
	}
}