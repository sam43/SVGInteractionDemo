package com.sam43.svginteractiondemo

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val JAVASCRIPT_OBJ = "javascript_obj"
        const val BASE_URL = "file:///android_asset/web/"
    }

    private lateinit var fileDownloaderVM: FileDownloaderVM
    private lateinit var pd: ProgressDialog

    private fun initProgressDialog() {
        pd = ProgressDialog(this)
        pd.setCancelable(false)
        pd.isIndeterminate = true
        pd.setTitle(getString(R.string.rendering_svg))
        pd.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initVM()
        setupButtonActions()
        setupWebLayout()
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setupButtonActions() {
        initProgressDialog()
        btnZoomIn.setOnClickListener { webView.zoomIn() }
        btnZoomOut.setOnClickListener { webView.zoomOut() }
        btnSendToWeb.setOnClickListener {
            webView.evaluateJavascript(
                "javascript: " +
                        "updateFromAndroid(\"" + etSendDataField.text + "\")",
                null
            )
        }
    }

    private fun initVM() {
        fileDownloaderVM = ViewModelProviders.of(this).get(FileDownloaderVM::class.java)
    }

    override fun onResume() {
        super.onResume()
        callVM()
    }

    private fun callVM() {
        val url = "https://svgshare.com/i/Gzd.svg"
        try {
            fileDownloaderVM.downloadFileFromServer(url)
                .observe(this, Observer { responseBody ->
                    val svgString = responseBody.string()
                    webView.loadDataWithBaseURL(
                        BASE_URL, getHTMLbody(svgString), "text/html",
                        "UTF-8", null
                    )
                    pd.dismiss()
                })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("AddJavascriptInterface", "SetJavaScriptEnabled")
    private fun setupWebLayout() {
        webView.setInitialScale(250)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.addJavascriptInterface(
            JavaScriptInterface(),
            JAVASCRIPT_OBJ
        )
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                injectJavaScriptFunction()
            }
        }
        webView.webChromeClient = WebChromeClient()
    }

    private fun injectJavaScriptFunction() {
        val textToAndroid = "javascript: window.androidObj.textToAndroid = function(message) { " +
                JAVASCRIPT_OBJ + ".textFromWeb(message) }"
        webView.loadUrl(textToAndroid)
    }


    inner class JavaScriptInterface {
        @SuppressLint("SetTextI18n")
        @JavascriptInterface
        fun textFromWeb(fromWeb: String) {
            runOnUiThread {
                tvStateName.text = fromWeb
            }
            toast(fromWeb)
        }
    }

    override fun onDestroy() {
        webView.removeJavascriptInterface(JAVASCRIPT_OBJ)
        super.onDestroy()
    }
}
