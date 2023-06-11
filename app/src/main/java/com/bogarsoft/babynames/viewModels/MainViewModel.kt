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
    fun getBabyNamesByLazyLoading(skip: Int) {
        viewModelScope.launch {
            repository.getBabyNamesByLazyLoading(skip)
        }
    }
}