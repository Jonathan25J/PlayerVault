package me.jonaqhan.playervault.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.jonaqhan.playervault.Main;

public class PlayerVault {

	private static Main plugin;
	private static FileConfiguration dataConfig = null;
	private static File configFile = null;

	public PlayerVault(Main plugin) {
		this.plugin = plugin;
		saveDefaultConfig();
	}

	public static void reloadConfig() {

		if (configFile == null)
			configFile = new File(plugin.getDataFolder(), "playervault.yml");

		dataConfig = YamlConfiguration.loadConfiguration(configFile);

		InputStream defaultStream = plugin.getResource("playervault.yml");

		if (defaultStream != null) {
			YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
			dataConfig.setDefaults(defaultConfig);
		}

	}

	public static FileConfiguration getConfig() {

		if (dataConfig == null)
			reloadConfig();

		return dataConfig;

	}

	public static void saveConfig() {

		if (dataConfig == null || configFile == null)
			return;

		try {
			getConfig().save(configFile);
		} catch (IOException e) {
		}

	}

	public void saveDefaultConfig() {
		if (configFile == null) {

			configFile = new File(this.plugin.getDataFolder(), "playervault.yml");
		}

		if (!configFile.exists()) {
			plugin.saveResource("playervault.yml", false);

		}

	}

}
