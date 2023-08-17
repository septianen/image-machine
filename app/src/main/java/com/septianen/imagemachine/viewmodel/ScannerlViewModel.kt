package com.septianen.imagemachine.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.model.Image
import com.septianen.imagemachine.model.Machine
import com.septianen.imagemachine.model.Temporary
import com.septianen.imagemachine.repository.MachineRepository
import com.septianen.imagemachine.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerlViewModel @Inject constructor(
    private val repository: MachineRepository
): ViewModel() {

    val machineLiveData: MutableLiveData<Resource<Machine>> = MutableLiveData()

    private var machine: Machine? = null

    fun getData(machineNumber: Int?) = viewModelScope.launch(Dispatchers.IO) {
        machine = machineNumber?.let { repository.getMachineByNumber(it) }
        if (machine == null) {
            machineLiveData.postValue(Resource.Error(Message.NO_DATA))
        } else {
            machineLiveData.postValue(Resource.Success(machine!!))
        }
    }
}