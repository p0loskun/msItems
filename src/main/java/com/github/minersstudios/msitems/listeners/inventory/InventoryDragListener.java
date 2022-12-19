package com.github.minersstudios.msitems.listeners.inventory;

import com.github.minersstudios.msitems.items.RenameableItem;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

public class InventoryDragListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryDrag(@NotNull InventoryDragEvent event) {
		Component inventoryTitle = event.getView().title();
		event.setCancelled(
				inventoryTitle.contains(RenameableItem.Menu.MENU_NAME)
				|| inventoryTitle.contains(RenameableItem.Menu.RENAME_SELECTION_NAME)
		);
	}
}
