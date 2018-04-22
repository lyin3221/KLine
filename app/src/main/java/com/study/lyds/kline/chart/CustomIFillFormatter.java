package com.study.lyds.kline.chart;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

/**
 * Created by Administrator on 2017/9/13.
 */

public class CustomIFillFormatter implements IFillFormatter {
    @Override
    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
        return -10E30f;
    }
}
