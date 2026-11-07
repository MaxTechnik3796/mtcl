package cz.maxtechnik.mtcl.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import cz.maxtechnik.mtcl.MtclMod;
import cz.maxtechnik.mtcl.gui.core.*;
import net.minecraft.ChatFormatting;
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
	private TextButton pageButton, idButton;
	private SlotGrid slotGrid;
	private int tagIndex=0;
	private String currentTagId="";
	private static final ResourceLocation TEXTURE=ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"textures/gui/item_info.png");
	public ItemInfo(ItemStack itemStack,Screen parent){
		super(Component.translatable("screen."+MtclMod.MODID+".item_info"));
		this.parent=parent;
		this.itemStack=itemStack;
	}
	@Override
	public void init(){
		super.init();
		this.loadTags();
		this.addRenderableWidget(new Button(width/2+166,height/2-55,40,14,Component.literal("Reload"),button->{
			onClose();
			if(minecraft!=null) minecraft.setScreen(new ItemInfo(this.itemStack,this.parent));
		}));
		slotGrid=this.addRenderableWidget(new SlotGrid(width/2+12,height/2+10,9,5,(clickedStack,index)->{
			System.out.println("Klik na item v mřížce: "+clickedStack.getHoverName().getString()+" (index: "+index+")");
		}));
		int tabY=height/2-34;
		if(!this.itemTags.isEmpty()){
			itemTab=this.addRenderableWidget(new Tab(width/2-41,tabY,Component.literal("Item Tags"),new ItemStack(Items.IRON_INGOT),button->selectTab(TagType.ITEM)));
			itemTab.setHover("info."+MtclMod.MODID+".item_tags");
			tabY+=30;
		}
		if(!this.blockTags.isEmpty()){
			blockTab=this.addRenderableWidget(new Tab(width/2-41,tabY,Component.literal("Block Tags"),new ItemStack(Items.STONE),button->selectTab(TagType.BLOCK)));
			blockTab.setHover("info."+MtclMod.MODID+".block_tags");
			tabY+=30;
		}
		if(!this.fluidTags.isEmpty()){
			fluidTab=this.addRenderableWidget(new Tab(width/2-41,tabY,Component.literal("Fluid Tags"),new ItemStack(Items.WATER_BUCKET),button->selectTab(TagType.FLUID)));
			fluidTab.setHover("info."+MtclMod.MODID+".fluid_tags");
		}
		leftTag=this.addRenderableWidget(new Button(width/2+12,height/2-20,14,14,Component.literal("<"),button->previousTag()));
		leftTag.setSkipFocus(true);
		rightTag=this.addRenderableWidget(new Button(width/2+177,height/2-20,14,14,Component.literal(">"),button->nextTag()));
		rightTag.setSkipFocus(true);
		int boxX=width/2+28;
		int boxWidth=147;
		pageButton=this.addRenderableWidget(new TextButton(boxX,height/2-17,boxWidth,Component.empty(),button->{
		}));
		pageButton.setCentered(true);
		idButton=this.addRenderableWidget(new TextButton(boxX,height/2-2,boxWidth,Component.empty(),button->{
			if(!currentTagId.isEmpty()) copy(currentTagId);
		}));
		idButton.setUnderline(true);
		idButton.setCentered(true);
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
		if(!this.itemTags.isEmpty()) selectTab(TagType.ITEM);
		else if(!this.blockTags.isEmpty()) selectTab(TagType.BLOCK);
		else if(!this.fluidTags.isEmpty()) selectTab(TagType.FLUID);
		else updateTagView();
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
		if(this.slotGrid!=null){
			this.slotGrid.setItems(items);
		}
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
		// Levý panel komponent
		Window.create(gui,Renderer.Texture.TEXT,width/2-210,height/2-117,170,235);
		// Horní panel informací o itemu
		Window.create(gui,Renderer.Texture.CLASSIC,width/2-38,height/2-117,248,80);
		Window.create(gui,Renderer.Texture.BOARD,width/2-34,height/2-113,72,72);
		Window.create(gui,Renderer.Texture.BOARD,width/2+40,height/2-113,150,72);
		// Zvětšený render itemu (4x)
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
		// Spodní panel tagů
		Window.create(gui,Renderer.Texture.CLASSIC,width/2-9,height/2-37,219,155);
		gui.fill(width/2+26,height/2-20,width/2+177,height/2-6,0xFFA1A1A1);
		gui.fill(width/2+26,height/2-5,width/2+177,height/2+9,0xFFA1A1A1);
		RenderSystem.disableBlend();
	}
	private void copy(String copy){
		if(minecraft!=null) minecraft.keyboardHandler.setClipboard(copy);
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