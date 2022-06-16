package com.slyworks.cryptocompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp


/**
 *Created by Joshua Sylvanus, 3:11 PM, 02-Jun-22.
 */

@Composable
fun NetworkStatusView(state: State<Boolean>){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(35.dp)
            .fillMaxWidth()
            .background(
                color = if (state.value)
                    Color.Blue
                else
                    Color.Black
            )
    ){
        Image(
            modifier = Modifier.height(20.dp)
                .width(20.dp),
            imageVector = Icons.Default.AddCircle,
            contentDescription = "")

        Text(
            text = if(state.value)

                       "connected"
                    else
                       "no network connection" )
    }
}