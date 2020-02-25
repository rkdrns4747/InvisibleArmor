package com.janmaki.mqrimo.invisible_armor;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {
    private static Map<Player, Map<String, Boolean>> map;
    private static CustomConfiguration sfile;
    private static FileConfiguration save;
    @Override
    public void onEnable() {
        new Regularly(this);


        sfile = new CustomConfiguration(this,"save.yml");
        sfile.saveDefaultConfig();
        save = sfile.getConfig();
       //Bukkit.getServer().getPluginManager().registerEvents(new ListInv(this,sfile),this);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinEvent(sfile),this);

        new Core(sfile);
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
        List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
        if (args[0].equalsIgnoreCase("me")) {
            if (!(player.hasPermission("invArmor.*") || player.hasPermission("invArmor.me"))) {
                player.sendMessage(ChatColor.RED+"*Permission Error.( invArmor.* )");
                return true;
            }
            for (Player p: players) {
                Core.invArmor(player);
            }
            player.sendMessage(ChatColor.GREEN+"Your armor has become invisible.");
            return true;
        }
        /**if (args[0].equalsIgnoreCase("reset")) {
            if (!(player.hasPermission("invArmor.*") || player.hasPermission("invArmor.reset"))) {
                player.sendMessage(ChatColor.RED+"*Permission Error.( invArmor.* )");
                return true;
            }
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
        }**/
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
