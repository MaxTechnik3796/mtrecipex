package cz.maxtechnik.mtrecipex;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

		MTRecipexRegistry.addShaped("dirt_to_diamond",
				new ItemStack(Items.DIAMOND),
				"DDD",
				"DDD",
				"DDD",
				'D', Blocks.DIRT
		);

		// Příklad 2: Crafting Beaconu (Majáku) z hlíny a železa
		MTRecipexRegistry.addShaped("dirt_beacon",
				new ItemStack(Blocks.BEACON,3),
				"DDD",
				"DID",
				"DDD",
				'D', Blocks.DIRT,
				'I', Items.IRON_INGOT
		);



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
