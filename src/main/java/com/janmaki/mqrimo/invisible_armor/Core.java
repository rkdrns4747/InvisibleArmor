package com.janmaki.mqrimo.invisible_armor;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Core {
    private static Map<Player, Map<String, Boolean>> map = new HashMap<>();
    private static CustomConfiguration sfile;
    private static FileConfiguration save;


    Core(CustomConfiguration sfile) {
        this.sfile = sfile;
        this.save = this.sfile.getConfig();
        map = Core.get();
    }

    public static void invArmor(Player victim) {
        invArmor(victim ,true);
    }

    public static void invArmor(Player victim ,boolean b) {
        EntityPlayer entityPlayer = ((CraftPlayer) victim).getHandle();

        List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
        for(Player p:players) {
            CraftPlayer craftPlayer = (CraftPlayer) p;
            ItemStack itemStack = new ItemStack(Material.AIR);
            PacketPlayOutEntityEquipment head = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStack));
            craftPlayer.getHandle().playerConnection.sendPacket(head);
            PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(itemStack));
            craftPlayer.getHandle().playerConnection.sendPacket(chest);
            PacketPlayOutEntityEquipment feet = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(itemStack));
            craftPlayer.getHandle().playerConnection.sendPacket(feet);
            PacketPlayOutEntityEquipment legs = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(itemStack));
            craftPlayer.getHandle().playerConnection.sendPacket(legs);
        }



        Map<String, Boolean> set = map.get(victim);
        if (set == null) {
            set = new HashMap<String, Boolean>() {
            };
        }
        set.put("isArmorInvisible", true);
        map.put(victim, set);

        /**List<String> list = new ArrayList<>();
        for(Player sp:set) {
            if (sp == null) {
                continue;
            }
            list.add(sp.getDisplayName());
        }**/
        save.set(victim.getUniqueId().toString(), set);
        sfile.saveConfig();
    }

    static Map<Player, Map<String, Boolean>> get(){
        return map;
    }

    public static Map<String, Boolean> get(Player player) {
        return map.get(player);
    }

    static void put(Player player,Map<String, Boolean> set) {
        map.put(player,set);
    }

}
