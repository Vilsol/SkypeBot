package me.vilsol.skypebot;

public class Utils {

    public static boolean isInteger(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(Exception ignore){
        }

        return false;
    }

    public static boolean isDouble(String s){
        try{
            Double.parseDouble(s);
            return true;
        }catch(Exception ignore){
        }

        return false;
    }

}
