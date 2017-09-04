package cn.bulletjet.headline.dao;

import cn.bulletjet.headline.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    //public void updatePasswordById(User user,String password);
}


