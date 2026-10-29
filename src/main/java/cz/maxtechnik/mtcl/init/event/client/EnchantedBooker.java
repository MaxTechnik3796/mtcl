package cz.maxtechnik.mtcl.init.event.client;

import cz.maxtechnik.mtcl.MtclMod;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
public class EnchantedBooker{
	public static void registerItemProperties(FMLClientSetupEvent event){
		event.enqueueWork(()->ItemProperties.register(
				Items.ENCHANTED_BOOK,
				ResourceLocation.fromNamespaceAndPath(MtclMod.MODID,"book_type"),
				(itemStack,level,entity,seed)->{
					ItemEnchantments enchantments=itemStack.get(DataComponents.STORED_ENCHANTMENTS);
					if(enchantments==null||enchantments.isEmpty()) return 0F;
					var entry=enchantments.entrySet().iterator().next();
					Holder<Enchantment> holder=entry.getKey();
					int enchantLevel=entry.getIntValue();
					ResourceLocation id=holder.unwrapKey().map(ResourceKey::location).orElse(null);
					if(id==null) return 0F;
					int baseType=switch(id.getPath()){
						case "aqua_affinity" -> 1;
						case "bane_of_arthropods" -> 2;
						case "breach" -> 3;
						case "blast_protection" -> 4;
						case "channeling" -> 5;
						case "binding_curse" -> 6;
						case "density" -> 7;
						case "depth_strider" -> 8;
						case "efficiency" -> 9;
						case "feather_falling" -> 10;
						case "fire_aspect" -> 11;
						case "fire_protection" -> 12;
						case "flame" -> 13;
						case "fortune" -> 14;
						case "frost_walker" -> 15;
						case "impaling" -> 16;
						case "infinity" -> 17;
						case "knockback" -> 18;
						case "looting" -> 19;
						case "loyalty" -> 20;
						case "luck_of_the_sea" -> 21;
						case "lure" -> 22;
						case "mending" -> 23;
						case "multishot" -> 24;
						case "piercing" -> 25;
						case "power" -> 26;
						case "projectile_protection" -> 27;
						case "protection" -> 28;
						case "punch" -> 29;
						case "quick_charge" -> 30;
						case "respiration" -> 31;
						case "riptide" -> 32;
						case "sharpness" -> 33;
						case "silk_touch" -> 34;
						case "smite" -> 35;
						case "soul_speed" -> 36;
						case "sweeping_edge" -> 37;
						case "swift_sneak" -> 38;
						case "thorns" -> 39;
						case "vanishing_curse" -> 40;
						case "unbreaking" -> 41;
						case "wind_burst" -> 42;
						default -> 0;
					};
					if(baseType==0) return 0F;
					return baseType+(enchantLevel/10F)+0.005F;
				}
		));
	}
}