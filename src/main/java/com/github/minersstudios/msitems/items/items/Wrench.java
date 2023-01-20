package com.github.minersstudios.msitems.items.items;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.items.CustomItem;
import com.github.minersstudios.msitems.utils.ChatUtils;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Wrench implements CustomItem {
	private @NotNull NamespacedKey namespacedKey;
	private @NotNull ItemStack itemStack;
	private @Nullable List<Recipe> recipes;
	private boolean showInCraftsMenu;

	public Wrench() {
		this.namespacedKey = new NamespacedKey(Main.getInstance(), "wrench");
		this.itemStack = new ItemStack(Material.IRON_SHOVEL);
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledName("Гаечный ключ"));
		itemMeta.setCustomModelData(1);
		itemMeta.lore(ChatUtils.convertStringsToComponents(
				ChatUtils.COLORLESS_DEFAULT_STYLE.color(NamedTextColor.GRAY),
				"С его помощью вы можете",
				"изменять вид декораций,",
				"которые помечены как : ",
				ChatColor.WHITE + "ꀳ"
		));
		this.itemStack.setItemMeta(itemMeta);
		ShapedRecipe shapedRecipe = new ShapedRecipe(this.namespacedKey, this.itemStack)
				.shape("I", "I", "I")
				.setIngredient('I', Material.IRON_INGOT);
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
