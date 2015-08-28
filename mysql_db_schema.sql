/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `addresses` (
  `ADDR_ID` int(11) NOT NULL AUTO_INCREMENT,
  `STREET` varchar(50) NOT NULL,
  `CITY` varchar(50) NOT NULL,
  `STATE` varchar(50) NOT NULL,
  `ZIP` varchar(10) DEFAULT NULL,
  `COUNTRY` varchar(50) NOT NULL,
  PRIMARY KEY (`ADDR_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `authentication_user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authentication_user_roles` (
  `user_role_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `ROLE` varchar(45) NOT NULL,
  PRIMARY KEY (`user_role_id`),
  UNIQUE KEY `uni_username_role` (`ROLE`,`username`),
  KEY `fk_username_idx` (`username`),
  CONSTRAINT `fk_username` FOREIGN KEY (`username`) REFERENCES `authentication_users` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `authentication_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authentication_users` (
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `books` (
  `bookCode` varchar(10) NOT NULL,
  `entity` varchar(10) DEFAULT NULL,
  `isValid` char(1) DEFAULT NULL,
  `lastUpdated` datetime DEFAULT NULL,
  `lastUpdatedBy` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`bookCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `chatmessages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chatmessages` (
  `sequenceId` int(10) unsigned NOT NULL,
  `requestForQuoteId` int(10) unsigned NOT NULL,
  `owner` varchar(20) NOT NULL,
  `content` varchar(500) NOT NULL,
  `createTime` varchar(20) NOT NULL,
  PRIMARY KEY (`requestForQuoteId`,`sequenceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `identifier` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `isValid` char(1) NOT NULL DEFAULT 'Y',
  `tier` varchar(10) NOT NULL,
  `lastUpdated` datetime NOT NULL,
  `lastUpdatedBy` varchar(20) NOT NULL,
  PRIMARY KEY (`identifier`),
  UNIQUE KEY `id_UNIQUE` (`identifier`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `course_enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `course_enrollment` (
  `COURSE_ID` int(11) NOT NULL,
  `STUD_ID` int(11) NOT NULL,
  PRIMARY KEY (`COURSE_ID`,`STUD_ID`),
  KEY `FK_ENROLLMENT_STUD` (`STUD_ID`),
  CONSTRAINT `FK_ENROLLMENT_COURSE` FOREIGN KEY (`COURSE_ID`) REFERENCES `courses` (`COURSE_ID`),
  CONSTRAINT `FK_ENROLLMENT_STUD` FOREIGN KEY (`STUD_ID`) REFERENCES `students` (`STUD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `courses` (
  `COURSE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL,
  `DESCRIPTION` varchar(512) DEFAULT NULL,
  `START_DATE` date DEFAULT NULL,
  `END_DATE` date DEFAULT NULL,
  `TUTOR_ID` int(11) NOT NULL,
  PRIMARY KEY (`COURSE_ID`),
  KEY `FK_COURSE_TUTOR` (`TUTOR_ID`),
  CONSTRAINT `FK_COURSE_TUTOR` FOREIGN KEY (`TUTOR_ID`) REFERENCES `tutors` (`TUTOR_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `groupId` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(45) NOT NULL,
  `isValid` char(1) NOT NULL DEFAULT 'Y',
  `lastUpdated` datetime DEFAULT NULL,
  `lastUpdatedBy` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`groupId`),
  UNIQUE KEY `groupName_UNIQUE` (`groupName`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `holidays`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `holidays` (
  `location` varchar(20) NOT NULL,
  `bankHolidayDate` datetime NOT NULL,
  `lastUpdatedBy` varchar(20) NOT NULL,
  `identifier` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `isValid` char(1) DEFAULT 'Y',
  `lastUpdated` datetime NOT NULL,
  PRIMARY KEY (`identifier`),
  UNIQUE KEY `location_date_index` (`location`,`bankHolidayDate`),
  UNIQUE KEY `identifier_UNIQUE` (`identifier`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `optionleg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `optionleg` (
  `requestId` int(11) NOT NULL,
  `legId` int(11) NOT NULL,
  `delta` decimal(13,6) NOT NULL,
  `gamma` decimal(13,6) NOT NULL,
  `vega` decimal(13,6) NOT NULL,
  `theta` decimal(13,6) NOT NULL,
  `rho` decimal(13,6) NOT NULL,
  `volatility` decimal(13,6) NOT NULL,
  `daysToExpiry` decimal(13,6) NOT NULL,
  `yearsToExpiry` decimal(13,6) NOT NULL,
  `tradeDate` datetime NOT NULL,
  `maturityDate` datetime NOT NULL,
  `underlyingPrice` decimal(13,6) NOT NULL,
  `underlyingRIC` varchar(10) NOT NULL,
  `isCall` char(1) NOT NULL DEFAULT 'Y',
  `isEuropean` varchar(45) NOT NULL DEFAULT 'Y',
  `dayCountConvention` decimal(13,6) NOT NULL,
  `interestRate` decimal(13,6) NOT NULL,
  `premium` decimal(13,6) NOT NULL,
  `premiumPercentage` decimal(13,6) NOT NULL,
  `strike` decimal(13,6) NOT NULL,
  `strikePercentage` decimal(13,6) NOT NULL,
  `quantity` int(11) NOT NULL,
  `side` varchar(1) NOT NULL DEFAULT 'Y',
  `UpdatedBy` varchar(20) NOT NULL,
  `Updated` datetime NOT NULL,
  PRIMARY KEY (`requestId`,`legId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `requestforquotemain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requestforquotemain` (
  `lotSize` int(10) DEFAULT NULL,
  `identifier` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `clientId` int(10) unsigned NOT NULL,
  `bookCode` varchar(20) DEFAULT NULL,
  `request` varchar(100) NOT NULL,
  `isOTC` char(1) NOT NULL DEFAULT 'Y',
  `status` varchar(20) DEFAULT 'PENDING',
  `pickedUpBy` varchar(20) DEFAULT NULL,
  `notionalMillions` decimal(13,3) NOT NULL DEFAULT '1.000',
  `notionalCurrency` char(3) DEFAULT 'USD',
  `notionalFXRate` decimal(13,3) DEFAULT NULL,
  `contracts` int(10) NOT NULL DEFAULT '100',
  `multiplier` int(10) NOT NULL DEFAULT '1',
  `quantity` int(10) DEFAULT NULL,
  `tradeDate` datetime NOT NULL,
  `expiryDate` datetime DEFAULT NULL,
  `premiumSettlementCurrency` char(3) DEFAULT 'USD',
  `premiumSettlementDate` datetime DEFAULT NULL,
  `premiumSettlementDaysOverride` int(10) DEFAULT NULL,
  `premiumSettlementFXRate` decimal(13,3) DEFAULT NULL,
  `salesCreditAmount` decimal(13,3) DEFAULT NULL,
  `salesCreditPercentage` decimal(13,3) DEFAULT NULL,
  `salesCreditFXRate` decimal(13,3) DEFAULT NULL,
  `salesCreditCurrency` char(3) DEFAULT 'USD',
  `salesComment` varchar(300) DEFAULT NULL,
  `traderComment` varchar(300) DEFAULT NULL,
  `clientComment` varchar(300) DEFAULT NULL,
  `premiumAmount` decimal(13,3) DEFAULT NULL,
  `premiumPercentage` decimal(13,3) DEFAULT NULL,
  `impliedVol` decimal(13,3) DEFAULT NULL,
  `delta` decimal(13,3) DEFAULT NULL,
  `gamma` decimal(13,3) DEFAULT NULL,
  `vega` decimal(13,3) DEFAULT NULL,
  `theta` decimal(13,3) DEFAULT NULL,
  `rho` decimal(13,3) DEFAULT NULL,
  `deltaNotional` decimal(13,3) DEFAULT NULL,
  `gammaNotional` decimal(13,3) DEFAULT NULL,
  `vegaNotional` decimal(13,3) DEFAULT NULL,
  `thetaNotional` decimal(13,3) DEFAULT NULL,
  `rhoNotional` decimal(13,3) DEFAULT NULL,
  `deltaShares` decimal(13,3) DEFAULT NULL,
  `gammaShares` decimal(13,3) DEFAULT NULL,
  `vegaShares` decimal(13,3) DEFAULT NULL,
  `thetaShares` decimal(13,3) DEFAULT NULL,
  `rhoShares` decimal(13,3) DEFAULT NULL,
  `totalPremium` decimal(13,3) DEFAULT NULL,
  `hedgePrice` decimal(13,3) DEFAULT NULL,
  `hedgeType` varchar(20) DEFAULT NULL,
  `askFinalAmount` decimal(13,3) DEFAULT NULL,
  `askFinalPercentage` decimal(13,3) DEFAULT NULL,
  `askImpliedVol` decimal(13,3) DEFAULT NULL,
  `askPremiumAmount` decimal(13,3) DEFAULT NULL,
  `askPremiumPercentage` decimal(13,3) DEFAULT NULL,
  `bidFinalAmount` decimal(13,3) DEFAULT NULL,
  `bidFinalPercentage` decimal(13,3) DEFAULT NULL,
  `bidImpliedVol` decimal(13,3) DEFAULT NULL,
  `bidPremiumAmount` decimal(13,3) DEFAULT NULL,
  `bidPremiumPercentage` decimal(13,3) DEFAULT NULL,
  `savedBy` varchar(20) DEFAULT 'anonymous',
  `dayCountConvention` decimal(13,3) DEFAULT '250.000',
  `intrinsicValue` decimal(13,3) NOT NULL DEFAULT '0.000',
  `timeValue` decimal(13,3) NOT NULL DEFAULT '0.000',
  `lambda` decimal(13,3) NOT NULL DEFAULT '0.000',
  `underlyingRIC` varchar(10) NOT NULL DEFAULT 'NONE',
  `underlyingPrice` decimal(13,3) NOT NULL DEFAULT '0.000',
  `lastUpdate` datetime DEFAULT NULL,
  `lastUpdatedBy` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`identifier`),
  UNIQUE KEY `identifier_UNIQUE` (`identifier`)
) ENGINE=InnoDB AUTO_INCREMENT=2276 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `searches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `searches` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `owner` varchar(45) NOT NULL,
  `searchKey` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `value` varchar(255) NOT NULL,
  `isPrivate` char(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`id`),
  UNIQUE KEY `owner_and_key_and_control_UNIQUE` (`owner`,`searchKey`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `students` (
  `STUD_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `PHONE` varchar(15) DEFAULT NULL,
  `DOB` date DEFAULT NULL,
  `BIO` longtext,
  `PIC` blob,
  `ADDR_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`STUD_ID`),
  KEY `FK_STUDENTS_ADDR` (`ADDR_ID`),
  CONSTRAINT `FK_STUDENTS_ADDR` FOREIGN KEY (`ADDR_ID`) REFERENCES `addresses` (`ADDR_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `tutors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tutors` (
  `TUTOR_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `PHONE` varchar(15) DEFAULT NULL,
  `DOB` date DEFAULT NULL,
  `BIO` longtext,
  `PIC` blob,
  `ADDR_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TUTOR_ID`),
  KEY `FK_TUTORS_ADDR` (`ADDR_ID`),
  CONSTRAINT `FK_TUTORS_ADDR` FOREIGN KEY (`ADDR_ID`) REFERENCES `addresses` (`ADDR_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `underlyings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `underlyings` (
  `ric` varchar(10) NOT NULL,
  `description` varchar(45) NOT NULL,
  `referencePrice` decimal(10,3) NOT NULL DEFAULT '100.000',
  `simulationPriceVariance` decimal(10,3) NOT NULL DEFAULT '1.000',
  `spread` decimal(10,3) NOT NULL DEFAULT '1.000',
  `dividendYield` decimal(10,3) NOT NULL DEFAULT '0.000',
  `isValid` char(1) NOT NULL DEFAULT 'Y',
  `lastUpdatedBy` varchar(20) NOT NULL,
  `lastUpdated` datetime NOT NULL,
  PRIMARY KEY (`ric`),
  UNIQUE KEY `RIC_UNIQUE` (`ric`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `user_pics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_pics` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL,
  `PIC` blob,
  `BIO` longtext,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `userId` varchar(20) NOT NULL,
  `firstName` varchar(20) NOT NULL,
  `lastName` varchar(20) NOT NULL,
  `emailAddress` varchar(100) NOT NULL,
  `groupName` varchar(100) NOT NULL,
  `locationName` varchar(100) NOT NULL,
  `isValid` char(1) NOT NULL,
  `lastUpdated` datetime NOT NULL,
  `lastUpdatedBy` varchar(20) NOT NULL,
  PRIMARY KEY (`userId`),
  UNIQUE KEY `emailAddress_UNIQUE` (`emailAddress`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 DROP FUNCTION IF EXISTS `GetNextSequenceId` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 FUNCTION `GetNextSequenceId`(p_requestForQuoteId INT) RETURNS int(11)
BEGIN
    DECLARE maxSequenceId INT;
    
    SELECT IFNULL(MAX(sequenceId), 0)
    FROM chatmessages 
    WHERE requestForQuoteId = p_requestForQuoteId
    INTO @maxSequenceId;
    
    RETURN @maxSequenceId + 1;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `GetTimeStampPrefix` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 FUNCTION `GetTimeStampPrefix`() RETURNS varchar(2) CHARSET utf8
BEGIN
    DECLARE prefix varchar(2) DEFAULT "";
    DECLARE difference INT;
    SET difference = TIMESTAMPDIFF(HOUR, UTC_TIMESTAMP, NOW());
    
    CASE 
        WHEN difference BETWEEN 1 AND 9 THEN SET prefix = "+0";
        WHEN difference BETWEEN -9 AND -1 THEN SET prefix = "-0";
        WHEN difference >= 10 THEN SET prefix = "+";
        ELSE SET prefix = "-";               
    END CASE;
    RETURN prefix;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `books_DELETE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `books_DELETE`(p_bookCode varchar(10))
BEGIN
    DELETE FROM books
    WHERE bookCode = p_bookCode;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `books_SAVE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `books_SAVE`(
p_bookCode varchar(10),
p_entity varchar(10),
p_savedBy varchar(20)
)
BEGIN
    INSERT INTO books(bookCode, entity, isValid, lastUpdated, lastUpdatedBy)
    VALUES(p_bookCode, p_entity, 'Y', now(), p_savedBy);
    
    SELECT * 
    FROM books
    WHERE bookCode = p_bookCode;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `books_SELECT` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `books_SELECT`(
p_bookCode VARCHAR(10)
)
BEGIN
    SELECT * 
    FROM books
    WHERE bookCode = p_bookCode;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `books_SELECT_ALL` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `books_SELECT_ALL`()
BEGIN
    SELECT * FROM books;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `books_UPDATE_VALIDITY` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `books_UPDATE_VALIDITY`(p_bookCode varchar(10), p_isValid CHAR(1))
BEGIN
    UPDATE books SET isValid = p_isValid
    WHERE bookCode = p_bookCode;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `chatMessages_GET` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `chatMessages_GET`(
p_requestForQuoteId INT,
p_sequenceId INT
)
BEGIN
    SELECT *
    FROM chatMessages
    WHERE requestForQuoteId = p_requestForQuoteId
    AND sequenceId >= p_sequenceId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `chatMessages_SAVE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `chatMessages_SAVE`(
p_requestForQuoteId INT,
p_owner varchar(20),
p_content varchar(500)
)
BEGIN    
    SET @nextSequenceId = GetNextSequenceId(p_requestForQuoteId);    
    
    INSERT INTO chatMessages (requestForQuoteId, sequenceId, owner, content, createTime)
    VALUES (p_requestForQuoteId, @nextSequenceId, p_owner, p_content, now());
          
    SELECT * 
    FROM chatMessages
    WHERE requestForQuoteId = p_requestForQuoteId
    AND sequenceId = @nextSequenceId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `clients_DELETE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `clients_DELETE`(p_identifier int)
BEGIN
    DELETE FROM clients
    WHERE identifier = p_identifier;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `clients_SAVE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `clients_SAVE`(
p_name varchar(100),
p_tier varchar(10),
p_savedBy varchar(20)
)
BEGIN
    INSERT INTO clients(name, tier, isValid, lastUpdated, lastUpdatedBy)
    VALUES(p_name, p_tier, 'Y', now(), p_savedBy);
    
    SELECT *
    FROM clients
    WHERE name = p_name;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `clients_SELECT` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `clients_SELECT`(
p_identifier int(11)
)
BEGIN
    SELECT * 
    FROM clients
    WHERE identifier = p_identifier;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `clients_SELECT_ALL` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `clients_SELECT_ALL`()
BEGIN
    SELECT * FROM clients;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `clients_UPDATE_TIER` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `clients_UPDATE_TIER`(
p_identifier int,
p_tier varchar(10),
p_updatedBy varchar(20)
)
BEGIN
    UPDATE 
        clients 
    SET 
        tier = p_tier, 
        lastUpdatedBy = p_updatedBy, 
        lastUpdated = now()
    WHERE
        identifier = p_identifier;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `clients_UPDATE_VALIDITY` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `clients_UPDATE_VALIDITY`(
p_identifier int, 
p_isValid CHAR(1),
p_updatedBy varchar(20)
)
BEGIN
    UPDATE 
        clients 
    SET 
        isValid = p_isValid, 
        lastUpdatedBy = p_updatedBy, 
        lastUpdated = now()
    WHERE 
        identifier = p_identifier;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `greeksByBookCode` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `greeksByBookCode`(
p_maturityDateFrom DATETIME,
P_maturityDateTo DATETIME,
p_minimumGreeks DECIMAL(13,3)
)
BEGIN   
    SELECT 
        IFNULL(rfq.bookCode,'NONE') categoryValue,
        SUM(ol.delta) delta,
        SUM(ol.gamma) gamma,
        SUM(ol.vega) vega,
        SUM(ol.theta) theta,
        SUM(ol.rho) rho
    FROM 
        requestforquotemain rfq, optionLeg ol
    WHERE
        ol.maturityDate <= p_maturityDateTo
    AND
        ol.maturityDate >= p_maturityDateFrom
    AND
        rfq.identifier = ol.requestId
    GROUP By 
        rfq.bookCode
    HAVING 
        ABS(SUM(ol.delta)) > p_minimumGreeks
    AND 
        ABS(SUM(ol.gamma)) > p_minimumGreeks
    AND
        ABS(SUM(ol.vega)) > p_minimumGreeks
    AND 
        ABS(SUM(ol.theta)) > p_minimumGreeks
    AND
        ABS(SUM(ol.rho)) > p_minimumGreeks;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `greeksByClient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `greeksByClient`(
p_maturityDateFrom DATETIME,
P_maturityDateTo DATETIME,
p_minimumGreeks DECIMAL(13,3)
)
BEGIN
    SELECT 
        IFNULL(cl.name,'NONE') categoryValue, 
        SUM(ol.delta) delta,
        SUM(ol.gamma) gamma,
        SUM(ol.vega) vega,
        SUM(ol.theta) theta,
        SUM(ol.rho) rho
    FROM 
        requestforquotemain rfq, clients cl, optionleg ol
    WHERE
        ol.maturityDate <= p_maturityDateTo
    AND
        ol.maturityDate >= p_maturityDateFrom
    AND
        cl.identifier = rfq.clientId
    AND
        rfq.identifier = ol.requestId
    GROUP By 
        rfq.clientId
    HAVING 
        ABS(SUM(ol.delta)) > p_minimumGreeks
    AND 
        ABS(SUM(ol.gamma)) > p_minimumGreeks
    AND
        ABS(SUM(ol.vega)) > p_minimumGreeks
    AND 
        ABS(SUM(ol.theta)) > p_minimumGreeks
    AND
        ABS(SUM(ol.rho)) > p_minimumGreeks;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `greeksByMaturityDate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `greeksByMaturityDate`(
p_maturityDateFrom DATETIME,
P_maturityDateTo DATETIME,
p_minimumGreeks DECIMAL(13,3)
)
BEGIN
    SELECT 
        IFNULL(DATE(maturityDate),'NONE') categoryValue,
        SUM(delta) delta,
        SUM(gamma) gamma,
        SUM(vega) vega,
        SUM(theta) theta,
        SUM(rho) rho
    FROM 
        optionleg
    WHERE
        maturityDate <= p_maturityDateTo
    AND
        maturityDate >= p_maturityDateFrom
    GROUP By 
        maturityDate
    HAVING 
        ABS(SUM(delta)) > p_minimumGreeks
    AND 
        ABS(SUM(gamma)) > p_minimumGreeks
    AND
        ABS(SUM(vega)) > p_minimumGreeks
    AND 
        ABS(SUM(theta)) > p_minimumGreeks
    AND
        ABS(SUM(rho)) > p_minimumGreeks;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `greeksByStatus` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `greeksByStatus`(
p_maturityDateFrom DATETIME,
P_maturityDateTo DATETIME,
p_minimumGreeks DECIMAL(13,3)
)
BEGIN
    SELECT 
        IFNULL(rfq.status,'NONE') categoryValue,
        SUM(ol.delta) delta,
        SUM(ol.gamma) gamma,
        SUM(ol.vega) vega,
        SUM(ol.theta) theta,
        SUM(ol.rho) rho
    FROM 
        requestforquotemain rfq, optionleg ol
    WHERE
        ol.maturityDate <= p_maturityDateTo
    AND
        ol.maturityDate >= p_maturityDateFrom
    AND
        ol.requestId = rfq.identifier
    GROUP By 
        rfq.status
    HAVING 
        ABS(SUM(ol.delta)) > p_minimumGreeks
    AND 
        ABS(SUM(ol.gamma)) > p_minimumGreeks
    AND
        ABS(SUM(ol.vega)) > p_minimumGreeks
    AND 
        ABS(SUM(ol.theta)) > p_minimumGreeks
    AND
        ABS(SUM(ol.rho)) > p_minimumGreeks;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `greeksByTradeDate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `greeksByTradeDate`(
p_maturityDateFrom DATETIME,
P_maturityDateTo DATETIME,
p_minimumGreeks DECIMAL(13,3)
)
BEGIN
    SELECT 
        IFNULL(DATE(rfq.tradeDate),'NONE') categoryValue,
        SUM(ol.delta) delta,
        SUM(ol.gamma) gamma,
        SUM(ol.vega) vega,
        SUM(ol.theta) theta,
        SUM(ol.rho) rho
    FROM 
        requestforquotemain rfq, optionleg ol
    WHERE
        ol.maturityDate <= p_maturityDateTo
    AND
        ol.maturityDate >= p_maturityDateFrom
    AND
        ol.requestId = rfq.identifier
    GROUP By 
        rfq.tradeDate
    HAVING 
        ABS(SUM(ol.delta)) > p_minimumGreeks
    AND 
        ABS(SUM(ol.gamma)) > p_minimumGreeks
    AND
        ABS(SUM(ol.vega)) > p_minimumGreeks
    AND 
        ABS(SUM(ol.theta)) > p_minimumGreeks
    AND
        ABS(SUM(ol.rho)) > p_minimumGreeks;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `groups_DELETE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `groups_DELETE`(
p_groupId INT(11)
)
BEGIN
    DELETE 
    FROM groups
    WHERE groupId = p_groupId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `groups_SAVE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `groups_SAVE`(
p_groupName VARCHAR(45),
p_savedBy varchar(20)
)
BEGIN
    INSERT INTO groups(groupName, isValid, lastUpdated, lastUpdatedBy)
    VALUES(p_groupName, 'Y', now(), p_savedBy);
        
    SELECT *
    FROM groups
    WHERE groupName = p_groupName;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `groups_SELECT_ALL` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `groups_SELECT_ALL`()
BEGIN
    SELECT *
    FROM groups;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `groups_SELECT_BY_GROUP_ID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `groups_SELECT_BY_GROUP_ID`(
p_groupId INT(11)
)
BEGIN
    SELECT *
    FROM groups
    WHERE groupId = p_groupId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `groups_UPDATE_VALIDITY` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `groups_UPDATE_VALIDITY`(
p_groupId int(11),
p_isValid CHAR(1),
p_updatedBy varchar(20)
)
BEGIN
    UPDATE 
        groups 
    SET 
        isValid = p_isValid, 
        lastUpdatedBy = p_updatedBy, 
        lastUpdated = now()
    WHERE 
        groupId = p_groupId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `holidays_DELETE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `holidays_DELETE`(
p_location varchar(20),
p_holidayDate varchar(20)
)
BEGIN
    DELETE FROM holidays
    WHERE location = p_location AND holidayDate = p_holidayDate;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `holidays_GET` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `holidays_GET`(p_location varchar(20))
BEGIN
    SELECT * FROM holidays
    WHERE location = p_location;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `holidays_SAVE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `holidays_SAVE`(
p_location varchar(20),
p_holidayDate varchar(20),
p_addedBy varchar(20)
)
BEGIN
    INSERT INTO holidays(location, holidayDate, addedBy)
    VALUES(p_location, p_holidayDate, p_addedBy);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `holidays_SELECT_ALL` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `holidays_SELECT_ALL`()
BEGIN
    SELECT * FROM holidays;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `optionLeg_SAVE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `optionLeg_SAVE`(
p_requestId INT, 
p_legId INT, 
p_delta DECIMAL(13,6),
p_gamma DECIMAL(13,6),
p_vega DECIMAL(13,6),
p_theta DECIMAL(13,6),
p_rho DECIMAL(13,6),
p_volatility DECIMAL(13,6),
p_daysToExpiry DECIMAL(13,6),
p_yearsToExpiry DECIMAL(13,6),
p_maturityDate DATETIME, 
p_tradeDate DATETIME, 
p_underlyingPrice DECIMAL(13,6),
p_underlyingRIC VARCHAR(10), 
p_isCall CHAR(1), 
p_isEuropean CHAR(1),
p_interestRate DECIMAL(13,6),
p_dayCountConvention DECIMAL(13,6),
p_premium DECIMAL(13,6),
p_premiumPercentage DECIMAL(13,6),
p_strike DECIMAL(13,6),
p_strikePercentage DECIMAL(13,6),
p_side VARCHAR(1), 
p_quantity DECIMAL(13,6),
p_updatedBy VARCHAR(20)
)
BEGIN

INSERT INTO `rfq_development`.`optionleg` 
(requestId, 
legId, 
delta, 
gamma, 
vega, 
theta, 
rho, 
volatility, 
daysToExpiry, 
yearsToExpiry, 
maturityDate,
tradeDate,
underlyingPrice, 
underlyingRIC, 
isCall, 
isEuropean, 
dayCountConvention, 
interestRate, 
premium, 
premiumPercentage, 
strike, 
strikePercentage, 
quantity, 
side, 
UpdatedBy, 
Updated)
VALUES
(
p_requestId, 
p_legId, 
p_delta, 
p_gamma, 
p_vega, 
p_theta, 
p_rho, 
p_volatility, 
p_daysToExpiry, 
p_yearsToExpiry, 
p_maturityDate,
p_tradeDate,
p_underlyingPrice, 
p_underlyingRIC, 
p_isCall, 
p_isEuropean, 
p_dayCountConvention, 
p_interestRate, 
p_premium, 
p_premiumPercentage, 
p_strike, 
p_strikePercentage, 
p_quantity, 
p_side, 
p_updatedBy, 
now()
);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `optionLeg_SELECT` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `optionLeg_SELECT`(
p_requestId INT
)
BEGIN
    SELECT *
    FROM optionLeg
    WHERE requestId = p_requestId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `optionLeg_UPDATE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `optionLeg_UPDATE`(
p_requestId INT, 
p_legId INT, 
p_delta DECIMAL(13,6),
p_gamma DECIMAL(13,6),
p_vega DECIMAL(13,6),
p_theta DECIMAL(13,6),
p_rho DECIMAL(13,6),
p_volatility DECIMAL(13,6),
p_daysToExpiry DECIMAL(13,6),
p_yearsToExpiry DECIMAL(13,6),
p_maturityDate DATETIME, 
p_tradeDate DATETIME, 
p_underlyingPrice DECIMAL(13,6),
p_underlyingRIC VARCHAR(10), 
p_isCall CHAR(1), 
p_isEuropean CHAR(1), 
p_dayCountConvention DECIMAL(13,6),
p_interestRate DECIMAL(13,6),
p_premium DECIMAL(13,6),
p_premiumPercentage DECIMAL(13,6),
p_strike DECIMAL(13,6),
p_strikePercentage DECIMAL(13,6),
p_side VARCHAR(1), 
p_quantity DECIMAL(13,6),
p_updatedBy VARCHAR(20)
)
BEGIN
    UPDATE `rfq_development`.`optionleg`
    SET
    `delta` = p_delta,
    `gamma` = p_gamma,
    `vega` = p_vega,
    `theta` = p_theta,
    `rho` = p_rho,

    `volatility` = p_volatility,
    `daysToExpiry` = p_daysToExpiry,
    `yearsToExpiry` = p_yearsToExpiry,
    `maturityDate` = p_maturityDate,
    `tradeDate` = p_tradeDate,
    `underlyingPrice` = p_underlyingPrice,

    `underlyingRIC` = p_underlyingRIC,
    `isCall` = p_isCall,
    `isEuropean` = p_isEuropean,
    `dayCountConvention` = p_dayCountConvention,
    `interestRate` = p_interestRate,

    `premium` = p_premium,
    `premiumPercentage` = p_premiumPercentage,
    `strike` = p_strike,
    `strikePercentage` = p_strikePercentage,
    `quantity` = p_quantity,

    `side` = p_side,
    `UpdatedBy` = p_UpdatedBy
    WHERE 
    requestId = p_requestId AND legId = p_legId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `requestsCountByBookCode` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `requestsCountByBookCode`(
p_fromDate DATETIME,
p_minimumCount INT
)
BEGIN
    SELECT 
        IFNULL(bookCode,'NONE') categoryValue, 
        COUNT(*) requestCount
    FROM 
        requestforquotemain
    WHERE 
        tradeDate >= p_fromDate
    GROUP By 
        bookCode
    HAVING 
        COUNT(*) > p_minimumCount;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `requestsCountByClient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `requestsCountByClient`(
p_fromDate DATETIME,
p_minimumCount INT
)
BEGIN
    SELECT 
        IFNULL(cl.name,'NONE') categoryValue, 
        COUNT(*) requestCount
    FROM 
        requestforquotemain rfqs, clients cl
    WHERE 
        tradeDate > p_fromDate
    AND
        cl.identifier = rfqs.clientId
    GROUP By 
        rfqs.clientId
    HAVING 
        COUNT(*) > p_minimumCount;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `requestsCountByMaturityDate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `requestsCountByMaturityDate`(
p_fromDate DATETIME,
p_minimumCount INT
)
BEGIN
    SELECT 
        IFNULL(DATE(ol.maturityDate),'NONE') categoryValue, 
        COUNT(*) requestCount
    FROM 
        requestforquotemain rfq, optionleg ol
    WHERE 
        rfq.tradeDate >= p_fromDate
    AND
        rfq.identifier = ol.requestId
    GROUP By 
        ol.maturityDate
    HAVING 
        COUNT(*) > p_minimumCount;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `requestsCountByPicker` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `requestsCountByPicker`(
p_fromDate DATETIME,
p_minimumCount INT
)
BEGIN
    SELECT 
        IFNULL(pickedUpBy,'NONE') categoryValue, 
        COUNT(*) requestCount
    FROM 
        requestforquotemain
    WHERE 
        tradeDate >= p_fromDate
    GROUP By 
        pickedUpBy
    HAVING 
        COUNT(*) > p_minimumCount;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `requestsCountByStatus` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `requestsCountByStatus`(
p_fromDate DATETIME,
p_minimumCount INT
)
BEGIN
    SELECT 
        IFNULL(status,'NONE') categoryValue, 
        COUNT(*) requestCount
    FROM 
        requestforquotemain
    WHERE 
        tradeDate >= p_fromDate
    GROUP By 
        status
    HAVING 
        COUNT(*) > p_minimumCount;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `requestsCountByTradeDate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `requestsCountByTradeDate`(
p_fromDate DATETIME,
p_minimumCount INT
)
BEGIN
    SELECT 
        IFNULL(DATE(tradeDate),'NONE') categoryValue, 
        COUNT(*) requestCount
    FROM 
        requestforquotemain
    WHERE 
        tradeDate >= p_fromDate
    GROUP By 
        tradeDate
    HAVING 
        COUNT(*) > p_minimumCount;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `requests_SEARCH` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `requests_SEARCH`(
p_owner VARCHAR(45),
p_keyValue VARCHAR(45)
)
BEGIN
DECLARE exitLoop INT DEFAULT 0;
DECLARE criterionKey VARCHAR(45);
DECLARE criterionValue VARCHAR(255);
DECLARE sqlStatement VARCHAR(500);

DECLARE criterion_cursor CURSOR FOR 
    SELECT controlName, controlValue 
    FROM searches 
    WHERE owner = p_owner AND keyValue = p_keyValue;    
    
DECLARE CONTINUE HANDLER FOR NOT FOUND SET exitLoop = 1;    

SET sqlStatement = "SELECT * FROM requestforquotemain WHERE";

OPEN criterion_cursor;

rfq_loop: LOOP
 
    FETCH criterion_cursor INTO criterionKey, criterionValue;
 
    IF exitLoop = 1 THEN
        LEAVE rfq_loop;
    END IF;
    
   
    CASE
        WHEN criterionKey = "Book" THEN
            SET sqlStatement = CONCAT(sqlStatement, " bookCode = \"", criterionValue, "\" AND");
        WHEN criterionKey = "Client" THEN
            SET sqlStatement = CONCAT(sqlStatement, " clientId = ", criterionValue, " AND");
        WHEN criterionKey = "Status" THEN
            SET sqlStatement = CONCAT(sqlStatement, " status = \"", criterionValue, "\" AND");
    END CASE;
 
 END LOOP rfq_loop;

CLOSE criterion_cursor;

SET @sqlStatement = CONCAT(sqlStatement, " 1 = 1"); 

PREPARE stmt FROM @sqlStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `requests_SELECT_TODAY` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `requests_SELECT_TODAY`()
BEGIN
    SELECT *
    FROM requestforquotemain
    WHERE DATE(tradeDate) = DATE(NOW());
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `request_GET` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `request_GET`(
p_identifier INT(10)
)
BEGIN
    SELECT *
    FROM requestforquotemain
    WHERE identifier = p_identifier;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `request_SAVE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `request_SAVE`(
p_request VARCHAR(100), 
p_bookCode VARCHAR(10), 
p_clientId INT(10),  
p_isOTC CHAR(1), 
p_status VARCHAR(20),

p_tradeDate DATETIME, 
p_expiryDate DATETIME,
p_dayCountConvention DECIMAL(13,3),
				
p_lotSize INT(10),
p_multiplier INT(10), 
p_contracts INT(10), 
p_quantity INT(10),

p_notionalMillions DECIMAL(13,3),
p_notionalFXRate DECIMAL(13,3), 
p_notionalCurrency CHAR(3),

p_delta DECIMAL(13,3), 
p_gamma DECIMAL(13,3),
p_vega DECIMAL(13,3), 
p_theta DECIMAL(13,3), 
p_rho DECIMAL(13,3),

p_deltaNotional DECIMAL(13,3), 
p_gammaNotional DECIMAL(13,3),
p_vegaNotional DECIMAL(13,3), 
p_thetaNotional DECIMAL(13,3), 
p_rhoNotional DECIMAL(13,3),

p_deltaShares DECIMAL(13,3),
p_gammaShares DECIMAL(13,3), 
p_vegaShares DECIMAL(13,3), 
p_thetaShares DECIMAL(13,3), 
p_rhoShares DECIMAL(13,3),

p_askFinalAmount DECIMAL(13,3), 
p_askFinalPercentage DECIMAL(13,3), 
p_askImpliedVol DECIMAL(13,3), 
p_askPremiumAmount DECIMAL(13,3),
p_askPremiumPercentage DECIMAL(13,3),

p_bidFinalAmount DECIMAL(13,3), 
p_bidFinalPercentage DECIMAL(13,3), 
p_bidImpliedVol DECIMAL(13,3),
p_bidPremiumAmount DECIMAL(13,3), 
p_bidPremiumPercentage DECIMAL(13,3),

p_premiumAmount DECIMAL(13,3), 
p_premiumPercentage DECIMAL(13,3),
p_impliedVol DECIMAL(13,3),

p_salesCreditAmount DECIMAL(13,3),
p_salesCreditPercentage DECIMAL(13,3),
p_salesCreditCurrency CHAR(3),
p_salesCreditFXRate DECIMAL(13,3),

p_premiumSettlementCurrency CHAR(3),
p_premiumSettlementDate DATETIME,
p_premiumSettlementDaysOverride INT(10),
p_premiumSettlementFXRate DECIMAL(13,3),

p_salesComment VARCHAR(300),
p_traderComment VARCHAR(300),
p_clientComment VARCHAR(300),

p_hedgePrice DECIMAL(13,3),
p_hedgeType VARCHAR(20),
p_totalPremium DECIMAL(13,3),
p_pickedUpBy VARCHAR(20),
p_savedBy VARCHAR(20)
)
BEGIN
    INSERT INTO requestforquotemain
    (
    lotSize, 
    clientId, 
    bookCode, 
    request, 
    isOTC, 
    status, 
    pickedUpBy, 
    notionalMillions, 
    notionalCurrency, 
    notionalFXRate, 
    contracts, 
    multiplier, 
    quantity, 
    tradeDate, 
    expiryDate,
    dayCountConvention,
    premiumSettlementCurrency, 
    premiumSettlementDate, 
    premiumSettlementDaysOverride, 
    premiumSettlementFXRate, 
    salesCreditAmount, 
    salesCreditPercentage, 
    salesCreditFXRate, 
    salesCreditCurrency, 
    salesComment, 
    traderComment, 
    clientComment, 
    premiumAmount, 
    premiumPercentage, 
    impliedVol, 
    delta, 
    gamma, 
    vega, 
    theta, 
    rho, 
    deltaNotional, 
    gammaNotional, 
    vegaNotional, 
    thetaNotional, 
    rhoNotional, 
    deltaShares, 
    gammaShares, 
    vegaShares, 
    thetaShares, 
    rhoShares, 
    totalPremium, 
    hedgePrice, 
    hedgeType, 
    askFinalAmount, 
    askFinalPercentage, 
    askImpliedVol, 
    askPremiumAmount, 
    askPremiumPercentage, 
    bidFinalAmount, 
    bidFinalPercentage, 
    bidImpliedVol, 
    bidPremiumAmount, 
    bidPremiumPercentage,
    savedBy
    )
    VALUES
    (
    p_lotSize, 
    p_clientId, 
    p_bookCode, 
    p_request, 
    p_isOTC, 
    p_status, 
    p_pickedUpBy, 
    p_notionalMillions, 
    p_notionalCurrency, 
    p_notionalFXRate, 
    p_contracts, 
    p_multiplier, 
    p_quantity, 
    p_tradeDate, 
    p_expiryDate,
    p_dayCountConvention,
    p_premiumSettlementCurrency, 
    p_premiumSettlementDate, 
    p_premiumSettlementDaysOverride, 
    p_premiumSettlementFXRate, 
    p_salesCreditAmount, 
    p_salesCreditPercentage, 
    p_salesCreditFXRate, 
    p_salesCreditCurrency, 
    p_salesComment, 
    p_traderComment, 
    p_clientComment, 
    p_premiumAmount, 
    p_premiumPercentage, 
    p_impliedVol, 
    p_delta, 
    p_gamma, 
    p_vega, 
    p_theta, 
    p_rho, 
    p_deltaNotional, 
    p_gammaNotional, 
    p_vegaNotional, 
    p_thetaNotional, 
    p_rhoNotional, 
    p_deltaShares, 
    p_gammaShares, 
    p_vegaShares, 
    p_thetaShares, 
    p_rhoShares, 
    p_totalPremium, 
    p_hedgePrice, 
    p_hedgeType, 
    p_askFinalAmount, 
    p_askFinalPercentage, 
    p_askImpliedVol, 
    p_askPremiumAmount, 
    p_askPremiumPercentage, 
    p_bidFinalAmount, 
    p_bidFinalPercentage, 
    p_bidImpliedVol, 
    p_bidPremiumAmount, 
    p_bidPremiumPercentage,
    p_savedBy
    );
    
    SELECT *
    FROM requestforquotemain
    WHERE identifier = (SELECT MAX(identifier) FROM requestforquotemain);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `request_UPDATE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `request_UPDATE`(
p_identifier INT(10),
p_request VARCHAR(100), 
p_bookCode VARCHAR(10), 
p_clientId INT(10),  
p_isOTC CHAR(1), 
p_status VARCHAR(20),

p_tradeDate DATETIME, 
p_expiryDate DATETIME,
p_dayCountConvention DECIMAL(13,3),
				
p_lotSize INT(10),
p_multiplier INT(10), 
p_contracts INT(10), 
p_quantity INT(10),

p_notionalMillions DECIMAL(13,3),
p_notionalFXRate DECIMAL(13,3), 
p_notionalCurrency CHAR(3),

p_delta DECIMAL(13,3), 
p_gamma DECIMAL(13,3),
p_vega DECIMAL(13,3), 
p_theta DECIMAL(13,3), 
p_rho DECIMAL(13,3),

p_deltaNotional DECIMAL(13,3), 
p_gammaNotional DECIMAL(13,3),
p_vegaNotional DECIMAL(13,3), 
p_thetaNotional DECIMAL(13,3), 
p_rhoNotional DECIMAL(13,3),

p_deltaShares DECIMAL(13,3),
p_gammaShares DECIMAL(13,3), 
p_vegaShares DECIMAL(13,3), 
p_thetaShares DECIMAL(13,3), 
p_rhoShares DECIMAL(13,3),

p_askFinalAmount DECIMAL(13,3), 
p_askFinalPercentage DECIMAL(13,3), 
p_askImpliedVol DECIMAL(13,3), 
p_askPremiumAmount DECIMAL(13,3),
p_askPremiumPercentage DECIMAL(13,3),

p_bidFinalAmount DECIMAL(13,3), 
p_bidFinalPercentage DECIMAL(13,3), 
p_bidImpliedVol DECIMAL(13,3),
p_bidPremiumAmount DECIMAL(13,3), 
p_bidPremiumPercentage DECIMAL(13,3),

p_premiumAmount DECIMAL(13,3), 
p_premiumPercentage DECIMAL(13,3),
p_impliedVol DECIMAL(13,3),

p_salesCreditAmount DECIMAL(13,3),
p_salesCreditPercentage DECIMAL(13,3),
p_salesCreditCurrency CHAR(3),
p_salesCreditFXRate DECIMAL(13,3),

p_premiumSettlementCurrency CHAR(3),
p_premiumSettlementDate DATETIME,
p_premiumSettlementDaysOverride INT(10),
p_premiumSettlementFXRate DECIMAL(13,3),

p_salesComment VARCHAR(300),
p_traderComment VARCHAR(300),
p_clientComment VARCHAR(300),

p_hedgePrice DECIMAL(13,3),
p_hedgeType VARCHAR(20),
p_totalPremium DECIMAL(13,3),
p_pickedUpBy VARCHAR(20),
p_updatedBy VARCHAR(20)
)
BEGIN
    UPDATE requestforquotemain
    SET lotSize = p_lotSize, 
    clientId = p_clientId,
    bookCode = p_bookCode,
    request = p_request, 
    isOTC = p_isOTC, 
    status = p_status, 
    pickedUpBy = p_pickedUpBy, 
    notionalMillions = p_notionalMillions, 
    notionalCurrency = p_notionalCurrency, 
    notionalFXRate = p_notionalFXRate, 
    contracts = p_contracts, 
    multiplier = p_multiplier, 
    quantity = p_quantity, 
    tradeDate = p_tradeDate, 
    expiryDate = p_expiryDate,
    dayCountConvention = p_dayCountConvention,
    premiumSettlementCurrency = p_premiumSettlementCurrency, 
    premiumSettlementDate = p_premiumSettlementDate, 
    premiumSettlementDaysOverride = p_premiumSettlementDaysOverride, 
    premiumSettlementFXRate = p_premiumSettlementFXRate, 
    salesCreditAmount = p_salesCreditAmount, 
    salesCreditPercentage = p_salesCreditPercentage, 
    salesCreditFXRate = p_salesCreditFXRate, 
    salesCreditCurrency = p_salesCreditCurrency, 
    salesComment = p_salesComment, 
    traderComment = p_traderComment, 
    clientComment = p_clientComment, 
    premiumAmount = p_premiumAmount, 
    premiumPercentage = p_premiumPercentage, 
    impliedVol = p_impliedVol, 
    delta = p_delta, 
    gamma = p_gamma, 
    vega = p_vega, 
    theta = p_theta, 
    rho = p_rho, 
    deltaNotional = p_deltaNotional, 
    gammaNotional = p_gammaNotional, 
    vegaNotional = p_vegaNotional, 
    thetaNotional = p_thetaNotional, 
    rhoNotional = p_rhoNotional, 
    deltaShares = p_deltaShares, 
    gammaShares = p_gammaShares, 
    vegaShares = p_vegaShares, 
    thetaShares = p_thetaShares, 
    rhoShares = p_rhoShares, 
    totalPremium = p_totalPremium, 
    hedgePrice = p_hedgePrice, 
    hedgeType = p_hedgeType, 
    askFinalAmount = p_askFinalAmount, 
    askFinalPercentage = p_askFinalPercentage, 
    askImpliedVol = p_askImpliedVol, 
    askPremiumAmount = p_askPremiumAmount, 
    askPremiumPercentage = p_askPremiumPercentage, 
    bidFinalAmount = p_bidFinalAmount, 
    bidFinalPercentage = p_bidFinalPercentage, 
    bidImpliedVol = p_bidImpliedVol, 
    bidPremiumAmount = p_bidPremiumAmount, 
    bidPremiumPercentage = p_bidPremiumPercentage,
    savedBy = p_updatedBy
    WHERE identifier = p_identifier;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `searches_DELETE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `searches_DELETE`(
p_owner varchar(45),
p_keyValue varchar(45)
)
BEGIN
    DELETE FROM searches
    WHERE owner = p_owner AND keyValue = p_keyValue;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `searches_GET` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `searches_GET`(
p_owner varchar(45),
p_keyValue varchar(45)
)
BEGIN
    SELECT * FROM searches 
    WHERE owner = p_owner AND keyValue = p_keyValue;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `searches_SAVE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `searches_SAVE`(
p_owner varchar(45),
P_keyValue varchar(45),
P_controlName varchar(45),
p_controlValue varchar(255),
P_isPrivate char(1),
p_isFilter char(1)
)
BEGIN
    INSERT INTO searches(owner, keyValue, controlName, controlValue, isPrivate, isFilter, lastUpdated)
    VALUES(p_owner, p_keyValue, p_controlName, p_controlValue, p_isPrivate, p_isFilter, now());
    
    SELECT *
    FROM searches
    WHERE owner = p_owner
    AND keyValue = p_keyValue
    AND controlName = p_controlName
    AND controlValue = p_controlValue;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `searches_SELECT_ALL` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `searches_SELECT_ALL`()
BEGIN
    SELECT * FROM searches;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `searches_UPDATE_PRIVACY` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `searches_UPDATE_PRIVACY`(p_owner varchar(45),
p_keyValue varchar(45),
p_isPrivate CHAR(1))
BEGIN
    UPDATE searches SET isPrivate = p_isPrivate
    WHERE owner = p_owner AND keyValue = p_keyValue;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `underlyings_SAVE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `underlyings_SAVE`(
p_ric VARCHAR(10),
p_description VARCHAR(45),
p_savedBy VARCHAR(20)
)
BEGIN
    INSERT INTO underlyings(ric, description, isValid, lastUpdatedBy, lastUpdated)
    VALUES(p_ric, p_description, 'Y', p_savedBy, now());
    
    SELECT *
    FROM underlyings
    WHERE ric = p_ric;    
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `underlyings_SELECT` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `underlyings_SELECT`(
p_ric varchar(10)
)
BEGIN
    SELECT * 
    FROM underlyings
    WHERE ric = p_ric;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `underlyings_SELECT_ALL` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `underlyings_SELECT_ALL`()
BEGIN
    SELECT *
    FROM underlyings;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `underlyings_UPDATE_VALIDITY` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `underlyings_UPDATE_VALIDITY`(
p_ric VARCHAR(10), 
p_isValid CHAR(1),
p_updatedBy varchar(20)
)
BEGIN
    UPDATE 
        underlyings 
    SET 
        isValid = p_isValid, 
        lastUpdatedBy = p_updatedBy, 
        lastUpdated = now()
    WHERE 
        ric = p_ric;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `users_DELETE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `users_DELETE`(
p_userId varchar(20)
)
BEGIN
    DELETE 
    FROM users
    WHERE userId = p_userId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `users_SAVE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `users_SAVE`(
p_userId varchar(20),
p_firstName varchar(20),
p_lastName varchar(20),
p_emailAddress varchar(100),
p_locationName varchar(100),
p_groupId INT(11),
p_savedBy varchar(20)
)
BEGIN
    INSERT INTO users(userId, firstName, lastName, emailAddress, locationName, groupId, isValid, lastUpdated, lastUpdatedBy)
    VALUES(p_userId, p_firstName, p_lastName, p_emailAddress, p_locationName, p_groupId, 'Y', now(), p_savedBy);
        
    SELECT * 
    FROM users
    WHERE userId = p_userId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `users_SELECT_ALL` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `users_SELECT_ALL`()
BEGIN
        SELECT *
        FROM users;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `users_SELECT_BY_EMAIL_ADDRESS` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `users_SELECT_BY_EMAIL_ADDRESS`(
p_emailAddress varchar(100)
)
BEGIN
    SELECT *
    FROM users
    WHERE emailAddress = p_emailAddress;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `users_SELECT_BY_GROUP` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `users_SELECT_BY_GROUP`(
p_groupId INT(11)
)
BEGIN
    SELECT *
    FROM users
    WHERE groupId = p_groupId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `users_SELECT_BY_LOCATION` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `users_SELECT_BY_LOCATION`(
p_locationName varchar(100)
)
BEGIN
    SELECT *
    FROM users
    WHERE locationName = p_locationName;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `users_SELECT_BY_USER_ID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `users_SELECT_BY_USER_ID`(
p_userId varchar(20)
)
BEGIN
    SELECT *
    FROM users
    WHERE userId = p_userId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `users_UPDATE_VALIDITY` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50020 DEFINER=`root`@`localhost`*/ /*!50003 PROCEDURE `users_UPDATE_VALIDITY`(
p_userId varchar(20), 
p_isValid CHAR(1),
p_updatedBy varchar(20)
)
BEGIN
    UPDATE 
        users 
    SET 
        isValid = p_isValid, 
        lastUpdatedBy = p_updatedBy, 
        lastUpdated = now()
    WHERE 
        userId = p_userId;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

