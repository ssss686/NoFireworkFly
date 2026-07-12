package com.fuxingcheng.nofireworkfly;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

@Mod(NoFireWorkFly.MODID)
public class NoFireWorkFly {

    public static final String MODID = "nofireworkfly_neo";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NoFireWorkFly(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
            PayloadRegistrar registrar = event.registrar(MODID);
            registrar.playToServer(
                    FlightBoostPayload.TYPE,
                    FlightBoostPayload.STREAM_CODEC,
                    FlightBoostPayload::handleServer
            );
            registrar.playToServer(
                    ElytraTakeoffPayload.TYPE,
                    ElytraTakeoffPayload.STREAM_CODEC,
                    ElytraTakeoffPayload::handleServer
            );
        });

        NeoForge.EVENT_BUS.register(ServerFlightHandler.class);
        LOGGER.info("NoFireWorkFly loaded");
    }
}
