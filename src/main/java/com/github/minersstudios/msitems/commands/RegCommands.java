package com.github.minersstudios.msitems.commands;

import com.github.minersstudios.msitems.Main;
import com.github.minersstudios.msitems.commands.other.RenamesCommand;
import com.github.minersstudios.msitems.tabcompleters.Empty;
import com.github.minersstudios.msitems.tabcompleters.TabCommandHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class RegCommands {

	private RegCommands() {
		throw new IllegalStateException();
	}

	public static void init(@NotNull Main plugin) {
		Objects.requireNonNull(plugin.getCommand("msitems")).setExecutor(new CommandHandler());
		Objects.requireNonNull(plugin.getCommand("msitems")).setTabCompleter(new TabCommandHandler());
		Objects.requireNonNull(plugin.getCommand("renames")).setExecutor(new RenamesCommand());
		Objects.requireNonNull(plugin.getCommand("renames")).setTabCompleter(new Empty());
	}
}
