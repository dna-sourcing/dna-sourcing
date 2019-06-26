package com.dna.sourcing.dao.contract;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tbl_contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dnaid")
    private String dnaid;

    @Column(name = "company_dnaid")
    private String companyDnaid;

    @Column(name = "txhash")
    private String txhash;

    @Column(name = "filehash")
    private String filehash;

    @Column(name = "detail")
    private String detail;

    @Column(name = "type")
    private String type;

    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "timestamp_sign")
    private String timestampSign;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "status")
    private Integer status;

    @Column(name = "revoke_tx")
    private String revokeTx;

    @Transient
    private Integer height;  // 跨表
}