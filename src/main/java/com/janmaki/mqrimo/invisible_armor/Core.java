package com.janmaki.mqrimo.invisible_armor;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Core {
    private static Map<Player, Map<String, Boolean>> map = new HashMap<>();
    private static File saveFile;
    private static YamlConfiguration yamlConfiguration;


    Core(File saveFile) {
        this.saveFile = saveFile;
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(saveFile);
        map = Core.get();
    }

    public static void invArmor(Player victim) {
        invArmor(victim ,true);
    }

    public static void regulator(Player victim){
        EntityPlayer entityPlayer = ((CraftPlayer) victim).getHandle();

        List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
        for(Player p:players) {
            /**
             if(p == victim){
             continue;
             }
             **/
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
    }

    public static void invArmor(Player victim ,boolean b) {
        EntityPlayer entityPlayer = ((CraftPlayer) victim).getHandle();

        List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
        for(Player p:players) {
            /**
             if(p == victim){
             continue;
             }
             **/
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

        yamlConfiguration.set(victim.getUniqueId().toString()+".isArmorInvisible", true);
        try {
            yamlConfiguration.save(saveFile);
        }catch(IOException e){
            e.getStackTrace();
        }


            reloadData(saveFile);
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
    public static Boolean sectionalBoolean(Player player){
        Object bool = yamlConfiguration.get(player.getUniqueId().toString()+".isArmorInvisible");
        if(!(bool instanceof Boolean)){
            if(bool == null)
                return false;

            try {
                yamlConfiguration.save(saveFile);
            }catch(IOException e){
                e.getStackTrace();
            }
        }


        return (Boolean) bool;
    }
    static Boolean offInvArmor(Player player) {
        Object bool = yamlConfiguration.get(player.getUniqueId().toString()+".isArmorInvisible");
        if(!(bool instanceof Boolean)) {
            return false;
        }else if((Boolean) bool != true) {
            return false;
        }else {
            map.remove(player);

            Map<String, Boolean> set = map.get(player);
            if (set == null) {
                set = new HashMap<String, Boolean>() {
                };
            }
            set.put("isArmorInvisible", false);
            map.put(player, set);
            yamlConfiguration.set(player.getUniqueId().toString()+".isArmorInvisible", null);
            try {
                yamlConfiguration.save(saveFile);
            }catch(IOException e){
                Bukkit.getLogger().warning(e.toString());
            }
            Core.reloadData(saveFile);


            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

            List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
            ItemStack itemStackHelmet = player.getInventory().getHelmet();
            ItemStack itemStackChest = player.getInventory().getChestplate();
            ItemStack itemStackLeggings = player.getInventory().getLeggings();
            ItemStack itemStackBoots = player.getInventory().getBoots();
            for(Player p:players) {
                /**
                if(p == player){
                    continue;
                }
                **/
                CraftPlayer craftPlayer = (CraftPlayer) p;
                PacketPlayOutEntityEquipment head = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(itemStackHelmet));
                craftPlayer.getHandle().playerConnection.sendPacket(head);
                PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(itemStackChest));
                craftPlayer.getHandle().playerConnection.sendPacket(chest);
                PacketPlayOutEntityEquipment feet = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(itemStackBoots));
                craftPlayer.getHandle().playerConnection.sendPacket(feet);
                PacketPlayOutEntityEquipment legs = new PacketPlayOutEntityEquipment(entityPlayer.getId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(itemStackLeggings));
                craftPlayer.getHandle().playerConnection.sendPacket(legs);
            }
            return true;
        }
    }
    static void reloadData(File file){
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);

    }

}
