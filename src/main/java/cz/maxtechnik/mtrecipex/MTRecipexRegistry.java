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
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MtrecipexMod.MODID,name),recipeJson);
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
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MtrecipexMod.MODID,name),recipeJson);
	}
	/**
	 * 3A. ZÁKLADNÍ CREATE PROCESSING (Bez času a tepla)
	 */
	public static void addCreateProcessing(String name,String createType,ItemLike ingredient,CreateOutput... outputs){
		addCreateProcessing(name,createType,ingredient,-1,HeatLevel.NONE,outputs);
	}
	/**
	 * 3B. CREATE PROCESSING S ČASEM (Ideální pro milling, crushing - např. 200 ticks)
	 */
	public static void addCreateProcessing(String name,String createType,ItemLike ingredient,int processingTime,CreateOutput... outputs){
		addCreateProcessing(name,createType,ingredient,processingTime,HeatLevel.NONE,outputs);
	}
	/**
	 * 3C. CREATE PROCESSING S TEPLEM (Ideální pro mixing, compacting)
	 */
	public static void addCreateProcessing(String name,String createType,ItemLike ingredient,HeatLevel heatRequirement,CreateOutput... outputs){
		addCreateProcessing(name,createType,ingredient,-1,heatRequirement,outputs);
	}
	/**
	 * 3D. HLAVNÍ METODA: CREATE PROCESSING S ČASEM I TEPLEM
	 */
	public static void addCreateProcessing(String name,String createType,ItemLike ingredient,int processingTime,HeatLevel heat,CreateOutput... outputs){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","create:"+createType);
		// Vstupy (Ingredients)
		JsonArray ingredientsArray=new JsonArray();
		JsonObject ingObj=new JsonObject();
		ingObj.addProperty("item",BuiltInRegistries.ITEM.getKey(ingredient.asItem()).toString());
		ingredientsArray.add(ingObj);
		recipeJson.add("ingredients",ingredientsArray);
		// Výstupy (Results)
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
		// PŘIDÁNÍ ČASU (Pokud je zadán větší než 0)
		if(processingTime>0){
			recipeJson.addProperty("processingTime",processingTime);
		}
		// PŘIDÁNÍ TEPLA (Pokud je jiné než "none")
		if(!heat.equals(HeatLevel.NONE)){
			recipeJson.addProperty("heatRequirement",heat.lvl);
		}
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MtrecipexMod.MODID,name),recipeJson);
	}
	public static Map<ResourceLocation,JsonElement> getVirtualRecipes(){
		return RECIPIES;
	}
}