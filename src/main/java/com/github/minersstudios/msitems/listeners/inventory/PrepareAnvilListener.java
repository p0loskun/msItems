package com.github.minersstudios.msitems.listeners.inventory;

import com.github.minersstudios.msitems.items.Renameable;
import com.github.minersstudios.msitems.items.RenameableItem;
import com.github.minersstudios.msitems.utils.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PrepareAnvilListener implements Listener {

	@EventHandler
	public void onPrepareAnvil(@NotNull PrepareAnvilEvent event) {
		AnvilInventory inventory = event.getInventory();
		ItemStack firstItem = inventory.getFirstItem();
		String renameText = inventory.getRenameText();
		if (
				inventory.getSecondItem() == null
				&& ItemUtils.getCustomItem(firstItem) instanceof Renameable renameable
		) {
			ItemStack renameableItem = renameable.createRenamedItem(firstItem, renameText);
			if (renameableItem != null) {
				event.setResult(renameableItem);
			}
		} else {
			RenameableItem renameableItem = ItemUtils.getRenameableItem(firstItem, renameText);
			if (renameableItem != null) {
				event.setResult(renameableItem.createRenamedItem(firstItem, renameText));
			}
		}
	}
}
