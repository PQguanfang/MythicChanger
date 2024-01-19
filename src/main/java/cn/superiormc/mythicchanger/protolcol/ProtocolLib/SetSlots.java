package cn.superiormc.mythicchanger.protolcol.ProtocolLib;

import cn.superiormc.mythicchanger.MythicChanger;
import cn.superiormc.mythicchanger.manager.ConfigManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;

public class SetSlots extends GeneralPackets {

    public SetSlots() {
        super();
    }
    @Override
    protected void initPacketAdapter(){
        packetAdapter = new PacketAdapter(MythicChanger.instance, ListenerPriority.LOWEST, PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                if (player.getGameMode() == GameMode.CREATIVE) {
                    return;
                }
                PacketContainer packet = event.getPacket();
                StructureModifier<ItemStack> itemStackStructureModifier = packet.getItemModifier();
                ItemStack serverItemStack = itemStackStructureModifier.read(0);
                if (serverItemStack == null || serverItemStack.getType().isAir()) {
                    return;
                }
                int windowID = packet.getIntegers().read(0);
                int slot = packet.getIntegers().read(packet.getIntegers().size() - 1);
                ItemStack clientItemStack = ConfigManager.configManager.startFakeChange(serverItemStack, player);
                // client 是加过 Lore 的，server 是没加过的！
                itemStackStructureModifier.write(0, clientItemStack);
                Bukkit.getConsoleSender().sendMessage(windowID + "");
                if (slot < 5 || slot > 44) {
                    return;
                }
                int spigotSlot;
                if (slot >= 36) {
                    spigotSlot = slot - 36;
                } else if (slot <= 8) {
                    spigotSlot = slot + 31;
                } else {
                    spigotSlot = slot;
                }
                ItemStack tempItemStack = event.getPlayer().getInventory().getItem(spigotSlot);
                if (tempItemStack == null || tempItemStack.getType().isAir()) {
                    return;
                }
                ItemStack newItem = ConfigManager.configManager.startRealChange(tempItemStack, player);
                if (newItem != null) {
                    Bukkit.getScheduler().callSyncMethod(MythicChanger.instance, () -> {
                        player.getInventory().setItem(spigotSlot, newItem);
                        return null;
                    });
                }
            }
        };
    }
}
