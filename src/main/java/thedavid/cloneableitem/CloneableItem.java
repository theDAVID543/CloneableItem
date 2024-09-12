package thedavid.cloneableitem;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import thedavid.cloneableitem.handler.CloneItemCommand;
import thedavid.cloneableitem.handler.DataHandler;
import thedavid.cloneableitem.manager.CloningPlayerManager;
import thedavid.cloneableitem.scheduler.CloneItem;

import java.util.Objects;

public final class CloneableItem extends JavaPlugin {
    public static JavaPlugin instance;
    public CloneItemCommand cloneItemCommand;
    public CloneItem cloneItem;
    public DataHandler dataHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        cloneItemCommand = new CloneItemCommand();
        Objects.requireNonNull(Bukkit.getPluginCommand("cloneitem")).setExecutor(cloneItemCommand);
        cloneItem = new CloneItem(this);
        cloneItem.runTaskTimer(this, 0, 10);
        DataHandler.createDataFile();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic\
        CloningPlayerManager.saveCloningPlayerData();
    }
}
