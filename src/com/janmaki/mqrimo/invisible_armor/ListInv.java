package com.janmaki.mqrimo.invisible_armor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

class ListInv implements Listener {
    private static Main main;
    private static Map<Player, Set<Player>> map;


    ListInv(Main main) {
        this.main = main;
        map = Core.get();
    }



    static void listHub(Player player) {
        ItemStack playerSkull = getSkull(player);
        ItemMeta meta = playerSkull.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD+"Players you can not see armor.");
        playerSkull.setItemMeta(meta);

        ItemStack defaultSkull = new ItemStack(Material.PLAYER_HEAD,1);
        meta = defaultSkull.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN+"Players who can not see your armor.");
        defaultSkull.setItemMeta(meta);

        Inventory inv = Bukkit.createInventory(player,27, "Invisible Armor");
        inv.setItem(11,defaultSkull);
        inv.setItem(15,playerSkull);

        putCloseItem(inv);

        openInv(player,inv);
    }

    @EventHandler
    private void listHubClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getTitle().equalsIgnoreCase("Invisible Armor")) {
            event.setCancelled(true);
            if ((event.getCurrentItem() == null) || (event.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }
            player.closeInventory();
            if (event.getSlot() == 15) {
                menu1(player);
            }
            if (event.getSlot() == 11) {
                menu2(player);
            }
            if (event.getSlot() == 18) {
                player.closeInventory();
            }
        }
    }

    private static void menu1(Player player) {
        Inventory inv = Bukkit.createInventory(player,54, "Invisible Armor => OtherPlayer");
        Set<Player> players = Core.get(player);
        if (players == null) {
            players = new HashSet<>();
        }
        for (Player p: players) {
            ItemStack skull = setLore(getSkull(p),p);
            inv.addItem(skull);
        }
        putBackItem(inv);
        openInv(player,inv);
    }

    private static void menu2(Player player) {
        Inventory inv = Bukkit.createInventory(player,54, "Invisible Armor => you");
        List<Player> players = player.getWorld().getPlayers();
        for (Player p: players) {
            Set set = Core.get(p);
            if (set == null) {
                set = new HashSet();
            }
            if (set.contains(player)) {
                ItemStack skull = setLore(getSkull(p),p);
                inv.addItem(skull);
            }
        }
        putBackItem(inv);
        openInv(player,inv);
    }

    @EventHandler
    private void menuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getTitle().equalsIgnoreCase("Invisible Armor => OtherPlayer")) {
            event.setCancelled(true);
            if ((event.getCurrentItem() == null) || (event.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }
            if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                ItemStack item = event.getCurrentItem();
                Player headPlayer = Bukkit.getPlayer(item.getItemMeta().getLore().get(0));
                Set<Player> set = Core.get(player);
                set.remove(headPlayer);
                map.put(player,set);
                menu1(player);
            }
            if (event.getCurrentItem().getType() == Material.SLIME_BALL) {
                listHub(player);
            }
        }

        if (event.getInventory().getTitle().equalsIgnoreCase("Invisible Armor => you")) {
            event.setCancelled(true);
            if ((event.getCurrentItem() == null) || (event.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }
            if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                ItemStack item = event.getCurrentItem();
                Player headPlayer = Bukkit.getPlayer(item.getItemMeta().getLore().get(0));
                Set<Player> set = Core.get(headPlayer);
                set.remove(player);
                map.put(headPlayer,set);
                menu2(player);
            }
            if (event.getCurrentItem().getType() == Material.SLIME_BALL) {
                listHub(player);
            }
        }
    }


    private static void openInv(Player player,Inventory inv) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(inv);
            }
        }.runTaskLater(main, 1L);
    }

    private static ItemStack getSkull(Player player) {
        ItemStack playerSkull = new ItemStack(Material.PLAYER_HEAD,1);
        SkullMeta pSkullMeta = (SkullMeta) playerSkull.getItemMeta();
        pSkullMeta.setOwningPlayer(player);
        pSkullMeta.setLocalizedName(ChatColor.stripColor(player.getDisplayName()));
        playerSkull.setItemMeta(pSkullMeta);
        return playerSkull;
    }

    private static ItemStack setLore(ItemStack item,Player player) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(player.getDisplayName());
        lore.add(ChatColor.AQUA+"Click to delete!");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }


    private static void putBackItem(Inventory inv) {
        ItemStack back = new ItemStack(Material.SLIME_BALL);
        ItemMeta meta = back.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY+"[ MenuHub ]");
        back.setItemMeta(meta);
        inv.setItem(45,back);
    }

    private static void putCloseItem(Inventory inv) {
        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta meta = back.getItemMeta();
        meta.setDisplayName(ChatColor.RED+"CLOSE");
        back.setItemMeta(meta);
        inv.setItem(18,back);
    }
}
