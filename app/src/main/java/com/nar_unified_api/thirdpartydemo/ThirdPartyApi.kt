package com.nar_unified_api.thirdpartydemo

import android.os.Parcelable


enum class ThirdPartyTypes {
    LONG,
    STRING,
    INTEGER,
    HASHMAP,
    BOOLEAN,
}

enum class ExtThirdPartyEventType( val value: String) {
    BEFORE_TRANSACTION("BEFORE TRANSACTION"),
    PAYMENT("PAYMENT"),
    E_RECEIPT("E RECEIPT"),
    AFTER_TRANSACTION("AFTER TRANSACTION"),
    AFTER_TOTAL_AMOUNT("AFTER TOTAL AMOUNT"),
    DCC("DCC")
}

enum class ExtThirdPartyTransaction {
    INVALID,
    AUTH_ONLY,
    SALE,
    PREAUTH,
    REFUND,
    VOID,
    VOID_PREAUTH,
    PREAUTH_COMPLETION,
    TIP_ADJUSTMENT,
    TRANSACTION_ADJUST,
    FORCE_SALE,
    FORCE_PREAUTH,
    TOKEN_REQUEST,
    BALANCE_INQUIRY,
    RECALL_LAST,
    INCREMENTAL_AUTH
}

enum class ThirdPartyIdentifiers( val value: String){
    REGISTER_REQUEST("register_request"),
    REGISTER_ACTION( "com.ingenico.nar.unified_api_service.REGISTER_APP"),
    REGISTER_PACKAGE("com.ingenico.nar.unified_api_service"),
    REGISTER_CLASS(".thirdpartymanager.ThirdPartyRegisterApp"),
    REGISTER_RESPONSE_RECEIVER(".RegisterResponseReceiver")
}
enum class ThirdPartyCardType( val value:String){
    GIFT_CARD("gift_card"),
    LOYALTY("loyalty"),
    OTHER( "other")
}

enum class ThirdPartyEntryMode( val value:String){
    SWIPE("Swipe"),
    TAP("Tap"),
    INSERT( "Insert"),
    MANUAL("Manual"),
    SCAN("Scan")

}

enum class ThirdPartyApiTranStatus(val value: String){
    APPROVED("approved"),
    PARTIAL_APPROVAL("partial approval"),
    DECLINED( "declined"),
    DIVIDE_ERRORS("errors_after_this_value"),
    COMM_ERROR( "comm_error"),
    CANCELLED("cancelled"),
    TIME_OUT("time_out"),
    NOT_COMPLETED( "not_completed"),
    RESERVED( "reserved"),
    MERCHANT_DECLINE("merchant_decline"),
    NOT_FOUND( "not_found"),
    ALREADY_VOIDED("already_voided"),
    INVALID_REQUEST( "invalid_request")

}

enum class ThirdPartyApi(val key: String, val type: ThirdPartyTypes) {
    DECIMAL_SHIFT("decimal_shift", ThirdPartyTypes.INTEGER),
    CURRENCY_CODE("currency_code", ThirdPartyTypes.LONG),
    AMOUNT("amount", ThirdPartyTypes.LONG),
    DISCOUNTED_AMOUNT("discounted_amount", ThirdPartyTypes.LONG),
    TOTAL_AMOUNT("total_amount", ThirdPartyTypes.LONG),
    DCC_CONVERSION_RATE("dcc_conversion_rate", ThirdPartyTypes.STRING),
    DCC_MARKUP_RATE("dcc_markup_rate", ThirdPartyTypes.STRING),
    DCC_OPT_IN("dcc_opt_in", ThirdPartyTypes.BOOLEAN),
    THIRD_PARTY_REQUEST_TYPE("third_party_request_type", ThirdPartyTypes.STRING),
    PARAMETERS("parameters", ThirdPartyTypes.HASHMAP),
    CALLER("caller", ThirdPartyTypes.STRING),
    CLERK_ID( "clerk_id", ThirdPartyTypes.LONG),
    INVOICE( "third_party_web_invoice", ThirdPartyTypes.STRING),
    TRAN_STATUS( "tran_status", ThirdPartyTypes.STRING),
    TRAN_TIME( "third_party_tran_time", ThirdPartyTypes.STRING), // "HHMMSS"
    TRAN_DATE( "third_party_tran_date", ThirdPartyTypes.STRING), // "YYMMDD"
    BATCH_ID( "third_party_batch_id", ThirdPartyTypes.INTEGER),
    THIRD_PARTY_CARD_TYPE( "third_party_card_type", ThirdPartyTypes.STRING),
    ACCOUNT_NUMBER( "account_number", ThirdPartyTypes.STRING),
    APPROVAL_CODE( "third_party_approval_code", ThirdPartyTypes.STRING),
    RESPONSE_TEXT( "third_party_response_text", ThirdPartyTypes.STRING),
    RESPONSE_CODE( "third_party_response_code", ThirdPartyTypes.STRING),
    AMOUNT_AUTHORIZED( "third_party_amount_authorized", ThirdPartyTypes.LONG),
    ACQUIRER_PROGRAM( "acquirer_program", ThirdPartyTypes.STRING),
    DEMO_MODE( "demo_mode", ThirdPartyTypes.BOOLEAN),
    TERMINAL_ID( "terminal_id", ThirdPartyTypes.STRING),
    MERCHANT_ID( "merchant_id", ThirdPartyTypes.STRING),
    //  FUNCTION( "function", ThirdPartyTypes.STRING),
    THIRD_PARTY_RESPONSE_RECEIVER("third_party_response_receiver", ThirdPartyTypes.STRING),
    THIRD_PARTY_CALLER("third_party_caller", ThirdPartyTypes.STRING),
    THIRD_PARTY_RESULT_CALLBACK("third_party_result_callback", ThirdPartyTypes.STRING),
    MERCHANT_RECEIPT_DATA("merchant_receipt_data", ThirdPartyTypes.STRING),
    CUSTOMER_RECEIPT_DATA("customer_receipt_data", ThirdPartyTypes.STRING),
    ENTRY_MODE("entry_mode", ThirdPartyTypes.STRING),
    TRANSACTION_TYPE("transaction_type", ThirdPartyTypes.STRING),
    CARD_BIN("cardBin", ThirdPartyTypes.STRING),
    CARD_TYPE("card_type", ThirdPartyTypes.STRING),
    CARD_BRAND("card_brand", ThirdPartyTypes.STRING),
    FLAVOR_PRIMARY_COLOR("flavor_primary_color", ThirdPartyTypes.STRING),
    FLAVOR_PRIMARY_DARK_COLOR("flavor_primary_dark_color", ThirdPartyTypes.STRING),
    FLAVOR_PRIMARY_DARK_COLOR50p("flavor_primary_dark_color50p", ThirdPartyTypes.STRING),
    FLAVOR_ACCENT_COLOR("flavor_accent_color", ThirdPartyTypes.STRING),
    LOYALTY_TOKEN("loyalty_token", ThirdPartyTypes.STRING),
    PHONE_NUMBER("phone_number", ThirdPartyTypes.STRING),
    MASKED_PAN("masked_pan", ThirdPartyTypes.STRING),
    EXPIRE_DATE("expire_date", ThirdPartyTypes.STRING),
    CARDHOLDER_NAME("cardholder_name", ThirdPartyTypes.STRING),
    CHECK_STATUS_RESULT( "check_status_result", ThirdPartyTypes.STRING),
    THIRD_PARTY_REQUEST("third_party_request", ThirdPartyTypes.BOOLEAN)
}