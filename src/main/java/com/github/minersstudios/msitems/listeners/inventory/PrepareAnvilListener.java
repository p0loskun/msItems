package com.github.minersstudios.msitems.listeners.inventory;

import com.github.minersstudios.msitems.items.Renameable;
import com.github.minersstudios.msitems.items.RenameableItem;
import com.github.minersstudios.msitems.utils.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class PrepareAnvilListener implements Listener {

	@EventHandler
	public void onPrepareAnvil(@NotNull PrepareAnvilEvent event) {
		AnvilInventory inventory = event.getInventory();
		ItemStack firstItem = inventory.getFirstItem();
		String renameText = inventory.getRenameText();
		if (ItemUtils.getCustomItem(firstItem) instanceof Renameable renameable) {
			ItemStack renameableItem = renameable.createRenamedItem(firstItem, renameText);
			if (renameableItem != null) {
				event.setResult(ItemUtils.combineDurability(renameableItem, inventory.getSecondItem()));
			}
		} else {
			RenameableItem renameableItem = ItemUtils.getRenameableItem(firstItem, renameText);
			if (renameableItem != null) {
				ItemStack renamedItem = renameableItem.createRenamedItem(firstItem, renameText);
				event.setResult(
						renamedItem == null ? null
						: ItemUtils.combineDurability(renamedItem, inventory.getSecondItem())
				);
			} else {
				ItemStack itemStack = event.getResult();
				if (itemStack == null) return;
				ItemMeta itemMeta = itemStack.getItemMeta();
				itemMeta.setCustomModelData(null);
				itemStack.setItemMeta(itemMeta);
				event.setResult(ItemUtils.combineDurability(itemStack, inventory.getSecondItem()));
			}
		}
	}
}
