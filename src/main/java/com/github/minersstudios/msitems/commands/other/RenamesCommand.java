package com.github.minersstudios.msitems.commands.other;

import com.github.minersstudios.msitems.items.RenameableItem;
import com.github.minersstudios.msitems.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RenamesCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			return ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
		}
		player.openInventory(RenameableItem.Menu.getInventory(0));
		return true;
	}
}
