package com.elcom.plus.user.client;

import com.elcom.plus.user.dto.response.MyVideoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "mecall-service")
public interface MeCallClient {
    @GetMapping("/rest/api/mecall/video/users/{userId}")
    List<MyVideoResponse> myVideo(@PathVariable(value = "userId") int userId,
                                  @RequestParam(value = "skip") int skip,
                                  @RequestParam(value = "take") int take);

    @GetMapping("/rest/api/mecall/video/users/check/{userId}")
    String checkVideo(@PathVariable("userId") int userId,
                      @RequestParam("skip") int skip,
                      @RequestParam("take") int take);
}
