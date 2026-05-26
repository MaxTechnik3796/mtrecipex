package cz.maxtechnik.mtrecipex;

import net.minecraft.world.item.ItemStack;
/**
 * Create Outputs, ItemStack & chance (0.0 - 1.0)
 */
public record CreateOutput(ItemStack itemStack,float chance){
	public CreateOutput(ItemStack stack){
		this(stack,1F);
	}
	public static CreateOutput of(ItemStack stack){
		return new CreateOutput(stack);
	}
	public static CreateOutput of(ItemStack stack,float chance){
		return new CreateOutput(stack,chance);
	}
}