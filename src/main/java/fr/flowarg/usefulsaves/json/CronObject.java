package fr.flowarg.usefulsaves.json;

/**
 * @author FlowArg - ZeAmateis
 */
public class CronObject
{
    private String cron;

    public CronObject(String cron)
    {
        this.cron = cron;
    }

    public String getCron()
    {
        return cron;
    }

    @Override
    public String toString()
    {
        return "CronObject{" +
                "cron='" + cron + '\'' +
                '}';
    }
}
