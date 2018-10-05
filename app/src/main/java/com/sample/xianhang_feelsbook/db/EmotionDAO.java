package com.sample.xianhang_feelsbook.db;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sample.xianhang_feelsbook.bean.Emotion;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * emotion dao
 */
public class EmotionDAO {
    private void writeTxtToFile(String strcontent,  String fileName) {
        String strFilePath = createFile(fileName);
        try {
            OutputStreamWriter write = null;
            BufferedWriter out = null;
            if (strFilePath != null) {
                try {
                    write = new OutputStreamWriter(new FileOutputStream(  strFilePath), Charset.forName("gbk"));
                    out = new BufferedWriter(write, 1000);
                } catch (Exception e) {
                }
            }
            out.write(strcontent);
            out.flush();
            out.close();


        } catch (Exception e) {
        }
    }


    /**
     * add new record
     *
     * @param emotion
     * @return true/false
     */
    public boolean insert(Emotion emotion) {
        Gson gson = new Gson();
        String strFilePath = createFile("a.txt");
        String oldStr=convertCodeAndGetText(strFilePath);
        List<Emotion> listOld=new ArrayList<>();
        if (!"".equals(oldStr)&&null!=oldStr){
            listOld= gson.fromJson(oldStr, new TypeToken<List<Emotion>>(){}.getType());
        }
        long  time=new Date().getTime();
        emotion.id=time;
        listOld.add(emotion);
        String json=gson.toJson(listOld);
        writeTxtToFile(json, "a.txt");

        return true;
    }
    public String convertCodeAndGetText(String filePath) {
        BufferedReader reader = null;
        String text = "";
        FileInputStream fis = null;
        BufferedInputStream in = null;
        InputStreamReader isr = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            in = new BufferedInputStream(fis);
            in.mark(4);
            byte[] first3bytes = new byte[3];
            in.read(first3bytes);
            in.reset();
            if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB
                    && first3bytes[2] == (byte) 0xBF) {
                isr = new InputStreamReader(in, "utf-8");
                reader = new BufferedReader(isr);
            } else if (first3bytes[0] == (byte) 0xFF
                    && first3bytes[1] == (byte) 0xFE) {
                isr = new InputStreamReader(in, "unicode");
                reader = new BufferedReader(isr);
            } else if (first3bytes[0] == (byte) 0xFE
                    && first3bytes[1] == (byte) 0xFF) {
                isr = new InputStreamReader(in, "utf-16be");
                reader = new BufferedReader(isr);
            } else if (first3bytes[0] == (byte) 0xFF
                    && first3bytes[1] == (byte) 0xFF) {
                isr = new InputStreamReader(in, "utf-16le");
                reader = new BufferedReader(isr);
            } else {
                isr = new InputStreamReader(in, "GBK");
                reader = new BufferedReader(isr);
            }
            String str = reader.readLine();
            while (str != null) {
                text = text + str + "\n";
                str = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return text;
    }

    public String createFile(String fileName_ ) {
        String filePath = null;
        boolean hasSDCard =Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            filePath =Environment.getExternalStorageDirectory().toString() + File.separator +fileName_;
        } else
            filePath =Environment.getDownloadCacheDirectory().toString() + File.separator +fileName_;

        try {
            File fileName = new File(filePath);
            if (!fileName.exists()) {
                fileName.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }



    /**
     * get all data
     *
     * order by time
     *
     * @return
     */
    public List<Emotion> queryAll(){
        Gson gson = new Gson();
        String strFilePath = createFile("a.txt");
        String oldStr=convertCodeAndGetText(strFilePath);
        List<Emotion> listOld=new ArrayList<>();
        if (!"".equals(oldStr)&&null!=oldStr){
            listOld= gson.fromJson(oldStr, new TypeToken<List<Emotion>>(){}.getType());
        }

        return listOld;
    }

    /**
     * get data by emotion type
     *
     * order by time
     *
     * @return
     */
    public List<Emotion> queryByEmotionType(int type){
        Gson gson = new Gson();
        String strFilePath = createFile("a.txt");
        String oldStr=convertCodeAndGetText(strFilePath);
        List<Emotion> listOld=new ArrayList<>();
        if (!"".equals(oldStr)&&null!=oldStr){
            listOld= gson.fromJson(oldStr, new TypeToken<List<Emotion>>(){}.getType());
        }

        List<Emotion> list = new ArrayList<>();
        for (int i=0;i<listOld.size();i++){
            if (listOld.get(i).emotion==type){
                list.add(listOld.get(i));
            }
        }
        return list;
    }

    /**
     * update emotion
     *
     * @param emotion
     * @return ture/false
     */
    public boolean update(Emotion emotion){


        Gson gson = new Gson();
        String strFilePath = createFile("a.txt");
        String oldStr=convertCodeAndGetText(strFilePath);
        List<Emotion> listOld=new ArrayList<>();
        if (!"".equals(oldStr)&&null!=oldStr){
            listOld= gson.fromJson(oldStr, new TypeToken<List<Emotion>>(){}.getType());
        }

        for (int i=0;i<listOld.size();i++){
            if (listOld.get(i).id==emotion.id){
                listOld.get(i).emotion=emotion.emotion;
                listOld.get(i).comment=emotion.comment;
                listOld.get(i).time=emotion.time;
            }
        }

        String json=gson.toJson(listOld);
        writeTxtToFile(json, "a.txt");
        return true;
    }

    /**
     * delate one record
     *
     * @param id emotion id
     * @return true/false
     */
    public boolean delete(long id){
        Gson gson = new Gson();
        String strFilePath = createFile("a.txt");
        String oldStr=convertCodeAndGetText(strFilePath);
        List<Emotion> listOld=new ArrayList<>();
        if (!"".equals(oldStr)&&null!=oldStr){
            listOld= gson.fromJson(oldStr, new TypeToken<List<Emotion>>(){}.getType());
        }

        for (int i=0;i<listOld.size();i++){
            if (listOld.get(i).id==id){
                listOld.remove(i);
            }
        }

        String json=gson.toJson(listOld);
        writeTxtToFile(json, "a.txt");
        return true;
    }


}
