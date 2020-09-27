package com.example.priyanka.mapsnearbyplaces.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.priyanka.mapsnearbyplaces.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileManager {
    Context context;
    public static String getPublicDirPath(Context context){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getString(R.string.public_storage_path));
        if (!file.exists()){
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    public static String getPrivateDirPath(Context context){
        //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/.com.android.systemss/sys/com.yingkuan/" + "Sales Management");
        //put in external storage for testing purpose
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getString(R.string.storage_path));
        if (!file.exists()){
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static JSONObject saveTempFile(Context context, String fileName, String data){
        JSONObject jsonObject = new JSONObject();
        FileOutputStream fileOutputStream = null;
        try{
            File privateDir = new File(FileManager.getPrivateDirPath(context) + File.separator + "temp_folder");
            if(!privateDir.exists()) {
                privateDir.mkdirs();
            }
            File file = new File(privateDir, fileName);
            if(!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes("UTF-8"));
            fileOutputStream.flush();
            fileOutputStream.close();
            return jsonObject;
        }
        catch (Exception e){

        }
        finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  jsonObject;
    }

    public static void removeTempFile(Context context, String fileName){
        FileOutputStream fileOutputStream = null;
        try{
            // Remove from private place
            File privateDir = new File(FileManager.getPrivateDirPath(context) + "/temp_folder");
            if(!privateDir.exists()) privateDir.mkdirs();
            File file = new File(privateDir, fileName);
            if(file.exists()) file.delete();

        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static JSONObject saveFile(Context context, String dir, String fileName, String data){
        JSONObject jsonObject = new JSONObject();
        FileOutputStream fileOutputStream = null;
        try {
            File publicDir = new File(FileManager.getPublicDirPath(context) + File.separator + "My Customer");
            if (!publicDir.exists()) publicDir.mkdirs();
            File file = new File(publicDir, fileName);
            if (!file.exists()) file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes("UTF-8"));
            return  jsonObject;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonObject;
    }

    public void moveFile(){

    }

    public static String[] retrieveTextContentAsStringArray(Context context, String filePath){
        String[] stringArray = null;
        File file = new File(filePath);
        Log.d("FYP", file.toString());
        if(file.exists()){
            StringBuffer sb = new StringBuffer();
            BufferedReader br = null;
            Log.d("FYP", "File existed.");
            try {
                br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\r\n");
                }
            }
            catch (Exception e1) {
                Log.d("FYP", "error.");
            }
            finally {
                if(br!=null)
                    try {
                        br.close();
                    } catch (IOException e) {
                        Log.d("FYP", "br is null");
                    }
            }
            if(sb.length()>0){
                stringArray = sb.toString().split("\r\n");
                Log.d("FYP", "Length is more than 0");
            }
        }
        return stringArray;
    }
}
