package com.janmaki.mqrimo.invisible_armor;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.janmaki.mqrimo.invisible_armor.Main;

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

        //List<String> list = save.getStringList(player.getUniqueId().toString());
        /**CustomConfiguration sfile = Core.getSfile();
        FileConfiguration save = sfile.getConfig();
        MemorySection memorySection = (MemorySection) save.get(player.getUniqueId().toString());
         **/
        Boolean sectionalBoolean = Core.sectionalBoolean(player);
        Map<String, Boolean> visibleMap = new HashMap<>();

        /**if(sectionalBoolean) {
            visibleMap.put("isArmorInvisible", true);
        }**/
        Core.put(player, visibleMap);

        Boolean isArmorInvisible = sectionalBoolean;
        if(isArmorInvisible){
                Core.invArmor(player);

                Core.reloadData(saveFile);
        }

    }
}
