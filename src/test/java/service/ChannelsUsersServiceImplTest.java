package service;

import com.alert.alert.entity.Channel;
import com.alert.alert.entity.ChannelUser;
import com.alert.alert.entity.User;
import com.alert.alert.repository.ChannelRepository;
import com.alert.alert.repository.ChannelsUsersRepository;
import com.alert.alert.service.impl.channel.ChannelServiceImpl;
import com.alert.alert.service.impl.channel.ChannelsUsersServiceImpl;
import com.alert.alert.service.impl.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChannelsUsersServiceImplTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private ChannelServiceImpl channelService;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ChannelsUsersRepository channelsUsersRepository;

    @InjectMocks
    private ChannelsUsersServiceImpl channelsUsersService;

    @Test
    void testGetChannelUser() {
        Long userId = 1L;
        Long channelId = 2L;
        ChannelUser channelUser = new ChannelUser();
        when(channelsUsersRepository.findByUserIdAndChannelId(userId, channelId))
                .thenReturn(Optional.of(channelUser));

        ChannelUser result = channelsUsersService.getChannelUser(userId, channelId);

        assertEquals(channelUser, result);
        verify(channelsUsersRepository, times(1)).findByUserIdAndChannelId(userId, channelId);
    }

    @Test
    void testGetUsers() {
        Long channelId = 1L;
        Set<User> users = Set.of(new User(), new User());
        when(channelsUsersRepository.getChannelsUsersByChannelId(channelId)).thenReturn(users);

        Set<User> result = channelsUsersService.getUsers(channelId);

        assertEquals(users, result);
        verify(channelsUsersRepository, times(1)).getChannelsUsersByChannelId(channelId);
    }

    @Test
    void testGetUsersChannel() {
        Long channelId = 1L;
        Set<ChannelUser> channelUsers = Set.of(new ChannelUser(), new ChannelUser());
        when(channelsUsersRepository.findAllByChannelId(channelId)).thenReturn(channelUsers);

        Set<ChannelUser> result = channelsUsersService.getUsersChannel(channelId);

        assertEquals(channelUsers, result);
        verify(channelsUsersRepository, times(1)).findAllByChannelId(channelId);
    }

    @Test
    void testAddUserToChannel() {
        Long userId = 1L;
        Long channelId = 2L;
        Channel channel = new Channel();
        User user = new User();

        when(channelService.channelExists(channelId)).thenReturn(true);
        when(userService.userExists(userId)).thenReturn(true);
        when(channelsUsersRepository.existsByUserIdAndChannelId(userId, channelId)).thenReturn(false);
        when(channelService.getChannel(channelId)).thenReturn(channel);
        when(userService.getUser(userId)).thenReturn(user);
        when(channelRepository.save(any(Channel.class))).thenReturn(channel);

        boolean result = channelsUsersService.addUserToChannel(userId, channelId);

        assertTrue(result);
        verify(channelRepository, times(1)).save(channel);
    }

    @Test
    void testAddUserToChannel_ChannelDoesNotExist() {
        Long userId = 1L;
        Long channelId = 2L;

        when(channelService.channelExists(channelId)).thenReturn(false);

        boolean result = channelsUsersService.addUserToChannel(userId, channelId);

        assertFalse(result);
        verify(channelRepository, never()).save(any(Channel.class));
    }

    @Test
    void testAddUserToChannel_UserDoesNotExist() {
        Long userId = 1L;
        Long channelId = 2L;

        when(channelService.channelExists(channelId)).thenReturn(true);
        when(userService.userExists(userId)).thenReturn(false);

        boolean result = channelsUsersService.addUserToChannel(userId, channelId);

        assertFalse(result);
        verify(channelRepository, never()).save(any(Channel.class));
    }

    @Test
    void testUpdateUserFromChannel_ChannelDoesNotExist() {
        Long userId = 1L;
        Long channelId = 2L;

        when(channelService.channelExists(channelId)).thenReturn(false);

        Channel result = channelsUsersService.updateUserFromChannel(userId, channelId, true, false, true, false);

        assertNull(result);
        verify(channelRepository, never()).save(any(Channel.class));
    }

    @Test
    void testRemoveUserFromChannel() {
        Long userId = 1L;
        Long channelId = 2L;
        Channel channel = new Channel();
        User user = new User();

        when(channelService.channelExists(channelId)).thenReturn(true);
        when(userService.userExists(userId)).thenReturn(true);
        when(channelService.getChannel(channelId)).thenReturn(channel);
        when(userService.getUser(userId)).thenReturn(user);
        when(channelRepository.save(any(Channel.class))).thenReturn(channel);

        boolean result = channelsUsersService.removeUserFromChannel(userId, channelId);

        assertTrue(result);
        verify(channelRepository, times(1)).save(channel);
    }

    @Test
    void testRemoveUserFromChannel_ChannelDoesNotExist() {
        Long userId = 1L;
        Long channelId = 2L;

        when(channelService.channelExists(channelId)).thenReturn(false);

        boolean result = channelsUsersService.removeUserFromChannel(userId, channelId);

        assertFalse(result);
        verify(channelRepository, never()).save(any(Channel.class));
    }

    @Test
    void testChannelUserExists() {
        Long userId = 1L;
        Long channelId = 2L;
        when(channelsUsersRepository.existsByUserIdAndChannelId(userId, channelId)).thenReturn(true);

        boolean result = channelsUsersService.channelUserExists(userId, channelId);

        assertTrue(result);
        verify(channelsUsersRepository, times(1)).existsByUserIdAndChannelId(userId, channelId);
    }
}
