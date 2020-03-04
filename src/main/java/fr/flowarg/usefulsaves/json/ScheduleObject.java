package fr.flowarg.usefulsaves.json;

import java.util.TimeZone;

/**
 * @author ZeAmateis
 */
public class ScheduleObject
{
    private CronObject cronObject;
    private String timeZone;

    public ScheduleObject(CronObject cronObject, TimeZone timeZone)
    {
        this.cronObject = cronObject;
        this.timeZone = timeZone.getID();
    }

    public String getTimeZone()
    {
        return this.timeZone;
    }

    public CronObject getCronObject()
    {
        return this.cronObject;
    }

    @Override
    public String toString()
    {
        return "ScheduleObject{" +
                "cronObject=" + cronObject +
                ", timeZone='" + timeZone + '\'' +
                '}';
    }
}
