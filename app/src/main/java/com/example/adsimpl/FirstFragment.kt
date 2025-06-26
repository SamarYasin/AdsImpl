package com.example.adsimpl

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.adsimpl.databinding.FragmentFirstBinding
import com.example.adsimpl.util.checkAndHandleAdPermission
import com.example.adsimpl.util.setSafeClickListener
import com.example.adsimpl.viewmodel.BannerViewModel
import com.example.adsimpl.viewmodel.InterstitialViewModel
import com.example.adsimpl.viewmodel.NativeViewModel
import com.example.adsimpl.viewmodel.RemoteEntry
import com.example.adsimpl.viewmodel.RemoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val remoteViewModel: RemoteViewModel by activityViewModels()
    private val nativeViewModel: NativeViewModel by activityViewModels()
    private val bannerViewModel: BannerViewModel by activityViewModels()
    private val interstitialViewModel: InterstitialViewModel by activityViewModels()

    private var nativeSmallAdIsAllowed: Boolean = false
    private var interstitialAdIsAllowed: Boolean = false
    private var bannerAdIsAllowed: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        // Setting Remote Configurations
        remoteViewModel.fetchRemoteData()
        Log.d("FirstFragment", "Remote data fetched successfully")
        // You can update UI or perform actions based on the fetched data here

        // Implementing Native Small Ad
        remoteViewModel.remoteDataList.observe(viewLifecycleOwner) { remoteDataList: List<RemoteEntry> ->
            // Keep Ad Name in String, so that it can be checked against remote config
            checkAndHandleAdPermission(
                adName = "native_small_ad",
                adList = remoteDataList,
                onAllowed = {
                    nativeSmallAdIsAllowed = true
                }
            )

            checkAndHandleAdPermission(
                adName = "interstitial_ad",
                adList = remoteDataList,
                onAllowed = {
                    interstitialAdIsAllowed = true
                }
            )

            checkAndHandleAdPermission(
                adName = "banner_ad",
                adList = remoteDataList,
                onAllowed = {
                    bannerAdIsAllowed = true
                }
            )

        }

        // Pre-fetching Native Ads
        nativeViewModel.preFetchNativeAd(requireContext(), getString(R.string.native_small_ad)) {
            Log.d("FirstFragment", "Native Small Ad pre-fetched successfully")
            if (nativeSmallAdIsAllowed) {
                Log.d("FirstFragment", "Native Small Ad is allowed to be shown")
                val shimmerLayout = binding.smallAdView.shimmerLayout
                shimmerLayout.visibility = View.VISIBLE
                shimmerLayout.startShimmer()
                nativeViewModel.showNativeAd(
                    context = requireContext(),
                    adUnitId = getString(R.string.native_small_ad),
                    adView = binding.smallAdView.nativeAdView.root,
                    shimmerLayout = binding.smallAdView.shimmerLayout,
                    onAdShown = {
                        Log.d("FirstFragment", "Native Small Ad shown successfully")
                        shimmerLayout.stopShimmer()
                        shimmerLayout.visibility = View.GONE
                        binding.smallAdView.root.visibility = View.VISIBLE
                        binding.smallAdView.nativeAdView.root.visibility = View.VISIBLE
                    }
                )
            } else {
                Log.d("FirstFragment", "Native Small Ad is not allowed to be shown")
            }
        }

        // Pre-fetching Interstitial Ad
        interstitialViewModel.preFetchInterstitialAd(
            requireContext(),
            getString(R.string.interstitial_ad)
        ) {
            Log.d("FirstFragment", "Interstitial Ad pre-fetched successfully")
            if (interstitialAdIsAllowed) {
                Log.d("FirstFragment", "Interstitial Ad is allowed to be shown")
                interstitialViewModel.showInterstitialAd(requireActivity(), onAdDismissed = {
                    Log.d("FirstFragment", "Interstitial Ad dismissed")

                })
            } else {
                Log.d("FirstFragment", "Interstitial Ad is not allowed to be shown")
            }
        }

        // Pre-fetching Banner Ad
        bannerViewModel.loadBannerAd(requireActivity(), binding.bannerAdView) {
            Log.d("FirstFragment", "Banner Ad pre-fetched successfully")
            if (bannerAdIsAllowed) {
                Log.d("FirstFragment", "Banner Ad is allowed to be shown")
                bannerViewModel.showBannerAd(binding.bannerAdView)
            } else {
                Log.d("FirstFragment", "Banner Ad is not allowed to be shown")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Additional setup after view creation can be done here
        Log.d("FirstFragment", "View created successfully")

        binding.nextTVBtn.setSafeClickListener(2000L) {
            Log.d("FirstFragment", "Next button clicked")
            // Navigate to SecondFragment
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}