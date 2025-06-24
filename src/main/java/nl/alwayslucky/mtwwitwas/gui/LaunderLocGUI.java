package nl.alwayslucky.mtwwitwas.gui;

import nl.alwayslucky.mtwwitwas.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class LaunderLocGUI implements Listener {

    public static void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§c§lMTWars §8> §7Launder Locaties");

        int locAmount = 0;

        for (Location location : LocationUtils.getAllLaunderLocations()) {
            // Launder Item
            ItemStack paper = new ItemStack(Material.PAPER);
            ItemMeta meta = paper.getItemMeta();

            int newLocAmount = locAmount + 1;

            meta.setDisplayName("§cLaunder Locatie - " + newLocAmount);
            meta.setLore(Arrays.asList(
                    "§7Launder Coords: §8" + location.getBlockX() + " §8- " + location.getBlockY() + " §8- " + location.getBlockZ(),
                    "§3",
                    "§a§oLeft-click om te teleporteren.",
                    "§c§oRight-click om te verwijderen."
            ));
            paper.setItemMeta(meta);
            gui.setItem(locAmount, paper);
            locAmount++;
        }

        player.openInventory(gui);
    }
}
