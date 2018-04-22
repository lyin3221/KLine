package com.study.lyds.kline.utils;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/5/23.
 */
public class ClientBaseUtils {

    public static char byteToChar(byte[] b) {
        char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
        return c;
    }

    /**
     * bytes转int
     * @param b
     * @param off
     * @return
     */
    public static int lBytesToInt( byte [] b,int off) {
        int  s =  0 ;
        for  ( int  i =  0 ; i <  3 ; i++) {
            if  (b[ off+3 -i] >=  0 ) {
                s = s + b[off + 3 -i];
            } else  {
                s = s + 256  + b[ off + 3 -i];
            }
            s = s * 256 ;
        }
        if  (b[ off + 0 ] >=  0 ) {
            s = s + b[off + 0 ];
        } else  {
            s = s + 256  + b[ off + 0 ];
        }
        return  s;
    }

    /**
     * 低字节数组转换为float
     * @param b byte[]
     * @return float
     */
    public static float lBytesToFloat(byte[] b,int off) {
        int i = 0;
        Float F = new Float(0.0);
        i = ((((b[off+3]&0xff)<<8 | (b[off+2]&0xff))<<8) | (b[off+1]&0xff))<<8 | (b[off+0]&0xff);
        return F.intBitsToFloat(i);
    }

    /**
     * 高字节数组转换为float
     * @param b byte[]
     * @return float
     */
    public static float hBytesToFloat(byte[] b,int off) {
        int i = 0;
        Float F = new Float(0.0);
        i = ((((b[off+0]&0xff)<<8 | (b[off+1]&0xff))<<8) | (b[off+2]&0xff))<<8 | (b[off+3]&0xff);
        return F.intBitsToFloat(i);
    }

    public static long bytesToLong(byte[] readBuffer,int off) {
        return ((((long)readBuffer[off+7] << 56) +
                ((long)(readBuffer[off+6] & 255) << 48) +
                ((long)(readBuffer[off+5] & 255) << 40) +
                ((long)(readBuffer[off+4] & 255) << 32) +
                ((long)(readBuffer[off+3] & 255) << 24) +
                ((readBuffer[off+2] & 255) << 16) +
                ((readBuffer[off+1] & 255) <<  8) +
                ((readBuffer[off+0] & 255) <<  0))
        );
    }

    /**
     * byte[8]转long
     * @param b
     * @param offset b的偏移量
     * @return
     */
    public static long lByteToLong(byte[] b, int offset) {
        return ((((long) b[offset + 0] & 0xff) << 56)
                | (((long) b[offset + 1] & 0xff) << 48)
                | (((long) b[offset + 2] & 0xff) << 40)
                | (((long) b[offset + 3] & 0xff) << 32)

                | (((long) b[offset + 4] & 0xff) << 24)
                | (((long) b[offset + 5] & 0xff) << 16)
                | (((long) b[offset + 6] & 0xff) << 8)
                | (((long) b[offset + 7] & 0xff) << 0));
    }

    /**
     * long转byte[8]
     * @param a
     * @param b
     * @param offset b的偏移量
     */
    public static void longToByte(long a, byte[] b, int offset) {
        b[offset + 0] = (byte) (a >> 56);
        b[offset + 1] = (byte) (a >> 48);
        b[offset + 2] = (byte) (a >> 40);
        b[offset + 3] = (byte) (a >> 32);

        b[offset + 4] = (byte) (a >> 24);
        b[offset + 5] = (byte) (a >> 16);
        b[offset + 6] = (byte) (a >> 8);
        b[offset + 7] = (byte) (a);
    }

    /**
     * @功能 字节的转换与短整型
     * @param //两位的字节数组
     * @return 短整型
     */
    public static short lByteToShort(byte[] b,int off) {
        short s = 0;
        short s0 = (short) (b[0+off] & 0xff);// 最低位
        short s1 = (short) (b[1+off] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    /**
     * 将byte[2]转换成short
     * @param b
     * @param offset
     * @return
     */
    public static short hByteToShort(byte[] b, int offset){
        return (short) (((b[offset] & 0xff) << 8) | (b[offset+1] & 0xff));
    }

    public static double bytesToDouble(byte[] readBuffer,int off) {
        return Double.longBitsToDouble((((long)readBuffer[off+7] << 56) +
                ((long)(readBuffer[off+6] & 255) << 48) +
                ((long)(readBuffer[off+5] & 255) << 40) +
                ((long)(readBuffer[off+4] & 255) << 32) +
                ((long)(readBuffer[off+3] & 255) << 24) +
                ((readBuffer[off+2] & 255) << 16) +
                ((readBuffer[off+1] & 255) <<  8) +
                ((readBuffer[off+0] & 255) <<  0))
        );
    }

    public static void  doubleToBytes(byte[] writeBuffer,int off, double d) {
        long v = Double.doubleToLongBits(d);
        writeBuffer[off+7] = (byte)(v >>> 56);
        writeBuffer[off+6] = (byte)(v >>> 48);
        writeBuffer[off+5] = (byte)(v >>> 40);
        writeBuffer[off+4] = (byte)(v >>> 32);
        writeBuffer[off+3] = (byte)(v >>> 24);
        writeBuffer[off+2] = (byte)(v >>> 16);
        writeBuffer[off+1] = (byte)(v >>>  8);
        writeBuffer[off+0] = (byte)(v >>>  0);
        return ;
    }

    public static void toLH(byte[]buf, int off, int n) {
        buf[0+off] = (byte) (n & 0xff);
        buf[1+off] = (byte) (n >> 8 & 0xff);
        buf[2+off] = (byte) (n >> 16 & 0xff);
        buf[3+off] = (byte) (n >> 24 & 0xff);
        return ;
    }

    public static void writeString_CH(byte[] target,int off,String str,int nLen) {
        if(nLen > str.length()) {
            try {
                System.arraycopy(str.getBytes("GBK"),0,target,off,str.getBytes("GBK").length);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            target[str.getBytes().length + off] = 0;
        }
        else {
            System.arraycopy(str.getBytes(),0,target,off,nLen);
            target[nLen + off] = 0;
        }
    }

    public static void writeString(byte[] target,int off,String str,int nLen) {
        if(nLen > str.length()) {
            System.arraycopy(str.getBytes(),0,target,off,str.getBytes().length);
            target[str.getBytes().length + off] = 0;
        }
        else {
            System.arraycopy(str.getBytes(),0,target,off,nLen);
            target[nLen + off] = 0;
        }
    }

    public static String readString(byte[] src,int off,int nMaxLength) {
        int nCurSize = nMaxLength;
        for(int i = 0; i < nMaxLength; i++) {
            if(src[i+off] == 0) {
                nCurSize = i;
                break;
            }
        }
        byte[] bszMsg = new byte[nCurSize];
        System.arraycopy(src, off, bszMsg, 0, nCurSize);
        String szMsg = null;
        try {
            szMsg = new String(bszMsg,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return szMsg;
    }

    public static String readStringFixedLen(byte[] src,int off,int nMaxLength,int nCurSize) {
        byte[] bszMsg = new byte[nCurSize];
        System.arraycopy(src, off, bszMsg, 0, nCurSize);
        return new String(bszMsg);
    }

    public static String readStringSplit(byte[] src,int off,int nMaxLength) {
        byte[] bszMsg = new byte[nMaxLength];
        System.arraycopy(src, off, bszMsg, 0, nMaxLength);
        String string = new String(bszMsg);
        return string.split("\0")[0];
    }

    //将发送的byte转16进制字符串，可用来对比PC内存数据
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    public static int GET_PACKET_LEN(int Flag){
        return Flag & 0x007FFFFF;
    }

    public static int  GET_PACKET_COUNT(int Flag){
        return Flag >> 24;
    }

    public static int SET_PACKET_END(int Flag){
        return Flag | 0x00800000;
    }

    public static int SET_PACKET_LEN(int Flag,int Len){
        return (Flag & 0xFF800000) | (Len);
    }

}
