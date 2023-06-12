package com.bogarsoft.babynames.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.bogarsoft.babynames.BuildConfig
import com.bogarsoft.babynames.R
import com.bogarsoft.babynames.interfaces.OnClickListener
import com.bogarsoft.babynames.models.local.MenuItem
import com.bogarsoft.babynames.viewholders.MenuViewHolder
import coil.request.CachePolicy
import com.bogarsoft.babynames.utils.RequestHeaderInterceptor

import okhttp3.OkHttpClient
class MenuAdapter(val list:List<MenuItem>,val activity: AppCompatActivity):RecyclerView.Adapter<MenuViewHolder>(){
    var imageLoader: ImageLoader = ImageLoader.Builder(activity)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .okHttpClient {
            OkHttpClient.Builder()
                // This header will be added to every image request.
                .addNetworkInterceptor(RequestHeaderInterceptor("api_key", BuildConfig.APP_TOKEN))
                .build()
        }
        .build()
    private lateinit var onItemClickListener: OnItemClickListener
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int, item: MenuItem)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_list,parent,false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = list[holder.adapterPosition]
        holder.bind(item,imageLoader,object : OnClickListener{
            override fun onClick() {
                if(::onItemClickListener.isInitialized)
                    onItemClickListener.onItemClick(position,item)
            }

        })
    }

}