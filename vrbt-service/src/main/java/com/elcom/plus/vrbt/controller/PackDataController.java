package com.elcom.plus.vrbt.controller;

import com.elcom.plus.common.util.response.ListResponse;
import com.elcom.plus.common.util.response.Response;
import com.elcom.plus.common.util.response.ResponseUtil;
import com.elcom.plus.vrbt.dto.response.PackDataResponse;
import com.elcom.plus.vrbt.service.PackageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/api/vrbt/pack-data")
public class PackDataController {
    private PackageService packageService;

    @GetMapping
    public ResponseEntity<ListResponse> packData() {
        List<PackDataResponse> data = packageService.packOfData();
        return ResponseUtil.buildResponseEntity(data, HttpStatus.OK);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<Response> registerPackData(@PathVariable Integer id,
                                                     @RequestHeader("phone") String phone,
                                                     @RequestHeader("username") String username) {
        Response result = packageService.registerPackData(id, phone, username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{id}/unregister")
    public ResponseEntity<Response> unRegisterPackData(@PathVariable Integer id,
                                                       @RequestHeader("phone") String phone,
                                                       @RequestHeader("username") String username) {
        Response result = packageService.unRegisterPackData(id, phone, username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{id}/app/buy-content")
    public ResponseEntity<Response> buyContent(@PathVariable Integer id,
                                               @RequestHeader("phone") String phone,
                                               @RequestHeader("username") String username) {
        Response result = packageService.buyContent(id, phone, username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{id}/app/cancel-content")
    public ResponseEntity<Response> cancelContent(@PathVariable Integer id,
                                                  @RequestHeader("phone") String phone,
                                                  @RequestHeader("username") String username) {
        Response result = packageService.cancelContent(id, phone, username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
