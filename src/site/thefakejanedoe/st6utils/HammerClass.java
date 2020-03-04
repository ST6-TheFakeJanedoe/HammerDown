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
				case "IV":
					enchLvl = 4;
					break;
				case "V":
					enchLvl = 5;
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
		if((level == 1) && (!(face == BlockFace.UP) || (!(face == BlockFace.DOWN)))){
			blocks.add(origin.add(0, -1, 0));
		}
		System.out.println(face);
		switch(face) {
		case UP: case DOWN:
			if((level == 2) || (level == 3)){
				for(int x = origin.getBlockX()-(level-1); x <= (origin.getBlockX()+(level-1)); x++) {
					for(int z = origin.getBlockZ()-(level-1); z <= (origin.getBlockZ()+(level-1)); z++) {
						blocks.add(new Location(origin.getWorld(), x, origin.getBlockY(), z));
					}
				}
			}
			if(level > 3) {
				for(int x = origin.getBlockX()-(level-1); x <= (origin.getBlockX()+(level-1)); x++) {
					for(int z = origin.getBlockZ()-(level-1); z <= (origin.getBlockZ()+(level-1)); z++) {
						blocks.add(new Location(origin.getWorld(), x, origin.getBlockY(), z));
					}
				}
			}
			break;
		case NORTH: case SOUTH:
			if((level == 2) || (level == 3)) {
				for(int x = origin.getBlockX()-(level-1); x <= (origin.getBlockX()+(level-1)); x++) {
					for(int y = origin.getBlockY()-(level-1); y <= (origin.getBlockY()+(level-1)); y++) {
						blocks.add(new Location(origin.getWorld(), x, y, origin.getZ()));
					}
				}
			}
			if((level == 4) || (level == 5)){
				int radius = 0;
				if(level == 4) radius = 1;
				if(level == 5) radius = 2;
				for(int x = origin.getBlockX()-radius; x <= (origin.getBlockX()+radius); x++) {
					for(int y = origin.getBlockY()-radius; y <= (origin.getBlockY()+radius); y++) {
						if(face == BlockFace.NORTH) {
							for(int z = origin.getBlockZ(); z < origin.getBlockZ()+3; z++) {
								blocks.add(new Location(origin.getWorld(), x, y, z));
							}
						} else if(face == BlockFace.SOUTH){
							for(int z = origin.getBlockZ(); z > origin.getBlockZ()-3; z--) {
								blocks.add(new Location(origin.getWorld(), x, y, z));
							}
						}
					}
				}
			}
			break;
		case EAST: case WEST:
			if((level == 2) || (level == 3)) {
				for(int z = origin.getBlockZ()-(level-1); z <= (origin.getBlockZ()+(level-1)); z++) {
					for(int y = origin.getBlockY()-(level-1); y <= (origin.getBlockY()+(level-1)); y++) {
						blocks.add(new Location(origin.getWorld(), origin.getX(), y, z));
					}
				}
			}
			if((level == 4) || (level == 5)){
				int radius = 0;
				if(level == 4) radius = 1;
				if(level == 5) radius = 2;
				for(int z = origin.getBlockZ()-radius; z <= (origin.getBlockZ()+radius); z++) {
					for(int y = origin.getBlockY()-radius; y <= (origin.getBlockY()+radius); y++) {
						if(face == BlockFace.WEST) {
							for(int x = origin.getBlockX(); x < origin.getBlockX()+3; x++) {
								blocks.add(new Location(origin.getWorld(), x, y, z));
							}
						} else if(face == BlockFace.EAST){
							for(int x = origin.getBlockX(); x > origin.getBlockX()-3; x--) {
								blocks.add(new Location(origin.getWorld(), x, y, z));
							}
						}
					}
				}
			}
			
			break;
		default:
			break;
		}
		List<Location> correctedBlocks = new ArrayList<Location>();
		if((face == BlockFace.EAST || face == BlockFace.WEST || face == BlockFace.NORTH || face == BlockFace.SOUTH) && ((level == 3) || (level == 5))) {
			for(Location locat : blocks) {
				correctedBlocks.add(locat.add(0, 1, 0));
			}
			return correctedBlocks;
		} else {
			return blocks;

		}
	}
	
	
	public BlockFace getBlockFace(Player player) {
	    List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
	    Block targetBlock = lastTwoTargetBlocks.get(1);
	    Block adjacentBlock = lastTwoTargetBlocks.get(0);
	    return targetBlock.getFace(adjacentBlock);
	}
}
