package cn.bulletjet.headline.model;

import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by nowcoder on 2016/7/3.
 */
@Entity
public class LoginTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private int userId;
    @Column
    private Date expired;
    @Column
    private int status = 0;// 0有效，1无效
    @Column
    private String ticket;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
