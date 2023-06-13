package com.bogarsoft.babynames.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bogarsoft.babynames.App
import com.bogarsoft.babynames.database.dao.FavoriteNameDao
import com.bogarsoft.babynames.models.global.AppUpdate
import com.bogarsoft.babynames.models.global.BabyName
import com.bogarsoft.babynames.models.global.Naksathra
import com.bogarsoft.babynames.models.global.Rashi
import com.bogarsoft.babynames.models.global.Religion
import com.bogarsoft.babynames.models.local.FavoriteName
import com.bogarsoft.babynames.network.ApiService
import com.bogarsoft.babynames.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRepository(val dao: FavoriteNameDao,val apiService: ApiService) {


    val isLoading = MutableLiveData<Boolean>()
    val babynames = MutableLiveData<ArrayList<BabyName>>()
    val favbabynames = MutableLiveData<ArrayList<BabyName>>()
    val religions = MutableLiveData<ArrayList<Religion>>()
    val rashis = MutableLiveData<ArrayList<Rashi>>()
    val nakshatras = MutableLiveData<ArrayList<Naksathra>>()
    val hideBottomNav = MutableLiveData<Boolean>()
    val favIds = MutableLiveData<ArrayList<Int>>()
    val appudpate = MutableLiveData<AppUpdate>()
    val isFav = MutableLiveData<Boolean>()
    val favRemoved = MutableLiveData<Int>()
    val newFavAdded = MutableLiveData<Boolean>()

    suspend fun getBabyNamesByLazyLoading(skip: Int,genders:ArrayList<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                isLoading.postValue(true)
                val SELECTED_MENU = App.prefs.pull(Constants.SELECTED_MENU, Constants.SELECTED_TAB.HOME)
                val  SELECTED_ID = App.prefs.pull(Constants.SELECTED_ID,0)
                val SELECTED_ALPHABET = App.prefs.pull(Constants.ALPHABET_FILTER,"A")
                val response = apiService.getBabyNamesByLazyLoading(skip,SELECTED_ALPHABET,genders,SELECTED_MENU.toString(),SELECTED_ID)
                if (response.isSuccessful) {
                    babynames.postValue(response.body()?.data)
                }
                isLoading.postValue(false)
            }catch (e:Exception){
                e.printStackTrace()
                isLoading.postValue(false)
            }
        }


    }

    suspend fun getreligionbydata(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = apiService.getReligionsByData()
                if (result.isSuccessful){
                    religions.postValue(result.body()?.data as ArrayList<Religion>?)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun getRashi(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = apiService.getRashi()
                if (result.isSuccessful){
                    rashis.postValue(result.body()?.data as ArrayList<Rashi>?)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun getNakshatra(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = apiService.getNakshatram()
                if (result.isSuccessful){
                    nakshatras.postValue(result.body()?.data as ArrayList<Naksathra>?)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun addFav(babyName: BabyName){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dao.insert(FavoriteName(babyId = babyName.id))
                isFav(babyName.id)
                newFavAdded.postValue(true)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun removeFav(babyName: BabyName){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dao.deleteByBabyId(babyName.id)
                isFav(babyName.id)
                favRemoved.postValue(babyName.id)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun isFav(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dao.getByBabyId(id)?.let {
                    Log.d(TAG, "isFav: $it")
                    isFav.postValue(true)
                }?:run {
                    isFav.postValue(false)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun getFavids(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = dao.getAll()
                Log.d(TAG, "getFavids: $result")
                val ids = ArrayList<Int>()
                result.forEach {
                    ids.add(it.babyId)
                }
                favIds.postValue(ids)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun getFavBabyNames(ids:ArrayList<Int>){
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val response = apiService.getBabyNamesByIds(ids)
                if (response.isSuccessful){
                    favbabynames.postValue(response.body()?.data)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun getAppUpdate(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = apiService.getAppUpdate()
                if (result.isSuccessful){
                    result.body()?.let { body ->
                        appudpate.postValue(body.data)
                    }
                }
            }catch (e:Exception) {
                e.printStackTrace()
            }
        }
    }

    fun hideBottomNav(){
        hideBottomNav.postValue(true)
    }
    fun showBottomNav(){
        hideBottomNav.postValue(false)
    }

    companion object{
        val TAG = "MainRepository"
    }


}