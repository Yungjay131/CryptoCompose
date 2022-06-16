package com.slyworks.models

data class CryptoInfoRequest(val name:String,
                             val aux:String =
                              "id,symbol,name,maxSupply,circulatingSupply,totalSupply,"+
                              "cmcRank,price,marketCap,dateAdded,tags")