package com.github.minersstudios.msitems.listeners.mechanic;

import com.github.minersstudios.msitems.items.items.cards.CardsBicycle;
import com.github.minersstudios.msitems.utils.ItemUtils;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CardBoxMechanic implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(@NotNull InventoryClickEvent event) {
		if (!event.isRightClick()) return;
		ItemStack cursorItem = event.getCursor();
		ItemStack currentItem = event.getCurrentItem();
		if (cursorItem == null || currentItem == null) return;
		if (
				!cursorItem.getType().isAir()
				&& ItemUtils.getCustomItem(currentItem) instanceof CardsBicycle
		) {
			addCardToCardBox(event, currentItem, cursorItem);
		} else if (
				!currentItem.getType().isAir()
				&& ItemUtils.getCustomItem(cursorItem) instanceof CardsBicycle
		) {
			addCardToCardBox(event, cursorItem, currentItem);
		}
	}

	private static void addCardToCardBox(
			@NotNull InventoryClickEvent event,
			@NotNull ItemStack cardBoxItem,
			@NotNull ItemStack cardItem
	) {
		List<ItemStack> cards = new ArrayList<>();
		cards.addAll(CardsBicycle.BLUE_CARD_ITEMS);
		cards.addAll(CardsBicycle.RED_CARD_ITEMS);
		if (ItemUtils.isListContainsItem(cards, cardItem)) {
			BundleMeta bundleMeta = (BundleMeta) cardBoxItem.getItemMeta();
			List<ItemStack> itemStacks = Lists.newArrayList(cardItem);
			itemStacks.addAll(bundleMeta.getItems());
			if (!(itemStacks.size() > 54)) {
				bundleMeta.setItems(itemStacks);
				cardBoxItem.setItemMeta(bundleMeta);
				cardItem.setAmount(0);
			}
		}
		((Player) event.getWhoClicked()).updateInventory();
		event.setCancelled(true);
	}
}
