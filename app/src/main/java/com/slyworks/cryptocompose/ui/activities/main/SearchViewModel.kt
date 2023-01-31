package com.slyworks.cryptocompose.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay3.PublishRelay
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.cryptocompose.plusAssign
import com.slyworks.data.DataManager
import com.slyworks.data.SpecificCryptoSearchType
import com.slyworks.models.CryptoModelCombo
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber


/**
 *Created by Joshua Sylvanus, 4:02 AM, 16-Jun-22.
 */
class SearchViewModel(private var dataManager: DataManager) : ViewModel(), IViewModel {
    //region Vars
    val searchObservable: PublishRelay<String> = PublishRelay.create()

    private val _successState:MutableLiveData<Boolean> = MutableLiveData()
    val successState:LiveData<Boolean>
    get() = _successState

    private val _successData:MutableLiveData<CryptoModelCombo> = MutableLiveData()
    val successData:LiveData<CryptoModelCombo>
    get() = _successData

    private val _failureState:MutableLiveData<Boolean> = MutableLiveData()
    val failureState:LiveData<Boolean>
    get() = _failureState

    private val _failureData:MutableLiveData<String> = MutableLiveData()
    val failureData:LiveData<String>
    get() = _failureData

    private val _errorState:MutableLiveData<Boolean> = MutableLiveData()
    val errorState:LiveData<Boolean>
    get() = _errorState

    private val _errorData:MutableLiveData<String> = MutableLiveData()
    val errorData:LiveData<String>
    get() = _errorData

    private val _noNetworkStateLiveData:MutableLiveData<Boolean> = MutableLiveData()
    val noNetworkStateLiveData:LiveData<Boolean>
    get() = _noNetworkStateLiveData

    private val _progressStateLiveData:MutableLiveData<Boolean> = MutableLiveData()
    val progressStateLiveData:LiveData<Boolean>
    get() = _progressStateLiveData

    lateinit var query:String

    private val disposables = CompositeDisposable()
    //endregion

    init {
        initSearch()
    }

    private fun initSearch(){
        /*fixme: mutating outside state before subscribe()*/
        disposables +=
            searchObservable
            //.debounce(3_000, TimeUnit.MILLISECONDS)
            .doOnNext{
                _errorState.postValue(false)
                _successState.postValue(false)
                _progressStateLiveData.postValue(true)
            }
            .filter { _ ->
                val status:Boolean = dataManager.getNetworkStatus()
                if(!status){
                   _progressStateLiveData.postValue(false)
                   _noNetworkStateLiveData.postValue(true)
                }

                return@filter status
            }
            .switchMap { _ ->
                dataManager.getSpecificCryptocurrency(query, SpecificCryptoSearchType.SEARCH)
             }
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({
                _progressStateLiveData.postValue(false)

                when{
                    it.isSuccess ->{
                        _successData.postValue(it.getTypedValue<CryptoModelCombo>())
                        _successState.postValue(true)
                    }
                    it.isFailure ->{
                        _failureData.postValue(it.getTypedValue<String>())
                        _failureState.postValue(true)
                    }
                    it.isError ->{
                        _failureData.postValue("No crypto currency was found matching the search query")
                        _failureState.postValue(true)
                    }
                }
            },{
                Timber.e(it, "initSearch: error occurred ${it.message}")
                _progressStateLiveData.postValue(false)
                _errorData.postValue("No crypto currency was found matching the search query")
                _errorState.postValue(true)
            })

    }

    fun resetState(){}

    override fun setItemFavoriteStatus(entity:Int,status:Boolean){
       disposables +=
           (if (status)
                dataManager.addToFavorites(entity)
            else
                dataManager.removeFromFavorites(entity))
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({},{
                Timber.e(it, "setItemFavorites: error occurred ${it.message}")
                _errorData.postValue("an error occurred while processing your request")
                _errorState.postValue(true)
            })

    }

    override fun observeNetworkState(): LiveData<Boolean> {
        disposables +=
        dataManager.observeNetworkStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                _noNetworkStateLiveData.postValue(it)
            },{
                Timber.e(it, "observeNetworkState: error occurred ${it.message}")
                _errorData.postValue("an error occurred while processing your request")
                _errorState.postValue(true)
            })

        return noNetworkStateLiveData
    }

    override fun onCleared() {
        super.onCleared()
        unbind()
    }

    fun unbind():Unit = disposables.clear()
}