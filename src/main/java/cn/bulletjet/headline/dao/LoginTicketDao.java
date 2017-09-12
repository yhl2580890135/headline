package cn.bulletjet.headline.dao;

import cn.bulletjet.headline.model.LoginTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface LoginTicketDao extends JpaRepository<LoginTicket, Integer> {
    @Transactional
    @Modifying
    @Query("update LoginTicket l set l.status=?1 where l.ticket=?2")
    public void updateStatusByTicket(int status, String ticket);

    public LoginTicket findByTicket(String ticket);
}
