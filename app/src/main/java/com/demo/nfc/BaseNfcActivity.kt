package com.demo.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.demo.nfc.utils.NfcUtils

/**
 * @description:
 * @date: 11/20/20 10:17 AM
 * @author: funaihui
 * @email: wizarddev@163.com
 **/
abstract class BaseNfcActivity : AppCompatActivity() {

    lateinit var mNfcUtils: NfcUtils

    override fun onResume() {
        super.onResume()
        //设定intentfilter和tech-list。如果两个都为null就代表优先接收任何形式的TAG action。也就是说系统会主动发TAG intent。
        mNfcUtils = NfcUtils(this)
        if (mNfcUtils.mNfcAdapter != null) {
            mNfcUtils.mNfcAdapter?.enableForegroundDispatch(
                this,
                mNfcUtils.mPendingIntent,
                mNfcUtils.mIntentFilter,
                mNfcUtils.mTechList
            )
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        processIntent(intent)

    }

    abstract fun processIntent(intent: Intent)

    override fun onPause() {
        super.onPause()
        if (mNfcUtils.mNfcAdapter != null) {
            mNfcUtils.mNfcAdapter!!.disableForegroundDispatch(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mNfcUtils.mNfcAdapter = null
    }


}