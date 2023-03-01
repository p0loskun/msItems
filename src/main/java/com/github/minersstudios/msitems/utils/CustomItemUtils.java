package com.github.minersstudios.msitems.utils;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.msitems.items.CustomItem;
import com.github.minersstudios.msitems.items.RenameableItem;
import com.github.minersstudios.msitems.items.Typed;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public final class CustomItemUtils {
	private CustomItemUtils() {
		throw new IllegalStateException("Utility class");
	}

	@Contract("null -> null")
	public static @Nullable CustomItem getCustomItem(@Nullable ItemStack itemStack) {
		if (itemStack == null || itemStack.getItemMeta() == null) return null;
		CustomItem customItem = MSCore.getConfigCache().customItemMap.getBySecondaryKey(itemStack.getItemMeta().getCustomModelData());
		if (customItem == null) return null;

		if (customItem.getItemStack().getType() != itemStack.getType()) {
			for (CustomItem anotherCI : MSCore.getConfigCache().customItemMap.values()) {
				if (anotherCI.isSimilar(itemStack)) {
					customItem = anotherCI;
					break;
				}
			}
		}

		if (customItem instanceof Typed typed) {
			Typed.Type type = typed.getType(itemStack);
			if (type != null) {
				return typed.createCustomItem(type);
			}
		}
		return customItem;
	}

	@Contract("null, null -> null")
	public static @Nullable RenameableItem getRenameableItem(@Nullable ItemStack itemStack, @Nullable String renameText) {
		if (
				itemStack == null
				|| renameText == null
		) return null;
		for (RenameableItem renameableItem : MSCore.getConfigCache().renameableItemMap.values()) {
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
