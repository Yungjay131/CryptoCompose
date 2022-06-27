package com.slyworks.cryptocompose.ui.activities.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
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

    val onboardingObservableNext:PublishSubject<Long> = PublishSubject.create()
    val onboardingObservablePrevious:PublishSubject<Long> = PublishSubject.create()
    //endregion

    fun startSequence(){
        var count:Long = 0

        Observable.merge(
            onboardingObservableNext
                .flatMap {
                    count += 1L
                    Observable.just(count)
                         },
            onboardingObservablePrevious
                .flatMap {
                    count -= 1L
                    Observable.just(count)
                         },
            Observable.interval(3L, TimeUnit.SECONDS)
                .toFlowable(BackpressureStrategy.BUFFER)
                .flatMap {
                    count += 1L
                    Flowable.just<Long>(count)
                }
                .toObservable()
             )
            .switchMap {
                /*trying to unsubscribe to old input and subscribe to new ones???*/
                Observable.just(it)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                when{
                    count in 1L..10L ->{
                        if(_tabToTint.value != 0)
                           _tabToTint.postValue(0)

                        if(_progressState1.value!! >= 1.0)
                            //means its from back button
                            _progressState1.postValue(0.0F)


                        val v = _progressState1.value!! + .1F
                        _progressState1.postValue(v)
                    }
                    count in 11L..20L ->{
                        if(_tabToTint.value != 1)
                           _tabToTint.postValue(1)

                        if(_progressState2.value!! >= 1.0)
                            _progressState2.postValue(0.0F)

                        val v = _progressState2.value!! + .1F
                        _progressState2.postValue(v)
                    }
                    count in 21L..30L ->{
                        if(_tabToTint.value != 0)
                            _tabToTint.postValue(2)

                        if(_progressState3.value!! >= 1.0)
                            _progressState3.postValue(0.0F)

                        val v = _progressState3.value!! + .1F
                        _progressState3.postValue(v)
                    }
                }
            }
    }

}