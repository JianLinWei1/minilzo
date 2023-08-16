package com.mrjan.minilzo.core;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * @author JianLinWei
 * @description 接口层
 * @date 2023-08-15 17:42
 */
public interface MinilzoLibrary extends Library {

    String LIB_NAME = "minilzo";

    MinilzoLibrary API_LIBRARY = Native.load(LIB_NAME, MinilzoLibrary.class);



     String lzo_version_string();

     int lzo_init();

     void lzo1x_1_compress(byte[] in  , int in_len , byte[] out , IntByReference out_len , PointerByReference wrkmem);

     void lzo1x_decompress(byte[] out , int out_len , byte[] in , IntByReference in_len ,PointerByReference wrkmem);

}
