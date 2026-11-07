package cz.maxtechnik.mtcl.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.maxtechnik.mtcl.MtclMod;
import cz.maxtechnik.mtcl.gui.core.Slot;
import cz.maxtechnik.mtcl.gui.core.Window;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
public class ItemInfo extends Screen{
	private final Screen parent;
	private final ItemStack itemStack;
	private static final ResourceLocation TEXTURE=ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/item_info.png");
	private static final ResourceLocation SCROLLER_SPRITE=ResourceLocation.withDefaultNamespace("container/stonecutter/scroller");
	private static final ResourceLocation SCROLLER_DISABLED_SPRITE=ResourceLocation.withDefaultNamespace("container/stonecutter/scroller_disabled");
	private static final int SLOT_SIZE=18; // Šířka a výška slotu v pixelech (16px item + 2px okraj)
	private static final int SCROLLER_WIDTH=12;
	private final List<Slot> activeGridSlots=new ArrayList<>();
	private ItemStack[] gridItems=new ItemStack[0];
	private int gridCols=0;
	private int gridRows=0;
	private int gridStartX=0;
	private int gridStartY=0;
	private int scrollRowOffset=0;
	private boolean isDraggingScroller=false;
	public ItemInfo(ItemStack itemStack,Screen parent){
		super(Component.translatable("screen."+MtclMod.MODID+".item_info"));
		this.parent=parent;
		this.itemStack=itemStack;
	}
	@Override
	public void init(){
		super.init();
		this.addRenderableWidget(new Slot(200,20,Slot.Data.CLASSIC,itemStack,button->{
			System.out.println("neco");
		}));
		TagKey<Item> tag = ItemTags.create(ResourceLocation.parse("c:dyes"));
		List<ItemStack> itemStacks = BuiltInRegistries.ITEM.getOrCreateTag(tag).stream().map(ItemStack::new).toList();
		createGrid(itemStacks.toArray(new ItemStack[0]),3,3,20,20);
	}
	@Override
	public void render(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTicks){
		super.render(gui,mouseX,mouseY,partialTicks);
		this.renderGridScrollbar(gui);
	}
	@Override
	public void renderBackground(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTick){
		super.renderBackground(gui,mouseX,mouseY,partialTick);
		RenderSystem.setShaderColor(1,1,1,1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Window.create(Window.TEXT,gui,width/2-150,height/4,17,17);
		Window.create(Window.TEXT,gui,width/2-130,height/4,18,18);
		Window.create(Window.TEXT,gui,width/2-100,height/4,30,30);
		Window.create(Window.CLASSIC,gui,width/2-150,height/4+40,8,8);
		Window.create(Window.CLASSIC,gui,width/2-130,height/4+40,9,9);
		Window.create(Window.CLASSIC,gui,width/2-100,height/4+40,30,30);

		RenderSystem.disableBlend();
	}
	public void createGrid(ItemStack[] itemStacks,int columns,int rows,int x,int y){
		this.gridItems=itemStacks!=null?itemStacks:new ItemStack[0];
		this.gridCols=columns;
		this.gridRows=rows;
		this.gridStartX=x;
		this.gridStartY=y;
		this.scrollRowOffset=0;
		this.updateGridSlots();
	}
	private void updateGridSlots(){
		// 1. Odstranění starých tlačítek z obrazovky
		for(Slot oldSlot: this.activeGridSlots) this.removeWidget(oldSlot);
		this.activeGridSlots.clear();
		if(this.gridCols<=0||this.gridRows<=0) return;
		// 2. Vytvoření pouze viditelných slotů (columns x rows)
		for(int r=0;r<this.gridRows;r++){
			for(int c=0;c<this.gridCols;c++){
				int itemIndex=(this.scrollRowOffset+r)*this.gridCols+c;
				int slotX=this.gridStartX+(c*SLOT_SIZE);
				int slotY=this.gridStartY+(r*SLOT_SIZE);
				if(itemIndex<this.gridItems.length){
					ItemStack stack=this.gridItems[itemIndex];
					if(!stack.isEmpty()){
						Slot slot=this.addRenderableWidget(new Slot(slotX,slotY,Slot.Data.CLASSIC,stack,button->{
							System.out.println("Kliknuto na: "+stack.getHoverName().getString()+" (index: "+itemIndex+")");
						}));
						this.activeGridSlots.add(slot);
					}
				}
			}
		}
	}
	private void renderGridScrollbar(GuiGraphics gui){
		if(this.gridCols<=0||this.gridRows<=0) return;
		int totalRows=(int)Math.ceil((double)this.gridItems.length/this.gridCols);
		int maxScrollRows=Math.max(0,totalRows-this.gridRows);
		int scrollerX=this.gridStartX+(this.gridCols*SLOT_SIZE)+2;
		int scrollerY=this.gridStartY;
		int scrollerHeight=this.gridRows*SLOT_SIZE;
		// Drážka za posuvníkem
		gui.fill(scrollerX+1,scrollerY,scrollerX+SCROLLER_WIDTH-1,scrollerY+scrollerHeight,0x50000000);
		// Samotný jezdec
		if(maxScrollRows>0){
			int thumbHeight=Math.max(12,scrollerHeight*this.gridRows/totalRows);
			int thumbY=scrollerY+(int)((scrollerHeight-thumbHeight)*((float)this.scrollRowOffset/maxScrollRows));
			gui.blitSprite(SCROLLER_SPRITE,scrollerX,thumbY,SCROLLER_WIDTH,thumbHeight);
		}else{
			// Neaktivní scrollbar (např. jen 1 řádek itemů)
			gui.blitSprite(SCROLLER_DISABLED_SPRITE,scrollerX,scrollerY,SCROLLER_WIDTH,scrollerHeight);
		}
	}
	@Override
	public boolean mouseScrolled(double mouseX,double mouseY,double scrollX,double scrollY){
		int totalRows=(int)Math.ceil((double)this.gridItems.length/this.gridCols);
		int maxScrollRows=Math.max(0,totalRows-this.gridRows);
		if(maxScrollRows>0){
			// Kontrola, zda je myš nad tabulkou nebo scrollerem
			int gridWidth=(this.gridCols*SLOT_SIZE)+2+SCROLLER_WIDTH;
			int gridHeight=this.gridRows*SLOT_SIZE;
			if(mouseX>=this.gridStartX&&mouseX<=this.gridStartX+gridWidth
					&&mouseY>=this.gridStartY&&mouseY<=this.gridStartY+gridHeight){
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
		}
		return super.mouseScrolled(mouseX,mouseY,scrollX,scrollY);
	}
	@Override
	public boolean mouseClicked(double mouseX,double mouseY,int button){
		if(button==0&&this.gridCols>0){
			int totalRows=(int)Math.ceil((double)this.gridItems.length/this.gridCols);
			int maxScrollRows=Math.max(0,totalRows-this.gridRows);
			if(maxScrollRows>0){
				int scrollerX=this.gridStartX+(this.gridCols*SLOT_SIZE)+2;
				int scrollerY=this.gridStartY;
				int scrollerHeight=this.gridRows*SLOT_SIZE;
				if(mouseX>=scrollerX&&mouseX<=scrollerX+SCROLLER_WIDTH
						&&mouseY>=scrollerY&&mouseY<=scrollerY+scrollerHeight){
					this.isDraggingScroller=true;
					this.updateScrollFromMouse(mouseY,scrollerY,scrollerHeight,maxScrollRows);
					return true;
				}
			}
		}
		return super.mouseClicked(mouseX,mouseY,button);
	}
	@Override
	public boolean mouseReleased(double mouseX,double mouseY,int button){
		if(button==0){
			this.isDraggingScroller=false;
		}
		return super.mouseReleased(mouseX,mouseY,button);
	}
	@Override
	public boolean mouseDragged(double mouseX,double mouseY,int button,double dragX,double dragY){
		if(this.isDraggingScroller&&button==0){
			int totalRows=(int)Math.ceil((double)this.gridItems.length/this.gridCols);
			int maxScrollRows=Math.max(0,totalRows-this.gridRows);
			if(maxScrollRows>0){
				int scrollerY=this.gridStartY;
				int scrollerHeight=this.gridRows*SLOT_SIZE;
				this.updateScrollFromMouse(mouseY,scrollerY,scrollerHeight,maxScrollRows);
				return true;
			}
		}
		return super.mouseDragged(mouseX,mouseY,button,dragX,dragY);
	}
	private void updateScrollFromMouse(double mouseY,int scrollerY,int scrollerHeight,int maxScrollRows){
		float relativeY=(float)(mouseY-scrollerY)/(float)scrollerHeight;
		int newOffset=Math.clamp(Math.round(relativeY*maxScrollRows),0,maxScrollRows);
		if(newOffset!=this.scrollRowOffset){
			this.scrollRowOffset=newOffset;
			this.updateGridSlots();
		}
	}
	/*public static void createGrid(int columns,int rows,int lastRemoved,int x,int y){
		if(rows<=0||columns<=0||(rows==1&&columns==lastRemoved)) return;
		int id=0;
		for(int row=0;row<rows;row++)
			for(int column=0;column<columns;column++){
				if(id>=columns*rows-lastRemoved) return;
				//create(Data.CLASSIC,gui,x+(Slot.Data.CLASSIC.size-1)*column,y+(Data.CLASSIC.size-1)*row);
				//gui.drawString(Minecraft.getInstance().font,String.valueOf(id),x+(Data.CLASSIC.size-1)*column,y+(Data.CLASSIC.size-1)*row,0xFFFFFF);
				id++;
			}
	}*/
	@Override
	public void onClose(){
		if(minecraft!=null) minecraft.setScreen(parent);
	}
	@Override
	public boolean isPauseScreen(){
		return false;
	}
}
