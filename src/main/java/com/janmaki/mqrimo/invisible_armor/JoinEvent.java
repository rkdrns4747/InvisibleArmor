package com.janmaki.mqrimo.invisible_armor;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JoinEvent implements Listener {
    FileConfiguration save;

    JoinEvent(FileConfiguration save) {
        this.save = save;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        List<String> list = save.getStringList(player.getDisplayName());
        Set<Player> players = new HashSet<>();
        for (String str:list) {
            players.add(Bukkit.getServer().getPlayer(str));
        }
        Core.put(player,players);
    }
}
