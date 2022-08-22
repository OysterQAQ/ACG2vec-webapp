package dev.cheerfun.deepix.exception.handler;

import dev.cheerfun.deepix.domain.Result;
import dev.cheerfun.deepix.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import javax.servlet.http.HttpServletRequest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author OysterQAQ
 * @version 2.0
 * @date 2019-08-16 14:17
 * @description 全局异常捕获
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<Result> handleBaseException(BaseException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new Result(e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result("输入参数中存在错误", e.getMessage()));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Result> handleIllegalArgumentException(IllegalArgumentException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result("参数错误"));
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public ResponseEntity<Result> handleNumberFormatException(NumberFormatException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result("参数非法"));
    }

    @ExceptionHandler(value = ExecutionException.class)
    public ResponseEntity<Result> handleExecutionException(ExecutionException e) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(new Result("请求超时"));
    }

    @ExceptionHandler(value = AsyncRequestTimeoutException.class)
    public ResponseEntity<Result> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException e, HttpServletRequest request) {
        System.out.println(request.getRequestURI());
        System.out.println("请求超时");
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(new Result("请求超时"));
    }

    @ExceptionHandler(value = TimeoutException.class)
    public ResponseEntity<Result> handleTimeoutException(TimeoutException e, HttpServletRequest request) {
        System.out.println(request.getRequestURI());
        System.out.println("请求超时");
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(new Result("请求超时"));
    }

    @ExceptionHandler(value = FileUploadException.class)
    public ResponseEntity<Result> handleFileUploadException(FileUploadException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result("上传出现错误"));
    }

    /*@ExceptionHandler(value = MessagingException.class)
    public ResponseEntity<Result> handleIllegalMessagingException(MessagingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result("登录信息异常"));
    }*/
    /*    @ExceptionHandler(Exception.class)//可以用来找异常类
        public ResponseEntity handleException(Exception ae) {
            System.out.println(ae);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result<>("登录异常"));
            // return null;
        }*/

}
