package com.spring2ljl.util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QiniuUtil {
    private static String ak;
    private static String sk;
    private static String bucket;

    @Value("${qiniu.accesskey}")
    private  void setAk(String ak) {
        QiniuUtil.ak = ak;
    }
    @Value("${qiniu.secretkey}")
    private  void setSk(String sk) {
        QiniuUtil.sk = sk;
    }
    @Value("${qiniu.bucket}")
    private  void setBucket(String bucket) {
        QiniuUtil.bucket = bucket;
    }
    private static String token(){
        Auth auth = Auth.create(ak, sk);
        return auth.uploadToken(bucket);
    }
    public static void upload(String path,String name) throws QiniuException {
        Configuration configuration = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(configuration);
        uploadManager.put(path,name,token());
    }
}
