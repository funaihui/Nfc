package com.demo.nfc.utils

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.NfcA
import java.io.IOException


/**
 * @description:
 * @date: 2020/6/12 8:45 AM
 * @author: funaihui
 * @email: wizarddev@163.com
 **/


val CARD_KEY_15 = byteArrayOf(
    0xff.toByte(),
    0xff.toByte(),
    0xff.toByte(),
    0xff.toByte(),
    0xff.toByte(),
    0xff.toByte()
)
//解析实体卡号
fun resolveCardNumIntent(intent: Intent?): String {
    if (intent == null) {
        return ""
    }
    //拿来装读取出来的数据，key代表扇区数，后面list存放四个块的内容
    //intent就是onNewIntent方法返回的那个intent
    val tag: Tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
    val mfc = MifareClassic.get(tag)
    //如果当前IC卡不是这个格式的mfc就会为空
    if (null != mfc) {
        try {
            if (!mfc.isConnected) {
                //链接NFC
                mfc.connect()
            }
            //验证扇区密码，否则会报错（链接失败错误）
            val isOpen = mfc.authenticateSectorWithKeyA(0 , CARD_KEY_15)
            if (isOpen) {
                //获取扇区第一个块对应芯片存储器的位置
                //（我是这样理解的，因为第0扇区的这个值是4而不是0）
                val bIndex = mfc.sectorToBlock(0)
                val data = mfc.readBlock(bIndex)
                val byteArrToInt = DataUtils.byteArrToInt(data, 0, 4)
//                LogUtils.print("卡号： $byteArrToInt")
                return byteArrToInt
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        } finally {
            try {
                mfc.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    return ""
}
