package com.cloudDisk.utils.validate;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.cloudDisk.utils.globalResult.R;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 全局异常处理类
 * @author 成大事
 * @date 2022/3/15 13:28
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @param e 异常
     * @return 处理 json 请求体调用接口对象参数校验失败抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R jsonParamsException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorList = CollectionUtil.newArrayList();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String msg = String.format("%s%s；", fieldError.getField(), fieldError.getDefaultMessage());
            errorList.add(msg);
        }
        return R.error(errorList);
    }


    /**
     * @param e 异常
     * @return 处理单个参数校验失败抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R ParamsException(ConstraintViolationException e) {

        List<String> errorList = CollectionUtil.newArrayList();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            StringBuilder message = new StringBuilder();
            Path path = violation.getPropertyPath();
            String[] pathArr = StrUtil.splitToArray(path.toString(), ".");
            String msg = message.append(pathArr[1]).append(violation.getMessage()).toString();
            errorList.add(msg);
        }
        return R.error(errorList);
    }

    /**
     * @param e 异常
     * @return  处理 form data方式调用接口对象参数校验失败抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public R formDaraParamsException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(o -> o.getField() + o.getDefaultMessage())
                .collect(Collectors.toList());
        return R.error(collect);
    }

    /**
     * @param e 异常
     * @return  请求方法不被允许异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return R.error(e.getMessage());
    }

    /**
     * @param e 异常
     * @return Content-Type/Accept 异常
     * application/json
     * application/x-www-form-urlencoded
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public R httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return R.error(e.getMessage());
    }

    /**
     * handlerMapping  接口不存在跑出异常
     * @param e  异常
     * @return R
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public R noHandlerFoundException(NoHandlerFoundException e) {
        return R.error(e.getMessage());
    }



    /**
     *
     * @param e 未知异常捕获
     * @return  R
     */
    @ExceptionHandler(Exception.class)
    public R UnNoException(Exception e) {
        return R.error(e.getMessage());
    }


}
