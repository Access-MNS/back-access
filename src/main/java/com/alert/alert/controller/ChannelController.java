package com.alert.alert.controller;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.User;
import com.alert.alert.entities.Views;
import com.alert.alert.payload.request.ChannelRequest;
import com.alert.alert.payload.request.UpdateChannelUserRequest;
import com.alert.alert.service.ChannelsUsersService;
import com.alert.alert.service.impl.ChannelServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class ChannelController {

    private final ChannelsUsersService channelsUsersService;
    private final ChannelServiceImpl channelService;

    public ChannelController(ChannelsUsersService channelsUsersService, ChannelServiceImpl channelService) {

        this.channelsUsersService = channelsUsersService;
        this.channelService = channelService;
    }

    @RequestMapping("/channels")
    @JsonView(Views.Public.class)
    Collection<Channel> getChannels() {

        return channelService.getChannels();
    }

    @RequestMapping("/channel/{id}/users")
    @JsonView(Views.Public.class)
    Collection<User> getChannelsUsers(@PathVariable Long id) {

        return channelsUsersService.getUsers(id);
    }

    @RequestMapping("channels/{id}")
    @JsonView(Views.Public.class)
    Collection<Channel> getChannelsForUser(@PathVariable Long id) {

        return channelsUsersService.getChannelsByUserId(id);
    }

    @RequestMapping("/channel/{id}")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> getChannel(@PathVariable Long id) {

        Channel channel = channelService.getChannel(id);

        return returnChannel(channel);
    }

    @PostMapping("/channels")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> createChannel(@Validated
                                          @RequestBody ChannelRequest channelRequest) {

        Channel channel = channelService.createChannel(channelRequest.toChannel());

        return returnChannel(channel);
    }

    @PutMapping("/channels")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> updateChannel(@Validated
                                          @RequestBody ChannelRequest channelRequest) {

        Channel channel = channelService.updateChannel(channelRequest.toChannel());

        return returnChannel(channel);
    }

    @DeleteMapping("/channels/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<String> deleteChannel(@PathVariable Long id) {

        return channelService.deleteChannel(id)
                ? ResponseEntity.ok("Channel deleted")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/channels/{channelId}/{userId}")
    @JsonView(Views.Public.class)
    ResponseEntity<String> removeUserFromChannel(@Validated
                                             @PathVariable Long userId,
                                             @PathVariable Long channelId) {

        return channelService.removeUserFromChannel(userId, channelId)
                ? ResponseEntity.ok("User successfully deleted from channel")
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/channels/{channelId}/{userId}")
    @JsonView(Views.Public.class)
    ResponseEntity<String> addUserToChannel(@Validated
                                             @PathVariable Long userId,
                                             @PathVariable Long channelId) {

        return channelService.addUserToChannel(userId, channelId)
                ? ResponseEntity.ok("User successfully added")
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/channels/{channelId}/{userId}")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> updateUserFromChannel(@Validated
                                                 @PathVariable Long userId,
                                                 @PathVariable Long channelId,
                                                 @RequestBody UpdateChannelUserRequest user) {

        Channel channel = channelService.updateUserFromChannel(userId, channelId,
                user.isCanEdit(), user.isCanDelete(), user.isCanView(), user.isCanInvite());

        return returnChannel(channel);
    }

    private ResponseEntity<Channel> returnChannel(Channel channel) {

        return channel!= null
                ? ResponseEntity.ok(channel)
                : ResponseEntity.notFound().build();
    }
}
