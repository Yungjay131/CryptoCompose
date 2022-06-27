package com.slyworks.models


/**
 *Created by Joshua Sylvanus, 6:29 AM, 23-Jun-22.
 */
data class CryptoModelCombo(val model:CryptoModel?,
                            val details:CryptoModelDetails?){
    companion object{
        fun empty():CryptoModelCombo = CryptoModelCombo(null, null)
    }
}
