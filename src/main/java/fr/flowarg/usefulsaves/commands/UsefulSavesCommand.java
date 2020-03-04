package fr.flowarg.usefulsaves.commands;

import fr.flowarg.spigotconfiguration.ConfigurationManager;
import fr.flowarg.usefulsaves.Main;
import fr.flowarg.usefulsaves.config.UsefulSavesConfig;
import fr.flowarg.usefulsaves.job.SaveJob;
import fr.flowarg.usefulsaves.job.SchedulerManager;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Main command handler for {@link Main}
 *
 * @author FlowArg - ZeAmateis
 */
public class UsefulSavesCommand implements CommandExecutor
{
    private static File fileToDelete;
    private final SchedulerManager manager;

    public UsefulSavesCommand(SchedulerManager manager)
    {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if (command.getName().equalsIgnoreCase("useful-saves"))
        {
            if (sender.isOp())
            {
                if (args.length > 0)
                {
                    // Clear Backup folder
                    if (args[0].equalsIgnoreCase("clear-backups-folder"))
                    {
                        clearBackupFiles(sender);
                        return true;
                    }
                    // Delete a specific backup file
                    else if (args[0].equalsIgnoreCase("delete"))
                    {
                        if (args.length == 2)
                        {
                            fileToDelete = new File(args[1]);
                            sender.sendMessage("usefulsaves.message.delete.confirm_one" + fileToDelete.getName());
                            sender.sendMessage("usefulsaves.message.delete.confirm_two");
                            return true;
                        }
                        else sender.sendMessage("Please use /useful-saves delete <name of backup file> !");
                        return true;
                    }
                    // Confirm delete action
                    else if (args[0].equalsIgnoreCase("confirm"))
                    {
                        if (args.length == 2)
                        {
                            if (args[1].equalsIgnoreCase("true"))
                            {
                                try
                                {
                                    FileUtils.forceDelete(fileToDelete);
                                } catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                                sender.sendMessage("usefulsaves.message.confirm.delete" + fileToDelete.getName());
                                return true;
                            }
                            else sender.sendMessage("Aborted.");
                            return true;
                        }
                        else sender.sendMessage("Please use /useful-saves confirm <true|false> !");
                        return true;
                    }
                    // Scheduled tasks
                    else if (args[0].equalsIgnoreCase("schedule"))
                    {
                        if (args.length == 2)
                        {
                            // Pause scheduled task
                            if(args[1].equalsIgnoreCase("pause"))
                            {
                                pauseTask(sender, this.manager);
                                return true;
                            }
                            // Resume scheduled tasks
                            else if (args[1].equalsIgnoreCase("resume"))
                            {
                                resumeTask(sender, this.manager);
                                return true;
                            }
                            // Stop scheduled task
                            else if (args[1].equalsIgnoreCase("stop"))
                            {
                                unscheduleTask(sender, this.manager);
                                return true;
                            }
                            else return false;
                        }
                        // Cron task manager
                        else if (args[1].equalsIgnoreCase("cron"))
                        {
                            if (args.length < 5)
                            {
                                // Default TimeZone
                                if (args.length == 3) this.manager.scheduleCronSave(sender, args[2], TimeZone.getTimeZone(UsefulSavesConfig.getTimeZone().get()));
                                // Custom TimeZone
                                else this.manager.scheduleCronSave(sender, args[2], TimeZone.getTimeZone(args[3]));
                                return true;
                            }
                            else return false;
                        }
                        // Restart sheduler in case of any crash or shutdowns
                        else if (args[1].equalsIgnoreCase("restart"))
                        {
                            if (!this.manager.isPaused() && this.manager.getScheduler() == null) this.manager.tryCreateScheduler();
                            return true;
                        }
                        else return false;
                    }
                    // Simple unscheduled save
                    else if(args[0].equalsIgnoreCase("save-now"))
                    {
                        processSave(sender, this.manager);
                        return true;
                    }
                    // Display informations
                    else if (args[0].equalsIgnoreCase("info"))
                    {
                        UsefulSavesCommand.listBackup(sender);
                        printInfo(sender, this.manager);
                    }
                    // Config parameters
                    else if (args[0].equalsIgnoreCase("config"))
                    {
                        if (args.length == 3)
                        {
                            // Print message in tchat
                            if (args[1].equalsIgnoreCase("printMessage"))
                            {
                                UsefulSavesConfig.getPrintMessage().set(Boolean.parseBoolean(args[2]));
                                UsefulSavesConfig.getInstance().saveConfig();
                                sender.sendMessage("usefulsaves.message.config.printChatMessage" + args[2]);
                            }
                            // Start task on starting server ?
                            else if (args[1].equalsIgnoreCase("enableTaskOnServerStart"))
                            {
                                UsefulSavesConfig.getEnableTaskOnServerStart().set(Boolean.parseBoolean(args[2]));
                                UsefulSavesConfig.getInstance().saveConfig();
                                sender.sendMessage("usefulsaves.message.config.enableTaskOnServerStart" + args[2]);
                            }
                            // Define the backups folder
                            else if (args[1].equalsIgnoreCase("backupsFolder"))
                            {
                                UsefulSavesConfig.getBackupsFolder().set(args[2]);
                                UsefulSavesConfig.getInstance().saveConfig();
                                sender.sendMessage("usefulsaves.message.config.backupsFolder" + args[2]);
                            }
                            // Define maximum saves in backup folder
                            else if (args[1].equalsIgnoreCase("maximumSavedBackups"))
                            {
                                UsefulSavesConfig.getMaximumSavedBackups().set(Integer.parseInt(args[2]));
                                UsefulSavesConfig.getInstance().saveConfig();
                                final String value = Integer.parseInt(args[2]) == -1 ? "usefulsaves.message.config.maximumSavedBackups.unlimited" : args[2];
                                sender.sendMessage("usefulsaves.message.config.printChatMessage" + value);
                            }
                            // Define TimeZone
                            else if (args[1].equalsIgnoreCase("timeZone"))
                            {
                                UsefulSavesConfig.getTimeZone().set(args[2]);
                                UsefulSavesConfig.getInstance().saveConfig();
                                sender.sendMessage("usefulsaves.message.config.timeZone" + args[2]);
                            }
                            // Save or not if server is empty
                            else if (args[1].equalsIgnoreCase("saveIfServerEmpty"))
                            {
                                UsefulSavesConfig.getSaveIfServerEmpty().set(Boolean.parseBoolean(args[2]));
                                UsefulSavesConfig.getInstance().saveConfig();
                                sender.sendMessage("usefulsaves.message.config.saveIfServerEmpty" + args[2]);
                            }
                            // Define if old backups are deleted if maximum files reach
                            else if (args[1].equalsIgnoreCase("deleteOldOnMaximumReach"))
                            {
                                UsefulSavesConfig.getDeleteOldOnMaximumReach().set(Boolean.parseBoolean(args[2]));
                                UsefulSavesConfig.getInstance().saveConfig();
                                sender.sendMessage("usefulsaves.message.config.deleteOldOnMaximumReach" + args[2]);
                            }
                        }
                        else sender.sendMessage(ChatColor.GOLD + "Not enough arguments : /useful-saves config <key> <value>");
                        return true;
                    }
                    else return false;
                }
                else return false;
            }
            else sender.sendMessage("You have to be op !");
            return true;
        }
        else return false;
    }

    private static void pauseTask(CommandSender sender, @NotNull SchedulerManager manager)
    {
        if (manager.getScheduler() != null)
        {
            try
            {
                if (!manager.isPaused())
                {
                    manager.getScheduler().pauseAll();
                    manager.setPaused(true);
                    manager.setStatus(SchedulerManager.SchedulerStatus.PAUSED);
                    sender.sendMessage("usefulsaves.message.scheduled.pause");
                }
            } catch (SchedulerException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void resumeTask(CommandSender sender, @NotNull SchedulerManager manager)
    {
        if (manager.getScheduler() != null)
        {
            try
            {
                if (manager.isPaused())
                {
                    manager.getScheduler().resumeAll();
                    manager.setPaused(false);
                    manager.setStatus(SchedulerManager.SchedulerStatus.RUNNING);
                    sender.sendMessage("usefulsaves.message.scheduled.resume");
                }
            } catch (SchedulerException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Process simple unscheduled save
     */
    private static void processSave(CommandSender sender, @NotNull SchedulerManager manager)
    {
        SaveJob saveJob = new SaveJob();
        // Check emptyness and process save
        if (!manager.filesToSave().isEmpty())
        {
            saveJob.setup(false, manager.filesToSave());
            if (!manager.getSchedulerStatus().equals(SchedulerManager.SchedulerStatus.RUNNING)) manager.setStatus(SchedulerManager.SchedulerStatus.RUNNING_NO_TASKS);
            saveJob.processSave(sender);
        }
    }

    /**
     * Count how many files are in backup folder
     */
    private static void listBackup(CommandSender sender)
    {
        try (Stream<Path> walk = Files.walk(Paths.get(Main.getInstance().getBackupFolder().getPath())))
        {
            List<String> backupList = walk
                    .filter(Files::isRegularFile)
                    .map(x -> x.getFileName().toString())
                    .collect(Collectors.toList());
            if (!backupList.isEmpty()) sender.sendMessage("usefulsaves.message.backupCount" + backupList.size());
            else sender.sendMessage("usefulsaves.message.backupCount.empty" + backupList.size());
        }
        catch (IOException ignored) {}
    }

    /**
     * Clear backup folder, remove all backups files
     */
    private static void clearBackupFiles(@NotNull CommandSender sender)
    {
        try {
            FileUtils.cleanDirectory(Main.getInstance().getBackupFolder());
            sender.sendMessage("usefulsaves.message.clearBackupFolder");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Stop the {@link SaveJob} task
     */
    public static void unscheduleTask(CommandSender sender, @NotNull SchedulerManager manager)
    {
        if (manager.unscheduleSaveJob()) sender.sendMessage("usefulsaves.message.scheduled.stop");
        else sender.sendMessage("usefulsaves.message.scheduled.stop.notRunning");
    }

    public static void printInfo(CommandSender sender, @NotNull SchedulerManager manager)
    {
        if (manager.getScheduler() != null)
        {
            List<TriggerKey> triggerKeys = manager.getTriggers().stream().filter(Objects::nonNull).map(Trigger::getKey).collect(Collectors.toList());
            if (!triggerKeys.isEmpty())
            {
                try
                {
                    sender.sendMessage("usefulsaves.message.scheduled.runningSince" + manager.getScheduler().getMetaData().getRunningSince() + manager.getSchedulerStatus());
                }
                catch (SchedulerException e)
                {
                    sender.sendMessage(ChatColor.DARK_RED + "An error as occurred when getting the metadata of the scheduler, please report this issue to developers !");
                    e.printStackTrace();
                }
            }
            sender.sendMessage("usefulsaves.message.scheduled.status" + manager.getSchedulerStatus());
        }
    }
}
