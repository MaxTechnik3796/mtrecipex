package cz.maxtechnik.mtrecipex;

import net.neoforged.neoforge.common.ModConfigSpec;
public class MtrecipexModCommonConfig{
	private static final ModConfigSpec.Builder BUILDER=new ModConfigSpec.Builder();
	static ModConfigSpec SPEC;
	public static final ModConfigSpec.BooleanValue DEBUG;
	static{
		DEBUG=BUILDER.define("debug",false);
		SPEC=BUILDER.build();
	}
}
