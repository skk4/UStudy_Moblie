package org.yoya.com.yoyaorg.update;

/**
 * Created by liujf on 16/3/14.
 * {"code":"200","msg":"获取成功","data":{
 *   "app_size":"10.50",
 *   "bulid_no":"1",
 *   "download_url":"https://github.com/ghostSamu/Yoya-/blob/mine/Yoya.plist",
 *   "is_force":"1",
 *   "version_content":"支持手机编辑互动电影。",
 *   "version_no":"1.0beta"}}
 */
public class UpdateInfoApiResp {
    public int code;
    public String msg;

    public UpdateInfo data;

    public class UpdateInfo {
        public String bulid_no;
        public String download_url;

        public String is_force;//1,是强制更新;
        public String app_size;

        public String version_no;
        public String version_content;
    }
}
