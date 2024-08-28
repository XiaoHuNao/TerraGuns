package org.confluence.terra_guns.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.confluence.terra_guns.TerraGuns;
import org.confluence.terra_guns.common.network.c2s.GunShootingPacketC2S;

import java.util.Optional;

import static net.minecraftforge.network.NetworkRegistry.ACCEPTVANILLA;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "0.0.1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(TerraGuns.MODID, "main"),
            () -> PROTOCOL_VERSION,
            s -> s.equals(ACCEPTVANILLA) || PROTOCOL_VERSION.equals(s),
            s -> s.equals(ACCEPTVANILLA) || PROTOCOL_VERSION.equals(s)
    );
    private static int id = 0;

    public static void register() {
        NETWORK.registerMessage(id++,
                GunShootingPacketC2S.class,
                GunShootingPacketC2S::encode,
                GunShootingPacketC2S::decode,
                GunShootingPacketC2S::serverHandle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}