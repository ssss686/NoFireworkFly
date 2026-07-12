package com.fuxingcheng.nofireworkfly;

import com.illusivesoulworks.caelus.api.CaelusApi;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public final class FlightHelper {

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    private FlightHelper() {}

    public static ItemStack findElytra(Player player) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.is(Items.ELYTRA)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean hasElytra(Player player) {
        return !findElytra(player).isEmpty();
    }

    public static boolean hasValidElytra(Player player) {
        ItemStack stack = findElytra(player);
        return !stack.isEmpty() && ElytraItem.isFlyEnabled(stack);
    }

    /**
     * Checks any armour slot OR the Caelus attribute to detect elytra.
     * Works with Elytra Slot and other mods that store elytra in custom slots
     * and register it via the Caelus API.
     */
    public static boolean canElytraFly(Player player) {
        if (hasValidElytra(player)) return true;
        AttributeInstance attr = player.getAttribute(CaelusApi.getInstance().getFallFlyingAttribute());
        return attr != null && attr.getValue() > 0;
    }

    public static void applyFlightVelocity(Player player) {
        Vec3 lookAngle = player.getLookAngle();
        Vec3 delta = player.getDeltaMovement();
        double xRotDiff = Math.abs(player.getXRot() - (-90));
        double speed = Config.WINGS_SPEED.get();

        if (xRotDiff <= 15.0) {
            speed *= 2.75;
        }

        if (Config.ARMOR_SLOWS.get() && !player.getAbilities().instabuild) {
            int armorValue = player.getArmorValue();
            speed /= Math.max(1.0, (armorValue / 30.0) * Config.MAX_SLOWED_MULTIPLIER.get());
        }

        player.setDeltaMovement(delta.add(
                lookAngle.x * speed + (lookAngle.x * 1.5 - delta.x) * speed,
                lookAngle.y * speed + (lookAngle.y * 1.5 - delta.y) * speed,
                lookAngle.z * speed + (lookAngle.z * 1.5 - delta.z) * speed
        ));
    }

    public static boolean canFly(Player player) {
        if (player.getAbilities().instabuild) return true;
        return player.getFoodData().getFoodLevel() > Config.REQUIRED_FOOD_AMOUNT.get();
    }

    public static void consumeFlightResource(Player player) {
        if (!Config.CONSUME_HUNGER.get()) return;
        if (player.getAbilities().instabuild) return;
        player.causeFoodExhaustion(Config.EXHAUSTION_AMOUNT.get().floatValue());
    }
}
