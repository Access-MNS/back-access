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

    @GetMapping("channels/{id}")
    @JsonView(Views.Public.class)
    Collection<Channel> getChannelsForUser(@PathVariable Long id) {
        return channelsUsersService.getChannelsByUserId(id);
    }

    @GetMapping("/channel/users/{id}")
    @JsonView(Views.Public.class)
    Collection<User> getChannelsUsers(@PathVariable Long id) {
        return channelsUsersService.getUsers(id);
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
    public ResponseEntity<String> deleteChannel(@PathVariable Long id) {

        return channelService.deleteChannel(id)
                ? ResponseEntity.ok("Channel deleted")
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

    @DeleteMapping("/channels/{channelId}/{userId}")
    @JsonView(Views.Public.class)
    ResponseEntity<String> removeUserFromChannel(@Validated
                                             @PathVariable Long userId,
                                             @PathVariable Long channelId) {

        return channelService.removeUserFromChannel(userId, channelId)
                ? ResponseEntity.ok("User successfully deleted from channel")
                : ResponseEntity.notFound().build();
    }

    private ResponseEntity<Channel> returnChannel(Channel channel) {

        return channel!= null
                ? ResponseEntity.ok(channel)
                : ResponseEntity.notFound().build();
    }
}
