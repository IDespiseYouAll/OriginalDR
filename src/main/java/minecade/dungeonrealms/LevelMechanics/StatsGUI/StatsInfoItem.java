package minecade.dungeonrealms.LevelMechanics.StatsGUI;

import java.util.Arrays;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StatsInfoItem implements MenuItem, BonusItem {

	private PlayerModel drPlayer;
	private PlayerLevel pLevel;
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
	}

	@Override
	public ItemStack getItem() {
		return new Builder(Material.EXP_BOTTLE)
				.setName(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Stat Point Info")
				.setLore(
						Arrays.asList(ChatColor.GREEN + "Points to Allocate: " + pLevel.getTempFreePoints(),
								ChatColor.GRAY + "LCLICK to allocate " + ChatColor.UNDERLINE + "1" + ChatColor.GRAY
										+ " point", ChatColor.GRAY + "RCLICK or SCLICK to allocate ",
								ChatColor.GRAY.toString() + ChatColor.UNDERLINE + "5" + ChatColor.GRAY + " points."))
				.getItem();
	}

	@Override
	public void setBonusData(Object player) {
		drPlayer = (PlayerModel) player;
		pLevel = drPlayer.getPlayerLevel();
		DynamicMenuModel.getMenu(((PlayerModel) player).getPlayer()).setName(
				ChatColor.RED.toString() + ChatColor.BOLD + "Allocate Stat Points");
	}

}