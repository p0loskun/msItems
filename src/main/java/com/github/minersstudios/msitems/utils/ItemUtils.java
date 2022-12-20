package com.github.minersstudios.msitems.utils;

import com.github.minersstudios.msitems.items.CustomItem;
import com.github.minersstudios.msitems.items.RenameableItem;
import com.github.minersstudios.msitems.items.cosmetics.LeatherHat;
import com.github.minersstudios.msitems.items.items.BanSword;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ItemUtils {
	public static final Map<String, CustomItem> CUSTOM_ITEMS = new HashMap<>();
	public static final Map<String, RenameableItem> RENAMEABLE_ITEMS = new HashMap<>();
	public static final List<RenameableItem> RENAMEABLE_ITEMS_MENU = new ArrayList<>();

	private ItemUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static void registerItems() {
		new LeatherHat().register();
		new BanSword().register();
	}

	@Contract("null -> null")
	public static @Nullable CustomItem getCustomItem(@Nullable ItemStack itemStack) {
		for (CustomItem customItem : CUSTOM_ITEMS.values()) {
			if (customItem.isSimilar(itemStack)) {
				return customItem;
			}
		}
		return null;
	}

	@Contract("null, null -> null")
	public static @Nullable RenameableItem getRenameableItem(@Nullable ItemStack itemStack, @Nullable String renameText) {
		if (
				itemStack == null
				|| renameText == null
		) return null;
		for (RenameableItem renameableItem : RENAMEABLE_ITEMS.values()) {
			for (ItemStack renameableItemStack : renameableItem.getRenameableItemStacks()) {
				if (
						renameableItemStack.getType() == itemStack.getType()
						&& StringUtils.startsWithIgnoreCase(renameText, renameableItem.getRenameText())
				) {
					return renameableItem;
				}
			}
		}
		return null;
	}
}
