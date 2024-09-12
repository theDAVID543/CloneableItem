package thedavid.cloneableitem.handler;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PatternPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.util.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import thedavid.cloneableitem.impl.CloningPlayer;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiHandler {
    public static void setupGuiPanes(ChestGui gui, CloningPlayer cloningPlayer){
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
    static OutlinePane setClonedItemsPane(ChestGui gui, CloningPlayer cloningPlayer){
        OutlinePane clonedItemsPane = new OutlinePane(1, 1, 8, 5);
        clonedItemsPane.setOnClick(e -> {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            ItemStack cursorItem = player.getItemOnCursor();
            ItemStack clickedItem = e.getCurrentItem();
            if(clickedItem == null || clickedItem.getType() == Material.AIR){
                return;
            }
            if(e.isShiftClick()){
                Map<Integer, ItemStack> cantAddItems = player.getInventory().addItem(new ItemStack(clickedItem.clone().getType(), clickedItem.clone().getAmount()));
//                player.sendMessage(String.valueOf(cantAddItems));
                int reduceAmount = clickedItem.clone().getAmount();
                int cantAddItemCount = 0;
                if(!cantAddItems.isEmpty()){
                    cantAddItemCount = cantAddItems.get(0).getAmount();
                    reduceAmount -= cantAddItemCount;
                }
                cloningPlayer.clonedAmount -= reduceAmount;
//                player.sendMessage("reduceAmount: " + reduceAmount);
//                player.sendMessage("cantAddItems: " + cantAddItemCount);
//                player.sendMessage("clickedItem.clone().getAmount(): " + clickedItem.clone().getAmount());
                updateItemAmount(cloningPlayer);
            }else {
                if (cursorItem.getType() == Material.AIR) {
                    player.setItemOnCursor(new ItemStack(clickedItem.clone().getType(), clickedItem.clone().getAmount()));
                    cloningPlayer.clonedAmount -= clickedItem.clone().getAmount();
//				    clickedItem.setAmount(0);
                    updateItemAmount(cloningPlayer);
                }
            }
        });
        gui.addPane(clonedItemsPane);
        return clonedItemsPane;
    }
    static void setPutItemPane(ChestGui gui, CloningPlayer cloningPlayer){
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
            if(Tag.WOOL_CARPETS.isTagged(cursorItem.getType()) || Tag.RAILS.isTagged(cursorItem.getType()) || cursorItem.getType() == Material.SAND || cursorItem.getType() == Material.RED_SAND){
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
        if(cloningPlayer.isCloningItem){
            putItemPane.addItem(new GuiItem(cloningPlayer.item), 0, 0);
        }
        gui.addPane(putItemPane);
    }
    static void setUpPane(ChestGui gui){
        Pattern pattern = new Pattern(
                "11111111"
        );
        PatternPane upPane = new PatternPane(1, 0, 8, 1, pattern);
        GuiItem noItemOutline = new GuiItem(new ItemStack(Material.RED_STAINED_GLASS_PANE), e -> e.setCancelled(true));
        upPane.bindItem('1', noItemOutline);
        gui.addPane(upPane);
    }
    static void setLeftPane(ChestGui gui){
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
    public static void updateItemAmount(CloningPlayer cloningPlayer){
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
