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

import java.util.Random;

public class LaunderManager {

    public static void startLaundering(Player player) {
        FileConfiguration config = Main.getInstance().getConfig();

        ItemStack inHand = player.getInventory().getItemInMainHand();
        ItemStack dirtyItem = ItemUtils.getConfiguredItem("items.dirty-money");

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

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            double radius = config.getDouble("launder-radius");

            if (player.getLocation().distance(target) < radius) {
                player.getInventory().addItem(ItemUtils.getConfiguredItem("items.clean-money"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        config.getString("messages.success")));

                if (new Random().nextInt(100) < config.getInt("alert-chance")) {
                    String msg = config.getString("messages.alert")
                            .replace("%location%", LocationUtils.format(player.getLocation()));
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "112 " + msg);
                }

            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        config.getString("messages.not-close-enough")));
            }

        }, config.getLong("launder-delay") * 20L);
    }
}
