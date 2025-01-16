package com.youcef_bounaas.cibo.features.mealmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Minimize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.youcef_bounaas.cibo.data.model.MenuItem
import com.youcef_bounaas.cibo.ui.theme.Grenadine
import com.youcef_bounaas.cibo.ui.theme.OliveGreen

@Composable
fun OrderScreen(menuItem: MenuItem){

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OliveGreen)
        ){
Column (
    modifier = Modifier
        .fillMaxSize(),

){
Box(
    modifier = Modifier.weight(1f)
        .fillMaxSize(),
            contentAlignment = Alignment.Center

){
    AsyncImage(
       menuItem.imageUrl,
        "item image",
    )
}


    Card (
        modifier = Modifier
            .weight(1.5f)
            .clip(
                RoundedCornerShape(
                    topStart = 100.dp,
                    topEnd = 0.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            )

    ){

        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(30.dp)

        ){

            Column {



                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){



                    Button(
                        onClick = {  },
                        modifier = Modifier.clip(RoundedCornerShape(100.dp)),
                        enabled = false,
                        colors = ButtonColors(
                            containerColor = OliveGreen,
                            contentColor = Grenadine,
                            disabledContainerColor = OliveGreen,
                            disabledContentColor = Grenadine
                        ),


                        ) {

                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ){
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Favorite",
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "4.8",
                                fontSize = 18.sp,
                                color = Color.White,
                            )
                        }

                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        "€  ${menuItem.price} ",
                        fontSize = 20.sp,
                        color = Color.Yellow
                    )



                }

                Spacer(modifier = Modifier.height(20.dp))

                Row {

                    Text(menuItem.name)
                    Spacer(modifier = Modifier.weight(1f))
                    OrderQuantity()





                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    menuItem.description,
                            modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(150.dp))


                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OliveGreen,
                        contentColor = Color.White
                    )
                ) {
                    Text("Add to Cart")
                }




            }






        }
    }


}
        }
    }


@Composable
fun OrderQuantity(){
    var count by remember{ mutableStateOf(0) }

    Row {
       Icon(
           imageVector = Icons.Outlined.AddCircle,
           "add",
           modifier = Modifier.clickable { count ++ }
       )

        Text(
            count.toString()
        )

        Icon(
            imageVector = Icons.Outlined.Minimize,
            "add",
            modifier = Modifier.clickable {
                if (count > 0){
                    count --
                }


            }
        )
    }




}