package thedavid.cloneableitem.scheduler;

import thedavid.cloneableitem.CloneableItem;
import thedavid.cloneableitem.handler.GuiHandler;

import static thedavid.cloneableitem.manager.CloningPlayerManager.cloningPlayers;

public class CloneItem extends org.bukkit.scheduler.BukkitRunnable{
	CloneableItem cloneableItem;
	public CloneItem(CloneableItem cloneableItem){
		this.cloneableItem = cloneableItem;
	}
	@Override
	public void run(){
		cloningPlayers.forEach((player, cloningPlayer) -> {
			if(cloningPlayer.isCloningItem && cloningPlayer.clonedAmount < 2560){
				cloningPlayer.clonedAmount++;
				GuiHandler.updateItemAmount(cloningPlayer);
			}
		});
	}
}
