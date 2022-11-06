package com.elcom.plus.mecall.dao;

import com.elcom.plus.mecall.dto.response.banner.BannerResponse;
import com.elcom.plus.mecall.dto.response.category.CategoryResponse;
import com.elcom.plus.mecall.dto.response.category.ListItemVideoResponse;
import com.elcom.plus.mecall.dto.response.category.VideoDetailResponse;
import com.elcom.plus.mecall.dto.response.channel.ChannelResponse;
import com.elcom.plus.mecall.dto.response.cp.CpResponse;
import com.elcom.plus.mecall.dto.response.hashtag.HashtagResponse;
import com.elcom.plus.mecall.dto.response.category.ItemVideoResponse;
import com.elcom.plus.mecall.dto.response.music.MusicResponse;
import com.elcom.plus.mecall.dto.response.top.VideoTopAll;
import com.elcom.plus.mecall.dto.response.top.VideoTopResponse;
import com.elcom.plus.mecall.dto.response.video.CheckVideo;
import com.elcom.plus.mecall.entity.Category;

import java.util.List;

public interface CategoryDao {
    List<CategoryResponse> findAll();

    List<ItemVideoResponse> findItemTrending(String phone, int userId, int videoId, int skip, int take);

    ListItemVideoResponse findItemByCategory(String phone, int userId, int id, int videoId, int skip, int take);

    ListItemVideoResponse findVideoByHashtag(String phone, int userId, String hashtag, int videoId, int skip, int take);

    List<BannerResponse> findBanner();

    List<HashtagResponse> findHashtag(int skip, int take);

    List<VideoTopResponse> findTopVideoByType(int userId, int type, int skip, int take);

    List<ChannelResponse> findChannel(int skip, int take);

    List<CpResponse> findCp(int skip, int take);

    List<CategoryResponse> findCategory();

    String findBackgroundUrl();

    List<MusicResponse> getMusics(String phone);

    ItemVideoResponse findVideoById(String phone, int videoId, int userID);

    List<ItemVideoResponse> findHashtagByName(String phone, int userId, String key, int order, int skip, int take);

    ListItemVideoResponse searchVideo(String phone, int userId, String key, int videoId, int order, int skip, int take);

    ListItemVideoResponse getListVideoOfChannel(String phone, int userId, String key, int videoId, int order, int skip, int take);

    ListItemVideoResponse getListVideoOfCp(String phone, int userId, String key, int videoId, int order, int skip, int take);

    CheckVideo checkVideoById(String phone, int videoId);
}
