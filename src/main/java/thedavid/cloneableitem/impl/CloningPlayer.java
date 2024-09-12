package thedavid.cloneableitem.impl;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CloningPlayer{
	public OfflinePlayer player;
	public Material type;
	public ItemStack item;
	public ChestGui gui;
	public Boolean isCloningItem = false;
	public Boolean isOpeningGui = false;
	public OutlinePane clonedItemsPane;
	public int clonedAmount = 0;
	public HashMap<ItemStack, GuiItem> clonedItems = new HashMap<>();
	public CloningPlayer(OfflinePlayer player){
		this.player = player;
	}
	public void setItem(ItemStack item){
		this.item = item.asOne();
		type = item.getType();
		isCloningItem = true;
	}
	public void unsetItem(){
		isCloningItem = false;
	}
}
