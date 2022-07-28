package fun.fifu.simplebloodvolumedisplay;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleBloodVolumeDisplay extends JavaPlugin implements Listener {
    public static SimpleBloodVolumeDisplay plugin;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EntityListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("感谢使用本插件");
    }

}
