package com.dna.sourcing.dao.contract;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "tbl_contract_company")
public class ContractCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dnaid")
    private String dnaid;

    @Column(name = "prikey")
    private String prikey;

    @Column(name = "code_addr")
    private String codeAddr;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}