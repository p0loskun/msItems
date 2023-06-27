package com.github.minersstudios.msitems.items.register.items;

import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.MSBlockUtils;
import com.github.minersstudios.mscore.utils.MSItemUtils;
import com.github.minersstudios.msitems.MSItems;
import com.github.minersstudios.msitems.items.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class RawPlumbum implements CustomItem {
	private @NotNull NamespacedKey namespacedKey;
	private @NotNull ItemStack itemStack;
	private @Nullable List<Map.Entry<Recipe, Boolean>> recipes;

	public RawPlumbum() {
		this.namespacedKey = new NamespacedKey(MSItems.getInstance(), "raw_plumbum");
		this.itemStack = new ItemStack(Material.PAPER);
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledText("Рудной свинец"));
		itemMeta.setCustomModelData(12001);
		itemMeta.getPersistentDataContainer().set(
				MSItemUtils.CUSTOM_ITEM_TYPE_NAMESPACED_KEY,
				PersistentDataType.STRING,
				this.getNamespacedKey().getKey()
		);
		this.itemStack.setItemMeta(itemMeta);
	}

	@Override
	public @NotNull List<Map.Entry<Recipe, Boolean>> initRecipes() {
		ShapedRecipe shapedRecipe = new ShapedRecipe(this.namespacedKey, this.itemStack)
				.shape(
						" I ",
						"BIB",
						" I "
				).setIngredient('I', Material.RAW_IRON)
				.setIngredient('B', Material.WATER_BUCKET);
		ItemStack rawPlumbumBlock = MSBlockUtils.getCustomBlockItem("raw_plumbum_block");
		ShapedRecipe blockShapedRecipe = new ShapedRecipe(new NamespacedKey(MSItems.getInstance(), "raw_plumbum_from_block"), this.itemStack.clone().add(8))
				.shape("I")
				.setIngredient('I', new RecipeChoice.ExactChoice(rawPlumbumBlock));
		return this.recipes = List.of(
				Map.entry(shapedRecipe, true),
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
