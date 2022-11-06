package com.elcom.plus.auth.service;

import com.elcom.plus.auth.entity.Address;
import com.elcom.plus.auth.entity.Rank;

import java.util.List;

public interface RankService {
    List<Rank> getAllRank();
    Rank getOneRank();
    boolean saveRank();
    boolean updateRank();
    boolean deleteRank();
}
