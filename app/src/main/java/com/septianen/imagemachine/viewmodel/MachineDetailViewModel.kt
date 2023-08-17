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
    val imagesLiveData: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    val saveItemLiveData: MutableLiveData<Resource<String>> = MutableLiveData()

    private lateinit var machine: Machine
    private lateinit var imagePaths: MutableList<String>

    init {

        getMachines()
    }

    private fun getMachines() = viewModelScope.launch(Dispatchers.IO) {
        machine = Temporary.machine?.copy() ?: Machine()
        getImages()

        machineLiveData.postValue(Resource.Success(machine))
    }

    private fun getImages() {

        imagePaths = machine.id?.let { repository.getImages(it) }?.toMutableList() ?: ArrayList()

        imagesLiveData.postValue(Resource.Success(imagePaths))
    }

    fun saveData(machine: Machine, imagePaths: List<String>?) = viewModelScope.launch(Dispatchers.IO) {

        when {
            machine.name.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_MACHINE_NAME)
            machine.type.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_MACHINE_TYPE)
            machine.qrNumber == null -> postErrorValidaton(Message.EMPTY_MACHINE_NUMBER)
            machine.date == null ->
                postErrorValidaton(Message.EMPTY_MACHINE_DATE)
            imagePaths.isNullOrEmpty() -> postErrorValidaton(Message.EMPTY_IMAGES)
            else -> {

                // Save Machine
                saveMachine()

                // Save Images
                saveImages(imagePaths)

                saveItemLiveData.postValue(Resource.Success(Message.SUCCESS_SAVE_DATA))
            }
        }
    }

    private fun saveMachine() = viewModelScope.launch(Dispatchers.IO) {
        if (machine.thumbnail.isNullOrEmpty()) {
            machine.thumbnail = imagePaths[0]
        }
        machine.id = repository.upsertMachine(machine)
        this@MachineDetailViewModel.machine = machine
        Temporary.machine = machine.copy()

        machineLiveData.postValue(Resource.Success(machine))
    }

    private fun saveImages(imagePaths: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        this@MachineDetailViewModel.imagePaths = imagePaths.toMutableList()
        val images = ArrayList<Image>()

        for (path in imagePaths) {
            images.add(
                Image(
                    machineId = machine.id,
                    imagePath = path,
                )
            )
        }

        repository.upsertImages(images)

        imagesLiveData.postValue(Resource.Success(imagePaths))
    }

    private fun postErrorValidaton(message: String) {
        saveItemLiveData.postValue(Resource.Error(message))
    }

    fun deleteImage(image: String?) = viewModelScope.launch(Dispatchers.IO) {
        if (image != null) {
            repository.deleteImage(image)
            imagePaths.remove(image)
            imagesLiveData.postValue(Resource.Success(imagePaths))
        }
    }

    fun deleteData(machine: Machine, images: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteImages(images)
        repository.deleteMachine(machine)
    }
}