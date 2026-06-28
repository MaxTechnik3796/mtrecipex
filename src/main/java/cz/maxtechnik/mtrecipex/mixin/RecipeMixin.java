package cz.maxtechnik.mtrecipex.mixin;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static cz.maxtechnik.mtrecipex.MTRecipexMod.LOGGER;
import static cz.maxtechnik.mtrecipex.MTRecipexModRegistry.getVirtualRecipes;
@Mixin(RecipeManager.class)
public class RecipeMixin{
	@Inject(method="apply*", at=@At("HEAD"))
	private void injectVirtualRecipes(Map<ResourceLocation,JsonElement> map,ResourceManager resourceManager,ProfilerFiller profiler,CallbackInfo ci){
		LOGGER.info("Applying Virtual Recipes...");
		LOGGER.info("Jsons of Virtual Recipes: {}",getVirtualRecipes());
		map.putAll(getVirtualRecipes());
	}
}