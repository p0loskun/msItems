package com.github.minersstudios.msitems.items;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msitems.MSItems;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unused")
public class RenameableItem {
	private @NotNull NamespacedKey namespacedKey;
	private @NotNull String renameText;
	private List<ItemStack> renameableItemStacks;
	private @NotNull ItemStack resultItemStack;
	private boolean showInRenameMenu;

	public RenameableItem(
			@NotNull NamespacedKey namespacedKey,
			@NotNull String renameText,
			@NotNull List<ItemStack> renameableItemStacks,
			@NotNull ItemStack resultItemStack,
			boolean showInRenameMenu
	) {
		this.namespacedKey = namespacedKey;
		this.renameText = renameText;
		this.showInRenameMenu = showInRenameMenu;
		this.renameableItemStacks = renameableItemStacks;
		this.resultItemStack = resultItemStack;
		ItemMeta itemMeta = this.resultItemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledText(renameText));
		this.resultItemStack.setItemMeta(itemMeta);
		if (showInRenameMenu) {
			MSCore.getConfigCache().renameableItemsMenu.add(this);
		}
	}

	@Contract("null, null -> null")
	public @Nullable ItemStack createRenamedItem(@Nullable ItemStack itemStack, @Nullable String renameText) {
		if (renameText == null || itemStack == null) return null;
		ItemStack newItemStack = itemStack.clone();
		ItemMeta itemMeta = newItemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledText(renameText));
		itemMeta.lore(this.resultItemStack.lore());
		itemMeta.setCustomModelData(this.resultItemStack.getItemMeta().getCustomModelData());
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

	public @NotNull List<ItemStack> getRenameableItemStacks() {
		return this.renameableItemStacks;
	}

	public void setRenameableItemStacks(@NotNull List<ItemStack> renameableItemStack) {
		this.renameableItemStacks = renameableItemStack;
	}

	public @NotNull ItemStack getResultItemStack() {
		return this.resultItemStack;
	}

	public void setResultItemStack(@NotNull ItemStack resultItemStack) {
		this.resultItemStack = resultItemStack;
	}

	public static class Menu {
		public static final Component
				MENU_NAME = ChatUtils.createDefaultStyledText("뀂ꀰ"),
				RENAME_SELECTION_NAME = ChatUtils.createDefaultStyledText("뀃ꀱ");
		public static RenameableItem[] values = MSCore.getConfigCache().renameableItemsMenu.toArray(new RenameableItem[0]);
		public static final int
				arrowSlot = 4,
				renameableItemSlot = 2,
				renamedItemSlot = 6,
				quitRenameButtonSlot = 40,
				quitRenamesButtonSlot = 40,
				currentRenameableItemSlot = 20,
				currentRenamedItemSlot = 24,
				redCrossSlot = 22;
		public static final List<Integer>
				previousPageButtonSlot = Lists.newArrayList(36, 37, 38, 39),
				nextPageButtonSlot = Lists.newArrayList(41, 42, 43, 44);

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
					Inventory inventory = Bukkit.createInventory(null, 5 * 9, RENAME_SELECTION_NAME);
					List<ItemStack> renameableItemStacks = renameableItem.renameableItemStacks;
					if (renameableItemStacks.size() == 1) {
						inventory.setItem(renameableItemSlot, renameableItemStacks.get(0));
					} else {
						new BukkitRunnable() {
							int index = 0;

							@Override
							public void run() {
								if (!player.getOpenInventory().title().contains(RENAME_SELECTION_NAME)) this.cancel();
								inventory.setItem(renameableItemSlot, renameableItemStacks.get(this.index));
								this.index++;
								if (this.index + 1 > renameableItemStacks.size()) {
									this.index = 0;
								}
							}
						}.runTaskTimer(MSItems.getInstance(), 0L, 10L);
					}
					inventory.setItem(arrowSlot, getArrow(pageIndex));
					inventory.setItem(renamedItemSlot, itemStack);
					inventory.setItem(quitRenameButtonSlot, getQuitButton());
					player.openInventory(inventory);
				}
			}
		}

		/**
		 * @return Crafts GUI
		 */
		@Contract("_ -> new")
		public static @NotNull Inventory getInventory(int index) {
			Inventory inventory = Bukkit.createInventory(null, 5 * 9, MENU_NAME);
			inventory.setItem(previousPageButtonSlot.get(0), getPreviousPageButton()[index == 0 ? 1 : 0]);
			inventory.setItem(previousPageButtonSlot.get(1), getPreviousPageButton()[1]);
			inventory.setItem(previousPageButtonSlot.get(2), getPreviousPageButton()[1]);
			inventory.setItem(previousPageButtonSlot.get(3), getPreviousPageButton()[1]);
			inventory.setItem(quitRenamesButtonSlot, getQuitButton());
			inventory.setItem(nextPageButtonSlot.get(0), getNextPageButton()[index + 37 > values.length ? 1 : 0]);
			inventory.setItem(nextPageButtonSlot.get(1), getNextPageButton()[1]);
			inventory.setItem(nextPageButtonSlot.get(2), getNextPageButton()[1]);
			inventory.setItem(nextPageButtonSlot.get(3), getNextPageButton()[1]);
			for (int i = 0, page = index; i <= 35 && page < values.length; page++, i++) {
				if (values[page].showInRenameMenu) {
					inventory.setItem(i, values[page].resultItemStack);
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

		@Contract(" -> new")
		public static @NotNull ItemStack getRedCross() {
			ItemStack itemStack = new ItemStack(Material.PAPER);
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.displayName(Component.text(ChatColor.GRAY + "Вам не хватает 1 очка опыта"));
			itemMeta.setCustomModelData(5003);
			itemStack.setItemMeta(itemMeta);
			return itemStack;
		}
	}
}
