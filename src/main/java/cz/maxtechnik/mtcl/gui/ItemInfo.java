package cz.maxtechnik.mtcl.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import cz.maxtechnik.mtcl.MtclMod;
import cz.maxtechnik.mtcl.gui.core.Slot;
import cz.maxtechnik.mtcl.gui.core.SlotGrid;
import cz.maxtechnik.mtcl.gui.core.Window;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
		// Samostatný slot
		this.addRenderableWidget(new Slot(200,20,Slot.Data.CLASSIC,this.itemStack,button->{
			System.out.println("Klik na hlavní slot");
		}));
		TagKey<Item> tag=ItemTags.create(ResourceLocation.parse("c:ingots"));
		List<ItemStack> itemStacks=BuiltInRegistries.ITEM.getOrCreateTag(tag).stream().map(ItemStack::new).toList();
		SlotGrid slotGrid=this.addRenderableWidget(new SlotGrid(250,20,3,3,(itemStack,index)->{
			System.out.println("Klik na item v mřížce: "+itemStack.getHoverName().getString()+" (index: "+index+")");
		}));
		slotGrid.setItems(itemStacks);
	}
	@Override
	public void render(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTicks){
		super.render(gui,mouseX,mouseY,partialTicks);
	}
	@Override
	public void renderBackground(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTick){
		super.renderBackground(gui,mouseX,mouseY,partialTick);
		RenderSystem.setShaderColor(1,1,1,1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		//Window.create(Window.TEXT,gui,width/2-210,height/2-117,170,235);
		//Window.create(Window.CLASSIC,gui,width/2-40,height/2-117,251,80);
		//Window.create(Window.CLASSIC,gui,width/2-40,height/2-37,251,155);


		//Window.create(Window.TEXT,gui,width/2-130,height/4,18,18);
		//Window.create(Window.TEXT,gui,width/2-100,height/4,30,30);

		//Window.create(Window.CLASSIC,gui,width/2-130,height/4+40,9,9);
		//Window.create(Window.CLASSIC,gui,width/2-100,height/4+40,30,30);
		RenderSystem.disableBlend();
	}
	@Override
	public void onClose(){
		if(this.minecraft!=null) this.minecraft.setScreen(this.parent);
	}
	@Override
	public boolean isPauseScreen(){
		return false;
	}
}