package com.demo.nfc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.demo.nfc.utils.DataUtils
import com.demo.nfc.utils.resolveCardNumIntent
import kotlinx.android.synthetic.main.activity_read_card.*

class ReadCardActivity : BaseNfcActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_card)
    }


    override fun processIntent(intent: Intent) {
        val resolveCardNumIntent = resolveCardNumIntent(intent)
        tvCardNum.text = "内卡号: $resolveCardNumIntent"
    }
}