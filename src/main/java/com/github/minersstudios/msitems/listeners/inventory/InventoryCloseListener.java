package com.github.minersstudios.msitems.listeners.inventory;

import com.github.minersstudios.msitems.items.RenameableItem;
import com.github.minersstudios.msitems.utils.ChatUtils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class InventoryCloseListener implements Listener {

	@EventHandler
	public void onInventoryClose(@NotNull InventoryCloseEvent event) {
		if (ChatUtils.convertComponentToString(event.getView().title()).equalsIgnoreCase(RenameableItem.Menu.RENAME_SELECTION_NAME)) {
			ItemStack itemStack = event.getInventory().getItem(21);
			if (itemStack != null) {
				HumanEntity player = event.getPlayer();
				PlayerInventory playerInventory = player.getInventory();
				Map<Integer, ItemStack> map = playerInventory.addItem(itemStack);
				if (!map.isEmpty()) {
					player.getWorld().dropItemNaturally(player.getLocation().add(0.0d, 0.5d, 0.0d), itemStack);
				}
			}
		}
	}
}
