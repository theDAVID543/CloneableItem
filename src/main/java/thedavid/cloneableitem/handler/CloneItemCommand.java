package thedavid.cloneableitem.handler;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import thedavid.cloneableitem.impl.CloningPlayer;
import thedavid.cloneableitem.manager.CloningPlayerManager;

import static thedavid.cloneableitem.handler.GuiHandler.setupGuiPanes;
import static thedavid.cloneableitem.handler.GuiHandler.updateItemAmount;
import static thedavid.cloneableitem.manager.CloningPlayerManager.cloningPlayers;

public class CloneItemCommand implements CommandExecutor{
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings){
		if(!(sender instanceof Player)){
			sender.sendMessage("You must be a player to use this command.");
			return false;
		}
		Player player = (Player) sender;
		if(cloningPlayers.containsKey(player)){
			CloningPlayer cloningPlayer = cloningPlayers.get(player);
			cloningPlayer.gui.show(player);
			updateItemAmount(cloningPlayer);
			cloningPlayer.isOpeningGui = true;
			return true;
		}
		ChestGui gui = new ChestGui(6, "物品複製器");
		CloningPlayer cloningPlayer = CloningPlayerManager.loadCloningPlayerData(player);
		if(cloningPlayer == null){
			cloningPlayer = new CloningPlayer(player);
		}
		cloningPlayer.gui = gui;
		cloningPlayers.put(player, cloningPlayer);
		setupGuiPanes(gui, cloningPlayer);
		gui.show(player);
		cloningPlayer.isOpeningGui = true;
		updateItemAmount(cloningPlayer);
		return true;
	}
}
