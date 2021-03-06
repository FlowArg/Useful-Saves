<p align="center">
  <img width="256" height="256" src="https://i.imgur.com/Xz7NQwI.png">
</p>

<p align="center">
	<a href="https://dev.bukkit.org/projects/useful-saves">
  		<img width="126" height="20" src="http://cf.way2muchnoise.eu/title/364598.svg">
	</a>
</p>

## Scheduler backing up to never lose anything !
### Useful Saves, a simple, commands only, plugin to create scheduled backup of your world or extras files
#### Thanks to [JibayMcs](https://github.com/JibayMcs) for the concept !

 
## Features
 
- Scheduled saves
- Instant backup
- Delete backup
- Cleaning backup folder
- Crontab helper command
 
## Config example

```yaml
# Print Useful Saves messages in chat ?
printMessage: true

# Define a TimeZone if server clock mismatch players clock
# Automatically generated by default
timeZone: Europe/Paris

# Define a json formatted cron task to save/load
# If "enableTaskOnServerStart" is enabled task will be loaded.
cronTaskObject: '{}'

# Define maximum created backups
# "-1" = unlimited saves
# Range: > -1
maximumSavedBackups: -1

# Enable the previous saved scheduled task on server start ?
# Ensure "cronTask" is not empty or null
enableTaskOnServerStart: true

# Define a backup folder
backupsFolder: ./backups

# Process save task if no player connected ?
saveIfServerEmpty: true

# Define a list of files or folder to save on saving process
# Use absolute path !
savedFileWhitelist: 
    -./logs
    -./config

# Defined to delete oldest backups if maximum saves are reach
# Used if "maximumSavedBackups" are defined
deleteOldOnMaximumReach: false
```
___

## Commands
Useful Saves’s command prefix are always /useful-saves

### - Deletions commands

`/useful-saves clear-backups-folder`
Clear save files from backup folder

`/useful-saves delete`
Delete a specified save file from backup folder

`/useful-saves confirm [yes/no]`
Confirm previous deletion

___

### - Scheduling commands
 
`/useful-saves schedule stop/pause/resume`
To stop, pause or resume the current running task

`/useful-saves schedule cron [<expression>]`
To schedule backup task, based on crontab expression

#### Optional parameters
 
`/useful-saves schedule cron [<expression>] [<TimeZone>]`
To schedule backup task, based on crontab expression, but by defining a time zone if the server clock/locale differ from user contry

___

## - Utilitary commands

`/useful-saves schedule restart`
Restart the scheduler system in case of any crash or shutdown

`/useful-saves save-now [flush]`
Launch an instant backup

`/useful-saves info`
Informations of the actual state of the scheduler

___

## - Config commands

`/useful-saves config backupsFolder`
Define a folder to store saves

`/useful-saves config deleteOldOnMaximumReach`
Delete oldest save if maximum backups reached

`/useful-saves config enableTaskOnServerStart`
Starting scheduled task on server start

`/useful-saves config maximumSavedBackups`
Set the number of backups the server keeps

`/useful-saves config printMessage`
Display or not infos to all players

`/useful-saves config saveIfServerEmpty`
Enable saves if server has no players connected

`/useful-saves config timeZone`
Define a time zone if the server clock/locale differ from user contry

# Crontab Helper command
 
Useful Saves are packaged with an human interpreter for cron expression

Thanks to [**cronexpressiondescriptor.azurewebsites.net**](https://cronexpressiondescriptor.azurewebsites.net/)

`/cron [<expression>]`
Get a human readable version of the cron expression

`/cron [<expression>] [<locale>]`
Get a human readable version of the cron expression, but in user language

**Example:** `/cron "*** * * ? * ***" fr-FR`

**Output:** `Toutes les secondes`


# Useful links

If you are not familiar with crontab let’s check [**Cron Expression Generator for Quartz**](https://www.freeformatter.com/cron-expression-generator-quartz.html) !

# Technologies used

- [**Quartz-Scheduler**](http://www.quartz-scheduler.org/)
- [**cronexpressiondescriptor.azurewebsites.net**](https://cronexpressiondescriptor.azurewebsites.net/)
