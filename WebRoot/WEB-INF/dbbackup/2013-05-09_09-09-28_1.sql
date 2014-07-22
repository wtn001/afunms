-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `act_id_info`
--

DROP TABLE IF EXISTS `act_id_info`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `act_id_info` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `USER_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `VALUE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PASSWORD_` longblob,
  `PARENT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `act_id_info`
--

LOCK TABLES `act_id_info` WRITE;
/*!40000 ALTER TABLE `act_id_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `act_id_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `act_id_membership`
--

DROP TABLE IF EXISTS `act_id_membership`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `act_id_membership` (
  `USER_ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `GROUP_ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`USER_ID_`,`GROUP_ID_`),
  KEY `ACT_FK_MEMB_GROUP` (`GROUP_ID_`),
  CONSTRAINT `ACT_FK_MEMB_GROUP` FOREIGN KEY (`GROUP_ID_`) REFERENCES `act_id_group` (`ID_`),
  CONSTRAINT `ACT_FK_MEMB_USER` FOREIGN KEY (`USER_ID_`) REFERENCES `act_id_user` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `act_id_membership`
--

LOCK TABLES `act_id_membership` WRITE;
/*!40000 ALTER TABLE `act_id_membership` DISABLE KEYS */;
/*!40000 ALTER TABLE `act_id_membership` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `act_id_user`
--

DROP TABLE IF EXISTS `act_id_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `act_id_user` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `FIRST_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `LAST_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EMAIL_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PWD_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PICTURE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `act_id_user`
--

LOCK TABLES `act_id_user` WRITE;
/*!40000 ALTER TABLE `act_id_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `act_id_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `act_re_deployment`
--

DROP TABLE IF EXISTS `act_re_deployment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `act_re_deployment` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DEPLOY_TIME_` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `act_re_deployment`
--

LOCK TABLES `act_re_deployment` WRITE;
/*!40000 ALTER TABLE `act_re_deployment` DISABLE KEYS */;
/*!40000 ALTER TABLE `act_re_deployment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `act_re_procdef`
--

DROP TABLE IF EXISTS `act_re_procdef`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `act_re_procdef` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `VERSION_` int(11) DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `RESOURCE_NAME_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DGRM_RESOURCE_NAME_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `HAS_START_FORM_KEY_` tinyint(4) DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `act_re_procdef`
--

LOCK TABLES `act_re_procdef` WRITE;
/*!40000 ALTER TABLE `act_re_procdef` DISABLE KEYS */;
/*!40000 ALTER TABLE `act_re_procdef` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `act_ru_event_subscr`
--

DROP TABLE IF EXISTS `act_ru_event_subscr`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `act_ru_event_subscr` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `EVENT_TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `EVENT_NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACTIVITY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `CONFIGURATION_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_EVENT_SUBSCR_CONFIG_` (`CONFIGURATION_`),
  KEY `ACT_FK_EVENT_EXEC` (`EXECUTION_ID_`),
  CONSTRAINT `ACT_FK_EVENT_EXEC` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `act_ru_event_subscr`
--

LOCK TABLES `act_ru_event_subscr` WRITE;
/*!40000 ALTER TABLE `act_ru_event_subscr` DISABLE KEYS */;
/*!40000 ALTER TABLE `act_ru_event_subscr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `act_ru_execution`
--

DROP TABLE IF EXISTS `act_ru_execution`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `act_ru_execution` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BUSINESS_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `SUPER_EXEC_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `IS_ACTIVE_` tinyint(4) DEFAULT NULL,
  `IS_CONCURRENT_` tinyint(4) DEFAULT NULL,
  `IS_SCOPE_` tinyint(4) DEFAULT NULL,
  `IS_EVENT_SCOPE_` tinyint(4) DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  UNIQUE KEY `ACT_UNIQ_RU_BUS_KEY` (`PROC_DEF_ID_`,`BUSINESS_KEY_`),
  KEY `ACT_IDX_EXEC_BUSKEY` (`BUSINESS_KEY_`),
  KEY `ACT_FK_EXE_PROCINST` (`PROC_INST_ID_`),
  KEY `ACT_FK_EXE_PARENT` (`PARENT_ID_`),
  KEY `ACT_FK_EXE_SUPER` (`SUPER_EXEC_`),
  CONSTRAINT `ACT_FK_EXE_PARENT` FOREIGN KEY (`PARENT_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_EXE_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ACT_FK_EXE_SUPER` FOREIGN KEY (`SUPER_EXEC_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `act_ru_execution`
--

LOCK TABLES `act_ru_execution` WRITE;
/*!40000 ALTER TABLE `act_ru_execution` DISABLE KEYS */;
/*!40000 ALTER TABLE `act_ru_execution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `act_ru_identitylink`
--

DROP TABLE IF EXISTS `act_ru_identitylink`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `act_ru_identitylink` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `GROUP_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `USER_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_IDENT_LNK_USER` (`USER_ID_`),
  KEY `ACT_IDX_IDENT_LNK_GROUP` (`GROUP_ID_`),
  KEY `ACT_FK_TSKASS_TASK` (`TASK_ID_`),
  CONSTRAINT `ACT_FK_TSKASS_TASK` FOREIGN KEY (`TASK_ID_`) REFERENCES `act_ru_task` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `act_ru_identitylink`
--

LOCK TABLES `act_ru_identitylink` WRITE;
/*!40000 ALTER TABLE `act_ru_identitylink` DISABLE KEYS */;
/*!40000 ALTER TABLE `act_ru_identitylink` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `act_ru_task`
--

DROP TABLE IF EXISTS `act_ru_task`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `act_ru_task` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TASK_DEF_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `OWNER_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ASSIGNEE_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DELEGATION_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PRIORITY_` int(11) DEFAULT NULL,
  `CREATE_TIME_` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `DUE_DATE_` datetime DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_TASK_CREATE` (`CREATE_TIME_`),
  KEY `ACT_FK_TASK_EXE` (`EXECUTION_ID_`),
  KEY `ACT_FK_TASK_PROCINST` (`PROC_INST_ID_`),
  KEY `ACT_FK_TASK_PROCDEF` (`PROC_DEF_ID_`),
  CONSTRAINT `ACT_FK_TASK_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_TASK_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
  CONSTRAINT `ACT_FK_TASK_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `act_ru_task`
--

LOCK TABLES `act_ru_task` WRITE;
/*!40000 ALTER TABLE `act_ru_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `act_ru_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_db_node`
--

DROP TABLE IF EXISTS `app_db_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `app_db_node` (
  `id` int(11) NOT NULL,
  `alias` varchar(30) DEFAULT NULL,
  `ip_address` varchar(15) DEFAULT NULL,
  `ip_long` bigint(11) DEFAULT NULL,
  `category` tinyint(2) DEFAULT NULL,
  `db_name` varchar(50) DEFAULT NULL,
  `port` varchar(5) DEFAULT NULL,
  `users` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `dbuse` varchar(100) DEFAULT NULL,
  `sendmobiles` varchar(200) DEFAULT NULL,
  `sendemail` varchar(200) DEFAULT NULL,
  `managed` int(2) DEFAULT NULL,
  `bid` varchar(100) DEFAULT NULL,
  `dbtype` int(11) DEFAULT NULL,
  `sendphone` varchar(200) DEFAULT NULL,
  `collecttype` int(2) DEFAULT NULL,
  `supperid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `app_db_node`
--

LOCK TABLES `app_db_node` WRITE;
/*!40000 ALTER TABLE `app_db_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_db_node` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:09:52
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hdc_eventlist`
--

DROP TABLE IF EXISTS `hdc_eventlist`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `hdc_eventlist` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eventListIndexSerialNumber` varchar(50) DEFAULT NULL,
  `eventListNickname` varchar(50) DEFAULT NULL,
  `eventListIndexRecordNo` varchar(50) DEFAULT NULL,
  `eventListREFCODE` varchar(50) DEFAULT NULL,
  `eventListDate` varchar(50) DEFAULT NULL,
  `eventListTime` varchar(50) DEFAULT NULL,
  `eventListDescription` varchar(50) DEFAULT NULL,
  `nodeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `hdc_eventlist`
--

LOCK TABLES `hdc_eventlist` WRITE;
/*!40000 ALTER TABLE `hdc_eventlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdc_eventlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdc_sys_info`
--

DROP TABLE IF EXISTS `hdc_sys_info`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `hdc_sys_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `raidlistSerialNumber` varchar(100) DEFAULT NULL,
  `raidlistMibNickName` varchar(100) DEFAULT NULL,
  `raidlistDKCMainVersion` varchar(100) DEFAULT NULL,
  `raidlistDKCProductName` varchar(100) DEFAULT NULL,
  `nodeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `hdc_sys_info`
--

LOCK TABLES `hdc_sys_info` WRITE;
/*!40000 ALTER TABLE `hdc_sys_info` DISABLE KEYS */;
INSERT INTO `hdc_sys_info` VALUES (21,'null','null','null','null',85),(25,'null','null','null','null',86);
/*!40000 ALTER TABLE `hdc_sys_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdc_sysinfo`
--

DROP TABLE IF EXISTS `hdc_sysinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `hdc_sysinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `dfSystemProductName` varchar(50) DEFAULT NULL,
  `dfSystemMicroRevsion` varchar(50) DEFAULT NULL,
  `dfSystemSerialNumber` varchar(50) DEFAULT NULL,
  `dfLUNSerialNumber` varchar(50) DEFAULT NULL,
  `dfLUNPortID` varchar(50) DEFAULT NULL,
  `dfWWNSerialNumber` varchar(50) DEFAULT NULL,
  `dfWWNPortID` varchar(50) DEFAULT NULL,
  `dfWWNControlIndex` varchar(50) DEFAULT NULL,
  `dfWWNNickName` varchar(50) DEFAULT NULL,
  `dfWWNID` varchar(50) DEFAULT NULL,
  `dfSwitchSerialNumber` varchar(50) DEFAULT NULL,
  `dfSwitchPortID` varchar(50) DEFAULT NULL,
  `dfLUNLUN` varchar(50) DEFAULT NULL,
  `nodeid` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `hdc_sysinfo`
--

LOCK TABLES `hdc_sysinfo` WRITE;
/*!40000 ALTER TABLE `hdc_sysinfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdc_sysinfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inbox`
--

DROP TABLE IF EXISTS `inbox`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `inbox` (
  `ID` int(4) NOT NULL AUTO_INCREMENT,
  `Sender` varchar(40) DEFAULT NULL,
  `Msg` varchar(500) DEFAULT NULL,
  `ArrivedTime` datetime DEFAULT NULL,
  `CommPort` int(4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PRIMARY_INBOX` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `inbox`
--

LOCK TABLES `inbox` WRITE;
/*!40000 ALTER TABLE `inbox` DISABLE KEYS */;
/*!40000 ALTER TABLE `inbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_alltype`
--

DROP TABLE IF EXISTS `ip_alltype`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_alltype` (
  `backbone_begin` varchar(15) DEFAULT NULL,
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `backbone_name` varchar(50) DEFAULT NULL,
  `pe_begin` varchar(15) DEFAULT NULL,
  `pe_end` varchar(15) DEFAULT NULL,
  `pe_ce_begin` varchar(15) DEFAULT NULL,
  `pe_ce_end` varchar(15) DEFAULT NULL,
  `loopback_begin` varchar(15) DEFAULT NULL,
  `loopback_end` varchar(15) DEFAULT NULL,
  `bussiness_begin` varchar(15) DEFAULT NULL,
  `backbone_end` varchar(15) DEFAULT NULL,
  `bussiness_end` varchar(15) DEFAULT NULL,
  `section` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_alltype`
--

LOCK TABLES `ip_alltype` WRITE;
/*!40000 ALTER TABLE `ip_alltype` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_alltype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_apply`
--

DROP TABLE IF EXISTS `ip_apply`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_apply` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `ids` varchar(64) DEFAULT NULL,
  `routeName` varchar(150) DEFAULT NULL,
  `routeType` varchar(50) DEFAULT NULL,
  `routeCount` int(8) DEFAULT '0',
  `switchName` varchar(150) DEFAULT NULL,
  `switchType` varchar(50) DEFAULT NULL,
  `switchCount` int(8) DEFAULT '0',
  `deviceType` varchar(50) DEFAULT NULL,
  `deviceCount` int(8) DEFAULT '0',
  `hasCabinet` varchar(2) DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL,
  `debugUnit` varchar(200) DEFAULT NULL,
  `flag` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_apply`
--

LOCK TABLES `ip_apply` WRITE;
/*!40000 ALTER TABLE `ip_apply` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_apply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_backbone`
--

DROP TABLE IF EXISTS `ip_backbone`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_backbone` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `backbone` varchar(50) DEFAULT NULL,
  `type` int(1) DEFAULT '0',
  `backbone_id` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_backbone`
--

LOCK TABLES `ip_backbone` WRITE;
/*!40000 ALTER TABLE `ip_backbone` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_backbone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_bussiness`
--

DROP TABLE IF EXISTS `ip_bussiness`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_bussiness` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `bussiness` varchar(15) DEFAULT NULL,
  `type` int(1) DEFAULT '0',
  `bussiness_id` bigint(11) DEFAULT NULL,
  `field_id` varchar(50) DEFAULT NULL,
  `bus_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_bussiness`
--

LOCK TABLES `ip_bussiness` WRITE;
/*!40000 ALTER TABLE `ip_bussiness` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_bussiness` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_bussinessstorage`
--

DROP TABLE IF EXISTS `ip_bussinessstorage`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_bussinessstorage` (
  `encryption` varchar(50) DEFAULT NULL,
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `backbone_name` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `segment` varchar(19) DEFAULT NULL,
  `gateway` varchar(15) DEFAULT NULL,
  `bussiness` varchar(50) DEFAULT NULL,
  `field_id` bigint(11) DEFAULT '0',
  `access_point` varchar(50) DEFAULT NULL,
  `vpn` varchar(50) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_bussinessstorage`
--

LOCK TABLES `ip_bussinessstorage` WRITE;
/*!40000 ALTER TABLE `ip_bussinessstorage` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_bussinessstorage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_bussinesstype`
--

DROP TABLE IF EXISTS `ip_bussinesstype`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_bussinesstype` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `descr` varchar(100) DEFAULT NULL,
  `bak` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_bussinesstype`
--

LOCK TABLES `ip_bussinesstype` WRITE;
/*!40000 ALTER TABLE `ip_bussinesstype` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_bussinesstype` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:09:53
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ip_encryptiontype`
--

DROP TABLE IF EXISTS `ip_encryptiontype`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_encryptiontype` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `descr` varchar(100) DEFAULT NULL,
  `bak` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_encryptiontype`
--

LOCK TABLES `ip_encryptiontype` WRITE;
/*!40000 ALTER TABLE `ip_encryptiontype` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_encryptiontype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_field`
--

DROP TABLE IF EXISTS `ip_field`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_field` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `loopback` varchar(50) DEFAULT NULL,
  `pe_f_id` varchar(50) DEFAULT NULL,
  `pe_e_id` varchar(50) DEFAULT NULL,
  `pe_ce_f_id` varchar(50) DEFAULT NULL,
  `pe_ce_e_id` varchar(50) DEFAULT NULL,
  `running` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `backbone_name` varchar(50) DEFAULT NULL,
  `section` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_field`
--

LOCK TABLES `ip_field` WRITE;
/*!40000 ALTER TABLE `ip_field` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_field` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_loopback`
--

DROP TABLE IF EXISTS `ip_loopback`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_loopback` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `loopback` varchar(15) DEFAULT NULL,
  `type` int(1) DEFAULT '0',
  `loopback_id` bigint(11) DEFAULT NULL,
  `field_id` varchar(50) DEFAULT 'null',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_loopback`
--

LOCK TABLES `ip_loopback` WRITE;
/*!40000 ALTER TABLE `ip_loopback` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_loopback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_pe`
--

DROP TABLE IF EXISTS `ip_pe`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_pe` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `pe` varchar(15) DEFAULT NULL,
  `type` int(1) DEFAULT '0',
  `pe_id` bigint(11) DEFAULT NULL,
  `field_id` varchar(50) DEFAULT 'null',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_pe`
--

LOCK TABLES `ip_pe` WRITE;
/*!40000 ALTER TABLE `ip_pe` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_pe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_pe_ce`
--

DROP TABLE IF EXISTS `ip_pe_ce`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_pe_ce` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `pe_ce` varchar(15) DEFAULT NULL,
  `type` int(1) DEFAULT '0',
  `pe_ce_id` bigint(11) DEFAULT NULL,
  `field_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_pe_ce`
--

LOCK TABLES `ip_pe_ce` WRITE;
/*!40000 ALTER TABLE `ip_pe_ce` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_pe_ce` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_portal_apply`
--

DROP TABLE IF EXISTS `ip_portal_apply`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_portal_apply` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `ids` varchar(64) DEFAULT NULL,
  `routeName` varchar(150) DEFAULT NULL,
  `routeType` varchar(50) DEFAULT NULL,
  `routeCount` int(8) DEFAULT '0',
  `switchName` varchar(150) DEFAULT NULL,
  `switchType` varchar(50) DEFAULT NULL,
  `switchCount` int(8) DEFAULT '0',
  `deviceType` varchar(50) DEFAULT NULL,
  `deviceCount` int(8) DEFAULT '0',
  `hasCabinet` varchar(2) DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL,
  `debugUnit` varchar(200) DEFAULT NULL,
  `flag` varchar(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_portal_apply`
--

LOCK TABLES `ip_portal_apply` WRITE;
/*!40000 ALTER TABLE `ip_portal_apply` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_portal_apply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_station`
--

DROP TABLE IF EXISTS `ip_station`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_station` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `station` varchar(15) DEFAULT NULL,
  `type` int(1) DEFAULT '0',
  `station_id` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_station`
--

LOCK TABLES `ip_station` WRITE;
/*!40000 ALTER TABLE `ip_station` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_station` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ip_stationtype`
--

DROP TABLE IF EXISTS `ip_stationtype`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ip_stationtype` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `descr` varchar(100) DEFAULT NULL,
  `bak` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ip_stationtype`
--

LOCK TABLES `ip_stationtype` WRITE;
/*!40000 ALTER TABLE `ip_stationtype` DISABLE KEYS */;
/*!40000 ALTER TABLE `ip_stationtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ipmac`
--

DROP TABLE IF EXISTS `ipmac`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ipmac` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT,
  `RELATEIPADDR` varchar(30) DEFAULT NULL,
  `IFINDEX` varchar(30) DEFAULT NULL,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `MAC` varchar(20) DEFAULT NULL,
  `IFBAND` varchar(2) DEFAULT NULL,
  `IFSMS` varchar(2) DEFAULT NULL,
  `COLLECTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `BAK` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ipmac`
--

LOCK TABLES `ipmac` WRITE;
/*!40000 ALTER TABLE `ipmac` DISABLE KEYS */;
/*!40000 ALTER TABLE `ipmac` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ipmacband`
--

DROP TABLE IF EXISTS `ipmacband`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ipmacband` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT,
  `RELATEIPADDR` varchar(30) DEFAULT NULL,
  `IFINDEX` varchar(30) DEFAULT NULL,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `MAC` varchar(20) DEFAULT NULL,
  `IFBAND` varchar(2) DEFAULT NULL,
  `IFSMS` varchar(2) DEFAULT NULL,
  `COLLECTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `BAK` varchar(100) DEFAULT NULL,
  `EMPLOYEE_ID` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ipmacband`
--

LOCK TABLES `ipmacband` WRITE;
/*!40000 ALTER TABLE `ipmacband` DISABLE KEYS */;
/*!40000 ALTER TABLE `ipmacband` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:09:54
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ipmacchange`
--

DROP TABLE IF EXISTS `ipmacchange`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ipmacchange` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `MAC` varchar(20) DEFAULT NULL,
  `CHANGETYPE` varchar(2) DEFAULT NULL,
  `DETAIL` varchar(200) DEFAULT NULL,
  `COLLECTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `BAK` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `ipmacchange`
--

LOCK TABLES `ipmacchange` WRITE;
/*!40000 ALTER TABLE `ipmacchange` DISABLE KEYS */;
/*!40000 ALTER TABLE `ipmacchange` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `iprouter`
--

DROP TABLE IF EXISTS `iprouter`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `iprouter` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT,
  `RELATEIPADDR` varchar(30) DEFAULT NULL,
  `IFINDEX` varchar(30) DEFAULT NULL,
  `NEXTHOP` varchar(30) DEFAULT NULL,
  `TYPE` bigint(10) DEFAULT NULL,
  `PROTO` bigint(10) DEFAULT NULL,
  `MASK` varchar(30) DEFAULT NULL,
  `PHYSADDRESS` varchar(100) DEFAULT NULL,
  `DEST` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `iprouter`
--

LOCK TABLES `iprouter` WRITE;
/*!40000 ALTER TABLE `iprouter` DISABLE KEYS */;
/*!40000 ALTER TABLE `iprouter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_agent_config`
--

DROP TABLE IF EXISTS `nms_agent_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_agent_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agentname` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(60) DEFAULT NULL,
  `agentport` int(11) DEFAULT NULL,
  `agentdesc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_agent_config`
--

LOCK TABLES `nms_agent_config` WRITE;
/*!40000 ALTER TABLE `nms_agent_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_agent_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_alarm_indicators`
--

DROP TABLE IF EXISTS `nms_alarm_indicators`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_alarm_indicators` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `subtype` varchar(255) DEFAULT NULL,
  `datatype` varchar(255) DEFAULT NULL,
  `moid` varchar(100) DEFAULT NULL,
  `threshold` varchar(255) DEFAULT NULL,
  `threshold_unit` varchar(10) DEFAULT NULL,
  `compare` varchar(10) DEFAULT '1',
  `compare_type` varchar(10) DEFAULT '1',
  `alarm_times` varchar(10) DEFAULT '1',
  `alarm_info` varchar(100) DEFAULT '',
  `alarm_level` varchar(10) DEFAULT '1',
  `enabled` varchar(10) DEFAULT '1',
  `poll_interval` varchar(10) DEFAULT NULL,
  `interval_unit` varchar(10) DEFAULT NULL,
  `subentity` varchar(50) DEFAULT NULL,
  `limenvalue0` bigint(20) DEFAULT NULL,
  `limenvalue1` bigint(20) DEFAULT NULL,
  `limenvalue2` bigint(20) DEFAULT NULL,
  `time0` int(3) DEFAULT NULL,
  `time1` int(3) DEFAULT NULL,
  `time2` int(3) DEFAULT NULL,
  `sms0` int(2) DEFAULT NULL,
  `sms1` int(2) DEFAULT NULL,
  `sms2` int(2) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `descr` varchar(50) DEFAULT NULL,
  `unit` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=494 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_alarm_indicators`
--

LOCK TABLES `nms_alarm_indicators` WRITE;
/*!40000 ALTER TABLE `nms_alarm_indicators` DISABLE KEYS */;
INSERT INTO `nms_alarm_indicators` VALUES (2,'ping','db','oracle','Number','null','1','%','0','1','null','连通率超过阀值','null','1','null','null','null',50,30,20,3,3,3,1,1,1,'null','连通率','null'),(3,'tablespace','db','oracle','Number','null','1','%','1','1','null','表空间超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','表空间','null'),(4,'buffercache','db','oracle','Number','null','1','%','0','1','null','缓冲区命中率超过阀值','null','1','null','null','null',90,80,70,3,3,3,1,1,1,'null','缓冲区命中率','null'),(5,'dictionarycache','db','oracle','Number','null','1','%','0','1','null','数据字典命中率超过阀值','null','1','null','null','null',90,80,70,3,3,3,1,1,1,'null','数据字典命中率','null'),(6,'pctmemorysorts','db','oracle','Number','null','1','%','0','1','null','内存中的排序超过阀值','null','1','null','null','null',90,80,70,3,3,3,1,0,1,'null','内存中的排序','null'),(7,'pctbufgets','db','oracle','Number','null','1','%','1','1','null','最浪费内存的前10个语句占全部内存读取量的比例超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','最浪费内存的前10个语句占全部内存读取量的比例','null'),(8,'tablespaceinc','db','oracle','Number','null','1','%','1','1','null','表空间增长率超过阀值','null','1','null','null','null',10,20,30,3,3,3,1,1,1,'null','表空间增长率','null'),(9,'opencursor','db','oracle','Number','null','1','个','1','1','null','打开的游标数超过阀值','null','1','null','null','null',80,90,100,3,3,3,1,1,1,'null','打开的游标数','null'),(10,'cpu','host','windows','Number','1.3.6.1.4.1.311.1.1.3.1.1','1','%','1','1',NULL,'cpu利用率超过阀值',NULL,'1','5','m',NULL,80,90,95,3,3,3,1,1,1,NULL,'cpu利用率',NULL),(11,'diskperc','host','windows','Number','null','1','%','1','1','null','磁盘利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','磁盘','null'),(12,'service','host','windows','String','null','1','无','1','1','null','服务丢失','null','1','5','m','null',1,1,1,3,3,3,1,1,1,'null','服务','null'),(13,'physicalmemory','host','windows','Number','null','1','%','1','1','null','物理内存超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','物理内存','null'),(14,'virtualmemory','host','windows','Number','null','1','%','1','1','null','虚拟内存超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','虚拟内存','null'),(15,'process','host','windows','String','null','1','无','1','1','null','进程丢失','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','进程','null'),(16,'hardware','host','windows','String','null','1','无','1','1','null','硬件信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','硬件信息','null'),(17,'storage','host','windows','String','null','1','无','1','1','null','存储信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','存储信息','null'),(18,'ipmac','host','windows','String','null','1','无','1','1','null','IPMAC信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','IPMAC信息','null'),(19,'utilhdx','host','windows','Number','null','1','Kb','1','1','null','网卡流速超过阀值','null','1','5','m','null',12000,24000,36000,3,3,3,1,1,1,'null','网卡流速信息','null'),(20,'software','host','windows','String','null','1','无','1','1','null','安装的软件信息发生变更','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','安装的软件信息','null'),(21,'systemgroup','host','windows','String','null','1','无','1','1','null','系统组信息改变','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','系统组信息','null'),(22,'ping','host','windows','Number','null','1','%','0','1','null','ping不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(23,'cpu','net','cisco','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(24,'ping','net','cisco','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(25,'memory','net','cisco','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(26,'flash','net','cisco','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(27,'temperature','net','cisco','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(28,'fan','net','cisco','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(29,'power','net','cisco','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(30,'voltage','net','cisco','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(31,'systemgroup','net','cisco','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(32,'ipmac','net','cisco','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(33,'fdb','net','cisco','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(34,'router','net','cisco','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(35,'interface','net','cisco','String',NULL,'1',' ','2','1',NULL,'接口信息异常',NULL,'1',NULL,NULL,NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(36,'utilhdx','net','cisco','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(37,'packs','net','cisco','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(38,'discardsperc','net','cisco','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(39,'errorsperc','net','cisco','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(40,'inpacks','net','cisco','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(41,'outpacks','net','cisco','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(42,'cpu','net','h3c','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(43,'ping','net','h3c','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(44,'memory','net','h3c','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(45,'flash','net','h3c','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(46,'temperature','net','h3c','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(47,'fan','net','h3c','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(48,'power','net','h3c','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(49,'voltage','net','h3c','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(50,'systemgroup','net','h3c','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(51,'ipmac','net','h3c','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(52,'fdb','net','h3c','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(53,'router','net','h3c','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(54,'interface','net','h3c','String',NULL,'1',' ','2','1',NULL,'接口信息异常',NULL,'1',NULL,NULL,NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(55,'utilhdx','net','h3c','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(56,'packs','net','h3c','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(57,'discardsperc','net','h3c','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(58,'errorsperc','net','h3c','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(59,'inpacks','net','h3c','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(60,'outpacks','net','h3c','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(75,'cpu','host','linux','Number','1.3.6.1.4.1.311.1.1.3.1.1','1','%','1','1',NULL,'cpu利用率超过阀值',NULL,'1','5','m',NULL,80,90,95,3,3,3,1,1,1,NULL,'cpu利用率',NULL),(76,'diskperc','host','linux','Number','null','1','%','1','1','null','文件系统利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','文件系统信息','null'),(77,'service','host','linux','String','null','1','无','1','1','null','服务丢失','null','1','5','m','null',1,1,1,3,3,3,1,1,1,'null','服务','null'),(78,'physicalmemory','host','linux','Number','null','1','%','1','1','null','物理内存超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','物理内存','null'),(79,'virtualmemory','host','linux','Number','null','1','%','1','1','null','交换内存超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','交换内存','null'),(80,'process','host','linux','String','null','1','无','1','1','null','进程丢失','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','进程','null'),(81,'hardware','host','linux','String','null','1','无','1','1','null','硬件信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','硬件信息','null'),(82,'storage','host','linux','String','null','1','无','1','1','null','存储信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','存储信息','null'),(83,'ipmac','host','linux','String','null','1','无','1','1','null','IPMAC信息发生变更','null','1','5','m','null',0,0,0,1,1,1,1,1,1,'null','IPMAC信息','null'),(84,'utilhdx','host','linux','Number','null','1','Kb','1','1','null','网卡流速超过阀值','null','1','5','m','null',12000,24000,36000,3,3,3,1,1,1,'null','网卡流速信息','null'),(85,'software','host','linux','String','null','1','无','1','1','null','安装的软件信息发生变更','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','安装的软件信息','null'),(86,'systemgroup','host','linux','String','null','1','无','1','1','null','系统组信息改变','null','1','5','m','null',0,0,0,3,3,3,1,1,1,'null','系统组信息','null'),(87,'ping','host','linux','Number','null','1','%','0','1','null','ping不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(88,'ping','host','aix','Number','null','1','%','0','1','null','PING不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性','null'),(89,'responsetime','host','aix','Number','null','1','ms','1','1','null','响应时间超过阀值','null','1','null','null','null',1000,2000,3000,3,3,3,1,1,1,'null','响应时间','null'),(90,'cpu','host','aix','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','CPU信息','null'),(91,'physicalmemory','host','aix','Number','null','1','%','1','1','null','物理内存利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','物理内存','null'),(92,'swapmemory','host','aix','Number','null','1','%','1','1','null','交换内存利用率超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','交换内存','null'),(93,'diskperc','host','aix','Number','null','1','%','1','1','null','文件系统利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','文件系统利用率','null'),(94,'diskinc','host','aix','Number','null','1','%','1','1','null','磁盘增长率超过阀值','null','1','5','m','null',1,2,3,3,3,3,1,1,1,'null','磁盘增长率','null'),(95,'diskbusy','host','aix','Number','null','1','%','1','1','null','磁盘繁忙程度超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','磁盘繁忙程度','null'),(96,'interface','host','aix','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(97,'utilhdx','host','aix','Number','null','1','kb','1','1','null','端口流速超过阀值','null','1','5','m','null',12000,24000,36000,3,3,3,1,1,1,'null','端口流速','null'),(98,'process','host','aix','String','null','1','无','1','1','null','进程异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','进程信息','null'),(99,'avque','host','aix','Number','null','1','个','1','1','null','I/0平均请求数超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','I/0平均请求数','null'),(100,'avwait','host','aix','Number','null','1','ms','1','1','null','I/O平均等待时间超过阀值','null','1','5','m','null',5,6,7,3,3,3,1,1,1,'null','I/O平均等待时间','null'),(101,'avserv','host','aix','Number','null','1','ms','1','1','null','I/O平均完成时间超过阀值','null','1','5','m','null',5,6,7,3,3,3,1,1,1,'null','I/O平均完成时间','null'),(102,'AllInBandwidthUtilHdx','net','cisco','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(103,'AllOutBandwidthUtilHdx','net','cisco','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(104,'AllInBandwidthUtilHdx','net','h3c','Number','null','1','kb/秒','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(105,'AllOutBandwidthUtilHdx','net','h3c','Number','null','1','kb/秒','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(106,'AllInBandwidthUtilHdx','host','windows','Number','null','1','kb/秒','1','1','null','入口流速超过阀值','null','1','5','m','null',10000,20000,30000,3,3,3,1,1,1,'null','入口流速','null'),(107,'AllOutBandwidthUtilHdx','host','windows','Number','null','1','kb/秒','1','1','null','出口流速超过阀值','null','1','5','m','null',10000,20000,30000,3,3,3,1,1,1,'null','出口流速','null'),(108,'AllInBandwidthUtilHdx','host','linux','Number','null','1','kb/秒','1','1','null','入口流速超过阀值','null','1','5','m','null',10000,20000,30000,3,3,3,1,1,1,'null','入口流速','null'),(109,'AllOutBandwidthUtilHdx','host','linux','Number','null','1','kb/秒','1','1','null','出口流速超过阀值','null','0','5','m','null',10000,20000,30000,3,3,3,1,1,0,'null','出口流速','null'),(110,'AllInBandwidthUtilHdx','host','aix','Number','null','1','kb/秒','1','1','null','入口流速超过阀值','null','1','5','m','null',10000,26000,30000,3,3,3,1,1,1,'null','入口流速','null'),(111,'AllOutBandwidthUtilHdx','host','aix','Number','null','1','kb/秒','1','1','null','出口流速超过阀值','null','1','5','m','null',10000,26000,30000,3,3,3,1,1,1,'null','出口流速','null'),(112,'interface','host','windows','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(113,'cpu','net','northtel','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(114,'ping','net','northtel','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(115,'memory','net','northtel','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(116,'flash','net','northtel','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(117,'temperature','net','northtel','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(118,'fan','net','northtel','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(119,'power','net','northtel','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(120,'voltage','net','northtel','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(121,'systemgroup','net','northtel','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(122,'ipmac','net','northtel','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(123,'fdb','net','northtel','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(124,'router','net','northtel','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(125,'interface','net','northtel','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(126,'utilhdx','net','northtel','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(127,'packs','net','northtel','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(128,'discardsperc','net','northtel','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(129,'errorsperc','net','northtel','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(130,'inpacks','net','northtel','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(131,'outpacks','net','northtel','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(132,'cpu','net','maipu','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(133,'ping','net','maipu','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(134,'memory','net','maipu','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(135,'flash','net','maipu','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(136,'temperature','net','maipu','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(137,'fan','net','maipu','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(138,'power','net','maipu','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(139,'voltage','net','maipu','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(140,'systemgroup','net','maipu','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(141,'ipmac','net','maipu','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(142,'fdb','net','maipu','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(143,'router','net','maipu','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(144,'interface','net','maipu','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(145,'utilhdx','net','maipu','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(146,'packs','net','maipu','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(147,'discardsperc','net','maipu','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(148,'errorsperc','net','maipu','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(149,'inpacks','net','maipu','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(150,'outpacks','net','maipu','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(151,'cpu','net','radware','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(152,'ping','net','radware','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(153,'memory','net','radware','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(154,'flash','net','radware','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(155,'temperature','net','radware','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(156,'fan','net','radware','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(157,'power','net','radware','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(158,'voltage','net','radware','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(159,'systemgroup','net','radware','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(160,'ipmac','net','radware','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(161,'fdb','net','radware','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(162,'router','net','radware','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(163,'interface','net','radware','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(164,'utilhdx','net','radware','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(165,'packs','net','radware','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(166,'discardsperc','net','radware','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(167,'errorsperc','net','radware','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(168,'inpacks','net','radware','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(169,'outpacks','net','radware','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(170,'cpu','net','redgiant','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(171,'ping','net','redgiant','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(172,'memory','net','redgiant','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(173,'flash','net','redgiant','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(174,'temperature','net','redgiant','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(175,'fan','net','redgiant','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(176,'power','net','redgiant','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(177,'voltage','net','redgiant','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(178,'systemgroup','net','redgiant','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(179,'ipmac','net','redgiant','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(180,'fdb','net','redgiant','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(181,'router','net','redgiant','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(182,'interface','net','redgiant','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(183,'utilhdx','net','redgiant','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(184,'packs','net','redgiant','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(185,'discardsperc','net','redgiant','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(186,'errorsperc','net','redgiant','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(187,'inpacks','net','redgiant','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(188,'outpacks','net','redgiant','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(189,'cpu','net','zte','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(190,'ping','net','zte','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(191,'memory','net','zte','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(192,'flash','net','zte','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(193,'temperature','net','zte','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(194,'fan','net','zte','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(195,'power','net','zte','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(196,'voltage','net','zte','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(197,'systemgroup','net','zte','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(198,'ipmac','net','zte','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(199,'fdb','net','zte','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(200,'router','net','zte','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(201,'interface','net','zte','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(202,'utilhdx','net','zte','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(203,'packs','net','zte','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(204,'discardsperc','net','zte','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(205,'errorsperc','net','zte','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(206,'inpacks','net','zte','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(207,'outpacks','net','zte','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(208,'cpu','net','bdcom','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(209,'ping','net','bdcom','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(210,'memory','net','bdcom','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(211,'flash','net','bdcom','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(212,'temperature','net','bdcom','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(213,'fan','net','bdcom','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(214,'power','net','bdcom','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(215,'voltage','net','bdcom','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(216,'systemgroup','net','bdcom','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(217,'ipmac','net','bdcom','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(218,'fdb','net','bdcom','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(219,'router','net','bdcom','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(220,'interface','net','bdcom','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(221,'utilhdx','net','bdcom','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(222,'packs','net','bdcom','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(223,'discardsperc','net','bdcom','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(224,'errorsperc','net','bdcom','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(225,'inpacks','net','bdcom','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(226,'outpacks','net','bdcom','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(227,'cpu','net','digitalchina','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(228,'ping','net','digitalchina','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(229,'memory','net','digitalchina','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(230,'flash','net','digitalchina','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(231,'temperature','net','digitalchina','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(232,'fan','net','digitalchina','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(233,'power','net','digitalchina','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(234,'voltage','net','digitalchina','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(235,'systemgroup','net','digitalchina','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(236,'ipmac','net','digitalchina','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(237,'fdb','net','digitalchina','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(238,'router','net','digitalchina','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(239,'interface','net','digitalchina','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(240,'utilhdx','net','digitalchina','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(241,'packs','net','digitalchina','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(242,'discardsperc','net','digitalchina','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(243,'errorsperc','net','digitalchina','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(244,'inpacks','net','digitalchina','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(245,'outpacks','net','digitalchina','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(246,'cpu','net','dlink','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(247,'ping','net','dlink','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(248,'memory','net','dlink','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(249,'flash','net','dlink','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(250,'temperature','net','dlink','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(251,'fan','net','dlink','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(252,'power','net','dlink','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(253,'voltage','net','dlink','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(254,'systemgroup','net','dlink','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(255,'ipmac','net','dlink','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(256,'fdb','net','dlink','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(257,'router','net','dlink','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(258,'interface','net','dlink','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(259,'utilhdx','net','dlink','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(260,'packs','net','dlink','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(261,'discardsperc','net','dlink','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(262,'errorsperc','net','dlink','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(263,'inpacks','net','dlink','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(264,'outpacks','net','dlink','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(265,'cpu','net','enterasys','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(266,'ping','net','enterasys','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(267,'memory','net','enterasys','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(268,'flash','net','enterasys','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(269,'temperature','net','enterasys','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(270,'fan','net','enterasys','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(271,'power','net','enterasys','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(272,'voltage','net','enterasys','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(273,'systemgroup','net','enterasys','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(274,'ipmac','net','enterasys','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(275,'fdb','net','enterasys','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(276,'router','net','enterasys','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(277,'interface','net','enterasys','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(278,'utilhdx','net','enterasys','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(279,'packs','net','enterasys','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(280,'discardsperc','net','enterasys','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(281,'errorsperc','net','enterasys','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(282,'inpacks','net','enterasys','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(283,'outpacks','net','enterasys','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(284,'cpu','net','harbour','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(285,'ping','net','harbour','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(286,'memory','net','harbour','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(287,'flash','net','harbour','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(288,'temperature','net','harbour','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(289,'fan','net','harbour','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(290,'power','net','harbour','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(291,'voltage','net','harbour','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(292,'systemgroup','net','harbour','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(293,'ipmac','net','harbour','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(294,'fdb','net','harbour','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(295,'router','net','harbour','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(296,'interface','net','harbour','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(297,'utilhdx','net','harbour','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(298,'packs','net','harbour','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(299,'discardsperc','net','harbour','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(300,'errorsperc','net','harbour','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(301,'inpacks','net','harbour','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(302,'outpacks','net','harbour','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(303,'cpu','net','huawei','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(304,'ping','net','huawei','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(305,'memory','net','huawei','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(306,'flash','net','huawei','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(307,'temperature','net','huawei','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(308,'fan','net','huawei','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(309,'power','net','huawei','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(310,'voltage','net','huawei','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(311,'systemgroup','net','huawei','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(312,'ipmac','net','huawei','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(313,'fdb','net','huawei','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(314,'router','net','huawei','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(315,'interface','net','huawei','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(316,'utilhdx','net','huawei','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(317,'packs','net','huawei','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(318,'discardsperc','net','huawei','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(319,'errorsperc','net','huawei','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(320,'inpacks','net','huawei','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(321,'outpacks','net','huawei','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(322,'AllInBandwidthUtilHdx','net','bdcom','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(323,'AllOutBandwidthUtilHdx','net','bdcom','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(324,'AllInBandwidthUtilHdx','net','digitalchina.java','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(325,'AllOutBandwidthUtilHdx','net','digitalchina','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(326,'AllInBandwidthUtilHdx','net','dlink','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(327,'AllOutBandwidthUtilHdx','net','dlink','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(328,'AllInBandwidthUtilHdx','net','enterasys','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(329,'AllOutBandwidthUtilHdx','net','enterasys','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(330,'AllInBandwidthUtilHdx','net','harbour','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(331,'AllOutBandwidthUtilHdx','net','harbour','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(332,'AllInBandwidthUtilHdx','net','huawei','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(333,'AllOutBandwidthUtilHdx','net','huawei','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(334,'AllInBandwidthUtilHdx','net','maipu','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(335,'AllOutBandwidthUtilHdx','net','maipu','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(336,'AllInBandwidthUtilHdx','net','northtel','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(337,'AllOutBandwidthUtilHdx','net','northtel','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(338,'AllInBandwidthUtilHdx','net','radware','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(339,'AllOutBandwidthUtilHdx','net','radware','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(340,'AllInBandwidthUtilHdx','net','redgiant','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(341,'AllOutBandwidthUtilHdx','net','redgiant','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(342,'AllInBandwidthUtilHdx','net','zte','Number','null','1','kb/s','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null'),(343,'AllOutBandwidthUtilHdx','net','zte','Number','null','1','kb/s','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null'),(344,'ping','db','sqlserver','Number','null','1','%','0','1','null','连通率低于阀值','null','1','null','null','null',30,20,10,3,3,1,1,1,1,'null','连通率','null'),(345,'jobnumber','host','as400','Number','null','1','个','1','1','null','作业数量超过阀值','null','1','null','null','null',100,200,300,3,3,3,1,1,1,'null','作业数量','null'),(346,'ping','host','as400','Number','null','1','%','0','1','null','PING不通','null','1','null','null','null',90,80,70,3,3,3,1,1,1,'null','可用性','null'),(347,'cpu','host','as400','Number','null','1','%','1','1','null','CUP利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','CUP利用率','null'),(348,'diskperc','host','as400','Number','null','1','%','1','1','null','磁盘利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','磁盘利用率','null'),(349,'ping','host','solaris','Number','null','1','%','0','1','null','PING不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','连通率','null'),(350,'cpu','host','solaris','Number','null','1','%','1','1','null','CPU超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','CPU利用率','null'),(351,'physicalmemory','host','solaris','Number','null','1','%','1','1','null','物理内存利用率超过阀值','null','1','null','null','null',80,90,95,3,3,3,1,1,1,'null','物理内存','null'),(352,'swapmemory','host','solaris','Number','null','1','%','1','1','null','交换内存利用率超过阀值','null','1','null','null','null',80,90,90,3,3,3,1,1,1,'null','交换内存','null'),(353,'diskperc','host','solaris','Number','null','1','%','1','1','null','磁盘利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','磁盘利用率','null'),(354,'responsetime','host','solaris','Number','null','1','ms','1','1','null','响应时间超过阀值','null','1','null','null','null',1000,2000,3000,3,3,3,1,1,1,'null','响应时间','null'),(355,'pagingusage','host','aix','Number','null','1','%','1','1','null','换页率超过阀值','null','1','null','null','null',60,70,80,3,3,3,1,1,1,'null','换页率','null'),(356,'ping','middleware','was','Number','null','1','%','0','1','null','不可用','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性','null'),(357,'send','service','mail','String','null','1',' ','1','1','null','邮件发送服务故障','null','0','null','null','null',0,0,0,3,3,3,1,1,1,'null','send','null'),(358,'receieve','service','mail','String','null','1',' ','1','1','null','邮件接收服务故障','null','1','null','null','null',0,1,2,3,3,3,1,1,1,'null','receieve','null'),(359,'upload','service','ftp','String','null','1','无','1','1','null','上载文件服务故障','null','1','null','null','null',0,0,0,1,1,1,1,1,1,'null','upload','null'),(360,'download','service','ftp','String','null','1','无','1','1','null','下载文件故障','null','1','null','null','null',0,0,0,1,1,1,1,1,1,'null','download','null'),(361,'socketping','service','socket','String','null','1',' ','1','1','null','Socket端口连接故障','null','1','null','null','null',0,0,0,1,1,1,1,1,1,'null','socketping','null'),(362,'iowait','host','aix','Number','null','1','%','1','1','null','I/O等待时间百分比超过阀值','null','1','null','null','null',40,50,60,3,3,3,1,1,1,'null','I/O等待时间百分比','null'),(363,'webping','service','url','Number',NULL,'1','%','0','1',NULL,'连通故障',NULL,'1',NULL,NULL,NULL,30,20,10,1,1,1,1,1,1,NULL,'ping',NULL),(364,'webresponsetime','service','url','Number',NULL,'1','ms','1','1',NULL,'响应时间超过阀值',NULL,'1',NULL,NULL,NULL,5000,6000,7000,3,3,3,1,1,1,NULL,'responsetime',NULL),(365,'webpagesize','service','url','String',NULL,'1',' ','1','1',NULL,'页面大小小于阀值',NULL,'1',NULL,NULL,NULL,0,0,0,1,1,1,1,1,1,NULL,'pagesize',NULL),(366,'webkeyword','service','url','Number','null','1','%','1','1','null','关键字被修改率超过阀值','null','1','null','null','null',70,80,90,1,1,1,1,1,1,'null','关键字','null'),(367,'ping','net','atm','Number','null','1','%','0','1','null','连通率超过阀值','null','1','null','null','null',30,20,10,1,1,1,1,1,1,'null','连通率','null'),(368,'droprate','net','cisco','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','null','null','null',10,30,50,3,3,3,1,1,1,'null','丢包率','null'),(369,'dropbytes','net','cisco','Number','null','1','kb','1','1','null','丢包数超过阀值','null','0','null','null','null',5000,10000,15000,3,3,3,1,0,1,'null','丢包数','null'),(370,'matches','net','cisco','Number','null','1','次','1','1','null','匹配差值超过阀值','null','1','null','null','null',10000,20000,30000,0,0,0,0,0,0,'null','匹配差值','null'),(371,'matches','net','h3c','Number','null','1','次','1','1','null','匹配差值超过阀值','null','1','null','null','null',10000,20000,30000,0,0,0,0,0,0,'null','匹配差值','null'),(372,'ping','host','hpunix','Number','null','1','%','0','1','null','PING不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性','null'),(373,'responsetime','hpunix','aix','Number','null','1','ms','1','1','null','响应时间超过阀值','null','1','null','null','null',1000,2000,3000,3,3,3,1,1,1,'null','响应时间','null'),(374,'cpu','host','hpunix','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','CPU信息','null'),(375,'physicalmemory','host','hpunix','Number','null','1','%','1','1','null','物理内存利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','物理内存','null'),(376,'swapmemory','host','hpunix','Number','null','1','%','1','1','null','交换内存利用率超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','交换内存','null'),(377,'diskperc','host','hpunix','Number','null','1','%','1','1','null','文件系统利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','文件系统利用率','null'),(378,'diskinc','host','hpunix','Number','null','1','%','1','1','null','磁盘增长率超过阀值','null','1','5','m','null',1,2,3,3,3,3,1,1,1,'null','磁盘增长率','null'),(379,'diskbusy','host','hpunix','Number','null','1','%','1','1','null','磁盘繁忙程度超过阀值','null','1','5','m','null',70,80,90,3,3,3,1,1,1,'null','磁盘繁忙程度','null'),(380,'interface','host','hpunix','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(381,'utilhdx','host','hpunix','Number','null','1','kb','1','1','null','端口流速超过阀值','null','1','5','m','null',12000,24000,36000,3,3,3,1,1,1,'null','端口流速','null'),(382,'process','host','hpunix','String','null','1','无','1','1','null','进程异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','进程信息','null'),(383,'avque','host','hpunix','Number','null','1','个','1','1','null','I/0平均请求数超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','I/0平均请求数','null'),(384,'avwait','host','hpunix','Number','null','1','ms','1','1','null','I/O平均等待时间超过阀值','null','1','5','m','null',5,6,7,3,3,3,1,1,1,'null','I/O平均等待时间','null'),(385,'avserv','host','hpunix','Number','null','1','ms','1','1','null','I/O平均完成时间超过阀值','null','1','5','m','null',5,6,7,3,3,3,1,1,1,'null','I/O平均完成时间','null'),(386,'rtt','sla','icmp','Number','null','1','ms','1','1','null','','null','1','null','null','null',10,20,30,3,3,3,1,1,1,'null','sla-icmp-rtt','null'),(387,'rtt','sla','tcp','Number',NULL,'1','ms','1','1',NULL,'',NULL,'1',NULL,NULL,NULL,10,20,30,3,3,3,1,1,1,NULL,'sla-tcp-rtt',NULL),(388,'outpacks','net','redgiant','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(389,'cpu','net','hp','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(390,'ping','net','hp','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(391,'memory','net','hp','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(392,'flash','net','hp','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(393,'temperature','net','hp','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(394,'fan','net','hp','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(395,'power','net','hp','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(396,'voltage','net','hp','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(397,'systemgroup','net','hp','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(398,'ipmac','net','hp','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(399,'fdb','net','hp','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(400,'router','net','hp','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(401,'interface','net','hp','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(402,'utilhdx','net','hp','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(403,'packs','net','hp','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(404,'discardsperc','net','hp','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(405,'errorsperc','net','hp','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(406,'inpacks','net','hp','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(407,'outpacks','net','hp','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(408,'ping','firewall','venus','Number',NULL,'1','%','0','1',NULL,'PING 不通',NULL,'1',NULL,NULL,NULL,30,20,10,3,3,3,1,1,1,NULL,'可用性检测',NULL),(409,'cpu','firewall','venus','Number',NULL,'1','%','1','1',NULL,'CPU利用率超过阀值',NULL,'1',NULL,NULL,NULL,50,60,70,3,3,3,1,1,1,NULL,'CPU利用率',NULL),(410,'memory','firewall','venus','Number',NULL,'1','%','1','1',NULL,'内存利用率超过阀值',NULL,'1','5','m',NULL,40,50,60,3,3,3,1,1,1,NULL,'内存利用率',NULL),(411,'systemgroup','firewall','venus','String',NULL,'1','无','1','1',NULL,'系统组信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'系统组信息',NULL),(412,'ipmac','firewall','venus','String',NULL,'1','无','1','1',NULL,'IPMAC信息变更异常',NULL,'0','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'IPMAC信息',NULL),(413,'fdb','firewall','venus','String',NULL,'1','无','1','1',NULL,'FDB表信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'FDB表信息',NULL),(414,'router','firewall','venus','String',NULL,'1','无','1','1',NULL,'路由表信息',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'路由表',NULL),(415,'interface','firewall','venus','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(424,'ping','firewall','tippingpoint','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(425,'cpu','firewall','tippingpoint','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','null','null','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(426,'memory','firewall','tippingpoint','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(427,'systemgroup','firewall','tippingpoint','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(428,'ipmac','firewall','tippingpoint','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(429,'fdb','firewall','tippingpoint','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(430,'router','firewall','tippingpoint','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(431,'interface','firewall','tippingpoint','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(432,'ping','firewall','secworld','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(433,'cpu','firewall','secworld','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','null','null','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(434,'memory','firewall','secworld','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(435,'systemgroup','firewall','secworld','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(436,'ipmac','firewall','secworld','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(437,'fdb','firewall','secworld','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(438,'router','firewall','secworld','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(439,'interface','firewall','secworld','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(440,'fdb','net','hillstone','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null'),(441,'router','net','hillstone','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null'),(442,'interface','net','hillstone','String',NULL,'1','无','2','1',NULL,'接口信息异常',NULL,'1','5','m',NULL,0,0,0,0,0,0,1,1,1,NULL,'接口信息',NULL),(443,'utilhdx','net','hillstone','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null'),(444,'packs','net','hillstone','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null'),(445,'discardsperc','net','hillstone','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null'),(446,'errorsperc','net','hillstone','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null'),(447,'inpacks','net','hillstone','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null'),(448,'outpacks','net','hillstone','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null'),(449,'ipmac','net','hillstone','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null'),(450,'systemgroup','net','hillstone','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null'),(451,'voltage','net','hillstone','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null'),(452,'power','net','hillstone','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null'),(453,'fan','net','hillstone','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null'),(454,'temperature','net','hillstone','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null'),(455,'flash','net','hillstone','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null'),(456,'memory','net','hillstone','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null'),(457,'ping','net','hillstone','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null'),(458,'cpu','net','hillstone','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null'),(459,'ping','service','dhcp','Number','null','1','%','0','1','null','连通率超过阀值','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','连通率','null'),(460,'ping','db','db2','Number','null','1','%','0','1','null','连通率超过阀值','null','1','null','null','null',60,70,20,3,3,3,1,1,1,'null','ping','null'),(461,'tablespace','db','db2','Number','null','1','%','1','1','null','表空间超过阀值','null','1','null','null','null',70,80,90,1,1,1,1,1,1,'null','表空间','null'),(462,'tablespace','db','informix','Number','null','1','%','1','1','null','表空间超过阀值','null','1','null','null','null',70,80,90,1,1,1,1,1,1,'null','表空间','null'),(463,'ping','db','informix','Number','null','1','%','0','1','null','连通率超过阀值','null','1','null','null','null',50,30,20,1,1,1,1,1,1,'null','连通率','null'),(464,'linkperc','link','link','String','null','1','无','1','1','1','带宽占用率超过阀值','1','1','null','null','null',0,0,0,3,3,3,1,1,1,'null','带宽占用率','null'),(465,'speed','link','link','String ','null','1','无','1','1','1','流速超过阀值','1','1','null','null','null',0,0,0,3,3,3,1,1,1,'null','流速信息','null'),(466,'ping','host','scounixware','Number',NULL,'1','%','0','1',NULL,'不能连通',NULL,'1',NULL,NULL,NULL,30,20,10,3,3,3,1,1,1,NULL,'连通率',NULL),(467,'cpu','host','scounixware','Number',NULL,'1','%','1','1',NULL,'CPU利用率超过阀值',NULL,'0',NULL,NULL,NULL,70,80,90,3,3,3,1,1,1,NULL,'CPU利用率',NULL),(468,'memory','host','scounixware','Number',NULL,'1','%','1','1',NULL,'内存利用率超过阀值',NULL,'1',NULL,NULL,NULL,70,80,90,3,3,3,1,1,1,NULL,'memory',NULL),(469,'diskperc','host','scounixware','Number',NULL,'1','%','1','1',NULL,'磁盘利用率超过阀值',NULL,'1',NULL,NULL,NULL,80,90,95,1,1,1,1,1,1,NULL,'磁盘利用率',NULL),(470,'cpu','virtual','vmware','Number',NULL,'1','%','1','1',NULL,'VMWare物理机cpu利用率',NULL,'0',NULL,NULL,NULL,100,100,100,3,3,3,1,1,0,'physical','VMWare物理机cpu利用率',NULL),(471,'memtotal','virtual','vmware','Number',NULL,'1','MB','1','1',NULL,'VMWare群集内存已消耗',NULL,'0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'yun','VMWare群集内存已消耗',NULL),(472,'cpuuse','virtual','vmware','Number',NULL,'1','MB','1','1',NULL,'VMWare物理机cpu使用情况',NULL,'0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'physical','VMWare物理机cpu使用情况',NULL),(473,'mem','virtual','vmware','Number',NULL,'1','%','1','1',NULL,'VMWare物理机内存使用情况',NULL,'0',NULL,NULL,NULL,100,100,100,3,3,3,1,1,0,'physical','VMWare物理机内存使用情况',NULL),(474,'cpu','virtual','vmware','Number',NULL,'1','%','1','1','','VMWare虚拟机cpu利用率','','0',NULL,NULL,NULL,100,100,100,3,3,3,1,1,0,'vmware','VMWare虚拟机cpu利用率',NULL),(475,'cpuuse','virtual','vmware','Number',NULL,'1','MHz','1','1','','VMWare资源池cpu使用情况','','0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'resourcepool','VMWare资源池cpu使用情况',NULL),(476,'cpuuse','virtual','vmware','Number',NULL,'1','MHz','1','1','','VMWare群集cpu使用情况','','0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'yun','VMWare群集cpu使用情况',NULL),(477,'meminc','virtual','vmware','Number',NULL,'1','MB','1','1','','VMWare物理机内存虚拟增长','','0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'physical','VMWare物理机内存虚拟增长',NULL),(478,'memin','virtual','vmware','Number',NULL,'1','MBps','1','1','','VMWare物理机内存换入速率','','0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'physical','VMWare物理机内存换入速率',NULL),(479,'memout','virtual','vmware','Number',NULL,'1','KBps','1','1','','VMWare物理机内存换出速率','','0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'physical','VMWare物理机内存换出速率',NULL),(480,'disk','virtual','vmware','Number',NULL,'1','KBps','1','1','','VMWare物理机磁盘使用情况','','0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'physical','VMWare物理机磁盘使用情况',NULL),(482,'mem','virtual','vmware','Number',NULL,'1','%','1','1',NULL,'vmware虚拟机内存使用情况',NULL,'0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'vmware','vmware虚拟机内存使用情况',NULL),(483,'cpuuse','virtual','vmware','Number',NULL,'1','MB','1','1',NULL,'vmware虚拟机cpu使用情况',NULL,'0',NULL,NULL,NULL,100,100,100,3,3,3,1,1,0,'vmware','vmware虚拟机cpu使用情况',NULL),(484,'memin','virtual','vmware','Number',NULL,'1','KBps','1','1',NULL,'vmware虚拟机内存换入速率',NULL,'0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'vmware','vmware虚拟机内存换入速率',NULL),(485,'memout','virtual','vmware','Number',NULL,'1','KBps','1','1',NULL,'vmware虚拟机内存换出速率',NULL,'0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'vmware','vmware虚拟机内存换出速率',NULL),(486,'meminc','virtual','vmware','Number',NULL,'1','MB','1','1',NULL,'vmware虚拟机内存虚拟增长',NULL,'0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'vmware','vmware虚拟机内存虚拟增长',NULL),(487,'disk','virtual','vmware','Number',NULL,'1','KBps','1','1',NULL,'vmware虚拟机磁盘使用情况',NULL,'0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'vmware','vmware虚拟机磁盘使用情况',NULL),(488,'net','virtual','vmware','Number',NULL,'1','KBps','1','1',NULL,'vmware虚拟机网络使用情况',NULL,'0',NULL,NULL,NULL,10000,10000,10000,3,3,3,1,1,0,'vmware','vmware虚拟机网络使用情况',NULL),(489,'ping','host','scoopenserver','Number',NULL,'1','%','0','1',NULL,'连通率超过阀值',NULL,'1',NULL,NULL,NULL,30,20,10,3,3,3,1,1,1,'','连通率',NULL),(490,'cpu','host','scoopenserver','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'','CPU利用率','null'),(491,'physicalmemory','host','scoopenserver','Number','null','1','%','1','1','null','物理内存利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'','物理内存利用率','null'),(492,'diskperc','host','scoopenserver','Number','null','1','%','1','1','null','磁盘利用率超过阀值','null','1','null','null','null',70,80,90,3,3,3,1,1,1,'null','磁盘利用率','null'),(493,'ping','virtual','vmware','Number','null','1','%','0','1','null','连通率超过阀值','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','连通率','null');
/*!40000 ALTER TABLE `nms_alarm_indicators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_alarm_indicators_node`
--

DROP TABLE IF EXISTS `nms_alarm_indicators_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_alarm_indicators_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `subtype` varchar(255) DEFAULT NULL,
  `datatype` varchar(255) DEFAULT NULL,
  `moid` varchar(100) DEFAULT NULL,
  `threshold` varchar(255) DEFAULT NULL,
  `threshold_unit` varchar(10) DEFAULT NULL,
  `compare` varchar(10) DEFAULT '1',
  `compare_type` varchar(10) DEFAULT '1',
  `alarm_times` varchar(10) DEFAULT '1',
  `alarm_info` varchar(100) DEFAULT '',
  `alarm_level` varchar(10) DEFAULT '1',
  `enabled` varchar(10) DEFAULT '1',
  `poll_interval` varchar(10) DEFAULT NULL,
  `interval_unit` varchar(10) DEFAULT NULL,
  `subentity` varchar(10) DEFAULT NULL,
  `limenvalue0` bigint(20) DEFAULT NULL,
  `limenvalue1` bigint(20) DEFAULT NULL,
  `limenvalue2` bigint(20) DEFAULT NULL,
  `time0` int(3) DEFAULT NULL,
  `time1` int(3) DEFAULT NULL,
  `time2` int(3) DEFAULT NULL,
  `sms0` int(2) DEFAULT NULL,
  `sms1` int(2) DEFAULT NULL,
  `sms2` int(2) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `descr` varchar(50) DEFAULT NULL,
  `unit` varchar(20) DEFAULT NULL,
  `way0` varchar(20) DEFAULT NULL,
  `way1` varchar(20) DEFAULT NULL,
  `way2` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_alarm_indicators_node`
--

LOCK TABLES `nms_alarm_indicators_node` WRITE;
/*!40000 ALTER TABLE `nms_alarm_indicators_node` DISABLE KEYS */;
INSERT INTO `nms_alarm_indicators_node` VALUES (1,'53','cpu','net','h3c','Number','null','1','%','1','1','null','CPU利用率超过阀值','null','1','5','m','null',50,60,70,3,3,3,1,1,1,'null','CPU利用率','null','null','null','null'),(2,'53','ping','net','h3c','Number','null','1','%','0','1','null','PING 不通','null','1','null','null','null',30,20,10,3,3,3,1,1,1,'null','可用性检测','null','null','null','null'),(3,'53','memory','net','h3c','Number','null','1','%','1','1','null','内存利用率超过阀值','null','1','5','m','null',40,50,60,3,3,3,1,1,1,'null','内存利用率','null','null','null','null'),(4,'53','flash','net','h3c','Number','null','1','%','1','1','null','闪存利用率超过阀值','null','1','5','m','null',60,70,80,3,3,3,1,1,1,'null','闪存利用率','null','null','null','null'),(5,'53','temperature','net','h3c','Number','null','1','度','1','1','null','温度超过阀值','null','1','5','m','null',20,25,30,3,3,3,1,1,1,'null','温度','null','null','null','null'),(6,'53','fan','net','h3c','Number','null','1','无','1','1','null','风扇状态异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','风扇状态','null','null','null','null'),(7,'53','power','net','h3c','Number','null','1','无','1','1','null','电源模块异常','null','1','5','m','null',0,-1,-2,3,3,3,1,1,1,'null','电源模块','null','null','null','null'),(8,'53','voltage','net','h3c','Number','null','1','无','1','1','null','电压模块异常','null','1','5','m','null',0,2,3,3,3,3,1,1,1,'null','电压模块','null','null','null','null'),(9,'53','systemgroup','net','h3c','String','null','1','无','1','1','null','系统组信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','系统组信息','null','null','null','null'),(10,'53','ipmac','net','h3c','String','null','1','无','1','1','null','IPMAC信息变更异常','null','0','5','m','null',0,0,0,0,0,0,1,1,1,'null','IPMAC信息','null','null','null','null'),(11,'53','fdb','net','h3c','String','null','1','无','1','1','null','FDB表信息异常','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','FDB表信息','null','null','null','null'),(12,'53','router','net','h3c','String','null','1','无','1','1','null','路由表信息','null','1','5','m','null',0,0,0,0,0,0,1,1,1,'null','路由表','null','null','null','null'),(13,'53','interface','net','h3c','String','null','1',' ','2','1','null','接口信息异常','null','1','null','null','null',0,0,0,0,0,0,1,1,1,'null','接口信息','null','null','null','null'),(14,'53','utilhdx','net','h3c','Number','null','1','kb','1','1','null','流速信息超过阀值','null','1','5','m','null',120000,240000,360000,3,3,3,1,1,1,'null','流速信息','null','null','null','null'),(15,'53','packs','net','h3c','Number','null','1','个','1','1','null','数据包超过阀值','null','1','5','m','null',1000000,2000000,3000000,3,3,3,1,1,1,'null','数据包','null','null','null','null'),(16,'53','discardsperc','net','h3c','Number','null','1','%','1','1','null','丢包率超过阀值','null','1','5','m','null',5,10,15,3,3,3,1,1,1,'null','丢包率','null','null','null','null'),(17,'53','errorsperc','net','h3c','Number','null','1','%','1','1','null','错误率超过阀值','null','0','5','m','null',2,5,10,3,3,3,1,1,1,'null','错误率','null','null','null','null'),(18,'53','inpacks','net','h3c','Number','null','1','个','1','1','null','入口数据包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','入口数据包','null','null','null','null'),(19,'53','outpacks','net','h3c','Number','null','1','个','1','1','null','出口数据库包超过阀值','null','1','5','m','null',5000,10000,15000,3,3,3,1,1,1,'null','出口数据库包','null','null','null','null'),(20,'53','AllInBandwidthUtilHdx','net','h3c','Number','null','1','kb/秒','1','1','null','入口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','入口流速','null','null','null','null'),(21,'53','AllOutBandwidthUtilHdx','net','h3c','Number','null','1','kb/秒','1','1','null','出口流速超过阀值','null','1','5','m','null',100000,200000,300000,3,3,3,1,1,1,'null','出口流速','null','null','null','null'),(22,'53','matches','net','h3c','Number','null','1','次','1','1','null','匹配差值超过阀值','null','1','null','null','null',10000,20000,30000,0,0,0,0,0,0,'null','匹配差值','null','null','null','null');
/*!40000 ALTER TABLE `nms_alarm_indicators_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_alarm_message`
--

DROP TABLE IF EXISTS `nms_alarm_message`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_alarm_message` (
  `id` varchar(15) DEFAULT NULL,
  `ip_address` varchar(15) DEFAULT NULL,
  `message` varchar(100) DEFAULT NULL,
  `alarm_level` tinyint(1) DEFAULT NULL,
  `category` tinyint(3) DEFAULT NULL,
  `log_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_alarm_message`
--

LOCK TABLES `nms_alarm_message` WRITE;
/*!40000 ALTER TABLE `nms_alarm_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_alarm_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_alarm_port_node`
--

DROP TABLE IF EXISTS `nms_alarm_port_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_alarm_port_node` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `portindex` bigint(20) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `subtype` varchar(50) DEFAULT NULL,
  `enabled` varchar(10) DEFAULT NULL,
  `compare` int(2) DEFAULT NULL,
  `levelinvalue1` bigint(20) DEFAULT NULL,
  `levelinvalue2` bigint(20) DEFAULT NULL,
  `levelinvalue3` bigint(20) DEFAULT NULL,
  `leveloutvalue1` bigint(20) DEFAULT NULL,
  `leveloutvalue2` bigint(20) DEFAULT NULL,
  `leveloutvalue3` bigint(20) DEFAULT NULL,
  `levelintimes1` int(3) DEFAULT NULL,
  `levelintimes2` int(3) DEFAULT NULL,
  `levelintimes3` int(3) DEFAULT NULL,
  `levelouttimes1` int(3) DEFAULT NULL,
  `levelouttimes2` int(3) DEFAULT NULL,
  `levelouttimes3` int(3) DEFAULT NULL,
  `smsin1` int(2) DEFAULT NULL,
  `smsin2` int(2) DEFAULT NULL,
  `smsin3` int(2) DEFAULT NULL,
  `smsout1` int(2) DEFAULT NULL,
  `smsout2` int(2) DEFAULT NULL,
  `smsout3` int(2) DEFAULT NULL,
  `alarm_info` varchar(200) DEFAULT NULL,
  `wayin1` varchar(20) DEFAULT NULL,
  `wayin2` varchar(20) DEFAULT NULL,
  `wayin3` varchar(20) DEFAULT NULL,
  `wayout1` varchar(20) DEFAULT NULL,
  `wayout2` varchar(20) DEFAULT NULL,
  `wayout3` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_alarm_port_node`
--

LOCK TABLES `nms_alarm_port_node` WRITE;
/*!40000 ALTER TABLE `nms_alarm_port_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_alarm_port_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_alarm_way`
--

DROP TABLE IF EXISTS `nms_alarm_way`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_alarm_way` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `description` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `is_default` varchar(2) CHARACTER SET gb2312 DEFAULT NULL,
  `is_page_alarm` varchar(2) CHARACTER SET gb2312 DEFAULT NULL,
  `is_sound_alarm` varchar(2) CHARACTER SET gb2312 DEFAULT NULL,
  `is_mail_alarm` varchar(2) CHARACTER SET gb2312 DEFAULT NULL,
  `is_phone_alarm` varchar(2) CHARACTER SET gb2312 DEFAULT NULL,
  `is_sms_alarm` varchar(2) CHARACTER SET gb2312 DEFAULT NULL,
  `is_desktop_alarm` varchar(2) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_alarm_way`
--

LOCK TABLES `nms_alarm_way` WRITE;
/*!40000 ALTER TABLE `nms_alarm_way` DISABLE KEYS */;
INSERT INTO `nms_alarm_way` VALUES (4,'默认告警方式','','1','0','0','1','0','1','1');
/*!40000 ALTER TABLE `nms_alarm_way` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_alarm_way_detail`
--

DROP TABLE IF EXISTS `nms_alarm_way_detail`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_alarm_way_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `alarm_way_id` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `alarm_category` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `date_type` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `send_times` varchar(20) CHARACTER SET gb2312 DEFAULT NULL,
  `start_date` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `end_date` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `start_time` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `end_time` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `user_ids` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_alarm_way_detail`
--

LOCK TABLES `nms_alarm_way_detail` WRITE;
/*!40000 ALTER TABLE `nms_alarm_way_detail` DISABLE KEYS */;
INSERT INTO `nms_alarm_way_detail` VALUES (32,'4','mail','month','2','1','31','0','23','7,6,4,1'),(33,'4','desktop','month','3','1','31','0','23','7,6,4,1'),(35,'4','sms','month','3','1','31','0','23','7,6,4,1');
/*!40000 ALTER TABLE `nms_alarm_way_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_apache_history`
--

DROP TABLE IF EXISTS `nms_apache_history`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_apache_history` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `apache_id` int(10) DEFAULT NULL,
  `is_canconnected` int(10) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `mon_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_apache_history`
--

LOCK TABLES `nms_apache_history` WRITE;
/*!40000 ALTER TABLE `nms_apache_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_apache_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:09:55
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_apache_realtime`
--

DROP TABLE IF EXISTS `nms_apache_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_apache_realtime` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `apache_id` int(10) DEFAULT NULL,
  `is_canconnected` int(10) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `mon_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sms_sign` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_apache_realtime`
--

LOCK TABLES `nms_apache_realtime` WRITE;
/*!40000 ALTER TABLE `nms_apache_realtime` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_apache_realtime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_apache_temp`
--

DROP TABLE IF EXISTS `nms_apache_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_apache_temp` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(15) CHARACTER SET gb2312 DEFAULT NULL,
  `entity` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `value` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_apache_temp`
--

LOCK TABLES `nms_apache_temp` WRITE;
/*!40000 ALTER TABLE `nms_apache_temp` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_apache_temp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_apacheconfig`
--

DROP TABLE IF EXISTS `nms_apacheconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_apacheconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `alias` varchar(100) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(50) DEFAULT NULL,
  `port` int(20) DEFAULT NULL,
  `flag` int(10) DEFAULT NULL,
  `sendmobiles` varchar(100) DEFAULT NULL,
  `sendemail` varchar(100) DEFAULT NULL,
  `sendphone` varchar(100) DEFAULT NULL,
  `netid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_apacheconfig`
--

LOCK TABLES `nms_apacheconfig` WRITE;
/*!40000 ALTER TABLE `nms_apacheconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_apacheconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_buscolltype`
--

DROP TABLE IF EXISTS `nms_buscolltype`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_buscolltype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `collecttype` varchar(50) DEFAULT NULL,
  `bct_desc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_buscolltype`
--

LOCK TABLES `nms_buscolltype` WRITE;
/*!40000 ALTER TABLE `nms_buscolltype` DISABLE KEYS */;
INSERT INTO `nms_buscolltype` VALUES (1,'http','HTTP接口数据采集');
/*!40000 ALTER TABLE `nms_buscolltype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_businessnode`
--

DROP TABLE IF EXISTS `nms_businessnode`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_businessnode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bid` int(11) DEFAULT NULL,
  `bn_desc` varchar(50) DEFAULT NULL,
  `collecttype` int(2) DEFAULT NULL,
  `method` varchar(200) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `flag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_businessnode`
--

LOCK TABLES `nms_businessnode` WRITE;
/*!40000 ALTER TABLE `nms_businessnode` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_businessnode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_cabinet_config`
--

DROP TABLE IF EXISTS `nms_cabinet_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_cabinet_config` (
  `id` bigint(11) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `machinex` varchar(20) DEFAULT NULL,
  `machiney` varchar(20) DEFAULT NULL,
  `machinez` varchar(20) DEFAULT NULL,
  `uselect` varchar(10) DEFAULT NULL,
  `motorroom` varchar(11) DEFAULT NULL,
  `standards` varchar(100) DEFAULT NULL,
  `powers` varchar(5) DEFAULT NULL,
  `heights` varchar(5) DEFAULT NULL,
  `widths` varchar(5) DEFAULT NULL,
  `depths` varchar(5) DEFAULT NULL,
  `nos` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_cabinet_config`
--

LOCK TABLES `nms_cabinet_config` WRITE;
/*!40000 ALTER TABLE `nms_cabinet_config` DISABLE KEYS */;
INSERT INTO `nms_cabinet_config` VALUES (18,'01','-10','-10','-10','42','3','35','2','200','30','40','IDC-001'),(19,'002','30','30','-30','42','4','34','4','23','33','33','33');
/*!40000 ALTER TABLE `nms_cabinet_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_cabinet_equipments`
--

DROP TABLE IF EXISTS `nms_cabinet_equipments`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_cabinet_equipments` (
  `id` int(11) NOT NULL,
  `cabinetid` int(11) NOT NULL,
  `nodeid` int(11) DEFAULT NULL,
  `nodename` varchar(50) DEFAULT NULL,
  `nodedescr` varchar(200) DEFAULT NULL,
  `unumbers` varchar(50) DEFAULT NULL,
  `businessName` varchar(100) DEFAULT NULL,
  `businessid` int(11) DEFAULT NULL,
  `operid` int(11) DEFAULT NULL,
  `contactname` varchar(50) DEFAULT NULL,
  `contactphone` varchar(50) DEFAULT NULL,
  `contactemail` varchar(100) DEFAULT NULL,
  `roomid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_cabinet_equipments`
--

LOCK TABLES `nms_cabinet_equipments` WRITE;
/*!40000 ALTER TABLE `nms_cabinet_equipments` DISABLE KEYS */;
INSERT INTO `nms_cabinet_equipments` VALUES (1,18,14,'10.10.1.1','HX_7506A','1,2','OA业务系统',1,1,'hukelei','13811372044','hukelei@dhcc.com.cn',3),(2,18,15,'10.10.1.2','HX_7506B_1','7,8','ERP业务系统',2,1,'hukelei','13811372044','hukelei@dhcc.com.cn',3);
/*!40000 ALTER TABLE `nms_cabinet_equipments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_cabinet_guest`
--

DROP TABLE IF EXISTS `nms_cabinet_guest`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_cabinet_guest` (
  `id` int(11) NOT NULL,
  `cabinetid` int(11) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `unit` varchar(200) DEFAULT NULL,
  `phone` varchar(64) DEFAULT NULL,
  `mail` varchar(96) DEFAULT NULL,
  `inTime` varchar(64) DEFAULT NULL,
  `outTime` varchar(64) DEFAULT NULL,
  `dotime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `reason` varchar(255) DEFAULT NULL,
  `audits` varchar(20) DEFAULT NULL,
  `bak` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_cabinet_guest`
--

LOCK TABLES `nms_cabinet_guest` WRITE;
/*!40000 ALTER TABLE `nms_cabinet_guest` DISABLE KEYS */;
INSERT INTO `nms_cabinet_guest` VALUES (1,1,'胡可磊','东华软件','13811372044','hukelei@dhcc.com.cn','2011-12-26 16:19:45','2011-12-26 19:19:45','2011-12-26 08:21:44','测试服务器','系统管理员','出门时需要提交接见人签字单据!');
/*!40000 ALTER TABLE `nms_cabinet_guest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_cabinet_law`
--

DROP TABLE IF EXISTS `nms_cabinet_law`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_cabinet_law` (
  `id` bigint(11) NOT NULL,
  `userid` int(10) DEFAULT NULL,
  `cabinetid` int(10) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `filename` varchar(200) DEFAULT NULL,
  `dotime` timestamp NULL DEFAULT NULL COMMENT 'c',
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_cabinet_law`
--

LOCK TABLES `nms_cabinet_law` WRITE;
/*!40000 ALTER TABLE `nms_cabinet_law` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_cabinet_law` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_cabinet_video`
--

DROP TABLE IF EXISTS `nms_cabinet_video`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_cabinet_video` (
  `id` bigint(11) NOT NULL,
  `userid` int(10) DEFAULT NULL,
  `cabinetid` int(10) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `filename` varchar(200) DEFAULT NULL,
  `dotime` timestamp NULL DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_cabinet_video`
--

LOCK TABLES `nms_cabinet_video` WRITE;
/*!40000 ALTER TABLE `nms_cabinet_video` DISABLE KEYS */;
INSERT INTO `nms_cabinet_video` VALUES (1,4,1,'机房出入规定','video.flv','2012-02-07 07:41:30','机房出入规定');
/*!40000 ALTER TABLE `nms_cabinet_video` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:09:56
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_connecttypeconfig`
--

DROP TABLE IF EXISTS `nms_connecttypeconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_connecttypeconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(11) DEFAULT NULL,
  `connecttype` varchar(20) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `login_prompt` varchar(50) DEFAULT NULL,
  `password_prompt` varchar(50) DEFAULT NULL,
  `shell_prompt` varchar(50) DEFAULT NULL,
  `flag` int(2) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_connecttypeconfig`
--

LOCK TABLES `nms_connecttypeconfig` WRITE;
/*!40000 ALTER TABLE `nms_connecttypeconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_connecttypeconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_cycle_report_config`
--

DROP TABLE IF EXISTS `nms_cycle_report_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_cycle_report_config` (
  `id` int(11) DEFAULT NULL,
  `reciever_id` varchar(20) DEFAULT NULL,
  `bid` varchar(20) DEFAULT NULL,
  `collection_of_device_id` varchar(50) DEFAULT NULL,
  `collection_of_generation_time` varchar(100) CHARACTER SET gb2312 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_cycle_report_config`
--

LOCK TABLES `nms_cycle_report_config` WRITE;
/*!40000 ALTER TABLE `nms_cycle_report_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_cycle_report_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_dbbackup`
--

DROP TABLE IF EXISTS `nms_dbbackup`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dbbackup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_dbbackup`
--

LOCK TABLES `nms_dbbackup` WRITE;
/*!40000 ALTER TABLE `nms_dbbackup` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_dbbackup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_dbbackup_auto`
--

DROP TABLE IF EXISTS `nms_dbbackup_auto`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dbbackup_auto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_dbbackup_auto`
--

LOCK TABLES `nms_dbbackup_auto` WRITE;
/*!40000 ALTER TABLE `nms_dbbackup_auto` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_dbbackup_auto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_device_type`
--

DROP TABLE IF EXISTS `nms_device_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_device_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sys_oid` varchar(100) DEFAULT NULL,
  `descr` varchar(100) DEFAULT NULL,
  `image` varchar(50) DEFAULT NULL,
  `producer` int(11) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `locate` varchar(100) DEFAULT NULL,
  `log_time` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_device_type`
--

LOCK TABLES `nms_device_type` WRITE;
/*!40000 ALTER TABLE `nms_device_type` DISABLE KEYS */;
INSERT INTO `nms_device_type` VALUES (1,'1.3.6.1.4.1.2011.2.23.31','S8500','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:14:04'),(2,'1.3.6.1.4.1.207.1.14.24','Allied Telesyn AT-SB4211 Control Blade','switch_router.gif',10,2,'齐鲁石化','2006-08-10 15:14:41'),(3,'1.3.6.1.4.1.2011.10.1.43','Quidway S5624P','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:15:09'),(4,'1.3.6.1.4.1.2011.2.23.33','S6506','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:15:41'),(5,'1.3.6.1.4.1.2011.2.23.32','S8505','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:16:13'),(6,'1.3.6.1.4.1.2011.2.23.38','Quidway S3552P','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:17:19'),(7,'1.3.6.1.4.1.207.1.14.22','Allied Telesyn AT-SB4211 Switch Control Card','switch_router.gif',10,2,'齐鲁石化','2006-08-10 15:17:45'),(8,'1.3.6.1.4.1.2011.2.23.67','Quidway S3552F','switch_router.gif',4,2,'齐鲁石化','2006-08-10 15:18:28'),(11,'1.3.6.1.4.1.43.10.27.4.1.2.2','3Com SuperStack II','switch_router.gif',8,2,'齐鲁石化','2006-08-10 15:25:13'),(12,'1.3.6.1.4.1.6296.1.2','3Com 3526','router.png',8,1,'齐鲁石化','2006-08-10 15:26:18'),(13,'1.3.6.1.4.1.311.1.1.3.1.3','Windows 2000 Server','win2k.png',1,4,'齐鲁石化','2006-08-10 15:30:32'),(14,'1.3.6.1.4.1.311.1.1.3.1.1','Windows Professional/XP','winxp.png',1,4,'齐鲁石化','2006-08-10 15:33:53'),(15,'1.3.6.1.4.1.311.1.1.3.1.2','Windows 2000 Server','win2k.png',1,4,'齐鲁石化','2006-08-10 15:34:17'),(16,'1.3.6.1.4.1.2021.250.10','Linux','linux.png',0,4,'齐鲁石化','2006-08-10 15:35:33'),(17,'1.3.6.1.4.1.8072.3.2.10','Redhat 9/Linux','linux.png',0,4,'齐鲁石化','2006-08-17 14:52:03'),(18,'1.3.6.1.4.1.2.3.1.2.1.1.2','IBM RS/6000','ibm.gif',3,4,'齐鲁石化','2006-08-10 15:37:34'),(19,'1.3.6.1.4.1.2.3.1.2.1.1.3','IBM PowerPC or RISC System','ibm.gif',3,4,'齐鲁石化','2006-08-10 15:38:49'),(20,'1.3.6.1.4.1.2.3.1.2.1.1.31','IBM','ibm.gif',3,4,'齐鲁石化','2006-08-10 15:39:31'),(21,'1.3.6.1.4.1.11.2.3.9.0','HP Jet-Direct Print Server','printer.png',2,5,'齐鲁石化','2006-08-10 15:39:31'),(22,'1.3.6.1.4.1.11.2.3.9.1','HP Jet-Direct Print Server','printer.png',2,5,'齐鲁石化','2006-08-10 15:43:40'),(23,'1.3.6.1.4.1.11.2.3.9.2','HP Jet-Direct Network Plotter','printer.png',2,5,'齐鲁石化','2006-08-10 15:44:57'),(24,'1.3.6.1.4.1.11.2.3.9.5','HP JetDirect J3113A','printer.png',2,5,'齐鲁石化','2006-08-10 15:45:27'),(25,'1.3.6.1.4.1.11.2.3.9.6','HP Jet-Direct Print Server','printer.png',2,5,'齐鲁石化','2006-08-10 15:45:50'),(26,'1.3.6.1.4.1.52.3.9.20.1.4','Enterasys X-Pedition 8600','switch_router.gif',7,2,'齐鲁石化','2006-08-10 15:47:56'),(28,'1.3.6.1.4.1.274.5.1','PictureTel 330 NetConference Multipoint Server','server.gif',12,0,'齐鲁石化','2006-08-26 10:14:32'),(32,'1.3.6.1.4.1.81.17.1.17','P330 Stackable Switch','switch.png',5,3,'齐鲁石化','2006-09-13 17:25:35'),(33,'1.3.6.1.4.1.81.17.1.18','P130 workgroup switch','switch.png',5,3,'齐鲁石化','2006-09-13 17:26:42'),(37,'1.3.6.1.4.1.5009.1.1.3','未知设备','other.gif',0,0,'齐鲁石化','2006-08-12 09:50:24'),(38,'1.3.7','未知设备','other.gif',0,0,'齐鲁石化','2006-08-12 09:50:48'),(39,'1.3.6.1.4.1.4526.1.10','未知设备','other.gif',0,0,'齐鲁石化','2006-08-12 09:51:16'),(40,'1.3.6.1.4.1.3742.25.1.1.1.1','24 + 2G Ethernet Switch','switch.png',0,3,'齐鲁石化','2006-09-13 17:22:44'),(41,'1.3.6.1.4.1.11.2.3.10.1.2','HP Solaris Sparc agent','other.gif',2,0,'齐鲁石化','2006-08-12 09:52:10'),(43,'1.3.6.1.4.1.11.2.3.10.1.1','HP SunOS Sparc agent','other.gif',2,0,'齐鲁石化','2006-08-12 09:53:54'),(44,'1.3.6.1.4.1.42.2.1.1','Sun Microsystems SunOS','server.gif',9,4,'齐鲁石化','2006-08-14 14:54:31'),(45,'1.3.6.1.4.1.9.1.186','Cisco 2611','router.png',6,1,'齐鲁石化','2006-08-14 14:54:21'),(46,'1.3.6.1.4.1.442.1.1.1.9.0','PEER Networks, a division of BMC Software, Inc., OptiMaster Release 1.9 on MS-Windows','other.gif',0,0,'齐鲁石化','2006-08-14 17:56:07'),(47,'1.3.6.1.4.1.11.1','未知设备','other.gif',2,0,'齐鲁石化','2006-08-20 17:10:55'),(48,'1.3.6.1.4.1.11.2.3.2.2','HP-UX HP 9000/300','server.gif',2,4,'齐鲁石化','2006-08-20 17:11:34'),(49,'1.3.6.1.4.1.11.2.3.2.3','HP-UX HP 9000/800','server.gif',2,4,'齐鲁石化','2006-08-20 17:11:59'),(50,'1.3.6.1.4.1.11.2.3.2.5','HP-UX HP 9000/700','server.gif',2,4,'齐鲁石化','2006-08-20 17:12:21'),(51,'1.3.6.1.4.1.9.1.324','Cisco Catalyst 2950-24','switch.png',6,3,'齐鲁石化','2006-09-13 17:28:13'),(52,'1.3.6.1.4.1.9.1.323','Cisco Catalyst 2950-12','switch.png',6,3,'齐鲁石化','2006-08-20 22:42:47'),(53,'1.3.6.1.4.1.9.1.325','Cisco Catalyst 2950C-24','switch.png',6,3,'齐鲁石化','2006-08-20 22:43:15'),(54,'1.3.6.1.4.0','INTELLINET Active Networking PS','other.gif',0,0,'齐鲁石化','2006-08-23 22:51:30'),(55,'1.3.6.1.4.1.9.1.27','Cisco 2511','router.png',6,1,'齐鲁石化','2006-08-23 22:52:09'),(56,'1.3.6.1.4.1.9.1.367','Cisco WS-C3550-48-SMI','switch_router.gif',6,2,'东华','2007-02-08 11:05:29'),(57,'1.3.6.1.4.1.2011.2.23.34','Quidway S3026C','switch.png',4,3,'齐鲁石化','2006-09-13 17:20:18'),(58,'1.3.6.1.4.1.2011.10.1.2','Quidway S2016','switch.png',4,3,'齐鲁石化','2006-09-13 17:19:18'),(59,'1.3.6.1.4.1.43.10.27.4.1.2.4','3Com Superstack 3 switch 4400','switch_router.gif',8,1,'齐鲁石化','2006-09-04 20:41:35'),(60,'1.3.6.1.4.1.171.10.48.1','DES-3226S Fast-Ethernet Switch','switch_router.gif',0,2,'齐鲁石化','2006-09-23 18:28:50'),(61,'1.3.6.1.4.1.6889.1.45.2','Avaya Cajun Switch Agent','switch_router.gif',5,2,'齐鲁石化','2006-09-23 18:32:00'),(62,'1.3.6.1.4.1.36.2.15.2.3','AlphaServer ES40 Compaq Tru64 UNIX','ibm.gif',0,4,'齐鲁石化','2006-12-04 11:40:23'),(63,'1.3.6.1.4.1.9.1.209','Cisco 2621','router.png',6,1,'齐鲁石化','2006-12-04 13:39:18'),(64,'1.3.6.1.4.1.9.1.217','Cisco Catalyst 2924R Switch','switch.png',6,3,'齐鲁石化','2006-12-04 13:26:23'),(65,'1.3.6.1.4.1.9.1.218','Cisco Catalyst 2924CR Switch','switch.png',6,3,'齐鲁石化','2006-12-04 13:26:52'),(66,'1.3.6.1.4.1.9.1.219','Catalyst 2912XL','switch.png',6,3,'东华','2007-02-06 10:55:19'),(67,'1.3.6.1.4.1.9.1.222','Cisco 7206','router.png',6,1,'东华','2007-02-06 11:05:18'),(68,'1.3.6.1.4.1.9.1.185','Cisco 2610','router.png',6,1,'东华','2007-02-06 11:08:04'),(69,'1.3.6.1.4.1.2011.2.23.41','huawei','switch_router.gif',4,2,'东华','2007-02-08 11:05:42'),(70,'1.3.6.1.4.1.9.1.110','Cisco 3640 Router','router.png',6,1,'东华合创','2007-03-06 16:24:52'),(71,'1.3.6.1.4.1.9.1.340','Cisco 3662Ac','router.png',6,1,'衡水信用社','2007-03-13 19:26:58'),(72,'1.3.6.1.4.1.9.1.366','Cisco Catalyst 3550-24','switch_router.gif',6,2,'衡水信用社','2007-03-13 19:58:24'),(73,'1.3.6.1.4.1.9.1.467','Cisco 2611 Router','router.png',6,1,'衡水信用社','2007-03-13 19:29:17'),(74,'1.3.6.1.4.1.4.1.2.1','TAINET Terminal Server TS-316','server.gif',0,4,'衡水信用社','2007-03-13 19:38:13'),(75,'1.3.6.1.4.1.1588.2.1.1.1','Fibre Channel-AL Switch','server.png',0,4,'衡水信用社','2007-03-13 19:45:16'),(76,'1.3.6.1.4.1.9.1.359','Cisco Catalyst 2950T-24','switch.png',6,3,'衡水信用社','2007-03-15 18:43:38'),(77,'1.3.6.1.4.1.9.1.283','Cisco Catalyst 6509','switch_router.gif',6,2,'恒源煤电','2007-04-05 07:33:30'),(78,'1.3.6.1.4.1.9.1.516','Cisco Catalyst 3750 series switches','switch.png',6,3,'恒源煤电','2007-04-05 07:36:49'),(79,'1.3.6.1.4.1.2011.2.23.23','huawei s3026E','switch.png',4,3,'恒源煤电','2007-04-05 07:41:07'),(80,'1.3.6.1.4.1.5624.2.1.52','Enterasys Networks, Inc. Matrix N7 Platinum','switch_router.gif',7,2,'潍柴动力','2007-04-19 09:26:19'),(81,'1.3.6.1.4.1.5624.2.1.24','Enterasys Networks Firmware Version: E9.1.3.0','switch_router.gif',7,2,'潍柴动力','2007-04-19 15:05:05'),(82,'1.3.6.1.4.1.9.1.429','Cisco Catalyst 2950G-48','switch.png',6,3,'潍柴动力','2007-04-19 15:08:30'),(83,'1.3.6.1.4.1.5624.2.1.28','Enterasys Networks Matrix E5 5H152-50','switch_router.gif',7,2,'潍柴动力','2007-04-19 15:11:24'),(84,'1.3.6.1.4.1.171.10.32.1.1','D-Link Fast Ethernet Switch','switch_router.gif',0,2,'潍柴动力','2007-04-19 15:15:24'),(85,'1.3.6.1.4.1.388.6.0','Symbol Mobius Wireless AP','switch.png',0,3,'潍柴动力','2007-04-19 15:16:48'),(86,'1.3.6.1.4.1.9.1.392','Cisco的PIX防火墙','firewall.png',6,6,'山西移动','2009-01-14 16:34:00'),(87,'1.3.6.1.4.1.9.1.502','CISCO4506交换机','switch.png',6,3,'齐鲁石化','2009-01-16 13:14:06'),(88,'1.3.6.1.4.1.25506.1.54','H3C路由交换机','switch_router.gif',13,2,'北京公司总部','2009-02-04 12:48:18'),(89,'1.3.6.1.4.1.25506.1.38','交换机','switch.png',13,3,'齐鲁石化','2009-02-04 16:12:22'),(90,'1.3.6.1.4.1.2011.2.39.11','无线接入器','wireless.png',13,3,'东华公司本部','2009-03-30 09:58:00'),(91,'1.3.6.1.4.1.9.1.278','Cisco Catalyst 3548XL','switch_router.gif',14,2,'CTSI','2009-03-12 13:12:07'),(92,'1.3.6.1.4.1.25506.1.224','H3C firewall---','firewall.png',13,6,'太原森林公安','2009-03-18 18:46:25'),(93,'1.3.6.1.4.1.25506.1.221','H3C MSR3011','switch_router.gif',13,2,'山西森林公安','2009-03-19 15:09:48'),(94,'1.3.6.1.4.1.25506.1.77','H3C MSR5060','switch_router.gif',13,2,'山西森林公安','2009-03-19 15:11:53'),(95,'1.3.6.1.4.1.25506.1.194','H3C SR6608','switch_router.gif',13,2,'山西森林公安','2009-03-19 15:13:55'),(96,'1.3.6.1.4.1.25506.1.159','H3C S7510','switch_router.gif',13,2,'山西森林公安','2009-03-19 16:09:19'),(97,'1.3.6.1.4.1.9.1.697','Cisco 2960','switch.png',14,3,'济南烟厂','2009-04-08 21:07:53'),(98,'1.3.6.1.4.1.9.1.428','Cisco 2950','switch.png',14,3,'齐鲁石化','2009-04-08 21:10:21'),(99,'1.3.6.1.4.1.9.1.696','CISCO 2960','switch.png',6,3,'济南烟厂','2009-04-08 22:34:32'),(100,'1.3.6.1.4.1.25506.1.40','交换机','switch.png',13,3,'公司','2009-04-24 10:56:22'),(101,'1.3.6.1.4.1.25506.1.75','H3C MSR 3060','switch_router.gif',13,2,'森林公安','2009-05-15 18:30:04'),(102,'1.3.6.1.4.1.2011.2.23.37','Quidway S3552G','switch_router.gif',13,2,'安徽蚌埠','2009-11-18 18:03:32'),(103,'1.3.6.1.4.1.2011.2.23.39','Quidway S3528G','switch_router.gif',13,2,'安徽蚌埠','2009-11-18 18:04:23'),(104,'1.3.6.1.4.1.3224.1.6','NetScreen-500 Firewall+VPN','firewall.png',15,66,'山西移动IDC','2009-11-26 13:59:44'),(105,'1.3.6.1.4.1.14331.1.4','NGFW4000防火墙','firewall.png',16,66,'','2010-01-12 09:41:10'),(106,'1.3.6.1.4.1.9.1.436','C3745','router.png',6,1,'德阳商行','2010-01-22 10:48:22'),(107,'1.3.6.1.4.1.9.1.444','C1700','router.png',6,1,'德阳商行','2010-01-22 10:49:25'),(108,'1.3.6.1.4.1.5651.1.101.13','MP7500','switch_router.gif',17,2,'吉林中行','2010-02-01 14:23:13'),(109,'1.3.6.1.4.1.9.1.620','C1841','router.png',14,1,'德阳商业银行','2010-03-01 11:50:48'),(110,'1.3.6.1.4.1.9.1.501','cat4000','switch_router.gif',14,2,'德阳商业银行','2010-03-01 11:51:32'),(111,'1.3.6.1.4.1.25506.1.187','SR8800','router.png',13,1,'广核','2010-12-07 11:00:43'),(112,'1.3.6.1.4.1.9.1.617','C3560','switch_router.gif',14,3,'','2010-12-07 14:54:47'),(113,'1.3.6.1.4.1.25506.1.33','S3600 交换机','switch.png',13,3,'公司','2010-12-31 13:36:36'),(114,'1.3.6.1.4.1.9.1.875','CISCO cat4500','switch.png',14,3,'','2011-01-11 18:27:01'),(115,'1.3.6.1.4.1.25506.1.151','h3c-S9512-EI','switch.png',13,3,'南京凤凰传媒','2011-04-26 14:25:11'),(116,'1.3.6.1.4.1.2636.1.1.1.2.35','Juniper SRX-Series Router/Firewall SRX3400','firewall.png',15,8,'','2011-04-26 14:35:01'),(117,'1.3.6.1.4.1.25506.1.246','h3c-S3100-26TP-EI','switch.png',13,3,'','2011-04-26 14:38:16'),(118,'1.3.6.1.4.1.25506.1.297','h3c-S3100-52TP-SI','switch.png',15,3,'','2011-04-26 14:39:04'),(119,'1.3.6.1.4.1.25506.1.37','h3c-S3600-28P-EI','switch.png',13,3,'','2011-04-26 14:40:25'),(120,'1.3.6.1.4.1.25506.1.160','h3c-S5100-24P-SI','switch.png',13,3,'','2011-04-26 14:41:11'),(121,'1.3.6.1.4.1.25506.1.31','h3c-S5100-48P-EI','switch.png',13,3,'','2011-04-26 14:41:52'),(122,'1.3.6.1.4.1.25506.1.1','h3c-S5500-28C-EI','switch.png',13,3,'','2011-04-26 14:43:02'),(123,'1.3.6.1.4.1.25506.1.5','h3c-S5500-28F-EI','switch.png',13,3,'','2011-04-26 14:43:49'),(124,'1.3.6.1.4.1.25506.1.9','h3c-S5500-52C-SI','switch.png',13,3,'','2011-04-26 14:44:36'),(125,'1.3.6.1.4.1.25506.1.12','h3c-S5510-24P','switch.png',13,3,'','2011-04-26 14:45:23'),(126,'1.3.6.1.4.1.25506.1.42','h3c-S5600-26C','switch.png',13,3,'','2011-04-26 14:46:11'),(127,'1.3.6.1.4.1.25506.1.43','h3c-S5600-50C','switch.png',13,3,'','2011-04-26 14:46:59'),(128,'1.3.6.1.4.1.25506.1.335','h3c-S5800-56C','switch.png',13,3,'','2011-04-26 14:47:42'),(129,'1.3.6.1.4.1.25506.1.341','h3c-S5820X-28S','switch.png',13,3,'','2011-04-26 14:48:25'),(130,'1.3.6.1.4.1.25506.1.208','h3c-S7503E','switch.png',13,3,'','2011-04-26 14:49:12'),(131,'1.3.6.1.4.1.25506.1.209','h3c-S7506E','switch.png',13,3,'','2011-04-26 14:49:55'),(132,'1.3.6.1.4.1.25506.1.55','h3c-S7506R','switch.png',13,3,'','2011-04-26 14:56:56'),(133,'1.3.6.1.4.1.25506.1.388','h3c-S9505E','switch.png',13,3,'','2011-04-26 14:57:35'),(134,'1.3.6.1.4.1.25506.1.149','h3c-S9508-EI','switch.png',13,3,'','2011-04-26 14:58:15'),(135,'1.3.6.1.4.1.25506.1.389','h3c-S9508E-V','switch.png',13,3,'','2011-04-26 14:58:59'),(136,'1.3.6.1.4.1.25506.1.182','h3c-9508-V5','switch.png',13,3,'','2011-04-26 14:59:40'),(137,'1.3.6.1.4.1.25506.1.391','h3c-S12508','switch.png',13,3,'','2011-04-26 15:00:29'),(138,'1.3.6.1.4.1.25506.1.392','h3c-S12518','switch.png',13,3,'','2011-04-26 15:01:15'),(139,'1.3.6.1.4.1.25506.1.188','h3c-SR8808-V5','router.png',13,1,'','2011-04-26 15:02:01'),(140,'1.3.6.1.4.1.25506.1.354','h3c-SR6604','router.png',13,1,'','2011-04-26 15:02:28'),(141,'1.3.6.1.4.1.25506.1.73','MSR30-20','switch.png',13,1,'','2011-04-26 15:03:22'),(142,'1.3.6.1.4.1.25506.1.261','MSR20-12','router.png',13,1,'','2011-04-26 15:03:55'),(143,'1.3.6.1.4.1.25506.1.232','MSR20-15','router.png',13,1,'','2011-04-26 15:04:22'),(144,'1.3.6.1.4.1.25506.1.76','H3C MSR50-40','router.png',13,1,'','2011-04-26 15:05:03'),(145,'1.3.6.1.4.1.25506.1.398','H3C Gateway routers  ICG2000','switch.png',13,1,'','2011-04-26 15:06:15'),(146,'1.3.6.1.4.1.25506.1.353','H3C WX Series Wireless Access Controll WX5004','wireless.png',13,3,'','2011-04-26 15:07:10'),(147,'1.3.6.1.4.1.25506.1.204','H3C WX Series Wireless Access Controll WX5002','wireless.png',13,3,'','2011-04-26 15:07:47'),(148,'1.3.6.1.4.1.25506.1.351','H3C WX Series Wireless Access Controll WX3024','wireless.png',13,3,'','2011-04-26 15:08:21'),(149,'1.3.6.1.4.1.25506.1.521','H3C WX Series Wireless Access Controll WX3008','wireless.png',13,3,'','2011-04-26 15:08:53'),(150,'1.3.6.1.4.1.25506.1.458','H3C WX Series Wireless Access Controll WX3010','wireless.png',13,3,'','2011-04-26 15:09:27'),(151,'1.3.6.1.4.1.311.1.1.3.1.2 3','Microsoft Windows Server 2008 Standard 32bit','server.png',1,4,'','2011-04-26 15:11:25'),(152,'1.3.6.1.4.1.89.1.1.62.8','RadWare LinkProof-Branch','router.png',19,1,'','2011-04-26 15:25:26'),(153,'1.3.6.1.4.1.89.1.1.62.20','App Director 1000 AS2','router.png',19,1,'','2011-04-26 16:06:31'),(154,'1.3.6.1.4.1.89.1.1.62.22','App Director 100','router.png',19,1,'','2011-04-26 16:08:02'),(155,'1.3.6.1.4.1.89.1.1.62.6','FireProof','router.png',19,1,'','2011-04-26 16:08:34'),(156,'1.3.6.1.4.1.89.1.1.62.2','RadWare Web Server Director','router.png',19,1,'','2011-04-26 16:10:44'),(157,'1.3.6.1.4.1.7564','VPN','switch_router.gif',20,12,'山西移动IDC','2011-07-29 13:09:09'),(158,'1.3.6.1.4.1.3902.3.100.6002.2','ZXR10 M6000 路由器','router.png',18,1,'吉林电力','2011-09-07 15:46:12'),(159,'1.3.6.1.4.1.3902.3.100.27','ZXR10 T600 路由器','router.png',18,1,'吉林电力','2011-09-07 16:41:19'),(160,'1.3.6.1.4.1.3902.3.100.40','ZXR10_5928E 路由器','router.png',18,1,'吉林电力','2011-09-07 16:45:23'),(161,'1.3.6.1.4.1.15227.1.3.1','VENUS VSOS V2.6','firewall.png',24,8,'宁夏铝业','2012-03-21 16:48:12'),(162,'1.3.6.1.4.1.10734.1.3.15','TippingPoint IPS','firewall.png',23,8,'宁夏铝业','2012-03-21 16:48:42'),(163,'1.3.6.1.4.1.24968.1.1.1','SecOS 3.6.2.0','firewall.png',22,8,'宁夏铝业','2012-03-21 16:48:54'),(164,'1.3.6.1.4.1.2.3.1.2.1.1','IBM AIX 服务器','ibm.gif',3,4,'宁夏铝业','2012-03-21 16:48:54'),(165,'1.3.6.1.4.1.11.2.3.10.1','HP UNIX 服务器','server.gif',2,4,'宁夏铝业','2012-03-21 16:48:54'),(166,'1.3.6.1.4.1.9.1.179','cisco UBR7246','switch.png',6,13,'无锡','2012-08-24 17:58:48'),(167,'1.3.6.1.4.1.9.1.191','ciscoUBR904','switch.png',6,13,'无锡','2012-08-24 17:59:29'),(168,'1.3.6.1.4.1.9.1.210','ciscoUBR7223','switch.png',6,13,'无锡','2012-08-24 18:00:18'),(169,'1.3.6.1.4.1.9.1.255','ciscoUBR924','switch.png',6,13,'无锡','2012-08-24 18:01:08'),(170,'1.3.6.1.4.1.9.1.271','ciscoUBR7246VXR','switch.png',6,13,'无锡','2012-08-24 18:01:48'),(171,'1.3.6.1.4.1.9.1.292','ciscoUBR912C','switch.png',6,13,'无锡','2012-08-24 18:02:39'),(172,'1.3.6.1.4.1.9.1.293','ciscoUBR912S','switch.png',6,13,'无锡','2012-08-24 18:03:23'),(173,'1.3.6.1.4.1.9.1.294','ciscoUBR914','switch.png',6,13,'无锡','2012-08-24 18:04:07'),(174,'1.3.6.1.4.1.9.1.316','ciscoUBR925','switch.png',6,13,'无锡','2012-08-24 18:04:42'),(175,'1.3.6.1.4.1.9.1.317','ciscoUBR10012','switch.png',6,13,'无锡','2012-08-24 18:05:21'),(176,'1.3.6.1.4.1.9.1.344','ciscoUBR7111','switch.png',6,13,'无锡','2012-08-24 18:06:38'),(177,'1.3.6.1.4.1.9.1.345','ciscoUBR7111E','switch.png',6,13,'无锡','2012-08-24 18:07:14'),(178,'1.3.6.1.4.1.9.1.346','ciscoUBR7114','switch.png',6,13,'无锡','2012-08-24 18:07:48'),(179,'1.3.6.1.4.1.9.1.347','ciscoUBR7114E','switch.png',6,13,'无锡','2012-08-24 18:08:24'),(180,'1.3.6.1.4.1.9.1.351','ciscoUBR905','switch.png',6,13,'无锡','2012-08-24 18:08:55'),(181,'1.3.6.1.4.1.9.1.827','ciscoUbr7225Vxr','switch.png',6,13,'无锡','2012-08-24 18:09:28'),(183,'1.3.6.1.4.1.116.','HDS存储','switch.png',25,14,'气象局','2012-09-21 17:50:28'),(184,'1.3.6.1.4.1.116.3.11.4.1.1','HDS存储USPV-29808','switch.png',25,14,'气象局','2012-09-21 17:50:28'),(185,'1.3.6.1.4.1.4881.1.1.10.1.43','Ruijie High-density IPv6 10G Core Routing Switch(S8606)','switch_router.gif',26,2,'宁夏银行','2012-10-17 16:05:03'),(186,'1.3.6.1.4.1.4881.1.2.1.1.28','Ruijie Router(RSR50-40)','router.png',26,1,'','2012-10-17 15:58:19'),(187,'1.3.6.1.4.1.4881.1.1.10.1.75','Routing Switch(S5760-48GT/4SFP)','switch_router.gif',26,2,'宁夏银行','2012-10-17 16:04:48'),(188,'1.3.6.1.4.1.4881.1.1.10.1.44','Ruijie High-density IPv6 10G Core Routing Switch(S8610)','switch_router.gif',26,2,'宁夏银行','2012-10-17 16:04:38'),(189,'1.3.6.1.4.1.4881.1.2.1.1.35','Ruijie Router (RSR20-24)','router.png',26,1,'宁夏银行','2012-10-17 16:07:46'),(190,'1.3.6.1.4.1.4881.1.1.10.1.113','Ruijie 10G Routing Switch(S5750-48GT/4SFP-E)','switch_router.gif',26,2,'','2012-10-17 16:11:11'),(191,'1.3.6.1.4.1.4881.1.1.10.1.12','Red-Giant Layer 3 Gigabit Intelligent Switch(S3550-24)','switch.png',26,3,'宁夏银行','2012-10-17 16:15:00'),(192,'1.3.6.1.4.1.6876.','VMWare','switch.png',27,15,'','2012-10-24 17:13:46');
/*!40000 ALTER TABLE `nms_device_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_dhcpconfig`
--

DROP TABLE IF EXISTS `nms_dhcpconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dhcpconfig` (
  `id` bigint(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(19) DEFAULT NULL,
  `community` varchar(100) DEFAULT NULL,
  `mon_flag` int(11) DEFAULT NULL,
  `netid` varchar(100) DEFAULT NULL,
  `supperid` int(11) DEFAULT NULL,
  `dhcptype` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_dhcpconfig`
--

LOCK TABLES `nms_dhcpconfig` WRITE;
/*!40000 ALTER TABLE `nms_dhcpconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_dhcpconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_diskconfig`
--

DROP TABLE IF EXISTS `nms_diskconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_diskconfig` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `DISKINDEX` int(20) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `LINKUSE` varchar(100) DEFAULT NULL,
  `SMS` int(2) DEFAULT NULL,
  `BAK` varchar(100) DEFAULT NULL,
  `REPORTFLAG` int(2) DEFAULT NULL,
  `LIMENVALUE` int(5) DEFAULT NULL,
  `LIMENVALUE1` int(5) DEFAULT NULL,
  `LIMENVALUE2` int(5) DEFAULT NULL,
  `SMS1` int(2) DEFAULT NULL,
  `SMS2` int(2) DEFAULT NULL,
  `SMS3` int(2) DEFAULT NULL,
  `monflag` int(2) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_diskconfig`
--

LOCK TABLES `nms_diskconfig` WRITE;
/*!40000 ALTER TABLE `nms_diskconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_diskconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_district`
--

DROP TABLE IF EXISTS `nms_district`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_district` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT ' ',
  `dis_desc` varchar(1000) DEFAULT ' ',
  `descolor` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_district`
--

LOCK TABLES `nms_district` WRITE;
/*!40000 ALTER TABLE `nms_district` DISABLE KEYS */;
INSERT INTO `nms_district` VALUES (1,'呼和浩特','呼和浩特','800080'),(2,'包头','包头','483D8B'),(3,'乌海','乌海','B0C4DE'),(4,'赤峰','赤峰','C0C0C0'),(5,'通辽','通辽','4169E1'),(6,'鄂尔多斯','鄂尔多斯','B0E0E6'),(7,'呼伦贝尔','呼伦贝尔','00CED1'),(8,'巴彦淖尔','巴彦淖尔','008080'),(9,'乌兰察布','乌兰察布','3CB371'),(10,'锡林郭勒','锡林郭勒','00B000'),(11,'阿拉善','阿拉善','B8860B'),(12,'兴安','兴安','CD853F');
/*!40000 ALTER TABLE `nms_district` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_dkdbareport`
--

DROP TABLE IF EXISTS `nms_dkdbareport`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dkdbareport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `begintime` varchar(50) DEFAULT NULL,
  `endtime` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_dkdbareport`
--

LOCK TABLES `nms_dkdbareport` WRITE;
/*!40000 ALTER TABLE `nms_dkdbareport` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_dkdbareport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_dkdbports`
--

DROP TABLE IF EXISTS `nms_dkdbports`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dkdbports` (
  `SUBSCRIBE_ID` bigint(20) NOT NULL,
  `ids` varchar(1000) DEFAULT NULL,
  KEY `SUBSCRIBE_ID` (`SUBSCRIBE_ID`),
  CONSTRAINT `nms_dkdbports_ibfk_1` FOREIGN KEY (`SUBSCRIBE_ID`) REFERENCES `nms_dkdbareport` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_dkdbports`
--

LOCK TABLES `nms_dkdbports` WRITE;
/*!40000 ALTER TABLE `nms_dkdbports` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_dkdbports` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:09:57
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_ftp_realtime`
--

DROP TABLE IF EXISTS `nms_ftp_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ftp_realtime` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ftp_id` int(11) DEFAULT NULL,
  `is_canconnected` int(11) DEFAULT NULL,
  `reason` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  `sms_sign` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ftp_realtime`
--

LOCK TABLES `nms_ftp_realtime` WRITE;
/*!40000 ALTER TABLE `nms_ftp_realtime` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_ftp_realtime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ftpconfig`
--

DROP TABLE IF EXISTS `nms_ftpconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ftpconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `str` varchar(255) DEFAULT NULL,
  `user_name` varchar(30) DEFAULT NULL,
  `user_password` varchar(30) DEFAULT NULL,
  `availability_string` varchar(255) DEFAULT NULL,
  `poll_interval` int(11) DEFAULT NULL,
  `unavailability_string` varchar(255) DEFAULT NULL,
  `timeout` int(11) DEFAULT NULL,
  `verify` int(11) DEFAULT NULL,
  `flag` int(11) DEFAULT NULL,
  `mon_flag` int(11) DEFAULT NULL,
  `alias` varchar(50) DEFAULT NULL,
  `sendmobiles` varchar(500) DEFAULT NULL,
  `netid` varchar(100) DEFAULT NULL,
  `filename` varchar(50) DEFAULT NULL,
  `sendemail` varchar(100) DEFAULT NULL,
  `sendphone` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(15) DEFAULT NULL,
  `supperid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ftpconfig`
--

LOCK TABLES `nms_ftpconfig` WRITE;
/*!40000 ALTER TABLE `nms_ftpconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_ftpconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ftpmonitorconfig`
--

DROP TABLE IF EXISTS `nms_ftpmonitorconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ftpmonitorconfig` (
  `id` bigint(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `timeout` int(10) DEFAULT NULL,
  `monflag` int(2) DEFAULT NULL,
  `filename` varchar(200) DEFAULT NULL,
  `bid` varchar(100) DEFAULT NULL,
  `sendmobiles` varchar(100) DEFAULT NULL,
  `sendemail` varchar(100) DEFAULT NULL,
  `sendphone` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(15) DEFAULT NULL,
  `supperid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ftpmonitorconfig`
--

LOCK TABLES `nms_ftpmonitorconfig` WRITE;
/*!40000 ALTER TABLE `nms_ftpmonitorconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_ftpmonitorconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ftptransconfig`
--

DROP TABLE IF EXISTS `nms_ftptransconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ftptransconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(15) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `flag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ftptransconfig`
--

LOCK TABLES `nms_ftptransconfig` WRITE;
/*!40000 ALTER TABLE `nms_ftptransconfig` DISABLE KEYS */;
INSERT INTO `nms_ftptransconfig` VALUES (1,'10.10.152.61','nms','webnms',1);
/*!40000 ALTER TABLE `nms_ftptransconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_func`
--

DROP TABLE IF EXISTS `nms_func`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_func` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `func_desc` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `ch_desc` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `level_desc` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `father_node` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `url` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `img_url` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `is_current_window` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `width` varchar(10) DEFAULT NULL,
  `height` varchar(10) DEFAULT NULL,
  `clientX` varchar(10) DEFAULT NULL,
  `clientY` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=289 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_func`
--

LOCK TABLES `nms_func` WRITE;
/*!40000 ALTER TABLE `nms_func` DISABLE KEYS */;
INSERT INTO `nms_func` VALUES (1,'0A','首页','1','0','user.do?action=home','images/menu01','0',NULL,NULL,NULL,NULL),(2,'0A0A','快捷功能','2','1',NULL,NULL,'0',NULL,NULL,NULL,NULL),(3,'0A0A01','添加设备','3','2','network.do?action=ready_add','resource/image/menu/tjsb.gif','0',NULL,NULL,NULL,NULL),(4,'0A0A02','自动发现','3','2','discover.do?action=config','resource/image/autoDiscovery16.gif','0',NULL,NULL,NULL,NULL),(5,'0A0A03','用户管理','3','2','user.do?action=list&jp=1','resource/image/menu/yh.gif','0',NULL,NULL,NULL,NULL),(6,'0A0A04','SNMP设置','3','2','snmp.do?action=list','resource/image/menu/snmp_mb.gif','0',NULL,NULL,NULL,NULL),(7,'0A0A05','密码设置','3','2','system/user/inputpwd.jsp','resource/image/menu/xgmm.gif','0',NULL,NULL,NULL,NULL),(8,'0A0A06','工作台历','3','2','userTaskLog.do?action=list','resource/image/calendar_view_month.png','0',NULL,NULL,NULL,NULL),(9,'0B','资源','1','0','network.do?action=list&jp=1','images/menu02','0',NULL,NULL,NULL,NULL),(10,'0B0A','拓扑','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL),(11,'0B0A01','网络拓扑','3','10','topology/network/index.jsp','resource/image/menu/wltp.gif','1',NULL,NULL,NULL,NULL),(13,'0B0C','设备维护','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL),(14,'0B0C01','设备列表','3','13','network.do?action=list&jp=1','resource/image/menu/sblb.gif','0',NULL,NULL,NULL,NULL),(15,'0B0C02','添加设备','3','13','network.do?action=ready_add','resource/image/menu/tjsb.gif','0',NULL,NULL,NULL,NULL),(16,'0B0C03','端口配置','3','13','portconfig.do?action=list&jp=1','resource/image/menu/dkpz.gif','0',NULL,NULL,NULL,NULL),(17,'0B0C04','链路配置','3','13','link.do?action=list&jp=1','resource/image/menu/llxx.gif','0',NULL,NULL,NULL,NULL),(18,'0B0B','性能监视','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL),(21,'0B0D','IP/MAC资源','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL),(22,'0B0D05','端口-IP-MAC基线','3','21','ipmacbase.do?action=list&jp=1','resource/image/menu/dk-ip-mac-jx.gif','0',NULL,NULL,NULL,NULL),(23,'0B0D07','当前MAC信息','3','21','ipmac.do?action=list&jp=1','resource/image/menu/dqmacxx.gif','0',NULL,NULL,NULL,NULL),(24,'0B0D06','MAC变更历史','3','21','ipmacchange.do?action=list&jp=1','resource/image/menu/macbgls.gif','0',NULL,NULL,NULL,NULL),(25,'0B0E','视图管理','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL),(26,'0B0E01','视图编辑','3','25','customxml.do?action=list&jp=1','resource/image/menu/stbj.gif','0',NULL,NULL,NULL,NULL),(27,'0B0E02','视图展示','3','25','topology/view/custom.jsp','resource/image/menu/stzs.gif','1',NULL,NULL,NULL,NULL),(28,'0B0F','设备面板配置管理','2','9',NULL,NULL,'0',NULL,NULL,NULL,NULL),(29,'0B0F01','面板模板编辑','3','28','panel.do?action=panelmodellist&jp=1','resource/image/menu/mbmbbj.gif','0',NULL,NULL,NULL,NULL),(30,'0B0F02','设备面板编辑','3','28','network.do?action=panelnodelist&jp=1','resource/image/menu/sbmbbj.gif','0',NULL,NULL,NULL,NULL),(31,'0E','告警','1','0',NULL,'images/menu03','0',NULL,NULL,NULL,NULL),(32,'0E0A','告警浏览','2','31',NULL,NULL,'0',NULL,NULL,NULL,NULL),(33,'0E0A01','告警列表','3','32','event.do?action=summary&jp=1','resource/image/menu/gjlb.gif','0',NULL,NULL,NULL,NULL),(34,'0E0A02','存在告警的设备','3','32','alarm/event/alarmnodelist.jsp','resource/image/menu/czgjdsb.gif','0',NULL,NULL,NULL,NULL),(35,'0E0B','告警统计','2','31',NULL,NULL,'0',NULL,NULL,NULL,NULL),(36,'0E0B01','按业务分布','3','35','event.do?action=businesslist&jp=1','resource/image/menu/anywfb.gif','0',NULL,NULL,NULL,NULL),(37,'0E0B02','按设备分布','3','35','event.do?action=equipmentlist&jp=1','resource/image/menu/ansbfb.gif','0',NULL,NULL,NULL,NULL),(38,'0E0C','Trap管理','2','31',NULL,NULL,'0',NULL,NULL,NULL,NULL),(39,'0E0C01','浏览Trap','3','38','trap.do?action=list&jp=1','resource/image/menu/lltrap.gif','0',NULL,NULL,NULL,NULL),(40,'0E0D','Syslog管理','2','31',NULL,NULL,'0',NULL,NULL,NULL,NULL),(41,'0E0D01','浏览Syslog','3','40','netsyslog.do?action=list&jp=1','resource/image/menu/llsyslog.gif','0',NULL,NULL,NULL,NULL),(42,'0E0D02','过滤规则','3','40','netsyslog.do?action=filter&jp=1','resource/image/menu/glgz.gif','0',NULL,NULL,NULL,NULL),(43,'0F','报表','1','0',NULL,'images/menu04','0',NULL,NULL,NULL,NULL),(44,'0F0A','报表浏览','2','43',NULL,NULL,'0',NULL,NULL,NULL,NULL),(45,'0F0A01','网络设备报表','3','44','netreport.do?action=list&jp=1','resource/image/menu/wlsbbb.gif','0',NULL,NULL,NULL,NULL),(46,'0F0A02','服务器报表','3','44','hostreport.do?action=list&jp=1','resource/image/menu/fwqbb.gif','0',NULL,NULL,NULL,NULL),(47,'0D','应用','1','0',NULL,'images/menu05','0',NULL,NULL,NULL,NULL),(48,'0D0A','数据库管理','2','47',NULL,NULL,'0',NULL,NULL,NULL,NULL),(49,'0D0A01','数据库监视','3','48','db.do?action=list&jp=1','resource/image/menu/sjkjs.gif','0',NULL,NULL,NULL,NULL),(50,'0D0A02','数据库类型管理','3','48','dbtype.do?action=list','resource/image/menu/sjklxgl.gif','0',NULL,NULL,NULL,NULL),(51,'0D0A03','Oracle告警设置','3','48','oraspace.do?action=list&jp=1','resource/image/menu/oracle_gjsz.gif','0',NULL,NULL,NULL,NULL),(52,'0D0A04','SQLServer告警设置','3','48','sqldbconfig.do?action=list&jp=1','resource/image/menu/sqlserver_gjsz.gif','0',NULL,NULL,NULL,NULL),(53,'0D0A05','DB2告警设置','3','48','db2config.do?action=list&jp=1','resource/image/menu/db2_gjsz.gif','0',NULL,NULL,NULL,NULL),(54,'0D0A06','Sybase告警设置','3','48','sybaseconfig.do?action=list&jp=1','resource/image/menu/sybase_gjsz.gif','0',NULL,NULL,NULL,NULL),(55,'0D0B','服务管理','2','47',NULL,NULL,'0',NULL,NULL,NULL,NULL),(56,'0D0B01','FTP服务监视','3','55','FTP.do?action=list&jp=1','resource/image/menu/ftp_fwjs.gif','0',NULL,NULL,NULL,NULL),(57,'0D0B02','Email服务监视','3','55','mail.do?action=list&jp=1','resource/image/menu/email_fwjs.gif','0',NULL,NULL,NULL,NULL),(59,'0D0B04','WEB访问服务监视','3','55','web.do?action=list&jp=1','resource/image/menu/web_fwfujs.gif','0',NULL,NULL,NULL,NULL),(63,'0D0C','中间件管理','2','47',NULL,NULL,'0',NULL,NULL,NULL,NULL),(64,'0D0C01','MQ监视','3','63','mq.do?action=list&jp=1','resource/image/menu/mq_js.gif','0',NULL,NULL,NULL,NULL),(65,'0D0C02','MQ告警设置','3','63','mqchannel.do?action=list&jp=1','resource/image/menu/mq_gjsz.gif','0',NULL,NULL,NULL,NULL),(66,'0D0C03','Domino监视','3','63','domino.do?action=list&jp=1','resource/image/menu/domino_js.gif','0',NULL,NULL,NULL,NULL),(67,'0D0C04','WAS监视','3','63','was.do?action=list&jp=1','resource/image/menu/was_js.gif','0',NULL,NULL,NULL,NULL),(68,'0D0C05','Weblogic监视','3','63','weblogic.do?action=list&jp=1','resource/image/menu/weblogic_js.gif','0',NULL,NULL,NULL,NULL),(69,'0D0C06','Tomcat监视','3','63','tomcat.do?action=list&jp=1','resource/image/menu/tomcat_js.gif','0',NULL,NULL,NULL,NULL),(70,'0K','系统管理','1','0','system.do?action=list','images/menu06','0',NULL,NULL,NULL,NULL),(71,'0K0A','资源管理','2','70',NULL,NULL,'0',NULL,NULL,NULL,NULL),(72,'0K0A01','SNNP模板','3','71','snmp.do?action=list','resource/image/menu/snmp_mb.gif','0',NULL,NULL,NULL,NULL),(73,'0K0A02','设备厂商','3','71','producer.do?action=list&jp=1','resource/image/menu/sbcs.gif','0',NULL,NULL,NULL,NULL),(74,'0K0A03','设备型号','3','71','devicetype.do?action=list&jp=1','resource/image/menu/sbxh.gif','0',NULL,NULL,NULL,NULL),(75,'0K0A04','服务','3','71','service.do?action=list&jp=1','resource/image/menu/fw.gif','0',NULL,NULL,NULL,NULL),(76,'0K0B','用户管理','2','70',NULL,NULL,'0',NULL,NULL,NULL,NULL),(77,'0K0B01','用户','3','76','user.do?action=list&jp=1','resource/image/menu/yh.gif','0',NULL,NULL,NULL,NULL),(78,'0K0B02','角色','3','76','role.do?action=list&jp=1','resource/image/menu/js.gif','0',NULL,NULL,NULL,NULL),(79,'0K0B03','部门','3','76','dept.do?action=list&jp=1','resource/image/menu/bm.gif','0',NULL,NULL,NULL,NULL),(80,'0K0B04','职位','3','76','position.do?action=list&jp=1','resource/image/menu/zw.gif','0',NULL,NULL,NULL,NULL),(81,'0K0B05','权限设置','3','76','admin.do?action=list&jp=1','resource/image/menu/qxsz.gif','0',NULL,NULL,NULL,NULL),(82,'0K0B06','修改密码','3','76','system/user/inputpwd.jsp','resource/image/menu/xgmm.gif','0',NULL,NULL,NULL,NULL),(83,'0K0B07','菜单设置','3','76','menu.do?action=list','resource/image/menu/cdsz.gif','0',NULL,NULL,NULL,NULL),(84,'0K0B08','员工库','3','76','employee.do?action=list&jp=1','resource/image/menu/ygk.gif','0',NULL,NULL,NULL,NULL),(85,'0K0B09','供应商信息','3','76','supper.do?action=list&jp=1','resource/image/menu/gysxx.gif','0',NULL,NULL,NULL,NULL),(86,'0K0B10','用户操作审计','3','76','userAudit.do?action=list','resource/image/menu/yhczsj.gif','0',NULL,NULL,NULL,NULL),(87,'0K0C','系统配置','2','70',NULL,NULL,'0',NULL,NULL,NULL,NULL),(88,'0K0C01','业务分类','3','87','business.do?action=list&jp=1','resource/image/menu/ywfl.gif','0',NULL,NULL,NULL,NULL),(89,'0K0C02','操作日志','3','87','syslog.do?action=list&jp=1','resource/image/menu/czrz.gif','0',NULL,NULL,NULL,NULL),(90,'0K0C03','告警邮箱设置','3','87','alertemail.do?action=list&jp=1','resource/image/menu/gjyxsz.gif','0',NULL,NULL,NULL,NULL),(91,'0K0C04','TFTP设置','3','87','tftpserver.do?action=list&jp=1','resource/image/menu/tftp_sz.gif','0',NULL,NULL,NULL,NULL),(92,'0B0B05','磁盘阀值一览表','3','18','disk.do?action=list&jp=1','resource/image/menu/cpfzylb.gif','0',NULL,NULL,NULL,NULL),(95,'0K0A05','防火墙类型','3','71','fwtype.do?action=list','resource/image/menu/fhqlx.gif','0',NULL,NULL,NULL,NULL),(96,'0D0C07','IIS监视','3','63','iis.do?action=list&jp=1','resource/image/menu/iis_js.gif','0',NULL,NULL,NULL,NULL),(97,'0D0B08','端口服务监视','3','55','pstype.do?action=list','resource/image/menu/dkfwjs.gif','0',NULL,NULL,NULL,NULL),(98,'0D0A07','MySql监视','3','48','mysqlconfig.do?action=list&jp=1','resource/image/menu/mysql_js.gif','0',NULL,NULL,NULL,NULL),(99,'0K0A06','中间件管理','3','71','middleware.do?action=list&jp=1','resource/image/menu/zjjgl.gif','0',NULL,NULL,NULL,NULL),(100,'0D0C08','CICS监视','3','63','cics.do?action=list&jp=1','resource/image/menu/cics_js.gif','0',NULL,NULL,NULL,NULL),(101,'0B0D09','MAC地址定位','3','21','maclocate.do?action=readyfind','resource/image/menu/mac_dzdw.gif','0',NULL,NULL,NULL,NULL),(102,'0D0C09','DNS监视','3','63','dns.do?action=list&jp=1','resource/image/menu/dns_js.gif','0',NULL,NULL,NULL,NULL),(103,'0K0D','数据库维护','2','70','','','0',NULL,NULL,NULL,NULL),(104,'0K0D01','数据导出','3','103','dbbackup.do?action=list','resource/image/menu/sjdc.gif','0',NULL,NULL,NULL,NULL),(105,'0K0D02','数据导入','3','103','dbbackup.do?action=dbbackuplist&jp=1','resource/image/menu/sjdr.gif','0',NULL,NULL,NULL,NULL),(106,'0F0B','决策支持','2','43','','','0',NULL,NULL,NULL,NULL),(107,'0F0B02','网络设备','3','106','netreport.do?action=choceDoc&jp=1','resource/image/menu/wlsb.gif','0',NULL,NULL,NULL,NULL),(108,'0F0B04','服务器','3','106','hostreport.do?action=hostchoce&jp=1','resource/image/menu/fwq.gif','0',NULL,NULL,NULL,NULL),(109,'0F0A03','数据库报表','3','44','dbreport.do?action=list&jp=1','resource/image/menu/sjkbb.gif','0',NULL,NULL,NULL,NULL),(110,'0F0D','业务报表浏览','2','43','','','0',NULL,NULL,NULL,NULL),(111,'0F0D02','业务报表','3','110','businessReport.do?action=list&jp=1','resource/image/menu/ywbb.gif','0',NULL,NULL,NULL,NULL),(112,'0D0A08','Informix告警设置','3','48','informixspace.do?action=list&jp=1','resource/image/menu/informix_gjsz.gif','0',NULL,NULL,NULL,NULL),(113,'0E0E','短信告警','2','31','','','0',NULL,NULL,NULL,NULL),(114,'0E0E01','短信告警浏览','3','113','smsevent.do?action=list&jp=1','resource/image/menu/dxgjll.gif','0',NULL,NULL,NULL,NULL),(115,'0K0C05','配置子项类别','3','87','subconfigcat.do?action=list&jp=1','resource/image/menu/pzzxlb.gif','0',NULL,NULL,NULL,NULL),(116,'0D0C0:','IISLog监视','3','63','iislog.do?action=list&jp=1','resource/image/menu/iislog_js.gif','0',NULL,NULL,NULL,NULL),(117,'0D0D','环境监控','2','47','','','0',NULL,NULL,NULL,NULL),(118,'0D0D01','温湿度监测','3','117','temperatureHumidity.do?action=list2&jp=1','resource/image/menu/wsdjc.gif','0',NULL,NULL,NULL,NULL),(119,'0D0D02','温湿度监测参数配置','3','117','temperatureHumidity.do?action=list&jp=1','resource/image/menu/wsdjccspz.gif','0',NULL,NULL,NULL,NULL),(120,'0B0E03','业务列表','3','25','businessNode.do?action=list&jp=1','resource/image/icon_detail.gif','0',NULL,NULL,NULL,NULL),(121,'0K0A07','业务采集类型','3','71','buscolltype.do?action=list&jp=1','resource/image/viewmac.gif','0',NULL,NULL,NULL,NULL),(122,'0B0A05','业务视图','3','10','flex/home.html','resource/image/menu/wltp.gif','1','null','null','null','null'),(123,'0D0C0;','JBOSS监视','3','63','jboss.do?action=list&jp=1','resource/image/jboss.gif','0',NULL,NULL,NULL,NULL),(124,'0K0A08','端口类型','3','71','porttype.do?action=list&jp=1','resource/image/menu/snmp_mb.gif','0',NULL,NULL,NULL,NULL),(125,'0B0C05','远程PING服务器','3','13','remotePing.do?action=list','resource/image/menu/snmp_mb.gif','0',NULL,NULL,NULL,NULL),(126,'0K0C08','区域管理','3','87','district.do?action=list&jp=1','resource/image/menu/sblb.gif','0',NULL,NULL,NULL,NULL),(127,'0K0C09','消息服务器设置','3','87','alertinfo.do?action=list&jp=1','resource/image/menu/gjyxsz.gif','0',NULL,NULL,NULL,NULL),(128,'0B0D0:','IP地址段管理','3','21','ipdistrict.do?action=list','resource/image/menu/pzzxlb.gif','0',NULL,NULL,NULL,NULL),(129,'0F0A04','中间件报表','3','44','midcapreport.do?action=midlist&jp=1','resource/image/viewreport.gif','0',NULL,NULL,NULL,NULL),(130,'0B0D0;','网段匹配','3','21','ipDistrictMatch.do?action=list&jp=1','resource/image/menu/dxgjll.gif','0',NULL,NULL,NULL,NULL),(131,'0B0D0<','mac配置','3','21','macconfig.do?action=list&jp=1','resource/image/menu/dqmacxx.gif','0',NULL,NULL,NULL,NULL),(132,'0B0D0=','网段管理','3','21','ipDistrictMatch.do?action=districtDetails&jp=1','resource/image/menu/czrz.gif','0',NULL,NULL,NULL,NULL),(133,'0D0C0<','APACHE监视','3','63','apache.do?action=list&jp=1','resource/image/apache.gif','0',NULL,NULL,NULL,NULL),(134,'0D0C0=','Tuxedo管理','3','63','tuxedo.do?action=list&jp=1','resource/image/menu/weblogic_js.gif','0',NULL,NULL,NULL,NULL),(135,'0G','3D机房','1','0','cabinetequipment.do?action=list&jp=1','images/menu07','0',NULL,NULL,NULL,NULL),(141,'0A0B04','Web Telnet','3','243','webutil/webtelnet.swf','','1','644','555',NULL,NULL),(142,'0A0B05','Web SSH','3','243','webutil/WebSSH.swf','','1','644','555',NULL,NULL),(143,'0A0B06','SNMP测试','3','243','tool/snmpping.jsp','','1','500','400',NULL,NULL),(144,'0D0E','存储管理','2','47','','','0','','','',''),(145,'0D0E01','存储类型管理','3','144','storagetype.do?action=list','resource/image/menu/cclx.gif','0','','','',''),(146,'0D0E02','存储管理','3','144','storage.do?action=list&jp=1','resource/image/menu/ccxx.gif','0','','','',''),(147,'0B0C07','连通性检测设置','3','13','connectConfig.do?action=list&jp=1','resource/image/toolbar/ping.gif','0','','','',''),(148,'0A0B07','链路分析','3','243','tool/linkAnalytics.jsp','','1','600','400',NULL,NULL),(149,'0C','性能','1','0','performance/index.jsp','images/menu08','0','','','',''),(150,'0B0B06','链路性能一览表','3','18','linkperformance.do?action=list&jp=1','resource/image/menu/llxx.gif','0','','','',''),(151,'0A0B08','数据库连接工具','3','243','tool/index.jsp','','1','800','600',NULL,NULL),(152,'0B0B07','告警指标配置','3','18','alarmIndicators.do?action=list&jp=1','resource/image/toolbar/jszbfzpz.gif','0','','','',''),(153,'0B0B08','采集指标配置','3','18','gatherIndicators.do?action=list&jp=1','resource/image/menu/pzzxlb.gif','0','','','',''),(154,'0B0B09','采集指标一览表','3','18','nodeGatherIndicators.do?action=showlist&jp=1','resource/image/menu/czrz.gif','0','','','',''),(155,'0B0B0;','指标阀值一览表','3','18','alarmIndicatorsNode.do?action=showlist&jp=1','resource/image/menu/zbqjfzylb.gif','0','','','',''),(156,'0B0C09','远程登陆设置','3','13','vpntelnetconf.do?action=list&jp=1','resource/image/menu/ywfl.gif','0','','','',''),(157,'0B0B0<','进程监视列表','3','18','processgroup.do?action=showlist&jp=1','resource/image/menu/cdsz.gif','0','','','',''),(158,'0B0B0=','主机服务监视','3','18','hostservicegroup.do?action=showlist&jp=1','resource/image/menu/fw.gif','0','','','',''),(159,'0E0D03','告警设置列表','3','40','netsyslogalarm.do?action=list&jp=1','resource/image/menu/llsyslog.gif','0','','','',''),(160,'0K0C0:','方案管理','3','87','knowledge.do?action=list&jp=1','resource/image/menu/snmp_mb.gif','0','','','',''),(161,'0K0C0;','知识库管理','3','87','knowledgebase.do?action=list&jp=1','resource/image/menu/snmp_mb.gif','0','','','',''),(162,'0E0A03','告警方式设置','3','32','alarmWay.do?action=list&jp=1','resource/image/menu/xgmm.gif','0','','','',''),(165,'0A0A07','设备配置文件列表','3','2','vpntelnetconf.do?action=deviceList&jp=1','resource/image/menu/ywfl.gif','0','','','',''),(167,'0K0C0<','系统运行模式','3','87','systemconfig.do?action=collectwebflag','resource/image/menu/xgmm.gif','0','','','',''),(169,'0K0C0=','FTP传输设置','3','87','ftptrans.do?action=list&jp=1','resource/image/menu/tftp_sz.gif','0','','','',''),(170,'0F0F','报表定制','2','43','','','0','','','',''),(171,'0F0F01','网络设备','3','170','netreport.do?action=networkReport','resource/image/menu/ywfl.gif','1','1155','666',NULL,NULL),(172,'0F0F02','服务器','3','170','hostreport.do?action=serverReport','resource/image/menu/ywfl.gif','1','1155','666',NULL,NULL),(178,'0I','自动化','1','0','vpntelnetconf.do?action=list&jp=1','images/menu10','0','','','',''),(179,'0F0F03','数据库报表','3','170','dbreport.do?action=dbReport','resource/image/menu/ywfl.gif','1','1150','666','',''),(180,'0I0A','远程设备维护','2','178','','','0','','','',''),(181,'0I0A03','设备密码配置','3','180','vpntelnetconf.do?action=passwdList&jp=1','resource/image/menu/qxsz.gif','0','','','',''),(182,'0I0A04','密码定时变更','3','180','vpntelnetconf.do?action=deviceList&jp=1','resource/image/menu/dotime.gif','0','','','',''),(183,'0I0B','配置文件管理','2','178','','','0','','','',''),(184,'0I0A03','批量修改密码','3','180','vpntelnetconf.do?action=ready_multi_modify','resource/image/menu/piliangmima.gif','0','','','',''),(185,'0I0B01','配置文件列表','3','183','vpntelnetconf.do?action=configlist&jp=1','resource/image/menu/snmp_mb.gif','0','','','',''),(186,'0I0B02','批量备份','3','183','vpntelnetconf.do?action=ready_backupForBatch','resource/image/menu/bat.gif','0','','','',''),(187,'0I0B03','批量应用','3','183','vpntelnetconf.do?action=ready_deployCfgForBatch','resource/image/menu/piliang.gif','0','','','',''),(188,'0I0B04','定时备份设置','3','183','vpntelnetconf.do?action=ready_timingBackup','resource/image/menu/dotime.gif','0','','','',''),(189,'0F0F05','信息资产报表','3','170','perform.do?action=monitorNodelist','resource/image/menu/czrz.gif','1','1160','800','',''),(190,'0I0C','自动化控制','2','178','','','0','','','',''),(191,'0I0C09','主机配置管理','3','222','serverUpAndDown.do?action=list&jp=1','resource/image/menu/updown.gif','0','','','',''),(192,'0K0E','首页模块控制','2','70','','','0','','','',''),(193,'0K0E01','模块分配','3','192','homerole.do?action=list','resource/image/menu/sblb.gif','0','','','',''),(194,'0K0E02','用户个性设置','3','192','homeuser.do?action=update','resource/image/menu/sblb.gif','0','','','',''),(195,'0I0C08','应急关机策略管理','3','222','serverUpAndDown.do?action=clusterList&jp=1','resource/image/menu/updown.gif','0','','','',''),(196,'0D0D03','环境动力设备','3','117','ups.do?action=list&jp=1','resource/image/menu/wsdjc.gif','0','','','',''),(197,'0I0C05','设备开关机审计','3','222','serverUpAndDown.do?action=logList&jp=1','resource/image/menu/shenji.gif','0','','','',''),(198,'0B0D0>','地址转发表信息','3','21','fdb.do?action=list&jp=1','resource/image/menu/dqmacxx.gif','0','','','',''),(199,'0I0A05','密码操作审计','3','180','vpntelnetconf.do?action=ready_multi_audit&jp=1','resource/image/menu/shenji.gif','0','','','',''),(200,'0I0B09','策略管理','3','220','configRule.do?action=strategyList','resource/image/menu/celue.gif','0','','','',''),(201,'0I0B0:','合规性管理','3','220','configRule.do?action=showAllDevice','resource/image/menu/heguixing.gif','0','','','',''),(202,'0I0C06','定时巡检管理','3','190','vpntelnetconf.do?action=ready_fileBackup','resource/image/menu/dotime.gif','0','','','',''),(203,'0I0C07','定时巡检列表','3','190','vpntelnetconf.do?action=fileList&jp=1','resource/image/menu/snmp_mb.gif','0','','','',''),(204,'0I0C08','执行脚本命令','3','190','vpntelnetconf.do?action=deviceCfg','resource/image/menu/execute.gif','0','','','',''),(205,'0K0C0>','采集代理配置','3','87','agent.do?action=list&jp=1','resource/image/menu/czrz.gif','0','','','',''),(206,'0I0C09','CBWFQ监控管理','3','190','telnetConfig.do?action=missionList&jp=1','resource/image/menu/wfq.gif','0','','','',''),(207,'0I0C0@','ACL配置管理','3','190','telnetConfig.do?action=telnetCfgList','resource/image/menu/acl.gif','0','','','',''),(208,'0E0D04','Syslog详细','3','40','netsyslog.do?action=statistic&jp=1','resource/image/menu/syslogxx.gif','0',NULL,NULL,NULL,NULL),(209,'0E0D05','问题对应','3','40','alarm/syslog/questionlist.jsp?jp=1','resource/image/menu/dk-ip-mac-jx.gif','0',NULL,NULL,NULL,NULL),(210,'0K0D03','数据库备份','3','103','dbbackup.do?action=autobackuplist','resource/image/menu/sjdr.gif','0','','','',''),(211,'0K0F','更换皮肤','2','70','','','0',NULL,NULL,NULL,NULL),(212,'0K0F01','商务蓝色','3','211','systemconfig.do?action=changeskins','resource/image/menu/sblb.gif','0',NULL,NULL,NULL,NULL),(213,'0K0F02','春天草绿','3','211','systemconfig.do?action=changeskins&skin=skin1','resource/image/menu/sblb.gif','0',NULL,NULL,NULL,NULL),(214,'0K0F03','海洋绿色','3','211','systemconfig.do?action=changeskins&skin=skin2','resource/image/menu/sblb.gif','0',NULL,NULL,NULL,NULL),(215,'0J0C0;','SLA监视管理','3','221','ciscosla.do?action=list&jp=1','resource/image/menu/sla.gif','0','','','',''),(216,'0J0C0<','SLA命令配置','3','221','slacmd.do?action=showcmd','resource/image/menu/execute.gif','0','','','',''),(217,'0J0C0=','SLA设备属性库','3','221','ciscoslaproperty.do?action=list&jp=1','resource/image/menu/sla.gif','0','','','',''),(218,'0J0C0>','SLA操作审计','3','221','slaAudit.do?action=list&jp=1','resource/image/menu/slasj.gif','0','','','',''),(219,'0J0C0?','SLA命令维护','3','221','slacmd.do?action=loadfilelist&jp=1','resource/image/menu/mbmbbj.gif','0','','','',''),(220,'0I0D','合规性检查','2','178','','','0','','','',''),(221,'0J0F','服务级别管理','2','224','','','0','','','',''),(222,'0I0F','应急预案管理','2','178','','','0','','','',''),(223,'0B0D0?','IP配置','3','21','macconfig.do?action=list1&jp=1','resource/image/menu/dqmacxx.gif','0','','','',''),(224,'0J','服务质量','1','0','ciscosla.do?action=list&jp=1','','0','','','',''),(225,'0J0G','ICMP端到端响应','2','224','','','0','','','',''),(226,'0J0H','UDP端到端抖动','2','224','','','0','','','',''),(227,'0J0I','UDP端到端响应','2','224','','','0','','','',''),(228,'0J0J','TCP连接响应','2','224','','','0','','','',''),(229,'0J0G01','ICMP监视列表','3','225','slaicmp.do?action=list&jp=1','resource/image/menu/ICMP.gif','0','','','',''),(230,'0J0H01','UDP抖动监视列表','3','226','slaudpjitter.do?action=list&jp=1','resource/image/menu/Jitter.gif','0','','','',''),(231,'0J0I01','UDP响应监视列表','3','227','slaudp.do?action=list&jp=1','resource/image/menu/UDP.gif','0','','','',''),(232,'0J0J01','TCP连接监视列表','3','228','slatcp.do?action=list&jp=1','resource/image/menu/TCP.GIF','0','','','',''),(233,'0J0G03','ICMP性能拓扑图','3','225','slaicmp.do?action=showmap','resource/image/menu/wltp.gif','0','','','',''),(234,'0J0H03','UDP抖动性能拓扑图','3','226','slaudpjitter.do?action=showmap','resource/image/menu/wltp.gif','0','','','',''),(235,'0J0I03','UDP响应性能拓扑图','3','227','slaudp.do?action=showmap','resource/image/menu/wltp.gif','0','','','',''),(236,'0J0J03','TCP连接性能拓扑图','3','228','slatcp.do?action=showmap','resource/image/menu/wltp.gif','0','','','',''),(238,'0G0B01','机柜设备配置','3','251','cabinetequipment.do?action=list&jp=1','resource/image/menu/jgsb.gif','0','','','',''),(239,'0G0B02','机柜配置','3','251','maccabinet.do?action=list&jp=1','resource/image/menu/jgpz.gif','0','','','',''),(240,'0G0B03','机房配置管理','3','251','eqproom.do?action=list&jp=1','resource/image/menu/jfpz.gif','0','','','',''),(241,'0I0C06','应急预案视频管理','3','222','mediaPlayer.do?action=list&jp=1','resource/image/menu/shipin.gif','0','','','',''),(242,'0I0C07','应急预案视频在线播放','3','222','mediaPlayer.do?action=listView&jp=1','resource/image/menu/zxbf.gif','0','','','',''),(243,'0A0B','工具','2','1','','','0','','','',''),(247,'0A0B01','Ping检测','3','243','tool/ping.jsp','','1','500','400','',''),(248,'0A0B02','路由跟踪','3','243','tool/tracerouter.jsp','','1','500','400','',''),(249,'0A0B03','远程Ping','3','243','tool/remote_Ping.jsp','','1','500','420','',''),(250,'0G0A','3D机房展示','2','135','','','0','','','',''),(251,'0G0B','机房配置','2','135','','','0','','','',''),(252,'0G0C','机房报表','2','135','','','0','','','',''),(253,'0K0C0?','业务系统','3','87','businesssystem.do?action=list&jp=1','resource/image/menu/stzs.gif','0','','','',''),(254,'0G0C01','机房使用报表','3','252','cabinetreport.do?action=list&jp=1','resource/image/menu/stzs.gif','0','','','',''),(255,'0G0C02','机柜使用报表','3','252','equipmentreport.do?action=list&jp=1','resource/image/menu/stzs.gif','0','','','',''),(256,'0G0A01','3D机房效果展示','3','250','cabinetshow.do?action=list&jp=1','resource/image/menu/snmp_mb.gif','1','1250','650','',''),(257,'0G0A02','3D机房树型展示','3','250','cabinet/eqprooms.jsp','resource/image/menu/sblb.gif','0','','','',''),(258,'0G0D','机房管理','2','135','','','0','','','',''),(259,'0G0D01','机房管理制度','3','258','roomlaw.do?action=list&jp=1','resource/image/menu/snmp_mb.gif','0','','','',''),(260,'0G0D02','机房安全视频管理','3','258','roomvideo.do?action=list&jp=1','resource/image/menu/shipin.gif','0','','','',''),(261,'0G0D03','机房安全视频播放','3','258','roomvideo.do?action=listView&jp=1','resource/image/menu/zxbf.gif','0','','','',''),(262,'0G0D04','机房门禁记录','3','258','roomlogin.do?action=list&jp=1','resource/image/menu/snmp_mb.gif','0','','','',''),(263,'0G0D05','机房来宾登记','3','258','roomguest.do?action=list&jp=1','resource/image/menu/js.gif','0','','','',''),(264,'0A0B09','Visio版拓扑图倒入','3','243','tool/importvisiotopo.jsp','','1','500','300','',''),(265,'0G0C03','业务系统机柜报表','3','252','cabinetbsreport.do?action=list','resource/image/menu/stzs.gif','0','','','',''),(266,'0B0D0B','IP分配信息','3','21','ipallot.do?action=list','resource/image/menu/czrz.gif','0','','','',''),(267,'0B0C0:','设备批量倒入','3','13','network.do?action=showaddbatchhosts','resource/image/menu/sjdr.gif','0','','','',''),(268,'0K0F04','炫黑蓝色','3','211','systemconfig.do?action=changeskins&skin=skin3','resource/image/menu/sblb.gif','0',NULL,NULL,NULL,NULL),(269,'0B0G','VPN管理','2','9','','','0','','','',''),(270,'0B0G01','vpn链路一览表','3','269','vpn.do?action=list','resource/image/menu/zw.gif','0','','','',''),(271,'0B0G02','vpn拓扑图','3','269','vpn.do?action=showmap','resource/image/menu/sblb.gif','0','','','',''),(272,'0D0B09','TFTP服务监视','3','55','tftp.do?action=list&jp=1','resource/image/menu/tftp_sz.gif','0','','','',''),(273,'0D0B0:','DHCP服务监视','3','55','dhcp.do?action=list&jp=1','resource/image/menu/dhcp.png','0','','','',''),(274,'0E0B03','告警决策','3','35','event.do?action=chartForAlarmSummarize&jp=1','resource/image/menu/ansbfb.gif','0','','','',''),(275,'0L','IP管理','1','0','ipfield.do?action=queryAll','','0','','','',''),(276,'0L0A','配置管理','2','275','','','0','','','',''),(277,'0L0A0?','骨干点管理','3','276','ipAll.do?action=list&jp=1','resource/image/menu/backbonetype.png','0','','','',''),(278,'0L0A05','场站状态管理','3','276','ipConfig.do?action=list&jp=1','resource/image/menu/station.png','0','','','',''),(279,'0L0A0;','业务类型管理','3','276','ipBussiness.do?action=list&jp=1','resource/image/menu/bussinesstype.png','0','','','',''),(280,'0L0A0<','设备类型管理','3','276','ipEncryption.do?action=list&jp=1','resource/image/menu/type.png','0','','','',''),(281,'0L0A0E','业务管理','3','276','ipstorage.do?action=list','resource/image/menu/bussiness.png','0','','','',''),(282,'0L0B','IP分配','2','275','','','0','','','',''),(283,'0L0B02','IP使用查询','3','282','ipfield.do?action=ready_query','resource/image/menu/stationselect.png','0','','','',''),(285,'0L0B06','待处理申请表','3','282','ipfield.do?action=facility','resource/image/menu/insertStation.png','0','','','',''),(286,'0L0B07','IP报表','3','282','ipAll.do?action=reportlist&jp=1','resource/image/menu/ywbb.gif','0','','','',''),(287,'OAOB10','nslookup','3','243','tool/nslookup.jsp',NULL,'1','500','400',NULL,NULL),(288,'0I0C0;','策略定制展现','3','222','topology/machineUpAndDown/index.jsp','resource/image/menu/gjyxsz.gif','1','1224','700','0','0');
/*!40000 ALTER TABLE `nms_func` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_gather_indicators`
--

DROP TABLE IF EXISTS `nms_gather_indicators`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_gather_indicators` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `type` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `subtype` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `alias` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `description` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `category` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `isDefault` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `isCollection` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `poll_interval` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `interval_unit` varchar(5) CHARACTER SET gb2312 DEFAULT NULL,
  `classpath` varchar(200) CHARACTER SET gb2312 DEFAULT ' ',
  `collecttype` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1365 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_gather_indicators`
--

LOCK TABLES `nms_gather_indicators` WRITE;
/*!40000 ALTER TABLE `nms_gather_indicators` DISABLE KEYS */;
INSERT INTO `nms_gather_indicators` VALUES (1,'ping','net','cisco','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(2,'cpu','net','cisco','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.CiscoCpuSnmp',1),(3,'memory','net','cisco','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.CiscoMemorySnmp',1),(4,'flash','net','cisco','flash','FLASH利用率','flash','1','1','10','m','com.afunms.polling.snmp.flash.CiscoFlashSnmp',1),(5,'temperature','net','cisco','temperature','温度','temperature','1','1','10','m','com.afunms.polling.snmp.temperature.CiscoTemperatureSnmp',1),(6,'fan','net','cisco','fan','风扇','fan','1','1','10','m','com.afunms.polling.snmp.fan.CiscoFanSnmp',1),(7,'power','net','cisco','power','电源','power','1','1','10','m','com.afunms.polling.snmp.power.CiscoPowerSnmp',1),(8,'voltage','net','cisco','voltage','电压','voltage','1','1','10','m','com.afunms.polling.snmp.voltage.CiscoVoltageSnmp',1),(9,'systemgroup','net','cisco','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(10,'ipmac','net','cisco','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(11,'fdb','net','cisco','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(12,'router','net','cisco','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(13,'interface','net','cisco','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(14,'ping','net','h3c','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(15,'cpu','net','h3c','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.H3CCpuSnmp',1),(16,'memory','net','h3c','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.H3CMemorySnmp',1),(17,'flash','net','h3c','flash','FLASH利用率','flash','1','1','10','m','com.afunms.polling.snmp.flash.H3CFlashSnmp',1),(18,'temperature','net','h3c','temperature','温度','temperature','1','1','10','m','com.afunms.polling.snmp.temperature.H3CTemperatureSnmp',1),(19,'fan','net','h3c','fan','风扇','fan','1','1','10','m','com.afunms.polling.snmp.fan.H3CFanSnmp',1),(20,'power','net','h3c','power','电源','power','1','1','10','m','com.afunms.polling.snmp.power.H3CPowerSnmp',1),(21,'voltage','net','h3c','voltage','电压','voltage','1','1','10','m','com.afunms.polling.snmp.voltage.H3CVoltageSnmp',1),(22,'systemgroup','net','h3c','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(23,'ipmac','net','h3c','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(24,'fdb','net','h3c','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(25,'router','net','h3c','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(26,'interface','net','h3c','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(27,'ping','net','entrasys','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(28,'cpu','net','entrasys','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.EnterasysCpuSnmp',1),(29,'memory','net','entrasys','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.EnterasysMemorySnmp',1),(30,'flash','net','entrasys','flash','FLASH利用率','flash','1','1','10','m',' ',1),(31,'temperature','net','entrasys','temperature','温度','temperature','1','1','10','m',' ',1),(32,'fan','net','entrasys','fan','风扇','fan','1','1','10','m',' ',1),(33,'power','net','entrasys','power','电源','power','1','1','10','m',' ',1),(34,'voltage','net','entrasys','voltage','电压','voltage','1','1','10','m',' ',1),(35,'systemgroup','net','entrasys','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(36,'ipmac','net','entrasys','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(37,'fdb','net','entrasys','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(38,'router','net','entrasys','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(39,'interface','net','entrasys','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(40,'ping','net','radware','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(41,'cpu','net','radware','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.RadwareCpuSnmp',1),(42,'memory','net','radware','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.EnterasysMemorySnmp',1),(43,'flash','net','radware','flash','FLASH利用率','flash','1','1','10','m',' ',1),(44,'temperature','net','radware','temperature','温度','temperature','1','1','10','m',' ',1),(45,'fan','net','radware','fan','风扇','fan','1','1','10','m',' ',1),(46,'power','net','radware','power','电源','power','1','1','10','m',' ',1),(47,'voltage','net','radware','voltage','电压','voltage','1','1','10','m',' ',1),(48,'systemgroup','net','radware','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(49,'ipmac','net','radware','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(50,'fdb','net','radware','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(51,'router','net','radware','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(52,'interface','net','radware','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(53,'ping','net','maipu','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(54,'cpu','net','maipu','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.MaipuCpuSnmp',1),(55,'memory','net','maipu','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.MaipuMemorySnmp',1),(56,'flash','net','maipu','flash','FLASH利用率','flash','1','1','10','m',' ',1),(57,'temperature','net','maipu','temperature','温度','temperature','1','1','10','m',' ',1),(58,'fan','net','maipu','fan','风扇','fan','1','1','10','m',' ',1),(59,'power','net','maipu','power','电源','power','1','1','10','m',' ',1),(60,'voltage','net','maipu','voltage','电压','voltage','1','1','10','m',' ',1),(61,'systemgroup','net','maipu','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(62,'ipmac','net','maipu','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(63,'fdb','net','maipu','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(64,'router','net','maipu','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(65,'interface','net','maipu','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(66,'ping','net','redgiant','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(67,'cpu','net','redgiant','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.RedGiantCpuSnmp',1),(68,'memory','net','redgiant','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.RedGiantMemorySnmp',1),(69,'flash','net','redgiant','flash','FLASH利用率','flash','1','1','10','m',' ',1),(70,'temperature','net','redgiant','temperature','温度','temperature','1','1','10','m',' ',1),(71,'fan','net','redgiant','fan','风扇','fan','1','1','10','m',' ',1),(72,'power','net','redgiant','power','电源','power','1','1','10','m',' ',1),(73,'voltage','net','redgiant','voltage','电压','voltage','1','1','10','m',' ',1),(74,'systemgroup','net','redgiant','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(75,'ipmac','net','redgiant','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(76,'fdb','net','redgiant','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(77,'router','net','redgiant','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(78,'interface','net','redgiant','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(79,'ping','net','northtel','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(80,'cpu','net','northtel','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.NortelCpuSnmp',1),(81,'memory','net','northtel','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.NortelMemorySnmp',1),(82,'flash','net','northtel','flash','FLASH利用率','flash','1','1','10','m',' ',1),(83,'temperature','net','northtel','temperature','温度','temperature','1','1','10','m',' ',1),(84,'fan','net','northtel','fan','风扇','fan','1','1','10','m',' ',1),(85,'power','net','northtel','power','电源','power','1','1','10','m',' ',1),(86,'voltage','net','northtel','voltage','电压','voltage','1','1','10','m',' ',1),(87,'systemgroup','net','northtel','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(88,'ipmac','net','northtel','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(89,'fdb','net','northtel','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(90,'router','net','northtel','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(91,'interface','net','northtel','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(92,'ping','net','dlink','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(93,'cpu','net','dlink','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.DLinkCpuSnmp',1),(94,'memory','net','dlink','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.DLinkMemorySnmp',1),(95,'flash','net','dlink','flash','FLASH利用率','flash','1','1','10','m',' ',1),(96,'temperature','net','dlink','temperature','温度','temperature','1','1','10','m',' ',1),(97,'fan','net','dlink','fan','风扇','fan','1','1','10','m',' ',1),(98,'power','net','dlink','power','电源','power','1','1','10','m',' ',1),(99,'voltage','net','dlink','voltage','电压','voltage','1','1','10','m',' ',1),(100,'systemgroup','net','dlink','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(101,'ipmac','net','dlink','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(102,'fdb','net','dlink','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(103,'router','net','dlink','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(104,'interface','net','dlink','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(105,'ping','net','bdcom','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(106,'cpu','net','bdcom','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.BDComCpuSnmp',1),(107,'memory','net','bdcom','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.BDComMemorySnmp',1),(108,'flash','net','bdcom','flash','FLASH利用率','flash','1','1','10','m','com.afunms.polling.snmp.flash.BDComFlashSnmp',1),(109,'temperature','net','bdcom','temperature','温度','temperature','1','1','10','m','com.afunms.polling.snmp.flash.BDComTemperatureSnmp',1),(110,'fan','net','bdcom','fan','风扇','fan','1','1','10','m',' ',1),(111,'power','net','bdcom','power','电源','power','1','1','10','m',' ',1),(112,'voltage','net','bdcom','voltage','电压','voltage','1','1','10','m',' ',1),(113,'systemgroup','net','bdcom','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(114,'ipmac','net','bdcom','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(115,'fdb','net','bdcom','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(116,'router','net','bdcom','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(117,'interface','net','bdcom','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(118,'ping','host','windows','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(119,'cpu','host','windows','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.WindowsCpuSnmp',1),(120,'disk','host','windows','disk','磁盘信息','disk','1','1','5','m','com.afunms.polling.snmp.disk.WindowsDiskSnmp',1),(121,'service','host','windows','service','服务信息','service','1','1','5','m','com.afunms.polling.snmp.service.WindowsServiceSnmp',1),(122,'physicalmemory','host','windows','physicalmemory','物理内存','physicalmemory','1','1','5','m','com.afunms.polling.snmp.memory.WindowsPhysicalMemorySnmp',1),(123,'virtualmemory','host','windows','virtualmemory','虚拟内存','virtualmemory','1','1','5','m','com.afunms.polling.snmp.memory.WindowsVirtualMemorySnmp',1),(124,'process','host','windows','process','进程信息','process','1','1','5','m','com.afunms.polling.snmp.process.WindowsProcessSnmp',1),(125,'hardware','host','windows','hardware','硬件信息','hardware','1','1','5','m','com.afunms.polling.snmp.device.WindowsDeviceSnmp',1),(126,'storage','host','windows','storage','存储信息','storage','1','1','5','m','com.afunms.polling.snmp.storage.WindowsStorageSnmp',1),(127,'ipmac','host','windows','ipmac','MAC信息表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(128,'interface','host','windows','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(129,'software','host','windows','software','软件信息','software','1','1','5','m','com.afunms.polling.snmp.software.WindowsSoftwareSnmp',1),(130,'systemgroup','host','windows','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(131,'session','db','oracle','session','会话信息','session','1','1','5','m','com.afunms.polling.snmp.oracle.OracleSessionSnmp',1),(132,'tablespace','db','oracle','tablespace','表空间信息','tablespace','1','1','5','m','com.afunms.polling.snmp.oracle.OracleTableSpaceSnmp',1),(133,'rollback','db','oracle','rollback','回滚段信息','rollback','1','1','5','m','com.afunms.polling.snmp.oracle.OracleRollbackSnmp',1),(134,'sysinfo','db','oracle','sysinfo','系统信息','sysinfo','1','1','5','m','com.afunms.polling.snmp.oracle.OracleSysInfoSnmp',1),(135,'gainfo','db','oracle','gainfo','PGA信息和SGA信息','gainfo','1','1','5','m','com.afunms.polling.snmp.oracle.OracleGASnmp',1),(137,'lock','db','oracle','lock','锁信息','lock','1','1','5','m','com.afunms.polling.snmp.oracle.OracleLockSnmp',1),(138,'memory','db','oracle','memory','内存及缓存信息','memory','1','1','5','m','com.afunms.polling.snmp.oracle.OracleMemorySnmp',1),(143,'cursors','db','oracle','cursors','打开的游标','cursors','1','1','5','m','com.afunms.polling.snmp.oracle.OracleCursorsSnmp ',1),(145,'table','db','oracle','table','表信息','table','1','1','5','m','com.afunms.polling.snmp.oracle.OracleTableSnmp',1),(146,'topsql','db','oracle','topsql','最浪费内存的前10个语句','topsql','1','1','5','m','com.afunms.polling.snmp.oracle.OracleTopSqlSnmp',1),(147,'controlfile','db','oracle','controlfile','控制文件','controlfile','1','1','5','m','com.afunms.polling.snmp.oracle.OracleControlFileSnmp ',1),(148,'log','db','oracle','log','日志文件信息','log','1','1','5','m','com.afunms.polling.snmp.oracle.OracleLogSnmp',1),(149,'keepobj','db','oracle','keepobj','固定缓存对象','keepobj','1','1','5','m','com.afunms.polling.snmp.oracle.OracleKeepPOBJSnmp',1),(150,'openmode','db','oracle','openmode','监听状态','openmode','1','1','5','m','com.afunms.polling.snmp.oracle.OracleOpenModeSnmp',1),(151,'extent','db','oracle','extent','字典管理表空间中的Extent总数','extent','1','1','5','m','com.afunms.polling.snmp.oracle.OracleExtentSnmp',1),(152,'dbio','db','oracle','dbio','数据库I/O状况','dbio','1','1','5','m','com.afunms.polling.snmp.oracle.OracleDBIOSnmp ',1),(153,'wait','db','oracle','wait','WAIT状况','wait','1','1','5','m','com.afunms.polling.snmp.oracle.OracleWaitSnmp ',1),(154,'user','db','oracle','user','用户信息','user','1','1','5','m','com.afunms.polling.snmp.oracle.OracleUserSnmp',1),(155,'ping','db','oracle','ping','连通性','ping','1','1','5','m','com.afunms.polling.snmp.oracle.OraclePingSnmp',1),(156,'ping','db','sqlserver','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.sqlserver.collect_ping',1),(157,'sysvalue','db','sqlserver','sysvalue','系统信息','sysvalue','1','1','5','m','com.afunms.polling.snmp.sqlserver.collect_sysvalue',1),(158,'lock','db','sqlserver','lock','锁信息','lock','1','1','5','m','com.afunms.polling.snmp.sqlserver.collect_lock',1),(159,'process','db','sqlserver','process','数据库进程信息','process','1','1','5','m','com.afunms.polling.snmp.sqlserver.collect_process',1),(160,'db','db','sqlserver','db','库信息','db','1','1','5','m','com.afunms.polling.snmp.sqlserver.collect_db',1),(167,'page','db','sqlserver','page','页信息','page','1','1','5','m','com.afunms.polling.snmp.sqlserver.MonitItemsDetail',1),(174,'sysvalue','db','informix','sysvalue','系统信息','sysvalue','1','1','5','m',' ',1),(175,'log','db','informix','log','日志信息','log','1','1','5','m',' ',1),(176,'space','db','informix','space','表空间信息','space','1','1','5','m',' ',1),(177,'config','db','informix','config','配置信息','config','1','1','5','m',' ',1),(178,'session','db','informix','session','会话信息','session','1','1','5','m',' ',1),(179,'lock','db','informix','lock','锁信息','lock','1','1','5','m',' ',1),(180,'io','db','informix','io','IO信息','io','1','1','5','m',' ',1),(181,'profile','db','informix','profile','概要文件信息','profile','1','1','5','m',' ',1),(182,'ping','db','informix','ping','连通性','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(183,'cpu','db','sybase','cpu','CPU信息','cpu','1','1','5','m',' ',1),(184,'ping','db','sybase','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(185,'version','db','sybase','version','版本','version','1','1','5','m',' ',1),(186,'io','db','sybase','io','输入输出','io','1','1','5','m',' ',1),(187,'pack','db','sybase','pack','网络上的数据速率','pack','1','1','5','m',' ',1),(188,'diskpack','db','sybase','diskpack','磁盘速率','diskpack','1','1','5','m',' ',1),(189,'servername','db','sybase','servername','SQL服务器名称','servername','1','1','5','m',' ',1),(190,'sysdevices','db','sybase','sysdevices','转储设备或数据库设备个数','sysdevices','1','1','5','m',' ',1),(191,'lock','db','sybase','lock','锁信息','lock','1','1','5','m',' ',1),(192,'systransactions','db','sybase','systransactions','事务的个数','systransactions','1','1','5','m',' ',1),(193,'totaldatacache','db','sybase','totaldatacache','总数据高速缓存大小','totaldatacache','1','1','5','m',' ',1),(194,'totalphysicalmemory','db','sybase','totalphysicalmemory','总物理内存大小','totalphysicalmemory','1','1','5','m',' ',1),(195,'metadatacache','db','sybase','metadatacache','Metadata缓存','metadatacache','1','1','5','m',' ',1),(196,'procedurecache','db','sybase','procedurecache','存储过程缓存大小','procedurecache','1','1','5','m',' ',1),(197,'totallogicalmemory','db','sybase','totallogicalmemory','总逻辑内存大小',NULL,'1','1','5','m',' ',1),(198,'datahitrate','db','sybase','datahitrate','数据缓存匹配度','datahitrate','1','1','5','m',' ',1),(199,'procedurehitrate','db','sybase','procedurehitrate','存储缓存匹配度','procedurehitrate','1','1','5','m',' ',1),(200,'device','db','sybase','device','转储设备或数据库设备信息','device','1','1','5','m',' ',1),(201,'user','db','sybase','user','当前数据库所有用户的信息','user','1','1','5','m',' ',1),(202,'db','db','sybase','db','数据库大小信息','db','1','1','5','m',' ',1),(203,'servers','db','sybase','servers','远程服务器信息','servers','1','1','5','m',' ',1),(1020,'sysvalue','db','db2','sysvalue','系统信息','sysvalue','1','1','5','m',' ',1),(1021,'space','db','db2','space','表空间信息','space','1','1','5','m',' ',1),(1022,'pool','db','db2','pool','池信息','pool','1','1','5','m',' ',1),(1023,'session','db','db2','session','会话信息','session','1','1','5','m',' ',1),(1024,'lock','db','db2','lock','锁信息','lock','1','1','5','m',' ',1),(1025,'topread','db','db2','topread','读频率高的表','topread','1','1','5','m',' ',1),(1026,'topwrite','db','db2','topwrite','写频率高的表','topwrite','1','1','5','m',' ',1),(1040,'conn','db','db2','conn','连接信息','conn','1','1','5','m',' ',1),(1041,'log','db','db2','log','日志使用信息','log','1','1','5','m',' ',1),(1042,'config','db','mysql','config','配置信息','config','1','1','5','m',' ',1),(1043,'tablestatus','db','mysql','tablestatus','表信息','tablestatus','1','1','5','m',' ',1),(1044,'process','db','mysql','process','进程信息','process','1','1','5','m',' ',1),(1045,'maxusedconnect','db','mysql','maxusedconnect','最大连接数','maxusedconnect','1','1','5','m',' ',1),(1046,'lock','db','mysql','lock','锁信息','lock','1','1','5','m',' ',1),(1047,'keyread','db','mysql','keyread','读索引信息','keyread','1','1','5','m',' ',1),(1048,'slow','db','mysql','slow','慢查询','slow','1','1','5','m',' ',1),(1049,'thread','db','mysql','thread','线程信息','thread','1','1','5','m',' ',1),(1050,'opentable','db','mysql','opentable','打开表信息','opentable','1','1','5','m',' ',1),(1051,'handlerread','db','mysql','handlerread','索引扫描信息','handlerread','1','1','5','m',' ',1),(1052,'variables','db','mysql','variables','变量信息','variables','1','1','5','m',' ',1),(1053,'status','db','mysql','status','状态信息','status','1','1','5','m',' ',1),(1054,'scan','db','mysql','scan','扫描信息','scan','1','1','5','m',' ',1),(1055,'createdtmp','db','mysql','createdtmp','创建的临时表信息','createdtmp','1','1','5','m',' ',1),(1056,'tmptable','db','mysql','tmptable','临时表创建信息','tmptable','1','1','5','m',' ',1),(1057,'ping','service','url','ping','可用性','ping','1','1','5','m','com.afunms.polling.task.WebDataCollector',1),(1061,'connect','service','socket','connect','SOCKET服务检测','connect','1','1','5','m',' ',1),(1062,'send','service','mail','send','发送邮件','send','1','1','5','m',' ',1),(1063,'receieve','service','mail','receieve','接收邮件','receieve','1','1','5','m',' ',1),(1064,'upload','service','ftp','upload','上载服务','upload','1','1','5','m',' ',1),(1065,'download','service','ftp','download','下载服务','download','1','1','5','m',' ',1),(1066,'domain','middleware','weblogic','domain','域信息','domain','1','1','5','m',' ',1),(1067,'queue','middleware','weblogic','queue','队列信息','queue','1','1','5','m',' ',1),(1068,'jdbc','middleware','weblogic','jdbc','JDBC信息','jdbc','1','1','5','m',' ',1),(1069,'webapp','middleware','weblogic','webapp','应用信息','webapp','1','1','5','m',' ',1),(1070,'heap','middleware','weblogic','heap','堆信息','heap','1','1','5','m',' ',1),(1071,'servlet','middleware','weblogic','servlet','SERVLET信息','servlet','1','1','5','m',' ',1),(1072,'server','middleware','weblogic','server','服务信息','server','1','1','5','m',' ',1),(1073,'system','middleware','was','system','性能信息','system','1','1','5','m','com.afunms.polling.snmp.was.WasSysInfoMonitor',1),(1080,'ping','middleware','was','ping','可用性信息','ping','1','1','5','m','com.afunms.polling.snmp.was.WasPing',1),(1081,'ping','middleware','weblogic','ping','可用性信息','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1084,'ping','middleware','tomcat','ping','可用性','ping','1','1','5','m','com.afunms.polling.task.TomcatDataCollector',1),(1086,'system','host','as400','system','系统信息','system','1','1','5','m',' ',1),(1087,'pool','host','as400','pool','池信息','pool','1','1','5','m',' ',1),(1088,'disk','host','as400','disk','磁盘信息','disk','1','1','5','m',' ',1),(1089,'job','host','as400','job','任务信息','job','1','1','5','m',' ',1),(1090,'ping','host','as400','ping','可用性','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1091,'ping','host','linux','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1092,'disk','host','linux','disk','磁盘信息','disk','1','1','5','m','com.afunms.polling.snmp.disk.LinuxDiskSnmp',1),(1093,'service','host','linux','service','服务信息','service','1','1','5','m',' ',1),(1094,'physicalmemory','host','linux','physicalmemory','物理内存','physicalmemory','1','1','5','m','com.afunms.polling.snmp.memory.LinuxPhysicalMemorySnmp',1),(1095,'virtualmemory','host','linux','virtualmemory','虚拟内存','virtualmemory','1','1','5','m',' ',1),(1096,'process','host','linux','process','进程信息','process','1','1','5','m','com.afunms.polling.snmp.process.LinuxProcessSnmp',1),(1097,'hardware','host','linux','hardware','硬件信息','hardware','1','1','5','m','com.afunms.polling.snmp.device.LinuxDeviceSnmp',1),(1098,'storage','host','linux','storage','存储信息','storage','1','1','5','m','com.afunms.polling.snmp.storage.LinuxStorageSnmp',1),(1099,'ipmac','host','linux','ipmac','MAC信息表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(1100,'interface','host','linux','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1101,'software','host','linux','software','软件信息','software','1','1','5','m','com.afunms.polling.snmp.software.LinuxSoftwareSnmp',1),(1102,'systemgroup','host','linux','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(1103,'subsystem','host','as400','subsystem','子系统','subsystem','1','1','5','m',' ',1),(1104,'ping','host','aix','ping','可用性','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1105,'cpudetail','host','aix','cpudetail','CPU利用率','cpu','1','1','5','m',' ',1),(1106,'disk','host','aix','disk','磁盘信息','disk','1','1','1','d',' ',1),(1107,'physicalmemory','host','aix','physicalmemory','物理内存信息','physicalmemory','1','1','5','m',' ',1),(1108,'swapmemory','host','aix','swapmemory','交换内存信息','swapmemory','1','1','5','m',' ',1),(1109,'process','host','aix','process','进程信息','process','1','1','5','m',' ',1),(1110,'pageingspace','host','aix','pageingspace','换页信息','page','1','1','5','m',' ',1),(1111,'ping','host','solaris','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1113,'cpu','net','zte','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.ZTECpuSnmp',1),(1114,'ping','net','zte','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1115,'interface','net','zte','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1116,'ping','host','hpunix','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1117,'route','host','aix','route','路由信息','route','1','1','30','m',' ',1),(1118,'volume','host','aix','volume','卷组信息','volume','1','1','1','d',' ',1),(1119,'errpt','host','aix','errpt','ERRPT日志信息','errpt','1','1','5','m',' ',1),(1120,'service','host','aix','service','服务信息','service','1','1','30','m',' ',1),(1121,'diskio','host','aix','diskio','磁盘IO','diskio','1','1','5','m',' ',1),(1122,'netperf','host','aix','netperf','网络性能','netperf','1','1','5','m',' ',1),(1123,'systemconfig','host','aix','systemconfig','系统配置','systemconfig','1','1','1','d',' ',1),(1124,'ping','net','atm','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1125,'ping','vpn','arraynetworks','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1140,'cpu','vpn','arraynetworks','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.ArrayNetworkCpuSnmp',1),(1141,'systemgroup','vpn','arraynetworks','systemgroup','系统组属性','systemgroup','1','1','5','m','com.afunms.polling.snmp.system.SystemSnmp',1),(1142,'ipmac','vpn','arraynetworks','ipmac','ARP表信息','ipmac','1','1','5','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(1143,'cluster','vpn','arraynetworks','cluster','集群信息','cluster','1','1','1','d','com.afunms.polling.snmp.vpn.ArrayVPNClusterSnmp',1),(1144,'ssl','vpn','arraynetworks','ssl','SSL信息','ssl','1','1','5','m','com.afunms.polling.snmp.vpn.ArrayVPNSSLSnmp',1),(1145,'session','vpn','arraynetworks','session','会话信息','session','1','1','5','m','com.afunms.polling.snmp.vpn.ArrayVPNSessionSnmp',1),(1146,'tcp','vpn','arraynetworks','tcp','TCP连接信息','tcp','1','1','5','m','com.afunms.polling.snmp.vpn.ArrayVPNTCPSnmp',1),(1147,'virtualsite','vpn','arraynetworks','virtualsite','虚拟站点信息','virtualsite','1','1','1','d','com.afunms.polling.snmp.vpn.ArrayVPNVirtualSiteSnmp',1),(1148,'vpn','vpn','arraynetworks','vpn','VPN信息','vpn','1','1','5','m','com.afunms.polling.snmp.vpn.ArrayVPNInforSnmp',1),(1149,'web','vpn','arraynetworks','web','WEB访问信息','web','1','1','5','m','com.afunms.polling.snmp.vpn.ArrayVPNWebSnmp',1),(1150,'virtualclient','vpn','arraynetworks','virtualclient','虚拟客户端信息','virtualclient','1','1','5','m','com.afunms.polling.snmp.vpn.ArrayVPNVClientAppSnmp',1),(1151,'tcs','vpn','arraynetworks','tcs','远程通信信息','tcs','1','1','5','m','com.afunms.polling.snmp.vpn.ArrayVPNTcsSnmp',1),(1152,'totalcount','vpn','arraynetworks','totalcount','综合计数信息','totalcount','1','1','1','d','com.afunms.polling.snmp.vpn.ArrayVPNCountSnmp',1),(1153,'interface','vpn','arraynetworks','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1154,'allgather','host','windows','allgather','文件采集','allgather','0','1','5','m','com.afunms.polling.snmp.LoadWindowsWMIFile',2),(1155,'ping','host','windows','ping','连通率','ping','0','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',2),(1156,'allgather','host','linux','allgather','文件采集','allgather','0','1','5','m','com.afunms.polling.snmp.LoadLinuxFile',2),(1157,'ping','host','linux','ping','连通率','ping','0','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',2),(1158,'allgather','host','hpunix','allgather','文件采集','allgather','0','1','5','m','com.afunms.polling.snmp.LoadHpUnixFile',2),(1159,'ping','host','hpunix','ping','连通率','ping','0','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',2),(1160,'allgather','host','solaris','allgather','文件采集','allgather','1','1','5','m','com.afunms.polling.snmp.LoadSunOSFile',2),(1161,'ping','host','solaris','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',2),(1162,'allgather','host','aix','allgather','文件采集','allgather','0','1','5','m','com.afunms.polling.snmp.LoadAixFile',2),(1163,'ping','host','aix','ping','连通率','ping','0','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',2),(1164,'systemgroup','net','zte','systemgroup','系统信息','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(1165,'router','net','zte','router','路由表信息','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(1166,'ipmac','net','zte','ipmac','ARP信息','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(1167,'fdb','net','zte','fdb','地址转发信息','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(1168,'ping','firewall','dptech','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1169,'interface','firewall','dptech','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1170,'systemgroup','firewall','dptech','systemgroup','系统组属性','systemgroup','1','1','5','m','com.afunms.polling.snmp.system.SystemSnmp',1),(1171,'ping','net','hp','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1172,'cpu','net','hp','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.HPNetCpuSnmp',1),(1173,'systemgroup','net','hp','systemgroup','系统组属性','systemgroup','1','1','5','m','com.afunms.polling.snmp.system.SystemSnmp',1),(1174,'ipmac','net','hp','ipmac','地址转发表信息','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(1175,'fdb','net','hp','fdb','FDB表信息','fdb','1','1','5','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(1176,'router','net','hp','router','路由表信息','router','1','1','10','m','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(1177,'interface','net','hp','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1178,'ping','net','hp','ping','连通率','ping','0','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',3),(1179,'ping','firewall','venus','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1180,'cpu','firewall','venus','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.VenusCpuSnmp',1),(1181,'memory','firewall','venus','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.VenusMemorySnmp',1),(1182,'systemgroup','firewall','venus','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(1183,'ipmac','firewall','venus','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(1184,'fdb','firewall','venus','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(1185,'router','firewall','venus','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(1186,'interface','firewall','venus','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1187,'ping','firewall','secworld','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1188,'cpu','firewall','secworld','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.SecWorldCpuSnmp',1),(1189,'memory','firewall','secworld','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.SecWorldMemorySnmp',1),(1190,'systemgroup','firewall','secworld','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(1191,'ipmac','firewall','secworld','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(1192,'fdb','firewall','secworld','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(1193,'router','firewall','secworld','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(1194,'interface','firewall','secworld','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1195,'ping','firewall','tippingpoint','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1196,'cpu','firewall','tippingpoint','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.VenusCpuSnmp',1),(1197,'memory','firewall','tippingpoint','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.VenusMemorySnmp',1),(1198,'systemgroup','firewall','tippingpoint','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(1199,'ipmac','firewall','tippingpoint','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(1200,'fdb','firewall','tippingpoint','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(1201,'router','firewall','tippingpoint','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(1202,'interface','firewall','tippingpoint','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1203,'ping','net','hillstone','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1204,'cpu','net','hillstone','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.HillStoneCpuSnmp',1),(1205,'memory','net','hillstone','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.HillStoneMemorySnmp',1),(1206,'flash','net','hillstone','flash','FLASH利用率','flash','1','1','10','m',' ',1),(1207,'temperature','net','hillstone','temperature','温度','temperature','1','1','10','m',' ',1),(1208,'fan','net','hillstone','fan','风扇','fan','1','1','10','m',' ',1),(1209,'power','net','hillstone','power','电源','power','1','1','10','m',' ',1),(1210,'voltage','net','hillstone','voltage','电压','voltage','1','1','10','m',' ',1),(1211,'systemgroup','net','hillstone','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(1212,'ipmac','net','hillstone','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(1213,'fdb','net','hillstone','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(1214,'router','net','hillstone','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(1215,'interface','net','hillstone','intererface','接口信息','numbererface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1216,'ping','service','windhcp','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1217,'dhcpscope','service','windhcp','dhcpscope','地址池','dhcpscope','1','1','5','m','com.afunms.polling.snmp.dhcp.PingSnmp',1),(1218,'ping','service','ciscodhcp','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1219,'dhcppool','service','ciscodhcp','dhcppool','地址池','dhcppool','1','1','5','m','null',1),(1222,'ping','storage','hds','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1223,'interface','storage','hds','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1224,'systemgroup','storage','hds','systemgroup','系统组信息','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(1225,'efan','storage','hds','efan','存储设备环境-风扇','efan','1','1','5','m','com.afunms.polling.snmp.fan.HDCEnvironmentFanSnmp',1),(1226,'epower','storage','hds','epower','存储设备环境-电源','epower','1','1','5','m','com.afunms.polling.snmp.power.HDCEnvironmentPowerSnmp',1),(1227,'eenv','storage','hds','eenv','存储设备环境-环境状态','eenv','1','1','5','m','com.afunms.polling.snmp.env.HDCEnvironmentEnvSnmp',1),(1228,'edrive','storage','hds','edrive','存储设备环境-驱动状态','edrive','1','1','5','m','com.afunms.polling.snmp.drive.HDCEnvironmentDriveSnmp',1),(1229,'rcable','storage','hds','rcable','运行状体：内部总线状态','rcable','1','1','5','m','com.afunms.polling.snmp.cache.HDCRunCableSnmp',1),(1230,'rcache','storage','hds','rcache','运行状体：缓存状态','rcache','1','1','5','m','com.afunms.polling.snmp.cache.HDCRunCacheSnmp',1),(1231,'rmemory','storage','hds','rmemory','运行状体：共享内存状态','rmemory','1','1','5','m','com.afunms.polling.snmp.memory.HDCRunMemorySnmp',1),(1232,'rpower','storage','hds','rpower','运行状体：电源状态','rpower','1','1','5','m','com.afunms.polling.snmp.power.HDCRunPowerSnmp',1),(1233,'rbutter','storage','hds','rbutter','运行状体：电池状态','rbutter','1','1','5','m','com.afunms.polling.snmp.battery.HDCRunBatterySnmp',1),(1234,'rfan','storage','hds','rfan','运行状体：风扇状态','rfan','1','1','5','m','com.afunms.polling.snmp.fan.HDCRunFanSnmp',1),(1235,'renv','storage','hds','renv','存储设备环境-环境状态','renv','1','1','5','m','com.afunms.polling.snmp.env.HDCRunEnvSnmp',1),(1236,'event','storage','hds','event','事件信息','event','1','1','5','m','com.afunms.polling.snmp.event.HdcEventlistSnmp',1),(1237,'rcpu','storage','hds','rcpu','运行状体：cpu状态','rcpu','1','1','5','m','com.afunms.polling.snmp.cpu.HDCRunCpuSnmp',1),(1238,'productinfo','storage','hds','productinfo','产品信息','productinfo','1','1','1','d','com.afunms.polling.snmp.system.HdcSysInfoSnmp',1),(1239,'ping','cmts','cisco','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1240,'systemgroup','cmts','cisco','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(1241,'interface','cmts','cisco','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1242,'channelstatus','cmts','cisco','channelstatus','通道状态','channelstatus','1','1','5','m','null',1),(1243,'noise','cmts','cisco','noise','通道信躁比','noise','1','1','5','m','null',1),(1244,'ipmac','cmts','cisco','ipmac','在线用户IPMAC信息','ipmac','1','1','5','m','null',1),(1245,'rspower','cmts','cisco','rspower','电平信息','rspower','1','1','5','m','null',1),(1246,'ping','firewall','topsec','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1247,'cpu','firewall','topsec','cpu','cpu利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.TopSecCpuSnmp',1),(1248,'memory','firewall','topsec','memory','内存利用率','memory','1','1','5','m','com.afunms.polling.snmp.memory.TopSecMemorySnmp',1),(1249,'systemgroup','firewall','topsec','systemgroup','系统组属性','systemgroup','1','1','5','m','com.afunms.polling.snmp.system.SystemSnmp',1),(1250,'ipmac','firewall','topsec','ipmac','ARP信息','ipmac','1','1','5','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(1251,'fdb','firewall','topsec','fdb','FDB信息','fdb','1','1','5','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(1252,'router','firewall','topsec','router','路由表信息','router','1','1','5','m','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(1253,'interface','firewall','topsec','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1254,'ping','firewall','redgiant','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1255,'cpu','firewall','redgiant','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.RedGiantFirewallCpuSnmp',1),(1256,'memory','firewall','redgiant','memory','内存利用率','memory','1','1','5','m','com.afunms.polling.snmp.memory.RedGiantFirewallMemorySnmp',1),(1257,'systemgroup','firewall','redgiant','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(1258,'ipmac','firewall','redgiant','ipmac','ARP信息','ipmac','1','1','5','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(1259,'fdb','firewall','redgiant','fdb','FDB信息','fdb','1','1','5','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(1260,'router','firewall','redgiant','router','路由表信息','router','1','1','1','h','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(1261,'interface','firewall','redgiant','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1262,'ping','firewall','netscreen','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1263,'cpu','firewall','netscreen','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.NetscreenCpuSnmp',1),(1264,'memory','firewall','netscreen','memory','内存利用率','memory','1','1','5','m','com.afunms.polling.snmp.memory.NetscreenMemorySnmp',1),(1265,'systemgroup','firewall','netscreen','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp',1),(1266,'ipmac','firewall','netscreen','ipmac','ARP信息','ipmac','1','1','5','m','com.afunms.polling.snmp.interfaces.ArpSnmp',1),(1267,'fdb','firewall','netscreen','fdb','FDB信息','fdb','1','1','5','m','com.afunms.polling.snmp.interfaces.FdbSnmp',1),(1268,'router','firewall','netscreen','router','路由表信息','router','1','1','5','m','com.afunms.polling.snmp.interfaces.RouterSnmp',1),(1269,'interface','firewall','netscreen','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp',1),(1270,'allgather','virtual','vmware','allgather','采集全部信息','allgather','1','1','5','m','com.afunms.polling.snmp.LoadVMWare',1),(1271,'guestcpu','virtual','vmware','guestcpu','客户机CPU利用率','guestcpu','1','1','5','m','null',1),(1272,'guestmemory','virtual','vmware','guestmemory','客户机内存利用率','guestmemory','1','1','5','m','null',1),(1273,'hba','virtual','vmware','hba','HBA、磁盘信息','hba','1','1','5','m','null',1),(1274,'net','virtual','vmware','net','网络适配器统计信息','net','1','1','5','m','null',1),(1275,'product','virtual','vmware','product','产品信息','product','1','1','5','m','null',1),(1276,'guesthost','virtual','vmware','guesthost','已在系统上配置的虚拟机信息','guesthost','1','1','1','d','null',1),(1277,'guesthba','virtual','vmware','guesthba','虚拟机 HBA 变量','guesthba','1','1','5','m','null',1),(1278,'guestnet','virtual','vmware','guestnet','虚拟机网络变量','guestnet','1','1','5','m','null',1),(1279,'guestcdrom','virtual','vmware','guestcdrom','虚拟 DVD 或 CD-ROM 变量','guestcdrom','1','1','5','m','null',1),(1280,'ping','virtual','vmware','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1346,'rwwncon','storage','hds','rwwncon','wwn控制器状态','rwwncon','1','1','5','m','com.afunms.polling.snmp.hdc.HdcDfWwnController',1),(1347,'rsluncon','storage','hds','rsluncon','slun控制器状态','rsluncon','1','1','5','m','com.afunms.polling.snmp.hdc.HdcDfSlunController',1),(1348,'rsafety','storage','hds','rsafety','slun的wwn安全','rsafety','1','1','5','m','com.afunms.polling.snmp.hdc.HdcDfSlunSafety',1),(1349,'syslist','storage','hds','syslist','系统版本信息','syslist','1','1','1','d','com.afunms.polling.snmp.hdc.HdcDfSystemParameter',1),(1350,'rswitch','storage','hds','rswitch','Lun转换开关','rswitch','1','1','5','m','com.afunms.polling.snmp.hdc.HdcDfLunSwitch',1),(1351,'rluncon','storage','hds','rluncon','lun控制器状态','rluncon','1','1','5','m','com.afunms.polling.snmp.hdc.HdcDfLunSwitchController',1),(1352,'rnumber','storage','hds','rnumber','wwn编号使用状态','rnumber','1','1','5','m','com.afunms.polling.snmp.hdc.HdcDfWwnNumber',1),(1353,'ping','storage','emc','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1354,'raid','storage','emc','raid','RAID组信息','raid','1','1','1','d','com.afunms.polling.snmp.storage.EMCRaidController',1),(1355,'disk','storage','emc','disk','磁盘信息','disk','1','1','5','m','com.afunms.polling.snmp.storage.EMCDiskController',1),(1356,'lunconfig','storage','emc','lunconfig','LUN信息','lunconfig','1','1','1','d','com.afunms.polling.snmp.storage.EMCLUNConfigController',1),(1357,'lunperf','storage','emc','lunperf','LUN性能','lunperf','1','1','5','m','com.afunms.polling.snmp.storage.EMCLUNPerfController',1),(1358,'environment','storage','emc','environment','环境状态信息','environment','1','1','5','m','com.afunms.polling.snmp.storage.EMCEnvController',1),(1359,'hardwarestatus','storage','emc','hardwarestatus','硬件状态信息','hardwarestatus','1','1','5','m','com.afunms.polling.snmp.storage.EMCHardwareController',1),(1360,'system','storage','emc','system','系统信息','system','1','1','1','d','com.afunms.polling.snmp.storage.EMCSystemController',1),(1361,'ping','host','scounixware','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1362,'allgather','host','scounixware','allgather','全部采集','allgather','1','1','5','m','com.afunms.polling.snmp.LoadScoUnixWareFile',1),(1363,'ping','host','scoopenserver','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp',1),(1364,'allgather','host','scoopenserver','allgather','全部采集','allgather','1','1','5','m','com.afunms.polling.snmp.LoadScoOpenServerFile',1);
/*!40000 ALTER TABLE `nms_gather_indicators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_gather_indicators_node`
--

DROP TABLE IF EXISTS `nms_gather_indicators_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_gather_indicators_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `name` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `type` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `subtype` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `alias` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `description` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `category` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `isDefault` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `isCollection` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `poll_interval` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `interval_unit` varchar(5) CHARACTER SET gb2312 DEFAULT NULL,
  `classpath` varchar(200) CHARACTER SET gb2312 DEFAULT ' ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=gbk;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_gather_indicators_node`
--

LOCK TABLES `nms_gather_indicators_node` WRITE;
/*!40000 ALTER TABLE `nms_gather_indicators_node` DISABLE KEYS */;
INSERT INTO `nms_gather_indicators_node` VALUES (1,'53','ping','net','h3c','ping','连通率','ping','1','1','5','m','com.afunms.polling.snmp.ping.PingSnmp'),(2,'53','cpu','net','h3c','cpu','CPU利用率','cpu','1','1','5','m','com.afunms.polling.snmp.cpu.H3CCpuSnmp'),(3,'53','memory','net','h3c','memory','内存利用率','memory','1','1','10','m','com.afunms.polling.snmp.memory.H3CMemorySnmp'),(4,'53','flash','net','h3c','flash','FLASH利用率','flash','1','1','10','m','com.afunms.polling.snmp.flash.H3CFlashSnmp'),(5,'53','temperature','net','h3c','temperature','温度','temperature','1','1','10','m','com.afunms.polling.snmp.temperature.H3CTemperatureSnmp'),(6,'53','fan','net','h3c','fan','风扇','fan','1','1','10','m','com.afunms.polling.snmp.fan.H3CFanSnmp'),(7,'53','power','net','h3c','power','电源','power','1','1','10','m','com.afunms.polling.snmp.power.H3CPowerSnmp'),(8,'53','voltage','net','h3c','voltage','电压','voltage','1','1','10','m','com.afunms.polling.snmp.voltage.H3CVoltageSnmp'),(9,'53','systemgroup','net','h3c','systemgroup','系统组属性','systemgroup','1','1','1','d','com.afunms.polling.snmp.system.SystemSnmp'),(10,'53','ipmac','net','h3c','ipmac','MAC地址表','ipmac','1','1','10','m','com.afunms.polling.snmp.interfaces.ArpSnmp'),(11,'53','fdb','net','h3c','fdb','地址转发表','fdb','1','1','10','m','com.afunms.polling.snmp.interfaces.FdbSnmp'),(12,'53','router','net','h3c','router','路由表','router','1','1','1','d','com.afunms.polling.snmp.interfaces.RouterSnmp'),(13,'53','interface','net','h3c','interface','接口信息','interface','1','1','5','m','com.afunms.polling.snmp.interfaces.InterfaceSnmp');
/*!40000 ALTER TABLE `nms_gather_indicators_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_grapes`
--

DROP TABLE IF EXISTS `nms_grapes`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_grapes` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `thevalue` varchar(2) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_grapes`
--

LOCK TABLES `nms_grapes` WRITE;
/*!40000 ALTER TABLE `nms_grapes` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_grapes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_grapesconfig`
--

DROP TABLE IF EXISTS `nms_grapesconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_grapesconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `supperdir` varchar(200) DEFAULT NULL,
  `subdir` varchar(200) DEFAULT NULL,
  `subfilesum` varchar(100) DEFAULT NULL,
  `filesize` int(5) DEFAULT NULL,
  `sendmobiles` varchar(200) DEFAULT NULL,
  `sendemail` varchar(200) DEFAULT NULL,
  `netid` varchar(100) DEFAULT NULL,
  `mon_flag` int(2) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_grapesconfig`
--

LOCK TABLES `nms_grapesconfig` WRITE;
/*!40000 ALTER TABLE `nms_grapesconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_grapesconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_hint_line`
--

DROP TABLE IF EXISTS `nms_hint_line`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_hint_line` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `line_id` varchar(20) DEFAULT NULL,
  `father_id` varchar(50) DEFAULT NULL,
  `child_id` varchar(50) DEFAULT NULL,
  `xmlfile` varchar(100) DEFAULT NULL,
  `line_name` varchar(100) DEFAULT NULL,
  `width` int(10) DEFAULT NULL,
  `father_xy` varchar(50) DEFAULT NULL,
  `child_xy` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_hint_line`
--

LOCK TABLES `nms_hint_line` WRITE;
/*!40000 ALTER TABLE `nms_hint_line` DISABLE KEYS */;
INSERT INTO `nms_hint_line` VALUES (1,'hl1','hin1','net22','1352977649policy.jsp','',2,'31px,91px','235px,37px');
/*!40000 ALTER TABLE `nms_hint_line` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:09:58
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_hint_node`
--

DROP TABLE IF EXISTS `nms_hint_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_hint_node` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(20) DEFAULT NULL,
  `xml_file` varchar(100) DEFAULT NULL,
  `node_type` varchar(100) DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `alias` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_hint_node`
--

LOCK TABLES `nms_hint_node` WRITE;
/*!40000 ALTER TABLE `nms_hint_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_hint_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_home_module`
--

DROP TABLE IF EXISTS `nms_home_module`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_home_module` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `enName` varchar(30) DEFAULT NULL,
  `chName` varchar(30) NOT NULL,
  `note` varchar(200) DEFAULT NULL,
  `type` int(2) NOT NULL,
  PRIMARY KEY (`id`,`chName`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_home_module`
--

LOCK TABLES `nms_home_module` WRITE;
/*!40000 ALTER TABLE `nms_home_module` DISABLE KEYS */;
INSERT INTO `nms_home_module` VALUES (1,'deviceCaptrue','设备快照',NULL,0),(2,'mainBussiness','关键业务',NULL,0),(3,'netTopo','网络拓扑图',NULL,0),(4,'deviceProperty','设备性能',NULL,0),(5,'alarmInfo','告警信息',NULL,1);
/*!40000 ALTER TABLE `nms_home_module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_home_module_role`
--

DROP TABLE IF EXISTS `nms_home_module_role`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_home_module_role` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `enName` varchar(30) DEFAULT NULL,
  `chName` varchar(30) NOT NULL,
  `role_id` int(3) DEFAULT NULL,
  `dept_id` int(3) DEFAULT NULL,
  `visible` int(3) DEFAULT NULL,
  `note` varchar(200) DEFAULT NULL,
  `type` int(2) NOT NULL,
  PRIMARY KEY (`id`,`chName`)
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_home_module_role`
--

LOCK TABLES `nms_home_module_role` WRITE;
/*!40000 ALTER TABLE `nms_home_module_role` DISABLE KEYS */;
INSERT INTO `nms_home_module_role` VALUES (121,'deviceCaptrue','设备快照',0,0,1,'',0),(122,'mainBussiness','关键业务',0,0,1,'',0),(123,'netTopo','网络拓扑图',0,0,1,'',0),(124,'deviceProperty','设备性能',0,0,1,'',0),(125,'alarmInfo','告警信息',0,0,1,'',1),(131,'deviceCaptrue','设备快照',2,0,1,'',0),(132,'mainBussiness','关键业务',2,0,1,'',0),(133,'netTopo','网络拓扑图',2,0,1,'',0),(134,'deviceProperty','设备性能',2,0,1,'',0),(135,'alarmInfo','告警信息',2,0,1,'',1),(136,'deviceCaptrue','设备快照',4,0,1,'',0),(137,'mainBussiness','关键业务',4,0,1,'',0),(138,'netTopo','网络拓扑图',4,0,1,'',0),(139,'deviceProperty','设备性能',4,0,1,'',0),(140,'alarmInfo','告警信息',4,0,1,'',1);
/*!40000 ALTER TABLE `nms_home_module_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_home_module_user`
--

DROP TABLE IF EXISTS `nms_home_module_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_home_module_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `chName` varchar(30) NOT NULL,
  `enName` varchar(30) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `user_id` varchar(30) DEFAULT NULL,
  `role_id` int(3) DEFAULT NULL,
  `dept_id` int(3) DEFAULT NULL,
  `visible` int(3) DEFAULT NULL,
  `businessids` varchar(30) DEFAULT NULL,
  `note` varchar(200) DEFAULT NULL,
  `type` int(2) NOT NULL,
  PRIMARY KEY (`id`,`chName`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_home_module_user`
--

LOCK TABLES `nms_home_module_user` WRITE;
/*!40000 ALTER TABLE `nms_home_module_user` DISABLE KEYS */;
INSERT INTO `nms_home_module_user` VALUES (91,'设备快照','deviceCaptrue','admin','admin',0,0,1,',2,4,5,','',0),(92,'关键业务','mainBussiness','admin','admin',0,0,1,',2,4,5,','',0),(93,'网络拓扑图','netTopo','admin','admin',0,0,1,',2,4,5,','',0),(94,'设备性能','deviceProperty','admin','admin',0,0,1,',2,4,5,','',0),(95,'告警信息','alarmInfo','admin','admin',0,0,1,',2,4,5,','',1),(96,'设备快照','deviceCaptrue','tet','test',4,0,1,',2,','',0),(97,'关键业务','mainBussiness','tet','test',4,0,1,',2,','',0),(98,'网络拓扑图','netTopo','tet','test',4,0,1,',2,','',0),(99,'设备性能','deviceProperty','tet','test',4,0,1,',2,','',0),(100,'告警信息','alarmInfo','tet','test',4,0,1,',2,','',1);
/*!40000 ALTER TABLE `nms_home_module_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_host_service_group`
--

DROP TABLE IF EXISTS `nms_host_service_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_host_service_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `nodeid` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `ipaddress` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `mon_flag` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `alarm_level` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_host_service_group`
--

LOCK TABLES `nms_host_service_group` WRITE;
/*!40000 ALTER TABLE `nms_host_service_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_host_service_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_host_service_group_config`
--

DROP TABLE IF EXISTS `nms_host_service_group_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_host_service_group_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `name` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `status` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_host_service_group_config`
--

LOCK TABLES `nms_host_service_group_config` WRITE;
/*!40000 ALTER TABLE `nms_host_service_group_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_host_service_group_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_hua3vpncfg`
--

DROP TABLE IF EXISTS `nms_hua3vpncfg`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_hua3vpncfg` (
  `id` int(11) DEFAULT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `fileName` varchar(200) DEFAULT NULL,
  `descri` varchar(200) DEFAULT NULL,
  `backup_time` datetime DEFAULT NULL,
  `file_size` int(11) DEFAULT NULL,
  `bkp_type` varchar(10) DEFAULT NULL,
  `baseline` int(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_hua3vpncfg`
--

LOCK TABLES `nms_hua3vpncfg` WRITE;
/*!40000 ALTER TABLE `nms_hua3vpncfg` DISABLE KEYS */;
INSERT INTO `nms_hua3vpncfg` VALUES (1,'10.10.117.176','D:\\Tomcat6.0\\tjjfgzj\\webapps\\afunms\\cfg\\10.10.117.176_20130308-15-53cfg.cfg','fff','2013-03-08 15:53:00',4,'run',0),(2,'10.10.117.176','D:/Tomcat6.0/tjjfgzj/webapps/afunms/cfg/10.10.117.176_20130308-15-54cfg.cfg','fss','2013-03-08 15:53:00',4,'run',0),(3,'10.10.117.176','D:/Tomcat6.0/tjjfgzj/webapps/afunms/cfg/10.10.117.176_20130308-15-54cfg(2).cfg','fss','2013-03-08 15:53:00',4,'startup',0),(4,'10.10.117.176','D:/Tomcat6.0/tjjfgzj/webapps/afunms/cfg/10.10.117.176_20130308-16-44cfg.cfg','10.10.117.176_20130308-16-44','2013-03-08 16:44:00',5,'run',0),(5,'10.10.117.176','D:\\Tomcat6.0\\tjjfgzj\\webapps\\afunms\\cfg\\10.10.117.176_20130311-15-28cfg.cfg','ffs','2013-03-11 15:28:00',1,'run',0),(6,'10.10.117.176','D:\\Tomcat6.0\\tjjfgzj\\webapps\\afunms\\cfg\\10.10.117.176_20130311-15-29cfg.cfg','fsd','2013-03-11 15:29:00',1,'run',1),(7,'10.10.117.176','D:\\Tomcat6.0\\tjjfgzj\\webapps\\afunms\\cfg\\10.10.117.176_20130311-15-44cfg.cfg','fsd','2013-03-11 15:44:00',4,'run',0),(8,'10.10.117.176','D:/Tomcat6.0/tjjfgzj/webapps/afunms/cfg/10.10.117.176_20130311-18-09cfg.cfg','10.10.117.176_20130311-18-09','2013-03-11 18:09:00',5,'run',0),(9,'10.10.117.176','D:/Tomcat6.0/tjjfgzj/webapps/afunms/cfg/10.10.117.176_20130311-18-28cfg.cfg','ff','2013-03-11 18:23:00',4,'run',0),(10,'10.10.117.176','D:/Tomcat6.0/tjjfgzj/webapps/afunms/cfg/10.10.117.176_20130311-18-39cfg.cfg','10.10.117.176_20130311-18-39','2013-03-11 18:39:00',5,'run',0);
/*!40000 ALTER TABLE `nms_hua3vpncfg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_iis_temp`
--

DROP TABLE IF EXISTS `nms_iis_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_iis_temp` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(15) DEFAULT NULL,
  `entity` varchar(50) DEFAULT NULL,
  `value` varchar(50) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_iis_temp`
--

LOCK TABLES `nms_iis_temp` WRITE;
/*!40000 ALTER TABLE `nms_iis_temp` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_iis_temp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_iisconfig`
--

DROP TABLE IF EXISTS `nms_iisconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_iisconfig` (
  `id` bigint(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(19) DEFAULT NULL,
  `community` varchar(100) DEFAULT NULL,
  `sendmobiles` varchar(100) DEFAULT NULL,
  `mon_flag` int(11) DEFAULT NULL,
  `netid` varchar(100) DEFAULT NULL,
  `sendemail` varchar(100) DEFAULT NULL,
  `sendphone` varchar(100) DEFAULT NULL,
  `supperid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_iisconfig`
--

LOCK TABLES `nms_iisconfig` WRITE;
/*!40000 ALTER TABLE `nms_iisconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_iisconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_iislogconfig`
--

DROP TABLE IF EXISTS `nms_iislogconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_iislogconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `history_row` int(10) DEFAULT NULL,
  `flag` int(2) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `netid` varbinary(50) DEFAULT NULL,
  `supperid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_iislogconfig`
--

LOCK TABLES `nms_iislogconfig` WRITE;
/*!40000 ALTER TABLE `nms_iislogconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_iislogconfig` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:09:59
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_indicators_topo_relation`
--

DROP TABLE IF EXISTS `nms_indicators_topo_relation`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_indicators_topo_relation` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `topo_id` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `indicators_id` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `sindex` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `nodeid` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=gbk;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_indicators_topo_relation`
--

LOCK TABLES `nms_indicators_topo_relation` WRITE;
/*!40000 ALTER TABLE `nms_indicators_topo_relation` DISABLE KEYS */;
INSERT INTO `nms_indicators_topo_relation` VALUES (38,'1','127','','52'),(39,'1','128','','52'),(40,'1','129','','52'),(41,'2','128','','52'),(42,'1','106','','51'),(43,'1','23','','47'),(44,'1','24','','47'),(45,'1','25','','47'),(46,'1','42','','47'),(47,'1','43','','47');
/*!40000 ALTER TABLE `nms_indicators_topo_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ip_change`
--

DROP TABLE IF EXISTS `nms_ip_change`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ip_change` (
  `id` int(10) NOT NULL,
  `address` varchar(20) DEFAULT NULL,
  `ip_long` bigint(10) DEFAULT NULL,
  `message` varchar(100) DEFAULT NULL,
  `tag` tinyint(1) DEFAULT '0',
  `log_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ip_change`
--

LOCK TABLES `nms_ip_change` WRITE;
/*!40000 ALTER TABLE `nms_ip_change` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_ip_change` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ip_district_match`
--

DROP TABLE IF EXISTS `nms_ip_district_match`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ip_district_match` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `relateipaddr` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `node_ip` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `node_name` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `is_online` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `original_district` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `current_district` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `is_match` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ip_district_match`
--

LOCK TABLES `nms_ip_district_match` WRITE;
/*!40000 ALTER TABLE `nms_ip_district_match` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_ip_district_match` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ip_mac`
--

DROP TABLE IF EXISTS `nms_ip_mac`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ip_mac` (
  `id` int(10) NOT NULL,
  `ip_address` varchar(15) DEFAULT NULL,
  `ip_long` bigint(10) DEFAULT NULL,
  `mac` varchar(20) DEFAULT NULL,
  `dept` varchar(50) DEFAULT NULL,
  `room` varchar(10) DEFAULT NULL,
  `person` varchar(30) DEFAULT NULL,
  `tel` varchar(30) DEFAULT NULL,
  `log_time` bigint(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ip_mac`
--

LOCK TABLES `nms_ip_mac` WRITE;
/*!40000 ALTER TABLE `nms_ip_mac` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_ip_mac` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ipaccountingdetail`
--

DROP TABLE IF EXISTS `nms_ipaccountingdetail`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ipaccountingdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pkts` double DEFAULT NULL,
  `byts` double DEFAULT NULL,
  `baseid` int(11) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ipaccountingdetail`
--

LOCK TABLES `nms_ipaccountingdetail` WRITE;
/*!40000 ALTER TABLE `nms_ipaccountingdetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_ipaccountingdetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ipaccountips`
--

DROP TABLE IF EXISTS `nms_ipaccountips`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ipaccountips` (
  `id` int(11) NOT NULL,
  `srcip` varchar(15) CHARACTER SET gb2312 DEFAULT NULL,
  `destip` varchar(15) CHARACTER SET gb2312 DEFAULT NULL,
  `protocol` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `nodeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ipaccountips`
--

LOCK TABLES `nms_ipaccountips` WRITE;
/*!40000 ALTER TABLE `nms_ipaccountips` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_ipaccountips` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ipconfig`
--

DROP TABLE IF EXISTS `nms_ipconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ipconfig` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(30) DEFAULT NULL,
  `discrictid` varchar(100) DEFAULT NULL,
  `deptid` int(10) DEFAULT NULL,
  `employeeid` int(10) DEFAULT NULL,
  `ipdesc` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ipconfig`
--

LOCK TABLES `nms_ipconfig` WRITE;
/*!40000 ALTER TABLE `nms_ipconfig` DISABLE KEYS */;
INSERT INTO `nms_ipconfig` VALUES (1,'10.10.1.1','1',0,0,'222');
/*!40000 ALTER TABLE `nms_ipconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ipdistrict`
--

DROP TABLE IF EXISTS `nms_ipdistrict`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ipdistrict` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `district_id` int(10) DEFAULT NULL,
  `startip` varchar(50) DEFAULT ' ',
  `endip` varchar(50) DEFAULT ' ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ipdistrict`
--

LOCK TABLES `nms_ipdistrict` WRITE;
/*!40000 ALTER TABLE `nms_ipdistrict` DISABLE KEYS */;
INSERT INTO `nms_ipdistrict` VALUES (1,1,'10.10.1.1','10.10.1.254');
/*!40000 ALTER TABLE `nms_ipdistrict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ipmacbase`
--

DROP TABLE IF EXISTS `nms_ipmacbase`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ipmacbase` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `relateipaddr` varchar(30) DEFAULT NULL,
  `ifindex` varchar(30) DEFAULT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `mac` varchar(20) DEFAULT NULL,
  `ifband` int(2) DEFAULT NULL,
  `ifsms` varchar(2) DEFAULT NULL,
  `iftel` varchar(2) DEFAULT NULL,
  `ifemail` varchar(2) DEFAULT NULL,
  `bak` varchar(100) DEFAULT NULL,
  `employee_id` bigint(11) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ipmacbase`
--

LOCK TABLES `nms_ipmacbase` WRITE;
/*!40000 ALTER TABLE `nms_ipmacbase` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_ipmacbase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_ipmacchange`
--

DROP TABLE IF EXISTS `nms_ipmacchange`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ipmacchange` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT,
  `RELATEIPADDR` varchar(30) DEFAULT NULL,
  `IFINDEX` varchar(30) DEFAULT NULL,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `MAC` varchar(20) DEFAULT NULL,
  `changetype` int(2) DEFAULT NULL,
  `detail` varchar(100) DEFAULT NULL,
  `COLLECTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `BAK` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_ipmacchange`
--

LOCK TABLES `nms_ipmacchange` WRITE;
/*!40000 ALTER TABLE `nms_ipmacchange` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_ipmacchange` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:00
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_jboss_history`
--

DROP TABLE IF EXISTS `nms_jboss_history`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_jboss_history` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `jboss_id` int(10) DEFAULT NULL,
  `is_canconnected` int(10) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `mon_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_jboss_history`
--

LOCK TABLES `nms_jboss_history` WRITE;
/*!40000 ALTER TABLE `nms_jboss_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_jboss_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_jboss_realtime`
--

DROP TABLE IF EXISTS `nms_jboss_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_jboss_realtime` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `jboss_id` int(10) DEFAULT NULL,
  `is_canconnected` int(10) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `mon_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sms_sign` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_jboss_realtime`
--

LOCK TABLES `nms_jboss_realtime` WRITE;
/*!40000 ALTER TABLE `nms_jboss_realtime` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_jboss_realtime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_jbossconfig`
--

DROP TABLE IF EXISTS `nms_jbossconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_jbossconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `alias` varchar(100) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(50) DEFAULT NULL,
  `port` int(20) DEFAULT NULL,
  `flag` int(10) DEFAULT NULL,
  `sendmobiles` varchar(100) DEFAULT NULL,
  `sendemail` varchar(100) DEFAULT NULL,
  `sendphone` varchar(100) DEFAULT NULL,
  `netid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_jbossconfig`
--

LOCK TABLES `nms_jbossconfig` WRITE;
/*!40000 ALTER TABLE `nms_jbossconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_jbossconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_macconfig`
--

DROP TABLE IF EXISTS `nms_macconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_macconfig` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mac` varchar(30) DEFAULT NULL,
  `discrictid` varchar(100) DEFAULT NULL,
  `deptid` int(10) DEFAULT NULL,
  `employeeid` int(10) DEFAULT NULL,
  `macdesc` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_macconfig`
--

LOCK TABLES `nms_macconfig` WRITE;
/*!40000 ALTER TABLE `nms_macconfig` DISABLE KEYS */;
INSERT INTO `nms_macconfig` VALUES (1,'00:0f:e2:49:fc:47','1',0,0,'00:0f:e2:49:fc:47');
/*!40000 ALTER TABLE `nms_macconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_machineoperatelog`
--

DROP TABLE IF EXISTS `nms_machineoperatelog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_machineoperatelog` (
  `id` int(11) DEFAULT NULL,
  `ip_address` varchar(30) DEFAULT NULL,
  `action` int(11) DEFAULT NULL,
  `oper_time` datetime DEFAULT NULL,
  `oper_user` varchar(20) DEFAULT NULL,
  `oper_id` varchar(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_machineoperatelog`
--

LOCK TABLES `nms_machineoperatelog` WRITE;
/*!40000 ALTER TABLE `nms_machineoperatelog` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_machineoperatelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_machistory`
--

DROP TABLE IF EXISTS `nms_machistory`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_machistory` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT,
  `RELATEIPADDR` varchar(30) DEFAULT NULL,
  `IFINDEX` varchar(30) DEFAULT NULL,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `MAC` varchar(20) DEFAULT NULL,
  `thevalue` varchar(1) DEFAULT '0',
  `COLLECTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_machistory`
--

LOCK TABLES `nms_machistory` WRITE;
/*!40000 ALTER TABLE `nms_machistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_machistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_manage_nodeconfigure`
--

DROP TABLE IF EXISTS `nms_manage_nodeconfigure`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_manage_nodeconfigure` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `text` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `father_id` int(10) DEFAULT NULL,
  `descn` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=gbk;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_manage_nodeconfigure`
--

LOCK TABLES `nms_manage_nodeconfigure` WRITE;
/*!40000 ALTER TABLE `nms_manage_nodeconfigure` DISABLE KEYS */;
INSERT INTO `nms_manage_nodeconfigure` VALUES (1,'document','文档',0,NULL),(2,'hardware','硬件',0,'打'),(3,'software','软件',0,''),(4,'ip','IP',0,''),(5,'allmend','所有补丁',0,''),(6,'allserial','所有序列号',0,''),(7,'doc_manage','管理类',1,NULL),(8,'doc_skill','技术类',1,NULL),(9,'doc_project','工程类',1,NULL),(10,'net_router','路由器',2,NULL),(11,'net_switch','交换机',2,'DFD'),(12,'net_server','服务器',2,NULL),(13,'cabinets','机柜',2,NULL),(14,'desktop	','台式机',2,NULL),(15,'notemachine','笔记本',2,'RER'),(16,'printers','打印机',2,NULL),(17,'scanners','扫描仪',2,NULL),(18,'tapelibrary','磁带库',2,NULL),(19,'raid','磁盘阵列',2,NULL),(20,'opticalswitch','光纤交换机',2,NULL),(21,'loadbalancing','负载均衡',2,NULL),(22,'ids','入侵检测',2,NULL),(23,'firewall','防火墙',2,NULL),(24,'kvm','KVM',2,NULL),(25,'ups','UPS',2,NULL),(26,'soft_independent','自主开发软件',3,NULL),(27,'soft_offshore','外包开发软件',3,NULL),(28,'soft_business','商业软件',3,NULL),(29,'soft_os','操作系统',3,NULL),(30,'soft_db','数据库',3,NULL),(31,'soft_midware','中间件',3,NULL),(32,'soft_tools','工具软件',3,NULL);
/*!40000 ALTER TABLE `nms_manage_nodeconfigure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_manage_nodetype`
--

DROP TABLE IF EXISTS `nms_manage_nodetype`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_manage_nodetype` (
  `id` int(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `text` varchar(100) DEFAULT NULL,
  `father_id` int(10) DEFAULT NULL,
  `table_name` varchar(50) DEFAULT NULL,
  `category` varchar(100) DEFAULT NULL,
  `node_tag` varchar(20) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `deceive_num` varchar(100) DEFAULT '0',
  `img_url` varchar(100) DEFAULT NULL,
  `is_have_child` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_manage_nodetype`
--

LOCK TABLES `nms_manage_nodetype` WRITE;
/*!40000 ALTER TABLE `nms_manage_nodetype` DISABLE KEYS */;
INSERT INTO `nms_manage_nodetype` VALUES (1,'net_router','路由器',0,'topo_host_node','1','net','/perform.do?action=monitornodelist&flag=1&category=net_router','0',NULL,'0'),(2,'net_switch','交换机',0,'topo_host_node','2,3,7','net','/perform.do?action=monitornodelist&flag=1&category=net_switch','0',NULL,'0'),(3,'middleware','中间件',0,NULL,NULL,'mid','/middleware.do?action=list&flag=1&category=middleware','0',NULL,'1'),(4,'domino','Domino',3,'nms_dominoconfig','','dom','/middleware.do?action=list&flag=1&category=domino','0',NULL,'0'),(5,'net_server','服务器',0,'topo_host_node','4','net','/perform.do?action=monitornodelist&flag=1&category=net_server','0',NULL,'0'),(6,'dbs','数据库',0,'app_db_node','','dbs','/db.do?action=list&flag=1','0',NULL,'0'),(7,'safeequip','安全设备',0,NULL,NULL,'saf','/perform.do?action=monitornodelist&category=safeequip','0',NULL,'1'),(8,'tomcat','Tomcat',3,'app_tomact_node','','tom','/middleware.do?action=list&flag=1&category=tomcat','0',NULL,'0'),(9,'cics','CICS',3,'nms_cicsconfig','','cic','/middleware.do?action=list&flag=1&category=cics','0',NULL,'0'),(10,'mqs','MQ',3,'nms_mqconfig','','mqs','/middleware.do?action=list&flag=1&category=mq','0',NULL,'0'),(11,'was','WAS',3,'nms_wasconfig','','was','/middleware.do?action=list&flag=1&category=was','0',NULL,'0'),(12,'weblogic','Weblogic',3,'nms_weblogicconfig','','web','/middleware.do?action=list&flag=1&category=weblogic','0',NULL,'0'),(13,'net_firewall','防火墙',7,'topo_host_node','8','net','/perform.do?action=monitornodelist','0',NULL,'0'),(14,'services','服务',0,NULL,NULL,'ser','/service.do?action=list&flag=1','0',NULL,'1'),(15,'mail','邮件服务',14,'nms_emailmonitorconf','','mai','/service.do?action=list&flag=1&category=mail','0',NULL,'0'),(16,'ftp','FTP服务',14,'nms_ftpconfig','','ftp','/service.do?action=list&flag=1&category=ftp','0',NULL,'0'),(17,'wes','WEB服务',14,'nms_urlconfig','','wes','/service.do?action=list&flag=1&category=web','0',NULL,'0'),(19,'ups','UPS',0,'app_ups_node',NULL,'ups','/ups.do?action=list&jp=1&flag=1','0',NULL,'0'),(20,'iis','IIS',3,'nms_iisconfig','','iis','/middleware.do?action=list&flag=1&category=iis','0',NULL,'0'),(21,'socket','Socket服务',14,'nms_portservice','','soc','/service.do?action=list&flag=1&category=portservice','0',NULL,'0'),(22,'bussiness','业务',0,'topo_manage_xml',NULL,'bus','/businessview.do?action=list&flag=1','0',NULL,'0'),(24,'interface','接口',0,'nms_businessnode',NULL,'int','/perform.do?action=monitornodelist','0',NULL,'0'),(25,'net_storage','存储',0,'topo_host_node','14','net','/perform.do?action=monitornodelist&flag=1&category=net_storage','0',NULL,'0'),(26,'jboss','JBoss',3,'nms_jbossconfig',NULL,'jbo','/middleware.do?action=list&flag=1&category=jboss','0',NULL,'0'),(27,'apache','Apache',3,NULL,NULL,'apa','/middleware.do?action=list&flag=1&category=apache','0',NULL,'0'),(28,'net_atm','ATM',0,'topo_host_node','9','net','/perform.do?action=monitornodelist&flag=1&category=net_atm','0',NULL,'0'),(29,'net_gateway','邮件安全网关',0,'topo_host_node','10','net','/perform.do?action=monitornodelist&flag=1&category=net_gateway','0',NULL,'0'),(30,'net_f5','负载均衡',0,'topo_host_node','11','net','/perform.do?action=monitornodelist&flag=1&category=net_f5','0',NULL,'0'),(31,'net_vpn','VPN',0,'topo_host_node','12','net','/perform.do?action=monitornodelist&flag=1&category=net_vpn','0',NULL,'0'),(34,'net_cmts','CMTS',0,'topo_host_node','13','net','/perform.do?action=monitornodelist&flag=1&category=net_cmts','0',NULL,'0'),(35,'virtual','虚拟化',0,'','15','net','/perform.do?action=monitornodelist&flag=1&category=net_virtual','0',NULL,'1'),(36,'net_vmware','VMWare',35,'topo_host_node','15','net','/perform.do?action=monitornodelist&flag=1&category=net_vmware','0',NULL,'0');
/*!40000 ALTER TABLE `nms_manage_nodetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_mediaplayer`
--

DROP TABLE IF EXISTS `nms_mediaplayer`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_mediaplayer` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `descr` varchar(255) DEFAULT NULL,
  `bsid` int(11) DEFAULT NULL,
  `operid` int(11) DEFAULT NULL,
  `dotime` varchar(50) DEFAULT NULL,
  `fileName` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_mediaplayer`
--

LOCK TABLES `nms_mediaplayer` WRITE;
/*!40000 ALTER TABLE `nms_mediaplayer` DISABLE KEYS */;
INSERT INTO `nms_mediaplayer` VALUES (2,'ERP视频','ERP视频',1,4,'2011-12-15 13:24:28','video_20111215131932.flv'),(3,'OA视频','OA视频',1,4,'2011-12-15 13:27:14','video_20111215132709.flv'),(5,'文件类型转换','测试',1,4,'2011-12-20 13:02:43','nyfz_20111220130111.flv');
/*!40000 ALTER TABLE `nms_mediaplayer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_menu`
--

DROP TABLE IF EXISTS `nms_menu`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_menu` (
  `id` bigint(11) NOT NULL,
  `func_desc` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `ch_desc` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `level_desc` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `father_node` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `url` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `img_url` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `is_current_window` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_menu`
--

LOCK TABLES `nms_menu` WRITE;
/*!40000 ALTER TABLE `nms_menu` DISABLE KEYS */;
INSERT INTO `nms_menu` VALUES (1,'0A','资源','1',NULL,NULL,NULL,'0'),(2,'0A0A','拓扑','2','0A',NULL,NULL,'0'),(3,'0A0A01','网络拓扑','3','0A0A','topology/network/index.jsp','resource/image/icon_cloud.gif','1'),(4,'0A0A02','主机服务器','3','0A0A','topology/server/index.jsp','resource/image/bmgl.GIF','1'),(5,'0A0B','设备维护','2','0A',NULL,NULL,'0'),(6,'0A0B02','添加设备','3','0A0B','network.do?action=ready_add','resource/image/addDevice.gif','0'),(7,'0A0B01','设备列表','3','0A0B','network.do?action=list&jp=1','resource/image/icon_detail.gif','0'),(8,'0A0B03','端口配置','3','0A0B','portconfig.do?action=list&jp=1','resource/image/manageDev.gif','0'),(9,'0A0B04','链路信息','3','0A0B','link.do?action=list&jp=1','resource/image/infopoint.gif','0'),(10,'0A0C','性能监视','2','0A',NULL,NULL,'0'),(11,'0A0C01','监视对象一览表','3','0A0C','network.do?action=monitornodelist&jp=1','resource/image/hostl.gif','0'),(12,'0A0C02','指标全局阀值一览表','3','0A0C','network.do?action=monitornodelist&jp=1','resource/image/desktops.gif','0'),(13,'0A0D','IP/MAC资源','2','0A',NULL,NULL,'0'),(14,'0A0D01','端口-IP-MAC基线','3','0A0D','network.do?action=monitornodelist&jp=1','resource/image/mac.jpg','0'),(15,'0A0D02','当前MAC信息','3','0A0D','moid.do?action=allmoidlist&jp=1','resource/image/maccurrent.gif','0'),(16,'0A0D03','MAC变更历史','3','0A0D','moid.do?action=allmoidlist&jp=1','resource/image/viewmac.gif','0'),(17,'0A0E','视图管理','2','0A',NULL,NULL,'0'),(18,'0A0E01','视图编辑','3','0A0E','customxml.do?action=list&jp=1','resource/image/mkdz.gif','0'),(19,'0A0E02','视图展示','3','0A0E','topology/view/custom.jsp','resource/image/zcbf.gif','1'),(20,'0A0F','设备面板配置管理','2','0A',NULL,NULL,'0'),(21,'0A0F01','面板模板编辑','3','0A0F','panel.do?action=showaddpanel&jp=1','resource/image/editicon.gif','0'),(22,'0A0F02','设备面板编辑','3','0A0F','network.do?action=panelnodelist&jp=1','resource/image/manageDev.gif','0'),(23,'0B','告警','1',NULL,NULL,NULL,'0'),(24,'0B0A','告警浏览','2','0B',NULL,NULL,'0'),(25,'0B0A01','告警列表','3','0B0A','event.do?action=list&jp=1','resource/image/sysloglist16.gif','0'),(26,'0B0A02','存在告警的设备','3','0B0A','alarm/event/alarmnodelist.jsp','resource/image/cancelMng.gif','0'),(27,'0B0B','告警统计','2','0B',NULL,NULL,'0'),(28,'0B0B01','按业务分布','3','0B0B','event.do?action=businesslist&jp=1','resource/image/reportList-16.gif','0'),(29,'0B0B02','按设备分布','3','0B0B','event.do?action=equipmentlist&jp=1','resource/image/reportsyspara-16.gif','0'),(30,'0B0C','Trap管理','2','0B',NULL,NULL,'0'),(31,'0B0C01','浏览Trap','3','0B0C','trap.do?action=list&jp=1','resource/image/integratedReports-16.gif','0'),(32,'0B0D','Syslog管理','2','0B',NULL,NULL,'0'),(33,'0B0D01','浏览Syslog','3','0B0D','netsyslog.do?action=list&jp=1','resource/image/syslog.gif','0'),(34,'0B0D02','过滤规则','3','0B0D','netsyslog.do?action=filter&jp=1','resource/image/filter16.gif','0'),(35,'0C','报表','1',NULL,NULL,NULL,'0'),(36,'0C0A','报表浏览','2','0C',NULL,NULL,'0'),(37,'0C0A01','网络设备报表','3','0C0A','netreport.do?action=list&jp=1','resource/image/graph1.png','0'),(38,'0C0A02','服务器报表','3','0C0A','hostreport.do?action=list&jp=1','resource/image/viewreport.gif','0'),(39,'0D','应用','1',NULL,NULL,NULL,'0'),(40,'0D0A','数据库管理','2','0D',NULL,NULL,'0'),(41,'0D0A01','数据库类型管理','3','0D0A','dbtype.do?action=list','resource/image/dbtype.gif','0'),(42,'0D0A02','数据库监视','3','0D0A','db.do?action=list&jp=1','resource/image/db.gif','0'),(43,'0D0A03','Oracle告警设置','3','0D0A','oraspace.do?action=list&jp=1','resource/image/oracle.gif','0'),(44,'0D0A04','SQLServer告警设置','3','0D0A','sqldbconfig.do?action=list&jp=1','resource/image/sqlserver.gif','0'),(45,'0D0A05','DB2告警设置','3','0D0A','db2config.do?action=list&jp=1','resource/image/db2.gif','0'),(46,'0D0A06','Sybase告警设置','3','0D0A','sybaseconfig.do?action=list&jp=1','resource/image/sybase.gif','0'),(47,'0D0B','服务管理','2','0D',NULL,NULL,'0'),(48,'0D0B01','FTP服务监视','3','0D0B','user.do?action=list&jp=1','resource/image/ftp.jpg','0'),(49,'0D0B02','Email服务监视','3','0D0B','role.do?action=list&jp=1','resource/image/friend.gif','0'),(50,'0D0B03','主机进程监视','3','0D0B','process.do?action=list&jp=1','resource/image/add-services.gif','0'),(51,'0D0B04','WEB访问服务监视','3','0D0B','web.do?action=list&jp=1','/resource/image/www.jpg','0'),(52,'0D0B05','Grapes监视','3','0D0B','grapes.do?action=list&jp=1','/resource/image/tomcat.gif','0'),(53,'0D0B06','Radar监视','3','0D0B','radar.do?action=list&jp=1','/resource/image/tomcat.gif','0'),(54,'0D0B07','Plot监视','3','0D0B','plot.do?action=list&jp=1','/resource/image/tomcat.gif','0'),(55,'0D0C','中间件管理','2','0D',NULL,NULL,'0'),(56,'0D0C01','MQ监视','3','0D0C','mq.do?action=list&jp=1','/resource/image/mq.jpg','0'),(57,'0D0C02','MQ告警设置','3','0D0C','mqchannel.do?action=list&jp=1','/resource/image/setalert.gif','0'),(58,'0D0C03','Domino监视','3','0D0C','domino.do?action=list&jp=1','/resource/image/domino.gif','0'),(59,'0D0C04','WAS监视','3','0D0C','was.do?action=list&jp=1','/resource/image/webphere.gif','0'),(60,'0D0C05','Weblogic监视','3','0D0C','weblogic.do?action=list&jp=1','/resource/image/bea.gif','0'),(61,'0D0C06','Tomcat监视','3','0D0C','tomcat.do?action=list&jp=1','/resource/image/tomcat.gif','0'),(62,'0E','系统管理','1',NULL,NULL,NULL,'0'),(63,'0E0A','资源管理','2','0E',NULL,NULL,'0'),(64,'0E0A01','SNNP模板','3','0E0A','snmp.do?action=list','resource/image/editicon.gif','0'),(65,'0E0A02','设备厂商','3','0E0A','producer.do?action=list&jp=1','resource/image/device_vendor-16.gif','0'),(66,'0E0A03','设备型号','3','0E0A','devicetype.do?action=list&jp=1','resource/image/device_type-16.gif','0'),(67,'0E0A04','服务','3','0E0A','service.do?action=list&jp=1','resource/image/vo_service_16.gif','0'),(68,'0E0B','用户管理','2','0E',NULL,NULL,'0'),(69,'0E0B01','用户','3','0E0B','user.do?action=list&jp=1','resource/image/zxry.GIF','0'),(70,'0E0B02','角色','3','0E0B','role.do?action=list&jp=1','resource/image/jsfp.GIF','0'),(71,'0E0B03','部门','3','0E0B','dept.do?action=list&jp=1','resource/image/bmgl.GIF','0'),(72,'0E0B04','职位','3','0E0B','position.do?action=list&jp=1','resource/image/jswh.GIF','0'),(73,'0E0B05','权限设置','3','0E0B','admin.do?action=list&jp=1','resource/image/sqgl.GIF','0'),(74,'0E0B06','修改密码','3','0E0B','system/user/inputpwd.jsp','resource/image/xgmm.GIF','0'),(75,'0E0C','系统配置','2','0E',NULL,NULL,'0'),(76,'0E0C01','业务分类','3','0E0C','business.do?action=list&jp=1','resource/image/mkdz.gif','0'),(77,'0E0C02','操作日志','3','0E0C','syslog.do?action=list&jp=1','resource/image/zcbf.gif','0'),(78,'0E0C03','告警邮箱设置','3','0E0C','alertemail.do?action=list&jp=1','resource/image/friend.gif','0'),(79,'0E0C04','TFTP设置','3','0E0C','tftpserver.do?action=list&jp=1','resource/image/ftp.jpg','0');
/*!40000 ALTER TABLE `nms_menu` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:00
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_mq_temp`
--

DROP TABLE IF EXISTS `nms_mq_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_mq_temp` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(15) DEFAULT NULL,
  `entity` varchar(50) DEFAULT NULL,
  `subentity` varchar(50) DEFAULT NULL,
  `sindex` varchar(50) DEFAULT NULL,
  `value` varchar(600) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_mq_temp`
--

LOCK TABLES `nms_mq_temp` WRITE;
/*!40000 ALTER TABLE `nms_mq_temp` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_mq_temp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_mqchannelconfig`
--

DROP TABLE IF EXISTS `nms_mqchannelconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_mqchannelconfig` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(50) DEFAULT NULL,
  `chlindex` int(20) DEFAULT NULL,
  `chlname` varchar(50) DEFAULT NULL,
  `linkuse` varchar(50) DEFAULT NULL,
  `sms` int(2) DEFAULT NULL,
  `bak` varchar(1000) DEFAULT NULL,
  `reportflag` int(2) DEFAULT NULL,
  `connipaddress` varchar(50) DEFAULT NULL,
  `netid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_mqchannelconfig`
--

LOCK TABLES `nms_mqchannelconfig` WRITE;
/*!40000 ALTER TABLE `nms_mqchannelconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_mqchannelconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_mqconfig`
--

DROP TABLE IF EXISTS `nms_mqconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_mqconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(19) DEFAULT NULL,
  `managername` varchar(100) DEFAULT NULL,
  `portnum` int(11) DEFAULT NULL,
  `sendmobiles` varchar(100) DEFAULT NULL,
  `mon_flag` int(11) DEFAULT NULL,
  `netid` varchar(100) DEFAULT NULL,
  `sendemail` varchar(100) DEFAULT NULL,
  `sendphone` varchar(100) DEFAULT NULL,
  `supperid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_mqconfig`
--

LOCK TABLES `nms_mqconfig` WRITE;
/*!40000 ALTER TABLE `nms_mqconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_mqconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_mysqlinfo`
--

DROP TABLE IF EXISTS `nms_mysqlinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_mysqlinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `variable_name` varchar(200) DEFAULT NULL,
  `value` varchar(200) DEFAULT NULL,
  `dbname` varchar(200) DEFAULT NULL,
  `typename` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_mysqlinfo`
--

LOCK TABLES `nms_mysqlinfo` WRITE;
/*!40000 ALTER TABLE `nms_mysqlinfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_mysqlinfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_netnodecfgfile`
--

DROP TABLE IF EXISTS `nms_netnodecfgfile`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_netnodecfgfile` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `recordtime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_netnodecfgfile`
--

LOCK TABLES `nms_netnodecfgfile` WRITE;
/*!40000 ALTER TABLE `nms_netnodecfgfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_netnodecfgfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_netsyslog`
--

DROP TABLE IF EXISTS `nms_netsyslog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_netsyslog` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `hostname` varchar(100) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `facility` int(10) DEFAULT NULL,
  `priority` int(10) DEFAULT NULL,
  `facilityname` varchar(50) DEFAULT NULL,
  `priorityname` varchar(50) DEFAULT NULL,
  `recordtime` timestamp NULL DEFAULT NULL,
  `businessid` varchar(50) DEFAULT NULL,
  `category` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_netsyslog`
--

LOCK TABLES `nms_netsyslog` WRITE;
/*!40000 ALTER TABLE `nms_netsyslog` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_netsyslog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_netsyslogalarmkey_node`
--

DROP TABLE IF EXISTS `nms_netsyslogalarmkey_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_netsyslogalarmkey_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(100) DEFAULT NULL,
  `keywords` varchar(100) DEFAULT NULL,
  `levels` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_netsyslogalarmkey_node`
--

LOCK TABLES `nms_netsyslogalarmkey_node` WRITE;
/*!40000 ALTER TABLE `nms_netsyslogalarmkey_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_netsyslogalarmkey_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_netsyslogrule`
--

DROP TABLE IF EXISTS `nms_netsyslogrule`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_netsyslogrule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `facility` varchar(100) DEFAULT NULL,
  `priority` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_netsyslogrule`
--

LOCK TABLES `nms_netsyslogrule` WRITE;
/*!40000 ALTER TABLE `nms_netsyslogrule` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_netsyslogrule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_netsyslogrule_node`
--

DROP TABLE IF EXISTS `nms_netsyslogrule_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_netsyslogrule_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(100) DEFAULT NULL,
  `facility` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=228 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_netsyslogrule_node`
--

LOCK TABLES `nms_netsyslogrule_node` WRITE;
/*!40000 ALTER TABLE `nms_netsyslogrule_node` DISABLE KEYS */;
INSERT INTO `nms_netsyslogrule_node` VALUES (11,'1',''),(63,'92',''),(64,'93',''),(70,'105',''),(71,'106',''),(214,'252',''),(226,'8',''),(227,'53','');
/*!40000 ALTER TABLE `nms_netsyslogrule_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_node_agent`
--

DROP TABLE IF EXISTS `nms_node_agent`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_node_agent` (
  `nodeid` int(11) NOT NULL,
  `agentid` int(11) NOT NULL,
  PRIMARY KEY (`nodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_node_agent`
--

LOCK TABLES `nms_node_agent` WRITE;
/*!40000 ALTER TABLE `nms_node_agent` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_node_agent` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:01
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_node_depend`
--

DROP TABLE IF EXISTS `nms_node_depend`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_node_depend` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(50) DEFAULT NULL,
  `location` varchar(50) DEFAULT NULL,
  `xml` varchar(50) DEFAULT NULL,
  `alias` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_node_depend`
--

LOCK TABLES `nms_node_depend` WRITE;
/*!40000 ALTER TABLE `nms_node_depend` DISABLE KEYS */;
INSERT INTO `nms_node_depend` VALUES (4,'mqs1','424px,340px','businessmap1349829371.jsp','mqtest');
/*!40000 ALTER TABLE `nms_node_depend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_nodeconfig`
--

DROP TABLE IF EXISTS `nms_nodeconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_nodeconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nodeid` int(11) DEFAULT NULL,
  `hostname` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `sysname` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `serialNumber` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `cSDVersion` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `numberOfProcessors` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `mac` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_nodeconfig`
--

LOCK TABLES `nms_nodeconfig` WRITE;
/*!40000 ALTER TABLE `nms_nodeconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_nodeconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_nodecpuconfig`
--

DROP TABLE IF EXISTS `nms_nodecpuconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_nodecpuconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nodeid` int(11) DEFAULT NULL,
  `dataWidth` varchar(255) DEFAULT NULL,
  `processorId` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `l2CacheSize` varchar(255) DEFAULT NULL,
  `l2CacheSpeed` varchar(255) DEFAULT NULL,
  `descrOfProcessors` varchar(255) DEFAULT NULL,
  `processorType` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `processorSpeed` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_nodecpuconfig`
--

LOCK TABLES `nms_nodecpuconfig` WRITE;
/*!40000 ALTER TABLE `nms_nodecpuconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_nodecpuconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_nodediskconfig`
--

DROP TABLE IF EXISTS `nms_nodediskconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_nodediskconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nodeid` int(11) DEFAULT NULL,
  `bytesPerSector` varchar(255) DEFAULT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `interfaceType` varchar(255) DEFAULT NULL,
  `sizes` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_nodediskconfig`
--

LOCK TABLES `nms_nodediskconfig` WRITE;
/*!40000 ALTER TABLE `nms_nodediskconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_nodediskconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_nodememconfig`
--

DROP TABLE IF EXISTS `nms_nodememconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_nodememconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nodeid` int(11) DEFAULT NULL,
  `totalVisibleMemorySize` varchar(255) DEFAULT NULL,
  `totalVirtualMemorySize` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_nodememconfig`
--

LOCK TABLES `nms_nodememconfig` WRITE;
/*!40000 ALTER TABLE `nms_nodememconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_nodememconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_passwordaudit`
--

DROP TABLE IF EXISTS `nms_passwordaudit`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_passwordaudit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(15) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `username` varchar(11) DEFAULT NULL,
  `oldpassword` varchar(50) DEFAULT NULL,
  `newpassword` varchar(50) DEFAULT NULL,
  `dotime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bak` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_passwordaudit`
--

LOCK TABLES `nms_passwordaudit` WRITE;
/*!40000 ALTER TABLE `nms_passwordaudit` DISABLE KEYS */;
INSERT INTO `nms_passwordaudit` VALUES (1,'172.25.25.240',4,'系统管理员','2','2','2012-02-07 00:58:48','');
/*!40000 ALTER TABLE `nms_passwordaudit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_perf_panel_indicators`
--

DROP TABLE IF EXISTS `nms_perf_panel_indicators`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_perf_panel_indicators` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `panelName` varchar(30) NOT NULL,
  `indicatorName` varchar(40) DEFAULT NULL,
  `indicatorDesc` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`,`panelName`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_perf_panel_indicators`
--

LOCK TABLES `nms_perf_panel_indicators` WRITE;
/*!40000 ALTER TABLE `nms_perf_panel_indicators` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_perf_panel_indicators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_performance_panel`
--

DROP TABLE IF EXISTS `nms_performance_panel`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_performance_panel` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `deviceId` varchar(40) DEFAULT NULL,
  `deviceType` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_performance_panel`
--

LOCK TABLES `nms_performance_panel` WRITE;
/*!40000 ALTER TABLE `nms_performance_panel` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_performance_panel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_plot`
--

DROP TABLE IF EXISTS `nms_plot`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_plot` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `thevalue` varchar(2) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_plot`
--

LOCK TABLES `nms_plot` WRITE;
/*!40000 ALTER TABLE `nms_plot` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_plot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_plotconfig`
--

DROP TABLE IF EXISTS `nms_plotconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_plotconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `supperdir` varchar(200) DEFAULT NULL,
  `subdir` varchar(200) DEFAULT NULL,
  `inter` varchar(100) DEFAULT NULL,
  `filesize` int(5) DEFAULT NULL,
  `sendmobiles` varchar(200) DEFAULT NULL,
  `sendemail` varchar(200) DEFAULT NULL,
  `netid` varchar(100) DEFAULT NULL,
  `mon_flag` int(2) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_plotconfig`
--

LOCK TABLES `nms_plotconfig` WRITE;
/*!40000 ALTER TABLE `nms_plotconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_plotconfig` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:02
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_portscan_config`
--

DROP TABLE IF EXISTS `nms_portscan_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_portscan_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `port` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `portName` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `description` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `type` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `timeout` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `status` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `isScanned` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `scantime` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_portscan_config`
--

LOCK TABLES `nms_portscan_config` WRITE;
/*!40000 ALTER TABLE `nms_portscan_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_portscan_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_portservice`
--

DROP TABLE IF EXISTS `nms_portservice`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_portservice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `port` varchar(50) DEFAULT NULL,
  `portdesc` varchar(50) DEFAULT NULL,
  `monflag` int(20) DEFAULT NULL,
  `flag` int(20) DEFAULT NULL,
  `timeout` int(10) DEFAULT NULL,
  `sendemail` varchar(100) DEFAULT NULL,
  `sendmobiles` varchar(100) DEFAULT NULL,
  `sendphone` varchar(100) DEFAULT NULL,
  `bid` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(15) DEFAULT NULL,
  `supperid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_portservice`
--

LOCK TABLES `nms_portservice` WRITE;
/*!40000 ALTER TABLE `nms_portservice` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_portservice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_porttype`
--

DROP TABLE IF EXISTS `nms_porttype`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_porttype` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `typeid` bigint(11) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `bak` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_porttype`
--

LOCK TABLES `nms_porttype` WRITE;
/*!40000 ALTER TABLE `nms_porttype` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_porttype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_process_group`
--

DROP TABLE IF EXISTS `nms_process_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_process_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `ipaddress` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `nodeid` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `mon_flag` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `alarm_level` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_process_group`
--

LOCK TABLES `nms_process_group` WRITE;
/*!40000 ALTER TABLE `nms_process_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_process_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_process_group_config`
--

DROP TABLE IF EXISTS `nms_process_group_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_process_group_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `name` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `times` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `status` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_process_group_config`
--

LOCK TABLES `nms_process_group_config` WRITE;
/*!40000 ALTER TABLE `nms_process_group_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_process_group_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_procs`
--

DROP TABLE IF EXISTS `nms_procs`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_procs` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` int(11) DEFAULT NULL,
  `wbstatus` int(2) DEFAULT NULL,
  `flag` int(2) DEFAULT NULL,
  `ipaddress` varchar(100) DEFAULT NULL,
  `procname` varchar(50) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `bak` varchar(100) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT NULL,
  `supperid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_procs`
--

LOCK TABLES `nms_procs` WRITE;
/*!40000 ALTER TABLE `nms_procs` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_procs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_producer`
--

DROP TABLE IF EXISTS `nms_producer`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_producer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `producer` varchar(100) DEFAULT NULL,
  `enterprise_oid` varchar(20) DEFAULT '',
  `website` varchar(100) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_producer`
--

LOCK TABLES `nms_producer` WRITE;
/*!40000 ALTER TABLE `nms_producer` DISABLE KEYS */;
INSERT INTO `nms_producer` VALUES (1,'微软(Microsoft)','1.3.6.1.4.1.311','www.microsoft.com'),(2,'惠普(HP)','1.3.6.1.4.1.11','www.hp.com'),(3,'IBM','1.3.6.1.4.1.2',''),(4,'华为(Huawei)','1.3.6.1.4.1.2011',''),(5,'Avaya','1.3.6.1.4.1.81','www.avaya.com'),(6,'思科(Cisco)','1.3.6.1.4.1.9','www.cisco.com'),(7,'凯创(Enterasys)','1.3.6.1.4.1.52',''),(8,'3Com','1.3.6.1.4.1.6296',''),(9,'Sun','1.3.6.1.4.1.42',''),(10,'安奈特(Allied Telesyn)','1.3.6.1.4.1.207','www.alliedtelesis.com'),(12,'Polycom','1.3.6.1.4.1.274','www.polycom.com'),(13,'华三(H3C)','1.3.6.1.4.1.25506',''),(14,'Cisco','1.3.6.1.4.1.9.1.278','www.cisco.com'),(15,'Juniper','1.3.6.1.4.1.3224',''),(16,'天融信','1.3.6.1.4.1.14331','www.topsec.com'),(17,'迈普','1.3.6.1.4.1.5651','http://www.maipu.cn/'),(18,'中兴','1.3.6.1.4.1.3902',''),(19,'RadWare','1.3.6.1.4.1.89','www.radware.com'),(20,'Array Networks','1.3.6.1.4.1.7564','www.arraynetworks.com'),(21,'戴尔（DELL）','1.3.6.1.4.1.12740','www.dell.com'),(22,'网域神州','1.3.6.1.4.1.24968.','www.secworld.com'),(23,'TippingPoint','1.3.6.1.4.1.10734.','www.tippingpoing.com'),(24,'启明星辰','1.3.6.1.4.1.15227.','www.venus.com'),(25,'日立','1.3.6.1.4.1.116.','www.hitachi.com'),(26,'锐捷','1.3.6.1.4.1.4881.','www.redgiant.com'),(27,'VMWare','1.3.6.1.4.1.6876.','www.vmware.com');
/*!40000 ALTER TABLE `nms_producer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_radar`
--

DROP TABLE IF EXISTS `nms_radar`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_radar` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `thevalue` varchar(2) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_radar`
--

LOCK TABLES `nms_radar` WRITE;
/*!40000 ALTER TABLE `nms_radar` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_radar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_radarconfig`
--

DROP TABLE IF EXISTS `nms_radarconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_radarconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `supperdir` varchar(200) DEFAULT NULL,
  `subdir` varchar(200) DEFAULT NULL,
  `inter` varchar(100) DEFAULT NULL,
  `filesize` int(5) DEFAULT NULL,
  `sendmobiles` varchar(200) DEFAULT NULL,
  `sendemail` varchar(200) DEFAULT NULL,
  `netid` varchar(100) DEFAULT NULL,
  `mon_flag` int(2) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_radarconfig`
--

LOCK TABLES `nms_radarconfig` WRITE;
/*!40000 ALTER TABLE `nms_radarconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_radarconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_remote_ping_host`
--

DROP TABLE IF EXISTS `nms_remote_ping_host`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_remote_ping_host` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(100) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `login_prompt` varchar(100) DEFAULT NULL,
  `password_prompt` varchar(100) DEFAULT NULL,
  `shell_prompt` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_remote_ping_host`
--

LOCK TABLES `nms_remote_ping_host` WRITE;
/*!40000 ALTER TABLE `nms_remote_ping_host` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_remote_ping_host` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:02
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_remote_ping_node`
--

DROP TABLE IF EXISTS `nms_remote_ping_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_remote_ping_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `child_node_id` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_remote_ping_node`
--

LOCK TABLES `nms_remote_ping_node` WRITE;
/*!40000 ALTER TABLE `nms_remote_ping_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_remote_ping_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_remote_up_down_cluster`
--

DROP TABLE IF EXISTS `nms_remote_up_down_cluster`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_remote_up_down_cluster` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) DEFAULT NULL,
  `serverType` varchar(50) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `xml` varchar(50) DEFAULT NULL,
  `bid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_remote_up_down_cluster`
--

LOCK TABLES `nms_remote_up_down_cluster` WRITE;
/*!40000 ALTER TABLE `nms_remote_up_down_cluster` DISABLE KEYS */;
INSERT INTO `nms_remote_up_down_cluster` VALUES (1,'111','混合','2012-11-15 19:07:29','1352977649policy.jsp',',2,,4,'),(2,'ttttt','混合','2012-11-15 19:11:06','1352977866policy.jsp',',2,,4,,5,,6,');
/*!40000 ALTER TABLE `nms_remote_up_down_cluster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_remote_up_down_machine`
--

DROP TABLE IF EXISTS `nms_remote_up_down_machine`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_remote_up_down_machine` (
  `id` int(11) DEFAULT NULL,
  `clusterId` int(11) unsigned zerofill DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `serverType` varchar(10) DEFAULT NULL,
  `lasttime` datetime DEFAULT NULL,
  `username` varchar(30) DEFAULT NULL,
  `passwd` varchar(30) DEFAULT NULL,
  `monitorStatus` int(30) DEFAULT NULL,
  `isMonitor` int(10) DEFAULT NULL,
  `isJoin` int(2) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `powerOffAfter` varchar(50) DEFAULT NULL,
  `powerOffBefore` varchar(50) DEFAULT NULL,
  `powerOnAfter` varchar(50) DEFAULT NULL,
  `powerOnBefore` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_remote_up_down_machine`
--

LOCK TABLES `nms_remote_up_down_machine` WRITE;
/*!40000 ALTER TABLE `nms_remote_up_down_machine` DISABLE KEYS */;
INSERT INTO `nms_remote_up_down_machine` VALUES (1,00000000001,'1111','10.10.151.157','windows','2012-11-15 19:07:49','111','11',0,0,0,1,NULL,'','',NULL),(2,00000000001,'2222','10.10.152.57','windows','2012-11-15 19:08:03','2222','2222',0,0,0,2,NULL,'','',NULL);
/*!40000 ALTER TABLE `nms_remote_up_down_machine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_role_func`
--

DROP TABLE IF EXISTS `nms_role_func`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_role_func` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `roleid` bigint(11) DEFAULT NULL,
  `funcid` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1391 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_role_func`
--

LOCK TABLES `nms_role_func` WRITE;
/*!40000 ALTER TABLE `nms_role_func` DISABLE KEYS */;
INSERT INTO `nms_role_func` VALUES (1011,4,1),(1012,4,2),(1013,4,7),(1014,4,8),(1015,4,165),(1016,4,9),(1017,4,10),(1018,4,11),(1020,4,122),(1021,4,21),(1022,4,22),(1023,4,24),(1024,4,23),(1025,4,101),(1026,4,128),(1027,4,130),(1028,4,131),(1029,4,132),(1030,4,149),(1031,4,31),(1032,4,32),(1033,4,33),(1034,4,34),(1035,4,162),(1036,4,35),(1037,4,36),(1038,4,37),(1039,4,38),(1040,4,39),(1041,4,40),(1042,4,41),(1043,4,42),(1044,4,159),(1045,4,113),(1046,4,114),(1047,4,43),(1048,4,44),(1049,4,45),(1050,4,46),(1051,4,109),(1052,4,129),(1053,4,106),(1054,4,107),(1055,4,108),(1056,4,110),(1057,4,111),(1060,4,170),(1061,4,171),(1062,4,172),(1063,4,179),(1064,4,189),(1065,4,135),(1066,4,137),(1067,4,140),(1068,4,141),(1069,4,142),(1070,4,143),(1071,4,136),(1072,4,148),(1073,4,139),(1074,4,151),(1075,4,70),(1076,4,192),(1077,4,194),(1078,0,1),(1079,0,2),(1080,0,4),(1081,0,5),(1082,0,6),(1083,0,7),(1084,0,8),(1085,0,165),(1086,0,141),(1087,0,142),(1088,0,143),(1089,0,148),(1090,0,151),(1091,0,9),(1092,0,10),(1093,0,11),(1094,0,122),(1095,0,18),(1096,0,92),(1097,0,150),(1098,0,152),(1099,0,153),(1100,0,154),(1101,0,155),(1102,0,157),(1103,0,158),(1104,0,13),(1105,0,14),(1106,0,15),(1107,0,16),(1108,0,17),(1109,0,125),(1110,0,147),(1111,0,156),(1112,0,21),(1113,0,22),(1114,0,24),(1115,0,23),(1116,0,101),(1117,0,128),(1118,0,130),(1119,0,131),(1120,0,132),(1121,0,25),(1122,0,26),(1123,0,27),(1124,0,120),(1125,0,28),(1126,0,29),(1127,0,30),(1128,0,149),(1129,0,47),(1130,0,48),(1131,0,49),(1132,0,50),(1133,0,51),(1134,0,52),(1135,0,53),(1136,0,54),(1137,0,98),(1138,0,112),(1139,0,55),(1140,0,56),(1141,0,57),(1142,0,59),(1143,0,97),(1144,0,63),(1145,0,64),(1146,0,65),(1147,0,66),(1148,0,67),(1149,0,68),(1150,0,69),(1151,0,96),(1152,0,100),(1153,0,102),(1154,0,116),(1155,0,123),(1156,0,133),(1157,0,134),(1158,0,117),(1159,0,118),(1160,0,119),(1161,0,144),(1162,0,145),(1163,0,146),(1164,0,31),(1165,0,32),(1166,0,33),(1167,0,34),(1168,0,162),(1169,0,35),(1170,0,36),(1171,0,37),(1172,0,38),(1173,0,39),(1174,0,40),(1175,0,41),(1176,0,42),(1177,0,159),(1178,0,113),(1179,0,114),(1180,0,43),(1181,0,44),(1182,0,45),(1183,0,46),(1184,0,109),(1185,0,129),(1186,0,106),(1187,0,107),(1188,0,108),(1189,0,110),(1190,0,111),(1191,0,170),(1192,0,171),(1193,0,172),(1194,0,179),(1195,0,189),(1196,0,135),(1197,0,178),(1198,0,180),(1199,0,181),(1200,0,184),(1201,0,182),(1202,0,183),(1203,0,185),(1204,0,186),(1205,0,187),(1206,0,188),(1207,0,190),(1208,0,206),(1209,0,191),(1210,0,70),(1211,0,71),(1212,0,72),(1213,0,73),(1214,0,74),(1215,0,75),(1216,0,95),(1217,0,99),(1218,0,121),(1219,0,124),(1220,0,76),(1221,0,77),(1222,0,78),(1223,0,79),(1224,0,80),(1225,0,81),(1226,0,82),(1227,0,83),(1228,0,84),(1229,0,85),(1230,0,86),(1231,0,87),(1232,0,88),(1233,0,89),(1234,0,90),(1235,0,91),(1236,0,115),(1237,0,126),(1238,0,127),(1239,0,160),(1240,0,161),(1241,0,167),(1242,0,169),(1243,0,103),(1244,0,104),(1245,0,105),(1246,0,192),(1247,0,193),(1248,0,194),(1249,2,1),(1250,2,2),(1251,2,3),(1252,2,4),(1253,2,5),(1254,2,6),(1255,2,7),(1256,2,8),(1257,2,165),(1258,2,141),(1259,2,142),(1260,2,143),(1261,2,148),(1262,2,151),(1263,2,25),(1264,2,26),(1265,2,27),(1266,2,120),(1267,2,28),(1268,2,29),(1269,2,30),(1270,2,149),(1271,2,47),(1272,2,48),(1273,2,49),(1274,2,50),(1275,2,51),(1276,2,52),(1277,2,53),(1278,2,54),(1279,2,98),(1280,2,112),(1281,2,55),(1282,2,56),(1283,2,57),(1284,2,59),(1285,2,97),(1286,2,63),(1287,2,64),(1288,2,65),(1289,2,66),(1290,2,67),(1291,2,68),(1292,2,69),(1293,2,96),(1294,2,100),(1295,2,102),(1296,2,116),(1297,2,123),(1298,2,133),(1299,2,134),(1300,2,117),(1301,2,118),(1302,2,119),(1303,2,144),(1304,2,145),(1305,2,146),(1306,2,31),(1307,2,32),(1308,2,33),(1309,2,34),(1310,2,162),(1311,2,35),(1312,2,36),(1313,2,37),(1314,2,38),(1315,2,39),(1316,2,40),(1317,2,41),(1318,2,42),(1319,2,159),(1320,2,113),(1321,2,114),(1322,2,43),(1323,2,44),(1324,2,45),(1325,2,46),(1326,2,109),(1327,2,129),(1328,2,106),(1329,2,107),(1330,2,108),(1331,2,110),(1332,2,111),(1333,2,170),(1334,2,171),(1335,2,172),(1336,2,179),(1337,2,189),(1338,2,135),(1339,2,178),(1340,2,180),(1341,2,181),(1342,2,184),(1343,2,182),(1344,2,183),(1345,2,185),(1346,2,186),(1347,2,187),(1348,2,188),(1349,2,190),(1350,2,206),(1351,2,191),(1352,2,70),(1353,2,71),(1354,2,72),(1355,2,73),(1356,2,74),(1357,2,75),(1358,2,95),(1359,2,99),(1360,2,121),(1361,2,124),(1362,2,76),(1363,2,77),(1364,2,78),(1365,2,79),(1366,2,80),(1367,2,81),(1368,2,82),(1369,2,83),(1370,2,84),(1371,2,85),(1372,2,86),(1373,2,87),(1374,2,88),(1375,2,89),(1376,2,90),(1377,2,91),(1378,2,115),(1379,2,126),(1380,2,127),(1381,2,160),(1382,2,161),(1383,2,167),(1384,2,169),(1385,2,103),(1386,2,104),(1387,2,105),(1388,2,192),(1389,2,193),(1390,2,194);
/*!40000 ALTER TABLE `nms_role_func` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_send_alarm_time`
--

DROP TABLE IF EXISTS `nms_send_alarm_time`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_send_alarm_time` (
  `name` varchar(200) CHARACTER SET gb2312 NOT NULL,
  `alarm_way_detail_id` varchar(20) CHARACTER SET gb2312 NOT NULL,
  `send_times` varchar(20) CHARACTER SET gb2312 DEFAULT NULL,
  `last_send_time` varchar(50) CHARACTER SET gb2312 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_send_alarm_time`
--

LOCK TABLES `nms_send_alarm_time` WRITE;
/*!40000 ALTER TABLE `nms_send_alarm_time` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_send_alarm_time` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_service`
--

DROP TABLE IF EXISTS `nms_service`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_service` (
  `id` int(2) NOT NULL,
  `service` varchar(20) DEFAULT NULL,
  `port` int(5) DEFAULT NULL,
  `time_out` int(5) DEFAULT NULL,
  `scan` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_service`
--

LOCK TABLES `nms_service` WRITE;
/*!40000 ALTER TABLE `nms_service` DISABLE KEYS */;
INSERT INTO `nms_service` VALUES (1,'HTTP',80,1000,1),(2,'DNS',53,1000,1),(3,'SMTP',25,1000,1),(4,'FTP',21,1000,1),(5,'Telnet',23,1000,1),(6,'POP',110,1000,1),(7,'Tomcat',8080,1000,1),(8,'WebLogic',7001,1000,1),(9,'MySQL',3306,1000,1),(10,'MSSQL',1433,1000,1),(11,'Oracle',1521,1000,1),(12,'DB2',8000,1000,1),(13,'Sybase',2638,1000,1),(14,'Informix',9088,1000,1),(15,'SSH',22,1000,1);
/*!40000 ALTER TABLE `nms_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_sla_audit`
--

DROP TABLE IF EXISTS `nms_sla_audit`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sla_audit` (
  `id` int(11) NOT NULL,
  `userid` int(11) DEFAULT NULL,
  `telnetconfigid` int(11) DEFAULT NULL,
  `slatype` varchar(100) DEFAULT NULL,
  `operation` varchar(100) DEFAULT NULL,
  `cmdcontent` varchar(500) DEFAULT NULL,
  `dotime` varchar(50) DEFAULT NULL,
  `dostatus` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_sla_audit`
--

LOCK TABLES `nms_sla_audit` WRITE;
/*!40000 ALTER TABLE `nms_sla_audit` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_sla_audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_sla_config_node`
--

DROP TABLE IF EXISTS `nms_sla_config_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sla_config_node` (
  `id` int(11) NOT NULL,
  `telnetconfig_id` int(11) NOT NULL,
  `slatype` varchar(50) DEFAULT NULL,
  `intervals` int(2) DEFAULT NULL,
  `intervalunit` varchar(2) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `descr` varchar(200) DEFAULT NULL,
  `bak` varchar(200) DEFAULT NULL,
  `bid` varchar(200) DEFAULT NULL,
  `entrynumber` varchar(50) NOT NULL,
  `mon_flag` int(2) DEFAULT NULL,
  `destip` varchar(20) DEFAULT NULL,
  `devicetype` varchar(50) DEFAULT NULL,
  `collecttype` varchar(20) DEFAULT NULL,
  `adminsign` varchar(100) DEFAULT NULL,
  `operatesign` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_sla_config_node`
--

LOCK TABLES `nms_sla_config_node` WRITE;
/*!40000 ALTER TABLE `nms_sla_config_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_sla_config_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_slanodeprop`
--

DROP TABLE IF EXISTS `nms_slanodeprop`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_slanodeprop` (
  `id` int(11) NOT NULL,
  `telnetconfigid` int(11) NOT NULL,
  `entrynumber` int(11) NOT NULL,
  `slatype` varchar(100) NOT NULL,
  `bak` varchar(100) DEFAULT NULL,
  `createTime` varchar(50) DEFAULT NULL,
  `operatorid` int(11) NOT NULL,
  `adminsign` varchar(100) DEFAULT NULL,
  `operatesign` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_slanodeprop`
--

LOCK TABLES `nms_slanodeprop` WRITE;
/*!40000 ALTER TABLE `nms_slanodeprop` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_slanodeprop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_smsconfig`
--

DROP TABLE IF EXISTS `nms_smsconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_smsconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `objectid` varchar(20) DEFAULT NULL,
  `objecttype` varchar(20) DEFAULT NULL,
  `begintime` varchar(50) DEFAULT NULL,
  `endtime` varchar(50) DEFAULT NULL,
  `userids` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_smsconfig`
--

LOCK TABLES `nms_smsconfig` WRITE;
/*!40000 ALTER TABLE `nms_smsconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_smsconfig` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:03
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_socket_realtime`
--

DROP TABLE IF EXISTS `nms_socket_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_socket_realtime` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `is_canconnected` int(2) DEFAULT NULL,
  `is_valid` int(2) DEFAULT NULL,
  `is_refresh` int(2) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `page_context` mediumtext,
  `mon_time` timestamp NULL DEFAULT NULL,
  `sms_sign` int(2) DEFAULT NULL,
  `condelay` int(10) DEFAULT NULL,
  `socket_id` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_socket_realtime`
--

LOCK TABLES `nms_socket_realtime` WRITE;
/*!40000 ALTER TABLE `nms_socket_realtime` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_socket_realtime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage`
--

DROP TABLE IF EXISTS `nms_storage`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `name` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `username` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `password` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `status` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `mon_flag` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `collecttype` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `company` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `type` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `serial_number` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `bid` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `collecttime` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `supperid` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `sendemail` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `sendmobiles` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `sendphone` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage`
--

LOCK TABLES `nms_storage` WRITE;
/*!40000 ALTER TABLE `nms_storage` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_arrayinfo`
--

DROP TABLE IF EXISTS `nms_storage_arrayinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_arrayinfo` (
  `id` bigint(11) NOT NULL,
  `nodeid` bigint(11) DEFAULT NULL,
  `arraystatus` varchar(20) DEFAULT NULL,
  `firmwarerevision` varchar(50) DEFAULT NULL,
  `productrevision` varchar(50) DEFAULT NULL,
  `localprdrevision` varchar(50) DEFAULT NULL,
  `remoteprdrevision` varchar(50) DEFAULT NULL,
  `collecttime` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_arrayinfo`
--

LOCK TABLES `nms_storage_arrayinfo` WRITE;
/*!40000 ALTER TABLE `nms_storage_arrayinfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_arrayinfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpcapacity`
--

DROP TABLE IF EXISTS `nms_storage_hpcapacity`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpcapacity` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` bigint(11) DEFAULT NULL,
  `totaldiskenclosures` int(5) DEFAULT NULL,
  `collecttime` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpcapacity`
--

LOCK TABLES `nms_storage_hpcapacity` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpcapacity` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpcapacity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpcapredugroup`
--

DROP TABLE IF EXISTS `nms_storage_hpcapredugroup`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpcapredugroup` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `capacityid` bigint(11) DEFAULT NULL,
  `redundancygroupid` varchar(5) DEFAULT NULL,
  `totaldisks` varchar(10) DEFAULT NULL,
  `totalphysicalsize` varchar(50) DEFAULT NULL,
  `allocatedluns` varchar(50) DEFAULT NULL,
  `allocatedbusicopies` varchar(50) DEFAULT NULL,
  `usedasactivehotspare` varchar(50) DEFAULT NULL,
  `usedforredundancy` varchar(50) DEFAULT NULL,
  `unallocated` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpcapredugroup`
--

LOCK TABLES `nms_storage_hpcapredugroup` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpcapredugroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpcapredugroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpcontrbattery`
--

DROP TABLE IF EXISTS `nms_storage_hpcontrbattery`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpcontrbattery` (
  `id` bigint(11) NOT NULL,
  `controllerid` bigint(11) DEFAULT NULL,
  `batteryaddr` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `identification` varchar(50) DEFAULT NULL,
  `manuname` varchar(50) DEFAULT NULL,
  `devicename` varchar(50) DEFAULT NULL,
  `manudate` varchar(50) DEFAULT NULL,
  `remainingcapacity` varchar(50) DEFAULT NULL,
  `prcremainingcapacity` varchar(50) DEFAULT NULL,
  `voltage` varchar(20) DEFAULT NULL,
  `dischargecycles` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpcontrbattery`
--

LOCK TABLES `nms_storage_hpcontrbattery` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpcontrbattery` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpcontrbattery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpcontrdimm`
--

DROP TABLE IF EXISTS `nms_storage_hpcontrdimm`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpcontrdimm` (
  `id` bigint(11) NOT NULL,
  `controllerid` bigint(11) DEFAULT NULL,
  `dimmaddr` varchar(50) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `identification` varchar(50) DEFAULT NULL,
  `capacity` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpcontrdimm`
--

LOCK TABLES `nms_storage_hpcontrdimm` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpcontrdimm` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpcontrdimm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpcontroller`
--

DROP TABLE IF EXISTS `nms_storage_hpcontroller`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpcontroller` (
  `id` bigint(11) NOT NULL,
  `nodeid` bigint(11) DEFAULT NULL,
  `controlleraddr` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `serialnumber` varchar(50) DEFAULT NULL,
  `vondorid` varchar(50) DEFAULT NULL,
  `productid` varchar(50) DEFAULT NULL,
  `productrevision` varchar(50) DEFAULT NULL,
  `firmwarerevision` varchar(50) DEFAULT NULL,
  `productcode` varchar(50) DEFAULT NULL,
  `controllertype` varchar(50) DEFAULT NULL,
  `batteryrevision` varchar(50) DEFAULT NULL,
  `enclswitchsetting` varchar(100) DEFAULT NULL,
  `driveaddr` varchar(50) DEFAULT NULL,
  `enclosureid` varchar(10) DEFAULT NULL,
  `looppair` varchar(10) DEFAULT NULL,
  `loopid` varchar(10) DEFAULT NULL,
  `hardaddress` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpcontroller`
--

LOCK TABLES `nms_storage_hpcontroller` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpcontroller` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpcontroller` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpcontrport`
--

DROP TABLE IF EXISTS `nms_storage_hpcontrport`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpcontrport` (
  `id` bigint(11) NOT NULL,
  `controllerid` bigint(11) DEFAULT NULL,
  `frontportaddr` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `portinstance` varchar(50) DEFAULT NULL,
  `hardaddress` varchar(50) DEFAULT NULL,
  `linkstate` varchar(20) DEFAULT NULL,
  `nodewwn` varchar(20) DEFAULT NULL,
  `portwwn` varchar(20) DEFAULT NULL,
  `topology` varchar(50) DEFAULT NULL,
  `datarate` varchar(20) DEFAULT NULL,
  `portid` varchar(20) DEFAULT NULL,
  `devicehostname` varchar(50) DEFAULT NULL,
  `hardwarepath` varchar(100) DEFAULT NULL,
  `collecttime` varchar(50) DEFAULT NULL,
  `porttype` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpcontrport`
--

LOCK TABLES `nms_storage_hpcontrport` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpcontrport` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpcontrport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpcontrpro`
--

DROP TABLE IF EXISTS `nms_storage_hpcontrpro`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpcontrpro` (
  `id` bigint(11) NOT NULL,
  `controllerid` bigint(11) DEFAULT NULL,
  `processoraddr` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `identification` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpcontrpro`
--

LOCK TABLES `nms_storage_hpcontrpro` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpcontrpro` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpcontrpro` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:04
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_storage_hpdisk`
--

DROP TABLE IF EXISTS `nms_storage_hpdisk`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpdisk` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` bigint(11) DEFAULT NULL,
  `diskaddr` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `diskstate` varchar(20) DEFAULT NULL,
  `vendorid` varchar(50) DEFAULT NULL,
  `productid` varchar(50) DEFAULT NULL,
  `productrevision` varchar(50) DEFAULT NULL,
  `datacapacity` varchar(20) DEFAULT NULL,
  `blocklength` varchar(20) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `nodewwn` varchar(50) DEFAULT NULL,
  `initializestate` varchar(50) DEFAULT NULL,
  `volumeserialnumber` varchar(50) DEFAULT NULL,
  `serialnumber` varchar(30) DEFAULT NULL,
  `firmwarerevision` varchar(30) DEFAULT NULL,
  `collecttime` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpdisk`
--

LOCK TABLES `nms_storage_hpdisk` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpdisk` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpdisk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpenclosures`
--

DROP TABLE IF EXISTS `nms_storage_hpenclosures`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpenclosures` (
  `id` bigint(11) NOT NULL,
  `nodeid` bigint(11) DEFAULT NULL,
  `enclosureaddr` varchar(50) DEFAULT NULL,
  `enclosureid` varchar(10) DEFAULT NULL,
  `enclosurestatus` varchar(10) DEFAULT NULL,
  `enclosuretype` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpenclosures`
--

LOCK TABLES `nms_storage_hpenclosures` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpenclosures` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpenclosures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpenclosuresfru`
--

DROP TABLE IF EXISTS `nms_storage_hpenclosuresfru`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpenclosuresfru` (
  `id` bigint(11) NOT NULL,
  `enclosureid` bigint(11) DEFAULT NULL,
  `fru` varchar(100) DEFAULT NULL,
  `hwcomp` varchar(100) DEFAULT NULL,
  `identi` varchar(50) DEFAULT NULL,
  `idstatus` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpenclosuresfru`
--

LOCK TABLES `nms_storage_hpenclosuresfru` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpenclosuresfru` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpenclosuresfru` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hplun`
--

DROP TABLE IF EXISTS `nms_storage_hplun`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hplun` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` bigint(11) DEFAULT NULL,
  `redundancygroup` varchar(50) DEFAULT NULL,
  `active` varchar(10) DEFAULT NULL,
  `datacapacity` varchar(50) DEFAULT NULL,
  `wwn` varchar(30) DEFAULT NULL,
  `numosbusicopies` varchar(10) DEFAULT NULL,
  `collecttime` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hplun`
--

LOCK TABLES `nms_storage_hplun` WRITE;
/*!40000 ALTER TABLE `nms_storage_hplun` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hplun` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpport`
--

DROP TABLE IF EXISTS `nms_storage_hpport`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpport` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` bigint(11) DEFAULT NULL,
  `portname` varchar(50) DEFAULT NULL,
  `portid` varchar(20) DEFAULT NULL,
  `behavior` varchar(50) DEFAULT NULL,
  `topology` varchar(20) DEFAULT NULL,
  `queuefullthreshold` varchar(10) DEFAULT NULL,
  `datarate` varchar(20) DEFAULT NULL,
  `collecttime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpport`
--

LOCK TABLES `nms_storage_hpport` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpport` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpsystem`
--

DROP TABLE IF EXISTS `nms_storage_hpsystem`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpsystem` (
  `id` bigint(11) NOT NULL,
  `nodeid` bigint(11) DEFAULT NULL,
  `vendorid` varchar(50) DEFAULT NULL,
  `productid` varchar(50) DEFAULT NULL,
  `arrayname` varchar(100) DEFAULT NULL,
  `arrayserinumber` varchar(100) DEFAULT NULL,
  `alias` varchar(50) DEFAULT NULL,
  `softrevision` varchar(50) DEFAULT NULL,
  `cmdexetime` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpsystem`
--

LOCK TABLES `nms_storage_hpsystem` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpsystem` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpsystem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storage_hpvfp`
--

DROP TABLE IF EXISTS `nms_storage_hpvfp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_hpvfp` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` bigint(11) DEFAULT NULL,
  `vfpname` varchar(50) DEFAULT NULL,
  `baudrate` varchar(20) DEFAULT NULL,
  `pagingvalue` varchar(20) DEFAULT NULL,
  `collecttime` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storage_hpvfp`
--

LOCK TABLES `nms_storage_hpvfp` WRITE;
/*!40000 ALTER TABLE `nms_storage_hpvfp` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_storage_hpvfp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_storagetype`
--

DROP TABLE IF EXISTS `nms_storagetype`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storagetype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `producer` int(11) DEFAULT NULL,
  `model` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `descr` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_storagetype`
--

LOCK TABLES `nms_storagetype` WRITE;
/*!40000 ALTER TABLE `nms_storagetype` DISABLE KEYS */;
INSERT INTO `nms_storagetype` VALUES (1,3,'DS8100','DS8100'),(2,21,'EqualLogic','EqualLogic系列存储');
/*!40000 ALTER TABLE `nms_storagetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_subconfig_category`
--

DROP TABLE IF EXISTS `nms_subconfig_category`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_subconfig_category` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT ' ',
  `subdesc` varchar(100) DEFAULT ' ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_subconfig_category`
--

LOCK TABLES `nms_subconfig_category` WRITE;
/*!40000 ALTER TABLE `nms_subconfig_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_subconfig_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_supper_info`
--

DROP TABLE IF EXISTS `nms_supper_info`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_supper_info` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `su_name` varchar(100) NOT NULL,
  `su_class` varchar(100) NOT NULL,
  `su_area` varchar(100) DEFAULT NULL,
  `su_desc` varchar(1000) DEFAULT NULL,
  `su_person` varchar(100) DEFAULT NULL,
  `su_email` varchar(100) DEFAULT NULL,
  `su_phone` varchar(100) DEFAULT NULL,
  `su_address` varchar(200) DEFAULT NULL,
  `su_dept` varchar(100) DEFAULT NULL,
  `su_url` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_supper_info`
--

LOCK TABLES `nms_supper_info` WRITE;
/*!40000 ALTER TABLE `nms_supper_info` DISABLE KEYS */;
INSERT INTO `nms_supper_info` VALUES (1,'思科（中国）有限公司','制造商','北京海淀区','思科（中国）公司竭诚为您服务','王一民','wangyimin@cisco.com','18688888888','北京海淀区','集成一部','http://www.cisco.com'),(2,'H3C公司','制造商','北京市朝阳区','硬件供应商','张然','zhangran@h3c.com','13986868686','北京市朝阳区','系统集成部','www.h3c.com');
/*!40000 ALTER TABLE `nms_supper_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:05
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_symantec`
--

DROP TABLE IF EXISTS `nms_symantec`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_symantec` (
  `begintime` varchar(20) DEFAULT NULL,
  `machine` varchar(30) DEFAULT NULL,
  `machine_ip` varchar(15) DEFAULT NULL,
  `virus` varchar(100) DEFAULT NULL,
  `virus_file` varchar(200) DEFAULT NULL,
  `deal_way` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_symantec`
--

LOCK TABLES `nms_symantec` WRITE;
/*!40000 ALTER TABLE `nms_symantec` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_symantec` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_symantec_log`
--

DROP TABLE IF EXISTS `nms_symantec_log`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_symantec_log` (
  `id` int(5) NOT NULL,
  `ip` varchar(15) DEFAULT NULL,
  `log_file` varchar(15) DEFAULT NULL,
  `log_row` int(10) DEFAULT NULL,
  `log_time` varchar(20) DEFAULT NULL,
  `info` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_symantec_log`
--

LOCK TABLES `nms_symantec_log` WRITE;
/*!40000 ALTER TABLE `nms_symantec_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_symantec_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_temperature`
--

DROP TABLE IF EXISTS `nms_temperature`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_temperature` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(15) DEFAULT NULL,
  `entryid` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `value` decimal(10,2) DEFAULT NULL,
  `category` varchar(10) DEFAULT NULL,
  `collecttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bak` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_temperature`
--

LOCK TABLES `nms_temperature` WRITE;
/*!40000 ALTER TABLE `nms_temperature` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_temperature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_temperature_his`
--

DROP TABLE IF EXISTS `nms_temperature_his`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_temperature_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(15) DEFAULT NULL,
  `entryid` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `value` decimal(10,2) DEFAULT NULL,
  `category` varchar(10) DEFAULT NULL,
  `collecttime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bak` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_temperature_his`
--

LOCK TABLES `nms_temperature_his` WRITE;
/*!40000 ALTER TABLE `nms_temperature_his` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_temperature_his` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_tftp_history`
--

DROP TABLE IF EXISTS `nms_tftp_history`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_tftp_history` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TFTP_ID` int(11) DEFAULT NULL,
  `IS_CANCONNECTED` int(2) DEFAULT NULL,
  `REASON` varchar(255) DEFAULT NULL,
  `MON_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_tftp_history`
--

LOCK TABLES `nms_tftp_history` WRITE;
/*!40000 ALTER TABLE `nms_tftp_history` DISABLE KEYS */;
INSERT INTO `nms_tftp_history` VALUES (1,1,1,'TFTP服务有效','2012-05-04 08:30:08');
/*!40000 ALTER TABLE `nms_tftp_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_tftp_realtime`
--

DROP TABLE IF EXISTS `nms_tftp_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_tftp_realtime` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TFTP_ID` int(11) DEFAULT NULL,
  `IS_CANCONNECTED` int(2) DEFAULT NULL,
  `REASON` varchar(255) DEFAULT NULL,
  `MON_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `SMS_SIGN` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_tftp_realtime`
--

LOCK TABLES `nms_tftp_realtime` WRITE;
/*!40000 ALTER TABLE `nms_tftp_realtime` DISABLE KEYS */;
INSERT INTO `nms_tftp_realtime` VALUES (1,1,1,'TFTP服务有效','2012-05-04 08:30:08',0);
/*!40000 ALTER TABLE `nms_tftp_realtime` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_tftpconfig`
--

DROP TABLE IF EXISTS `nms_tftpconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_tftpconfig` (
  `ID` int(11) NOT NULL,
  `STR` varchar(255) DEFAULT NULL,
  `USER_NAME` varchar(30) DEFAULT NULL,
  `USER_PASSWORD` varchar(30) DEFAULT NULL,
  `AVAILABILITY_STRING` varchar(255) DEFAULT NULL,
  `POLL_INTERVAL` int(5) DEFAULT NULL,
  `UNAVAILABILITY_STRING` varchar(255) DEFAULT NULL,
  `TIMEOUT` int(5) DEFAULT NULL,
  `VERIFY` int(5) DEFAULT NULL,
  `FLAG` int(2) DEFAULT NULL,
  `MON_FLAG` int(2) DEFAULT NULL,
  `ALIAS` varchar(50) DEFAULT NULL,
  `SENDMOBILES` varchar(500) DEFAULT NULL,
  `NETID` varchar(100) DEFAULT NULL,
  `FILENAME` varchar(50) DEFAULT NULL,
  `SENDEMAIL` varchar(100) DEFAULT NULL,
  `SENDPHONE` varchar(100) DEFAULT NULL,
  `IPADDRESS` varchar(15) DEFAULT NULL,
  `SUPPERID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_tftpconfig`
--

LOCK TABLES `nms_tftpconfig` WRITE;
/*!40000 ALTER TABLE `nms_tftpconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_tftpconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_tftpmonitorconfig`
--

DROP TABLE IF EXISTS `nms_tftpmonitorconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_tftpmonitorconfig` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `USERNAME` varchar(50) DEFAULT NULL,
  `PASSWORD` varchar(50) DEFAULT NULL,
  `TIMEOUT` int(5) DEFAULT NULL,
  `MONFLAG` int(2) DEFAULT NULL,
  `FILENAME` varchar(200) DEFAULT NULL,
  `BID` varchar(100) DEFAULT NULL,
  `SENDMOBILES` varchar(100) DEFAULT NULL,
  `SENDEMAIL` varchar(100) DEFAULT NULL,
  `SENDPHONE` varchar(100) DEFAULT NULL,
  `IPADDRESS` varchar(15) DEFAULT NULL,
  `SUPPERID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_tftpmonitorconfig`
--

LOCK TABLES `nms_tftpmonitorconfig` WRITE;
/*!40000 ALTER TABLE `nms_tftpmonitorconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_tftpmonitorconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_timegratherconfig`
--

DROP TABLE IF EXISTS `nms_timegratherconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_timegratherconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `objectid` varchar(20) DEFAULT NULL,
  `objecttype` varchar(20) DEFAULT NULL,
  `begintime` varchar(50) DEFAULT NULL,
  `endtime` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_timegratherconfig`
--

LOCK TABLES `nms_timegratherconfig` WRITE;
/*!40000 ALTER TABLE `nms_timegratherconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_timegratherconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_timeshareconfig`
--

DROP TABLE IF EXISTS `nms_timeshareconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_timeshareconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `objectid` varchar(20) CHARACTER SET gb2312 DEFAULT NULL,
  `objecttype` varchar(20) CHARACTER SET gb2312 DEFAULT NULL,
  `timesharetype` varchar(20) CHARACTER SET gb2312 DEFAULT NULL,
  `begintime` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `endtime` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `userids` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_timeshareconfig`
--

LOCK TABLES `nms_timeshareconfig` WRITE;
/*!40000 ALTER TABLE `nms_timeshareconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_timeshareconfig` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:05
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_tomcat_temp`
--

DROP TABLE IF EXISTS `nms_tomcat_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_tomcat_temp` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(15) DEFAULT NULL,
  `entity` varchar(50) DEFAULT NULL,
  `value` varchar(600) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_tomcat_temp`
--

LOCK TABLES `nms_tomcat_temp` WRITE;
/*!40000 ALTER TABLE `nms_tomcat_temp` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_tomcat_temp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_tracerts`
--

DROP TABLE IF EXISTS `nms_tracerts`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_tracerts` (
  `id` int(11) NOT NULL,
  `nodetype` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `configid` int(11) DEFAULT NULL,
  `dotime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_tracerts`
--

LOCK TABLES `nms_tracerts` WRITE;
/*!40000 ALTER TABLE `nms_tracerts` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_tracerts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_tracerts_details`
--

DROP TABLE IF EXISTS `nms_tracerts_details`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_tracerts_details` (
  `id` int(11) NOT NULL,
  `tracertsid` int(11) NOT NULL,
  `nodetype` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `details` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `configid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_tracerts_details`
--

LOCK TABLES `nms_tracerts_details` WRITE;
/*!40000 ALTER TABLE `nms_tracerts_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_tracerts_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_tracerts_details_temp`
--

DROP TABLE IF EXISTS `nms_tracerts_details_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_tracerts_details_temp` (
  `id` int(11) NOT NULL,
  `tracertsid` int(11) NOT NULL,
  `nodetype` varchar(50) DEFAULT NULL,
  `details` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_tracerts_details_temp`
--

LOCK TABLES `nms_tracerts_details_temp` WRITE;
/*!40000 ALTER TABLE `nms_tracerts_details_temp` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_tracerts_details_temp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_tracerts_temp`
--

DROP TABLE IF EXISTS `nms_tracerts_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_tracerts_temp` (
  `id` int(11) NOT NULL,
  `nodetype` varchar(50) DEFAULT NULL,
  `configid` int(11) DEFAULT NULL,
  `dotime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_tracerts_temp`
--

LOCK TABLES `nms_tracerts_temp` WRITE;
/*!40000 ALTER TABLE `nms_tracerts_temp` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_tracerts_temp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_trapoid`
--

DROP TABLE IF EXISTS `nms_trapoid`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_trapoid` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `enterpriseoid` varchar(100) DEFAULT NULL,
  `orders` int(2) DEFAULT NULL,
  `oid` varchar(100) DEFAULT NULL,
  `descr` varchar(100) DEFAULT NULL,
  `value1` varchar(100) DEFAULT NULL,
  `value2` varchar(100) DEFAULT NULL,
  `transflag` int(1) DEFAULT NULL,
  `compareflag` int(1) DEFAULT NULL,
  `transvalue1` varchar(100) DEFAULT NULL,
  `transvalue2` varchar(100) DEFAULT NULL,
  `traptype` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_trapoid`
--

LOCK TABLES `nms_trapoid` WRITE;
/*!40000 ALTER TABLE `nms_trapoid` DISABLE KEYS */;
INSERT INTO `nms_trapoid` VALUES (1,'1.3.6.1.4.1.9.1.209',1,'1.3.6.1.2.1.2.2.1.1.1','index','端口号',NULL,0,NULL,NULL,NULL,NULL),(2,'1.3.6.1.4.1.9.1.209',2,'1.3.6.1.2.1.2.2.1.2.1','desc','描述',NULL,2,NULL,NULL,NULL,NULL),(3,'1.3.6.1.4.1.9.1.209',3,'1.3.6.1.2.1.2.2.1.3.1','type','类型',NULL,0,NULL,NULL,NULL,NULL),(4,'1.3.6.1.4.1.9.1.209',4,'1.3.6.1.4.1.9.2.2.1.1.20.1','reasion','Keepalive failed','Keepalive OK',1,NULL,'关闭','启动','port'),(5,'1.3.6.1.4.1.25506.1.38',1,'1.3.6.1.2.1.2.2.1.1','index','端口号',NULL,2,NULL,NULL,NULL,NULL),(6,'1.3.6.1.4.1.25506.1.38',2,'1.3.6.1.2.1.2.2.1.7','adminstatus','2','1',0,NULL,'',NULL,NULL),(7,'1.3.6.1.4.1.25506.1.38',3,'1.3.6.1.2.1.2.2.1.8','operstatus','2','1',1,NULL,'关闭','启动','port');
/*!40000 ALTER TABLE `nms_trapoid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_tuxedoconfig`
--

DROP TABLE IF EXISTS `nms_tuxedoconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_tuxedoconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `ipaddress` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `community` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `port` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `status` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `mon_flag` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `bid` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `sendphone` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `sendemail` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `sendmobiles` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `reservation1` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `reservation2` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `reservation3` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_tuxedoconfig`
--

LOCK TABLES `nms_tuxedoconfig` WRITE;
/*!40000 ALTER TABLE `nms_tuxedoconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_tuxedoconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_urlconfig`
--

DROP TABLE IF EXISTS `nms_urlconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_urlconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `str` varchar(255) DEFAULT NULL,
  `user_name` varchar(30) DEFAULT NULL,
  `user_password` varchar(30) DEFAULT NULL,
  `query_string` varchar(255) DEFAULT NULL,
  `method` char(1) DEFAULT NULL,
  `availability_string` varchar(255) DEFAULT NULL,
  `poll_interval` int(11) DEFAULT NULL,
  `unavailability_string` varchar(255) DEFAULT NULL,
  `timeout` int(11) DEFAULT NULL,
  `verify` int(11) DEFAULT NULL,
  `flag` int(11) DEFAULT NULL,
  `mon_flag` int(11) DEFAULT NULL,
  `alias` varchar(50) DEFAULT NULL,
  `sendmobiles` varchar(500) DEFAULT NULL,
  `netid` varchar(100) DEFAULT NULL,
  `sendemail` varchar(500) DEFAULT NULL,
  `sendphone` varchar(100) DEFAULT NULL,
  `ipaddress` varchar(15) DEFAULT NULL,
  `supperid` int(11) DEFAULT NULL,
  `keyword` varchar(200) DEFAULT NULL,
  `pagesize_min` varchar(20) DEFAULT NULL,
  `tracertflag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_urlconfig`
--

LOCK TABLES `nms_urlconfig` WRITE;
/*!40000 ALTER TABLE `nms_urlconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_urlconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_user_audit`
--

DROP TABLE IF EXISTS `nms_user_audit`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_user_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `action` varchar(500) CHARACTER SET gb2312 DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_user_audit`
--

LOCK TABLES `nms_user_audit` WRITE;
/*!40000 ALTER TABLE `nms_user_audit` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_user_audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nms_user_tasklog`
--

DROP TABLE IF EXISTS `nms_user_tasklog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_user_tasklog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `content` varchar(1000) CHARACTER SET gb2312 DEFAULT NULL,
  `time` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `nms_user_tasklog`
--

LOCK TABLES `nms_user_tasklog` WRITE;
/*!40000 ALTER TABLE `nms_user_tasklog` DISABLE KEYS */;
/*!40000 ALTER TABLE `nms_user_tasklog` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:06
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `outbox`
--

DROP TABLE IF EXISTS `outbox`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `outbox` (
  `ID` int(4) NOT NULL AUTO_INCREMENT,
  `ExpressLevel` int(4) NOT NULL DEFAULT '2',
  `Sender` varchar(50) DEFAULT NULL,
  `ReceiverMobileNo` varchar(50) DEFAULT NULL,
  `Msg` varchar(500) DEFAULT NULL,
  `SendTime` datetime NOT NULL,
  `NeedReport` int(4) DEFAULT '0',
  `IsChinese` int(4) DEFAULT '1',
  `CommPort` int(4) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PRIMARY_OUTBOX` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `outbox`
--

LOCK TABLES `outbox` WRITE;
/*!40000 ALTER TABLE `outbox` DISABLE KEYS */;
/*!40000 ALTER TABLE `outbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sendedoutbox`
--

DROP TABLE IF EXISTS `sendedoutbox`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sendedoutbox` (
  `ID` int(4) NOT NULL AUTO_INCREMENT,
  `ExpressLevel` int(4) DEFAULT NULL,
  `Sender` varchar(50) DEFAULT NULL,
  `ReceiverMobileNo` varchar(50) DEFAULT NULL,
  `Msg` varchar(500) DEFAULT NULL,
  `SendTime` datetime DEFAULT NULL,
  `MsgReference` int(4) DEFAULT NULL,
  `NeedReport` int(4) DEFAULT '0',
  `IsChinese` int(4) DEFAULT '0',
  `CommPort` int(4) DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PRIMARY_SENDEDBOX` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sendedoutbox`
--

LOCK TABLES `sendedoutbox` WRITE;
/*!40000 ALTER TABLE `sendedoutbox` DISABLE KEYS */;
/*!40000 ALTER TABLE `sendedoutbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server_telnet_config`
--

DROP TABLE IF EXISTS `server_telnet_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `server_telnet_config` (
  `id` int(3) NOT NULL,
  `node_id` int(5) DEFAULT NULL,
  `users` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `prompt` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `server_telnet_config`
--

LOCK TABLES `server_telnet_config` WRITE;
/*!40000 ALTER TABLE `server_telnet_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `server_telnet_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sla_config_command`
--

DROP TABLE IF EXISTS `sla_config_command`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sla_config_command` (
  `id` int(11) NOT NULL,
  `filename` varchar(200) DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `fileDesc` varchar(200) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `slatype` varchar(200) NOT NULL,
  `devicetype` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sla_config_command`
--

LOCK TABLES `sla_config_command` WRITE;
/*!40000 ALTER TABLE `sla_config_command` DISABLE KEYS */;
INSERT INTO `sla_config_command` VALUES (2,'slascript/cisco/icmp.log','admin','2011-11-11 12:05:26','ICMP Echo 作业','icmp','icmp','cisco'),(3,'slascript/cisco/icmppath.log','admin','2011-11-11 12:05:46','ICMP PATH echo 作业','icmppath','icmppath','cisco'),(4,'slascript/cisco/udp.log','admin','2011-11-11 12:09:06','UDP Eche 作业','udp','udp','cisco'),(5,'slascript/cisco/jitter.log','admin','2011-11-11 12:09:31','jitter 抖动作业','jitter','jitter','cisco'),(6,'slascript/cisco/tcpconnectwithresponder.log','系统管理员','2011-11-20 15:52:22','tcpConnect 连接作业（启用Responder）','tcpconnectwithresponder','tcpconnectwithresponder','cisco'),(7,'slascript/cisco/tcpconnect-norespond.log','admin','2011-11-11 12:15:05','tcpConnect连接作业（不启用Respond）','tcpconnect-norespond','tcpconnect-noresponder','cisco'),(8,'slascript/cisco/http.log','admin','2011-11-11 12:13:46','HTTP连接作业','http','http','cisco'),(9,'slascript/cisco/dns.log','admin','2011-11-11 12:14:13','DNS作业','dns','dns','cisco'),(10,'slascript/h3c/icmp.log','系统管理员','2011-11-28 19:01:24','icmp','icmp','icmp','h3c'),(11,'slascript/h3c/jitter.log','系统管理员','2011-12-05 18:10:59','Jitter抖动','jitter','jitter','h3c'),(12,'slascript/h3c/tcp.log','系统管理员','2011-12-05 18:06:49','TCP连接','tcp','tcpconnect-noresponder','h3c'),(13,'slascript/h3c/udp echo.log','系统管理员','2011-12-05 18:08:33','UDP连接','udp echo','udp','h3c'),(14,'slascript/h3c/http.log','系统管理员','2011-12-05 18:10:39','HTTP连接作业','http','http','h3c');
/*!40000 ALTER TABLE `sla_config_command` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sms_server`
--

DROP TABLE IF EXISTS `sms_server`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sms_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `mobilenum` varchar(100) DEFAULT NULL,
  `eventlist` varchar(500) DEFAULT NULL,
  `eventtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sms_server`
--

LOCK TABLES `sms_server` WRITE;
/*!40000 ALTER TABLE `sms_server` DISABLE KEYS */;
INSERT INTO `sms_server` VALUES (1,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-13 00:53:52'),(2,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-13 00:53:52'),(3,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-13 00:53:52'),(4,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-13 00:53:52'),(5,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-13 00:53:53'),(6,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-13 00:53:53'),(7,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-13 00:53:53'),(8,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-13 00:53:53'),(9,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 00:59:24'),(10,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 00:59:24'),(11,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 00:59:24'),(12,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 00:59:24'),(13,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:39:40'),(14,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:39:40'),(15,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:39:40'),(16,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:39:40'),(17,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:39:43'),(18,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:39:43'),(19,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:39:43'),(20,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:39:43'),(21,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:43:07'),(22,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:43:07'),(23,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:43:07'),(24,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:43:07'),(25,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:43:10'),(26,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:43:10'),(27,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:43:10'),(28,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:43:10'),(29,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:45:38'),(30,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:45:38'),(31,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:45:38'),(32,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:45:38'),(33,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:45:41'),(34,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:45:41'),(35,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:45:41'),(36,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:45:41'),(37,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:50:35'),(38,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:50:35'),(39,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:50:35'),(40,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-13 02:50:35'),(41,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-14 02:54:24'),(42,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-14 02:54:24'),(43,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-14 02:54:24'),(44,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-14 02:54:24'),(45,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-14 02:54:25'),(46,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-14 02:54:25'),(47,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-14 02:54:25'),(48,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:9 % 阀值:3 %','2012-12-14 02:54:25'),(49,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-14 02:59:20'),(50,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-14 02:59:20'),(51,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-14 02:59:20'),(52,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-14 02:59:20'),(53,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:45:30'),(54,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:45:30'),(55,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:45:30'),(56,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:45:30'),(57,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:45:34'),(58,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:45:34'),(59,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:45:34'),(60,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:45:34'),(61,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:50:24'),(62,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:50:24'),(63,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:50:24'),(64,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-17 02:50:24'),(65,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:29:50'),(66,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:29:50'),(67,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:29:50'),(68,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:29:50'),(69,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:29:53'),(70,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:29:53'),(71,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:29:53'),(72,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:29:53'),(73,'netadmin','13981928489','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:41:40'),(74,'users','15928045542','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:41:41'),(75,'系统管理员','','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:41:41'),(76,'hukelei','13811372044','10.10.1.1(10.10.1.1)  CPU利用率超过阀值 当前值:8 % 阀值:3 %','2012-12-19 00:41:41');
/*!40000 ALTER TABLE `sms_server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `statusreport`
--

DROP TABLE IF EXISTS `statusreport`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `statusreport` (
  `ID` int(4) NOT NULL AUTO_INCREMENT,
  `MsgReference` int(4) DEFAULT NULL,
  `Receiver` varchar(50) DEFAULT NULL,
  `OriginSendTime` datetime DEFAULT NULL,
  `ArrivedTime` datetime DEFAULT NULL,
  `CommPort` int(4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PRIMARY_STATUSREPORT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `statusreport`
--

LOCK TABLES `statusreport` WRITE;
/*!40000 ALTER TABLE `statusreport` DISABLE KEYS */;
/*!40000 ALTER TABLE `statusreport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config_command`
--

DROP TABLE IF EXISTS `sys_config_command`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_config_command` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(200) DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `fileDesc` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sys_config_command`
--

LOCK TABLES `sys_config_command` WRITE;
/*!40000 ALTER TABLE `sys_config_command` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_config_command` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dkdb_ssresources`
--

DROP TABLE IF EXISTS `sys_dkdb_ssresources`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_dkdb_ssresources` (
  `SUBSCRIBE_ID` bigint(20) NOT NULL,
  `BIDTEXT` varchar(500) DEFAULT NULL,
  `BID` varchar(500) DEFAULT NULL,
  `USERNAME` varchar(50) DEFAULT NULL,
  `EMAIL` varchar(200) DEFAULT NULL,
  `EMAILTITLE` varchar(200) DEFAULT NULL,
  `EMAILCONTENT` varchar(1024) DEFAULT NULL,
  `ATTACHMENTFORMAT` varchar(10) DEFAULT NULL,
  `REPORT_TYPE` varchar(32) DEFAULT NULL,
  `REPORT_SENDDATE` decimal(13,0) DEFAULT NULL,
  `REPORT_SENDFREQUENCY` decimal(13,0) DEFAULT NULL,
  `REPORT_TIME_MONTH` varchar(200) DEFAULT NULL,
  `REPORT_TIME_WEEK` varchar(200) DEFAULT NULL,
  `REPORT_TIME_DAY` varchar(200) DEFAULT NULL,
  `REPORT_TIME_HOU` varchar(200) DEFAULT NULL,
  `REPORT_DAY_STOP` varchar(32) DEFAULT NULL,
  `REPORT_WEEK_STOP` varchar(32) DEFAULT NULL,
  `REPORT_MONTH_STOP` varchar(32) DEFAULT NULL,
  `REPORT_SEASON_STOP` varchar(32) DEFAULT NULL,
  `REPORT_YEAR_STOP` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`SUBSCRIBE_ID`),
  CONSTRAINT `sys_dkdb_ssresources_ibfk_1` FOREIGN KEY (`SUBSCRIBE_ID`) REFERENCES `nms_dkdbareport` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sys_dkdb_ssresources`
--

LOCK TABLES `sys_dkdb_ssresources` WRITE;
/*!40000 ALTER TABLE `sys_dkdb_ssresources` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dkdb_ssresources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_gather_aclbase`
--

DROP TABLE IF EXISTS `sys_gather_aclbase`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_gather_aclbase` (
  `id` bigint(13) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(50) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sys_gather_aclbase`
--

LOCK TABLES `sys_gather_aclbase` WRITE;
/*!40000 ALTER TABLE `sys_gather_aclbase` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_gather_aclbase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_gather_acldetail`
--

DROP TABLE IF EXISTS `sys_gather_acldetail`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_gather_acldetail` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `baseId` int(20) DEFAULT NULL,
  `name` varchar(150) DEFAULT NULL,
  `value` int(15) DEFAULT NULL,
  `matches` int(15) DEFAULT NULL,
  `desciption` varchar(200) DEFAULT NULL,
  `status` int(2) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sys_gather_acldetail`
--

LOCK TABLES `sys_gather_acldetail` WRITE;
/*!40000 ALTER TABLE `sys_gather_acldetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_gather_acldetail` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:07
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_gather_acllist`
--

DROP TABLE IF EXISTS `sys_gather_acllist`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_gather_acllist` (
  `id` bigint(13) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(50) DEFAULT NULL,
  `command` varchar(150) DEFAULT NULL,
  `isMonitor` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sys_gather_acllist`
--

LOCK TABLES `sys_gather_acllist` WRITE;
/*!40000 ALTER TABLE `sys_gather_acllist` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_gather_acllist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_gather_telnetconfig`
--

DROP TABLE IF EXISTS `sys_gather_telnetconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_gather_telnetconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `telnetIps` varchar(200) DEFAULT NULL,
  `commands` varchar(300) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `status` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sys_gather_telnetconfig`
--

LOCK TABLES `sys_gather_telnetconfig` WRITE;
/*!40000 ALTER TABLE `sys_gather_telnetconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_gather_telnetconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_pwdbackup_telnetconfig`
--

DROP TABLE IF EXISTS `sys_pwdbackup_telnetconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_pwdbackup_telnetconfig` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `telnetconfigips` varchar(200) DEFAULT NULL,
  `warntype` varchar(200) DEFAULT NULL,
  `BACKUP_FILENAME` varchar(200) DEFAULT NULL,
  `BACKUP_TYPE` varchar(200) DEFAULT NULL,
  `BACKUP_DATE` decimal(13,0) DEFAULT NULL,
  `BACKUP_SENDFREQUENCY` varchar(200) DEFAULT NULL,
  `BACKUP_TIME_MONTH` varchar(200) DEFAULT NULL,
  `BACKUP_TIME_WEEK` varchar(200) DEFAULT NULL,
  `BACKUP_TIME_DAY` varchar(200) DEFAULT NULL,
  `BACKUP_TIME_HOU` varchar(200) DEFAULT NULL,
  `BACKUP_DAY_STOP` varchar(32) DEFAULT NULL,
  `BACKUP_WEEK_STOP` varchar(32) DEFAULT NULL,
  `BACKUP_MONTH_STOP` varchar(32) DEFAULT NULL,
  `BACKUP_SEASON_STOP` varchar(32) DEFAULT NULL,
  `BACKUP_YEAR_STOP` varchar(32) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sys_pwdbackup_telnetconfig`
--

LOCK TABLES `sys_pwdbackup_telnetconfig` WRITE;
/*!40000 ALTER TABLE `sys_pwdbackup_telnetconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_pwdbackup_telnetconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_subscribe_history`
--

DROP TABLE IF EXISTS `sys_subscribe_history`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_subscribe_history` (
  `SUBSCRIBE_ID` varchar(13) NOT NULL,
  `FILE_NAME` varchar(500) NOT NULL,
  `REPORT_TYPE` varchar(20) NOT NULL,
  `CREATE_DATE` decimal(14,0) NOT NULL,
  `SUCCESS_POST` decimal(2,0) NOT NULL,
  `REPEAT_POST` decimal(2,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sys_subscribe_history`
--

LOCK TABLES `sys_subscribe_history` WRITE;
/*!40000 ALTER TABLE `sys_subscribe_history` DISABLE KEYS */;
INSERT INTO `sys_subscribe_history` VALUES ('6','D:\\Tomcat6.0\\tjjfgzj\\webapps\\afunms\\temp\\report_1362996582702.xls','day','20130311180943','0','1');
/*!40000 ALTER TABLE `sys_subscribe_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_subscribe_resources`
--

DROP TABLE IF EXISTS `sys_subscribe_resources`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_subscribe_resources` (
  `SUBSCRIBE_ID` int(20) NOT NULL AUTO_INCREMENT,
  `BIDTEXT` varchar(500) DEFAULT NULL,
  `BID` varchar(500) DEFAULT NULL,
  `USERNAME` varchar(50) DEFAULT NULL,
  `EMAIL` varchar(200) DEFAULT NULL,
  `EMAILTITLE` varchar(200) DEFAULT NULL,
  `EMAILCONTENT` varchar(1024) DEFAULT NULL,
  `ATTACHMENTFORMAT` varchar(10) DEFAULT NULL,
  `REPORT_TYPE` varchar(32) DEFAULT NULL,
  `REPORT_SENDDATE` decimal(13,0) DEFAULT NULL,
  `REPORT_SENDFREQUENCY` decimal(13,0) DEFAULT NULL,
  `REPORT_TIME_MONTH` varchar(200) DEFAULT NULL,
  `REPORT_TIME_WEEK` varchar(200) DEFAULT NULL,
  `REPORT_TIME_DAY` varchar(200) DEFAULT NULL,
  `REPORT_TIME_HOU` varchar(200) DEFAULT NULL,
  `REPORT_DAY_STOP` varchar(32) DEFAULT NULL,
  `REPORT_WEEK_STOP` varchar(32) DEFAULT NULL,
  `REPORT_MONTH_STOP` varchar(32) DEFAULT NULL,
  `REPORT_SEASON_STOP` varchar(32) DEFAULT NULL,
  `REPORT_YEAR_STOP` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`SUBSCRIBE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sys_subscribe_resources`
--

LOCK TABLES `sys_subscribe_resources` WRITE;
/*!40000 ALTER TABLE `sys_subscribe_resources` DISABLE KEYS */;
INSERT INTO `sys_subscribe_resources` VALUES (6,',2,',',2,','null','hukelei@dhcc.com.cn,hukelei@dhcc.com.cn,yangjunwg@dhcc.com.cn,hukelei@dhcc.com.cn,','网管邮件','设备报表','xls','day','2013031118','1','/01/','/0/','/01/','/18/','null','null','null','null','null');
/*!40000 ALTER TABLE `sys_subscribe_resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_timingbackup_condition`
--

DROP TABLE IF EXISTS `sys_timingbackup_condition`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_timingbackup_condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timingId` int(11) DEFAULT NULL,
  `isContain` int(2) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sys_timingbackup_condition`
--

LOCK TABLES `sys_timingbackup_condition` WRITE;
/*!40000 ALTER TABLE `sys_timingbackup_condition` DISABLE KEYS */;
INSERT INTO `sys_timingbackup_condition` VALUES (2,1,1,''),(3,2,1,'');
/*!40000 ALTER TABLE `sys_timingbackup_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_timingbackup_telnetconfig`
--

DROP TABLE IF EXISTS `sys_timingbackup_telnetconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sys_timingbackup_telnetconfig` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `telnetconfigips` varchar(32) DEFAULT NULL,
  `BACKUP_TYPE` varchar(32) DEFAULT NULL,
  `BACKUP_DATE` decimal(13,0) DEFAULT NULL,
  `BACKUP_SENDFREQUENCY` decimal(13,0) DEFAULT NULL,
  `BACKUP_TIME_MONTH` varchar(200) DEFAULT NULL,
  `BACKUP_TIME_WEEK` varchar(200) DEFAULT NULL,
  `BACKUP_TIME_DAY` varchar(200) DEFAULT NULL,
  `BACKUP_TIME_HOU` varchar(200) DEFAULT NULL,
  `BACKUP_DAY_STOP` varchar(32) DEFAULT NULL,
  `BACKUP_WEEK_STOP` varchar(32) DEFAULT NULL,
  `BACKUP_MONTH_STOP` varchar(32) DEFAULT NULL,
  `BACKUP_SEASON_STOP` varchar(32) DEFAULT NULL,
  `BACKUP_YEAR_STOP` varchar(32) DEFAULT NULL,
  `bkpType` varchar(10) DEFAULT NULL,
  `status` bigint(20) DEFAULT NULL,
  `content` varchar(500) DEFAULT NULL,
  `checkupdateflag` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `sys_timingbackup_telnetconfig`
--

LOCK TABLES `sys_timingbackup_telnetconfig` WRITE;
/*!40000 ALTER TABLE `sys_timingbackup_telnetconfig` DISABLE KEYS */;
INSERT INTO `sys_timingbackup_telnetconfig` VALUES (1,',10.10.117.176','null','2013030815','1','/01/','/0/','/01/','/01/02/03/04/05/06/07/08/09/10/11/12/13/14/15/16/17/18/19/20/21/22/23/','null','null','null','null','null','run',1,'null','null'),(2,',10.10.117.176','null','2013030816','1','/01/','/0/','/01/','/01/02/03/04/05/06/07/08/09/10/11/12/13/14/15/16/17/18/19/20/21/22/23/','null','null','null','null','null','0',1,'show run','null');
/*!40000 ALTER TABLE `sys_timingbackup_telnetconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_alertemail`
--

DROP TABLE IF EXISTS `system_alertemail`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_alertemail` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `smtp` varchar(50) DEFAULT NULL,
  `usedflag` int(2) DEFAULT NULL,
  `mail_address` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_alertemail`
--

LOCK TABLES `system_alertemail` WRITE;
/*!40000 ALTER TABLE `system_alertemail` DISABLE KEYS */;
INSERT INTO `system_alertemail` VALUES (1,'songxl','54255425','221.214.12.238',0,NULL),(2,'donhukelei','hukelei','sohu.com',0,'donghukelei@sohu.com'),(3,'333','333','333',0,NULL),(4,'hukelei','hutao!hutao!','mail.dhcc.com.cn',1,'hukelei@dhcc.com.cn');
/*!40000 ALTER TABLE `system_alertemail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_alertinfoserver`
--

DROP TABLE IF EXISTS `system_alertinfoserver`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_alertinfoserver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(50) DEFAULT NULL,
  `port` varchar(50) DEFAULT NULL,
  `infodesc` varchar(50) DEFAULT NULL,
  `flag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_alertinfoserver`
--

LOCK TABLES `system_alertinfoserver` WRITE;
/*!40000 ALTER TABLE `system_alertinfoserver` DISABLE KEYS */;
INSERT INTO `system_alertinfoserver` VALUES (7,'10.10.152.57','5000','使用客户端进行告警信息提醒的服务器IP设置',1);
/*!40000 ALTER TABLE `system_alertinfoserver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_business`
--

DROP TABLE IF EXISTS `system_business`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_business` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `descr` varchar(100) DEFAULT NULL,
  `pid` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_business`
--

LOCK TABLES `system_business` WRITE;
/*!40000 ALTER TABLE `system_business` DISABLE KEYS */;
INSERT INTO `system_business` VALUES (1,'所有业务','所有业务','0'),(2,'信息中心','信息中心','1'),(3,'网管业务','网管业务','1'),(4,'办公网网络业务','办公网网络业务','3'),(5,'广域网网络业务','广域网网络业务','3'),(6,'测试业务','测试业务','1'),(9,'办公大楼域','办公大楼域','8'),(10,'信贷/OA域','信贷/OA域','8'),(11,'生产域','生产域','8'),(12,'网银域','网银域','8'),(13,'外联服务域','外联服务域','8'),(14,'外联接入域','外联接入域','8'),(16,'银川分行','银川分行','15'),(17,'石嘴山分行','石嘴山分行','15'),(19,'西安钟楼分行','西安钟楼分行','18'),(24,'网银业务系统','网银业务系统','21'),(25,'大前置业务系统','大前置业务系统','21'),(27,'信贷业务系统','信贷业务系统','26');
/*!40000 ALTER TABLE `system_business` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:08
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `system_businesssystem`
--

DROP TABLE IF EXISTS `system_businesssystem`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_businesssystem` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `descr` varchar(200) DEFAULT NULL,
  `contactname` varchar(50) DEFAULT NULL,
  `contactemail` varchar(200) DEFAULT NULL,
  `contactphone` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_businesssystem`
--

LOCK TABLES `system_businesssystem` WRITE;
/*!40000 ALTER TABLE `system_businesssystem` DISABLE KEYS */;
INSERT INTO `system_businesssystem` VALUES (1,'OA业务系统','OA业务系统','胡可磊','hukelei@dhcc.com.cn','62662421'),(2,'ERP业务系统','ERP业务系统','张志强','zhangzhiqiang@dhcc.com.cn','62662421');
/*!40000 ALTER TABLE `system_businesssystem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_config`
--

DROP TABLE IF EXISTS `system_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_config` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `variable_name` varchar(20) DEFAULT NULL,
  `value` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_config`
--

LOCK TABLES `system_config` WRITE;
/*!40000 ALTER TABLE `system_config` DISABLE KEYS */;
INSERT INTO `system_config` VALUES (1,'collectwebflag','0'),(2,'treeshowflag','0');
/*!40000 ALTER TABLE `system_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_db2spaceconf`
--

DROP TABLE IF EXISTS `system_db2spaceconf`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_db2spaceconf` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `spacename` varchar(100) DEFAULT NULL,
  `linkuse` varchar(100) DEFAULT NULL,
  `sms` int(2) DEFAULT NULL,
  `bak` varchar(100) DEFAULT NULL,
  `reportflag` int(2) DEFAULT NULL,
  `alarmvalue` bigint(5) DEFAULT NULL,
  `dbname` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_db2spaceconf`
--

LOCK TABLES `system_db2spaceconf` WRITE;
/*!40000 ALTER TABLE `system_db2spaceconf` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_db2spaceconf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_department`
--

DROP TABLE IF EXISTS `system_department`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_department` (
  `id` int(3) NOT NULL,
  `dept` varchar(30) NOT NULL,
  `man` varchar(20) DEFAULT '',
  `tel` varchar(20) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_department`
--

LOCK TABLES `system_department` WRITE;
/*!40000 ALTER TABLE `system_department` DISABLE KEYS */;
INSERT INTO `system_department` VALUES (1,'软件事业部','胡可磊','13811372044'),(2,'信息技术部','林江珧','7586402'),(3,'系统集成部','王磊','62662421');
/*!40000 ALTER TABLE `system_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_envconfig`
--

DROP TABLE IF EXISTS `system_envconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_envconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(28) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `alarmvalue` int(10) DEFAULT NULL,
  `alarmlevel` varchar(10) DEFAULT NULL,
  `alarmtimes` int(3) DEFAULT NULL,
  `entity` varchar(28) DEFAULT NULL,
  `enabled` int(3) DEFAULT NULL,
  `bak` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_envconfig`
--

LOCK TABLES `system_envconfig` WRITE;
/*!40000 ALTER TABLE `system_envconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_envconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_eventreport`
--

DROP TABLE IF EXISTS `system_eventreport`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_eventreport` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `eventid` bigint(11) DEFAULT NULL,
  `report_man` varchar(50) DEFAULT NULL,
  `report_content` text,
  `deal_time` timestamp NULL DEFAULT NULL,
  `report_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_eventreport`
--

LOCK TABLES `system_eventreport` WRITE;
/*!40000 ALTER TABLE `system_eventreport` DISABLE KEYS */;
INSERT INTO `system_eventreport` VALUES (1,37,'系统管理员','10.10.117.176(10.10.117.176)  PING 不通 当前值:0 % 阀值:10 %','2012-12-26 16:00:00','2012-12-26 16:00:00'),(2,35,'系统管理员','10.10.1.2(10.10.1.2)  PING 不通 当前值:0 % 阀值:10 %','2012-12-26 16:00:00','2012-12-26 16:00:00');
/*!40000 ALTER TABLE `system_eventreport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_host_apply`
--

DROP TABLE IF EXISTS `system_host_apply`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_host_apply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一键',
  `nodeid` int(11) NOT NULL COMMENT '应用的ID',
  `type` varchar(30) DEFAULT NULL COMMENT '应用的类型',
  `subtype` varchar(15) DEFAULT NULL COMMENT '应用的子类型',
  `ipaddress` varchar(15) DEFAULT NULL COMMENT '服务器的IP地址',
  `isShow` varchar(10) DEFAULT NULL COMMENT '是否显示',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_host_apply`
--

LOCK TABLES `system_host_apply` WRITE;
/*!40000 ALTER TABLE `system_host_apply` DISABLE KEYS */;
INSERT INTO `system_host_apply` VALUES (2,10,'service','dhcp','10.10.151.156','true');
/*!40000 ALTER TABLE `system_host_apply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_infomixspaceconf`
--

DROP TABLE IF EXISTS `system_infomixspaceconf`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_infomixspaceconf` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `spacename` varchar(50) DEFAULT NULL,
  `linkuse` varchar(200) DEFAULT NULL,
  `sms` int(11) DEFAULT NULL,
  `bak` varchar(200) DEFAULT NULL,
  `reportflag` int(2) DEFAULT NULL,
  `alarmvalue` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_infomixspaceconf`
--

LOCK TABLES `system_infomixspaceconf` WRITE;
/*!40000 ALTER TABLE `system_infomixspaceconf` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_infomixspaceconf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_ipaddresspanel`
--

DROP TABLE IF EXISTS `system_ipaddresspanel`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_ipaddresspanel` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  `imagetype` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_ipaddresspanel`
--

LOCK TABLES `system_ipaddresspanel` WRITE;
/*!40000 ALTER TABLE `system_ipaddresspanel` DISABLE KEYS */;
INSERT INTO `system_ipaddresspanel` VALUES (1,'10.10.1.1','1','1'),(2,'10.10.117.176','1','1');
/*!40000 ALTER TABLE `system_ipaddresspanel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_knowledge`
--

DROP TABLE IF EXISTS `system_knowledge`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_knowledge` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) NOT NULL,
  `entity` varchar(50) NOT NULL,
  `subentity` varchar(50) NOT NULL,
  `attachfiles` varchar(200) NOT NULL,
  `bak` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_knowledge`
--

LOCK TABLES `system_knowledge` WRITE;
/*!40000 ALTER TABLE `system_knowledge` DISABLE KEYS */;
INSERT INTO `system_knowledge` VALUES (5,'net','h3c','cpu','snmp.pdf','test'),(9,'net','h3c','ping','net_ping.pdf','ping'),(10,'net','atm','ping','net_atm_ping.pdf','atm_ping'),(11,'host','windows','cpu','windows-CPU.pdf','Windows系统下CPU利用率方案'),(12,'host','windows','physicalmemory','windows-phymem.pdf','WINDOWS内存'),(13,'host','windows','virtualmemory','windows-virmem.pdf','WINDOWS虚拟内存方案'),(14,'host','windows','diskperc','windows_disk.pdf','WINDOWS磁盘事件解决方案'),(15,'host','windows','ping','windows_ping.pdf','WINDOWS连通率事件解决方案'),(16,'host','aix','cpu','aix-CPU.pdf','AIX的CPU解决方案'),(17,'host','aix','diskperc','AIX-disk.pdf','AIX的磁盘解决方案'),(18,'host','aix','physicalmemory','AIX-mem.pdf','AIX内存事件解决方案'),(19,'network','h3c','interface','L30_RG_CN_01.pdf','测试电话');
/*!40000 ALTER TABLE `system_knowledge` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:09
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `system_knowledgebase`
--

DROP TABLE IF EXISTS `system_knowledgebase`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_knowledgebase` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) NOT NULL,
  `entity` varchar(50) NOT NULL,
  `subentity` varchar(50) NOT NULL,
  `titles` varchar(50) NOT NULL,
  `contents` varchar(5000) NOT NULL,
  `bak` varchar(100) DEFAULT NULL,
  `attachfiles` varchar(100) NOT NULL,
  `userid` varchar(30) NOT NULL,
  `ktime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_knowledgebase`
--

LOCK TABLES `system_knowledgebase` WRITE;
/*!40000 ALTER TABLE `system_knowledgebase` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_knowledgebase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_menu`
--

DROP TABLE IF EXISTS `system_menu`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_menu` (
  `id` varchar(10) NOT NULL,
  `title` varchar(30) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `sort` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_menu`
--

LOCK TABLES `system_menu` WRITE;
/*!40000 ALTER TABLE `system_menu` DISABLE KEYS */;
INSERT INTO `system_menu` VALUES ('0100','系统管理','top_menu',8),('0101','用户','user.do?action=list&jp=1',8),('0102','角色','role.do?action=list&jp=1',8),('0103','部门','dept.do?action=list&jp=1',8),('0104','职位','position.do?action=list&jp=1',8),('0105','菜单','menu.do?action=list_top',8),('0106','权限','admin.do?action=list',8),('0107','日志','syslog.do?action=list&jp=1',8),('0200','拓扑管理','top_menu',1),('0201','网络拓扑','topology/network/index.jsp',1),('0202','服务器','topology/server/index.jsp',1),('0203','子网','subnet.do?action=list',1),('0204','链路信息','link.do?action=list',1),('0205','发现结果','discover.do?action=list&jp=1',1),('0206','自动发现','discover.do?action=config',1),('0207','网络VLAN拓扑','topology/network/vlanindex.jsp',1),('0300','系统设置','top_menu',7),('0301','服务','service.do?action=list',7),('0302','生产商','producer.do?action=list&jp=1',7),('0303','设备类型','devicetype.do?action=list&jp=1',7),('0400','视图定制','top_menu',2),('0402','视图编辑','customxml.do?action=list&jp=1',2),('0403','视图展现','topology/view/custom.jsp',2),('0500','应用','top_menu',6),('0501','数据库','db.do?action=list',6),('0502','Tomcat','tomcat.do?action=list',6),('0503','IP列表','ipnode.do?action=list',6),('0600','告警','top_menu',5),('0601','告警','alarm.do?action=list&jp=1',5),('0602','报表','inform/report/report.jsp',5),('0603','告警浏览','event.do?action=list&jp=1',5),('0700','IP资源','top_menu',3),('0701','IP列表','ipmac.do?action=list&jp=1',3),('0702','IP变更','ipres.do?action=list&jp=1',3),('0703','IP分布','ipres.do?action=detail',3),('0704','IP定位','ipres.do?action=locate',3),('0800','报表','top_menu',4),('0801','门禁日志','security/gate/index.jsp',4),('0802','UPS监控','ups.do?action=list',4),('0900','资源','top_menu',9),('0901','业务分类','business.do?action=list',9),('0902','设备维护','network.do?action=list',9);
/*!40000 ALTER TABLE `system_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_mysqlspaceconf`
--

DROP TABLE IF EXISTS `system_mysqlspaceconf`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_mysqlspaceconf` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `dbname` varchar(100) DEFAULT NULL,
  `linkuse` varchar(100) DEFAULT NULL,
  `sms` int(2) DEFAULT NULL,
  `bak` varchar(200) DEFAULT NULL,
  `reportflag` int(2) DEFAULT NULL,
  `alarmvalue` bigint(3) DEFAULT NULL,
  `logflag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_mysqlspaceconf`
--

LOCK TABLES `system_mysqlspaceconf` WRITE;
/*!40000 ALTER TABLE `system_mysqlspaceconf` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_mysqlspaceconf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_nodetobusiness`
--

DROP TABLE IF EXISTS `system_nodetobusiness`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_nodetobusiness` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` bigint(11) DEFAULT NULL,
  `businessid` bigint(11) DEFAULT NULL,
  `elementtype` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_nodetobusiness`
--

LOCK TABLES `system_nodetobusiness` WRITE;
/*!40000 ALTER TABLE `system_nodetobusiness` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_nodetobusiness` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_orasessiondata`
--

DROP TABLE IF EXISTS `system_orasessiondata`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_orasessiondata` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(15) DEFAULT NULL,
  `dbname` varchar(50) DEFAULT NULL,
  `machine` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `program` varchar(200) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `sessiontype` varchar(10) DEFAULT NULL,
  `command` varchar(200) DEFAULT NULL,
  `logontime` timestamp NULL DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_orasessiondata`
--

LOCK TABLES `system_orasessiondata` WRITE;
/*!40000 ALTER TABLE `system_orasessiondata` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_orasessiondata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_oraspaceconf`
--

DROP TABLE IF EXISTS `system_oraspaceconf`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_oraspaceconf` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `spacename` varchar(50) DEFAULT NULL,
  `linkuse` varchar(200) DEFAULT NULL,
  `sms` int(11) DEFAULT NULL,
  `bak` varchar(200) DEFAULT NULL,
  `reportflag` int(2) DEFAULT NULL,
  `alarmvalue` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_oraspaceconf`
--

LOCK TABLES `system_oraspaceconf` WRITE;
/*!40000 ALTER TABLE `system_oraspaceconf` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_oraspaceconf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_ostype`
--

DROP TABLE IF EXISTS `system_ostype`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_ostype` (
  `ostypeid` bigint(11) NOT NULL AUTO_INCREMENT,
  `ostypename` varchar(50) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ostypeid`),
  UNIQUE KEY `ostypeid_index` (`ostypeid`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_ostype`
--

LOCK TABLES `system_ostype` WRITE;
/*!40000 ALTER TABLE `system_ostype` DISABLE KEYS */;
INSERT INTO `system_ostype` VALUES (1,'cisco','cisco'),(2,'h3c','h3c'),(3,'Entrasys','Entrasys'),(4,'Radware','Radware'),(5,'Windows','Windows'),(6,'AIX','AIX'),(7,'HPUNIX','HP UNIX'),(8,'SUNSolaris','SUN Solaris'),(9,'Linux','Linux'),(10,'MaiPu','MaiPu'),(11,'RedGiant','RedGiant'),(12,'NorthTel','NorthTel'),(13,'DLink','DLink'),(14,'BDCom','BDCom'),(15,'AS400','AS400'),(16,'DigitalChina','DigitalChina');
/*!40000 ALTER TABLE `system_ostype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_panelmodel`
--

DROP TABLE IF EXISTS `system_panelmodel`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_panelmodel` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `oid` varchar(100) DEFAULT NULL,
  `width` varchar(10) DEFAULT NULL,
  `height` varchar(10) DEFAULT NULL,
  `imagetype` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_panelmodel`
--

LOCK TABLES `system_panelmodel` WRITE;
/*!40000 ALTER TABLE `system_panelmodel` DISABLE KEYS */;
INSERT INTO `system_panelmodel` VALUES (5,'1.3.6.1.4.1.25506.1.38','1049','166','1'),(6,'1.3.6.1.4.1.25506.1.40','807','301','1'),(8,'1.3.6.1.4.1.2011.2.39.11','11','11','1'),(13,'1.3.6.1.4.1.25506.1.38','1049','166','1'),(14,'1.3.6.1.4.1.25506.1.40','807','301','1'),(15,'1.3.6.1.4.1.2011.2.39.11','11','11','1'),(16,'1.3.6.1.4.1.9.1.516','979','96','1'),(17,'1.3.6.1.4.1.9.1.367','983','98','1'),(18,'1.3.6.1.4.1.9.1.502','686','596','1'),(19,'1.3.6.1.4.1.9.1.283','720','891','1'),(20,'1.3.6.1.4.1.9.5.46','832','577','1'),(21,'1.3.6.1.4.1.9.1.617','936','94','1'),(22,'1.3.6.1.4.1.25506.1.54','808','493','1'),(23,'1.3.6.1.4.1.2011.2.23.33','803','563','1'),(24,'1.3.6.1.4.1.25506.1.54','500','500','2'),(25,'1.3.6.1.4.1.25506.1.33','1024','137','1'),(26,'1.3.6.1.4.1.2011.1.1.1.12811','799','76','1'),(27,'1.3.6.1.4.1.25506.1.187','600','400','1');
/*!40000 ALTER TABLE `system_panelmodel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_portconfig`
--

DROP TABLE IF EXISTS `system_portconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_portconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `portindex` bigint(20) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `linkuse` varchar(100) DEFAULT NULL,
  `sms` int(2) DEFAULT NULL,
  `bak` varchar(100) DEFAULT NULL,
  `reportflag` int(2) DEFAULT NULL,
  `inportalarm` varchar(50) DEFAULT NULL,
  `outportalarm` varchar(50) DEFAULT NULL,
  `speed` varchar(20) DEFAULT NULL,
  `alarmlevel` char(1) DEFAULT NULL,
  `flag` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=818 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_portconfig`
--

LOCK TABLES `system_portconfig` WRITE;
/*!40000 ALTER TABLE `system_portconfig` DISABLE KEYS */;
INSERT INTO `system_portconfig` VALUES (555,'10.10.151.157',3,'VMware Virtual Ethernet Adapter for VMnet1','',0,'',0,'2000','2000','100000','1','1'),(556,'10.18.1.79',29,'sit0','',0,'',0,'2000','2000','','1','1'),(557,'10.10.151.157',2,'VMware Virtual Ethernet Adapter for VMnet8','',0,'',0,'2000','2000','100000','1','1'),(558,'10.18.1.79',28,'usb0','',0,'',0,'2000','2000','','1','1'),(561,'10.10.151.157',1,'MS TCP Loopback interface','',0,'',0,'2000','2000','10000','1','1'),(562,'10.18.1.79',27,'eth1','',0,'',0,'2000','2000','','1','1'),(564,'10.18.1.79',26,'eth0','',0,'',0,'2000','2000','','1','1'),(565,'10.18.1.79',25,'lo','',0,'',0,'2000','2000','','1','1'),(659,'10.204.8.4',2,'en5','',0,'',0,'2000','2000','1000','1','1'),(660,'10.204.8.4',1,'en2','',0,'',0,'2000','2000','1000','1','1'),(661,'10.56.12.23',37,'sit0','',0,'',0,'2000','2000','','1','1'),(662,'10.56.12.23',36,'usb0','',0,'',0,'2000','2000','','1','1'),(663,'10.56.12.23',35,'eth3','',0,'',0,'2000','2000','','1','1'),(664,'10.56.12.23',34,'eth2','',0,'',0,'2000','2000','','1','1'),(665,'10.56.12.23',33,'eth1','',0,'',0,'2000','2000','','1','1'),(666,'10.56.12.23',32,'eth0','',0,'',0,'2000','2000','','1','1'),(667,'10.56.12.23',31,'lo','',0,'',0,'2000','2000','','1','1'),(668,'10.10.151.157',1,'MS TCP Loopback interface','',0,'',0,'2000','2000','10000','1','1'),(669,'10.10.151.157',2,'VMware Virtual Ethernet Adapter for VMnet8','',0,'',0,'2000','2000','10000','1','1'),(670,'10.10.151.157',3,'VMware Virtual Ethernet Adapter for VMnet1','',0,'',0,'2000','2000','10000','1','1'),(671,'10.10.151.157',4,'Marvell Yukon 88E8056 PCI-E Gigabit Ethernet Contr','',0,'',0,'2000','2000','10000','1','1'),(814,'10.10.151.157',1,'MS TCP Loopback interface','',0,'',0,'2000','2000','10000','1','1'),(815,'10.10.151.157',2,'VMware Virtual Ethernet Adapter for VMnet8','',0,'',0,'2000','2000','10000','1','1'),(816,'10.10.151.157',3,'VMware Virtual Ethernet Adapter for VMnet1','',0,'',0,'2000','2000','10000','1','1'),(817,'10.10.151.157',4,'Marvell Yukon 88E8056 PCI-E Gigabit Ethernet Contr','',0,'',0,'2000','2000','10000','1','1');
/*!40000 ALTER TABLE `system_portconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_position`
--

DROP TABLE IF EXISTS `system_position`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_position` (
  `id` int(5) NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_position`
--

LOCK TABLES `system_position` WRITE;
/*!40000 ALTER TABLE `system_position` DISABLE KEYS */;
INSERT INTO `system_position` VALUES (1,'软件工程师'),(2,'信息技术部人员');
/*!40000 ALTER TABLE `system_position` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:10
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `system_role`
--

DROP TABLE IF EXISTS `system_role`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_role` (
  `id` int(5) NOT NULL,
  `role` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_role`
--

LOCK TABLES `system_role` WRITE;
/*!40000 ALTER TABLE `system_role` DISABLE KEYS */;
INSERT INTO `system_role` VALUES (0,'superadmin'),(2,'管理员'),(4,'一般人员');
/*!40000 ALTER TABLE `system_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_role_menu`
--

DROP TABLE IF EXISTS `system_role_menu`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_role_menu` (
  `id` int(5) NOT NULL,
  `role_id` int(5) DEFAULT NULL,
  `menu_id` varchar(10) DEFAULT NULL,
  `operate` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_role_menu`
--

LOCK TABLES `system_role_menu` WRITE;
/*!40000 ALTER TABLE `system_role_menu` DISABLE KEYS */;
INSERT INTO `system_role_menu` VALUES (1,0,'0100',3),(2,0,'0101',3),(3,0,'0102',3),(4,0,'0103',3),(5,0,'0104',3),(6,0,'0105',0),(7,0,'0106',0),(8,0,'0107',3),(9,1,'0100',3),(10,1,'0101',3),(11,1,'0102',3),(12,1,'0103',3),(13,1,'0104',3),(14,1,'0105',0),(15,1,'0106',0),(16,1,'0107',3),(17,0,'0200',3),(18,1,'0200',3),(19,0,'0300',0),(20,1,'0300',0),(21,0,'0201',3),(22,1,'0201',3),(23,0,'0202',3),(24,1,'0202',3),(25,0,'0203',3),(26,1,'0203',3),(27,0,'0204',1),(28,1,'0204',3),(29,0,'0205',3),(30,1,'0205',3),(31,0,'0301',0),(32,1,'0301',0),(33,0,'0302',0),(34,1,'0302',0),(35,0,'0303',0),(36,1,'0303',0),(39,0,'0400',0),(40,1,'0400',0),(43,0,'0402',0),(44,1,'0402',0),(45,0,'0403',0),(46,1,'0403',0),(47,0,'0500',0),(48,1,'0500',0),(49,0,'0501',0),(50,1,'0501',0),(51,0,'0502',0),(52,1,'0502',0),(53,0,'0503',0),(54,1,'0503',0),(55,0,'0600',0),(56,1,'0600',0),(57,0,'0601',0),(58,1,'0601',0),(59,0,'0602',0),(60,1,'0602',0),(61,0,'0700',0),(62,1,'0700',0),(63,0,'0701',0),(64,1,'0701',0),(65,0,'0702',0),(66,1,'0702',0),(67,0,'0703',0),(68,1,'0703',0),(69,0,'0704',0),(70,1,'0704',0),(71,2,'0100',3),(72,2,'0101',3),(73,2,'0102',3),(74,2,'0103',3),(75,2,'0104',3),(76,2,'0105',0),(77,2,'0106',0),(78,2,'0107',3),(79,2,'0200',3),(80,2,'0300',0),(81,2,'0201',3),(82,2,'0202',3),(83,2,'0203',3),(84,2,'0204',3),(85,2,'0205',3),(86,2,'0301',0),(87,2,'0302',0),(88,2,'0303',0),(89,2,'0400',0),(90,2,'0402',0),(91,2,'0403',0),(92,2,'0500',0),(93,2,'0501',0),(94,2,'0502',0),(95,2,'0503',0),(96,2,'0600',0),(97,2,'0601',0),(98,2,'0602',0),(99,2,'0700',0),(100,2,'0701',0),(101,2,'0702',0),(102,2,'0703',0),(103,2,'0704',0),(104,0,'0800',0),(105,1,'0800',0),(106,2,'0800',0),(107,0,'0801',0),(108,1,'0801',0),(109,2,'0801',0),(110,0,'0802',0),(111,1,'0802',0),(112,2,'0802',0),(113,3,'0100',3),(114,3,'0101',2),(115,3,'0102',2),(116,3,'0103',2),(117,3,'0104',2),(118,3,'0105',0),(119,3,'0106',0),(120,3,'0107',2),(121,3,'0200',3),(122,3,'0300',0),(123,3,'0201',3),(124,3,'0202',3),(125,3,'0203',3),(126,3,'0204',1),(127,3,'0205',3),(128,3,'0301',0),(129,3,'0302',0),(130,3,'0303',0),(131,3,'0400',3),(132,3,'0402',0),(133,3,'0403',0),(134,3,'0500',3),(135,3,'0501',0),(136,3,'0502',0),(137,3,'0503',0),(138,3,'0600',3),(139,3,'0601',0),(140,3,'0602',0),(141,3,'0700',3),(142,3,'0701',0),(143,3,'0702',0),(144,3,'0703',0),(145,3,'0704',0),(146,3,'0800',3),(147,3,'0801',0),(148,3,'0802',0),(149,4,'0100',2),(150,4,'0101',2),(151,4,'0102',1),(152,4,'0103',2),(153,4,'0104',2),(154,4,'0105',0),(155,4,'0106',0),(156,4,'0107',2),(157,4,'0200',2),(158,4,'0300',0),(159,4,'0201',2),(160,4,'0202',2),(161,4,'0203',2),(162,4,'0204',2),(163,4,'0205',2),(164,4,'0301',0),(165,4,'0302',0),(166,4,'0303',0),(167,4,'0400',0),(168,4,'0402',0),(169,4,'0403',0),(170,4,'0500',0),(171,4,'0501',0),(172,4,'0502',0),(173,4,'0503',0),(174,4,'0600',0),(175,4,'0601',0),(176,4,'0602',0),(177,4,'0700',0),(178,4,'0701',0),(179,4,'0702',0),(180,4,'0703',0),(181,4,'0704',0),(182,4,'0800',0),(183,4,'0801',0),(184,4,'0802',0),(185,0,'0206',0),(186,1,'0206',0),(187,2,'0206',0),(188,4,'0206',0);
/*!40000 ALTER TABLE `system_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_serial_node`
--

DROP TABLE IF EXISTS `system_serial_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_serial_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `name` varchar(200) CHARACTER SET gb2312 DEFAULT NULL,
  `description` varchar(1000) CHARACTER SET gb2312 DEFAULT NULL,
  `monflag` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `serialportid` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `baudrate` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `databits` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `stopbits` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `parity` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `bid` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `sendmail` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_serial_node`
--

LOCK TABLES `system_serial_node` WRITE;
/*!40000 ALTER TABLE `system_serial_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_serial_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_snmpconfig`
--

DROP TABLE IF EXISTS `system_snmpconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_snmpconfig` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `snmpversion` int(5) DEFAULT NULL,
  `readcommunity` varchar(50) DEFAULT NULL,
  `writecommunity` varchar(50) DEFAULT NULL,
  `timeout` int(5) DEFAULT NULL,
  `trytime` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_snmpconfig`
--

LOCK TABLES `system_snmpconfig` WRITE;
/*!40000 ALTER TABLE `system_snmpconfig` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_snmpconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_sqldbconf`
--

DROP TABLE IF EXISTS `system_sqldbconf`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_sqldbconf` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `dbname` varchar(100) DEFAULT NULL,
  `linkuse` varchar(100) DEFAULT NULL,
  `sms` int(2) DEFAULT NULL,
  `bak` varchar(200) DEFAULT NULL,
  `reportflag` int(2) DEFAULT NULL,
  `alarmvalue` bigint(3) DEFAULT NULL,
  `logflag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_sqldbconf`
--

LOCK TABLES `system_sqldbconf` WRITE;
/*!40000 ALTER TABLE `system_sqldbconf` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_sqldbconf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_sybspaceconf`
--

DROP TABLE IF EXISTS `system_sybspaceconf`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_sybspaceconf` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(15) DEFAULT NULL,
  `spacename` varchar(200) DEFAULT NULL,
  `linkuse` varchar(200) DEFAULT NULL,
  `sms` int(2) DEFAULT NULL,
  `bak` varchar(200) DEFAULT NULL,
  `reportflag` int(2) DEFAULT NULL,
  `alarmvalue` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_sybspaceconf`
--

LOCK TABLES `system_sybspaceconf` WRITE;
/*!40000 ALTER TABLE `system_sybspaceconf` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_sybspaceconf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_sys_log`
--

DROP TABLE IF EXISTS `system_sys_log`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_sys_log` (
  `id` int(10) NOT NULL,
  `event` varchar(20) DEFAULT NULL,
  `log_time` varchar(20) DEFAULT NULL,
  `ip` varchar(15) DEFAULT '',
  `username` varchar(50) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_sys_log`
--

LOCK TABLES `system_sys_log` WRITE;
/*!40000 ALTER TABLE `system_sys_log` DISABLE KEYS */;
INSERT INTO `system_sys_log` VALUES (1,'系统启动','2013-01-27 11:20:02','127.0.0.1','Tomcat'),(2,'系统启动','2013-02-17 08:48:54','127.0.0.1','Tomcat'),(3,'系统启动','2013-02-17 09:04:44','127.0.0.1','Tomcat'),(4,'系统启动','2013-03-07 15:36:34','127.0.0.1','Tomcat'),(5,'系统启动','2013-03-07 15:37:29','127.0.0.1','Tomcat'),(6,'系统启动','2013-03-07 15:45:16','127.0.0.1','Tomcat'),(7,'登录系统','2013-03-07 15:46:51','0:0:0:0:0:0:0:1','系统管理员'),(8,'系统启动','2013-03-07 16:10:03','127.0.0.1','Tomcat'),(9,'登录系统','2013-03-07 16:10:22','0:0:0:0:0:0:0:1','系统管理员'),(10,'系统启动','2013-03-07 16:54:32','127.0.0.1','Tomcat'),(11,'登录系统','2013-03-07 16:54:50','0:0:0:0:0:0:0:1','系统管理员'),(12,'系统启动','2013-03-07 16:57:27','127.0.0.1','Tomcat'),(13,'登录系统','2013-03-07 16:57:46','0:0:0:0:0:0:0:1','系统管理员'),(14,'登录系统','2013-03-07 17:02:05','0:0:0:0:0:0:0:1','系统管理员'),(15,'登录系统','2013-03-07 17:02:21','0:0:0:0:0:0:0:1','系统管理员'),(16,'系统启动','2013-03-07 17:43:51','127.0.0.1','Tomcat'),(17,'系统关闭','2013-03-07 17:43:56','127.0.0.1','Tomcat'),(18,'系统启动','2013-03-07 17:45:10','127.0.0.1','Tomcat'),(19,'系统关闭','2013-03-07 17:45:16','127.0.0.1','Tomcat'),(20,'系统启动','2013-03-07 17:47:15','127.0.0.1','Tomcat'),(21,'系统关闭','2013-03-07 17:47:22','127.0.0.1','Tomcat'),(22,'系统启动','2013-03-07 17:48:54','127.0.0.1','Tomcat'),(23,'系统关闭','2013-03-07 17:49:00','127.0.0.1','Tomcat'),(24,'系统启动','2013-03-07 18:09:53','127.0.0.1','Tomcat'),(25,'系统启动','2013-03-07 18:11:12','127.0.0.1','Tomcat'),(26,'系统关闭','2013-03-07 18:11:18','127.0.0.1','Tomcat'),(27,'系统启动','2013-03-08 08:44:41','127.0.0.1','Tomcat'),(28,'系统关闭','2013-03-08 08:44:51','127.0.0.1','Tomcat'),(29,'系统启动','2013-03-08 08:46:30','127.0.0.1','Tomcat'),(30,'系统关闭','2013-03-08 08:46:36','127.0.0.1','Tomcat'),(31,'系统启动','2013-03-08 08:53:52','127.0.0.1','Tomcat'),(32,'系统关闭','2013-03-08 08:53:57','127.0.0.1','Tomcat'),(33,'系统启动','2013-03-08 08:56:04','127.0.0.1','Tomcat'),(34,'系统关闭','2013-03-08 08:56:10','127.0.0.1','Tomcat'),(35,'系统关闭','2013-03-08 09:13:36','127.0.0.1','Tomcat'),(36,'系统关闭','2013-03-08 09:48:34','127.0.0.1','Tomcat'),(37,'系统关闭','2013-03-08 09:50:12','127.0.0.1','Tomcat'),(38,'系统关闭','2013-03-08 09:51:22','127.0.0.1','Tomcat'),(39,'系统启动','2013-03-08 09:52:56','127.0.0.1','Tomcat'),(40,'系统关闭','2013-03-08 09:53:06','127.0.0.1','Tomcat'),(41,'系统启动','2013-03-08 09:55:26','127.0.0.1','Tomcat'),(42,'系统关闭','2013-03-08 09:55:37','127.0.0.1','Tomcat'),(43,'系统启动','2013-03-08 09:58:54','127.0.0.1','Tomcat'),(44,'系统关闭','2013-03-08 09:58:59','127.0.0.1','Tomcat'),(45,'系统启动','2013-03-08 10:01:20','127.0.0.1','Tomcat'),(46,'系统关闭','2013-03-08 10:01:25','127.0.0.1','Tomcat'),(47,'系统启动','2013-03-08 10:08:06','127.0.0.1','Tomcat'),(48,'登录系统','2013-03-08 10:08:36','0:0:0:0:0:0:0:1','系统管理员'),(49,'登录系统','2013-03-08 11:14:18','0:0:0:0:0:0:0:1','系统管理员'),(50,'系统启动','2013-03-08 11:38:16','127.0.0.1','Tomcat'),(51,'登录系统','2013-03-08 11:38:39','0:0:0:0:0:0:0:1','系统管理员'),(52,'系统启动','2013-03-08 11:51:16','127.0.0.1','Tomcat'),(53,'登录系统','2013-03-08 11:51:41','0:0:0:0:0:0:0:1','系统管理员'),(54,'登录系统','2013-03-08 11:53:49','0:0:0:0:0:0:0:1','系统管理员'),(55,'系统启动','2013-03-08 14:07:11','127.0.0.1','Tomcat'),(56,'系统启动','2013-03-08 15:34:19','127.0.0.1','Tomcat'),(57,'登录系统','2013-03-08 15:34:33','0:0:0:0:0:0:0:1','系统管理员'),(58,'系统启动','2013-03-08 15:35:53','127.0.0.1','Tomcat'),(59,'登录系统','2013-03-08 15:36:12','0:0:0:0:0:0:0:1','系统管理员'),(60,'系统启动','2013-03-08 15:52:38','127.0.0.1','Tomcat'),(61,'登录系统','2013-03-08 15:53:04','0:0:0:0:0:0:0:1','系统管理员'),(62,'系统启动','2013-03-08 16:11:00','127.0.0.1','Tomcat'),(63,'系统启动','2013-03-08 16:13:01','127.0.0.1','Tomcat'),(64,'系统启动','2013-03-08 16:17:41','127.0.0.1','Tomcat'),(65,'登录系统','2013-03-08 16:33:16','0:0:0:0:0:0:0:1','系统管理员'),(66,'系统启动','2013-03-08 16:36:56','127.0.0.1','Tomcat'),(67,'登录系统','2013-03-08 16:37:15','0:0:0:0:0:0:0:1','系统管理员'),(68,'系统启动','2013-03-08 16:38:52','127.0.0.1','Tomcat'),(69,'登录系统','2013-03-08 16:39:14','0:0:0:0:0:0:0:1','系统管理员'),(70,'系统启动','2013-03-08 16:40:42','127.0.0.1','Tomcat'),(71,'系统启动','2013-03-08 16:41:22','127.0.0.1','Tomcat'),(72,'系统启动','2013-03-08 16:43:09','127.0.0.1','Tomcat'),(73,'系统启动','2013-03-08 16:44:38','127.0.0.1','Tomcat'),(74,'登录系统','2013-03-08 16:45:35','0:0:0:0:0:0:0:1','系统管理员'),(75,'系统启动','2013-03-08 16:47:34','127.0.0.1','Tomcat'),(76,'登录系统','2013-03-08 16:47:53','0:0:0:0:0:0:0:1','系统管理员'),(77,'系统启动','2013-03-11 09:17:40','127.0.0.1','Tomcat'),(78,'系统启动','2013-03-11 14:07:26','127.0.0.1','Tomcat'),(79,'登录系统','2013-03-11 14:17:49','10.10.152.60','系统管理员'),(80,'登录系统','2013-03-11 14:20:49','10.10.152.60','系统管理员'),(81,'登录系统','2013-03-11 14:21:14','10.10.152.60','系统管理员'),(82,'系统启动','2013-03-11 15:31:34','127.0.0.1','Tomcat'),(83,'登录系统','2013-03-11 15:31:52','10.10.152.60','系统管理员'),(84,'系统启动','2013-03-11 15:34:18','127.0.0.1','Tomcat'),(85,'登录系统','2013-03-11 15:34:32','10.10.152.60','系统管理员'),(86,'系统启动','2013-03-11 15:43:09','127.0.0.1','Tomcat'),(87,'登录系统','2013-03-11 15:43:28','10.10.152.60','系统管理员'),(88,'系统启动','2013-03-11 18:09:40','127.0.0.1','Tomcat'),(89,'登录系统','2013-03-11 18:10:14','10.10.152.60','系统管理员'),(90,'登录系统','2013-03-11 18:52:07','10.10.152.60','系统管理员'),(91,'系统启动','2013-04-01 13:44:07','127.0.0.1','Tomcat'),(92,'系统启动','2013-05-09 08:50:48','127.0.0.1','Tomcat');
/*!40000 ALTER TABLE `system_sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_temper_humi_thresh`
--

DROP TABLE IF EXISTS `system_temper_humi_thresh`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_temper_humi_thresh` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `min_temperature` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `max_temperature` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `min_humidity` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `max_humidity` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_temper_humi_thresh`
--

LOCK TABLES `system_temper_humi_thresh` WRITE;
/*!40000 ALTER TABLE `system_temper_humi_thresh` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_temper_humi_thresh` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_temperature_humidity`
--

DROP TABLE IF EXISTS `system_temperature_humidity`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_temperature_humidity` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `temperature` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `humidity` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `time` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_temperature_humidity`
--

LOCK TABLES `system_temperature_humidity` WRITE;
/*!40000 ALTER TABLE `system_temperature_humidity` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_temperature_humidity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_tftpserver`
--

DROP TABLE IF EXISTS `system_tftpserver`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_tftpserver` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(15) DEFAULT NULL,
  `usedflag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_tftpserver`
--

LOCK TABLES `system_tftpserver` WRITE;
/*!40000 ALTER TABLE `system_tftpserver` DISABLE KEYS */;
INSERT INTO `system_tftpserver` VALUES (1,'10.10.152.30',1);
/*!40000 ALTER TABLE `system_tftpserver` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:11
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `system_user`
--

DROP TABLE IF EXISTS `system_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `system_user` (
  `id` int(5) NOT NULL,
  `user_id` varchar(30) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `password` varchar(40) DEFAULT NULL,
  `sex` int(1) DEFAULT NULL,
  `role_id` int(3) DEFAULT NULL,
  `dept_id` int(3) DEFAULT NULL,
  `position_id` int(3) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `mobile` varchar(30) DEFAULT NULL,
  `businessids` varchar(200) DEFAULT NULL,
  `skins` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `system_user`
--

LOCK TABLES `system_user` WRITE;
/*!40000 ALTER TABLE `system_user` DISABLE KEYS */;
INSERT INTO `system_user` VALUES (1,'hukelei','hukelei','9173CD7ED6F444A5C1B61CE055CF65FC',1,0,2,2,'','hukelei@dhcc.com.cn','13811372044','','skin2'),(4,'admin','系统管理员','21232F297A57A5A743894A0E4A801FC3',1,0,2,2,'7586402','hukelei@dhcc.com.cn','',',2,4,5,','null'),(6,'users','users','9BC65C2ABEC141778FFAA729489F3E87',1,4,2,2,'','yangjunwg@dhcc.com.cn','15928045542',',6,',NULL),(7,'netadmin','netadmin','07DED64F812AFB8DA181014A9D087728',1,4,2,2,'','hukelei@dhcc.com.cn','13981928489',NULL,NULL),(8,'test','tet','098F6BCD4621D373CADE4E832627B4F6',1,4,1,1,'','','',',2,',NULL);
/*!40000 ALTER TABLE `system_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_status`
--

DROP TABLE IF EXISTS `test_status`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `test_status` (
  `id` int(3) NOT NULL,
  `descr` varchar(100) DEFAULT NULL,
  `status` tinyint(2) DEFAULT NULL,
  `log_time` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `test_status`
--

LOCK TABLES `test_status` WRITE;
/*!40000 ALTER TABLE `test_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_arp`
--

DROP TABLE IF EXISTS `topo_arp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_arp` (
  `id` bigint(11) NOT NULL,
  `node_id` bigint(11) DEFAULT NULL,
  `ifindex` varchar(50) DEFAULT NULL,
  `physaddress` varchar(200) DEFAULT NULL,
  `ipaddress` varchar(20) DEFAULT NULL,
  `monflag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_arp`
--

LOCK TABLES `topo_arp` WRITE;
/*!40000 ALTER TABLE `topo_arp` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_arp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_custom_xml`
--

DROP TABLE IF EXISTS `topo_custom_xml`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_custom_xml` (
  `id` int(5) NOT NULL,
  `xml_name` varchar(50) DEFAULT NULL,
  `view_name` varchar(50) DEFAULT NULL,
  `default_view` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_custom_xml`
--

LOCK TABLES `topo_custom_xml` WRITE;
/*!40000 ALTER TABLE `topo_custom_xml` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_custom_xml` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_discover_config`
--

DROP TABLE IF EXISTS `topo_discover_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_discover_config` (
  `id` int(3) NOT NULL,
  `address` varchar(15) DEFAULT NULL,
  `community` varchar(20) DEFAULT NULL,
  `flag` varchar(10) DEFAULT NULL,
  `shieldnetstart` varchar(15) DEFAULT NULL,
  `shieldnetend` varchar(15) DEFAULT NULL,
  `includenetstart` varchar(15) DEFAULT NULL,
  `includenetend` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_discover_config`
--

LOCK TABLES `topo_discover_config` WRITE;
/*!40000 ALTER TABLE `topo_discover_config` DISABLE KEYS */;
INSERT INTO `topo_discover_config` VALUES (1,'10.254.254.254','ycccb','core',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `topo_discover_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_discover_stat`
--

DROP TABLE IF EXISTS `topo_discover_stat`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_discover_stat` (
  `id` int(3) NOT NULL,
  `start_time` varchar(20) DEFAULT NULL,
  `end_time` varchar(20) DEFAULT NULL,
  `elapse_time` varchar(20) DEFAULT NULL,
  `host_total` int(3) DEFAULT NULL,
  `subnet_total` int(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_discover_stat`
--

LOCK TABLES `topo_discover_stat` WRITE;
/*!40000 ALTER TABLE `topo_discover_stat` DISABLE KEYS */;
INSERT INTO `topo_discover_stat` VALUES (1,'2012-10-17 13:21:48','2012-10-17 13:22:38','00:00:50',0,0),(2,'2012-10-17 14:19:31','发现未完成...','0-375126:0-19:0-31',207,55),(3,'2012-10-17 14:19:31','2012-10-17 15:42:52','01:23:21',0,0);
/*!40000 ALTER TABLE `topo_discover_stat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_equip_pic`
--

DROP TABLE IF EXISTS `topo_equip_pic`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_equip_pic` (
  `id` int(10) NOT NULL,
  `category` int(10) DEFAULT NULL,
  `cn_name` varchar(50) DEFAULT NULL,
  `en_name` varchar(50) DEFAULT NULL,
  `topo_image` varchar(50) DEFAULT NULL,
  `lost_image` varchar(50) DEFAULT NULL,
  `alarm_image` varchar(50) DEFAULT NULL,
  `path` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_equip_pic`
--

LOCK TABLES `topo_equip_pic` WRITE;
/*!40000 ALTER TABLE `topo_equip_pic` DISABLE KEYS */;
INSERT INTO `topo_equip_pic` VALUES (1,1,'路由器','net_router','router.png','router_lost.png','router-alarm.gif','/afunms/resource/image/topo/router.png'),(2,2,'路由交换机','net_switch_router','switch_with_route.png','switch_with_route.png','switch_with_route_alarm3.gif','/afunms/resource/image/topo/switch_with_route.png'),(3,3,'交换机','net_switch','switch.png','switch_lost.png','switch_alarm3.gif','/afunms/resource/image/topo/switch.png'),(4,4,'主机','net_server','server.gif','server_lost.gif','server_alarm.gif','/afunms/resource/image/topo/server.gif'),(5,5,'打印机','printer','printer.gif','printer_lost.gif','printer_alarm.gif','/afunms/resource/image/topo/printer.gif'),(6,7,'无线路由器','net_wireless','wireless.png','wireless_lost.png','wireless.png','/afunms/resource/image/topo/wireless.png'),(7,50,'IP地址','ip_node','terminal.gif','terminal_lost.gif','terminal_alarm.gif','/afunms/resource/image/topo/terminal.gif'),(8,51,'Tomcat','tomcat','tomcat.gif','tomcat_lost.gif','tomcat_alarm.gif','/afunms/resource/image/topo/tomcat.gif'),(9,52,'MySQL','mysql','mysql.gif','mysql_lost.gif','mysql_alarm.gif','/afunms/resource/image/topo/mysql.gif'),(10,53,'Oracle','oracle','oracle.gif','oracle_lost.gif','oracle_alarm.gif','/afunms/resource/image/topo/oracle.gif'),(11,54,'SQL-Server','sql-server','mssql.gif','mssql_lost.gif','mssql_alarm.gif','/afunms/resource/image/topo/mssql.gif'),(12,55,'Sybase','sybase','sybase.gif','sybase_lost.gif','sybase_alarm.gif','/afunms/resource/image/topo/sybase.gif'),(13,101,'UPS','ups','ups.jpg','ups_lost.jpg','ups_alarm.gif','/afunms/resource/image/topo/ups.jpg'),(18,55,'Sybase','sybase','sybase1.gif','sybase_lost1.gif','sybase_alarm1.gif','/afunms/resource/image/topo/sybase1.gif'),(19,4,'主机','net_server','win_xp.gif','','win_xp_alarm.gif','/afunms/resource/image/topo/win_xp.gif'),(20,4,'主机','net_server','ibm.gif','','ibm_alarm.gif','/afunms/resource/image/topo/ibm.gif'),(21,4,'主机','net_server','win_2000.gif','','win_2000_alarm.gif','/afunms/resource/image/topo/win_2000.gif'),(22,4,'主机','net_server','win_nt.gif','','win_nt_alarm.gif','/afunms/resource/image/topo/win_nt.gif'),(23,4,'主机','net_server','linux.gif','','linux_alarm.gif','/afunms/resource/image/topo/linux.gif'),(24,4,'主机','net_server','solaris.gif','','solaris_alarm.gif','/afunms/resource/image/topo/solaris.gif'),(25,4,'主机','net_server','hp.gif','','hp_alarm.gif','/afunms/resource/image/topo/hp.gif'),(26,4,'主机','net_server','compaq.gif','','compaq_alarm.gif','/afunms/resource/image/topo/compaq.gif'),(27,3,'交换机','net_switch','Switch-B-32.gif',NULL,'switch-alarm.gif','/afunms/resource/image/topo/Switch-B-32.gif'),(28,61,'MQ','mq','mq.jpg','mq_lost.jpg','mq_alarm.gif','/afunms/resource/image/topo/mq.jpg'),(29,62,'Domino','domino','domino.gif','domino_lost.jpg','domino_alarm.gif','/afunms/resource/image/topo/domino.gif'),(30,63,'WAS','was','webphere.gif','was_lost.jpg','was_alarm.gif','/afunms/resource/image/topo/webphere.gif'),(31,64,'Weblogic','weblogic','bea_32.gif','weblogic_lost.jpg','bea_32_alarm.gif','/afunms/resource/image/topo/weblogic.gif'),(32,65,'CICS','cics','cics.jpg','cics_lost.jpg','cics_alarm.gif','/afunms/resource/image/topo/cics.jpg'),(33,66,'防火墙','firewall','firewall.gif','firewall_lost.jpg','firewall_alarm.gif','/afunms/resource/image/topo/firewall.gif'),(34,56,'邮件服务','mail','mail.gif','mail_lost.gif','mail_alarm.gif','/afunms/resource/image/topo/mail.gif'),(35,57,'WEB服务','web','web.gif','web_lost.gif','web_alarm.gif','/afunms/resource/image/topo/web.gif'),(36,67,'IIS','iis','iis.gif','iis_lost.gif','iis_alarm.gif','/afunms/resource/image/topo/iis.gif'),(37,68,'Socket','socket','socket.gif','socket_lost.gif','socket_alarm.gif','/afunms/resource/image/topo/socket.gif'),(38,1,'路由器','net_router','router-1.gif','router-1.gif','router-1-alarm.gif','/afunms/resource/image/topo/router-1.gif'),(39,2,'路由交换机','net_switch_router','switch_with_route.gif','switch_with_route.gif','switch_with_route.gif','/afunms/resource/image/topo/switch_with_route.gif'),(40,3,'交换机','net_switch','switch-1.gif','','switch-1-alarm.gif','/afunms/resource/image/topo/switch-1.gif'),(41,1,'路由器','net_router','router-hxly.png','router-hxly.png','hxly-60-a.gif','/afunms/resource/image/topo/router-hxly.png'),(42,1,'路由器','net_router','router-60.png','router-60.png','router-60-a.gif','/afunms/resource/image/topo/router-60.png'),(43,3,'交换机','net_switch','hxjh-60.png','hxjh-60.png','hxjh-60-a.gif','/afunms/resource/image/topo/hxjh-60.png'),(44,2,'路由交换机','net_switch_router','l3jh-60.png','l3jh-60.png','l3jh-60-a.gif','/afunms/resource/image/topo/l3jh-60.png'),(45,3,'交换机','net_switch','hjjh-60.png','hjjh-60.png','hjjh-60-a.gif','/afunms/resource/image/topo/hjjh-60.png');
/*!40000 ALTER TABLE `topo_equip_pic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_hint_meta`
--

DROP TABLE IF EXISTS `topo_hint_meta`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_hint_meta` (
  `icon_id` varchar(255) NOT NULL,
  `icon_path` varchar(255) NOT NULL,
  `sort_name` varchar(255) DEFAULT NULL,
  `icon_name` varchar(255) NOT NULL,
  `id` varchar(255) NOT NULL,
  `web_icon_path` varchar(255) NOT NULL,
  PRIMARY KEY (`icon_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_hint_meta`
--

LOCK TABLES `topo_hint_meta` WRITE;
/*!40000 ALTER TABLE `topo_hint_meta` DISABLE KEYS */;
INSERT INTO `topo_hint_meta` VALUES ('11111111111111','image\\topo\\up.gif','标题','up','1','/afunms/resource/image/topo/up.gif'),('21312123222222','image\\topo\\wireless\\1.png','无线设备','1','28','/afunms/resource/image/topo/wireless/1.png'),('23231414478788','image\\topo\\unit\\2.gif','Unit集','2','26','/afunms/resource/image/topo/unit/2.gif'),('27933552548997','image\\topo\\router\\1.gif','路由器','1','2','/afunms/resource/image/topo/router/1.gif'),('27933554569645','image\\topo\\router\\2-1.gif','路由器','2','2','/afunms/resource/image/topo/router/2-1.gif'),('27933556455638','image\\topo\\router\\3-1.gif','路由器','3','2','/afunms/resource/image/topo/router/3-1.gif'),('27933558407842','image\\topo\\router\\router.gif','路由器','4','2','/afunms/resource/image/topo/router/router.gif'),('27933560762890','image\\topo\\router\\6-1.gif','路由器','5','2','/afunms/resource/image/topo/router/6-1.gif'),('27933562884388','image\\topo\\router\\5-1.gif','路由器','6','2','/afunms/resource/image/topo/router/5-1.gif'),('27933600171250','image\\topo\\switch\\1.gif','交换机','1','3','/afunms/resource/image/topo/switch/1.gif'),('27933601992990','image\\topo\\switch\\2.gif','交换机','2','3','/afunms/resource/image/topo/switch/2.gif'),('27933603881778','image\\topo\\switch\\3.gif','交换机','3','3','/afunms/resource/image/topo/switch/3.gif'),('27933643840208','image\\topo\\switch3\\1.gif','三层交换机','1','4','/afunms/resource/image/topo/switch3/1.gif'),('27933645365262','image\\topo\\switch3\\2.gif','三层交换机','2','4','/afunms/resource/image/topo/switch3/2.gif'),('27933646969377','image\\topo\\switch3\\3.gif','三层交换机','3','4','/afunms/resource/image/topo/switch3/3.gif'),('27933648611206','image\\topo\\switch3\\4.gif','三层交换机','4','4','/afunms/resource/image/topo/switch3/4.gif'),('27933690112290','image\\topo\\firewall\\3.gif','防火墙','2','5','/afunms/resource/image/topo/firewall/3.gif'),('27933691805243','image\\topo\\firewall\\4.gif','防火墙','3','5','/afunms/resource/image/topo/firewall/4.gif'),('27933727445806','image\\topo\\firewall\\5.gif','防火墙','4','5','/afunms/resource/image/topo/firewall/5.gif'),('27933729538251','image\\topo\\firewall\\3.gif','防火墙','5','5','/afunms/resource/image/topo/firewall/3.gif'),('27933731611419','image\\topo\\firewall\\4.gif','防火墙','6','5','/afunms/resource/image/topo/firewall/4.gif'),('27933754277149','image\\topo\\server\\1.gif','服务器','1','6','/afunms/resource/image/topo/server/1.gif'),('27933760410331','image\\topo\\server\\22.gif','服务器','2','6','/afunms/resource/image/topo/server/22.gif'),('27933766741024','image\\topo\\server\\33.gif','服务器','3','6','/afunms/resource/image/topo/server/33.gif'),('27933772772796','image\\topo\\server\\44.gif','服务器','4','6','/afunms/resource/image/topo/server/44.gif'),('27933772772797','image\\topo\\server\\55.gif','服务器','5','6','/afunms/resource/image/topo/server/55.gif'),('27933772772798','image\\topo\\server\\11.gif','服务器','6','6','/afunms/resource/image/topo/server/11.gif'),('27933881477115','image\\topo\\printer\\1.gif','打印机','1','7','/afunms/resource/image/topo/printer/1.gif'),('27933883663705','image\\topo\\printer\\2.gif','打印机','2','7','/afunms/resource/image/topo/printer/2.gif'),('27933889403541','image\\topo\\printer\\3.gif','打印机','3','7','/afunms/resource/image/topo/printer/3.gif'),('27933891824798','image\\topo\\printer\\4.gif','打印机','4','7','/afunms/resource/image/topo/printer/4.gif'),('27933906919454','image\\topo\\ups\\1.gif','UPS','1','8','/afunms/resource/image/topo/ups/1.gif'),('27933909091797','image\\topo\\ups\\2.gif','UPS','2','8','/afunms/resource/image/topo/ups/2.gif'),('27933915659392','image\\topo\\ups\\3.gif','UPS','3','8','/afunms/resource/image/topo/ups/3.gif'),('27933994308773','image\\topo\\hub\\1.gif','Hub','1','9','/afunms/resource/image/topo/hub/1.gif'),('27933997093205','image\\topo\\hub\\2.gif','Hub','2','9','/afunms/resource/image/topo/hub/2.gif'),('27934106481410','image\\topo\\jigui\\1.png','机柜','1','10','/afunms/resource/image/topo/jigui/1.png'),('27934256524223','image\\topo\\cpu\\1.gif','CPU','1','11','/afunms/resource/image/topo/cpu/1.gif'),('27934258471956','image\\topo\\cpu\\2.gif','CPU','2','11','/afunms/resource/image/topo/cpu/2.gif'),('27934416736764','image\\topo\\webservice\\1.gif','网页服务','1','12','/afunms/resource/image/topo/webservice/1.gif'),('27934418700980','image\\topo\\webservice\\2.gif','网页服务','2','12','/afunms/resource/image/topo/webservice/2.gif'),('27934420497856','image\\topo\\webservice\\3.gif','网页服务','3','12','/afunms/resource/image/topo/webservice/3.gif'),('27934428194924','image\\topo\\mail\\1.gif','邮件服务','1','13','/afunms/resource/image/topo/mail/1.gif'),('27934428194925','image\\topo\\mail\\1.gif','邮件服务','2','13','/afunms/resource/image/topo/mail/2.gif'),('27934450352209','image\\topo\\ftpservice\\1.gif','文件传输服务','1','14','/afunms/resource/image/topo/ftpservice/1.gif'),('27934509164712','image\\topo\\mysql\\11.gif','Mysql系列','1','15','/afunms/resource/image/topo/mysql/11.gif'),('27934510723010','image\\topo\\mysql\\22.gif','Mysql系列','2','15','/afunms/resource/image/topo/mysql/22.gif'),('27934510723011','image\\topo\\mysql\\33.gif','Mysql系列','3','15','/afunms/resource/image/topo/mysql/33.gif'),('27934537416906','image\\topo\\sqlserver\\1.gif','Sqlserver系列','1','16','/afunms/resource/image/topo/sqlserver/1.gif'),('27934539003979','image\\topo\\sqlserver\\2.gif','Sqlserver系列','2','16','/afunms/resource/image/topo/sqlserver/2.gif'),('27934565916897','image\\topo\\oracle\\1.gif','Oracle系列','1','17','/afunms/resource/image/topo/oracle/1.gif'),('27934567490840','image\\topo\\oracle\\2.gif','Oracle系列','2','17','/afunms/resource/image/topo/oracle/2.gif'),('27934567490841','image\\topo\\oracle\\3.gif','Oracle系列','3','17','/afunms/resource/image/topo/oracle/3.gif'),('27934594878119','image\\topo\\sybase\\1.gif','Sybase系列','1','18','/afunms/resource/image/topo/sybase/1.gif'),('27934596573586','image\\topo\\sybase\\2.gif','Sybase系列','2','18','/afunms/resource/image/topo/sybase/2.gif'),('27934613997868','image\\topo\\weblogic\\11.gif','Weblogic系列','1','19','/afunms/resource/image/topo/weblogic/11.gif'),('27934615555608','image\\topo\\weblogic\\22.gif','Weblogic系列','2','19','/afunms/resource/image/topo/weblogic/22.gif'),('27934665503855','image\\topo\\websphere\\1.gif','Websphere系列','1','20','/afunms/resource/image/topo/websphere/1.gif'),('27934667372808','image\\topo\\websphere\\2.gif','Websphere系列','2','20','/afunms/resource/image/topo/websphere/2.gif'),('27934740825719','image\\topo\\tomcat\\1.gif','Tomcat系列','1','21','/afunms/resource/image/topo/tomcat/1.gif'),('27934742709478','image\\topo\\tomcat\\2.gif','Tomcat系列','2','21','/afunms/resource/image/topo/tomcat/2.gif'),('27934765043881','image\\topo\\db2\\1.gif','DB2系列','1','22','/afunms/resource/image/topo/db2/1.gif'),('27934767097494','image\\topo\\db2\\2.gif','DB2系列','2','22','/afunms/resource/image/topo/db2/2.gif'),('27934767097495','image\\topo\\db2\\3.gif','DB2系列','3','22','/afunms/resource/image/topo/db2/3.gif'),('27934836760538','image\\topo\\Apache\\1.gif','Apache系列','1','23','/afunms/resource/image/topo/Apache/1.gif'),('27934844358989','image\\topo\\Apache\\a_5.gif','Apache系列','2','23','/afunms/resource/image/topo/Apache/2.gif'),('27934858976210','image\\topo\\jboss\\1.gif','JBoss系列','1','24','/afunms/resource/image/topo/jboss/1.gif'),('27934860674471','image\\topo\\jboss\\2.gif','JBoss系列','2','24','/afunms/resource/image/topo/jboss/2.gif'),('27935267018294','image\\topo\\unit\\1.png','Unit集','1','26','/afunms/resource/image/topo/unit/1.png'),('27935290622970','image\\topo\\area\\1.gif','地域','1','27','/afunms/resource/image/topo/area/1.gif'),('27935292600037','image\\topo\\area\\2.gif','地域','2','27','/afunms/resource/image/topo/area/2.gif'),('31223231122222','image\\topo\\wireless\\2.png','无线设备','2','28','/afunms/resource/image/topo/wireless/2.png'),('31231232132132','image\\topo\\LAC\\LAC.png','LAC','LAC','29','/afunms/resource/image/topo/LAC/LAC.png'),('32112321312333','image\\topo\\jigui\\3.gif','机柜','3','10','/afunms/resource/image/topo/jigui/3.gif'),('32131231232131','image\\topo\\jigui\\2.gif','机柜','2','10','/afunms/resource/image/topo/jigui/2.gif'),('32334545467777','image\\topo\\WAP\\WAP.png','WAP','WAP','30','/afunms/resource/image/topo/WAP/WAP.png');
/*!40000 ALTER TABLE `topo_hint_meta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_host_node`
--

DROP TABLE IF EXISTS `topo_host_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_host_node` (
  `id` int(5) NOT NULL,
  `ip_address` varchar(15) DEFAULT NULL,
  `ip_long` bigint(10) DEFAULT NULL,
  `sys_name` varchar(50) DEFAULT NULL,
  `alias` varchar(50) DEFAULT NULL,
  `net_mask` varchar(15) DEFAULT NULL,
  `sys_descr` mediumtext,
  `sys_oid` varchar(50) DEFAULT NULL,
  `community` varchar(20) DEFAULT NULL,
  `write_community` varchar(20) DEFAULT NULL,
  `category` int(3) DEFAULT NULL,
  `managed` tinyint(1) DEFAULT NULL,
  `type` varchar(100) DEFAULT NULL,
  `super_node` int(3) DEFAULT NULL,
  `local_net` int(3) DEFAULT NULL,
  `layer` int(1) DEFAULT NULL,
  `bridge_address` varchar(200) DEFAULT NULL,
  `status` int(3) DEFAULT NULL,
  `discoverstatus` int(3) DEFAULT NULL,
  `sys_location` varchar(100) DEFAULT NULL,
  `sys_contact` varchar(100) DEFAULT NULL,
  `snmpversion` int(3) DEFAULT NULL,
  `collecttype` int(3) DEFAULT NULL,
  `ostype` int(3) DEFAULT NULL,
  `sendmobiles` varchar(200) DEFAULT NULL,
  `sendemail` varchar(200) DEFAULT NULL,
  `bid` varchar(200) DEFAULT NULL,
  `endpoint` int(2) DEFAULT '0',
  `sendphone` varchar(200) DEFAULT NULL,
  `supperid` int(11) DEFAULT NULL,
  `transfer` int(11) DEFAULT '0',
  `asset_id` varchar(200) DEFAULT NULL,
  `location` varchar(200) DEFAULT NULL,
  `securitylevel` int(1) DEFAULT NULL,
  `securityname` varchar(50) DEFAULT NULL,
  `v3_ap` int(1) DEFAULT NULL,
  `authpassphrase` varchar(50) DEFAULT NULL,
  `v3_privacy` int(1) DEFAULT NULL,
  `privacypassphrase` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_host_node`
--

LOCK TABLES `topo_host_node` WRITE;
/*!40000 ALTER TABLE `topo_host_node` DISABLE KEYS */;
INSERT INTO `topo_host_node` VALUES (53,'10.10.117.176',168457648,'H3C','10.10.117.176','255.255.255.0','H3C Comware Platform Software\r\nComware softwa','1.3.6.1.4.1.25506.1.33','public','',3,1,'S3600 交换机',0,0,0,'00:23:89:90:b3:e6',0,-1,'Hangzhou China','Hangzhou H3C Technologies Co., Ltd.',0,1,2,'','','',0,'',0,0,'','',NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `topo_host_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_interface`
--

DROP TABLE IF EXISTS `topo_interface`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_interface` (
  `id` int(10) NOT NULL,
  `node_id` int(5) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `descr` varchar(100) DEFAULT NULL,
  `port` varchar(10) DEFAULT NULL,
  `speed` varchar(15) DEFAULT NULL,
  `alias` varchar(100) DEFAULT NULL,
  `phys_address` varchar(50) DEFAULT NULL,
  `ip_address` text,
  `oper_status` tinyint(1) DEFAULT NULL,
  `type` int(10) DEFAULT NULL,
  `chassis` int(5) DEFAULT NULL,
  `slot` int(5) DEFAULT NULL,
  `uport` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_interface`
--

LOCK TABLES `topo_interface` WRITE;
/*!40000 ALTER TABLE `topo_interface` DISABLE KEYS */;
INSERT INTO `topo_interface` VALUES (1,53,'14','NULL0','','0','NULL0','00:00:00:00:00:00','',2,1,-1,-1,-1),(2,53,'16','InLoopBack0','','0','InLoopBack0','00:00:00:00:00:00','',2,24,-1,-1,-1),(3,53,'31','Vlan-interface1','','0','Vlan-interface1','00:23:89:90:b3:e7','10.10.117.176',2,136,-1,-1,-1),(4,53,'4227614','Aux1/0/0','','0','Aux1/0/0','00:00:00:00:00:00','',2,23,-1,-1,-1),(5,53,'4227626','Ethernet1/0/1','1','0','Ethernet1/0/1','00:23:89:90:b3:e6','',2,6,1,0,1),(6,53,'4227634','Ethernet1/0/2','2','100000000','Ethernet1/0/2','00:23:89:90:b3:e6','',2,6,1,0,2),(7,53,'4227642','Ethernet1/0/3','3','100000000','Ethernet1/0/3','00:23:89:90:b3:e6','',2,6,1,0,3),(8,53,'4227650','Ethernet1/0/4','4','100000000','Ethernet1/0/4','00:23:89:90:b3:e6','',2,6,1,0,4),(9,53,'4227658','Ethernet1/0/5','5','100000000','Ethernet1/0/5','00:23:89:90:b3:e6','',2,6,1,0,5),(10,53,'4227666','Ethernet1/0/6','6','0','Ethernet1/0/6','00:23:89:90:b3:e6','',2,6,1,0,6),(11,53,'4227674','Ethernet1/0/7','7','100000000','Ethernet1/0/7','00:23:89:90:b3:e6','',2,6,1,0,7),(12,53,'4227682','Ethernet1/0/8','8','100000000','Ethernet1/0/8','00:23:89:90:b3:e6','',2,6,1,0,8),(13,53,'4227690','Ethernet1/0/9','9','100000000','Ethernet1/0/9','00:23:89:90:b3:e6','',2,6,1,0,9),(14,53,'4227698','Ethernet1/0/10','10','100000000','Ethernet1/0/10','00:23:89:90:b3:e6','',2,6,1,0,10),(15,53,'4227706','Ethernet1/0/11','11','100000000','Ethernet1/0/11','00:23:89:90:b3:e6','',2,6,1,0,11),(16,53,'4227714','Ethernet1/0/12','12','0','Ethernet1/0/12','00:23:89:90:b3:e6','',2,6,1,0,12),(17,53,'4227722','Ethernet1/0/13','13','100000000','Ethernet1/0/13','00:23:89:90:b3:e6','',2,6,1,0,13),(18,53,'4227730','Ethernet1/0/14','14','100000000','Ethernet1/0/14','00:23:89:90:b3:e6','',2,6,1,0,14),(19,53,'4227738','Ethernet1/0/15','15','100000000','Ethernet1/0/15','00:23:89:90:b3:e6','',2,6,1,0,15),(20,53,'4227746','Ethernet1/0/16','16','100000000','Ethernet1/0/16','00:23:89:90:b3:e6','',2,6,1,0,16),(21,53,'4227754','Ethernet1/0/17','17','0','Ethernet1/0/17','00:23:89:90:b3:e6','',2,6,1,0,17),(22,53,'4227762','Ethernet1/0/18','18','100000000','Ethernet1/0/18','00:23:89:90:b3:e6','',2,6,1,0,18),(23,53,'4227770','Ethernet1/0/19','19','100000000','Ethernet1/0/19','00:23:89:90:b3:e6','',2,6,1,0,19),(24,53,'4227778','Ethernet1/0/20','20','0','Ethernet1/0/20','00:23:89:90:b3:e6','',2,6,1,0,20),(25,53,'4227786','Ethernet1/0/21','21','0','Ethernet1/0/21','00:23:89:90:b3:e6','',2,6,1,0,21),(26,53,'4227794','Ethernet1/0/22','22','0','Ethernet1/0/22','00:23:89:90:b3:e6','',2,6,1,0,22),(27,53,'4227802','Ethernet1/0/23','23','0','Ethernet1/0/23','00:23:89:90:b3:e6','',2,6,1,0,23),(28,53,'4227810','Ethernet1/0/24','24','0','Ethernet1/0/24','00:23:89:90:b3:e6','',2,6,1,0,24),(29,53,'4227818','Ethernet1/0/25','25','100000000','Ethernet1/0/25','00:23:89:90:b3:e6','',2,6,1,0,25),(30,53,'4227826','Ethernet1/0/26','26','100000000','Ethernet1/0/26','00:23:89:90:b3:e6','',2,6,1,0,26),(31,53,'4227834','Ethernet1/0/27','27','0','Ethernet1/0/27','00:23:89:90:b3:e6','',2,6,1,0,27),(32,53,'4227842','Ethernet1/0/28','28','100000000','Ethernet1/0/28','00:23:89:90:b3:e6','',2,6,1,0,28),(33,53,'4227850','Ethernet1/0/29','29','100000000','Ethernet1/0/29','00:23:89:90:b3:e6','',2,6,1,0,29),(34,53,'4227858','Ethernet1/0/30','30','100000000','Ethernet1/0/30','00:23:89:90:b3:e6','',2,6,1,0,30),(35,53,'4227866','Ethernet1/0/31','31','10000000','Ethernet1/0/31','00:23:89:90:b3:e6','',2,6,1,0,31),(36,53,'4227874','Ethernet1/0/32','32','100000000','Ethernet1/0/32','00:23:89:90:b3:e6','',2,6,1,0,32),(37,53,'4227882','Ethernet1/0/33','33','0','Ethernet1/0/33','00:23:89:90:b3:e6','',2,6,1,0,33),(38,53,'4227890','Ethernet1/0/34','34','0','Ethernet1/0/34','00:23:89:90:b3:e6','',2,6,1,0,34),(39,53,'4227898','Ethernet1/0/35','35','0','Ethernet1/0/35','00:23:89:90:b3:e6','',2,6,1,0,35),(40,53,'4227906','Ethernet1/0/36','36','100000000','Ethernet1/0/36','00:23:89:90:b3:e6','',2,6,1,0,36),(41,53,'4227914','Ethernet1/0/37','37','0','Ethernet1/0/37','00:23:89:90:b3:e6','',2,6,1,0,37),(42,53,'4227922','Ethernet1/0/38','38','0','Ethernet1/0/38','00:23:89:90:b3:e6','',2,6,1,0,38),(43,53,'4227930','Ethernet1/0/39','39','0','Ethernet1/0/39','00:23:89:90:b3:e6','',2,6,1,0,39),(44,53,'4227938','Ethernet1/0/40','40','100000000','Ethernet1/0/40','00:23:89:90:b3:e6','',2,6,1,0,40),(45,53,'4227946','Ethernet1/0/41','41','0','Ethernet1/0/41','00:23:89:90:b3:e6','',2,6,1,0,41),(46,53,'4227954','Ethernet1/0/42','42','0','Ethernet1/0/42','00:23:89:90:b3:e6','',2,6,1,0,42),(47,53,'4227962','Ethernet1/0/43','43','0','Ethernet1/0/43','00:23:89:90:b3:e6','',2,6,1,0,43),(48,53,'4227970','Ethernet1/0/44','44','0','Ethernet1/0/44','00:23:89:90:b3:e6','',2,6,1,0,44),(49,53,'4227978','Ethernet1/0/45','45','0','Ethernet1/0/45','00:23:89:90:b3:e6','',2,6,1,0,45),(50,53,'4227986','Ethernet1/0/46','46','100000000','Ethernet1/0/46','00:23:89:90:b3:e6','',2,6,1,0,46),(51,53,'4227994','Ethernet1/0/47','47','0','Ethernet1/0/47','00:23:89:90:b3:e6','',2,6,1,0,47),(52,53,'4228002','Ethernet1/0/48','48','0','Ethernet1/0/48','00:23:89:90:b3:e6','',2,6,1,0,48),(53,53,'4228041','GigabitEthernet1/1/1','49','1000000000','GigabitEthernet1/1/1','00:23:89:90:b3:e6','',2,117,1,1,1),(54,53,'4228049','GigabitEthernet1/1/2','50','0','GigabitEthernet1/1/2','00:23:89:90:b3:e6','',2,117,1,1,2),(55,53,'4228057','GigabitEthernet1/1/3','51','0','GigabitEthernet1/1/3','00:23:89:90:b3:e6','',2,117,1,1,3),(56,53,'4228065','GigabitEthernet1/1/4','52','1000000000','GigabitEthernet1/1/4','00:23:89:90:b3:e6','',2,117,1,1,4);
/*!40000 ALTER TABLE `topo_interface` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:11
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `topo_interface_data`
--

DROP TABLE IF EXISTS `topo_interface_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_interface_data` (
  `node_id` int(5) DEFAULT NULL,
  `entity` varchar(50) DEFAULT NULL,
  `moid` varchar(6) DEFAULT NULL,
  `value` int(10) DEFAULT NULL,
  `percentage` float(6,2) DEFAULT NULL,
  `log_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_interface_data`
--

LOCK TABLES `topo_interface_data` WRITE;
/*!40000 ALTER TABLE `topo_interface_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_interface_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_interface_threshold`
--

DROP TABLE IF EXISTS `topo_interface_threshold`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_interface_threshold` (
  `if_id` int(10) NOT NULL,
  `threshold` int(10) DEFAULT NULL,
  `compare_type` tinyint(1) DEFAULT '1',
  `upper_times` tinyint(2) DEFAULT '1',
  `enable` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`if_id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_interface_threshold`
--

LOCK TABLES `topo_interface_threshold` WRITE;
/*!40000 ALTER TABLE `topo_interface_threshold` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_interface_threshold` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_ipalias`
--

DROP TABLE IF EXISTS `topo_ipalias`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_ipalias` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ipaddress` varchar(30) DEFAULT NULL,
  `aliasip` varchar(30) DEFAULT NULL,
  `indexs` int(11) DEFAULT NULL,
  `descr` varchar(200) DEFAULT NULL,
  `speeds` varchar(100) DEFAULT NULL,
  `types` varchar(50) DEFAULT NULL,
  `usedflag` tinyint(2) DEFAULT NULL,
  UNIQUE KEY `ipaliasindex` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_ipalias`
--

LOCK TABLES `topo_ipalias` WRITE;
/*!40000 ALTER TABLE `topo_ipalias` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_ipalias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_manage_xml`
--

DROP TABLE IF EXISTS `topo_manage_xml`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_manage_xml` (
  `id` int(5) NOT NULL,
  `xml_name` varchar(50) DEFAULT NULL COMMENT '生成的xml文件名',
  `topo_name` varchar(100) DEFAULT NULL COMMENT '拓扑图名称',
  `alias_name` varchar(100) DEFAULT NULL COMMENT '别名',
  `topo_title` varchar(100) DEFAULT NULL COMMENT '标题',
  `topo_area` varchar(100) DEFAULT NULL COMMENT '地域',
  `topo_bg` varchar(50) DEFAULT NULL COMMENT '背景图片',
  `topo_type` tinyint(1) DEFAULT '0' COMMENT '拓扑图类型:0.默认 1.业务拓扑图 2.示意拓扑图 3.缩略拓扑图 4.子图',
  `relation_node` varchar(50) DEFAULT NULL COMMENT ' 关联节点',
  `default_view` tinyint(1) DEFAULT '0',
  `bid` varchar(50) DEFAULT NULL,
  `home_view` int(2) DEFAULT '0',
  `bus_home_view` int(2) DEFAULT '0',
  `zoom_percent` float DEFAULT '1',
  `max_utilhdx` varchar(10) DEFAULT '200000',
  `max_utilhdxperc` varchar(5) DEFAULT '10',
  `supperid` varchar(5) DEFAULT '0',
  `fatherid` varchar(5) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_manage_xml`
--

LOCK TABLES `topo_manage_xml` WRITE;
/*!40000 ALTER TABLE `topo_manage_xml` DISABLE KEYS */;
INSERT INTO `topo_manage_xml` VALUES (1,'network.jsp','物理根图','null','物理根图','null','dhcc.jpg',0,NULL,1,',2,4,5,6,',0,0,1,'200000000','1','0','0'),(2,'submap1356346118.jsp','testtest','null','testtest','null','0',4,NULL,0,',2,4,5,6,9,10,11,12,13,14,16,17,19,24,25,27,',0,0,1,'200000','10','0','0');
/*!40000 ALTER TABLE `topo_manage_xml` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_network_link`
--

DROP TABLE IF EXISTS `topo_network_link`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_network_link` (
  `id` int(3) NOT NULL,
  `start_id` int(3) DEFAULT NULL,
  `start_index` varchar(15) DEFAULT '',
  `start_ip` varchar(15) DEFAULT '',
  `start_descr` varchar(100) DEFAULT '',
  `start_port` varchar(10) DEFAULT '',
  `start_mac` varchar(20) DEFAULT '',
  `end_id` int(3) DEFAULT NULL,
  `end_ip` varchar(15) DEFAULT '',
  `end_index` varchar(15) DEFAULT '',
  `end_descr` varchar(100) DEFAULT '',
  `end_port` varchar(10) DEFAULT '',
  `end_mac` varchar(20) DEFAULT '',
  `assistant` tinyint(1) DEFAULT '0',
  `type` tinyint(1) DEFAULT NULL,
  `findtype` int(2) DEFAULT '-1',
  `linktype` int(2) DEFAULT '-1',
  `link_name` varchar(100) DEFAULT NULL,
  `max_speed` varchar(50) DEFAULT NULL,
  `max_per` varchar(10) DEFAULT NULL,
  `showinterf` int(2) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `link_name` (`link_name`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_network_link`
--

LOCK TABLES `topo_network_link` WRITE;
/*!40000 ALTER TABLE `topo_network_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_network_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_node_equip`
--

DROP TABLE IF EXISTS `topo_node_equip`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_node_equip` (
  `id` int(10) NOT NULL,
  `xml_name` varchar(100) DEFAULT NULL,
  `node_id` varchar(50) DEFAULT NULL,
  `equip_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_node_equip`
--

LOCK TABLES `topo_node_equip` WRITE;
/*!40000 ALTER TABLE `topo_node_equip` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_node_equip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_node_id`
--

DROP TABLE IF EXISTS `topo_node_id`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_node_id` (
  `id` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_node_id`
--

LOCK TABLES `topo_node_id` WRITE;
/*!40000 ALTER TABLE `topo_node_id` DISABLE KEYS */;
INSERT INTO `topo_node_id` VALUES (54);
/*!40000 ALTER TABLE `topo_node_id` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_node_monitor`
--

DROP TABLE IF EXISTS `topo_node_monitor`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_node_monitor` (
  `id` int(5) NOT NULL,
  `node_id` int(5) DEFAULT NULL,
  `moid` varchar(6) DEFAULT NULL,
  `threshold` int(10) DEFAULT NULL,
  `threshold_unit` varchar(10) DEFAULT NULL,
  `compare` tinyint(1) DEFAULT '1',
  `compare_type` tinyint(1) DEFAULT '1',
  `upper_times` tinyint(2) DEFAULT '1',
  `alarm_info` varchar(100) DEFAULT '',
  `alarm_level` tinyint(1) DEFAULT '1',
  `enabled` tinyint(1) DEFAULT '1',
  `poll_interval` int(5) DEFAULT NULL,
  `interval_unit` char(1) DEFAULT NULL,
  `nodetype` varchar(10) DEFAULT NULL,
  `subentity` varchar(50) DEFAULT NULL,
  `limenvalue0` bigint(20) DEFAULT NULL,
  `limenvalue1` bigint(20) DEFAULT NULL,
  `limenvalue2` bigint(20) DEFAULT NULL,
  `time0` int(3) DEFAULT NULL,
  `time1` int(3) DEFAULT NULL,
  `time2` int(3) DEFAULT NULL,
  `sms0` int(2) DEFAULT NULL,
  `sms1` int(2) DEFAULT NULL,
  `sms2` int(2) DEFAULT NULL,
  `node_ip` varchar(15) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `descr` varchar(50) DEFAULT NULL,
  `unit` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_node_monitor`
--

LOCK TABLES `topo_node_monitor` WRITE;
/*!40000 ALTER TABLE `topo_node_monitor` DISABLE KEYS */;
INSERT INTO `topo_node_monitor` VALUES (2,53,'002001',80,'%',1,1,2,'CPU利用率超过阀值',1,1,10,'m','net','Utilization',60,70,80,3,3,3,0,0,0,'10.10.117.176','cpu','CPU利用率','%'),(3,53,'002002',80,'%',1,1,2,'内存利用率超过阀值',1,1,10,'m','net','Utilization',60,70,80,3,3,3,0,0,0,'10.10.117.176','memory','内存利用率','%'),(4,53,'002003',80,'%',1,1,2,'Ping不通',1,1,10,'m','net','ConnectUtilization',30,20,10,3,3,3,0,0,0,'10.10.117.176','ping','连通性测试','%'),(5,53,'003001',0,'Kb/秒',1,1,1,'入口流速超过阀值',1,1,15,'m','net','AllInBandwidthUtilHdx',1000000,1200000,1400000,3,3,3,0,0,0,'10.10.117.176','interface','入口流速','Kb/秒'),(6,53,'003002',0,'Kb/秒',1,1,1,'出口流速超过阀值',1,1,15,'m','net','AllOutBandwidthUtilHdx',1000000,1200000,1400000,3,3,3,0,0,0,'10.10.117.176','interface','出口流速','Kb/秒'),(7,53,'003005',0,'%',1,1,1,'接口错误率超过阀值',1,0,15,'m','net','Utilization',10,20,30,3,3,3,0,0,0,'10.10.117.176','errors','接口错误率','%'),(8,53,'003006',0,'%',1,1,1,'接口丢包率超过阀值',1,0,15,'m','net','Utilization',10,20,30,3,3,3,0,0,0,'10.10.117.176','discards','接口丢包率','%');
/*!40000 ALTER TABLE `topo_node_monitor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_node_multi_data`
--

DROP TABLE IF EXISTS `topo_node_multi_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_node_multi_data` (
  `node_id` int(5) NOT NULL,
  `entity` varchar(50) DEFAULT NULL,
  `moid` varchar(6) DEFAULT NULL,
  `value` bigint(10) DEFAULT NULL,
  `percentage` float(6,2) DEFAULT NULL,
  `log_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_node_multi_data`
--

LOCK TABLES `topo_node_multi_data` WRITE;
/*!40000 ALTER TABLE `topo_node_multi_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_node_multi_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_node_single_data`
--

DROP TABLE IF EXISTS `topo_node_single_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_node_single_data` (
  `node_id` int(5) NOT NULL,
  `moid` varchar(6) DEFAULT NULL,
  `value` int(10) DEFAULT NULL,
  `log_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_node_single_data`
--

LOCK TABLES `topo_node_single_data` WRITE;
/*!40000 ALTER TABLE `topo_node_single_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_node_single_data` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:12
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `topo_node_telnetconfig`
--

DROP TABLE IF EXISTS `topo_node_telnetconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_node_telnetconfig` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(15) DEFAULT '',
  `users` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT '',
  `port` int(10) DEFAULT '23',
  `suuser` varchar(100) DEFAULT '',
  `supassword` varchar(100) DEFAULT '',
  `default_promtp` varchar(50) DEFAULT '',
  `enablevpn` int(11) DEFAULT '0',
  `is_synchronized` int(11) DEFAULT NULL,
  `device_render` varchar(30) DEFAULT NULL,
  `pattern` varchar(10) DEFAULT NULL,
  `encrypt` int(5) DEFAULT NULL,
  `ostype` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_node_telnetconfig`
--

LOCK TABLES `topo_node_telnetconfig` WRITE;
/*!40000 ALTER TABLE `topo_node_telnetconfig` DISABLE KEYS */;
INSERT INTO `topo_node_telnetconfig` VALUES (27,'10.10.117.176','root','root',22,'enable','root','#',0,1,'h3c','ssh',NULL,'null');
/*!40000 ALTER TABLE `topo_node_telnetconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_other_node`
--

DROP TABLE IF EXISTS `topo_other_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_other_node` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `ipAddress` varchar(30) CHARACTER SET gb2312 DEFAULT NULL,
  `alais` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `category` int(5) DEFAULT NULL,
  `collecttype` varchar(20) CHARACTER SET gb2312 DEFAULT '代理',
  `sendmobiles` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `sendemail` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `sendphone` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `bid` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `managed` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_other_node`
--

LOCK TABLES `topo_other_node` WRITE;
/*!40000 ALTER TABLE `topo_other_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_other_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_repair_link`
--

DROP TABLE IF EXISTS `topo_repair_link`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_repair_link` (
  `id` int(3) NOT NULL,
  `start_index` varchar(15) DEFAULT '',
  `start_ip` varchar(15) DEFAULT '',
  `end_ip` varchar(15) DEFAULT '',
  `end_index` varchar(15) DEFAULT '',
  `new_start_index` varchar(15) DEFAULT '',
  `new_end_index` varchar(15) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_repair_link`
--

LOCK TABLES `topo_repair_link` WRITE;
/*!40000 ALTER TABLE `topo_repair_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_repair_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_subnet`
--

DROP TABLE IF EXISTS `topo_subnet`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_subnet` (
  `id` int(5) NOT NULL,
  `net_address` varchar(15) DEFAULT NULL,
  `net_mask` varchar(15) DEFAULT NULL,
  `net_long` bigint(10) DEFAULT NULL,
  `managed` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_subnet`
--

LOCK TABLES `topo_subnet` WRITE;
/*!40000 ALTER TABLE `topo_subnet` DISABLE KEYS */;
/*!40000 ALTER TABLE `topo_subnet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `topo_vpn_link`
--

DROP TABLE IF EXISTS `topo_vpn_link`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topo_vpn_link` (
  `id` int(11) NOT NULL,
  `source_ip` varchar(32) DEFAULT NULL,
  `source_id` int(6) DEFAULT NULL,
  `sourceport_name` varchar(120) DEFAULT NULL,
  `sourceport_index` varchar(15) DEFAULT NULL,
  `des_ip` varchar(32) DEFAULT NULL,
  `des_id` int(6) DEFAULT NULL,
  `desport_name` varchar(120) DEFAULT NULL,
  `desport_index` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `topo_vpn_link`
--

LOCK TABLES `topo_vpn_link` WRITE;
/*!40000 ALTER TABLE `topo_vpn_link` DISABLE KEYS */;
INSERT INTO `topo_vpn_link` VALUES (4,'2.22.11.1',27,'gei_2/5','17','2.22.15.1',28,'pos3_4/1 to yanbian','31'),(5,'2.22.19.1',29,'gei_1/1','3','2.22.15.1',28,'gei_1/1','3');
/*!40000 ALTER TABLE `topo_vpn_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vpn_config_command`
--

DROP TABLE IF EXISTS `vpn_config_command`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `vpn_config_command` (
  `id` int(11) NOT NULL,
  `filename` varchar(200) DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `fileDesc` varchar(200) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `vpntype` varchar(200) NOT NULL,
  `devicetype` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `vpn_config_command`
--

LOCK TABLES `vpn_config_command` WRITE;
/*!40000 ALTER TABLE `vpn_config_command` DISABLE KEYS */;
INSERT INTO `vpn_config_command` VALUES (1,'slascript/zte/创建策略_policy.log','系统管理员','2012-02-24 11:48:14','创建VPN策略','创建策略','policy','zte'),(2,'slascript/zte/配置端口_interface.log','系统管理员','2012-02-24 11:49:46','配置vpn端口,前提是在已有的策略上','配置端口','interface','zte'),(3,'slascript/zte/发布_deploy.log','系统管理员','2012-02-24 11:53:09','发布已配置的命令','发布','deploy','zte'),(4,'slascript/zte/取消端口配置_interface.log','系统管理员','2012-02-24 14:33:09','取消端口配置','取消端口配置','interface','zte'),(5,'slascript/zte/取消策略_policy.log','系统管理员','2012-02-24 14:33:47','取消策略','取消策略','policy','zte');
/*!40000 ALTER TABLE `vpn_config_command` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:13
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_as400_job_group`
--

DROP TABLE IF EXISTS `nms_as400_job_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_as400_job_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `nodeid` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `ipaddress` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `mon_flag` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `alarm_level` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_as400_job_group_detail`
--

DROP TABLE IF EXISTS `nms_as400_job_group_detail`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_as400_job_group_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `num` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `name` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `status` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `active_status` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `active_status_type` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_as400_subsystem`
--

DROP TABLE IF EXISTS `nms_as400_subsystem`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_as400_subsystem` (
  `id` int(11) DEFAULT NULL,
  `nodeid` varchar(11) CHARACTER SET gb2312 DEFAULT NULL,
  `ipaddress` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `name` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `current_active_jobs` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `is_exists` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `path` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `object_description` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `collect_time` varchar(50) CHARACTER SET gb2312 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_as400_system_pool`
--

DROP TABLE IF EXISTS `nms_as400_system_pool`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_as400_system_pool` (
  `id` int(11) DEFAULT NULL,
  `nodeid` varchar(11) CHARACTER SET gb2312 DEFAULT NULL,
  `ipaddress` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `system_pool` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `name` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `sizes` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `reserved_size` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `maximum_active_threads` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `collect_time` varchar(50) CHARACTER SET gb2312 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_as400_system_value`
--

DROP TABLE IF EXISTS `nms_as400_system_value`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_as400_system_value` (
  `nodeid` varchar(11) CHARACTER SET gb2312 DEFAULT NULL,
  `ipaddress` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `category` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `value` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `unit` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `description` varchar(255) CHARACTER SET gb2312 DEFAULT NULL,
  `collect_time` varchar(255) CHARACTER SET gb2312 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_checkevent`
--

DROP TABLE IF EXISTS `nms_checkevent`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_checkevent` (
  `nodeid` varchar(30) NOT NULL,
  `alarmlevel` int(2) DEFAULT NULL,
  `collecttime` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `indicators_name` varchar(50) DEFAULT NULL,
  `content` varchar(100) DEFAULT NULL,
  `thevalue` varchar(50) DEFAULT NULL,
  `sindex` varchar(100) DEFAULT NULL,
  `bid` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_cpu_data_temp`
--

DROP TABLE IF EXISTS `nms_cpu_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_cpu_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2cach`
--

DROP TABLE IF EXISTS `nms_db2cach`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2cach` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `cat_cache_lookups` varchar(50) DEFAULT NULL,
  `cat_cache_inserts` varchar(50) DEFAULT NULL,
  `cat_cache_overflows` varchar(50) DEFAULT NULL,
  `pkg_cache_lookups` varchar(50) DEFAULT NULL,
  `pkg_cache_inserts` varchar(50) DEFAULT NULL,
  `pkg_cache_num_overflows` varchar(50) DEFAULT NULL,
  `appl_section_lookups` varchar(50) DEFAULT NULL,
  `appl_section_inserts` varchar(50) DEFAULT NULL,
  `mod_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2common`
--

DROP TABLE IF EXISTS `nms_db2common`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2common` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `host_name` varchar(50) DEFAULT '',
  `prod_release` varchar(50) DEFAULT '',
  `total_memory` varchar(50) DEFAULT '',
  `os_name` varchar(50) DEFAULT '',
  `configured_cpu` varchar(50) DEFAULT '',
  `installed_prod` varchar(50) DEFAULT '',
  `total_cpu` varchar(50) DEFAULT '',
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2conn`
--

DROP TABLE IF EXISTS `nms_db2conn`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2conn` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `db_name` varchar(50) DEFAULT '',
  `commitsql` varchar(50) DEFAULT '',
  `db_location` varchar(50) DEFAULT '',
  `appls_cur_cons` varchar(50) DEFAULT '',
  `total_cons` varchar(50) DEFAULT '',
  `db_conn_time` varchar(50) DEFAULT '',
  `sqlm_elm_last_backup` varchar(50) DEFAULT '',
  `db_status` varchar(50) DEFAULT '',
  `failedsql` varchar(50) DEFAULT '',
  `connections_top` varchar(50) DEFAULT '',
  `db_path` varchar(50) DEFAULT '',
  `dbname` varchar(20) DEFAULT '',
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:17
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_db2lock`
--

DROP TABLE IF EXISTS `nms_db2lock`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2lock` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `db_name` varchar(50) DEFAULT '',
  `total_sorts` varchar(50) DEFAULT '',
  `lock_waits` varchar(50) DEFAULT '',
  `lock_escals` varchar(50) DEFAULT '',
  `lock_wait_time` varchar(50) DEFAULT '',
  `rows_selected` varchar(50) DEFAULT '',
  `deadlocks` varchar(50) DEFAULT '',
  `total_sort_time` varchar(50) DEFAULT '',
  `rows_read` varchar(50) DEFAULT NULL,
  `lock_list_in_use` varchar(50) DEFAULT NULL,
  `x_lock_escals` varchar(50) DEFAULT NULL,
  `lock_timeouts` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2log`
--

DROP TABLE IF EXISTS `nms_db2log`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2log` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `maxlogused` varchar(50) DEFAULT '',
  `logused` varchar(50) DEFAULT '',
  `pctused` varchar(50) DEFAULT '',
  `logspacefree` varchar(50) DEFAULT '',
  `maxsecused` varchar(50) DEFAULT '',
  `dbname` varchar(50) DEFAULT '',
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2pool`
--

DROP TABLE IF EXISTS `nms_db2pool`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2pool` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `index_hit_ratio` varchar(50) DEFAULT '',
  `Async_read_pct` varchar(50) DEFAULT '',
  `bp_name` varchar(50) DEFAULT '',
  `Direct_RW_Ratio` varchar(50) DEFAULT '',
  `data_hit_ratio` varchar(50) DEFAULT '',
  `BP_hit_ratio` varchar(50) DEFAULT '',
  `dbname` varchar(50) DEFAULT '',
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2read`
--

DROP TABLE IF EXISTS `nms_db2read`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2read` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `page_reorgs` varchar(50) DEFAULT '',
  `overflow_accesses` varchar(50) DEFAULT '',
  `tbname` varchar(50) DEFAULT '',
  `rows_read` varchar(50) DEFAULT '',
  `tbschema` varchar(50) DEFAULT '',
  `rows_written` varchar(50) DEFAULT '',
  `dbname` varchar(50) DEFAULT '',
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2session`
--

DROP TABLE IF EXISTS `nms_db2session`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2session` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `CLIENT_PLATFORM` varchar(50) DEFAULT '',
  `APPL_STATUS` varchar(50) DEFAULT '',
  `APPL_NAME` varchar(50) DEFAULT '',
  `SNAPSHOT_TIMESTAMP` varchar(50) DEFAULT '',
  `CLIENT_PROTOCOL` varchar(50) DEFAULT '',
  `CLIENT_NNAME` varchar(50) DEFAULT '',
  `dbname` varchar(50) DEFAULT '',
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2spaceinfo`
--

DROP TABLE IF EXISTS `nms_db2spaceinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2spaceinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `usablespac` varchar(50) DEFAULT '',
  `totalspac` varchar(50) DEFAULT '',
  `usableper` varchar(50) DEFAULT '',
  `tablespace_name` varchar(50) DEFAULT '',
  `dbname` varchar(50) DEFAULT '',
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2sysinfo`
--

DROP TABLE IF EXISTS `nms_db2sysinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2sysinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `host_name` varchar(50) DEFAULT '',
  `prod_release` varchar(50) DEFAULT '',
  `total_memory` varchar(50) DEFAULT '',
  `os_name` varchar(50) DEFAULT '',
  `configured_cpu` varchar(50) DEFAULT '',
  `installed_prod` varchar(50) DEFAULT '',
  `total_cpu` varchar(50) DEFAULT '',
  `dbname` varchar(50) DEFAULT '',
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2tablespace`
--

DROP TABLE IF EXISTS `nms_db2tablespace`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2tablespace` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `usablespac` varchar(50) DEFAULT '',
  `totalspac` varchar(50) DEFAULT '',
  `usableper` varchar(50) DEFAULT '',
  `tablespace_name` varchar(50) DEFAULT '',
  `dbname` varchar(20) DEFAULT '',
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2variable`
--

DROP TABLE IF EXISTS `nms_db2variable`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2variable` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `variable_name` varchar(50) DEFAULT NULL,
  `value` varchar(50) DEFAULT NULL,
  `typename` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_db2write`
--

DROP TABLE IF EXISTS `nms_db2write`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_db2write` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `page_reorgs` varchar(50) DEFAULT '',
  `overflow_accesses` varchar(50) DEFAULT '',
  `tbname` varchar(50) DEFAULT '',
  `rows_read` varchar(50) DEFAULT '',
  `tbschema` varchar(50) DEFAULT '',
  `rows_written` varchar(50) DEFAULT '',
  `dbname` varchar(50) DEFAULT '',
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:18
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_device_data_temp`
--

DROP TABLE IF EXISTS `nms_device_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_device_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `deviceindex` varchar(30) DEFAULT NULL,
  `dtype` varchar(30) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_disk_data_temp`
--

DROP TABLE IF EXISTS `nms_disk_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_disk_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(300) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_diskio_data_temp`
--

DROP TABLE IF EXISTS `nms_diskio_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_diskio_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_diskperf_data_temp`
--

DROP TABLE IF EXISTS `nms_diskperf_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_diskperf_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_dominocache_realtime`
--

DROP TABLE IF EXISTS `nms_dominocache_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dominocache_realtime` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(20) DEFAULT NULL,
  `CACHECOMMANDCOUNT` varchar(50) DEFAULT NULL,
  `CACHECOMMANDDISRATE` varchar(30) DEFAULT NULL,
  `CACHECOMMANDHITRATE` varchar(30) DEFAULT NULL,
  `CACHECOMMANDSIZE` varchar(50) DEFAULT NULL,
  `CACHEDBHITRATE` varchar(30) DEFAULT NULL,
  `CACHESESSIONCOUNT` varchar(30) DEFAULT NULL,
  `CACHESESSIONDISRATE` varchar(30) DEFAULT NULL,
  `CACHESESSIONHITRATE` varchar(30) DEFAULT NULL,
  `CACHESESSIONSIZE` varchar(50) DEFAULT NULL,
  `CACHEUSERCOUNT` varchar(50) DEFAULT NULL,
  `CACHEUSERDISRATE` varchar(30) DEFAULT NULL,
  `CACHEUSERHITRATE` varchar(30) DEFAULT NULL,
  `CACHEUSRSIZE` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_dominodb_realtime`
--

DROP TABLE IF EXISTS `nms_dominodb_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dominodb_realtime` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(20) DEFAULT NULL,
  `DBBUFFPOOLMAX` varchar(50) DEFAULT NULL,
  `DBBUFFPOOLPEAK` varchar(50) DEFAULT NULL,
  `DBBUFFPOOLREADS` varchar(50) DEFAULT NULL,
  `DBBUFFPOOLWRITES` varchar(50) DEFAULT NULL,
  `DBBUFFPOOLREADHIT` varchar(20) DEFAULT NULL,
  `DBCACHEENTRY` varchar(50) DEFAULT NULL,
  `DBCACHEWATERMARK` varchar(100) DEFAULT NULL,
  `DBCACHEHIT` varchar(30) DEFAULT NULL,
  `DBCACHEDBOPEN` varchar(30) DEFAULT NULL,
  `DBNIFPOOLPEAK` varchar(30) DEFAULT NULL,
  `DBNIFPOOLUSE` varchar(30) DEFAULT NULL,
  `DBNSFPOOLPEAK` varchar(30) DEFAULT NULL,
  `DBNSFPOOLUSE` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_dominodisk_realtime`
--

DROP TABLE IF EXISTS `nms_dominodisk_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dominodisk_realtime` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(20) DEFAULT NULL,
  `DISKNAME` varchar(100) DEFAULT NULL,
  `DISKSIZE` varchar(50) DEFAULT NULL,
  `DISKFREE` varchar(50) DEFAULT NULL,
  `DISKUSEDPCTUTIL` varchar(50) DEFAULT NULL,
  `DISKTYPE` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_dominohttp_realtime`
--

DROP TABLE IF EXISTS `nms_dominohttp_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dominohttp_realtime` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(20) DEFAULT NULL,
  `HTTPACCEPT` varchar(30) DEFAULT NULL,
  `HTTPREFUSED` varchar(30) DEFAULT NULL,
  `HTTPCURRENTCON` varchar(30) DEFAULT NULL,
  `HTTPMAXCON` varchar(30) DEFAULT NULL,
  `HTTPPEAKCON` varchar(30) DEFAULT NULL,
  `HTTPWORKERREQUEST` varchar(30) DEFAULT NULL,
  `HTTPWORKERREQUESTTIME` varchar(100) DEFAULT NULL,
  `HTTPWORKERBYTESREAD` varchar(30) DEFAULT NULL,
  `HTTPWORKERBYTESWRITTEN` varchar(30) DEFAULT NULL,
  `HTTPWORKERREQUESTPROCESS` varchar(100) DEFAULT NULL,
  `HTTPWORKERTOTALREQUEST` varchar(100) DEFAULT NULL,
  `HTTPERRORURL` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_dominoldap_realtime`
--

DROP TABLE IF EXISTS `nms_dominoldap_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dominoldap_realtime` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(20) DEFAULT NULL,
  `LDAPRUNNING` varchar(30) DEFAULT NULL,
  `LDAPINBOUNDQUE` varchar(30) DEFAULT NULL,
  `LDAPINBOUNDACTIVE` varchar(30) DEFAULT NULL,
  `LDAPINBOUNDACTIVESSL` varchar(30) DEFAULT NULL,
  `LDAPINBOUNDBYTESRESEIVED` varchar(100) DEFAULT NULL,
  `LDAPINBOUNDBYTESSENT` varchar(100) DEFAULT NULL,
  `LDAPINBOUNDPEAK` varchar(30) DEFAULT NULL,
  `LDAPINBOUNDPEAKSSL` varchar(30) DEFAULT NULL,
  `LDAPINBOUNDTOTAL` varchar(30) DEFAULT NULL,
  `LDAPINBOUNDTOTALSSL` varchar(30) DEFAULT NULL,
  `LDAPBADHANDSHAKE` varchar(30) DEFAULT NULL,
  `LDAPTHREADSBUSY` varchar(30) DEFAULT NULL,
  `LDAPTHREADSLDLE` varchar(30) DEFAULT NULL,
  `LDAPTHREADSINPOOL` varchar(30) DEFAULT NULL,
  `LDAPTHREADSPEAK` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_dominomail_realtime`
--

DROP TABLE IF EXISTS `nms_dominomail_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dominomail_realtime` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(20) DEFAULT NULL,
  `MAILDEAD` varchar(20) DEFAULT NULL,
  `MAILWAITING` varchar(20) DEFAULT NULL,
  `MAILWAITINGRECIPIENTS` varchar(20) DEFAULT NULL,
  `MAILDELIVERRATE` varchar(100) DEFAULT NULL,
  `MAILTRANSFERRATE` varchar(100) DEFAULT NULL,
  `MAILDELIVERTHREADSMAX` varchar(20) DEFAULT NULL,
  `MAILDELIVERTHREADSTOTAL` varchar(20) DEFAULT NULL,
  `MAILTRANSFERTHREADSMAX` varchar(20) DEFAULT NULL,
  `MAILTRANSFERTHREADSTOTAL` varchar(20) DEFAULT NULL,
  `MAILAVGSIZE` varchar(20) DEFAULT NULL,
  `MAILAVGTIME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:18
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_dominomem_realtime`
--

DROP TABLE IF EXISTS `nms_dominomem_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dominomem_realtime` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(20) DEFAULT NULL,
  `MEMALLOCATE` varchar(50) DEFAULT NULL,
  `MEMALLOCATEPROCESS` varchar(50) DEFAULT NULL,
  `MEMALLOCATESHARE` varchar(50) DEFAULT NULL,
  `MEMPHYSICAL` varchar(50) DEFAULT NULL,
  `MEMFREE` varchar(50) DEFAULT NULL,
  `PLATFORMMEMPHYPCTUTIL` varchar(50) DEFAULT NULL,
  `PLATFORMMEMPHYSICAL` varchar(50) DEFAULT NULL,
  `MEMPCTUTIL` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_dominoping_realtime`
--

DROP TABLE IF EXISTS `nms_dominoping_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dominoping_realtime` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `RESTYPE` varchar(20) DEFAULT NULL,
  `CATEGORY` varchar(50) DEFAULT NULL,
  `ENTITY` varchar(100) DEFAULT NULL,
  `SUBENTITY` varchar(60) DEFAULT NULL,
  `THEVALUE` varchar(255) DEFAULT NULL,
  `COLLECTTIME` varchar(200) DEFAULT 'CURRENT_TIMESTAMP',
  `UNIT` varchar(30) DEFAULT NULL,
  `COUNT` bigint(20) DEFAULT NULL,
  `BAK` varchar(100) DEFAULT NULL,
  `CHNAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_dominoserver_realtime`
--

DROP TABLE IF EXISTS `nms_dominoserver_realtime`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_dominoserver_realtime` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(20) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `TITLE` varchar(100) DEFAULT NULL,
  `OS` varchar(100) DEFAULT NULL,
  `ARCHITECTURE` varchar(128) DEFAULT NULL,
  `STARTTIME` varchar(100) DEFAULT NULL,
  `CPUTYPE` varchar(50) DEFAULT NULL,
  `CPUCOUNT` varchar(50) DEFAULT NULL,
  `PORTNUMBER` varchar(100) DEFAULT NULL,
  `CPUPCTUTIL` varchar(30) DEFAULT NULL,
  `IMAPSTATUS` varchar(50) DEFAULT NULL,
  `LDAPSTATUS` varchar(50) DEFAULT NULL,
  `POP3STATUS` varchar(30) DEFAULT NULL,
  `SMTPSTATUS` varchar(30) DEFAULT NULL,
  `AVAILABILITYINDEX` varchar(50) DEFAULT NULL,
  `SESSIONSDROPPED` varchar(50) DEFAULT NULL,
  `TASKS` varchar(50) DEFAULT NULL,
  `TRANSPERMINUTE` varchar(50) DEFAULT NULL,
  `REQUESTSPER1HOUR` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_envir_data_temp`
--

DROP TABLE IF EXISTS `nms_envir_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_envir_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(100) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_errptlog`
--

DROP TABLE IF EXISTS `nms_errptlog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_errptlog` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LABELS` varchar(100) DEFAULT NULL,
  `IDENTIFIER` varchar(50) DEFAULT NULL,
  `COLLETTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `SEQNUMBER` int(10) DEFAULT NULL,
  `NODEID` varchar(20) DEFAULT NULL,
  `MACHINEID` varchar(20) DEFAULT NULL,
  `ERRPTCLASS` varchar(5) DEFAULT NULL,
  `ERRPTTYPE` varchar(5) DEFAULT NULL,
  `RESOURCENAME` varchar(50) DEFAULT NULL,
  `RESOURCECLASS` varchar(50) DEFAULT NULL,
  `RESOURCETYPE` varchar(50) DEFAULT NULL,
  `LOCATIONS` varchar(100) DEFAULT NULL,
  `VPD` text,
  `DESCRIPTIONS` text,
  `HOSTID` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_fdb_data_temp`
--

DROP TABLE IF EXISTS `nms_fdb_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_fdb_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `ifindex` varchar(30) DEFAULT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `mac` varchar(30) DEFAULT NULL,
  `ifband` varchar(2) DEFAULT NULL,
  `ifsms` varchar(2) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_fibrecapability_data_temp`
--

DROP TABLE IF EXISTS `nms_fibrecapability_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_fibrecapability_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_fibreconfig_data_temp`
--

DROP TABLE IF EXISTS `nms_fibreconfig_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_fibreconfig_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_flash_data_temp`
--

DROP TABLE IF EXISTS `nms_flash_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_flash_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_informixabout`
--

DROP TABLE IF EXISTS `nms_informixabout`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixabout` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `value` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:19
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_informixbaractlog`
--

DROP TABLE IF EXISTS `nms_informixbaractlog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixbaractlog` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `entity` varchar(50) DEFAULT NULL,
  `subentity` varchar(50) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_informixconfig`
--

DROP TABLE IF EXISTS `nms_informixconfig`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixconfig` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `cf_default` varchar(200) DEFAULT NULL,
  `cf_original` varchar(200) DEFAULT NULL,
  `cf_name` varchar(200) DEFAULT NULL,
  `cf_effective` varchar(200) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_informixdatabase`
--

DROP TABLE IF EXISTS `nms_informixdatabase`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixdatabase` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `bufflog` varchar(50) DEFAULT NULL,
  `createtime` varchar(50) DEFAULT NULL,
  `log` varchar(50) DEFAULT NULL,
  `dbserver` varchar(50) DEFAULT NULL,
  `gls` varchar(50) DEFAULT NULL,
  `createuser` varchar(50) DEFAULT NULL,
  `ansi` varchar(50) DEFAULT NULL,
  `dbname` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_informixio`
--

DROP TABLE IF EXISTS `nms_informixio`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixio` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `pagesread` varchar(50) DEFAULT NULL,
  `readsstr` varchar(50) DEFAULT NULL,
  `writes` varchar(50) DEFAULT NULL,
  `mwrites` varchar(50) DEFAULT NULL,
  `chunknum` varchar(50) DEFAULT NULL,
  `mreads` varchar(50) DEFAULT NULL,
  `pageswritten` varchar(50) DEFAULT NULL,
  `mpagesread` varchar(50) DEFAULT NULL,
  `mpageswritten` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_informixlock`
--

DROP TABLE IF EXISTS `nms_informixlock`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixlock` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `hostname` varchar(50) DEFAULT NULL,
  `dbsname` varchar(50) DEFAULT NULL,
  `tabname` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_informixlog`
--

DROP TABLE IF EXISTS `nms_informixlog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixlog` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `is_backed_up` varchar(50) DEFAULT NULL,
  `is_current` varchar(50) DEFAULT NULL,
  `sizes` varchar(50) DEFAULT NULL,
  `used` varchar(50) DEFAULT NULL,
  `is_temp` varchar(50) DEFAULT NULL,
  `uniqid` varchar(50) DEFAULT NULL,
  `is_archived` varchar(50) DEFAULT NULL,
  `is_used` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_informixoffset`
--

DROP TABLE IF EXISTS `nms_informixoffset`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixoffset` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `dbnodeid` varchar(50) DEFAULT NULL,
  `lastoffset` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_informixother`
--

DROP TABLE IF EXISTS `nms_informixother`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixother` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `entity` varchar(50) DEFAULT NULL,
  `subentity` varchar(50) DEFAULT NULL,
  `thevalue` varchar(100) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_informixsession`
--

DROP TABLE IF EXISTS `nms_informixsession`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixsession` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `bufwrites` varchar(50) DEFAULT NULL,
  `pagwrites` varchar(50) DEFAULT NULL,
  `pagreads` varchar(50) DEFAULT NULL,
  `locksheld` varchar(50) DEFAULT NULL,
  `bufreads` varchar(50) DEFAULT NULL,
  `accesses` varchar(50) DEFAULT NULL,
  `connected` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `lktouts` varchar(50) DEFAULT NULL,
  `lockreqs` varchar(50) DEFAULT NULL,
  `hostname` varchar(50) DEFAULT NULL,
  `lockwts` varchar(50) DEFAULT NULL,
  `deadlks` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_informixspace`
--

DROP TABLE IF EXISTS `nms_informixspace`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixspace` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `owner` varchar(50) DEFAULT NULL,
  `pages_free` varchar(50) DEFAULT NULL,
  `dbspace` varchar(50) DEFAULT NULL,
  `pages_size` varchar(50) DEFAULT NULL,
  `pages_used` varchar(50) DEFAULT NULL,
  `file_name` varchar(200) DEFAULT NULL,
  `fname` varchar(200) DEFAULT NULL,
  `percent_free` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:20
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_informixstatus`
--

DROP TABLE IF EXISTS `nms_informixstatus`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_informixstatus` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_interface_data_temp`
--

DROP TABLE IF EXISTS `nms_interface_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_interface_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_lights_data_temp`
--

DROP TABLE IF EXISTS `nms_lights_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_lights_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_memory_data_temp`
--

DROP TABLE IF EXISTS `nms_memory_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_memory_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_orabanner`
--

DROP TABLE IF EXISTS `nms_orabanner`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_orabanner` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `detail` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_orabaseinfo`
--

DROP TABLE IF EXISTS `nms_orabaseinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_orabaseinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `entity` varchar(50) DEFAULT NULL,
  `subentity` varchar(50) DEFAULT NULL,
  `thevalue` tinytext,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oracle_sids`
--

DROP TABLE IF EXISTS `nms_oracle_sids`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oracle_sids` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dbid` int(11) DEFAULT NULL,
  `sid` varchar(64) CHARACTER SET gb2312 DEFAULT NULL,
  `users` varchar(64) CHARACTER SET gb2312 DEFAULT NULL,
  `password` varchar(64) CHARACTER SET gb2312 DEFAULT NULL,
  `gzerid` varchar(128) CHARACTER SET gb2312 DEFAULT NULL,
  `collecttype` int(2) DEFAULT NULL,
  `alias` varchar(32) CHARACTER SET gb2312 DEFAULT NULL,
  `managed` int(2) DEFAULT NULL,
  `bid` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oracontrfile`
--

DROP TABLE IF EXISTS `nms_oracontrfile`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oracontrfile` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `is_recovery_dest_file` varchar(50) DEFAULT NULL,
  `block_size` varchar(50) DEFAULT NULL,
  `file_size_blks` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oracursors`
--

DROP TABLE IF EXISTS `nms_oracursors`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oracursors` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `opencur` varchar(50) DEFAULT NULL,
  `curconnect` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oradbio`
--

DROP TABLE IF EXISTS `nms_oradbio`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oradbio` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `filename` varchar(200) DEFAULT NULL,
  `pyr` varchar(50) DEFAULT NULL,
  `pbr` varchar(50) DEFAULT NULL,
  `pyw` varchar(50) DEFAULT NULL,
  `pbw` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:21
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_oraextent`
--

DROP TABLE IF EXISTS `nms_oraextent`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oraextent` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `tablespace_name` varchar(200) DEFAULT NULL,
  `extents` varchar(200) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oraisarchive`
--

DROP TABLE IF EXISTS `nms_oraisarchive`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oraisarchive` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `created` varchar(50) DEFAULT NULL,
  `log_mode` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_orajob`
--

DROP TABLE IF EXISTS `nms_orajob`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_orajob` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `job` varchar(500) DEFAULT NULL,
  `loguser` varchar(50) DEFAULT NULL,
  `lastdate` timestamp NULL DEFAULT NULL,
  `failures` int(11) DEFAULT NULL,
  `serverip` varchar(100) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_orakeepobj`
--

DROP TABLE IF EXISTS `nms_orakeepobj`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_orakeepobj` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `owner` varchar(50) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `db_link` varchar(50) DEFAULT NULL,
  `namespace` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `sharable_mem` varchar(50) DEFAULT NULL,
  `loads` varchar(50) DEFAULT NULL,
  `executions` varchar(50) DEFAULT NULL,
  `locks` varchar(50) DEFAULT NULL,
  `pins` varchar(50) DEFAULT NULL,
  `kept` varchar(50) DEFAULT NULL,
  `child_latch` varchar(50) DEFAULT NULL,
  `invalidations` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oralock`
--

DROP TABLE IF EXISTS `nms_oralock`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oralock` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `username` varchar(15) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `machine` varchar(50) DEFAULT NULL,
  `sessiontype` varchar(50) DEFAULT NULL,
  `logontime` varchar(200) DEFAULT NULL,
  `program` varchar(50) DEFAULT NULL,
  `locktype` varchar(50) DEFAULT NULL,
  `lmode` varchar(50) DEFAULT NULL,
  `requeststr` varchar(200) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oralockinfo`
--

DROP TABLE IF EXISTS `nms_oralockinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oralockinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `entity` varchar(50) DEFAULT NULL,
  `subentity` varchar(50) DEFAULT NULL,
  `thevalue` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oralogfile`
--

DROP TABLE IF EXISTS `nms_oralogfile`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oralogfile` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `groupstr` varchar(200) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `member` varchar(200) DEFAULT NULL,
  `is_recovery_dest_file` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oramemperfvalue`
--

DROP TABLE IF EXISTS `nms_oramemperfvalue`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oramemperfvalue` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `pctmemorysorts` varchar(50) DEFAULT NULL,
  `pctbufgets` varchar(50) DEFAULT NULL,
  `dictionarycache` varchar(50) DEFAULT NULL,
  `buffercache` varchar(50) DEFAULT NULL,
  `librarycache` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oramemvalue`
--

DROP TABLE IF EXISTS `nms_oramemvalue`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oramemvalue` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `agg_pga_auto_target` varchar(50) DEFAULT NULL,
  `tpga_used_manu_workareas` varchar(50) DEFAULT NULL,
  `total_pga_inuse` varchar(50) DEFAULT NULL,
  `maximum_pga_allocated` varchar(50) DEFAULT NULL,
  `cache_hit_percentage` varchar(50) DEFAULT NULL,
  `recycle_buffer_cache` varchar(50) DEFAULT NULL,
  `keep_buffer_cache` varchar(50) DEFAULT NULL,
  `process_count` varchar(50) DEFAULT NULL,
  `tpga_used_auto_workareas` varchar(50) DEFAULT NULL,
  `asm_buffer_cache` varchar(50) DEFAULT NULL,
  `over_allocation_count` varchar(50) DEFAULT NULL,
  `bytes_processed` varchar(50) DEFAULT NULL,
  `java_pool` varchar(50) DEFAULT NULL,
  `maxpga_used_manu_workareas` varchar(50) DEFAULT NULL,
  `streams_pool` varchar(50) DEFAULT NULL,
  `default_2k_buffer_cache` varchar(50) DEFAULT NULL,
  `max_processes_count` varchar(50) DEFAULT NULL,
  `total_pga_allocated` varchar(50) DEFAULT NULL,
  `default_4k_buffer_cache` varchar(50) DEFAULT NULL,
  `shared_pool` varchar(50) DEFAULT NULL,
  `default_32k_buffer_cache` varchar(50) DEFAULT NULL,
  `default_buffer_cache` varchar(50) DEFAULT NULL,
  `large_pool` varchar(50) DEFAULT NULL,
  `agg_pga_target_parameter` varchar(50) DEFAULT NULL,
  `default_16k_buffer_cache` varchar(50) DEFAULT NULL,
  `global_memory_bound` varchar(50) DEFAULT NULL,
  `default_8k_buffer_cache` varchar(50) DEFAULT NULL,
  `extra_bytes_read_written` varchar(50) DEFAULT NULL,
  `pga_mem_freed_back_os` varchar(50) DEFAULT NULL,
  `tot_free_pga_memory` varchar(50) DEFAULT NULL,
  `recom_count_total` varchar(50) DEFAULT NULL,
  `maxpga_used_auto_workareas` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_orarollback`
--

DROP TABLE IF EXISTS `nms_orarollback`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_orarollback` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `rollback` varchar(50) DEFAULT NULL,
  `wraps` varchar(50) DEFAULT NULL,
  `shrink` varchar(50) DEFAULT NULL,
  `ashrink` varchar(50) DEFAULT NULL,
  `extend` varchar(200) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:22
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_orasessiondata`
--

DROP TABLE IF EXISTS `nms_orasessiondata`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_orasessiondata` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(15) DEFAULT NULL,
  `dbname` varchar(50) DEFAULT NULL,
  `machine` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `program` varchar(200) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `sessiontype` varchar(10) DEFAULT NULL,
  `command` varchar(200) DEFAULT NULL,
  `logontime` timestamp NULL DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oraspaces`
--

DROP TABLE IF EXISTS `nms_oraspaces`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oraspaces` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `tablespace` varchar(15) DEFAULT NULL,
  `free_mb` varchar(50) DEFAULT NULL,
  `size_mb` varchar(50) DEFAULT NULL,
  `percent_free` varchar(50) DEFAULT NULL,
  `file_name` varchar(200) DEFAULT NULL,
  `chunks` varchar(20) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_orastatus`
--

DROP TABLE IF EXISTS `nms_orastatus`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_orastatus` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `lstrnstatu` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_orasys`
--

DROP TABLE IF EXISTS `nms_orasys`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_orasys` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `INSTANCE_NAME` varchar(15) DEFAULT NULL,
  `HOST_NAME` varchar(50) DEFAULT NULL,
  `DBNAME` varchar(50) DEFAULT NULL,
  `VERSION` varchar(50) DEFAULT NULL,
  `STARTUP_TIME` varchar(200) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `ARCHIVER` varchar(20) DEFAULT NULL,
  `BANNER` varchar(20) DEFAULT NULL,
  `java_pool` varchar(20) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oratables`
--

DROP TABLE IF EXISTS `nms_oratables`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oratables` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `segment_name` varchar(50) DEFAULT NULL,
  `spaces` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oratopsql`
--

DROP TABLE IF EXISTS `nms_oratopsql`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oratopsql` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `sql_text` longtext,
  `pct_bufgets` varchar(10) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oratopsql_readwrite`
--

DROP TABLE IF EXISTS `nms_oratopsql_readwrite`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oratopsql_readwrite` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `sqltext` longtext,
  `totaldisk` varchar(10) DEFAULT NULL,
  `totalexec` varchar(10) DEFAULT NULL,
  `diskreads` varchar(10) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_oratopsql_sort`
--

DROP TABLE IF EXISTS `nms_oratopsql_sort`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_oratopsql_sort` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `sqltext` longtext,
  `sorts` varchar(10) DEFAULT NULL,
  `executions` varchar(10) DEFAULT NULL,
  `sortsexec` varchar(10) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_orauserinfo`
--

DROP TABLE IF EXISTS `nms_orauserinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_orauserinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `parsing_user_id` varchar(50) DEFAULT NULL,
  `cpu_time` varchar(50) DEFAULT NULL,
  `sorts` varchar(50) DEFAULT NULL,
  `buffer_gets` varchar(50) DEFAULT NULL,
  `runtime_mem` varchar(50) DEFAULT NULL,
  `version_count` varchar(50) DEFAULT NULL,
  `disk_reads` varchar(50) DEFAULT NULL,
  `users` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `account_status` varchar(50) DEFAULT NULL,
  `label` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_orawait`
--

DROP TABLE IF EXISTS `nms_orawait`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_orawait` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `event` varchar(200) DEFAULT NULL,
  `prev` varchar(50) DEFAULT NULL,
  `curr` varchar(50) DEFAULT NULL,
  `tot` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:23
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_other_data_temp`
--

DROP TABLE IF EXISTS `nms_other_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_other_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_ping_data_temp`
--

DROP TABLE IF EXISTS `nms_ping_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ping_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_process_data_temp`
--

DROP TABLE IF EXISTS `nms_process_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_process_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_route_data_temp`
--

DROP TABLE IF EXISTS `nms_route_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_route_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `ifindex` varchar(30) DEFAULT NULL,
  `nexthop` varchar(30) DEFAULT NULL,
  `proto` varchar(30) DEFAULT NULL,
  `rtype` varchar(300) DEFAULT NULL,
  `mask` varchar(50) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `physaddress` varchar(50) DEFAULT NULL,
  `dest` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sercice_data_temp`
--

DROP TABLE IF EXISTS `nms_sercice_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sercice_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `instate` varchar(30) DEFAULT NULL,
  `opstate` varchar(30) DEFAULT NULL,
  `paused` varchar(50) DEFAULT NULL,
  `uninst` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `startMode` varchar(30) DEFAULT NULL,
  `pathName` varchar(200) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `serviceType` varchar(30) DEFAULT NULL,
  `pid` varchar(30) DEFAULT NULL,
  `groupstr` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_software_data_temp`
--

DROP TABLE IF EXISTS `nms_software_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_software_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `insdate` varchar(30) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `stype` varchar(30) DEFAULT NULL,
  `swid` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlservercaches`
--

DROP TABLE IF EXISTS `nms_sqlservercaches`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlservercaches` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `cacheHitRatio` varchar(50) DEFAULT NULL,
  `cacheHitRatioBase` varchar(50) DEFAULT NULL,
  `cacheCount` varchar(50) DEFAULT NULL,
  `cachePages` varchar(50) DEFAULT NULL,
  `cacheUsed` varchar(50) DEFAULT NULL,
  `cacheUsedRate` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlserverconns`
--

DROP TABLE IF EXISTS `nms_sqlserverconns`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlserverconns` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `connections` varchar(50) DEFAULT NULL,
  `totalLogins` varchar(50) DEFAULT NULL,
  `totalLoginsRate` varchar(50) DEFAULT NULL,
  `totalLogouts` varchar(50) DEFAULT NULL,
  `totalLogoutsRate` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlserverdbvalue`
--

DROP TABLE IF EXISTS `nms_sqlserverdbvalue`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlserverdbvalue` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `usedperc` varchar(50) DEFAULT NULL,
  `usedsize` varchar(50) DEFAULT NULL,
  `sizes` varchar(50) DEFAULT NULL,
  `logname` varchar(50) DEFAULT NULL,
  `dbname` varchar(50) DEFAULT NULL,
  `instance_name` varchar(50) DEFAULT NULL,
  `label` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlserverinfo_v`
--

DROP TABLE IF EXISTS `nms_sqlserverinfo_v`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlserverinfo_v` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `spid` varchar(50) DEFAULT NULL,
  `waittime` varchar(50) DEFAULT NULL,
  `lastwaittype` varchar(50) DEFAULT NULL,
  `waitresource` varchar(50) DEFAULT NULL,
  `dbname` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `cpu` varchar(50) DEFAULT NULL,
  `physical_io` varchar(50) DEFAULT NULL,
  `memusage` varchar(50) DEFAULT NULL,
  `login_time` varchar(50) DEFAULT NULL,
  `last_batch` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `hostname` varchar(50) DEFAULT NULL,
  `program_name` varchar(50) DEFAULT NULL,
  `hostprocess` varchar(50) DEFAULT NULL,
  `cmd` varchar(50) DEFAULT NULL,
  `nt_domain` varchar(50) DEFAULT NULL,
  `nt_username` varchar(50) DEFAULT NULL,
  `net_library` varchar(50) DEFAULT NULL,
  `loginame` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:23
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_sqlserverlockinfo_v`
--

DROP TABLE IF EXISTS `nms_sqlserverlockinfo_v`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlserverlockinfo_v` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `rsc_text` varchar(50) DEFAULT NULL,
  `rsc_dbid` varchar(50) DEFAULT NULL,
  `dbname` varchar(50) DEFAULT NULL,
  `rsc_indid` varchar(50) DEFAULT NULL,
  `rsc_objid` varchar(50) DEFAULT NULL,
  `rsc_type` varchar(50) DEFAULT NULL,
  `rsc_flag` varchar(50) DEFAULT NULL,
  `req_mode` varchar(50) DEFAULT NULL,
  `req_status` varchar(50) DEFAULT NULL,
  `req_refcnt` varchar(50) DEFAULT NULL,
  `req_cryrefcnt` varchar(50) DEFAULT NULL,
  `req_lifetime` varchar(50) DEFAULT NULL,
  `req_spid` varchar(50) DEFAULT NULL,
  `req_ecid` varchar(50) DEFAULT NULL,
  `req_ownertype` varchar(50) DEFAULT NULL,
  `req_transactionID` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlserverlocks`
--

DROP TABLE IF EXISTS `nms_sqlserverlocks`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlserverlocks` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `lockRequests` varchar(50) DEFAULT NULL,
  `lockRequestsRate` varchar(50) DEFAULT NULL,
  `lockWaits` varchar(50) DEFAULT NULL,
  `lockWaitsRate` varchar(50) DEFAULT NULL,
  `lockTimeouts` varchar(50) DEFAULT NULL,
  `lockTimeoutsRate` varchar(50) DEFAULT NULL,
  `deadLocks` varchar(50) DEFAULT NULL,
  `deadLocksRate` varchar(50) DEFAULT NULL,
  `avgWaitTime` varchar(50) DEFAULT NULL,
  `avgWaitTimeBase` varchar(50) DEFAULT NULL,
  `latchWaits` varchar(50) DEFAULT NULL,
  `latchWaitsRate` varchar(50) DEFAULT NULL,
  `avgLatchWait` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlservermems`
--

DROP TABLE IF EXISTS `nms_sqlservermems`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlservermems` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `totalMemory` varchar(50) DEFAULT NULL,
  `sqlMem` varchar(50) DEFAULT NULL,
  `optMemory` varchar(50) DEFAULT NULL,
  `memGrantPending` varchar(50) DEFAULT NULL,
  `memGrantSuccess` varchar(50) DEFAULT NULL,
  `lockMem` varchar(50) DEFAULT NULL,
  `conMemory` varchar(50) DEFAULT NULL,
  `grantedWorkspaceMem` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlserverpages`
--

DROP TABLE IF EXISTS `nms_sqlserverpages`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlserverpages` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `bufferCacheHitRatio` varchar(50) DEFAULT NULL,
  `planCacheHitRatio` varchar(50) DEFAULT NULL,
  `cursorManagerByTypeHitRatio` varchar(50) DEFAULT NULL,
  `catalogMetadataHitRatio` varchar(50) DEFAULT NULL,
  `dbOfflineErrors` varchar(50) DEFAULT NULL,
  `killConnectionErrors` varchar(50) DEFAULT NULL,
  `userErrors` varchar(50) DEFAULT NULL,
  `infoErrors` varchar(50) DEFAULT NULL,
  `sqlServerErrors_total` varchar(50) DEFAULT NULL,
  `cachedCursorCounts` varchar(50) DEFAULT NULL,
  `cursorCacheUseCounts` varchar(50) DEFAULT NULL,
  `cursorRequests_total` varchar(50) DEFAULT NULL,
  `activeCursors` varchar(50) DEFAULT NULL,
  `cursorMemoryUsage` varchar(50) DEFAULT NULL,
  `cursorWorktableUsage` varchar(50) DEFAULT NULL,
  `activeOfCursorPlans` varchar(50) DEFAULT NULL,
  `dbPages` varchar(50) DEFAULT NULL,
  `totalPageLookups` varchar(50) DEFAULT NULL,
  `totalPageLookupsRate` varchar(50) DEFAULT NULL,
  `totalPageReads` varchar(50) DEFAULT NULL,
  `totalPageReadsRate` varchar(50) DEFAULT NULL,
  `totalPageWrites` varchar(50) DEFAULT NULL,
  `totalPageWritesRate` varchar(50) DEFAULT NULL,
  `totalPages` varchar(50) DEFAULT NULL,
  `freePages` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlserverscans`
--

DROP TABLE IF EXISTS `nms_sqlserverscans`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlserverscans` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `fullScans` varchar(50) DEFAULT NULL,
  `fullScansRate` varchar(50) DEFAULT NULL,
  `rangeScans` varchar(50) DEFAULT NULL,
  `rangeScansRate` varchar(50) DEFAULT NULL,
  `probeScans` varchar(50) DEFAULT NULL,
  `probeScansRate` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlserversqls`
--

DROP TABLE IF EXISTS `nms_sqlserversqls`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlserversqls` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `batchRequests` varchar(50) DEFAULT NULL,
  `batchRequestsRate` varchar(50) DEFAULT NULL,
  `sqlCompilations` varchar(50) DEFAULT NULL,
  `sqlCompilationsRate` varchar(50) DEFAULT NULL,
  `sqlRecompilation` varchar(50) DEFAULT NULL,
  `sqlRecompilationRate` varchar(50) DEFAULT NULL,
  `autoParams` varchar(50) DEFAULT NULL,
  `autoParamsRate` varchar(50) DEFAULT NULL,
  `failedAutoParams` varchar(50) DEFAULT NULL,
  `failedAutoParamsRate` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlserverstatisticshash`
--

DROP TABLE IF EXISTS `nms_sqlserverstatisticshash`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlserverstatisticshash` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `pj_lockWaits` varchar(50) DEFAULT NULL,
  `pj_memGrantQueWaits` varchar(50) DEFAULT NULL,
  `pj_thdSafeMemObjWaits` varchar(50) DEFAULT NULL,
  `pj_logWriteWaits` varchar(50) DEFAULT NULL,
  `pj_logBufferWaits` varchar(50) DEFAULT NULL,
  `pj_networkIOWaits` varchar(50) DEFAULT NULL,
  `pj_pageIOLatchWaits` varchar(50) DEFAULT NULL,
  `pj_pageLatchWaits` varchar(50) DEFAULT NULL,
  `pj_nonPageLatchWaits` varchar(50) DEFAULT NULL,
  `pj_waitForTheWorker` varchar(50) DEFAULT NULL,
  `pj_workspaceSynWaits` varchar(50) DEFAULT NULL,
  `pj_traOwnershipWaits` varchar(50) DEFAULT NULL,
  `jx_lockWaits` varchar(50) DEFAULT NULL,
  `jx_memGrantQueWaits` varchar(50) DEFAULT NULL,
  `jx_thdSafeMemObjWaits` varchar(50) DEFAULT NULL,
  `jx_logWriteWaits` varchar(50) DEFAULT NULL,
  `jx_logBufferWaits` varchar(50) DEFAULT NULL,
  `jx_networkIOWaits` varchar(50) DEFAULT NULL,
  `jx_pageIOLatchWaits` varchar(50) DEFAULT NULL,
  `jx_pageLatchWaits` varchar(50) DEFAULT NULL,
  `jx_nonPageLatchWaits` varchar(50) DEFAULT NULL,
  `jx_waitForTheWorker` varchar(50) DEFAULT NULL,
  `jx_workspaceSynWaits` varchar(50) DEFAULT NULL,
  `jx_traOwnershipWaits` varchar(50) DEFAULT NULL,
  `qd_lockWaits` varchar(50) DEFAULT NULL,
  `qd_memGrantQueWaits` varchar(50) DEFAULT NULL,
  `qd_thdSafeMemObjWaits` varchar(50) DEFAULT NULL,
  `qd_logWriteWaits` varchar(50) DEFAULT NULL,
  `qd_logBufferWaits` varchar(50) DEFAULT NULL,
  `qd_networkIOWaits` varchar(50) DEFAULT NULL,
  `qd_pageIOLatchWaits` varchar(50) DEFAULT NULL,
  `qd_pageLatchWaits` varchar(50) DEFAULT NULL,
  `qd_nonPageLatchWaits` varchar(50) DEFAULT NULL,
  `qd_waitForTheWorker` varchar(50) DEFAULT NULL,
  `qd_workspaceSynWaits` varchar(50) DEFAULT NULL,
  `qd_traOwnershipWaits` varchar(50) DEFAULT NULL,
  `lj_lockWaits` varchar(50) DEFAULT NULL,
  `lj_memGrantQueWaits` varchar(50) DEFAULT NULL,
  `lj_thdSafeMemObjWaits` varchar(50) DEFAULT NULL,
  `lj_logWriteWaits` varchar(50) DEFAULT NULL,
  `lj_logBufferWaits` varchar(50) DEFAULT NULL,
  `lj_networkIOWaits` varchar(50) DEFAULT NULL,
  `lj_pageIOLatchWaits` varchar(50) DEFAULT NULL,
  `lj_pageLatchWaits` varchar(50) DEFAULT NULL,
  `lj_nonPageLatchWaits` varchar(50) DEFAULT NULL,
  `lj_waitForTheWorker` varchar(50) DEFAULT NULL,
  `lj_workspaceSynWaits` varchar(50) DEFAULT NULL,
  `lj_traOwnershipWaits` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlserverstatus`
--

DROP TABLE IF EXISTS `nms_sqlserverstatus`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlserverstatus` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sqlserversysvalue`
--

DROP TABLE IF EXISTS `nms_sqlserversysvalue`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sqlserversysvalue` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `productlevel` varchar(50) DEFAULT NULL,
  `version` varchar(200) DEFAULT NULL,
  `machinename` varchar(50) DEFAULT NULL,
  `issingleuser` varchar(50) DEFAULT NULL,
  `processid` varchar(50) DEFAULT NULL,
  `isintegratedsecurityonly` varchar(50) DEFAULT NULL,
  `isclustered` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_storage_data_temp`
--

DROP TABLE IF EXISTS `nms_storage_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_storage_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `stype` varchar(30) DEFAULT NULL,
  `cap` varchar(30) DEFAULT NULL,
  `storageindex` varchar(50) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:24
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_sybasedbdetailinfo`
--

DROP TABLE IF EXISTS `nms_sybasedbdetailinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sybasedbdetailinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `name` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `db_created` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `dumptrdate` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  `status` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sybasedbinfo`
--

DROP TABLE IF EXISTS `nms_sybasedbinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sybasedbinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `db_created` varchar(50) DEFAULT NULL,
  `db_freesize` varchar(50) DEFAULT NULL,
  `db_namer` varchar(50) DEFAULT NULL,
  `db_owner` varchar(50) DEFAULT NULL,
  `db_size` varchar(50) DEFAULT NULL,
  `db_status` varchar(50) DEFAULT NULL,
  `db_usedperc` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sybasedeviceinfo`
--

DROP TABLE IF EXISTS `nms_sybasedeviceinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sybasedeviceinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `device_description` varchar(200) DEFAULT NULL,
  `device_name` varchar(50) DEFAULT NULL,
  `device_physical_name` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sybaseengineinfo`
--

DROP TABLE IF EXISTS `nms_sybaseengineinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sybaseengineinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `status` varchar(10) CHARACTER SET gb2312 DEFAULT NULL,
  `starttime` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  `engine` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sybaseperformance`
--

DROP TABLE IF EXISTS `nms_sybaseperformance`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sybaseperformance` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `cpu_busy` varchar(50) DEFAULT NULL,
  `idle` varchar(50) DEFAULT NULL,
  `version` varchar(200) DEFAULT NULL,
  `io_busy` varchar(50) DEFAULT NULL,
  `Sent_rate` varchar(50) DEFAULT NULL,
  `Received_rate` varchar(50) DEFAULT NULL,
  `Write_rate` varchar(50) DEFAULT NULL,
  `Read_rate` varchar(50) DEFAULT NULL,
  `ServerName` varchar(50) DEFAULT NULL,
  `Cpu_busy_rate` varchar(50) DEFAULT NULL,
  `Io_busy_rate` varchar(50) DEFAULT NULL,
  `Disk_count` varchar(50) DEFAULT NULL,
  `Locks_count` varchar(50) DEFAULT NULL,
  `Xact_count` varchar(50) DEFAULT NULL,
  `Total_dataCache` varchar(50) DEFAULT NULL,
  `Total_physicalMemory` varchar(50) DEFAULT NULL,
  `Metadata_cache` varchar(50) DEFAULT NULL,
  `Procedure_cache` varchar(50) DEFAULT NULL,
  `Total_logicalMemory` varchar(50) DEFAULT NULL,
  `Data_hitrate` varchar(50) DEFAULT NULL,
  `Procedure_hitrate` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  `offlineengine` varchar(50) DEFAULT NULL,
  `maxconn` varchar(50) DEFAULT NULL,
  `usedconn` varchar(50) DEFAULT NULL,
  `processcount` varchar(50) DEFAULT NULL,
  `sleepingproccount` varchar(50) DEFAULT NULL,
  `maxlock` varchar(50) DEFAULT NULL,
  `longtrans` varchar(50) DEFAULT NULL,
  `maxPageSize` varchar(50) DEFAULT NULL,
  `totallog` varchar(50) DEFAULT NULL,
  `freelog` varchar(50) DEFAULT NULL,
  `usedlog` varchar(50) DEFAULT NULL,
  `reservedlog` varchar(50) DEFAULT NULL,
  `logusedperc` varchar(50) DEFAULT NULL,
  `maxsegusedperc` varchar(50) DEFAULT NULL,
  `curdate` varchar(50) DEFAULT NULL,
  `boottime` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sybaseprocessinfo`
--

DROP TABLE IF EXISTS `nms_sybaseprocessinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sybaseprocessinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `spid` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `hostname` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `status` varchar(100) CHARACTER SET gb2312 DEFAULT NULL,
  `hostprocess` varchar(500) CHARACTER SET gb2312 DEFAULT NULL,
  `program_name` varchar(500) CHARACTER SET gb2312 DEFAULT NULL,
  `serverip` varchar(500) CHARACTER SET gb2312 DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sybaseserversinfo`
--

DROP TABLE IF EXISTS `nms_sybaseserversinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sybaseserversinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `Server_class` varchar(50) DEFAULT NULL,
  `Server_name` varchar(50) DEFAULT NULL,
  `Server_network_name` varchar(50) DEFAULT NULL,
  `Server_status` varchar(200) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sybasestatus`
--

DROP TABLE IF EXISTS `nms_sybasestatus`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sybasestatus` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_sybaseuserinfo`
--

DROP TABLE IF EXISTS `nms_sybaseuserinfo`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_sybaseuserinfo` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `serverip` varchar(50) DEFAULT NULL,
  `Group_name` varchar(50) DEFAULT NULL,
  `ID_in_db` varchar(50) DEFAULT NULL,
  `Login_name` varchar(50) DEFAULT NULL,
  `Users_name` varchar(50) DEFAULT NULL,
  `mon_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_system_data_temp`
--

DROP TABLE IF EXISTS `nms_system_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_system_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(30) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:25
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_ups_battery_data_temp`
--

DROP TABLE IF EXISTS `nms_ups_battery_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ups_battery_data_temp` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `RESTYPE` varchar(20) DEFAULT NULL,
  `CATEGORY` varchar(50) DEFAULT NULL,
  `ENTITY` varchar(100) DEFAULT NULL,
  `SUBENTITY` varchar(60) DEFAULT NULL,
  `THEVALUE` varchar(255) DEFAULT NULL,
  `COLLECTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UNIT` varchar(30) DEFAULT NULL,
  `COUNT` bigint(20) DEFAULT NULL,
  `BAK` varchar(100) DEFAULT NULL,
  `CHNAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_ups_bypass_data_temp`
--

DROP TABLE IF EXISTS `nms_ups_bypass_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ups_bypass_data_temp` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `RESTYPE` varchar(20) DEFAULT NULL,
  `CATEGORY` varchar(50) DEFAULT NULL,
  `ENTITY` varchar(100) DEFAULT NULL,
  `SUBENTITY` varchar(60) DEFAULT NULL,
  `THEVALUE` varchar(255) DEFAULT NULL,
  `COLLECTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UNIT` varchar(30) DEFAULT NULL,
  `COUNT` bigint(20) DEFAULT NULL,
  `BAK` varchar(100) DEFAULT NULL,
  `CHNAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_ups_input_data_temp`
--

DROP TABLE IF EXISTS `nms_ups_input_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ups_input_data_temp` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `RESTYPE` varchar(20) DEFAULT NULL,
  `CATEGORY` varchar(50) DEFAULT NULL,
  `ENTITY` varchar(100) DEFAULT NULL,
  `SUBENTITY` varchar(60) DEFAULT NULL,
  `THEVALUE` varchar(255) DEFAULT NULL,
  `COLLECTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UNIT` varchar(30) DEFAULT NULL,
  `COUNT` bigint(20) DEFAULT NULL,
  `BAK` varchar(100) DEFAULT NULL,
  `CHNAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_ups_output_data_temp`
--

DROP TABLE IF EXISTS `nms_ups_output_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ups_output_data_temp` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `RESTYPE` varchar(20) DEFAULT NULL,
  `CATEGORY` varchar(50) DEFAULT NULL,
  `ENTITY` varchar(100) DEFAULT NULL,
  `SUBENTITY` varchar(60) DEFAULT NULL,
  `THEVALUE` varchar(255) DEFAULT NULL,
  `COLLECTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UNIT` varchar(30) DEFAULT NULL,
  `COUNT` bigint(20) DEFAULT NULL,
  `BAK` varchar(100) DEFAULT NULL,
  `CHNAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_ups_statue_data_temp`
--

DROP TABLE IF EXISTS `nms_ups_statue_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ups_statue_data_temp` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `RESTYPE` varchar(20) DEFAULT NULL,
  `CATEGORY` varchar(50) DEFAULT NULL,
  `ENTITY` varchar(100) DEFAULT NULL,
  `SUBENTITY` varchar(60) DEFAULT NULL,
  `THEVALUE` varchar(255) DEFAULT NULL,
  `COLLECTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UNIT` varchar(30) DEFAULT NULL,
  `COUNT` bigint(20) DEFAULT NULL,
  `BAK` varchar(100) DEFAULT NULL,
  `CHNAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_ups_systemgroup_data_temp`
--

DROP TABLE IF EXISTS `nms_ups_systemgroup_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_ups_systemgroup_data_temp` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `IPADDRESS` varchar(30) DEFAULT NULL,
  `RESTYPE` varchar(20) DEFAULT NULL,
  `CATEGORY` varchar(50) DEFAULT NULL,
  `ENTITY` varchar(100) DEFAULT NULL,
  `SUBENTITY` varchar(60) DEFAULT NULL,
  `THEVALUE` varchar(255) DEFAULT NULL,
  `COLLECTTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UNIT` varchar(30) DEFAULT NULL,
  `COUNT` bigint(20) DEFAULT NULL,
  `BAK` varchar(100) DEFAULT NULL,
  `CHNAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_user_data_temp`
--

DROP TABLE IF EXISTS `nms_user_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_user_data_temp` (
  `nodeid` varchar(10) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `entity` varchar(30) DEFAULT NULL,
  `subentity` varchar(30) DEFAULT NULL,
  `sindex` varchar(300) DEFAULT NULL,
  `thevalue` varchar(300) DEFAULT NULL,
  `chname` varchar(50) DEFAULT NULL,
  `restype` varchar(20) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `unit` varchar(10) DEFAULT NULL,
  `bak` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_cluster_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_cluster_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_cluster_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `clusterVirIndex` int(4) DEFAULT NULL,
  `clusterId` int(4) DEFAULT NULL,
  `clusterVirState` int(2) DEFAULT NULL,
  `clusterVirIfname` varchar(50) DEFAULT NULL,
  `clusterVirAddr` varchar(50) DEFAULT NULL,
  `clusterVirAuthType` int(11) DEFAULT NULL,
  `clusterVirAuthPasswd` varchar(20) DEFAULT NULL,
  `clusterVirPreempt` int(5) DEFAULT NULL,
  `clusterVirInterval` int(5) DEFAULT NULL,
  `clusterVirPriority` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_count_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_count_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_count_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `imapsCount` int(5) DEFAULT NULL,
  `smtpsCount` int(5) DEFAULT NULL,
  `appFilterCount` int(5) DEFAULT NULL,
  `dvpnSiteCount` int(5) DEFAULT NULL,
  `dvpnResourceCount` int(5) DEFAULT NULL,
  `dvpnTunnelCount` int(5) DEFAULT NULL,
  `dvpnAclCount` int(5) DEFAULT NULL,
  `maxCluster` int(5) DEFAULT NULL,
  `clusterNum` int(5) DEFAULT NULL,
  `infNumber` int(5) DEFAULT NULL,
  `virtualSiteCount` int(5) DEFAULT NULL,
  `vpnCount` int(5) DEFAULT NULL,
  `webCount` int(5) DEFAULT NULL,
  `rsCount` int(5) DEFAULT NULL,
  `vsCount` int(5) DEFAULT NULL,
  `vclientAppCount` int(5) DEFAULT NULL,
  `virtualSiteGroupCount` int(5) DEFAULT NULL,
  `tcsModuleCount` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_flowrate_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_flowrate_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_flowrate_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `totalBytesRcvd` int(10) DEFAULT NULL,
  `totalBytesSent` int(10) DEFAULT NULL,
  `rcvdBytesPerSec` int(10) DEFAULT NULL,
  `sentBytesPerSec` int(10) DEFAULT NULL,
  `peakRcvdBytesPerSec` int(10) DEFAULT NULL,
  `peakSentBytesPerSec` int(10) DEFAULT NULL,
  `activeTransac` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:25
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_vpn_interface_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_interface_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_interface_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `infIndex` int(4) DEFAULT NULL,
  `infDescr` varchar(10) DEFAULT NULL,
  `infOperStatus` varchar(10) DEFAULT NULL,
  `infAddress` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_log_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_log_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_log_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `logNotificationsSent` int(4) DEFAULT NULL,
  `logNotificationsEnabled` int(4) DEFAULT NULL,
  `logMaxSeverity` int(10) DEFAULT NULL,
  `logHistTableMaxLength` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_session_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_session_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_session_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `numSessions` int(10) DEFAULT NULL,
  `successLogin` int(10) DEFAULT NULL,
  `successLogout` int(10) DEFAULT NULL,
  `failureLogin` int(10) DEFAULT NULL,
  `totalBytesIn` int(20) DEFAULT NULL,
  `totalBytesOut` int(20) DEFAULT NULL,
  `maxActiveSessions` int(10) DEFAULT NULL,
  `errorLogin` int(4) DEFAULT NULL,
  `lockOutLogin` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_ssl_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_ssl_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_ssl_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `sslIndex` int(6) DEFAULT NULL,
  `vhostName` varchar(50) DEFAULT NULL,
  `openSSLConns` int(10) DEFAULT NULL,
  `acceptedConns` int(10) DEFAULT NULL,
  `requestedConns` int(10) DEFAULT NULL,
  `resumedSess` int(10) DEFAULT NULL,
  `resumableSess` int(10) DEFAULT NULL,
  `missSess` int(10) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_sslsysinfor_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_sslsysinfor_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_sslsysinfor_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `sslStatus` varchar(5) DEFAULT NULL,
  `vhostNum` int(4) DEFAULT NULL,
  `totalOpenSSLConns` int(10) DEFAULT NULL,
  `totalAcceptedConns` int(10) DEFAULT NULL,
  `totalRequestedConns` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_system_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_system_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_system_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `cpuUtilization` int(5) DEFAULT NULL,
  `connectionsPerSec` int(5) DEFAULT NULL,
  `requestsPerSec` int(5) DEFAULT NULL,
  `sysDescr` varchar(30) DEFAULT NULL,
  `sysObjectID` varchar(30) DEFAULT NULL,
  `sysUpTime` varchar(30) DEFAULT NULL,
  `sysContact` varchar(30) DEFAULT NULL,
  `sysName` varchar(30) DEFAULT NULL,
  `sysLocation` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_tcp_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_tcp_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_tcp_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `ctcpActiveOpens` int(10) DEFAULT NULL,
  `ctcpAttemptFails` int(10) DEFAULT NULL,
  `ctcpPassiveOpens` int(10) DEFAULT NULL,
  `ctcpEstabResets` int(10) DEFAULT NULL,
  `ctcpCurrEstab` int(10) DEFAULT NULL,
  `ctcpInSegs` int(10) DEFAULT NULL,
  `ctcpOutSegs` int(10) DEFAULT NULL,
  `ctcpRetransSegs` int(10) DEFAULT NULL,
  `ctcpInErrs` int(10) DEFAULT NULL,
  `ctcpOutRsts` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_tcs_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_tcs_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_tcs_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `tcsModuleIndex` int(4) DEFAULT NULL,
  `tcsVirtualSite` varchar(20) DEFAULT NULL,
  `tcsBytesIn` int(10) DEFAULT NULL,
  `tcsBytesOut` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_vc_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_vc_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_vc_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `vclientAppIndex` int(4) DEFAULT NULL,
  `vclientAppVirtualSite` varchar(20) DEFAULT NULL,
  `vclientAppBytesIn` int(10) DEFAULT NULL,
  `vclientAppBytesOut` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_vip_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_vip_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_vip_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `vipStatus` int(4) DEFAULT NULL,
  `hostName` varchar(20) DEFAULT NULL,
  `currentTime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:26
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `nms_vpn_vpn_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_vpn_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_vpn_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `vpnId` varchar(20) DEFAULT NULL,
  `vpnTunnelsOpen` int(10) DEFAULT NULL,
  `vpnTunnelsEst` int(10) DEFAULT NULL,
  `vpnTunnelsRejected` int(10) DEFAULT NULL,
  `vpnTunnelsTerminated` int(10) DEFAULT NULL,
  `vpnBytesIn` int(10) DEFAULT NULL,
  `vpnBytesOut` int(10) DEFAULT NULL,
  `vpnUnauthPacketsIn` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_vs_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_vs_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_vs_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `vsIndex` int(4) DEFAULT NULL,
  `vsID` varchar(20) DEFAULT NULL,
  `vsProtocol` int(4) DEFAULT NULL,
  `vsIpAddr` varchar(20) DEFAULT NULL,
  `vsPort` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_vsite_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_vsite_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_vsite_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `virtualSiteId` varchar(20) DEFAULT NULL,
  `virtualSiteActiveSessions` int(10) DEFAULT NULL,
  `virtualSiteSuccessLogin` int(10) DEFAULT NULL,
  `virtualSiteFailureLogin` int(10) DEFAULT NULL,
  `virtualSiteErrorLogin` int(10) DEFAULT NULL,
  `virtualSiteSuccessLogout` int(10) DEFAULT NULL,
  `virtualSiteBytesIn` int(20) DEFAULT NULL,
  `virtualSiteBytesOut` int(20) DEFAULT NULL,
  `vSiteMaxActSess` int(10) DEFAULT NULL,
  `vSiteFileAuthReq` int(10) DEFAULT NULL,
  `vSiteFileUnauthReq` int(10) DEFAULT NULL,
  `virtualSiteFileBytesIn` int(10) DEFAULT NULL,
  `virtualSiteFileBytesOut` int(10) DEFAULT NULL,
  `virtualSiteLockedLogin` int(10) DEFAULT NULL,
  `virtualSiteRejectedLogin` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_vpn_web_data_temp`
--

DROP TABLE IF EXISTS `nms_vpn_web_data_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_vpn_web_data_temp` (
  `id` bigint(11) NOT NULL,
  `ipaddress` varchar(30) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `subtype` varchar(30) DEFAULT NULL,
  `Collecttime` varchar(20) DEFAULT NULL,
  `webId` varchar(20) DEFAULT NULL,
  `webAuthorizedReq` int(10) DEFAULT NULL,
  `webUnauthorizedReq` int(10) DEFAULT NULL,
  `webBytesIn` int(20) DEFAULT NULL,
  `webBytesOut` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_weblogic_heap`
--

DROP TABLE IF EXISTS `nms_weblogic_heap`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_weblogic_heap` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(15) DEFAULT NULL,
  `jvmRuntimeName` varchar(50) DEFAULT NULL,
  `jvmRuntimeHeapSizeCurrent` varchar(50) DEFAULT NULL,
  `jvmRuntimeHeapFreeCurrent` varchar(50) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_weblogic_jdbc`
--

DROP TABLE IF EXISTS `nms_weblogic_jdbc`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_weblogic_jdbc` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(15) DEFAULT NULL,
  `jdbcConnectionPoolName` varchar(50) DEFAULT NULL,
  `ConPoolRunActConnsCurCount` varchar(50) DEFAULT NULL,
  `ConPoolRunVerJDBCDriver` varchar(50) DEFAULT NULL,
  `ConPoolRunMaxCapacity` varchar(50) DEFAULT NULL,
  `ConPoolRunActConsAvgCount` varchar(50) DEFAULT NULL,
  `ConPoolRunHighestNumAvai` varchar(50) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_weblogic_normal`
--

DROP TABLE IF EXISTS `nms_weblogic_normal`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_weblogic_normal` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(15) DEFAULT NULL,
  `domainName` varchar(50) DEFAULT NULL,
  `domainActive` varchar(50) DEFAULT NULL,
  `domainAdministrationPort` varchar(50) DEFAULT NULL,
  `domainConfigurationVersion` varchar(50) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_weblogic_queue`
--

DROP TABLE IF EXISTS `nms_weblogic_queue`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_weblogic_queue` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(15) DEFAULT NULL,
  `executeQueueRuntimeName` varchar(50) DEFAULT NULL,
  `thdPoolRunExeThdIdleCnt` varchar(50) DEFAULT NULL,
  `exeQueRunPendReqOldTime` varchar(50) DEFAULT NULL,
  `exeQueRunPendReqCurCount` varchar(50) DEFAULT NULL,
  `exeQueRunPendReqTotCount` varchar(50) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_weblogic_server`
--

DROP TABLE IF EXISTS `nms_weblogic_server`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_weblogic_server` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `nodeid` varchar(15) DEFAULT NULL,
  `serverRuntimeName` varchar(50) DEFAULT NULL,
  `serverRuntimeListenAddress` varchar(50) DEFAULT NULL,
  `serverRuntimeListenPort` varchar(50) DEFAULT NULL,
  `RunOpenSocketsCurCount` varchar(50) DEFAULT NULL,
  `serverRuntimeState` varchar(50) DEFAULT NULL,
  `ipaddress` varchar(50) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `nms_weblogic_servlet`
--

DROP TABLE IF EXISTS `nms_weblogic_servlet`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `nms_weblogic_servlet` (
  `id` bigint(11) NOT NULL,
  `nodeid` varchar(15) DEFAULT NULL,
  `RunType` varchar(50) DEFAULT NULL,
  `RunName` varchar(200) DEFAULT NULL,
  `RunServletName` varchar(200) DEFAULT NULL,
  `RunReloadTotalCnt` varchar(50) DEFAULT NULL,
  `RunInvoTotCnt` varchar(50) DEFAULT NULL,
  `RunPoolMaxCapacity` varchar(50) DEFAULT NULL,
  `RunExecTimeTotal` varchar(50) DEFAULT NULL,
  `RunExecTimeHigh` varchar(50) DEFAULT NULL,
  `RunExecTimeLow` varchar(50) DEFAULT NULL,
  `RunExecTimeAvg` varchar(50) DEFAULT NULL,
  `RunURL` varchar(200) DEFAULT NULL,
  `collecttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-09  1:10:27
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- MySQL dump 10.13  Distrib 5.1.32, for Win32 (ia32)
--
-- Host: localhost    Database: afunms
-- ------------------------------------------------------
-- Server version	5.1.32-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
