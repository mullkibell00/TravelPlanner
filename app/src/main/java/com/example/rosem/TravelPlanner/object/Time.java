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
        Time result = new Time();
        result.hour = this.hour + t.hour;
        result.min = this.min+t.min;
        if(result.min > 60)
        {
            result.min = result.min- 60;
            result.hour++;
        }
        return result;
    }

    public Calendar getCalendar()
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,this.hour);
        c.set(Calendar.MINUTE, this.min);
        return c;
    }
    public Time sub(Time t)
    {
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

    public int compareTo(Time t)
    {
        Time sub = this.sub(t);

        if(sub.min >0 || sub.hour>0)
        {
            return 1; //this > t (this - t >0)
        }
        else if(sub.min==0 && sub.hour ==0)
        {
            return 0; //this == t
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
