package com.study.lyds.kline.chart;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.study.lyds.kline.R;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/9/12.
 */
public class BarBottomMarkerView extends MarkerView {

    private TextView markerTv;
    private String num;
    private DecimalFormat mFormat;
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public BarBottomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        mFormat = new DecimalFormat("#0.00");
        markerTv = (TextView) findViewById(R.id.marker_tv);
        markerTv.setTextSize(10);
    }

    public void setData(String num){
        this.num=num;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
//        markerTv.setText(mFormat.format(num));
        markerTv.setText(num);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
