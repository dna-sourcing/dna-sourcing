package com.dna.sourcing.util._crypt;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by ZhouQ on 2017/8/28.
 */
@ApiModel(value = "响应结果")
@Data
public class Result {
    @ApiModelProperty(value = "行为")
    public String Action;
    @ApiModelProperty(value = "响应码")
    public int Error;
    @ApiModelProperty(value = "描述，成功为SUCCESS，失败为对应的描述")
    public String Desc;
    @ApiModelProperty(value = "请求结果")
    public Object Result;
    @ApiModelProperty(value = "版本")
    public String Version;

    public Result() {
    }

    public Result(String action, int error, String desc, Object result) {
        Action = action;
        Error = error;
        Desc = desc;
        Result = result;
        Version = "v1";
    }

    private Result(String action, int error, String desc, Object result, String version) {
        Action = action;
        Error = error;
        Desc = desc;
        Result = result;
        Version = version;
    }

    public String getAction() {
        return Action;
    }

    public int getError() {
        return Error;
    }

    public String getDesc() {
        return Desc;
    }

    public Object getResult() {
        return Result;
    }

    public String getVersion() {
        return Version;
    }
}
