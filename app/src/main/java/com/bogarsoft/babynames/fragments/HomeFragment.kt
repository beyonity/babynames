package com.bogarsoft.babynames.fragments

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bogarsoft.babynames.App
import com.bogarsoft.babynames.BuildConfig
import com.bogarsoft.babynames.R
import com.bogarsoft.babynames.adapters.BabyNameAdapter
import com.bogarsoft.babynames.adapters.MenuAdapter
import com.bogarsoft.babynames.databinding.AlphabetListBinding
import com.bogarsoft.babynames.databinding.AlphabetalertdialogBinding
import com.bogarsoft.babynames.databinding.FragmentHomeBinding
import com.bogarsoft.babynames.models.local.Feed
import com.bogarsoft.babynames.utils.RecyclerViewLoadMoreScroll
import com.bogarsoft.babynames.interfaces.OnLoadMoreListener
import com.bogarsoft.babynames.models.local.MenuItem
import com.bogarsoft.babynames.utils.Constants
import com.bogarsoft.babynames.utils.Helper
import com.bogarsoft.babynames.utils.StorageUtil
import com.bogarsoft.babynames.utils.hide
import com.bogarsoft.babynames.utils.show
import com.bogarsoft.babynames.viewModels.MainViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.chip.Chip


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding:FragmentHomeBinding
    private lateinit var religionAdapter:MenuAdapter
    private lateinit var rashiAdapter: MenuAdapter
    private lateinit var nakshatraAdapter: MenuAdapter

    private val religionList = ArrayList<MenuItem>()
    private val rashiList = ArrayList<MenuItem>()
    private val nakshatraList = ArrayList<MenuItem>()

    private lateinit var adapter: BabyNameAdapter
    private val list:ArrayList<Feed?> = ArrayList()
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var isLoadingAdded = false
    private var currentListSize = 0
    private var SELECTED_MENU = Constants.SELECTED_TAB.HOME
    private var SELECTED_ID = 0
    private var SELECTED_ALPHABET = "A"
    private val SELECTED_GENDERS = ArrayList<String>()
    private lateinit var drawerLayout: DrawerLayout
    private var backPressedTime: Long = 0
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var storageUtil: StorageUtil
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
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        storageUtil = StorageUtil(requireContext())
        SELECTED_MENU = App.prefs.pull(Constants.SELECTED_MENU,Constants.SELECTED_TAB.HOME)
        SELECTED_ID = App.prefs.pull(Constants.SELECTED_ID,0)
        SELECTED_ALPHABET = App.prefs.pull(Constants.ALPHABET_FILTER,"A")
        SELECTED_GENDERS.clear()
        SELECTED_GENDERS.addAll(storageUtil.genderArray)

        drawerLayout = binding.root.findViewById(R.id.drawer_layout)
        binding.maincontent.topbar.mainmenu.setOnClickListener {
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }else{
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        religionAdapter = MenuAdapter(religionList,requireActivity() as AppCompatActivity)
        binding.religionlist.layoutManager = LinearLayoutManager(requireContext())
        binding.religionlist.adapter = religionAdapter


        rashiAdapter = MenuAdapter(rashiList,requireActivity() as AppCompatActivity)
        binding.rashilist.layoutManager = LinearLayoutManager(requireContext())
        binding.rashilist.adapter = rashiAdapter

        nakshatraAdapter = MenuAdapter(nakshatraList,requireActivity() as AppCompatActivity)
        binding.nakshatralist.layoutManager = LinearLayoutManager(requireContext())
        binding.nakshatralist.adapter = nakshatraAdapter

        adapter = BabyNameAdapter(list,requireActivity() as AppCompatActivity)

        val mLayoutManager = LinearLayoutManager(requireContext())
        binding.maincontent.veilRecyclerView.run {
            setLayoutManager(mLayoutManager)
            setVeilLayout(R.layout.name_list_preview)
            addVeiledItems(10)
            setAdapter(adapter)
        }

        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                Handler(Looper.getMainLooper()).postDelayed({
                    Log.d(TAG, "onLoadMore: $isLoadingAdded")
                    if (!isLoadingAdded) {
                        isLoadingAdded = true
                        adapter.addLoadingView()
                        lazyLoadNames()
                    }
                }, 500)
            }
            override fun onScroll(lastItem: Int) {
                Log.d(TAG, "onScroll: $lastItem")
                if (lastItem > 25){
                    binding.maincontent.totop.show()
                }else {
                    binding.maincontent.totop.hide()
                }
            }
        })
        binding.maincontent.veilRecyclerView.getRecyclerView().addOnScrollListener(scrollListener)
        binding.maincontent.veilRecyclerView.getRecyclerView().addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
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

        viewModel.babynames.observe(viewLifecycleOwner,{
            binding.maincontent.veilRecyclerView.unVeil()
            binding.maincontent.progressBar.hide()
            scrollListener.setLoaded()
            if (isLoadingAdded){
                adapter.removeLoadingView()
                isLoadingAdded = false
            }
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

        viewModel.religions.observe(viewLifecycleOwner,{
            religionList.clear()
            it.forEach { religion ->
                Log.d(TAG, "onCreateView: ${Helper.getImage(religion.image)}")
                religionList.add(MenuItem(religion.id, Helper.getImage(religion.image),religion.name))
            }
            religionAdapter.notifyDataSetChanged()
        })

        viewModel.rashis.observe(viewLifecycleOwner,{
            rashiList.clear()
            it.forEach { rashi ->
                Log.d(TAG, "onCreateView: ${Helper.getImage(rashi.image)}")
                rashiList.add(MenuItem(rashi.id, Helper.getImage(rashi.image),rashi.name))
            }
            rashiAdapter.notifyDataSetChanged()
        })


        viewModel.nakshatras.observe(viewLifecycleOwner,{
            nakshatraList.clear()
            it.forEach { nakshatra ->
                Log.d(TAG, "onCreateView: ${Helper.getImage(nakshatra.image)}")
                nakshatraList.add(MenuItem(nakshatra.id, Helper.getImage(nakshatra.image),nakshatra.name))
            }
            nakshatraAdapter.notifyDataSetChanged()
        })

        binding.homecard.setOnClickListener {
            SELECTED_ID = 0
            SELECTED_MENU = Constants.SELECTED_TAB.HOME
            App.prefs.push(Constants.SELECTED_MENU,SELECTED_MENU)
            App.prefs.push(Constants.SELECTED_ID,SELECTED_ID)
            setSelectedContent()
            freshLoadingData()
        }

        binding.religioncard.setOnClickListener {
            if(binding.religionlist.visibility == View.VISIBLE) {
                binding.religionlist.visibility = View.GONE
                binding.religionarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            }else{
                binding.religionlist.visibility = View.VISIBLE
                binding.religionarrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            }
        }

        binding.rashicard.setOnClickListener {
            if(binding.rashilist.visibility == View.VISIBLE) {
                binding.rashilist.visibility = View.GONE
                binding.rashiarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            }else{
                binding.rashilist.visibility = View.VISIBLE
                binding.rashiarrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            }
        }

        binding.nakshatracard.setOnClickListener {
            if(binding.nakshatralist.visibility == View.VISIBLE) {
                binding.nakshatralist.visibility = View.GONE
                binding.nakshatraarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            }else{
                binding.nakshatralist.visibility = View.VISIBLE
                binding.nakshatraarrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            }
        }

        binding.religions.setOnClickListener {
            if(binding.religionlist.visibility == View.VISIBLE) {
                binding.religionlist.visibility = View.GONE
                binding.religionarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            }else{
                binding.religionlist.visibility = View.VISIBLE
                binding.religionarrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            }
        }

        binding.rashis.setOnClickListener {
            if(binding.rashilist.visibility == View.VISIBLE) {
                binding.rashilist.visibility = View.GONE
                binding.rashiarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            }else{
                binding.rashilist.visibility = View.VISIBLE
                binding.rashiarrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            }
        }

        binding.nakshatras.setOnClickListener {
            if(binding.nakshatralist.visibility == View.VISIBLE) {
                binding.nakshatralist.visibility = View.GONE
                binding.nakshatraarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
            }else{
                binding.nakshatralist.visibility = View.VISIBLE
                binding.nakshatraarrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
            }
        }

        religionAdapter.setOnItemClickListener(object : MenuAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, item: MenuItem) {
                SELECTED_MENU = Constants.SELECTED_TAB.RELIGION
                SELECTED_ID = item.id
                App.prefs.push(Constants.SELECTED_MENU,SELECTED_MENU)
                App.prefs.push(Constants.SELECTED_ID,SELECTED_ID)
                setSelectedContent()
                freshLoadingData()
            }

        })

        rashiAdapter.setOnItemClickListener(object : MenuAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, item: MenuItem) {
                SELECTED_MENU = Constants.SELECTED_TAB.RASHI
                SELECTED_ID = item.id
                App.prefs.push(Constants.SELECTED_MENU,SELECTED_MENU)
                App.prefs.push(Constants.SELECTED_ID,SELECTED_ID)
                setSelectedContent()
                freshLoadingData()
            }

        })

        nakshatraAdapter.setOnItemClickListener(object : MenuAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, item: MenuItem) {
                SELECTED_MENU = Constants.SELECTED_TAB.NAKSHATRA
                SELECTED_ID = item.id
                App.prefs.push(Constants.SELECTED_MENU,SELECTED_MENU)
                App.prefs.push(Constants.SELECTED_ID,SELECTED_ID)
                setSelectedContent()
                freshLoadingData()
            }

        })

        binding.maincontent.alphabet.text = SELECTED_ALPHABET
        binding.maincontent.alphabet.setOnClickListener {
            showAlphabetDialog()
        }
        binding.maincontent.alphabetarrow.setOnClickListener {
            showAlphabetDialog()
        }

        binding.maincontent.gendergroup.setOnCheckedStateChangeListener { group, checkedIds ->

            val genders = arrayListOf<String>()
            Log.d(TAG, "onCreateView: $checkedIds")
            checkedIds.forEach {
                when(it){
                    R.id.boy -> genders.add("Boy")
                    R.id.girl -> genders.add("Girl")
                    R.id.unisex -> genders.add("Unisex")
                }
            }
            storageUtil.genderArray = genders
            SELECTED_GENDERS.clear()
            SELECTED_GENDERS.addAll(genders)
            freshLoadingData()

        }


        SELECTED_GENDERS.forEach {
            when(it){
                "Boy" -> binding.maincontent.gendergroup.post { binding.maincontent.boy.isChecked = true }
                "Girl" -> binding.maincontent.gendergroup.post { binding.maincontent.girl.isChecked = true }
                "Unisex" -> binding.maincontent.gendergroup.post { binding.maincontent.unisex.isChecked = true }
            }
        }

        viewModel.getreligionbydata()
        viewModel.getRashi()
        viewModel.getNakshatram()
        lazyLoadNames()
        setSelectedContent()
        return binding.root
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

                            list.add(index,Feed(Constants.AD_VIEW_TYPE,nativead))


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
                        requireActivity().resources.getString(R.string.admob_names_ad_unit)
                    )
                    try {
                        val adLoader = builder.forNativeAd { nativead ->
                            list.add(index,Feed(Constants.AD_VIEW_TYPE,nativead))
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


    private fun lazyLoadNames() {
        viewModel.getBabyNamesByLazyLoading(currentListSize,SELECTED_GENDERS)
    }

    private fun setSelectedContent(){

        when(SELECTED_MENU){
            Constants.SELECTED_TAB.HOME ->{
                binding.homecard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.selection_color))
                binding.religionlist.visibility = View.GONE
                binding.religionarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
                binding.rashilist.visibility = View.GONE
                binding.rashiarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
                binding.nakshatralist.visibility = View.GONE
                binding.nakshatraarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
                religionList.forEach { it.isSelected = false }
                rashiList.forEach { it.isSelected = false }
                nakshatraList.forEach { it.isSelected = false }
                religionAdapter.notifyDataSetChanged()
                rashiAdapter.notifyDataSetChanged()
                nakshatraAdapter.notifyDataSetChanged()




            }
            Constants.SELECTED_TAB.RELIGION -> {
                binding.homecard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.transparent))
                binding.religionlist.visibility = View.VISIBLE
                binding.religionarrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
                binding.rashilist.visibility = View.GONE
                binding.rashiarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
                binding.nakshatralist.visibility = View.GONE
                binding.nakshatraarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
                religionList.forEach { it.isSelected = false }
                rashiList.forEach { it.isSelected = false }
                nakshatraList.forEach { it.isSelected = false }
                religionAdapter.notifyDataSetChanged()
                rashiAdapter.notifyDataSetChanged()
                nakshatraAdapter.notifyDataSetChanged()

                religionList.find { it.id == SELECTED_ID }?.let {
                    it.isSelected = true
                    religionAdapter.notifyDataSetChanged()
                }

            }
            Constants.SELECTED_TAB.RASHI -> {
                binding.homecard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.transparent))
                binding.religionlist.visibility = View.GONE
                binding.religionarrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
                binding.rashilist.visibility = View.VISIBLE
                binding.rashiarrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
                binding.nakshatralist.visibility = View.GONE
                binding.nakshatraarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
                religionList.forEach { it.isSelected = false }
                rashiList.forEach { it.isSelected = false }
                nakshatraList.forEach { it.isSelected = false }
                religionAdapter.notifyDataSetChanged()
                rashiAdapter.notifyDataSetChanged()
                nakshatraAdapter.notifyDataSetChanged()

                rashiList.find { it.id == SELECTED_ID }?.let {
                    it.isSelected = true
                    rashiAdapter.notifyDataSetChanged()
                }

            }
            Constants.SELECTED_TAB.NAKSHATRA -> {
                binding.homecard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.transparent))

                binding.religionlist.visibility = View.GONE
                binding.religionarrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
                binding.nakshatralist.visibility = View.GONE
                binding.rashiarrow.setImageResource(R.drawable.baseline_arrow_drop_down_24)
                binding.nakshatralist.visibility = View.VISIBLE
                binding.nakshatraarrow.setImageResource(R.drawable.baseline_arrow_drop_up_24)
                religionList.forEach { it.isSelected = false }
                rashiList.forEach { it.isSelected = false }
                nakshatraList.forEach { it.isSelected = false }
                religionAdapter.notifyDataSetChanged()
                rashiAdapter.notifyDataSetChanged()
                nakshatraAdapter.notifyDataSetChanged()

                nakshatraList.find { it.id == SELECTED_ID }?.let {
                    it.isSelected = true
                    nakshatraAdapter.notifyDataSetChanged()
                }


            }
        }

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }



    }

    private fun freshLoadingData(){
        list.clear()
        currentListSize = 0
        adapter.notifyDataSetChanged()
        binding.maincontent.progressBar.show()

        lazyLoadNames()
    }


    private fun setNoData(){
        if(list.isEmpty()){
            binding.maincontent.noDataLayout.visibility = View.VISIBLE
        }else{
            binding.maincontent.noDataLayout.visibility = View.GONE
        }
    }



    private fun showAlphabetDialog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val view = AlphabetalertdialogBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(view.root)
        val array = arrayListOf<String>("A","B","C","D","E","F","G","H","I","J","K","L","M","N",
            "O","P","Q","R","S","T","U","V","W","X","Y","Z")
        array.forEach {
            val chip = Chip(requireContext())
            chip.text = it
            chip.isCheckable = true
            chip.checkedIcon = ContextCompat.getDrawable(requireContext(),R.drawable.baseline_check_24)
            chip.isChecked = SELECTED_ALPHABET == it
            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    SELECTED_ALPHABET = it
                    App.prefs.push(Constants.ALPHABET_FILTER,it)
                    binding.maincontent.alphabet.setText(it)
                    freshLoadingData()
                    dialog.dismiss()
                }

            }
            view.chipGroup.addView(chip)
        }

        dialog.show()
    }


    companion object {
        private const val TAG = "HomeFragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
