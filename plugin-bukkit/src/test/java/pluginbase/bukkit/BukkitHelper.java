package pluginbase.bukkit;

import pluginbase.plugin.PluginInfo;
import pluginbase.plugin.ServerInterface;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BukkitHelper {

    public static ServerInterface getServerInterface(@NotNull final Server server) {
        return new BukkitServerInterface(server);
    }

    public static PluginInfo getPluginInfo(@NotNull final Plugin plugin) {
        return new BukkitPluginInfo(plugin);
    }
}
