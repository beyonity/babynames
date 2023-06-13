package com.bogarsoft.babynames.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogarsoft.babynames.BuildConfig
import com.bogarsoft.babynames.R
import com.bogarsoft.babynames.adapters.BabyNameAdapter
import com.bogarsoft.babynames.adapters.FavoriteBabyNameAdapter
import com.bogarsoft.babynames.databinding.FragmentFavoriteBinding
import com.bogarsoft.babynames.dialogs.BabyNameInfoDialog
import com.bogarsoft.babynames.interfaces.OnLoadMoreListener
import com.bogarsoft.babynames.models.global.BabyName
import com.bogarsoft.babynames.models.local.Feed
import com.bogarsoft.babynames.utils.Constants
import com.bogarsoft.babynames.utils.RecyclerViewLoadMoreScroll
import com.bogarsoft.babynames.utils.hide
import com.bogarsoft.babynames.utils.show
import com.bogarsoft.babynames.viewModels.MainViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapter: FavoriteBabyNameAdapter
    private val list:ArrayList<Feed?> = ArrayList()
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var isLoadingAdded = false
    private lateinit var binding:FragmentFavoriteBinding
    private var currentListSize = 0
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        adapter = FavoriteBabyNameAdapter(list,requireActivity() as AppCompatActivity)

        val mLayoutManager = LinearLayoutManager(requireContext())
        binding.veilRecyclerView.run {
            setLayoutManager(mLayoutManager)
            setVeilLayout(R.layout.name_list_preview)
            addVeiledItems(10)
            setAdapter(adapter)
        }

        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                /*Handler(Looper.getMainLooper()).postDelayed({
                    Log.d(HomeFragment.TAG, "onLoadMore: $isLoadingAdded")
                    if (!isLoadingAdded) {
                        isLoadingAdded = true
                        adapter.addLoadingView()
                        lazyLoadNames()
                    }
                }, 500)*/
            }
            override fun onScroll(lastItem: Int) {
                Log.d(TAG, "onScroll: $lastItem")
                if (lastItem > 25){
                    viewModel.hideBottomNav()
                }else {
                    viewModel.showBottomNav()
                }
            }
        })
        binding.veilRecyclerView.getRecyclerView().addOnScrollListener(scrollListener)
        binding.veilRecyclerView.getRecyclerView().addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(
                rv: RecyclerView,
                e: MotionEvent
            ): Boolean {

                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }

        })

        viewModel.favRemoved.observe(viewLifecycleOwner,{id ->
            val l = list.filter { it!!.data is BabyName }
            val j = l.find { (it!!.data as BabyName).id == id }
            if (j != null){
                val index = list.indexOf(j)
                list.removeAt(index)
                adapter.notifyItemRemoved(index)
                setNoData()
            }
        })

        viewModel.newFavAdded.observe(viewLifecycleOwner,{
            if (it){
                viewModel.getFavIds()
                viewModel.newFavAdded.value = false
            }
        })

        viewModel.favIds.observe(viewLifecycleOwner,{
            Log.d(TAG, "onCreateView: $it")
                if(it.isNotEmpty()){
                    viewModel.getFavNames(it)
                }else {
                    binding.veilRecyclerView.unVeil()
                    list.clear()
                    adapter.notifyDataSetChanged()
                    setNoData()
                }
        })

        viewModel.favbabynames.observe(viewLifecycleOwner,{
            binding.veilRecyclerView.unVeil()
            list.clear()
            var adscount = 0
            it.forEachIndexed( { index,element ->

                if (index %11 == 0){
                    buildAd(list.size-1)
                    list.add(Feed(Constants.NAME_VIEW_TYPE,element))
                    adscount++
                }else {
                    list.add(Feed(Constants.NAME_VIEW_TYPE,element))
                }
            })
            adapter.notifyItemRangeInserted(
                list.size,
                (list.size + it.size + adscount)
            )
            currentListSize = currentListSize + it.size
            Log.d(TAG, "onCreateView: list size $currentListSize")
            setNoData()
        })

        adapter.setonItemClickListener(object : FavoriteBabyNameAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, babyName: BabyName) {
                val dialog = BabyNameInfoDialog(babyName)
                dialog.show(childFragmentManager,"babynameinfo")
                dialog.setOnRemoveFavListener(object : BabyNameInfoDialog.OnRemoveFavListener{
                    override fun onRemoveFav() {
                        dialog.dismiss()
                    }
                })
            }

        })

        viewModel.getFavIds()

        return binding.root
    }

    private fun setNoData(){
        if(list.isEmpty()){
            binding.noDataLayout.visibility = View.VISIBLE
        }else{
            binding.noDataLayout.visibility = View.GONE
        }
    }

    private fun buildAd(index:Int) {

        try {
            if (BuildConfig.DEBUG){
                if (list.size > index && index >= 0){
                    val builder = AdLoader.Builder(
                        requireContext(),
                        "ca-app-pub-3940256099942544/2247696110"
                    )
                    try {
                        val adLoader = builder.forNativeAd { nativead ->

                            try{
                                list.add(index,Feed(Constants.AD_VIEW_TYPE,nativead))
                            }catch (e:Exception) {
                                e.printStackTrace()
                            }




                        }.withAdListener(object : AdListener() {
                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                super.onAdFailedToLoad(loadAdError)
                                val error =
                                    """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}
          """"

                                Log.d(TAG, "onAdFailedToLoad: " + error)
                            }

                            override fun onAdLoaded() {
                                super.onAdLoaded()
                            }
                        }).build()
                        adLoader.loadAd(AdRequest.Builder().build())
                    }catch (e:Exception){
                        e.printStackTrace()
                    }


                }

            }else {
                if (list.size > index && index >= 0){
                    val builder = AdLoader.Builder(
                        requireContext(),
                        requireActivity().resources.getString(R.string.admob_fav_ad_unit)
                    )
                    try {
                        val adLoader = builder.forNativeAd { nativead ->
                            try{
                                list.add(index,Feed(Constants.AD_VIEW_TYPE,nativead))
                            }catch (e:Exception) {
                                e.printStackTrace()
                            }
                        }.withAdListener(object : AdListener() {
                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                super.onAdFailedToLoad(loadAdError)
                                val error =
                                    """
           domain: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}          """"

                                Log.d(TAG, "onAdFailedToLoad: " + error)
                            }
                            override fun onAdLoaded() {
                                super.onAdLoaded()
                            }
                        }).build()
                        adLoader.loadAd(AdRequest.Builder().build())
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }

            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    companion object {

        private const val TAG = "FavoriteFragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PageTwoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}