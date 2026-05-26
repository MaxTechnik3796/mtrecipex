## MT-Recipex

**MT-Recipex** is an advanced developer API mod for **Minecraft 1.21.1 (NeoForge)**. It restores the  recipe registration directly from Java, inspired by  old version **1.7.10** (`GameRegistry.addRecipe`).

---

## 🚀 Key Features & Advantages

- **Full JEI (Just Enough Items) Compatibility:** Because recipes are hot-swapped directly into the engine via a Mixin, JEI completely reads and correctly renders them across all appropriate tabs (including drop chances, processing time, and heat requirements).
- **Type Safety:** Strict Java Enums (`CreateRecipeType`, `HeatLevel`...) instead of strings prevents typos.

---

## 🛠️ Supported Recipe Types

### 1. Vanilla Recipes (Crafting Table, Furnace, Smithing Table)
* **Shaped (`addShaped`):** Traditional 1.7.10 grid layout style (`"DDD"`, `"DID"`) followed by symbol definitions.
* **Shapeless (`addShapeless`):** Simple ingredients list (up to 9 items) that the player can throw anywhere into the crafting grid.
* **Furnace / Cooking (`Smelting`, `Blasting`, `Smoking`, `Campfire Cooking`):** Support for the regular furnace, blast furnace, smoker and camp fire.
* **Smithing Table (`addSmithingTransform`):** Full specification of the modern smithing upgrade layout requiring a Template, a Base item, and an Addition ingredient.

### 2. Create Mod
* **Single-Ingredient Machines (`addCreateProcessing`):** Pressing, sandpaper polishing (`PRESSING`, `SANDPAPER_POLISHING`).
* **Time-Dependent Machines:** Crushing and cutting (`CRUSHING`, `CUTTING`, `MILLING`,`HAUNTING`, `SPLASHING`) with an optional explicit `processingTime` parameter.
* **Output Chances (`CreateOutput` Record):** Create processing outputs accept an array of `CreateOutput` objects, combining an `ItemStack` and a float drop chance (from `0.0F` to `1.0F`).

### 3. Basin based Create Machines (Mixing & Compacting)
* **Mixing and Compacting (`addCreateAdvanced`):** Multi-ingredient support for `MIXING` and `COMPACTING`.
* **Combined Inputs & Outputs:** Accepts arrays of clean `ItemLike` objects and NeoForge `FluidStack` liquids on the input side, matching `CreateOutput` objects and `FluidStack` liquids on the output side.
* **Heat Requirements (`HeatLevel` Enum):** Recipes can be strictly restricted to a specific Blaze Burner status: `NONE`, `HEATED`, or `SUPERHEATED`.
* **Filling & Emptying (`addCreateFilling`, `addCreateEmptying`):** Fluid-container interactions handled programmatically.
* **Mechanical Deployer (`addCreateDeploying`):** Simulates a deployer hand clicking an item onto a base block moving on a belt.
* **Item Application (`addCreateItemApplication`):** Right-clicking an item onto a specific block (e.g., creating Casings).
* **Mechanical Crafting (`addCreateMechanicalCrafting`):** Large crafting grids (5x5 and above) hooked up to shafts, featuring a toggleable `acceptMirrored` parameter.

### 4. Sequenced Assembly
* **`addCreateSequencedAssembly`:** Used for Precision Mechanisms and high-tech components.
* **Complete Sub-Step Automation:** Manually coding JSONs for sequences is extremely tedious in 1.21.1  **MT-Recipex handles all of this automatically behind the scenes!** You only define the steps in their natural order.
* **Built-in Step Factory Methods:** `stepPressing()`, `stepCutting(time)`, `stepFilling(FluidStack)`, `stepDeploying(ItemLike)`.

---

## 💻 Code Examples (Cheat Sheet)

Here is a preview of how clean and fast recipes are declared inside your mod:






```java
import cz.maxtechnik.mtrecipex.MTRecipexModRegistry;
public class MyModClass(){
	MTRecipexModRegistry.addShaped("shaped_dirt_to_diamond",
	new ItemStack(Items.DIAMOND),
        "DDD",
	"DID",
	"DDD",
	'D', Blocks.DIRT,
	'I', Items.IRON_INGOT
    );



    MTRecipexModRegistry.addShapeless("shapeless_clay_and_flint",
    new ItemStack(Items.IRON_INGOT, 2),
    Blocks.CLAY,
    Items.FLINT
    );

    MTRecipexModRegistry.addSmithingTransform("smithing_wood_to_stone",
	Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
	Items.WOODEN_SWORD,
	Blocks.STONE,
			new ItemStack(Items.STONE_SWORD)
    );

MTRecipexModRegistry.addSmelting("smelt_dirt_to_iron", Blocks.DIRT, new ItemStack(Items.IRON_INGOT));

MTRecipexModRegistry.addFurnace("advanced_blast_furnace_iron",
	FurnaceType.BLASTING,                   // Typ pece (blasting, smoking, smelting...)
	Blocks.IRON_ORE,                        // Vstupní surovina
			new ItemStack(Items.IRON_INGOT, 2),     // Výstupní surovina
    1.0F,                                   // Množství zkušeností (XP)
			100                                     // Doba vaření v tickách (100 ticks = 5 sekund)
			);

MTRecipexModRegistry.addCreateProcessing("crush_rose_bush_example",
	CreateRecipeType.CRUSHING,
	Blocks.ROSE_BUSH, // Vstupní blok
			250,              // Čas zpracování v tickách (0 nebo méně čas vypne)
			CreateOutput.of(new ItemStack(Items.RED_DYE, 4)),         // 100% šance na drop
			CreateOutput.of(new ItemStack(Items.GREEN_DYE, 1), 0.5F)  // 50% šance na bonusový drop
			);

MTRecipexModRegistry.addCreateBasin("basin_liquids_only",
	CreateRecipeType.MIXING,
	HeatLevel.HEATED, // Úroveň tepla (NONE, HEATED, SUPERHEATED)
			new FluidStack[]{ new FluidStack(Fluids.WATER, 1000) }, // Pole vstupních fluidů
			new FluidStack[]{ new FluidStack(Fluids.LAVA, 500) }    // Pole výstupních fluidů
);

MTRecipexModRegistry.addCreateBasin("basin_items_only",
	CreateRecipeType.COMPACTING,
	HeatLevel.NONE,
			new ItemLike[]{ Blocks.COBBLESTONE, Blocks.SAND },      // Pole vstupních itemů
			new CreateOutput[]{ CreateOutput.of(new ItemStack(Items.DIAMOND)) } // Pole výstupních itemů
);

MTRecipexModRegistry.addCreateBasin("basin_combined_advanced",
	CreateRecipeType.MIXING,
	HeatLevel.SUPERHEATED,
			new ItemLike[]{ Items.SUGAR, Blocks.DIRT },               // Vstupní itemy
			new FluidStack[]{ new FluidStack(Fluids.WATER, 500) },    // Vstupní fluidy
			new CreateOutput[]{ CreateOutput.of(new ItemStack(Items.BLAZE_POWDER, 2)) }, // Výstupní itemy
			new FluidStack[]{ new FluidStack(Fluids.LAVA, 250) }       // Výstupní fluidy
);

MTRecipexModRegistry.addDifDistillation("distill_water_test",
		new FluidStack(Fluids.WATER, 1000), // Vstupní tekutina
    new FluidStack[]{                   // Pole výstupních tekutin
		new FluidStack(Fluids.LAVA, 250),
				new FluidStack(Fluids.WATER, 100)
	}
);

MTRecipexModRegistry.addCreateFilling("fill_glass_bottle",
	Items.GLASS_BOTTLE,               // Vstupní nádoba
			new FluidStack(Fluids.WATER, 250), // Tekutina, která do ní nateče
    CreateOutput.of(new ItemStack(Items.POTION)) // Výsledný naplněný předmět
			);

MTRecipexModRegistry.addCreateEmptying("drain_water_bucket",
	Items.WATER_BUCKET,                  // Vstupní plný předmět
			CreateOutput.of(new ItemStack(Items.BUCKET)), // Co zbude v ruce/na pásu (může být i null)
			new FluidStack(Fluids.WATER, 1000)   // Tekutina, která vyteče do potrubí
);

MTRecipexModRegistry.addCreateDeploying("deploy_flint_on_stone",
	Blocks.STONE, Items.FLINT, // 1. Podložný blok na pásu, 2. Item držený deployerem
			CreateOutput.of(new ItemStack(Items.ARROW)) // Výsledný drop na pásu
			);

MTRecipexModRegistry.addCreateDeploying("deploy_consume_log",
	Blocks.OAK_LOG, Items.IRON_AXE,
			true, // TRUE = spotřebuje/zničí sekyru | FALSE = sekyra zůstane v ruce deployeru
			CreateOutput.of(new ItemStack(Blocks.STRIPPED_OAK_LOG))
			);

MTRecipexModRegistry.addCreateItemApplication("apply_iron_on_wood",
	Blocks.OAK_LOG, Items.IRON_INGOT, // Kliknutím železa na dubové dřevo...
			new ItemStack(Items.SHIELD)        // ...vznikne štít
);

MTRecipexModRegistry.addCreateMechanicalCrafting("mechanical_simple_sword",
		new ItemStack(Items.DIAMOND_SWORD),
    "D",
			"D",
			"S",
			'D', Blocks.DIAMOND_BLOCK,
			'S', Items.STICK
);

MTRecipexModRegistry.addCreateMechanicalCrafting("mechanical_strict_pickaxe",
		new ItemStack(Items.DIAMOND_PICKAXE),
    false, // FALSE = vzorec se nesmí zrcadlově otočit (musí být přesně takto)
			"DDD",
			" S ",
			" S ",
			'D', Items.DIAMOND,
			'S', Items.STICK
);

MTRecipexModRegistry.addCreateSequencedAssembly("precision_diamond_assembly",
	Items.IRON_INGOT,     // 1. Vstupní surovina na začátku pásu
	Items.GOLD_INGOT,     // 2. Přechodný nekompletní item (transitional_item)
			5,                    // 3. Počet smyček/opakování (loops)
			new CreateOutput[]{ CreateOutput.of(new ItemStack(Items.DIAMOND)) }, // 4. Finální pole odměn

			// 5. Následuje pole sub-kroků za sebou (voláš vestavěné step funkce z registru):
			MTRecipexModRegistry.stepPressing(),                                      // Krok 1: Lis
			MTRecipexModRegistry.stepCutting(120),                                    // Krok 2: Pila (120 ticks)
			MTRecipexModRegistry.stepFilling(new FluidStack(Fluids.WATER, 250)),      // Krok 3: Plnička vodou
			MTRecipexModRegistry.stepDeploying(Items.FLINT)                           // Krok 4: Deployer s křesadlem
			);
}


```











