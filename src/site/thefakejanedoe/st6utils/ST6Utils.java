package site.thefakejanedoe.st6utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class ST6Utils extends JavaPlugin{
	
	HashMap<UUID, Inventory> inventories = new HashMap<>();
	public List<Material> PickaxeList = new ArrayList<Material>();
	public List<Material> ShovelList = new ArrayList<Material>();

	private static Economy econ;
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new HammerClass(this), this);
        System.out.println("Economy Setup: " + (econ != null));
        System.out.println("Economy name: " + econ.currencyNameSingular());
        SetupMaterialLists();
	}
	
	public void onDisable() {}
	
	public static Economy getEconomy() {
		return econ;
	}
	
	public void SetupMaterialLists() {
		Material[] AllMats = Material.values();
		ShovelList.add(Material.GRASS_BLOCK);
		ShovelList.add(Material.GRASS);
		ShovelList.add(Material.GRASS_PATH);
		ShovelList.add(Material.SAND);
		ShovelList.add(Material.GRAVEL);
		PickaxeList.add(Material.GRANITE);
		PickaxeList.add(Material.ANDESITE);
		PickaxeList.add(Material.DIORITE);
		
		for(Material mat : AllMats) {
			//Pickaxe Checks
			if (mat.toString().toLowerCase().contains("stone")) {
				PickaxeList.add(mat);
			}
			if (mat.toString().toLowerCase().contains("ore")) {
				PickaxeList.add(mat);
			}
			//Shovel Checks
			//Nothing to check
		}
	}
}
