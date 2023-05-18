package com.DariusApp.TeamEventManager.Payload.Request.members;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class ManageMemberByIdRequest {
    private Integer userId;
    private Integer eventId;
    private Integer memberToManageId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManageMemberByIdRequest that = (ManageMemberByIdRequest) o;
        return Objects.equals(userId, that.userId) && Objects.equals(eventId, that.eventId) && Objects.equals(memberToManageId, that.memberToManageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, eventId, memberToManageId);
    }

    @Override
    public String toString() {
        return "ManageMemberByIdRequest{" +
                "userId=" + userId +
                ", eventId=" + eventId +
                ", memberToManageId=" + memberToManageId +
                '}';
    }
}

