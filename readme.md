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
