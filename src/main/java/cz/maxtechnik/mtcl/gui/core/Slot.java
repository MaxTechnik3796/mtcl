package cz.maxtechnik.mtcl.gui.core;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
public class Slot extends Button{
	private final ItemStack itemStack;
	public Slot(int x,int y,Data size,ItemStack itemStack,OnPress onPress){
		super(x,y,size.size,size.size,Component.empty(),onPress,DEFAULT_NARRATION);
		this.itemStack=itemStack;
	}
	@Override
	public void onClick(double mouseX,double mouseY){
		super.onClick(mouseX,mouseY);
		this.setFocused(false);
	}
	@Override
	protected void renderWidget(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTick){
		RenderSystem.setShaderColor(1,1,1,1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Renderer.render(gui,this.getX(),this.getY(),this.getWidth()+1,this.getHeight()+1,26,0,1);
		gui.pose().pushPose();
		gui.renderItem(itemStack,this.getX()+1,this.getY()+1);
		gui.renderItemDecorations(Minecraft.getInstance().font,itemStack,this.getX()+1,this.getY()+1);
		gui.pose().popPose();
		if(this.isHovered()){
			gui.pose().pushPose();
			gui.pose().translate(0,0,300);
			gui.fill(this.getX()+1,this.getY()+1,this.getX()+this.getWidth()-1,this.getY()+this.getHeight()-1,0x80FFFFFF);
			gui.renderTooltip(Minecraft.getInstance().font,itemStack,mouseX,mouseY);
			gui.pose().popPose();
		}
		RenderSystem.disableBlend();
	}


	public enum Data{
		CLASSIC(18),
		OUTPUT(26);
		private final int size;
		Data(int size){
			this.size=size;
		}
		/*public static void createGrid(int columns,int rows,int lastRemoved,GuiGraphics gui,int x,int y){
			if(rows<=0||columns<=0||(rows==1&&columns==lastRemoved)) return;
			int id=0;
			for(int row=0;row<rows;row++)
				for(int column=0;column<columns;column++){
					if(id>=columns*rows-lastRemoved) return;
					create(Data.CLASSIC,gui,x+(Slot.Data.CLASSIC.size-1)*column,y+(Data.CLASSIC.size-1)*row);
					gui.drawString(Minecraft.getInstance().font,String.valueOf(id),x+(Data.CLASSIC.size-1)*column,y+(Data.CLASSIC.size-1)*row,0xFFFFFF);
					id++;
				}
		}*/
	}
}
