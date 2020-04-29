package utils;

import java.util.List;

public class ListMatch {

    public static boolean listMatch(String base, String[] target){
        /**
         * Base: unformatted list string, e.g. "a,b,"cC""
         * Target: formatted list, e.g. [a,b,c]
         * Return: whether unformatted string contain whatever element in the target list
         */
        if (target[0].equals("")){ return true; }
        for (String s:target){
            if (base.contains(s)){
                return true;
            }
        }
        return false;
    }

}