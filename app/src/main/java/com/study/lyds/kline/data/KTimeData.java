package com.study.lyds.kline.data;

import com.study.lyds.kline.model.MarketTimeData;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ly on 2018/1/22.
 */

public class KTimeData {
    private ArrayList<MarketTimeData> realTimeDatas = new ArrayList<>();
    private double baseValue = 0;
    private float permaxmin = 0;
    private int mAllVolume = 0;
    private float volMaxTimeLine;
    public boolean isMaxValueChange = false;

    /**添加实时数据到分数数据集合*/
    public void addARealTimeData(MarketTimeData realTimeData,String productID){
        isMaxValueChange = false;
        if(getRealTimeData().size() == 0){
            if(!realTimeData.m_szInstrumentID.equals(productID)){
                return;
            }
            mAllVolume = realTimeData.m_nVolume;
            permaxmin = 0;
            volMaxTimeLine = 0;
            if(baseValue == 0){
                baseValue = realTimeData.m_fC;
            }
            realTimeData.m_nTotal = realTimeData.m_fC * realTimeData.m_nVolume;
            realTimeData.m_nAvprice = realTimeData.m_fC;
        }else{
            mAllVolume += realTimeData.m_nVolume;
            realTimeData.m_nTotal = realTimeData.m_fC * realTimeData.m_nVolume + getRealTimeData().get(getRealTimeData().size() - 1).m_nTotal;
            realTimeData.m_nAvprice = realTimeData.m_nTotal/mAllVolume;
        }
        realTimeData.m_nCha = realTimeData.m_fC - baseValue;
        realTimeData.m_nPercent = realTimeData.m_nCha/baseValue;
        double cha = realTimeData.m_fC - baseValue;
        if (Math.abs(cha) > permaxmin/1.2) {
            isMaxValueChange = true;
            perPerMaxmin = permaxmin;
            permaxmin = (float) Math.abs(cha)*(1.2f);//最大值和百分比都增加20%，防止内容顶在边框
        }
        perVolMaxTimeLine = volMaxTimeLine;
        volMaxTimeLine = Math.max(realTimeData.m_nVolume, volMaxTimeLine);
        getRealTimeData().add(realTimeData);
    }

    float perPerMaxmin = 0;
    float perVolMaxTimeLine = 0;
    public void removeLastData(){
        MarketTimeData realTimeData = getRealTimeData().get(getRealTimeData().size() - 1);
        mAllVolume -= realTimeData.m_nVolume;
        volMaxTimeLine = perVolMaxTimeLine;
        getRealTimeData().remove(getRealTimeData().size() - 1);
    }

    public synchronized ArrayList<MarketTimeData> getRealTimeData(){
        return realTimeDatas;
    }

    public void resetTimeData(){
        permaxmin = 0;
        baseValue = 0;
        getRealTimeData().clear();
    }

    public float getMin() {
        return (float)(baseValue - permaxmin);
    }

    public float getMax() {
        return (float)(baseValue + permaxmin);
    }

    public float getPercentMax() {
        return (float)((permaxmin) / baseValue);
    }

    public float getPercentMin() {
        return (float)-getPercentMax();
    }

    public float getVolMaxTime() {
        return volMaxTimeLine;
    }

    public ArrayList<MarketTimeData> getDatas() {
        return realTimeDatas;
    }

}
