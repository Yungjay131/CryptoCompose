package com.slyworks.cryptocompose.ui.activities.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.slyworks.cryptocompose.ui.activities.main.MainActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    /*TODO:find a way to deal with these annotations leaking like an exception */
    @ExperimentalUnitApi
    override fun onStart() {
        super.onStart()

        Single.timer(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::init, ::init)
    }

    @ExperimentalUnitApi
    private fun init(l:Long){
        startActivity(
            Intent(this, MainActivity::class.java)
                .apply{
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
        )
    }

    @ExperimentalUnitApi
    private fun init(t:Throwable){
        startActivity(
            Intent(this, MainActivity::class.java)
                .apply{
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
        )
    }
}