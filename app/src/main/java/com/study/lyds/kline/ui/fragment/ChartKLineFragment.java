package com.study.lyds.kline.ui.fragment;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.study.lyds.kline.R;
import com.study.lyds.kline.enums.TimeType;
import com.study.lyds.kline.base.BaseFragment;
import com.study.lyds.kline.chart.BarBottomMarkerView;
import com.study.lyds.kline.chart.CoupleChartGestureListener;
import com.study.lyds.kline.chart.KCombinedChart;
import com.study.lyds.kline.chart.KLeftMarkerView;
import com.study.lyds.kline.chart.KRightMarkerView;
import com.study.lyds.kline.chart.LyCombinedChart;
import com.study.lyds.kline.data.DBHelper;
import com.study.lyds.kline.data.KLineData;
import com.study.lyds.kline.utils.CommonUtil;
import com.study.lyds.kline.utils.DataTimeUtil;
import com.study.lyds.kline.utils.NumberUtils;

import butterknife.BindView;

/**
 * Created by ly on 2016/11/8.
 */
public class ChartKLineFragment extends BaseFragment {
    @BindView(R.id.combinedchart)
    KCombinedChart mChartKline;
    @BindView(R.id.barchart)
    LyCombinedChart mChartCharts;
    @BindView(R.id.chart_type_des)
    TextView chartTypeDes;

    XAxis xAxisBar, xAxisK;
    YAxis axisLeftBar, axisLeftK;
    YAxis axisRightBar, axisRightK;

    private KLineData kLineData;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mChartCharts.setAutoScaleMinMaxEnabled(true);
            mChartKline.setAutoScaleMinMaxEnabled(true);

            mChartKline.notifyDataSetChanged();
            mChartCharts.notifyDataSetChanged();
            mChartKline.invalidate();
            mChartCharts.invalidate();
        }
    };

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_kline;
    }

    @Override
    protected void initBase(View view) {
        initChart();
        kLineData = DBHelper.getDbHelper(mAct).getkLineData();
        setDataToChart();
    }

    public void initChart() {
        mChartCharts.setDrawBorders(true);
        mChartCharts.setBorderWidth(0.7f);
        mChartCharts.setBorderColor(Color.LTGRAY);
        mChartCharts.setDragEnabled(true);
        mChartCharts.setScaleYEnabled(false);

        mChartCharts.setHardwareAccelerationEnabled(true);

        Legend mChartChartsLegend = mChartCharts.getLegend();
        mChartChartsLegend.setEnabled(false);
        mChartKline.setDescription(null);
        mChartKline.setHardwareAccelerationEnabled(true);

        xAxisBar = mChartCharts.getXAxis();
        xAxisBar.setDrawGridLines(false);
        xAxisBar.setDrawAxisLine(false);
        xAxisBar.setDrawLabels(true);
        xAxisBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(getResources().getColor(R.color.minute_grayLine));
        xAxisBar.setLabelCount(4,true);
        xAxisBar.setAxisMinimum(0);
        xAxisBar.setAvoidFirstLastClipping(true);

        axisLeftBar = mChartCharts.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftBar.setDrawLabels(true);
        axisLeftBar.setLabelCount(2,true);
        axisLeftBar.setValueLineInside(true);
        axisLeftBar.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
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
        axisRightBar = mChartCharts.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);

        mChartKline.setDrawBorders(true);
        mChartKline.setBorderWidth(0.7f);
        mChartKline.setBorderColor(Color.LTGRAY);
        mChartKline.setDragEnabled(true);
        mChartKline.setScaleYEnabled(false);

        int offset = CommonUtil.dip2px(mAct,10);
        mChartKline.setViewPortOffsets(offset, CommonUtil.dip2px(mAct,5),offset, CommonUtil.dip2px(mAct,15));
        mChartCharts.setViewPortOffsets(offset, CommonUtil.dip2px(mAct,5),offset, CommonUtil.dip2px(mAct,15));
        mChartCharts.setDescription(null);

        Legend mChartKlineLegend = mChartKline.getLegend();
        mChartKlineLegend.setEnabled(false);
        xAxisK = mChartKline.getXAxis();
        xAxisK.setDrawLabels(true);
        xAxisK.setDrawGridLines(false);
        xAxisK.setDrawAxisLine(false);
        xAxisK.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        xAxisK.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxisK.setGridColor(getResources().getColor(R.color.minute_grayLine));
        xAxisK.setAvoidFirstLastClipping(true);
        xAxisK.setLabelCount(2,true);
        xAxisK.setAxisMinimum(0);

        axisLeftK = mChartKline.getAxisLeft();
        axisLeftK.setDrawGridLines(true);
        axisLeftK.setDrawAxisLine(false);
        axisLeftK.setDrawLabels(true);
        axisLeftK.setLabelCount(5,true);
        axisLeftK.enableGridDashedLine(CommonUtil.dip2px(mAct,4), CommonUtil.dip2px(mAct,3),0);
        axisLeftK.setTextColor(getResources().getColor(R.color.minute_zhoutv));
        axisLeftK.setGridColor(Color.GRAY);
        axisLeftK.setValueLineInside(true);
        axisLeftK.setDrawTopBottomGridLine(false);
        axisLeftK.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeftK.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return NumberUtils.keepPrecisionR(value, 2);
            }
        });
        axisRightK = mChartKline.getAxisRight();
        axisRightK.setDrawLabels(false);
        axisRightK.setDrawGridLines(false);
        axisRightK.setDrawAxisLine(false);
        axisRightK.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRightK.setGridColor(Color.GRAY);
        mChartKline.setDragDecelerationEnabled(true);
        mChartCharts.setDragDecelerationEnabled(true);
        mChartKline.setDragDecelerationFrictionCoef(0.92f);//0.92持续滚动时的速度快慢，[0,1) 0代表立即停止。
        mChartCharts.setDragDecelerationFrictionCoef(0.92f);//设置太快，切换滑动源滑动不同步

        CoupleChartGestureListener gestureListenerK = new CoupleChartGestureListener(mChartKline, new Chart[]{mChartCharts});
        CoupleChartGestureListener gestureListenerCharts = new CoupleChartGestureListener(mChartCharts, new Chart[]{mChartKline});
        gestureListenerK.setCoupleClick(new CoupleChartGestureListener.CoupleClick() {
            @Override
            public void singleClickListener() {
                doMainChartSwitch();
            }
        });
        gestureListenerCharts.setCoupleClick(new CoupleChartGestureListener.CoupleClick() {
            @Override
            public void singleClickListener() {
                doMinorChartSwitch();
            }
        });
        mChartKline.setOnChartGestureListener(gestureListenerK);
        mChartCharts.setOnChartGestureListener(gestureListenerCharts);
        mChartKline.setDoubleTapToZoomEnabled(false);
        mChartCharts.setDoubleTapToZoomEnabled(false);

        mChartKline.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mChartKline.highlightValue(h);
                if(mChartCharts.getData().getBarData().getDataSets().size() != 0){
                    Highlight highlight = new Highlight(h.getX(), h.getDataSetIndex(), h.getStackIndex());
                    highlight.setDataIndex(h.getDataIndex());
                    mChartCharts.highlightValues(new Highlight[]{highlight});
                }else{
                    Highlight highlight = new Highlight(h.getX(), 2, h.getStackIndex());
                    highlight.setDataIndex(0);
                    mChartCharts.highlightValues(new Highlight[]{highlight});
                }
            }

            @Override
            public void onNothingSelected() {
                mChartCharts.highlightValues(null);
            }
        });
        mChartCharts.setOnChartValueSelectedListener(new OnChartValueSelectedListener(){

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mChartCharts.highlightValue(h);
                Highlight highlight = new Highlight(h.getX(), 0, h.getStackIndex());
                highlight.setDataIndex(1);
                mChartKline.highlightValues(new Highlight[]{highlight});
            }

            @Override
            public void onNothingSelected() {
                mChartKline.highlightValues(null);
            }
        });

        mChartKline.setNoDataText("");
        mChartCharts.setNoDataText("");
    }

    public void setDataToChart(){
        setMarkerView(kLineData);
        setBottomMarkerView(kLineData);

        kLineData.initCandle();
        kLineData.initVolume();
        kLineData.initMaLine();

        mChartKline.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int)(value - kLineData.getOffSet());
                if(index <= 0 || index >= kLineData.getxVals().size()){
                    return value+"";
                }else{
                    return kLineData.getxVals().get(index);
                }
            }
        });
        mChartCharts.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int)(value - kLineData.getOffSet());
                if(index <= 0 || index >= kLineData.getKLineDatas().size()){
                    return value+"";
                }else{
                    return DataTimeUtil.secToTime(kLineData.getKLineDatas().get(index).m_nStartTime).substring(0,5);
                }
            }
        });

        ViewPortHandler viewPortHandlerBar = mChartCharts.getViewPortHandler();
//        float maxScale = calMaxScale(viewPortHandlerBar.contentRight() - viewPortHandlerBar.contentLeft(), kLineData.getxVals().size());
        float maxScale = calMaxScale(1020, kLineData.getxVals().size());
        float xScale = maxScale/4;
        viewPortHandlerBar.setMaximumScaleX(maxScale);
        Matrix touchBar = viewPortHandlerBar.getMatrixTouch();
        touchBar.setScale(xScale, 1f);
        ViewPortHandler viewPortHandlerCombin = mChartKline.getViewPortHandler();
        viewPortHandlerCombin.setMaximumScaleX(maxScale);
        Matrix touchMatrix = viewPortHandlerCombin.getMatrixTouch();
        touchMatrix.setScale(xScale, 1f);

        CombinedData combinedChartsData = new CombinedData();
        combinedChartsData.setData(new BarData(kLineData.getVolumeDataSet()));
        combinedChartsData.setData(new LineData());
        mChartCharts.setData(combinedChartsData);

        CombinedData combinedKlineData = new CombinedData();
        LineData lineData = new LineData(kLineData.getLineDataMA());
        CandleData candleData = new CandleData(kLineData.getCandleDataSet());
        combinedKlineData.setData(lineData);
        combinedKlineData.setData(candleData);
        mChartKline.setData(combinedKlineData);

        mChartKline.getXAxis().setAxisMaximum(combinedKlineData.getXMax() + kLineData.getOffSet());
        mChartCharts.getXAxis().setAxisMaximum(combinedChartsData.getXMax() + kLineData.getOffSet());
        mChartKline.moveViewToX(kLineData.getKLineDatas().size() - 1);
        mChartCharts.moveViewToX(kLineData.getKLineDatas().size() - 1);
        handler.sendEmptyMessageDelayed(0, 0);
    }

    protected int chartType = 1;
    protected int chartTypes = 3;
    public void doMainChartSwitch(){
        chartType++;
        if(chartType > chartTypes){
            chartType = 1;
        }
        switch (chartType){
            case 1:
                setMAToChart();
                break;
            case 2:
                setEMAToChart();
                break;
            case 3:
                setBOLLToChart();
                break;
//            case 4:
//                setSMAToChart();
//                break;
            default:
                break;
        }
    }

    protected int chartType1 = 1;
    protected int chartTypes1 = 5;
    public void doMinorChartSwitch(){
        chartType1++;
        if(chartType1 > chartTypes1){
            chartType1 = 1;
        }
        switch (chartType1){
            case 1:
                setVolumeToChart();
                break;
            case 2:
                setKDJToChart();
                break;
            case 3:
                setRSIToChart();
                break;
            case 4:
                setMACDToChart();
                break;
            case 5:
                setCCIToChart();
                break;
            default:
                break;
        }
    }

    public void setCCIToChart(){
        if(mChartCharts != null) {
            if (mChartCharts.getBarData() != null) {
                mChartCharts.getBarData().clearValues();
            }
            if (mChartCharts.getLineData() != null) {
                mChartCharts.getLineData().clearValues();
            }
            chartTypeDes.setText("CCI");
            kLineData.initCCI();
            axisLeftBar.resetAxisMinimum();

            CombinedData combinedData = mChartCharts.getData();
            combinedData.setData(new LineData(kLineData.getLineDataCCI()));
            mChartCharts.notifyDataSetChanged();
        }
    }

    public void setVolumeToChart(){
        if(mChartCharts != null) {
            if (mChartCharts.getBarData() != null) {
                mChartCharts.getBarData().clearValues();
            }
            if (mChartCharts.getLineData() != null) {
                mChartCharts.getLineData().clearValues();
            }
            chartTypeDes.setText("VOL");
            kLineData.initVolume();
            axisLeftBar.setAxisMinValue(0);

            CombinedData combinedData = mChartCharts.getData();
            combinedData.setData(new BarData(kLineData.getVolumeDataSet()));
            mChartCharts.notifyDataSetChanged();
        }
    }

    public void setMACDToChart(){
        if(mChartCharts != null) {
            if (mChartCharts.getBarData() != null) {
                mChartCharts.getBarData().clearValues();
            }
            if (mChartCharts.getLineData() != null) {
                mChartCharts.getLineData().clearValues();
            }
            chartTypeDes.setText("MACD");
            kLineData.initMACD();
            axisLeftBar.resetAxisMinimum();

            CombinedData combinedData = mChartCharts.getData();
            combinedData.setData(new LineData(kLineData.getLineDataMACD()));
            combinedData.setData(new BarData(kLineData.getBarDataMACD()));
            mChartCharts.notifyDataSetChanged();
        }
    }

    public void setRSIToChart(){
        if(mChartCharts != null) {
            if (mChartCharts.getBarData() != null) {
                mChartCharts.getBarData().clearValues();
            }
            if (mChartCharts.getLineData() != null) {
                mChartCharts.getLineData().clearValues();
            }
            chartTypeDes.setText("RSI");
            kLineData.initRSI();
            axisLeftBar.resetAxisMinimum();

            CombinedData combinedData = mChartCharts.getData();
            combinedData.setData(new LineData(kLineData.getLineDataRSI()));
            mChartCharts.notifyDataSetChanged();
        }
    }

    public void setKDJToChart(){
        if(mChartCharts != null){
            if(mChartCharts.getBarData() != null){
                mChartCharts.getBarData().clearValues();
            }
            if(mChartCharts.getLineData() != null){
                mChartCharts.getLineData().clearValues();
            }
            chartTypeDes.setText("KDJ");
            kLineData.initKDJ();
            axisLeftBar.resetAxisMinimum();

            CombinedData combinedData = mChartCharts.getData();
            combinedData.setData(new LineData(kLineData.getLineDataKDJ()));
            mChartCharts.notifyDataSetChanged();
        }
    }

    public void setSMAToChart(){
        if(mChartKline != null && mChartKline.getLineData() != null){
            mChartKline.getLineData().clearValues();

            kLineData.initSMA();
            CombinedData combinedData = mChartKline.getData();
            combinedData.setData(new LineData(kLineData.getLineDataSMA()));
            mChartKline.notifyDataSetChanged();
        }
    }

    public void setBOLLToChart(){
        if(mChartKline != null && mChartKline.getLineData() != null){
            mChartKline.getLineData().clearValues();

            kLineData.initBOLL();
            CombinedData combinedData = mChartKline.getData();
            combinedData.setData(new LineData(kLineData.getLineDataBOLL()));
            mChartKline.notifyDataSetChanged();
        }
    }

    public void setEMAToChart(){
        if(mChartKline != null && mChartKline.getLineData() != null){
            mChartKline.getLineData().clearValues();

            kLineData.initEMA();
            CombinedData combinedData = mChartKline.getData();
            combinedData.setData(new LineData(kLineData.getLineDataEMA()));
            mChartKline.notifyDataSetChanged();
        }
    }

    public void setMAToChart(){
        if(mChartKline != null && mChartKline.getLineData() != null){
            mChartKline.getLineData().clearValues();

            kLineData.initMaLine();
            CombinedData combinedData = mChartKline.getData();
            combinedData.setData(new LineData(kLineData.getLineDataMA()));
            mChartKline.notifyDataSetChanged();
        }
    }

    public void setMarkerView(KLineData kLineData) {
        KLeftMarkerView leftMarkerView = new KLeftMarkerView(mAct, R.layout.ly_markerview);
        KRightMarkerView rightMarkerView = new KRightMarkerView(mAct, R.layout.ly_markerview);
        mChartKline.setMarker(leftMarkerView, rightMarkerView,kLineData);
    }

    protected TimeType timeType = TimeType.TIME_HOUR;
    public void setBottomMarkerView(KLineData kLineData){
        BarBottomMarkerView bottomMarkerView = new BarBottomMarkerView(mAct, R.layout.ly_markerview);
        mChartCharts.setMarker(bottomMarkerView,null, kLineData,timeType);
    }

    public float calMaxScale(float viewPortWidth, float count) {
        float maxScale = 1;
        if(count >= 73){
            maxScale = 56/(viewPortWidth/count);
        }else{
            maxScale = 56/(viewPortWidth/count)/4;
        }
        return maxScale;
    }

    public void addAData(KLineData kLineData){
        int size = kLineData.getKLineDatas().size();
        CombinedData combinedData0 = mChartCharts.getData();
        IBarDataSet barDataSet = combinedData0.getBarData().getDataSetByIndex(0);
        if(barDataSet == null){//当没有数据时
            return;
        }
        float color = kLineData.getKLineDatas().get(size - 1).m_fO > kLineData.getKLineDatas().get(size - 1).m_fC ? 0f : 1f;
        BarEntry barEntry = new BarEntry(size - 1 + kLineData.getOffSet(),kLineData.getKLineDatas().get(size - 1).m_nVolume, color);

        barDataSet.addEntry(barEntry);

        CombinedData combinedData = mChartKline.getData();
        CandleData candleData = combinedData.getCandleData();
        String xValue = kLineData.getKLineDatas().get(size - 1).m_szDate;
        kLineData.getxVals().add(xValue);

        ICandleDataSet candleDataSet = candleData.getDataSetByIndex(0);
        int i = size - 1;
        candleDataSet.addEntry(new CandleEntry(i + kLineData.getOffSet(), (float) kLineData.getKLineDatas().get(i).m_fH, (float)kLineData.getKLineDatas().get(i).m_fL, (float)kLineData.getKLineDatas().get(i).m_fO, (float)kLineData.getKLineDatas().get(i).m_fC));
        kLineData.getxVals().add(kLineData.getKLineDatas().get(i).m_szDate);

//        doRefreshK(chartType);
//        doRefreshCharts(chartType1);

        mChartKline.getXAxis().setAxisMaximum(i + 1f);
        mChartCharts.getXAxis().setAxisMaximum(i + 1f);
        mChartCharts.notifyDataSetChanged();
        mChartKline.notifyDataSetChanged();
        mChartCharts.invalidate();
        mChartKline.invalidate();
    }

    public void updateAData(KLineData kLineData){
        int size = kLineData.getKLineDatas().size();
        CombinedData combinedData = mChartKline.getData();
        CandleData candleData = combinedData.getCandleData();
        ICandleDataSet candleDataSet = candleData.getDataSetByIndex(0);
        candleDataSet.removeEntry(size - 1);
        int i = size - 1;
        candleDataSet.addEntry(new CandleEntry(i + kLineData.getOffSet(), (float) kLineData.getKLineDatas().get(i).m_fH, (float)kLineData.getKLineDatas().get(i).m_fL, (float)kLineData.getKLineDatas().get(i).m_fO, (float)kLineData.getKLineDatas().get(i).m_fC));
        if(chartType1 == 1){
            CombinedData combinedData0 = mChartCharts.getData();
            IBarDataSet barDataSet = combinedData0.getBarData().getDataSetByIndex(0);
            barDataSet.removeEntry(size - 1);
            float color = kLineData.getKLineDatas().get(size - 1).m_fO > kLineData.getKLineDatas().get(size - 1).m_fC ? 0f : 1f;
            BarEntry barEntry = new BarEntry(size - 1 + kLineData.getOffSet(), kLineData.getKLineDatas().get(size - 1).m_nVolume, color);
            barDataSet.addEntry(barEntry);
        }else{
//            doRefreshCharts(chartType1);
        }
        if(chartType == 1){
            kLineData.setOneMaValue(combinedData.getLineData(),i);
        }else{
//            doRefreshK(chartType);
        }

        mChartCharts.notifyDataSetChanged();
        mChartKline.notifyDataSetChanged();
        mChartCharts.invalidate();
        mChartKline.invalidate();
    }

//    public void doRefreshK(int chartType){
//        switch (chartType){
//            case 1:
//                setMAToChart();
//                break;
//            case 2:
//                setEMAToChart();
//                break;
//            case 3:
//                setBOLLToChart();
//                break;
////            case 4:
////                setSMAToChart();
////                break;
//            default:
//                break;
//        }
//    }

//    public void doRefreshCharts(int chartType){
//        switch (chartType){
//            case 1:
//                setVolumeToChart();
//                break;
//            case 2:
//                setKDJToChart();
//                break;
//            case 3:
//                setRSIToChart();
//                break;
//            case 4:
//                setMACDToChart();
//                break;
//            case 5:
//                setCCIToChart();
//                break;
//            default:
//                break;
//        }
//    }

}
