package com.alert.alert.controller.channel;

import com.alert.alert.entities.Channel;
import com.alert.alert.entities.ChannelUser;
import com.alert.alert.entities.User;
import com.alert.alert.entities.Views;
import com.alert.alert.entities.enums.PermissionType;
import com.alert.alert.payload.request.UpdateChannelUserRequest;
import com.alert.alert.service.ChannelsUsersService;
import com.alert.alert.validation.IsUser;
import com.alert.alert.validation.PermissionCheck;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/channel/")
@IsUser
public class ChannelUserController {
    private final ChannelsUsersService channelsUsersService;

    @Autowired
    public ChannelUserController(ChannelsUsersService channelsUsersService) {
        this.channelsUsersService = channelsUsersService;
    }

    @GetMapping("/user/{idChannel}/{idUser}")
    @JsonView(Views.Public.class)
    ChannelUser getChannelUser(@PathVariable Long idChannel, @PathVariable Long idUser) {
        return channelsUsersService.getChannelUser(idUser, idChannel);
    }

    @GetMapping("/user/{id}")
    @JsonView(Views.Public.class)
    Collection<User> getChannelsUsers(@PathVariable Long id) {
        return channelsUsersService.getUsers(id);
    }

    @GetMapping("/channel_user/{channelId}")
    @JsonView(Views.Public.class)
    Collection<ChannelUser> getChannelUsers(@PathVariable Long channelId) {
        return channelsUsersService.getUsersChannel(channelId);
    }

    @GetMapping("/list/{id}")
    @JsonView(Views.Public.class)
    Collection<Channel> getChannelsForUser(@PathVariable Long id) {
        return channelsUsersService.getChannelsByUserId(id);
    }

    @PostMapping("/{channelId}/{userId}")
    @JsonView(Views.Public.class)
    ResponseEntity<String> addUserToChannel(@Validated
                                            @PathVariable Long userId,
                                            @PermissionCheck(PermissionType.INVITE) @PathVariable Long channelId) {

        return channelsUsersService.addUserToChannel(userId, channelId)
                ? ResponseEntity.ok("User successfully added")
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{channelId}/{userId}")
    @JsonView(Views.Public.class)
    ResponseEntity<Channel> updateUserFromChannel(@Validated
                                                  @PathVariable Long userId,
                                                  @PermissionCheck(PermissionType.EDIT) @PathVariable Long channelId,
                                                  @RequestBody UpdateChannelUserRequest user) {

        Channel channel = channelsUsersService.updateUserFromChannel(userId, channelId,
                user.isCanEdit(), user.isCanDelete(), user.isCanView(), user.isCanInvite());

        return channel!= null
                ? ResponseEntity.ok(channel)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{channelId}/{userId}")
    @JsonView(Views.Public.class)
    ResponseEntity<String> removeUserFromChannel(@Validated
                                                 @PathVariable Long userId,
                                                 @PermissionCheck(PermissionType.DELETE) @PathVariable Long channelId) {

        return channelsUsersService.removeUserFromChannel(userId, channelId)
                ? ResponseEntity.ok("User successfully deleted from channel")
                : ResponseEntity.notFound().build();
    }
}
