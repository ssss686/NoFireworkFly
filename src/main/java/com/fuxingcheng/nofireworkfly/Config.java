package com.fuxingcheng.nofireworkfly;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_NO_FIREWORK_FLIGHT = BUILDER
            .comment("Enable no-firework elytra flight. When disabled, elytra behaves like vanilla.")
            .define("enableNoFireworkFlight", true);

    public static final ModConfigSpec.BooleanValue ARMOR_SLOWS = BUILDER
            .comment("Whether armor slows elytra flight speed")
            .define("armorSlows", false);

    public static final ModConfigSpec.BooleanValue CAN_SLOW_FALL = BUILDER
            .comment("Allow slow falling when flight stops (sneak key)")
            .define("canSlowFall", true);

    public static final ModConfigSpec.DoubleValue MAX_SLOWED_MULTIPLIER = BUILDER
            .comment("Maximum speed reduction from armor at full armor points")
            .defineInRange("maxSlowedMultiplier", 3.0, 1.0, 10.0);

    public static final ModConfigSpec.DoubleValue WINGS_SPEED = BUILDER
            .comment("Base flight speed for elytra")
            .defineInRange("wingsSpeed", 0.01, 0.008, 0.1);

    public static final ModConfigSpec.BooleanValue CONSUME_DURABILITY = BUILDER
            .comment("Whether elytra loses durability during flight")
            .define("consumeDurability", true);

    public static final ModConfigSpec.BooleanValue CONSUME_HUNGER = BUILDER
            .comment("Whether flight consumes hunger")
            .define("consumeHunger", true);

    public static final ModConfigSpec.DoubleValue EXHAUSTION_AMOUNT = BUILDER
            .comment("Hunger exhaustion per flight tick")
            .defineInRange("exhaustionAmount", 0.03, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue REQUIRED_FOOD_AMOUNT = BUILDER
            .comment("Minimum food level required to fly")
            .defineInRange("requiredFoodAmount", 6.0, 0.0, 20.0);

    public static final ModConfigSpec.BooleanValue MAX_HEIGHT_ENABLED = BUILDER
            .comment("Whether to enforce a maximum flight height")
            .define("maxHeightEnabled", true);

    public static final ModConfigSpec.BooleanValue DROP_OUT_OF_SKY_WHEN_TIRED = BUILDER
            .comment("Stop flying when food runs out")
            .define("dropOutOfSkyWhenTired", true);

    public static final ModConfigSpec.IntValue MAX_HEIGHT_ABOVE_WORLD = BUILDER
            .comment("Maximum height above world limit for flight")
            .defineInRange("maxHeightAboveWorld", 384, 16, 384);

    static final ModConfigSpec SPEC = BUILDER.build();
}
