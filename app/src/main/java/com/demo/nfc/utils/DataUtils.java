package com.demo.nfc.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {
    //-------------------------------------------------------
    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    public static int isOdd(int num) {
        return num & 1;
    }

    //-------------------------------------------------------
    //Hex字符串转int
    public static int HexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }
    //-------------------------------------------------------
    //Hex字符串转int
    public static long hexToLong(String inHex) {
        return Long.parseLong(inHex, 16);
    }

    public static String IntToHex(int intHex) {
        return Integer.toHexString(intHex);
    }

    //-------------------------------------------------------
    //Hex字符串转byte
    public static byte HexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    //-------------------------------------------------------
    //1字节转2个Hex字符
    public static String Byte2Hex(Byte inByte) {
        return String.format("%02x", new Object[]{inByte}).toUpperCase();
    }

    //-------------------------------------------------------
    //字节数组转转hex字符串
    public static String byteToString(byte[] inBytArr) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : inBytArr) {
            strBuilder.append(Byte2Hex(valueOf));
//            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    //字节数组转转hex字符串，可选长度
    public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    //转hex字符串转字节数组
    public static byte[] HexToByteArr(String inHex) {
        byte[] result;
        int hexlen = inHex.length();
        if (isOdd(hexlen) == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    //字节数组转转hex字符串
    public static String ByteArrToHex(byte[] inBytArr) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : inBytArr) {
            strBuilder.append(Byte2Hex(Byte.valueOf(valueOf)));
//            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }


    public static byte[] string2byteArr(String inHex) {
        int hexlen = inHex.length()/2;

        byte[] result = new byte[hexlen];


//        if (isOdd(hexlen) == 1) {
//            hexlen++;
//            result = new byte[(hexlen / 2)];
//            inHex = "0" + inHex;
//        } else {
//            result = new byte[(hexlen / 2)];
//        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            byte[] bytes = inHex.substring(i, i + 2).getBytes();
            j++;
        }
        return result;
    }

    /**
     * 按照指定长度切割字符串
     *
     * @param inputString 需要切割的源字符串
     * @param length      指定的长度
     * @return
     */
    public static List<String> getDivLines(String inputString, int length) {
        List<String> divList = new ArrayList<>();
        int remainder = (inputString.length()) % length;
        // 一共要分割成几段
        int number = (int) Math.floor((inputString.length()) / length);
        for (int index = 0; index < number; index++) {
            String childStr = inputString.substring(index * length, (index + 1) * length);
            divList.add(childStr);
        }
        if (remainder > 0) {
            String cStr = inputString.substring(number * length, inputString.length());
            divList.add(cStr);
        }
        return divList;
    }

    /**
     * 计算长度，两个字节长度
     *
     * @param val value
     * @return 结果
     */
    public static String twoByte(String val) {
        if (val.length() > 4) {
            val = val.substring(0, 4);
        } else {
            int l = 4 - val.length();
            for (int i = 0; i < l; i++) {
                val = "0" + val;
            }
        }
        return val;
    }


    public static int decodeHEX(String hexs) {
        BigInteger bigint = new BigInteger(hexs, 16);
        int numb = bigint.intValue();
        return numb;
    }


    //-------------------------------------------------------
    //字节数组转转hex字符串，可选长度
    public static String byteArrToInt(byte[] inBytArr, int offset, int byteCount) {

        long decodeHEX = hexToLong(ByteArrToHex(inBytArr, offset, byteCount));
        return decodeHEX + "";
    }

    //-------------------------------------------------------
    //字节数组转转hex字符串，可选长度
    public static String byteArrToInt(byte[] inBytArr) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : inBytArr) {
            String byte2Hex = Byte2Hex(valueOf);
            int decodeHEX = decodeHEX(byte2Hex);
            strBuilder.append(decodeHEX);
        }
        return strBuilder.toString();
    }


    public static String asciiStringToString(String content) {
        String result = "";
        int length = content.length() / 2;
        for (int i = 0; i < length; i++) {
            String c = content.substring(i * 2, i * 2 + 2);
            int a = hexStringToAlgorism(c);
            char b = (char) a;
            String d = String.valueOf(b);
            result += d;
        }
        return result;
    }

    /**
     * 十六进制字符串装十进制
     *
     * @param hex
     *            十六进制字符串
     * @return 十进制数值
     */
    public static int hexStringToAlgorism(String hex) {
        hex = hex.toUpperCase();
        int max = hex.length();
        int result = 0;
        for (int i = max; i > 0; i--) {
            char c = hex.charAt(i - 1);
            int algorism = 0;
            if (c >= '0' && c <= '9') {
                algorism = c - '0';
            } else {
                algorism = c - 55;
            }
            result += Math.pow(16, max - i) * algorism;
        }
        return result;
    }
}
