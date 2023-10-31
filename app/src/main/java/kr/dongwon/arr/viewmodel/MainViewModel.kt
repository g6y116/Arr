package kr.dongwon.arr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.dongwon.arr.base.AppRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {

    fun saveData(key: String, value: Boolean) { viewModelScope.launch { repository.saveData(key, value)  } }
    fun saveData(key: String, value: String) { viewModelScope.launch { repository.saveData(key, value) } }
}