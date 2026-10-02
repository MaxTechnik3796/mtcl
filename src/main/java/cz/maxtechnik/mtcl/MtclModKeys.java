package cz.maxtechnik.mtcl;

import com.mojang.blaze3d.platform.InputConstants;
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
	public static final String CATEGORY="key.categories.mtcl";
	static InputConstants.Type INPUT=InputConstants.Type.KEYSYM;
	public static final KeyMapping BACKPACK=new KeyMapping(
			"key.mtcl.backpack",INPUT,
			InputConstants.UNKNOWN.getValue(),
			CATEGORY
	);
	@SubscribeEvent
	public static void onKeyRegister(RegisterKeyMappingsEvent event){
		event.register(BACKPACK);
	}
	@EventBusSubscriber(modid=MtclMod.MODID, value=Dist.CLIENT)
	public static class KeyInputHandler{
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event){
			Minecraft mc=Minecraft.getInstance();
			if(mc.player==null) return;
			while(BACKPACK.consumeClick()){
				if(mc.player.isCrouching()) mc.player.connection.sendCommand("enderchest");
				else mc.player.connection.sendCommand("backpack");
			}
		}
	}
}
