package com.sikstree.newproduct.Data

data class ReviewData (
    val review_brand_idx : Int, // 브랜드 명 구분 값
    val review_imoji_idx  : Int, // 상품 평 이모지 구분 값
    val review_title : String, // 상품 명
    val review_great : String, // 상품 평가 ex)최고에요
    val review_price : String, // 가격
    val review_text : String, // 상품 메인 리뷰
    val review_comment_count : String, // 리뷰 건 수 ex) 3개외 리뷰 모두보기
    val review_cm_id : String, // 다음 리뷰 작성자 이름
    val review_cm_comment : String, // 다음 리뷰 문구
)