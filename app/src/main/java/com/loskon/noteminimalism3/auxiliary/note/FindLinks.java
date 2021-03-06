package com.loskon.noteminimalism3.auxiliary.note;

import android.app.Activity;

import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Поиск ссылок в тексте и добавление к ним в конце пробела
 */

public class FindLinks {

    private Activity activity;

    private final boolean isWeb;
    private final boolean isMail;
    private final boolean isPhone;

    public FindLinks (Activity activity) {
        this.activity = activity;
        isWeb = GetSharedPref.isWeb(activity);
        isMail = GetSharedPref.isMail(activity);
        isPhone = GetSharedPref.isPhone(activity);
    }

    public String getLinks(String text) {
        if (isWeb) {
            for (String foundString : pullWebLinks(text)) {
                text = methodReplace(foundString, text);
            }
        }

        if (isMail) {
            for (String foundString : pullMailLinks(text)) {
                text = methodReplace(foundString, text);
            }
        }

        if (isPhone) {
            for (String foundString : pullNumberLinks(text)) {
                text = methodReplace(foundString, text);
            }
        }

        return text;
    }

    private String methodReplace(String foundString, String text) {
        // Добавление пробела в конце ссылки
        try{
            int numEnd = text.indexOf(foundString) + foundString.length();
            String findWord = text.substring(numEnd, numEnd + 1);
            boolean retVal = findWord.endsWith(" ");
            if (!retVal) {
                text = text.replace(foundString, foundString + " ");
            }
        } catch (Exception e) {
            text = text.replace(foundString, foundString + " ");
            //e.printStackTrace();
        }
        return text;
    }

    private ArrayList<String> pullWebLinks(String text) {
        ArrayList<String> links = new ArrayList<>();

        String regex = "\\(?\\b(http://|https://|www|Www|wWw|WWW|wwW[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()) {
            String webStr = matcher.group();
            if (webStr.startsWith("(") && webStr.endsWith(")")) {
                webStr = webStr.substring(1, webStr.length() - 1);
            }
            links.add(webStr);
        }

        return links;
    }

    private ArrayList<String> pullMailLinks(String text) {
        ArrayList<String> links = new ArrayList<>();

        String regex = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()) {
            String mailStr = matcher.group();
            links.add(mailStr);
        }

        return links;
    }

    private  ArrayList<String> pullNumberLinks(String text) {
        ArrayList<String> links = new ArrayList<>();

        // Только для российских номеров
        String regex = "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?(\\s?\\d\\s?){3}[\\s\\-]?(\\s?\\d\\s?){2}[\\s\\-]?(\\s?\\d\\s?){2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()) {
            String phoneStr = matcher.group();
            links.add(phoneStr);
        }

        return links;
    }
}
