package org.yoya.com.yoyaorg.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.yoya.com.yoyaorg.R;
import org.yoya.com.yoyaorg.constants.Constants;
import org.yoya.com.yoyaorg.dialog.LoadingDialog;
import org.yoya.com.yoyaorg.js.YoyaOrgJsHandler;
import org.yoya.com.yoyaorg.model.FileModel;
import org.yoya.com.yoyaorg.network.FileUploadModel;
import org.yoya.com.yoyaorg.network.HttpApiDataHandler;
import org.yoya.com.yoyaorg.network.HttpClient;
import org.yoya.com.yoyaorg.update.UpdateUtils;
import org.yoya.com.yoyaorg.utils.FileUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity {
    /**
     * 选择文件
     */
    public static final int REQUEST_CODE_CHOSE_FILE = 10001;

    @Bind(R.id.webView)
    WebView mWebView;
    @Bind(R.id.fl_all_screen)
    FrameLayout mFlFullView;

    private WebChromeClient.CustomViewCallback xCustomViewCallback;
    private MyWebChromeClient xwebchromeclient;
    private View xCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        requestPermissions();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UpdateUtils.checkUpdate(MainActivity.this, false);
            }
        }, 500);
    }


    private void init() {
        WebSettings settings = mWebView.getSettings();
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setSavePassword(false);
        settings.setSaveFormData(false);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);

        //启用支持javascript
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("GBK");
        settings.setUseWideViewPort(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        settings.setPluginState(WebSettings.PluginState.ON);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
        }

        String ua = mWebView.getSettings().getUserAgentString();
        mWebView.getSettings().setUserAgentString(ua + "; YoyaOrg-Android");


        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("Url", "Url-->" + url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //页面开始加载
                Log.e("Url", "Url-->onPageStarted:" + url);
                if (Build.VERSION.SDK_INT < 17) {
//                    view.loadUrl("javascript:if(window.Native == undefined){window.Native={call:function(arg0,arg1){prompt('{\"methodName\":' + arg0 + ',\"jsonValue\":' + arg1 + '}')}}};");
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //页面加载完毕
                Log.e("Url", "Url-->onPageStarted:" + url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e("Url", "Url-->onReceivedError:" + description);
                //加载出现失败
                super.onReceivedError(view, errorCode, description, failingUrl);
            }


        });

        xwebchromeclient = new MyWebChromeClient();
        mWebView.setWebChromeClient(xwebchromeclient);

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        mWebView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });


        double start, end;
        start = System.currentTimeMillis();
        mWebView.clearCache(true);
        mWebView.clearHistory();
        end = System.currentTimeMillis();
        Log.e("test", "清除缓存时间＝＝＝" + (end - start) + "ms");

        mWebView.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        mWebView.addJavascriptInterface(new YoyaOrgJsHandler(MainActivity.this),
                "yoyaOrgJsHandler");
        mWebView.loadUrl(Constants.BASE_URL);
        Log.e("test", "url--->" + Constants.BASE_URL);


        //以下逻辑是为了修复api17以下addJavascriptInterface漏洞
//        if (Build.VERSION.SDK_INT < 17) {
//            mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
//        } else {
//            mWebView.addJavascriptInterface(new YoyaOrgJsHandler(MainActivity.this),
//                    "yoyaOrgJsHandler");
//        }
    }

    /**
     * 处理onActivityResult回调逻辑
     */
    private void handleonActivityResult(int requestCode, int resultCode, Intent data) {
        //选择文件
        if (requestCode == REQUEST_CODE_CHOSE_FILE && data != null && resultCode == RESULT_OK) {
            String path = "";
            int apiLevel = Build.VERSION.SDK_INT;
            path = FileUtils.getImageAbsolutePath(MainActivity.this, data.getData());

            //过滤非jpg,png,jpeg,gif
            String suffix = FileUtils.getFileSuffix(path);
            Log.d("test", "文件后缀＝＝＝" + suffix);
            if ("jpg".equalsIgnoreCase(suffix)
                    || "png".equalsIgnoreCase(suffix)
                    || "jpeg".equalsIgnoreCase(suffix)
                    || "gif".equalsIgnoreCase(suffix)) {

                File file = new File(path);
                Log.e("test", "apiLevel->" + apiLevel);
                Log.e("test", "path->" + path);
                final FileModel model = new FileModel();
                model.name = file.getName();
                model.size = file.length() + "";
                model.path = file.getPath();
                model.type = FileUtils.getMIMEType(file);
                final Dialog dialog = LoadingDialog.createLoadingDialog(this, "上传中");
                dialog.show();
                HttpClient.uploadFile(this, model, new HttpApiDataHandler<FileUploadModel>() {
                    @Override
                    public void onDone(int code, String msg, FileUploadModel data) {
                        dialog.dismiss();
                        Log.d("test", "onDone==code:" + code + ";msg:" + msg);
                        if (code == 200) {
                            final String jsonString = new Gson().toJson(data);
                            Log.e("test", "jsonString-->" + jsonString);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mWebView.loadUrl("javascript:callWebViewFile('" + jsonString + "','" + null + "')");
                                }
                            });
                            Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                Toast.makeText(this, "暂不支持此格式的文件", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 判断是否是全屏
     *
     * @return
     */
    public boolean inCustomView() {
        return (xCustomView != null);
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mWebView.setVisibility(View.GONE);
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            mFlFullView.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            mFlFullView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onHideCustomView() {
            if (xCustomView == null)// 不是全屏播放状态
                return;

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            mFlFullView.removeView(xCustomView);
            xCustomView = null;
            mFlFullView.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            mWebView.setVisibility(View.VISIBLE);
        }

        @Override
        public View getVideoLoadingProgressView() {
            return super.getVideoLoadingProgressView();
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFlFullView.removeAllViews();
        mWebView.stopLoading();
        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.destroy();
        mWebView = null;
    }


    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub˛
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                hideCustomView();
                return true;
            } else if (mWebView.canGoBack()) {
                mWebView.goBack();//返回上一页面
                return true;
            } else {
                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        handleonActivityResult(requestCode, resultCode, data);
    }

    /**
     * 请求运行时权限
     */
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
