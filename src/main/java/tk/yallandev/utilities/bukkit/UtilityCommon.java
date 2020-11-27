package tk.yallandev.utilities.bukkit;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import tk.yallandev.utilities.bukkit.item.ActionItemListener;
import tk.yallandev.utilities.bukkit.menu.MenuListener;

@Getter
public class UtilityCommon {

	@Getter
	private static UtilityCommon instance;

	private Logger logger;
	private Plugin plugin;

	public UtilityCommon(Logger logger, Plugin plugin) {
		instance = this;

		this.logger = logger;
		this.plugin = plugin;
	}

	public void registerListener() {
		registerMenu();
		registerActionItem();
	}

	public void registerMenu() {
		Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
	}

	public void registerActionItem() {
		Bukkit.getPluginManager().registerEvents(new ActionItemListener(), plugin);
	}

}
