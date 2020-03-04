package fr.flowarg.usefulsaves.commands;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import fr.flowarg.usefulsaves.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Simple helping command to deal with crontab
 *
 * @author FlowArg - ZeAmateis
 */
public class CronHelpCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(command.getName().equalsIgnoreCase("cron"))
        {
            if(args.length > 0 && args.length < 3)
            {
                if (args.length == 1) getDescription(sender, args[0], "en-US");
                else getDescription(sender, args[0], args[1]);
                return true;
            }
            sender.sendMessage(ChatColor.GOLD + "Bad command usage.");
            return false;
        }
        return false;
    }

    private static void getDescription(@NotNull CommandSender sender, String expression, String locale)
    {
        System.out.println(expression + " " + locale);
        expression = expression.replaceAll("\\s", "+").replaceAll("\"", "");
        try
        {
            String fromattedURL = String.format("https://cronexpressiondescriptor.azurewebsites.net/api/descriptor/?expression=%s&locale=%s", expression, locale);
            URL url = new URL(fromattedURL);
            JsonObject descriptor = Main.getInstance().getGson().fromJson(new InputStreamReader(url.openStream()), new TypeToken<JsonObject>()
            {
            }.getType());
            sender.sendMessage("usefulsaves.message.cronhelp.result" + " " +descriptor.get("description").getAsString());

        } catch (IOException ex)
        {
            sender.sendMessage("usefulsaves.message.cronhelp.unableToReachURL");
        }
    }
}
