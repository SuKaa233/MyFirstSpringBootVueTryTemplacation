package com.study.mapper;

import com.study.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Select("SELECT * from db_account where username= #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Insert("insert into db_account(username, password, email) values (#{username},#{password},#{email})")
    int creatAccount(String username, String password,String email);

    @Update("update db_account set password = #{password} where email = #{email}")
    int resetPassword(String password,String email);
}
