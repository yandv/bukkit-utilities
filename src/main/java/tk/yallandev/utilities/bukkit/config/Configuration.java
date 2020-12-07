package tk.yallandev.utilities.bukkit.config;

import static tk.yallandev.utilities.bukkit.utils.StringUtils.translateColor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Joiner;

import lombok.Getter;
import tk.yallandev.utilities.bukkit.item.ItemBuilder;

@Getter
public class Configuration {

	private File file;
	private FileConfiguration fileConfiguration;

	private Map<String, Object> configMap;

	public Configuration(File file, FileConfiguration fileConfiguration) {
		this.file = file;
		this.fileConfiguration = fileConfiguration;

		this.configMap = new HashMap<>();
	}

	public static Configuration fromPath(Plugin plugin, String path) {
		File file = new File(plugin.getDataFolder(), path);

		if (!file.exists()) {
			plugin.saveResource(path, false);
			file = new File(plugin.getDataFolder(), path);
		}

		FileConfiguration fileConfiguration = Utf8YamlConfiguration.loadConfiguration(file);
		return new Configuration(file, fileConfiguration);
	}

	public ConfigurationSection asConfigurationSection(String path) {
		if (this.configMap.containsKey(path)) {
			return (ConfigurationSection) this.configMap.get(path);
		}

		ConfigurationSection object = fileConfiguration.getConfigurationSection(path);
		this.configMap.put(path, object);
		return object;
	}

	public String asString(String path) {
		if (this.configMap.containsKey(path)) {
			return (String) this.configMap.get(path);
		}

		String object = getString(path);
		this.configMap.put(path, object);
		return object;
	}

	public String getString(String path) {
		return fileConfiguration.getString(path, String.format("[not-found: %s]", path));
	}

	public boolean asBoolean(String path) {
		if (this.configMap.containsKey(path)) {
			return (boolean) this.configMap.get(path);
		}

		boolean object = getBoolean(path);
		this.configMap.put(path, object);
		return object;
	}

	public boolean getBoolean(String path) {
		return fileConfiguration.getBoolean(path, false);
	}

	public int asInteger(String path) {
		if (this.configMap.containsKey(path)) {
			return (int) this.configMap.get(path);
		}

		int object = getInteger(path);
		this.configMap.put(path, object);
		return object;
	}

	public int getInteger(String path) {
		return fileConfiguration.getInt(path, 1);
	}

	public double asDouble(String path) {
		if (this.configMap.containsKey(path)) {
			return (double) this.configMap.get(path);
		}

		double object = fileConfiguration.getDouble(path, 1);
		this.configMap.put(path, object);
		return object;
	}

	public long asLong(String path) {
		if (this.configMap.containsKey(path)) {
			return (long) this.configMap.get(path);
		}

		long object = fileConfiguration.getLong(path, 1);
		this.configMap.put(path, object);
		return object;
	}

	public String getStringListAsString(String path) {
		if (this.configMap.containsKey(path)) {
			return (String) this.configMap.get(path);
		}

		String object = Joiner.on("\n").join(fileConfiguration.getStringList(path));
		this.configMap.put(path, object);
		return object;
	}

	public ItemStack asItemStack(String path) {
		if (this.configMap.containsKey(path)) {
			return (ItemStack) this.configMap.get(path);
		}

		ItemStack itemStack = getItemStack(path);
		this.configMap.put(path, itemStack);
		return itemStack;
	}

	public ItemStack getItemStack(String path) {
		String displayName = fileConfiguration.getString(path + ".displayName");
		Material material = null;

		try {
			material = Material.valueOf(fileConfiguration.getString(path + ".material"));
		} catch (Exception e) {
			Integer materialId = null;

			try {
				materialId = fileConfiguration.getInt(path + ".material");
			} catch (NumberFormatException ex) {
			}

			material = Material.getMaterial(materialId);
		}

		Short data = null;

		try {
			data = (short) fileConfiguration.getInt(path + ".data");
		} catch (NumberFormatException ex) {
		}

		int amount = fileConfiguration.getInt(path + ".amount", 1);

		List<String> loreList = new ArrayList<>();

		if (fileConfiguration.contains(path + ".lore"))
			loreList = fileConfiguration.getStringList(path + ".lore");

		String skinName = fileConfiguration.getString(path + ".skinName");
		String skinLink = fileConfiguration.getString(path + ".skinLink");

		ItemBuilder itemBuilder = new ItemBuilder();

		if (material != null)
			itemBuilder.type(material);

		itemBuilder.name(translateColor(displayName));
		itemBuilder.durability(data);
		itemBuilder.lore(loreList);
		itemBuilder.skin(skinName);
		itemBuilder.skinURL(skinLink);
		itemBuilder.amount(amount);

		ItemStack itemStack = itemBuilder.build();
		return itemStack;
	}

	public void set(String path, Object value) {
		this.configMap.put(path, value);
		this.fileConfiguration.set(path, value);
	}

	public void reloadConfig() {
		this.fileConfiguration = Utf8YamlConfiguration.loadConfiguration(file);
		this.configMap.clear();
	}

	public void save() {
		try {
			this.fileConfiguration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
