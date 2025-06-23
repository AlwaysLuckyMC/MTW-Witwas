package nl.alwayslucky.mtwwitwas.utils;

import nl.alwayslucky.mtwwitwas.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class ItemUtils {

    public static ItemStack getConfiguredItem(String path) {
        FileConfiguration config = Main.getInstance().getConfig();

        Material material = Material.valueOf(config.getString(path + ".material"));
        String name = config.getString(path + ".name");
        List<String> lore = config.getStringList(path + ".lore");

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (name != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (lore != null) {
            meta.setLore(lore.stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .collect(Collectors.toList()));
        }
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isSimilar(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;
        if (a.getType() != b.getType()) return false;

        ItemMeta metaA = a.getItemMeta();
        ItemMeta metaB = b.getItemMeta();

        if (metaA == null || metaB == null) return false;

        if (!ChatColor.stripColor(metaA.getDisplayName())
                .equalsIgnoreCase(ChatColor.stripColor(metaB.getDisplayName()))) {
            return false;
        }

        return true;
    }
}
