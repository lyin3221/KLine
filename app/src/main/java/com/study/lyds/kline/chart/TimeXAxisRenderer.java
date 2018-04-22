package com.study.lyds.kline.chart;

import android.graphics.Canvas;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by ly on 2017/2/8.
 */
public class TimeXAxisRenderer extends XAxisRenderer {
    private final BarLineChartBase mChart;
    protected LyXAxis mXAxis;

    public TimeXAxisRenderer(ViewPortHandler viewPortHandler, LyXAxis xAxis, Transformer trans, BarLineChartBase chart) {
        super(viewPortHandler, xAxis, trans);
        mXAxis = xAxis;
        mChart = chart;
    }

    @Override
    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
        float[] position = new float[]{0f, 0f};
        int count = mXAxis.getXLabels().size();
        for (int i = 0; i < count; i ++) {
              /*获取label对应key值，也就是x轴坐标0,60,121,182,242*/
            int ix = mXAxis.getXLabels().keyAt(i);
            position[0] = ix;
            /*在图表中的x轴转为像素，方便绘制text*/
            mTrans.pointValuesToPixel(position);
            /*x轴越界*/
//            if (mViewPortHandler.isInBoundsX(position[0])) {
                String label = mXAxis.getXLabels().valueAt(i);
                /*文本长度*/
                int labelWidth = Utils.calcTextWidth(mAxisLabelPaint, label);
                /*右出界*/
                if ((labelWidth / 2 + position[0]) > mChart.getViewPortHandler().contentRight()) {
                    position[0] = mChart.getViewPortHandler().contentRight() - labelWidth / 2;
                } else if ((position[0] - labelWidth / 2) < mChart.getViewPortHandler().contentLeft()) {//左出界
                    position[0] = mChart.getViewPortHandler().contentLeft() + labelWidth / 2;
                }
                c.drawText(label, position[0],pos+ Utils.convertPixelsToDp(mChart.getViewPortHandler().offsetBottom()),
                        mAxisLabelPaint);
//            }
        }
    }

//    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
//        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
//        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();
//
//        float[] positions = new float[mXAxis.mEntryCount * 2];
//
//        for (int i = 0; i < positions.length; i += 2) {
//
//            // only fill x values
//            if (centeringEnabled) {
//                positions[i] = mXAxis.mCenteredEntries[i / 2];
//            } else {
//                positions[i] = mXAxis.mEntries[i / 2];
//            }
//        }
//
//        mTrans.pointValuesToPixel(positions);
//
//        for (int i = 0,j = 1; i < positions.length; i += 2,j+=1) {
//            float x = positions[i];
//
//            if (mViewPortHandler.isInBoundsX(x)) {
//
//                String label = mXAxis.getXLabels().valueAt(j-1);
////                String label = mXAxis.getValueFormatter().getFormattedValue(mXAxis.mEntries[i / 2], mXAxis);
//
//                if (mXAxis.isAvoidFirstLastClippingEnabled()) {
//
//                    // avoid clipping of the last 0  2  4  6
//                    if (j == mXAxis.mEntryCount && mXAxis.mEntryCount > 1) {
//                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
//
////                        if (width > mViewPortHandler.offsetRight() * 2 && x + width > mViewPortHandler.getChartWidth())
//                            x -= width / 2;
//
//                        // avoid clipping of the first
//                    } else if (j == 1) {
//                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
//                        x += width / 2;
//                    }
//                }
//                drawLabel(c, label, (int)(x+0.5), pos, anchor, labelRotationAngleDegrees);//处理坐标抖动
//            }
//        }
//    }

    /*x轴垂直线*/
    @Override
    public void renderGridLines(Canvas c) {
        if (!mXAxis.isDrawGridLinesEnabled() || !mXAxis.isEnabled())
            return;
        float[] position = new float[]{
                0f, 0f
        };

        mGridPaint.setColor(mXAxis.getGridColor());
        mGridPaint.setStrokeWidth(mXAxis.getGridLineWidth());
        mGridPaint.setPathEffect(mXAxis.getGridDashPathEffect());
        int count = mXAxis.getXLabels().size();
        if (!mChart.isScaleXEnabled()) {
            count -= 1;
        }
        for (int i = 0; i < count; i ++) {
            int ix = mXAxis.getXLabels().keyAt(i);
            position[0] = ix;
            mTrans.pointValuesToPixel(position);
            c.drawLine(position[0], mViewPortHandler.offsetTop(), position[0],
                    mViewPortHandler.contentBottom(), mGridPaint);
        }
    }

}
