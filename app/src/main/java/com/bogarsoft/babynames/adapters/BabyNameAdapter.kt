package com.bogarsoft.babynames.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.bogarsoft.babynames.R
import com.bogarsoft.babynames.databinding.ListAdLayoutBinding
import com.bogarsoft.babynames.databinding.NameListBinding
import com.bogarsoft.babynames.models.global.BabyName
import com.bogarsoft.babynames.models.local.Feed
import com.bogarsoft.babynames.utils.Constants
import com.bogarsoft.babynames.utils.invisible
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class BabyNameAdapter(val list:ArrayList<Feed?>, val activity:AppCompatActivity):RecyclerView.Adapter<ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener
    fun setonItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int,babyName: BabyName)
    }



    class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    class LoadingViewHolder(view: View):RecyclerView.ViewHolder(view)
    class AdViewHolder(view: View) : RecyclerView.ViewHolder(view)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.LIST_VIEW){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.name_list,parent,false)
            ViewHolder(view)
        }else if (viewType == Constants.AD_VIEW){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_ad_layout,parent,false)
            AdViewHolder(view)
        }else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_loading_screen,parent,false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == Constants.LIST_VIEW) {
            val baby = list.get(position)?.data as BabyName
            val binding = NameListBinding.bind(holder.itemView)
            binding.name.text = baby.english
            binding.subtitle.text = baby.meaning.split(";").joinToString(",")
            binding.name.isSelected = true
            binding.subtitle.isSelected = true
            if(baby.gender.equals("Boy")){
                binding.imageView.load(R.drawable.babyboy)
            }else if(baby.gender.equals("Girl")){
                binding.imageView.load(R.drawable.babygirl)
            }else {
                binding.imageView.load(R.drawable.baby)
            }

        }else if (holder.itemViewType == Constants.AD_VIEW){
            val binding = ListAdLayoutBinding.bind(holder.itemView)
            val feed = list.get(holder.adapterPosition)
            val layout = activity.layoutInflater.inflate(R.layout.gnt_small_template_view2,binding.root,false) as NativeAdView
            feed?.let {
                val nativead = it.data as NativeAd
                populateUnifiedNativeAdView(nativead,layout)
                binding.adLayout.removeAllViews()
                binding.adLayout.addView(layout)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        list.get(position)?.let {
            return if (it.type.equals(Constants.NAME_VIEW_TYPE)){
                return Constants.LIST_VIEW
            }else {
                return Constants.AD_VIEW
            }
        }?:run{
            return Constants.LOADING_VIEW
        }


    }


    fun addLoadingView() {
        //add loading item
        Handler(Looper.getMainLooper()).post {
            list.add(null)
            notifyItemInserted(list.size - 1)
        }
    }

    fun removeLoadingView() {
        //remove loading item
        if (list.size != 0) {
            list.removeAt(list.size - 1)
            notifyItemRemoved(list.size)
        }
    }
    private fun populateUnifiedNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Set the media view.
        adView.mediaView = adView.findViewById<MediaView>(R.id.media_view)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.primary)
        adView.bodyView = adView.findViewById(R.id.body)
        adView.callToActionView = adView.findViewById(R.id.cta)
        adView.iconView = adView.findViewById(R.id.icon)
        //adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.rating_bar)
        //adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.secondary)

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline


        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to

        nativeAd.mediaContent?.let { mc ->
            adView.mediaView?.setMediaContent(mc)
        }
        //adView.mediaView.setMediaContent(nativeAd.mediaContent)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView?.invisible()
            //adView.bodyView.visibility = View.INVISIBLE
        }

        /*else {
            adView.bodyView?.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
            adView.bodyView?.isSelected = true
        }*/

        if (nativeAd.callToAction == null) {
            adView.callToActionView?.visibility = View.INVISIBLE
        } else {
            adView.callToActionView?.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView?.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon?.drawable
            )
            adView.iconView?.visibility = View.VISIBLE
        }

        /* if (nativeAd.price == null) {
             adView.priceView.visibility = View.INVISIBLE
         } else {
             adView.priceView.visibility = View.VISIBLE
             (adView.priceView as TextView).text = nativeAd.price
         }

         if (nativeAd.store == null) {
             adView.storeView.visibility = View.INVISIBLE
         } else {
             adView.storeView.visibility = View.VISIBLE
             (adView.storeView as TextView).text = nativeAd.store
         }*/

        if (nativeAd.starRating == null) {
            adView.starRatingView?.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView?.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView?.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView?.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.

    }


}