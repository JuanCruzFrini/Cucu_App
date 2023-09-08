package com.cucu.cucuapp.presentation.navdrawer.categories.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cucu.cucuapp.application.Routes
import com.cucu.cucuapp.data.models.ItemCategory
import com.cucu.cucuapp.presentation.navdrawer.categories.viewmodel.CategoriesViewModel

@Composable
fun CategoriesScreen(mainNavController: NavHostController) {
    CategoriesList(mainNavController)
}

@Composable
fun CategoriesList(
    mainNavController: NavHostController,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    viewModel.getCategories()

    viewModel.categories.observeAsState().value?.let { list ->
        LazyColumn {
            items(list, key = { it.category!! }){
                CategoryItem(it, mainNavController)
            }
        }
    }
}

@Composable
fun CategoryItem(category: ItemCategory, mainNavController: NavHostController) {

    Card(
        Modifier
            .fillMaxWidth()
            .padding(0.dp, 4.dp)
            .clickable {
                mainNavController.currentBackStackEntry?.savedStateHandle?.set(
                    key = "category",
                    value = category.category
                )
                mainNavController.navigate(Routes.Category.createRoute(category.category!!))
            },
        elevation = CardDefaults.cardElevation(2.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(modifier = Modifier.size(30.dp), imageVector = category.icon!!, contentDescription = "")
            Text(text = "${category.category}", fontSize = 16.sp, textAlign = TextAlign.Start)
            Icon(imageVector = Icons.Filled.KeyboardArrowRight, contentDescription = "")
        }
    }
}


