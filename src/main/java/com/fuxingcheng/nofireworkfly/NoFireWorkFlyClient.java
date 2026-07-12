package com.fuxingcheng.nofireworkfly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod(value = NoFireWorkFly.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = NoFireWorkFly.MODID, value = Dist.CLIENT)
public class NoFireWorkFlyClient {

    private static final long DOUBLE_TAP_WINDOW_MS = 300;
    private static final long DEBOUNCE_MIN_MS = 50;

    private static long lastJumpPressTime = 0;
    private static boolean jumpWasPressed = false;

    public NoFireWorkFlyClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (mc.screen != null) return;

        if (!Config.ENABLE_NO_FIREWORK_FLIGHT.get()) return;

        boolean jumpPressed = mc.options.keyJump.isDown();

        if (jumpPressed && !jumpWasPressed) {
            long now = System.currentTimeMillis();
            long gap = now - lastJumpPressTime;

            if (gap < DOUBLE_TAP_WINDOW_MS && gap > DEBOUNCE_MIN_MS) {
                if (!FlightHelper.canFly(player)) {
                    player.displayClientMessage(
                            Component.translatable("nofireworkfly_neo.message.too_hungry_to_fly"), true);
                } else if (canTakeoff(player)) {
                    PacketDistributor.sendToServer(new ElytraTakeoffPayload());
                    lastJumpPressTime = 0;
                    return;
                }
            }
            lastJumpPressTime = now;
        }
        jumpWasPressed = jumpPressed;
    }

    private static boolean canTakeoff(LocalPlayer player) {
        if (player.isFallFlying()) return false;
        if (player.isInWater() || player.isInLava()) return false;
        if (player.getAbilities().flying) return false;
        return FlightHelper.canElytraFly(player);
    }
}
