package kr.dongwon.arr.customview

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kr.dongwon.arr.base.log
import kotlin.math.abs

interface WebViewListener {
    fun onPageStarted(url: String)
    fun onPageFinished(view: WebView?, url: String)
    fun shouldOverrideUrlLoading(url: String)
    fun onShowFileChooser(filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: WebChromeClient.FileChooserParams?)
    fun onSwipeDown()
    fun onSwipeUp()
    fun nowScroll(nowY: Int, percent: Int)
}

class CustomWebView (
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int
) : WebView(context, attrs, defStyle) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var mWebViewListener: WebViewListener? = null
    private val gestureDetector: GestureDetector

    fun setWebViewListener(webViewListener: WebViewListener) { mWebViewListener = webViewListener }

    init {
        with(settings) {
            allowContentAccess = true
            allowFileAccess = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            domStorageEnabled = true
            userAgentString = "${userAgentString}/inApp"
            javaScriptCanOpenWindowsAutomatically = true
            javaScriptEnabled = true
            loadWithOverviewMode = true
            loadsImagesAutomatically = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            useWideViewPort = true
            defaultTextEncodingName = "UTF-8"
            textZoom = 100
            setGeolocationEnabled(false)
            setSupportMultipleWindows(true)
            isVerticalScrollBarEnabled = true
            isScrollbarFadingEnabled = true

            CookieManager.getInstance().apply {
                setAcceptCookie(true)
                setAcceptThirdPartyCookies(this@CustomWebView, true)
            }
        }

        webViewClient = CustomWebViewClient()
        webChromeClient = CustomWebChromeClient()
        overScrollMode = View.OVER_SCROLL_NEVER

        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean { return true }
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                try {
                    val diffY = e2.y - e1!!.y
                    if (abs(diffY) > 100 && abs(velocityY) > 100) {
                        if (diffY > 0) { mWebViewListener?.onSwipeUp() }
                        else { mWebViewListener?.onSwipeDown() }
                        return true
                    }
                } catch (_: Exception) { return false }
                return false
            }
        })

        // event - banner
        setOnScrollChangeListener { view, x, y, oldX, oldY ->
            val allY = computeVerticalScrollRange() - computeVerticalScrollExtent()
            val nowY = computeVerticalScrollOffset()
            val nowPercent = (nowY.toDouble() * 100 / allY.toDouble()).toInt()
            mWebViewListener?.nowScroll(nowY, nowPercent)
        }
    }

    override fun onCheckIsTextEditor() = true

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    inner class CustomWebViewClient: WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            if (url != null) mWebViewListener?.onPageStarted(url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            if (url != null) mWebViewListener?.onPageFinished(view, url)
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            log("url : ${request?.url.toString()}")
            mWebViewListener?.shouldOverrideUrlLoading(request?.url.toString())
            return true
        }
    }

    inner class CustomWebChromeClient: WebChromeClient() {

        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            val popupDialog = Dialog(context)
            val popupWebView = WebView(context).apply {
                scrollBarStyle = SCROLLBARS_OUTSIDE_OVERLAY
                isScrollbarFadingEnabled = false
                isHorizontalScrollBarEnabled = true
                isVerticalScrollBarEnabled = false
                scrollBarStyle = SCROLLBARS_OUTSIDE_OVERLAY

                with(settings) {
                    allowContentAccess = true
                    allowFileAccess = true
                    cacheMode = WebSettings.LOAD_NO_CACHE
                    domStorageEnabled = true
                    userAgentString = "${userAgentString}/inApp"
                    javaScriptCanOpenWindowsAutomatically = true
                    javaScriptEnabled = true
                    loadWithOverviewMode = true
                    loadsImagesAutomatically = true
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    useWideViewPort = true
                    defaultTextEncodingName = "UTF-8"
                    textZoom = 100

                    CookieManager.getInstance().apply {
                        setAcceptCookie(true)
                        setAcceptThirdPartyCookies(this@CustomWebView, true)
                    }
                }

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        val url = request?.url.toString()

                        val externalSchemeIntent: Intent? = if (url.startsWith("intent") || url.startsWith("Intent")) {
                            Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        } else { null }

                        if (externalSchemeIntent != null) { // 실행 가능한 앱이 있으면 앱 실행
                            return try {
                                context.startActivity(externalSchemeIntent)
                                true
                            } catch (_: Exception) { true }
                        }

                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onCloseWindow(window: WebView?) {
                        super.onCloseWindow(window)
                        popupDialog.dismiss()
                    }
                }
            }

            with(popupDialog) {
                setContentView(popupWebView)
                window!!.attributes?.let {
                    it.width = ViewGroup.LayoutParams.MATCH_PARENT
                    it.height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                show()
            }

            (resultMsg!!.obj as WebViewTransport).webView = popupWebView
            resultMsg.sendToTarget()
            return true
        }

        override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
            mWebViewListener?.onShowFileChooser(filePathCallback, fileChooserParams)
            return true
        }
    }
}