package com.sample.xianhang_feelsbook.utils;

/**
 * common utils
 */
public class CommonUtils {

    /**
     * int to emotion string
     *
     * @param emotionType
     * @return
     */
    public static String intToEmotionStr(int emotionType){
        switch (emotionType){
            case 1:
                return "love";
            case 2:
                return "joy";
            case 3:
                return "surprise";
            case 4:
                return "anger";
            case 5:
                return "sadness";
            case 6:
                return "fear";
            default:
                return "love";
        }
    }
}
