package nl.alwayslucky.mtwwitwas;

import nl.alwayslucky.mtwwitwas.commands.Kraak;
import nl.alwayslucky.mtwwitwas.commands.WitWas;
import nl.alwayslucky.mtwwitwas.events.NPCInteractListener;
import nl.alwayslucky.mtwwitwas.gui.KraakGUI;
import nl.alwayslucky.mtwwitwas.gui.KraakLocationsGUI;
import nl.alwayslucky.mtwwitwas.gui.LaunderLocGUI;
import nl.alwayslucky.mtwwitwas.gui.LocationsGUI;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new LocationsGUI(), this);
        getServer().getPluginManager().registerEvents(new KraakLocationsGUI(), this);
        getServer().getPluginManager().registerEvents(new LaunderLocGUI(), this);

        getServer().getPluginManager().registerEvents(new NPCInteractListener(), this);
        getServer().getPluginManager().registerEvents(new KraakGUI(), this);

        getCommand("witwas").setExecutor(new WitWas());
        getCommand("kraak").setExecutor(new Kraak());
    }

    public static Main getInstance(){
        return instance;
    }
}
