package com.slyworks.cryptocompose.ui.activities.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.slyworks.cryptocompose.Navigator
import com.slyworks.cryptocompose.ui.activities.main.MainActivity
import com.slyworks.cryptocompose.ui.activities.onboarding.OnboardingActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    /*TODO:find a way to deal with these annotations leaking like an exception */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @ExperimentalUnitApi
    override fun onStart() {
        super.onStart()

        Single.timer(500L, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::init, ::init)
    }

    @ExperimentalUnitApi
    private fun init(l:Long) =
        Navigator.intentFor<OnboardingActivity>(this)
            .newAndClearTask()
            .finishCaller()
            .navigate()

    @ExperimentalUnitApi
    private fun init(t:Throwable) =
        Navigator.intentFor<OnboardingActivity>(this)
            .newAndClearTask()
            .finishCaller()
            .navigate()
    
}