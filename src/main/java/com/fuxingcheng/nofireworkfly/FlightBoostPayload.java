package com.fuxingcheng.nofireworkfly;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record FlightBoostPayload() implements CustomPacketPayload {

    public static final Type<FlightBoostPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(NoFireWorkFly.MODID, "flight_boost"));

    public static final StreamCodec<FriendlyByteBuf, FlightBoostPayload> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FlightBoostPayload decode(FriendlyByteBuf buf) { return new FlightBoostPayload(); }
        @Override
        public void encode(FriendlyByteBuf buf, FlightBoostPayload payload) {}
    };

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handleServer(final FlightBoostPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() != null) {
                FlightHelper.consumeFlightResource(context.player());
            }
        });
    }
}
