package cz.maxtechnik.mtrecipex;

public enum HeatLevel{
	NONE(""),
	HEATED("heated"),
	SUPERHEATED("superheated");
	public final String lvl;
	HeatLevel(String lvl){
		this.lvl=lvl;
	}
}