package com.bogarsoft.babynames.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bogarsoft.babynames.R
import com.bogarsoft.babynames.databinding.NameinfoDialogBinding
import com.bogarsoft.babynames.models.global.BabyName
import com.bogarsoft.babynames.utils.hide
import com.bogarsoft.babynames.utils.show
import com.bogarsoft.babynames.viewModels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BabyNameInfoDialog(val babyName: BabyName):BottomSheetDialogFragment(){


    private lateinit var onRemoveFavListener: OnRemoveFavListener
    fun setOnRemoveFavListener(onRemoveFavListener: OnRemoveFavListener){
        this.onRemoveFavListener = onRemoveFavListener
    }
    interface OnRemoveFavListener{
        fun onRemoveFav()
    }

    private val viewModel: MainViewModel by activityViewModels()
    private var isFav = false
    private lateinit var binding:NameinfoDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(STYLE_NO_FRAME, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NameinfoDialogBinding.inflate(inflater, container, false)

        if(babyName.meaning.isNotEmpty()) {
            binding.meaning.text = babyName.meaning.replace(";",",")


        }else {
            binding.meaning.text = getString(R.string.will_be_updated_soon)
        }

        binding.name.isSelected = true

        binding.name.text = babyName.english
        binding.gender.text = babyName.gender
        binding.religion.text = babyName.religion.name
        binding.numerology.text = babyName.numerology.toString()
        babyName.rashi?.let {
            binding.rashi.text = "${it.name} (${it.letters})"
            binding.rashilayout.show()
        }?:run{
            binding.rashilayout.hide()
        }

        babyName.naksathra?.let {
            binding.nakshatra.text = "${it.name} (${it.letters})"
            binding.nakshatralayout.show()
        }?:run{
            binding.nakshatralayout.hide()
        }

        if(binding.rashilayout.visibility == View.GONE && binding.nakshatralayout.visibility == View.GONE) {
            binding.horoscopelayout.hide()
        }else {
            binding.horoscopelayout.show()
        }

        viewModel.isFav.observe(viewLifecycleOwner, {
            isFav = it
            if(it) {

                binding.favorite.setImageResource(R.drawable.favorite_filled)
            }else {
                binding.favorite.setImageResource(R.drawable.favorite)
            }
        })

        binding.favoritecard.setOnClickListener {
            if(isFav) {
                viewModel.removeFav(babyName)

            }else {
                viewModel.addFav(babyName)
            }
            if(::onRemoveFavListener.isInitialized) {
                onRemoveFavListener.onRemoveFav()
            }
        }

        viewModel.isFav(babyName.id)

        return binding.root
    }





    companion object{
        private const val TAG = "SongDetailsPopUpDialog"
    }

}
