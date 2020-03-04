package fr.flowarg.usefulsaves.util;

import fr.flowarg.usefulsaves.Main;
import fr.flowarg.usefulsaves.config.UsefulSavesConfig;
import org.bukkit.Bukkit;

/**
 * @author FlowArg - ZeAmateis
 */
public final class MessageUtils
{
    public static void printMessageForAllPlayers(String message)
    {
        if (UsefulSavesConfig.getPrintMessage().get()) Bukkit.broadcastMessage(message);
    }

    public static void printMessageInConsole(String message, Object... args)
    {
        Main.getPluginLogger().info(String.format(message, args));
    }
}
