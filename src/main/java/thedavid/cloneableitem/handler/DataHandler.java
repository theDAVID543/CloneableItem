package thedavid.cloneableitem.handler;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import thedavid.cloneableitem.CloneableItem;

import java.io.File;
import java.io.IOException;
import java.util.HexFormat;
import java.util.Set;

public class DataHandler {
    static File playerCloneDataFile;
    static FileConfiguration playerCloneDataConfig;
    public static void createDataFile(){
        playerCloneDataFile = new File(CloneableItem.instance.getDataFolder(), "data.yml");
        if (!playerCloneDataFile.exists()) {
            playerCloneDataFile.getParentFile().mkdirs();
            CloneableItem.instance.saveResource("data.yml", false);
        }

        playerCloneDataConfig = new YamlConfiguration();
        try {
            playerCloneDataConfig.load(playerCloneDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public static void saveDataFile(){
        try {
            playerCloneDataConfig.save(playerCloneDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void setString(String path, String value){
        playerCloneDataConfig.set(path, value);
    }
    public static void setInteger(String path, int value){
        playerCloneDataConfig.set(path, value);
    }
    public static void setByteArray(String path, byte[] value){
        HexFormat hexFormat = HexFormat.of();
        playerCloneDataConfig.set(path, hexFormat.formatHex(value));
    }
    public static String getString(String path){
        return playerCloneDataConfig.getString(path);
    }
    public static int getInteger(String path){
        return playerCloneDataConfig.getInt(path);
    }
    public static byte[] getByteArray(String path){
        HexFormat hexFormat = HexFormat.of();
        return hexFormat.parseHex(playerCloneDataConfig.getString(path));
    }
    public static Set<String> getKeys(String path){
        if(playerCloneDataConfig.getConfigurationSection(path) == null){
            return null;
        }
        return playerCloneDataConfig.getConfigurationSection(path).getKeys(false);
    }
}
