package com.cucu.cucuapp.presentation.detail.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.cucu.cucuapp.R
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.presentation.navdrawer.TopBarNavigateBack
import com.cucu.cucuapp.ui.theme.Purple40
import com.cucu.cucuapp.ui.theme.PurpleGrey80
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, product:Product?) {

    Scaffold(
        Modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState()),
        topBar = { TopBarNavigateBack(navController) }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            DetailContent(product)
        }
    }
}

@Composable
fun DetailContent(product: Product?) {
    var showDialog by remember { mutableStateOf(false) }

    DialogSelectCartQuantity(
        show = showDialog,
        stock = product?.stock,
        onDismiss = { showDialog = !showDialog }
        )

    Column(Modifier.fillMaxSize()) {

        Text(
            text = product?.name.toString(),
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        AsyncImage(
            model = product?.img,
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            error = painterResource(id = R.drawable.ic_launcher_foreground),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Column(Modifier.padding(16.dp)) {
            Text(
                text = "$" + product?.newPrice?.roundToInt().toString(),
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                modifier = Modifier
                    .padding(0.dp, 8.dp)
                    .fillMaxWidth(),
                text = "Stock disponible: " + product?.stock.toString() + " unidades" ,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple40,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                onClick = {}) {
                Text(text = "Comprar ahora")
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleGrey80,
                    contentColor = Purple40
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                onClick = { showDialog = true }) {
                Text(text = "Agregar al carrito")
            }
            Text(text = "Descripción", fontSize = 20.sp, modifier = Modifier.padding(0.dp, 16.dp))
            Text(text = product?.description.toString(), Modifier.fillMaxWidth(), color = Color.Gray)
        }
    }
}

@Composable
fun DialogSelectCartQuantity(
    show: Boolean,
    stock: Int?,
    onDismiss:()-> Unit
){
    var selectedQuantity by remember { mutableIntStateOf(1) }
    var stockAvailable by remember { mutableStateOf(stock?.minus(selectedQuantity)) }

    if (show){
        Dialog(onDismissRequest = { onDismiss() }) {
            Card(
                colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray
            )) {
                Column(
                    Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "¿Cuantas unidades desea agregar al carrito?", textAlign = TextAlign.Center)
                    Text(text = "(${stockAvailable} unidades disponibles)")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "$selectedQuantity", Modifier.background(Color.Transparent), fontSize = 24.sp)

                    Row {
                        IconButton(
                            onClick = {
                                stock?.let {
                                    if (stockAvailable in 0 until it && selectedQuantity > 1) {
                                        selectedQuantity = selectedQuantity.minus(1)
                                        stockAvailable = it.minus(selectedQuantity)
                                    }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = null, tint = Color.Red)
                        }
                        IconButton(
                            onClick = {
                                stock?.let {
                                    if (stockAvailable != 0) {
                                        selectedQuantity = selectedQuantity.plus(1)
                                        stockAvailable = it.minus(selectedQuantity)
                                    }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null, tint = Color.Green)
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PurpleGrey80,
                                contentColor = Purple40
                            ),
                            onClick = { onDismiss() }) {
                            Text(text = "Cancelar")
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple40,
                                contentColor = Color.White
                            ),
                            onClick = { onDismiss() }) {
                            Text(text = "Continuar")
                        }
                    }
                }
            }

        }
    }
}





















