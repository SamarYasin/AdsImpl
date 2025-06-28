package com.example.adsimpl

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.adsimpl.databinding.ActivityMainBinding
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import com.google.firebase.BuildConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var consentInformation: ConsentInformation
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        val paramBuilder = ConsentRequestParameters.Builder()
        if (BuildConfig.DEBUG) {
            val debugSetting = ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId("A73055854F1392D713892297BDB23876")
                .build()
            paramBuilder.setConsentDebugSettings(debugSetting)
        }
        val param = paramBuilder.build()

        consentInformation.requestConsentInfoUpdate(
            this,
            param,
            {
                when (consentInformation.privacyOptionsRequirementStatus) {
                    ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED -> {
                        Log.d("Consent", "Privacy options are required.")
                        // Navigate to privacy options screen if needed
                        if (consentInformation.isConsentFormAvailable) {
                            loadAndShowConsentForm()
                        }
                    }

                    ConsentInformation.PrivacyOptionsRequirementStatus.NOT_REQUIRED -> {
                        Log.d("Consent", "Privacy options are not required.")
                    }

                    ConsentInformation.PrivacyOptionsRequirementStatus.UNKNOWN -> {
                        Log.d("Consent", "Privacy options status is unknown.")
                        if (consentInformation.isConsentFormAvailable) {
                            loadAndShowConsentForm()
                        }
                    }
                }
            },
            { formError: FormError ->
                // Handle the error
                Log.e("Consent", "Error: ${formError.message}")
            }
        )

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

    }

    private fun loadAndShowConsentForm() {
        UserMessagingPlatform.loadConsentForm(
            this,
            { consentForm ->
                consentForm.show(
                    this
                ) {
                    // Handle dismissal or consent state update
                    Log.d("Consent", "Consent form dismissed or updated.")
                }
            },
            { formError ->
                // Handle the error
                Log.e("Consent", "Form load error: ${formError.message}")
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy: Clearing ad cache")
        _binding = null
    }

}