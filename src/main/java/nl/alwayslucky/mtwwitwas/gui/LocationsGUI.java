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
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class LocationsGUI implements Listener {

    FileConfiguration config = Main.getInstance().getConfig();

    public static void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "§c§lMTWars §8> §7Witwas Locaties");

        // Launder Item
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName("§cLaunder Locaties");
        paper.setItemMeta(meta);
        gui.setItem(11, paper); // Center slot

        // NPC Item
        ItemStack gold_ingot = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta2 = gold_ingot.getItemMeta();
        meta2.setDisplayName("§cNPC Locaties");
        gold_ingot.setItemMeta(meta2);
        gui.setItem(15, gold_ingot); // Center slot

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        Inventory inv = event.getInventory();
        String title = inv.getTitle();

        if (title.equals("§c§lMTWars §8> §7Launder Locaties") || title.equals("§c§lMTWars §8> §7Witwas Locaties")) {
            event.setCancelled(true);

            // Check dat click in de GUI zelf is
            if (event.getRawSlot() != event.getSlot()) return;

            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) return;

            int slot = event.getRawSlot();

            if (title.equals("§c§lMTWars §8> §7Launder Locaties")) {
                List<Location> locations = LocationUtils.getAllLaunderLocations();

                if (event.getClick() == ClickType.RIGHT) {
                    if (slot >= 0 && slot < locations.size()) {
                        LocationUtils.removeLaunderLocation(slot);

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.gui.launder-delete").replace("%slot%", Integer.toString(slot + 1))));

                        player.closeInventory();
                        return;
                    }
                } else if (event.getClick() == ClickType.LEFT) {
                    if (slot >= 0 && slot < locations.size()) {
                        Location target = locations.get(slot);
                        player.teleport(target);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.gui.launder-tp").replace("%slot%", Integer.toString(slot + 1))));
                        player.closeInventory();
                        return;
                    }
                }
            } else if (title.equals("§c§lMTWars §8> §7Witwas Locaties")) {
                if (event.getClick() == ClickType.LEFT) {
                    if (clicked.getType() == Material.PAPER) {
                        player.closeInventory();
                        LaunderLocGUI.openMainMenu(player);
                        return;
                    }
                    if (clicked.getType() == Material.GOLD_INGOT) {
                        player.closeInventory();
                        WitwasLocGUI.openMainMenu(player);
                        return;
                    }
                    if (clicked.getType() == Material.SKULL || clicked.getType() == Material.SKULL_ITEM) {
                        List<Location> npcLocations = LocationUtils.getNPCLocations();
                        if (event.getClick() == ClickType.LEFT) {
                            if (slot >= 0 && slot < npcLocations.size()) {
                                Location target = npcLocations.get(slot);
                                player.teleport(target);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.gui.npc-tp").replace("%slot%", Integer.toString(slot + 1))));
                                player.closeInventory();
                                return;
                            }
                        }
                    }
                }else if (event.getClick() == ClickType.RIGHT){
                    if (clicked.getType() == Material.SKULL || clicked.getType() == Material.SKULL_ITEM) {
                        List<Location> npcLocations = LocationUtils.getNPCLocations();
                        if (event.getClick() == ClickType.RIGHT) {
                            if (slot >= 0 && slot < npcLocations.size()) {
                                try {
                                    LocationUtils.removeNPCLocation(slot);
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.gui.npc-delete").replace("%slot%", Integer.toString(slot + 1))));
                                } catch (Exception ex) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.gui.npc-delete").replace("%slot%", Integer.toString(slot + 1))));
                                }
                                player.closeInventory();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
