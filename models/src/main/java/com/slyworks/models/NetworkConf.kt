package com.slyworks.models


/**
 *Created by Joshua Sylvanus, 3:07 PM, 04-Jun-22.
 */
interface NetworkConf {
    val cacheSize:Long
    val baseUrl:String
    val dateFormat: String
        get() = "yyyy-MM-dd'T'HH:mm:ssZ"
}