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
	 *Shaped Crafting
	 */
	public static void addShaped(String name,ItemStack result,Object... params){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","crafting_shaped");
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
	 *Shapeless Crafting
	 */
	public static void addShapeless(String name,ItemStack result,Object... ingredients){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","crafting_shapeless");
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
	 *Smithing Table
	 */
	public static void addSmithingTransform(String name,ItemLike template,ItemLike base,ItemLike addition,ItemStack result){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","minecraft:smithing_transform");
		JsonObject templateObj=new JsonObject();
		templateObj.addProperty("item",BuiltInRegistries.ITEM.getKey(template.asItem()).toString());
		recipeJson.add("template",templateObj);
		JsonObject baseObj=new JsonObject();
		baseObj.addProperty("item",BuiltInRegistries.ITEM.getKey(base.asItem()).toString());
		recipeJson.add("base",baseObj);
		JsonObject additionObj=new JsonObject();
		additionObj.addProperty("item",BuiltInRegistries.ITEM.getKey(addition.asItem()).toString());
		recipeJson.add("addition",additionObj);
		JsonObject resultObj=new JsonObject();
		resultObj.addProperty("id",BuiltInRegistries.ITEM.getKey(result.getItem()).toString());
		if(result.getCount()>1){
			resultObj.addProperty("count",result.getCount());
		}
		recipeJson.add("result",resultObj);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 *Furnace
	 */
	public static void addSmelting(String name,ItemLike ingredient,ItemStack result){
		addFurnace(name,FurnaceType.SMELTING,ingredient,result,0.1F,200);
	}
	/**
	 *Furnaces (advanced)
	 */
	public static void addFurnace(String name,FurnaceType type,ItemLike ingredient,ItemStack result,float experience,int cookingTime){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type",type.id);
		JsonObject ingredientObj=new JsonObject();
		ingredientObj.addProperty("item",BuiltInRegistries.ITEM.getKey(ingredient.asItem()).toString());
		recipeJson.add("ingredient",ingredientObj);
		JsonObject resultObj=new JsonObject();
		resultObj.addProperty("id",BuiltInRegistries.ITEM.getKey(result.getItem()).toString());
		if(result.getCount()>1){
			resultObj.addProperty("count",result.getCount());
		}
		recipeJson.add("result",resultObj);
		recipeJson.addProperty("experience",experience);
		recipeJson.addProperty("cookingtime",cookingTime);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 *Processing
	 * @param processingTime Processing Time (ticks)
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
			resObj.addProperty("id",BuiltInRegistries.ITEM.getKey(out.itemStack().getItem()).toString());
			resObj.addProperty("count",out.itemStack().getCount());
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
	 *Mixing / Compacting
	 *@param fluidInputs Fluids Inputs
	 *@param fluidOutputs Fluids Outputs
	 */
	public static void addCreateBasin(String name,CreateRecipeType type,HeatLevel heat,FluidStack[] fluidInputs,FluidStack[] fluidOutputs){
		addCreateBasin(name,type,heat,null,fluidInputs,null,fluidOutputs);
	}
	/**
	 *Mixing / Compacting
	 *@param itemInputs Items Inputs
	 *@param itemOutputs Items Outputs
	 */
	public static void addCreateBasin(String name,CreateRecipeType type,HeatLevel heat,ItemLike[] itemInputs,CreateOutput[] itemOutputs){
		addCreateBasin(name,type,heat,itemInputs,null,itemOutputs,null);
	}
	/**
	 *Mixing / Compacting
	 *@param itemInputs Items Inputs
	 *@param fluidInputs Fluids Inputs
	 *@param itemOutputs Items Outputs
	 *@param fluidOutputs Fluids Outputs
	 */
	public static void addCreateBasin(String name,CreateRecipeType type,HeatLevel heat,ItemLike[] itemInputs,FluidStack[] fluidInputs,CreateOutput[] itemOutputs,FluidStack[] fluidOutputs){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","create:"+type.id);
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
				obj.addProperty("type","neoforge:single");
				obj.addProperty("fluid",BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());
				obj.addProperty("amount",fluid.getAmount());
				ingredientsArray.add(obj);
			}
		}
		recipeJson.add("ingredients",ingredientsArray);
		JsonArray resultsArray=new JsonArray();
		if(itemOutputs!=null){
			for(CreateOutput out: itemOutputs){
				JsonObject obj=new JsonObject();
				obj.addProperty("id",BuiltInRegistries.ITEM.getKey(out.itemStack().getItem()).toString());
				obj.addProperty("count",out.itemStack().getCount());
				if(out.chance()<1.0f){
					obj.addProperty("chance",out.chance());
				}
				resultsArray.add(obj);
			}
		}
		if(fluidOutputs!=null){
			for(FluidStack fluid: fluidOutputs){
				JsonObject obj=new JsonObject();
				obj.addProperty("id",BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());
				obj.addProperty("amount",fluid.getAmount());
				resultsArray.add(obj);
			}
		}
		recipeJson.add("results",resultsArray);
		if(heat!=null&&heat!=HeatLevel.NONE){
			recipeJson.addProperty("heatRequirement",heat.lvl);
		}
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 *Distillation
	 */
	public static void addDifDistillation(String name,FluidStack input,FluidStack[] outputs){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","dif:distillation");
		JsonObject inputJson=new JsonObject();
		inputJson.addProperty("fluid",BuiltInRegistries.FLUID.getKey(input.getFluid()).toString());
		inputJson.addProperty("amount",input.getAmount());
		JsonArray outputsArray=new JsonArray();
		if(outputs!=null){
			for(FluidStack fluid: outputs){
				JsonObject obj=new JsonObject();
				obj.addProperty("id",BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());
				obj.addProperty("amount",fluid.getAmount());
				outputsArray.add(obj);
			}
		}
		recipeJson.add("outputs",outputsArray);
		recipeJson.add("input",inputJson);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 *Filling (Spout)
	 */
	public static void addCreateFilling(String name,ItemLike itemInput,FluidStack fluidInput,CreateOutput itemOutput){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","create:filling");
		JsonArray ingredientsArray=new JsonArray();
		JsonObject itemObj=new JsonObject();
		itemObj.addProperty("item",BuiltInRegistries.ITEM.getKey(itemInput.asItem()).toString());
		ingredientsArray.add(itemObj);
		JsonObject fluidObj=new JsonObject();
		fluidObj.addProperty("type","neoforge:single");
		fluidObj.addProperty("fluid",BuiltInRegistries.FLUID.getKey(fluidInput.getFluid()).toString());
		fluidObj.addProperty("amount",fluidInput.getAmount());
		ingredientsArray.add(fluidObj);
		recipeJson.add("ingredients",ingredientsArray);
		JsonArray resultsArray=new JsonArray();
		JsonObject resObj=new JsonObject();
		resObj.addProperty("id",BuiltInRegistries.ITEM.getKey(itemOutput.itemStack().getItem()).toString());
		resObj.addProperty("count",itemOutput.itemStack().getCount());
		if(itemOutput.chance()<1F) resObj.addProperty("chance",itemOutput.chance());
		resultsArray.add(resObj);
		recipeJson.add("results",resultsArray);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 *Emptying (Item Drain)
	 */
	public static void addCreateEmptying(String name,ItemLike itemInput,CreateOutput itemOutput,FluidStack fluidOutput){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","create:emptying");
		JsonArray ingredientsArray=new JsonArray();
		JsonObject itemObj=new JsonObject();
		itemObj.addProperty("item",BuiltInRegistries.ITEM.getKey(itemInput.asItem()).toString());
		ingredientsArray.add(itemObj);
		recipeJson.add("ingredients",ingredientsArray);
		JsonArray resultsArray=new JsonArray();
		if(itemOutput!=null){
			JsonObject resObj=new JsonObject();
			resObj.addProperty("id",BuiltInRegistries.ITEM.getKey(itemOutput.itemStack().getItem()).toString());
			resObj.addProperty("count",itemOutput.itemStack().getCount());
			resultsArray.add(resObj);
		}
		JsonObject fluidObj=new JsonObject();
		fluidObj.addProperty("id",BuiltInRegistries.FLUID.getKey(fluidOutput.getFluid()).toString());
		fluidObj.addProperty("amount",fluidOutput.getAmount());
		resultsArray.add(fluidObj);
		recipeJson.add("results",resultsArray);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 *Deploying
	 */
	public static void addCreateDeploying(String name,ItemLike baseInput,ItemLike heldItemInput,CreateOutput... outputs){
		addCreateDeploying(name,baseInput,heldItemInput,false,outputs);
	}
	/**
	 *Deploying
	 *@param consume Consume heldItem?
	 */
	public static void addCreateDeploying(String name,ItemLike baseInput,ItemLike heldItemInput,boolean consume,CreateOutput... outputs){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","create:deploying");
		recipeJson.addProperty("keep_held_item",!consume);
		JsonArray ingredientsArray=new JsonArray();
		JsonObject baseObj=new JsonObject();
		baseObj.addProperty("item",BuiltInRegistries.ITEM.getKey(baseInput.asItem()).toString());
		ingredientsArray.add(baseObj);
		JsonObject heldObj=new JsonObject();
		heldObj.addProperty("item",BuiltInRegistries.ITEM.getKey(heldItemInput.asItem()).toString());
		ingredientsArray.add(heldObj);
		recipeJson.add("ingredients",ingredientsArray);
		JsonArray resultsArray=new JsonArray();
		for(CreateOutput out: outputs){
			JsonObject resObj=new JsonObject();
			resObj.addProperty("id",BuiltInRegistries.ITEM.getKey(out.itemStack().getItem()).toString());
			resObj.addProperty("count",out.itemStack().getCount());
			if(out.chance()<1.0f) resObj.addProperty("chance",out.chance());
			resultsArray.add(resObj);
		}
		recipeJson.add("results",resultsArray);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 *Item Application
	 */
	public static void addCreateItemApplication(String name,ItemLike baseBlock,ItemLike heldItem,ItemStack result){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","create:item_application");
		JsonArray ingredientsArray=new JsonArray();
		JsonObject baseObj=new JsonObject();
		baseObj.addProperty("item",BuiltInRegistries.ITEM.getKey(baseBlock.asItem()).toString());
		ingredientsArray.add(baseObj);
		JsonObject heldObj=new JsonObject();
		heldObj.addProperty("item",BuiltInRegistries.ITEM.getKey(heldItem.asItem()).toString());
		ingredientsArray.add(heldObj);
		recipeJson.add("ingredients",ingredientsArray);
		JsonArray resultsArray=new JsonArray();
		JsonObject resObj=new JsonObject();
		resObj.addProperty("id",BuiltInRegistries.ITEM.getKey(result.getItem()).toString());
		resObj.addProperty("count",result.getCount());
		resultsArray.add(resObj);
		recipeJson.add("results",resultsArray);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 *Mechanical Crafting
	 */
	public static void addCreateMechanicalCrafting(String name,ItemStack result,Object... params){
		addCreateMechanicalCrafting(name,result,true,params);
	}
	/**
	 *Mechanical Crafting
	 * @param acceptMirrored Accept mirrored pattern?
	 */
	public static void addCreateMechanicalCrafting(String name,ItemStack result,boolean acceptMirrored,Object... params){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","create:mechanical_crafting");
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
		recipeJson.addProperty("accept_mirrored",acceptMirrored);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	/**
	 * Sequenced Assembly
	 */
	public static void addCreateSequencedAssembly(String name,ItemLike startItem,ItemLike transitionalItem,int loops,CreateOutput[] finalResults,JsonObject... sequenceSteps){
		JsonObject recipeJson=new JsonObject();
		recipeJson.addProperty("type","create:sequenced_assembly");
		String transId=BuiltInRegistries.ITEM.getKey(transitionalItem.asItem()).toString();
		JsonObject ingObj=new JsonObject();
		ingObj.addProperty("item",BuiltInRegistries.ITEM.getKey(startItem.asItem()).toString());
		recipeJson.add("ingredient",ingObj);
		JsonObject transObj=new JsonObject();
		transObj.addProperty("id",transId);
		recipeJson.add("transitional_item",transObj);
		// AUTOMATICKÁ OPRAVA POD-KROKŮ SEKVENCE
		JsonArray sequenceArray=new JsonArray();
		for(JsonObject step: sequenceSteps){
			JsonObject processedStep=step.deepCopy(); // Vytvoříme kopii kroku
			JsonArray newIngredients=new JsonArray();
			// 1. První položka v kroku musí být VŽDY přechodný item (transitionalItem)
			JsonObject transInput=new JsonObject();
			transInput.addProperty("item",transId);
			newIngredients.add(transInput);
			// Pokud pod-krok už obsahoval nějaké své ingredience (např. fluid nebo held item), přidáme je hned za to
			if(processedStep.has("ingredients")){
				JsonArray existingIngs=processedStep.getAsJsonArray("ingredients");
				for(JsonElement element: existingIngs){
					newIngredients.add(element);
				}
			}
			processedStep.add("ingredients",newIngredients);
			// 2. Každý krok v sekvenci MUSÍ mít pole "results" vracející transitional item s klíčem "id"
			JsonArray stepResults=new JsonArray();
			JsonObject transOutput=new JsonObject();
			transOutput.addProperty("id",transId);
			stepResults.add(transOutput);
			processedStep.add("results",stepResults);
			sequenceArray.add(processedStep);
		}
		recipeJson.add("sequence",sequenceArray);
		JsonArray resultsArray=new JsonArray();
		for(CreateOutput out: finalResults){
			JsonObject resObj=new JsonObject();
			resObj.addProperty("id",BuiltInRegistries.ITEM.getKey(out.itemStack().getItem()).toString());
			resObj.addProperty("count",out.itemStack().getCount());
			if(out.chance()<1.0f) resObj.addProperty("chance",out.chance());
			resultsArray.add(resObj);
		}
		recipeJson.add("results",resultsArray);
		recipeJson.addProperty("loops",loops);
		RECIPIES.put(ResourceLocation.fromNamespaceAndPath(MTRecipexMod.MODID,name),recipeJson);
	}
	public static JsonObject stepPressing(){
		JsonObject step=new JsonObject();
		step.addProperty("type","create:pressing");
		return step;
	}
	public static JsonObject stepCutting(int processingTime){
		JsonObject step=new JsonObject();
		step.addProperty("type","create:cutting");
		if(processingTime>0) step.addProperty("processingTime",processingTime);
		return step;
	}
	public static JsonObject stepFilling(FluidStack fluid){
		JsonObject step=new JsonObject();
		JsonArray ingredients=new JsonArray();
		JsonObject fluidObj=new JsonObject();
		// OPRAVA: V sekvenci v 1.21.1 vyžadují tekutiny single typ pro NeoForge ingredience
		step.addProperty("type","create:filling");
		fluidObj.addProperty("type","neoforge:single");
		fluidObj.addProperty("fluid",BuiltInRegistries.FLUID.getKey(fluid.getFluid()).toString());
		fluidObj.addProperty("amount",fluid.getAmount());
		ingredients.add(fluidObj);
		step.add("ingredients",ingredients);
		return step;
	}
	public static JsonObject stepDeploying(ItemLike heldItem){
		JsonObject step=new JsonObject();
		JsonArray MathIngredients=new JsonArray();
		JsonObject itemObj=new JsonObject();
		step.addProperty("type","create:deploying");
		itemObj.addProperty("item",BuiltInRegistries.ITEM.getKey(heldItem.asItem()).toString());
		MathIngredients.add(itemObj);
		step.add("ingredients",MathIngredients);
		return step;
	}
	public static Map<ResourceLocation,JsonElement> getVirtualRecipes(){
		return RECIPIES;
	}
}