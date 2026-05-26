package cz.maxtechnik.mtrecipex;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import org.slf4j.Logger;
@Mod(MTRecipexMod.MODID)
@SuppressWarnings("removal")
public class MTRecipexMod{
	public static final String MODID="mtrecipex";
	public static final Logger LOGGER=LogUtils.getLogger();
	public MTRecipexMod(IEventBus modEventBus){
		modEventBus.addListener(this::commonSetup);
		NeoForge.EVENT_BUS.register(this);
		MTRecipexModRegistry.addShaped("dirt_to_diamond",new ItemStack(Items.DIAMOND),"DDD","DDD","DDD",'D',Blocks.DIRT);
		MTRecipexModRegistry.addShaped("dirt_beacon",new ItemStack(Blocks.BEACON,3),"DDD","DID","DDD",'D',Blocks.DIRT,'I',Items.IRON_INGOT);
		MTRecipexModRegistry.addShapeless("clay_flint_to_iron",new ItemStack(Items.IRON_INGOT,2),Blocks.CLAY,Items.FLINT);


		MTRecipexModRegistry.addCreateProcessing("crush_rose_bush", CreateRecipeType.CRUSHING,
				Blocks.ROSE_BUSH, 250,
				CreateOutput.of(new ItemStack(Items.RED_DYE, 4)),
				CreateOutput.of(new ItemStack(Items.GREEN_DYE, 1), 0.5F)
		);

		MTRecipexModRegistry.addCreateAdvanced("super_mixing_test",
				CreateRecipeType.MIXING,
				HeatLevel.HEATED,

				new ItemLike[]{ Blocks.DIRT, Items.SUGAR },

				new FluidStack[]{new FluidStack(Fluids.WATER, 1000)},

				new CreateOutput[]{ CreateOutput.of(new ItemStack(Items.BLAZE_POWDER, 2)) },

				new FluidStack[]{new FluidStack(Fluids.LAVA, 250)}
		);

		MTRecipexModRegistry.addCreateAdvanced("compact_items_to_diamond",
				CreateRecipeType.COMPACTING,
				HeatLevel.NONE,
				new ItemLike[]{ Blocks.COBBLESTONE, Blocks.SAND, Blocks.GRAVEL },
				null,
				new CreateOutput[]{ CreateOutput.of(new ItemStack(Items.DIAMOND)) },
				null
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
