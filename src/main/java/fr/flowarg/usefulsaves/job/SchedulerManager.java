package fr.flowarg.usefulsaves.job;

import fr.flowarg.usefulsaves.Main;
import fr.flowarg.usefulsaves.config.UsefulSavesConfig;
import fr.flowarg.usefulsaves.json.CronObject;
import fr.flowarg.usefulsaves.json.ScheduleObject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link SchedulerFactory} & {@link Scheduler} Manager
 *
 * @author ZeAmateis
 */
public class SchedulerManager
{
    private final SchedulerFactory factory = new StdSchedulerFactory();
    private final JobDetail saveJob = JobBuilder.newJob(SaveJob.class).withIdentity("SaveTask", "default").build();
    private Scheduler scheduler;

    private CronTrigger cronTrigger;
    private Set<Trigger> triggers = new HashSet<>();
    // Execute save just after typped scheduled task
    private Trigger nowTrigger;

    private SchedulerStatus schedulerStatus = SchedulerStatus.UNKNOWN;
    private boolean pause;

    public SchedulerManager()
    {
        //If server crash or stopped, shutdown properly the executer service
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            this.updateTaskInConfig();
            this.unscheduleSaveJob();
            try {
                scheduler.shutdown();
                setStatus(SchedulerStatus.SHUTDOWN);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }));

    }

    private void updateTaskInConfig()
    {
        if (UsefulSavesConfig.getEnableTaskOnServerStart().get())
        {
            UsefulSavesConfig.getCronTaskObject().set(Main.getInstance().getGson().toJson(Main.getInstance().taskObject.get()));
            UsefulSavesConfig.getInstance().saveConfig();
        }
    }

    public void tryCreateScheduler()
    {
        try
        {
            scheduler = factory.getScheduler();
            Main.getPluginLogger().info("Created Scheduler");
            scheduler.start();
            Main.getPluginLogger().info("Scheduler Started");
            setStatus(SchedulerStatus.RUNNING_NO_TASKS);
        }
        catch (SchedulerException ex)
        {
            setStatus(SchedulerStatus.UNKNOWN);
            ex.printStackTrace();
        }
    }

    /**
     * Remove Job from scheduler
     */
    public boolean unscheduleSaveJob()
    {
        if (this.scheduler != null)
        {
            List<TriggerKey> triggerKeys = this.triggers.stream().filter(Objects::nonNull).map(Trigger::getKey).collect(Collectors.toList());
            if (!triggerKeys.isEmpty())
            {
                try
                {
                    this.scheduler.unscheduleJobs(triggerKeys);
                    this.setStatus(SchedulerManager.SchedulerStatus.NOT_RUNNING);
                    return true;
                } catch (SchedulerException e)
                {
                    return false;
                }
            }
        }
        return false;
    }

    public void scheduleCronSave(CommandSender commandSender, String cronTask, TimeZone timeZone)
    {
        try {
            Main.getInstance().taskObject.set(new ScheduleObject(new CronObject(cronTask), timeZone));
            updateTaskInConfig();

            if (!UsefulSavesConfig.getTimeZone().equals(timeZone.getID()))
            {
                UsefulSavesConfig.getTimeZone().set(timeZone.getID());
                UsefulSavesConfig.getInstance().saveConfig();
            }

            cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("SaveCronTask", "default")
                    .withSchedule(
                            CronScheduleBuilder
                                    .cronSchedule(cronTask)
                                    .inTimeZone(TimeZone.getTimeZone(Main.getInstance().taskObject.get().getTimeZone()))
                    )
                    .startNow()
                    .build();

            nowTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("SaveNowTask", "default")
                    .startNow()
                    .build();

            triggers.add(nowTrigger);
            triggers.add(cronTrigger);

            saveJob.getJobDataMap().put("commandSender", commandSender);
            // But pretty useless to define now for external user
            saveJob.getJobDataMap().put("deleteExisting", false);

            // Check emptyness
            if (!filesToSave().isEmpty())
            {
                saveJob.getJobDataMap().putIfAbsent("sourceWhitelist", filesToSave());
            }

            scheduler.scheduleJob(saveJob, triggers, true);
            setStatus(SchedulerStatus.RUNNING);
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Create a list of paths usable without duplicated entries
     */
    public List<Path> filesToSave() {
        //Read whiteList config, filter non-existing files/folder and process
        List<Path> whitelistPath = UsefulSavesConfig.getSavedFileWhitelist().get().stream().map(Paths::get).filter(path -> path.toFile().exists()).collect(Collectors.toList());

        Bukkit.getWorlds().forEach(world ->
        {
            if (whitelistPath.stream().noneMatch(o -> o.equals(world.getWorldFolder().toPath()))) whitelistPath.add(world.getWorldFolder().toPath());
            if (UsefulSavesConfig.getSavedFileWhitelist().get().stream().noneMatch(o -> o.equals(world.getWorldFolder().toPath().toString())))
            {
                UsefulSavesConfig.getSavedFileWhitelist().get().add(world.getWorldFolder().toPath().toString());
                UsefulSavesConfig.getInstance().saveConfig();
            }
        });

        //Check duplicated entry, collect and process
        return whitelistPath.stream().distinct().filter(path -> path.toFile().exists()).collect(Collectors.toList());
    }

    /**
     * Getter for Scheduler
     */
    public Scheduler getScheduler()
    {
        return scheduler;
    }

    public Set<Trigger> getTriggers()
    {
        return triggers;
    }

    public boolean isPaused()
    {
        return pause;
    }

    public void setPaused(boolean pause)
    {
        this.pause = pause;
    }

    public void setStatus(SchedulerStatus statusIn)
    {
        this.schedulerStatus = statusIn;
    }

    public SchedulerStatus getSchedulerStatus()
    {
        return schedulerStatus;
    }

    public enum SchedulerStatus
    {
        NOT_RUNNING,
        RUNNING_NO_TASKS,
        RUNNING,
        PAUSED,
        MAXIMUM_BACKUP_REACH,
        SHUTDOWN,
        UNKNOWN
    }
}
