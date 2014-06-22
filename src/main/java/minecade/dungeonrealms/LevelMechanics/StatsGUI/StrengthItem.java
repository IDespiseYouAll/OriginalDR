package minecade.dungeonrealms.LevelMechanics.StatsGUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import me.vilsol.menuengine.engine.BonusItem;
import me.vilsol.menuengine.engine.DynamicMenuModel;
import me.vilsol.menuengine.engine.MenuItem;
import org.bukkit.event.inventory.ClickType;
import me.vilsol.menuengine.utils.Builder;
import minecade.dungeonrealms.LevelMechanics.PlayerLevel;
import minecade.dungeonrealms.models.PlayerModel;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StrengthItem implements MenuItem, BonusItem {

	private PlayerModel drPlayer;
	private PlayerLevel pLevel;
	private int points = 0;
	int slot = -1;

	@Override
	public void registerItem() {
		MenuItem.items.put(this.getClass(), this);
	}

	@Override
	public void execute(Player plr, ClickType click) {
		if (slot == -1) {
			for (Entry<Integer, MenuItem> entry : DynamicMenuModel.getMenu(plr).getDynamicItems().entrySet()) {
				if (((MenuItem) entry.getValue()).getItem().equals(getItem())) {
					slot = entry.getKey();
				}
			}
		}
		if (pLevel.getTempFreePoints() > 0) {
		    if (click.equals(ClickType.LEFT)) {
	            allocatePoints(1, plr);
	        }
		    else if (click.equals(ClickType.SHIFT_RIGHT)) {
		        allocatePoints(-PlayerLevel.POINTS_PER_LEVEL, plr);
		    }
		    else if (click.equals(ClickType.SHIFT_LEFT) && pLevel.getTempFreePoints() >= PlayerLevel.POINTS_PER_LEVEL) {
		        allocatePoints(PlayerLevel.POINTS_PER_LEVEL, plr);
		    }
	        else if (click.equals(ClickType.RIGHT)) {
	            allocatePoints(-1, plr);
	        }
	        else if (click.equals(ClickType.MIDDLE) || click.equals(ClickType.DOUBLE_CLICK)) {
	        }
		}
	}

	@Override
	public ItemStack getItem() {
		return new Builder(Material.MAP)
				.setName(ChatColor.DARK_PURPLE + "Strength")
				.setDurability((short) 2)
				.setLore(
						Arrays.asList(ChatColor.GRAY + "Adds armor, block chance, axe ", ChatColor.GRAY
								+ "damage, and polearm damage.", ChatColor.GREEN + "Allocated Points: " + points, ChatColor.RED + "Free Points: " + pLevel.getTempFreePoints())).getItem();
	}

	@Override
	public void setBonusData(Object player) {
		drPlayer = (PlayerModel) player;
		pLevel = drPlayer.getPlayerLevel();
		points = pLevel.getStrPoints();
		pLevel.setTempFreePoints(pLevel.getFreePoints());
	}

	private void allocatePoints(int points, Player plr) {
	    if (points > 0 || (points < 0 && (this.points - pLevel.getStrPoints()) > 0)) {
	        ItemMeta im = getItem().getItemMeta();
	        List<String> lore = new ArrayList<String>(im.getLore());
	        this.points += points;
	        lore.set(lore.size() - 2, lore.get(lore.size() - 2).split(":")[0] + " " + points);
	        pLevel.setTempFreePoints(pLevel.getTempFreePoints() - points);
	        lore.set(lore.size() - 1, lore.get(lore.size() - 1).split(":")[0] + " " + pLevel.getTempFreePoints());
	        im.setLore(lore);
	        getItem().setItemMeta(im);
	        plr.playSound(plr.getLocation(), Sound.SHEEP_SHEAR, 1.0F, 1.3F);
	        for (Entry<Integer, MenuItem> entry : DynamicMenuModel.getMenu(plr).getDynamicItems().entrySet()) {
	            DynamicMenuModel.getMenu(plr).getInventory().setItem(entry.getKey(), entry.getValue().getItem());
	        }
	    }
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

}