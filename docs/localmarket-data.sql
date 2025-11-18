-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: localmarket
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `board_id` int NOT NULL AUTO_INCREMENT,
  `board_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `board_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `hit_cnt` int DEFAULT '0',
  `like_cnt` int DEFAULT '0',
  `write_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `member_num` int NOT NULL,
  `store_id` int DEFAULT NULL,
  `market_id` int DEFAULT NULL COMMENT '시장 ID',
  `board_filename` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`board_id`),
  KEY `member_num` (`member_num`),
  KEY `store_id` (`store_id`),
  KEY `idx_board_market_id` (`market_id`),
  CONSTRAINT `board_ibfk_1` FOREIGN KEY (`member_num`) REFERENCES `member` (`member_num`) ON DELETE CASCADE,
  CONSTRAINT `board_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_board_market` FOREIGN KEY (`market_id`) REFERENCES `market` (`market_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

LOCK TABLES `board` WRITE;
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
INSERT INTO `board` VALUES (7,'동대문시장 방문 후기 (수정됨)','동대문시장에 다녀왔는데 정말 좋았습니다. 다양한 옷들이 많고 가격도 저렴해서 만족스러웠어요! 다시 가고 싶네요.',3,1,'2025-10-30 11:26:37','2025-11-10 11:34:45',6,NULL,NULL,NULL),(8,'김씨 한우전문점 리뷰','김씨 한우전문점에서 고기를 샀는데 품질도 좋고 사장님이 친절하셨습니다. 추천해요!',0,2,'2025-10-30 11:26:37','2025-11-10 11:34:16',6,3,1,NULL),(9,'전통시장의 매력','전통시장만의 특별한 정취와 인정 넘치는 분위기가 정말 좋습니다.',0,0,'2025-10-30 11:26:37','2025-10-30 11:26:37',6,NULL,NULL,NULL),(10,'동대문시장 방문 후기','동대문시장에 다녀왔는데 정말 좋았습니다. 다양한 옷들이 많고 가격도 저렴해서 만족스러웠어요!',10,3,'2025-10-30 11:31:11','2025-11-06 11:22:41',6,NULL,NULL,NULL),(11,'김씨 한우전문점 리뷰','김씨 한우전문점에서 고기를 샀는데 품질도 좋고 사장님이 친절하셨습니다. 추천해요!',5,0,'2025-10-30 11:31:11','2025-11-13 16:19:46',6,3,1,NULL),(12,'전통시장의 매력','전통시장만의 특별한 정취와 인정 넘치는 분위기가 정말 좋습니다.',1,0,'2025-10-30 11:31:11','2025-11-07 12:36:26',6,NULL,NULL,NULL),(15,'테스트2','1111',0,0,'2025-11-06 11:24:09','2025-11-06 11:24:09',216,NULL,NULL,NULL),(16,'테스트3','테스트3입니다. 제발 에러 잡자 수정하자!!!!',11,1,'2025-11-06 11:31:15','2025-11-06 12:45:55',216,NULL,NULL,NULL),(17,'동대문시장 테스트','테스트입니다.',1,0,'2025-11-10 11:11:48','2025-11-10 11:34:16',216,4,2,NULL),(18,'동대문 테스트 2','웃핫핫핫',2,0,'2025-11-10 11:26:57','2025-11-10 11:27:11',215,NULL,NULL,NULL),(19,'동대문 테스트 3','ㅁㅁㅁㅁㅁㅁ',0,0,'2025-11-10 11:35:49','2025-11-10 11:35:49',215,NULL,2,NULL),(20,'광장 테스트 1','123',4,1,'2025-11-10 11:39:43','2025-11-14 10:04:40',216,NULL,3,NULL),(21,'여기 반찬이 정말 맛있어요','여기 가게에서 파는 젓갈이 완전 밥도둑이네요~',12,1,'2025-11-14 09:36:47','2025-11-14 10:38:50',215,9,2,'board_temp_1763082112086.jpg');
/*!40000 ALTER TABLE `board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `cart_id` int NOT NULL AUTO_INCREMENT,
  `member_num` int NOT NULL,
  `product_id` int NOT NULL,
  `cart_quantity` int NOT NULL DEFAULT '1',
  `cart_price` decimal(10,2) NOT NULL,
  `cart_selected` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cart_id`),
  UNIQUE KEY `unique_cart_item` (`member_num`,`product_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`member_num`) REFERENCES `member` (`member_num`) ON DELETE CASCADE,
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (2,211,43,3,35000.00,0,'2025-10-29 19:22:56','2025-10-29 19:22:56');
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `member_num` int NOT NULL,
  `parent_comment_id` int DEFAULT NULL,
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `like_cnt` int DEFAULT '0',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_id`),
  KEY `board_id` (`board_id`),
  KEY `member_num` (`member_num`),
  KEY `parent_comment_id` (`parent_comment_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`) ON DELETE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`member_num`) REFERENCES `member` (`member_num`) ON DELETE CASCADE,
  CONSTRAINT `comment_ibfk_3` FOREIGN KEY (`parent_comment_id`) REFERENCES `comment` (`comment_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (4,7,6,NULL,'좋은 글이네요! 저도 동대문시장 가보고 싶어요. (수정됨)',1,'2025-10-30 11:27:04','2025-10-30 11:33:42'),(5,7,6,NULL,'정보 감사합니다. 도움이 되었어요!',2,'2025-10-30 11:27:04','2025-10-30 11:33:42'),(6,8,6,NULL,'김씨 한우전문점 정말 괜찮나요? 저도 가보려고 하는데요.',0,'2025-10-30 11:27:04','2025-10-30 11:27:04'),(7,7,6,NULL,'좋은 글이네요! 저도 동대문시장 가보고 싶어요.',0,'2025-10-30 11:33:42','2025-10-30 11:33:42'),(8,7,6,NULL,'정보 감사합니다. 도움이 되었어요!',0,'2025-10-30 11:33:42','2025-10-30 11:33:42'),(9,8,6,NULL,'김씨 한우전문점 정말 괜찮나요? 저도 가보려고 하는데요.',0,'2025-10-30 11:33:42','2025-10-30 11:33:42'),(15,16,216,NULL,'우헤헤헤',0,'2025-11-06 11:38:46','2025-11-06 11:38:46');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite` (
  `favorite_id` int NOT NULL AUTO_INCREMENT,
  `member_num` int NOT NULL,
  `target_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_id` int NOT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`favorite_id`),
  UNIQUE KEY `unique_favorite` (`member_num`,`target_type`,`target_id`),
  CONSTRAINT `favorite_ibfk_1` FOREIGN KEY (`member_num`) REFERENCES `member` (`member_num`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorite`
--

LOCK TABLES `favorite` WRITE;
/*!40000 ALTER TABLE `favorite` DISABLE KEYS */;
INSERT INTO `favorite` VALUES (1,6,'STORE',2,'2025-10-30 12:43:52'),(2,6,'MARKET',2,'2025-10-30 12:43:52'),(3,6,'STORE',1,'2025-10-30 12:43:52'),(4,211,'MARKET',1,'2025-10-30 12:43:52'),(5,211,'STORE',2,'2025-10-30 12:43:52'),(13,6,'MARKET',3,'2025-10-30 12:46:58'),(14,211,'STORE',3,'2025-10-30 12:46:58'),(15,212,'MARKET',4,'2025-10-30 12:46:58'),(17,213,'MARKET',5,'2025-10-30 12:46:58'),(19,216,'MARKET',2,'2025-11-07 09:46:04'),(21,215,'MARKET',3,'2025-11-11 16:22:26'),(22,215,'MARKET',1,'2025-11-11 16:22:27'),(23,215,'MARKET',2,'2025-11-11 16:47:18'),(24,215,'STORE',3,'2025-11-11 17:05:02'),(25,215,'STORE',7,'2025-11-11 17:05:07'),(26,215,'STORE',4,'2025-11-11 17:05:07'),(27,215,'STORE',9,'2025-11-14 10:06:51');
/*!40000 ALTER TABLE `favorite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `market`
--

DROP TABLE IF EXISTS `market`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `market` (
  `market_id` int NOT NULL AUTO_INCREMENT,
  `market_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `market_local` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `market_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `market_introduce` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `market_filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `market_URL` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`market_id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `market`
--

LOCK TABLES `market` WRITE;
/*!40000 ALTER TABLE `market` DISABLE KEYS */;
INSERT INTO `market` VALUES (1,'남대문시장','서울특별시','서울특별시 중구 남대문로 21','상설시장 시장입니다. 점포수: 약 1,700개개. 운영형태: 연중무휴. 편의시설: 아케이드, 화장실, 주차장, 음식점, 공공시설, 은행ATM. 영업시간: 24시간 (점포별 상이). ','market_1_1762742850045.jpg','tel:02-753-2805','2025-10-27 15:08:02'),(2,'동대문시장','서울특별시','서울특별시 중구 을지로6가 18-12','상설시장 시장입니다. 점포수: 약 800개개. 운영형태: 연중무휴. 편의시설: 엘리베이터, 화장실, 음식점, 은행ATM. 영업시간: 19:00-05:00 (새벽시장). ','market_2_1762739308080.jpg','tel:02-2266-4745','2025-10-27 15:08:02'),(3,'광장시장','서울특별시','서울특별시 종로구 창경궁로 88','상설시장 시장입니다. 점포수: 약 1,300개개. 운영형태: 연중무휴. 편의시설: 고객지원센터, 화장실, 주차장, 음식점, 은행ATM, 문화시설. 영업시간: 09:00-18:00. ','market_3_1762742762307.jpg','tel:02-2267-0291','2025-10-27 15:08:02'),(4,'노량진수산시장','서울특별시','서울특별시 동작구 노량진로 688','수산시장 시장입니다. 점포수: 약 700개개. 운영형태: 연중무휴. 편의시설: 엘리베이터, 에스컬레이터, 화장실, 주차장, 음식점, 금융시설, 은행ATM. 영업시간: 01:00-14:00 (수산물 경매). ',NULL,'tel:02-814-2211','2025-10-27 15:08:02'),(5,'강남역지하상가','서울특별시','서울특별시 강남구 강남대로 396','지하상가 시장입니다. 점포수: 약 200개개. 운영형태: 연중무휴. 편의시설: 엘리베이터, 에스컬레이터, 화장실, 주차장, 음식점, 은행ATM, 문화시설. 영업시간: 06:00-24:00. ',NULL,'tel:02-2185-8898','2025-10-27 15:08:02'),(6,'자갈치시장','부산광역시','부산광역시 중구 자갈치해안로 52','수산시장 시장입니다. 점포수: 약 1,100개개. 운영형태: 연중무휴. 편의시설: 엘리베이터, 화장실, 주차장, 음식점, 금융시설, 은행ATM, 문화시설. 영업시간: 05:00-22:00. ',NULL,'tel:051-245-2594','2025-10-27 15:08:03'),(7,'국제시장','부산광역시','부산광역시 중구 신창동4가 14-1','상설시장 시장입니다. 점포수: 약 1,500개개. 운영형태: 연중무휴. 편의시설: 아케이드, 화장실, 음식점, 은행ATM, 문화시설. 영업시간: 09:00-20:00. ',NULL,'tel:051-245-5410','2025-10-27 15:08:03'),(8,'부평깡통시장','부산광역시','부산광역시 중구 부평동 1가 14-2','상설시장 시장입니다. 편의시설: 아케이드, 화장실. ',NULL,'','2025-10-27 15:08:03'),(9,'서문시장','대구광역시','대구광역시 중구 큰장로26길 45','상설시장 시장입니다. 편의시설: 아케이드, 화장실, 주차장. ',NULL,'','2025-10-27 15:08:03'),(10,'부평시장','인천광역시','인천광역시 부평구 부평대로 52번길 12','상설시장 시장입니다. 편의시설: 아케이드, 화장실. ',NULL,'','2025-10-27 15:08:03'),(11,'신포국제시장','인천광역시','인천광역시 중구 신포로23번길 9','상설시장 시장입니다. 편의시설: 고객지원센터, 화장실, 주차장. ',NULL,'','2025-10-27 15:08:03'),(12,'동대문시장','서울','서울특별시 중구 을지로6가 18-12','서울의 대표적인 도매시장으로 24시간 영업하는 패션의 메카입니다. 다양한 의류와 액세서리를 저렴한 가격에 구매할 수 있습니다.',NULL,'http://www.ddm.co.kr','2025-10-28 14:17:22'),(13,'명동시장','서울','서울특별시 중구 명동2가 31-1','명동 중심가에 위치한 전통시장으로 관광객들에게 인기가 높습니다.','myeongdong.jpg','','2025-10-28 14:17:36'),(14,'강남시장','서울','서울특별시 강남구 논현동 38-1','강남구에 위치한 현대적인 시장으로 고급 식재료와 생활용품을 판매합니다.','gangnam.jpg','','2025-10-28 14:17:36'),(15,'잠실시장','서울','서울특별시 송파구 잠실동 40-1','잠실 신도시 내 위치한 대형 전통시장으로 신선한 농수산물을 취급합니다.','jamsil.jpg','','2025-10-28 14:17:36'),(16,'홍대시장','서울','서울특별시 마포구 홍익로 17','홍익대학교 주변 젊은 상권의 중심가로 트렌디한 상품들을 판매합니다.','hongdae.jpg','','2025-10-28 14:17:36'),(18,'국제시장','부산','부산광역시 중구 신창동 4가 10-2','부산의 역사가 깊은 전통시장으로 다양한 먹거리와 생활용품을 판매합니다.','gukje.jpg','','2025-10-28 14:19:40'),(19,'서문시장','대구','대구광역시 중구 대신동 300','대구의 600년 전통을 자랑하는 역사적인 시장으로 한약재와 직물이 유명합니다.','seomun.jpg','http://www.seomun.co.kr','2025-10-28 14:19:40'),(20,'신포시장','인천','인천광역시 중구 신포동 11-5','인천의 오래된 전통시장으로 신포닭강정으로 유명하며 다양한 먹거리가 있습니다.','sinpo.jpg','','2025-10-28 14:19:40'),(21,'양동시장','광주','광주광역시 동구 동명동 1-1','광주의 대표적인 전통시장으로 호남지역 특산품과 농산물이 풍부합니다.','yangdong.jpg','','2025-10-28 14:21:49'),(22,'중앙시장','대전','대전광역시 중구 은행동 145-1','대전의 중심가에 위치한 전통시장으로 다양한 농산물과 생활용품을 판매합니다.','jungang_daejeon.jpg','','2025-10-28 14:21:49'),(23,'수원시장','경기','경기도 수원시 팔달구 팔달로2가 24','수원의 전통적인 재래시장으로 경기도 특산품과 농산물이 풍부합니다.','suwon.jpg','','2025-10-28 14:21:49'),(24,'춘천시장','강원','강원도 춘천시 죽림동 18-1','춘천의 대표적인 전통시장으로 춘천닭갈비와 막국수가 유명합니다.','chuncheon.jpg','','2025-10-28 14:21:49'),(25,'청주시장','충북','충청북도 청주시 상당구 남문로2가 27','청주의 전통적인 재래시장으로 충북지역 농산물과 특산품이 풍부합니다.','cheongju.jpg','','2025-10-28 14:21:49'),(26,'서울시 동대문시장','서울','서울시 중구 동대문로','동대문 패션의 중심지',NULL,'http://dongdaemun.co.kr','2025-10-29 11:42:02'),(28,'인천시 신포시장','인천','인천시 중구 신포로','인천의 전통시장',NULL,'http://sinpo.co.kr','2025-10-29 11:42:02'),(29,'대구시 서문시장','대구','대구시 중구 큰장로','대구의 대표 전통시장',NULL,'http://seomun.co.kr','2025-10-29 11:42:02'),(30,'광주시 대인시장','광주','광주시 동구 대인로','광주의 전통시장',NULL,'http://daein.co.kr','2025-10-29 11:42:02'),(31,'테스트시장1','서울특별시','서울시 중구 테스트로 123','전통시장 테스트',NULL,'http://test1.co.kr','2025-10-29 14:19:23'),(32,'테스트시장2','부산광역시','부산시 해운대구 테스트대로 456','해산물 전문시장',NULL,'http://test2.co.kr','2025-10-29 14:19:23'),(33,'테스트시장3','대구광역시','대구시 중구 테스트길 789','과일야채 전문시장',NULL,'http://test3.co.kr','2025-10-29 14:19:23'),(34,'JSON테스트시장1','인천광역시','인천시 남동구 JSON로 100','JSON으로 등록된 테스트시장','market_34_1762740122260.png','http://jsontest1.co.kr','2025-10-29 14:19:35'),(35,'JSON테스트시장2','광주광역시','광주시 서구 JSON대로 200','JSON 파일 업로드 테스트',NULL,'http://jsontest2.co.kr','2025-10-29 14:19:35'),(56,'반송큰시장(반송2동시장)','부산광역시','부산광역시 해운대구 윗반송로51번길 86',NULL,NULL,NULL,'2025-11-13 16:48:11'),(57,'반송골목시장','부산광역시','부산광역시 해운대구 아랫반송로 39번길 18-1',NULL,NULL,NULL,'2025-11-13 16:48:11'),(58,'반여시장','부산광역시','부산광역시 해운대구 재반로226번길 48',NULL,NULL,NULL,'2025-11-13 16:48:11'),(59,'반여2동상가시장','부산광역시','부산광역시 해운대구 해운대로61번길 95-83',NULL,NULL,NULL,'2025-11-13 16:48:11'),(60,'반여3동골목시장','부산광역시','부산광역시 해운대구 재반로256번길 8',NULL,NULL,NULL,'2025-11-13 16:48:11'),(61,'우동종합상가시장','부산광역시','부산광역시 해운대구 해운대로383번길 37-10',NULL,NULL,NULL,'2025-11-13 16:48:11'),(62,'재송시장','부산광역시','부산광역시 해운대구 재송1로60번길 16 서해재송아파트',NULL,NULL,NULL,'2025-11-13 16:48:11'),(63,'재송한마음시장','부산광역시','부산광역시 해운대구 재반로 81-20',NULL,NULL,NULL,'2025-11-13 16:48:11'),(64,'좌동재래시장','부산광역시','부산광역시 해운대구 좌동로91번길 20',NULL,NULL,NULL,'2025-11-13 16:48:11'),(65,'해운대시장','부산광역시','부산광역시 해운대구 구남로41번길 22-1',NULL,NULL,NULL,'2025-11-13 16:48:11');
/*!40000 ALTER TABLE `market` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_num` int NOT NULL AUTO_INCREMENT,
  `member_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `member_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Unknown',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `birth` date DEFAULT NULL,
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `national` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `member_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `member_grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'BUYER',
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`member_num`),
  UNIQUE KEY `member_id` (`member_id`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=219 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (6,'test_member_2024','테스트사용자','$2a$10$PNAFAhoVHPKgXp55yQ4RrOjCfdP4GK18lZrIPsRVJ.I2gdJzVvqWu',NULL,NULL,NULL,'010-1234-5678',NULL,'test2024@example.com','BUYER','2025-10-28 11:22:55'),(211,'seller01','김판매자','$2a$10$PNAFAhoVHPKgXp55yQ4RrOjCfdP4GK18lZrIPsRVJ.I2gdJzVvqWu',NULL,NULL,NULL,'010-1111-1111',NULL,'seller01@localmarket.com','SELLER','2025-10-29 16:46:03'),(212,'seller02','박상인','$2a$10$PNAFAhoVHPKgXp55yQ4RrOjCfdP4GK18lZrIPsRVJ.I2gdJzVvqWu',NULL,NULL,NULL,'010-2222-2222',NULL,'seller02@localmarket.com','SELLER','2025-10-29 16:46:03'),(213,'seller03','이사장','$2a$10$PNAFAhoVHPKgXp55yQ4RrOjCfdP4GK18lZrIPsRVJ.I2gdJzVvqWu',NULL,NULL,NULL,'010-3333-3333',NULL,'seller03@localmarket.com','SELLER','2025-10-29 16:46:03'),(214,'seller04','최점주','$2a$10$PNAFAhoVHPKgXp55yQ4RrOjCfdP4GK18lZrIPsRVJ.I2gdJzVvqWu',NULL,NULL,NULL,'010-4444-4444',NULL,'seller04@localmarket.com','SELLER','2025-10-29 16:46:03'),(215,'sunsin','이순신','$2a$10$PNAFAhoVHPKgXp55yQ4RrOjCfdP4GK18lZrIPsRVJ.I2gdJzVvqWu',NULL,NULL,NULL,'010-1111-2222','부산시 부산진구','sunsin@exam.com','BUYER','2025-10-31 14:34:58'),(216,'admin','관리자','$2a$10$PNAFAhoVHPKgXp55yQ4RrOjCfdP4GK18lZrIPsRVJ.I2gdJzVvqWu',NULL,NULL,NULL,'010-0000-0000','부산시 기장군','admin@localmarket.com','ADMIN','2025-11-01 09:55:54'),(217,'gildong','홍길동','$2a$10$PNAFAhoVHPKgXp55yQ4RrOjCfdP4GK18lZrIPsRVJ.I2gdJzVvqWu',NULL,NULL,NULL,'010-1111-1234','부산시 부산진구 범내골','exam123@exam.com','SELLER','2025-11-11 17:08:06'),(218,'admin2','관리자','$2a$10$PNAFAhoVHPKgXp55yQ4RrOjCfdP4GK18lZrIPsRVJ.I2gdJzVvqWu','2025-11-01','M','내국인','010-1111-2223','부산시 부산진구','admin2@exam.com','BUYER','2025-11-14 17:29:08');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT,
  `notice_title` varchar(200) NOT NULL,
  `notice_content` text NOT NULL,
  `hit_cnt` int DEFAULT '0',
  `like_cnt` int DEFAULT '0',
  `write_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `member_num` int NOT NULL,
  PRIMARY KEY (`notice_id`),
  KEY `member_num` (`member_num`),
  CONSTRAINT `notice_ibfk_1` FOREIGN KEY (`member_num`) REFERENCES `member` (`member_num`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (2,'테스트','테스트',4,0,'2025-11-07 12:19:39','2025-11-07 12:21:39',216),(3,'테스트11수정버전','테스트22',10,0,'2025-11-07 12:23:03','2025-11-14 12:08:55',216);
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `order_detail_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `store_id` int NOT NULL,
  `order_quantity` int NOT NULL,
  `order_price` decimal(10,2) NOT NULL,
  `cancel_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'NONE',
  `cancel_date` datetime DEFAULT NULL,
  `cancel_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`order_detail_id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `order_detail_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  CONSTRAINT `order_detail_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE,
  CONSTRAINT `order_detail_ibfk_3` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (2,16,54,5,1,25000.00,'NONE',NULL,NULL),(3,16,58,7,1,20000.00,'NONE',NULL,NULL),(4,16,53,4,1,8000.00,'NONE',NULL,NULL),(5,17,54,5,2,25000.00,'NONE',NULL,NULL),(6,17,58,7,2,20000.00,'NONE',NULL,NULL),(7,17,53,4,1,8000.00,'NONE',NULL,NULL),(8,18,54,5,2,25000.00,'NONE',NULL,NULL),(9,18,58,7,2,20000.00,'NONE',NULL,NULL),(10,18,53,4,2,8000.00,'NONE',NULL,NULL),(11,19,55,5,1,15000.00,'NONE',NULL,NULL),(12,20,51,8,50,35000.00,'NONE',NULL,NULL),(13,21,55,4,40,15000.00,'NONE',NULL,NULL);
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `member_num` int NOT NULL,
  `order_total_price` decimal(10,2) NOT NULL,
  `order_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `order_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `delivery_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `delivery_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `payment_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `payment_date` datetime DEFAULT NULL,
  `transaction_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `member_num` (`member_num`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`member_num`) REFERENCES `member` (`member_num`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,211,105000.00,'CONFIRMED','2025-10-29 19:46:05','서울시 강남구 테헤란로 456 (수정됨)','010-1111-2222','경비실에 맡겨주세요 (수정됨)','CARD','COMPLETED','2025-10-29 19:53:30','TXN20241029001_UPDATED'),(2,211,35000.00,'CANCELLED','2025-10-29 19:46:05','서울시 강남구 테헤란로 456','010-1111-2222','경비실에 맡겨주세요','KAKAO','CANCELLED',NULL,'TXN20241029002'),(5,211,105000.00,'PENDING','2025-10-29 19:46:42','서울시 강남구 테헤란로 123','010-1234-5678','문 앞에 두고 가세요','CARD','PENDING',NULL,'TXN20241029001'),(6,211,35000.00,'CONFIRMED','2025-10-29 19:46:42','서울시 강남구 테헤란로 456','010-1111-2222','경비실에 맡겨주세요','KAKAO','COMPLETED',NULL,'TXN20241029002'),(7,212,25000.00,'SHIPPED','2025-10-29 19:46:42','부산시 해운대구 마린시티 789','010-9876-5432','배송 전 전화 주세요','BANK','COMPLETED',NULL,'TXN20241029003'),(8,213,15000.00,'COMPLETED','2025-10-29 19:46:42','대구시 중구 동성로 321','010-5555-6666','직접 수령','NAVER','COMPLETED',NULL,'TXN20241029004'),(9,6,50000.00,'PENDING','2025-10-29 19:46:42','인천시 연수구 송도국제도시','010-7777-8888','부재 시 택배함에','CARD','PENDING',NULL,'TXN20241029005'),(11,6,35000.00,'PENDING','2025-10-29 19:50:59','서울시 강남구 테헤란로 123','010-1234-5678','문 앞에 두고 가세요','CARD','PENDING',NULL,'TXN20241029001'),(12,211,25000.00,'CONFIRMED','2025-10-29 19:50:59','부산시 해운대구 마린시티 456','010-9876-5432','배송 전 전화 주세요','KAKAO','COMPLETED',NULL,'TXN20241029002'),(13,6,35000.00,'PENDING','2025-10-29 19:53:30','서울시 강남구 테헤란로 123','010-1234-5678','문 앞에 두고 가세요','CARD','PENDING',NULL,'TXN20241029001'),(14,211,25000.00,'CONFIRMED','2025-10-29 19:53:30','부산시 해운대구 마린시티 456','010-9876-5432','배송 전 전화 주세요','KAKAO','COMPLETED',NULL,'TXN20241029002'),(16,215,53000.00,'CANCELLED','2025-11-11 12:29:41','부산 기장군 일광읍 해빛1로 5 1111','111-1111-1111','','bank','CANCELLED',NULL,NULL),(17,215,98000.00,'PENDING','2025-11-11 12:33:15','부산 기장군 일광읍 해빛1로 5 1111','111-1111-1111','','card','PENDING',NULL,NULL),(18,215,106000.00,'PENDING','2025-11-11 12:44:09','부산 기장군 일광읍 해빛1로 5 1111','111-1111-1111','문 앞에 놓아주세요','naver','PENDING',NULL,'TEST-1762832649178'),(19,215,15000.00,'COMPLETED','2025-11-11 13:29:21','부산 기장군 일광읍 해빛1로 5 1111','111-1111-1111','배송 전 연락 부탁드립니다','kakao','PENDING',NULL,'TEST-1762835361295'),(20,215,1750000.00,'RETURN','2025-11-13 11:14:04','부산 기장군 일광읍 해빛1로 5 111','111-1111-1111','문 앞에 놓아주세요','naver','PENDING',NULL,'TEST-1763000044186'),(21,215,600000.00,'SWAP','2025-11-13 11:20:58','부산 기장군 일광읍 해빛1로 5 1111','111-1111-1111','문 앞에 놓아주세요','kakao','PENDING',NULL,'TEST-1763000458422');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `product_price` decimal(10,2) NOT NULL,
  `product_amount` int DEFAULT '0',
  `product_filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `store_id` int NOT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`product_id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (42,'한우 등심 1kg',89000.00,25,'product_42_1762756182942.jpg',3,'2025-10-29 16:47:33','2025-11-10 15:29:44'),(43,'유기농 사과 5kg',35000.00,50,'product_43_1762756192230.jpg',8,'2025-10-29 16:47:33','2025-11-12 12:14:34'),(44,'전복 1마리',12000.00,30,'product_44_1762756208158.jpg',4,'2025-10-29 16:47:33','2025-11-10 15:30:09'),(45,'김치 1포기',8000.00,100,'product_45_1762756219585.jpg',9,'2025-10-29 16:47:33','2025-11-12 12:20:13'),(46,'생선회 모듬',25000.00,15,'product_46_1762756230076.jpg',5,'2025-10-29 16:47:33','2025-11-10 15:30:31'),(47,'오징어 500g',15000.00,40,'product_47_1762756237944.jpg',4,'2025-10-29 16:47:33','2025-11-12 12:21:26'),(48,'한복 여성용',250000.00,10,'product_48_1762756247847.jpg',7,'2025-10-29 16:47:33','2025-11-12 12:21:47'),(49,'전통 찻잔 세트',45000.00,20,'product_49_1762756256193.jpg',6,'2025-10-29 16:47:33','2025-11-10 15:30:59'),(50,'한우 등심 1kg (수정됨)',90000.00,35,'product_50_1762756061990.jpg',3,'2025-10-29 16:50:19','2025-11-10 15:27:44'),(51,'유기농 사과 5kg',35000.00,50,'product_51_1762756094826.jpg',8,'2025-10-29 16:50:19','2025-11-12 12:14:23'),(52,'전복 1마리',12000.00,30,'product_52_1762756103248.jpg',4,'2025-10-29 16:50:19','2025-11-10 15:28:25'),(53,'김치 1포기',8000.00,100,'product_53_1762756113164.jpg',9,'2025-10-29 16:50:19','2025-11-12 12:20:32'),(54,'생선회 모듬',25000.00,15,'product_54_1762756121597.jpg',5,'2025-10-29 16:50:19','2025-11-10 15:28:43'),(55,'오징어 500g',15000.00,0,'product_55_1762756130243.jpg',4,'2025-10-29 16:50:19','2025-11-13 11:20:58'),(56,'한복 여성용',250000.00,10,'product_56_1762756139818.jpg',7,'2025-10-29 16:50:19','2025-11-12 12:21:56'),(57,'전통 찻잔 세트',45000.00,20,'product_57_1762756152484.jpg',6,'2025-10-29 16:50:19','2025-11-10 15:29:14'),(58,'내복(남성용)',20000.00,100,'product_58_1762759027763.jpg',7,'2025-11-10 16:17:07','2025-11-10 16:17:07'),(59,'동대문 히야 루돌프 키링',2000.00,1000,'product_59_1763098993793.jpg',10,'2025-11-14 14:43:13','2025-11-14 14:43:13');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store`
--

DROP TABLE IF EXISTS `store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store` (
  `store_id` int NOT NULL AUTO_INCREMENT,
  `store_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `store_index` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `store_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `store_filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `market_id` int NOT NULL,
  `member_num` int DEFAULT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`store_id`),
  KEY `market_id` (`market_id`),
  KEY `member_num` (`member_num`),
  CONSTRAINT `store_ibfk_1` FOREIGN KEY (`market_id`) REFERENCES `market` (`market_id`) ON DELETE CASCADE,
  CONSTRAINT `store_ibfk_2` FOREIGN KEY (`member_num`) REFERENCES `member` (`member_num`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store`
--

LOCK TABLES `store` WRITE;
/*!40000 ALTER TABLE `store` DISABLE KEYS */;
INSERT INTO `store` VALUES (3,'김씨 한우전문점','최고급 한우와 유기농 채소를 판매하는 전문점입니다.','육류','store_3_1762753529774.jpg',1,211,'2025-10-29 16:46:16'),(4,'박씨 수산물','신선한 해산물과 수산물을 취급하는 가게입니다.','수산물','store_4_1762753587878.jpg',2,212,'2025-10-29 16:46:16'),(5,'이씨 한식당','전통 한식과 김치류를 전문으로 하는 음식점입니다.','음식점','store_5_1762754789662.jpg',3,213,'2025-10-29 16:46:16'),(6,'최씨 전통공예','한복과 전통 찻잔 등 전통공예품을 판매합니다.','잡화','store_6_1762754819834.jpg',4,214,'2025-10-29 16:46:16'),(7,'다홍','B동 111호','의류','store_7_1762753516344.jpg',2,217,'2025-11-10 12:49:33'),(8,'길동이네 과일가게','B동 112호','농산물','store_new_1762917251301.jpg',2,217,'2025-11-12 12:14:11'),(9,'길동이네 반찬가게','B동 113호','식품','store_new_1762917595774.jpg',2,217,'2025-11-12 12:19:56'),(10,'동대문히야가게','B동 5129호','잡화','store_new_1763098954719.jpg',2,217,'2025-11-14 14:42:35');
/*!40000 ALTER TABLE `store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `view_all_favorites`
--

DROP TABLE IF EXISTS `view_all_favorites`;
/*!50001 DROP VIEW IF EXISTS `view_all_favorites`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_all_favorites` AS SELECT 
 1 AS `favorite_id`,
 1 AS `member_num`,
 1 AS `target_type`,
 1 AS `target_id`,
 1 AS `created_date`,
 1 AS `name`,
 1 AS `location`,
 1 AS `type`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_cancelled_orders`
--

DROP TABLE IF EXISTS `view_cancelled_orders`;
/*!50001 DROP VIEW IF EXISTS `view_cancelled_orders`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_cancelled_orders` AS SELECT 
 1 AS `order_detail_id`,
 1 AS `order_id`,
 1 AS `member_num`,
 1 AS `order_date`,
 1 AS `product_name`,
 1 AS `order_quantity`,
 1 AS `order_price`,
 1 AS `store_name`,
 1 AS `cancel_status`,
 1 AS `cancel_date`,
 1 AS `cancel_reason`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_market_favorite_stats`
--

DROP TABLE IF EXISTS `view_market_favorite_stats`;
/*!50001 DROP VIEW IF EXISTS `view_market_favorite_stats`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_market_favorite_stats` AS SELECT 
 1 AS `market_id`,
 1 AS `market_name`,
 1 AS `market_local`,
 1 AS `favorite_count`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_market_favorites`
--

DROP TABLE IF EXISTS `view_market_favorites`;
/*!50001 DROP VIEW IF EXISTS `view_market_favorites`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_market_favorites` AS SELECT 
 1 AS `favorite_id`,
 1 AS `member_num`,
 1 AS `created_date`,
 1 AS `market_id`,
 1 AS `market_name`,
 1 AS `market_local`,
 1 AS `market_address`,
 1 AS `market_introduce`,
 1 AS `market_filename`,
 1 AS `market_URL`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_member_favorite_stats`
--

DROP TABLE IF EXISTS `view_member_favorite_stats`;
/*!50001 DROP VIEW IF EXISTS `view_member_favorite_stats`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_member_favorite_stats` AS SELECT 
 1 AS `member_num`,
 1 AS `market_count`,
 1 AS `store_count`,
 1 AS `total_count`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_order_details`
--

DROP TABLE IF EXISTS `view_order_details`;
/*!50001 DROP VIEW IF EXISTS `view_order_details`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_order_details` AS SELECT 
 1 AS `order_detail_id`,
 1 AS `order_id`,
 1 AS `member_num`,
 1 AS `order_date`,
 1 AS `order_status`,
 1 AS `product_name`,
 1 AS `order_quantity`,
 1 AS `order_price`,
 1 AS `store_name`,
 1 AS `market_name`,
 1 AS `cancel_status`,
 1 AS `cancel_date`,
 1 AS `cancel_reason`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_store_favorite_stats`
--

DROP TABLE IF EXISTS `view_store_favorite_stats`;
/*!50001 DROP VIEW IF EXISTS `view_store_favorite_stats`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_store_favorite_stats` AS SELECT 
 1 AS `store_id`,
 1 AS `store_name`,
 1 AS `store_category`,
 1 AS `market_name`,
 1 AS `market_local`,
 1 AS `favorite_count`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_store_favorites`
--

DROP TABLE IF EXISTS `view_store_favorites`;
/*!50001 DROP VIEW IF EXISTS `view_store_favorites`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_store_favorites` AS SELECT 
 1 AS `favorite_id`,
 1 AS `member_num`,
 1 AS `created_date`,
 1 AS `store_id`,
 1 AS `store_name`,
 1 AS `store_category`,
 1 AS `market_name`,
 1 AS `market_local`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `view_all_favorites`
--

/*!50001 DROP VIEW IF EXISTS `view_all_favorites`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = euckr */;
/*!50001 SET character_set_results     = euckr */;
/*!50001 SET collation_connection      = euckr_korean_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_all_favorites` AS select `f`.`favorite_id` AS `favorite_id`,`f`.`member_num` AS `member_num`,`f`.`target_type` AS `target_type`,`f`.`target_id` AS `target_id`,`f`.`created_date` AS `created_date`,`m`.`market_name` AS `name`,`m`.`market_local` AS `location`,'MARKET' AS `type` from (`favorite` `f` join `market` `m` on((`f`.`target_id` = `m`.`market_id`))) where (`f`.`target_type` = 'MARKET') union all select `f`.`favorite_id` AS `favorite_id`,`f`.`member_num` AS `member_num`,`f`.`target_type` AS `target_type`,`f`.`target_id` AS `target_id`,`f`.`created_date` AS `created_date`,`s`.`store_name` AS `name`,`m`.`market_local` AS `location`,'STORE' AS `type` from ((`favorite` `f` join `store` `s` on((`f`.`target_id` = `s`.`store_id`))) join `market` `m` on((`s`.`market_id` = `m`.`market_id`))) where (`f`.`target_type` = 'STORE') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_cancelled_orders`
--

/*!50001 DROP VIEW IF EXISTS `view_cancelled_orders`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = euckr */;
/*!50001 SET character_set_results     = euckr */;
/*!50001 SET collation_connection      = euckr_korean_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_cancelled_orders` AS select `od`.`order_detail_id` AS `order_detail_id`,`od`.`order_id` AS `order_id`,`o`.`member_num` AS `member_num`,`o`.`order_date` AS `order_date`,`p`.`product_name` AS `product_name`,`od`.`order_quantity` AS `order_quantity`,`od`.`order_price` AS `order_price`,`s`.`store_name` AS `store_name`,`od`.`cancel_status` AS `cancel_status`,`od`.`cancel_date` AS `cancel_date`,`od`.`cancel_reason` AS `cancel_reason` from (((`order_detail` `od` join `orders` `o` on((`od`.`order_id` = `o`.`order_id`))) join `product` `p` on((`od`.`product_id` = `p`.`product_id`))) join `store` `s` on((`od`.`store_id` = `s`.`store_id`))) where (`od`.`cancel_status` <> 'NONE') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_market_favorite_stats`
--

/*!50001 DROP VIEW IF EXISTS `view_market_favorite_stats`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = euckr */;
/*!50001 SET character_set_results     = euckr */;
/*!50001 SET collation_connection      = euckr_korean_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_market_favorite_stats` AS select `m`.`market_id` AS `market_id`,`m`.`market_name` AS `market_name`,`m`.`market_local` AS `market_local`,count(`f`.`favorite_id`) AS `favorite_count` from (`market` `m` left join `favorite` `f` on(((`m`.`market_id` = `f`.`target_id`) and (`f`.`target_type` = 'MARKET')))) group by `m`.`market_id`,`m`.`market_name`,`m`.`market_local` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_market_favorites`
--

/*!50001 DROP VIEW IF EXISTS `view_market_favorites`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = euckr */;
/*!50001 SET character_set_results     = euckr */;
/*!50001 SET collation_connection      = euckr_korean_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_market_favorites` AS select `f`.`favorite_id` AS `favorite_id`,`f`.`member_num` AS `member_num`,`f`.`created_date` AS `created_date`,`m`.`market_id` AS `market_id`,`m`.`market_name` AS `market_name`,`m`.`market_local` AS `market_local`,`m`.`market_address` AS `market_address`,`m`.`market_introduce` AS `market_introduce`,`m`.`market_filename` AS `market_filename`,`m`.`market_URL` AS `market_URL` from (`favorite` `f` join `market` `m` on((`f`.`target_id` = `m`.`market_id`))) where (`f`.`target_type` = 'MARKET') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_member_favorite_stats`
--

/*!50001 DROP VIEW IF EXISTS `view_member_favorite_stats`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = euckr */;
/*!50001 SET character_set_results     = euckr */;
/*!50001 SET collation_connection      = euckr_korean_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_member_favorite_stats` AS select `favorite`.`member_num` AS `member_num`,count((case when (`favorite`.`target_type` = 'MARKET') then 1 end)) AS `market_count`,count((case when (`favorite`.`target_type` = 'STORE') then 1 end)) AS `store_count`,count(0) AS `total_count` from `favorite` group by `favorite`.`member_num` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_order_details`
--

/*!50001 DROP VIEW IF EXISTS `view_order_details`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = euckr */;
/*!50001 SET character_set_results     = euckr */;
/*!50001 SET collation_connection      = euckr_korean_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_order_details` AS select `od`.`order_detail_id` AS `order_detail_id`,`od`.`order_id` AS `order_id`,`o`.`member_num` AS `member_num`,`o`.`order_date` AS `order_date`,`o`.`order_status` AS `order_status`,`p`.`product_name` AS `product_name`,`od`.`order_quantity` AS `order_quantity`,`od`.`order_price` AS `order_price`,`s`.`store_name` AS `store_name`,`m`.`market_name` AS `market_name`,`od`.`cancel_status` AS `cancel_status`,`od`.`cancel_date` AS `cancel_date`,`od`.`cancel_reason` AS `cancel_reason` from ((((`order_detail` `od` join `orders` `o` on((`od`.`order_id` = `o`.`order_id`))) join `product` `p` on((`od`.`product_id` = `p`.`product_id`))) join `store` `s` on((`od`.`store_id` = `s`.`store_id`))) join `market` `m` on((`s`.`market_id` = `m`.`market_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_store_favorite_stats`
--

/*!50001 DROP VIEW IF EXISTS `view_store_favorite_stats`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = euckr */;
/*!50001 SET character_set_results     = euckr */;
/*!50001 SET collation_connection      = euckr_korean_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_store_favorite_stats` AS select `s`.`store_id` AS `store_id`,`s`.`store_name` AS `store_name`,`s`.`store_category` AS `store_category`,`m`.`market_name` AS `market_name`,`m`.`market_local` AS `market_local`,count(`f`.`favorite_id`) AS `favorite_count` from ((`store` `s` join `market` `m` on((`s`.`market_id` = `m`.`market_id`))) left join `favorite` `f` on(((`s`.`store_id` = `f`.`target_id`) and (`f`.`target_type` = 'STORE')))) group by `s`.`store_id`,`s`.`store_name`,`s`.`store_category`,`m`.`market_name`,`m`.`market_local` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_store_favorites`
--

/*!50001 DROP VIEW IF EXISTS `view_store_favorites`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = euckr */;
/*!50001 SET character_set_results     = euckr */;
/*!50001 SET collation_connection      = euckr_korean_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_store_favorites` AS select `f`.`favorite_id` AS `favorite_id`,`f`.`member_num` AS `member_num`,`f`.`created_date` AS `created_date`,`s`.`store_id` AS `store_id`,`s`.`store_name` AS `store_name`,`s`.`store_category` AS `store_category`,`m`.`market_name` AS `market_name`,`m`.`market_local` AS `market_local` from ((`favorite` `f` join `store` `s` on((`f`.`target_id` = `s`.`store_id`))) join `market` `m` on((`s`.`market_id` = `m`.`market_id`))) where (`f`.`target_type` = 'STORE') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-18 15:04:42
