package nl.alwayslucky.mtwwitwas.events;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import nl.alwayslucky.mtwwitwas.Main;
import nl.alwayslucky.mtwwitwas.utils.LocationUtils;
import nl.alwayslucky.mtwwitwas.managers.LaunderManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

public class NPCInteractListener implements Listener {

    @EventHandler
    public void onNPCClick(PlayerInteractEntityEvent event){
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        Entity clicked = event.getRightClicked();

        if (!CitizensAPI.getNPCRegistry().isNPC(clicked)) return;

        NPC npc = CitizensAPI.getNPCRegistry().getNPC(clicked);

        String configName = Main.getInstance().getConfig().getString("npc.name");

        if (configName == null) {
            player.sendMessage("§c[DEBUG] Config npc.name is niet ingesteld.");
            return;
        }

        if (npc.getName().equalsIgnoreCase(configName)) {
            List<Location> npcLocaties = LocationUtils.getNPCLocations();

            for (Location npcLoc : npcLocaties) {
                if (clicked.getLocation().getWorld().equals(npcLoc.getWorld()) &&
                        clicked.getLocation().distance(npcLoc) < 2) {
                    LaunderManager.startLaundering(player);
                    break;
                }
            }
        }
    }
}
