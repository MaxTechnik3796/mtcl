package cz.maxtechnik.mtcl.gui.core;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
public class TextButton extends Button{
	private static final float SCROLL_SPEED=28F;
	private static final long PAUSE_TIME_MS=1200L;
	private boolean underline=false;
	private boolean centered=false;
	private int color=0xFFFFFF;
	private Component text;
	public TextButton(int x,int y,int width,Component text,OnPress onPress){
		super(x,y,width,9,text,onPress);
		this.text=text;
	}
	public TextButton(int x,int y,int width,String text,OnPress onPress){
		super(x,y,width,9,Component.literal(text),onPress);
		this.text=Component.literal(text);
	}
	public void setUnderline(boolean value){
		this.underline=value;
	}
	public void setCentered(boolean value){
		this.centered=value;
	}
	public void setColor(int value){
		this.color=value;
	}
	public void setText(String value){
		this.setText(Component.literal(value));
	}
	public void setText(Component value){
		this.text=value;
		this.setMessage(value);
	}
	@Override
	public void renderWidget(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTicks){
		this.renderScrollingString(gui,Minecraft.getInstance().font,0,this.color);
	}
	@Override
	protected void renderScrollingString(@NotNull GuiGraphics gui,@NotNull Font font,int width,int color){
		int minX=this.getX()+width;
		int maxX=this.getX()+this.getWidth()-width;
		int textY=this.getY()+(this.getHeight()-8)/2;
		Component renderText=(this.underline&&this.isHovered)?this.text.copy().withStyle(Style.EMPTY.withUnderlined(true)):this.text;
		this.renderLinearScrollingText(gui,font,renderText,minX,this.getY(),maxX,this.getY()+this.getHeight(),textY,color);
	}
	private void renderLinearScrollingText(GuiGraphics gui,Font font,Component text,int minX,int minY,int maxX,int maxY,int textY,int color){
		int textWidth=font.width(text);
		int availableWidth=maxX-minX;
		if(textWidth<=availableWidth){
			int renderX=this.centered?minX+(availableWidth-textWidth)/2:minX;
			gui.drawString(font,text,renderX,textY,color);
			return;
		}
		int maxScroll=(textWidth-availableWidth)+4;
		float offset=getOffset(maxScroll);
		gui.enableScissor(minX,minY,maxX,maxY);
		gui.drawString(font,text,minX-Math.round(offset),textY,color);
		gui.disableScissor();
	}
	private static float getOffset(int maxScroll){
		long scrollDurationMs=(long)((maxScroll/SCROLL_SPEED)*1000F);
		long totalCycleMs=(PAUSE_TIME_MS*2)+(scrollDurationMs*2);
		long timeInCycle=Util.getMillis()%totalCycleMs;
		if(timeInCycle<PAUSE_TIME_MS) return 0F;
		else if(timeInCycle<PAUSE_TIME_MS+scrollDurationMs){
			float progress=(float)(timeInCycle-PAUSE_TIME_MS)/(float)scrollDurationMs;
			return progress*maxScroll;
		}else if(timeInCycle<(PAUSE_TIME_MS*2)+scrollDurationMs) return (float)maxScroll;
		else{
			float progress=(float)(timeInCycle-(PAUSE_TIME_MS*2+scrollDurationMs))/(float)scrollDurationMs;
			return (1F-progress)*maxScroll;
		}
	}
}