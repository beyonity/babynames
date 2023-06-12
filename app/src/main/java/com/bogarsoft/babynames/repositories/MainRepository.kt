package com.bogarsoft.babynames.repositories

import androidx.lifecycle.MutableLiveData
import com.bogarsoft.babynames.App
import com.bogarsoft.babynames.models.global.BabyName
import com.bogarsoft.babynames.models.global.Naksathra
import com.bogarsoft.babynames.models.global.Rashi
import com.bogarsoft.babynames.models.global.Religion
import com.bogarsoft.babynames.network.ApiService
import com.bogarsoft.babynames.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRepository(val apiService: ApiService) {


    val isLoading = MutableLiveData<Boolean>()
    val babynames = MutableLiveData<ArrayList<BabyName>>()
    val religions = MutableLiveData<ArrayList<Religion>>()
    val rashis = MutableLiveData<ArrayList<Rashi>>()
    val nakshatras = MutableLiveData<ArrayList<Naksathra>>()


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


}