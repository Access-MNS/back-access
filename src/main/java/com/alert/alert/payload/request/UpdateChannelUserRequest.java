package com.alert.alert.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateChannelUserRequest {
    boolean canEdit;
    boolean canDelete;
    boolean canInvite;
    boolean canView;
}
