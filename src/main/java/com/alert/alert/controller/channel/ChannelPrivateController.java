package com.alert.alert.controller.channel;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.Views;
import com.alert.alert.payload.request.ChannelRequest;
import com.alert.alert.service.ChannelPrivateService;
import com.alert.alert.service.impl.ChannelPrivateServiceImpl;
import com.alert.alert.validation.IsUser;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/channel")
@IsUser
public class ChannelPrivateController {

    private final ChannelPrivateService channelPrivateService;

    @Autowired
    public ChannelPrivateController(ChannelPrivateServiceImpl channelPrivateService) {
        this.channelPrivateService = channelPrivateService;
    }


    @PostMapping("private/{id}")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> createPrivateChannel(@Validated
                                                 @RequestBody ChannelRequest channelRequest,
                                                 @PathVariable Long id) {

        Channel channel = channelPrivateService.createPrivateChannel(channelRequest.toChannel(), id);
        if (channel != null) {
            return ResponseEntity.ok(channel);
        }
        return ResponseEntity.notFound().build();
    }
}
