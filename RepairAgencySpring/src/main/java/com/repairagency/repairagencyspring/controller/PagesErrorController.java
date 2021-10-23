package com.repairagency.repairagencyspring.controller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


@Log4j2
@Controller
public class PagesErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

//        if (status != null) {
//
//            int statusCode = Integer.parseInt(status.toString());
//
//            if(statusCode == HttpStatus.NOT_FOUND.value()) {
//                return "errors/403";
//            }
//            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                return "errors/403";
//            }
//            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
//                return "errors/403";
//            }
//        }
        return "errors/403";
    }


}