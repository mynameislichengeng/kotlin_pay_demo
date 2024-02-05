package com.nar_unified_api.thirdpartydemo

import android.content.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.nar_unified_api.thirdpartydemo.databinding.ActivityPaymentBinding
import com.nar_unified_api.thirdpartydemo.util.Constant
import java.text.SimpleDateFormat
import java.util.*

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.app_label)
        binding.actPaymentFullBtn.setOnClickListener { returnPaymentResult(ThirdPartyApiTranStatus.APPROVED) }
        binding.actPaymentPartialBtn.setOnClickListener { returnPaymentResult(ThirdPartyApiTranStatus.PARTIAL_APPROVAL) }
        binding.actPaymentContinueBtn.setOnClickListener { returnPaymentResult(ThirdPartyApiTranStatus.CANCELLED) }
        binding.actPaymentDeclineBtn.setOnClickListener { returnPaymentResult(ThirdPartyApiTranStatus.DECLINED) }

        binding.editTextNumberDecimal.setText(MainActivity.inParams[ThirdPartyApi.AMOUNT.key]?:"0")

    }

    override fun onPause() {
        super.onPause()
        finishAffinity()
    }

    private fun returnPaymentResult(tranStatus: ThirdPartyApiTranStatus) {

        // Backup needed info to return response
        val caller = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_CALLER.key]
        val receiver = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_RESPONSE_RECEIVER.key]
        val requestType = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_REQUEST_TYPE.key]

        val currency = MainActivity.inParams[ThirdPartyApi.CURRENCY_CODE.key]?.toIntOrNull()
        val decimalShift = MainActivity.inParams[ThirdPartyApi.DECIMAL_SHIFT.key]?.toIntOrNull()
        val amount = MainActivity.inParams[ThirdPartyApi.AMOUNT.key]?:"0"
        // Clear container data
        MainActivity.inParams.clear()

        MainActivity.inParams[ThirdPartyApi.TRAN_STATUS.key] = tranStatus.name
        MainActivity.inParams[ThirdPartyApi.TRAN_DATE.key] = SimpleDateFormat(
            Constant.DATE_FORMAT,
            Locale.getDefault()
        ).format(Calendar.getInstance().time)
        MainActivity.inParams[ThirdPartyApi.TRAN_TIME.key] = SimpleDateFormat(
            Constant.TIME_FORMAT,
            Locale.getDefault()
        ).format(Calendar.getInstance().time)
        if(tranStatus != ThirdPartyApiTranStatus.CANCELLED) {
            MainActivity.inParams[ThirdPartyApi.ACCOUNT_NUMBER.key] = Constant.ACCOUNT_NUMBER_DEMO
            MainActivity.inParams[ThirdPartyApi.APPROVAL_CODE.key] = Constant.APPROVAL_CODE_DEMO
            MainActivity.inParams[ThirdPartyApi.CARD_TYPE.key] = ThirdPartyCardType.GIFT_CARD.name
            MainActivity.inParams[ThirdPartyApi.ENTRY_MODE.key] = ThirdPartyEntryMode.SWIPE.name
            MainActivity.inParams[ThirdPartyApi.INVOICE.key] = "12345"
            MainActivity.inParams[ThirdPartyApi.RESPONSE_CODE.key] = Constant.RESPONSE_CODE_DEMO
            MainActivity.inParams[ThirdPartyApi.RESPONSE_TEXT.key] = when(tranStatus) {
                ThirdPartyApiTranStatus.APPROVED -> Constant.RESPONSE_TEXT_APPROVED_DEMO
                ThirdPartyApiTranStatus.PARTIAL_APPROVAL -> Constant.RESPONSE_TEXT_PARTIAL_APPROVED_DEMO
                ThirdPartyApiTranStatus.DECLINED -> Constant.RESPONSE_TEXT_DECLINED_DEMO
                else -> ""
            }
        }
        if (tranStatus == ThirdPartyApiTranStatus.PARTIAL_APPROVAL || tranStatus == ThirdPartyApiTranStatus.APPROVED) {
            val amt = findViewById<TextView>(R.id.editTextNumberDecimal)?.text.toString()
            MainActivity.inParams[ThirdPartyApi.AMOUNT_AUTHORIZED.key] = amt
            MainActivity.inParams[ThirdPartyApi.TOTAL_AMOUNT.key] = amount
            MainActivity.inParams[ThirdPartyApi.CURRENCY_CODE.key] = currency.toString()
            MainActivity.inParams[ThirdPartyApi.DECIMAL_SHIFT.key] = decimalShift.toString()
        }
        if (requestType != null) {
            MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_REQUEST_TYPE.key] = requestType
        }
        if(caller != null && receiver != null) {
            val componentName = ComponentName(
                caller,
                receiver
            )
            MainActivity.inProgress = false

            Log.d("PaymentReceiver","Send response to NUA : $MainActivity.inParams")
            sendBroadcast(Intent(ThirdPartyApi.THIRD_PARTY_RESULT_CALLBACK.key).apply {
                component = componentName
                putExtra(ThirdPartyApi.PARAMETERS.key, MainActivity.inParams as HashMap<String, String>)
            })
            finishAffinity()
        }

    }
}

class PaymentReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("thirddemo", "payment onReceive: received ")
        MainActivity.inParams =
            intent.getSerializableExtra(ThirdPartyApi.PARAMETERS.key) as HashMap<String, String>
        Log.d("PaymentReceiver", "Receive parameters from NUA : ${MainActivity.inParams}")
        MainActivity.inProgress = true
        context.startActivity(Intent().apply {
            setClassName(
                "com.nar_unified_api.thirdpartydemo",
                "com.nar_unified_api.thirdpartydemo.PaymentActivity"
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}