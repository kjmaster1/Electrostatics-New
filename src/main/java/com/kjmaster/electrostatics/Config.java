package com.kjmaster.electrostatics;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Electrostatics.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.LongValue STATIC_GENERATOR_MAX_CAPACITY = BUILDER
            .comment("The max energy capacity of the static generator")
            .defineInRange("static_generator_max_capacity", 10000, 0, Long.MAX_VALUE);

    private static final ForgeConfigSpec.LongValue STATIC_GENERATOR_RF_PER_TICK = BUILDER
            .comment("The rf per tick per entity using rubber boots on carpet that the static generator generates")
            .defineInRange("static_generator_rf_per_tick", 30, 0, Long.MAX_VALUE);

    private static final ForgeConfigSpec.LongValue STATIC_GENERATOR_CHARGE_ITEM_RF = BUILDER
            .comment("The rf per tick that the static generator can charge items with")
            .defineInRange("static_generator_charge_per_tick", 10000, 0, Long.MAX_VALUE);

    private static final ForgeConfigSpec.LongValue STATIC_GENERATOR_SEND_RF = BUILDER
            .comment("The rf per tick that the static generator can send")
            .defineInRange("static_generator_send_per_tick", 10000, 0, Long.MAX_VALUE);


    private static final ForgeConfigSpec.IntValue STATIC_GENERATOR_X_RANGE = BUILDER
            .comment("The area that the static generator will scan in the positive and negative x direction for entities with rubber boots on carpet")
            .defineInRange("static_generator_x_range", 2, 0, 20);

    private static final ForgeConfigSpec.IntValue STATIC_GENERATOR_Y_RANGE = BUILDER
            .comment("The area that the static generator will scan in the positive and negative y direction for entities with rubber boots on carpet")
            .defineInRange("static_generator_y_range", 2, 0, 20);

    private static final ForgeConfigSpec.IntValue STATIC_GENERATOR_Z_RANGE = BUILDER
            .comment("The area that the static generator will scan in the positive and negative z direction for entities with rubber boots on carpet")
            .defineInRange("static_generator_z_range", 2, 0, 20);

    private static final ForgeConfigSpec.IntValue STATIC_GENERATOR_MAX_ENTITIES = BUILDER
            .comment("The maximum amount of entities around the static generator that can generate rf")
            .defineInRange("static_generator_max_entities", 10, 1, 100);

    private static final ForgeConfigSpec.BooleanValue IS_TAP_ENABLED = BUILDER
            .comment("Toggle being able to obtain resin with the treetap, this mod's treetap is fairly overpowered and makes it easy to obtain rubber so if another mod adds a way to obtain rubber it is recommended you disable the treetap")
            .define("is_tap_enabled", true);

    private static final ForgeConfigSpec.IntValue TAP_DURABILITY = BUILDER
            .comment("The durability of the treetap")
            .defineInRange("tap_durability", 21, 1, Integer.MAX_VALUE);


    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static long staticGeneratorMaxCapacity;
    public static long staticGeneratorRfPerTick;
    public static long staticGeneratorChargePerTick;
    public static long staticGeneratorSendPerTick;
    public static long staticGeneratorXRange;
    public static long staticGeneratorYRange;
    public static long staticGeneratorZRange;
    public static long staticGeneratorMaxEntities;
    public static boolean isTapEnabled;
    public static int tapDurability;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        staticGeneratorMaxCapacity = STATIC_GENERATOR_MAX_CAPACITY.get();
        staticGeneratorRfPerTick = STATIC_GENERATOR_RF_PER_TICK.get();
        staticGeneratorChargePerTick = STATIC_GENERATOR_CHARGE_ITEM_RF.get();
        staticGeneratorSendPerTick = STATIC_GENERATOR_SEND_RF.get();
        staticGeneratorXRange = STATIC_GENERATOR_X_RANGE.get();
        staticGeneratorYRange = STATIC_GENERATOR_Y_RANGE.get();
        staticGeneratorZRange = STATIC_GENERATOR_Z_RANGE.get();
        staticGeneratorMaxEntities = STATIC_GENERATOR_MAX_ENTITIES.get();
        isTapEnabled = IS_TAP_ENABLED.get();
        tapDurability = TAP_DURABILITY.get();
    }
}
