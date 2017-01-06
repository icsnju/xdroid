package com.nata.xdroid.injector;

public interface InputType {
    public static final int TYPE_MASK_CLASS = 0x0000000f;//15
    public static final int TYPE_MASK_VARIATION = 0x00000ff0;//4080
    public static final int TYPE_MASK_FLAGS = 0x00fff000;//16773120
    public static final int TYPE_NULL = 0x00000000;//0
    public static final int TYPE_CLASS_TEXT = 0x00000001;//1
    public static final int TYPE_TEXT_FLAG_CAP_CHARACTERS = 0x00001000;//4096
    public static final int TYPE_TEXT_FLAG_CAP_WORDS = 0x00002000;//8192
    public static final int TYPE_TEXT_FLAG_CAP_SENTENCES = 0x00004000;//16384
    public static final int TYPE_TEXT_FLAG_AUTO_CORRECT = 0x00008000;//32768
    public static final int TYPE_TEXT_FLAG_AUTO_COMPLETE = 0x00010000;//65536
    public static final int TYPE_TEXT_FLAG_MULTI_LINE = 0x00020000;//131072
    public static final int TYPE_TEXT_FLAG_IME_MULTI_LINE = 0x00040000;//262144
    public static final int TYPE_TEXT_FLAG_NO_SUGGESTIONS = 0x00080000;//524288
    public static final int TYPE_TEXT_VARIATION_NORMAL = 0x00000000;//0
    public static final int TYPE_TEXT_VARIATION_URI = 0x00000010;//16
    public static final int TYPE_TEXT_VARIATION_EMAIL_ADDRESS = 0x00000020;//32
    public static final int TYPE_TEXT_VARIATION_EMAIL_SUBJECT = 0x00000030;//48
    public static final int TYPE_TEXT_VARIATION_SHORT_MESSAGE = 0x00000040;//64
    public static final int TYPE_TEXT_VARIATION_LONG_MESSAGE = 0x00000050;//80
    public static final int TYPE_TEXT_VARIATION_PERSON_NAME = 0x00000060;//96
    public static final int TYPE_TEXT_VARIATION_POSTAL_ADDRESS = 0x00000070;//112
    public static final int TYPE_TEXT_VARIATION_PASSWORD = 0x00000080;//128
    public static final int TYPE_TEXT_VARIATION_VISIBLE_PASSWORD = 0x00000090;//144
    public static final int TYPE_TEXT_VARIATION_WEB_EDIT_TEXT = 0x000000a0;//160
    public static final int TYPE_TEXT_VARIATION_FILTER = 0x000000b0;//176
    public static final int TYPE_TEXT_VARIATION_PHONETIC = 0x000000c0;//192
    public static final int TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS = 0x000000d0;
    public static final int TYPE_TEXT_VARIATION_WEB_PASSWORD = 0x000000e0;
    public static final int TYPE_CLASS_NUMBER = 0x00000002;//2
    public static final int TYPE_NUMBER_FLAG_SIGNED = 0x00001000;//4096
    public static final int TYPE_NUMBER_FLAG_DECIMAL = 0x00002000;//8192
    public static final int TYPE_NUMBER_VARIATION_NORMAL = 0x00000000;
    public static final int TYPE_NUMBER_VARIATION_PASSWORD = 0x00000010;
    public static final int TYPE_CLASS_PHONE = 0x00000003;//3
    public static final int TYPE_CLASS_DATETIME = 0x00000004;//4
    public static final int TYPE_DATETIME_VARIATION_NORMAL = 0x00000000;//0
    public static final int TYPE_DATETIME_VARIATION_DATE = 0x00000010;//16
    public static final int TYPE_DATETIME_VARIATION_TIME = 0x00000020;//32
}
