package com.github.minersstudios.msitems.items.items;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.items.CustomItem;
import com.github.minersstudios.msitems.utils.ChatUtils;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Wrench implements CustomItem {
	private @NotNull NamespacedKey namespacedKey;
	private @NotNull ItemStack itemStack;
	private @Nullable Recipe recipe;
	private boolean showInCraftsMenu;

	public Wrench() {
		this.namespacedKey = new NamespacedKey(Main.getInstance(), "wrench");
		this.itemStack = new ItemStack(Material.IRON_SHOVEL);
		ItemMeta itemMeta = this.itemStack.getItemMeta();
		itemMeta.displayName(ChatUtils.createDefaultStyledName("Гаечный ключ"));
		itemMeta.setCustomModelData(1);
		itemMeta.lore(Lists.newArrayList(
				Component.text("С его помощью вы можете").style(ChatUtils.COLORLESS_DEFAULT_STYLE).color(NamedTextColor.GRAY),
				Component.text("изменять вид декораций").style(ChatUtils.COLORLESS_DEFAULT_STYLE).color(NamedTextColor.GRAY)
		));
		this.itemStack.setItemMeta(itemMeta);
		this.recipe = new ShapedRecipe(new NamespacedKey(Main.getInstance(), "wrench"), this.itemStack)
				.shape("I", "I", "I")
				.setIngredient('I', Material.IRON_INGOT);
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
	public @Nullable Recipe getRecipe() {
		return this.recipe;
	}

	@Override
	public void setRecipe(@Nullable Recipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public boolean isShowInCraftsMenu() {
		return this.showInCraftsMenu;
	}

	@Override
	public void setShowInCraftsMenu(boolean showInCraftsMenu) {
		this.showInCraftsMenu = showInCraftsMenu;
	}
}
