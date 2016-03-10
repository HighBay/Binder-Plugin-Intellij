package BinderPackage.code.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by Aaron on 3/7/2016.
 */
public class ConsolePrintUtils {

    private static boolean mPrintThese = true;

    public static void printListIntoConsole(List<String> listToPrint, String tag) {
        if (mPrintThese) {
            for (int i = 0; i < listToPrint.size(); i++) {
                printToConsole(listToPrint.get(i), tag);
            }
        } else {
            printingDisabled();
        }
    }

    public static void printToConsole(String StringToPrint, String tag) {
        if (mPrintThese) {
            System.out.println(ConsolePrintUtils.CreateTag(tag) + "\t\t" + StringToPrint);
        } else {
            printingDisabled();
        }
    }

    private static void printingDisabled() {
        System.out.println("Printing Disabled By Developer to change go to ConsolePrintUtils and change mPrintThese boolean");
    }


    private static String CreateTag(String Tag) {
        int tagMaxLength = 20;
        if (Tag.length() < tagMaxLength) {
            while (Tag.length() != tagMaxLength) {
                Tag = Tag.concat("*");
            }
        } else if (Tag.length() >= tagMaxLength) {
                Tag = Tag.substring(0,tagMaxLength-1);
        }
        return Tag;
    }
}
