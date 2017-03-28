package com.example.rosem.TravelPlanner.object;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;

/**
 * Created by rosem on 2017-03-27.
 */

public class Schedule{

    private String planName;
    private int numOfDays = 0;
    private Calendar departure;
    private Calendar arrived;
    private boolean isHotelReserved;
    private ArrayList<Calendar> checkInList = null;
    private ArrayList<Calendar> checkOutList = null;
    private ArrayList<Site> hotel = null;
    private ArrayList<Site> recommendHotelList = null;
    private String country;
    private Time tourStart = null;
    private Time tourEnd = null;
    private ArrayList<Site> siteList = new ArrayList<Site>();
    private LinkedList<Site> fixedHourSiteList = new LinkedList<Site>();
    private LinkedList<Site> overHourSiteList = new LinkedList<Site>();
    //for calculation
    private int touringHourInUnit =0;
    private int numOfSites = 0;
    private int numOfHotels = 0;
    private int totalNum = 0;
    private long [][] timeMat = null;
    private int [][] costMat = null;
    private int [][] unitMat= null;
    private int TIMEUNIT = 0;
    private int HOUR_IN_TIMEUNIT=0;
    private boolean [] isSelected= null;

    private Comparator<Site> sortByVisitTimeLate = new Comparator<Site>()
    {

        @Override
        public int compare(Site s1, Site s2) {
            // TODO Auto-generated method stub
            //visitTIme이 늦은 순으로 정렬
            return -(s1.getVisitTime().compareTo(s2.getVisitTime()));
        }

    };

    private Comparator<Site> sortByVisitTimeEarly = new Comparator<Site>()
    {

        @Override
        public int compare(Site s1, Site s2) {
            // TODO Auto-generated method stub
            //visitTime이 빠른 순으로 정렬
            return (s1.getVisitTime().compareTo(s2.getVisitTime()));
        }

    };

    //about schedule class
    public Calendar getArrived() {
        return this.arrived;
    }

    public void setArrived(Calendar arrived) {
        this.arrived = arrived;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Calendar getDeparture() {
        return this.departure;
    }

    public void setDeparture(Calendar departure) {
        this.departure = departure;
    }

    public ArrayList<Site> getHotel() {
        return this.hotel;
    }

    public void setHotel(ArrayList<Site> hotel) {
        this.hotel = hotel;
    }

    public boolean isHotelReserved() {
        return this.isHotelReserved;
    }

    public void setHotelReserved(boolean hotelReserved) {
        this.isHotelReserved = hotelReserved;
    }

    public int getNumOfDays() {
        return this.numOfDays;
    }

    public void setNumOfDays(int numOfDays) {
        this.numOfDays = numOfDays;
    }

    public ArrayList<Site> getSiteList() {
        return this.siteList;
    }

    public void setSiteList(ArrayList<Site> siteList) {
        this.siteList = siteList;
    }

    public String getPlanName() {
        return this.planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Time getTourEnd() {
        return this.tourEnd;
    }

    public void setTourEnd(Time tourEnd) {
        this.tourEnd = tourEnd;
    }

    public Time getTourStart() {
        return this.tourStart;
    }

    public void setTourStart(Time tourStart) {
        this.tourStart = tourStart;
    }

    public void addSite(Site p)
    {
        this.siteList.add(p);
    }

    public int getScheduleSize()
    {
        return this.siteList.size();
    }


    public ArrayList<Calendar> getCheckInList() {
        return this.checkInList;
    }

    public void setCheckInList(ArrayList<Calendar> checkInList) {
        this.checkInList = checkInList;
    }

    public ArrayList<Calendar> getCheckOutList() {
        return this.checkOutList;
    }

    public void setCheckOutList(ArrayList<Calendar> checkOutList) {
        this.checkOutList = checkOutList;
    }

    public ArrayList<Site> getRecommendHotelList()
    {
        return this.recommendHotelList;
    }

    public void setRecommendHotelList(ArrayList<Site> list)
    {
        this.recommendHotelList =list;
    }


    public void getSchedule(int tu, JSONObject json)
    {
        //set datas
        int touringHourInUnit =0;
        numOfSites = siteList.size();
        numOfHotels = hotel.size();
        totalNum = numOfHotels+numOfSites;
        timeMat = new long[totalNum][totalNum];
        costMat = new int[totalNum][totalNum];
        unitMat = new int[totalNum][totalNum];
        TIMEUNIT = tu;
        HOUR_IN_TIMEUNIT = 60/tu;
        isSelected = new boolean[numOfSites];

        try {
            JSONArray response = json.getJSONArray("results");
            //set matrix
            for(int i =0; i<response.length();i++)
            {
                JSONObject obj = response.getJSONObject(i);
                JSONArray rows = obj.getJSONArray("rows");
                for(int j = 0; j<rows.length();j++)
                {
                    JSONObject rowObj = rows.getJSONObject(j);
                    JSONArray elements = rowObj.getJSONArray("elements");
                    JSONObject elementObj = elements.getJSONObject(0);
                    JSONObject duration = elementObj.getJSONObject("duration");

                    timeMat[j][i] = duration.getLong("value");
                    //j = dest
                    // i = start
                }
            }

            for (int dest = 0; dest < totalNum; dest++) {
                for (int start = 0; start < totalNum; start++) {

                    unitMat[dest][start] = timeToUnit(durationToTime(timeMat[dest][start])) + timeToUnit(siteList.get(dest).getSpendTime());
                    costMat[dest][start] = timeToUnit(durationToTime(timeMat[dest][start]));
                    //System.out.print(+unitMat[dest][start]+",");
                }
                //System.out.println();
            }

            LinkedList<LinkedList<Integer>> result = new LinkedList<>();
            for(int i =0; i<numOfDays;i++)
            {
                CalculateDailySchedule dailySchedule =
                        new CalculateDailySchedule(siteList.get(numOfSites),siteList.get(numOfSites),i,isSelected);
                result.add(dailySchedule.getCourse());
            }

            //return result
        } catch (JSONException e) {
            e.printStackTrace();
            //return error
        }
    }

    private class CalculateDailySchedule
    {
        boolean[] isSelected;
        Site startSite;
        Site endSite;
        int totalTimeUnit;
        int today;

        public CalculateDailySchedule(Site start, Site end,int nthDay, boolean[] presentSelected)
        {
            this.startSite = start;
            this.endSite = end;
            totalTimeUnit = touringHourInUnit;
            isSelected = presentSelected;
            today = nthDay;
        }

        private LinkedList<Integer> getCourse()
        {
            LinkedList<Integer> finalCourse = new LinkedList<Integer>();
            LinkedList<Integer> course = new LinkedList<Integer>();
            LinkedList<Integer> selectedFixedVisit = getSelectedFixedVisit();
            //fixed visit hour는 이른 순부터 sort되어있다 가정
            if(selectedFixedVisit.size()>0)
            {
                int fixedVisitIdx = selectedFixedVisit.removeFirst();
                Site fixedVisit = siteList.get(fixedVisitIdx);
                //selection
                isSelected[fixedVisitIdx] = true;
                fixedHourSiteList.remove(fixedVisit);
                //get diff from tourStart to fixed hour
                int upperHalfUnit = timeToUnit(getTimeDiff(fixedVisit.getVisitTime(),tourStart));
                //upperHalfUnit+=timeToUnit(fixedVisit.spendTime);
                //get upper course
                course = getCourseBetween(siteList.indexOf(startSite),fixedVisitIdx, upperHalfUnit,false);
                if(course!=null)
                {
                    finalCourse.addAll(course);
                    course.clear();
                }
                int iterationNum = selectedFixedVisit.size();
                //iterate
                for(int i = 0 ; i<iterationNum;i++)
                {
                    int siteIdx = selectedFixedVisit.removeFirst();
                    Site site = siteList.get(siteIdx);
                    isSelected[siteIdx] = true;
                    fixedHourSiteList.remove(site);
                    int unit = timeToUnit(getTimeDiff(site.visitTime,fixedVisit.visitTime));
                    //unit += timeToUnit(site.spendTime);
                    course = getCourseBetween(fixedVisitIdx,siteIdx,unit,false);
                    if(course!=null)
                    {
                        course.removeFirst();
                        finalCourse.addAll(course);
                        course.clear();
                    }
                    fixedVisit = site;
                    fixedVisitIdx = siteIdx;
                }
                //get diff from fixed hour to tourEnd
                int lowerHalfUnit = timeToUnit(getTimeDiff(tourEnd,fixedVisit.visitTime));
                //lowerHalfUnit -= timeToUnit(fixedVisit.spendTime);
                //get lower course
                course = getCourseBetween(fixedVisitIdx,siteList.indexOf(endSite),lowerHalfUnit,true);
                if(course!=null)
                {
                    //erase duplicate(fixed visit)
                    course.removeFirst();
                    finalCourse.addAll(course);
                    course.clear();
                }
            }
            else
            {
                course = getCourseBetween(siteList.indexOf(startSite),siteList.indexOf(endSite),totalTimeUnit, true );
                if(course!=null)
                {
                    finalCourse.addAll(course);
                    course.clear();
                }

            }

            return finalCourse;
            //return null;
        }

        public LinkedList<Integer> getCourseBetween(int startIdx, int endIdx, int endLimitUnit, boolean isEnd) {
            //int startHotel = siteList.indexOf(startSite);
            //int endHotel = siteList.indexOf(endSite);
            ArrayList<LinkedList<Integer>> course = new ArrayList<LinkedList<Integer>>();
            int [] costs;
            int startHotel = startIdx;
            int endHotel = endIdx;
            //int endSite = -1;
            int limitUnit = endLimitUnit;
            int fixedSiteNum = 2;
            int overTourSiteNum = overTouringHour.size();
            int overTourCost = 0;
            int overTourTimeUnit = 0;
            Time presentTime = new Time(); //presentTime
            LinkedList<Integer> overTourList = new LinkedList<Integer>();

            //initialize
            for (int i = 0; i < isSelected.length; i++)// set course size of
            // unselected
            // sites'number
            {
                if (isSelected[i] == false) {
                    course.add(new LinkedList<Integer>());
                }
            }
            costs = new int[course.size()];

            //is there overTouringhour site?
            if(isEnd && overTourSiteNum>0)
            {
                //check timeUnit of overTouringHour

                //check any of them has date(and that date == today)

                //check num of overTour
                Site overSite = null;
                int overSiteIdx = -1;
                int overTimeUnit = 0;
				/*
				if(overTourSiteNum<=(numOfDay-today))
				{
					//choose site which has smallest visit time
					overSite = overTouringHour.removeLast();
				}
				else
				{
				*/
                //choose site which has largest visit time
                overSite = overTouringHour.removeFirst();
                //	}
                overSiteIdx = siteList.indexOf(overSite);
                isSelected[overSiteIdx] = true;
                fixedSiteNum++;
                overTourList.add(overSiteIdx);

                //get difference between tourEnd and tourOver
                Time diff = getTimeDiff(overSite.visitTime,tourEnd);
                overTimeUnit = timeToUnit(diff)
                        +timeToUnit(siteList.get(overSiteIdx).spendTime);
                limitUnit+=overTimeUnit;
                //set initialTimeUnit of overtour
                //overTourTimeUnit = timeUnitMat[overSiteIdx][endHotel];
                overTourTimeUnit = timeUnitMat[endHotel][overSiteIdx];
                //set initial cost of overTour
                //overTourCost = timeCostMat[overSiteIdx][endHotel];
                overTourCost = timeCostMat[endHotel][overSiteIdx];
                presentTime = overSite.visitTime;

                //	if(overTourSiteNum<=numOfDay)
                //	{
                //erase one per day
                //enlarge totalTourHour
                //add diff to totalTimeUnit(totalTourHour)
                //	endSite = overSiteIdx;
                //	}
                //	else
                //	{
                int overTourMaxTimeUnit = overTimeUnit;
                int presentOverTU = 0;
                int minimumIdx =-1;
                int minimumCost = -1;
                int curIdx = overSiteIdx;

                while(presentOverTU<overTourMaxTimeUnit)
                {
                    //reset minimumIdx
                    minimumIdx = -1;
                    minimumCost = -1;
                    Iterator<Site> it = overTouringHour.iterator();
                    while(it.hasNext())
                    {
                        Site prev = it.next();
                        int prevIdx = siteList.indexOf(prev);
                        if(isOk(curIdx,prevIdx,presentOverTU,overTourMaxTimeUnit,isSelected)
                                &&scheduleConflictCheck(presentTime,prevIdx,curIdx))
                        {
                            if(minimumCost==-1)
                            {
                                //minimumCost = timeCostMat[prevIdx][curIdx];
                                minimumCost = timeCostMat[curIdx][prevIdx];
                                minimumIdx = prevIdx;
                            }
                            //else if (minimumCost > timeCostMat[prevIdx][curIdx])
                            else if (minimumCost > timeCostMat[curIdx][prevIdx])
                            {
                                //minimumCost = timeCostMat[prevIdx][curIdx];
                                minimumCost = timeCostMat[curIdx][prevIdx];
                                minimumIdx = prevIdx;
                            }
                        }
                    }
                    if(minimumIdx!=-1)
                    {
                        Site minimumSite = siteList.get(minimumIdx);
                        //add to overTourList
                        isSelected[minimumIdx] = true;
                        overTourList.add(minimumIdx);
                        fixedSiteNum++;
                        presentTime.sub(unitToTime(minimumCost).add(minimumSite.spendTime));
                        //update OverTU
                        //presentOverTU +=timeUnitMat[minimumIdx][curIdx];
                        presentOverTU +=timeUnitMat[curIdx][minimumIdx];
                        //update cost
                        overTourCost += minimumCost; //timeCostMat[minimumIdx][curIdx];
                        //prepare for next loop
                        curIdx = minimumIdx;
                        //erase from unselected overTour
                        overTouringHour.remove(minimumSite);
                    }
                    else//minimumIdx is -1 (none of Site is Ok)
                    {
                        break;
                    }
                }
                //add time diff
                //endSite = curIdx;
                overTourTimeUnit += presentOverTU;
                //	}//end of else (overTourSiteNum<=numOfDay)

            }//end of else (is there overTourSite)

            // course.add(siteList.indexOf(end));
            for (int start = 0, idx = 0; start < numOfSite; start++) {
                if (!isSelected[start]&&!isInSpecialList(start)) {
                    LinkedList<Integer> list = course.get(idx);
                    int initialTU = 0;
                    if(overTourList.size()<=0)
                    {
                        // add end hotel
                        //initialTU = timeUnitMat[start][endHotel];
                        //initialTU = timeUnitMat[endHotel][start];
                        initialTU = timeUnitMat[endHotel][start];
                        initialTU-=timeToUnit(siteList.get(endHotel).spendTime);
                        initialTU+=timeToUnit(siteList.get(start).spendTime);

                        list.add(endHotel);
                        //costs[idx] = timeCostMat[start][endHotel];
                        costs[idx] = timeCostMat[endHotel][start];
                    }
                    else//should change
                    {
                        list.add(endHotel);

                        //get cost between site and endHotel
                        Iterator<Integer> it = overTourList.iterator();
                        while(it.hasNext())
                        {
                            Integer s = it.next();
                            list.add(s);
                        }

                        //initialTU = overTourTimeUnit+timeUnitMat[start][list.getLast()];//
                        initialTU = overTourTimeUnit+timeUnitMat[list.getLast()][start];//

                        //costs[idx] = overTourCost+timeCostMat[start][list.getLast()];
                        costs[idx] = overTourCost+timeCostMat[list.getLast()][start];
                    }

                    //check initialTU and limitUnit
                    if(initialTU > limitUnit)
                    {
                        list.clear();
                    }
                    else
                    {
                        // add tour cost
                        CourseCostResult result = caculateCourseNN(start, list, initialTU, limitUnit);
                        if(result==null)
                        {
                            return null;
                        }
                        costs[idx] += result.cost;
                        int presentTU = result.timeUnit;

                        // add start hotel
                        int last = list.getLast();
                        //while (presentTU + timeUnitMat[last][startHotel] > limitUnit)
                        while (presentTU + timeUnitMat[startHotel][last] > limitUnit){
                            list.removeLast();
                            //presentTU -= timeUnitMat[course.get(idx).getLast()][last];
                            presentTU -= timeUnitMat[last][course.get(idx).getLast()];
                            last = list.getLast();
                        }
                        //costs[idx] += timeCostMat[startHotel][list.getLast()];
                        costs[idx] += timeCostMat[list.getLast()][startHotel];
                        //updatePresentTimeUnit
                        list.add(startHotel);

                        course.set(idx, list);
                        idx++;
                    }//end of if(initialTU>limitTU)

                }//end of if(isSelected & isInSpecial)
            }//end of for
            double minimumCost = -1;
            int minimumStart = -1;
            // find the smallest cost
            for (int i = 0; i < costs.length; i++) {
                // Log("getCourse::loopStart","cost="+String.valueOf(costs[i])+"\nminimumCost="+String.valueOf(minimumCost));
                double cost = Integer.valueOf(costs[i]).doubleValue()
                        / (Integer.valueOf(course.get(i).size()).doubleValue()-fixedSiteNum);
                // Log("getCourse::loopStart","cost="+String.valueOf(costs[i])+"\nsize="+String.valueOf(course.get(i).size()));
                if(minimumCost==-1)
                {
                    minimumCost = cost;
                    minimumStart = i;
                }
                else if(cost<=0)
                {
                    //skip
                }
                else if (minimumCost > cost) {
                    minimumCost = cost;
                    minimumStart = i;
                }
                else if(minimumCost==cost)
                {
                    //choose course has more visits
                    if(course.get(i).size()>course.get(minimumStart).size())
                    {
                        minimumStart=1;
                    }
                }
                // Log("getCourse::loopEnd","cost="+String.valueOf(costs[i])+"\nminimumCost="+String.valueOf(minimumCost));
            }
            // set boolean
            if(minimumStart!=-1)
            {
                LinkedList<Integer> minimum = course.get(minimumStart);
                for (int i = 1; i < minimum.size() - 1; i++)// exclude hotel(first
                // and last visit)
                {
                    isSelected[minimum.get(i)] = true;
                }

                // for checking costs of each plan
                for (int i = 0; i < costs.length; i++) {
                    System.out.println("Course " + i + " =");
                    LinkedList<Integer> c = course.get(i);
                    Iterator<Integer> it = c.iterator();
                    while (it.hasNext()) {
                        System.out.println("\t" + siteList.get(it.next()).placeName);
                    }
                    System.out.println("cost=" + costs[i]);
                    System.out.println("evaluation cost = "+(Integer.valueOf(costs[i]).doubleValue()
                            / (Integer.valueOf(course.get(i).size()).doubleValue()-fixedSiteNum)));

                }

                Collections.reverse(minimum);

                return minimum;
            }
            return null;

        }

        public CourseCostResult caculateCourseNN(int start, LinkedList<Integer> course, int presentTimeUnit, int maxTimeUnit) {
            int totalCost = 0;
            // int presentTimeUnit = TU;
            //int tempIndex = (start + 1 < numOfSite) ? start + 1 : 0;
            //int presentTimeUnit = initialTimeUnit;
            boolean[] curSelected = isSelected.clone();

            course.add(start);
            curSelected[start] = true;

            for (int i = 0; i < numOfSite; i++) {
                int current = course.getLast();
                int minimumIdx = -1;
                int minimumCost = -1;// num of node

                for (int prev = 0; prev < numOfSite; prev++) {
                    if (current != prev
                            && isOk(current, prev, presentTimeUnit,maxTimeUnit, curSelected)
                            &&!isInSpecialList(prev))
                    {
                        if(minimumCost==-1)
                        {
                            //minimumCost = timeCostMat[prev][current];
                            minimumCost = timeCostMat[current][prev];
                            minimumIdx = prev;
                        }
                        //else if (minimumCost > timeCostMat[prev][current])
                        else if (minimumCost > timeCostMat[current][prev]){
                            //minimumCost = timeCostMat[prev][current];
                            minimumCost = timeCostMat[current][prev];
                            minimumIdx = prev;
                        }
                    }
                }
                if(minimumIdx==-1)
                {
                    //return null;
                }
                else if(!curSelected[minimumIdx]) {
                    course.add(minimumIdx);
                    totalCost += minimumCost;
                    //presentTimeUnit += timeUnitMat[minimumIdx][current];
                    presentTimeUnit += timeUnitMat[current][minimumIdx];
                    curSelected[minimumIdx] = true;
                }

            }
            //initialTimeUnit = new Integer(presentTimeUnit);
            CourseCostResult result = new CourseCostResult(totalCost,presentTimeUnit);
            return result;
        }

        public boolean isOk(int current, int prev, int presentTimeUnit, int maxTU, boolean[] isSelected) {
            //int nextTimeUnit = presentTimeUnit + timeUnitMat[prev][current];
            int nextTimeUnit = presentTimeUnit + timeUnitMat[current][prev];
            int maxTimeUnit = maxTU;

            if (maxTimeUnit < nextTimeUnit) {
                return false;
            }
            if (isSelected[prev]) {
                return false;
            }
            return true;
        }

        public static boolean isInSpecialList(int idx)
        {
            int overTourSize = overTouringHour.size();
            int fixedVisitSize = fixedVisitHour.size();
            if(overTourSize<=0 && fixedVisitSize<=0)
            {
                return false;
            }
            else
            {
                if(overTourSize>0 && overTouringHour.contains(siteList.get(idx)))
                {
                    return true;
                }
                else if(fixedVisitSize>0 &&fixedVisitHour.contains(siteList.get(idx)))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        public static boolean scheduleConflictCheck(Time presentTime, int prev, int current)
        {
            //true = ok false = conflict
            //check inTime
            Site prevSite = siteList.get(prev);
            if(prevSite.visitTime==null)
            {
                return true;
            }
            else if(presentTime.compareTo(prevSite.visitTime)>0)
            {
                //check conflict
                Time endOfPrevVisit = new Time(prevSite.visitTime);
                endOfPrevVisit = endOfPrevVisit.add(prevSite.spendTime);
                if(endOfPrevVisit.compareTo(presentTime)!=-1)
                {
                    return false;
                }

                //int cost = timeCostMat[prev][current];
                int cost = timeCostMat[current][prev];
                Time costTime = unitToTime(cost);
                endOfPrevVisit = endOfPrevVisit.add(costTime);
                //futureTime = futureTime.sub(costTime);
                //futureTime = futureTime.sub(prevSite.spendTime);
                //should change
                if(timeToUnit(presentTime.sub(endOfPrevVisit))>=0)
                {
                    return true;
                }
            }

            return false;
        }

        public static LinkedList<Integer> getSelectedFixedVisit()
        {
            int iteration = fixedVisitHour.size();
            int curIt = 0;
            LinkedList<Integer> selected = new LinkedList<Integer>();

            if(iteration <= 0)
            {
                return selected;
            }
            Site pivot = fixedVisitHour.getFirst();
            int pivotIdx = siteList.indexOf(pivot);
            Iterator<Site> it = fixedVisitHour.listIterator();

            //initialize
            selected.add(pivotIdx);
            while(curIt<iteration)
            {
                Site minimum = null;
                int minimumIdx = -1;
                int minimumCost = -1;

                while(it.hasNext())
                {
                    Site visitSite = it.next();
                    int siteIdx = siteList.indexOf(visitSite);
                    //check same local
                    if(pivot!=visitSite && true)
                    {
                        //check having later visit Time
                        if(pivot.visitTime.compareTo(visitSite.visitTime)<0)
                        {
                            if(scheduleConflictCheck(visitSite.visitTime,pivotIdx,siteIdx))//if do not conflict
                            {
                                int cost = timeCostMat[siteIdx][pivotIdx];
                                //set minimum
                                if(minimumIdx==-1 || minimumCost > cost)
                                {
                                    minimum = visitSite;
                                    minimumIdx = siteIdx;
                                    minimumCost = cost ;
                                }
                            }//end of if (check conflict)
                        }//end of if(check visit Time)
                    }//end of if(check in same local)
                }//end of while(it)//get minimum
                if(minimumIdx!=-1)
                {
                    selected.add(minimumIdx);
                    pivotIdx = minimumIdx;
                    pivot = minimum;
                    curIt++;
                }
                else
                {
                    //if nothing selected -> get out of while
                    break;
                }
            }
            return selected;
        }
        public static class CourseCostResult
        {
            public int cost;
            public int timeUnit;

            public CourseCostResult()
            {
                cost = 0;
                timeUnit=0;
            }

            public CourseCostResult(int c, int t)
            {
                cost = c;
                timeUnit= t;
            }
        }
    }



    private Time durationToTime(long duration) {
        Time t = new Time();
        t.hour = Long.valueOf(duration / 3600).intValue();
        t.min = Long.valueOf((duration - (t.hour * 3600)) / 60).intValue();
        return t;
    }

    private int hourToUnit(int hour) {
        return (hour * 60) / TIMEUNIT;
    }

    private int hourToUnit(Time t) {
        return (t.hour * 60) / TIMEUNIT;
    }

    private int minToUnit(int min) {
        int unit = min / TIMEUNIT;
        if (min % TIMEUNIT != 0) {
            if(unit>=0)
            {
                unit++;
            }
            else
            {
                unit--;
            }
        }
        return unit;
    }

    private int minToUnit(Time t) {
        int unit = t.min / TIMEUNIT;
        if (t.min % TIMEUNIT != 0) {
            if(unit>=0)
            {
                unit++;
            }
            else
            {
                unit--;
            }
        }
        return unit;
    }

    private int timeToUnit(Time t) {
        return (hourToUnit(t.hour)) + minToUnit(t.min);
    }
    private Time unitToTime(int unit)
    {
        Time t = new Time();
        while(unit > HOUR_IN_TIMEUNIT)
        {
            t.hour++;
            unit -= HOUR_IN_TIMEUNIT;
        }
        t.min = unit*TIMEUNIT;
        return t;
    }

    private Time getTimeDiff(Time t1, Time t2)
    {
        return t1.sub(t2);
    }



    class CourseCostResult
    {
        public int cost;
        public int timeUnit;

        public CourseCostResult()
        {
            cost = 0;
            timeUnit=0;
        }

        public CourseCostResult(int c, int t)
        {
            cost = c;
            timeUnit= t;
        }
    }
}
