package com.dna.sourcing.model.util;

import com.github.ontio.network.exception.RestfulException;
import com.google.gson.Gson;
import com.dna.sourcing.util.GlobalVariable;
import com.dna.sourcing.util.exp.ErrorCode;
import com.dna.sourcing.util.exp.DNASourcingException;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class Result {

    //
    public Result() {
        this.setVersion(GlobalVariable.API_VERSION);
        this.setError(0);
        this.setDesc("SUCCESS");
        this.setAction("");
        this.setResult("");
    }

    public Result(String action) {
        this.setVersion(GlobalVariable.API_VERSION);
        this.setError(0);
        this.setDesc("SUCCESS");
        this.setAction(action);
        this.setResult("");
    }

    //
    private String Action;
    private int Error;
    private String Desc;
    private Object Result;
    private String Version;

    //
    public void setErrorAndDesc(RestfulException e) {
        RestfulExceptionMsg msg = new Gson().fromJson(e.getMessage(), RestfulExceptionMsg.class);
        this.setError(msg.getError());
        this.setDesc(msg.getDesc());
        this.setResult(msg.getResult());
    }

    //
    public void setErrorAndDesc(ErrorCode error) {
        this.setError(error.getId());
        this.setDesc(error.getMessage());
    }

    //
    public void setErrorAndDesc(Exception e) {
        this.setError(ErrorCode.INTERNAL_SERVER_ERROR.getId());
        this.setDesc(e.getMessage());
    }

    //
    public void setErrorAndDesc(DNASourcingException e) {
        //
        this.setError(e.getErrorCode().getId());
        //
        if (StringUtils.isEmpty(e.getMessage()))
            this.setDesc(e.getErrorCode().getMessage());
        else
            this.setDesc(e.getMessage());
    }
}
