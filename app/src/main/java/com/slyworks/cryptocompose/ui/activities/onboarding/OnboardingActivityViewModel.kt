package com.slyworks.cryptocompose.ui.activities.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit


/**
 *Created by Joshua Sylvanus, 7:19 AM, 25-Jun-22.
 */
class OnboardingActivityViewModel : ViewModel() {
    //region Vars
    private val _progressState1:MutableLiveData<Float> = MutableLiveData(0.0F)
    val progressState1:LiveData<Float>
    get() = _progressState1

    private val _progressState2:MutableLiveData<Float> = MutableLiveData(0.0F)
    val progressState2:LiveData<Float>
    get() = _progressState2

    private val _progressState3:MutableLiveData<Float> = MutableLiveData(0.0F)
    val progressState3:LiveData<Float>
    get() = _progressState3

    private val _tabToTint:MutableLiveData<Int> = MutableLiveData()
    val tabToTint:LiveData<Int>
    get() = _tabToTint

    val onboardingObservableNext:PublishSubject<Int> = PublishSubject.create()
    val onboardingObservablePrevious:PublishSubject<Int> = PublishSubject.create()

    private val mO:PublishSubject<Int> = PublishSubject.create()

    private var d1:Disposable = Disposable.empty()
    private var d2:Disposable = Disposable.empty()
    private var d3:Disposable = Disposable.empty()

    private val mSubscriptions:CompositeDisposable = CompositeDisposable()
    //endregion

    fun startSequence(){
        val d =
            Observable.merge(
               onboardingObservableNext,
               onboardingObservablePrevious,
               mO,
               Observable.just(0)
            )
           .subscribeOn(Schedulers.io())
           .observeOn(Schedulers.io())
           .subscribe {
               when(it){
                   0 ->{
                       _tabToTint.postValue(0)
                          d2.dispose()
                          d3.dispose()
                       _progressState1.postValue(0.0F)
                       _progressState2.postValue(0.0F)
                       _progressState3.postValue(0.0F)
                       startSequence1()
                   }
                   1 -> {
                       _tabToTint.postValue(1)
                          d1.dispose()
                          d3.dispose()
                       _progressState1.postValue(1.0F)
                       _progressState2.postValue(0.0F)
                       _progressState3.postValue(0.0F)
                       startSequence2()
                   }
                   2 -> {
                       _tabToTint.postValue(2)
                          d1.dispose()
                          d2.dispose()
                       _progressState1.postValue(1.0F)
                       _progressState2.postValue(1.0F)
                       _progressState3.postValue(0.0F)
                       startSequence3()
                   }
               }
           }

        mSubscriptions.add(d)
    }

    private fun startSequence1() {
        d1 = Observable.interval(50L, TimeUnit.MILLISECONDS)
            .toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                if (_progressState1.value!! < 1.0F)
                    _progressState1.postValue(_progressState1.value!! + .01F)
                else
                    mO.onNext(1)
            }
    }

    private fun startSequence2() {
        d2 = Observable.interval(50L, TimeUnit.MILLISECONDS)
            .toFlowable(BackpressureStrategy.BUFFER)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                if (_progressState2.value!! < 1.0F)
                    _progressState2.postValue(_progressState2.value!! + .01F)
                else
                    mO.onNext(2)
            }
    }

    private fun startSequence3(){
        d3 = Observable.interval(150L, TimeUnit.MILLISECONDS)
             .toFlowable(BackpressureStrategy.BUFFER)
             .subscribeOn(Schedulers.io())
             .observeOn(Schedulers.io())
             .subscribe {
                 if(_progressState3.value!! < 1.0F)
                     _progressState3.postValue(_progressState3.value!! + .01F)
             }
    }

    override fun onCleared() {
        super.onCleared()
    }
}