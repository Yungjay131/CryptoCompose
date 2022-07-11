package com.slyworks.cryptocompose.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.data.DataManager
import com.slyworks.data.SpecificCryptoSearchType
import com.slyworks.models.CryptoModelCombo
import com.slyworks.models.Outcome
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 *Created by Joshua Sylvanus, 4:02 AM, 16-Jun-22.
 */
class SearchViewModel(private var dataManager: DataManager) : ViewModel(), IViewModel {
    //region Vars
    private val TAG: String? = SearchViewModel::class.simpleName

    val searchObservable: PublishSubject<String> = PublishSubject.create()

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

    val progressState:MutableLiveData<Boolean> = MutableLiveData()

    private val mSubscriptions = CompositeDisposable()
    //endregion


    fun initSearch(){
        val d = searchObservable
            .debounce(3, TimeUnit.SECONDS)
            .switchMap { s ->
                dataManager.observeNetworkStatus()
                    .flatMap {
                        if(it)
                          dataManager.getSpecificCryptocurrency(s, SpecificCryptoSearchType.SEARCH)
                              .flatMap { it2 : CryptoModelCombo ->
                                  if(it2.details == null)
                                      Observable.just(Outcome.FAILURE(value = "no results for query found"))
                                  else
                                      Observable.just(Outcome.SUCCESS(it2))
                              }
                        else
                           Observable.just(Outcome.ERROR(value = "no internet connection"))
                    }
            }
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe({
                progressState.postValue(false)

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
                        _errorData.postValue(it.getTypedValue<String>())
                        _errorState.postValue(true)
                    }
                }
            },{
                Timber.e(it, "initSearch: error occurred")
                _errorData.postValue("an error occurred while processing your request")
                _errorState.postValue(true)

            })

        mSubscriptions.add(d)
    }


    override fun setItemFavoriteStatus(entity:Int,status:Boolean){
        val o: Completable =
            if (status)
                dataManager.addToFavorites(entity)
            else
                dataManager.removeFromFavorites(entity)

        val d  = o.observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe()

        mSubscriptions.add(d)
    }

    override fun observeNetworkState(): LiveData<Boolean> {
        val l:MutableLiveData<Boolean> = MutableLiveData()
        val d = dataManager.observeNetworkStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                l.postValue(it)
            }

        mSubscriptions.add(d)

        return l
    }

    override fun onCleared() {
        super.onCleared()
        mSubscriptions.clear()
    }
}