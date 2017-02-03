package org.yoya.com.yoyaorg.update;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


import org.yoya.com.yoyaorg.R;
import org.yoya.com.yoyaorg.activity.MainActivity;
import org.yoya.com.yoyaorg.constants.Constants;
import org.yoya.com.yoyaorg.dialog.LoadingDialog;
import org.yoya.com.yoyaorg.dialog.UpdateTipsDialog;
import org.yoya.com.yoyaorg.network.DownloadApiDataHandler;
import org.yoya.com.yoyaorg.network.DownloadApiRespBean;
import org.yoya.com.yoyaorg.network.HttpApiDataHandler;
import org.yoya.com.yoyaorg.network.HttpClient;
import org.yoya.com.yoyaorg.utils.FileUtils;
import org.yoya.com.yoyaorg.utils.StringUtils;

import java.io.File;

/**
 * Created by lzhiwei on 2016/3/29.
 */
public class UpdateUtils {

    public static void checkUpdate(final Activity activity, final boolean needShowDialog) {
        final int versionCode = UpdateUtils.getVersionCode(activity);
        final Dialog dailog = LoadingDialog.createLoadingDialog(activity, "检查中");
        if (needShowDialog) {
            dailog.show();
        }
        HttpClient.queryAppVersionInfo(activity, "yoya_org", versionCode,
                new HttpApiDataHandler<UpdateInfoApiResp.UpdateInfo>() {
                    @Override
                    public void onDone(int code, String msg, UpdateInfoApiResp.UpdateInfo data) {
                        dailog.dismiss();
                        if (code == 200 && !StringUtils.isEmpty(data.bulid_no)) {
                            if (versionCode < Integer.valueOf(data.bulid_no)) {//需要更新
                                    UpdateUtils.showUpdateTipsDialog(activity, data);
                            } else {//不需要更新
                                if (needShowDialog) {
                                    Toast.makeText(activity, "已是最新版", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            if (needShowDialog) {
                                Toast.makeText(activity, "检查更新失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    /**
     * 更新
     *
     * @param activity
     * @param data
     */
    private static void update(final Activity activity, final UpdateInfoApiResp.UpdateInfo data) {
        final Dialog dialog = new LoadingDialog().createLoadingDialog(activity, "下载中");
        dialog.setCancelable(false);
        if (data.is_force.equals("1")) {
            dialog.show();
        }

        File dir = new File(Constants.DOWNLOAD_UPDATE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        final String localFilePath = Constants.DOWNLOAD_UPDATE_DIR + "yoya_org_android.apk";
        HttpClient.asyncDownloadFile(activity,
                data.download_url,
                new DownloadApiDataHandler<DownloadApiRespBean>() {

                    @Override
                    public void onDone(int code, String msg, final DownloadApiRespBean data) {
                        if (code == 200) {//下载完成
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        FileUtils.saveFile(localFilePath, data.getData());
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (dialog != null && dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }

                                                Toast.makeText(activity, "下载完成", Toast.LENGTH_SHORT).show();
                                                openApk(localFilePath, activity);
                                            }
                                        });
                                    } catch (Exception e) {
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (dialog != null && dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Toast.makeText(activity, "保存失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }).start();
                        } else {
                            Toast.makeText(activity, "检查更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onProgress(int code, String msg, DownloadApiRespBean data, long progress, long total) {
                        Log.e("onProgress", "已下载->" + progress + "   byte；总共->" + total);
                        double a = progress / (double) total;
                        int hasUploadLength = (int) (a * 100);
                        showNotification(activity, hasUploadLength);
                    }
                }
        );
    }

    /**
     * 通知栏显示进度
     *
     * @param activity
     * @param progress
     */
    private static void showNotification(Activity activity, int progress) {
        if (progress == 100) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager mNotificationManager = (NotificationManager) activity
                    .getApplicationContext().getSystemService(ns);
            mNotificationManager.cancel(12345);
            return;
        }
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) activity
                .getSystemService(ns);
        int icon = R.mipmap.ic_launcher;
        CharSequence tickerText = "系统更新";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        Intent notificationIntent = new Intent(activity, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(activity, 1011,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews contentView = new RemoteViews(activity.getPackageName(),
                R.layout.update_notification_view);
        contentView.setProgressBar(R.id.pb_update, 100, progress, false);
        contentView.setTextViewText(R.id.tv_title, "更新中  " + progress + "%");
        notification.contentView = contentView;
        notification.contentIntent = contentIntent;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(12345, notification);
    }

    private static void openApk(String path, Activity activity) {
        try {
            Intent intent = new Intent();
            File file = new File(path);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            activity.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(activity, "打开APK文件失败", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 显示更新提示框
     *
     * @param activity
     * @param data
     */
    public static void showUpdateTipsDialog(final Activity activity, final UpdateInfoApiResp.UpdateInfo data) {
        final Dialog dialog = UpdateTipsDialog.showDialog(activity);
        TextView tvContent = (TextView) dialog.findViewById(R.id.tv_dailog_content);
        TextView tvConfirm = (TextView) dialog.findViewById(R.id.tv_dialog_confirm);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_dialog_cancel);
        tvContent.setText(data.version_content.replace("\\n", "\n"));
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                update(activity, data);
            }
        });
        if (data.is_force.equals("1")) {
            dialog.setCancelable(false);
            tvCancel.setVisibility(View.GONE);
        } else {
            dialog.setCancelable(true);
            tvCancel.setVisibility(View.VISIBLE);
        }
        dialog.show();
    }


    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}
