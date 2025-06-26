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

public class KraakNPCLocGUI implements Listener {

    public static void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§c§lMTWars §8> §7Kraak NPC Locaties");

        int locAmount = 0;

        for (Location location : LocationUtils.getKraakNPCLocations()) {
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3); // Skull in 1.12.2
            ItemMeta meta = skull.getItemMeta();

            int newLocAmount = locAmount + 1;

            meta.setDisplayName("§bNPC Locatie - " + newLocAmount);
            meta.setLore(Arrays.asList(
                    "§7NPC Coords: §8" + location.getBlockX() + " §8- " + location.getBlockY() + " §8- " + location.getBlockZ(),
                    "§3",
                    "§a§oLeft-click om te teleporteren.",
                    "§c§oRight-click om te verwijderen."
            ));
            skull.setItemMeta(meta);
            gui.setItem(locAmount, skull);
            locAmount++;
        }

        player.openInventory(gui);
    }
}
