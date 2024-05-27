package com.adman.craft.comments_mgmt.errorhandling.exception;

import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidCredentialException extends RuntimeException {

    private ErrorCode errorCode;

    public InvalidCredentialException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
