package com.xfhy.androidbasiclibs.common.util

import android.content.Context
import android.content.Intent
import io.reactivex.annotations.NonNull

/**
 * @author xfhy
 * @create at 2017/11/11 16:07
 * description：分享工具类(本来准备使用ShareSDK集成分享的,
 * 但是需要申请很多的三方app key,可能这个app不能通过审核,于是后来放弃了,使用的是系统的分享方式)
 */
object ShareUtil {
    fun shareUrl(@NonNull context: Context, url: String) {
        val intent = Intent()
        intent.type = "text/plain"
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "我正在使用Daily,看到一篇文章很不错,分享给大家: " + url)
        context.startActivity(Intent.createChooser(intent, "分享到"))
    }
}