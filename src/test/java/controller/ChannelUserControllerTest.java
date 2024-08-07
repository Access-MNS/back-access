package controller;

import com.alert.alert.controller.channel.ChannelUserController;
import com.alert.alert.entity.Channel;
import com.alert.alert.entity.ChannelUser;
import com.alert.alert.entity.User;
import com.alert.alert.payload.request.UpdateChannelUserRequest;
import com.alert.alert.service.impl.channel.ChannelsUsersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelUserControllerTest {

    @Mock
    private ChannelsUsersServiceImpl channelsUsersService;

    @InjectMocks
    private ChannelUserController channelUserController;

    @BeforeEach
    void setUp() {
        channelUserController = new ChannelUserController(channelsUsersService);
    }

    @Test
    void testGetChannelUser() {
        ChannelUser channelUser = new ChannelUser();
        when(channelsUsersService.getChannelUser(1L, 1L)).thenReturn(channelUser);

        ChannelUser response = channelUserController.getChannelUser(1L, 1L);

        assertEquals(channelUser, response);
        verify(channelsUsersService, times(1)).getChannelUser(1L, 1L);
    }

    @Test
    void testGetChannelsUsers() {
        Set<User> users = Set.of(new User(), new User());
        when(channelsUsersService.getUsers(anyLong())).thenReturn(users);

        Collection<User> response = channelUserController.getChannelsUsers(1L);

        assertEquals(users, response);
        verify(channelsUsersService, times(1)).getUsers(1L);
    }

    @Test
    void testGetChannelUsers() {
        Set<ChannelUser> channelUsers = Set.of(new ChannelUser(), new ChannelUser());
        when(channelsUsersService.getUsersChannel(1L)).thenReturn(channelUsers);

        Collection<ChannelUser> response = channelUserController.getChannelUsers(1L);

        assertEquals(channelUsers, response);
        verify(channelsUsersService, times(1)).getUsersChannel(1L);
    }

    @Test
    void testAddUserToChannel() {
        when(channelsUsersService.addUserToChannel(1L, 1L)).thenReturn(true);

        ResponseEntity<String> response = channelUserController.addUserToChannel(1L, 1L);

        assertEquals(ResponseEntity.ok("User successfully added"), response);
        verify(channelsUsersService, times(1)).addUserToChannel(1L, 1L);
    }

    @Test
    void testUpdateUserFromChannel() {
        Channel channel = new Channel();
        UpdateChannelUserRequest request = new UpdateChannelUserRequest();
        request.setCanEdit(true);
        request.setCanDelete(false);
        request.setCanView(true);
        request.setCanInvite(false);

        when(channelsUsersService.updateUserFromChannel(1L, 1L, true, false, true, false)).thenReturn(channel);

        ResponseEntity<Channel> response = channelUserController.updateUserFromChannel(1L, 1L, request);

        assertEquals(ResponseEntity.ok(channel), response);
        verify(channelsUsersService, times(1)).updateUserFromChannel(1L, 1L, true, false, true, false);
    }

    @Test
    void testRemoveUserFromChannel() {
        when(channelsUsersService.removeUserFromChannel(1L, 1L)).thenReturn(true);

        ResponseEntity<String> response = channelUserController.removeUserFromChannel(1L, 1L);

        assertEquals(ResponseEntity.ok("User successfully deleted from channel"), response);
        verify(channelsUsersService, times(1)).removeUserFromChannel(1L, 1L);
    }
}
