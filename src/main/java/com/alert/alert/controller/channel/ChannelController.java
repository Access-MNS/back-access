package com.alert.alert.controller.channel;

import com.alert.alert.entity.Channel;
import com.alert.alert.entity.Views;
import com.alert.alert.entity.enums.PermissionType;
import com.alert.alert.payload.request.ChannelRequest;
import com.alert.alert.service.channel.ChannelService;
import com.alert.alert.service.impl.channel.ChannelServiceImpl;
import com.alert.alert.validation.IsUser;
import com.alert.alert.validation.PermissionCheck;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/channel")
@IsUser
public class ChannelController {

    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelServiceImpl channelService) {
        this.channelService = channelService;
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> getChannel(@PathVariable Long id) {
        Channel channel = channelService.getChannel(id);
        return returnChannel(channel);
    }

    @GetMapping
    @JsonView(Views.Public.class)
    Collection<Channel> getChannels() {
        return channelService.getChannels();
    }


    @PostMapping
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> createChannel(@Validated @RequestBody ChannelRequest channelRequest) {
        return returnChannel(channelService.createChannel(channelRequest.toChannel()));
    }

    @PutMapping
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> updateChannel(@Validated @RequestBody ChannelRequest channelRequest) {
        return returnChannel(channelService.updateChannel(channelRequest.toChannel()));
    }

    @DeleteMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<String> deleteChannel(@PermissionCheck(PermissionType.DELETE) @PathVariable Long id) {
        if (channelService.deleteChannel(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<Channel> returnChannel(Channel channel) {
        if (channel != null) {
            return ResponseEntity.ok(channel);
        }
        return ResponseEntity.notFound().build();
    }
}
