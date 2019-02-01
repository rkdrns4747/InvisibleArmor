package com.janmaki.mqrimo.invisible_armor;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {
    private static Map<Player, Set<Player>> map;

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new ListInv(this),this);
        new Regularly(this);
        map = Core.get();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(label.equalsIgnoreCase("invArmor"))) {
            return true;
        }
        if (!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED+"*Cant be executed from the console");
            return true;
        }
        if (args.length < 1) {
            if (sender.hasPermission("invArmor.*")) {
                sendHelp((Player) sender);
            }
            return true;
        }
        Player player = (Player) sender;
        if (!(player.hasPermission("invArmor.*"))) {
            player.sendMessage(ChatColor.RED+"*Permission Error.( invArmor.* )");
            return true;
        }
        List<Player> players = player.getWorld().getPlayers();
        if (args[0].equalsIgnoreCase("all")) {
            for (Player p: players) {
                Core.invArmor(player,p);
            }
            player.sendMessage(ChatColor.GREEN+"Invisibility of other all player\'s armor.");
            return true;
        }
        if (args[0].equalsIgnoreCase("me")) {
            for (Player p: players) {
                Core.invArmor(p,player);
            }
            player.sendMessage(ChatColor.GREEN+"Your armor has become invisible.");
            return true;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            Core.reset(player);
            for (Player p: players) {
                Set set = Core.get(p);
                if (set == null) {
                    set = new HashSet();
                }
                if (set.contains(player)) {
                    set.remove(player);
                    map.put(p,set);
                }
            }
            player.sendMessage(ChatColor.GREEN+"Solved invisibility about your armor.");
            return true;
        }
        if (args[0].equalsIgnoreCase("player")) {
            if (args.length < 2) {
                return true;
            }
            Player victim = Bukkit.getPlayer(args[1]);
            if (victim == null) {
                player.sendMessage(ChatColor.RED+"Invalid player.");
                return true;
            }
            Core.invArmor(player,victim);
            player.sendMessage(ChatColor.GREEN+"Invisibility of "+victim.getDisplayName()+"\'s armor.");
            return true;
        }
        if (args[0].equalsIgnoreCase("list")) {
            ListInv.listHub(player);
            return true;
        }
        sendHelp(player);
        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(ChatColor.DARK_GREEN+"=---------------------------------=");
        player.sendMessage( ChatColor.GREEN+"/invArmor player %player%"+ChatColor.DARK_GREEN+": "+ChatColor.GREEN+"Invisibility of Player's armor.");
        player.sendMessage( ChatColor.GREEN+"/invArmor all"+ChatColor.DARK_GREEN+": "+ChatColor.GREEN+"Invisibility of other all player's armor.");
        player.sendMessage( ChatColor.GREEN+"/invArmor me"+ChatColor.DARK_GREEN+": "+ChatColor.GREEN+"Invisibility of other all your armor.");
        player.sendMessage( ChatColor.GREEN+"/invArmor reset"+ChatColor.DARK_GREEN+": "+ChatColor.GREEN+"Solved invisibility about your armor.");
        player.sendMessage( ChatColor.GREEN+"/invArmor list"+ChatColor.DARK_GREEN+": "+ChatColor.GREEN+"Open the GUI for management.");
        player.sendMessage(ChatColor.DARK_GREEN+"=---------------------------------=");
    }
}
