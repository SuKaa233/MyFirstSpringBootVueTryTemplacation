package com.study.mapper;

import com.study.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * from db_account where username= #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Insert("insert into db_account(username, password, email) values (#{username},#{password},#{email})")
    int creatAccount(String username, String password,String email);
}
