package cz.maxtechnik.mtrecipex;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;

public class MTRecipexRegistry{
	// Statická mapa, kam budeme ukládat naše virtuální recepty v RAM
	public static final Map<ResourceLocation, JsonElement> RECIPIES = new HashMap<>();

	/**
	 * Zaregistruje tvarovaný recept ve stylu verze 1.7.10
	 * @param name Unikátní název receptu (např. "my_generator_craft")
	 * @param result Výsledný předmět/blok a počet
	 * @param params Mřížka a klíče: "AAA", "ABA", 'A', Blocks.DIRT...
	 */
	public static void addShaped(String name,ItemStack result,Object... params) {
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "minecraft:crafting_shaped");
		recipeJson.addProperty("category", "misc");

		JsonArray patternArray = new JsonArray();
		int i = 0;

		// 1. Načteme textové řádky mřížky (v 1.7.10 to byly první 1 až 3 Stringy)
		while (i < params.length && params[i] instanceof String) {
			patternArray.add((String) params[i]);
			i++;
		}
		recipeJson.add("pattern", patternArray);

		// 2. Načteme dvojice: Character (znak) a ItemLike (blok nebo item)
		JsonObject keyObject = new JsonObject();
		while (i < params.length) {
			Character symbol = (Character) params[i];
			ItemLike ingredient = (ItemLike) params[i + 1];
			
			// Získáme registry ID předmětu (např. "minecraft:dirt")
			String ingredientId = BuiltInRegistries.ITEM.getKey(ingredient.asItem()).toString();

			JsonObject itemObject = new JsonObject();
			itemObject.addProperty("item", ingredientId);
			keyObject.add(symbol.toString(), itemObject);
			
			i += 2; // Skočíme na další dvojici
		}
		recipeJson.add("key", keyObject);

		// 3. Dosadíme výsledek
		JsonObject resultObject = new JsonObject();
		String resultId = BuiltInRegistries.ITEM.getKey(result.getItem()).toString();
		resultObject.addProperty("id", resultId);
		resultObject.addProperty("count", result.getCount());
		recipeJson.add("result", resultObject);

		// Uložíme do naší paměti pod modid "mtrecipex"
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MtrecipexMod.MODID, name), recipeJson);
	}

	// Metoda, kterou zavolá Mixin, aby získal všechny recepty
	public static Map<ResourceLocation, JsonElement> getVirtualRecipes() {
		return RECIPIES;
	}
}