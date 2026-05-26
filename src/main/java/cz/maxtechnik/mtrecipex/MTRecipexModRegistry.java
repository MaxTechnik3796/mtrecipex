package cz.maxtechnik.mtrecipex;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class MTRecipexModRegistry {
	public static final Map<ResourceLocation, JsonElement> RECIPIES = new HashMap<>();

	/**
	 * 1. SHAPED RECEPT (1.7.10 Styl s mřížkou)
	 */
	public static void addShaped(String name, ItemStack result, Object... params) {
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "minecraft:crafting_shaped");
		recipeJson.addProperty("category", "misc");
		JsonArray patternArray = new JsonArray();
		int i = 0;
		while (i < params.length && params[i] instanceof String) {
			patternArray.add((String) params[i]);
			i++;
		}
		recipeJson.add("pattern", patternArray);
		JsonObject keyObject = new JsonObject();
		while (i < params.length) {
			Character symbol = (Character) params[i];
			ItemLike ingredient = (ItemLike) params[i + 1];
			String ingredientId = BuiltInRegistries.ITEM.getKey(ingredient.asItem()).toString();
			JsonObject itemObject = new JsonObject();
			itemObject.addProperty("item", ingredientId);
			keyObject.add(symbol.toString(), itemObject);
			i += 2;
		}
		recipeJson.add("key", keyObject);
		JsonObject resultObject = new JsonObject();
		String resultId = BuiltInRegistries.ITEM.getKey(result.getItem()).toString();
		resultObject.addProperty("id", resultId);
		resultObject.addProperty("count", result.getCount());
		recipeJson.add("result", resultObject);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID, name), recipeJson);
	}

	/**
	 * 2. SHAPELESS RECEPT (Beztvarý)
	 */
	public static void addShapeless(String name, ItemStack result, Object... ingredients) {
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "minecraft:crafting_shapeless");
		recipeJson.addProperty("category", "misc");
		JsonArray ingredientsArray = new JsonArray();
		for (Object ing : ingredients) {
			if (ing instanceof ItemLike itemLike) {
				JsonObject ingObj = new JsonObject();
				ingObj.addProperty("item", BuiltInRegistries.ITEM.getKey(itemLike.asItem()).toString());
				ingredientsArray.add(ingObj);
			}
		}
		recipeJson.add("ingredients", ingredientsArray);
		JsonObject resultObject = new JsonObject();
		resultObject.addProperty("id", BuiltInRegistries.ITEM.getKey(result.getItem()).toString());
		resultObject.addProperty("count", result.getCount());
		recipeJson.add("result", resultObject);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID, name), recipeJson);
	}

	/**
	 * 3A. ZÁKLADNÍ CREATE PROCESSING (Milling, Pressing, Sandpaper Polishing, Haunting, Splashing)
	 */
	public static void addCreateProcessing(String name, CreateRecipeType type, ItemLike ingredient, CreateOutput... outputs) {
		addCreateProcessing(name, type, ingredient, -1, outputs);
	}

	/**
	 * 3B. CREATE PROCESSING S ČASEM (Crushing, Cutting)
	 */
	public static void addCreateProcessing(String name, CreateRecipeType type, ItemLike ingredient, int processingTime, CreateOutput... outputs) {
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "create:" + type.id);
		JsonArray ingredientsArray = new JsonArray();
		JsonObject ingObj = new JsonObject();
		ingObj.addProperty("item", BuiltInRegistries.ITEM.getKey(ingredient.asItem()).toString());
		ingredientsArray.add(ingObj);
		recipeJson.add("ingredients", ingredientsArray);

		JsonArray resultsArray = new JsonArray();
		for (CreateOutput out : outputs) {
			JsonObject resObj = new JsonObject();
			// OPRAVA 1.21.1: Výstupní klíč změněn na "id"
			resObj.addProperty("id", BuiltInRegistries.ITEM.getKey(out.stack().getItem()).toString());
			resObj.addProperty("count", out.stack().getCount());
			if (out.chance() < 1F) {
				resObj.addProperty("chance", out.chance());
			}
			resultsArray.add(resObj);
		}
		recipeJson.add("results", resultsArray);
		if (processingTime > 0) {
			recipeJson.addProperty("processingTime", processingTime);
		}
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID, name), recipeJson);
	}

	/**
	 * 4. POKROČILÝ CREATE RECEPT (Mixing, Compacting)
	 */
	public static void addCreateAdvanced(String name, CreateRecipeType type, HeatLevel heat, ItemLike[] itemInputs, FluidStack[] fluidInputs, CreateOutput[] itemOutputs, FluidStack[] fluidOutputs) {
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "create:" + type.id);

		// Vstupy si ponechávají klíče "item" a "fluid"
		JsonArray ingredientsArray = new JsonArray();
		if (itemInputs != null) {
			for (ItemLike item : itemInputs) {
				JsonObject obj = new JsonObject();
				obj.addProperty("item", BuiltInRegistries.ITEM.getKey(item.asItem()).toString());
				ingredientsArray.add(obj);
			}
		}
		if (fluidInputs != null) {
			for (FluidStack fluid : fluidInputs) {
				JsonObject obj = new JsonObject();
				obj.addProperty("fluid", BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());
				obj.addProperty("amount", fluid.getAmount());
				ingredientsArray.add(obj);
			}
		}
		recipeJson.add("ingredients", ingredientsArray);

		// Výstupy (Results) se mění na "id"
		JsonArray resultsArray = new JsonArray();
		if (itemOutputs != null) {
			for (CreateOutput out : itemOutputs) {
				JsonObject obj = new JsonObject();
				// OPRAVA 1.21.1: Item výstup používá "id"
				obj.addProperty("id", BuiltInRegistries.ITEM.getKey(out.stack().getItem()).toString());
				obj.addProperty("count", out.stack().getCount());
				if (out.chance() < 1.0f) {
					obj.addProperty("chance", out.chance());
				}
				resultsArray.add(obj);
			}
		}
		if (fluidOutputs != null) {
			for (FluidStack fluid : fluidOutputs) {
				JsonObject obj = new JsonObject();
				// OPRAVA 1.21.1: Fluid výstup používá také "id"
				obj.addProperty("id", BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());
				obj.addProperty("amount", fluid.getAmount());
				resultsArray.add(obj);
			}
		}
		recipeJson.add("results", resultsArray);
		if (heat != null && heat != HeatLevel.NONE) {
			recipeJson.addProperty("heatRequirement", heat.lvl);
		}
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID, name), recipeJson);
	}

	/**
	 * 5. PLNIČKA (Filling)
	 */
	public static void addCreateFilling(String name, ItemLike itemInput, FluidStack fluidInput, CreateOutput itemOutput) {
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "create:filling");
		JsonArray ingredientsArray = new JsonArray();
		JsonObject itemObj = new JsonObject();
		itemObj.addProperty("item", BuiltInRegistries.ITEM.getKey(itemInput.asItem()).toString());
		ingredientsArray.add(itemObj);
		JsonObject fluidObj = new JsonObject();
		fluidObj.addProperty("fluid", BuiltInRegistries.FLUID.getKey(fluidInput.getFluid()).toString());
		fluidObj.addProperty("amount", fluidInput.getAmount());
		ingredientsArray.add(fluidObj);
		recipeJson.add("ingredients", ingredientsArray);

		JsonArray resultsArray = new JsonArray();
		JsonObject resObj = new JsonObject();
		// OPRAVA 1.21.1: Výstup změněn na "id"
		resObj.addProperty("id", BuiltInRegistries.ITEM.getKey(itemOutput.stack().getItem()).toString());
		resObj.addProperty("count", itemOutput.stack().getCount());
		if (itemOutput.chance() < 1.0f) resObj.addProperty("chance", itemOutput.chance());
		resultsArray.add(resObj);
		recipeJson.add("results", resultsArray);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID, name), recipeJson);
	}

	/**
	 * 6. VYPRAZDŇOVAČ (Emptying)
	 */
	public static void addCreateEmptying(String name, ItemLike itemInput, CreateOutput itemOutput, FluidStack fluidOutput) {
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "create:emptying");
		JsonArray ingredientsArray = new JsonArray();
		JsonObject itemObj = new JsonObject();
		itemObj.addProperty("item", BuiltInRegistries.ITEM.getKey(itemInput.asItem()).toString());
		ingredientsArray.add(itemObj);
		recipeJson.add("ingredients", ingredientsArray);

		JsonArray resultsArray = new JsonArray();
		if (itemOutput != null) {
			JsonObject resObj = new JsonObject();
			// OPRAVA 1.21.1: Změněno na "id"
			resObj.addProperty("id", BuiltInRegistries.ITEM.getKey(itemOutput.stack().getItem()).toString());
			resObj.addProperty("count", itemOutput.stack().getCount());
			resultsArray.add(resObj);
		}
		JsonObject fluidObj = new JsonObject();
		// OPRAVA 1.21.1: Fluid výstup změněn na "id"
		fluidObj.addProperty("id", BuiltInRegistries.FLUID.getKey(fluidOutput.getFluid()).toString());
		fluidObj.addProperty("amount", fluidOutput.getAmount());
		resultsArray.add(fluidObj);
		recipeJson.add("results", resultsArray);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID, name), recipeJson);
	}

	/**
	 * 7. MECHANICKÝ DEPLOYER (Deploying)
	 */
	public static void addCreateDeploying(String name, ItemLike baseInput, ItemLike heldItemInput, CreateOutput... outputs) {
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "create:deploying");
		JsonArray ingredientsArray = new JsonArray();
		JsonObject baseObj = new JsonObject();
		baseObj.addProperty("item", BuiltInRegistries.ITEM.getKey(baseInput.asItem()).toString());
		ingredientsArray.add(baseObj);
		JsonObject heldObj = new JsonObject();
		heldObj.addProperty("item", BuiltInRegistries.ITEM.getKey(heldItemInput.asItem()).toString());
		ingredientsArray.add(heldObj);
		recipeJson.add("ingredients", ingredientsArray);

		JsonArray resultsArray = new JsonArray();
		for (CreateOutput out : outputs) {
			JsonObject resObj = new JsonObject();
			// OPRAVA 1.21.1: Změněno na "id"
			resObj.addProperty("id", BuiltInRegistries.ITEM.getKey(out.stack().getItem()).toString());
			resObj.addProperty("count", out.stack().getCount());
			if (out.chance() < 1.0f) resObj.addProperty("chance", out.chance());
			resultsArray.add(resObj);
		}
		recipeJson.add("results", resultsArray);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID, name), recipeJson);
	}

	/**
	 * 8. RUČNÍ NANÁŠENÍ (Item Application)
	 */
	public static void addCreateItemApplication(String name, ItemLike baseBlock, ItemLike heldItem, ItemStack result) {
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "create:item_application");
		JsonArray ingredientsArray = new JsonArray();
		JsonObject baseObj = new JsonObject();
		baseObj.addProperty("item", BuiltInRegistries.ITEM.getKey(baseBlock.asItem()).toString());
		ingredientsArray.add(baseObj);
		JsonObject heldObj = new JsonObject();
		heldObj.addProperty("item", BuiltInRegistries.ITEM.getKey(heldItem.asItem()).toString());
		ingredientsArray.add(heldObj);
		recipeJson.add("ingredients", ingredientsArray);

		JsonArray resultsArray = new JsonArray();
		JsonObject resObj = new JsonObject();
		// OPRAVA 1.21.1: Změněno na "id"
		resObj.addProperty("id", BuiltInRegistries.ITEM.getKey(result.getItem()).toString());
		resObj.addProperty("count", result.getCount());
		resultsArray.add(resObj);
		recipeJson.add("results", resultsArray);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID, name), recipeJson);
	}

	/**
	 * 9. MECHANICKÉ CRAFTOVÁNÍ (Mechanical Crafting)
	 */
	public static void addCreateMechanicalCrafting(String name, ItemStack result,boolean acceptMirrored, Object... params) {
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "create:mechanical_crafting");
		JsonArray patternArray = new JsonArray();
		int i = 0;
		while (i < params.length && params[i] instanceof String) {
			patternArray.add((String) params[i]);
			i++;
		}
		recipeJson.add("pattern", patternArray);
		JsonObject keyObject = new JsonObject();
		while (i < params.length) {
			Character symbol = (Character) params[i];
			ItemLike ingredient = (ItemLike) params[i + 1];
			String ingredientId = BuiltInRegistries.ITEM.getKey(ingredient.asItem()).toString();
			JsonObject itemObject = new JsonObject();
			itemObject.addProperty("item", ingredientId);
			keyObject.add(symbol.toString(), itemObject);
			i += 2;
		}
		recipeJson.add("key", keyObject);
		JsonObject resultObject = new JsonObject();
		String resultId = BuiltInRegistries.ITEM.getKey(result.getItem()).toString();
		resultObject.addProperty("id", resultId);
		resultObject.addProperty("count", result.getCount());
		recipeJson.add("result", resultObject);
		recipeJson.addProperty("accept_mirrored",acceptMirrored);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID, name), recipeJson);
	}

	/**
	 * 10. SEKVENČNÍ SESTAVOVÁNÍ (Sequenced Assembly)
	 */
	public static void addCreateSequencedAssembly(String name, ItemLike startItem, ItemLike transitionalItem, int loops, CreateOutput[] finalResults, JsonObject... sequenceSteps) {
		JsonObject recipeJson = new JsonObject();
		recipeJson.addProperty("type", "create:sequenced_assembly");
		JsonObject ingObj = new JsonObject();
		ingObj.addProperty("item", BuiltInRegistries.ITEM.getKey(startItem.asItem()).toString());
		recipeJson.add("ingredient", ingObj);
		JsonObject transObj = new JsonObject();
		transObj.addProperty("item", BuiltInRegistries.ITEM.getKey(transitionalItem.asItem()).toString());
		recipeJson.add("transitionalItem", transObj);
		JsonArray sequenceArray = new JsonArray();
		for (JsonObject step : sequenceSteps) {
			sequenceArray.add(step);
		}
		recipeJson.add("sequence", sequenceArray);

		JsonArray resultsArray = new JsonArray();
		for (CreateOutput out : finalResults) {
			JsonObject resObj = new JsonObject();
			// OPRAVA 1.21.1: Finální výstupy sekvence změněny na "id"
			resObj.addProperty("id", BuiltInRegistries.ITEM.getKey(out.stack().getItem()).toString());
			resObj.addProperty("count", out.stack().getCount());
			if (out.chance() < 1.0f) resObj.addProperty("chance", out.chance());
			resultsArray.add(resObj);
		}
		recipeJson.add("results", resultsArray);
		recipeJson.addProperty("loops", loops);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID, name), recipeJson);
	}

	// Pod-kroky sekvence (internal steps) definují pouze své specifické vnitřní vstupy, zde "item"/"fluid" zůstává.
	public static JsonObject stepPressing() {
		JsonObject step = new JsonObject();
		step.addProperty("type", "create:pressing");
		return step;
	}
	public static JsonObject stepCutting(int processingTime) {
		JsonObject step = new JsonObject();
		step.addProperty("type", "create:cutting");
		if (processingTime > 0) step.addProperty("processingTime", processingTime);
		return step;
	}
	public static JsonObject stepFilling(FluidStack fluid) {
		JsonObject step = new JsonObject();
		step.addProperty("type", "create:filling");
		JsonArray ingredients = new JsonArray();
		JsonObject fluidObj = new JsonObject();
		fluidObj.addProperty("fluid", BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());
		fluidObj.addProperty("amount", fluid.getAmount());
		ingredients.add(fluidObj);
		step.add("ingredients", ingredients);
		return step;
	}
	public static JsonObject stepDeploying(ItemLike heldItem) {
		JsonObject step = new JsonObject();
		step.addProperty("type", "create:deploying");
		JsonArray ingredients = new JsonArray();
		JsonObject itemObj = new JsonObject();
		itemObj.addProperty("item", BuiltInRegistries.ITEM.getKey(heldItem.asItem()).toString());
		ingredients.add(itemObj);
		step.add("ingredients", ingredients);
		return step;
	}

	public static Map<ResourceLocation, JsonElement> getVirtualRecipes() {
		return RECIPIES;
	}
}