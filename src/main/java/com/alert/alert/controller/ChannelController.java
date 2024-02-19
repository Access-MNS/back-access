package com.alert.alert.controller;

import com.alert.alert.entities.Channel;
import com.alert.alert.service.impl.ChannelServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/")
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
    ResponseEntity<Channel> createChannel(@Validated @RequestBody Channel channel) {
        return channelService.createChannel(channel)
                ? ResponseEntity.ok(channel)
                : ResponseEntity.notFound().build();
    }

    @PutMapping("channels")
    ResponseEntity<Channel> updateChannel(@Validated @RequestBody Channel channel) {
        return channelService.updateChannel(channel)
                ? ResponseEntity.ok(channel)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("channels/{id}")
    public ResponseEntity<Channel> deleteChannel(@PathVariable Long id) {
        return channelService.deleteChannel(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
