package nl.alwayslucky.mtwwitwas;

import nl.alwayslucky.mtwwitwas.commands.WitWas;
import nl.alwayslucky.mtwwitwas.events.NPCInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new NPCInteractListener(), this);
        getCommand("witwas").setExecutor(new WitWas());
    }

    public static Main getInstance(){
        return instance;
    }
}
