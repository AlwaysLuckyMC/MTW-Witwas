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

public class NPCInteractListener implements Listener {

    @EventHandler
    public void onNPCClick(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        Entity clicked = event.getRightClicked();

        if (!CitizensAPI.getNPCRegistry().isNPC(clicked)) return;

        NPC npc = CitizensAPI.getNPCRegistry().getNPC(clicked);

        // Haal veilig de config via singleton
        String configName = Main.getInstance().getConfig().getString("npc.name");

        if (configName == null) {
            player.sendMessage("§c[DEBUG] Config npc.name is niet ingesteld.");
            return;
        }

        if (npc.getName().equalsIgnoreCase(configName)) {
            Location npcLoc = LocationUtils.getNPCLocation();
            if (clicked.getLocation().distance(npcLoc) < 2) {
                LaunderManager.startLaundering(player);
            }
        }
    }
}
