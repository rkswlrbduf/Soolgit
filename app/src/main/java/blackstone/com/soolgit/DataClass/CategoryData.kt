package blackstone.com.soolgit.DataClass

import com.google.gson.annotations.SerializedName

data class CategoryData(
        @SerializedName("CATEGORY_ID")
        val CATEGORY_ID: Int,
        @SerializedName("CATEGORY_NM")
        val CATEGORY_NM: String,
        @SerializedName("CATEGORY_IMG")
        val CATEGORY_IMG: String)