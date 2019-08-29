package com.pigdogbay.lib.math;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Co-ordinate information and whether the point represents a peak or trough
 *
 * Created by Mark on 02/07/2015.
 */
public class StationaryPoints
{
    /**
     * Co-ordinate information and whether the point represents a peak or trough
     */
    public static class TurningPoint{
        private DPoint Point;
        private boolean IsPeak;
        private int Index;

        public TurningPoint(DPoint point, boolean isPeak, int index)
        {
            this.Point = point;
            this.IsPeak = isPeak;
            this.Index = index;
        }

        /**
         * @return X and Y value of the data point
         */
        public DPoint getPoint() {
            return Point;
        }

        /**
         * @return True - turning point is a peak, False - turning point is a trough
         */
        public boolean isPeak() {
            return IsPeak;
        }

        /**
         * @return index of the point in the data set
         */
        public int getIndex() {
            return Index;
        }
    }
    enum SPSS
    {
        Nothing,
        StartOfPeak,
        EndOfPeak,
        StartOfTrough,
        EndOfTrough,
        Plateau,
    }

    private double threshold = 0;
    private List<DPoint> diffPoints;
    private List<DPoint> points;
    private List<TurningPoint> turningPoints;

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public StationaryPoints(List<DPoint> points){
        this.points = points;
        this.diffPoints = new Differentiator(points).Differentiate();
    }

    /**
     * Calculates the peaks and troughs of the data set.
     *
     * @return list of  the peaks and troughs of the data set
     */
    public List<TurningPoint> CalculateTurningPoints()
    {
        turningPoints = new ArrayList<>();
        smoothDiffPoints();
        findTurningPoints();
        return turningPoints;

    }
    private void smoothDiffPoints() {
        if (threshold == 0d) {
            //no smoothing required
            return;
        }
        double yMin = DPoint.getMinY(diffPoints).Y;
        double range = DPoint.getMaxY(diffPoints).Y - yMin;
        for (int i = 0; i < diffPoints.size(); i++)
        {
            double d = Math.abs(diffPoints.get(i).Y / range);
            if (d < threshold)
            {
                diffPoints.set(i, new DPoint(diffPoints.get(i).X, 0));
            }
        }
    }
    private void findTurningPoints()
    {
        int startIndex = 0;
        SPSS state = SPSS.Nothing;
        for (int i = 0; i < diffPoints.size(); i++)
        {
            double y = diffPoints.get(i).Y;
            if (y == 0d)
            {
                continue;
            }
            switch (state)
            {
                case Nothing:
                    state = SPSS.StartOfPeak;
                    if (y < 0)
                    {
                        state = SPSS.StartOfTrough;
                    }
                    startIndex = i;
                    break;
                case StartOfPeak:
                    if (y < 0)
                    {
                        state = SPSS.EndOfPeak;
                    }
                    break;
                case EndOfPeak:
                    scanForPeak(startIndex, i);
                    state = SPSS.Nothing;
                    break;
                case StartOfTrough:
                    if (y > 0)
                    {
                        state = SPSS.EndOfTrough;
                    }
                    break;
                case EndOfTrough:
                    scanForTrough(startIndex, i);
                    state = SPSS.Nothing;
                    break;
                default:
                    break;
            }
        }
    }
    private void scanForPeak(int startIndex, int endIndex)
    {
        DPoint pt = points.get(startIndex);
        int index = startIndex;
        for (int i = startIndex; i <= endIndex; i++)
        {
            if (pt.Y < points.get(i).Y)
            {
                pt = points.get(i);
                index = i;
            }
        }
        turningPoints.add(new TurningPoint(pt,true,index));
    }
    private void scanForTrough(int startIndex, int endIndex)
    {
        DPoint pt = points.get(startIndex);
        int index = startIndex;
        for (int i = startIndex; i <= endIndex; i++)
        {
            if (pt.Y > points.get(i).Y)
            {
                pt = points.get(i);
                index = i;
            }
        }
        turningPoints.add(new TurningPoint(pt, false, index));

    }

}
