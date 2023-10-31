package kr.dongwon.arr.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.dongwon.arr.base.AppRepository
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {


}