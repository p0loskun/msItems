package com.github.minersstudios.msitems.items.items;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msitems.MSItems;
import com.github.minersstudios.msitems.items.CustomItem;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RawPlumbum implements CustomItem {
	private @NotNull NamespacedKey namespacedKey;
	private @NotNull ItemStack itemStack;
	private @Nullable List<Recipe> recipes;
	private boolean showInCraftsMenu;

	public RawPlumbum() {
		this.namespacedKey = new NamespacedKey(MSItems.getInstance(), "raw_plumbum");
		this.itemStack = new ItemStack(Material.PAPER);
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledText("Рудной свинец"));
		itemMeta.setCustomModelData(12001);
		this.itemStack.setItemMeta(itemMeta);
		ShapedRecipe shapedRecipe = new ShapedRecipe(this.namespacedKey, this.itemStack)
				.shape(
						" I ",
						"BIB",
						" I "
				).setIngredient('I', Material.RAW_IRON)
				.setIngredient('B', Material.WATER_BUCKET);
		this.recipes = Lists.newArrayList(shapedRecipe);
		this.showInCraftsMenu = true;
	}

	@Override
	public @NotNull NamespacedKey getNamespacedKey() {
		return this.namespacedKey;
	}

	@Override
	public void setNamespacedKey(@NotNull NamespacedKey namespacedKey) {
		this.namespacedKey = namespacedKey;
	}

	@Override
	public @NotNull ItemStack getItemStack() {
		return this.itemStack;
	}

	@Override
	public void setItemStack(@NotNull ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	@Override
	public @Nullable List<Recipe> getRecipes() {
		return this.recipes;
	}

	@Override
	public void setRecipes(@Nullable List<Recipe> recipes) {
		this.recipes = recipes;
	}

	@Override
	public boolean isShowInCraftsMenu() {
		return this.showInCraftsMenu;
	}

	@Override
	public void setShowInCraftsMenu(boolean showInCraftsMenu) {
		this.showInCraftsMenu = showInCraftsMenu;
	}

	@Override
	public @NotNull CustomItem clone() {
		try {
			return (CustomItem) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
