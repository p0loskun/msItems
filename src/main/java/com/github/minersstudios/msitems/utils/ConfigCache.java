package com.github.minersstudios.msitems.utils;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.items.RenameableItem;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.apache.commons.lang.exception.ExceptionUtils;
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
		try (Stream<Path> path = Files.walk(Paths.get(Main.getInstance().getDataFolder() + "/items"))) {
			path.filter(Files::isRegularFile)
					.map(Path::toFile)
					.forEach((file) -> {
						String fileName = file.getName();
						if (fileName.equalsIgnoreCase("example.yml")) return;
						YamlConfiguration renameableItemConfig = YamlConfiguration.loadConfiguration(file);
						Material material = Material.valueOf(renameableItemConfig.getString("material"));
						ItemStack itemStack = new ItemStack(material);
						ItemMeta itemMeta = itemStack.getItemMeta();
						List<String> lore = renameableItemConfig.getStringList("lore");
						if (!lore.isEmpty()) {
							List<Component> loreComponentList = new ArrayList<>();
							for (String text : lore) {
								loreComponentList.add(Component.text(text));
							}
							itemMeta.lore(loreComponentList);
						}
						itemMeta.setCustomModelData(renameableItemConfig.getInt("custom-model-data"));
						itemStack.setItemMeta(itemMeta);
						RenameableItem renameableItem = new RenameableItem(
								new NamespacedKey(Main.getInstance(), Objects.requireNonNull(renameableItemConfig.getString("namespaced-key"), fileName + " namespaced-key must be NotNull!")),
								Objects.requireNonNull(renameableItemConfig.getString("rename-text"), fileName + " rename-text must be NotNull!"),
								new ItemStack(material),
								itemStack,
								renameableItemConfig.getBoolean("show-in-rename-menu")
						);
						ItemUtils.RENAMEABLE_ITEMS.put(renameableItem.getNamespacedKey().getKey(), renameableItem);
					});
		} catch (IOException e) {
			Main.getInstance().getLogger().info(ExceptionUtils.getFullStackTrace(e));
		}
	}
}
