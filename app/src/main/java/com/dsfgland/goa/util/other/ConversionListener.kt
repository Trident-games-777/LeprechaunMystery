package com.dsfgland.goa.util.other

import com.appsflyer.AppsFlyerConversionListener

class ConversionListener(
    private val onSuccess: (MutableMap<String, Any>?) -> Unit,
    private val onError: (String) -> Unit
) : AppsFlyerConversionListener {
    override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
        onSuccess(data)
    }

    override fun onConversionDataFail(message: String?) {
        onError(message.toString())
    }

    override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {}
    override fun onAttributionFailure(p0: String?) {}
}