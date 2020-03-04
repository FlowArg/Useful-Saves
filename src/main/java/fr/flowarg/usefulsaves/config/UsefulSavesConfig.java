package fr.flowarg.usefulsaves.config;

import fr.flowarg.spigotconfiguration.ConfigurationManager;
import fr.flowarg.spigotconfiguration.SpigotConfigEntry;
import fr.flowarg.usefulsaves.Main;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author FlowArg - ZeAmateis
 * Totally rewrited and adapted to spigot by FlowArg.
 * @see SpigotConfigEntry
 */
public class UsefulSavesConfig implements ConfigurationManager
{
    private static final FileConfiguration CONFIGURATION = Main.getInstance().getConfig();

    private static final SpigotConfigEntry.StringEntry timeZone = new SpigotConfigEntry.StringEntry("timeZone", CONFIGURATION);
    private static final SpigotConfigEntry.StringEntry backupsFolder = new SpigotConfigEntry.StringEntry("backupsFolder", CONFIGURATION);
    private static final SpigotConfigEntry.StringEntry cronTaskObject = new SpigotConfigEntry.StringEntry("cronTaskObject", CONFIGURATION);

    private static final SpigotConfigEntry.BooleanEntry saveIfServerEmpty = new SpigotConfigEntry.BooleanEntry("saveIfServerEmpty", CONFIGURATION);
    private static final SpigotConfigEntry.BooleanEntry printMessage = new SpigotConfigEntry.BooleanEntry("printMessage", CONFIGURATION);
    private static final SpigotConfigEntry.BooleanEntry enableTaskOnServerStart = new SpigotConfigEntry.BooleanEntry("enableTaskOnServerStart", CONFIGURATION);
    private static final SpigotConfigEntry.BooleanEntry deleteOldOnMaximumReach = new SpigotConfigEntry.BooleanEntry("deleteOldOnMaximumReach", CONFIGURATION);

    private static final SpigotConfigEntry.IntegerEntry maximumSavedBackups = new SpigotConfigEntry.IntegerEntry("maximumSavedBackups", CONFIGURATION);

    private static final SpigotConfigEntry.ListEntry.StringListEntry savedFileWhitelist = new SpigotConfigEntry.ListEntry.StringListEntry("savedFileWhitelist", CONFIGURATION);

    @Override
    public void saveConfig()
    {
        Main.getInstance().saveConfig();
    }

    // No loading configuration task because we work with the default config of Bukkit (config.yml).
    @Override
    public void loadConfig() {}

    @Override
    public FileConfiguration getConfig()
    {
        return CONFIGURATION;
    }

    public static SpigotConfigEntry.StringEntry getTimeZone()
    {
        return timeZone;
    }

    public static SpigotConfigEntry.StringEntry getBackupsFolder()
    {
        return backupsFolder;
    }

    public static SpigotConfigEntry.StringEntry getCronTaskObject()
    {
        return cronTaskObject;
    }

    public static SpigotConfigEntry.BooleanEntry getSaveIfServerEmpty()
    {
        return saveIfServerEmpty;
    }

    public static SpigotConfigEntry.BooleanEntry getPrintMessage()
    {
        return printMessage;
    }

    public static SpigotConfigEntry.BooleanEntry getEnableTaskOnServerStart()
    {
        return enableTaskOnServerStart;
    }

    public static SpigotConfigEntry.BooleanEntry getDeleteOldOnMaximumReach()
    {
        return deleteOldOnMaximumReach;
    }

    public static SpigotConfigEntry.IntegerEntry getMaximumSavedBackups()
    {
        return maximumSavedBackups;
    }

    public static SpigotConfigEntry.ListEntry.StringListEntry getSavedFileWhitelist()
    {
        return savedFileWhitelist;
    }

    public static UsefulSavesConfig getInstance()
    {
        return new UsefulSavesConfig();
    }
}
