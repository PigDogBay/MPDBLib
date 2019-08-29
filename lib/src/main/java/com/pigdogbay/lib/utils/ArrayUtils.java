package com.pigdogbay.lib.utils;

/**
 * Created by Mark on 08/08/2016.
 */
public class ArrayUtils {

    /**
     * Reverse operation of Arrays.toString(int[])
     * @param str [1, 2, 3, 4, 5]
     * @return reverse array
     */
    public static int[] fromString(String str){
        String[] strings = str.replace("[", "").replace("]", "").split(",");
        int[] result = new int[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(strings[i].trim());
        }
        return result;
    }

    public static boolean contains(int[] data, int value){
        for (int aData : data) {
            if (aData == value) return true;
        }
        return false;
    }
    public static boolean contains(int[] data, int value, int len){
        for (int i=0; i<len; i++) {
            if (data[i] == value) return true;
        }
        return false;
    }
    public static void clear(int[] data, int value){
        for (int i=0;i<data.length;i++){
            data[i]=value;
        }
    }
    public static void clear(char[] data, char value){
        for (int i=0;i<data.length;i++){
            data[i]=value;
        }
    }

    public static int indexOf(char[] data, char c){
        for (int i=0;i<data.length;i++) {
            if (data[i]==c) return i;
        }
        return -1;
    }
    public static int indexOf(char[] data, char c, int len){
        for (int i=0;i<len;i++) {
            if (data[i]==c) return i;
        }
        return -1;
    }
}
