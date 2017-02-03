package org.yoya.com.yoyaorg.network;

/**
 * Created by Administrator on 2016/3/29.
 */
public abstract  class DownloadApiDataHandler<T> {
    /**
     * 当调用服务器接口完成时,调用这个方法
     */
    public abstract void onDone(int code, String msg,T data);

    public abstract void onProgress(int code, String msg,T data,long progress,long total);
}
