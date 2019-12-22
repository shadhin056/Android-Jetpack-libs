package com.shadhin.android_jetpack.view.view_model

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shadhin.android_jetpack.view.model.DogApiService
import com.shadhin.android_jetpack.view.model.DogDatabase
import com.shadhin.android_jetpack.view.model.DogModel
import com.shadhin.android_jetpack.view.util.NotificationHelper
import com.shadhin.android_jetpack.view.util.SharedPreferencesHelper
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.SchedulerMultiWorkerSupport
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {

    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L
    private val dogsService = DogApiService()
    private val disposable = CompositeDisposable()
    private var prefHelper = SharedPreferencesHelper(getApplication())
    val dogs = MutableLiveData<List<DogModel>>()
    val dogsLoadError = MutableLiveData<Boolean>()
    val dogsloading = MutableLiveData<Boolean>()

    fun refresh() {
        val updateTime = prefHelper.getUpdateTIme()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDB()
        } else {
            fetchFromRemote()
        }

        /* val dog1 = DogModel("1", "1", "1", "1", "1", "1", "1")
         val dog2 = DogModel("2", "2", "2", "2", "2", "2", "2")
         val dog3 = DogModel("3", "3", "3", "3", "3", "3", "3")
         val dogList = arrayListOf<DogModel>(dog1, dog2, dog3)
         dogs.value = dogList
         dogsLoadError.value = false
         dogsloading.value = false*/

    }

    private fun fetchFromDB() {
        dogsloading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrived(dogs)
            Toast.makeText(getApplication(), "From Database", Toast.LENGTH_LONG).show()
        }

    }

    private fun fetchFromRemote() {
        dogsloading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogModel>>() {
                    override fun onSuccess(data: List<DogModel>) {
                        storeDogsLocally(data)
                        Toast.makeText(getApplication(), "From Remote", Toast.LENGTH_LONG).show()
                        NotificationHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        dogsloading.value = false
                        e.printStackTrace()
                    }

                })

        )
    }

    private fun dogsRetrived(data: List<DogModel>) {
        dogs.value = data
        dogsloading.value = false
        dogsLoadError.value = false
    }

    fun refreshBypassCacheDB() {
        fetchFromRemote()
    }

    private fun storeDogsLocally(data: List<DogModel>) {
        launch {
            val dog = DogDatabase(getApplication()).dogDao()
            dog.deleteAllDogs()
            val result = dog.insertAll(*data.toTypedArray())
            var i = 0
            while (i < data.size) {
                data[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrived(data)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
