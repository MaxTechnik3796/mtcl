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
	private Component message;
	public TextButton(int x,int y,int width,Component message,OnPress onPress){
		super(x,y,width,9,message,onPress);
		this.message=message;
	}
	public TextButton(int x,int y,int width,String message,OnPress onPress){
		super(x,y,width,9,Component.literal(message),onPress);
		this.message=Component.literal(message);
	}
	public TextButton(int x,int y,int width,Component message){
		super(x,y,width,9,message,null);
		this.message=message;
	}
	public TextButton(int x,int y,int width,String message){
		super(x,y,width,9,Component.literal(message),null);
		this.message=Component.literal(message);
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
	public void setMessage(String value){
		this.setText(Component.literal(value));
	}
	public void setText(Component value){
		this.message=value;
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
		Component renderText=(this.underline&&this.isHovered)?this.message.copy().withStyle(Style.EMPTY.withUnderlined(true)):this.message;
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