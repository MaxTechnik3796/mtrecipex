package cz.maxtechnik.mtrecipex;

import com.mojang.logging.LogUtils;
import cz.maxtechnik.mtrecipex.recipe.SmithingExtraRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.function.Supplier;
@SuppressWarnings("unused")
@Mod(MTRecipexMod.MODID)
public class MTRecipexMod{
	public static final String MODID="mtrecipex";
	public static final Logger LOGGER=LogUtils.getLogger();
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS=DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER,MODID);
	public static final Supplier<RecipeSerializer<SmithingExtraRecipe>> SMITHING_EXTRA_SERIALIZER=RECIPE_SERIALIZERS.register("smithing_extra",()->SmithingExtraRecipe.Serializer.INSTANCE);
	public MTRecipexMod(IEventBus modEventBus){
		RECIPE_SERIALIZERS.register(modEventBus);
		modEventBus.addListener(this::commonSetup);
		NeoForge.EVENT_BUS.register(this);
		//MTRecipexModRegistry.addSmithingExtra("extra",new ItemStack(Items.SUGAR,3),new ItemStack(Items.STONE,64),new ItemStack(Items.STONE_HOE),new ItemStack(Items.NAME_TAG,5));
	}
	private void commonSetup(final FMLCommonSetupEvent event){
		LOGGER.info("MT-Recipex: Common Setup");
	}
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event){
		LOGGER.info("MT-Recipex: Server Starting");
	}
}
