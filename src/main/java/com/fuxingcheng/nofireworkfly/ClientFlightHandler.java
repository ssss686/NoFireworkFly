package com.fuxingcheng.nofireworkfly;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = NoFireWorkFly.MODID, value = Dist.CLIENT)
public final class ClientFlightHandler {

    private static int hungerMessageCooldown;

    private ClientFlightHandler() {}

    @SubscribeEvent
    public static void onPlayerTickPre(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof LocalPlayer player)) return;
        if (!Config.ENABLE_NO_FIREWORK_FLIGHT.get()) return;
        if (!player.isFallFlying()) return;
        if (!FlightHelper.canElytraFly(player)) return;

        if (!FlightHelper.canFly(player)) {
            if (hungerMessageCooldown <= 0) {
                player.displayClientMessage(
                        Component.translatable("nofireworkfly_neo.message.too_hungry_to_fly"), true);
                hungerMessageCooldown = 40;
            }
            hungerMessageCooldown--;
            return;
        }
        hungerMessageCooldown = 0;

        if (player.zza <= 0) return;

        FlightHelper.applyFlightVelocity(player);
        PacketDistributor.sendToServer(new FlightBoostPayload());
    }
}
