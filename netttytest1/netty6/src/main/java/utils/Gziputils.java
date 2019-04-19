package utils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Gziputils {
    /**
     * 解压缩
     * @param source 原数据，需要解压的数据
     * @return 解压后的数据，恢复的数据
     * @throws IOException
     */
    public static byte[] unzip(byte[] source) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        ByteArrayInputStream in=new ByteArrayInputStream(source);
        //jdk提供，专门用于亚索和解压缩的流对象，可以处理字节数组数据
        GZIPInputStream zipIn=new GZIPInputStream(in);
        byte[] temp=new byte[256];
        int length=0;
        while ((length=zipIn.read(temp,0,temp.length))!=-1){
            out.write(temp,0,length);
        }
        //将字节数组输出流中的数据，转换为一个字节数组
        byte[] target=out.toByteArray();

        zipIn.close();
        out.close();

        return target;

    }

    /**
     * 压缩
     * @param source 源数据，需要压缩的数据
     * @return 压缩后的数据
     * @throws IOException
     */
    public static byte[] zip(byte[] source) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        //jdk提供的，专门用于解压缩的
        GZIPOutputStream zipOut=new GZIPOutputStream(out);
        //将压缩的信息写入内存，写入过程会实现解压缩
        zipOut.write(source);
        //结束
        zipOut.finish();
        byte[] target=out.toByteArray();
        zipOut.close();
        return target;
    }


}
