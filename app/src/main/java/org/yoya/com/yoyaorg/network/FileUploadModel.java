package org.yoya.com.yoyaorg.network;

/**
 * Created by yisheng on 2016/1/25.
 */
public class FileUploadModel {

    public String code;
    public String msg;
    public FileData data;

    public class FileData {
        public String fileNames;
    }

    public String orgFileNames;
    public String sizes;
    public String is_single;
}
