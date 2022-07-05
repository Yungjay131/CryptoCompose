package com.slyworks.models

data class CryptoModelDetails(val id:Int,
                              val name:String,
                              val symbol:String,
                              val category:String,
                              val description: String,
                              val slug:String,
                              val logo:String,
                              val tags:List<String>,
                              val dateAdded:String )