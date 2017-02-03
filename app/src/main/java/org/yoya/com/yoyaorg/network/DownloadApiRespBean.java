package org.yoya.com.yoyaorg.network;

/**
 * Created by Administrator on 2016/3/29.
 */
public class DownloadApiRespBean {
    private int code;
    private String msg;
    private byte[] data;
    private  Throwable error;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}