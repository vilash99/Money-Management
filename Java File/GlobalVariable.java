package com.example.vilash.moneymanagement;

import java.util.*;
import java.text.NumberFormat;

public class GlobalVariable {
    public static String currentYear;
    public static int currentYearInt;
    public static String globalQuery;
    public static int selectedID;
    public static boolean editMode;

    public static String getCurrencyString(String myStr) {
        int ix = Integer.parseInt(myStr);
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        String s = n.format(ix);
        s = s.replace("$", "");
        s = s.replace(".00", "");
        return s;
    }
}
