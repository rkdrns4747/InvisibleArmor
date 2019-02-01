package com.janmaki.mqrimo.invisible_armor;

import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumItemSlot;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityEquipment;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Core {
    private static Map<Player, Set<Player>> map = new HashMap<>();

    static void invArmor(Player player,Player victim) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        if (victim.equals(player)) {
            return;
        }
        EntityPlayer entityPlayer = ((CraftPlayer) victim).getHandle();
        ItemStack itemStack = new ItemStack(Material.AIR);
        PacketPlayOutEntityEquipment head = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStack));
        craftPlayer.getHandle().playerConnection.sendPacket(head);
        PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(entityPlayer.getId(),EnumItemSlot.CHEST,CraftItemStack.asNMSCopy(itemStack));
        craftPlayer.getHandle().playerConnection.sendPacket(chest);
        PacketPlayOutEntityEquipment feet = new PacketPlayOutEntityEquipment(entityPlayer.getId(),EnumItemSlot.FEET,CraftItemStack.asNMSCopy(itemStack));
        craftPlayer.getHandle().playerConnection.sendPacket(feet);
        PacketPlayOutEntityEquipment legs = new PacketPlayOutEntityEquipment(entityPlayer.getId(),EnumItemSlot.LEGS,CraftItemStack.asNMSCopy(itemStack));
        craftPlayer.getHandle().playerConnection.sendPacket(legs);
        Set<Player> set = map.get(player);
        if (set == null) {
            set = new HashSet<>();
        }
        set.add(victim);
        map.put(player,set);
    }

    static Map<Player, Set<Player>> get(){
        return map;
    }

    static Set<Player> get(Player player) {
        return map.get(player);
    }

    static void put(Player player,Set<Player> set) {
        map.put(player,set);
    }

    static void reset(Player player) {
        map.remove(player);
    }
}
