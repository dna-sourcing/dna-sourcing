-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 172.17.0.1:3306
-- Generation Time: Jun 25, 2019 at 08:01 AM
-- Server version: 5.7.26-0ubuntu0.16.04.1
-- PHP Version: 7.2.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dna-sourcing`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_action_dnaid`
--
drop table if exists `tbl_action_dnaid`;
CREATE TABLE `tbl_action_dnaid` (
  `id` int(11) NOT NULL,
  `action_index` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `ddo` text,
  `keystore` text,
  `dnaid` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `txhash` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_contract_company`
--
drop table if exists `tbl_contract_company`;
CREATE TABLE `tbl_contract_company` (
  `id` int(11) NOT NULL,
  `dnaid` varchar(128) NOT NULL,
  `prikey` varchar(128) NOT NULL COMMENT '私钥，必须和合约中的地址相对应',
  `code_addr` varchar(128) NOT NULL COMMENT '智能合约地址',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_contract_dnaid`
--
drop table if exists `tbl_contract_dnaid`;
CREATE TABLE `tbl_contract_dnaid` (
  `id` int(11) NOT NULL,
  `dnaid` varchar(255) DEFAULT NULL,
  `contract_index` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_contract_index`
--
drop table if exists `tbl_contract_index`;
CREATE TABLE `tbl_contract_index` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `flag` int(11) NOT NULL DEFAULT '0' COMMENT '当前操作表'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbl_event`
--
drop table if exists `tbl_event`;
CREATE TABLE `tbl_event` (
  `id` int(11) NOT NULL,
  `txhash` varchar(255) NOT NULL,
  `event` text NOT NULL,
  `height` int(11) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_action_dnaid`
--
ALTER TABLE `tbl_action_dnaid`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_contract_company`
--
ALTER TABLE `tbl_contract_company`
  ADD PRIMARY KEY (`id`),
  ADD KEY `dnaid` (`dnaid`);

--
-- Indexes for table `tbl_contract_dnaid`
--
ALTER TABLE `tbl_contract_dnaid`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_contract_index`
--
ALTER TABLE `tbl_contract_index`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `tbl_event`
--
ALTER TABLE `tbl_event`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `txhash` (`txhash`) USING BTREE;

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_action_dnaid`
--
ALTER TABLE `tbl_action_dnaid`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_contract_company`
--
ALTER TABLE `tbl_contract_company`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_contract_dnaid`
--
ALTER TABLE `tbl_contract_dnaid`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_contract_index`
--
ALTER TABLE `tbl_contract_index`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_event`
--
ALTER TABLE `tbl_event`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


ALTER TABLE `tbl_action_dnaid` ADD INDEX(`username`);