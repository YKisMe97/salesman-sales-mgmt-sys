package com.example.priyanka.mapsnearbyplaces.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;

public class FTPManager {
    public FTPClient mFTPClient = null;
    private static final String TAG = "FYP";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    public static boolean downloadProductFile(Context context, FTPClient ftpClient, String remoteFilePath, String savePath) throws IOException {
        File downloadFile = new File(savePath);
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(context,
                new String[] { downloadFile.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
        File parentDir = downloadFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }

        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.retrieveFile(remoteFilePath, outputStream);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static boolean uploadFile(FTPClient ftpClient, String localFilePath, String remoteDirPath) {
        FileInputStream fileInputStream = null;
        try {
            File file = new File(localFilePath);
            fileInputStream = new FileInputStream(file);
            ftpClient.changeWorkingDirectory(remoteDirPath);
            boolean status = ftpClient.storeFile(remoteDirPath + "/" + file.getName(), fileInputStream);
            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean ftpConnect(Activity activity, String host, String username, String password, int port) {
        try {
            return new connect(activity, host, username, password,port).execute().get();
        }
        catch (Exception e) {
            return false;
        }
    }

        public class connect extends AsyncTask<Void, Void, Boolean> {
            private String host;
            private String username;
            private String password;
            private int port;
            private Activity activity;

            connect(Activity activity, String host, String username, String password, int port) {
                this.host = host;
                this.password = password;
                this.username = username;
                this.port = port;
                this.activity = activity;
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                mFTPClient = new FTPClient();
                try {
                    FTPManager.verifyStoragePermissions(activity);
                    mFTPClient.connect(host, port);
                    mFTPClient.login(username, password);
                    Log.d(TAG,"Logged in");
                    int replyCode = mFTPClient.getReplyCode();
                    if (!FTPReply.isPositiveCompletion(replyCode)) {
                        Log.d(TAG,"FTP server refused connection.");
                        mFTPClient.disconnect();
                    }
                    mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                    mFTPClient.setBufferSize(1024);
                    mFTPClient.enterLocalPassiveMode();
                    final File dir = new File(FileManager.getPrivateDirPath(activity)+File.separator+"product");
                    Log.d(TAG,dir.toString());
                    if (!dir.exists()) {
                        dir.mkdir();
                    }

                    FTPFile[] filesList = null;
                    filesList = mFTPClient.listFiles("/KUAN/product/");
                    if (filesList == null) {
                        Log.d(TAG,"file list is null");
                        return false;
                    }
                    Boolean status = false;
                    for (int fl = 0; fl < filesList.length; fl++) {
                        try {
                            Log.d(TAG, filesList[fl].getName() + " " + filesList.length);
                            //FileOutputStream desFileStream=null;
                            OutputStream outputStream =null;
                            try {
                                outputStream = new BufferedOutputStream(new FileOutputStream(dir+ dir.separator + filesList[fl].getName()));
                                 //desFileStream = new FileOutputStream(dir + dir.separator + filesList[fl].getName());
                                    status = mFTPClient.retrieveFile(filesList[fl].getName(), outputStream);

                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                Log.d(TAG,"Message 1: "+e);
                            }
                            finally {
                                if (outputStream!=null) {
                                    outputStream.close();
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG,"Message 2: "+e);
                        }
                    }
                    return status;
                }
                catch (Exception e) {
                    Log.d(TAG, "Error: " + e);
                    e.printStackTrace();
                }
                finally {
                    if (mFTPClient!=null)
                    disconnectFromFtpServer();
                }
                return false;
            }
        }

    public boolean disconnectFromFtpServer() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            Log.d(TAG,"Message dc: "+ioException);
        }
        return false;
    }

    public boolean uploadAllToServer(Context context,String host, String username, String password,int port,String desDirectory) {
        try {
            Log.d("FYP", "upload success");
            return new uploadAll(context,host, username, password, port, desDirectory).execute().get();
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("FYP", "upload fail");
            return false;
        }
    }

    public class uploadAll extends AsyncTask<Void, Void, Boolean> {
        private String host;
        private String username;
        private String password;
        private int port;
        private String desDirectory;
        private Context context;

        uploadAll(Context context,String host, String username, String password,int port,String desDirectory) {
            this.context = context;
            this.host = host;
            this.password = password;
            this.username = username;
            this.port = port;
            this.desDirectory = desDirectory;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                mFTPClient = new FTPClient();
                // connecting to the host
                mFTPClient.connect(host, port);
                boolean status = mFTPClient.login(username, password);

                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();
                Log.d("FYP", "Succeed");
                String path = FileManager.getPrivateDirPath(context)+File.separator+"product";
                Log.d("FYP", "Path: " + path);
                File directory = new File(path);
                File[] files = directory.listFiles();
                for (int i = 0; i < files.length; i++) {
                    FileInputStream srcFileStream = new FileInputStream(path+"/"+files[i].getName());
                    boolean changeWorkingDirectory = mFTPClient.changeWorkingDirectory(desDirectory);
                    if (changeWorkingDirectory) {
                        Log.d("FYP",files[i].getName());
                        status = mFTPClient.storeFile(files[i].getName(), srcFileStream);
                        srcFileStream.close();
                    }

                }

                mFTPClient.disconnect();
                return status;
            }
            catch(Exception e){
                e.printStackTrace();
                Log.d("FYP", "Error");
            }
            return false;
        }
    }
    public static boolean isDirectoryExist(FTPClient ftpClient, String remoteDirPath) throws IOException {
        return ftpClient.changeWorkingDirectory(remoteDirPath);
    }

    public static boolean createDirectory(FTPClient ftpClient, String remoteDirPath) throws IOException {
        return ftpClient.makeDirectory(remoteDirPath);
    }
}
