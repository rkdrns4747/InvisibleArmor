package com.janmaki.mqrimo.invisible_armor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ClickEvent implements Listener {

    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent event){
        if(!(event.getView().getTopInventory().getType().name().contains("CRAFTING")))
            return;

        if(event.getClickedInventory() instanceof PlayerInventory){
            if(!(event.getWhoClicked() instanceof Player))
                return;

            Player player = (Player) event.getWhoClicked();
            Boolean isArmorInvisible = Core.sectionalBoolean(player);
            if(!isArmorInvisible){
                return;
            }
            if(event.getSlotType() == InventoryType.SlotType.ARMOR) {
                if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT){
                    if(event.getCurrentItem() != null) {
                        player.sendMessage(Main.prefix + ChatColor.RED + "투명 갑옷 활성화 상태에서는 'SHIFT + 마우스 좌클릭'을 통한 방어구 탈의가 불가능합니다.");
                        event.setCancelled(true);
                        return;
                    }
                }
                if(event.getClick() == ClickType.NUMBER_KEY){
                    if(event.getCurrentItem() != null) {
                        if(event.getInventory().getItem(event.getHotbarButton()) != null)
                            player.sendMessage(Main.prefix + ChatColor.RED + "투명 갑옷 활성화 상태에서는 숫자 키를 통한 방어구 교체가 불가능합니다.");
                        else
                            player.sendMessage(Main.prefix + ChatColor.RED + "투명 갑옷 활성화 상태에서는 숫자 키를 통한 방어구 탈의가 불가능합니다.");

                        event.setCancelled(true);
                        return;
                    }
                }

            }else if(event.getSlotType() == InventoryType.SlotType.CONTAINER || event.getSlotType() == InventoryType.SlotType.QUICKBAR){
                if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT){
                    ItemStack item;
                    boolean check = false;
                    if(event.getCurrentItem().getType().name().contains("BOOT")) {
                        item = ((PlayerInventory) event.getClickedInventory()).getArmorContents()[0];
                        check = true;
                    }

                    else if(event.getCurrentItem().getType().name().contains("LEGGING")) {
                        item = ((PlayerInventory) event.getClickedInventory()).getArmorContents()[1];
                        check = true;
                    }

                    else if(event.getCurrentItem().getType().name().contains("CHESTPLATE")) {
                        item = ((PlayerInventory) event.getClickedInventory()).getArmorContents()[2];
                        check = true;
                    }

                    else if(event.getCurrentItem().getType().name().contains("HELMET")) {
                        item = ((PlayerInventory) event.getClickedInventory()).getArmorContents()[3];
                        check = true;
                    }else{
                        item = null;
                        check = false;
                    }

                    if(check){
                        if(item != null && !item.getType().name().equals("AIR")){
                            event.setCancelled(true);
                            player.sendMessage(Main.prefix + ChatColor.RED + "투명 갑옷 활성화 상태에서는 'SHIFT + 마우스 좌클릭'을 통한 방어구 교체가 불가능합니다.");
                            return;
                        }
                    }
                }
            }
        }
    }

}
