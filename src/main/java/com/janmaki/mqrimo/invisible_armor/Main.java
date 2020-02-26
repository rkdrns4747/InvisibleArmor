package com.janmaki.mqrimo.invisible_armor;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends JavaPlugin {
    private static Map<Player, Map<String, Boolean>> map;
    private static Main instance;
    private static File saveFile;
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
                player.sendMessage(ChatColor.RED+"아직 이 상품을 구매하지 않았거나, 상품을 사용할 권한을 부여받지 못했습니다.");
                return true;
            }
            if(Core.offInvArmor(player)){
                player.sendMessage(ChatColor.GREEN+"갑옷 투명화를 비활성화 했습니다.");
            }else{
                Core.invArmor(player);
                player.sendMessage(ChatColor.GREEN+"갑옷 투명화를 활성화 했습니다.");
            }

            return true;
        }
        sendHelp(player);
        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(ChatColor.DARK_GREEN + "=---------------------------------=");
        player.sendMessage(ChatColor.GREEN + "/invArmor toggle" + ChatColor.DARK_GREEN + ": " + ChatColor.GREEN + "갑옷 투명화를 켜거나 끕니다.");
        player.sendMessage(ChatColor.DARK_GREEN + "=---------------------------------=");
    }
    public static Main getInstance(){
        return instance;
    }
}
