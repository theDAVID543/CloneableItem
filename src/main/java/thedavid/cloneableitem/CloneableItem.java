package thedavid.cloneableitem;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import thedavid.cloneableitem.handler.CloneItemCommand;
import thedavid.cloneableitem.scheduler.CloneItem;

import java.util.Objects;

public final class CloneableItem extends JavaPlugin {
    public CloneItemCommand cloneItemCommand;
    public CloneItem cloneItem;

    @Override
    public void onEnable() {
        // Plugin startup logic
        cloneItemCommand = new CloneItemCommand();
        Objects.requireNonNull(Bukkit.getPluginCommand("cloneitem")).setExecutor(cloneItemCommand);
        cloneItem = new CloneItem(this);
        cloneItem.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
