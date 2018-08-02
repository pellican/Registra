package com.progetto.registra.database;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.progetto.registra.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
public class Backupdb {
    public static void backupDatabase(String dbPath, String nomeFileBackup, Activity act, Resources res) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                File currentDB = new File(dbPath);
                File backupDB = new File(sd, nomeFileBackup);
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(act.getBaseContext(), res.getString(R.string.msg_db_location) + "\n " + backupDB.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                return;
            }
            Log.e("Permission denied", "Can't write to SD card, add permission");
        } catch (IOException e) {
            Toast.makeText(act.getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        } catch (Throwable th) {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    public static void restoreDatabase(String dbPath, String percorsoFileBackup, Activity act, Resources res) {
        try {
            if (Environment.getExternalStorageDirectory().canRead()) {
                File currentDB = new File(dbPath);
                File backupDB = new File(percorsoFileBackup);
                copyFile(backupDB, currentDB);
                Toast.makeText(act.getBaseContext(), String.format(res.getString(R.string.restore_done)+percorsoFileBackup), Toast.LENGTH_SHORT).show();
                return;
            }
            Log.e("Permission denied", "Can't read from SD card, add permission");
        } catch (IOException e) {
            Toast.makeText(act.getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
