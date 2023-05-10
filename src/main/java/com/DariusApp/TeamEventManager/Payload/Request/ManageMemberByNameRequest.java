package com.DariusApp.TeamEventManager.Payload.Request;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class ManageMemberByNameRequest {
    private Integer userId;
    private Integer eventId;
    private String memberToManage;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManageMemberByNameRequest that = (ManageMemberByNameRequest) o;
        return Objects.equals(userId, that.userId) && Objects.equals(eventId, that.eventId) && Objects.equals(memberToManage, that.memberToManage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, eventId, memberToManage);
    }

    @Override
    public String toString() {
        return "ManageMemberByIdRequest{" +
                "userId=" + userId +
                ", eventId=" + eventId +
                ", memberToManageId=" + memberToManage +
                '}';
    }
}

