package com.slyworks.cryptocompose.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.cryptocompose.plusAssign
import com.slyworks.data.DataManager
import com.slyworks.models.CryptoModel
import com.slyworks.models.Outcome
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber


/**
 *Created by Joshua Sylvanus, 4:04 AM, 16-Jun-22.
 */
class FavoritesViewModel(private val dataManager:DataManager) : ViewModel(), IViewModel {
    //region Vars
    private val _successState:MutableLiveData<Boolean> = MutableLiveData()
    val successState:LiveData<Boolean>
    get() = _successState

    private val _successData:MutableLiveData<List<CryptoModel>> = MutableLiveData()
    val successData:LiveData<List<CryptoModel>>
    get() = _successData

    private val _noDataState:MutableLiveData<Boolean> = MutableLiveData()
    val noDataState:LiveData<Boolean>
    get() = _noDataState

    private val _noNetworkState:MutableLiveData<Boolean> = MutableLiveData()
    val noNetworkState:LiveData<Boolean>
    get() = _noNetworkState

    private val _errorState:MutableLiveData<Boolean> = MutableLiveData()
    val errorState:LiveData<Boolean>
    get() = _errorState

    private val _errorData:MutableLiveData<String> = MutableLiveData()
    val errorData:LiveData<String>
    get() = _errorData

    private val _progressState:MutableLiveData<Boolean> = MutableLiveData()
    val progressState:LiveData<Boolean>
    get() = _progressState

    private val networkState:MutableLiveData<Boolean> = MutableLiveData()

    private val disposables = CompositeDisposable()
    //endregion

    fun getFavorites() {
        disposables +=
            Observable.just(dataManager.getNetworkStatus())
                .filter { it: Boolean ->
                    if (!it) {
                        _progressState.postValue(false)
                        _noNetworkState.postValue(true)
                    }

                    it
                }
                .flatMap { dataManager.getFavorites() }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ it: Outcome ->
                    _progressState.postValue(false)

                    when {
                        it.isSuccess -> {
                            _successData.postValue(it.getTypedValue<List<CryptoModel>>())
                            _successState.postValue(true)
                        }
                        it.isError -> {
                            _errorData.postValue(it.getAdditionalInfo())
                            _errorState.postValue(true)
                        }
                }
          },{
                Timber.e(it, "getFavorites: error occurred ${it.message}")
                _errorData.postValue("an error occurred while processing your request")
                _errorState.postValue(true)
          })
    }

    override fun setItemFavoriteStatus(entity:Int, status:Boolean){
        val o: Completable =
            if (status)
                dataManager.addToFavorites(entity)
            else
                dataManager.removeFromFavorites(entity)

        disposables +=
          o.observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({},{
                Timber.e(it, "setItemFavoritesStatue: error occurred ${it.message}")
                _errorData.postValue("an error occurred while processing your request")
                _errorState.postValue(true)
            })

    }

    override fun observeNetworkState(): LiveData<Boolean> {
        disposables +=
            dataManager.observeNetworkStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                networkState.postValue(it)
            }

        return networkState
    }

    override fun onCleared() {
        super.onCleared()
        unbind()
    }

    fun unbind():Unit = disposables.clear()
}