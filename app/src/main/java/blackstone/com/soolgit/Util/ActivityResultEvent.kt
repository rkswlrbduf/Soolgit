package blackstone.com.soolgit.Util

import android.content.Intent



class ActivityResultEvent() {

    private var requestCode: Int = 0
    private var resultCode: Int = 0
    private var data: Intent? = null

    constructor(requestCode: Int, resultCode: Int, data: Intent?) : this() {
        this.requestCode = requestCode
        this.resultCode = resultCode
        this.data = data
    }

    fun getRequestCode(): Int {
        return requestCode
    }

    fun setRequestCode(requestCode: Int) {
        this.requestCode = requestCode
    }

    fun getResultCode(): Int {
        return resultCode
    }

    fun setResultCode(resultCode: Int) {
        this.resultCode = resultCode
    }

    fun getData(): Intent? {
        return data
    }

    fun setData(data: Intent) {
        this.data = data
    }


}