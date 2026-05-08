using System;
using System.Globalization;

public class DateTimeUtil
{
    private const string DateFormat = "yyyy-MM-dd";
    private const string TimeFormat = "HH:mm";

    public static string FormatDate(DateTime date)
    {
        return date.ToString(DateFormat);
    }

    public static DateTime ParseDate(string dateStr)
    {
        return DateTime.ParseExact(dateStr, DateFormat, CultureInfo.InvariantCulture);
    }

    public static string FormatTime(TimeSpan time)
    {
        return DateTime.Today.Add(time).ToString(TimeFormat);
    }

    public static TimeSpan ParseTime(string timeStr)
    {
        return DateTime.ParseExact(timeStr, TimeFormat, CultureInfo.InvariantCulture).TimeOfDay;
    }

    public static string Today()
    {
        return DateTime.Today.ToString(DateFormat);
    }
}
