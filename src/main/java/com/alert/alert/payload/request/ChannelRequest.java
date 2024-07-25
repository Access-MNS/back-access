package com.alert.alert.payload.request;

import com.alert.alert.entity.Channel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelRequest {

    @NotBlank(message = "Name can't be empty")
    private String name;
    private String description;
    private Channel parentChannelId;

    public Channel toChannel() {
        return new Channel()
                .setName(name)
                .setDescription(description)
                .setParentChannelId(parentChannelId);
    }
}
