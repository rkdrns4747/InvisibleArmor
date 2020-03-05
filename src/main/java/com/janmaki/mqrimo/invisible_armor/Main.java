package com.janmaki.mqrimo.invisible_armor;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
    public static String prefix = ChatColor.GOLD+"[ "+ChatColor.GREEN+""+ChatColor.BOLD+"Earth Realm "+ChatColor.GOLD+"]"+ChatColor.RESET+" ";
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
        Bukkit.getServer().getPluginManager().registerEvents(new ClickEvent(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new InteractEvent(), this);
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
            if (!sender.hasPermission("invArmor.*") && !sender.hasPermission("invArmor.command.toggle") && !sender.hasPermission("invArmor.command.*")) {
                ((Player) sender).sendMessage(prefix+ChatColor.RED+"아직 이 상품을 구매하지 않았거나, 상품을 사용할 권한을 부여받지 못했습니다.");
            }else{
                sendHelp((Player) sender, prefix);
            }
            return true;
        }
        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("toggle")) {
            if (!player.hasPermission("invArmor.*") && !player.hasPermission("invArmor.command.toggle") && !player.hasPermission("invArmor.command.*")) {
                player.sendMessage(prefix+ChatColor.RED+"아직 이 상품을 구매하지 않았거나, 상품을 사용할 권한을 부여받지 못했습니다.");
                return true;

            }
            if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                player.sendMessage(prefix+ChatColor.RED + "투명 갑옷은 서바이벌에서만 사용 가능합니다. 현재 플레이어는 " + ChatColor.AQUA + player.getGameMode().name() + " " + ChatColor.RED + "상태입니다.");
                return true;
            }
            if(Core.offInvArmor(player)){
                player.sendMessage(prefix+ChatColor.GREEN+"갑옷 투명화를 비활성화 했습니다.");
            }else{
                Core.invArmor(player);
                player.sendMessage(prefix+ChatColor.GREEN+"갑옷 투명화를 활성화 했습니다.");
            }

            return true;
        }
        sendHelp(player, prefix);
        return true;
    }

    private void sendHelp(Player player, String prefix) {
        player.sendMessage(ChatColor.DARK_GREEN + "=----------------- "+prefix+ChatColor.DARK_GREEN+"----------------=");
        player.sendMessage(ChatColor.GREEN + "/invArmor toggle" + ChatColor.DARK_GREEN + ": " + ChatColor.GREEN + "갑옷 투명화를 켜거나 끕니다.");
        player.sendMessage(ChatColor.DARK_GREEN + "=-------------------------------------------------=");
    }
    public static Main getInstance(){
        return instance;
    }
}
