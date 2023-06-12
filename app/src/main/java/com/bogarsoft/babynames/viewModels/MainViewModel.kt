package com.bogarsoft.babynames.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}