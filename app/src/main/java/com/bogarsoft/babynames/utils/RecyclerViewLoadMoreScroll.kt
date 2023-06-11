package com.bogarsoft.babynames.utils

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bogarsoft.babynames.interfaces.OnLoadMoreListener

class RecyclerViewLoadMoreScroll : RecyclerView.OnScrollListener {

    private var visibleThreshold = 10
    private lateinit var mOnLoadMoreListener: OnLoadMoreListener
    private var isLoading: Boolean = false
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var mLayoutManager: RecyclerView.LayoutManager
    private var verticleScroll  = 0
    private var currentVisibleItem = 0
    fun setLoaded() {
        isLoading = false
    }

    fun getLoaded(): Boolean {
        return isLoading
    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    constructor(layoutManager: LinearLayoutManager) {
        this.mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        verticleScroll = dy
        /*if (dy <= 0) return

        totalItemCount = mLayoutManager.itemCount

        if (mLayoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions =
                (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            // get maximum element within the list
            lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions)
        } else if (mLayoutManager is GridLayoutManager) {
            lastVisibleItem = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
        } else if (mLayoutManager is LinearLayoutManager) {
            lastVisibleItem = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
            mOnLoadMoreListener.onLoadMore()
            isLoading = true
        }*/
    }


    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE){
            Log.d(TAG, "onScrollStateChanged: its idle now")
            if (verticleScroll <= 0) return

            totalItemCount = mLayoutManager.itemCount

            if (mLayoutManager is StaggeredGridLayoutManager) {
                val lastVisibleItemPositions =
                    (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                // get maximum element within the list
                lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions)
            } else if (mLayoutManager is GridLayoutManager) {
                lastVisibleItem = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            } else if (mLayoutManager is LinearLayoutManager) {
                lastVisibleItem = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }

            Log.d(TAG, "onScrollStateChanged: "+lastVisibleItem)
            Log.d(TAG, "onScrollStateChanged: "+visibleThreshold)

            Log.d(TAG, "onScrollStateChanged: "+isLoading+" "+totalItemCount+" "+(totalItemCount <= lastVisibleItem + visibleThreshold))
            if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                mOnLoadMoreListener.onLoadMore()
                isLoading = true
            }


        }else{
            if (mLayoutManager is StaggeredGridLayoutManager) {
                val lastVisibleItemPositions =
                    (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                // get maximum element within the list
                currentVisibleItem = getLastVisibleItem(lastVisibleItemPositions)
            } else if (mLayoutManager is GridLayoutManager) {
                currentVisibleItem = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            } else if (mLayoutManager is LinearLayoutManager) {
                currentVisibleItem = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
            mOnLoadMoreListener.onScroll(currentVisibleItem)
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    fun getLastItem():Int{
        return lastVisibleItem
    }
    
    companion object {
        private const val TAG = "RecyclerViewLoadMoreScr"
    }
}