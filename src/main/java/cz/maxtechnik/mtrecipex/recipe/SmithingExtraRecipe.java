package cz.maxtechnik.mtrecipex.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
public class SmithingExtraRecipe implements SmithingRecipe {
	public final SizedIngredientExtra template;
	public final SizedIngredientExtra base;
	public final SizedIngredientExtra addition;
	public final ItemStack result;

	public SmithingExtraRecipe(SizedIngredientExtra template, SizedIngredientExtra base, SizedIngredientExtra addition, ItemStack result) {
		this.template = template;
		this.base = base;
		this.addition = addition;
		this.result = result;
	}

	@Override
	public boolean isTemplateIngredient(@NotNull ItemStack itemStack) {
		return this.template.ingredient().test(itemStack) && itemStack.getCount() >= this.template.count();
	}

	@Override
	public boolean isBaseIngredient(@NotNull ItemStack itemStack) {
		return this.base.ingredient().test(itemStack) && itemStack.getCount() >= this.base.count();
	}

	@Override
	public boolean isAdditionIngredient(@NotNull ItemStack itemStack) {
		return this.addition.ingredient().test(itemStack) && itemStack.getCount() >= this.addition.count();
	}

	@Override
	public boolean matches(@NotNull SmithingRecipeInput input, @NotNull Level level) {
		return this.isTemplateIngredient(input.getItem(0)) && input.getItem(0).getCount() >= this.template.count() &&
				this.isBaseIngredient(input.getItem(1)) && input.getItem(1).getCount() >= this.base.count() &&
				this.isAdditionIngredient(input.getItem(2)) && input.getItem(2).getCount() >= this.addition.count();
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull SmithingRecipeInput input, @NotNull HolderLookup.Provider provider) {
		ItemStack itemstack = this.result.copy();
		itemstack.applyComponents(input.getItem(1).getComponentsPatch());
		return itemstack;
	}

	@Override
	public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider provider) {
		return this.result;
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return RecipeType.SMITHING;
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}
	public static class Serializer implements RecipeSerializer<SmithingExtraRecipe>{
		public static final Serializer INSTANCE=new Serializer();
		public static final MapCodec<SmithingExtraRecipe> CODEC=RecordCodecBuilder.mapCodec(inst->inst.group(
				SizedIngredientExtra.CODEC.optionalFieldOf("template",new SizedIngredientExtra(Ingredient.EMPTY,0)).forGetter(r->r.template),
				SizedIngredientExtra.CODEC.optionalFieldOf("base",new SizedIngredientExtra(Ingredient.EMPTY,0)).forGetter(r->r.base),
				SizedIngredientExtra.CODEC.optionalFieldOf("addition",new SizedIngredientExtra(Ingredient.EMPTY,0)).forGetter(r->r.addition),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r->r.result)
		).apply(inst,SmithingExtraRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf,SmithingExtraRecipe> STREAM_CODEC=StreamCodec.of(
				(buf,r)->{
					SizedIngredientExtra.STREAM_CODEC.encode(buf,r.template);
					SizedIngredientExtra.STREAM_CODEC.encode(buf,r.base);
					SizedIngredientExtra.STREAM_CODEC.encode(buf,r.addition);
					ItemStack.STREAM_CODEC.encode(buf,r.result);
				},
				buf->{
					SizedIngredientExtra template=SizedIngredientExtra.STREAM_CODEC.decode(buf);
					SizedIngredientExtra base=SizedIngredientExtra.STREAM_CODEC.decode(buf);
					SizedIngredientExtra addition=SizedIngredientExtra.STREAM_CODEC.decode(buf);
					ItemStack result=ItemStack.STREAM_CODEC.decode(buf);
					return new SmithingExtraRecipe(template,base,addition,result);
				}
		);
		@Override
		public @NotNull MapCodec<SmithingExtraRecipe> codec(){
			return CODEC;
		}
		@Override
		public @NotNull StreamCodec<RegistryFriendlyByteBuf,SmithingExtraRecipe> streamCodec(){
			return STREAM_CODEC;
		}
	}
}
