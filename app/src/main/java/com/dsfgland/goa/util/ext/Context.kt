package com.dsfgland.goa.util.ext

import android.content.Context
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.appsflyer.AppsFlyerLib
import com.dsfgland.goa.BASE_URL
import com.onesignal.OneSignal
import java.util.*

private const val PREFS_FILE = "url_store"

const val IS_SAVED_KEY = "is_saved"
const val URL_KEY = "url"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_FILE)

fun Context.createUrl(
    appLink: String,
    appData: MutableMap<String, Any>?,
    ad: String
): String {
    return BASE_URL.toUri().buildUpon().apply {
        appendQueryParameter("3CfQHozH2e", "1XGVRguV2S")
        appendQueryParameter("vWVnSDlsD2", TimeZone.getDefault().id)
        appendQueryParameter("skoz9xIxbA", ad)
        appendQueryParameter("DVhGuz9ccQ", appLink)
        appendQueryParameter(
            "mH4tiPwBHG",
            if (appLink != "null") "deeplink" else appData?.get("media_source").toString()
        )
        appendQueryParameter(
            "Zt02zufQzu",
            AppsFlyerLib.getInstance().getAppsFlyerUID(this@createUrl)
        )
        appendQueryParameter("uZ6vdikzJj", appData?.get("adset_id").toString())
        appendQueryParameter("H4xX3LtelN", appData?.get("campaign_id").toString())
        appendQueryParameter("AvxhE6vbd4", appData?.get("campaign").toString())
        appendQueryParameter("uPRZvEfVx9", appData?.get("adset").toString())
        appendQueryParameter("WFuX7NL2bK", appData?.get("adgroup").toString())
        appendQueryParameter("Z4HKvaAcKL", appData?.get("orig_cost").toString())
        appendQueryParameter("r8DlViQPHg", appData?.get("af_siteid").toString())
    }.toString()
}

fun Context.notifyOneSignal(
    appLink: String,
    appData: MutableMap<String, Any>?,
    ad: String
) {
    OneSignal.initWithContext(this)
    OneSignal.setAppId("b36e6129-0358-4204-bbe5-4b2260df5c32")
    OneSignal.setExternalUserId(ad)

    when {
        appData?.get("campaign").toString() == "null" && appLink == "null" -> {
            OneSignal.sendTag("key2", "organic")
        }
        appLink != "null" -> {
            OneSignal.sendTag(
                "key2",
                appLink.replace("myapp://", "").substringBefore("/")
            )
        }
        appData?.get("campaign").toString() != "null" -> {
            OneSignal.sendTag(
                "key2",
                appData?.get("campaign").toString().substringBefore("_")
            )
        }
    }
}