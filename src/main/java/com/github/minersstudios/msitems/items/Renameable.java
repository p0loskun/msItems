package com.github.minersstudios.msitems.items;

import com.github.minersstudios.msitems.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface Renameable extends CustomItem {

	Item @NotNull [] getRenameableItems();

	@NotNull
	ItemStack getItemStack();

	@Contract("null -> false")
	default boolean hasRenameableItem(@Nullable ItemStack itemStack) {
		if (
				itemStack == null
				|| !itemStack.hasItemMeta()
				|| !itemStack.getItemMeta().hasCustomModelData()
		) return false;
		for (Item renameableItem : this.getRenameableItems()) {
			if (renameableItem.getCustomModelData() == itemStack.getItemMeta().getCustomModelData()) {
				return true;
			}
		}
		return false;
	}

	@Contract("null, null -> null")
	default @Nullable ItemStack createRenamedItem(@Nullable ItemStack itemStack, @Nullable String renameText) {
		if (renameText == null || itemStack == null) return null;
		for (Item renameableItem : this.getRenameableItems()) {
			if (StringUtil.startsWithIgnoreCase(renameText, renameableItem.getRenameText())) {
				ItemStack newItemStack = itemStack.clone();
				ItemMeta itemMeta = newItemStack.getItemMeta();
				itemMeta.setCustomModelData(renameableItem.getCustomModelData());
				itemMeta.displayName(ChatUtils.createDefaultStyledName(renameText));
				if (renameableItem.getLore() != null) {
					List<Component> loreComponentList = new ArrayList<>();
					for (String text : renameableItem.getLore()) {
						loreComponentList.add(Component.text(text));
					}
					itemMeta.lore(loreComponentList);
				} else {
					itemMeta.lore(null);
				}
				newItemStack.setItemMeta(itemMeta);
				return newItemStack;
			}
		}
		return null;
	}

	interface Item {
		@NotNull
		String getKey();

		int getCustomModelData();

		@NotNull
		String getRenameText();

		@Nullable
		List<String> getLore();

		boolean isShowInRenameMenu();
	}
}
