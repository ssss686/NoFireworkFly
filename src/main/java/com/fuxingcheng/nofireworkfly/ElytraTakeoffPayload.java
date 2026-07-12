package com.fuxingcheng.nofireworkfly;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ElytraTakeoffPayload() implements CustomPacketPayload {

    public static final Type<ElytraTakeoffPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(NoFireWorkFly.MODID, "elytra_takeoff"));

    public static final StreamCodec<FriendlyByteBuf, ElytraTakeoffPayload> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ElytraTakeoffPayload decode(FriendlyByteBuf buf) { return new ElytraTakeoffPayload(); }
        @Override
        public void encode(FriendlyByteBuf buf, ElytraTakeoffPayload payload) {}
    };

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handleServer(final ElytraTakeoffPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ServerFlightHandler.performGroundTakeoff(player);
            }
        });
    }
}
