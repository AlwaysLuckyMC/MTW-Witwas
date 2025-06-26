package nl.alwayslucky.mtwwitwas.gui;

import nl.alwayslucky.mtwwitwas.Main;
import nl.alwayslucky.mtwwitwas.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class KraakLocationsGUI implements Listener {

    FileConfiguration config = Main.getInstance().getConfig();

    public static void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§c§lMTWars §8> §7Kraken");

        // Launder Item
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName("§cKraak Locaties");
        paper.setItemMeta(meta);
        gui.setItem(11, paper); // Center slot

        // NPC Item
        ItemStack gold_ingot = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta2 = gold_ingot.getItemMeta();
        meta2.setDisplayName("§cKraak NPC Locaties");
        gold_ingot.setItemMeta(meta2);
        gui.setItem(15, gold_ingot); // Center slot

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        String title = event.getView().getTitle();

        if (!title.equals("§c§lMTWars §8> §7Kraak Locaties") && !title.equals("§c§lMTWars §8> §7Kraak NPC Locaties") && !title.equals("§c§lMTWars §8> §7Kraken")) return;

        event.setCancelled(true);

        if (event.getRawSlot() >= inv.getSize()) return;

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        ClickType click = event.getClick();
        int slot = event.getRawSlot();

        if (title.equals("§c§lMTWars §8> §7Kraken")) {
            if (clicked.getType() == Material.PAPER) {
                if (click == ClickType.LEFT) {
                    player.closeInventory();
                    KraakLocGUI.openMainMenu(player);
                    return;
                }
            } else if (clicked.getType() == Material.GOLD_INGOT) {
                if (click == ClickType.LEFT) {
                    player.closeInventory();
                    KraakNPCLocGUI.openMainMenu(player);
                    return;
                }
            }
        }

        // === KRAAK LOCATIE TABS ===
        if (title.equals("§c§lMTWars §8> §7Kraak Locaties")) {
            List<Location> npcLocations = LocationUtils.getAllKraakLocations();

            if (slot < 0 || slot >= npcLocations.size()) {
                player.sendMessage(ChatColor.RED + "Ongeldige locatie geselecteerd.");
                return;
            }

            Location npcLoc = npcLocations.get(slot);

            if (npcLoc == null || npcLoc.getWorld() == null) {
                player.sendMessage(ChatColor.RED + "Deze locatie is ongeldig of de wereld bestaat niet.");
                return;
            }

            if (click == ClickType.LEFT) {
                player.teleport(npcLoc);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        config.getString("messages.gui.npc-tp").replace("%slot%", Integer.toString(slot + 1))));
                player.closeInventory();

            } else if (click == ClickType.RIGHT) {
                LocationUtils.removeKraakLocation(slot);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        config.getString("messages.gui.npc-delete").replace("%slot%", Integer.toString(slot + 1))));
                player.closeInventory();
            }
        }

        // === NPC LOCATIES ===
        else if (title.equals("§c§lMTWars §8> §7Kraak NPC Locaties")) {
            List<Location> npcLocations = LocationUtils.getKraakNPCLocations();

            if (slot < 0 || slot >= npcLocations.size()) {
                player.sendMessage(ChatColor.RED + "Ongeldige locatie geselecteerd.");
                return;
            }

            Location npcLoc = npcLocations.get(slot);

            if (npcLoc == null || npcLoc.getWorld() == null) {
                player.sendMessage(ChatColor.RED + "Deze locatie is ongeldig of de wereld bestaat niet.");
                return;
            }

            if (click == ClickType.LEFT) {
                player.teleport(npcLoc);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        config.getString("messages.gui.npc-tp").replace("%slot%", Integer.toString(slot + 1))));
                player.closeInventory();

            } else if (click == ClickType.RIGHT) {
                LocationUtils.removeKraakNPCLocation(slot);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        config.getString("messages.gui.npc-delete").replace("%slot%", Integer.toString(slot + 1))));
                player.closeInventory();
            }
        }

    }

}
