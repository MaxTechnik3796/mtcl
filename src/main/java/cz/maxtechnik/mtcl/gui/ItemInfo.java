package cz.maxtechnik.mtcl.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import cz.maxtechnik.mtcl.MtclMod;
import cz.maxtechnik.mtcl.MtclModClientConfig;
import cz.maxtechnik.mtcl.gui.core.*;
import cz.maxtechnik.mtcl.init.other.MtclModKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class ItemInfo extends Screen{
	private final Screen parent;
	private final ItemStack itemStack;
	private enum TagType{ITEM,BLOCK,FLUID}
	private TagType currentTabType=TagType.ITEM;
	private final List<TagKey<Item>> itemTags=new ArrayList<>();
	private final List<TagKey<Block>> blockTags=new ArrayList<>();
	private final List<TagKey<Fluid>> fluidTags=new ArrayList<>();
	private Tab itemTab, blockTab, fluidTab;
	private Button leftTag, rightTag;
	private TextButton pageButton, idButton, clipboardButton;
	private SlotGrid slotGrid;
	private int tagIndex=0;
	private String currentTagId="";
	private CodeView codeViewer;
	private boolean onlyComponentChanges=false;
	public ItemInfo(ItemStack itemStack,Screen parent){
		super(Component.translatable("screen."+MtclMod.MODID+".item_info"));
		this.parent=parent;
		this.itemStack=itemStack;
	}
	@Override
	public void init(){
		super.init();
		this.loadTags();
		ResourceLocation rawItemRegisterName=BuiltInRegistries.ITEM.getKey(itemStack.getItem());
		String itemRegisterName=rawItemRegisterName.toString();
		initCode(itemRegisterName);
		initItemInfo(rawItemRegisterName,itemRegisterName);
		initTags();
	}
	private void initCode(String itemRegisterName){
		CodeButton toggleChangesButton=this.addRenderableWidget(new CodeButton(width/2-37,height/2+33,Component.translatable(onlyComponentChanges?"info."+MtclMod.MODID+".code.changes":"info."+MtclMod.MODID+".code.all"),onlyComponentChanges?CodeButton.Texture.CHANGES:CodeButton.Texture.ALL,button->{
			onlyComponentChanges=!onlyComponentChanges;
			button.setMessage(Component.translatable(onlyComponentChanges?"info."+MtclMod.MODID+".code.changes":"info."+MtclMod.MODID+".code.all"));
			button.setTexture(onlyComponentChanges?CodeButton.Texture.CHANGES:CodeButton.Texture.ALL);
			if(codeViewer!=null) codeViewer.loadFromItemStack(this.itemStack,onlyComponentChanges);
		}));
		toggleChangesButton.setSkipFocus(true);
		CodeButton copyAllButton=this.addRenderableWidget(new CodeButton(width/2-37,height/2+62,Component.translatable("info."+MtclMod.MODID+".code.copy"),CodeButton.Texture.COPY,button->{
			String serialized=ComponentHandler.extractComponentsToString(this.itemStack,onlyComponentChanges);
			if(!serialized.isEmpty()) copy(serialized);
		}));
		copyAllButton.setSkipFocus(true);
		CodeButton giveButton=this.addRenderableWidget(new CodeButton(width/2-37,height/2+91,Component.translatable("info."+MtclMod.MODID+".code.give"),CodeButton.Texture.GIVE,button->{
			String serialized=ComponentHandler.extractComponentsToString(this.itemStack,onlyComponentChanges);
			if(!serialized.isEmpty()) copy("/give @s "+itemRegisterName+" "+serialized+" "+itemStack.getCount());
		}));
		giveButton.setSkipFocus(true);
		codeViewer=this.addRenderableWidget(new CodeView(this.font,width/2-205,height/2-111,160,224,(x,y,text)->copy(text)));
		codeViewer.loadFromItemStack(this.itemStack,onlyComponentChanges);
		String clipboard=Minecraft.getInstance().keyboardHandler.getClipboard();
		clipboardButton=this.addRenderableWidget(new TextButton(width/2-2,height/2+102,206,clipboard));
		clipboardButton.setColor(0xAAAAAA);
	}
	private void initItemInfo(ResourceLocation rawItemRegisterName,String itemRegisterName){
		if(MtclModClientConfig.DEBUG.get())
			this.addRenderableWidget(new Button(width/2+166,height/2-55,40,14,Component.literal("Reload"),button->{
				onClose();
				Minecraft.getInstance().setScreen(new ItemInfo(this.itemStack,this.parent));
			}));
		String itemDisplayName=itemStack.getHoverName().getString();
		TextButton itemDisplayNameButton=this.addRenderableWidget(new TextButton(width/2+45,height/2-108,156,itemDisplayName,button->copy(itemDisplayName)));
		itemDisplayNameButton.setUnderline(true);
		TextButton itemRegisterNameButton=this.addRenderableWidget(new TextButton(width/2+45,height/2-94,156,itemRegisterName,button->copy(itemRegisterName)));
		itemRegisterNameButton.setUnderline(true);
		itemRegisterNameButton.setColor(0x55FF55);
		String itemName=rawItemRegisterName.getPath();
		TextButton itemNameButton=this.addRenderableWidget(new TextButton(width/2+45,height/2-84,156,itemName,button->copy(itemName)));
		itemNameButton.setUnderline(true);
		itemNameButton.setColor(0x00AA00);
		String value="null";
		try{
			var container=ModList.get().getModContainerById(rawItemRegisterName.getNamespace());
			if(container.isPresent()) value=container.get().getModInfo().getDisplayName();
		}catch(Exception ignored){
		}
		String modDisplayName=value;
		TextButton modDisplayNameButton=this.addRenderableWidget(new TextButton(width/2+45,height/2-70,156,modDisplayName,button->copy(modDisplayName)));
		modDisplayNameButton.setUnderline(true);
		modDisplayNameButton.setColor(0x5555FF);
		String modName=rawItemRegisterName.getNamespace();
		TextButton modNameButton=this.addRenderableWidget(new TextButton(width/2+45,height/2-60,156,modName,button->copy(modName)));
		modNameButton.setUnderline(true);
		modNameButton.setColor(0x5555FF);
	}
	private void initTags(){
		int tabY=height/2-34;
		if(!this.itemTags.isEmpty()){
			itemTab=this.addRenderableWidget(new Tab(width/2-8,tabY,Component.literal("Item Tags"),new ItemStack(Items.IRON_INGOT),button->selectTab(TagType.ITEM)));
			itemTab.setHover("info."+MtclMod.MODID+".item_tags");
			tabY+=29;
		}
		if(!this.blockTags.isEmpty()){
			blockTab=this.addRenderableWidget(new Tab(width/2-8,tabY,Component.literal("Block Tags"),new ItemStack(Items.STONE),button->selectTab(TagType.BLOCK)));
			blockTab.setHover("info."+MtclMod.MODID+".block_tags");
			tabY+=29;
		}
		if(!this.fluidTags.isEmpty()){
			fluidTab=this.addRenderableWidget(new Tab(width/2-8,tabY,Component.literal("Fluid Tags"),new ItemStack(Items.WATER_BUCKET),button->selectTab(TagType.FLUID)));
			fluidTab.setHover("info."+MtclMod.MODID+".fluid_tags");
		}
		leftTag=this.addRenderableWidget(new Button(width/2+28,height/2-33,14,29,Component.literal("<"),button->previousTag()));
		leftTag.setSkipFocus(true);
		rightTag=this.addRenderableWidget(new Button(width/2+192,height/2-33,14,29,Component.literal(">"),button->nextTag()));
		rightTag.setSkipFocus(true);
		pageButton=this.addRenderableWidget(new TextButton(width/2+44,height/2-30,147,Component.empty()));
		pageButton.setCentered(true);
		idButton=this.addRenderableWidget(new TextButton(width/2+44,height/2-15,147,Component.empty(),button->{
			if(!currentTagId.isEmpty()) copy(currentTagId);
		}));
		idButton.setUnderline(true);
		idButton.setCentered(true);
		slotGrid=this.addRenderableWidget(new SlotGrid(width/2+28,height/2-2,9,5,(clickedStack,index)->Minecraft.getInstance().setScreen(new ItemInfo(clickedStack,Minecraft.getInstance().screen))));
		if(!this.itemTags.isEmpty()) selectTab(TagType.ITEM);
		else if(!this.blockTags.isEmpty()) selectTab(TagType.BLOCK);
		else if(!this.fluidTags.isEmpty()) selectTab(TagType.FLUID);
		else updateTagView();
	}
	@Override
	public void renderBackground(@NotNull GuiGraphics gui,int mouseX,int mouseY,float partialTick){
		super.renderBackground(gui,mouseX,mouseY,partialTick);
		RenderSystem.setShaderColor(1,1,1,1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		renderBackgroundCode(gui);
		renderBackgroundItemInfo(gui);
		renderBackgroundTags(gui);
		RenderSystem.disableBlend();
	}
	private void renderBackgroundCode(GuiGraphics gui){
		Window.create(gui,Renderer.Texture.TEXT,width/2-210,height/2-117,170,235);
		ResourceLocation codeTab=ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/code/tab.png");
		gui.blit(codeTab,width/2-40,height/2+86,0,0,32,28,32,28);
		gui.blit(codeTab,width/2-40,height/2+57,0,0,32,28,32,28);
		gui.blit(codeTab,width/2-40,height/2+28,0,0,32,28,32,28);
		Window.create(gui,Renderer.Texture.TEXT,width/2-9,height/2+95,219,21);
	}
	private void renderBackgroundItemInfo(GuiGraphics gui){
		Window.create(gui,Renderer.Texture.CLASSIC,width/2-38,height/2-117,248,80);
		Window.create(gui,Renderer.Texture.BOARD,width/2-34,height/2-113,72,72);
		Window.create(gui,Renderer.Texture.BOARD,width/2+40,height/2-113,166,72);
		gui.pose().pushPose();
		gui.pose().translate((float)(width-64)/2+2,(float)(height-64)/2-77,0F);
		gui.pose().scale(4,4,1);
		Lighting.setupForEntityInInventory();
		gui.renderItem(itemStack,0,0);
		gui.renderItemDecorations(Minecraft.getInstance().font,itemStack,0,0);
		gui.flush();
		Lighting.setupFor3DItems();
		gui.pose().popPose();
	}
	private void renderBackgroundTags(GuiGraphics gui){
		Window.create(gui,Renderer.Texture.CLASSIC,width/2+24,height/2-37,186,130);
		gui.fill(width/2+32,height/2-33,width/2+192,height/2-19,0xFFA1A1A1);
		gui.fill(width/2+32,height/2-18,width/2+192,height/2-4,0xFFA1A1A1);
	}
	private void copy(String copy){
		if(minecraft!=null) minecraft.keyboardHandler.setClipboard(copy);
		clipboardButton.setMessage(copy);
	}
	private void loadTags(){
		this.itemTags.clear();
		this.blockTags.clear();
		this.fluidTags.clear();
		BuiltInRegistries.ITEM.wrapAsHolder(this.itemStack.getItem()).tags().sorted(Comparator.comparing(tag->tag.location().toString())).forEach(this.itemTags::add);
		if(this.itemStack.getItem() instanceof BlockItem blockItem) BuiltInRegistries.BLOCK.wrapAsHolder(blockItem.getBlock()).tags().sorted(Comparator.comparing(tag->tag.location().toString())).forEach(this.blockTags::add);
		Fluid fluid=null;
		for(Fluid entry: BuiltInRegistries.FLUID){
			if(entry.isSource(entry.defaultFluidState())&&entry.getBucket().equals(this.itemStack.getItem())){
				fluid=entry;
				break;
			}
		}
		if(fluid==null){
			for(Fluid entry: BuiltInRegistries.FLUID){
				if(entry.getBucket()==this.itemStack.getItem()&&entry!=Fluids.EMPTY){
					fluid=entry;
					break;
				}
			}
		}
		if(fluid!=null) BuiltInRegistries.FLUID.wrapAsHolder(fluid).tags().sorted(Comparator.comparing(tag->tag.location().toString())).forEach(this.fluidTags::add);
	}
	private void selectTab(TagType tab){
		this.currentTabType=tab;
		this.tagIndex=0;
		if(this.itemTab!=null) this.itemTab.press(tab==TagType.ITEM);
		if(this.blockTab!=null) this.blockTab.press(tab==TagType.BLOCK);
		if(this.fluidTab!=null) this.fluidTab.press(tab==TagType.FLUID);
		updateTagView();
	}
	private void previousTag(){
		int count=getCurrentTagCount();
		if(count>1){
			this.tagIndex=(this.tagIndex-1+count)%count;
			this.updateTagView();
		}
	}
	private void nextTag(){
		int count=getCurrentTagCount();
		if(count>1){
			this.tagIndex=(this.tagIndex+1)%count;
			this.updateTagView();
		}
	}
	private int getCurrentTagCount(){
		return switch(this.currentTabType){
			case ITEM -> this.itemTags.size();
			case BLOCK -> this.blockTags.size();
			case FLUID -> this.fluidTags.size();
		};
	}
	private void updateTagView(){
		int count=getCurrentTagCount();
		if(count==0){
			this.leftTag.active=false;
			this.rightTag.active=false;
			this.pageButton.setText(Component.literal("0 / 0"));
			this.idButton.setText(Component.translatable("info."+MtclMod.MODID+".no_tags").withStyle(ChatFormatting.ITALIC));
			this.currentTagId="";
			if(this.slotGrid!=null) this.slotGrid.setItems(List.of());
			return;
		}
		this.leftTag.active=count>1;
		this.rightTag.active=count>1;
		if(this.tagIndex<0) this.tagIndex=0;
		if(this.tagIndex>=count) this.tagIndex=count-1;
		this.pageButton.setText(Component.literal((this.tagIndex+1)+" / "+count));
		List<ItemStack> items=List.of();
		switch(this.currentTabType){
			case ITEM -> {
				TagKey<Item> tag=this.itemTags.get(this.tagIndex);
				this.currentTagId=tag.location().toString();
				items=BuiltInRegistries.ITEM.getOrCreateTag(tag).stream()
						.map(Holder::value)
						.filter(item->item!=Items.AIR)
						.distinct()
						.map(ItemStack::new)
						.toList();
			}
			case BLOCK -> {
				TagKey<Block> tag=this.blockTags.get(this.tagIndex);
				this.currentTagId=tag.location().toString();
				items=BuiltInRegistries.BLOCK.getOrCreateTag(tag).stream()
						.map(Holder::value)
						.map(Block::asItem)
						.filter(item->item!=Items.AIR)
						.distinct()
						.map(ItemStack::new)
						.toList();
			}
			case FLUID -> {
				TagKey<Fluid> tag=this.fluidTags.get(this.tagIndex);
				this.currentTagId=tag.location().toString();
				items=BuiltInRegistries.FLUID.getOrCreateTag(tag).stream()
						.map(Holder::value)
						.filter(f->f.isSource(f.defaultFluidState()))
						.map(Fluid::getBucket)
						.filter(item->item!=Items.AIR)
						.distinct()
						.map(ItemStack::new)
						.toList();
			}
		}
		this.idButton.setText(Component.literal(this.currentTagId));
		if(this.slotGrid!=null) this.slotGrid.setItems(items);
	}
	@Override
	public void onClose(){
		Minecraft mc=Minecraft.getInstance();
		if(!Screen.hasShiftDown()) mc.setScreen(this.parent);
		else mc.setScreen(null);
	}
	@Override
	public boolean isPauseScreen(){
		return false;
	}
	@Override
	public boolean keyPressed(int key,int scan,int mods){
		if(key==GLFW.GLFW_KEY_ESCAPE||key==MtclModKeys.ITEM_INFO.getKey().getValue()){
			onClose();
			return true;
		}
		return super.keyPressed(key,scan,mods);
	}
}