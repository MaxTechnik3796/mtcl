package cz.maxtechnik.mtcl.init.other;

import com.mojang.blaze3d.platform.InputConstants;
import cz.maxtechnik.mtcl.MtclMod;
import cz.maxtechnik.mtcl.gui.ItemInfo;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
@SuppressWarnings("removal")
@EventBusSubscriber(modid=MtclMod.MODID, bus=EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class MtclModKeys{
	public static final KeyMapping
			BACKPACK=registerKey("key."+MtclMod.MODID+".backpack"),
	 		AUTO_FROGPORT=registerKey("key."+MtclMod.MODID+".auto_frogport"),
			ITEM_INFO=registerKey("key."+MtclMod.MODID+".item_info");
	@SubscribeEvent
	public static void onKeyRegister(RegisterKeyMappingsEvent event){
		KeyMapping[] keys={BACKPACK,AUTO_FROGPORT,ITEM_INFO};
		for(KeyMapping key: keys) event.register(key);
	}
	@EventBusSubscriber(modid=MtclMod.MODID, value=Dist.CLIENT)
	public static class KeyInputHandler{
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event){
			Minecraft mc=Minecraft.getInstance();
			if(mc.player==null) return;
			while(BACKPACK.consumeClick()){
				if(mc.player.isShiftKeyDown()) mc.player.connection.sendCommand("enderchest");
				else mc.player.connection.sendCommand("backpack");
			}
			while(ITEM_INFO.consumeClick()) mc.setScreen(new ItemInfo(mc.player.getMainHandItem(),mc.screen));
		}
	}
	private static KeyMapping registerKey(String name){
		return new KeyMapping(name,InputConstants.Type.KEYSYM,InputConstants.UNKNOWN.getValue(),"key.categories.mtcl");
	}
}
