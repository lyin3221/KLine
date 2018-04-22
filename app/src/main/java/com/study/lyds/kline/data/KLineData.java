package com.study.lyds.kline.data;

import android.graphics.Color;
import android.graphics.Paint;
import android.widget.TextView;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.study.lyds.kline.R;
import com.study.lyds.kline.base.LyApplication;
import com.study.lyds.kline.model.MarketKLineData;
import com.study.lyds.kline.model.bean.BOLLEntity;
import com.study.lyds.kline.model.bean.CCIEntity;
import com.study.lyds.kline.model.bean.EXPMAEntity;
import com.study.lyds.kline.model.bean.KDJEntity;
import com.study.lyds.kline.model.bean.MACDEntity;
import com.study.lyds.kline.model.bean.RSIEntity;
import com.study.lyds.kline.model.bean.SMAEntity;
import com.study.lyds.kline.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ly on 2017/8/20.
 */
public class KLineData {
    private ArrayList<MarketKLineData> kDatas = new ArrayList<>();
    private float offSet = 0.5f;

    //MA参数
    private int N1 = 5;
    private int N2 = 20;
    private int N3 = 30;
    //EMA参数
    private int EMAN1 = 5;
    private int EMAN2 = 10;
    private int EMAN3 = 30;
    //RSI参数
    private int RSIN1 = 7;
    private int RSIN2 = 14;
    //BOLL参数
    private int BOLLN = 26;
    //KDJ参数
    private int KDJN = 9;
    private int KDJM1 = 3;
    private int KDJM2 = 3;
    //CCI参数
    private int CCIN = 14;
    //MACD参数
    private int SHORT = 12;
    private int LONG = 26;
    private int M = 9;

    private ArrayList<String> xVal = new ArrayList<>();

    BarDataSet volumeDataSet;
    CandleDataSet candleDataSet;

    private List<ILineDataSet>lineDataMA = new ArrayList<>();

    private List<ILineDataSet>lineDataEMA = new ArrayList<>();
    private ArrayList<Entry> emaDataN1 = new ArrayList<>();
    private ArrayList<Entry> emaDataN2 = new ArrayList<>();
    private ArrayList<Entry> emaDataN3 = new ArrayList<>();

    private List<ILineDataSet>lineDataBOLL = new ArrayList<>();
    private ArrayList<Entry> bollDataUP = new ArrayList<>();
    private ArrayList<Entry> bollDataMB = new ArrayList<>();
    private ArrayList<Entry> bollDataDN = new ArrayList<>();

    private List<ILineDataSet>lineDataKDJ = new ArrayList<>();
    private ArrayList<Entry> kData = new ArrayList<>();
    private ArrayList<Entry> dData = new ArrayList<>();
    private ArrayList<Entry> jData = new ArrayList<>();

    private List<ILineDataSet>lineDataRSI = new ArrayList<>();
    private ArrayList<Entry> rsiData7 = new ArrayList<>();
    private ArrayList<Entry> rsiData14 = new ArrayList<>();

    private List<ILineDataSet>lineDataMACD = new ArrayList<>();
    private BarDataSet barDataMACD;
    private ArrayList<BarEntry> macdData = new ArrayList<>();
    private ArrayList<Entry> deaData = new ArrayList<>();
    private ArrayList<Entry> difData = new ArrayList<>();

    private List<ILineDataSet>lineDataCCI = new ArrayList<>();
    private ArrayList<Entry> cciData = new ArrayList<>();

    private List<ILineDataSet>lineDataSMA = new ArrayList<>();
    private ArrayList<Entry> smaData = new ArrayList<>();

    public void initSMA(){
        SMAEntity smaEntity = new SMAEntity(getKLineDatas(),14);
        smaData = new ArrayList<>();

        for(int i= 0;i < getKLineDatas().size();i++){
            smaData.add(new Entry(i+offSet,smaEntity.getSMAs().get(i)));
        }
        lineDataSMA.add(setALine(ColorType.blue,smaData));
    }

    public void initCCI(){
        CCIEntity cciEntity = new CCIEntity(getKLineDatas(),CCIN);
        cciData = new ArrayList<>();

        for(int i = 0;i < getKLineDatas().size();i++){
            cciData.add(new Entry(i+offSet,cciEntity.getCCIs().get(i)));
        }
        lineDataCCI.add(setALine(ColorType.blue,cciData,true));
    }

    /**
     * 初始化MACD
     */
    public void initMACD() {
        MACDEntity macdEntity = new MACDEntity(getKLineDatas(),SHORT,LONG,M);

        macdData = new ArrayList<>();
        deaData = new ArrayList<>();
        difData = new ArrayList<>();
        for (int i = 0; i < macdEntity.getMACD().size(); i++) {
            macdData.add(new BarEntry(i+offSet,macdEntity.getMACD().get(i),macdEntity.getMACD().get(i)));
            deaData.add(new Entry(i+offSet,macdEntity.getDEA().get(i)));
            difData.add(new Entry(i+offSet,macdEntity.getDIF().get(i)));
        }
        barDataMACD = setABar(macdData);
        lineDataMACD.add(setALine(ColorType.blue,deaData));
        lineDataMACD.add(setALine(ColorType.yellow,difData));
    }

    /**
     * 初始化RSI
     */
    public void initRSI(){
        RSIEntity rsiEntity7 = new RSIEntity(getKLineDatas(), RSIN1);
        RSIEntity rsiEntity14 = new RSIEntity(getKLineDatas(), RSIN2);

        rsiData7 = new ArrayList<>();
        rsiData14 = new ArrayList<>();
        for (int i = 0; i < rsiEntity7.getRSIs().size(); i++) {
            rsiData7.add(new Entry(i+offSet,rsiEntity7.getRSIs().get(i)));
            rsiData14.add(new Entry(i+offSet,rsiEntity14.getRSIs().get(i)));
        }
        lineDataRSI.add(setALine(ColorType.blue,rsiData7,"RSI"+RSIN1,false));
        lineDataRSI.add(setALine(ColorType.purple,rsiData14,"RSI"+RSIN2,true));
    }

    /**
     * 初始化KDJ
     */
    public void initKDJ(){
        KDJEntity kdjEntity = new KDJEntity(getKLineDatas(), KDJN,KDJM1,KDJM2);

        kData = new ArrayList<>();
        dData = new ArrayList<>();
        jData = new ArrayList<>();
        for (int i = 0; i < kdjEntity.getD().size(); i++) {
            kData.add(new Entry(i+offSet,kdjEntity.getK().get(i)));
            dData.add(new Entry(i+offSet,kdjEntity.getD().get(i)));
            jData.add(new Entry(i+offSet,kdjEntity.getJ().get(i)));
        }
        lineDataKDJ.add(setALine(ColorType.blue,kData,"KDJ"+N1,false));
        lineDataKDJ.add(setALine(ColorType.yellow,dData,"KDJ"+N2,false));
        lineDataKDJ.add(setALine(ColorType.purple,jData,"KDJ"+N3,true));
    }

    /**
     * 初始化EMA
     */
    public void initEMA(){
        EXPMAEntity emaEntity5 = new EXPMAEntity(getKLineDatas(),EMAN1);
        EXPMAEntity emaEntity10 = new EXPMAEntity(getKLineDatas(),EMAN2);
        EXPMAEntity emaEntity20 = new EXPMAEntity(getKLineDatas(),EMAN3);

        emaDataN1 = new ArrayList<>();
        emaDataN2 = new ArrayList<>();
        emaDataN3 = new ArrayList<>();
        for (int i = 0; i < emaEntity5.getEXPMAs().size(); i++) {
                emaDataN1.add(new Entry(i+offSet,emaEntity5.getEXPMAs().get(i)));
                emaDataN2.add(new Entry(i+offSet,emaEntity10.getEXPMAs().get(i)));
                emaDataN3.add(new Entry(i+offSet,emaEntity20.getEXPMAs().get(i)));
            }
        lineDataEMA.add(setALine(ColorType.blue,emaDataN1,false));
        lineDataEMA.add(setALine(ColorType.yellow,emaDataN2,false));
        lineDataEMA.add(setALine(ColorType.purple,emaDataN3,false));
    }

    /**
     * 初始化BOLL
     */
    public void initBOLL() {
        BOLLEntity bollEntity = new BOLLEntity(getKLineDatas(), BOLLN);
        bollDataUP = new ArrayList<>();
        bollDataMB = new ArrayList<>();
        bollDataDN = new ArrayList<>();
        for (int i = 0; i < bollEntity.getUPs().size(); i++) {
            bollDataUP.add(new Entry(i+offSet,bollEntity.getUPs().get(i)));
            bollDataMB.add(new Entry(i+offSet,bollEntity.getMBs().get(i)));
            bollDataDN.add(new Entry(i+offSet,bollEntity.getDNs().get(i)));
        }
        lineDataBOLL.add(setALine(ColorType.blue,bollDataUP,false));
        lineDataBOLL.add(setALine(ColorType.yellow,bollDataMB,false));
        lineDataBOLL.add(setALine(ColorType.purple,bollDataDN,false));
    }

    /**
     * 初始化基本数据
     */
    public void initCandle(){
        xVal.clear();
        ArrayList<CandleEntry> candleEntries = new ArrayList<>();
        for (int i = 0; i < getKLineDatas().size(); i++) {
            xVal.add(getKLineDatas().get(i).m_szDate);
            candleEntries.add(new CandleEntry(i+offSet, (float) getKLineDatas().get(i).m_fH, (float)getKLineDatas().get(i).m_fL, (float)getKLineDatas().get(i).m_fO, (float)getKLineDatas().get(i).m_fC));
        }
        candleDataSet = setACandle(candleEntries);
    }

    /**
     * 初始化成交量
     */
    public void initVolume(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < getKLineDatas().size(); i++) {
//            xVals.add(TradeCalculateUtil.secToTimeShort(mData.getKLineDatas().get(i).m_nStartTime));//mData.getKLineDatas().get(i).getM_szDate()
//            barEntries.add(new BarEntry(mData.getKLineDatas().get(i).m_nVolume, i));
            float color = getKLineDatas().get(i).m_fO > getKLineDatas().get(i).m_fC ? 0f : 1f;//这里耗时10毫秒
            barEntries.add(new BarEntry(i+offSet,getKLineDatas().get(i).m_nVolume,color));
        }
        volumeDataSet = setABar(barEntries,"成交量");
    }

    /**
     * 初始化均线
     */
    public void initMaLine(){
        int kDataSize = getKLineDatas().size();
        ArrayList<Entry> line5Entries = new ArrayList<>();
        ArrayList<Entry> line10Entries = new ArrayList<>();
        ArrayList<Entry> line20Entries = new ArrayList<>();
        if(kDataSize >= N1){
            sum = getSum(0, N1 - 1);
            line5Entries.add(new Entry(N1 - 1+offSet, sum / N1));
            for(int i = N1;i < kDataSize;i++){
                sum = (float) (sum - getKLineDatas().get(i-N1).m_fC + getKLineDatas().get(i).m_fC);
                line5Entries.add(new Entry(i+offSet, sum / N1));
            }
            lineDataMA.add(setALine(ColorType.blue, line5Entries,false));
        }
        if(kDataSize >= N2){
            sum = getSum(0, N2 - 1);
            line10Entries.add(new Entry(N2 - 1+offSet,sum / N2));
            for(int i = N2;i < kDataSize;i++){
                sum = (float) (sum - getKLineDatas().get(i-N2).m_fC + getKLineDatas().get(i).m_fC);
                line10Entries.add(new Entry(i+offSet,sum / N2));
            }
            lineDataMA.add(setALine(ColorType.yellow, line10Entries,false));
        }
        if(kDataSize >= N3){
            sum = getSum(0, N3 - 1);
            line20Entries.add(new Entry(N3 - 1+offSet,sum / N3));
            for(int i = N3;i < kDataSize;i++){
                sum = (float) (sum - getKLineDatas().get(i-N3).m_fC + getKLineDatas().get(i).m_fC);
                line20Entries.add(new Entry(i+offSet,sum / N3));
            }
            lineDataMA.add(setALine(ColorType.purple, line20Entries,false));
        }
    }

    private BarDataSet setABar(ArrayList<BarEntry> barEntries){
        String label = "BarDataSet";
        return setABar(barEntries, label);
    }

    private BarDataSet setABar(ArrayList<BarEntry> barEntries, String label){
        BarDataSet barDataSet = new BarDataSet(barEntries, label);
        barDataSet.setHighlightEnabled(true);
        //barDataSet.setBarSpacePercent(50);
        barDataSet.setHighLightColor(Color.GRAY);
        //barDataSet.setHighLightAlpha(255);
        barDataSet.setValueTextSize(10);
        barDataSet.setDrawValues(false);
        List<Integer> list=new ArrayList<>();
        list.add(Color.RED);
        list.add(Color.parseColor("#3fa65f"));
        barDataSet.setColors(list);
        return barDataSet;
    }

    private CandleDataSet setACandle(ArrayList<CandleEntry> candleEntries){
        CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "KLine");
        candleDataSet.setDrawHorizontalHighlightIndicator(true);
        candleDataSet.setHighlightEnabled(true);
        candleDataSet.setHighLightColor(Color.GRAY);
//        candleDataSet.setValueTextSize(10f);
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
//        candleDataSet.setShadowWidth(0.7f);
        candleDataSet.setDecreasingColor(Color.parseColor("#3fa65f"));
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setIncreasingColor(Color.RED);
        candleDataSet.setIncreasingPaintStyle(Paint.Style.STROKE);
        candleDataSet.setValueTextSize(10);
        candleDataSet.setDrawValues(true);
//        candleDataSet.setIncreasingPaintStyle(Paint.Style.STROKE);
        candleDataSet.setNeutralColor(Color.RED);
        candleDataSet.setShadowColorSameAsCandle(true);
        candleDataSet.setHighlightLineWidth(0.5f);
        candleDataSet.setHighLightColor(Color.GRAY);
        return candleDataSet;
    }

    private LineDataSet setALine(ColorType ma, ArrayList<Entry> lineEntries) {
        String label = "ma"+ma;
        return setALine(ma, lineEntries,label);
    }

    private LineDataSet setALine(ColorType ma, ArrayList<Entry> lineEntries, boolean highlightEnable) {
        String label = "ma"+ma;
        return setALine(ma, lineEntries,label,highlightEnable);
    }

    private LineDataSet setALine(ColorType ma, ArrayList<Entry> lineEntries, String label) {
        boolean highlightEnable = false;
        return setALine(ma,lineEntries,label,highlightEnable);
    }

    private LineDataSet setALine(ColorType colorType, ArrayList<Entry> lineEntries, String label, boolean highlightEnable) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, label);
        lineDataSetMa.setDrawHorizontalHighlightIndicator(false);
        lineDataSetMa.setHighlightEnabled(highlightEnable);
        lineDataSetMa.setHighLightColor(Color.GRAY);
        lineDataSetMa.setDrawValues(false);
        if (colorType == ColorType.blue) {
            lineDataSetMa.setColor(LyApplication.getInstance().getResources().getColor(R.color.ma5));
        } else if (colorType == ColorType.yellow) {
            lineDataSetMa.setColor(LyApplication.getInstance().getResources().getColor(R.color.ma10));
        } else if(colorType == ColorType.purple){
            lineDataSetMa.setColor(LyApplication.getInstance().getResources().getColor(R.color.ma20));
        }
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetMa;
    }

    private void setMaValue(TextView maTv, float yValue, int NX){
        if(NX == 0){
            maTv.setText(0.00+"");
        }else{
            maTv.setText(NumberUtils.keepPrecision2(yValue));
        }
    }

    float sum = 0;
    private float getSum(Integer a, Integer b) {
        sum = 0;
        if(a < 0){
            return 0;
        }
        for (int i = a; i <= b; i++) {
            sum += getKLineDatas().get(i).m_fC;
        }
        return sum;
    }

    public void addAKLineData(MarketKLineData kLineData){
        kDatas.add(kLineData);
    }

    public void addKLineDatas(List<MarketKLineData> kLineData){
        kDatas.addAll(kLineData);
    }

    public synchronized ArrayList<MarketKLineData> getKLineDatas() {
        return kDatas;
    }

    public void resetKLineData(){
        kDatas.clear();
    }

    public void setKLineData(ArrayList<MarketKLineData> datas){
        kDatas.clear();
        kDatas.addAll(datas);
    }

    public ArrayList<String> getxVals() {
        return xVal;
    }

    public List<ILineDataSet> getLineDataMA() {
        return lineDataMA;
    }

    public List<ILineDataSet> getLineDataBOLL() {
        return lineDataBOLL;
    }

    public List<ILineDataSet> getLineDataEMA() {
        return lineDataEMA;
    }

    public List<ILineDataSet> getLineDataKDJ() {
        return lineDataKDJ;
    }

    public List<ILineDataSet> getLineDataRSI() {
        return lineDataRSI;
    }

    public List<ILineDataSet> getLineDataMACD() {
        return lineDataMACD;
    }

    public BarDataSet getBarDataMACD() {
        return barDataMACD;
    }

    public BarDataSet getVolumeDataSet() {
        return volumeDataSet;
    }

    public CandleDataSet getCandleDataSet() {
        return candleDataSet;
    }

    public float getOffSet() {
        return offSet;
    }

    public List<ILineDataSet> getLineDataCCI() {
        return lineDataCCI;
    }

    public List<ILineDataSet> getLineDataSMA() {
        return lineDataSMA;
    }

    public ArrayList<Entry> getBollDataUP() {
        return bollDataUP;
    }

    public ArrayList<Entry> getBollDataMB() {
        return bollDataMB;
    }

    public ArrayList<Entry> getBollDataDN() {
        return bollDataDN;
    }

    public void setOneMaValue(LineData lineData, int i){
        for(int k = 0;k < lineData.getDataSets().size();k++){
            ILineDataSet lineDataSet = lineData.getDataSetByIndex(k);
            lineDataSet.removeEntryByXValue(i);
            if(k == 0){
                if(i >= N1){
                    sum = 0;
                    float all5 = getSum(i - (N1 - 1), i) / N1;
                    lineDataSet.addEntry(new Entry(i + offSet,all5));
                }
            }else if(k == 1){
                if(i >= N2){
                    sum = 0;
                    float all10 = getSum(i - (N2 - 1), i) / N2;
                    lineDataSet.addEntry(new Entry(i+ offSet, all10));
                }
            }else if(k == 2){
                if(i >= N3){
                    sum = 0;
                    float all20 = getSum(i - (N3 - 1), i) / N3;
                    lineDataSet.addEntry(new Entry(i+ offSet, all20));
                }
            }
        }
    }

    enum ColorType{
        blue,
        yellow,
        purple
    }

}
