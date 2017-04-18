package com.liyanli.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liyanli on 2017/4/7.
 */

public class StreamUtil {
    /**
     * 流转换成字符串
     * @param is 流对象
     * @return  流转换成字符串 返回null代表异常
     */
    public static String Stream2String(InputStream is){
        //1.在读取过程中，将读取的内容存储到缓存中，然后一次性转换成字符串
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //2.读流的操作，读到没有数据为止
        byte[] buffer = new byte[1024];
        //3.记录流的临时变量
        int temp = -1;
        try{
            while((is.read(buffer))!=-1){
                bos.write(buffer,0,temp);
            }
            //返回读取的数据
               return bos.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                is.close();
                bos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
