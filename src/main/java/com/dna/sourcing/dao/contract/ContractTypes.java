package com.dna.sourcing.dao.contract;

public enum ContractTypes {

    //
    INDEX(0, "INDEX"),
    TEXT(1, "TEXT"),
    PDF(2, "PDF"),
    IMAGE(3, "IMAGE"),
    VIDEO(4, "VIDEO");

    //
    private final int id;
    private final String message;

    ContractTypes(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public static boolean contains(String test) {

        for (ContractTypes c : ContractTypes.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
}