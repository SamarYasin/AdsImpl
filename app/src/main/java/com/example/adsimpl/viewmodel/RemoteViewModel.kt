package com.example.adsimpl.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adsimpl.network.NetworkHandler
import com.example.adsimpl.util.getSubscriptionStatus
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class RemoteViewModel @Inject constructor(
    private val networkHandler: NetworkHandler
) : ViewModel() {

    private val _remoteDataList = MutableLiveData<List<RemoteEntry>>()
    val remoteDataList: LiveData<List<RemoteEntry>> get() = _remoteDataList

    fun fetchRemoteData(
        onRemoteDataFetched: () -> Unit = {}
    ) {

        when (networkHandler.isNetworkAvailable() && !getSubscriptionStatus()) {

            true -> {
                viewModelScope.launch {
                    val remoteConfig = FirebaseRemoteConfig.getInstance()
                    val configSettings = remoteConfigSettings {
                        minimumFetchIntervalInSeconds = 3600
                    }
                    remoteConfig.setConfigSettingsAsync(configSettings)
                    remoteConfig.fetchAndActivate()
                        .addOnFailureListener {
                            Log.e("RemoteViewModel", "Error fetching remote config", it)
                        }
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val value = remoteConfig.getString("ad_impl_remote_config")
                                try {
                                    val json = Json { ignoreUnknownKeys = true }
                                    val dataList = json.decodeFromString<List<RemoteEntry>>(value)
                                    _remoteDataList.postValue(dataList)
                                    Log.d("RemoteViewModel", "Remote data fetched successfully: $dataList")
                                } catch (e: Exception) {
                                    Log.e("RemoteViewModel", "JSON parsing error", e)
                                }
                            } else {
                                Log.w("RemoteViewModel", "Fetch failed")
                            }
                        }
                        .addOnCanceledListener {
                            Log.w("RemoteViewModel", "Fetch canceled")
                        }

                    remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener{
                        override fun onUpdate(configUpdate: ConfigUpdate) {
                            Log.d("Remote View Model", "onUpdate: Remote Config Updated Successfully ${configUpdate.updatedKeys}")
                        }

                        override fun onError(error: FirebaseRemoteConfigException) {
                            Log.d("Remote View Model", "onError: Remote Config Update Failed ${error.message}")
                        }

                    })

                }.invokeOnCompletion {
                    Log.d("Remote View Model", "fetchRemoteData: Remote Config Fetch Completed")
                    onRemoteDataFetched.invoke()
                }
            }

            false -> {
                Log.d("Remote View Model", "fetchRemoteData: No Internet Connection")
            }

        }

    }

}