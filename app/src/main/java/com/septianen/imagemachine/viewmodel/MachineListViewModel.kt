package com.septianen.imagemachine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.septianen.imagemachine.constant.Message
import com.septianen.imagemachine.model.Machine
import com.septianen.imagemachine.repository.MachineRepository
import com.septianen.imagemachine.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MachineListViewModel @Inject constructor(
    private val repository: MachineRepository
): ViewModel() {

    val machineLiveData: MutableLiveData<Resource<List<Machine>>> = MutableLiveData()

    init {

        getMachines()
    }

    private fun getMachines() = viewModelScope.launch(Dispatchers.IO) {
        val machines = repository.getMachines()

        if (machines.isNullOrEmpty()) {
            machineLiveData.postValue(Resource.Error(Message.NO_DATA))
        } else {
            machineLiveData.postValue(Resource.Success(machines))
        }
    }
}