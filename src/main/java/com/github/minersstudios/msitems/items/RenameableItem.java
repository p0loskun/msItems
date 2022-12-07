package com.github.minersstudios.msitems.items;

import com.github.minersstudios.msitems.utils.ChatUtils;
import com.github.minersstudios.msitems.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class RenameableItem {
	@NotNull private NamespacedKey namespacedKey;
	@NotNull private String renameText;

	@NotNull private ItemStack renameableItemStack;
	@NotNull private ItemStack resultItemStack;
	private boolean showInRenameMenu;

	public RenameableItem(
			@NotNull NamespacedKey namespacedKey,
			@NotNull String renameText,
			@NotNull ItemStack renameableItemStack,
			@NotNull ItemStack resultItemStack,
			boolean showInRenameMenu
	) {
		this.namespacedKey = namespacedKey;
		this.renameText = renameText;
		this.showInRenameMenu = showInRenameMenu;
		this.renameableItemStack = renameableItemStack;
		this.resultItemStack = resultItemStack;
		ItemMeta itemMeta = this.resultItemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledName(renameText));
		this.resultItemStack.setItemMeta(itemMeta);
		if (showInRenameMenu) {
			ItemUtils.RENAMEABLE_ITEMS_MENU.add(this);
		}
	}

	@Contract("null, null -> null")
	public @Nullable ItemStack createRenamedItem(@Nullable ItemStack itemStack, @Nullable String renameText) {
		if (renameText == null || itemStack == null) return null;
		ItemStack newItemStack = itemStack.clone();
		ItemMeta itemMeta = this.resultItemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledName(renameText));
		newItemStack.setItemMeta(itemMeta);
		return newItemStack;
	}

	public @NotNull NamespacedKey getNamespacedKey() {
		return this.namespacedKey;
	}

	public void setNamespacedKey(@NotNull NamespacedKey namespacedKey) {
		this.namespacedKey = namespacedKey;
	}

	public @NotNull String getRenameText() {
		return this.renameText;
	}

	public void setRenameText(@NotNull String renameText) {
		this.renameText = renameText;
	}

	public boolean isShowInRenameMenu() {
		return this.showInRenameMenu;
	}

	public void setShowInRenameMenu(boolean showInRenameMenu) {
		this.showInRenameMenu = showInRenameMenu;
	}

	public @NotNull ItemStack getRenameableItemStack() {
		return this.renameableItemStack;
	}

	public void setRenameableItemStack(@NotNull ItemStack renameableItemStack) {
		this.renameableItemStack = renameableItemStack;
	}

	public @NotNull ItemStack getResultItemStack() {
		return this.resultItemStack;
	}

	public void setResultItemStack(@NotNull ItemStack resultItemStack) {
		this.resultItemStack = resultItemStack;
	}

	public static class Menu {
		public static final String
				MENU_NAME = ChatColor.WHITE + "뀂\uA030",
				RENAME_SELECTION_NAME = ChatColor.WHITE + "뀂\uA031";
		public static final RenameableItem[] values = ItemUtils.RENAMEABLE_ITEMS_MENU.toArray(new RenameableItem[0]);

		public static int getItemIndex(@NotNull ItemStack itemStack) {
			int index = 0;
			for (RenameableItem renameableItem : values) {
				if (itemStack.isSimilar(renameableItem.resultItemStack)) {
					return index;
				}
				index++;
			}
			return -1;
		}

		public static void openRename(@NotNull Player player, @NotNull ItemStack itemStack, int pageIndex) {
			for (RenameableItem renameableItem : values) {
				if (itemStack.isSimilar(renameableItem.resultItemStack)) {
					Inventory inventory = Bukkit.createInventory(null, 4 * 9, Component.text(RENAME_SELECTION_NAME));
					inventory.setItem(2, renameableItem.renameableItemStack);
					inventory.setItem(4, getArrow(pageIndex));
					inventory.setItem(6, itemStack);
					inventory.setItem(31, getQuitButton());
					player.openInventory(inventory);
				}
			}
		}

		/**
		 * @return Crafts GUI
		 */
		@Contract("_ -> new")
		public static @NotNull Inventory getInventory(int index) {
			Inventory inventory = Bukkit.createInventory(null, 5 * 9, Component.text(MENU_NAME));
			inventory.setItem(36, getPreviousPageButton()[index == 0 ? 1 : 0]);
			inventory.setItem(37, getPreviousPageButton()[1]);
			inventory.setItem(38, getPreviousPageButton()[1]);
			inventory.setItem(39, getPreviousPageButton()[1]);
			inventory.setItem(40, getQuitButton());
			inventory.setItem(41, getNextPageButton()[index + 37 > values.length ? 1 : 0]);
			inventory.setItem(42, getNextPageButton()[1]);
			inventory.setItem(43, getNextPageButton()[1]);
			inventory.setItem(44, getNextPageButton()[1]);
			for (int i = 0; i <= 35 && index < values.length; index++, i++) {
				if (values[index].showInRenameMenu) {
					inventory.setItem(i, values[index].resultItemStack);
				}
			}
			return inventory;
		}

		@Contract(" -> new")
		private static ItemStack @NotNull [] getPreviousPageButton() {
			ItemStack previousPage = new ItemStack(Material.PAPER),
					previousPageNoCMD = new ItemStack(Material.PAPER);
			ItemMeta previousPageMeta = previousPage.getItemMeta(),
					previousPageMetaNoCMD = previousPageNoCMD.getItemMeta();
			assert previousPageMeta != null && previousPageMetaNoCMD != null;
			previousPageMetaNoCMD.displayName(Component.text(ChatColor.WHITE + "Предыдущая страница"));
			previousPageMeta.displayName(Component.text(ChatColor.WHITE + "Предыдущая страница"));
			previousPageMeta.setCustomModelData(5001);
			previousPageMetaNoCMD.setCustomModelData(1);
			previousPageNoCMD.setItemMeta(previousPageMetaNoCMD);
			previousPage.setItemMeta(previousPageMeta);
			return new ItemStack[]{previousPage, previousPageNoCMD};
		}

		@Contract(" -> new")
		private static ItemStack @NotNull [] getNextPageButton() {
			ItemStack nextPage = new ItemStack(Material.PAPER),
					nextPageNoCMD = new ItemStack(Material.PAPER);
			ItemMeta nextPageMeta = nextPage.getItemMeta(),
					nextPageMetaNoCMD = nextPageNoCMD.getItemMeta();
			nextPageMetaNoCMD.displayName(Component.text(ChatColor.WHITE + "Следующая страница"));
			nextPageMeta.displayName(Component.text(ChatColor.WHITE + "Следующая страница"));
			nextPageMeta.setCustomModelData(5002);
			nextPageMetaNoCMD.setCustomModelData(1);
			nextPageNoCMD.setItemMeta(nextPageMetaNoCMD);
			nextPage.setItemMeta(nextPageMeta);
			return new ItemStack[]{nextPage, nextPageNoCMD};
		}

		@Contract(" -> new")
		private static @NotNull ItemStack getQuitButton() {
			ItemStack itemStack = new ItemStack(Material.PAPER);
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.displayName(Component.text(ChatColor.WHITE + "Вернуться"));
			itemMeta.setCustomModelData(1);
			itemStack.setItemMeta(itemMeta);
			return itemStack;
		}

		@Contract("_ -> new")
		private static @NotNull ItemStack getArrow(int pageIndex) {
			ItemStack itemStack = new ItemStack(Material.PAPER);
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.displayName(Component.text(ChatColor.GRAY + " -> "));
			itemMeta.setCustomModelData(pageIndex + 1);
			itemStack.setItemMeta(itemMeta);
			return itemStack;
		}
	}
}
