package cz.maxtechnik.mtcl.gui.core;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
public class Button extends net.minecraft.client.gui.components.Button{
	private boolean skipFocus=false;
	public Button(int x,int y,int width,int height,Component message,OnPress onPress){
		super(x,y,width,height,message,onPress,DEFAULT_NARRATION);
	}
	public void setSkipFocus(boolean value){
		this.skipFocus=value;
		if(value) super.setFocused(false);
	}
	@Override
	public void setFocused(boolean focused){
		if(this.skipFocus){
			super.setFocused(false);
			return;
		}
		super.setFocused(focused);
	}
	@Override
	public boolean isFocused(){
		return !this.skipFocus&&super.isFocused();
	}
	@Override
	public boolean mouseReleased(double mouseX,double mouseY,int button){
		if(this.skipFocus&&Minecraft.getInstance().screen!=null){
			Minecraft.getInstance().screen.setFocused(null);
		}
		return super.mouseReleased(mouseX,mouseY,button);
	}
}
