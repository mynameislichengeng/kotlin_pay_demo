package com.nar_unified_api.thirdpartydemo

import androidx.annotation.Keep
//import com.ingenico.nar.nar_unified_api_aidl.types.TransactionType
//import com.ingenico.nar.unified_api_service.utils.ThirdPartyTransaction
import kotlinx.serialization.Serializable

enum class RegistrationType( val registrationType: String) {
    REGISTER("register_app"),
    UNREGISTER( "unregister_app"),
    CHECK_STATUS("check_status")
}

@Keep
@Serializable
data class ThirdPartyRegisterRequest(
    val registrationType: String,
    val thirdPartyEventType: String? = null,
    val transactionsSupported: List<String>? = null,
    val thirdPartyPackage: String,
    val thirdPartyReceiver: String? = null,
    val registerResponseReceiver: String? = null,
    val thirdPartyAction: String? = null,
    val buttonTextEn: String? = null,
    val buttonTextFr: String? = null,
    val buttonTextSp: String? = null,
    val applicationId:String? = null
)

@Keep
@Serializable
data class RegistredHookList(val registredHooks: MutableList<RegistredHook>? = null)

@Keep
@Serializable
data class RegistredHook(
    val appId: String,
    val hook: String,
    val transactions: List<String>
)
