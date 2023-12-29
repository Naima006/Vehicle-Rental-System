import java.io.Serializable;

class Date implements Serializable
{
    private String month;
    private String day;
    private String year;

    public Date(String month, String day, String year)
    {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public void setMonth(String month)
    {
        this.month = month;
    }

    public String getMonth()
    {
        return month;
    }

    public void setDay(String day)
    {
        this.day = day;
    }

    public String getDay()
    {
        return day;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getYear()
    {
        return year;
    }
    
}