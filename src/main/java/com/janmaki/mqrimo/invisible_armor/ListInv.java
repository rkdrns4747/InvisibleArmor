package com.janmaki.mqrimo.invisible_armor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
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
    private static CustomConfiguration sfile;
    private static FileConfiguration save;

    ListInv(Main main,CustomConfiguration sfile) {
        this.main = main;
        map = Core.get();

        this.sfile = sfile;
        save = this.sfile.getConfig();
    }



    static void listHub(Player player) {
        ItemStack playerSkull = getSkull(player);
        ItemMeta meta = playerSkull.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD+"Players you can not see armor.");
        playerSkull.setItemMeta(meta);
        ItemStack mainSkull = new ItemStack(Material.SKULL_ITEM, 1);
        SkullMeta skullMeta = (SkullMeta) mainSkull.getItemMeta();
        UUID defaultUniqueId = UUID.fromString("8667ba71-b85a-4004-af54-457a9734eed7");
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(defaultUniqueId));
        mainSkull.setItemMeta(skullMeta);
        ItemStack defaultSkull = mainSkull;
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
            if (p  == null) {
                continue;
            }
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
                if (p == null) {
                    continue;
                }
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
            if (event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                ItemStack item = event.getCurrentItem();
                Player headPlayer = Bukkit.getPlayer(item.getItemMeta().getLore().get(0));
                Set<Player> set = Core.get(player);
                set.remove(headPlayer);
                map.put(player,set);

                List<String> list = new ArrayList<>();
                for(Player sp:set) {
                    if (sp == null) {
                        continue;
                    }
                    list.add(sp.getDisplayName());
                }
                save.set(player.getDisplayName(),list);
                sfile.saveConfig();
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
            if (event.getCurrentItem().getType() == Material.SKULL_ITEM) {
                ItemStack item = event.getCurrentItem();
                Player headPlayer = Bukkit.getPlayer(item.getItemMeta().getLore().get(0));
                Set<Player> set = Core.get(headPlayer);
                set.remove(player);
                map.put(headPlayer,set);

                List<String> list = new ArrayList<>();
                for(Player sp:set) {
                    if (sp == null) {
                        continue;
                    }
                    list.add(sp.getDisplayName());
                }
                save.set(player.getDisplayName(),list);
                sfile.saveConfig();
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
        ItemStack playerSkull = new ItemStack(Material.SKULL_ITEM,1);
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
