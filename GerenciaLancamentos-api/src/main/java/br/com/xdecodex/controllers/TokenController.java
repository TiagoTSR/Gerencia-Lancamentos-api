package br.com.xdecodex.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @PostMapping("/tokens/revoke")
    public void revoke(HttpServletRequest request) throws ServletException {
        request.logout(); // invalida sessão e segurança
    }
}