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
import com.nar_unified_api.thirdpartydemo.databinding.ActivityDiscountBinding
import com.nar_unified_api.thirdpartydemo.util.Constant
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DiscountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscountBinding


    private fun doDiscount(amount: String?) {
        // Backup needed info to return response
        val caller = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_CALLER.key]
        val receiver = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_RESPONSE_RECEIVER.key]
        val requestType = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_REQUEST_TYPE.key]

        val currency = MainActivity.inParams[ThirdPartyApi.CURRENCY_CODE.key]?.toIntOrNull()
        val decimalShift = MainActivity.inParams[ThirdPartyApi.DECIMAL_SHIFT.key]?.toIntOrNull()

        // Clear container data
        MainActivity.inParams.clear()

        if (amount != null) {

            MainActivity.inParams[ThirdPartyApi.DISCOUNTED_AMOUNT.key] = amount
            MainActivity.inParams[ThirdPartyApi.CURRENCY_CODE.key] = currency.toString()
            MainActivity.inParams[ThirdPartyApi.DECIMAL_SHIFT.key] = decimalShift.toString()
            if (requestType != null) {
                MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_REQUEST_TYPE.key] = requestType
            }
            MainActivity.inParams[ThirdPartyApi.TRAN_DATE.key] = SimpleDateFormat(
                Constant.DATE_FORMAT,
                Locale.getDefault()
            ).format(Calendar.getInstance().time)
            MainActivity.inParams[ThirdPartyApi.TRAN_TIME.key] = SimpleDateFormat(
                Constant.TIME_FORMAT,
                Locale.getDefault()
            ).format(Calendar.getInstance().time)
        }
        if(caller != null && receiver != null) {
            val componentName = ComponentName(
                caller,
                receiver
            )
            MainActivity.inProgress = false

            Log.d("DiscountReceiver","Send response to NUA : $MainActivity.inParams")
            sendBroadcast(Intent(ThirdPartyApi.THIRD_PARTY_RESULT_CALLBACK.key).apply {
                component = componentName
                putExtra(ThirdPartyApi.PARAMETERS.key, MainActivity.inParams as HashMap<String, String>)
            })
            MainActivity.inParams.clear()
            finishAffinity()
        }

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
        binding.total.text = "Total: " + format.format(total)


        val discount = 0.10
        val afterDiscount = total * (1.0 - discount)

        binding.discount1.apply {
            text = "Apply 10% " + format.format(afterDiscount)
            setOnClickListener {
                doDiscount((afterDiscount * 100).toInt().toString())
            }
        }
        val discount2Amt = 0.20
        binding.discount2.apply {
            text = "Apply 20% " + format.format(total * (1.00 - discount2Amt))
            setOnClickListener {
                doDiscount(((total * (1.00 - discount2Amt)) * 100).toInt().toString())
            }
        }
        binding.quitDiscount.apply {
            text = "No Discount"
            setOnClickListener {
                doDiscount((total * 100).toInt().toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onWindowFocusChanged(hasFocus: Boolean) {

        //  supportFragmentManager()
        if (hasFocus) {

            Log.d("demo", "demo has focus")
            UpDateUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // UpDateUI()
        binding = ActivityDiscountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.app_label)


    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onPause() {
        super.onPause()
        finishAffinity()
    }

}

class DiscountReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("thirddemo", "discount onReceive: received ")
        MainActivity.inParams =
            intent.getSerializableExtra(ThirdPartyApi.PARAMETERS.key) as HashMap<String, String>
        Log.d("DiscountReceiver", "Receive parameters from NUA : ${MainActivity.inParams}")
        MainActivity.inProgress = true
        context.startActivity(Intent( context, DiscountActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP))
    }
}