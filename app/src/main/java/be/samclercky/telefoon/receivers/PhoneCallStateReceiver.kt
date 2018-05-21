package be.samclercky.telefoon.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import be.samclercky.telefoon.R
import be.samclercky.telefoon.utils.buildNotification
import be.samclercky.telefoon.utils.getR
import be.samclercky.telefoon.utils.showToast

class PhoneCallStateReceiver: BroadcastReceiver() {
    private var telephonyManager: TelephonyManager? = null

    override fun onReceive(context: Context, intent: Intent) {
        val phoneListener = PhoneState(context)
        telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager?.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    fun onDestroy() = telephonyManager?.listen(null, PhoneStateListener.LISTEN_NONE)
}

class PhoneState(val context: Context) : PhoneStateListener() {

    companion object {
        private var ringing = false
        private var call_received = false
    }

    override fun onCallStateChanged(state: Int, incomingNumber: String?) {
        super.onCallStateChanged(state, incomingNumber)

        try {
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> ringing = true
                TelephonyManager.CALL_STATE_OFFHOOK -> call_received = true
                TelephonyManager.CALL_STATE_IDLE -> {
                    if (ringing == true && call_received == false) {
                        buildNotification(context,
                                title = R.string.not_missed_call.getR(context),
                                text = "$incomingNumber")
                        ringing = false
                    }
                    call_received = false
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace(System.err)
        }
    }
}