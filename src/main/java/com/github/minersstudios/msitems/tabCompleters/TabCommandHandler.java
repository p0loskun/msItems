package com.github.minersstudios.msitems.tabCompleters;

import com.github.minersstudios.msitems.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabCommandHandler implements TabCompleter {

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull ... args) {
		List<String> completions = new ArrayList<>();
		switch (args.length) {
			case 1 -> {
				completions.add("reload");
				completions.add("give");
			}
			case 2 -> {
				for (Player player : Bukkit.getOnlinePlayers()) {
					completions.add(player.getName());
				}
			}
			case 3 -> {
				completions.addAll(ItemUtils.CUSTOM_ITEMS.keySet());
				completions.addAll(ItemUtils.RENAMEABLE_ITEMS.keySet());
			}
		}
		return completions;
	}
}
