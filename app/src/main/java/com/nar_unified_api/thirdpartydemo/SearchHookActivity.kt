package com.nar_unified_api.thirdpartydemo

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nar_unified_api.thirdpartydemo.databinding.ActivitySearchhookBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class SearchHookActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchhookBinding
//    BuildConfig.APPLICATION_ID
    private var PACKAGE_NAME: String = "com.nar_unified_api.thirdpartydemo"

    companion object {
        private var hook = "ALL"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val intent = intent
        if (!intent.hasExtra(ThirdPartyApi.PARAMETERS.key)) {
            binding = ActivitySearchhookBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupUI()

            setSupportActionBar(binding.toolbar1)
            binding.exitBtn.setOnClickListener { finish() }
            binding.back.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            binding.search.setOnClickListener { checkStatus()}


        }else { finish() }
    }

    private fun checkStatus() {
        val appID = findViewById<EditText>(R.id.appId).text.toString()
        val searchRequest = ThirdPartyRegisterRequest(
            registrationType = RegistrationType.CHECK_STATUS.name,
            thirdPartyEventType = hookToExtThirdPartyEventTypeString(hook),
            thirdPartyPackage = PACKAGE_NAME,
            applicationId= if (appID != "") appID else null,
            registerResponseReceiver = PACKAGE_NAME + ThirdPartyIdentifiers.REGISTER_RESPONSE_RECEIVER.value
        )
        val searchRequestString = Json.encodeToString(searchRequest)
        val componentName = ComponentName(
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value,
            ThirdPartyIdentifiers.REGISTER_PACKAGE.value + ThirdPartyIdentifiers.REGISTER_CLASS.value
        )
        Log.d("thirddemo","Get registred thirdparty element : application id = ${searchRequest.applicationId?: "ALL"} , third party hook = ${searchRequest.thirdPartyEventType?:"ALL"}")
        sendBroadcast(Intent(ThirdPartyIdentifiers.REGISTER_ACTION.value).apply {
            setComponent(componentName)
            putExtra(ThirdPartyIdentifiers.REGISTER_REQUEST.value, searchRequestString)
        })
    }

    override fun onResume() {
        super.onResume()
        MainActivity.result?.let {
            val result = Json.decodeFromString<RegistredHookList>(it)
            displayResponse(result)
        }
    }


    fun displayResponse(result: RegistredHookList) {
        val outerListView = findViewById<ListView>(R.id.outerListView)
        var outerList : MutableList<OuterListItem>? = arrayListOf()
        result.registredHooks?.forEach { registredHook ->
            outerList?.add(OuterListItem(registredHook.appId,registredHook.hook,registredHook.transactions))
        }

        val outerAdapter = outerList?.let { OuterListAdapter(it) }
        outerListView.adapter = outerAdapter
    }

    private fun setupUI(){
        val hookArray = arrayOf(
            "ALL",
            ExtThirdPartyEventType.BEFORE_TRANSACTION.value,
            ExtThirdPartyEventType.PAYMENT.value,
            ExtThirdPartyEventType.E_RECEIPT.value,
            ExtThirdPartyEventType.AFTER_TOTAL_AMOUNT.value,
            ExtThirdPartyEventType.DCC.value,
            ExtThirdPartyEventType.AFTER_TRANSACTION.value
        )
        val hooksAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, hookArray)
        val spinnerHooks = findViewById<Spinner>(R.id.transactionTypeSpinner)
        spinnerHooks.adapter = hooksAdapter
        spinnerHooks.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                hook = hookArray[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {


            }
        }
    }
}
private fun hookToExtThirdPartyEventTypeString(hook: String): String? {
    return when (hook) {
        ExtThirdPartyEventType.BEFORE_TRANSACTION.value -> ExtThirdPartyEventType.BEFORE_TRANSACTION.toString()
        ExtThirdPartyEventType.PAYMENT.value-> ExtThirdPartyEventType.PAYMENT.toString()
        ExtThirdPartyEventType.E_RECEIPT.value -> ExtThirdPartyEventType.E_RECEIPT.toString()
        ExtThirdPartyEventType.AFTER_TOTAL_AMOUNT.value -> ExtThirdPartyEventType.AFTER_TOTAL_AMOUNT.toString()
        ExtThirdPartyEventType.AFTER_TRANSACTION.value -> ExtThirdPartyEventType.AFTER_TRANSACTION.toString()
        ExtThirdPartyEventType.DCC.value-> ExtThirdPartyEventType.DCC.toString()
        else -> null
    }
}

class OuterListAdapter(private val outerList: List<OuterListItem>) : BaseAdapter() {

    override fun getCount(): Int = outerList.size

    override fun getItem(position: Int): Any = outerList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val context = parent?.context
        val outerItem = outerList[position]

        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.list_item, parent, false)

        val titleTextView = itemView.findViewById<TextView>(R.id.applicationID)
        val hook = itemView.findViewById<TextView>(R.id.hook)
        val innerListView = itemView.findViewById<ListView>(R.id.innerListView)

        titleTextView.text = outerItem.appId
        hook.text = outerItem.hook
        val innerAdapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.list_transactions,
                outerItem.transactions.map { it }
            )
        }
        innerListView.adapter = innerAdapter
        val params = innerListView.layoutParams
        params.height = calculateInnerListViewHeight(outerItem.transactions.size, innerAdapter?.count?: 1)
        innerListView.layoutParams = params

        return itemView
    }
    private fun calculateInnerListViewHeight(itemCount: Int, itemHeight: Int): Int {
        return itemCount * itemHeight *4 +170
    }
}

data class OuterListItem(val appId: String,val hook: String, val transactions: List<String>)
