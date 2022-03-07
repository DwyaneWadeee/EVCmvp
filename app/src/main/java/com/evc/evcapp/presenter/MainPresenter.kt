package com.evc.evcapp.presenter

import com.alibaba.fastjson.JSON
import com.evc.evcapp.view.MainView
import com.evc.toolkit.extension.convert2RequestBody
import com.evc.evcapp.service.CommomService
import com.evc.foundation.mvppresnter.BaseKtPresenter

/**
 *
 * @Package: com.evc.evcapp
 * @Description: java类作用描述
 * @Author: EvanChan
 * @CreateDate: 9/26/21 10:56 AM
 * @m-mail: dadaintheair@gmail.com
 */
class MainPresenter : BaseKtPresenter<MainView>() {
    fun getToken(){
        var params = HashMap<String, String>()
        params.put("partyId","101")
        params.put("idCard","430623198604218320")
        params.put("timestamp","1632469176498")
        params.put("sign","erokRWdZtB0EXz3TQQoonQesGY72Oyv+Zt3ua/b3qszfCmjjhn54DZk06FC5MYvI+Qvf3fK0kAh4Rglz0tBINfH3fqx/3NgVWgzJ5d44kJMo6IBoU4W7HOP67xeQ3XcDCxj6hFDActjBE2aO9nYyomJhvtvYw/4hrmqExGr8QHFgFerIgEagWGTrUxq7X1RCe3MmPrQgmZ6KCtctPgT+3IZD6A9013jUWYUShdmMWB5+3IiUj/2DPlJHWUqa+Q84tBD8+3GEZ3/FYNnZ5ktL63nTboiQMoKRi6WfuuQteomZ+49XIUtCigjTqIMcgtJaOjwwboMBRku3jA94z+SQqg==")

        var tempStr = JSON.toJSONString(params)

        requset(CommomService.api.getToken(tempStr.convert2RequestBody()))
            .response(loadingAnim = false, showLoading = true, loadingMsg = "请稍后", showErrorMsg = false, onHttpSuccess = {
//                getThatView()?.onGetSuccess(it)
//                getThatView().onLocationGet(it)
            }, onDisposeEmptyResult = { result, msg ->
//                getThatView().onLocationGetFail(msg)
            }, onHttpFail = { result, msg ->
//                getThatView().onLocationGetFail(msg)
            })
    }
}