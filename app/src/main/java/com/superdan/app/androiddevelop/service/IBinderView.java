package com.superdan.app.androiddevelop.service;

/**
 * Created by Administrator on 2016/4/5.
 */
public interface IBinderView {
    /**
     * 开始下载
     */
    void downloadStart();

    /**
     *下载成功
     */
    void downloadSuccess(String imageFilePath);

    /**
     *下载失败
     */
    void downloadFailure();
}
