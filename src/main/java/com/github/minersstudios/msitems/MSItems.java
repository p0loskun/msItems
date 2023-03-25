package com.github.minersstudios.msitems;

import com.github.minersstudios.mscore.MSPlugin;
import com.github.minersstudios.mscore.utils.MSPluginUtils;
import com.github.minersstudios.msitems.items.CustomItem;
import com.github.minersstudios.msitems.utils.ConfigCache;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
        configCache = new ConfigCache();
        configCache.registerItems();
        instance.loadedCustoms = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (MSPluginUtils.isLoadedCustoms()) {
                    for (CustomItem customItem : configCache.recipeItems) {
                        customItem.registerRecipes();
                    }
                    configCache.recipeItems.clear();
                    this.cancel();
                }
            }
        }.runTaskTimer(instance, 0L, 50L);
    }

    @Contract(pure = true)
    public static @NotNull MSItems getInstance() {
        return instance;
    }

    @Contract(pure = true)
    public static @NotNull ConfigCache getConfigCache() {
        return configCache;
    }
}
