package com.elcom.plus.auth.service.impl;

import com.elcom.plus.auth.dao.impl.AbstractDao;
import com.elcom.plus.auth.entity.Rank;
import com.elcom.plus.auth.service.RankService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankServiceImpl extends AbstractDao implements RankService {
    @Override
    public List<Rank> getAllRank() {
        return null;
    }

    @Override
    public Rank getOneRank() {
        return null;
    }

    @Override
    public boolean saveRank() {
        return false;
    }

    @Override
    public boolean updateRank() {
        return false;
    }

    @Override
    public boolean deleteRank() {
        return false;
    }
}
