package cz.maxtechnik.mtrecipex;

import net.minecraft.world.item.ItemStack;
/**
 * Pomocný objekt pro Create výstupy kombinující ItemStack a šanci (0.0 až 1.0)
 */
public record CreateOutput(ItemStack stack,float chance){
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