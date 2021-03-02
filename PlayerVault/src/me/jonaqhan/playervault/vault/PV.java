package me.jonaqhan.playervault.vault;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.jonaqhan.playervault.Main;
import me.jonaqhan.playervault.data.PlayerVault;
import me.jonaqhan.playervault.tools.Chat;

public class PV implements CommandExecutor, Listener {

	public static Map<String, ItemStack[]> storage = new HashMap<String, ItemStack[]>();

	public static Map<String, String> value = new HashMap<String, String>();

	private Main plugin;

	public PV(Main plugin) {
		this.plugin = plugin;

		plugin.getCommand("pv").setExecutor(this);

		Bukkit.getPluginManager().registerEvents(this, plugin);

		if (PlayerVault.getConfig().contains("PlayerVaults"))
			restoreInvs();

		if (PlayerVault.getConfig().contains("Value"))
			restoreInvsV();

	}

	public static void saveInvs() {

		for (Entry<String, ItemStack[]> entry : storage.entrySet()) {
			PlayerVault.getConfig().set("PlayerVaults." + entry.getKey(), entry.getValue());
			;

		}
		PlayerVault.saveConfig();

	}

	public static void restoreInvs() {
		PlayerVault.getConfig().getConfigurationSection("PlayerVaults").getKeys(false).forEach(key -> {
			@SuppressWarnings("unchecked")
			ItemStack[] content = ((List<ItemStack>) PlayerVault.getConfig().get("PlayerVaults." + key))
					.toArray(new ItemStack[0]);
			storage.put(key, content);

		});

	}

	public static void saveInvsV() {

		for (Entry<String, String> entry : value.entrySet()) {
			PlayerVault.getConfig().set("Value." + entry.getKey(), entry.getValue());

		}
		PlayerVault.saveConfig();

	}

	public static void restoreInvsV() {
		PlayerVault.getConfig().getConfigurationSection("Value").getKeys(false).forEach(key -> {
			String content = (String) PlayerVault.getConfig().get("Value." + key);
			value.put(key, content);

		});

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
			return true;

		Player p = (Player) sender;

		if (!p.hasPermission("playervault.use")) {

			p.sendMessage(Chat.tools("&cYou need the permission playervault.use"));

			return true;
		}

		if (args.length == 0) {
			p.sendMessage(Chat.tools("&cYou didn't gave an argument"));

			return true;
		}

		try {

			int value = Integer.parseInt(args[0]);
		} catch (NumberFormatException ex) {

			p.sendMessage(Chat.tools("&cYou didn't gave a value"));

			return true;
		}

		if (!p.hasPermission("playervault.use.vault." + args[0])) {

			p.sendMessage(Chat.tools("&cYou need the permission playervault.use.vault." + args[0]));

			return true;
		}

		value.put(p.getUniqueId().toString(), args[0]);

		Inventory inv = Bukkit.createInventory(p, 54, Chat.tools("&l#00e1ff") + p.getName() + Chat.tools("'s")
				+ Chat.tools("&l#4287f5 vault") + Chat.tools("&l#00ff6a (") + args[0] + ")");

		if (storage.containsKey(p.getUniqueId().toString() + " " + args[0]))
			inv.setContents(storage.get(p.getUniqueId().toString() + " " + args[0]));

		p.sendMessage(Chat.tools("#00e1ffOpened vault #00ff6a(" + args[0] + ")"));
		p.openInventory(inv);

		return false;
	}

	@EventHandler
	public void openInventory(InventoryCloseEvent e) {

		String number = value.get(e.getPlayer().getUniqueId().toString());

		if (e.getView().getTitle().contains(Chat.tools("&l#00e1ff") + e.getPlayer().getName() + Chat.tools("'s")
				+ Chat.tools("&l#4287f5 vault") + Chat.tools("&l#00ff6a (") + number + ")"))
			storage.put(e.getPlayer().getUniqueId().toString() + " " + number, e.getInventory().getContents());

	}

}
