package com.onlineExam.utils.file;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 成大事
 * @date 2021-12-20
 * 通过mine/type 获得文件后缀
 */
public class FileTypeUtil {

    public final static Map<String,String> FILE_TYPE_MAP = new HashMap<>();

    static{
        //一些常见文件后缀
        FILE_TYPE_MAP.put("application/msword",".doc" );
        FILE_TYPE_MAP.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document",".docx" );
        FILE_TYPE_MAP.put("application/pdf",".pdf" );
        FILE_TYPE_MAP.put("text/csv",".csv" );
        FILE_TYPE_MAP.put("application/rtf",".rtf" );
        FILE_TYPE_MAP.put("application/vnd.ms-excel",".xls" );
        FILE_TYPE_MAP.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",".xlsx" );
        FILE_TYPE_MAP.put("application/vnd.ms-powerpoint",".ppt" );
        FILE_TYPE_MAP.put("application/x-rar-compressed",".rar" );
        FILE_TYPE_MAP.put("application/zip",".zip" );
        FILE_TYPE_MAP.put("text/plain",".txt" );
        FILE_TYPE_MAP.put(" text/x-sql",".sql" );

        //一些常见图片后缀
        FILE_TYPE_MAP.put("image/bmp",".bmp" );
        FILE_TYPE_MAP.put("image/png",".png" );
        FILE_TYPE_MAP.put("image/cis-cod",".cod" );
        FILE_TYPE_MAP.put("image/gif",".gif" );
        FILE_TYPE_MAP.put("image/ief",".ief" );
        FILE_TYPE_MAP.put("image/jpeg",".jpg" );
        FILE_TYPE_MAP.put("image/pipeg",".jfif" );
        FILE_TYPE_MAP.put("image/svg+xml",".svg" );
        FILE_TYPE_MAP.put("image/tiff",".tiff" );
        FILE_TYPE_MAP.put("image/x-cmu-raster",".ras" );
        FILE_TYPE_MAP.put("image/x-cmx",".cmx" );
        FILE_TYPE_MAP.put("image/x-icon",".ico" );
        FILE_TYPE_MAP.put("image/x-portable-anymap",".pnm" );
        FILE_TYPE_MAP.put("image/x-portable-bitmap",".pbm" );
        FILE_TYPE_MAP.put("image/x-portable-graymap",".pgm" );
        FILE_TYPE_MAP.put("image/x-portable-pixmap",".ppm" );
        FILE_TYPE_MAP.put("image/x-rgb",".rgb" );
        FILE_TYPE_MAP.put("image/x-xbitmap",".xbm" );
        FILE_TYPE_MAP.put("image/x-xpixmap",".xpm" );
        FILE_TYPE_MAP.put("image/x-xwindowdump",".xwd" );
    }
}
