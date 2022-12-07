package com.github.minersstudios.msitems.listeners.inventory;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.items.Renameable;
import com.github.minersstudios.msitems.items.RenameableItem;
import com.github.minersstudios.msitems.items.Wearable;
import com.github.minersstudios.msitems.utils.ChatUtils;
import com.github.minersstudios.msitems.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InventoryClickListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(@NotNull InventoryClickEvent event) {
		ClickType clickType = event.getClick();
		Player player = (Player) event.getWhoClicked();
		PlayerInventory inventory = player.getInventory();
		Inventory clickedInventory = event.getClickedInventory();
		String inventoryTitle = ChatUtils.convertComponentToString(event.getView().title());
		ItemStack cursorItem = event.getCursor(),
				currentItem = event.getCurrentItem();
		int slot = event.getSlot();

		if (
				slot == 39
				&& event.getSlotType() == InventoryType.SlotType.ARMOR
				&& cursorItem != null
				&& !cursorItem.getType().isAir()
				&& ItemUtils.getCustomItem(cursorItem) instanceof Wearable
		) {
			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
				inventory.setHelmet(cursorItem);
				player.setItemOnCursor(currentItem);
			});
		}
		if (
				event.isShiftClick()
				&& player.getOpenInventory().getType() == InventoryType.CRAFTING
				&& inventory.getHelmet() == null
				&& ItemUtils.getCustomItem(currentItem) instanceof Wearable
		) {
			event.setCancelled(true);
			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
				inventory.setHelmet(currentItem);
				currentItem.setAmount(0);
			});
		}

		if (clickedInventory == null) return;

		if (
				(clickedInventory.getType() == InventoryType.PLAYER
				&& (clickType.isShiftClick() || clickType == ClickType.DOUBLE_CLICK)
				&& (inventoryTitle.equalsIgnoreCase(RenameableItem.Menu.RENAME_SELECTION_NAME)
				|| inventoryTitle.equalsIgnoreCase(RenameableItem.Menu.MENU_NAME)))
		) {
			event.setCancelled(true);
		}

		if (clickedInventory.getType() != InventoryType.PLAYER) {
			if (inventoryTitle.equalsIgnoreCase(RenameableItem.Menu.MENU_NAME)) {
				ItemStack firstItem = clickedInventory.getItem(0);
				if (firstItem != null && !clickType.isCreativeAction()) {
					int firstItemIndex = RenameableItem.Menu.getItemIndex(firstItem);
					if (slot >= 36 && slot <= 39 && firstItemIndex - 35 >= 0) {
						player.openInventory(RenameableItem.Menu.getInventory(firstItemIndex - 36));
					} else if (slot == 40) {
						player.closeInventory();
					} else if (slot >= 41 && slot <= 44 && firstItemIndex + 36 < RenameableItem.Menu.values.length) {
						player.openInventory(RenameableItem.Menu.getInventory(firstItemIndex + 36));
					} else if (currentItem != null) {
						RenameableItem.Menu.openRename(player, currentItem, firstItemIndex);
					}
				}
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
				event.setCancelled(!clickType.isCreativeAction());
			}

			if (inventoryTitle.equalsIgnoreCase(RenameableItem.Menu.RENAME_SELECTION_NAME)) {
				ItemStack arrow = clickedInventory.getItem(4);
				if (arrow != null && arrow.getItemMeta() != null) {
					ItemStack item = clickedInventory.getItem(21);
					if (slot == 31) {
						player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
						player.openInventory(RenameableItem.Menu.getInventory(arrow.getItemMeta().getCustomModelData() - 1));
					} else if (slot == 21) {
						ItemStack secondItem = clickedInventory.getItem(6);
						if (secondItem != null) {
							String renameText = ChatUtils.convertPlainComponentToString(Objects.requireNonNull(secondItem.getItemMeta().displayName()));
							Bukkit.getScheduler().runTask(Main.getInstance(), () ->
									this.craftRenamedItem(event.getCurrentItem(), clickedInventory, renameText)
							);
						}
						return;
					} else if (
							slot == 23
							&& item != null
							&& cursorItem != null
							&& cursorItem.getType().isAir()
					) {
						player.setItemOnCursor(clickedInventory.getItem(23));
						clickedInventory.setItem(21, null);
						clickedInventory.setItem(23, null);
					}
				}
				event.setCancelled(!clickType.isCreativeAction());
			}
		}
	}

	private void craftRenamedItem(ItemStack itemStack, Inventory inventory, String renameText) {
		if (ItemUtils.getCustomItem(itemStack) instanceof Renameable renameable) {
			inventory.setItem(23, renameable.createRenamedItem(itemStack, renameText));
			return;
		} else {
			RenameableItem renameableItem = ItemUtils.getRenameableItem(itemStack, renameText);
			if (renameableItem != null) {
				inventory.setItem(23, renameableItem.createRenamedItem(itemStack, renameText));
				return;
			}
		}
		inventory.setItem(23, null);
	}
}
