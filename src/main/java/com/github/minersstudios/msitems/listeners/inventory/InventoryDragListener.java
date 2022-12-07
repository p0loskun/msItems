package com.github.minersstudios.msitems.listeners.inventory;

import com.github.minersstudios.msitems.items.RenameableItem;
import com.github.minersstudios.msitems.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

public class InventoryDragListener implements Listener {

	@EventHandler
	public void onInventoryDrag(@NotNull InventoryDragEvent event) {
		String inventoryTitle = ChatUtils.convertComponentToString(event.getView().title());
		if (
				inventoryTitle.equalsIgnoreCase(RenameableItem.Menu.MENU_NAME)
				|| inventoryTitle.equalsIgnoreCase(RenameableItem.Menu.RENAME_SELECTION_NAME)
		) {
			event.setCancelled(true);
		}
	}
}
