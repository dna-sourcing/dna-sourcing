package com.dna.sourcing.controller;

import ch.qos.logback.classic.Logger;
import com.google.gson.Gson;
import com.dna.sourcing.model.util.ExceptionMsg;
import com.dna.sourcing.model.util.Result;
import com.dna.sourcing.service.WalletService;
import com.dna.sourcing.util.exp.ErrorCode;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    //
    private Logger logger = (Logger) LoggerFactory.getLogger(WalletController.class);

    //
    private Gson gson = new Gson();

    //
    private WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        //
        this.walletService = walletService;
    }

    //
    @PostConstruct
    public void postConstructor() {
        //
        logger.info("WalletController PostConstruct start ...");
    }

    @GetMapping("/test")
    public ResponseEntity<Result> test() {

        //
        Result rst = new Result();
        rst.setAction("test");
        rst.setVersion("1.0.0");
        rst.setError(0);
        rst.setDesc("hello world.");
        rst.setResult("");
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Result> createWallet(@RequestBody LinkedHashMap<String, Object> obj) {

        //
        Result rst = new Result();
        rst.setAction("createWallet");
        rst.setVersion("1.0.0");
        rst.setResult("");

        //
        String pwd = (String) obj.get("password");
        if (StringUtils.isEmpty(pwd)) {
            rst.setError(ErrorCode.PARAMS.getId());
            rst.setDesc(ErrorCode.PARAMS.getMessage());
        } else {
            String addr;
            try {
                addr = walletService.createWallet(pwd);
                rst.setError(ErrorCode.SUCCESSS.getId());
                rst.setDesc(ErrorCode.SUCCESSS.getMessage());
                rst.setResult(addr);
            } catch (Exception e) {
                logger.error(e.getMessage());
                ExceptionMsg msg = gson.fromJson(e.getMessage(), ExceptionMsg.class);
                rst.setError(msg.getError());
                rst.setDesc(msg.getDesc());
            }
        }

        //
        return new ResponseEntity<>(rst, HttpStatus.OK);
    }

}
