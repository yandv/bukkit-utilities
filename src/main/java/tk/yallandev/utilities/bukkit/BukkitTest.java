package tk.yallandev.utilities.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitTest extends JavaPlugin {

	private UtilityCommon utilityCommon;

	@Override
	public void onEnable() {
		utilityCommon = new UtilityCommon(getLogger(), this);
		utilityCommon.registerListener();
		super.onEnable();
	}

}
