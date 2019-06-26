package com.dna.sourcing.controller.util;

import com.alibaba.fastjson.JSON;
import com.dna.sourcing.util.GlobalVariable;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("")
@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @GetMapping(ERROR_PATH)
    @ResponseBody
    public String error(HttpServletRequest request) {

        System.out.println("CustomErrorController start ... " + request.getRequestURI());

        Map<String, Object> map = new HashMap<String, Object>();

        // Enumeration<String> attributes = request.getAttributeNames();
        // while (attributes.hasMoreElements()) {
        //     String name = attributes.nextElement();
        //     if (name.startsWith("java")) {
        //         // spring本身的属性不宜对外暴露，切记！
        //         Object value = request.getAttribute(name);
        //         map.put(name, value);
        //     }
        // }

        map.put("API_VERSION", GlobalVariable.API_VERSION);

        return JSON.toJSONString(map);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}