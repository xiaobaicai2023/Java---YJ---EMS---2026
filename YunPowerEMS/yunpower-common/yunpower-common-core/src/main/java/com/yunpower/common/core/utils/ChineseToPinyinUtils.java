package com.yunpower.common.core.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @title: 汉字转拼音
 * @Author: Jiajiaglam
 * @date: 2023-11-23 9:57
 * @description:
 */
public class ChineseToPinyinUtils {
    /**
     * 获取字符串拼音的第一个字母
     *
     * @param chinese 中文字符串
     * @return 结果
     */
    public static String ToFirstChar(String chinese) {
        chinese = cleanChar(chinese);

        StringBuilder pinyinStr = new StringBuilder();
        char[] newChar = chinese.toCharArray();  //转为单个字符
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : newChar) {
            if (c > 128) {
                try {
                    pinyinStr.append(PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0].charAt(0));
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinStr.append(c);
            }
        }
        return pinyinStr.toString();
    }

    /**
     * 汉字转为拼音
     *
     * @param chinese 中文字符串
     * @return 结果
     */
    public static String ToPinyin(String chinese) {
        chinese = cleanChar(chinese);

        StringBuilder pinyinStr = new StringBuilder();
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : newChar) {
            if (c > 128) {
                try {
                    pinyinStr.append(PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinStr.append(c);
            }
        }
        return pinyinStr.toString();
    }

    /**
     * 汉字转为拼音 并且首字母大写
     *
     * @param chinese 中文字符串
     * @return 结果
     */
    public static String ToPinyinFirstCharUpper(String chinese) {
        chinese = cleanChar(chinese);

        StringBuilder pinyinStr = new StringBuilder();
        char[] newChar = chinese.toCharArray();  //转为单个字符
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char c : newChar) {
            if (c > 128) {
                try {
                    String py = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat)[0];
                    py = py.substring(0, 1).toUpperCase() + py.substring(1, py.length());
                    pinyinStr.append(py);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinStr.append(c);
            }
        }
        return pinyinStr.toString();
    }

    /**
     * 清理特殊字符以便得到
     *
     * @param chinese 中文字符串
     * @return 结果
     */
    public static String cleanChar(String chinese) {
        //^a-zA-Z_\\u4e00-\\u9fa5（可以去掉数字、英文）
        Pattern pattern = Pattern.compile("[^\\u4e00-\\u9fa5a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(chinese);
        chinese = matcher.replaceAll("").trim();
        return chinese;
    }

    public static void main(String[] args) {
        String str = "活动规则：@ 3个好友转发并4评论-这条微博Ac即可 。⚠️（评论获赞��最多的前三名,为幸运粉丝[污]）截  ";
        System.out.println(cleanChar(str));

        System.out.print(Boolean.parseBoolean("true"));
    }
}
