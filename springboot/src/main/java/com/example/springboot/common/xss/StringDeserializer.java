package com.example.springboot.common.xss;//package com.nongYingC.config.xss;
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import org.apache.commons.lang3.StringUtils;
//
//import java.io.IOException;
//
///**
// * @author 成大事
// * @since 2022/5/24 10:29
// */
//public class StringDeserializer extends JsonDeserializer<String> {
//    @Override
//    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
//        String str = jsonParser.getText().trim();
//        //sql注入拦截
//        if (sqlInject(str)) {
//            throw new CustomerException("参数含有非法攻击字符，已禁止继续访问！");
//        }
//
//        return xssClean(str);
//
//    }
//
//    public boolean sqlInject(String str) {
//
//        if (StringUtils.isEmpty(str)) {
//            return false;
//        }
//
//        //去掉'|"|;|\字符
//        str = org.apache.commons.lang3.StringUtils.replace(str, "'", "");
//        str = org.apache.commons.lang3.StringUtils.replace(str, "\"", "");
//        str = org.apache.commons.lang3.StringUtils.replace(str, ";", "");
//        str = org.apache.commons.lang3.StringUtils.replace(str, "\\", "");
//
//        //转换成小写
//        str = str.toLowerCase();
//
//        //非法字符
//        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert","alter", "drop"};
//
//        //判断是否包含非法字符
//        for (String keyword : keywords) {
//            if (str.indexOf(keyword) != -1) {
//                return true;
//            }
//        }
//        return false;
//
//    }
//
//    //xss攻击拦截
//
//    public String xssClean(String value) {
//        if (value == null || "".equals(value)) {
//            return value;
//        }
//
//        //非法字符
//        String[] keywords = {"<", ">", "<>", "()", ")", "(", "javascript:", "script","alter", "''","'"};
//        //判断是否包含非法字符
//        for (String keyword : keywords) {
//            if (value.indexOf(keyword) != -1) {
//                throw new CustomerException("参数含有非法攻击字符，已禁止继续访问！");
//            }
//        }
//
//        return value;
//    }
//
//}
