package com.nar_unified_api.thirdpartydemo
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.nar_unified_api.thirdpartydemo.databinding.ActivityEreceiptBinding
import java.util.*

class EReceiptActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEreceiptBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEreceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.app_label)
        binding.submitBtn.setOnClickListener { returnEReceiptResult(true) }
        binding.cancelBtn.setOnClickListener { returnEReceiptResult(false) }
        var receiptData = ""
        MainActivity.inParams.forEach {
            if (it.value.isNotEmpty()) {
                receiptData += it.key + "=" + it.value + "\n"
            }
        }
        binding.eReceiptView.movementMethod = ScrollingMovementMethod()
        binding.eReceiptView.text = receiptData
    }

    override fun onPause() {
        super.onPause()
        finishAffinity()
    }

    private fun returnEReceiptResult(approved: Boolean) {
        // Backup needed info to return response
        val caller = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_CALLER.key]
        val receiver = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_RESPONSE_RECEIVER.key]
        val requestType = MainActivity.inParams[ThirdPartyApi.THIRD_PARTY_REQUEST_TYPE.key]

        // Clear container data
        MainActivity.inParams.clear()

        MainActivity.inParams[ThirdPartyApi.TRAN_STATUS.key] = if (approved) {
                ThirdPartyApiTranStatus.APPROVED.name
        } else {
                ThirdPartyApiTranStatus.CANCELLED.name
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

            Log.d("EReceiptReceiver","Send response to NUA : $MainActivity.inParams")
            sendBroadcast(Intent(ThirdPartyApi.THIRD_PARTY_RESULT_CALLBACK.key).apply {
                component = componentName
                putExtra(ThirdPartyApi.PARAMETERS.key, MainActivity.inParams as HashMap<String, String>)
            })
            finishAffinity()
        }
    }
}

class EReceiptReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("thirddemo", "e-receipt onReceive: received ")
        MainActivity.inParams =
            intent.getSerializableExtra(ThirdPartyApi.PARAMETERS.key) as HashMap<String, String>
        Log.d("EReceiptReceiver", "Receive parameters from NUA : ${MainActivity.inParams}")
        MainActivity.inProgress = true
        context.startActivity(Intent().apply {
            setClassName(
                "com.nar_unified_api.thirdpartydemo",
                "com.nar_unified_api.thirdpartydemo.EReceiptActivity"
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}