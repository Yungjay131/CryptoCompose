package com.slyworks.cryptocompose.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slyworks.data.DataManager
import com.slyworks.models.CryptoModel
import com.slyworks.models.Outcome
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit


/**
 *Created by Joshua Sylvanus, 4:02 AM, 16-Jun-22.
 */
class SearchViewModel(private var dataManager: DataManager) : ViewModel(), IMainActivityViewModel {
    //region Vars
    private val TAG: String? = SearchViewModel::class.simpleName

    val searchObservable: PublishSubject<String> = PublishSubject.create()

    private val _searchStateLiveData: MutableLiveData<Outcome> = MutableLiveData(Outcome.ERROR(null))
    val searchStateLiveData: LiveData<Outcome>
        get() = _searchStateLiveData

    private val _searchDataListLiveData: MutableLiveData<List<CryptoModel>> = MutableLiveData()
    val searchDataListLiveData: LiveData<List<CryptoModel>>
        get() = _searchDataListLiveData

    private val mSubscriptions = CompositeDisposable()
    //endregion


    fun search(){
        val d = searchObservable
            .debounce(3, TimeUnit.SECONDS)
            .switchMap {
                dataManager.getSpecificCryptocurrency(it)
            }
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe {
                if(it.isNullOrEmpty()) {
                    _searchStateLiveData.postValue(Outcome.FAILURE(value = "no results for query found"))
                } else{
                    _searchStateLiveData.postValue(Outcome.SUCCESS(value = null))
                    _searchDataListLiveData.postValue(it)
                }
            }

        mSubscriptions.add(d)
    }
    fun search(query:String){
        /*fixme:use SwitchMap and Debounce here*/
        val d =  dataManager.getSpecificCryptocurrency(query)
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe {
                if(it.isNullOrEmpty()) {
                    _searchStateLiveData.postValue(Outcome.FAILURE(value = "no results for query found"))
                } else{
                    _searchStateLiveData.postValue(Outcome.SUCCESS(value = null))
                    _searchDataListLiveData.postValue(it)
                }
            }

        mSubscriptions.add(d)
    }

    override fun setItemFavoriteStatus(entity:CryptoModel,state:Boolean){
        val o: Observable<Boolean> =
            if (state)
                dataManager.addToFavorites(entity)
            else
                dataManager.removeFromFavorites(entity)

        val d  = o.observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe { _ -> }

        mSubscriptions.add(d)
    }

    override fun onCleared() {
        super.onCleared()
        mSubscriptions.clear()
    }
}