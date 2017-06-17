package com.asdc.mybattery.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.os.Environment;
import android.widget.Toast;

public class FileHelper {
	
	/**
     * SD卡下创建文件夹
     * @param folder
     */
    public static boolean createFolder(String path,Context mContext){
    	boolean isSuccess = false;
        File file=new File(path); 
        if(!file.exists()) 
         file.mkdir(); 
        isSuccess = true;
        if(!file.exists()) {
        	isSuccess = false;
        	if(mContext!=null)
        		Toast.makeText(mContext, "未检测到SD卡,某些功能可能无法正常使用!", Toast.LENGTH_LONG).show();
        }
        
        return isSuccess;
    }
	
	

}
