package com.alert.alert.validation.Validators;

import com.alert.alert.entities.ChannelsUsers;
import com.alert.alert.entities.User;
import com.alert.alert.entities.enums.PermissionType;
import com.alert.alert.service.impl.ChannelsUsersServiceImpl;
import com.alert.alert.validation.PermissionCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public class PermissionValidator implements ConstraintValidator<PermissionCheck, Long> {

    private final ChannelsUsersServiceImpl channelsUsersService;
    private PermissionType permissionType;

    @Autowired
    public PermissionValidator(ChannelsUsersServiceImpl channelsUsersService) {
        this.channelsUsersService = channelsUsersService;
    }

    @Override
    public void initialize(PermissionCheck constraintAnnotation) {
        this.permissionType = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Long channelId, ConstraintValidatorContext context) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User user)) {return false;}
        if (channelId == null) {return false;}

        ChannelsUsers rights = channelsUsersService.getChannelUser(user.getId(), channelId);
        return switch (permissionType) {
            case VIEW -> rights != null && rights.canView();
            case EDIT -> rights != null && rights.canEdit();
            case INVITE -> rights != null && rights.canInvite();
            case DELETE -> rights != null && rights.canDelete();
        };
    }
}
