package com.elcom.plus.vrbt.dao.impl;

import com.elcom.plus.vrbt.dao.PackageDao;
import com.elcom.plus.vrbt.dto.response.PackDataResponse;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PackageDaoImpl extends AbstractDao implements PackageDao {
    @Override
    public List<PackDataResponse> packOfData() {
        List<PackDataResponse> result = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_LIST_PACKAGE(?)}");
            cs.setInt(1, 1);
            rs = cs.executeQuery();
            while (rs.next()) {
                PackDataResponse data = new PackDataResponse();
                data.setId(rs.getLong("id"));
                data.setTitle(rs.getString("name"));
                data.setPrice(rs.getInt("registration_fee"));
                data.setDescription(rs.getString("description"));
                data.setType(rs.getInt("package_type"));
                data.setRegistrationUseTime(rs.getString("registration_use_time"));
                data.setMoContent(rs.getString("mo_content"));
                result.add(data);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        return null;
    }

    @Override
    public PackDataResponse findById(Integer id) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = connectionManager.getConnection();
            cs = con.prepareCall("{CALL GET_PACKAGE_BY_ID(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();
            while (rs.next()) {
                PackDataResponse data = new PackDataResponse();
                data.setId(rs.getLong("id"));
                data.setTitle(rs.getString("name"));
                data.setPrice(rs.getInt("registration_fee"));
                data.setDescription(rs.getString("description"));
                data.setType(rs.getInt("package_type"));
                data.setRegistrationUseTime(rs.getString("registration_use_time"));
                data.setMoContent(rs.getString("mo_content"));
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finallyConnection(con, cs, rs);
        }
        return null;
    }
}
