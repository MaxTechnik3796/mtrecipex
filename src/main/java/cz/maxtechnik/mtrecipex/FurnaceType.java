package cz.maxtechnik.mtrecipex;

public enum FurnaceType{
	SMELTING("smelting"),
	SMOKING("smoking"),
	BLASTING("blasting"),
	CAMPFIRE_COOKING("campfire_cooking");
	public final String id;
	FurnaceType(String id){
		this.id=id;
	}
}