package antiautoclicker.nick.antiautoclicker.Nick;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class antiautoclicker extends JavaPlugin {

    private static antiautoclicker plugin;
    public ArrayList<String> clickList = new ArrayList<>();
    @Override
    public void onEnable() {
        plugin = this;

        getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new ClickCheck(), this);

        Bukkit.getLogger().info("Enabling Anti-Autoclicker");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Disabling Anti-Autoclicker");
    }

    public static antiautoclicker getPlugin(){
        return plugin;
    }

    // Remove player from clickList
    public void removePlayer(Player player){
        this.clickList.remove(player.getName());
    }

    // Add player to clickList
    public void addPlayer(Player player){
        this.clickList.add(player.getName());
    }
}
