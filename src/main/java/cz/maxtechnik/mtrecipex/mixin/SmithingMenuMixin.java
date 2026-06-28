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
	@Inject(method="onTake", at=@At("HEAD"))
	private void mtrecipex$onTakeHead(Player player,ItemStack stack,CallbackInfo ci){
		SmithingMenu menu=(SmithingMenu)(Object)this;
		ItemStack template=menu.getSlot(0).getItem();
		ItemStack base=menu.getSlot(1).getItem();
		ItemStack addition=menu.getSlot(2).getItem();
		SmithingRecipeInput input=new SmithingRecipeInput(template.copy(),base.copy(),addition.copy());
		var optional=player.level().getRecipeManager().getRecipeFor(RecipeType.SMITHING,input,player.level());
		if(optional.isPresent()&&optional.get().value() instanceof SmithingExtraRecipe extraRecipe)
			ACTIVE_EXTRA_RECIPE.set(extraRecipe);
		else ACTIVE_EXTRA_RECIPE.remove();
	}
	@Inject(method="onTake", at=@At("RETURN"))
	private void mtrecipex$onTakeReturn(Player player,ItemStack stack,CallbackInfo ci){
		SmithingExtraRecipe recipe=ACTIVE_EXTRA_RECIPE.get();
		if(recipe!=null){
			SmithingMenu menu=(SmithingMenu)(Object)this;
			mtrecipex$shrinkExtra(menu,0,recipe.template().count());
			mtrecipex$shrinkExtra(menu,1,recipe.base().count());
			mtrecipex$shrinkExtra(menu,2,recipe.addition().count());
		}
		ACTIVE_EXTRA_RECIPE.remove();
	}
	@Unique
	private void mtrecipex$shrinkExtra(SmithingMenu menu,int slotIndex,int recipeCount){
		if(recipeCount>1){
			ItemStack slotStack=menu.getSlot(slotIndex).getItem();
			if(!slotStack.isEmpty()){
				slotStack.shrink(recipeCount-1);
				if(slotStack.isEmpty()||slotStack.getCount()<=0) menu.getSlot(slotIndex).set(ItemStack.EMPTY);
				else menu.getSlot(slotIndex).set(slotStack);
			}
		}
	}
}
