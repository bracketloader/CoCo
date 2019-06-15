package com.qw.soulphototaker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.qw.photo.CoCo
import com.qw.photo.callback.GetImageCallBack
import com.qw.photo.constant.CompressStrategy
import com.qw.photo.dispose.ImageDisposer
import com.qw.photo.pojo.CaptureResult
import kotlinx.android.synthetic.main.activity_funtion_detail.*


/**
 * @author cd5160866
 */
class TakePictureActivity : BaseFunctionActivity() {

    override fun start(isMatrix: Boolean, degree: Int) {
        if (degree == -1) {
            CoCo.with(this)
                .take(createSDCardFile())
                .apply()
                .start(object : GetImageCallBack<CaptureResult> {
                    override fun onSuccess(data: CaptureResult) {
                        val bitmap: Bitmap = BitmapFactory.decodeFile(data.targetFile!!.path)
                        getImageView().setImageBitmap(bitmap)
                        tv_result.text = getImageSizeDesc(bitmap)
                    }

                    override fun onFailed(exception: Exception) {
                        Toast.makeText(this@TakePictureActivity, "拍照异常: $exception", Toast.LENGTH_SHORT).show()
                    }

                })
        } else {
            val strategy: CompressStrategy = if (isMatrix) {
                CompressStrategy.MATRIX
            } else {
                CompressStrategy.QUALITY
            }
            CoCo.with(this@TakePictureActivity)
                .take(createSDCardFile())
                .applyWithDispose(
                    ImageDisposer().degree(degree)
                        .strategy(strategy)
                )
                .start(object : GetImageCallBack<CaptureResult> {

                    override fun onDisposeStart() {
                        Toast.makeText(this@TakePictureActivity, "拍照成功,开始处理", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCancel() {
                        Toast.makeText(this@TakePictureActivity, "拍照取消", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailed(exception: Exception) {
                        Toast.makeText(this@TakePictureActivity, "拍照异常: $exception", Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(data: CaptureResult) {
                        Toast.makeText(this@TakePictureActivity, "拍照操作最终成功", Toast.LENGTH_SHORT).show()
                        getImageView().setImageBitmap(data.compressBitmap)
                        tv_result.text = getImageSizeDesc(data.compressBitmap!!)
                    }

                })
        }
    }
}
