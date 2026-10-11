package cz.maxtechnik.mtcl;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import java.awt.*;
@Mod(MtclMod.MODID)
@SuppressWarnings("removal")
public class MtclMod{
	public static final String MODID="mtcl";
	public static final Logger LOGGER=LogUtils.getLogger();
	public MtclMod(IEventBus bus){
		bus.addListener(this::commonSetup);
		NeoForge.EVENT_BUS.register(this);
	}
	private void commonSetup(final FMLCommonSetupEvent event){
		LOGGER.info("MT-Client: Common Setup");
	}
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event){
		LOGGER.info("MT-Client: Server Starting");
	}
	@EventBusSubscriber(modid=MODID, bus=EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
	public static class ClientModEvents{
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event){
			LOGGER.info("MT-Client: Client Setup");
			if(ModList.get().isLoaded("create")) NeoForge.EVENT_BUS.register(AutoFrogportHandler.class);
		}
	}
	public static Component toggleMessage(String message,boolean state){
		return Component.translatable("info."+MODID+"."+message).append(": ").append(Component.translatable(state?"info."+MODID+".on":"info."+MODID+".off"));
	}
}
