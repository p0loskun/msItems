package com.github.minersstudios.msitems.commands;

import com.github.minersstudios.mscore.MSCore;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.msitems.MSItems;
import com.github.minersstudios.msitems.items.RenameableItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class ReloadCommand {

	public static boolean runCommand(@NotNull CommandSender sender) {
		long time = System.currentTimeMillis();
		Iterator<Recipe> crafts = Bukkit.recipeIterator();
		while (crafts.hasNext()) {
			Recipe recipe = crafts.next();
			if (recipe instanceof ShapedRecipe shapedRecipe && shapedRecipe.getKey().getNamespace().equals("msitems")) {
				Bukkit.removeRecipe(shapedRecipe.getKey());
			}
		}
		MSCore.getConfigCache().customItemMap.clear();
		MSCore.getConfigCache().renameableItemMap.clear();
		MSCore.getConfigCache().renameableItemsMenu.clear();
		MSCore.getConfigCache().customItemRecipes.clear();
		MSItems.reloadConfigs();
		RenameableItem.Menu.values = MSCore.getConfigCache().renameableItemsMenu.toArray(new RenameableItem[0]);
		if (MSItems.getInstance().isEnabled()) {
			return ChatUtils.sendFine(sender, Component.text("Плагин был успешно перезагружён за " + (System.currentTimeMillis() - time) + "ms"));
		}
		return ChatUtils.sendError(sender, Component.text("Плагин был перезагружён неудачно"));
	}
}
