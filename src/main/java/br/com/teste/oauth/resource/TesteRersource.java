package br.com.teste.oauth.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteRersource {

    @RequestMapping("/test")
    public void teste() {
        System.out.println("Ok");

    }

}
