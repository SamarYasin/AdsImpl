package com.example.adsimpl

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.adsimpl.databinding.FragmentForthBinding
import com.example.adsimpl.util.checkAndHandleAdPermission
import com.example.adsimpl.viewmodel.RemoteEntry
import com.example.adsimpl.viewmodel.RemoteViewModel
import com.example.adsimpl.viewmodel.RewardedInterstitialViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForthFragment : Fragment() {

    private var _binding: FragmentForthBinding? = null
    private val binding get() = _binding!!

    private val remoteViewModel: RemoteViewModel by activityViewModels()
    private val rewardedInterstitialViewModel: RewardedInterstitialViewModel by activityViewModels()

    private var rewardedInterstitialAdIsAllowed: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForthBinding.inflate(inflater, container, false)

        // Setting Remote Configurations
        remoteViewModel.fetchRemoteData()
        Log.d("ForthFragment", "Remote data fetched successfully")
        // You can update UI or perform actions based on the fetched data here

        // Implementing Native Small Ad
        remoteViewModel.remoteDataList.observe(viewLifecycleOwner) { remoteDataList: List<RemoteEntry> ->
            // Keep Ad Name in String, so that it can be checked against remote config
            checkAndHandleAdPermission(
                adName = "rewarded_interstitial_ad",
                adList = remoteDataList,
                onAllowed = {
                    rewardedInterstitialAdIsAllowed = true
                }
            )
        }

        rewardedInterstitialViewModel.loadRewardedInterstitialAd(
            requireContext(),
            getString(R.string.rewarded_interstitial_ad),
            onAdLoaded = {
                when (rewardedInterstitialAdIsAllowed) {
                    true -> {
                        Log.d("ForthFragment", "Rewarded Interstitial Ad is allowed")
                        Log.d("ForthFragment", "Rewarded Interstitial Ad loaded successfully")
                        rewardedInterstitialViewModel.showRewardedInterstitialAd(
                            requireActivity(),
                            onUserEarnedReward = {
                                Log.d("ForthFragment", "User earned reward from Rewarded Interstitial Ad")
                            },
                            onAdDismissed = {
                                Log.d("ForthFragment", "Rewarded Interstitial Ad dismissed")
                                // Navigate to another fragment or perform any action after ad dismissal
                            }
                        )
                    }

                    false -> {
                        Log.d("ForthFragment", "Rewarded Interstitial Ad is not allowed")
                    }
                }
            },
            onAdFailedToLoad = {
                Log.e("ForthFragment", "Failed to load Rewarded Interstitial Ad: $it")
            }
        )

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}