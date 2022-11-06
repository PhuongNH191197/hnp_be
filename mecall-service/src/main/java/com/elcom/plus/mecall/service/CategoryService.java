package com.elcom.plus.mecall.service;

import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.mecall.dto.response.banner.BannerResponse;
import com.elcom.plus.mecall.dto.response.category.CategoryGroupResponse;
import com.elcom.plus.mecall.dto.response.category.CategoryResponse;
import com.elcom.plus.mecall.dto.response.channel.ChannelResponse;
import com.elcom.plus.mecall.dto.response.cp.CpResponse;
import com.elcom.plus.mecall.dto.response.hashtag.HashtagResponse;
import com.elcom.plus.mecall.dto.response.category.ItemVideoResponse;
import com.elcom.plus.mecall.dto.response.music.MusicResponse;
import com.elcom.plus.mecall.dto.response.top.VideoTopAll;
import com.elcom.plus.mecall.dto.response.top.VideoTopResponse;
import schemasMicrosoftComVml.CTShapetype;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> listCategory();

    List<ItemVideoResponse> findItemTrending(String phone, int userId, int videoId, int skip, int take);

    Response findItemByCategory(String phone, int userId, int videoId, String typeSearch, String keySearch, int skip, int take);

    Response checkVideoById(String phone, int videoId);

    List<BannerResponse> findBanner();

    List<HashtagResponse> findHashtag(int skip, int take);

    VideoTopAll findAllTopVideo(int userId);

    List<VideoTopResponse> findTopVideoByType(int userId, int type, int skip, int take);

    List<ChannelResponse> findChannel(int skip, int take);

    List<CpResponse> findCp(int skip, int take);

    CategoryGroupResponse findCategoryGroup();

    List<MusicResponse> getMusics(String phone, int userId);

    List<ItemVideoResponse> getListVideoByType(String phone, int userId, int type, String key, int order, int skip, int take);
}
