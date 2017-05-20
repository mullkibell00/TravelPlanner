package com.example.rosem.TravelPlanner.object;

import java.util.Calendar;

/**
 * Created by rosem on 2017-03-27.
 */

public class Time {

    public int hour;
    public int min;

    public Time() {
        hour = 0;
        min = 0;
    }

    public Time(Time t)
    {
        hour = t.hour;
        min = t.min;
    }

    public Time(int h, int m) {
        hour = h;
        min = m;
    }

    public Time(Calendar c)
    {
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
    }
    public Time add(Time t)
    {
        if(t==null)
        {
            Time result = new Time(this.hour, this.min);
            return result;
        }
        else
        {
            Time result = new Time();

            result.min = this.min+t.min;
            if(result.min >= 60)
            {
                result.min = result.min- 60;
                result.hour++;
            }
            result.hour = (result.hour+this.hour + t.hour)%25;
            return result;
        }

    }

    public void setTime(int hour, int min)
    {
        this.hour = hour;
        this.min = min;
    }

    public void setHour(int hour)
    {
        this.hour = hour;
    }
    public void setMin(int min)
    {
        this.min = min;
    }

    public Calendar getCalendar()
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,this.hour);
        c.set(Calendar.MINUTE, this.min);
        return c;
    }

    public Time copyOf()
    {
        Time copy = new Time();
        copy.hour = this.hour;
        copy.min = this.min;
        return copy;
    }

    public Time copyOf(Time t)
    {
        Time copy = new Time();
        copy.hour = t.hour;
        copy.min = t.min;
        return copy;
    }
    public Time sub(Time t)
    {
        if(t==null)
        {
            Time result = new Time(this.hour, this.min);
            return result;
        }
        Time result = new Time();
        result.hour = this.hour - t.hour;
        if(this.hour == t.hour)
        {
            result.min = this.min - t.min;
        }
        else if(this.hour > t.hour)
        {
            if(this.min<t.min)
            {
                result.hour = result.hour -1;
                result.min = (60-t.min+this.min);
            }
            else
            {//this.min >= t.min
                result.min = this.min - t.min;
            }
        }else
        {
            if(this.min>t.min)
            {
                result.hour = result.hour +1;
                result.min = (60-this.min+t.min);
            }
            else
            {//this.min <= t.min
                result.min = t.min - this.min;
            }
            result.min = - result.min;
        }
        return result;

    }
    public String toString()
    {
        String str = Integer.toString(hour)+":"+Integer.toString(min);
        return str;
    }
    public String toStringInText()
    {
        String str = null;
        if(hour==0)
        {
            str = Integer.toString(min)+"min";
        }
        else
        {
            str = Integer.toString(hour)+"hour "+Integer.toString(min)+"min";
        }
        return str;
    }
    public int compareTo(Time t)
    {
        Time sub = this.sub(t);

        if(sub.min==0 && sub.hour ==0)
        {
            return 0; //this == t
        }
        else if(sub.hour>=0 && sub.min >0)
        {
            return 1; //this > t (this - t >0)
        }
        else
        {
            return -1; // this < t
        }
    }

    public static Time getTimeDiff(Time t1, Time t2)
    {
        return t1.sub(t2);
    }
}
