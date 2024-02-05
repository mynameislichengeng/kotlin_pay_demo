package com.nar_unified_api.thirdpartydemo
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.nar_unified_api.thirdpartydemo.databinding.ActivityDccBinding
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class DccActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDccBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDccBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.app_label)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun UpDateUI() {
        val decimals = MainActivity.inParams[ThirdPartyApi.DECIMAL_SHIFT.key]?.toInt() ?: 2
        val currencyCode = MainActivity.inParams[ThirdPartyApi.CURRENCY_CODE.key]?.toInt() ?: 840
        val decimalDenominator = (BigDecimal(10).pow(decimals)).toDouble()

        val total =
            ((MainActivity.inParams[ThirdPartyApi.AMOUNT.key]?.toDouble()
                ?: 0.0) / decimalDenominator)


        val format: NumberFormat = NumberFormat.getCurrencyInstance().apply {
            minimumFractionDigits = decimals
            maximumFractionDigits = decimals
            currency =
                Currency.getAvailableCurrencies().first { it.numericCode == currencyCode }
                    ?: Currency.getInstance("USD")
        }
        // findViewById(R.id.totalLine) as t
        binding.dccTotal.text = "Total: " + format.format(total)
        binding.dccCurrencyCode.text = "Currency: $currencyCode"
        var pan = MainActivity.inParams[ThirdPartyApi.CARD_BIN.key]
        binding.dccPan.text = "PAN: " + pan

        binding.actDccSubmitBtn.setOnClickListener { returnDccResult((total * 1.3 * 100).toInt().toString(), currencyCode.toString(),ThirdPartyApiTranStatus.APPROVED) }
        binding.actDccCancelBtn.setOnClickListener { returnDccResult((total * 100).toInt().toString(), currencyCode.toString() ,ThirdPartyApiTranStatus.DECLINED) }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onWindowFocusChanged(hasFocus: Boolean) {

        //  supportFragmentManager()
        if (hasFocus) {

            Log.d("demo", "demo has focus")
            UpDateUI()
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    private fun returnDccResult(amount: String?,currency: String?,status: ThirdPartyApiTranStatus) {
        // Backup needed info to return response
        val caller = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_CALLER.key]
        val receiver = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_RESPONSE_RECEIVER.key]
        val requestType = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_REQUEST_TYPE.key]

        val decimalShift = MainActivity.inParams[ThirdPartyApi.DECIMAL_SHIFT.key]?.toIntOrNull()
        // Clear container data
        MainActivity.inParams.clear()
        amount?.let { MainActivity.inParams[ThirdPartyApi.AMOUNT.key] = it }
        currency?.let {
            MainActivity.inParams[ThirdPartyApi.CURRENCY_CODE.key] =
                if (status == ThirdPartyApiTranStatus.APPROVED) "124"
                else it
        }
        MainActivity.inParams[ThirdPartyApi.DECIMAL_SHIFT.key] = decimalShift.toString()
        MainActivity.inParams[ThirdPartyApi.TRAN_STATUS.key] = status.name
        if (requestType != null) {
            MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_REQUEST_TYPE.key] = requestType
        }
        if(caller != null && receiver != null) {
            val componentName = ComponentName(
                caller,
                receiver
            )
            MainActivity.inProgress = false

            Log.d("DccReceiver","Send response to NUA : $MainActivity.inParams")
            sendBroadcast(Intent(ThirdPartyApi.THIRD_PARTY_RESULT_CALLBACK.key).apply {
                component = componentName
                putExtra(ThirdPartyApi.PARAMETERS.key, MainActivity.inParams as HashMap<String, String>)
            })
            finish()
        }
    }
}

class DccReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("thirddemo", "dcc onReceive: received ")
        MainActivity.inParams =
            intent.getSerializableExtra(ThirdPartyApi.PARAMETERS.key) as HashMap<String, String>
        Log.d("DccReceiver", "Receive parameters from NUA : ${MainActivity.inParams}")
        MainActivity.inProgress = true
        context.startActivity(Intent().apply {
            setClassName(
                "com.nar_unified_api.thirdpartydemo",
                "com.nar_unified_api.thirdpartydemo.DccActivity"
            )
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}