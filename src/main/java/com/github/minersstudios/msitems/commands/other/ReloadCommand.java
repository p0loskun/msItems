package com.github.minersstudios.msitems.commands.other;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.items.RenameableItem;
import com.github.minersstudios.msitems.utils.ChatUtils;
import com.github.minersstudios.msitems.utils.ItemUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class ReloadCommand {

	public static boolean runCommand(@NotNull CommandSender sender) {
		long time = System.currentTimeMillis();
		ItemUtils.CUSTOM_ITEMS.clear();
		ItemUtils.RENAMEABLE_ITEMS.clear();
		ItemUtils.RENAMEABLE_ITEMS_MENU.clear();
		ItemUtils.CUSTOM_ITEM_RECIPES.clear();
		HandlerList.unregisterAll(Main.getInstance());
		Iterator<Recipe> crafts = Bukkit.recipeIterator();
		while (crafts.hasNext()) {
			Recipe recipe = crafts.next();
			if (recipe instanceof ShapedRecipe shapedRecipe && shapedRecipe.getKey().getNamespace().equals("msitems")) {
				Bukkit.removeRecipe(shapedRecipe.getKey());
			}
		}
		Main.getInstance().load();
		RenameableItem.Menu.values = ItemUtils.RENAMEABLE_ITEMS_MENU.toArray(new RenameableItem[0]);
		if (Main.getInstance().isEnabled()) {
			return ChatUtils.sendFine(sender, Component.text("Плагин был успешно перезагружён за " + (System.currentTimeMillis() - time) + "ms"));
		}
		return ChatUtils.sendError(sender, Component.text("Плагин был перезагружён неудачно"));
	}
}
