package com.evc.foundation.mvpview

/**
 *
 * @Package: com.evc.foundation.mvpview
 * @Description: View的基类
 * @Author: EvanChan
 * @CreateDate: 8/31/21 5:30 PM
 * @m-mail: dadaintheair@gmail.com
 */
interface BaseKtView {

    /**
     * 网络请求之前，走这里
     *
     * @param refreshing
     */
    fun onRefreshing(refreshing: Boolean)

    /**
     * {
     * "code": 0,
     * "msg": "保存成功",
     * "data": {}
     * } data 里面没数据返回的，走这里
     *
     * @param msg 服务器返回的tib
     */
    fun onHttpEmptySuccess(msg: String?)

    fun onStatusLoading()

    // TODO: 2020/10/27 根据自己的网络协议进行修改
    /**
     * 网络正常返回，但是返回的code！=0 的其他code的时候，走这里
     *
     * @param code
     * @param msg
     */
    fun onHttpError(code: Int, msg: String?)

    // TODO: 2020/10/27 根据自己的网络协议进行修改
    /**
     * 网络异常，走这里，比如500之类
     *
     * @param msg
     */
    fun onHttpNetworkError(msg: String?)

    // TODO: 2020/10/27 根据自己的网络协议进行修改
    /**
     * {
     * "code": 0,
     * "msg": "保存成功",
     * "data": {}
     * } data 里面有数据返回的，走这里
     *
     * @param data
     */
    fun onHttpDataGet(data: Any?)

    /**
     * 不管成功与否，都会走这里 onHttpCompleted
     */
    fun onHttpCompleted()

    /**
     * 目前 onHttpNetworkError和onHttpError 会自动toast出对应的msg，不用在各自的回调中手动toast
     *
     * @param msg
     */
    fun showToast(msg: String?)

    /**
     * 目前 onHttpNetworkError和onHttpError 会自动toast出对应的msg，不用在各自的回调中手动toast
     *
     * @param msg
     */
    fun showToastLong(msg: String?)

    fun showContent()

    fun showLoading(msg: String? = null)

    fun dismissLoading()
}