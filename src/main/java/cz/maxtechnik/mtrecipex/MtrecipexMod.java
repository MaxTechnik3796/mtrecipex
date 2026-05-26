package cz.maxtechnik.mtrecipex;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
@Mod(MtrecipexMod.MODID)
@SuppressWarnings("removal")
public class MtrecipexMod{
	public static final String MODID="mtrecipex";
	public static final Logger LOGGER=LogUtils.getLogger();
	public MtrecipexMod(IEventBus modEventBus,ModContainer modContainer){
		modEventBus.addListener(this::commonSetup);
		NeoForge.EVENT_BUS.register(this);
		modContainer.registerConfig(ModConfig.Type.COMMON,MtrecipexModCommonConfig.SPEC);
	}
	private void commonSetup(final FMLCommonSetupEvent event){
		LOGGER.info("MT-Recipex: Common Setup");
	}
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event){
		LOGGER.info("MT-Recipex: Server Starting");
	}
	@EventBusSubscriber(modid=MODID, bus=EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
	public static class ClientModEvents{
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event){
			LOGGER.info("MT-Recipex: Client Setup");
		}
	}
}
