package cz.maxtechnik.mtcl.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import cz.maxtechnik.mtcl.MtclMod;
import cz.maxtechnik.mtcl.gui.core.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
public class ItemInfo extends Screen{
	private final Screen parent;
	private final ItemStack itemStack;
	private Tab itemTab, blockTab, fluidTab;
	private Button leftTag,rightTag;
	private TextButton itemDisplayName,itemRegisterName,itemName,modDisplayName;
	private static final ResourceLocation TEXTURE=ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/item_info.png");
	public ItemInfo(ItemStack itemStack,Screen parent){
		super(Component.translatable("screen."+MtclMod.MODID+".item_info"));
		this.parent=parent;
		this.itemStack=itemStack;
	}
	@Override
	public void init(){
		super.init();
		TagKey<Item> tag=ItemTags.create(ResourceLocation.parse("c:ingots"));
		//TagKey<Item> tag=ItemTags.create(ResourceLocation.parse("minecraft:stairs"));
		List<ItemStack> itemStacks=BuiltInRegistries.ITEM.getOrCreateTag(tag).stream().map(ItemStack::new).toList();
		SlotGrid slotGrid=this.addRenderableWidget(new SlotGrid(width/2+12,height/2+10,9,5,(itemStack,index)->{
			System.out.println("Klik na item v mřížce: "+itemStack.getHoverName().getString()+" (index: "+index+")");
		}));
		slotGrid.setItems(itemStacks);
		itemTab=this.addRenderableWidget(new Tab(width/2-41,height/2-34,Component.literal("Item Tags"),new ItemStack(Items.IRON_INGOT),button->{
			System.out.println("Item Tag");
			selectTab(0);
		}));
		blockTab=this.addRenderableWidget(new Tab(width/2-41,height/2-4,Component.literal("Block Tags"),new ItemStack(Items.STONE),button->{
			System.out.println("Block Tag");
			selectTab(1);
		}));
		fluidTab=this.addRenderableWidget(new Tab(width/2-41,height/2+26,Component.literal("Fluid Tags"),new ItemStack(Items.WATER_BUCKET),button->{
			System.out.println("Fluid Tag");
			selectTab(2);
		}));
		leftTag=this.addRenderableWidget(new Button(width/2+12,height/2-5,14,14,Component.literal("<"),button -> {}));
		leftTag.setSkipFocus(true);
		rightTag=this.addRenderableWidget(new Button(width/2+177,height/2-5,14,14,Component.literal(">"),button -> {}));
		rightTag.setSkipFocus(true);
	}
	@Override
	public void render(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTicks){
		super.render(gui,mouseX,mouseY,partialTicks);
	}
	@Override
	public void renderBackground(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTick){
		super.renderBackground(gui,mouseX,mouseY,partialTick);
		if(minecraft==null) return;
		RenderSystem.setShaderColor(1,1,1,1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		//Components:
		Window.create(gui,Renderer.Texture.TEXT,width/2-210,height/2-117,170,235);
		//Item Info:
		Window.create(gui,Renderer.Texture.CLASSIC,width/2-38,height/2-117,248,80);
		Window.create(gui,Renderer.Texture.BOARD,width/2-34,height/2-113,72,72);
		int scale=4;
		int itemSize=16*scale;
		int x=(width-itemSize)/2+2;
		int y=(height-itemSize)/2-77;
		gui.pose().pushPose();
		gui.pose().translate(x,y,0F);
		gui.pose().scale(scale,scale,1F);
		Lighting.setupForEntityInInventory();
		gui.renderItem(itemStack,0,0);
		gui.renderItemDecorations(minecraft.font,itemStack,0,0);
		gui.flush();
		Lighting.setupFor3DItems();
		gui.pose().popPose();
		//Tags:
		Window.create(gui,Renderer.Texture.CLASSIC,width/2-9,height/2-37,219,155);

		gui.fill(width/2+26,height/2-5,width/2+177,height/2+9,0xFFA1A1A1);
		gui.drawCenteredString(minecraft.font,Component.literal("xxxxxxxxxxxxxxxxxxxxxxxxx"),width/2+101,height/2-3,0xFFFFFF);

		RenderSystem.disableBlend();
	}
	private void selectTab(int tab){
		switch(tab){
			case 1 -> {
				itemTab.press(false);
				blockTab.press(true);
				fluidTab.press(false);
			}
			case 2 -> {
				itemTab.press(false);
				blockTab.press(false);
				fluidTab.press(true);
			}
			default -> {
				itemTab.press(true);
				blockTab.press(false);
				fluidTab.press(false);
			}
		}
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