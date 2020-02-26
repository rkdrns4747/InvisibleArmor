package com.janmaki.mqrimo.invisible_armor;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends JavaPlugin {
    private static Map<Player, Map<String, Boolean>> map;
    private static Main instance;
    private static File saveFile;
    private static YamlConfiguration yamlConfiguration;
    @Override
    public void onEnable() {
        new Regularly(this);
        instance = this;
        saveFile = new File(this.getDataFolder(), "save.yml");
        if(!saveFile.exists()) {
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.getStackTrace();
            }
        }
        Bukkit.getServer().getPluginManager().registerEvents(new JoinEvent(saveFile),this);
        new Core(saveFile);
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
        if (args[0].equalsIgnoreCase("toggle")) {
            if (!(player.hasPermission("invArmor.*") || player.hasPermission("invArmor.command.toggle") || player.hasPermission("invArmor.command.*"))) {
                player.sendMessage(ChatColor.RED+"You don't have enough permission, or you haven't buy this feature!");
                return true;
            }
            if(Core.offInvArmor(player)){
                player.sendMessage(ChatColor.GREEN+"Your armor has become visible.");
            }else{
                Core.invArmor(player);
                player.sendMessage(ChatColor.GREEN+"Your armor has become invisible.");
            }

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
        player.sendMessage(ChatColor.DARK_GREEN + "=---------------------------------=");
        player.sendMessage(ChatColor.GREEN + "/invArmor toggle" + ChatColor.DARK_GREEN + ": " + ChatColor.GREEN + "Invisibility of other all your armor.");
        player.sendMessage(ChatColor.DARK_GREEN + "=---------------------------------=");
    }
    public static Main getInstance(){
        return instance;
    }
}
