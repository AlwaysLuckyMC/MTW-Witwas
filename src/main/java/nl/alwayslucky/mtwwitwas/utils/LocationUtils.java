package nl.alwayslucky.mtwwitwas.utils;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import nl.alwayslucky.mtwwitwas.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class LocationUtils {

    public static void addNPCLocation(Location newLoc) {
        FileConfiguration config = Main.getInstance().getConfig();
        List<Map<String, Object>> npcLocList = new ArrayList<>();

        List<?> rawList = config.getList("witwassen.npc-locations");
        if (rawList != null) {
            for (Object obj : rawList) {
                if (obj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) obj;
                    npcLocList.add(map);
                }
            }
        }

        Map<String, Object> newMap = new LinkedHashMap<>();
        newMap.put("world", newLoc.getWorld().getName());
        newMap.put("x", newLoc.getX());
        newMap.put("y", newLoc.getY());
        newMap.put("z", newLoc.getZ());

        npcLocList.add(newMap);

        config.set("witwassen.npc-locations", npcLocList);
        Main.getInstance().saveConfig();
    }

    public static void removeNPCLocation(int index) {
        FileConfiguration config = Main.getInstance().getConfig();
        List<Map<?, ?>> npcLocs = config.getMapList("witwassen.npc-locations");

        if (index < 0 || index >= npcLocs.size()) return;

        Map<?, ?> locMap = npcLocs.get(index);

        String worldName = (String) locMap.get("world");
        double x = ((Number) locMap.get("x")).doubleValue();
        double y = ((Number) locMap.get("y")).doubleValue();
        double z = ((Number) locMap.get("z")).doubleValue();

        Location locToRemove = new Location(Bukkit.getWorld(worldName), x, y, z);

        npcLocs.remove(index);
        config.set("witwassen.npc-locations", npcLocs);
        Main.getInstance().saveConfig();

        CitizensAPI.getNPCRegistry().forEach(npc -> {
            if (!npc.isSpawned()) return;

            Location npcLoc = npc.getEntity().getLocation();
            if (npcLoc.getWorld().equals(locToRemove.getWorld()) &&
                    npcLoc.distance(locToRemove) < 1) { // binnen 1 block radius

                npc.despawn();
                CitizensAPI.getNPCRegistry().deregister(npc);
            }
        });
    }

    public static List<Location> getNPCLocations() {
        List<Location> locations = new ArrayList<>();
        FileConfiguration config = Main.getInstance().getConfig();

        List<?> rawList = config.getList("witwassen.npc-locations");
        if (rawList == null) return locations;

        for (Object obj : rawList) {
            if (!(obj instanceof Map)) continue;

            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;

            String worldName = (String) map.get("world");
            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;

            double x = map.containsKey("x") ? Double.parseDouble(map.get("x").toString()) : 0;
            double y = map.containsKey("y") ? Double.parseDouble(map.get("y").toString()) : 0;
            double z = map.containsKey("z") ? Double.parseDouble(map.get("z").toString()) : 0;

            locations.add(new Location(world, x, y, z));
        }

        return locations;
    }

    public static void addKraakNPCLocation(Location newLoc) {
        FileConfiguration config = Main.getInstance().getConfig();
        List<Map<String, Object>> npcLocList = new ArrayList<>();

        List<?> rawList = config.getList("kraken.npc-locations");
        if (rawList != null) {
            for (Object obj : rawList) {
                if (obj instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) obj;
                    npcLocList.add(map);
                }
            }
        }

        Map<String, Object> newMap = new LinkedHashMap<>();
        newMap.put("world", newLoc.getWorld().getName());
        newMap.put("x", newLoc.getX());
        newMap.put("y", newLoc.getY());
        newMap.put("z", newLoc.getZ());

        npcLocList.add(newMap);

        config.set("kraken.npc-locations", npcLocList);
        Main.getInstance().saveConfig();
    }

    public static void removeKraakNPCLocation(int index) {
        FileConfiguration config = Main.getInstance().getConfig();
        List<Map<?, ?>> npcLocs = config.getMapList("kraken.npc-locations");

        if (index < 0 || index >= npcLocs.size()) return;

        Map<?, ?> locMap = npcLocs.get(index);

        String worldName = (String) locMap.get("world");
        double x = ((Number) locMap.get("x")).doubleValue();
        double y = ((Number) locMap.get("y")).doubleValue();
        double z = ((Number) locMap.get("z")).doubleValue();

        Location locToRemove = new Location(Bukkit.getWorld(worldName), x, y, z);

        npcLocs.remove(index);
        config.set("kraken.npc-locations", npcLocs);
        Main.getInstance().saveConfig();

        CitizensAPI.getNPCRegistry().forEach(npc -> {
            if (!npc.isSpawned()) return;

            Location npcLoc = npc.getEntity().getLocation();
            if (npcLoc.getWorld().equals(locToRemove.getWorld()) &&
                    npcLoc.distance(locToRemove) < 1) { // binnen 1 block radius

                npc.despawn();
                CitizensAPI.getNPCRegistry().deregister(npc);
            }
        });
    }

    public static List<Location> getKraakNPCLocations(){
        List<Location> locations = new ArrayList<>();
        FileConfiguration config = Main.getInstance().getConfig();

        List<?> rawList = config.getList("kraken.npc-locations");
        if (rawList == null) return locations;

        for (Object obj : rawList) {
            if (!(obj instanceof Map)) continue;

            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;

            String worldName = (String) map.get("world");
            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;

            double x = map.containsKey("x") ? Double.parseDouble(map.get("x").toString()) : 0;
            double y = map.containsKey("y") ? Double.parseDouble(map.get("y").toString()) : 0;
            double z = map.containsKey("z") ? Double.parseDouble(map.get("z").toString()) : 0;

            locations.add(new Location(world, x, y, z));
        }

        return locations;
    }

    public static Location getRandomKraakLocation() {
        List<String> list = Main.getInstance().getConfig().getStringList("kraken.kraak-locations");
        String[] parts = list.get(new Random().nextInt(list.size())).split(",");
        return new Location(
                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3])
        );
    }

    public static List<Location> getAllKraakLocations() {
        List<Location> locations = new ArrayList<>();
        List<String> list = Main.getInstance().getConfig().getStringList("kraken.kraak-locations");

        for (String locString : list) {
            String[] parts = locString.split(",");
            if (parts.length >= 4) {
                World world = Bukkit.getWorld(parts[0]);
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);

                if (world != null) {
                    locations.add(new Location(world, x, y, z));
                }
            }
        }
        return locations;
    }

    public static void addKraakLocation(Location loc) {
        Main plugin = Main.getInstance();
        List<String> list = plugin.getConfig().getStringList("kraken.kraak-locations");

        String locString = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();

        list.add(locString);

        plugin.getConfig().set("kraken.kraak-locations", list);
        plugin.saveConfig();
    }

    public static boolean removeKraakLocation(int index) {
        Main plugin = Main.getInstance();
        List<String> list = plugin.getConfig().getStringList("kraken.kraak-locations");

        if (index < 0 || index >= list.size()) {
            return false; // Ongeldige index
        }

        list.remove(index);

        plugin.getConfig().set("kraken.kraak-locations", list);
        plugin.saveConfig();

        return true; // Verwijderen gelukt
    }

    //

    public static Location getRandomLaunderLocation() {
        List<String> list = Main.getInstance().getConfig().getStringList("witwassen.launder-locations");
        String[] parts = list.get(new Random().nextInt(list.size())).split(",");
        return new Location(
                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3])
        );
    }

    public static List<Location> getAllLaunderLocations() {
        List<Location> locations = new ArrayList<>();
        List<String> list = Main.getInstance().getConfig().getStringList("witwassen.launder-locations");

        for (String locString : list) {
            String[] parts = locString.split(",");
            if (parts.length >= 4) {
                World world = Bukkit.getWorld(parts[0]);
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);

                if (world != null) {
                    locations.add(new Location(world, x, y, z));
                }
            }
        }
        return locations;
    }

    public static void addLaunderLocation(Location loc) {
        Main plugin = Main.getInstance();
        List<String> list = plugin.getConfig().getStringList("witwassen.launder-locations");

        String locString = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();

        list.add(locString);

        plugin.getConfig().set("witwassen.launder-locations", list);
        plugin.saveConfig();
    }

    public static boolean removeLaunderLocation(int index) {
        Main plugin = Main.getInstance();
        List<String> list = plugin.getConfig().getStringList("witwassen.launder-locations");

        if (index < 0 || index >= list.size()) {
            return false; // Ongeldige index
        }

        list.remove(index);

        plugin.getConfig().set("launder-locations", list);
        plugin.saveConfig();

        return true; // Verwijderen gelukt
    }

    public static String format(Location loc) {
        return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
    }
}
