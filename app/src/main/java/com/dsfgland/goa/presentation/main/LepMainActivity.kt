package com.dsfgland.goa.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.appsflyer.AppsFlyerLib
import com.dsfgland.goa.databinding.ActivityLepMainBinding
import com.dsfgland.goa.presentation.web.LepWebViewActivity
import com.dsfgland.goa.util.ext.*
import com.dsfgland.goa.util.other.ConversionListener
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LepMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLepMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLepMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var url: String

        lifecycleScope.launch(Dispatchers.IO) {
            if (dataStore.data.first()[booleanPreferencesKey(IS_SAVED_KEY)] == true) {
                url = dataStore.data.first()[stringPreferencesKey(URL_KEY)]!!
                start<LepWebViewActivity>(url)
            } else {
                AppLinkData.fetchDeferredAppLinkData(this@LepMainActivity) { appLinkData ->
                    val appLink = appLinkData?.targetUri.toString()
                    val ad =
                        AdvertisingIdClient.getAdvertisingIdInfo(this@LepMainActivity).id.toString()
                    if (appLink != "null") {
                        url =
                            createUrl(appLink = appLink, appData = null, ad = ad)
                        notifyOneSignal(appLink = appLink, appData = null, ad = ad)
                        start<LepWebViewActivity>(url)
                    } else {
                        AppsFlyerLib.getInstance().init("E9UeAZNwU55DYGbwGXr7se",
                            ConversionListener(
                                onSuccess = { appData ->
                                    url = createUrl("null", appData, ad)
                                    notifyOneSignal("null", appData, ad)
                                    start<LepWebViewActivity>(url)
                                },
                                onError = {
                                    url = createUrl("null", null, ad)
                                    notifyOneSignal("null", null, ad)
                                    start<LepWebViewActivity>(url)
                                }
                            ),
                            this@LepMainActivity
                        )
                        AppsFlyerLib.getInstance().start(this@LepMainActivity)
                    }
                }
            }
        }
    }
}