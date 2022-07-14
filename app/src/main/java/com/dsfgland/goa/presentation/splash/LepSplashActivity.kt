package com.dsfgland.goa.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.dsfgland.goa.BASE_URL
import com.dsfgland.goa.presentation.game.LepGameActivity
import com.dsfgland.goa.presentation.main.LepMainActivity
import com.dsfgland.goa.util.ext.IS_SAVED_KEY
import com.dsfgland.goa.util.ext.URL_KEY
import com.dsfgland.goa.util.ext.dataStore
import com.dsfgland.goa.util.ext.start
import com.dsfgland.goa.util.other.SecureChecker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class LepSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!SecureChecker(this@LepSplashActivity).secured) {
            lifecycleScope.launch {
                if (dataStore.data.first().asMap().isEmpty()) {
                    dataStore.edit { prefs ->
                        prefs[booleanPreferencesKey(IS_SAVED_KEY)] = false
                        prefs[stringPreferencesKey(URL_KEY)] = BASE_URL
                    }
                }
                start<LepMainActivity>()
            }
        } else {
            start<LepGameActivity>()
        }
    }
}