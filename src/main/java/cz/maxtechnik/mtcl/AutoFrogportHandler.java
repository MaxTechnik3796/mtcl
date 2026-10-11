package cz.maxtechnik.mtcl;

import com.simibubi.create.content.logistics.packagePort.PackagePortScreen;
import com.simibubi.create.content.logistics.packagePort.frogport.FrogportBlockEntity;
import com.simibubi.create.foundation.gui.widget.IconButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.lang.reflect.Field;

import static cz.maxtechnik.mtcl.MtclModKeys.AUTO_FROGPORT;
public class AutoFrogportHandler{
	public static boolean active=false;
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Post event){
		Minecraft mc=Minecraft.getInstance();
		if(mc.player==null) return;
		while(AUTO_FROGPORT.consumeClick()){
			active=!active;
			mc.player.displayClientMessage(MtclMod.toggleMessage("auto_frogport",active),true);
		}
	}
	@SubscribeEvent
	public static void onScreenInit(ScreenEvent.Init.Post event){
		if(!active) return;
		if(event.getScreen() instanceof PackagePortScreen screen){
			if(!(screen.getMenu().contentHolder instanceof FrogportBlockEntity)) return;
			try{
				Field addressBoxField=PackagePortScreen.class.getDeclaredField("addressBox");
				addressBoxField.setAccessible(true);
				EditBox addressBox=(EditBox)addressBoxField.get(screen);
				Field acceptField=PackagePortScreen.class.getDeclaredField("acceptPackages");
				acceptField.setAccessible(true);
				IconButton acceptPackages=(IconButton)acceptField.get(screen);
				Field dontAcceptField=PackagePortScreen.class.getDeclaredField("dontAcceptPackages");
				dontAcceptField.setAccessible(true);
				IconButton dontAcceptPackages=(IconButton)dontAcceptField.get(screen);
				addressBox.setValue("-");
				acceptPackages.green=false;
				dontAcceptPackages.green=true;
				Player player=Minecraft.getInstance().player;
				if(player==null) return;
				player.closeContainer();
			}catch(Exception ignored){
			}
		}
	}
}