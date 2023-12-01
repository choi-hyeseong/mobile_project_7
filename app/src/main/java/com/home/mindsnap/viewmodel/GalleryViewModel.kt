package com.home.mindsnap.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.home.mindsnap.LOG_HEADER
import com.home.mindsnap.model.Image
import com.home.mindsnap.usecase.GetAllImages
import com.home.mindsnap.usecase.ShareImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getAllImages: GetAllImages,
    private val shareImage: ShareImage
) : ViewModel() {

    val imageLiveData: MutableLiveData<List<Image>> = MutableLiveData()
    val intentLiveData: MutableLiveData<Intent> = MutableLiveData()

    init {
        getAllImage()
    }

    private fun getAllImage() {
        CoroutineScope(Dispatchers.IO).launch {
            imageLiveData.postValue(getAllImages.getAllImages())
        }
    }

    fun shareImage(fileName: String) {
        // 맞다 livedata set..
        intentLiveData.value = shareImage.shareImage(fileName)
    }
}