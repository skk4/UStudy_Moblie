package org.yoya.com.yoyaorg.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WX on 16/1/20.
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return (str == null || str.trim().equals(""));
    }

    /**
     * 判断给定的文本是否是数字
     *
     * @param numberString
     * @return
     */
    public static boolean isNumber(String numberString) {
        return numberString.matches("^[0-9]*$");
    }

    /**
     * 将单精度浮点数字形式的字符串转换成float类型数据
     *
     * @param floatString
     * @return
     */
    public static float getFloat(String floatString) {
        float number = 0;
        try {
            number = Float.parseFloat(floatString);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return number;
    }

    /**
     * 将整形形式的字符串转换成int类型数据
     *
     * @param intString
     * @return
     */
    public static int getInt(String intString) {
        int number = 0;
        try {
            number = Integer.parseInt(intString.trim());
        } catch (NumberFormatException e) {
            // TODO: handle exception
        }
        return number;
    }

    /**
     * 将双精度浮点数字形式的字符串转换成float类型数据
     *
     * @param doubleString
     * @return
     */
    public static Double getDouble(String doubleString) {
        Double number = 0d;
        try {
            number = Double.parseDouble(doubleString);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return number;
    }

    /**
     * 判断是否为邮箱
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        // 邮箱验证规则
        String regEx = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        return rs;
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
//        String regEx = "^[1][3,4,5,8][0-9]{9}$";// 验证手机号
        String regEx = "^[1][0-9]{10}$";// 验证手机号
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        boolean b = m.matches();
        return b;
    }

    /**
     * 特殊符号
     *
     * @param str
     * @return
     */
    public static boolean regEx(String str) {
//        String regEx =  "(\\?|\\|\\&|\\‘|\\’|\'|\"|\\“|\\”|<|>|\\$)";
        String regEx = "(\\\'|\\\"|\\&|\\x24|\\?|\\>|\\<|\\/|\\\\|\\$|\\|)";
        Pattern p = Pattern.compile(regEx);
        boolean b = false;
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            Matcher m = p.matcher(temp);
            b = m.matches();
            if (b)
                break;
        }
        return b;
    }

    /**
     * 过滤特殊符号
     *
     * @param qString
     * @return
     */
    public static boolean hasCrossScriptRisk(String qString) {
        String regx = "!|！|@|◎|#|＃|(\\$)|￥|%|％|(\\^)|……|(\\&)|※|(\\*)|×|(\\()|（|(\\))|）|_|——|(\\+)|＋|(\\|)|§ ";
        if (qString != null) {
            qString = qString.trim();
            Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(qString);
            return m.find();
        }
        return false;
    }

    public static boolean isName(String qString) {
        String regx = "(([\u4E00-\u9FA5]{2,7})|([a-zA-Z]{3,20}))";
        if (qString != null) {
            qString = qString.trim();
            Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(qString);
            return m.matches();
        }
        return false;
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 计算长度
     *
     * @param value
     * @return
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 路径判断
     *
     * @param path
     * @return
     */
    public static String strPath(String path) {
        if (!StringUtils.isEmpty(path)) {
            if (!path.substring(0, 1).equals("/"))
                path = "/" + path;
            String[] str = path.split("/");
            if (!str[1].equals("asset"))
                path = "/asset/sc" + path;
        }

        return path;
    }

    /**
     * 高亮显示
     */
    public static void highLight(TextView tv, String wholeText, String keyWord) {
        SpannableString sp = new SpannableString(wholeText);
        int start, end;
        start = wholeText.indexOf(keyWord);

        if (start == -1) {
            tv.setText(wholeText);
            return;
        }
        end = start + keyWord.length();
        sp.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(sp);
    }

    public static boolean contain(String str, String reg) {
        return str.contains(reg);
//        return str.matches(reg);
    }

    /**
     * 根据播放路径截取share_id
     * @param playUrl 播放路径
     * @return
     */
    public static String subStringShareIdByPlayUrl(String playUrl){
        if(null==playUrl||"".equals(playUrl)){
            return null;
        }

        int start = playUrl.lastIndexOf("-")+1;
        int end = playUrl.lastIndexOf(".");

        if(end<=start){
            return null;
        }

        String result = playUrl.substring(start,end);
        return result;
    }
}

