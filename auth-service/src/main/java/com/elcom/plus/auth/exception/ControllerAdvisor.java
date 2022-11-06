package com.elcom.plus.auth.exception;

import com.elcom.plus.common.util.constant.ResponseMessageAPI;
import com.elcom.plus.common.util.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(ControllerAdvisor.class);

    /**
     * Tất cả các Exception không được khai báo sẽ được xử lý tại đây
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Response handleAllException(Exception ex, WebRequest request) {
        ResponseMessageAPI responseMessageAPI = new ResponseMessageAPI();
        logger.info("[]========== {} ==========: {}", request, ex.getLocalizedMessage());
        return new Response(999, responseMessageAPI.getMessage("ERROR_CODE_400"));
    }

    /**
     * Truyền sai hoặc thiếu key value sẽ được xử lý riêng tại đây
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Response ParameterException(MissingServletRequestParameterException mx, WebRequest request) {
        ResponseMessageAPI responseMessageAPI = new ResponseMessageAPI();
        logger.info("[]========== {} ==========: {}", request, mx.getMessage());
        return new Response(400, responseMessageAPI.getMessage("ERROR_CODE_400"));
    }

    /**
     * Goi sai phương thức như get thì lại gọi post
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Response MethodException(HttpRequestMethodNotSupportedException hx, WebRequest request) {
        logger.info("[]========== {} ==========: {}", request, hx.getMessage());
        ResponseMessageAPI responseMessageAPI = new ResponseMessageAPI();
        return new Response(405, responseMessageAPI.getMessage("ERROR_CODE_400"));
    }
}
