package com.septianen.imagemachine.viewmodel

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
class MachineDetailViewModel @Inject constructor(
    private val repository: MachineRepository
): ViewModel() {

    val machineLiveData: MutableLiveData<Resource<Machine>> = MutableLiveData()
    val imagesLiveData: MutableLiveData<Resource<List<Image>>> = MutableLiveData()
    val saveItemLiveData: MutableLiveData<Resource<String>> = MutableLiveData()

    private lateinit var machine: Machine
    private lateinit var images: List<Image>

    init {
        getMachines()
    }

    private fun getMachines() = viewModelScope.launch(Dispatchers.IO) {
        machine = Temporary.machine?.copy() ?: Machine()
        getImages()

        machineLiveData.postValue(Resource.Success(machine))
    }

    private fun getImages() {

        images = machine.id?.let { repository.getImages(it) } ?: ArrayList()

        imagesLiveData.postValue(Resource.Success(images))
    }

    fun saveData(machine: Machine, imagePaths: List<Image>?) = viewModelScope.launch(Dispatchers.IO) {

        when {
            machine.name.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_MACHINE_NAME)
            machine.type.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_MACHINE_TYPE)
            machine.qrNumber.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_MACHINE_NUMBER)
            machine.date == null ->
                postErrorValidaton(Message.EMPTY_MACHINE_DATE)
            imagePaths.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_IMAGES)
            else -> {

                this@MachineDetailViewModel.images = imagePaths

                // Save Machine
                saveMachine()

                saveItemLiveData.postValue(Resource.Success(Message.SUCCESS_SAVE_DATA))
            }
        }
    }

    private fun saveMachine() = viewModelScope.launch(Dispatchers.IO) {
        if (machine.thumbnail.isNullOrEmpty()) {
            machine.thumbnail = images[0].imagePath
        }
        machine.id = repository.upsertMachine(machine)
        this@MachineDetailViewModel.machine = machine
        Temporary.machine = machine.copy()

        // Save Images
        saveImages(images)

        machineLiveData.postValue(Resource.Success(machine))
    }

    private fun saveImages(imagePaths: List<Image>) = viewModelScope.launch(Dispatchers.IO) {
        this@MachineDetailViewModel.images = imagePaths

        imagePaths.map { it.copy(machineId = machine.id) }

        imagePaths.forEach {
            it.machineId = machine.id
        }

        machine.id?.let { repository.updateImages(it, imagePaths) }

        imagesLiveData.postValue(Resource.Success(imagePaths))
    }

    private fun postErrorValidaton(message: String) {
        saveItemLiveData.postValue(Resource.Error(message))
    }

    fun deleteData(machineId: Long) = viewModelScope.launch(Dispatchers.IO) {
        machineId.let { repository.deleteMachineData(it) }
    }

    fun countMaximumImage(selectedImage: Int, savedImage: Int): Int {

        return minOf(selectedImage, (10 - savedImage))
    }
}