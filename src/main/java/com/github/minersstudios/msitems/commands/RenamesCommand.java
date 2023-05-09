package com.github.minersstudios.msitems.commands;

import com.github.minersstudios.mscore.MSCommand;
import com.github.minersstudios.mscore.MSCommandExecutor;
import com.github.minersstudios.mscore.inventory.CustomInventory;
import com.github.minersstudios.mscore.utils.ChatUtils;
import com.github.minersstudios.mscore.utils.InventoryUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@MSCommand(
		command = "renames",
		aliases = {"optifine", "rename", "renameables"},
		usage = " ꀑ §cИспользуй: /<command>",
		description = "Открывает меню с переименованиями предметов"
)
public class RenamesCommand implements MSCommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (!(sender instanceof Player player)) {
			ChatUtils.sendError(sender, Component.text("Только игрок может использовать эту команду!"));
			return true;
		}
		CustomInventory inventory = InventoryUtils.getCustomInventory("renames_inventory");
		if (inventory == null) {
			ChatUtils.sendError(sender, Component.text("Похоже, что-то пошло не так..."));
			return true;
		}
		player.openInventory(inventory);
		return true;
	}
}
