package com.study.lyds.kline.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/29.
 */

public class TimeLineXFormatter implements IAxisValueFormatter {
    ArrayList<String> xVals = new ArrayList<>();

    public TimeLineXFormatter(ArrayList<String> xVals) {
        this.xVals = xVals;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(xVals.size() == 0 || value >= xVals.size()){
            return value+"";
        }else
        return xVals.get((int) (value));
    }

}
