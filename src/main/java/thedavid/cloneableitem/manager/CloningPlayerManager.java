package thedavid.cloneableitem.manager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import thedavid.cloneableitem.handler.DataHandler;
import thedavid.cloneableitem.impl.CloningPlayer;

import javax.annotation.Nullable;
import java.util.*;

public class CloningPlayerManager {

    public static HashMap<OfflinePlayer, CloningPlayer> cloningPlayers = new HashMap<>();

    public static void saveCloningPlayerData() {
        cloningPlayers.forEach((player, cloningPlayer) ->{
            String path = "CloningPlayers." + player.getUniqueId();
            DataHandler.setByteArray(path + ".item", cloningPlayer.item.serializeAsBytes());
            DataHandler.setInteger(path + ".amount", cloningPlayer.clonedAmount);
        });
        DataHandler.saveDataFile();
    }
    public static @Nullable CloningPlayer loadCloningPlayerData(Player player) {
        Set<String> playerUUIDs = DataHandler.getKeys("CloningPlayers");
        if(playerUUIDs == null){
            return null;
        }
        UUID uuid = player.getUniqueId();
        String path = "CloningPlayers." + uuid;
        byte[] data = DataHandler.getByteArray(path + ".item");
        ItemStack itemStack = ItemStack.deserializeBytes(data);
        CloningPlayer cloningPlayer = new CloningPlayer(player);
        cloningPlayer.setItem(itemStack);
        cloningPlayer.clonedAmount = DataHandler.getInteger(path + ".amount");
        return cloningPlayer;
    }
}
