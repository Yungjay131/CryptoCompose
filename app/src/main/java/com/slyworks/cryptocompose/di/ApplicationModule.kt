package com.slyworks.cryptocompose.di

import com.slyworks.data.di.DataModule
import dagger.Module


/**
 *Created by Joshua Sylvanus, 4:03 PM, 04-Jun-22.
 */
@Module(
    includes = [
        DataModule::class,
    ]
)
object ApplicationModule
