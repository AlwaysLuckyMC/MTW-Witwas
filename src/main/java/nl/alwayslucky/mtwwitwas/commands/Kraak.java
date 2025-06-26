package nl.alwayslucky.mtwwitwas.commands;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import nl.alwayslucky.mtwwitwas.Main;
import nl.alwayslucky.mtwwitwas.gui.KraakGUI;
import nl.alwayslucky.mtwwitwas.gui.KraakLocationsGUI;
import nl.alwayslucky.mtwwitwas.gui.LocationsGUI;
import nl.alwayslucky.mtwwitwas.utils.ItemUtils;
import nl.alwayslucky.mtwwitwas.utils.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Kraak implements CommandExecutor {

    FileConfiguration config = Main.getInstance().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cAlleen spelers kunnen dit commando gebruiken.");
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage("§cGebruik: /kraak <locations | add-location | spawn-npc>");
            return true;
        }

        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("locations")) {
            KraakLocationsGUI.openMainMenu(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.gui.open")));
        }else if (args[0].equalsIgnoreCase("add-location")) {
            LocationUtils.addKraakLocation(player.getLocation());

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.gui.add-kraak")));
        }else if (args[0].equalsIgnoreCase("spawn-npc")) {
            Location newNPCloc = player.getLocation().clone();

            newNPCloc.setYaw(90);
            newNPCloc.setPitch(0f);

            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Kraker");

            npc.spawn(newNPCloc);
            npc.data().setPersistent(NPC.Metadata.PLAYER_SKIN_UUID, "AlwaysLucky_");
            npc.data().setPersistent(NPC.Metadata.PLAYER_SKIN_USE_LATEST, true);

            npc.faceLocation(newNPCloc);

            LocationUtils.addKraakNPCLocation(newNPCloc);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.kraak.spawn-npc")));
        }else if (args[0].equalsIgnoreCase("kraak")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.kraak.start")));
            KraakGUI.openMainMenu(player);
        }else{
            player.sendMessage("§cGebruik: /witwas <give | locations | add-launder | spawn-npc>");
        }

        return true;
    }
}
