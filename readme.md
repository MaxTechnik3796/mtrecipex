## MT-Recipex

**MT-Recipex** is an advanced developer API mod for **Minecraft 1.21.1 (NeoForge)**. It restores the recipe registration directly from Java, inspired by old version **1.7.10** (`GameRegistry.addRecipe`).

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

### 2. Create Mod Processing
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
* **Complete Sub-Step Automation:** Manually coding JSONs for sequences is extremely tedious in 1.21.1 **MT-Recipex handles all of this automatically behind the scenes!** You only define the steps in their natural order.
* **Built-in Step Factory Methods:** `stepPressing()`, `stepCutting(time)`, `stepFilling(FluidStack)`, `stepDeploying(ItemLike)`.

### 5. Custom Raw JSON Recipes
* **Custom (`addCustom`):** Allows you to build and inject a completely raw `JsonObject` for any custom recipe type from other mods that are not natively supported by the API.

---

## 💻 Code Examples (Cheat Sheet)

```java
import cz.maxtechnik.mtrecipex.MTRecipexModRegistry;
import cz.maxtechnik.mtrecipex.CreateRecipeType;
import cz.maxtechnik.mtrecipex.HeatLevel;
import cz.maxtechnik.mtrecipex.FurnaceType;
import cz.maxtechnik.mtrecipex.CreateOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

public class MyModClass {

    public static void registerRecipes() {
        // ====================================================================
        // 1. VANILLA RECIPES
        // ====================================================================

        //Shaped Crafting
        MTRecipexModRegistry.addShaped("shaped_dirt_to_diamond",
            new ItemStack(Items.DIAMOND),
            "DDD",
            "DID",
            "DDD",
            'D', Blocks.DIRT,
            'I', Items.IRON_INGOT
        );

        //Shapeless Crafting
        MTRecipexModRegistry.addShapeless("shapeless_clay_and_flint",
            new ItemStack(Items.IRON_INGOT, 2),
            Blocks.CLAY,
            Items.FLINT
        );

        //Smithing Table
        MTRecipexModRegistry.addSmithingTransform("smithing_wood_to_stone",
            Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            Items.WOODEN_SWORD,
            Blocks.STONE,
            new ItemStack(Items.STONE_SWORD)
        );

        //Furnace (simple)
        MTRecipexModRegistry.addSmelting("smelt_dirt_to_iron", Blocks.DIRT, new ItemStack(Items.IRON_INGOT));

        //Smelting (any furnace or camp fire)
        MTRecipexModRegistry.addFurnace("advanced_blast_furnace_iron",
            FurnaceType.BLASTING,
            Blocks.IRON_ORE,
            new ItemStack(Items.IRON_INGOT, 2),
            1.0F,
            100
        );

        // ====================================================================
        // 2. CREATE MOD PROCESSING
        // ====================================================================

        //Crushning
        MTRecipexModRegistry.addCreateProcessing("crush_rose_bush_example",
            CreateRecipeType.CRUSHING,
            Blocks.ROSE_BUSH,
            250,
            CreateOutput.of(new ItemStack(Items.RED_DYE, 4)),
            CreateOutput.of(new ItemStack(Items.GREEN_DYE, 1), 0.5F)
        );

        // ====================================================================
        // 3. BASIN BASED CREATE MACHINES
        // ====================================================================

        //Mixing (only items)
        MTRecipexModRegistry.addCreateBasin("basin_liquids_only",
            CreateRecipeType.MIXING,
            HeatLevel.HEATED,
            new FluidStack[]{ new FluidStack(Fluids.WATER, 1000) },
            new FluidStack[]{ new FluidStack(Fluids.LAVA, 500) }
        );

        //Compacting
        MTRecipexModRegistry.addCreateBasin("basin_items_only",
            CreateRecipeType.COMPACTING,
            HeatLevel.NONE,
            new ItemLike[]{ Blocks.COBBLESTONE, Blocks.SAND },
            new CreateOutput[]{ CreateOutput.of(new ItemStack(Items.DIAMOND)) }
        );

        //Mixing (items & fluids)
        MTRecipexModRegistry.addCreateBasin("basin_combined_advanced",
            CreateRecipeType.MIXING,
            HeatLevel.SUPERHEATED,
            new ItemLike[]{ Items.SUGAR, Blocks.DIRT },
            new FluidStack[]{ new FluidStack(Fluids.WATER, 500) },
            new CreateOutput[]{ CreateOutput.of(new ItemStack(Items.BLAZE_POWDER, 2)) },
            new FluidStack[]{ new FluidStack(Fluids.LAVA, 250) }
        );

        //Filling (Spouting)
        MTRecipexModRegistry.addCreateFilling("fill_glass_bottle",
            Items.GLASS_BOTTLE,
            new FluidStack(Fluids.WATER, 250),
            CreateOutput.of(new ItemStack(Items.POTION))
        );

        //Emptying (Item Drain)
        MTRecipexModRegistry.addCreateEmptying("drain_water_bucket",
            Items.WATER_BUCKET,
            CreateOutput.of(new ItemStack(Items.BUCKET)),
            new FluidStack(Fluids.WATER, 1000)
        );

        //Deploying
        MTRecipexModRegistry.addCreateDeploying("deploy_flint_on_stone",
            Blocks.STONE, Items.FLINT,
            CreateOutput.of(new ItemStack(Items.ARROW))
        );

        //Deploying (consume/damage held item)
        MTRecipexModRegistry.addCreateDeploying("deploy_consume_log",
            Blocks.OAK_LOG, Items.IRON_AXE,
            true,
            CreateOutput.of(new ItemStack(Blocks.STRIPPED_OAK_LOG))
        );

        //ItemApplication (Casing Crafting)
        MTRecipexModRegistry.addCreateItemApplication("apply_iron_on_wood",
            Blocks.OAK_LOG, Items.IRON_INGOT,
            new ItemStack(Items.SHIELD)
        );

        //MechanicalCrafting
        MTRecipexModRegistry.addCreateMechanicalCrafting("mechanical_simple_sword",
            new ItemStack(Items.DIAMOND_SWORD),
            "D",
            "D",
            "S",
            'D', Blocks.DIAMOND_BLOCK,
            'S', Items.STICK
        );

        //MechanicalCrafting (no mirrored)
        MTRecipexModRegistry.addCreateMechanicalCrafting("mechanical_strict_pickaxe",
            new ItemStack(Items.DIAMOND_PICKAXE),
            false,
            "DDD",
            " SD",
            "S D",
            'D', Items.DIAMOND,
            'S', Items.STICK
        );

        // ====================================================================
        // 4. SEQUENCED ASSEMBLY
        // ====================================================================

        MTRecipexModRegistry.addCreateSequencedAssembly("precision_diamond_assembly",
            Items.IRON_INGOT,
            Items.GOLD_INGOT,
            5,
            new CreateOutput[]{ CreateOutput.of(new ItemStack(Items.DIAMOND)) },
            MTRecipexModRegistry.stepPressing(),
            MTRecipexModRegistry.stepCutting(120),
            MTRecipexModRegistry.stepFilling(new FluidStack(Fluids.WATER, 250)),
            MTRecipexModRegistry.stepDeploying(Items.FLINT)
        );

        // ====================================================================
        // 5. CUSTOM MOD RECIPES (DIF)
        // ====================================================================

        //Distillation
        MTRecipexModRegistry.addDifDistillation("distill_water_test",
            new FluidStack(Fluids.WATER, 1000),
            new FluidStack[]{
                new FluidStack(Fluids.LAVA, 250),
                new FluidStack(Fluids.WATER, 100)
            }
        );
        
        // ====================================================================
        // 6. COMPLETELY CUSTOM RAW JSON RECIPE
        // ====================================================================

        JsonObject customJson = new JsonObject();
        customJson.addProperty("type", "examplemod:freezing");

        JsonObject ingredientObj = new JsonObject();
        ingredientObj.addProperty("item", "minecraft:water_bucket");
        customJson.add("ingredient", ingredientObj);

        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("id", "minecraft:ice");
        resultObj.addProperty("count", 1);
        customJson.add("result", resultObj);

        MTRecipexModRegistry.addCustom("custom_raw_freezing_ice", customJson);
    }
}