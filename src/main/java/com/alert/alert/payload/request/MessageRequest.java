package com.alert.alert.payload.request;

import com.alert.alert.entities.Message;
import com.alert.alert.entities.enums.Action;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequest {

    private UserRequest sender;

    @NotBlank(message = "Message can't be empty")
    private String comment;

    @Enumerated(EnumType.STRING)
    private Action action;

    public Message toMessage() {
        return new Message()
                .setSender(sender.toUser())
                .setComment(comment)
                .setAction(action);
    }
}
