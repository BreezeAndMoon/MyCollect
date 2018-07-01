package com.joint.jointpolice.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Joint229 on 2018/4/2.
 */

public class Stream2Byte {

    //从流中读取数据
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}
