package kr.dongwon.arr.base

import kr.dongwon.arr.BuildConfig

object Const {

    // base // 변경 필요
    val BASE_URL =  if (BuildConfig.SERVICE_SERVER) "https://m.thebanchan.co.kr" else "https://devm.thebanchan.co.kr"
    val IMAGE_URL =  if (BuildConfig.SERVICE_SERVER) "https://image.thebanchan.co.kr" else "https://dev-image.thebanchan.co.kr"

    // pref
    const val PREFERENCES_NAME = "kr.dongwon.arr"
}