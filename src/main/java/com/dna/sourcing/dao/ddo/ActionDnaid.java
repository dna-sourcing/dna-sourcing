package com.dna.sourcing.dao.ddo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tbl_action_dnaid")
public class ActionDnaid {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String dnaid;

    @Column
    private String txhash;

    @Column
    private String keystore;

    @Column
    private String ddo;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "action_index")
    private Integer actionIndex;

}