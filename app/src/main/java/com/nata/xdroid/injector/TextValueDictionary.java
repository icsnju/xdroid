package com.nata.xdroid.injector;

import java.util.*;

public class TextValueDictionary {
    public static final String[] CREDIT_CARD_invalid;
    public static final String[] CREDIT_CARD_valid;
    public static final String[] EMAIL_invalid;
    public static final String[] EMAIL_valid;
    public static final String[] ISBN_invalid;
    public static final String[] ISBN_valid;
    public static ArrayList<String> MIX_invalid;
    public static ArrayList<String> MIX_valid;
    public static final String[] NUMBER_DECIMAL_invalid;
    public static final String[] NUMBER_DECIMAL_valid;
    public static final String[] NUMBER_SIGNED_DECIMAL_invalid;
    public static final String[] NUMBER_SIGNED_DECIMAL_valid;
    public static final String[] NUMBER_SIGNED_invalid;
    public static final String[] NUMBER_SIGNED_valid;
    public static final String[] NUMBER_invalid;
    public static final String[] NUMBER_valid;
    public static final String[] STRING_invalid;
    public static final String[] STRING_valid;
    public static final String[] URL_invalid;
    public static final String[] URL_valid;
    public static final String[] ZIP_invalid;
    public static final String[] ZIP_valid;
    public static final String[] PHONE_valid;
    public static final String[] PHONE_invalid;
    public static Random random = new Random();

    private static TextValueDictionary dictionary = null;

    private TextValueDictionary() {
    }

    public static TextValueDictionary getInstance() {
        if (dictionary == null) {
            dictionary = new TextValueDictionary();
        }
        return dictionary;
    }

    static {
        EMAIL_valid = new String[]{"email@domain.com", "firstname.lastname@domain.com", "email@subdomain.domain.com", "firstname+lastname@domain.com", "email@123.123.123.123", "email@[123.123.123.123]", "\"email\"@domain.com", "1234567890@domain.com", "email@domain-one.com", "_______@domain.com", "email@domain.name", "email@domain.co.jp", "firstname-lastname@domain.com"};
        EMAIL_invalid = new String[]{"plainaddress", "#@%^%#$@#$@#.com", "@domain.com", "Joe Smith <email@domain.com>", "email.domain.com", "email@domain@domain.com", ".email@domain.com", "email.@domain.co", "email..email@domain.com", "Ã£ï¿½â€šÃ£ï¿½â€žÃ£ï¿½â€ Ã£ï¿½Ë†Ã£ï¿½Å @domain.com", "email@domain.com (Joe Smith)", "email@domain", "email@-domain.com", "email@domain.web", "email@111.222.333.44444", "email@domain..com", "\nemail@domain.com", "\temail@domain.com", " ", "/", "^"};
        URL_valid = new String[]{"http://www.google.it", "g:h", "http://a/b/c/g", "http://a/b/c/g", "http://a/b/c/g/", "http://a/g", "http://g", "http://a/b/c/d;p?y", "http://a/b/c/g?y", "http://a/b/c/d;p?q#s", "http://a/b/c/g#s", "http://a/b/c/g?y#s", "http://a/b/c/;x", "http://a/b/c/g;x", "http://a/b/c/g;x?y#s", "http://a/b/c/d;p?q", "http://a/b/c/", "http://a/b/c/", "http://a/b/", "http://a/b/", "http://a/b/g", "http://a/", "http://a/", "http://a/g"};
        URL_invalid = new String[]{"http;\\/www;google;it", "ht\ttp:@www.google.com:80/;p?#", "http:////////user:@google.com:99?foo", "http:\\\\\\\\www.google.com\\\\foo", "http://foo:-80/", "http://www.google.it\n", "\thttp://www.google.it", "htto;//pippo,com", " ", "/", "^", "http://www.pippo .com", "http://www..pippo.com", "http:://www.pippo.com", "http://www.pippo.com\\"};
        ISBN_valid = new String[]{"1116928000", "1284700151", "128470016X", "1452472319", "1452472327", "1620244470", "1620244489", "1788016637", "1788016645", "7827814395", "7827814409", "7995586558", "7995586566", "8163358718", "8163358726", "8331130871", "833113088X", "8498903033"};
        ISBN_invalid = new String[]{"816335872U", "78278178278144094409", "32554744", "8163358721", "ASDBS!!DFF", "\n1452472319", "\t1452472319", "0", " ", "/", "^"};
        CREDIT_CARD_valid = new String[]{"5123695007103193", "4116480559370132", "6011976857117225", "344058488426266", "378282246310005", "371449635398431", "378734493671000", "5610591081018250", "30569309025904", "38520000023237", "6011111111111117", "6011000990139424", "3530111333300000", "3566002020360505", "5555555555554444", "5105105105105100", "4012888888881881", "4222222222222", "76009244561", "5019717010103742", "6331101999990016"};
        CREDIT_CARD_invalid = new String[]{"37828AB46310005", "371449621321335398431", "378734493671000", "-30569309025904", "385.20000.023237", "6121111111111117", "A011000990139424", "35000111333300000", "3561001120360505", "5431255555554444", "AAABBB5105105100", "411111!111111111", "\n4116480559370132", "\t4116480559370132", "123456", "12", "0", " ", "/", "^"};
        ZIP_valid = new String[]{"35801", "44101", "82941"};
        ZIP_invalid = new String[]{"0", "3a5801a", "4a4101", "\n82941", "\t82941", "829411234", "abcdef", "!!!!!!", " ", "/", "^"};
        NUMBER_valid = new String[]{"1", "10", "9999", "34", "40", "55", "70", "99", "1000", "2988881", "1234124", "2", "33", "435"};
        NUMBER_invalid = new String[]{"0", "AAA", "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999", "-12", "\n12", "\t12", "!!-", "12.01", "12%", "z<q", " ", "/", "^"};
        NUMBER_SIGNED_valid = new String[]{"-1", "10", "-9999", "-34", "40", "55", "70", "99", "-1000", "2988881", "-1234124", "-2", "-33", "435"};
        NUMBER_SIGNED_invalid = new String[]{"0", "12,3", "ab00", "99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999", "!12", "233176401123040024477515224301703382452989758054156037914702679301887293196935329184208300820842156635598983942674598.921047033915219852965519484067098016447", "10.-9", "\n10.9", "\t10.9", "hello", "09@11+", "*12.01", "AAA", "!!-", "12.01", "12%", "z<q", " ", "/", "^"};
        NUMBER_DECIMAL_valid = new String[]{"12.3", "122323.3331", "0.3331", "11.78", "12.78", "1156.1728", "1314.1278", "4411.7448", "99.9", "90.77"};
        NUMBER_DECIMAL_invalid = new String[]{"0", "12,3", "ab00", "233176401123040024477515224301703382452989758054156037914702679301887293196935329184208300820842156635598983942674598.921047033915219852965519484067098016447", "10.-9", "hello", "09@11+", "-12", "\n3.42", "\t12.3", "AAA", "!!-", "*12.01", "12.01", "12%", "z<q", " ", "/", "^"};
        NUMBER_SIGNED_DECIMAL_valid = new String[]{"-12.3", "122323.3331", "-0.3331", "11.78", "12.78", "1156.1728", "-1314.1278", "4411.7448", "-99.9", "-90.77"};
        NUMBER_SIGNED_DECIMAL_invalid = new String[]{"0", "12,3", "12\n78", "ab00", "233176401123040024477515224301703382452989758054156037914702679301887293196935329184208300820842156635598983942674598.921047033915219852965519484067098016447", "10.-9", "4411 7448", "hello", "09@11+", "\n124", "\t234", "*12.01", "AAA", "!!-", "12.01", "12%", "z<q", " ", "/", "^"};
        STRING_valid = new String[]{"string", "zyxel129", "cercei", "1mxrwiha", "hehui2015", "jamisdakar", "x3cNitoO", "langfield345", "qch123", "19790511", "lsxusu0417", "U7tw3Fr117", "TuMIUc3s", "tindersticks", "porty45qs", "19811983", "362137624", "mybigtoe", "falcons420", "abdd870c", "ganga-giulia", "z885633", "123456pk45", "zxc52520", "qq19890418", "huiming", "sandisk", "3232", "4596", "lilihappy", "angga", "herguless", "fatchiken", "fjqxm0000", "sts1903", "HAIXIA2009", "zou186187"};
        STRING_invalid = new String[]{"ndkgskgjskjsdkfjdflkdsjfdskfjsdkfjsdkfljsdlkfsfkdfhdfjgksgjskgjskgsklsdjkfjsdkfjsdkghsesggjskljlkdljkh", "\n\n\n\t\t\n", "hthw\tfafadf", "peworiw\nfldflsdk", " ", "\n", "\t"};
        PHONE_valid = new String[]{"15996270647", "15996270641", "18251931956", "15996270742"};
        PHONE_invalid = new String[]{"159ds_llsdf", "sdfsl_14213", "1sdf_dsf123", "1utoe2"};
    }

    public String getRandomValidValue() {
        if (MIX_valid == null) {
            MIX_valid = new ArrayList<String>();
            MIX_valid.addAll(Arrays.asList(EMAIL_valid));
            MIX_valid.addAll(Arrays.asList(URL_valid));
            MIX_valid.addAll(Arrays.asList(ISBN_valid));
            MIX_valid.addAll(Arrays.asList(CREDIT_CARD_valid));
            MIX_valid.addAll(Arrays.asList(ZIP_valid));
            MIX_valid.addAll(Arrays.asList(NUMBER_valid));
            MIX_valid.addAll(Arrays.asList(NUMBER_SIGNED_valid));
            MIX_valid.addAll(Arrays.asList(NUMBER_DECIMAL_valid));
            MIX_valid.addAll(Arrays.asList(NUMBER_SIGNED_DECIMAL_valid));
            MIX_valid.addAll(Arrays.asList(STRING_valid));
            MIX_valid.addAll(Arrays.asList(PHONE_valid));
        }
        Collections.shuffle(MIX_valid);
        return MIX_valid.get(0);
    }

    public String getRandomInValidValue() {
        if (MIX_invalid == null) {
            MIX_invalid = new ArrayList<String>();
            MIX_invalid.addAll(Arrays.asList(EMAIL_invalid));
            MIX_invalid.addAll(Arrays.asList(URL_invalid));
            MIX_invalid.addAll(Arrays.asList(ISBN_invalid));
            MIX_invalid.addAll(Arrays.asList(CREDIT_CARD_invalid));
            MIX_invalid.addAll(Arrays.asList(ZIP_invalid));
            MIX_invalid.addAll(Arrays.asList(NUMBER_invalid));
            MIX_invalid.addAll(Arrays.asList(NUMBER_SIGNED_invalid));
            MIX_invalid.addAll(Arrays.asList(NUMBER_DECIMAL_invalid));
            MIX_invalid.addAll(Arrays.asList(NUMBER_SIGNED_DECIMAL_invalid));
            MIX_invalid.addAll(Arrays.asList(STRING_invalid));
            MIX_valid.addAll(Arrays.asList(PHONE_invalid));
        }
        Collections.shuffle(MIX_invalid);
        return MIX_invalid.get(0);
    }

    public String getRandomValidValue(String inputType) {
        if (inputType.equals("email")) {
            return getRandomValue(EMAIL_valid);
        }
        if (inputType.equals("url")) {
            return getRandomValue(URL_valid);
        }
        if (inputType.equals("zip")) {
            return getRandomValue(ZIP_valid);
        }
        if (inputType.equals("isbn")) {
            return getRandomValue(ISBN_valid);
        }
        if (inputType.equals("creditCard")) {
            return getRandomValue(CREDIT_CARD_valid);
        }
        if (inputType.equals("number")) {
            return getRandomValue(NUMBER_valid);
        }
        if (inputType.equals("numberDecimal")) {
            return getRandomValue(NUMBER_valid);
        }
        if (inputType.equals("numberSigned")) {
            return getRandomValue(NUMBER_SIGNED_valid);
        }
        if (inputType.equals("numberSignedDecimal")) {
            return getRandomValue(NUMBER_SIGNED_DECIMAL_valid);
        }
        if (inputType.equals("phone")) {
            return getRandomValue(PHONE_valid);
        }
        return getRandomValue(STRING_valid);
    }

    public String getRandomInValidValue(String inputType) {
        if (inputType.equals("email")) {
            return getRandomValue(EMAIL_invalid);
        }
        if (inputType.equals("url")) {
            return getRandomValue(URL_invalid);
        }
        if (inputType.equals("zip")) {
            return getRandomValue(ZIP_invalid);
        }
        if (inputType.equals("isbn")) {
            return getRandomValue(ISBN_invalid);
        }
        if (inputType.equals("creditCard")) {
            return getRandomValue(CREDIT_CARD_invalid);
        }
        if (inputType.equals("number")) {
            return getRandomValue(NUMBER_invalid);
        }
        if (inputType.equals("numberDecimal")) {
            return getRandomValue(NUMBER_DECIMAL_invalid);
        }
        if (inputType.equals("numberSigned")) {
            return getRandomValue(NUMBER_SIGNED_invalid);
        }
        if (inputType.equals("numberSignedDecimal")) {
            return getRandomValue(NUMBER_SIGNED_DECIMAL_invalid);
        }
        if (inputType.equals("phone")) {
            return getRandomValue(PHONE_invalid);
        }
        return getRandomValue(STRING_invalid);
    }

    public String getRandomValue(String[] paramArrayOfString) {
        int index = random.nextInt(paramArrayOfString.length - 1);
        return paramArrayOfString[index];
    }
}
