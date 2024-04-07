package thedavid.cloneableitem.scheduler;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import org.bukkit.inventory.ItemStack;
import thedavid.cloneableitem.CloneableItem;

public class CloneItem extends org.bukkit.scheduler.BukkitRunnable{
	CloneableItem cloneableItem;
	public CloneItem(CloneableItem cloneableItem){
		this.cloneableItem = cloneableItem;
	}
	@Override
	public void run(){
		cloneableItem.cloneItemCommand.cloningPlayers.forEach((player, cloningPlayer) -> {
			if(cloningPlayer.isCloningItem){
				cloningPlayer.clonedAmount++;
				if(cloningPlayer.isOpeningGui){
					int stackAmount = cloningPlayer.clonedAmount / cloningPlayer.item.getMaxStackSize();
					int remainder = cloningPlayer.clonedAmount % cloningPlayer.item.getMaxStackSize();
					cloningPlayer.clonedItemsPane.clear();
					cloningPlayer.clonedItems.clear();
					for(int i = 0; i < stackAmount; i++){
						ItemStack itemStack = new ItemStack(cloningPlayer.type, cloningPlayer.item.getMaxStackSize());
						GuiItem guiItem = new GuiItem(itemStack);
						cloningPlayer.clonedItemsPane.addItem(guiItem);
						cloningPlayer.clonedItems.put(guiItem.getItem(), guiItem);
					}
					ItemStack itemStack = new ItemStack(cloningPlayer.type, remainder);
					GuiItem guiItem = new GuiItem(itemStack);
					cloningPlayer.clonedItemsPane.addItem(guiItem);
					cloningPlayer.clonedItems.put(guiItem.getItem(), guiItem);
					cloningPlayer.gui.update();
				}
			}
		});
	}
}
