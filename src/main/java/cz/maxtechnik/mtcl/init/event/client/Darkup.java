package cz.maxtechnik.mtcl.init.event.client;

import cz.maxtechnik.mtcl.MtclMod;
import cz.maxtechnik.mtcl.MtclModClientConfig;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;

import java.util.List;
@SuppressWarnings("removal")
@EventBusSubscriber(modid=MtclMod.MODID, bus=EventBusSubscriber.Bus.GAME, value=Dist.CLIENT)
public class Darkup{
	private static final List<String> BLOCKED_ANNOUNCEMENTS=List.of(
			"nezapomeň si přečíst pravidla na webu www.darkup",
			"nevíš si rady? navštiv naši wiki: wiki.darkup.cz",
			"viděl jsi hráče porušovat pravidla? nahlaš ho příkazem: /report <hráč>",
			"přijď si pokecat na náš discord: discord.darkup.cz",
			"hlasuj na survivalu a oneblocku pro server pomocí /vote a získej odměnu."
	);
	@SubscribeEvent
	public static void onChatMessage(ClientChatReceivedEvent event){
		String message=event.getMessage().getString().replaceAll("(?i)§[0-9a-fk-or]","").trim().toLowerCase();
		if(!MtclModClientConfig.DARKUP_CHAT_FILTER.get()) return;
		if(message.isEmpty()){
			event.setCanceled(true);
			return;
		}
		if(message.contains("[oznámení]")){
			for(String blocked: BLOCKED_ANNOUNCEMENTS)
				if(message.contains(blocked)){
					event.setCanceled(true);
					return;
				}
		}
	}
	public static void registerItemProperties(FMLClientSetupEvent event){
		event.enqueueWork(()->ItemProperties.register(Items.TRIPWIRE_HOOK,
				ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"key_type"),
				(itemStack,level,entity,seed)->{
					CustomData customData=itemStack.get(DataComponents.CUSTOM_DATA);
					if(customData==null) return 0F;
					CompoundTag tag=customData.copyTag();
					if(!tag.contains("PublicBukkitValues",Tag.TAG_COMPOUND)) return 0F;
					CompoundTag bukkit=tag.getCompound("PublicBukkitValues");
					if(!bukkit.contains("nexo:id",Tag.TAG_STRING)) return 0F;
					return switch(bukkit.getString("nexo:id")){
						case "crates_vote_key" -> 1F;
						case "crates_pinata_key" -> 2F;
						case "crates_event_key" -> 3F;
						case "crates_vehicle_key" -> 4F;
						case "littlecatset_littlecat_key" -> 5F;//azur key
						case "littlecapybaraset_littlecapybara_key" -> 6F;//kapybara key
						case "aurion_set_key" -> 7F;//hvezda key
						default -> 0F;
					};
				}
		));
		event.enqueueWork(()->ItemProperties.register(Items.PAPER,
				ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"item_type"),
				(itemStack,level,entity,seed)->{
					CustomData customData=itemStack.get(DataComponents.CUSTOM_DATA);
					if(customData==null) return 0F;
					CompoundTag tag=customData.copyTag();
					if(!tag.contains("PublicBukkitValues",Tag.TAG_COMPOUND)) return 0F;
					CompoundTag bukkit=tag.getCompound("PublicBukkitValues");
					if(!bukkit.contains("nexo:id",Tag.TAG_STRING)) return 0F;
					return switch(bukkit.getString("nexo:id")){
						case "afk_fragment" -> 1F;
						case "plny_splashfest_set_key" -> 2F;//bazen key
						case "petragoras_mystic_key" -> 3F;//les key
						case "divine_zephyr_key" -> 4F;//morph key
						case "halloween26_key" -> 5F;//krev key
						default -> 0F;
					};
				}
		));
	}
}
