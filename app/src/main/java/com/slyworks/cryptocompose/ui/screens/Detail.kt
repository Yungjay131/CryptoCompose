package com.slyworks.cryptocompose.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.slyworks.cryptocompose.App
import com.slyworks.cryptocompose.IViewModel
import com.slyworks.cryptocompose.R
import com.slyworks.cryptocompose.ui.activities.main.MainActivity
import com.slyworks.models.CryptoModel


/**
 *Created by Joshua Sylvanus, 3:00 PM, 17-Jun-22.
 */

private fun String.parseTags():List<String> = this.split(",")

@Composable
fun DetailMain(entity:CryptoModel,
               viewModel: IViewModel
){

    LazyColumn(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
       /*TODO:show favorite icon on CollapsingToolBarLayout*/
        item{
            Image(
                painter = rememberImagePainter(
                    data = entity.image as String,
                    builder = App.imageRequest,
                ),
                contentDescription = "cryptocurrency's logo",
                modifier = Modifier.size(80.dp)
            )
            TextField(label = "Name", entity.name)
            TextField(label = "Symbol", entity.symbol)
            TextField(label = "Cmc Rank", entity.cmcRank.toString())
            TextField(label = "Price Unit", entity.priceUnit)
            TextField(label = "Price", entity.price.toString())
            TextField(label = "Max Supply", entity.maxSupply.toString())
            TextField(label = "Circulating Supply", entity.circulatingSupply.toString())
            TextField(label = "Total Supply", entity.totalSupply.toString())
            TextField(label = "MarketCap", entity.marketCap.toString())
            TextField(label = "Date Added", entity.dateAdded)
            TagLayout(tags = entity.tags.parseTags())
            ButtonFavorites(viewModel = viewModel, entity = entity)
        }

    }
    
}

@Composable
fun ButtonFavorites(viewModel: IViewModel,
                    entity: CryptoModel){
    IconToggleButton(
        modifier = Modifier
            .height(50.dp)
            .wrapContentWidth()
            .padding(6.dp),
        checked = entity.isFavorite,
        onCheckedChange = {
            viewModel.setItemFavoriteStatus(entity = entity, status = it)
        }) {
        Icon(
            tint = Color.Magenta,
            imageVector = Icons.Default.Favorite,
            contentDescription = "")
    }
}
@Composable
fun TextField(label:String,
              value:String){

    OutlinedTextField(
        readOnly = true,
        value = value,
        onValueChange = {},
        label = {
            Text(text = label)
        })
}

@Composable
fun TagView(tag:String){
     Row(modifier = Modifier
         .border(
             border = BorderStroke(1.dp, Color.Blue),
             shape = RoundedCornerShape(corner = CornerSize(6.dp))
         )
         .height(45.dp)
         .wrapContentWidth()
         .padding(4.dp),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically) {
         
         Text(text = tag)    
     }
}

@Composable
fun TagLayout(tags:List<String>){

    CustomLayout {
        for(t in tags)
            TagView(tag = t)
    }
}

@Composable
fun CustomLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit){

    Layout(modifier = modifier,
           measurePolicy = customLayoutMeasurePolicy(),
           content = content)
}

fun customLayoutMeasurePolicy():MeasurePolicy{
    return MeasurePolicy{ measurables, constraints ->
        val placeables:List<Placeable> = measurables.map{ measurable ->
            measurable.measure(constraints)
        }

        layout(
            constraints.maxWidth,
            constraints.maxHeight){
            var yPos = 0
            var xPos = 0
            var maxY = 0

            placeables.forEach { placeable ->
                /*moving to next "line" since item won't fit on this line*/
                if(xPos + placeable.width > constraints.maxWidth){
                   xPos = 0
                   yPos += maxY
                   maxY = 0
                }

                placeable.placeRelative(
                    x = xPos,
                    y = yPos )

                xPos += placeable.width
                if(maxY < placeable.height){
                    maxY = placeable.height
                }
            }
        }
    }
}