package com.cucu.cucuapp.presentation.navdrawer.categories.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cucu.cucuapp.application.firstCharToUpperCase
import com.cucu.cucuapp.presentation.mainscreen.home.view.ProductItem
import com.cucu.cucuapp.presentation.navdrawer.TopBarNavigateBack
import com.cucu.cucuapp.presentation.navdrawer.categories.viewmodel.CategoriesViewModel
import com.cucu.cucuapp.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    category: String?,
    mainNavController: NavHostController,
    viewModel: CategoriesViewModel = hiltViewModel()
){
    viewModel.getCategory(category.toString())

    viewModel.productsByCategory.observeAsState().value?.let { list ->
        Scaffold(
            topBar = { TopBarNavigateBack(mainNavController = mainNavController) }
        ) { paddingValues ->
            LazyColumn(contentPadding = paddingValues) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Purple80, RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp))
                            .padding(16.dp),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        text = "${category?.firstCharToUpperCase()}")
                }

                items(list, key = { product -> product.id!! }) { product ->
                    ProductItem(product, mainNavController)
                }
            }
        }
    }
}