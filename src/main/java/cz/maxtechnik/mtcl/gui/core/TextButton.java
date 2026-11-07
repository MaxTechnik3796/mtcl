package cz.maxtechnik.mtcl.gui.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
public class TextButton extends Button{
	private boolean underline=false;
	private int color=0xFFFFFF;
	private Component text;
	public TextButton(int x,int y,int width,int height,Component text,OnPress onPress){
		super(x,y,width,height,text,onPress);
		this.text=text;
	}
	public void setUnderline(boolean value){
		underline=value;
	}
	public void setColor(int value){
		color=value;
	}
	@Override
	public void renderWidget(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTicks){
		text=ComponentUtils.mergeStyles(text.copy(),Style.EMPTY.withUnderlined(underline&&isHovered));
		gui.drawString(Minecraft.getInstance().font,text,getX(),getY(),color);
	}
}
