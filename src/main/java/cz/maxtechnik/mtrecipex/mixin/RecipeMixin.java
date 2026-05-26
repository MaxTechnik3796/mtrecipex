package cz.maxtechnik.mtrecipex.mixin;

import com.google.gson.JsonElement;
import cz.maxtechnik.mtrecipex.MTRecipexRegistry;
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
		System.out.println("====== MIXIN SPUŠTĚN: IMPORTUJI 1.7.10 STYLE RECEPTY ======");
		// Prásk! Vezmeme celou mapu z našeho registru a nasypeme ji do Minecraftu
		map.putAll(MTRecipexRegistry.getVirtualRecipes());
	}
}