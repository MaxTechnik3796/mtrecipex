package cz.maxtechnik.mtrecipex.mixin;

import com.google.gson.JsonElement;
import cz.maxtechnik.mtrecipex.MTRecipexMod;
import cz.maxtechnik.mtrecipex.MTRecipexModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
@Mixin(RecipeManager.class)
public class RecipeMixin{
	@Inject(method="apply*", at=@At("HEAD"))
	private void injectVirtualRecipes(Map<ResourceLocation,JsonElement> map,ResourceManager resourceManager,ProfilerFiller profiler,CallbackInfo ci){
		MTRecipexMod.LOGGER.info("Applying Virtual Recipes");
		map.putAll(MTRecipexModRegistry.getVirtualRecipes());
	}
}