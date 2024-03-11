package com.alert.alert.controller;

import com.alert.alert.entities.Channel;
import com.alert.alert.payload.request.UpdateChannelUserRequest;
import com.alert.alert.service.impl.ChannelServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasAnyRole('ADMIN','USER')")
public class ChannelController {

    private final ChannelServiceImpl channelService;

    public ChannelController(ChannelServiceImpl channelService) {
        this.channelService = channelService;
    }

    @RequestMapping("/channels")
    Collection<Channel> channels() {
        return channelService.getChannels();
    }

    @RequestMapping("/channels/{id}")
    ResponseEntity<Channel> getChannel(@PathVariable Long id) {
        Channel channel = channelService.getChannel(id);
        return channel != null
                ? ResponseEntity.ok(channel)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/channels")
    ResponseEntity<Channel> createChannel(@Validated
                                          @RequestBody Channel channel) {
        return channelService.createChannel(channel)
                ? ResponseEntity.ok(channel)
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/channels")
    ResponseEntity<Channel> updateChannel(@Validated
                                          @RequestBody Channel channel) {
        return channelService.updateChannel(channel)
                ? ResponseEntity.ok(channel)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/channels/{id}")
    public ResponseEntity<String> deleteChannel(@PathVariable Long id) {

        return channelService.deleteChannel(id)
                ? ResponseEntity.ok("Channel deleted")
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/channels/{channelId}/{userId}")
    ResponseEntity<String> removeUserFromChannel(@Validated
                                             @PathVariable Long userId,
                                             @PathVariable Long channelId) {
        return channelService.removeUserFromChannel(userId, channelId)
                ? ResponseEntity.ok("User successfully deleted from channel")
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/channels/{channelId}/{userId}")
    ResponseEntity<String> addUserToChannel(@Validated
                                             @PathVariable Long userId,
                                             @PathVariable Long channelId) {
        return channelService.addUserToChannel(userId, channelId)
                ? ResponseEntity.ok("User successfully added")
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/channels/{channelId}/{userId}")
    ResponseEntity<String> updateUserFromChannel(@Validated
                                                 @PathVariable Long userId,
                                                 @PathVariable Long channelId,
                                                 @RequestBody UpdateChannelUserRequest user) {
        return channelService.updateUserFromChannel(userId, channelId,
                user.isCanEdit(), user.isCanDelete(), user.isCanView(), user.isCanInvite())
                ? ResponseEntity.ok("User updated successfully")
                : ResponseEntity.notFound().build();
    }
}
