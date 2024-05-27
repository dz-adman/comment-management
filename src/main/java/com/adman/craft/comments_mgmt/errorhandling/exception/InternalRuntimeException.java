package com.adman.craft.comments_mgmt.errorhandling.exception;

import com.adman.craft.comments_mgmt.errorhandling.ErrorCode;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * This class is only for internal error handling.
 * Details from this exception won't be exposed to user.
 * Sole purpose of this class is to propagate details of exception till it's logged.
 * User will see UNKNOWN_EXCEPTION.
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class InternalRuntimeException extends RuntimeException {

    private ErrorCode errorCode;
    private List<String> messages;

    public InternalRuntimeException(@Nonnull ErrorCode errorCode, List<String> messages) {
        this.errorCode = errorCode;
        this.messages = messages;
    }
}
