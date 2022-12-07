package com.github.minersstudios.msitems.commands;

import com.github.minersstudios.msitems.commands.other.GiveCommand;
import com.github.minersstudios.msitems.commands.other.ReloadCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class CommandHandler implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		if (args.length > 0) {
			String utilsCommand = args[0].toLowerCase(Locale.ROOT);
			if ("reload".equalsIgnoreCase(utilsCommand)) {
				return ReloadCommand.runCommand(sender);
			}
			if ("give".equalsIgnoreCase(utilsCommand)) {
				return GiveCommand.runCommand(sender, args);
			}
		}
		return false;
	}
}
