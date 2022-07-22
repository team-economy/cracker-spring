package com.cracker.exception.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.NamedStoredProcedureQueries;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

public class ExceptionHandlingController implements ErrorController {
    private final String ERROR_400_PAGE_PATH = "/error/400";
    private final String ERROR_404_PAGE_PATH = "/error/404";
    private final String ERROR_500_PAGE_PATH = "/error/500";
    private final String ERROR_ETC_PAGE_PATH = "/error/error_page";

    @RequestMapping(value = "/error")
    public String handleError(HttpServletRequest request) {

        // 에러 코드를 획득한다.
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // 에러 코드에 대한 상태 정보
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(status.toString()));

        if (status != null) {
            // HttpStatus와 비교해 페이지 분기를 나누기 위한 변수
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return ERROR_400_PAGE_PATH;
            }

            // 404 error
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // 에러 페이지에 표시할 정보
                return ERROR_404_PAGE_PATH;
            }

            // 500 error
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // 서버에 대한 에러이기 때문에 사용자에게 정보를 제공하지 않는다.
                return ERROR_500_PAGE_PATH;
            }
        }

        // 정의한 에러 외 모든 에러는 error/error 페이지로 보낸다.
        return ERROR_ETC_PAGE_PATH;
    }

}
