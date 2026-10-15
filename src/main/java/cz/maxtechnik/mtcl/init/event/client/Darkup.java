package cz.maxtechnik.mtcl.init.event.client;

import cz.maxtechnik.mtcl.MtclMod;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
public class Darkup{
	public static void registerKeysTextures(FMLClientSetupEvent event){
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
						case "littlecatset_littlecat_key"->5F;//azur
						case "littlecapybaraset_littlecapybara_key"->6F;//kapybara
						case "aurion_set_key"->7F;//hvězdný
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
					return bukkit.getString("nexo:id").equals("afk_fragment")?1F:0F;
				}
		));
	}
}
