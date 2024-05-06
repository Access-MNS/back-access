package com.alert.alert.controller;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.ChannelsUsers;
import com.alert.alert.entities.User;
import com.alert.alert.entities.Views;
import com.alert.alert.entities.enums.PermissionType;
import com.alert.alert.payload.request.ChannelRequest;
import com.alert.alert.payload.request.UpdateChannelUserRequest;
import com.alert.alert.service.ChannelsUsersService;
import com.alert.alert.service.impl.ChannelServiceImpl;
import com.alert.alert.validation.IsAdmin;
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
    @IsAdmin
    @JsonView(Views.Public.class)
    Collection<Channel> getChannels() {
        return channelService.getChannels();
    }

    @GetMapping("channels/{id}")
    @JsonView(Views.Public.class)
    Collection<Channel> getChannelsForUser(@PathVariable Long id) {
        return channelsUsersService.getChannelsByUserId(id);
    }

    @GetMapping("/channel/users/{idChannel}/{idUser}")
    @JsonView(Views.Public.class)
    ChannelsUsers getChannelUser(@PathVariable Long idChannel, @PathVariable Long idUser) {
        return channelsUsersService.getChannelUser(idUser, idChannel);
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

    @PostMapping("/channels/private/{id}")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> createPrivateChannel(@Validated @RequestBody ChannelRequest channelRequest,
                                                 @PathVariable Long id) {
        return returnChannel(channelService.createPrivateChannel(channelRequest.toChannel(), id));
    }

    @PutMapping("/channels")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> updateChannel(@Validated @RequestBody ChannelRequest channelRequest) {
        return returnChannel(channelService.updateChannel(channelRequest.toChannel()));
    }

    @DeleteMapping("/channels/{id}")
    @IsAdmin
    @JsonView(Views.Public.class)
    public ResponseEntity<String> deleteChannel(@PermissionCheck(PermissionType.DELETE) @PathVariable Long id) {

        return channelService.deleteChannel(id)
                ? ResponseEntity.ok("Channel deleted")
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/channels/{channelId}/{userId}")
    @JsonView(Views.Public.class)
    ResponseEntity<String> addUserToChannel(@Validated
                                            @PathVariable Long userId,
                                            @PermissionCheck(PermissionType.INVITE) @PathVariable Long channelId) {

        return channelService.addUserToChannel(userId, channelId)
                ? ResponseEntity.ok("User successfully added")
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/channels/{channelId}/{userId}")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> updateUserFromChannel(@Validated
                                                  @PathVariable Long userId,
                                                  @PermissionCheck(PermissionType.EDIT) @PathVariable Long channelId,
                                                  @RequestBody UpdateChannelUserRequest user) {

        Channel channel = channelService.updateUserFromChannel(userId, channelId,
                user.isCanEdit(), user.isCanDelete(), user.isCanView(), user.isCanInvite());

        return returnChannel(channel);
    }

    @DeleteMapping("/channels/{channelId}/{userId}")
    @JsonView(Views.Public.class)
    ResponseEntity<String> removeUserFromChannel(@Validated
                                                 @PathVariable Long userId,
                                                 @PermissionCheck(PermissionType.DELETE) @PathVariable Long channelId) {

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
