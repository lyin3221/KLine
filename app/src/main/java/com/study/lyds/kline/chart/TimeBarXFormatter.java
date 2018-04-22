package com.study.lyds.kline.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.study.lyds.kline.model.MarketTimeData;
import com.study.lyds.kline.utils.DataTimeUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/13.
 */

public class TimeBarXFormatter implements IAxisValueFormatter {

    ArrayList<MarketTimeData> xVals = new ArrayList<>();

    public TimeBarXFormatter(ArrayList<MarketTimeData> xVals) {
        this.xVals = xVals;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(xVals.size() == 0 || value > xVals.size()){
            return value+"";
        }else{
            return DataTimeUtil.secToTime(xVals.get((int) value).m_nUpdateTime).substring(0,5);
        }
    }

}
