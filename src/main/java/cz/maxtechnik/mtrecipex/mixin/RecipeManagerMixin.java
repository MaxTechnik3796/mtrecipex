package cz.maxtechnik.mtrecipex.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
public class RecipeManagerMixin{

	@Inject(method ="apply*", at = @At("HEAD"))
	private void injectVirtualRecipes(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
		System.out.println("====== MIXIN SPUŠTĚN: VSTŘIKUJI VIRTUÁLNÍ RECEPTY DO RAM ======");

		// --- 1. VYTVOŘENÍ RECEPTU PRO STEAM GENERATOR ČISTĚ V RAM ---
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "minecraft:crafting_shaped");
		recipeJson.addProperty("category", "misc");

		// Mřížka (Pattern)
		JsonArray pattern = new JsonArray();
		pattern.add("DDD");
		pattern.add("DID");
		pattern.add("DDD");
		recipeJson.add("pattern", pattern);

		// Klíče (Key)
		JsonObject key = new JsonObject();
		JsonObject dKey = new JsonObject();
		dKey.addProperty("item", "minecraft:dirt");
		JsonObject iKey = new JsonObject();
		iKey.addProperty("item", "minecraft:iron_ingot");
		key.add("D", dKey);
		key.add("I", iKey);
		recipeJson.add("key", key);

		// Výsledek (Result) - v 1.21.1 se používá "id" namísto "item"
		JsonObject result = new JsonObject();
		result.addProperty("id", "minecraft:diamond");
		result.addProperty("count", 1);
		recipeJson.add("result", result);

		// Vložíme recept přímo do načítací mapy Minecraftu pod unikátním ID
		map.put(ResourceLocation.fromNamespaceAndPath("random", "diamond"), recipeJson);
	}
}