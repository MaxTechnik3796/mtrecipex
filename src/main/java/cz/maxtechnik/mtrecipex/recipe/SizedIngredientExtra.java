package cz.maxtechnik.mtrecipex.recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
public record SizedIngredientExtra(Ingredient ingredient,int count){
	public static final Codec<SizedIngredientExtra> CODEC=Codec.PASSTHROUGH.flatXmap(
			dynamic->{
				try{
					JsonElement json=dynamic.convert(JsonOps.INSTANCE).getValue();
					Ingredient ing=Ingredient.CODEC.parse(JsonOps.INSTANCE,json).getOrThrow();
					int count=1;
					if(json.isJsonObject()&&json.getAsJsonObject().has("count"))
						count=json.getAsJsonObject().get("count").getAsInt();
					return DataResult.success(new SizedIngredientExtra(ing,count));
				}catch(Exception exception){
					return DataResult.error(exception::getMessage);
				}
			},
			sized->{
				try{
					JsonElement json=Ingredient.CODEC.encodeStart(JsonOps.INSTANCE,sized.ingredient()).getOrThrow();
					if(json.isJsonObject())
						json.getAsJsonObject().addProperty("count",sized.count());
					return DataResult.success(new Dynamic<>(JsonOps.INSTANCE,json));
				}catch(Exception exception){
					return DataResult.error(exception::getMessage);
				}
			}
	);
	public static final StreamCodec<RegistryFriendlyByteBuf,SizedIngredientExtra> STREAM_CODEC=StreamCodec.of(
			(buf,sized)->{
				Ingredient.CONTENTS_STREAM_CODEC.encode(buf,sized.ingredient());
				buf.writeVarInt(sized.count());
			},
			buf->{
				Ingredient ing=Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
				int count=buf.readVarInt();
				return new SizedIngredientExtra(ing,count);
			}
	);
}
