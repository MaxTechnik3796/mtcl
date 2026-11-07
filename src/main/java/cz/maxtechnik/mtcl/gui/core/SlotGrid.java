package cz.maxtechnik.mtcl.gui.core;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
public class SlotGrid extends AbstractWidget{
	private static final ResourceLocation
			SCROLLER_SPRITE=ResourceLocation.withDefaultNamespace("container/stonecutter/scroller"),
			SCROLLER_DISABLED_SPRITE=ResourceLocation.withDefaultNamespace("container/stonecutter/scroller_disabled");
	private static final int SLOT_SIZE=18, SCROLLER_WIDTH=12, SCROLLER_HEIGHT=15;
	private final int gridCols, gridRows;
	private final List<Slot> activeGridSlots=new ArrayList<>();
	private final OnSlotClick onSlotClick;
	private ItemStack[] gridItems=new ItemStack[0];
	private int scrollRowOffset=0;
	private boolean isDraggingScroller=false;
	@FunctionalInterface
	public interface OnSlotClick{
		void onClick(ItemStack itemStack,int index);
	}
	public SlotGrid(int x,int y,int columns,int rows,OnSlotClick onSlotClick){
		super(x,y,(columns*SLOT_SIZE)+4+SCROLLER_WIDTH,rows*SLOT_SIZE,Component.empty());
		this.gridCols=columns;
		this.gridRows=rows;
		this.onSlotClick=onSlotClick;
	}
	public SlotGrid(int x,int y,int columns,int rows){
		this(x,y,columns,rows,null);
	}
	public void setItems(ItemStack[] items){
		this.gridItems=items!=null?items:new ItemStack[0];
		this.scrollRowOffset=0;
		this.updateGridSlots();
	}
	public void setItems(List<ItemStack> items){
		this.setItems(items!=null?items.toArray(ItemStack[]::new):new ItemStack[0]);
	}
	private void updateGridSlots(){
		this.activeGridSlots.clear();
		if(this.gridCols<=0||this.gridRows<=0) return;
		for(int r=0;r<this.gridRows;r++){
			for(int c=0;c<this.gridCols;c++){
				int itemIndex=(this.scrollRowOffset+r)*this.gridCols+c;
				int slotX=this.getX()+(c*SLOT_SIZE);
				int slotY=this.getY()+(r*SLOT_SIZE);
				if(itemIndex<this.gridItems.length){
					ItemStack stack=this.gridItems[itemIndex];
					if(!stack.isEmpty()){
						Slot slot=new Slot(slotX,slotY,Slot.Data.CLASSIC,stack,button->{
							if(this.onSlotClick!=null) this.onSlotClick.onClick(stack,itemIndex);
						});
						this.activeGridSlots.add(slot);
					}
				}
			}
		}
	}
	@Override
	protected void renderWidget(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTick){
		for(Slot slot: this.activeGridSlots) slot.render(gui,mouseX,mouseY,partialTick);
		this.renderGridScrollbar(gui);
	}
	private void renderGridScrollbar(GuiGraphics gui){
		if(this.gridCols<=0||this.gridRows<=0) return;
		int totalRows=(int)Math.ceil((double)this.gridItems.length/this.gridCols);
		int maxScrollRows=Math.max(0,totalRows-this.gridRows);
		int scrollerX=this.getX()+(this.gridCols*SLOT_SIZE)+3;
		int scrollerY=this.getY();
		int scrollerHeight=this.gridRows*SLOT_SIZE;
		Renderer.render(gui,scrollerX-1,scrollerY,SCROLLER_WIDTH+3,scrollerHeight+1,26,0,1);
		int trackStartY=scrollerY+1;
		int trackAvailable=scrollerHeight-SCROLLER_HEIGHT-2;
		if(maxScrollRows>0&&trackAvailable>0){
			int thumbY=trackStartY+(int)(trackAvailable*((float)this.scrollRowOffset/maxScrollRows));
			gui.blitSprite(SCROLLER_SPRITE,scrollerX,thumbY,SCROLLER_WIDTH,SCROLLER_HEIGHT);
		}else gui.blitSprite(SCROLLER_DISABLED_SPRITE,scrollerX,trackStartY,SCROLLER_WIDTH,SCROLLER_HEIGHT);
	}
	@Override
	public boolean mouseScrolled(double mouseX,double mouseY,double scrollX,double scrollY){
		if(!this.active||!this.visible) return false;
		int totalRows=(int)Math.ceil((double)this.gridItems.length/this.gridCols);
		int maxScrollRows=Math.max(0,totalRows-this.gridRows);
		if(maxScrollRows>0&&this.isMouseOver(mouseX,mouseY)){
			if(scrollY>0&&this.scrollRowOffset>0){
				this.scrollRowOffset--;
				this.updateGridSlots();
				return true;
			}else if(scrollY<0&&this.scrollRowOffset<maxScrollRows){
				this.scrollRowOffset++;
				this.updateGridSlots();
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean mouseClicked(double mouseX,double mouseY,int button){
		if(!this.active||!this.visible) return false;
		if(button==0&&this.gridCols>0){
			int totalRows=(int)Math.ceil((double)this.gridItems.length/this.gridCols);
			int maxScrollRows=Math.max(0,totalRows-this.gridRows);
			int scrollerX=this.getX()+(this.gridCols*SLOT_SIZE)+3;
			int scrollerY=this.getY();
			int scrollerHeight=this.gridRows*SLOT_SIZE;
			if(maxScrollRows>0&&mouseX>=scrollerX-1&&mouseX<=scrollerX+SCROLLER_WIDTH+2&&mouseY>=scrollerY&&mouseY<=scrollerY+scrollerHeight){
				this.isDraggingScroller=true;
				this.updateScrollFromMouse(mouseY,scrollerY,scrollerHeight,maxScrollRows);
				return true;
			}
		}
		for(Slot slot: this.activeGridSlots) if(slot.mouseClicked(mouseX,mouseY,button)) return true;
		return false;
	}
	@Override
	public boolean mouseReleased(double mouseX,double mouseY,int button){
		if(button==0) this.isDraggingScroller=false;
		for(Slot slot: this.activeGridSlots) slot.mouseReleased(mouseX,mouseY,button);
		return super.mouseReleased(mouseX,mouseY,button);
	}
	@Override
	public boolean mouseDragged(double mouseX,double mouseY,int button,double dragX,double dragY){
		if(this.isDraggingScroller&&button==0&&this.gridCols>0){
			int totalRows=(int)Math.ceil((double)this.gridItems.length/this.gridCols);
			int maxScrollRows=Math.max(0,totalRows-this.gridRows);
			if(maxScrollRows>0){
				int scrollerY=this.getY();
				int scrollerHeight=this.gridRows*SLOT_SIZE;
				this.updateScrollFromMouse(mouseY,scrollerY,scrollerHeight,maxScrollRows);
				return true;
			}
		}
		for(Slot slot: this.activeGridSlots) if(slot.mouseDragged(mouseX,mouseY,button,dragX,dragY)) return true;
		return false;
	}
	private void updateScrollFromMouse(double mouseY,int scrollerY,int scrollerHeight,int maxScrollRows){
		int trackStartY=scrollerY+1;
		int trackAvailable=scrollerHeight-SCROLLER_HEIGHT-2;
		if(trackAvailable<=0) return;
		float relativeY=(float)(mouseY-trackStartY-(SCROLLER_HEIGHT/2F))/(float)trackAvailable;
		float clampedY=Math.clamp(relativeY,0F,1F);
		int newOffset=Math.round(clampedY*maxScrollRows);
		if(newOffset!=this.scrollRowOffset){
			this.scrollRowOffset=newOffset;
			this.updateGridSlots();
		}
	}
	@Override
	protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput){
	}
}