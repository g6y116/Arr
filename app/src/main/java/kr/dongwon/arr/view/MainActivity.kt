package kr.dongwon.arr.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.dongwon.arr.base.Const
import kr.dongwon.arr.base.log
import kr.dongwon.arr.base.toast
import kr.dongwon.arr.customview.WebViewListener
import kr.dongwon.arr.databinding.ActivityMainBinding
import kr.dongwon.arr.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity(), WebViewListener {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

    private var waitTime = 0L

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(System.currentTimeMillis() - waitTime >= 1000 ) {
                waitTime = System.currentTimeMillis()
                toast(this@MainActivity, "\"이전\" 버튼을 한 번 더 누르시면 앱이 종료됩니다.")
            } else { finish() }
        }
    }

    private lateinit var settingActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // https://onlyfor-me-blog.tistory.com/522
        this.onBackPressedDispatcher.addCallback(this, callback)

        binding.wv.setWebViewListener(this)
        binding.wv.addJavascriptInterface(this, "appInterface")
        binding.wv.loadUrl(Const.BASE_URL)

        settingActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // TODO : 세팅 -> 메인 시 반응
            }
        }
    }

    private fun moveUrl(url: String) {

        // TODO : url 에 따른 로직 필요 시 작성
        binding.wv.loadUrl(url)
    }

    @JavascriptInterface
    fun postMessage(data: String) {
        log("server data : $data")

        if (data.contains("앱설정")) {
            runOnUiThread {
                settingActivityResultLauncher.launch(Intent(this, SettingActivity::class.java))
            }
        }
    }

    override fun onPageStarted(url: String) {
        // TODO : 로딩 시작 시 반응
    }

    override fun onPageFinished(view: WebView?, url: String) {
        // TODO : 로딩 종료 시 반응
        binding.srl.isRefreshing = false
    }

    override fun shouldOverrideUrlLoading(url: String) {
        // TODO : URL 별로 동작 반응

        moveUrl(url)
    }

    override fun onShowFileChooser(
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: WebChromeClient.FileChooserParams?
    ) {
        // TODO : 파일 업로드 반응 로직
    }

    override fun onSwipeDown() {
        // TODO : 제스처 반응
    }

    override fun onSwipeUp() {
        // TODO : 제스처 반응
    }
}