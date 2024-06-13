package org.example.store_everything.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute("jakarta.servlet.error.status_code");
        Object errorMessage = request.getAttribute("jakarta.servlet.error.message");
        Object requestUri = request.getAttribute("jakarta.servlet.error.request_uri");

        model.addAttribute("status", statusCode != null ? " " + statusCode : "");
        model.addAttribute("path", requestUri != null ? " (" + requestUri + ")" : "");
        model.addAttribute("error",
                (errorMessage != null && errorMessage != "") ? errorMessage.toString() : "nieznany");

        return "error";
    }
}
