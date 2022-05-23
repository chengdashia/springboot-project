package com.cloudDisk.utils.date;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author 成大事
 * @since 2022/4/10 8:57
 *  @ControllerAdvice+@InitBinder解决date入参问题
 * 注意这个只能解决@RequestAttribute，或使用@RequestParam的date参数问题，不适用于@RequestBody格式的参数接收
 */
@ControllerAdvice
public class ControllerHandler {
    private Logger logger = LoggerFactory.getLogger(ControllerHandler.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 方法1，注册converter
        GenericConversionService genericConversionService = (GenericConversionService) binder.getConversionService();
        if (genericConversionService != null) {
            genericConversionService.addConverter(new DateConverter());
        }

        // 方法2，定义单格式的日期转换，可以通过替换格式，定义多个dateEditor，代码不够简洁
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CustomDateEditor dateEditor = new CustomDateEditor(df, true);
        binder.registerCustomEditor(Date.class, dateEditor);


        // 方法3，同样注册converter
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(new DateConverter().convert(text));
            }
        });

    }
}
