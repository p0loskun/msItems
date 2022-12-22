package com.github.minersstudios.msitems.listeners;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.listeners.inventory.InventoryClickListener;
import com.github.minersstudios.msitems.listeners.inventory.InventoryCloseListener;
import com.github.minersstudios.msitems.listeners.inventory.InventoryDragListener;
import com.github.minersstudios.msitems.listeners.inventory.PrepareAnvilListener;
import com.github.minersstudios.msitems.listeners.mechanic.BanSwordMechanic;
import com.github.minersstudios.msitems.listeners.mechanic.WrenchMechanic;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public final class RegEvents {

    private RegEvents() {
        throw new IllegalStateException();
    }

    public static void init(@NotNull Main plugin) {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new InventoryCloseListener(), plugin);
        pluginManager.registerEvents(new InventoryClickListener(), plugin);
        pluginManager.registerEvents(new PrepareAnvilListener(), plugin);
        pluginManager.registerEvents(new InventoryDragListener(), plugin);

        pluginManager.registerEvents(new BanSwordMechanic(), plugin);
        pluginManager.registerEvents(new WrenchMechanic(), plugin);
    }
}
