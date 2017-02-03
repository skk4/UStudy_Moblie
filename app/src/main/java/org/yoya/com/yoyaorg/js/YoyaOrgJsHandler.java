package org.yoya.com.yoyaorg.js;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;

import org.yoya.com.yoyaorg.activity.MainActivity;
import org.yoya.com.yoyaorg.constants.Constants;
import org.yoya.com.yoyaorg.model.LoginModel;
import org.yoya.com.yoyaorg.network.DownloadApiDataHandler;
import org.yoya.com.yoyaorg.network.DownloadApiRespBean;
import org.yoya.com.yoyaorg.network.HttpClient;
import org.yoya.com.yoyaorg.utils.FileUtils;
import org.yoya.com.yoyaorg.utils.SharedPreferenceUtils;

import java.io.File;

/**
 * Description:提供接口对外给js调用
 * Author: liaozhiwei
 * Date: 16/9/19
 */
public class YoyaOrgJsHandler {

    private Context mContext;

    private SharedPreferenceUtils mSharedPreferenceUtils;

    public YoyaOrgJsHandler(Context context) {
        this.mContext = context;
        mSharedPreferenceUtils = new SharedPreferenceUtils(context);
    }


    @JavascriptInterface
    public void onpenFileChoser() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        ((Activity) mContext).startActivityForResult(Intent.createChooser(i, "File Chooser"),
                MainActivity.REQUEST_CODE_CHOSE_FILE);
    }

    @JavascriptInterface
    public void openFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(mContext, "文件不存在", Toast.LENGTH_SHORT).show();
        } else {
            FileUtils.openFile(mContext, file);
        }

    }

    @JavascriptInterface
    public void saveLoginInfo(String username, String psw) {
        mSharedPreferenceUtils.writeData("loginInfo", "username", username);
        mSharedPreferenceUtils.writeData("loginInfo", "psw", psw);
    }

    @JavascriptInterface
    public String readLoginInfo() {
        String username = mSharedPreferenceUtils.readData("loginInfo", "username");
        String psw = mSharedPreferenceUtils.readData("loginInfo", "psw");
        LoginModel model = new LoginModel();
        model.username = username;
        model.psw = psw;
        return new Gson().toJson(model);
    }

    @JavascriptInterface
    public void downloadFile(final String fileUrl) {

        HttpClient.downloadFile((Activity) mContext, fileUrl, new DownloadApiDataHandler<DownloadApiRespBean>() {
            @Override
            public void onDone(int code, String msg, final DownloadApiRespBean data) {
                //下载成功
                if (code == 200) {
                    Log.e("test", "下载成功－－》");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                File file = new File(Constants.DOWNLOAD_FILE_DIR);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                Log.e("test", FileUtils.getFileNameByPath(fileUrl));
                                FileUtils.saveFile(Constants.DOWNLOAD_FILE_DIR + FileUtils
                                        .getFileNameByPath(fileUrl), data.getData());
                            } catch (Exception e) {

                            }
                        }
                    }).start();


                    //下载失败
                } else {
                    Log.e("test", "下载失败－－》" + msg);
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(int code, String msg, DownloadApiRespBean data, long progress, long total) {
                Log.e("test", "下载－－》" + progress);
            }
        });
    }
}
