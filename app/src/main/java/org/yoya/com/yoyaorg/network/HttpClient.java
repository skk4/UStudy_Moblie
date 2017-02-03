package org.yoya.com.yoyaorg.network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.yoya.com.yoyaorg.constants.Constants;
import org.yoya.com.yoyaorg.model.FileModel;
import org.yoya.com.yoyaorg.update.UpdateInfoApiResp;

import java.io.File;

import cz.msebera.android.httpclient.Header;

/**
 * Created by liaozhiwei on 16/5/26.
 */
public class HttpClient {

    /**
     * 上传文件
     *
     * @param activity
     * @param fileModel
     * @param dataHandler
     */
    public static void uploadFile(final Activity activity, FileModel fileModel, final HttpApiDataHandler<FileUploadModel> dataHandler) {
        StringBuilder apiUrl = new StringBuilder();
        apiUrl.append(Constants.HTTP_CLIENT_BASE_URL).append("do?action=api/wap/school_public&start=api_commonUploadFile")
                .append("&is_file=1&is_single=1&orgFileNames=")
                .append(fileModel.name)
                .append("&sizes=").append(fileModel.size);

        Log.d("test", "uploadFile request===" + apiUrl.toString());
        RequestParams params = new RequestParams();
        try {
            params.put("file", new File(fileModel.path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient mClient = new AsyncHttpClient();
        mClient.post(activity, apiUrl.toString(), params, new BaseJsonHttpResponseHandler<FileUploadModel>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, FileUploadModel response) {
                Log.d("test", "上传  == " + rawJsonResponse);
                if (statusCode == 200 && response != null) {
                    if (dataHandler != null)
                        dataHandler.onDone(Integer.parseInt(response.code), response.msg, response);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, FileUploadModel errorResponse) {
                Log.d("test", "上传 失败啦 == " + rawJsonData);
                if (dataHandler != null)
                    dataHandler.onDone(statusCode, "上传失败", null);
            }

            @Override
            protected FileUploadModel parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return YoyaJsonParse.parseGSON(rawJsonData, FileUploadModel.class);
            }
        });
    }

    /**
     * 下载文件
     *
     * @param activity
     * @param url      文件的web地址
     * @param handler  回调
     */
    public static void downloadFile(final Activity activity,
                                    String url,
                                    final DownloadApiDataHandler<DownloadApiRespBean> handler) {
        AsyncHttpClient client = new AsyncHttpClient();
        String[] allowedContentTypes = new String[]{".*"};
        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  final byte[] binaryData) {
                DownloadApiRespBean bean = new DownloadApiRespBean();
                bean.setCode(200);
                bean.setData(binaryData);
                bean.setMsg("onSuccess");
                handler.onDone(200, "onSuccess", bean);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {
                DownloadApiRespBean bean = new DownloadApiRespBean();
                bean.setCode(-1);
                bean.setData(binaryData);
                bean.setError(error);
                bean.setMsg("onFailure");
                handler.onDone(-1, "onFailure", bean);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                handler.onProgress(-1, null, null, bytesWritten, totalSize);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }
        });
    }

    /**
     * 获取app更新信息
     *
     * @param context
     * @param app_name
     * @param bulid_no
     * @param dataHandler
     */
    public static void queryAppVersionInfo(Context context, String app_name, int bulid_no, final HttpApiDataHandler<UpdateInfoApiResp.UpdateInfo> dataHandler) {
        RequestParams params = new RequestParams();
        params.put("action", "api/public_all");
        params.put("start", "app_update_info");
        params.put("app_os", "Android");
        params.put("app_name", app_name);
        params.put("cur_bulid_no", bulid_no);

        Log.e("queryAppVersionInfo","queryAppVersionInfo== URL==  "+Constants.MAIN_URL + "?" + params);
        AsyncHttpClient mClient = new AsyncHttpClient();
        mClient.post(Constants.MAIN_URL, params, new BaseJsonHttpResponseHandler<UpdateInfoApiResp>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, UpdateInfoApiResp response) {
                Log.w("queryAppVersionInfo","获取app更新信息  == " + rawJsonResponse);
                if (statusCode == 200 && response != null) {
                    if (dataHandler != null)
                        dataHandler.onDone(response.code, response.msg, response.data);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, UpdateInfoApiResp errorResponse) {
                Log.w("queryAppVersionInfo","获取app更新信息  失败啦 == " + rawJsonData);
                if (dataHandler != null)
                    dataHandler.onDone(statusCode, "请求失败", null);
            }

            @Override
            protected UpdateInfoApiResp parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return YoyaJsonParse.parseGSON(rawJsonData, UpdateInfoApiResp.class);
            }
        });

    }

    /**
     * 下载文件
     *
     * @param activity
     * @param url      文件的web地址
     * @param handler  回调
     */
    public static void asyncDownloadFile(final Activity activity,
                                         String url,
                                         final DownloadApiDataHandler<DownloadApiRespBean> handler) {
        AsyncHttpClient client = new AsyncHttpClient();
        String[] allowedContentTypes = new String[]{".*"};
        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  final byte[] binaryData) {
                DownloadApiRespBean bean = new DownloadApiRespBean();
                bean.setCode(200);
                bean.setData(binaryData);
                bean.setMsg("onSuccess");
                handler.onDone(200, "onSuccess", bean);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {
                DownloadApiRespBean bean = new DownloadApiRespBean();
                bean.setCode(-1);
                bean.setData(binaryData);
                bean.setError(error);
                bean.setMsg("onFailure");
                handler.onDone(-1, "onFailure", bean);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                handler.onProgress(-1, null, null, bytesWritten, totalSize);
            }

            @Override
            public void onRetry(int retryNo) {
                super.onRetry(retryNo);
            }
        });
    }


}
