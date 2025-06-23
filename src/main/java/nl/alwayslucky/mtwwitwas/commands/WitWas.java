package nl.alwayslucky.mtwwitwas.commands;

import nl.alwayslucky.mtwwitwas.utils.ItemUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WitWas implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Alleen spelers kunnen dit commando gebruiken.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2 || !args[0].equalsIgnoreCase("give")) {
            player.sendMessage("Gebruik: /witwas give <dirty|clean>");
            return true;
        }

        String type = args[1].toLowerCase();
        if (type.equals("dirty")) {
            player.getInventory().addItem(ItemUtils.getConfiguredItem("items.dirty-money"));
        } else if (type.equals("clean")) {
            player.getInventory().addItem(ItemUtils.getConfiguredItem("items.clean-money"));
        } else {
            player.sendMessage("Gebruik: /witwas give <dirty|clean>");
        }

        return true;
    }
}
