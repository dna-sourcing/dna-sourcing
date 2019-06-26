package com.dna.sourcing.util.exp;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class DNASourcingException extends Exception {

    ErrorCode errorCode;

    public DNASourcingException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
