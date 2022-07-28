package fun.fifu.simplebloodvolumedisplay;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ActionbarUtil {
    private static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf(".") + 1);
    private static Class<?> craftPlayerClass;
    private static Constructor<?> ppoc;
    private static Class<?> packet;
    private static Class<?> chat;
    private static Class<?> chatBaseComponent;

    public static void sendMessage(Player player, String message) {
        if (player == null || message == null) {
            return;
        }
        if (!NMS_VERSION.startsWith("v1_9_R") && !NMS_VERSION.startsWith("v1_8_R")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            return;
        }
        try {
            Object craftPlayer = craftPlayerClass.cast(player);
            Object object = NMS_VERSION.equalsIgnoreCase("v1_8_R1") ? chatBaseComponent.cast(chat.getDeclaredMethod("a", String.class).invoke(chat, "{'text': '" + message + "'}")) : chat.getConstructor(String.class).newInstance(message);
            Object packetPlayOutChat = ppoc.newInstance(object, (byte)2);
            Method handle = craftPlayerClass.getDeclaredMethod("getHandle");
            Object iCraftPlayer = handle.invoke(craftPlayer);
            Field playerConnectionField = iCraftPlayer.getClass().getDeclaredField("playerConnection");
            Object playerConnection = playerConnectionField.get(iCraftPlayer);
            Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", packet);
            sendPacket.invoke(playerConnection, packetPlayOutChat);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static {
        try {
            if (NMS_VERSION.startsWith("v1_8_R") || NMS_VERSION.startsWith("v1_9_R")) {
                craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + NMS_VERSION + ".entity.CraftPlayer");
                chatBaseComponent = Class.forName("net.minecraft.server." + NMS_VERSION + ".IChatBaseComponent");
                ppoc = Class.forName("net.minecraft.server." + NMS_VERSION + ".PacketPlayOutChat").getConstructor(chatBaseComponent, Byte.TYPE);
                packet = Class.forName("net.minecraft.server." + NMS_VERSION + ".Packet");
                chat = Class.forName("net.minecraft.server." + NMS_VERSION + (NMS_VERSION.equalsIgnoreCase("v1_8_R1") ? ".ChatSerializer" : ".ChatComponentText"));
            }
        }
        catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
