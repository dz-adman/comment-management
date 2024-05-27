package com.adman.craft.comments_mgmt.errorhandling.exception;

import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class ExternalRuntimeException extends RuntimeException {

    private ErrorCode errorCode;
    private List<String> messages;

    public ExternalRuntimeException(@Nonnull ErrorCode errorCode, List<String> messages) {
        this.errorCode = errorCode;
        this.messages = messages;
    }
}

