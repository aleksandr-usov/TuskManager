package com.example.tuskmanager.data.domain.model

import java.io.Serializable

data class CategoryDomainModel(
    var id: Long = 0L,
    var title: String,
    var icon: String,
    var color: String
) : Serializable {
    companion object {
        val CATEGORY_ADD_NEW: CategoryDomainModel
            get() {
                return CategoryDomainModel(
                    0,
                    "",
                    "",
                    "#179AB7"
                )
            }
    }
}

