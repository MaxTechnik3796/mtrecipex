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
public class MTRecipexModRegistry{
	public static final Map<ResourceLocation,JsonElement> RECIPIES=new HashMap<>();
	/**
	 * 1. SHAPED RECEPT (1.7.10 Styl s mřížkou)
	 */
	public static void addShaped(String name,ItemStack result,Object... params){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","minecraft:crafting_shaped");
		recipeJson.addProperty("category","misc");
		JsonArray patternArray=new JsonArray();
		int i=0;
		while(i<params.length&&params[i] instanceof String){
			patternArray.add((String)params[i]);
			i++;
		}
		recipeJson.add("pattern",patternArray);
		JsonObject keyObject=new JsonObject();
		while(i<params.length){
			Character symbol=(Character)params[i];
			ItemLike ingredient=(ItemLike)params[i+1];
			String ingredientId=BuiltInRegistries.ITEM.getKey(ingredient.asItem()).toString();
			JsonObject itemObject=new JsonObject();
			itemObject.addProperty("item",ingredientId);
			keyObject.add(symbol.toString(),itemObject);
			i+=2;
		}
		recipeJson.add("key",keyObject);
		JsonObject resultObject=new JsonObject();
		String resultId=BuiltInRegistries.ITEM.getKey(result.getItem()).toString();
		resultObject.addProperty("id",resultId);
		resultObject.addProperty("count",result.getCount());
		recipeJson.add("result",resultObject);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 * 2. SHAPELESS RECEPT (Beztvarý)
	 */
	public static void addShapeless(String name,ItemStack result,Object... ingredients){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","minecraft:crafting_shapeless");
		recipeJson.addProperty("category","misc");
		JsonArray ingredientsArray=new JsonArray();
		for(Object ing: ingredients){
			if(ing instanceof ItemLike itemLike){
				JsonObject ingObj=new JsonObject();
				ingObj.addProperty("item",BuiltInRegistries.ITEM.getKey(itemLike.asItem()).toString());
				ingredientsArray.add(ingObj);
			}
		}
		recipeJson.add("ingredients",ingredientsArray);
		JsonObject resultObject=new JsonObject();
		resultObject.addProperty("id",BuiltInRegistries.ITEM.getKey(result.getItem()).toString());
		resultObject.addProperty("count",result.getCount());
		recipeJson.add("result",resultObject);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 * 3A. ZÁKLADNÍ CREATE PROCESSING (Pro jednosložkové věci - Milling, Pressing)
	 */
	public static void addCreateProcessing(String name,CreateRecipeType type,ItemLike ingredient,CreateOutput... outputs){
		addCreateProcessing(name,type,ingredient,-1,outputs);
	}
	/**
	 * 3B. CREATE PROCESSING S ČASEM (Pro Crushing / Milling s jedním vstupem)
	 */
	public static void addCreateProcessing(String name,CreateRecipeType type,ItemLike ingredient,int processingTime,CreateOutput... outputs){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","create:"+type.id);
		JsonArray ingredientsArray=new JsonArray();
		JsonObject ingObj=new JsonObject();
		ingObj.addProperty("item",BuiltInRegistries.ITEM.getKey(ingredient.asItem()).toString());
		ingredientsArray.add(ingObj);
		recipeJson.add("ingredients",ingredientsArray);
		JsonArray resultsArray=new JsonArray();
		for(CreateOutput out: outputs){
			JsonObject resObj=new JsonObject();
			resObj.addProperty("item",BuiltInRegistries.ITEM.getKey(out.stack().getItem()).toString());
			resObj.addProperty("count",out.stack().getCount());
			if(out.chance()<1F){
				resObj.addProperty("chance",out.chance());
			}
			resultsArray.add(resObj);
		}
		recipeJson.add("results",resultsArray);
		if(processingTime>0){
			recipeJson.addProperty("processingTime",processingTime);
		}
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 * 4. POKROČILÝ CREATE RECEPT (Ideální pro Mixing a Compacting)
	 * - Žádný processing time
	 * - List (pole) čistých Itemů na vstupu
	 * - Podpora pro tekutiny (Fluidy) na vstupu i výstupu
	 */
	public static void addCreateAdvanced(String name,CreateRecipeType type,HeatLevel heat,ItemLike[] itemInputs,FluidStack[] fluidInputs,CreateOutput[] itemOutputs,FluidStack[] fluidOutputs){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","create:"+type.id);
		// --- VSTUPY (Ingredients) ---
		JsonArray ingredientsArray=new JsonArray();
		if(itemInputs!=null){
			for(ItemLike item: itemInputs){
				JsonObject obj=new JsonObject();
				obj.addProperty("item",BuiltInRegistries.ITEM.getKey(item.asItem()).toString());
				ingredientsArray.add(obj);
			}
		}
		if(fluidInputs!=null){
			for(FluidStack fluid: fluidInputs){
				JsonObject obj=new JsonObject();
				obj.addProperty("fluid",BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());
				obj.addProperty("amount",fluid.getAmount());
				ingredientsArray.add(obj);
			}
		}
		recipeJson.add("ingredients",ingredientsArray);
		// --- VÝSTUPY (Results) ---
		JsonArray resultsArray=new JsonArray();
		if(itemOutputs!=null){
			for(CreateOutput out: itemOutputs){
				JsonObject obj=new JsonObject();
				obj.addProperty("item",BuiltInRegistries.ITEM.getKey(out.stack().getItem()).toString());
				obj.addProperty("count",out.stack().getCount());
				if(out.chance()<1.0f){
					obj.addProperty("chance",out.chance());
				}
				resultsArray.add(obj);
			}
		}
		if(fluidOutputs!=null){
			for(FluidStack fluid: fluidOutputs){
				JsonObject obj=new JsonObject();
				obj.addProperty("fluid",BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());
				obj.addProperty("amount",fluid.getAmount());
				resultsArray.add(obj);
			}
		}
		recipeJson.add("results",resultsArray);
		// --- TEPLO ---
		if(heat!=null&&heat!=HeatLevel.NONE){
			recipeJson.addProperty("heatRequirement",heat.lvl);
		}
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	public static Map<ResourceLocation,JsonElement> getVirtualRecipes(){
		return RECIPIES;
	}
}