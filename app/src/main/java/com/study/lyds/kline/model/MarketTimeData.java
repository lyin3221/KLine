package com.study.lyds.kline.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/8.
 */
public class MarketTimeData extends DataSupport implements Serializable{
    // 合约ID
    public String m_szInstrumentID;//16
    // 更新开始时间（小时:分钟：秒，譬如09:15:15，等于 9 * 3600 + 15 * 60 + 15）
    public int m_nUpdateTime;
    // 收盘
    public double m_fC;
    // 成交量
    public int m_nVolume;
    // 维度
    public int m_nDimension;

    public double m_nAvprice;

    public double m_nCha;

    public double m_nPercent;

    public double m_nTotal;

    @Override
    public String toString() {
        return "MarketHistoryRealTimeData{" +
                "m_szInstrumentID='" + m_szInstrumentID + '\'' +
                ", m_nUpdateTime=" + m_nUpdateTime +
                ", m_fC=" + m_fC +
                ", m_nVolume=" + m_nVolume +
                ", m_nDimension=" + m_nDimension +
                '}';
    }
}
