package com.mrjan.minilzo.api;

import com.mrjan.minilzo.core.MinilzoLibrary;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import com.sun.jna.ptr.ByteByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author JianLinWei
 * @description
 * @date 2023-08-15 17:47
 */
public class MinilzoAPi {
    static{
        try{
            if(Platform.isWindows()){
                String  dlls[]  = Platform.is64Bit() ? new String[]{"minilzo"}
                        :new String[]{"minilzo"};

                String sys_tmp_dir = System.getProperty("java.io.tmpdir");
                File file = new File(sys_tmp_dir +"/minilzo");
                if(!file.exists()){
                    Files.createDirectory(file.toPath());
                }


                String platformperfix  = Platform.RESOURCE_PREFIX;

                for(int i = 0 ; i < dlls.length ; i++){
                    String file_name = dlls[i]+ ".dll";
                    File dll_file = new File(file, file_name);
                    try(FileOutputStream outputStream = new FileOutputStream(dll_file)){
                        try(InputStream inputStream  = MinilzoLibrary.class.getClassLoader().getResourceAsStream(platformperfix +"/"+file_name)){
                            byte[]  buf = new byte[inputStream.available()];
                            int count = 0;
                            while((count = inputStream.read(buf, 0, buf.length))> 0){
                                outputStream.write(buf, 0, count);
                            }
                        }
                    }

                    NativeLibrary.addSearchPath(dlls[i], file.getAbsolutePath());
                }
                file.deleteOnExit();

            }

        }catch(Exception e){
           e.printStackTrace();
        }
    }


    public static void main(String[] args) {
       String pointer = MinilzoLibrary.API_LIBRARY.lzo_version_string();
       System.out.println( "version:"+pointer);
       int init = MinilzoLibrary.API_LIBRARY.lzo_init();
       System.out.println("init:" + init);
       if (init != 0){
           System.out.println("初始化失败");
           return;
       }
       byte[]  strBytes = "eqrewruqweurqoweuroqefka".getBytes();
       byte[]  outByte = new byte[1024];
       IntByReference outLen = new IntByReference();

       MinilzoLibrary.API_LIBRARY.lzo1x_1_compress(strBytes,strBytes.length, outByte,outLen,null);
       System.out.println("compress out len:" + outLen.getValue());
       System.out.println("compress out:" + new String(outByte));


        byte[]  outByteDe = new byte[1024];
        IntByReference outLenDe = new IntByReference();
        MinilzoLibrary.API_LIBRARY.lzo1x_decompress(outByte,outLen.getValue(), outByteDe,outLenDe,null);
        System.out.println("decompress out len:" + outLenDe.getValue());
        System.out.println("decompress out:" + new String(outByteDe));





    }
}
