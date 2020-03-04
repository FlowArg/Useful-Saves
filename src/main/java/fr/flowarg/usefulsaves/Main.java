package fr.flowarg.usefulsaves;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.flowarg.usefulsaves.commands.CronHelpCommand;
import fr.flowarg.usefulsaves.commands.UsefulSavesCommand;
import fr.flowarg.usefulsaves.config.UsefulSavesConfig;
import fr.flowarg.usefulsaves.job.SchedulerManager;
import fr.flowarg.usefulsaves.json.ScheduleObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.quartz.SchedulerException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author FlowArg - ZeAmateis
 */
public class Main extends JavaPlugin
{
    private static final Logger LOGGER = LogManager.getLogger("Useful-Saves");
    private static Main instance;

    public final AtomicReference<ScheduleObject> taskObject = new AtomicReference<>();

    private File backupFolder;
    private SchedulerManager schedulerManager;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onLoad()
    {
        instance = this;
        if (!this.getDataFolder().exists()) this.getDataFolder().mkdirs();
        this.schedulerManager = new SchedulerManager();
    }

    @Override
    public void onEnable()
    {
        // Register Commands
        this.getCommand("cron").setExecutor(new CronHelpCommand());
        this.getCommand("useful-saves").setExecutor(new UsefulSavesCommand(this.schedulerManager));

        // Create backup folder if doesn't exist
        this.saveDefaultConfig();
        this.backupFolder = new File(UsefulSavesConfig.getBackupsFolder().get());
        if (!Files.exists(backupFolder.toPath()))
        {
            try
            {
                Files.createDirectories(backupFolder.toPath());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        try {
            // Create and start scheduler if is null
            if (this.schedulerManager.getScheduler() == null)
            {
                this.schedulerManager.tryCreateScheduler();
            }
            // Restart scheduler if is only shutdown
            else if (this.schedulerManager.getScheduler() != null)
            {
                if (this.schedulerManager.getScheduler().isShutdown()) this.schedulerManager.tryCreateScheduler();
            }

            // Auto-reload task from file
            if (!UsefulSavesConfig.getCronTaskObject().get().isEmpty() && UsefulSavesConfig.getEnableTaskOnServerStart().get())
            {
                try
                {
                    getPluginLogger().info("Loading startup tasks from file...");
                    this.taskObject.set(instance.getGson().fromJson(UsefulSavesConfig.getCronTaskObject().get(), ScheduleObject.class));

                    if (this.taskObject.get() != null)
                        // Check if cron object is not null
                        if (this.taskObject.get().getCronObject() != null)
                        {
                            // Loading from file
                            this.schedulerManager.scheduleCronSave(null, this.taskObject.get().getCronObject().getCron(), TimeZone.getTimeZone(this.taskObject.get().getTimeZone()));
                        } else getPluginLogger().info("cronObject null or empty, ignoring starting task... NOTE : You can specify a custom cronObject in the configuration file !");
                    else getPluginLogger().info("Json Task empty, ignoring starting task...");
                } finally
                {
                    getPluginLogger().info("Loaded tasks from file finished.");
                }
            }
        } catch (SchedulerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable()
    {
        // Shutdown scheduler on stopping server
        if (this.schedulerManager.getScheduler() != null)
        {
            try
            {
                this.schedulerManager.getScheduler().shutdown();
            } catch (SchedulerException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static Main getInstance()
    {
        return instance;
    }

    public static Logger getPluginLogger()
    {
        return LOGGER;
    }

    public SchedulerManager getSchedulerManager()
    {
        return this.schedulerManager;
    }

    public File getBackupFolder()
    {
        return this.backupFolder;
    }

    public Gson getGson()
    {
        return this.gson;
    }
}
