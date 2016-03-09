package BinderPackage.code.utils;

import java.util.List;

/**
 * Created by Aaron on 3/7/2016.
 */
public class ConsolePrintUtils {

    public static void printListIntoConsole(List<String> listToPrint, String tag) {
        for (int i = 0; i < listToPrint.size(); i++) {
            printToConsole(listToPrint.get(i), tag);
        }
        System.out.print("\n");
    }

    public static void printToConsole(String StringToPrint, String tag) {
        System.out.print("\n" + tag + "\t" + StringToPrint);
    }

}
