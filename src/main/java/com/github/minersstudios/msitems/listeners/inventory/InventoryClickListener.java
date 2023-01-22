package com.github.minersstudios.msitems.listeners.inventory;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.items.Renameable;
import com.github.minersstudios.msitems.items.RenameableItem;
import com.github.minersstudios.msitems.items.Wearable;
import com.github.minersstudios.msitems.utils.ChatUtils;
import com.github.minersstudios.msitems.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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

import static com.github.minersstudios.msitems.items.RenameableItem.Menu.*;

public class InventoryClickListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(@NotNull InventoryClickEvent event) {
		ClickType clickType = event.getClick();
		Player player = (Player) event.getWhoClicked();
		PlayerInventory inventory = player.getInventory();
		Inventory clickedInventory = event.getClickedInventory();
		Component inventoryTitle = event.getView().title();
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
				&& (inventoryTitle.contains(RENAME_SELECTION_NAME)
				|| inventoryTitle.contains(MENU_NAME)))
		) {
			event.setCancelled(true);
		}

		if (clickedInventory.getType() != InventoryType.PLAYER) {
			if (inventoryTitle.contains(MENU_NAME)) {
				ItemStack firstItem = clickedInventory.getItem(0);
				if (firstItem != null && !clickType.isCreativeAction()) {
					int firstItemIndex = getItemIndex(firstItem);
					if (previousPageButtonSlot.contains(slot) && firstItemIndex - 35 >= 0) {
						player.openInventory(getInventory(firstItemIndex - 36));
					} else if (slot == quitRenamesButtonSlot) {
						player.closeInventory();
					} else if (nextPageButtonSlot.contains(slot) && firstItemIndex + 36 < values.length) {
						player.openInventory(getInventory(firstItemIndex + 36));
					} else if (currentItem != null) {
						openRename(player, currentItem, firstItemIndex);
					}
				}
				player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.5f, 1.0f);
				event.setCancelled(!clickType.isCreativeAction());
			}

			if (inventoryTitle.contains(RENAME_SELECTION_NAME)) {
				ItemStack arrow = clickedInventory.getItem(arrowSlot);
				if (arrow != null && arrow.getItemMeta() != null) {
					ItemStack item = clickedInventory.getItem(currentRenameableItemSlot);
					boolean hasExp = player.getLevel() >= 1 || player.getGameMode() == GameMode.CREATIVE;
					if (slot == quitRenameButtonSlot) {
						player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.5f, 1.0f);
						player.openInventory(getInventory(arrow.getItemMeta().getCustomModelData() - 1));
					} else if (slot == currentRenameableItemSlot) {
						ItemStack secondItem = clickedInventory.getItem(renamedItemSlot);
						assert secondItem != null;
						String renameText = ChatUtils.convertPlainComponentToString(Objects.requireNonNull(secondItem.getItemMeta().displayName()));
						Bukkit.getScheduler().runTask(Main.getInstance(), () ->
								this.craftRenamedItem(event.getCurrentItem(), clickedInventory, renameText, hasExp)
						);
						return;
					} else if (
							slot == currentRenamedItemSlot
							&& currentItem != null
							&& item != null
							&& cursorItem != null
							&& cursorItem.getType().isAir()
							&& hasExp
					) {
						player.setItemOnCursor(currentItem);
						clickedInventory.setItem(currentRenameableItemSlot, null);
						clickedInventory.setItem(currentRenamedItemSlot, null);
						player.giveExpLevels(-1);
					}
				}
				event.setCancelled(!clickType.isCreativeAction());
			}
		}
	}

	private void craftRenamedItem(ItemStack itemStack, Inventory inventory, String renameText, boolean hasExp) {
		if (ItemUtils.getCustomItem(itemStack) instanceof Renameable renameable) {
			inventory.setItem(currentRenamedItemSlot, renameable.createRenamedItem(itemStack, renameText));
			if (!hasExp) {
				inventory.setItem(redCrossSlot, getRedCross());
			}
			return;
		} else {
			RenameableItem renameableItem = ItemUtils.getRenameableItem(itemStack, renameText);
			if (renameableItem != null) {
				inventory.setItem(currentRenamedItemSlot, renameableItem.createRenamedItem(itemStack, renameText));
				if (!hasExp) {
					inventory.setItem(redCrossSlot, getRedCross());
				}
				return;
			}
		}
		inventory.setItem(currentRenamedItemSlot, null);
		ItemStack redCross = inventory.getItem(redCrossSlot);
		if (redCross != null) {
			inventory.setItem(redCrossSlot, null);
		}
	}
}
