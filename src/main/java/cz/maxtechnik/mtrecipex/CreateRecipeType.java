package cz.maxtechnik.mtrecipex;

public enum CreateRecipeType{
	PRESSING("pressing"),
	SANDPAPER_POLISHING("sandpaper_polishing"),
	MILLING("milling"),
	CRUSHING("crushing"),
	CUTTING("cutting"),
	HAUNTING("haunting"),
	SPLASHING("splashing"),
	FILLING("filling"),
	EMPTYING("emptying"),
	DEPLOYING("deploying"),
	ITEM_APPLICATION("item_application"),
	MIXING("mixing"),
	COMPACTING("compacting"),
	MECHANICAL_CRAFTING("mechanical_crafting"),
	SEQUENCED_ASSEMBLY("SEQUENCED_ASSEMBLY");
	public final String id;
	CreateRecipeType(String id){
		this.id=id;
	}
}