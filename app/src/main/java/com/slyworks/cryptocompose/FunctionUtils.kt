package com.slyworks.cryptocompose

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


/**
 *Created by Joshua Sylvanus, 9:41 AM, 14-Aug-22.
 */

operator fun CompositeDisposable.plusAssign(d: Disposable){
    this.add(d)
}