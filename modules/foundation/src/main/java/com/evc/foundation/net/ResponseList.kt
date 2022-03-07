package com.bigkoo.katafoundation.model

/**
 * Created by xiaojiang on 2020/4/30.
 */
class ResponseList<T>(val total: Int,
                      val pageSize: Int,
                      val pages: Int,
                      val pageNum: Int,
                      val list: List<T>?) {

}