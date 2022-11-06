package com.elcom.plus.user.dto.response;

import com.elcom.plus.common.util.response.AbstractResponse;
import com.elcom.plus.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends AbstractResponse {
    private Long id;
    private String username;
    private String phone;
    private int loginType;
    private String email;
    private int status;
    private int channel;
    private String avatar;
    private int autoPlayVideo;
    private int earnMoney;
    private int policy;
    private int followers;
    private int followings;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.phone = user.getPhone();
        this.loginType = user.getLoginType();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.channel = user.getChannel();
        this.avatar = user.getAvatar();
        this.autoPlayVideo = user.getAutoPlayVideo();
        this.earnMoney = user.getEarnMoney();
        this.policy = user.getPolicy();
        this.followers = user.getFollowers();
        this.followings = user.getFollowings();
    }
}
