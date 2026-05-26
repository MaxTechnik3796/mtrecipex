package cz.maxtechnik.mtrecipex;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
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
	public MtrecipexMod(IEventBus modEventBus){
		modEventBus.addListener(this::commonSetup);
		NeoForge.EVENT_BUS.register(this);
		MTRecipexRegistry.addShaped("dirt_to_diamond",
				new ItemStack(Items.DIAMOND),
				"DDD",
				"DDD",
				"DDD",
				'D',Blocks.DIRT
		);
		// Příklad 2: Crafting Beaconu (Majáku) z hlíny a železa
		MTRecipexRegistry.addShaped("dirt_beacon",
				new ItemStack(Blocks.BEACON,3),
				"DDD",
				"DID",
				"DDD",
				'D',Blocks.DIRT,
				'I',Items.IRON_INGOT
		);
		MTRecipexRegistry.addShapeless("clay_flint_to_iron",
				new ItemStack(Items.IRON_INGOT,2),
				Blocks.CLAY,Items.FLINT
		);
		// VARIANT 1: Crushing (Drcení) s vlastním ČASEM (250 ticků), bez tepla
		MTRecipexRegistry.addCreateProcessing("crush_rose_bush","crushing",
				Blocks.ROSE_BUSH,
				250, // <-- Processing Time v tlacích (ticks)
				CreateOutput.of(new ItemStack(Items.RED_DYE,4)),
				CreateOutput.of(new ItemStack(Items.GREEN_DYE,1),0.5f)
		);
		// VARIANT 2: Mixing (Míchání) s ČASEM (400 ticků) a ZAHŘÁTÝM hořákem ("heated")
		// Možnosti tepla: "none", "heated", "superheated"
		MTRecipexRegistry.addCreateProcessing("blaze_mixing_test","mixing",
				Blocks.DIRT,
				HeatLevel.HEATED,   // <-- Heat Requirement level
				CreateOutput.of(new ItemStack(Items.BLAZE_POWDER,2)),
				CreateOutput.of(new ItemStack(Items.MAGMA_CREAM,1),0.25f)
		);
		// Klasický způsob bez času a tepla stále funguje též:
		MTRecipexRegistry.addCreateProcessing("simple_mill","milling",
				Blocks.COBBLESTONE,
				CreateOutput.of(new ItemStack(Blocks.SAND))
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
