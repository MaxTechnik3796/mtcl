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
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
public class ItemInfo extends Screen{
	private final Screen parent;
	private final ItemStack itemStack;
	private Tab itemTab, blockTab, fluidTab;
	private Button leftTag,rightTag;
	private TextButton pageButton, idButton;
	private int page=0;
	private static final ResourceLocation TEXTURE=ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/item_info.png");
	public ItemInfo(ItemStack itemStack,Screen parent){
		super(Component.translatable("screen."+MtclMod.MODID+".item_info"));
		this.parent=parent;
		this.itemStack=itemStack;
	}
	@Override
	public void init(){
		super.init();
		this.addRenderableWidget(new Button(width/2+166,height/2-55,40,14,Component.literal("Reload"),button ->{
			onClose();
			if(minecraft!=null) minecraft.setScreen(this);
		}));
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
		itemTab.setHover("info."+MtclMod.MODID+".item_tags");
		blockTab=this.addRenderableWidget(new Tab(width/2-41,height/2-4,Component.literal("Block Tags"),new ItemStack(Items.STONE),button->{
			System.out.println("Block Tag");
			selectTab(1);
		}));
		blockTab.setHover("info."+MtclMod.MODID+".block_tags");
		fluidTab=this.addRenderableWidget(new Tab(width/2-41,height/2+26,Component.literal("Fluid Tags"),new ItemStack(Items.WATER_BUCKET),button->{
			System.out.println("Fluid Tag");
			selectTab(2);
		}));
		fluidTab.setHover("info."+MtclMod.MODID+".fluid_tags");
		leftTag=this.addRenderableWidget(new Button(width/2+12,height/2-20,14,14,Component.literal("<"),button -> {}));
		leftTag.setSkipFocus(true);
		rightTag=this.addRenderableWidget(new Button(width/2+177,height/2-20,14,14,Component.literal(">"),button -> {}));
		rightTag.setSkipFocus(true);

		String pageText="%page%";
		int pageWidth=font.width(pageText);
		int pageX=width/2+101-pageWidth+pageWidth/2;
		pageButton=this.addRenderableWidget(new TextButton(pageX,height/2-17,pageWidth,pageText,button -> {}));

		String idText="%id%";
		int idWidth=font.width(idText);
		int idX=width/2+101-idWidth+idWidth/2;
		idButton=this.addRenderableWidget(new TextButton(idX,height/2-2,idWidth,idText,button->{}));

		String itemDisplayName=itemStack.getHoverName().getString();
		TextButton itemDisplayNameButton=this.addRenderableWidget(new TextButton(width/2+45,height/2-108,140,itemDisplayName,button->copy(itemDisplayName)));
		itemDisplayNameButton.setUnderline(true);
		ResourceLocation rawItemRegisterName=BuiltInRegistries.ITEM.getKey(itemStack.getItem());
		String itemRegisterName=rawItemRegisterName.toString();
		TextButton itemRegisterNameButton=this.addRenderableWidget(new TextButton(width/2+45,height/2-94,140,itemRegisterName,button->copy(itemRegisterName)));
		itemRegisterNameButton.setUnderline(true);
		itemRegisterNameButton.setColor(0x55FF55);
		String itemName=rawItemRegisterName.getPath();
		TextButton itemNameButton=this.addRenderableWidget(new TextButton(width/2+45,height/2-84,140,itemName,button->copy(itemName)));
		itemNameButton.setUnderline(true);
		itemNameButton.setColor(0x00AA00);
		String value="null";
		try{
			var container=ModList.get().getModContainerById(rawItemRegisterName.getNamespace());
			if(container.isPresent()) value=container.get().getModInfo().getDisplayName();
		}catch(Exception ignored){
		}
		String modDisplayName=value;
		TextButton modDisplayNameButton=this.addRenderableWidget(new TextButton(width/2+45,height/2-70,140,modDisplayName,button->copy(modDisplayName)));
		modDisplayNameButton.setUnderline(true);
		modDisplayNameButton.setColor(0x5555FF);
		String modName=rawItemRegisterName.getNamespace();
		TextButton modNameButton=this.addRenderableWidget(new TextButton(width/2+45,height/2-60,140,modName,button->copy(modName)));
		modNameButton.setUnderline(true);
		modNameButton.setColor(0x5555FF);
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
		Window.create(gui,Renderer.Texture.BOARD,width/2+40,height/2-113,150,72);
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
		gui.fill(width/2+26,height/2-20,width/2+177,height/2-6,0xFFA1A1A1);

		gui.fill(width/2+26,height/2-5,width/2+177,height/2+9,0xFFA1A1A1);


		RenderSystem.disableBlend();
	}
	private void copy(String copy){
		if(minecraft==null) return;
		minecraft.keyboardHandler.setClipboard(copy);
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