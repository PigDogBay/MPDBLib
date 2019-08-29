package com.pigdogbay.lib.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 02/07/2015.
 *
 * Differentiates a data set
 */
public class Differentiator
{
    private int sampleSize = 9;
    private List<DPoint> points;


    public int getSampleSize() {
        return sampleSize;
    }

    /**
     * The differential algorithm takes a sample of data around a point to calculate the best line fit, the slope of the best line
     * is then considered to be that points differential value. Use smaller sample sizes if you have smooth continuous data, uses larger sample sizes
     * if the data is noisy, as larger sample sample sizes will give a better average.
     *
     * @param sampleSize Sample Size
     */
    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public List<DPoint> getPoints() {
        return points;
    }

    public Differentiator(){
        points = new ArrayList<>();
    }

    public Differentiator(List<DPoint> points){
        this.points = points;
    }

    public void Add(double x, double y){
        points.add(new DPoint(x,y));
    }

    /**
     *
     * If there is enough data, a moving sample averaged differentiation is performed.
     * Here a sample of data is taken, a best line fitted, the differential of the mid point is the slope of the best line.
     * The sample statrting index is then moved along by one, for the next differential to be calculated.
     * Since only the midpoint of the sample is calculated, the start and end points of the entire data set will be missed. In these
     * cases a best line is fitted for these values.
     *
     * For data sets smaller than the sample size, a best line is fitted whose slope is assigned to each points differential value.
     *
     * Note that the points are first sorted in ascending order
     * Note that points with the same x value will give have a NaN differential value
     *
     * @return Array of points whose Y-values are the differential values
     */
    public List<DPoint> Differentiate(){
        return points.size()>sampleSize ?SampleAveragedDifferentiation():SmallDataSet();
    }

    /**
     * Uses a moving sample and best line slope to calculate the differentials for each data point
     * @return list of points
     */
    private List<DPoint> SampleAveragedDifferentiation()
    {
        //ensure points are sorted, in order of x value
        DPoint.sortByX(points);
        ArrayList<DPoint> diffPoints = new ArrayList<>();
        int start = sampleSize / 2;
        int end = points.size() - start;

        //Start points: calculate best line fit of the first batch of points
        //and set the differential as the lines slope.
        List<DPoint> selection = points.subList(0, sampleSize);
        BestLineFit bl = new BestLineFit(selection);
        for (int i = 0; i < start; i++)
        {
            diffPoints.add(new DPoint(points.get(i).X, bl.getSlope()));
        }
        //mid points
        for (int i = start; i < end; i++)
        {
            selection = points.subList(i-start,i-start+sampleSize);
            bl = new BestLineFit(selection);
            diffPoints.add(new DPoint(points.get(i).X, bl.getSlope()));
        }
        //end points, use the last calculated best line fit
        for (int i = end; i < points.size(); i++)
        {
            diffPoints.add(new DPoint(points.get(i).X, bl.getSlope()));
        }
        return diffPoints;
    }

    /**
     * Simply fits a best line to calculate the differential
     * @return list of points
     */
    private List<DPoint> SmallDataSet()
    {
        ArrayList<DPoint> diffPoints = new ArrayList<>();
        BestLineFit bl = new BestLineFit(points);
        for (int i = 0; i < points.size(); i++)
        {
            diffPoints.add(new DPoint(points.get(i).X, bl.getSlope()));
        }
        return diffPoints;

    }
}
