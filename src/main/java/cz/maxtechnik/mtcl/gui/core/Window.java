package cz.maxtechnik.mtcl.gui.core;

import net.minecraft.client.gui.GuiGraphics;
public enum Window{
	TEXT(17),
	CLASSIC(8);
	private final int mixSize;
	Window(int minSize){
		this.mixSize=minSize;
	}
	public static void create(Window type,GuiGraphics gui,int x,int y,int width,int height){
		if(Math.min(width,height)<type.mixSize) return;
		if(type.equals(Window.TEXT)) Renderer.render(gui,x,y,width,height,0,0,8);
		if(type.equals(Window.CLASSIC)) Renderer.render(gui,x,y,width,height,17,0,4);
	}
}