package tk.yallandev.utilities.bukkit.utils;

import net.md_5.bungee.api.ChatColor;

public class StringUtils {

	public static String translateColor(char colorChar, String message) {
		return ChatColor.translateAlternateColorCodes(colorChar, message);
	}

	public static String translateColor(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

}
