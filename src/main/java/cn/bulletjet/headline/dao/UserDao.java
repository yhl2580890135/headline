package cn.bulletjet.headline.dao;

import cn.bulletjet.headline.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    //public void updatePasswordById(User user,String password);
    public User findUserByUsername(String username);

    @Transactional
    @Modifying
    @Query("update User user set user.password=?2 where user.id=?1")
    public boolean updatePassword(String userId, String password);
}


