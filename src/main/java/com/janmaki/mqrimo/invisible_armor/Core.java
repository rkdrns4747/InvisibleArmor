package com.janmaki.mqrimo.invisible_armor;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

class Core {
    private static Map<Player, Set<Player>> map = new HashMap<>();
    private static CustomConfiguration sfile;
    private static FileConfiguration save;


    Core(CustomConfiguration sfile) {
        this.sfile = sfile;
        this.save = this.sfile.getConfig();
        map = Core.get();
    }

    static void invArmor(Player player,Player victim) {
        invArmor(player,victim ,true);
    }

    static void invArmor(Player player,Player victim ,boolean b) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        if (victim == null) {
            return;
        }
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

        if (b) {
            return;
        }
        List<String> list = new ArrayList<>();
        for(Player sp:set) {
            if (sp == null) {
                continue;
            }
            list.add(sp.getDisplayName());
        }
        save.set(player.getDisplayName(),list);
        sfile.saveConfig();
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

        Set set = new HashSet();
        set = get(player);
        save.set(player.getDisplayName(),set);
        sfile.saveConfig();
    }
}
