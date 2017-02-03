package org.yoya.com.yoyaorg.network;

/**
 * Created by liujf on 16/1/21.
 */
public abstract class HttpApiDataHandler<T> {

    /**
     * 当调用服务器接口完成时,调用这个方法
     */
    public abstract void onDone(int code, String msg,T data);

}