package cz.maxtechnik.mtcl.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.maxtechnik.mtcl.MtclMod;
import cz.maxtechnik.mtcl.gui.core.Slot;
import cz.maxtechnik.mtcl.gui.core.Window;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
public class ItemInfo extends Screen{
	private final Screen parent;
	private final ItemStack itemStack;
	private static final ResourceLocation TEXTURE=ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/item_info.png");
	public ItemInfo(ItemStack itemStack,Screen parent){
		super(Component.translatable("screen."+MtclMod.MODID+".item_info"));
		this.parent=parent;
		this.itemStack=itemStack;
	}
	@Override
	public void init(){
		super.init();
		this.addRenderableWidget(new Slot(20,20,Slot.Data.CLASSIC,itemStack,button->{
			System.out.println("neco");
		}));
	}
	@Override
	public void render(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTicks){
		super.render(gui,mouseX,mouseY,partialTicks);
		// Sem pak dáváš jen věci, co mají být úplně v popředí (item ikony, texty, tooltips):
		/*if(this.itemStack!=null&&!this.itemStack.isEmpty()){
			gui.renderItem(this.itemStack,width/2-145,height/4+2);
		}*/
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

		//Slot.create(Slot.CLASSIC,gui,width/2-150,height/4+80);
		//Slot.create(Slot.OUTPUT,gui,width/2-130,height/4+80);

		//Slot.createGrid(12,8,6,gui,0,0);
		//Slot.create(Slot.CLASSIC,gui,0,0);

		RenderSystem.disableBlend();
	}
	@Override
	public void onClose(){
		if(minecraft!=null) minecraft.setScreen(parent);
	}
	@Override
	public boolean isPauseScreen(){
		return false;
	}
}
