package antiautoclicker.nick.antiautoclicker.Nick;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class ClickCheck implements Listener {

    final FileConfiguration config = antiautoclicker.getPlugin().getConfig();

    antiautoclicker plugin = antiautoclicker.getPlugin();

    @EventHandler
    public void onPlayerClick(PlayerArmSwingEvent event) {
        Player player = event.getPlayer();

        Entity entityHitting = player.getTargetEntity(5);

        boolean isRiding = (player.getVehicle() != null);
        boolean isHoldingPickaxe = player.getActiveItem().getType().toString().toLowerCase().contains("pickaxe");

        // If player is hitting an entity
        if (entityHitting != null && !isRiding && !isHoldingPickaxe) {

            // Add player to the list that keeps track of player clicks
            plugin.addPlayer(player);

            // Get # of times player is in the list
            int frequency = Collections.frequency(plugin.clickList, player.getName());

            // Kick player is frequency equals threshold in console
            if (frequency == config.getInt("clicks_to_kick") && config.getBoolean("kick_player")) {

                // If notify_admin is enabled, notify people with permission antiautoclicker.notify
                if(config.getBoolean("notify_admin")){
                    String notify_message = config.getString("notify_admin_message");

                    if (notify_message != null) {
                        notify_message = notify_message.replace("%PLAYER%", player.getName());
                    } else{
                        notify_message = player.getName()+" has been kicked for autoclicking.";
                    }

                    Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                    Set<OfflinePlayer> operators = plugin.getServer().getOperators();
                    for (Player notify: onlinePlayers){
                        if(notify.hasPermission("antiautoclicker.notify") || operators.contains(notify)){
                            Msg.send(notify, notify_message);
                        }
                    }

                }

                // Kicks player
                if (config.getString("kick_reason") != null)
                    player.kick(Component.text(Objects.requireNonNull(config.getString("kick_reason"))));
                else
                    player.kick();
                Bukkit.getLogger().warning(player.getName()+" HAS BEEN CAUGHT AUTO-CLICKING");

            }

            // Delay to remove player from clickList
            Bukkit.getScheduler().scheduleSyncDelayedTask(antiautoclicker.getPlugin(), () -> {
                plugin.removePlayer(player);

            }, 20L * config.getLong("time_clicking"));
        }
    }

}
