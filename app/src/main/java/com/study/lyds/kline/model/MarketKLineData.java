package com.study.lyds.kline.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/8.
 */
public class MarketKLineData extends DataSupport implements Serializable{
    public double m_fH;
    public double m_fL;
    public double m_fO;
    public double m_fC;
    public double m_fPreC;
    public int m_nVolume;
    public int m_nDimension;
    public String m_szInstrumentID;
    public String m_szDate;
    // K线开始时间（小时:分钟：秒，譬如09:15:15，等于 9 * 3600 + 15 * 60 + 15）
    public int m_nStartTime;

    @Override
    public String toString() {
        return "MarketHistoryKLineData{" +
                "m_fH=" + m_fH +
                ", m_fL=" + m_fL +
                ", m_fO=" + m_fO +
                ", m_fC=" + m_fC +
                ", m_fPreC=" + m_fPreC +
                ", m_nVolume=" + m_nVolume +
                ", m_nDimension=" + m_nDimension +
                ", m_szInstrumentID='" + m_szInstrumentID + '\'' +
                ", m_szDate='" + m_szDate + '\'' +
                ", m_nStartTime=" + m_nStartTime +
                '}';
    }
}
