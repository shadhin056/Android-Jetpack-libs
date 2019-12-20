package com.shadhin.android_jetpack.view.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shadhin.android_jetpack.view.model.DogApiService
import com.shadhin.android_jetpack.view.model.DogModel
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.SchedulerMultiWorkerSupport
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel : ViewModel() {
    private val dogsService = DogApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogModel>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val dogsloading = MutableLiveData<Boolean>()

    fun refresh() {
        dogsloading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogModel>>() {
                    override fun onSuccess(data: List<DogModel>) {
                        dogs.value = data
                        dogsloading.value = false
                        dogsLoadError.value = false
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        dogsloading.value = false
                        e.printStackTrace()
                    }

                })

        )
        /* val dog1 = DogModel("1", "1", "1", "1", "1", "1", "1")
         val dog2 = DogModel("2", "2", "2", "2", "2", "2", "2")
         val dog3 = DogModel("3", "3", "3", "3", "3", "3", "3")
         val dogList = arrayListOf<DogModel>(dog1, dog2, dog3)
         dogs.value = dogList
         dogsLoadError.value = false
         dogsloading.value = false*/

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
