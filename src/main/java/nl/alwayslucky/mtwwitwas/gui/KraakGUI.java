package nl.alwayslucky.mtwwitwas.gui;

import nl.alwayslucky.mtwwitwas.Main;
import nl.alwayslucky.mtwwitwas.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class KraakGUI implements Listener {

    private static final Map<UUID, List<Byte>> combinationAnswers = new HashMap<>();
    private static final List<Byte> colorDataValues = Arrays.asList((byte)14, (byte)5, (byte)11, (byte)4, (byte)10);
    private static final Set<UUID> completedPlayers = new HashSet<>();

    public static void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§c§lMTWars §8> §cAan het kraken...");

        ItemStack fillItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
        ItemMeta fillMeta = fillItem.getItemMeta();
        fillMeta.setDisplayName("§c");
        fillItem.setItemMeta(fillMeta);

        int[] borderSlots = {0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,49,50,51,52,53};
        for (int slot : borderSlots) {
            gui.setItem(slot, fillItem);
        }

        for (int i = 0; i < 5; i++) {
            gui.setItem(20 + i, createColoredPane(colorDataValues.get(0), "Slot " + (i + 1)));
        }

        ItemStack checkButton = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta checkMeta = checkButton.getItemMeta();
        checkMeta.setDisplayName("§aKlik om te controleren");
        checkButton.setItemMeta(checkMeta);
        gui.setItem(31, checkButton);

        Random random = new Random();
        List<Byte> answer = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            answer.add(colorDataValues.get(random.nextInt(colorDataValues.size())));
        }
        combinationAnswers.put(player.getUniqueId(), answer);
        completedPlayers.remove(player.getUniqueId());

        player.openInventory(gui);

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            if (!completedPlayers.contains(player.getUniqueId())
                    && player.getOpenInventory().getTopInventory().equals(gui)) {
                player.closeInventory();
                player.sendMessage("§eDe tijd is verlopen. Probeer het opnieuw.");
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_FALL, 1f, 1f);
            }
        }, 20L * 15); //15 = seconden
    }

    private static ItemStack createColoredPane(byte data, String name) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§f" + name);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getView().getTitle().contains("Aan het kraken")) return;

        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);
        Inventory inv = event.getInventory();
        int slot = event.getRawSlot();

        // Klik op puzzel-slots (20 t/m 24)
        if (slot >= 20 && slot <= 24) {
            ItemStack current = inv.getItem(slot);
            if (current == null) return;

            byte currentData = current.getData().getData();
            List<Byte> options = colorDataValues;
            int index = options.indexOf(currentData);
            byte next = options.get((index + 1) % options.size());

            inv.setItem(slot, createColoredPane(next, "Slot " + (slot - 19)));

            List<Byte> answer = combinationAnswers.get(player.getUniqueId());
            if (answer != null && answer.size() >= (slot - 20 + 1)) {
                byte expected = answer.get(slot - 20);
                if (next == expected) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1f, 1.5f);
                }
            }
            return;
        }

        if (slot == 31) {
            List<Byte> answer = combinationAnswers.get(player.getUniqueId());
            if (answer == null) return;

            boolean correct = true;
            for (int i = 0; i < 5; i++) {
                ItemStack clicked = inv.getItem(20 + i);
                if (clicked == null || clicked.getData().getData() != answer.get(i)) {
                    correct = false;
                    break;
                }
            }

            if (correct) {
                completedPlayers.add(player.getUniqueId());
                player.sendMessage("§aJe hebt het slot gekraakt!");

                player.getInventory().addItem(ItemUtils.getConfiguredItem("witwassen.items.dirty-money"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);

                player.closeInventory();
            } else {
                player.sendMessage("§cVerkeerde combinatie. Probeer opnieuw.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1f, 0.5f);
            }
        }
    }
}
