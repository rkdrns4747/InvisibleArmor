package com.janmaki.mqrimo.invisible_armor;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Set;


class Regularly implements Listener {
    private static Map<Player, Set<Player>> map;
    private static Main main;

    Regularly(Main main) {
        this.main = main;
        map = Core.get();
        regularly();
    }


    private void regularly() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player:map.keySet()) {
                    for (Player victim:map.get(player)) {
                        Core.invArmor(player,victim,false);
                    }
                }
                regularly();
            }
        }.runTaskLater(main, 20L);
    }
}
