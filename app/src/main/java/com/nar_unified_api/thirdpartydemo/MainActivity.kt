package com.nar_unified_api.thirdpartydemo

import android.content.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.nar_unified_api.thirdpartydemo.MainActivity.amounts.result
import com.nar_unified_api.thirdpartydemo.databinding.ActivityMainBinding
import kotlinx.serialization.encodeToString

import kotlinx.serialization.json.Json

import java.util.HashMap


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var PACKAGE_NAME: String = "com.nar_unified_api.thirdpartydemo"

    public companion object amounts {
        var result: String? = null
        var inProgress = false //false

        //var amount:String
        //hard coded defaults so UI will start with default values
        var inParams = mutableMapOf<String, String>().also {
            it.put(ThirdPartyApi.DECIMAL_SHIFT.key, "2")
            it.put(ThirdPartyApi.AMOUNT.key, "0")
            it.put(ThirdPartyApi.CURRENCY_CODE.key, "840")
        }
    }

    fun registerAppForDiscount(unregister: Boolean = false) {
        val registerRequest = ThirdPartyRegisterRequest(
            registrationType = if (unregister) {
                RegistrationType.UNREGISTER.name
            } else {
                RegistrationType.REGISTER.name
            },
            thirdPartyEventType = ExtThirdPartyEventType.BEFORE_TRANSACTION.toString(),
            transactionsSupported = listOf(
                ExtThirdPartyTransaction.SALE.toString(),
                ExtThirdPartyTransaction.REFUND.toString(),
                ExtThirdPartyTransaction.FORCE_SALE.toString(),
                ExtThirdPartyTransaction.PREAUTH.toString(),
                ExtThirdPartyTransaction.PREAUTH_COMPLETION.toString(),
                ExtThirdPartyTransaction.VOID.toString(),
                ExtThirdPartyTransaction.INCREMENTAL_AUTH.toString(),
                ExtThirdPartyTransaction.FORCE_PREAUTH.toString()
            ),
            thirdPartyPackage = PACKAGE_NAME,
            thirdPartyReceiver = PACKAGE_NAME + ".DiscountReceiver",
            registerResponseReceiver = PACKAGE_NAME + ThirdPartyIdentifiers.REGISTER_RESPONSE_RECEIVER.value,
            thirdPartyAction = PACKAGE_NAME + ".ACTION_APPLY_BEFORE_TRANSACTION",
            buttonTextEn = "Discount Demo",
            buttonTextFr = "Remise Demo",
            buttonTextSp = "Descuento Demo",
        )
        val registerRequestString = Json.encodeToString(registerRequest)
        val componentName = ComponentName(
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value,
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value + ThirdPartyIdentifiers.REGISTER_CLASS.value
        )
        Log.d("thirddemo", "Send register request $registerRequestString")
        sendBroadcast(Intent(ThirdPartyIdentifiers.REGISTER_ACTION.value).apply {
            setComponent(componentName)
            putExtra(ThirdPartyIdentifiers.REGISTER_REQUEST.value, registerRequestString)
        })

    }

    fun registerAppForPayment(unregister: Boolean = false) {
        val registerRequest = ThirdPartyRegisterRequest(
            registrationType = if (unregister) {
                RegistrationType.UNREGISTER.name
            } else {
                RegistrationType.REGISTER.name
            },
            thirdPartyEventType = ExtThirdPartyEventType.PAYMENT.toString(),
            transactionsSupported = listOf(
                ExtThirdPartyTransaction.SALE.toString(),
                ExtThirdPartyTransaction.REFUND.toString(),
                ExtThirdPartyTransaction.VOID.toString()
            ),
            thirdPartyPackage = PACKAGE_NAME,
            thirdPartyReceiver = PACKAGE_NAME + ".PaymentReceiver",
            registerResponseReceiver = PACKAGE_NAME + ThirdPartyIdentifiers.REGISTER_RESPONSE_RECEIVER.value,
            thirdPartyAction = PACKAGE_NAME + ".ACTION_APPLY_PAYMENT",
            buttonTextEn = "Payment Demo",
            buttonTextFr = "Paiement Demo",
            buttonTextSp = "Pago Demo",
        )
        val registerRequestString = Json.encodeToString(registerRequest)
        val componentName = ComponentName(
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value,
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value + ThirdPartyIdentifiers.REGISTER_CLASS.value
        )
        Log.d("thirddemo", "Send register request $registerRequestString")
        sendBroadcast(Intent(ThirdPartyIdentifiers.REGISTER_ACTION.value).apply {
            setComponent(componentName)
            putExtra(ThirdPartyIdentifiers.REGISTER_REQUEST.value, registerRequestString)
        })

    }




    fun registerAppForEReceipt(unregister: Boolean = false) {
        val registerRequest = ThirdPartyRegisterRequest(
            registrationType = if (unregister) {
                RegistrationType.UNREGISTER.name
            } else {
                RegistrationType.REGISTER.name
            },
            thirdPartyEventType = ExtThirdPartyEventType.E_RECEIPT.toString(),
            transactionsSupported = listOf(
                ExtThirdPartyTransaction.SALE.toString(),
                ExtThirdPartyTransaction.PREAUTH.toString(),
                ExtThirdPartyTransaction.PREAUTH_COMPLETION.toString(),
                ExtThirdPartyTransaction.REFUND.toString(),
                ExtThirdPartyTransaction.FORCE_SALE.toString(),
                ExtThirdPartyTransaction.VOID.toString(),
                ExtThirdPartyTransaction.INCREMENTAL_AUTH.toString(),
                ExtThirdPartyTransaction.FORCE_PREAUTH.toString()
            ),
            thirdPartyPackage = packageName,
            thirdPartyReceiver = packageName + ".EReceiptReceiver",
            registerResponseReceiver = packageName + ThirdPartyIdentifiers.REGISTER_RESPONSE_RECEIVER.value,
            thirdPartyAction = packageName + ".ACTION_APPLY_E_RECEIPT",
            buttonTextEn = "E-Receipt Demo",
            buttonTextFr = "Reçu Demo",
            buttonTextSp = "Recibo Demo",
        )


        val registerRequestString = Json.encodeToString(registerRequest)
        val componentName = ComponentName(
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value,
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value + ThirdPartyIdentifiers.REGISTER_CLASS.value
        )
        Log.d("thirddemo", "Send register request $registerRequestString")
        sendBroadcast(Intent(ThirdPartyIdentifiers.REGISTER_ACTION.value).apply {
            setComponent(componentName)
            putExtra(ThirdPartyIdentifiers.REGISTER_REQUEST.value, registerRequestString)
        })

    }

    fun registerAppForAfterTxn(unregister: Boolean = false) {
        val registerRequest = ThirdPartyRegisterRequest(
            registrationType = if (unregister) {
                RegistrationType.UNREGISTER.name
            } else {
                RegistrationType.REGISTER.name
            },
            thirdPartyEventType = ExtThirdPartyEventType.AFTER_TRANSACTION.toString(),
            transactionsSupported = listOf(
                ExtThirdPartyTransaction.SALE.toString(),
                ExtThirdPartyTransaction.REFUND.toString(),
                ExtThirdPartyTransaction.FORCE_SALE.toString(),
                ExtThirdPartyTransaction.PREAUTH.toString(),
                ExtThirdPartyTransaction.PREAUTH_COMPLETION.toString(),
                ExtThirdPartyTransaction.VOID.toString(),
                ExtThirdPartyTransaction.INCREMENTAL_AUTH.toString(),
                ExtThirdPartyTransaction.FORCE_PREAUTH.toString(),
            ),
            thirdPartyPackage = PACKAGE_NAME,
            thirdPartyReceiver = PACKAGE_NAME + ".AfterTxnReceiver",
            registerResponseReceiver = PACKAGE_NAME + ThirdPartyIdentifiers.REGISTER_RESPONSE_RECEIVER.value,
            thirdPartyAction = PACKAGE_NAME + ".ACTION_APPLY_AFTER_TXN",
            buttonTextEn = "After Transaction Demo",
            buttonTextFr = "Enquête Demo",
            buttonTextSp = "Encuesta Demo",
        )
        val registerRequestString = Json.encodeToString(registerRequest)
        val componentName = ComponentName(
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value,
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value + ThirdPartyIdentifiers.REGISTER_CLASS.value
        )
        Log.d("thirddemo", "Send register request $registerRequestString")
        sendBroadcast(Intent(ThirdPartyIdentifiers.REGISTER_ACTION.value).apply {
            setComponent(componentName)
            putExtra(ThirdPartyIdentifiers.REGISTER_REQUEST.value, registerRequestString)
        })

    }

    fun registerAppForDCC(unregister: Boolean = false) {
        val registerRequest = ThirdPartyRegisterRequest(
            registrationType = if (unregister) {
                RegistrationType.UNREGISTER.name
            } else {
                RegistrationType.REGISTER.name
            },
            thirdPartyEventType = ExtThirdPartyEventType.DCC.toString(),
            transactionsSupported = listOf(
                ExtThirdPartyTransaction.SALE.toString(),
                ExtThirdPartyTransaction.REFUND.toString(),
                ExtThirdPartyTransaction.PREAUTH.toString(),
                ExtThirdPartyTransaction.PREAUTH_COMPLETION.toString()
            ),
            thirdPartyPackage = PACKAGE_NAME,
            thirdPartyReceiver = PACKAGE_NAME + ".DccReceiver",
            registerResponseReceiver = PACKAGE_NAME + ThirdPartyIdentifiers.REGISTER_RESPONSE_RECEIVER.value,
            thirdPartyAction = PACKAGE_NAME + ".ACTION_APPLY_DCC",
            buttonTextEn = "DCC Demo",
            buttonTextFr = "DCC Demo",
            buttonTextSp = "DCC Demo",
        )
        val registerRequestString = Json.encodeToString(registerRequest)
        val componentName = ComponentName(
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value,
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value + ThirdPartyIdentifiers.REGISTER_CLASS.value
        )
        Log.d("thirddemo", "Send register request $registerRequestString")
        sendBroadcast(Intent(ThirdPartyIdentifiers.REGISTER_ACTION.value).apply {
            setComponent(componentName)
            putExtra(ThirdPartyIdentifiers.REGISTER_REQUEST.value, registerRequestString)
        })

    }

    fun registerAppForAfterTotalAmount(unregister: Boolean = false) {
        val registerRequest = ThirdPartyRegisterRequest(
            registrationType = if (unregister) {
                RegistrationType.UNREGISTER.name
            } else {
                RegistrationType.REGISTER.name
            },
            thirdPartyEventType = ExtThirdPartyEventType.AFTER_TOTAL_AMOUNT.toString(),
            transactionsSupported = listOf(
                ExtThirdPartyTransaction.SALE.toString(),
                ExtThirdPartyTransaction.REFUND.toString(),
                ExtThirdPartyTransaction.PREAUTH.toString(),
                ExtThirdPartyTransaction.PREAUTH_COMPLETION.toString()
            ),
            thirdPartyPackage = PACKAGE_NAME,
            thirdPartyReceiver = PACKAGE_NAME + ".AfterTotalAmountReceiver",
            registerResponseReceiver = PACKAGE_NAME + ThirdPartyIdentifiers.REGISTER_RESPONSE_RECEIVER.value,
            thirdPartyAction = PACKAGE_NAME + ".ACTION_APPLY_AFTER_TOTAL_AMOUNT",
            buttonTextEn = "After Total Amount Demo",
            buttonTextFr = "Après totale Demo",
            buttonTextSp = "Después Del Total Demo",
        )
        val registerRequestString = Json.encodeToString(registerRequest)
        val componentName = ComponentName(
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value,
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value + ThirdPartyIdentifiers.REGISTER_CLASS.value
        )
        Log.d("thirddemo", "Send register request $registerRequestString")
        sendBroadcast(Intent(ThirdPartyIdentifiers.REGISTER_ACTION.value).apply {
            setComponent(componentName)
            putExtra(ThirdPartyIdentifiers.REGISTER_REQUEST.value, registerRequestString)
        })

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val intent = intent

        if (!intent.getBooleanExtra(ThirdPartyApi.THIRD_PARTY_REQUEST.key, false)) {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setSupportActionBar(binding.toolbar)
            binding.discountRegisterBtn.setOnClickListener { registerAppForDiscount(false) }
            binding.discountUnregisterBtn.setOnClickListener { registerAppForDiscount(true) }
            binding.paymentRegisterBtn.setOnClickListener { registerAppForPayment(false) }
            binding.paymentUnregisterBtn.setOnClickListener { registerAppForPayment(true) }
            binding.eReceiptRegisterBtn.setOnClickListener { registerAppForEReceipt(false) }
            binding.eReceiptUnregisterBtn.setOnClickListener { registerAppForEReceipt(true) }
            binding.afterTxnRegisterBtn.setOnClickListener { registerAppForAfterTxn(false) }
            binding.afterTxnUnregisterBtn.setOnClickListener { registerAppForAfterTxn(true) }
            binding.afterTotalAmountRegisterBtn.setOnClickListener {
                registerAppForAfterTotalAmount(
                    false
                )
            }
            binding.afterTotalAmountUnregisterBtn.setOnClickListener {
                registerAppForAfterTotalAmount(
                    true
                )
            }
//            binding.dccRegisterBtn.setOnClickListener {  }
            binding.dccRegisterBtn.setOnClickListener { registerAppForDCC(false) }
            binding.dccUnregisterBtn.setOnClickListener { registerAppForDCC(true) }
            binding.exitBtn.setOnClickListener { finish() }
            binding.searchForRegistration.setOnClickListener {
                val intent = Intent(this, SearchHookActivity::class.java)
                startActivity(intent)
            }
        } else {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}

class RegisterResponseReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("thirddemo", "onReceive: receive registration response from NUA ")
        val registrationType = intent.getStringExtra(
            ThirdPartyIdentifiers.REGISTER_REQUEST.value
        ) as String
        if (registrationType == RegistrationType.CHECK_STATUS.name) {
            val statusResult = intent.getStringExtra(
                ThirdPartyApi.CHECK_STATUS_RESULT.key
            ) as String
            result = statusResult
            Log.d("thirddemo", "Check status result received from NUA : $statusResult")
            context.startActivity(
                Intent(
                    context,
                    SearchHookActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        } else {
            val requestType = intent.getStringExtra(
                ThirdPartyApi.THIRD_PARTY_REQUEST_TYPE.key
            ) as String
            val msg = "$requestType ${registrationType.lowercase()}ed successfully"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            Log.d("thirddemo", msg)
        }

    }
}