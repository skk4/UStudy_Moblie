package org.yoya.com.yoyaorg.constants;

import android.os.Environment;

/**
 * Created by liaozhiwei on 16/5/23.
 */
public class Constants {
    //99cj环境
//    public static final String BASE_URL = "http://school.99cj.com/mobile/activities.html";
//    public static final String HTTP_CLIENT_BASE_URL = "http://school.99cj.com/";
//    public static final String MAIN_URL = "http://www.99cj.com/do";  //99cj主站


    //测试环境
//    public static final String BASE_URL = "http://school.test.yoya.com/mobile/activities.html";
//    public static final String HTTP_CLIENT_BASE_URL = "http://school.test.yoya.com/";
//    public static final String MAIN_URL = "http://test.yoya.com/do";

    //灰度环境
//    public static final String BASE_URL = "http://school.gray.yoya.com/mobile/activities.html";
//    public static final String HTTP_CLIENT_BASE_URL = "http://school.gray.yoya.com/";
//    public static final String MAIN_URL = "http://gray.yoya.com/do";  //灰度主站

    //正式环境
    public static final String BASE_URL = "http://school.yoya.com/mobile/activities.html";
    public static final String HTTP_CLIENT_BASE_URL = "http://school.yoya.com/";
    public static final String MAIN_URL = "http://www.yoya.com/do";  //正式环境主站193


    //----------------------------------------------------------------------------------------------------------------------//
    public static final String PROJECT_BASE_DIR = Environment.getExternalStorageDirectory() + "/YoyaOrg";

    public static final String DOWNLOAD_FILE_DIR = PROJECT_BASE_DIR + "/File/";

    public static final String DOWNLOAD_UPDATE_DIR = PROJECT_BASE_DIR + "/Update/";

}
