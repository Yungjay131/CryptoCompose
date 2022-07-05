package com.slyworks.cryptocompose.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.data.DataManager
import com.slyworks.models.CryptoModel
import com.slyworks.models.Outcome
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 *Created by Joshua Sylvanus, 4:04 AM, 16-Jun-22.
 */
class FavoritesViewModel(private var dataManager:DataManager) : ViewModel(), IViewModel {
    //region Vars
    private val TAG: String? = FavoritesViewModel::class.simpleName

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

    val progressState:MutableLiveData<Boolean> = MutableLiveData()

    private val mSubscriptions = CompositeDisposable()
    //endregion

    fun getFavorites(){
       val d = dataManager.getFavorites()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe { it:Triple<List<CryptoModel>,Int ,String?> ->
                /*using destructuring assignment, brings back JS memories...LOL*/
                val (data, status, message) = it

                when(status){
                    1 ->{
                        _noNetworkState.postValue(true)
                    }
                    2 ->{
                        _errorData.postValue(message)
                        _errorState.postValue(true)
                    }
                    3 ->{
                        _successData.postValue(data)
                        _successState.postValue(true)
                    }
                }
          }

        mSubscriptions.add(d)
    }

    override fun setItemFavoriteStatus(entity:Int, status:Boolean){
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