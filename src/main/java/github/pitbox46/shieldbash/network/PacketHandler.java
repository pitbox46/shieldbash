package github.pitbox46.shieldbash.network;

import com.omircon.shield_bash.network.ShieldBashPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("shieldbash", "enchantconfig"), () -> "1.5.2", (s) -> true, (s) -> true);
    private static int ID = 0;

    public static void registerMessages() {
        CHANNEL.registerMessage(
                ID++,
                ShieldBashPacket.class,
                (msg, pb) -> {},
                pb -> new ShieldBashPacket(),
                ShieldBashPacket::handle
        );
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        CHANNEL.sendTo(packet, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        CHANNEL.sendTo(packet, Minecraft.getInstance().player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_SERVER);
    }
}
