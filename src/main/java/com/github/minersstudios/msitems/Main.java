package com.github.minersstudios.msitems;

import com.github.minersstudios.msitems.commands.RegCommands;
import com.github.minersstudios.msitems.listeners.RegEvents;
import com.github.minersstudios.msitems.utils.ChatUtils;
import com.github.minersstudios.msitems.utils.ConfigCache;
import com.github.minersstudios.msitems.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Main extends JavaPlugin {
    private static Main instance;
    private static ConfigCache configCache;

    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();
        this.load();
        if (this.isEnabled()) {
            ChatUtils.log(Level.INFO, ChatColor.GREEN + "Enabled in " + (System.currentTimeMillis() - time) + "ms");
        }
    }

    public void load() {
        instance = this;

        ItemUtils.registerItems();
        reloadConfigs();

        RegEvents.init(this);
        RegCommands.init(this);
    }

    public static void reloadConfigs() {
        instance.saveResource("items/example.yml", true);
        instance.saveDefaultConfig();
        instance.reloadConfig();
        configCache = new ConfigCache();
    }

    public static Main getInstance() {
        return instance;
    }

    public static ConfigCache getConfigCache() {
        return configCache;
    }
}
