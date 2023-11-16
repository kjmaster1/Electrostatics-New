package com.kjmaster.electrostatics.treetap;

import com.kjmaster.electrostatics.setup.Registration;
import com.kjmaster.kjlib.datagen.DataGen;
import com.kjmaster.kjlib.datagen.Dob;
import com.kjmaster.kjlib.items.BaseItem;
import com.kjmaster.kjlib.modules.IModule;
import cpw.mods.modlauncher.EnumerationHelper;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import static com.kjmaster.electrostatics.Electrostatics.tab;

public class TreeTapModule implements IModule {

    public static final RegistryObject<TreeTapItem> TREE_TAP = Registration.ITEMS.register("treetap", tab(TreeTapItem::new));

    public static final RegistryObject<BaseItem> RESIN = Registration.ITEMS.register("resin", tab(() -> new BaseItem(new Item.Properties())));

    public static final RegistryObject<BaseItem> RUBBER = Registration.ITEMS.register("rubber", tab(() -> new BaseItem(new Item.Properties())));

    public static final ArmorMaterial rubberArmorMaterial = new ArmorMaterial() {
        @Override
        public int getDurabilityForType(ArmorItem.Type pType) {
            return ArmorMaterials.LEATHER.getDurabilityForType(ArmorItem.Type.BOOTS);
        }

        @Override
        public int getDefenseForType(ArmorItem.Type pType) {
            return ArmorMaterials.IRON.getDefenseForType(ArmorItem.Type.BOOTS);
        }

        @Override
        public int getEnchantmentValue() {
            return ArmorMaterials.LEATHER.getEnchantmentValue();
        }

        @Override
        public SoundEvent getEquipSound() {
            return ArmorMaterials.LEATHER.getEquipSound();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(RUBBER.get());
        }

        @Override
        public String getName() {
            return "electrostatics:rubber";
        }

        @Override
        public float getToughness() {
            return ArmorMaterials.IRON.getToughness();
        }

        @Override
        public float getKnockbackResistance() {
            return ArmorMaterials.IRON.getKnockbackResistance();
        }
    };

    public static final RegistryObject<ArmorItem> RUBBER_BOOTS = Registration.ITEMS.register("rubber_boots", tab(() -> new ArmorItem(rubberArmorMaterial, ArmorItem.Type.BOOTS, new Item.Properties())));

    @Override
    public void init(FMLCommonSetupEvent fmlCommonSetupEvent) {

    }

    @Override
    public void initClient(FMLClientSetupEvent fmlClientSetupEvent) {

    }

    @Override
    public void initDatagen(DataGen dataGen) {
        dataGen.add(
                Dob.itemBuilder(RUBBER)
                        .smelting(cookingRecipeBuilder -> cookingRecipeBuilder.unlockedBy("has_resin", InventoryChangeTrigger.TriggerInstance.hasItems(RESIN.get())),
                                Ingredient.of(new ItemStack(RESIN.get())), 0.3F, 200
                        ),
                Dob.itemBuilder(TREE_TAP)
                        .shaped(shapedRecipeBuilder -> shapedRecipeBuilder
                                .unlockedBy("has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT)),
                                " i ", "iii", "i  "
                        ),
                Dob.itemBuilder(RUBBER_BOOTS)
                        .shaped(shapedRecipeBuilder -> shapedRecipeBuilder
                                .define('u', Registration.RUBBER)
                                        .unlockedBy("rubber", InventoryChangeTrigger.TriggerInstance.hasItems(TreeTapModule.RUBBER.get())),
                                "u u", "u u"
                        )
        );
    }
}
