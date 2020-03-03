package site.thefakejanedoe.st6utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class HammerClass implements Listener{
	
	private ST6Utils plugin;
	
	public HammerClass(ST6Utils st6utils) {
		this.plugin = st6utils;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void playerBlockBreakEvent(BlockBreakEvent e) {
		if(!(e.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("pickaxe")
				 || e.getPlayer().getInventory().getItemInMainHand().getType().toString().toLowerCase().contains("shovel"))) return;
		if(e.isCancelled()) return;
		List<String> lores = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore();
		if(lores == null || lores.isEmpty()) return;
		boolean cont = false;
		int enchLvl = 0;
		for(String lore : lores) {
			System.out.println(lore);
			if(lore.contains("Hammering")) {
				String[] strings = lore.split(" ");
				switch (strings[strings.length-1]) {
				case "I":
					enchLvl = 1;
					break;
				case "II":
					enchLvl = 2;
					break;
				case "III":
					enchLvl = 3;
					break;
				default:
					break;
				}
				cont = true;
			}
		}
		BlockFace blockFace;
		blockFace = getBlockFace(e.getPlayer());
		if(blockFace == null) {
			e.getPlayer().sendMessage("&cWe're sorry but something happened with your hammer. Please report the bug!");
			e.setCancelled(true);
			return;
		}
		if(enchLvl == 1 && (blockFace.equals(BlockFace.DOWN) || (blockFace.equals(BlockFace.UP)))) return;
		for(Location loc: figureOutBlocks(e.getBlock().getLocation(), enchLvl, e.getPlayer(), blockFace)) {
			if(!isValidBlock(e.getPlayer().getInventory().getItemInMainHand(), loc.getBlock().getType(), loc, e.getPlayer())) continue;
			loc.getBlock().breakNaturally(e.getPlayer().getInventory().getItemInMainHand());
		}
	}
	
	private boolean isValidBlock(ItemStack itemInMainHand, Material type, Location loc, Player p) {
		
		if(itemInMainHand.getType().toString().toLowerCase().contains("pickaxe")) {
			if(plugin.PickaxeList.contains(type)) {
				return true;
			}
		}
		if(itemInMainHand.getType().toString().toLowerCase().contains("shovel")) {
			if(plugin.ShovelList.contains(type)) {
				return true;
			}
		}
		return false;
	}

	public List<Location> figureOutBlocks(Location origin, int level, Player player, BlockFace face){
		List<Location> blocks = new ArrayList<Location>();
		blocks.add(origin.getBlock().getLocation().add(0, -1, 0));
		if(level > 1) {
			blocks.add(origin.getBlock().getLocation().add(0, 1, 0));
			switch(face) {
			case EAST: case WEST:
				blocks.add(origin.getBlock().getLocation().add(0, 0, 1));
				blocks.add(origin.getBlock().getLocation().add(0, 0, -1));
				blocks.add(origin.getBlock().getLocation().add(0, 1, 1));
				blocks.add(origin.getBlock().getLocation().add(0, 1, -1));
				blocks.add(origin.getBlock().getLocation().add(0, -1, 1));
				blocks.add(origin.getBlock().getLocation().add(0, -1, -1));
				if(level > 2) {
					blocks.add(origin.getBlock().getLocation().add(0, 0, -2));
					blocks.add(origin.getBlock().getLocation().add(0, 0, 2));
					blocks.add(origin.getBlock().getLocation().add(0, 1, -2));
					blocks.add(origin.getBlock().getLocation().add(0, 1, 2));
					blocks.add(origin.getBlock().getLocation().add(0, 2, -2));
					blocks.add(origin.getBlock().getLocation().add(0, 2, 2));
					blocks.add(origin.getBlock().getLocation().add(0, -1, -2));
					blocks.add(origin.getBlock().getLocation().add(0, -1, 2));
					blocks.add(origin.getBlock().getLocation().add(0, -2, -2));
					blocks.add(origin.getBlock().getLocation().add(0, -2, 2));
					blocks.add(origin.getBlock().getLocation().add(0, -2, -1));
					blocks.add(origin.getBlock().getLocation().add(0, -2, 0));
					blocks.add(origin.getBlock().getLocation().add(0, -2, 1));
					blocks.add(origin.getBlock().getLocation().add(0, 2, -1));
					blocks.add(origin.getBlock().getLocation().add(0, 2, 0));
					blocks.add(origin.getBlock().getLocation().add(0, 2, 1));
				}
				break;
			case NORTH: case SOUTH:
				blocks.add(origin.getBlock().getLocation().add(1, 0, 0));
				blocks.add(origin.getBlock().getLocation().add(-1, 0, 0));
				blocks.add(origin.getBlock().getLocation().add(1, 1, 0));
				blocks.add(origin.getBlock().getLocation().add(-1, 1, 0));
				blocks.add(origin.getBlock().getLocation().add(1, -1, 0));
				blocks.add(origin.getBlock().getLocation().add(-1, -1, 0));
				if(level > 2) {
					blocks.add(origin.getBlock().getLocation().add(-2, 0, 0));
					blocks.add(origin.getBlock().getLocation().add(2, 0, 0));
					blocks.add(origin.getBlock().getLocation().add(-2, 1, 0));
					blocks.add(origin.getBlock().getLocation().add(2, 1, 0));
					blocks.add(origin.getBlock().getLocation().add(-2, 2, 0));
					blocks.add(origin.getBlock().getLocation().add(2, 2, 0));
					blocks.add(origin.getBlock().getLocation().add(-2, -1, 0));
					blocks.add(origin.getBlock().getLocation().add(2, -1, 0));
					blocks.add(origin.getBlock().getLocation().add(-2, -2, 0));
					blocks.add(origin.getBlock().getLocation().add(2, -2, 0));
					blocks.add(origin.getBlock().getLocation().add(-1, -2, 0));
					blocks.add(origin.getBlock().getLocation().add(0, -2, 0));
					blocks.add(origin.getBlock().getLocation().add(1, -2, 0));
					blocks.add(origin.getBlock().getLocation().add(-1, 2, 0));
					blocks.add(origin.getBlock().getLocation().add(0, 2, 0));
					blocks.add(origin.getBlock().getLocation().add(1, 2, 0));
				}
				break;
			case UP: case DOWN:
				blocks.remove(0);
				blocks.remove(0);
				blocks.add(origin.getBlock().getLocation().add(0, 0, 1));
				blocks.add(origin.getBlock().getLocation().add(0, 0, -1));
				blocks.add(origin.getBlock().getLocation().add(1, 0, 0));
				blocks.add(origin.getBlock().getLocation().add(-1, 0, 0));
				blocks.add(origin.getBlock().getLocation().add(1, 0, 1));
				blocks.add(origin.getBlock().getLocation().add(-1, 0, 1));
				blocks.add(origin.getBlock().getLocation().add(1, 0, -1));
				blocks.add(origin.getBlock().getLocation().add(-1, 0, -1));
				if(level > 2) {
					blocks.add(origin.getBlock().getLocation().add(2, 0, 2));
					blocks.add(origin.getBlock().getLocation().add(2, 0, 1));
					blocks.add(origin.getBlock().getLocation().add(2, 0, 0));
					blocks.add(origin.getBlock().getLocation().add(2, 0, -1));
					blocks.add(origin.getBlock().getLocation().add(2, 0, -2));
					blocks.add(origin.getBlock().getLocation().add(-2, 0, 2));
					blocks.add(origin.getBlock().getLocation().add(-2, 0, 1));
					blocks.add(origin.getBlock().getLocation().add(-2, 0, 0));
					blocks.add(origin.getBlock().getLocation().add(-2, 0, -1));
					blocks.add(origin.getBlock().getLocation().add(-2, 0, -2));
					blocks.add(origin.getBlock().getLocation().add(-1, 0, 2));
					blocks.add(origin.getBlock().getLocation().add(0, 0, 2));
					blocks.add(origin.getBlock().getLocation().add(1, 0, 2));
					blocks.add(origin.getBlock().getLocation().add(-1, 0, -2));
					blocks.add(origin.getBlock().getLocation().add(0, 0, -2));
					blocks.add(origin.getBlock().getLocation().add(1, 0, -2));
				}
				break;
			default:
				break;
			}
		}
		return blocks;
	}
	
	
	public BlockFace getBlockFace(Player player) {
	    List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
	    Block targetBlock = lastTwoTargetBlocks.get(1);
	    Block adjacentBlock = lastTwoTargetBlocks.get(0);
	    return targetBlock.getFace(adjacentBlock);
	}
}
