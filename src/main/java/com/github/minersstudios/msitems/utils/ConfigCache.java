package com.github.minersstudios.msitems.utils;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.msitems.MSItems;
import com.github.minersstudios.msitems.items.RenameableItem;
import com.github.minersstudios.msitems.items.cosmetics.LeatherHat;
import com.github.minersstudios.msitems.items.items.*;
import com.github.minersstudios.msitems.items.items.cards.CardsBicycle;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class ConfigCache {

	public ConfigCache() {
		try (Stream<Path> path = Files.walk(Paths.get(MSItems.getInstance().getPluginFolder() + "/items"))) {
			path.filter(Files::isRegularFile)
					.map(Path::toFile)
					.forEach((file) -> {
						String fileName = file.getName();
						if (fileName.equalsIgnoreCase("example.yml")) return;
						YamlConfiguration renameableItemConfig = YamlConfiguration.loadConfiguration(file);
						List<String> materialsString = renameableItemConfig.getStringList("material");
						if (materialsString.isEmpty()) {
							materialsString.add(renameableItemConfig.getString("material"));
						}
						List<Material> materials = new ArrayList<>();
						for (String material : materialsString) {
							materials.add(Material.valueOf(material));
						}
						List<ItemStack> renameableItemStacks = new ArrayList<>();
						for (Material material : materials) {
							renameableItemStacks.add(new ItemStack(material));
						}
						ItemStack resultItemStack = new ItemStack(materials.get(0));
						ItemMeta itemMeta = resultItemStack.getItemMeta();
						List<String> lore = renameableItemConfig.getStringList("lore");
						if (!lore.isEmpty()) {
							List<Component> loreComponentList = new ArrayList<>();
							for (String text : lore) {
								loreComponentList.add(Component.text(text));
							}
							itemMeta.lore(loreComponentList);
						}
						itemMeta.setCustomModelData(renameableItemConfig.getInt("custom-model-data"));
						resultItemStack.setItemMeta(itemMeta);
						RenameableItem renameableItem = new RenameableItem(
								new NamespacedKey(MSItems.getInstance(), Objects.requireNonNull(renameableItemConfig.getString("namespaced-key"), fileName + " namespaced-key must be NotNull!")),
								Objects.requireNonNull(renameableItemConfig.getString("rename-text"), fileName + " rename-text must be NotNull!"),
								renameableItemStacks,
								resultItemStack,
								renameableItemConfig.getBoolean("show-in-rename-menu")
						);
						MSCore.getConfigCache().renameableItemMap.put(renameableItem.getNamespacedKey().getKey(), itemMeta.getCustomModelData(), renameableItem);
					});
		} catch (IOException e) {
			MSItems.getInstance().getLogger().info(ExceptionUtils.getStackTrace(e));
		}
	}

	public static void registerItems() {
		new LeatherHat().register();
		new RawPlumbum().register();
		new Plumbum().register();
		new BanSword().register();
		new Wrench().register();
		new CardsBicycle().register();
		new Cocaine().register();
	}
}
