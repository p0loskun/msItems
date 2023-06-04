package com.github.minersstudios.msitems.items;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.mscore.inventory.CustomInventory;
import com.github.minersstudios.mscore.inventory.ElementListedInventory;
import com.github.minersstudios.mscore.inventory.InventoryButton;
import com.github.minersstudios.mscore.inventory.ListedInventory;
import com.github.minersstudios.mscore.inventory.actions.ButtonClickAction;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.MSItemUtils;
import com.github.minersstudios.msitems.MSItems;
import com.github.minersstudios.msitems.utils.CustomItemUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.IntStream;

import static com.github.minersstudios.mscore.inventory.InventoryButton.playClickSound;
import static com.github.minersstudios.mscore.utils.ChatUtils.createDefaultStyledText;

@SuppressWarnings("unused")
public class RenameableItem {
	private @NotNull NamespacedKey namespacedKey;
	private @NotNull String renameText;
	private List<ItemStack> renameableItemStacks;
	private @NotNull ItemStack resultItemStack;
	private boolean showInRenameMenu;
	private final @NotNull Set<OfflinePlayer> whiteList = new HashSet<>();

	public RenameableItem(
			@NotNull NamespacedKey namespacedKey,
			@NotNull String renameText,
			@NotNull List<ItemStack> renameableItemStacks,
			@NotNull ItemStack resultItemStack,
			boolean showInRenameMenu,
			@NotNull Set<OfflinePlayer> whiteList
	) {
		this.namespacedKey = namespacedKey;
		this.renameText = renameText;
		this.showInRenameMenu = showInRenameMenu;
		this.renameableItemStacks = renameableItemStacks;
		this.resultItemStack = resultItemStack;
		ItemMeta itemMeta = this.resultItemStack.getItemMeta();
		itemMeta.displayName(createDefaultStyledText(renameText));
		itemMeta.getPersistentDataContainer().set(
				MSItemUtils.CUSTOM_ITEM_RENAMEABLE_NAMESPACED_KEY,
				PersistentDataType.STRING,
				this.getNamespacedKey().getKey()
		);
		this.resultItemStack.setItemMeta(itemMeta);
		this.whiteList.addAll(whiteList);
		if (showInRenameMenu) {
			MSCore.getConfigCache().renameableItemsMenu.add(this);
		}
	}

	@Contract("null, null -> null")
	public @Nullable ItemStack createRenamedItem(@Nullable ItemStack itemStack, @Nullable String renameText) {
		if (renameText == null || itemStack == null) return null;
		ItemStack newItemStack = itemStack.clone();
		ItemMeta itemMeta = newItemStack.getItemMeta();
		itemMeta.displayName(createDefaultStyledText(renameText));
		itemMeta.lore(this.resultItemStack.lore());
		itemMeta.setCustomModelData(this.resultItemStack.getItemMeta().getCustomModelData());
		itemMeta.getPersistentDataContainer().set(
				MSItemUtils.CUSTOM_ITEM_RENAMEABLE_NAMESPACED_KEY,
				PersistentDataType.STRING,
				this.getNamespacedKey().getKey()
		);
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

	public boolean isShowInRenameMenu() {
		return this.showInRenameMenu;
	}

	public void setShowInRenameMenu(boolean showInRenameMenu) {
		this.showInRenameMenu = showInRenameMenu;
	}

	public boolean isWhiteListed(@Nullable OfflinePlayer player) {
		return this.whiteList.isEmpty() || this.whiteList.contains(player);
	}

	public @NotNull Set<OfflinePlayer> getWhiteListedPlayers() {
		return this.whiteList;
	}

	public static class Menu {
		public static final int
				renameableItemSlot = 2,
				renamedItemSlot = 6,
				quitRenameButtonSlot = 40,
				quitRenamesButtonSlot = 40,
				currentRenameableItemSlot = 20,
				currentRenamedItemSlot = 24,
				redCrossSlot = 22;

		@Contract(" -> new")
		public static @NotNull CustomInventory create() {
			List<InventoryButton> elements = new ArrayList<>();
			for (RenameableItem renameableItem : MSCore.getConfigCache().renameableItemsMenu) {
				ItemStack resultItem = renameableItem.getResultItemStack();
				elements.add(new InventoryButton(resultItem, (buttonEvent, inventory, button) -> {
					Player player = (Player) buttonEvent.getWhoClicked();
					CustomInventory renameInventory = new CustomInventory("뀃ꀱ", 5);
					List<ItemStack> renameableItemStacks = renameableItem.getRenameableItemStacks();
					if (renameableItemStacks.size() == 1) {
						renameInventory.setItem(renameableItemSlot, renameableItemStacks.get(0));
					} else {
						new BukkitRunnable() {
							int index = 0;

							@Override
							public void run() {
								if (!buttonEvent.getView().getTopInventory().equals(renameInventory)) this.cancel();
								renameInventory.setItem(renameableItemSlot, renameableItemStacks.get(this.index));
								this.index++;
								if (this.index + 1 > renameableItemStacks.size()) {
									this.index = 0;
								}
							}
						}.runTaskTimer(MSItems.getInstance(), 0L, 10L);
					}
					renameInventory.setItem(renamedItemSlot, resultItem);
					renameInventory.setButtonAt(quitRenameButtonSlot, new InventoryButton(RenameableItem.Menu.createQuitButton(), (e, i, b) -> {
						player.openInventory(inventory);
						playClickSound(player);
					}));

					renameInventory.setCloseAction((e, customInventory) -> {
						ItemStack itemStack = customInventory.getItem(currentRenameableItemSlot);
						if (itemStack != null) {
							Map<Integer, ItemStack> map = player.getInventory().addItem(itemStack);
							if (!map.isEmpty()) {
								player.getWorld().dropItemNaturally(player.getLocation().add(0.0d, 0.5d, 0.0d), itemStack);
							}
						}
					});

					renameInventory.setClickAction(((clickEvent, customInventory) -> {
						int slot = clickEvent.getSlot();
						ItemStack currentItem = clickEvent.getCurrentItem();
						ItemStack cursorItem = clickEvent.getCursor();
						boolean hasExp = player.getLevel() >= 1 || player.getGameMode() == GameMode.CREATIVE;

						if (slot == currentRenameableItemSlot) {
							ItemStack secondItem = renameInventory.getItem(renamedItemSlot);
							assert secondItem != null;
							String renameText = ChatUtils.serializePlainComponent(Objects.requireNonNull(secondItem.getItemMeta().displayName()));
							Bukkit.getScheduler().runTask(MSItems.getInstance(), () ->
									createRenamedItem(clickEvent.getCurrentItem(), renameInventory, renameText, hasExp)
							);
							return;
						} else if (
								slot == currentRenamedItemSlot
								&& currentItem != null
								&& renameInventory.getItem(currentRenameableItemSlot) != null
								&& cursorItem != null
								&& cursorItem.getType().isAir()
								&& hasExp
						) {
							player.setItemOnCursor(currentItem);
							renameInventory.setItem(currentRenameableItemSlot, null);
							renameInventory.setItem(currentRenamedItemSlot, null);
							player.giveExpLevels(-1);
						}

						clickEvent.setCancelled(!clickEvent.getClick().isCreativeAction());
					}));
					player.openInventory(renameInventory);
				}));
			}

			ElementListedInventory renameInventory = new ElementListedInventory("뀂ꀰ", 5, elements, IntStream.range(0, 36).toArray());

			ButtonClickAction previousClick = (event, customInventory, button) -> {
				if (!(customInventory instanceof ListedInventory listedInventory)) return;
				Player player = (Player) event.getWhoClicked();
				ListedInventory previousPage = renameInventory.getPage(listedInventory.getPreviousPageIndex());
				if (previousPage != null) {
					player.openInventory(previousPage);
					playClickSound(player);
				}
			};
			renameInventory.setStaticButtonAt(36, inventory -> new InventoryButton(createPreviousPageButton()[inventory.getPreviousPageIndex() == -1 ? 1 : 0], previousClick));
			renameInventory.setStaticButtonAt(37, i -> new InventoryButton(createPreviousPageButton()[1], previousClick));
			renameInventory.setStaticButtonAt(38, i -> new InventoryButton(createPreviousPageButton()[1], previousClick));
			renameInventory.setStaticButtonAt(39, i -> new InventoryButton(createPreviousPageButton()[1], previousClick));

			renameInventory.setStaticButtonAt(quitRenamesButtonSlot, i -> new InventoryButton(createQuitButton(), (event, customInventory, inventoryButton) -> {
				Player player = (Player) event.getWhoClicked();
				player.closeInventory();
				playClickSound(player);
			}));

			ButtonClickAction nextClick = (event, customInventory, button) -> {
				if (!(customInventory instanceof ListedInventory listedInventory)) return;
				Player player = (Player) event.getWhoClicked();
				ListedInventory nextPage = renameInventory.getPage(listedInventory.getNextPageIndex());
				if (nextPage != null) {
					player.openInventory(nextPage);
					playClickSound(player);
				}
			};
			renameInventory.setStaticButtonAt(41, inventory -> new InventoryButton(createNextPageButton()[inventory.getNextPageIndex() == -1 ? 1 : 0], nextClick));
			renameInventory.setStaticButtonAt(42, i -> new InventoryButton(createNextPageButton()[1], nextClick));
			renameInventory.setStaticButtonAt(43, i -> new InventoryButton(createNextPageButton()[1], nextClick));
			renameInventory.setStaticButtonAt(44, i -> new InventoryButton(createNextPageButton()[1], nextClick));

			renameInventory.updatePages();

			return renameInventory;
		}

		private static void createRenamedItem(
				@Nullable ItemStack itemStack,
				@NotNull Inventory inventory,
				@NotNull String renameText,
				boolean hasExp
		) {
			if (MSItemUtils.getCustomItem(itemStack) instanceof Renameable renameable) {
				inventory.setItem(currentRenamedItemSlot, renameable.createRenamedItem(itemStack, renameText));
				if (!hasExp) {
					inventory.setItem(redCrossSlot, createRedCross());
				}
				return;
			} else {
				RenameableItem renameableItem = CustomItemUtils.getRenameableItem(itemStack, renameText);
				if (
						renameableItem != null
						&& renameableItem.isWhiteListed((OfflinePlayer) inventory.getViewers().get(0))
				) {
					inventory.setItem(currentRenamedItemSlot, renameableItem.createRenamedItem(itemStack, renameText));
					if (!hasExp) {
						inventory.setItem(redCrossSlot, createRedCross());
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

		@Contract(" -> new")
		private static ItemStack @NotNull [] createPreviousPageButton() {
			ItemStack previousPage = new ItemStack(Material.PAPER),
					previousPageNoCMD = new ItemStack(Material.PAPER);
			ItemMeta previousPageMeta = previousPage.getItemMeta(),
					previousPageMetaNoCMD = previousPageNoCMD.getItemMeta();
			previousPageMetaNoCMD.displayName(createDefaultStyledText("Предыдущая страница"));
			previousPageMeta.displayName(createDefaultStyledText("Предыдущая страница"));
			previousPageMeta.setCustomModelData(5001);
			previousPageMetaNoCMD.setCustomModelData(1);
			previousPageNoCMD.setItemMeta(previousPageMetaNoCMD);
			previousPage.setItemMeta(previousPageMeta);
			return new ItemStack[]{previousPage, previousPageNoCMD};
		}

		@Contract(" -> new")
		private static ItemStack @NotNull [] createNextPageButton() {
			ItemStack nextPage = new ItemStack(Material.PAPER),
					nextPageNoCMD = new ItemStack(Material.PAPER);
			ItemMeta nextPageMeta = nextPage.getItemMeta(),
					nextPageMetaNoCMD = nextPageNoCMD.getItemMeta();
			nextPageMetaNoCMD.displayName(createDefaultStyledText("Следующая страница"));
			nextPageMeta.displayName(createDefaultStyledText("Следующая страница"));
			nextPageMeta.setCustomModelData(5002);
			nextPageMetaNoCMD.setCustomModelData(1);
			nextPageNoCMD.setItemMeta(nextPageMetaNoCMD);
			nextPage.setItemMeta(nextPageMeta);
			return new ItemStack[]{nextPage, nextPageNoCMD};
		}

		@Contract(" -> new")
		private static @NotNull ItemStack createQuitButton() {
			ItemStack itemStack = new ItemStack(Material.PAPER);
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.displayName(createDefaultStyledText("Вернуться"));
			itemMeta.setCustomModelData(1);
			itemStack.setItemMeta(itemMeta);
			return itemStack;
		}

		@Contract(" -> new")
		private static @NotNull ItemStack createRedCross() {
			ItemStack itemStack = new ItemStack(Material.PAPER);
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.displayName(Component.text("Вам не хватает 1 уровня опыта", ChatUtils.COLORLESS_DEFAULT_STYLE).color(NamedTextColor.GRAY));
			itemMeta.setCustomModelData(5003);
			itemStack.setItemMeta(itemMeta);
			return itemStack;
		}
	}
}
