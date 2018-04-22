package com.study.lyds.kline.ui.fragment;

import android.graphics.Color;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.study.lyds.kline.R;
import com.study.lyds.kline.base.BaseFragment;
import com.study.lyds.kline.chart.BarBottomMarkerView;
import com.study.lyds.kline.chart.CoupleChartGestureListener;
import com.study.lyds.kline.chart.KLeftMarkerView;
import com.study.lyds.kline.chart.LyLineChart;
import com.study.lyds.kline.chart.LyTimeBarChart;
import com.study.lyds.kline.chart.LyXAxis;
import com.study.lyds.kline.chart.TimeRightMarkerView;
import com.study.lyds.kline.data.DBHelper;
import com.study.lyds.kline.data.KTimeData;
import com.study.lyds.kline.model.CirclePositionTime;
import com.study.lyds.kline.model.MarketTimeData;
import com.study.lyds.kline.utils.CommonUtil;
import com.study.lyds.kline.utils.DataTimeUtil;
import com.study.lyds.kline.utils.NumberUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ly on 2016/9/5.
 */
public class ChartTimeFragment extends BaseFragment {
    @BindView(R.id.line_chart)
    LyLineChart lineChart;
    @BindView(R.id.bar_chart)
    LyTimeBarChart barChart;
    @BindView(R.id.circle_frame_time)
    FrameLayout cirCleView;

    private LineDataSet d1, d2;
    LyXAxis xAxisLine;

    YAxis axisRightLine;
    YAxis axisLeftLine;
    BarDataSet barDataSet;

    LyXAxis xAxisBar;
    YAxis axisLeftBar;
    YAxis axisRightBar;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_time;
    }

    @Override
    protected void initBase(View view) {
        EventBus.getDefault().register(this);
        playHeartbeatAnimation(cirCleView.findViewById(R.id.anim_view));
        initChart();
        setDataValues(DBHelper.getDbHelper(mAct).getkTimeData());
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initChart() {
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(Color.LTGRAY);
        lineChart.setBorderWidth(0.7f);
        lineChart.setNoDataText("");
        Legend lineChartLegend = lineChart.getLegend();
        lineChartLegend.setEnabled(false);
        lineChart.setDescription(null);

        barChart.setScaleEnabled(false);
        barChart.setDrawBorders(true);
        barChart.setBorderColor(Color.LTGRAY);
        barChart.setBorderWidth(0.7f);
        barChart.setDescription(null);
        barChart.setNoDataText("");

        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);
        xAxisLine = (LyXAxis) lineChart.getXAxis();
        xAxisLine.setDrawLabels(false);
        xAxisLine.setDrawGridLines(false);
//        xAxisLine.setLabelsToSkip(150);
        xAxisLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxisLine.setLabelCount(5,true);
        xAxisLine.setAvoidFirstLastClipping(true);
        xAxisLine.setAxisMinimum(0);
//        xAxisLine.setXLabels(stringSparseArray);

        axisLeftLine = lineChart.getAxisLeft();
        axisLeftLine.setLabelCount(2, true);
        axisLeftLine.setDrawLabels(true);
        axisLeftLine.setDrawGridLines(true);
        axisLeftLine.setValueLineInside(true);
        axisLeftLine.setDrawTopBottomGridLine(false);
        axisLeftLine.setDrawAxisLine(false);
        axisLeftLine.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeftLine.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return NumberUtils.keepPrecisionR(value, 2);
            }
        });
        axisRightLine = lineChart.getAxisRight();
        axisRightLine.setLabelCount(5, true);
        axisRightLine.setDrawLabels(true);
        axisRightLine.setDrawTopBottomGridLine(false);
        axisRightLine.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                return mFormat.format(value);
            }
        });

        axisRightLine.setStartAtZero(false);
        axisRightLine.setDrawGridLines(true);
        axisRightLine.enableGridDashedLine(CommonUtil.dip2px(mAct,4), CommonUtil.dip2px(mAct,3),0);
        axisRightLine.setDrawAxisLine(false);
        axisRightLine.setValueLineInside(true);
        axisRightLine.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //背景线
        xAxisLine.setGridColor(getResources().getColor(R.color.minute_grayLine));
        xAxisLine.setAxisLineColor(Color.TRANSPARENT);
        xAxisLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftLine.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisRightLine.setAxisLineColor(getResources().getColor(R.color.minute_grayLine));
        axisRightLine.setTextColor(getResources().getColor(R.color.minute_zhoutv));

        xAxisBar = (LyXAxis) barChart.getXAxis();
        xAxisBar.setDrawLabels(true);
        xAxisBar.setDrawGridLines(false);
        xAxisBar.setDrawAxisLine(false);
        xAxisBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setAvoidFirstLastClipping(true);

        xAxisBar.setGridColor(getResources().getColor(R.color.minute_grayLine));
        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftBar.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeftBar.setDrawLabels(true);
        axisLeftBar.setLabelCount(2,true);
        axisLeftBar.setSpaceTop(5);
        axisLeftBar.setValueLineInside(true);
        axisLeftBar.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value == 0){
                    return "";
                }else{
                    return (int)value+"";
                }
            }
        });

        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);

        lineChart.setViewPortOffsets(0, CommonUtil.dip2px(mAct,3),0, CommonUtil.dip2px(mAct,15));
        barChart.setViewPortOffsets(0, CommonUtil.dip2px(mAct,5),0, CommonUtil.dip2px(mAct,15));
        CoupleChartGestureListener coupleChartGestureListener = new CoupleChartGestureListener(lineChart, new Chart[]{barChart});
        lineChart.setOnChartGestureListener(coupleChartGestureListener);
        initPopupWindow(coupleChartGestureListener);
    }

    private void initPopupWindow(CoupleChartGestureListener coupleChartGestureListener){
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                lineChart.highlightValue(h);
                barChart.highlightValue(new Highlight(h.getX(),h.getDataSetIndex(),-1));
            }

            @Override
            public void onNothingSelected() {
                barChart.highlightValues(null);
            }
        });
    }

    private void setShowLabels(boolean isShow){
        lineChart.getAxisLeft().setDrawLabels(isShow);
        lineChart.getAxisRight().setDrawLabels(isShow);
        barChart.getAxisLeft().setDrawLabels(isShow);
        barChart.getXAxis().setDrawLabels(isShow);
    }

    public void setDataValues(KTimeData mData){
        if(mData.getDatas().size() == 0){
            cirCleView.setVisibility(View.GONE);
            return;
        }else{
            cirCleView.setVisibility(View.VISIBLE);
        }
        setShowLabels(true);
        setMarkerView(mData);
        setBottomMarkerView(mData);

        axisLeftLine.setAxisMinValue(mData.getMin());
        axisLeftLine.setAxisMaxValue(mData.getMax());

        if(Float.isNaN(mData.getPercentMax()) || mData.getPercentMax() == 0){
            axisLeftBar.setAxisMaxValue(0);
            axisRightLine.setAxisMinValue(-0.01f);
            axisRightLine.setAxisMaxValue(0.01f);
        }else{
            axisRightLine.setAxisMinValue(mData.getPercentMin());
            axisRightLine.setAxisMaxValue(mData.getPercentMax());
            axisLeftBar.setAxisMaxValue(mData.getVolMaxTime());
        }
//        axisLeftBar.setShowMaxAndUnit(unit);
        axisRightBar.setAxisMaxValue(mData.getVolMaxTime());
        ArrayList<Entry> lineCJEntries = new ArrayList<>();
        ArrayList<Entry> lineJJEntries = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0, j = 0; i < mData.getDatas().size(); i++, j++) {
            MarketTimeData t = mData.getDatas().get(j);
            if (t == null) {
                lineCJEntries.add(new Entry(i,Float.NaN));
                lineJJEntries.add(new Entry(i,Float.NaN));
                barEntries.add(new BarEntry(i,Float.NaN));
                continue;
            }
            lineCJEntries.add(new Entry(i,(float) mData.getDatas().get(i).m_fC));
            lineJJEntries.add(new Entry(i,(float) mData.getDatas().get(i).m_nAvprice));
            barEntries.add(new BarEntry(i,mData.getDatas().get(i).m_nVolume));
        }
        d1 = new LineDataSet(lineCJEntries, "成交价");
        d2 = new LineDataSet(lineJJEntries, "均价");
        d1.setDrawValues(false);
        d2.setDrawValues(false);
        d1.setLineWidth(0.7f);
        d1.setColor(getResources().getColor(R.color.minute_blue));
        d1.setFillColor(Color.parseColor("#D0F1FF"));
        d1.setMode(LineDataSet.Mode.LINEAR);
        d2.setColor(getResources().getColor(R.color.minute_yellow));
        d1.setHighLightColor(Color.GRAY);
        d2.setHighlightEnabled(false);
        d1.setDrawFilled(true);
        d2.setDrawCircles(false);
        d1.setDrawCircles(false);

        barDataSet = new BarDataSet(barEntries, "成交量");
//        barDataSet.setBarSpacePercent(50);
        barDataSet.setHighLightColor(Color.GRAY);
//        barDataSet.setHighLightAlpha(255);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setColor(Color.parseColor("#e24c4d"));
        List<Integer> list=new ArrayList<>();
        list.add(Color.parseColor("#e24c4d"));
        list.add(Color.parseColor("#3fa65f"));
        barDataSet.setColors(list);
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);
        LineData cd = new LineData(sets);
        lineChart.setData(cd);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

//        lineChart.getXAxis().setValueFormatter(new TimeLineXFormatter(xVals));
//        barChart.getXAxis().setValueFormatter(new TimeBarXFormatter(mData.getDatas()));
        Long timeMilli = 840l;
        xAxisBar.setXLabels(getXLabels());
        xAxisBar.setLabelCount(getXLabels().size(),true);
        lineChart.setVisibleXRange(timeMilli, timeMilli);
        barChart.setVisibleXRange(timeMilli, timeMilli);

        lineChart.moveViewToX(mData.getDatas().size() - 1);
        barChart.moveViewToX(mData.getDatas().size() - 1);
        lineChart.invalidate();
        barChart.invalidate();
    }

    public void dynamicsAddOne(String xValue,float chPrice,float junPrice,float vol,int length){
        int index = length - 1;
        LineData lineData = lineChart.getData();
        ILineDataSet d1 = lineData.getDataSetByIndex(0);
        d1.addEntry(new Entry(index,chPrice));
        ILineDataSet d2 = lineData.getDataSetByIndex(1);
        d2.addEntry(new Entry(index,junPrice));

        BarData barData = barChart.getData();
        IBarDataSet barDataSet = barData.getDataSetByIndex(0);
        barDataSet.addEntry(new BarEntry(index,vol));
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        Long timeMilli = 840l;
        if(timeMilli != null){
            lineChart.setVisibleXRange(timeMilli, timeMilli);
        }
        lineChart.moveViewToX(index);

        barData.notifyDataChanged();
        barChart.notifyDataSetChanged();
        if(timeMilli != null){
            barChart.setVisibleXRange(timeMilli, timeMilli);
        }
        barChart.moveViewToX(index);
    }

    public void dynamicsUpdateOne(String xValue,float chPrice,float junPrice,float vol,int length){
        int index = length - 1;
        LineData lineData = lineChart.getData();
        ILineDataSet d1 = lineData.getDataSetByIndex(0);
        Entry e = d1.getEntryForIndex(index);
        d1.removeEntry(e);
        d1.addEntry(new Entry(index,chPrice));

        ILineDataSet d2 = lineData.getDataSetByIndex(1);
        Entry e2 = d2.getEntryForIndex(index);
        d2.removeEntry(e2);
        d2.addEntry(new Entry(index,junPrice));

        BarData barData = barChart.getData();
        IBarDataSet barDataSet = barData.getDataSetByIndex(0);
        barDataSet.removeEntry(index);
        barDataSet.addEntry(new BarEntry(index,vol));

        //不可见修改数据不刷新
        lineData.notifyDataChanged();
        lineChart.notifyDataSetChanged();
        lineChart.moveViewToX(index);

        barData.notifyDataChanged();
        barChart.notifyDataSetChanged();
        barChart.moveViewToX(index);
    }

    private void setMarkerView(KTimeData mData) {
        KLeftMarkerView leftMarkerView = new KLeftMarkerView(mAct, R.layout.ly_markerview);
        TimeRightMarkerView rightMarkerView = new TimeRightMarkerView(mAct, R.layout.ly_markerview);
        lineChart.setMarker(leftMarkerView, rightMarkerView, mData);
    }

    private void setBottomMarkerView(KTimeData kDatas){
        BarBottomMarkerView bottomMarkerView = new BarBottomMarkerView(mAct, R.layout.ly_markerview);
        barChart.setMarker(bottomMarkerView, kDatas, null,-1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserEvent(CirclePositionTime position) {
        cirCleView.setX(position.cx - CommonUtil.dip2px(mAct,7));
        cirCleView.setY(position.cy - CommonUtil.dip2px(mAct,14));
    }

    private void playHeartbeatAnimation(final View heartbeatView) {
        AnimationSet swellAnimationSet = new AnimationSet(true);
        swellAnimationSet.addAnimation(new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f));
        swellAnimationSet.setDuration(1000);
        swellAnimationSet.setInterpolator(new AccelerateInterpolator());
        swellAnimationSet.setFillAfter(true);//动画终止时停留在最后一帧~不然会回到没有执行之前的状态
        heartbeatView.startAnimation(swellAnimationSet);
        swellAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet shrinkAnimationSet = new AnimationSet(true);
                shrinkAnimationSet.addAnimation(new ScaleAnimation(2.0f, 1.0f, 2.0f, 1.0f,Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
                shrinkAnimationSet.setDuration(1000);
                shrinkAnimationSet.setInterpolator(new DecelerateInterpolator());
                shrinkAnimationSet.setFillAfter(false);
                heartbeatView.startAnimation(shrinkAnimationSet);// 动画结束时重新开始，实现心跳的View
                shrinkAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        playHeartbeatAnimation(heartbeatView);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    private SparseArray<String> getXLabels() {
        SparseArray<String> xLabels = new SparseArray<>();
        xLabels.put(0, "17:15");
        xLabels.put(240, "21:15");
        xLabels.put(465, "01:00");
        xLabels.put(630, "12:00");
        xLabels.put(840, "16:30");
        return xLabels;
    }

    public void cleanData(){
        if(lineChart != null && lineChart.getLineData() != null){
            setShowLabels(false);
            lineChart.clearValues();
            barChart.clearValues();
        }
        if(cirCleView != null){
            cirCleView.setVisibility(View.GONE);
        }
    }

}