package com.github.minersstudios.msitems.items;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.utils.ItemUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface CustomItem {
	@NotNull
	NamespacedKey getNamespacedKey();

	void setNamespacedKey(@NotNull NamespacedKey namespacedKey);

	@NotNull
	ItemStack getItemStack();

	void setItemStack(@NotNull ItemStack itemStack);

	@Nullable
	default Recipe getRecipe() {
		return null;
	}

	default void setRecipe(@Nullable Recipe recipe) {}

	default boolean isShowInCraftsMenu() {
		return false;
	}

	default void setShowInCraftsMenu(boolean showInCraftsMenu) {}

	default void register() {
		ItemUtils.CUSTOM_ITEMS.put(this.getNamespacedKey().getKey(), this);
		if (this instanceof Renameable renameable) {
			for (Renameable.Item item : renameable.getRenameableItems()) {
				ItemStack itemStack = renameable.createRenamedItem(renameable.getItemStack(), item.getRenameText());
				if (itemStack != null && !renameable.getItemStack().isSimilar(itemStack)) {
					RenameableItem renameableItem = new RenameableItem(
							new NamespacedKey(Main.getInstance(), this.getNamespacedKey().getKey() + "." + item.getKey()),
							item.getRenameText(),
							getItemStack(),
							itemStack,
							item.isShowInRenameMenu()
					);
				}
			}
		}
	}

	@Contract("null -> false")
	default boolean isSimilar(@Nullable ItemStack itemStack) {
		if (
				itemStack == null
				|| itemStack.getType() != this.getItemStack().getType()
				|| !itemStack.hasItemMeta()
				|| !itemStack.getItemMeta().hasCustomModelData()
		) return false;
		if (this instanceof Renameable renameable) {
			for (Renameable.Item renameableItem : renameable.getRenameableItems()) {
				if (renameable.hasRenameableItem(itemStack)) {
					return true;
				}
			}
		}
		return itemStack.getItemMeta().getCustomModelData() == this.getItemStack().getItemMeta().getCustomModelData();
	}

	@Contract("null -> false")
	default boolean isSimilar(@Nullable CustomItem customItem) {
		if (customItem == null) return false;
		if (customItem == this) return true;
		return this.isSimilar(customItem.getItemStack());
	}
}
