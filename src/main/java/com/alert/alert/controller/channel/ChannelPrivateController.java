package com.alert.alert.controller.channel;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.Views;
import com.alert.alert.payload.request.ChannelRequest;
import com.alert.alert.service.impl.ChannelPrivateServiceImpl;
import com.alert.alert.validation.IsUser;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@IsUser
public class ChannelPrivateController {

    private final ChannelPrivateServiceImpl channelPrivateService;

    public ChannelPrivateController(ChannelPrivateServiceImpl channelPrivateService) {
        this.channelPrivateService = channelPrivateService;
    }


    @PostMapping("/channels/private/{id}")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> createPrivateChannel(@Validated @RequestBody ChannelRequest channelRequest,
                                                 @PathVariable Long id) {

        Channel channel = channelPrivateService.createPrivateChannel(channelRequest.toChannel(), id);
        return channel != null
                ? ResponseEntity.ok(channel)
                : ResponseEntity.notFound().build();
    }
}
