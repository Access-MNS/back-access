package com.alert.alert.controller.channel;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.Views;
import com.alert.alert.entities.enums.PermissionType;
import com.alert.alert.payload.request.ChannelRequest;
import com.alert.alert.service.impl.ChannelServiceImpl;
import com.alert.alert.validation.IsUser;
import com.alert.alert.validation.PermissionCheck;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
@IsUser
public class ChannelController {

    private final ChannelServiceImpl channelService;

    public ChannelController(ChannelServiceImpl channelService) {
        this.channelService = channelService;
    }

    @GetMapping("/channel/{id}")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> getChannel(@PathVariable Long id) {

        Channel channel = channelService.getChannel(id);

        return returnChannel(channel);
    }

    @GetMapping("/channels")
    @JsonView(Views.Public.class)
    Collection<Channel> getChannels() {
        return channelService.getChannels();
    }


    @PostMapping("/channels")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> createChannel(@Validated @RequestBody ChannelRequest channelRequest) {
        return returnChannel(channelService.createChannel(channelRequest.toChannel()));
    }

    @PutMapping("/channels")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> updateChannel(@Validated @RequestBody ChannelRequest channelRequest) {
        return returnChannel(channelService.updateChannel(channelRequest.toChannel()));
    }

    @DeleteMapping("/channels/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<String> deleteChannel(@PermissionCheck(PermissionType.DELETE) @PathVariable Long id) {

        return channelService.deleteChannel(id)
                ? ResponseEntity.ok("Channel deleted")
                : ResponseEntity.notFound().build();
    }

    private ResponseEntity<Channel> returnChannel(Channel channel) {

        return channel!= null
                ? ResponseEntity.ok(channel)
                : ResponseEntity.notFound().build();
    }
}
