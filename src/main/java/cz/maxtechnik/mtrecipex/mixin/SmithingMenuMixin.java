package cz.maxtechnik.mtrecipex.mixin;

import cz.maxtechnik.mtrecipex.recipe.SmithingExtraRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin{
	@Unique
	private static final ThreadLocal<SmithingExtraRecipe> ACTIVE_EXTRA_RECIPE=new ThreadLocal<>();
	@Unique
	private static final ThreadLocal<Integer> TEMPLATE_COST=new ThreadLocal<>();
	@Unique
	private static final ThreadLocal<Integer> BASE_COST=new ThreadLocal<>();
	@Unique
	private static final ThreadLocal<Integer> ADDITION_COST=new ThreadLocal<>();
	@Inject(method="onTake", at=@At("HEAD"))
	private void mtrecipex$onTakeHead(Player player,ItemStack stack,CallbackInfo ci){
		SmithingMenu menu=(SmithingMenu)(Object)this;
		ItemStack template=menu.getSlot(0).getItem();
		ItemStack base=menu.getSlot(1).getItem();
		ItemStack addition=menu.getSlot(2).getItem();
		SmithingRecipeInput input=new SmithingRecipeInput(template.copy(),base.copy(),addition.copy());
		var optional=player.level().getRecipeManager().getRecipeFor(RecipeType.SMITHING,input,player.level());
		if(optional.isPresent()&&optional.get().value() instanceof SmithingExtraRecipe extraRecipe){
			ACTIVE_EXTRA_RECIPE.set(extraRecipe);
			TEMPLATE_COST.set(extraRecipe.getTemplateConsumeCount(input));
			BASE_COST.set(extraRecipe.getBaseConsumeCount(input));
			ADDITION_COST.set(extraRecipe.getAdditionConsumeCount(input));
		}else{
			mtrecipex$clearThreads();
		}
	}
	@Inject(method="onTake", at=@At("RETURN"))
	private void mtrecipex$onTakeReturn(Player player,ItemStack stack,CallbackInfo ci){
		SmithingExtraRecipe recipe=ACTIVE_EXTRA_RECIPE.get();
		if(recipe!=null){
			SmithingMenu menu=(SmithingMenu)(Object)this;
			mtrecipex$shrinkExtra(menu,0,TEMPLATE_COST.get());
			mtrecipex$shrinkExtra(menu,1,BASE_COST.get());
			mtrecipex$shrinkExtra(menu,2,ADDITION_COST.get());
		}
		mtrecipex$clearThreads();
	}
	@Unique
	private void mtrecipex$shrinkExtra(SmithingMenu menu,int slotIndex,Integer recipeCount){
		if(recipeCount!=null&&recipeCount>1){
			ItemStack slotStack=menu.getSlot(slotIndex).getItem();
			if(!slotStack.isEmpty()){
				slotStack.shrink(recipeCount-1);
				if(slotStack.isEmpty()||slotStack.getCount()<=0){
					menu.getSlot(slotIndex).set(ItemStack.EMPTY);
				}else{
					menu.getSlot(slotIndex).set(slotStack);
				}
			}
		}
	}
	@Unique
	private static void mtrecipex$clearThreads(){
		ACTIVE_EXTRA_RECIPE.remove();
		TEMPLATE_COST.remove();
		BASE_COST.remove();
		ADDITION_COST.remove();
	}
}