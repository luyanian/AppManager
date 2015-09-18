package cn.ryon.manager.http;

/**
 * Created by Administrator on 2015/9/15.
 */
public abstract class AsyncHttpHandleListener {
    public abstract void success();
    public abstract void failed();
    public void cancle(){};
}
