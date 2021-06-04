package com.ondev.wallpaper.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.widget.Toast
import com.ondev.wallpaper.MainAplication
import com.ondev.wallpaper.preferences.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SMSReceiver : BroadcastReceiver() {
    private val pduType = "pdus"
    private val prefixActivate = "ACTIVATE"
    private lateinit var userPref: UserPreferencesRepository


    override fun onReceive(context: Context, intent: Intent) {
        userPref = (context.applicationContext as MainAplication).userPrefsRepo
        val bundle = intent.extras
        val msgs: Array<SmsMessage?>
        val format = bundle!!.getString("format")
        val pdus = bundle!![pduType] as Array<Any>?
        when {
            !pdus.isNullOrEmpty() -> {
                msgs = arrayOfNulls(pdus.size)
                msgs.indices.forEach { i ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
                    } else {
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    }
                    processMessageAndActivate(context, msgs[i])
                }
            }
        }
    }

    private fun processMessageAndActivate(ctx: Context, smsMessage: SmsMessage?) {
        val active1 =
            smsMessage!!.messageBody.contains("Usted ha transferido 10 CUP al numero 54074127")
        val active2 =
            smsMessage!!.originatingAddress!!.contains("54074127") && smsMessage!!.messageBody.startsWith(
                prefixActivate
            )
        if (active1 || active2) {
            GlobalScope.launch(Dispatchers.IO) {
                userPref.updateUserPay(true)
            }

            Toast.makeText(ctx, "Ya puedes buscar los fondos en internet", Toast.LENGTH_SHORT)
                .show()

        }
    }
}
