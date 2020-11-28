package com.demo.nfc.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.nfc.*
import android.nfc.tech.MifareClassic
import android.nfc.tech.Ndef
import android.nfc.tech.NfcA
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.IOException
import java.io.UnsupportedEncodingException

/**
 * @description:
 * @date: 11/19/20 2:16 PM
 * @author: funaihui
 * @email: wizarddev@163.com
 */
class NfcUtils(activity: Activity) {
    //nfc
    var mNfcAdapter: NfcAdapter? = null
    var mIntentFilter: Array<IntentFilter>? = null
    var mPendingIntent: PendingIntent? = null
    var mTechList: Array<Array<String>>? = null

    /**
     * 检查NFC是否打开
     */
    fun NfcCheck(activity: Activity): NfcAdapter? {
        val mNfcAdapter = NfcAdapter.getDefaultAdapter(activity)
        if (mNfcAdapter == null) {
            Toast.makeText(activity, "设备不支持NFC功能!", Toast.LENGTH_SHORT).show()
            return null
        } else {
            if (!mNfcAdapter.isEnabled) {
                IsToSet(activity)
            } else {
                Toast.makeText(activity, "NFC功能已打开!", Toast.LENGTH_SHORT).show()
            }
        }
        return mNfcAdapter
    }

    /**
     * 初始化nfc设置
     */
    fun NfcInit(activity: Activity) {
        val intent = Intent(activity, activity.javaClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        mPendingIntent = PendingIntent.getActivity(activity, 0, intent, 0)
        //做一个IntentFilter过滤你想要的action 这里过滤的是ndef
        val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        //如果你对action的定义有更高的要求，比如data的要求，你可以使用如下的代码来定义intentFilter
        //        IntentFilter filter2 = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        //        try {
        //            filter.addDataType("*/*");
        //        } catch (IntentFilter.MalformedMimeTypeException e) {
        //            e.printStackTrace();
        //        }
        //        mIntentFilter = new IntentFilter[]{filter, filter2};
        //        mTechList = null;
        try {
            filter.addDataType("*/*")
        } catch (e: MalformedMimeTypeException) {
            e.printStackTrace()
        }
        mTechList = arrayOf(
            arrayOf(
                MifareClassic::class.java.name
            ), arrayOf(NfcA::class.java.name)
        )
        //生成intentFilter
        mIntentFilter = arrayOf(filter)
    }

    /**
     * 读取NFC的数据
     */
    @Throws(UnsupportedEncodingException::class)
    fun readNFCFromTag(intent: Intent): String {
        val rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        if (rawArray != null) {
            val mNdefMsg = rawArray[0] as NdefMessage
            val mNdefRecord = mNdefMsg.records[0]
            if (mNdefRecord != null) {
                return String(mNdefRecord.payload)
            }
        }
        return ""
    }

    /**
     * 往nfc写入数据
     */
    @Throws(IOException::class, FormatException::class)
    fun writeNFCToTag(data: String?, intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        val ndef = Ndef.get(tag)
        ndef.connect()
        var ndefRecord: NdefRecord? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ndefRecord = NdefRecord.createTextRecord(null, data)
        }
        val records = arrayOf(ndefRecord)
        val ndefMessage = NdefMessage(records)
        ndef.writeNdefMessage(ndefMessage)
    }

    /**
     * 读取nfcID
     */
    @Throws(UnsupportedEncodingException::class)
    fun readNFCId(intent: Intent): String {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        return ByteArrayToHexString(tag.id)
    }

    /**
     * 将字节数组转换为字符串
     */
    private fun ByteArrayToHexString(inarray: ByteArray): String {
        var i: Int
        var j: Int
        var `in`: Int
        val hex = arrayOf(
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "A",
            "B",
            "C",
            "D",
            "E",
            "F"
        )
        var out = ""
        j = 0
        while (j < inarray.size) {
            `in` = inarray[j].toInt() and 0xff
            i = `in` shr 4 and 0x0f
            out += hex[i]
            i = `in` and 0x0f
            out += hex[i]
            ++j
        }
        return out
    }

    private fun IsToSet(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("是否跳转到设置页面打开NFC功能")
        //        builder.setTitle("提示");
        builder.setPositiveButton("确认") { dialog, which ->
            goToSet(activity)
            dialog.dismiss()
        }
        builder.setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
        builder.create().show()
    }

    private fun goToSet(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            // 进入设置系统应用权限界面
            val intent = Intent(Settings.ACTION_SETTINGS)
            activity.startActivity(intent)
            return
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 运行系统在5.x环境使用
            // 进入设置系统应用权限界面
            val intent = Intent(Settings.ACTION_SETTINGS)
            activity.startActivity(intent)
            return
        }
    }

    init {
        mNfcAdapter = NfcCheck(activity)
        NfcInit(activity)
    }
}