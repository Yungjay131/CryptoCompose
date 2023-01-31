package com.slyworks.cryptocompose.ui.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.cryptocompose.plusAssign
import com.slyworks.data.DataManager
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 *Created by Joshua Sylvanus, 4:12 PM, 22-Jun-22.
 */
class MainActivityViewModel(private var dataManager: DataManager):ViewModel(), IViewModel {
    //region Vars
    private val TAG: String? = MainActivityViewModel::class.simpleName

    private val networkLiveData:MutableLiveData<Boolean> = MutableLiveData()
    private val mSubscriptions:CompositeDisposable = CompositeDisposable()
    //endregion

    override fun setItemFavoriteStatus(entity: Int, status: Boolean)  =
        throw UnsupportedOperationException("this op is not supported for this ViewModel")

    override fun observeNetworkState(): LiveData<Boolean> {
        mSubscriptions +=
        dataManager.observeNetworkStatus()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                networkLiveData.postValue(it)
            }

        return networkLiveData
    }

    override fun onCleared() {
        super.onCleared()
        mSubscriptions.clear()
    }
}