package gui.model;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class CargoModel
{
    private String type;
    private int location;
    private String insDate;
    private String duration;

    public CargoModel(String type, int location, Date insDate, Duration duration)
    {
        this.type = type.replace("Impl", "");
        this.location = location;

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        this.insDate = (insDate == null) ? "Not Inspect!" : formatter.format(insDate);

        long totalSeconds = duration.getSeconds();
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        this.duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public String getType()
    {
        return type;
    }

    public int getLocation()
    {
        return location;
    }

    public String getInsDate()
    {
        return insDate;
    }

    public String getDuration()
    {
        return duration;
    }
}
