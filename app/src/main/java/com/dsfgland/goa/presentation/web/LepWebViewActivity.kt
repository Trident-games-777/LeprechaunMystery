package com.dsfgland.goa.presentation.web

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.dsfgland.goa.R
import com.dsfgland.goa.databinding.ActivityLepWebViewBinding
import com.dsfgland.goa.presentation.game.LepGameActivity
import com.dsfgland.goa.util.ext.IS_SAVED_KEY
import com.dsfgland.goa.util.ext.URL_KEY
import com.dsfgland.goa.util.ext.dataStore
import com.dsfgland.goa.util.ext.start
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LepWebViewActivity : AppCompatActivity() {
    private var messageAb: ValueCallback<Array<Uri?>>? = null
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityLepWebViewBinding.inflate(layoutInflater)
    }
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.black, null)

        webView = binding.webView
        val url = requireNotNull(intent.getStringExtra(URL_KEY))
        webView.loadUrl(url)

        webView.webViewClient = LeprechaunClient()
        webView.settings.javaScriptEnabled = true
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.settings.domStorageEnabled = true
        webView.settings.loadWithOverviewMode = false
        webView.settings.userAgentString =
            "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }

            //For Android API >= 21 (5.0 OS)
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                messageAb = filePathCallback
                selectImageIfNeed()
                return true
            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun onCreateWindow(
                view: WebView?, isDialog: Boolean,
                isUserGesture: Boolean, resultMsg: Message
            ): Boolean {
                val newWebView = WebView(applicationContext)
                newWebView.settings.javaScriptEnabled = true
                newWebView.webChromeClient = this
                newWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                newWebView.settings.domStorageEnabled = true
                newWebView.settings.setSupportMultipleWindows(true)
                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()
                return true
            }
        }
    }

    private fun selectImageIfNeed() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = IMAGE_MIME_TYPE
        startActivityForResult(
            Intent.createChooser(intent, IMAGE_CHOOSER_TITLE),
            RESULT_CODE
        )
    }

    private inner class LeprechaunClient : WebViewClient() {
        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            if (errorCode == -2) {
                Toast.makeText(this@LepWebViewActivity, "Error", Toast.LENGTH_LONG).show()
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            url?.let {
                if (it.contains("leprechaunmystery.online")) {
                    start<LepGameActivity>()
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        if (dataStore.data.first()[booleanPreferencesKey(IS_SAVED_KEY)] == false) {
                            dataStore.edit { prefs ->
                                prefs[stringPreferencesKey(URL_KEY)] = it
                                prefs[booleanPreferencesKey(IS_SAVED_KEY)] = true
                            }
                        }
                        CookieManager.getInstance().flush()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val IMAGE_CHOOSER_TITLE = "Image Chooser"
        private const val IMAGE_MIME_TYPE = "image/*"

        private const val RESULT_CODE = 1
    }
}