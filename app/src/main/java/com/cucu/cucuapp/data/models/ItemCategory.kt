package com.cucu.cucuapp.data.models

import android.os.Parcelable
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ItemCategory(
    val category: String? = "",
    val icon:
    @Contextual @RawValue ImageVector? = null,
    val route:String?=""
):Parcelable
