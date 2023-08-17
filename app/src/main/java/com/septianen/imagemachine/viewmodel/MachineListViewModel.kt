package com.septianen.imagemachine.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septianen.imagemachine.constant.Constant
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.model.Machine
import com.septianen.imagemachine.repository.MachineRepository
import com.septianen.imagemachine.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject


@HiltViewModel
class MachineListViewModel @Inject constructor(
    private val repository: MachineRepository
): ViewModel() {

    val machineLiveData: MutableLiveData<Resource<MutableList<Machine>>> = MutableLiveData()

    private var machines: MutableList<Machine>? = null

    init {

        getMachines()
    }

    fun getMachines() = viewModelScope.launch(Dispatchers.IO) {
        machines = repository.getMachines()?.toMutableList()

        if (machines.isNullOrEmpty()) {
            machineLiveData.postValue(Resource.Error(Message.NO_DATA))
        } else {
            machineLiveData.postValue(Resource.Success(machines!!))
        }
    }

    fun sortMachines(category: Int, sort: Int) = viewModelScope.launch(Dispatchers.IO) {


        if (machines.isNullOrEmpty()) {
            machineLiveData.postValue(Resource.Error(Message.NO_DATA))
        } else {

            machines = repository.getMachines(category, sort)?.toMutableList()

            machineLiveData.postValue(Resource.Success(machines!!))
        }
    }
}