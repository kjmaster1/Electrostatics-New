package com.kjmaster.electrostatics.staticgenerator;

import com.kjmaster.electrostatics.setup.Registration;
import com.kjmaster.electrostatics.staticgenerator.client.GuiStaticGenerator;
import com.kjmaster.electrostatics.treetap.TreeTapModule;
import com.kjmaster.kjlib.blocks.BaseBlock;
import com.kjmaster.kjlib.container.GenericContainer;
import com.kjmaster.kjlib.datagen.DataGen;
import com.kjmaster.kjlib.datagen.Dob;
import com.kjmaster.kjlib.modules.IModule;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import static com.kjmaster.electrostatics.Electrostatics.tab;
import static com.kjmaster.electrostatics.setup.Registration.*;

public class StaticGeneratorModule implements IModule {

    public static final RegistryObject<BaseBlock> STATICGENERATOR = BLOCKS.register("static_generator", StaticGeneratorTileEntity::createBlock);

    public static final RegistryObject<Item> STATICGENERATOR_ITEM = ITEMS.register("static_generator", tab(() -> new BlockItem(STATICGENERATOR.get(), Registration.createStandardProperties())));

    public static final RegistryObject<BlockEntityType<?>> TYPE_STATICGENERATOR = TILES.register("static_generator", () -> BlockEntityType.Builder.of(StaticGeneratorTileEntity::new, STATICGENERATOR.get()).build(null));

    public static final RegistryObject<MenuType<GenericContainer>> CONTAINER_STATICGENERATOR = CONTAINERS.register("static_generator", GenericContainer::createContainerType);


    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
           GuiStaticGenerator.register();
        });
    }

    @Override
    public void initDatagen(DataGen dataGen) {
        dataGen.add(
                Dob.blockBuilder(STATICGENERATOR)
                        .stonePickaxeTags()
                        .standardLoot(TYPE_STATICGENERATOR)
                        .shaped(shapedRecipeBuilder -> shapedRecipeBuilder
                                .define('u', RUBBER)
                                .define('f', Items.FURNACE)
                                .define('s', Tags.Items.STONE)
                                .define('C', Tags.Items.INGOTS_COPPER)
                                .unlockedBy("rubber", InventoryChangeTrigger.TriggerInstance.hasItems(TreeTapModule.RUBBER.get(), Items.FURNACE)),
                                "CRC", "ufu", "sus"

                        )
        );
    }
}
