package com.engineer.android.library.util;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 *
 * Created by L.J on 2016/6/5.
 */
public final class AndroidInstallationUtil {
    private static String ID;
    private static final String INSTALLATION = "Installation";

    public synchronized static String getID(Context context){
        if(ID == null){
            try {
                File installation = new File(context.getFilesDir(),INSTALLATION);
                if(!installation.exists())
                    writeInstallationFile(installation);
                ID = readInstallationFile(installation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation,"r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}
