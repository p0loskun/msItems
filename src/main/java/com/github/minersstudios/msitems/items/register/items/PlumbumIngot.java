package com.github.minersstudios.msitems.items.register.items;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.MSBlockUtils;
import com.github.minersstudios.mscore.utils.MSItemUtils;
import com.github.minersstudios.msitems.MSItems;
import com.github.minersstudios.msitems.items.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlumbumIngot implements CustomItem {
	private @NotNull NamespacedKey namespacedKey;
	private @NotNull ItemStack itemStack;
	private @Nullable List<Map.Entry<Recipe, Boolean>> recipes;

	public PlumbumIngot() {
		this.namespacedKey = new NamespacedKey(MSItems.getInstance(), "plumbum_ingot");
		this.itemStack = new ItemStack(Material.PAPER);
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledText("Свинцовый слиток"));
		itemMeta.setCustomModelData(12000);
		itemMeta.getPersistentDataContainer().set(
				MSItemUtils.CUSTOM_ITEM_TYPE_NAMESPACED_KEY,
				PersistentDataType.STRING,
				this.getNamespacedKey().getKey()
		);
		this.itemStack.setItemMeta(itemMeta);
	}

	@Override
	public @NotNull List<Map.Entry<Recipe, Boolean>> initRecipes() {
		ItemStack input = Objects.requireNonNull(MSCore.getCache().customItemMap.getByPrimaryKey("raw_plumbum")).getItemStack();
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
		ItemStack plumbumBlock = MSBlockUtils.getCustomBlockItem("plumbum_block");
		ShapedRecipe blockShapedRecipe = new ShapedRecipe(new NamespacedKey(MSItems.getInstance(), "plumbum_ingot_from_block"), this.itemStack.clone().add(8))
				.shape("I")
				.setIngredient('I', new RecipeChoice.ExactChoice(plumbumBlock));
		return this.recipes = List.of(
				Map.entry(furnaceRecipe, false),
				Map.entry(blastingRecipe, false),
				Map.entry(blockShapedRecipe, true)
		);
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
	public @Nullable List<Map.Entry<Recipe, Boolean>> getRecipes() {
		return this.recipes;
	}

	@Override
	public void setRecipes(@Nullable List<Map.Entry<Recipe, Boolean>> recipes) {
		this.recipes = recipes;
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
