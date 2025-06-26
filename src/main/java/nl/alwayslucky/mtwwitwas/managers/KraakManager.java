package nl.alwayslucky.mtwwitwas.managers;

import nl.alwayslucky.mtwwitwas.Main;
import nl.alwayslucky.mtwwitwas.gui.KraakGUI;
import nl.alwayslucky.mtwwitwas.utils.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class KraakManager {

    public static void startLaundering(Player player) {
        final double radius = 2.0; // afstand waarbinnen hij geaccepteerd wordt
        final Location kraakLocation = LocationUtils.getRandomKraakLocation();

        player.sendMessage(
                ChatColor.GREEN + ("Je hebt 5 minuten om naar een kraak locatie te gaan! %x% %y% %z%")
                        .replace("%x%", String.valueOf(kraakLocation.getBlockX()))
                        .replace("%y%", String.valueOf(kraakLocation.getBlockY()))
                        .replace("%z%", String.valueOf(kraakLocation.getBlockZ()))
        );

        new BukkitRunnable() {
            int timeLeft = 300;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                Location playerLoc = player.getLocation();

                // Check of speler dichtbij de kraakLocation is
                if (playerLoc.distance(kraakLocation) <= radius) {
                    player.sendMessage(ChatColor.GREEN + "Je bent aangekomen bij een kraak locatie!");
                    KraakGUI.openMainMenu(player);
                    cancel();
                    return;
                }

                timeLeft--;
                if (timeLeft <= 0) {
                    player.sendMessage(ChatColor.RED + "Je hebt niet op tijd een kraak locatie bereikt. Opdracht geannuleerd.");
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }
}
