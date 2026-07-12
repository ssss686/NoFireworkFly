package com.fuxingcheng.nofireworkfly;

import com.illusivesoulworks.caelus.api.CaelusApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class ServerFlightHandler {

    private static final double TAKEOFF_VELOCITY = 0.2;
    private static final double TAKEOFF_FORWARD = 0.125;

    private static final ResourceLocation ELYTRA_FLIGHT_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(NoFireWorkFly.MODID, "elytra_flight");
    private static final AttributeModifier ELYTRA_FLIGHT_MODIFIER =
            new AttributeModifier(ELYTRA_FLIGHT_MODIFIER_ID, 1.0, AttributeModifier.Operation.ADD_VALUE);

    private static int savedDurability = -1;

    private ServerFlightHandler() {}

    @SubscribeEvent
    public static void onPlayerTickPre(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        manageCaelusModifier(player);

        if (!Config.ENABLE_NO_FIREWORK_FLIGHT.get()) return;
        if (!player.isFallFlying()) return;

        if (!FlightHelper.canElytraFly(player)) {
            player.stopFallFlying();
            return;
        }

        if (!Config.CONSUME_DURABILITY.get()) {
            ItemStack elytra = FlightHelper.findElytra(player);
            savedDurability = elytra.isEmpty() ? -1 : elytra.getDamageValue();
        }

        if (Config.CAN_SLOW_FALL.get()) {
            if (player.isShiftKeyDown() && !player.getAbilities().flying) {
                player.stopFallFlying();
                player.addEffect(new MobEffectInstance(
                        MobEffects.SLOW_FALLING, 20, 0, false, false, true));
                return;
            }
        }

        if (Config.MAX_HEIGHT_ENABLED.get() && !player.getAbilities().instabuild
                && player.getY() > player.level().getMaxBuildHeight() + Config.MAX_HEIGHT_ABOVE_WORLD.get()) {
            player.stopFallFlying();
            return;
        }

        if (Config.DROP_OUT_OF_SKY_WHEN_TIRED.get() && !FlightHelper.canFly(player)) {
            player.stopFallFlying();
            return;
        }

    }

    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        if (Config.CONSUME_DURABILITY.get()) return;
        if (!player.isFallFlying() || savedDurability < 0) return;

        ItemStack elytra = FlightHelper.findElytra(player);
        if (!elytra.isEmpty()) {
            elytra.setDamageValue(savedDurability);
        }
        savedDurability = -1;
    }

    private static void manageCaelusModifier(Player player) {
        AttributeInstance attr = player.getAttribute(CaelusApi.getInstance().getFallFlyingAttribute());
        if (attr == null) return;

        if (!Config.ENABLE_NO_FIREWORK_FLIGHT.get()) {
            attr.removeModifier(ELYTRA_FLIGHT_MODIFIER_ID);
            return;
        }

        boolean hasElytra = FlightHelper.hasValidElytra(player);

        if (attr.hasModifier(ELYTRA_FLIGHT_MODIFIER_ID)) {
            if (!hasElytra || !player.isFallFlying()) {
                attr.removeModifier(ELYTRA_FLIGHT_MODIFIER_ID);
            }
        } else {
            if (hasElytra) {
                attr.addTransientModifier(ELYTRA_FLIGHT_MODIFIER);
            }
        }
    }

    // ==================== 地面起飞 ====================

    public static void performGroundTakeoff(ServerPlayer player) {
        if (!Config.ENABLE_NO_FIREWORK_FLIGHT.get()) return;
        if (player.isFallFlying()) return;

        if (!FlightHelper.canElytraFly(player)) return;
        if (player.isInWater() || player.isInLava()) return;
        if (player.hasEffect(MobEffects.LEVITATION)) return;
        if (player.getAbilities().flying) return;

        player.setSharedFlag(7, true);

        Vec3 look = player.getLookAngle();
        player.setDeltaMovement(
                look.x * TAKEOFF_FORWARD,
                TAKEOFF_VELOCITY,
                look.z * TAKEOFF_FORWARD
        );

        player.level().broadcastEntityEvent(player, (byte) 36);
    }
}
