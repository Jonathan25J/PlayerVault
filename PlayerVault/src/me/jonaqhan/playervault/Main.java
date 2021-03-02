package me.jonaqhan.playervault;

import org.bukkit.plugin.java.JavaPlugin;

import me.jonaqhan.playervault.data.PlayerVault;
import me.jonaqhan.playervault.vault.PV;

public class Main extends JavaPlugin {

	public PlayerVault data;

	@Override
	public void onEnable() {

		this.data = new PlayerVault(this);

		PlayerVault.saveConfig();

		new PV(this);

	}
	
	public void onDisable() {

		if (!PV.storage.isEmpty())
			PV.saveInvs();
		
		if (!PV.value.isEmpty())
			PV.saveInvsV();


	}


}
