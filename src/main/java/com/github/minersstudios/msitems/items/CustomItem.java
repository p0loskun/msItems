package com.github.minersstudios.msitems.items;

import com.github.minersstudios.msitems.MSItems;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.minersstudios.mscore.MSCore.getConfigCache;

@SuppressWarnings("unused")
public interface CustomItem extends Cloneable {
	@NotNull
	NamespacedKey getNamespacedKey();

	void setNamespacedKey(@NotNull NamespacedKey namespacedKey);

	@NotNull
	ItemStack getItemStack();

	void setItemStack(@NotNull ItemStack itemStack);

	default @Nullable List<Recipe> getRecipes() {
		return null;
	}

	default void setRecipes(@Nullable List<Recipe> recipes) {}

	default boolean isShowInCraftsMenu() {
		return false;
	}

	default void setShowInCraftsMenu(boolean showInCraftsMenu) {}

	default void register() {
		this.register(true);
	}

	default void register(boolean regRecipes) {
		if (this instanceof FullTyped fullTyped) {
			for (Typed.Type type : fullTyped.getTypes()) {
				getConfigCache().customItemMap.put(type.getNamespacedKey().getKey(), type.getCustomModelData(), fullTyped.createCustomItem(type));
			}
		} else if (this instanceof Renameable renameable) {
			for (Renameable.Item item : renameable.getRenameableItems()) {
				ItemStack itemStack = renameable.createRenamedItem(renameable.getItemStack(), item.getRenameText());
				if (itemStack != null && !renameable.getItemStack().isSimilar(itemStack)) {
					getConfigCache().customItemMap.put(renameable.getNamespacedKey().getKey(), itemStack.getItemMeta().getCustomModelData(), this);
					new RenameableItem(
							new NamespacedKey(MSItems.getInstance(), this.getNamespacedKey().getKey() + "." + item.getKey()),
							item.getRenameText(),
							Lists.newArrayList(getItemStack()),
							itemStack,
							item.isShowInRenameMenu()
					);
				}
			}
		} else {
			getConfigCache().customItemMap.put(this.getNamespacedKey().getKey(), this.getItemStack().getItemMeta().getCustomModelData(), this);
		}

		if (regRecipes) {
			List<Recipe> recipes = this.getRecipes();
			if (recipes != null) {
				for (Recipe recipe : recipes) {
					Bukkit.addRecipe(recipe);
				}
				if (isShowInCraftsMenu()) {
					getConfigCache().customItemRecipes.addAll(this.getRecipes());
				}
			}
		}
	}

	CustomItem clone();

	@Contract("null -> false")
	default boolean isSimilar(@Nullable ItemStack itemStack) {
		if (
				itemStack == null
				|| itemStack.getType() != this.getItemStack().getType()
				|| !itemStack.hasItemMeta()
				|| !itemStack.getItemMeta().hasCustomModelData()
				|| !this.getItemStack().getItemMeta().hasCustomModelData()
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
