package thedavid.cloneableitem.handler;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import thedavid.cloneableitem.impl.CloningPlayer;

import java.util.HashMap;

public class CloneItemCommand implements CommandExecutor{
	public HashMap<Player, CloningPlayer> cloningPlayers = new HashMap<>();
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings){
		if(!(sender instanceof Player)){
			sender.sendMessage("You must be a player to use this command.");
			return false;
		}
		Player player = (Player) sender;
		if(cloningPlayers.containsKey(player)){
			cloningPlayers.get(player).gui.show(player);
			cloningPlayers.get(player).isOpeningGui = true;
			return true;
		}
		ChestGui gui = new ChestGui(6, "物品複製器");
		CloningPlayer cloningPlayer = new CloningPlayer(player, gui);
		cloningPlayers.put(player, cloningPlayer);
		setupGuiPanes(gui, cloningPlayer);
		gui.show(player);
		cloningPlayer.isOpeningGui = true;
		return true;
	}
	public void setupGuiPanes(ChestGui gui, CloningPlayer cloningPlayer){
		setPutItemPane(gui, cloningPlayer);
		setUpPane(gui);
		setLeftPane(gui);
		cloningPlayer.clonedItemsPane = setClonedItemsPane(gui, cloningPlayer);
		gui.setOnBottomClick(e -> {
			if(e.isShiftClick()){
				e.setCancelled(true);
			}
		});
		gui.setOnClose(e -> {
			cloningPlayer.isOpeningGui = false;
		});
		gui.setOnGlobalDrag(e -> {
			e.setCancelled(true);
		});
	}
	OutlinePane setClonedItemsPane(ChestGui gui, CloningPlayer cloningPlayer){
		OutlinePane clonedItemsPane = new OutlinePane(1, 1, 8, 5);
		clonedItemsPane.setOnClick(e -> {
			e.setCancelled(true);
			Player player = (Player) e.getWhoClicked();
			ItemStack cursorItem = player.getItemOnCursor();
			ItemStack clickedItem = e.getCurrentItem();
			if(clickedItem == null || clickedItem.getType() == Material.AIR){
				return;
			}
			if(cursorItem.getType() == Material.AIR){
				player.setItemOnCursor(new ItemStack(clickedItem.clone().getType(), clickedItem.clone().getAmount()));
				cloningPlayer.clonedAmount -= clickedItem.clone().getAmount();
//				clickedItem.setAmount(0);
				clonedItemsPane.removeItem(cloningPlayer.clonedItems.get(clickedItem));
				gui.update();
				return;
			}
		});
		gui.addPane(clonedItemsPane);
		return clonedItemsPane;
	}
	void setPutItemPane(ChestGui gui, CloningPlayer cloningPlayer){
		StaticPane putItemPane = new StaticPane(0, 0, 1, 1);
		putItemPane.setOnClick(e -> {
			e.setCancelled(true);
			Player player = (Player) e.getWhoClicked();
			ItemStack cursorItem = player.getItemOnCursor();
			if(cloningPlayer.isCloningItem){
				if(cursorItem.getType() == Material.AIR){
					player.setItemOnCursor(cloningPlayer.item.clone());
					cloningPlayer.unsetItem();
					putItemPane.clear();
					gui.update();
				}else{
					player.sendMessage(Component.text("請先將正在複製的物品取回").color(NamedTextColor.RED));
				}
				return;
			}
			if(cursorItem.getType() == Material.AIR){
				return;
			}
			if(Tag.WOOL_CARPETS.isTagged(cursorItem.getType()) || Tag.RAILS.isTagged(cursorItem.getType())){
				if(cloningPlayer.clonedAmount != 0){
					player.sendMessage(Component.text("請先將複製出的物品取回").color(NamedTextColor.RED));
					return;
				}
				putItemPane.addItem(new GuiItem(cursorItem.clone().asOne()), 0, 0);
				cloningPlayer.setItem(cursorItem.clone());
				cursorItem.setAmount(cursorItem.getAmount() - 1);
				gui.update();
			}else{
				player.sendMessage(Component.text("只能複製地毯和鐵軌").color(NamedTextColor.RED));
			}
		});
		gui.addPane(putItemPane);
	}
	void setUpPane(ChestGui gui){
		Pattern pattern = new Pattern(
				"11111111"
		);
		PatternPane upPane = new PatternPane(1, 0, 8, 1, pattern);
		GuiItem noItemOutline = new GuiItem(new ItemStack(Material.RED_STAINED_GLASS_PANE), e -> e.setCancelled(true));
		upPane.bindItem('1', noItemOutline);
		gui.addPane(upPane);
	}
	void setLeftPane(ChestGui gui){
		Pattern pattern = new Pattern(
				"1",
				"1",
				"1",
				"1",
				"1"
		);
		PatternPane leftPane = new PatternPane(0, 1, 1, 5, pattern);
		GuiItem noItemOutline = new GuiItem(new ItemStack(Material.RED_STAINED_GLASS_PANE), e -> e.setCancelled(true));
		leftPane.bindItem('1', noItemOutline);
		gui.addPane(leftPane);
	}
}
