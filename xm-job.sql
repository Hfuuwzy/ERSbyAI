/*
 Navicat Premium Dump SQL

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50720 (5.7.20)
 Source Host           : localhost:3306
 Source Schema         : xm-job

 Target Server Type    : MySQL
 Target Server Version : 50720 (5.7.20)
 File Encoding         : 65001

 Date: 10/05/2025 16:21:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='管理员表';

-- ----------------------------
-- Records of admin
-- ----------------------------
BEGIN;
INSERT INTO `admin` (`id`, `username`, `password`, `name`, `avatar`, `role`, `phone`, `email`) VALUES (1, 'admin', 'admin', '管理员', 'http://localhost:9090/files/download/1721114905635-柴犬.jpeg', 'ADMIN', '18899990011', 'admin2@xm.com');
COMMIT;

-- ----------------------------
-- Table structure for advertise
-- ----------------------------
DROP TABLE IF EXISTS `advertise`;
CREATE TABLE `advertise` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '主图',
  `position_id` int(11) DEFAULT NULL COMMENT '职位ID',
  `location` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '位置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='广告信息表';

-- ----------------------------
-- Records of advertise
-- ----------------------------
BEGIN;
INSERT INTO `advertise` (`id`, `img`, `position_id`, `location`) VALUES (1, 'http://localhost:9090/files/download/1741762686335-center.png', 2, '中心大图');
INSERT INTO `advertise` (`id`, `img`, `position_id`, `location`) VALUES (2, 'http://localhost:9090/files/download/1741762938302-center-down.png', 3, '中心小图');
INSERT INTO `advertise` (`id`, `img`, `position_id`, `location`) VALUES (3, 'http://localhost:9090/files/download/1741763728030-right-up.png', 6, '右侧大图');
INSERT INTO `advertise` (`id`, `img`, `position_id`, `location`) VALUES (4, 'http://localhost:9090/files/download/1741763760897-right-up.png', 10, '左侧大图');
INSERT INTO `advertise` (`id`, `img`, `position_id`, `location`) VALUES (5, 'http://localhost:9090/files/download/1741763768129-right-down.png', 7, '右侧小图');
INSERT INTO `advertise` (`id`, `img`, `position_id`, `location`) VALUES (6, 'http://localhost:9090/files/download/1741763798751-right-down.png', 12, '左侧小图');
COMMIT;

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` int(11) DEFAULT NULL COMMENT '学生ID',
  `position_id` int(11) DEFAULT NULL COMMENT '岗位ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='收藏信息表';

-- ----------------------------
-- Records of collect
-- ----------------------------
BEGIN;
INSERT INTO `collect` (`id`, `student_id`, `position_id`) VALUES (1, 1, 3);
INSERT INTO `collect` (`id`, `student_id`, `position_id`) VALUES (2, 1, 4);
INSERT INTO `collect` (`id`, `student_id`, `position_id`) VALUES (4, 1, 6);
INSERT INTO `collect` (`id`, `student_id`, `position_id`) VALUES (5, 2, 3);
INSERT INTO `collect` (`id`, `student_id`, `position_id`) VALUES (6, 2, 7);
INSERT INTO `collect` (`id`, `student_id`, `position_id`) VALUES (7, 2, 6);
INSERT INTO `collect` (`id`, `student_id`, `position_id`) VALUES (8, 1, 10);
INSERT INTO `collect` (`id`, `student_id`, `position_id`) VALUES (9, 1, 5);
COMMIT;

-- ----------------------------
-- Table structure for employ
-- ----------------------------
DROP TABLE IF EXISTS `employ`;
CREATE TABLE `employ` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'logo',
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  `city` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '详细地址',
  `industry_id` int(11) DEFAULT NULL COMMENT '行业id',
  `scale` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规模',
  `stage` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '融资',
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='企业信息表';

-- ----------------------------
-- Records of employ
-- ----------------------------
BEGIN;
INSERT INTO `employ` (`id`, `username`, `password`, `name`, `avatar`, `role`, `city`, `address`, `industry_id`, `scale`, `stage`, `status`) VALUES (1, 'meituan', '123456', '美团', 'http://localhost:9090/files/download/1739774495288-meituan.jpg', 'EMPLOY', '北京市', '北京市朝阳区美团', 10, '10000人以上', '已上市', '待审核');
INSERT INTO `employ` (`id`, `username`, `password`, `name`, `avatar`, `role`, `city`, `address`, `industry_id`, `scale`, `stage`, `status`) VALUES (2, 'pdd', '123456', '拼多多', 'http://localhost:9090/files/download/1739775298601-pinduoduo.jpg', 'EMPLOY', '上海市', '上海市长宁区金虹桥国际中心娄山关路533号', 11, '10000人以上', '已上市', '审核通过');
INSERT INTO `employ` (`id`, `username`, `password`, `name`, `avatar`, `role`, `city`, `address`, `industry_id`, `scale`, `stage`, `status`) VALUES (3, 'BOSS', '123456', 'BOSS直聘', 'http://localhost:9090/files/download/1739775380199-boss.jpg', 'EMPLOY', '北京市', '北京市朝阳区', 9, '10000人以上', '已上市', '审核通过');
INSERT INTO `employ` (`id`, `username`, `password`, `name`, `avatar`, `role`, `city`, `address`, `industry_id`, `scale`, `stage`, `status`) VALUES (4, 'xunfei', '123456', '科大讯飞', 'http://localhost:9090/files/download/1739775431332-kedaxunfei.jpg', 'EMPLOY', '合肥市', '合肥市蜀山区科大讯飞股份有限公司望江西路666号', 12, '10000人以上', '已上市', '审核通过');
INSERT INTO `employ` (`id`, `username`, `password`, `name`, `avatar`, `role`, `city`, `address`, `industry_id`, `scale`, `stage`, `status`) VALUES (5, 'huawei', '123456', '华为', 'http://localhost:9090/files/download/1741092786293-huawei.jpg', 'EMPLOY', '上海市', '上海市浦东新区金桥经济技术开发区新金桥路2223号', 12, '10000人以上', '不需要融资', '待审核');
COMMIT;

-- ----------------------------
-- Table structure for industry
-- ----------------------------
DROP TABLE IF EXISTS `industry`;
CREATE TABLE `industry` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '行业名称',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '行业描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='行业信息表';

-- ----------------------------
-- Records of industry
-- ----------------------------
BEGIN;
INSERT INTO `industry` (`id`, `name`, `content`) VALUES (1, '计算机硬件', '这是计算机硬件');
INSERT INTO `industry` (`id`, `name`, `content`) VALUES (2, '人工智能/AI', '这是人工智能/AI');
INSERT INTO `industry` (`id`, `name`, `content`) VALUES (3, '大数据/云计算', '这是大数据/云计算');
INSERT INTO `industry` (`id`, `name`, `content`) VALUES (4, '在线教育', '这是在线教育');
INSERT INTO `industry` (`id`, `name`, `content`) VALUES (5, '游戏', '这是游戏');
INSERT INTO `industry` (`id`, `name`, `content`) VALUES (6, '半导体/芯片', '这是半导体/芯片');
INSERT INTO `industry` (`id`, `name`, `content`) VALUES (8, '电子/硬件开发', '这是电子/硬件开发');
INSERT INTO `industry` (`id`, `name`, `content`) VALUES (9, '社交网络/媒体', '这是社交网络/媒体');
INSERT INTO `industry` (`id`, `name`, `content`) VALUES (10, '生活服务(o2o)', '这是生活服务(o2o)');
INSERT INTO `industry` (`id`, `name`, `content`) VALUES (11, '互联网/电子商务', '这是互联网/电子商务');
INSERT INTO `industry` (`id`, `name`, `content`) VALUES (12, '计算机软件', '这是计算机软件');
COMMIT;

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '公告标题',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '公告内容',
  `time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='系统公告表';

-- ----------------------------
-- Records of notice
-- ----------------------------
BEGIN;
INSERT INTO `notice` (`id`, `title`, `content`, `time`) VALUES (4, '公告1', '85Boss招聘了', '2025-04-07 19:24:30');
COMMIT;

-- ----------------------------
-- Table structure for position
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职位名称',
  `employ_id` int(11) DEFAULT NULL COMMENT '企业ID',
  `industry_id` int(11) DEFAULT NULL COMMENT '行业ID',
  `type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '求职类型',
  `experience` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工作经验',
  `salary` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '薪资待遇',
  `education` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学历要求',
  `tags` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职位标签',
  `content` longtext COLLATE utf8mb4_unicode_ci COMMENT '职位描述',
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '职位状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='职位信息表';

-- ----------------------------
-- Records of position
-- ----------------------------
BEGIN;
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (1, '前端软件开发工程师', 1, 10, '全职', '应届生', '10-20k', '硕士', '核心部门,福利待遇好,五险一金,节假日双休', '<p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\">岗位职责<br>1. 负责品牌大客在线点餐、品牌营销、会员、预订服务等B、C端的移动端、PC端业务产品开发，以及把控需求评估、方案设计及核心功能实现等重要环节，保障按时保质地交付；<br>2. 应用并共建诸如框架、组件、DevOps等大前端工程化产品，持续提升团队交付效率，保障产品工程质量；<br>3. 迎难而上，保持好奇和学习心态，促进团队共同成长。<br>4. 关注业界大前端动态，探索包括但不限于低代码、自动化等方向，持续迭代技术方案及技术规划。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>岗位基本需求<br>1. 2年以上前端开发经验，主导或参与过复杂系统或项目的建设；<br>2. 有良好的技术储备，了解业界常见类库、框架、设计模式、解决方案，并在某一方向有深入的研究；<br>3. 熟练运用前端工程化技术，如Node.js、Webpack/Vite代码构建与优化、持续集成与部署，改善团队研发效率；<br>4. 具备一定的项目管理能力，能承担中大型项目，通过定义清晰的目标、合理的计划、有效的过程管理、控制风险最终达成项目目标；<br>5. 具备良好的团队协作沟通能力，有较强的逻辑思维能力，善于分析、归纳、解决问题，持续学习和总结，自我迭代；<br>6. 有较强的产品和业务理解，能协同产品及业务团队持续推动产品优化、达成业务目标。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>具备以下者优先<br>1. 有ReactNative跨端开发经验者优先；<br>2. 热爱互联网和新技术，具有极强的快速学习能力，研究过优秀开源软件的源码并有心得者优先；<br>3. 在工程化、组件库、跨端等方向有深入研究及实践者优先；<br>4. 对研发体系稳定、质量、效率、性能等各方面有系统化认知，并有长远的规划能力者加分。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>岗位亮点<br>1. 到店餐饮核心业务的前端团队，聚焦品牌大客业务领域的B、C端全链路，可以快速提升你的业务理解力和技术赋能业务能力；<br>2. 大团队中既有行业大会讲师、社区极客，也在持续不断地培养和引导校招新人，有完整的培养体系和广阔的成长空间。<br>3. 熟悉核心业务的发展过程，迭代认知，沉淀方法。</span></p>', '审核通过');
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (2, '高级Java开发工程师', 1, 10, '全职', '1到3年', '10-20k', '本科', '个人成长,晋升通道,福利待遇好', '<p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\">部门介绍<br>企业平台研发部作为美团内部企业应用的技术研发团队，肩负着帮助美团运营得更好，经营得更好的使命。我们通过提供多样的工具、系统和平台，致力于打造数字化、智能化的财务、供应链、人力资源等信息化体系，帮助美团职能及业务团队完成精细化管理和高效运营。同时，为美团全体员工提供安全高效的协同办公平台，推动组织效能提升，实现美团组织运营的数字化升级。<br>我们期待优秀的你一起加入，共同建设数据化、智能化的企业研发平台。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>岗位职责<br>1.负责美团公司事务平台廉正与阳光职场方向的系统设计与研发；<br>2.负责技术规划和架构设计，结合业务阶段，持续优化演进技术，提高系统能力复用性、扩展性；<br>3.以业务和技术价值为导向，能利用科学的方法进行指标定义、收集、分析，从而进行成果的衡量，对工作改进和提升提供指导；<br>4.指导初级工程师，促进团队共同成长和团队高效率运转；<br>5.跟进研究前沿技术和行业最佳实践，给团队带来创新突破。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>岗位基本要求<br>1.三年及以上相关工作经验，具备良好的沟通技巧、较强自驱力、较强结果导向的心智；<br>2.精通Java及面向对象设计开发，熟悉JDK源码及JVM基本原理；<br>3.熟悉常见设计模式，精通Spring，MyBatis等主流开源框架；<br>4.熟悉MySQL应用开发，熟悉数据库原理和常用性能优化技术，以及NoSQL使用场景；<br>5.熟悉HTTPS协议、搜索引擎、缓存、RPC，消息等中间件，并且有相关实践经验；<br>6.重视系统建模方法论，对复杂业务分析、系统建模感兴趣，对系统设计有高标准；<br>7.熟悉DDD、微服务相关理论，能通过模块化、服务化、平台化的建设，降低系统间依赖，提升系统稳定性、可扩展性，提升开发效率。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>具备以下条件优先<br>1.参与过大型分布式系统设计开发，对高可用、高性能、可扩展、可伸缩系统有深刻的理解；<br>2.具有敏捷开发经验，实践过持续集成和自动化；<br>3.有过复杂系统或领域驱动设计经验；<br>4.参与过优秀开源项目。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>岗位亮点<br>1.良好的团队工作氛围，同事间乐于互助，共同成长；<br>2.团队有丰富的互联网技术储备和沉淀，能够在工作中快速提升核心竞争力；<br>3.面向高复杂度的公司级业务，需要跨业务线的沟通协调能力，助于从复杂的逻辑中抽象解决问题的方法，提高自身推进与解决问题能力。</span></p>', '审核通过');
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (3, '高级C语言工程师', 5, 12, '全职', '3到5年', '20-50k', '硕士', '高薪,晋升通道,岗位竞争力', '<p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\">1、本科及以上学历，计算机、通信、自动化、电子信息相关专业毕业；<br>2、熟练掌握c/c++语言，熟悉Windows/Linux下常用调试方法和系统编程；<br>3、熟悉TCP/IP协议，熟悉socket编程；<br>4、熟悉常用代码管理工具SVN、CVS等，有良好的代码编写能力和习惯；<br>5、有Windows或Linux下网络通信产品开发经验优先考虑；<br>6、有较强的团队合作精神，主动性强，能够承受较大压力。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>岗位职责：<br>1、负责量子通信产品系统软件的开发设计和维护；<br>2、负责开发过程中相关文档的编写和评审；<br>3、参与产品的调试与后期维护。</span></p>', '审核通过');
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (4, '华为云测试工程师', 5, 12, '全职', '经验不限', '10-20k', '本科', '自动化测试,白盒测试,Shell', '<p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\">华为云数据库相关岗位，此岗位为OD岗位，入职后满足条件可转华为。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>【岗位职责】<br>1. 负责华为云数据库的测试设计、测试验证与交付工作,保证产品高质量发布。<br>2. 负责华为云数据库的测试架构设计和开发,支撑自动化测试,提升测试质量和效率。<br>3. 支撑负责华为云数据库的性能/过载测试、可靠性/混沌测试、安全测试、客户化测试等。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>【技能要求】<br>1. 熟悉测试基本的技术和方法,包括测试设计、测试执行、测试分析、测试工具、测试仿真、故障注入、自动化等。<br>2. 熟练掌握测试需求分析和设计方法,熟悉黑白盒测试流程,熟悉常见缺陷管理工具。<br>3. 熟悉自动化脚本的开发,熟练使用常用的自动化测试框架,具备独立的问题定位分析能力。<br>4. 有强烈的责任心和使命感,具备良好的沟通能力和团队合作意识。<br>5. 至少熟悉一种编程语言C、C++、java、Python、Go、shell等。</span></p>', '审核通过');
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (5, '高级运营', 2, 11, '全职', '3到5年', '20-50k', '硕士', '五险一金,零食水果,福利待遇好,薪资高', '<p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">岗位职责： </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1、社区团购-多多买菜业务，通过上门拜访，与商户（便利店、商铺等）谈判并达成合作； </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2、负责区域内客情维护，引导团长完成分享与推广任务，提高团长经营收入。 </span></p><p style=\"line-height: 2;\"><br></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">任职要求：</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1、学历不限，工作区域可就近安排；</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2、有销售经验，尤其有互联网、美团优选、淘菜菜，京喜拼拼、兴盛优选、十荟团、同程生活等社区团购经验者优先考虑； </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">3、适应互联网的快工作节奏，极致追求结果达成。 目前岗位在招区域:陈村 北滘 伦教 大良 龙江 </span></p><p style=\"line-height: 2;\"><br></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">岗位要求: </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1.自备电动车 ，在家附近上班，不包吃住 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2.名下没有营业执照 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">3.善于沟通，乐于沟通，敢陌拜 </span></p><p style=\"line-height: 2;\"><br></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">工作内容：</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">维护区域内的团长，加团长微信，做好微信群运营，前期每天上门拜访20个，前期有师傅带 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">工作时间：</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">9点到6点，做六休一，外勤打卡 不用到公司报道 底薪3700+绩效12000，一个月平均8000-15000 如果满足以上条件，可以发简历和电话过来，进一步沟通</span></p>', '审核通过');
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (6, 'Java软件开发工程师', 2, 11, '全职', '应届生', '20-50k', '硕士', 'Java,Springboot,Vue,福利待遇好,薪资高', '<p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">岗位职责： </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1、负责仓储供应链基础功能的研发； </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2、学习理解能力强，有能力主导系统的分析，架构设计，核心功能开发； </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">3、对系统稳定性和潜在问题高度敏感，主动发现潜在风险，并积极推动落地解决； </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">4、较好的代码规范和文档规范，保证项目的进度和质量。 </span></p><p style=\"line-height: 2;\"><br></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">任职要求： </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1、计算机软件或相关专业本科及以上学历优先，</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">5年以上分布式系统开发经验； </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2、Java基础扎实，理解并发，IO，对JVM原理和调优有一定的经验； </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">3、熟悉web开发常用开发框架和工具，spring，spring boot，数据库，缓存，ES，flink等，有较强的编程能力和编程经验； </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">4、熟悉分布式系统的设计和应用，有高并发系统的开发和调优经验，有长期线上系统稳定性治理和维护的经验； </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">5、对技术有强烈的兴趣，学习能力强，具备较好的沟通能力，对线上系统能保持敬畏之心。 </span></p><p style=\"line-height: 2;\"><br></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">加分项： 有主导大型项目开发经验者优先</span></p>', '审核通过');
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (7, '前端开发', 4, 12, '全职', '1到3年', '10-20k', '本科', '前端,css,JavaScript,Vue', '<p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">关于我们: </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">我们是美团外卖成都研发中心前端组，在这里我们通过在业务中的不断探索和实践，期望通过配置化的战略来提升中后台系统的前端研发效率，建立行业标准，成为行业标杆。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">关于你: </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">期望你是一位对前端充满激情和富有经验的工程师，有永远向上的心态和积极努力的态度。 我们在成都等你，春天去龙泉山看桃花，夏天去青城山消暑，秋天去四姑娘山看雪山红叶，冬天去吃最正宗的火锅。 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">岗位职责: </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">●负责美团外卖CRM相关业务的前端技术方案设计、需求把控、功能开发，将业务需求拆解细化并实施; </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">●探索和实践针对中后台系统的前端体系化研发方案，提升前端研发效率; </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">●促进团队与产品及相关团队的密切合作。 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">岗位要求: </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1)岗位基本要求: </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">●3年及以上互联网研发工作经验; </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">●具有扎实的计算机科学功底，扎实的编程基础和数据结构算法基础，较强的编程能力和问题解决能力; 对前端相关框架有深入研究，以及前端工程化经验，并在大中型项目中有过实际应用和调优经历; </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">●对前端的基础知识，CSS、 JavaScript、 性能优化、网络原理等有深入的理解; </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">●善于交流，有良好的团队合作精神和协调沟通能力，有与产品、后端、移动端等多方密切配合的经验和意识; </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">●全日制统招本科及以上学历。 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2)具备以下者优先: </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">●热爱互联网和新技术，具有极强的快速学习能力，研究过优秀开源软件的源码并有心得者优先; </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">●有移动端或类似系统的研发经历; </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">●有“代码洁癖”、有极客精神。 欢迎有想法的同学私聊</span></p>', '审核通过');
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (8, 'Java后端开发', 4, 12, '全职', '1年以内', '5-10k', '本科', '福利待遇好,晋升空间大', '<p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">岗位职责： </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1、负责到家站外广告系统的相关设计和研发，包括但不限于DSP平台、广告投放引擎等的搭建和开发</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2、协同业务方探索到家商家广告在外部场景下的投放，支撑海量投放业务快速落地 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">3、支持广告投放在工程落地上的实践和调优，沉淀业务优秀实践，保障所有服务稳定运行 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">岗位基本要求： </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1、计算机相关专业，本科及以上学历 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2、三年以上的开发经验，精通Java后台服务研发 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">3、对技术有追求，具备较强的代码编写和问题解决能力 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">4、善于交流，有良好的团队合作精神和协调沟通能力，有强烈的责任心和主人翁意识 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">具备以下优先： </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1、有广告系统、推荐系统相关的工程开发经验者优先 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2、有海量分布式系统架构设计、优化经验者优先 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">3、熟悉hadoop, flink, storm, HBase, hive, spark, storm等分布式大数据处理技术优先 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">岗位亮点：</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\"> 广告业务是互联网重要的变现方式之一 广告投放系统承载亿级用户、百亿级流量，有海量数据和高并发的技术挑战 广告业务结合了海量的系统架构和推荐算法（是AI落地产生价值的重要场景） 部门介绍： 美团的使命是“帮大家吃得更好，生活更好”。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">作为一家生活服务电子商务平台，公司聚焦“Food +Platform”战略，以“吃”为核心，通过科技创新，和广大商户与各类合作伙伴一起，努力为消费者提供品质生活，推动生活服务业需求侧和供给侧数字化升级。 2018年9月20日，美团正式在港交所挂牌上市。美团将始终坚持以客户为中心，不断加大在科技研发方面的投入，更好承担社会责任，更多创造社会价值，与广大合作伙伴一起发展共赢。</span></p>', '审核通过');
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (9, '抖音直播内容运营', 4, 12, '全职', '1年以内', '5-10k', '本科', '抖音直播,千川投流,福利待遇好', '<p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\">岗位职责：<br>1、需要有抖音相关经验，其中直播投放相关经验者优先；<br>2、能够完成短视频创意、内容策划，独立撰稿及制作分镜脚本，优化与迭代渠道投放素材内容的表达，提升素材整体的爆款率和转化率；<br>3、负责短视频项目拍摄执行，跟进及反馈所负责项目制作进度，对产出结果负责；<br>4、熟悉媒体环境，研究行业热门的各类短视频作品，收集和分析行业视频创意(不限于种草、日常、直播预热等视频)会总结与方法论辅助提升团队短视频能力；<br>5、对项目有完善的策划能力，能独立完成短视频整体项目的方向把控和系列性内容策划。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>岗位要求：<br>1、3年左右工作经验，本科及以上学历编导、传媒、广播电视、新闻学等相关专业优先，能自编自导自剪优先；<br>2、对新媒体内容运营有一定认知，有较强的创造性、发散性思维能力，良好的策划能力、文案能力、沟通能力；<br>3、具备用户运营以及产品运营思维，营销思维强，销售导向；<br>4、有高度的责任心和自驱力执行力强能主动思考推动工作的优化。</span></p>', '审核通过');
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (10, '星火军团产品经理', 4, 12, '全职', '1到3年', '5-10k', '本科', 'B端产品,商业化,晋升通道', '<p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\">工作职责<br>1、负责代码大模型产品的商业化工作，根据产品矩阵及市场情况，制定商业化策略<br> 2、通过产品数据分析及市场洞察，驱动产品进行迭代更新，负责用户触达、转化、留存等生命周期的提升 <br>3、跟踪行业/竞品最新动向和分析政策趋势变化，挖掘商业化的创新及改进的潜在机会<br> 4、通过一些列商业化方案举措达成组织战略目标</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>任职要求<br>1、本科及以上学历，有产品商业化实践经验为佳 <br>2、具备较好的目标感及逻辑思维，商业化嗅觉灵敏，对数据敏感，视角多元，深刻理解用户需求，通过数据分析和挖掘制定商业化策略，不断扩展产品市场规模<br> 3、具备高度的责任心和良好的抗压能力、协同推进能力，善于学习和独立思考 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>此岗位为科大讯飞集团统一招聘岗位，人员通过简历筛选、笔试、初试、复试、终审等环节后录用。</span></p>', '审核通过');
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (11, '运营经理', 3, 9, '全职', '1到3年', '10-20k', '硕士', '待遇好,高速成长', '<p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">岗位诱惑：</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">高速成长，团队氛围好，待遇好，优秀员工平均工资都过万！ </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">岗位职责： </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1，想象一下你会遇到五星级酒店的经理人，身价几千万的连锁大客户，别具风格的网红餐厅。也有路边十平米得小店，……我们需要你根据客户需求，制定个性化的营销方案，与商户谈判并达成合作。 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2，开发及维护老客户，与各商户建立长期稳定的合作关系。 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">3，通过电话营销+上门拜访面销，执行公司销售政策，帮助客户做好效果，实现合作共赢。 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">4，与公司各部门有效配合，并快速有效的解决商户上线前后遇到的问题及突发事件，提高商家满意度。 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">岗位要求： </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1，硕士学历 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2，一年以上的销售经验，行业不限，其中有面对面销售，电商行业销售，互联网广告销售，广告从业经验者优先考虑，优秀的应届生可以择优录取 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">3，具备较强的人际沟通能力，及逻辑思维能力 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">4，认真负责，吃苦耐劳，对工作有激情，有上进心 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">5，热爱销售工作，有强烈的成功欲望和赚钱的企图心 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">薪酬福利： </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">1，美团铁军的履历，极高的行业认可度！ </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">2，互联网行业最优的销售及营销策划培训，你可以学到，餐饮从0到1的经典营销案例，网红爆款的如何打造…… </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">3，完善的培训机制，新人入职7天内即可进行培训，采取师徒制加主管直带双向管理培训，1个月内帮助你快速成长，签单！ </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">4，6-8k+薪酬绩效，规范透明，公司竞争的工作环境 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">5，完善的福利待遇，五险一金，分量足够的年终奖，各种补贴，体检，团建及合作精美的节日礼品 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">6，活动多，每月不低于2次团建，每个季度不定期的团建出游活动 </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">7，双休，我们是一群没有后台没有背景靠自己打拼的有为青年，平均年龄25岁，沟通0障碍，团队氛围好，如果你想赚钱，如果想有更好的发展，请加入我们！ </span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 14px;\">公司介绍： 这是高速发展得互联网公司 你将和有战斗力的团队和有激情的伙伴一起工作 这是一份付出和回报对等的工作 这是自由发展得公司 覆盖全国一二三线超过150个城市的工作地点，可供选择 你还等什么，跟我沟通，发我简历 期待你的加入！</span></p>', '审核通过');
INSERT INTO `position` (`id`, `name`, `employ_id`, `industry_id`, `type`, `experience`, `salary`, `education`, `tags`, `content`, `status`) VALUES (15, 'Java架构师', 5, 12, '全职', '5到10年', '20-50k', '博士', '架构师', '<p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\">【Java架构师】全职岗位，博士学历优先，5-10年经验，薪资20-50k。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>【岗位职责】<br>1. 主导企业级Java应用系统架构设计，制定高可用、高扩展性技术方案。<br>2. 负责核心业务系统技术选型与架构优化，设计分布式系统及微服务架构。<br>3. 制定开发规范与编码标准，指导研发团队进行系统模块设计和代码重构。<br>4. 攻克高并发、高负载场景下的技术难题，保障系统稳定性与性能指标。<br>5. 跟踪前沿技术发展趋势，推动架构持续演进和技术创新落地。</span></p><p style=\"line-height: 2;\"><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 15px;\"><br>【技能要求】<br>1. 精通Java生态体系，熟练掌握SpringCloud/Dubbo等微服务框架及原理。<br>2. 具备大型分布式系统架构设计经验，熟悉容器化、服务治理等技术体系。<br>3. 深入理解分布式数据库、消息队列、缓存等中间件的原理与应用场景。<br>4. 具有技术团队管理经验，能有效进行技术决策并推动架构方案实施。<br>5. 优秀的系统问题诊断能力，对性能调优和系统容灾设计有丰富经验。<br>6. 具备技术文档撰写能力，擅长跨部门技术沟通与方案宣讲。</span></p>', '待审核');
COMMIT;

-- ----------------------------
-- Table structure for resume
-- ----------------------------
DROP TABLE IF EXISTS `resume`;
CREATE TABLE `resume` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简历名称',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `sex` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `salary` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '期望薪资',
  `education` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '学历',
  `experience` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工作年限',
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `edu_exps` longtext COLLATE utf8mb4_unicode_ci COMMENT '教育经历',
  `work_exps` longtext COLLATE utf8mb4_unicode_ci COMMENT '工作经历',
  `pro_exps` longtext COLLATE utf8mb4_unicode_ci COMMENT '项目经验',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='简历信息表';

-- ----------------------------
-- Records of resume
-- ----------------------------
BEGIN;
INSERT INTO `resume` (`id`, `name`, `username`, `sex`, `salary`, `education`, `experience`, `phone`, `email`, `edu_exps`, `work_exps`, `pro_exps`, `user_id`) VALUES (1, '前端开发', '张三', '女', '5-40k', '本科', '应届生', '12345678910', 'shangsan@85Boss.com', '[{\"school\":\"中科大\",\"speciality\":\"电子信息工程\",\"education\":\"硕士\",\"start\":\"2025-02-24\",\"end\":\"2025-03-04\",\"course\":\"人工智能\"}]', '[]', '[]', 1);
INSERT INTO `resume` (`id`, `name`, `username`, `sex`, `salary`, `education`, `experience`, `phone`, `email`, `edu_exps`, `work_exps`, `pro_exps`, `user_id`) VALUES (3, 'Java', '张三', '男', '5-10k', '本科', '应届生', '11122233344', 'zhnagsan@85boss.com', '[{\"school\":\"合肥大学\",\"speciality\":\"软件工程\",\"education\":\"本科\",\"start\":\"2021-07-01\",\"course\":\"Java\"}]', NULL, NULL, 1);
INSERT INTO `resume` (`id`, `name`, `username`, `sex`, `salary`, `education`, `experience`, `phone`, `email`, `edu_exps`, `work_exps`, `pro_exps`, `user_id`) VALUES (4, 'Java后端简历', '张三', '男', '10-20k', '本科', '1年以内', '29915443522', 'zhangsan@pp.com', '[{\"school\":\"合肥工业大学\",\"speciality\":\"软件工程\",\"education\":\"本科\",\"start\":\"2020-07-01\",\"end\":\"2024-07-01\",\"course\":\"Java，软件工程，SpringBoot\"}]', '[{\"employ\":\"拼多多\",\"project\":\"广告投放平台\",\"position\":\"Java后端开发\",\"type\":\"全职\",\"start\":\"2024-07-01\",\"end\":\"2024-12-01\",\"content\":\"这个项目是xxx\"}]', '[{\"name\":\"二手交易平台\",\"start\":\"2025-01-01\",\"end\":\"2025-03-01\",\"content\":\"开发二手交易平台\"},{\"name\":\"企业招聘平台\",\"start\":\"2025-02-24\",\"end\":\"2025-03-16\",\"content\":\"bishe\"}]', 1);
INSERT INTO `resume` (`id`, `name`, `username`, `sex`, `salary`, `education`, `experience`, `phone`, `email`, `edu_exps`, `work_exps`, `pro_exps`, `user_id`) VALUES (8, '网络空间安全', '网络空间安全', '男', '3-5k', '高中', '3到5年', '08', '08', '[]', '[]', '[]', 1);
INSERT INTO `resume` (`id`, `name`, `username`, `sex`, `salary`, `education`, `experience`, `phone`, `email`, `edu_exps`, `work_exps`, `pro_exps`, `user_id`) VALUES (9, '网络空间安全', '李四', '男', '10-20k', '硕士', '在校生', '1888', '888@85boss.com', '[{\"school\":\"暨南大学\",\"speciality\":\"网络空间安全\",\"education\":\"硕士\",\"start\":\"2025-06-01\",\"course\":\"密码学，区块链\"}]', '[]', '[]', 2);
INSERT INTO `resume` (`id`, `name`, `username`, `sex`, `salary`, `education`, `experience`, `phone`, `email`, `edu_exps`, `work_exps`, `pro_exps`, `user_id`) VALUES (10, '全栈', '张三', '男', '20-50k', '硕士', '1年以内', '12345678912', '998877665544@85boss.com', '[{\"school\":\"合肥大学\",\"speciality\":\"软件工程\",\"education\":\"本科\",\"start\":\"2019-03-01\",\"end\":\"2023-06-01\",\"course\":\"软件工程，java\"},{\"school\":\"中国科学技术大学\",\"speciality\":\"电子信息-软件工程\",\"education\":\"硕士\",\"start\":\"2023-08-01\",\"end\":\"2025-03-07\",\"course\":\"人工智能\"}]', '[{\"employ\":\"华为\",\"project\":\"开发\",\"position\":\"后端\",\"type\":\"实习\",\"start\":\"2024-09-01\",\"end\":\"2025-05-01\",\"content\":\"在华为进行后端开发\"}]', '[{\"name\":\"智能机器人\",\"start\":\"2024-03-01\",\"end\":\"2025-03-13\",\"content\":\"自研人形机器人\"}]', 1);
COMMIT;

-- ----------------------------
-- Table structure for submit
-- ----------------------------
DROP TABLE IF EXISTS `submit`;
CREATE TABLE `submit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `employ_id` int(11) DEFAULT NULL COMMENT '企业ID',
  `position_id` int(11) DEFAULT NULL COMMENT '岗位ID',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `resume_id` int(11) DEFAULT NULL COMMENT '简历ID',
  `time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '投递时间',
  `status` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简历状态',
  `ai_score` smallint(4) DEFAULT NULL COMMENT 'ai打分',
  `ai_review` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ai审核',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='简历投递表';

-- ----------------------------
-- Records of submit
-- ----------------------------
BEGIN;
INSERT INTO `submit` (`id`, `employ_id`, `position_id`, `user_id`, `resume_id`, `time`, `status`, `ai_score`, `ai_review`) VALUES (2, 2, 6, 1, 4, '2025-03-20 15:43:02', '面试中', NULL, NULL);
INSERT INTO `submit` (`id`, `employ_id`, `position_id`, `user_id`, `resume_id`, `time`, `status`, `ai_score`, `ai_review`) VALUES (3, 2, 5, 1, 10, '2025-03-20 16:20:02', '已投递', NULL, NULL);
INSERT INTO `submit` (`id`, `employ_id`, `position_id`, `user_id`, `resume_id`, `time`, `status`, `ai_score`, `ai_review`) VALUES (4, 5, 4, 2, 9, '2025-03-20 16:36:16', '不适合', NULL, NULL);
INSERT INTO `submit` (`id`, `employ_id`, `position_id`, `user_id`, `resume_id`, `time`, `status`, `ai_score`, `ai_review`) VALUES (5, 2, 6, 2, 9, '2025-03-20 16:48:11', '已投递', NULL, NULL);
INSERT INTO `submit` (`id`, `employ_id`, `position_id`, `user_id`, `resume_id`, `time`, `status`, `ai_score`, `ai_review`) VALUES (7, 5, 4, 1, 3, '2025-04-08 15:18:44', '通过', NULL, NULL);
INSERT INTO `submit` (`id`, `employ_id`, `position_id`, `user_id`, `resume_id`, `time`, `status`, `ai_score`, `ai_review`) VALUES (12, 5, 3, 2, 9, '2025-05-10 14:30:15', '不适合', 50, '不合格');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `username`, `password`, `name`, `avatar`, `role`, `phone`, `email`) VALUES (1, 'zhangsan', '123456', '张三', 'http://localhost:9090/files/download/1739781887135-img.png', 'USER', '123213123123', 'zhangsan@jb.com');
INSERT INTO `user` (`id`, `username`, `password`, `name`, `avatar`, `role`, `phone`, `email`) VALUES (2, 'lisi', '123456', '李四', 'http://localhost:9090/files/download/1739782004729--3965bdb3aae64dbc.jpg', 'USER', '18812123232', 'lisi@qq.com');
INSERT INTO `user` (`id`, `username`, `password`, `name`, `avatar`, `role`, `phone`, `email`) VALUES (3, 'wangwu', '123456', '王五', 'http://localhost:9090/files/download/1739782030368-IMG_20230219_235434.jpg', 'USER', '19923230101', 'wangwu@jb.com');
INSERT INTO `user` (`id`, `username`, `password`, `name`, `avatar`, `role`, `phone`, `email`) VALUES (4, 'zhaoliu', '123456', '赵六', 'http://localhost:9090/files/download/1741173174446-employee.jpg', 'USER', '18872727272', 'zhaoliu@hfuu.edu');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
