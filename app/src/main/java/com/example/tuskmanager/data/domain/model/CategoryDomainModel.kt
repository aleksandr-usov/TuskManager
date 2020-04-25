package com.example.tuskmanager.data.domain.model

data class CategoryDomainModel(
    var id: Long = 0L,
    var title: String,
    var icon: String,
    var color: String
) {
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

