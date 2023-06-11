package com.bogarsoft.babynames.repositories

import androidx.lifecycle.MutableLiveData
import com.bogarsoft.babynames.models.global.BabyName
import com.bogarsoft.babynames.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRepository(val apiService: ApiService) {


    val isLoading = MutableLiveData<Boolean>()
    val babynames = MutableLiveData<ArrayList<BabyName>>()


    suspend fun getBabyNamesByLazyLoading(skip: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                isLoading.postValue(true)
                val response = apiService.getBabyNamesByLazyLoading(skip)
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


}