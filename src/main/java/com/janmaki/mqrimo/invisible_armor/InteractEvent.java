package com.janmaki.mqrimo.invisible_armor;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class InteractEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();

        Boolean isArmorInvisible = Core.sectionalBoolean(player);
        if(!isArmorInvisible)
            return;

        if(!event.getAction().name().toLowerCase().contains("right"))
            return;


        ItemStack heldItem = event.getItem();
        if(heldItem == null || heldItem.getType() == Material.AIR)
            return;

        boolean isInteractable = false;
        if(event.getAction().name().toLowerCase().contains("block"))
            isInteractable = interactables.contains(event.getClickedBlock().getType());

        if(isInteractable)
            return;

        ItemStack item;
        boolean check = false;
        if(heldItem.getType().name().contains("BOOT")) {
            item = player.getInventory().getArmorContents()[0];
            check = true;
        }

        else if(heldItem.getType().name().contains("LEGGING")) {
            item = player.getInventory().getArmorContents()[1];
            check = true;
        }

        else if(heldItem.getType().name().contains("CHESTPLATE")) {
            item = player.getInventory().getArmorContents()[2];
            check = true;
        }

        else if(heldItem.getType().name().contains("HELMET")) {
            item = player.getInventory().getArmorContents()[3];
            check = true;
        }else{
            item = null;
            check = false;
        }

        if(check){
            if(item != null && !item.getType().name().equals("AIR")){
                event.setCancelled(true);
                player.sendMessage(Main.prefix + ChatColor.RED + "투명 갑옷 활성화 상태에서는 마우스 우클릭을 통한 방어구 교체가 불가능합니다.");
                return;
            }
        }
    }
    private static List<Material> interactables = Arrays.asList(
            Material.ACACIA_DOOR,
            Material.ACACIA_FENCE_GATE,
            Material.ANVIL,
            Material.BEACON,
            Material.BED,
            Material.BIRCH_DOOR,
            Material.BIRCH_FENCE_GATE,
            Material.BOAT,
            Material.BOAT_ACACIA,
            Material.BOAT_BIRCH,
            Material.BOAT_DARK_OAK,
            Material.BOAT_JUNGLE,
            Material.BOAT_SPRUCE,
            Material.BREWING_STAND,
            Material.COMMAND,
            Material.CHEST,
            Material.DARK_OAK_DOOR,
            Material.DARK_OAK_FENCE_GATE,
            Material.DAYLIGHT_DETECTOR,
            Material.DAYLIGHT_DETECTOR_INVERTED,
            Material.DISPENSER,
            Material.DROPPER,
            Material.ENCHANTMENT_TABLE,
            Material.ENDER_CHEST,
            Material.FENCE_GATE,
            Material.WORKBENCH,
            Material.FURNACE,
            Material.HOPPER,
            Material.HOPPER_MINECART,
            Material.ITEM_FRAME,
            Material.JUNGLE_DOOR,
            Material.JUNGLE_FENCE_GATE,
            Material.LEVER,
            Material.MINECART,
            Material.NOTE_BLOCK,
            Material.POWERED_MINECART,
            Material.REDSTONE_COMPARATOR,
            Material.REDSTONE_COMPARATOR_OFF,
            Material.REDSTONE_COMPARATOR_ON,
            Material.SIGN,
            Material.SIGN_POST,
            Material.STORAGE_MINECART,
            Material.TRAP_DOOR,
            Material.TRAPPED_CHEST,
            Material.WALL_SIGN,
            Material.WOOD_BUTTON,
            Material.WOOD_DOOR);
}

