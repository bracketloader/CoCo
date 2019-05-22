package com.qw.photo.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import com.qw.photo.callback.BaseCallBack
import com.qw.photo.pojo.Action
import com.qw.photo.pojo.BaseParams
import com.qw.photo.pojo.CaptureParams
import com.qw.photo.pojo.ResultData


/**
 *
 * @author cd5160866
 */
class SupportFragment : Fragment(), IWorker {

    private lateinit var mAction: Action

    private lateinit var mParam: BaseParams

    private lateinit var mCallBack: BaseCallBack

    override fun setActions(action: Action) {
        this.mAction = action
    }

    override fun setParams(params: BaseParams) {
        this.mParam = params
    }

    override fun start(callBack: BaseCallBack) {
        this.mCallBack = callBack
        when (mAction) {
            Action.CAPTURE -> takePhoto()
            else -> {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_CODE_IMAGE_CAPTURE -> {
                data?.extras ?: return
                val imageBitmap = data.extras!!.get("data") as Bitmap
                val result = ResultData()
                result.thumbnailData = imageBitmap
                mCallBack.onSuccess(result)
            }
            REQUEST_CODE_IMAGE_PICK -> {

            }
        }
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (null === takePictureIntent.resolveActivity(activity!!.packageManager)) {
            mCallBack.onFailed(IllegalStateException("activity status error"))
            return
        }
        val params: CaptureParams = mParam as CaptureParams
        var uri = params.uri
        if (null === uri) {

        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE)
    }

    companion object {

        private const val REQUEST_CODE_IMAGE_CAPTURE = 1

        private const val REQUEST_CODE_IMAGE_PICK = 2

    }
}