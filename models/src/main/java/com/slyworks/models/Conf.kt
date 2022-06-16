package com.slyworks.models

import com.slyworks.models.NetworkConf


/**
 *Created by Joshua Sylvanus, 9:41 AM, 05-Jun-22.
 */
const val URL = "https://pro-api.coinmarketcap.com"
object Conf : NetworkConf {
    override val cacheSize: Long
        get() = 100 * 1024 //100k
    override val baseUrl: String
        get() = URL
}