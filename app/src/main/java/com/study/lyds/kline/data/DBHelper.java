package com.study.lyds.kline.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.study.lyds.kline.model.MarketKLineData;
import com.study.lyds.kline.model.MarketTimeData;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class DBHelper {
    private KLineData kLineData = new KLineData();
    private KTimeData kTimeData = new KTimeData();
    Context context;

    //数据库的名称
    private String DB_NAME = "kline.db";
    //数据库的地址
    private String DB_PATH = "/data/data/com.study.lyds.kline/databases/";

    private static DBHelper dbHelper;

    public static DBHelper getDbHelper(Context context) {
        if(dbHelper == null){
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    private DBHelper(Context context){
        this.context = context;
        initFile();
        readDBChartData();
    }

    public KLineData getkLineData() {
        return kLineData;
    }

    public KTimeData getkTimeData() {
        return kTimeData;
    }

    private void readDBChartData(){
        kLineData.addKLineDatas(DataSupport.findAll(MarketKLineData.class));
        List<MarketTimeData> marketTimeDatas = DataSupport.findAll(MarketTimeData.class);
        for(MarketTimeData marketTimeData : marketTimeDatas){
            kTimeData.addARealTimeData(marketTimeData,"HSI");
        }
    }

    private void initFile(){
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            InputStream is = context.getAssets().open(DB_NAME);
            OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
            byte[] buffer = new byte[1024];//用来复制文件
            int length;//保存已经复制的长度
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}