package nl.alwayslucky.mtwwitwas.managers;

import nl.alwayslucky.mtwwitwas.Main;
import nl.alwayslucky.mtwwitwas.utils.ItemUtils;
import nl.alwayslucky.mtwwitwas.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class LaunderManager {

    public static void startLaundering(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();

        ItemStack inHand = player.getInventory().getItemInMainHand();
        ItemStack dirtyItem = ItemUtils.getConfiguredItem("witwassen.items.dirty-money");

        if (!ItemUtils.isSimilar(inHand, dirtyItem)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    config.getString("messages.no-dirty-money")));
            return;
        }

        inHand.setAmount(inHand.getAmount() - 1);
        Location target = LocationUtils.getRandomLaunderLocation();

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                config.getString("messages.go-to-location")
                        .replace("%x%", String.valueOf(target.getBlockX()))
                        .replace("%y%", String.valueOf(target.getBlockY()))
                        .replace("%z%", String.valueOf(target.getBlockZ()))
        ));

        Location targetLocation = target.clone();

        long delayInSeconds = config.getLong("witwassen.launder-delay");
        double radius = config.getDouble("witwassen.launder-radius");

        long[] timeLeft = {delayInSeconds};

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                if (player.getLocation().distance(targetLocation) <= radius) {
                    player.getInventory().addItem(ItemUtils.getConfiguredItem("witwassen.items.clean-money"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            config.getString("messages.success")));

                    if (new Random().nextInt(100) < config.getInt("witwassen.alert-chance")) {
                        String msg = config.getString("messages.alert")
                                .replace("%location%", LocationUtils.format(player.getLocation()));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "112 " + msg);
                    }

                    cancel();
                    return;
                }

                timeLeft[0]--;

                if (timeLeft[0] <= 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            config.getString("messages.not-close-enough")));
                    cancel();
                }

            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }
}
