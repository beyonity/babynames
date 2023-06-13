package com.bogarsoft.babynames.viewholders

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.bogarsoft.babynames.R
import com.bogarsoft.babynames.databinding.MenuListBinding
import com.bogarsoft.babynames.interfaces.OnClickListener
import com.bogarsoft.babynames.models.local.MenuItem

class MenuViewHolder(val view: View) : RecyclerView.ViewHolder(view){
   private var binding:MenuListBinding = MenuListBinding.bind(view)
    fun bind(item:MenuItem,imageLoader:ImageLoader,onClickListener: OnClickListener){
        val title = "${item.title} ${if(item.letters.isNotEmpty()) "(${item.letters})" else ""}"
        binding.name.text = title
        binding.icon.load(item.icon){
            crossfade(true)
            crossfade(100)
        }

        val request = ImageRequest.Builder(view.context)
            .data(item.icon)
            .target(binding.icon)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .crossfade(true)
            .crossfade(500)
            .build()

        imageLoader.enqueue(request)
        binding.menucard.setOnClickListener {
            onClickListener.onClick()
        }

        if (item.isSelected){
            binding.menucard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context,R.color.selection_color))
        }else {
            binding.menucard.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context,R.color.transparent))
        }
    }
}