package com.example.adsimpl

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.adsimpl.databinding.FragmentThirdBinding
import com.example.adsimpl.util.checkAndHandleAdPermission
import com.example.adsimpl.util.setSafeClickListener
import com.example.adsimpl.viewmodel.InterstitialViewModel
import com.example.adsimpl.viewmodel.RemoteEntry
import com.example.adsimpl.viewmodel.RemoteViewModel
import com.example.adsimpl.viewmodel.RewardedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    private val remoteViewModel: RemoteViewModel by activityViewModels()
    private val rewardedViewModel: RewardedViewModel by activityViewModels()
    private val interstitialViewModel: InterstitialViewModel by activityViewModels()

    private var rewardedAdIsAllowed: Boolean = false
    private var interstitialAdIsAllowed: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)

        // Setting Remote Configurations
        remoteViewModel.fetchRemoteData()
        Log.d("ThirdFragment", "Remote data fetched successfully")
        // You can update UI or perform actions based on the fetched data here

        // Implementing Native Small Ad
        remoteViewModel.remoteDataList.observe(viewLifecycleOwner) { remoteDataList: List<RemoteEntry> ->
            // Keep Ad Name in String, so that it can be checked against remote config
            checkAndHandleAdPermission(
                adName = "rewarded_ad",
                adList = remoteDataList,
                onAllowed = {
                    rewardedAdIsAllowed = true
                }
            )

            checkAndHandleAdPermission(
                adName = "interstitial_ad",
                adList = remoteDataList,
                onAllowed = {
                    interstitialAdIsAllowed = true
                }
            )

        }

        rewardedViewModel.loadRewardedAd(
            requireActivity(),
            getString(R.string.rewarded_ad),
            onLoaded = {
                Log.d("ThirdFragment", "Rewarded ad loaded successfully")
                when (rewardedAdIsAllowed) {
                    true -> {
                        rewardedViewModel.showRewardedAd(
                            requireActivity(),
                            onUserEarnedReward = { rewardItem ->
                                Log.d(
                                    "ThirdFragment",
                                    "User earned reward: ${rewardItem.type} - ${rewardItem.amount}"
                                )
                            },
                            onAdDismissed = {
                                Log.d("ThirdFragment", "Rewarded ad dismissed")
                            }
                        )
                        Log.d("ThirdFragment", "Rewarded ad is allowed")
                    }

                    false -> {
                        Log.d("ThirdFragment", "Rewarded ad is not allowed")
                    }
                }
            },
            onFailed = {
                Log.d("ThirdFragment", "Failed to load rewarded ad: $it")
            })

        // Pre-fetching Interstitial Ad
        interstitialViewModel.preFetchInterstitialAd(
            requireContext(),
            getString(R.string.interstitial_ad)
        ) {
            Log.d("SecondFragment", "Interstitial Ad pre-fetched successfully")
            if (interstitialAdIsAllowed) {
                Log.d("FirstFragment", "Interstitial Ad is allowed to be shown")
                interstitialViewModel.showInterstitialAd(requireActivity(), onAdDismissed = {
                    Log.d("FirstFragment", "Interstitial Ad dismissed")

                })
            } else {
                Log.d("SecondFragment", "Interstitial Ad is not allowed to be shown")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextTVBtn.setSafeClickListener(1000) {
            findNavController().navigate(R.id.action_thirdFragment_to_forthFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}