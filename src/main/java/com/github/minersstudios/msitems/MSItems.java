package com.github.minersstudios.msitems;

import com.github.minersstudios.mscore.MSPlugin;
import com.github.minersstudios.msitems.utils.ConfigCache;

public final class MSItems extends MSPlugin {
    private static MSItems instance;
    private static ConfigCache configCache;

    @Override
    public void enable() {
        instance = this;

        reloadConfigs();
    }

    public static void reloadConfigs() {
        instance.saveResource("items/example.yml", true);
        instance.saveDefaultConfig();
        instance.reloadConfig();
        ConfigCache.registerItems();
        configCache = new ConfigCache();
    }

    public static MSItems getInstance() {
        return instance;
    }

    public static ConfigCache getConfigCache() {
        return configCache;
    }
}
