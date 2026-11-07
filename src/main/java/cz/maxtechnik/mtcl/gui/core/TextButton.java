package cz.maxtechnik.mtcl.gui.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
public class TextButton extends Button{
	private boolean underline=false;
	private int color=0xFFFFFF;
	private Component text;
	public TextButton(int x,int y,int width,Component text,OnPress onPress){
		super(x,y,width,9,text,onPress);
		this.text=ComponentUtils.mergeStyles(text.copy(),Style.EMPTY.withUnderlined(underline&&isHovered));
	}
	public void setUnderline(boolean value){
		underline=value;
	}
	public void setColor(int value){
		color=value;
	}
	@Override
	public void renderWidget(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTicks){
		this.renderString(gui,Minecraft.getInstance().font,color|Mth.ceil(this.alpha*255F)<<24);
	}
	@Override
	public void renderString(@NotNull GuiGraphics gui,@NotNull Font font,int color){
		this.renderScrollingString(gui,font,0,color);
	}
	@Override
	protected void renderScrollingString(@NotNull GuiGraphics gui,@NotNull Font font,int width,int color){
		int i=this.getX()+width;
		int j=this.getX()+this.getWidth()-width;
		renderScrollingString(gui,font,text,i,this.getY(),j,this.getY()+this.getHeight(),color);
	}
}
