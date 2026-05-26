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
		MTRecipexModRegistry.addCreateProcessing("crush_rose_bush",CreateRecipeType.CRUSHING,
				Blocks.ROSE_BUSH,250,
				CreateOutput.of(new ItemStack(Items.RED_DYE,4)),
				CreateOutput.of(new ItemStack(Items.GREEN_DYE,1),0.5F)
		);
		MTRecipexModRegistry.addCreateAdvanced("super_mixing_test",
				CreateRecipeType.MIXING,
				HeatLevel.HEATED,
				new ItemLike[]{Blocks.DIRT,Items.SUGAR},
				new FluidStack[]{new FluidStack(Fluids.WATER,1000)},
				new CreateOutput[]{CreateOutput.of(new ItemStack(Items.BLAZE_POWDER,2))},
				new FluidStack[]{new FluidStack(Fluids.LAVA,250)}
		);
		MTRecipexModRegistry.addCreateAdvanced("compact_items_to_diamond",
				CreateRecipeType.COMPACTING,
				HeatLevel.NONE,
				new ItemLike[]{Blocks.COBBLESTONE,Blocks.SAND,Blocks.GRAVEL},
				null,
				new CreateOutput[]{CreateOutput.of(new ItemStack(Items.DIAMOND))},
				null
		);
		// 1. UKÁZKA PLNIČKY (Filling)
		// Skleněná láhev se naplní 250mb lávy a vznikne Magma Cream
		MTRecipexModRegistry.addCreateFilling("fill_bottle_with_lava",
				Items.GLASS_BOTTLE,
				new FluidStack(Fluids.LAVA,250),
				CreateOutput.of(new ItemStack(Items.MAGMA_CREAM))
		);
		// 2. UKÁZKA MECHANICKÉHO DEPLOYERU (Deploying)
		// Kliknutím železným ingotem (held) na Cobblestone (base) vznikne s 80% šancí Iron Ore
		MTRecipexModRegistry.addCreateDeploying("deploy_iron_on_cobble",
				Blocks.COBBLESTONE,Items.IRON_INGOT,
				CreateOutput.of(new ItemStack(Items.IRON_ORE),0.8f)
		);
		// 3. UKÁZKA VELKÉHO MECHANICKÉHO CRAFTOVÁNÍ (Mechanical Crafting 5x3)
		// Umožní ti poskládat velký recept bez JSONu
		MTRecipexModRegistry.addCreateMechanicalCrafting("giant_sword",
				new ItemStack(Items.NETHERITE_SWORD),true,
				"  N  ",
				"  N  ",
				"  I  ",
				'N',Blocks.NETHERITE_BLOCK,
				'I',Blocks.IRON_BLOCK
		);
		// 4. MAJSTRŠTYK: SEKVENČNÍ SESTAVOVÁNÍ (Sequenced Assembly - Precision Mechanism)
		// Vstup: Železný ingot, Přechodný item: Zlatý ingot, Smyčky: 3x, Finální výstup: Diamant (100% šance)
		// Kroky v jedné smyčce: Zlisovat -> Oříznout -> Naplnit vodou -> Přimontovat Flint přes Deployer
		MTRecipexModRegistry.addCreateSequencedAssembly("precision_assembly_test",
				Items.IRON_INGOT,  // Startovní surovina
				Items.GOLD_INGOT,  // Incomplete transitional item
				3,                 // Počet opakování celé sekvence (loops)
				new CreateOutput[]{CreateOutput.of(new ItemStack(Items.DIAMOND))}, // Výstupní pole
				// Tady skládáš jednotlivé kroky za sebou:
				MTRecipexModRegistry.stepPressing(),
				MTRecipexModRegistry.stepCutting(100),
				MTRecipexModRegistry.stepFilling(new FluidStack(Fluids.WATER,100)),
				MTRecipexModRegistry.stepDeploying(Items.FLINT)
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
