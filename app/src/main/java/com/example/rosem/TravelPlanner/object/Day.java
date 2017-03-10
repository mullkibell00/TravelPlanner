package com.example.rosem.TravelPlanner.object;

import com.example.rosem.TravelPlanner.course.Course;

import java.util.ArrayList;

/**
 * Created by rosem on 2017-03-09.
 */

public class Day {

    ArrayList<Course> courseList;
    boolean [] timetable = null;
    int totalTimeUnit=0;
    int availableTimeUnit=0;
    int totalFare = 0;
    int totalDuration = 0;

    public Day()
    {
        courseList = new ArrayList<Course>();
    }

    public Day(int timeU)
    {
        courseList = new ArrayList<Course>();
        availableTimeUnit = timeU;
        timetable = new boolean[availableTimeUnit];
    }

    public Day(ArrayList<Course> list)
    {
        if(list!=null)
        {
            courseList = list;
        }
        else
        {
            courseList = new ArrayList<Course>();
        }

    }

    public int getAvailableTimeUnit() {
        return availableTimeUnit;
    }

    public void setAvailableTimeUnit(int availableTimeUnit) {
        this.availableTimeUnit = availableTimeUnit;
    }

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    public int getTotalTimeUnit() {
        return totalTimeUnit;
    }

    public void setTotalTimeUnit(int totalTimeUnit) {
        this.totalTimeUnit = totalTimeUnit;
    }

    public void addCourse(Course c)
    {
        courseList.add(c);
    }

}
