package com.janmaki.mqrimo.invisible_armor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.util.*;

public class JoinEvent implements Listener {
    private static File saveFile;
    private static Map<String, Boolean> map;
    private static Main main;



    JoinEvent(File save) {
        this.saveFile = save;
    }

    public static void setMain(Main main) {
        JoinEvent.main = main;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Boolean sectionalBoolean = Core.sectionalBoolean(player);
        Map<String, Boolean> visibleMap = new HashMap<>();


        Core.put(player, visibleMap);

        Boolean isArmorInvisible = sectionalBoolean;
        if(isArmorInvisible){
                Core.invArmor(player);

                Core.reloadData(saveFile);
        }

    }
}
