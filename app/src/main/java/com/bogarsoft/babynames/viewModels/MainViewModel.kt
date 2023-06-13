package com.bogarsoft.babynames.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogarsoft.babynames.models.global.BabyName
import com.bogarsoft.babynames.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(val repository: MainRepository):ViewModel(){
    val isLoading = repository.isLoading
    val babynames = repository.babynames
    val religions = repository.religions
    val rashis = repository.rashis
    val nakshatras = repository.nakshatras
    val hideBottomNav = repository.hideBottomNav
    val isFav = repository.isFav
    val favIds = repository.favIds
    val favbabynames = repository.favbabynames
    val favRemoved = repository.favRemoved
    val newFavAdded = repository.newFavAdded
    val appupdate = repository.appudpate
    fun getBabyNamesByLazyLoading(skip: Int,gender:ArrayList<String>) {
        viewModelScope.launch {
            repository.getBabyNamesByLazyLoading(skip,gender)
        }
    }

    fun getreligionbydata(){
        viewModelScope.launch {
            repository.getreligionbydata()
        }
    }

    fun getRashi(){
        viewModelScope.launch {
            repository.getRashi()
        }
    }

    fun getNakshatram(){
        viewModelScope.launch {
            repository.getNakshatra()
        }
    }

    fun isFav(id:Int){
        viewModelScope.launch {
            repository.isFav(id)
        }
    }

    fun addFav(babyName: BabyName){
        viewModelScope.launch {
            repository.addFav(babyName)
        }
    }

    fun removeFav(babyName: BabyName){
        viewModelScope.launch {
            repository.removeFav(babyName)
        }
    }

    fun getFavIds(){
        viewModelScope.launch {
            repository.getFavids()
        }
    }

    fun getFavNames(ids:ArrayList<Int>){
        viewModelScope.launch {
            repository.getFavBabyNames(ids)
        }
    }

    fun hideBottomNav(){
        viewModelScope.launch {
            repository.hideBottomNav()
        }
    }

    fun showBottomNav(){
        viewModelScope.launch {
            repository.showBottomNav()
        }
    }

    fun getAppUpdate(){
        viewModelScope.launch {
            repository.getAppUpdate()
        }
    }
}