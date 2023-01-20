package com.github.minersstudios.msitems.utils;

import com.github.minersstudios.msitems.Main;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ChatUtils {
	public static final Style DEFAULT_STYLE = Style.style(
			NamedTextColor.WHITE,
			TextDecoration.OBFUSCATED.withState(false),
			TextDecoration.BOLD.withState(false),
			TextDecoration.ITALIC.withState(false),
			TextDecoration.STRIKETHROUGH.withState(false),
			TextDecoration.UNDERLINED.withState(false)
	);

	public static final Style DEFAULT_STYLE_BOLD = Style.style(
			NamedTextColor.WHITE,
			TextDecoration.OBFUSCATED.withState(false),
			TextDecoration.BOLD.withState(true),
			TextDecoration.ITALIC.withState(false),
			TextDecoration.STRIKETHROUGH.withState(false),
			TextDecoration.UNDERLINED.withState(false)
	);

	public static final Style COLORLESS_DEFAULT_STYLE = Style.style(
			TextDecoration.OBFUSCATED.withState(false),
			TextDecoration.BOLD.withState(false),
			TextDecoration.ITALIC.withState(false),
			TextDecoration.STRIKETHROUGH.withState(false),
			TextDecoration.UNDERLINED.withState(false)
	);
	public static final List<String> PAINTABLE_LORE = Lists.newArrayList(ChatColor.WHITE + "ꀢ");
	public static final List<Component> PAINTABLE_LORE_COMPONENT = Lists.newArrayList(createDefaultStyledName("ꀢ"));

	private ChatUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Sends info message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendInfo(@Nullable Object target, @NotNull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(Component.text(" ").append(message));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(Component.text(" ").append(message));
		} else {
			Bukkit.getLogger().info(convertComponentToString(message));
		}
		return true;
	}

	/**
	 * Sends fine message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendFine(@Nullable Object target, @NotNull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(Symbols.GREEN_EXCLAMATION_MARK.append(message.color(NamedTextColor.GREEN)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.GREEN));
		} else {
			Bukkit.getLogger().info(convertComponentToString(message.color(NamedTextColor.GREEN)));
		}
		return true;
	}

	/**
	 * Sends warning message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendWarning(@Nullable Object target, @NotNull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(Symbols.YELLOW_EXCLAMATION_MARK.append(message.color(NamedTextColor.GOLD)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.GOLD));
		} else {
			Bukkit.getLogger().warning(convertComponentToString(message.color(NamedTextColor.GOLD)));
		}
		return true;
	}

	/**
	 * Sends error message to target
	 *
	 * @param target  target
	 * @param message warning message
	 */
	public static boolean sendError(@Nullable Object target, @NotNull Component message) {
		if (target instanceof Player player) {
			player.sendMessage(Symbols.RED_EXCLAMATION_MARK.append(message.color(NamedTextColor.RED)));
		} else if (target instanceof CommandSender sender && !(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(message.color(NamedTextColor.RED));
		} else {
			Bukkit.getLogger().severe(convertComponentToString(message.color(NamedTextColor.RED)));
		}
		return true;
	}

	/**
	 * Sends message to console with plugin name
	 *
	 * @param level log level
	 * @param message message
	 */
	public static void log(@NotNull Level level, @NotNull String message) {
		Logger.getLogger(Main.getInstance().getName()).log(level, message);
	}

	@Contract("_ -> new")
	public static @NotNull Component createDefaultStyledName(@NotNull String name) {
		return Component.text().append(Component.text(name).style(ChatUtils.DEFAULT_STYLE)).build();
	}

	@Contract("_ -> new")
	public static @NotNull String convertComponentToString(@NotNull Component component) {
		return LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().build().serialize(component);
	}

	@Contract("_ -> new")
	public static @NotNull String convertPlainComponentToString(@NotNull Component component) {
		return PlainTextComponentSerializer.plainText().serialize(component);
	}

	public static @Nullable List<Component> convertStringsToComponents(@Nullable Style style, String @NotNull ... strings) {
		List<Component> components = new ArrayList<>();
		for (String string : strings) {
			Component component = Component.text(string);
			components.add(
					style == null ? component
					: component.style(style)
			);
		}
		return components.isEmpty() ? null : components;
	}

	public static class Symbols {
		public static final Component
				GREEN_EXCLAMATION_MARK = Component.text(" ꀒ "),
				YELLOW_EXCLAMATION_MARK = Component.text(" ꀓ "),
				RED_EXCLAMATION_MARK = Component.text(" ꀑ ");
	}
}
