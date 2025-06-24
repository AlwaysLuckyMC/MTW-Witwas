package nl.alwayslucky.mtwwitwas.commands;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import nl.alwayslucky.mtwwitwas.Main;
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

public class WitWas implements CommandExecutor {

    FileConfiguration config = Main.getInstance().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cAlleen spelers kunnen dit commando gebruiken.");
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage("§cGebruik: /witwas <give | locations | add-launder | spawn-npc>");
            return true;
        }

        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("give")) {
            if(args.length == 2){
                String type = args[1].toLowerCase();
                if (type.equals("dirty")) {
                    player.getInventory().addItem(ItemUtils.getConfiguredItem("items.dirty-money"));
                } else if (type.equals("clean")) {
                    player.getInventory().addItem(ItemUtils.getConfiguredItem("items.clean-money"));
                } else {
                    player.sendMessage("§cGebruik: /witwas give <dirty | clean>");
                }
            }else {
                player.sendMessage("§cGebruik: /witwas give <dirty | clean>");
            }
        } else if (args[0].equalsIgnoreCase("locations")) {
            LocationsGUI.openMainMenu(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.gui.open")));
        }else if (args[0].equalsIgnoreCase("add-launder")) {
            LocationUtils.addLaunderLocation(player.getLocation());

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.gui.add-launder")));
        }else if (args[0].equalsIgnoreCase("spawn-npc")) {
            Location newNPCloc = player.getLocation().clone();

            newNPCloc.setYaw(90);
            newNPCloc.setPitch(0f);

            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Witwasser");

            npc.spawn(newNPCloc);
            npc.data().setPersistent(NPC.Metadata.PLAYER_SKIN_UUID, "AlwaysLucky_");
            npc.data().setPersistent(NPC.Metadata.PLAYER_SKIN_USE_LATEST, true);

            npc.faceLocation(newNPCloc);

            LocationUtils.addNPCLocation(newNPCloc);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("messages.gui.spawn-npc")));
        }else{
            player.sendMessage("§cGebruik: /witwas <give | locations | add-launder | spawn-npc>");
        }

        return true;
    }
}
