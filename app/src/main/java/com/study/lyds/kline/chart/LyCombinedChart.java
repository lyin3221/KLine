package com.study.lyds.kline.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.study.lyds.kline.enums.TimeType;
import com.study.lyds.kline.data.KLineData;
import com.study.lyds.kline.data.KTimeData;
import com.study.lyds.kline.utils.DataTimeUtil;

/**
 * Created by ly on 2018/3/14.
 */
public class LyCombinedChart extends CombinedChart {
    private BarBottomMarkerView markerBottom;
    private KTimeData kTimeData;
    private KLineData kLineData;

    public LyCombinedChart(Context context) {
        super(context);
    }

    public LyCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LyCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initRenderer() {
        mRenderer = new LyCombinedChartRenderer(this, mAnimator, mViewPortHandler);
    }

    private TimeType timeType = TimeType.TIME_HOUR;
    public void setMarker(BarBottomMarkerView markerBottom, KTimeData kTimeData, KLineData kLineData, TimeType timeType) {
        this.markerBottom = markerBottom;
        this.kTimeData = kTimeData;
        this.kLineData = kLineData;
        this.timeType = timeType;
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        // if there is no marker view or drawing marker is disabled
        if (!isDrawMarkersEnabled() || !valuesToHighlight())
            return;

        for (int i = 0; i < mIndicesToHighlight.length; i++) {

            Highlight highlight = mIndicesToHighlight[i];

            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());

            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            int entryIndex = set.getEntryIndex(e);

            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX())
                continue;

            float[] pos = getMarkerPosition(highlight);

            // check bounds
            if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                continue;

            String date = null;
            if(kLineData == null){
                date = DataTimeUtil.secToTime(kTimeData.getDatas().get((int) e.getX()).m_nUpdateTime);
            }else{
                if(this.timeType == TimeType.TIME_HOUR){
                    date = DataTimeUtil.secToTime(kLineData.getKLineDatas().get((int) e.getX()).m_nStartTime);
                }else{
                    date = kLineData.getKLineDatas().get((int) e.getX()).m_szDate;
                }
            }
            markerBottom.setData(date);
            markerBottom.refreshContent(e, highlight);
            markerBottom.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            markerBottom.layout(0, 0, markerBottom.getMeasuredWidth(), markerBottom.getMeasuredHeight());

            int width = markerBottom.getWidth()/2;
            if(mViewPortHandler.contentRight() - pos[0] <= width){
                markerBottom.draw(canvas, mViewPortHandler.contentRight() - markerBottom.getWidth()/2, mViewPortHandler.contentBottom() + markerBottom.getHeight());//-markerBottom.getHeight()   CommonUtil.dip2px(getContext(),65.8f)
            }else if(pos[0] - mViewPortHandler.contentLeft() <= width){
                markerBottom.draw(canvas, mViewPortHandler.contentLeft() + markerBottom.getWidth()/2, mViewPortHandler.contentBottom() + markerBottom.getHeight());
            }else{
                markerBottom.draw(canvas, pos[0], mViewPortHandler.contentBottom() + markerBottom.getHeight());
            }
        }
    }


}
