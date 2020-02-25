package com.janmaki.mqrimo.invisible_armor;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.janmaki.mqrimo.invisible_armor.Main;

import java.util.*;

public class JoinEvent implements Listener {
    CustomConfiguration sfile;
    private static Map<String, Boolean> map;
    private Main main;


    JoinEvent(CustomConfiguration save) {
        this.sfile = save;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //List<String> list = save.getStringList(player.getUniqueId().toString());
        sfile = new CustomConfiguration(main,"save.yml");
        new Core(sfile);
        Map<String, Boolean> visibleMap = new HashMap<>();
        map = Core.get(player);
        if(map.get("isArmorInvisible")) {
            visibleMap.put("isArmorInvisible", true);
        }
        Core.put(player, visibleMap);

        Boolean isArmorInvisible = map.get("isArmorInvisible");
        player.sendMessage(String.valueOf(isArmorInvisible));
        player.sendMessage(map.toString());
        List<Player> players = (List<Player>) Bukkit.getOnlinePlayers();
        //if(isArmorInvisible){
                Core.invArmor(player);
        //}

    }
}
