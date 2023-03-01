package com.github.minersstudios.msitems.items.items;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msitems.MSItems;
import com.github.minersstudios.msitems.items.CustomItem;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class Plumbum implements CustomItem {
	private @NotNull NamespacedKey namespacedKey;
	private @NotNull ItemStack itemStack;
	private @Nullable List<Recipe> recipes;
	private boolean showInCraftsMenu;

	public Plumbum() {
		this.namespacedKey = new NamespacedKey(MSItems.getInstance(), "plumbum_ingot");
		this.itemStack = new ItemStack(Material.PAPER);
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledText("Свинцовый слиток"));
		itemMeta.setCustomModelData(12000);
		this.itemStack.setItemMeta(itemMeta);
		ItemStack input = Objects.requireNonNull(MSCore.getConfigCache().customItemMap.getByPrimaryKey("raw_plumbum")).getItemStack();
		FurnaceRecipe furnaceRecipe = new FurnaceRecipe(
				new NamespacedKey(MSItems.getInstance(), "plumbum_ingot_furnace"),
				this.itemStack,
				new RecipeChoice.ExactChoice(input),
				0.7f,
				200
		);
		BlastingRecipe blastingRecipe = new BlastingRecipe(
				new NamespacedKey(MSItems.getInstance(), "plumbum_ingot_blast"),
				this.itemStack,
				new RecipeChoice.ExactChoice(input),
				0.7f,
				100
		);
		this.recipes = Lists.newArrayList(furnaceRecipe, blastingRecipe);
		this.showInCraftsMenu = false;
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
