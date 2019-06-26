package com.dna.sourcing.util.exp;

public enum ErrorCode {

    //
    SUCCESSS(0, "SUCCESS"),
    //
    PARAMS(61001, "INVALID_PARAMS"),
    //
    ONTID_NOT_EXIST(71001, "ONTID_NOT_EXIST"),
    ONTID_EXIST(71002, "ONTID_EXIST"),
    ONTID_PubKey_EXIST(71003, "ONTID_PubKey_EXIST"),
    //
    BLOCKCHAIN(81001, "BLOCKCHAIN_ERROR"),
    FILEHASH_NOT_EXIST(81002, "FILEHASH_NOT_EXIST"),
    TXHASH_NOT_EXIST(81003, "TXHASH_NOT_EXIST"),
    //
    SFL_ERROR(90001, "司法链接口调用失败"),
    //
    INTERNAL_SERVER_ERROR(100000, "");

    //
    private final int id;
    private final String message;

    ErrorCode(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}