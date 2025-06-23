package nl.alwayslucky.mtwwitwas.utils;

import nl.alwayslucky.mtwwitwas.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Random;

public class LocationUtils {

    public static Location getNPCLocation() {
        FileConfiguration config = Main.getInstance().getConfig();
        return new Location(
                Bukkit.getWorld(config.getString("npc-location.world")),
                config.getDouble("npc-location.x"),
                config.getDouble("npc-location.y"),
                config.getDouble("npc-location.z")
        );
    }

    public static Location getRandomLaunderLocation() {
        List<String> list = Main.getInstance().getConfig().getStringList("launder-locations");
        String[] parts = list.get(new Random().nextInt(list.size())).split(",");
        return new Location(
                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3])
        );
    }

    public static String format(Location loc) {
        return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
    }
}
