package com.elcom.plus.mecall.dto.response.category;

import com.elcom.plus.common.util.response.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemVideoResponse extends AbstractResponse {
    private Long catId;
    private Long itemId;
    private String creatorAvatar;
    private String thumbnail;
    private String title;
    private String username;
    private String fileName;
    private String filePath;

    private List<String> hashtags;
    private Long price;
    private int numberView;
    private int numberLike;
    private int numberShare;
    private String supplier;
    private int isLike;
    private int userId;
    private int creatorType;
    private int cpId;
    private int channelId;

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) return false;
        ItemVideoResponse videoResponse = (ItemVideoResponse) obj;
        return this.itemId.equals(videoResponse.itemId);
    }

    @Override
    public int hashCode() {
        return itemId.hashCode();
    }
}
