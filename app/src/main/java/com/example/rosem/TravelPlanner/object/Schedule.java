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
    private LinkedList<Site> fixedDateSiteList = new LinkedList<Site>();
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

    public LinkedList<Site> getFixedDateSiteList() {
        return fixedDateSiteList;
    }

    public void setFixedDateSiteList(LinkedList<Site> fixedDateSiteList) {
        this.fixedDateSiteList = fixedDateSiteList;
    }

    public LinkedList<Site> getFixedHourSiteList() {
        return fixedHourSiteList;
    }

    public void setFixedHourSiteList(LinkedList<Site> fixedHourSiteList) {
        this.fixedHourSiteList = fixedHourSiteList;
    }

    public LinkedList<Site> getOverHourSiteList() {
        return overHourSiteList;
    }

    public void setOverHourSiteList(LinkedList<Site> overHourSiteList) {
        this.overHourSiteList = overHourSiteList;
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


    public LinkedList<LinkedList<Integer>> getSchedule(int tu, JSONObject json)
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

            Collections.sort(overHourSiteList,sortByVisitTimeLate);
            Collections.sort(fixedHourSiteList,sortByVisitTimeEarly);
            //Collections.sort(fixedDateSiteList, sortByVisitDateEarly);

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
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            //return error
            return null;
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
                    int unit = timeToUnit(getTimeDiff(site.getVisitTime(),fixedVisit.getVisitTime()));

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
                int lowerHalfUnit = timeToUnit(getTimeDiff(tourEnd,fixedVisit.getVisitTime()));
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
        }

        public LinkedList<Integer> getCourseBetween(int startIdx, int endIdx, int endLimitUnit, boolean isEnd) {
            ArrayList<LinkedList<Integer>> course = new ArrayList<LinkedList<Integer>>();
            int [] costs;
            int startHotel = startIdx;
            int endHotel = endIdx;
            int limitUnit = endLimitUnit;
            int fixedSiteNum = 2;
            int overTourSiteNum = overHourSiteList.size();
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
                //check num of overTour
                Site overSite = null;
                int overSiteIdx = -1;
                int overTimeUnit = 0;
                //choose site which has largest visit time
                overSite = overHourSiteList.removeFirst();
                overSiteIdx = siteList.indexOf(overSite);
                isSelected[overSiteIdx] = true;
                fixedSiteNum++;
                overTourList.add(overSiteIdx);

                //get difference between tourEnd and tourOver
                Time diff = getTimeDiff(overSite.getVisitTime(),tourEnd);
                overTimeUnit = timeToUnit(diff)
                        +timeToUnit(siteList.get(overSiteIdx).getSpendTime());
                limitUnit+=overTimeUnit;
                //set initialTimeUnit of overtour
                overTourTimeUnit = unitMat[endHotel][overSiteIdx];
                //set initial cost of overTour
                overTourCost = costMat[endHotel][overSiteIdx];
                presentTime = overSite.getVisitTime();

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
                    Iterator<Site> it = overHourSiteList.iterator();
                    while(it.hasNext())
                    {
                        Site prev = it.next();
                        int prevIdx = siteList.indexOf(prev);
                        if(isOk(curIdx,prevIdx,presentOverTU,overTourMaxTimeUnit,isSelected)
                                &&scheduleConflictCheck(presentTime,prevIdx,curIdx))
                        {
                            if(minimumCost==-1)
                            {
                                //minimumCost = costMat[prevIdx][curIdx];
                                minimumCost = costMat[curIdx][prevIdx];
                                minimumIdx = prevIdx;
                            }
                            //else if (minimumCost > costMat[prevIdx][curIdx])
                            else if (minimumCost > costMat[curIdx][prevIdx])
                            {
                                //minimumCost = costMat[prevIdx][curIdx];
                                minimumCost = costMat[curIdx][prevIdx];
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
                        presentTime.sub(unitToTime(minimumCost).add(minimumSite.getSpendTime()));
                        //update OverTU
                        presentOverTU +=unitMat[curIdx][minimumIdx];
                        //update cost
                        overTourCost += minimumCost; //costMat[minimumIdx][curIdx];
                        //prepare for next loop
                        curIdx = minimumIdx;
                        //erase from unselected overTour
                        overHourSiteList.remove(minimumSite);
                    }
                    else//minimumIdx is -1 (none of Site is Ok)
                    {
                        break;
                    }
                }
                overTourTimeUnit += presentOverTU;
                //	}//end of else (overTourSiteNum<=numOfDay)

            }//end of else (is there overTourSite)
            for (int start = 0, idx = 0; start < numOfSites; start++) {
                if (!isSelected[start]&&!isInSpecialList(start)) {
                    LinkedList<Integer> list = course.get(idx);
                    int initialTU = 0;
                    if(overTourList.size()<=0)
                    {
                        // add end hotel
                        initialTU = unitMat[endHotel][start];
                        initialTU-=timeToUnit(siteList.get(endHotel).getSpendTime());
                        initialTU+=timeToUnit(siteList.get(start).getSpendTime());

                        list.add(endHotel);
                        costs[idx] = costMat[endHotel][start];
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
                        initialTU = overTourTimeUnit+unitMat[list.getLast()][start];//
                        costs[idx] = overTourCost+costMat[list.getLast()][start];
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
                        //while (presentTU + unitMat[last][startHotel] > limitUnit)
                        while (presentTU + unitMat[startHotel][last] > limitUnit){
                            list.removeLast();
                            presentTU -= unitMat[last][course.get(idx).getLast()];
                            last = list.getLast();
                        }
                        costs[idx] += costMat[list.getLast()][startHotel];
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
                double cost = Integer.valueOf(costs[i]).doubleValue()
                        / (Integer.valueOf(course.get(i).size()).doubleValue()-fixedSiteNum);
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
                Collections.reverse(minimum);
                return minimum;
            }
            return null;
        }

        public CourseCostResult caculateCourseNN(int start, LinkedList<Integer> course, int presentTimeUnit, int maxTimeUnit) {
            int totalCost = 0;
            boolean[] curSelected = isSelected.clone();

            course.add(start);
            curSelected[start] = true;

            for (int i = 0; i < numOfSites; i++) {
                int current = course.getLast();
                int minimumIdx = -1;
                int minimumCost = -1;// num of node

                for (int prev = 0; prev < numOfSites; prev++) {
                    if (current != prev
                            && isOk(current, prev, presentTimeUnit,maxTimeUnit, curSelected)
                            &&!isInSpecialList(prev))
                    {
                        if(minimumCost==-1)
                        {
                            //minimumCost = costMat[prev][current];
                            minimumCost = costMat[current][prev];
                            minimumIdx = prev;
                        }
                        //else if (minimumCost > costMat[prev][current])
                        else if (minimumCost > costMat[current][prev]){
                            //minimumCost = costMat[prev][current];
                            minimumCost = costMat[current][prev];
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
                    //presentTimeUnit += unitMat[minimumIdx][current];
                    presentTimeUnit += unitMat[current][minimumIdx];
                    curSelected[minimumIdx] = true;
                }

            }
            CourseCostResult result = new CourseCostResult(totalCost,presentTimeUnit);
            return result;
        }

        public boolean isOk(int current, int prev, int presentTimeUnit, int maxTU, boolean[] isSelected) {
            int nextTimeUnit = presentTimeUnit + unitMat[current][prev];
            int maxTimeUnit = maxTU;

            if (maxTimeUnit < nextTimeUnit) {
                return false;
            }
            if (isSelected[prev]) {
                return false;
            }
            return true;
        }

        public boolean isInSpecialList(int idx)
        {
            int overTourSize = overHourSiteList.size();
            int fixedVisitSize = fixedHourSiteList.size();
            if(overTourSize<=0 && fixedVisitSize<=0)
            {
                return false;
            }
            else
            {
                if(overTourSize>0 && overHourSiteList.contains(siteList.get(idx)))
                {
                    return true;
                }
                else if(fixedVisitSize>0 &&fixedHourSiteList.contains(siteList.get(idx)))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        public boolean scheduleConflictCheck(Time presentTime, int prev, int current)
        {
            //true = ok false = conflict
            //check inTime
            Site prevSite = siteList.get(prev);
            if(prevSite.getVisitTime()==null)
            {
                return true;
            }
            else if(presentTime.compareTo(prevSite.getVisitTime())>0)
            {
                //check conflict
                Time endOfPrevVisit = new Time(prevSite.getVisitTime());
                endOfPrevVisit = endOfPrevVisit.add(prevSite.getSpendTime());
                if(endOfPrevVisit.compareTo(presentTime)!=-1)
                {
                    return false;
                }

                int cost = costMat[current][prev];
                Time costTime = unitToTime(cost);
                endOfPrevVisit = endOfPrevVisit.add(costTime);
                //should change
                if(timeToUnit(presentTime.sub(endOfPrevVisit))>=0)
                {
                    return true;
                }
            }

            return false;
        }

        public LinkedList<Integer> getSelectedFixedVisit()
        {
            int iteration = fixedHourSiteList.size();
            int curIt = 0;
            LinkedList<Integer> selected = new LinkedList<Integer>();

            if(iteration <= 0)
            {
                return selected;
            }
            Site pivot = fixedHourSiteList.getFirst();
            int pivotIdx = siteList.indexOf(pivot);
            Iterator<Site> it = fixedHourSiteList.listIterator();

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
                        if(pivot.getVisitTime().compareTo(visitSite.getVisitTime())<0)
                        {
                            if(scheduleConflictCheck(visitSite.getVisitTime(),pivotIdx,siteIdx))//if do not conflict
                            {
                                int cost = costMat[siteIdx][pivotIdx];
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
        public class CourseCostResult
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
