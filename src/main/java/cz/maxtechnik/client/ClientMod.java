package cz.maxtechnik.client;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
@Mod(ClientMod.MODID)
@SuppressWarnings("removal")
public class ClientMod{
	public static final String MODID="client";
	public static final Logger LOGGER=LogUtils.getLogger();
	public ClientMod(IEventBus bus,ModContainer modContainer){
		bus.addListener(this::commonSetup);
		NeoForge.EVENT_BUS.register(this);
	}
	private void commonSetup(final FMLCommonSetupEvent event){
		LOGGER.info("Client: Common Setup");
	}
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event){
		LOGGER.info("Client: Server Starting");
	}
	@EventBusSubscriber(modid=MODID, bus=EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
	public static class ClientModEvents{
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event){
			LOGGER.info("Client: Client Setup");
		}
	}
}
