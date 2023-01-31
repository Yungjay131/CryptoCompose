package com.slyworks.cryptocompose.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.cryptocompose.plusAssign
import com.slyworks.data.DataManager
import com.slyworks.models.CryptoModel
import com.slyworks.models.Outcome
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber


/**
 *Created by Joshua Sylvanus, 2:35 PM, 01-Jun-22.
 */
class HomeViewModel(private val dataManager: DataManager) : ViewModel(), IViewModel {
    //region Vars
    private val _successDataLiveData:MutableLiveData<List<CryptoModel>> = MutableLiveData()
    val successDataLiveData: LiveData<List<CryptoModel>>
    get() = _successDataLiveData as LiveData<List<CryptoModel>>

    private val _successStateLiveData:MutableLiveData<Boolean> = MutableLiveData()
    val successStateLiveData: LiveData<Boolean>
    get() = _successStateLiveData

    private val _failureDataLiveData:MutableLiveData<String> = MutableLiveData()
    val failureDataLiveData:LiveData<String>
    get() = _failureDataLiveData as LiveData<String>

    private val _failureStateLiveData:MutableLiveData<Boolean>  = MutableLiveData()
    val failureStateLiveData:LiveData<Boolean>
    get() = _failureStateLiveData as LiveData<Boolean>

    private val _networkErrorStateLive:MutableLiveData<Boolean> = MutableLiveData()
    val networkStateLiveData:LiveData<Boolean>
    get() = _networkErrorStateLive as LiveData<Boolean>

    private val _progressStateLiveData:MutableLiveData<Boolean> = MutableLiveData()
    val progressStateLiveData:LiveData<Boolean>
    get() = _progressStateLiveData as LiveData<Boolean>


    private val disposables = CompositeDisposable()
    //endregion

    fun getData(){
        _successStateLiveData.postValue(false)
        _failureStateLiveData.postValue(false)
        _progressStateLiveData.postValue(true)

        disposables +=
        dataManager.getData()
                   .subscribeOn(Schedulers.io())
                   .observeOn(Schedulers.io())
                   .subscribe(
                       {
                           /* resetting all the fields first */
                           _progressStateLiveData.postValue(false)
                           _successStateLiveData.postValue(false)
                           _failureStateLiveData.postValue(false)

                           if(it.isSuccess){
                              _successDataLiveData.postValue(it.getTypedValue<List<CryptoModel>>())
                              _successStateLiveData.postValue(true)
                           }else{
                              _failureDataLiveData.postValue(it.getTypedValue<String>())
                               _failureStateLiveData.postValue(true)
                           }
                       },
                       {
                           Timber.e(it,"getData: error occurred ${it.message}")
                           _failureDataLiveData.postValue("an error occurred while processing your request")
                       })
    }


    override fun setItemFavoriteStatus(entity:Int, status:Boolean){
        disposables += //type Completable
            (if (status)
                dataManager.addToFavorites(entity)
            else
                dataManager.removeFromFavorites(entity))
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({}, {
                Timber.e(it,"setItemFavorites: error occurred  ${it.message}")
                _failureDataLiveData.postValue("an error occurred while processing your request")
            })
    }

    override fun observeNetworkState(): LiveData<Boolean> {
        val l:MutableLiveData<Boolean> = MutableLiveData()
        val filureFunc:(e:Throwable) -> Unit = {
            Timber.e(it, "observeNetworkStatus: error occurred  ${it.message}")
            _failureDataLiveData.postValue("an error occurred while processing your request")
        }

        disposables +=
            dataManager.observeNetworkStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(l::postValue, filureFunc)
        return l
    }

    override fun onCleared() {
        super.onCleared()
        unbind()
    }

    fun unbind():Unit {
        _successStateLiveData.postValue(false)
        _progressStateLiveData.postValue(true)
        disposables.clear()
    }
}