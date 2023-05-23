package com.sikstree.newproduct.Data

data class ReviewData (
    val review_profile : String,
    val review_id : String,
    val review_grade : Int,
    val review_text : String,
    val review_date : String,
    val image_has : Boolean
)