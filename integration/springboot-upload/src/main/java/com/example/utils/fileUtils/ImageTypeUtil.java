package com.example.utils.fileUtils;

import java.util.HashMap;
import java.util.Map;

public class ImageTypeUtil {

    public final static Map<String,String> FILE_TYPE_MAP = new HashMap<>();

    static{
        FILE_TYPE_MAP.put("image/bmp","bmp" );
        FILE_TYPE_MAP.put("image/png","png" );
        FILE_TYPE_MAP.put("image/cis-cod","cod" );
        FILE_TYPE_MAP.put("image/gif","gif" );
        FILE_TYPE_MAP.put("image/ief","ief" );
        FILE_TYPE_MAP.put("image/jpeg","jpg" );
        FILE_TYPE_MAP.put("image/pipeg","jfif" );
        FILE_TYPE_MAP.put("image/svg+xml","svg" );
        FILE_TYPE_MAP.put("image/tiff","tiff" );
        FILE_TYPE_MAP.put("image/x-cmu-raster","ras" );
        FILE_TYPE_MAP.put("image/x-cmx","cmx" );
        FILE_TYPE_MAP.put("image/x-icon","ico" );
        FILE_TYPE_MAP.put("image/x-portable-anymap","pnm" );
        FILE_TYPE_MAP.put("image/x-portable-bitmap","pbm" );
        FILE_TYPE_MAP.put("image/x-portable-graymap","pgm" );
        FILE_TYPE_MAP.put("image/x-portable-pixmap","ppm" );
        FILE_TYPE_MAP.put("image/x-rgb","rgb" );
        FILE_TYPE_MAP.put("image/x-xbitmap","xbm" );
        FILE_TYPE_MAP.put("image/x-xpixmap","xpm" );
        FILE_TYPE_MAP.put("image/x-xwindowdump","xwd" );
    }
}
