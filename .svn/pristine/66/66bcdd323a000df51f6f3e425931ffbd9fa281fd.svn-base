--
--    LssclM2M - http://www.lsscl.com
--    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
--     
--    
--     
--     
--     
--     
--
--     
--     
--     
--     
--
--     
--    
--
--

--
-- System settings
create table systemSettings (
  settingName nvarchar(32) not null,
  settingValue ntext,
  primary key (settingName)
);


--
-- Users
create table users (
  id int not null identity,
  username nvarchar(40) not null,
  password nvarchar(30) not null,
  email nvarchar(255) not null,
  phone nvarchar(40),
  admin char(1) not null,
  disabled char(1) not null,
  lastLogin bigint,
  selectedWatchList int,
  homeUrl nvarchar(255),
  receiveAlarmEmails int not null,
  receiveOwnAuditEvents char(1) not null,
  primary key (id)
);

create table userComments (
  userId int,
  commentType int not null,
  typeKey int not null,
  ts bigint not null,
  commentText nvarchar(1024) not null
);
alter table userComments add constraint userCommentsFk1 foreign key (userId) references users(id);
----------------------------------------

CREATE TABLE userEventHandlers(
	userId int primary key NOT NULL,
	eventHandlerCount int NOT NULL,
	limit bigint NOT NULL
) ;

-------------------------------------------
--
-- Mailing lists
create table mailingLists (
  id int not null identity,
  xid nvarchar(50) not null,
  name nvarchar(40) not null,
  scopeid int not null,
  primary key (id)
);
alter table mailingLists add constraint mailingListsUn1 unique (xid);

create table mailingListInactive (
  mailingListId int not null,
  inactiveInterval int not null
);
alter table mailingListInactive add constraint mailingListInactiveFk1 foreign key (mailingListId) 
  references mailingLists(id) on delete cascade;

create table mailingListMembers (
  mailingListId int not null,
  typeId int not null,
  userId int,
  address nvarchar(255)
);
alter table mailingListMembers add constraint mailingListMembersFk1 foreign key (mailingListId) 
  references mailingLists(id) on delete cascade;




--
--
-- Data Sources
--
create table dataSources (
  id int not null identity,
  xid nvarchar(50) not null,
  name nvarchar(40) not null,
  dataSourceType int not null,
  data image not null,
  factoryId int not null,
  primary key (id)
);
alter table dataSources add constraint dataSourcesUn1 unique (xid);


-- Data source permissions
create table dataSourceUsers (
  dataSourceId int not null,
  userId int not null
);
alter table dataSourceUsers add constraint dataSourceUsersFk1 foreign key (dataSourceId) references dataSources(id);
alter table dataSourceUsers add constraint dataSourceUsersFk2 foreign key (userId) references users(id) on delete cascade;



--
--
-- Data Points
--
create table dataPoints (
  id int not null identity,
  xid nvarchar(50) not null,
  dataSourceId int not null,
  data image not null,
  primary key (id)
);
alter table dataPoints add constraint dataPointsUn1 unique (xid);
alter table dataPoints add constraint dataPointsFk1 foreign key (dataSourceId) references dataSources(id);


-- Data point permissions
create table dataPointUsers (
  dataPointId int not null,
  userId int not null,
  permission int not null
);
alter table dataPointUsers add constraint dataPointUsersFk1 foreign key (dataPointId) references dataPoints(id);
alter table dataPointUsers add constraint dataPointUsersFk2 foreign key (userId) references users(id) on delete cascade;


--
--
-- Views
--
create table mangoViews (
  id int not null identity,
  xid nvarchar(50) not null,
  name nvarchar(100) not null,
  background nvarchar(255),
  userId int not null,
  anonymousAccess int not null,
  data image not null,
  factoryId int not null,
  primary key (id)
);
alter table mangoViews add constraint mangoViewsUn1 unique (xid);
alter table mangoViews add constraint mangoViewsFk1 foreign key (userId) references users(id) on delete cascade;

create table mangoViewUsers (
  mangoViewId int not null,
  userId int not null,
  accessType int not null,
  primary key (mangoViewId, userId)
);
alter table mangoViewUsers add constraint mangoViewUsersFk1 foreign key (mangoViewId) references mangoViews(id);
alter table mangoViewUsers add constraint mangoViewUsersFk2 foreign key (userId) references users(id) on delete cascade;


--
--
-- Point Values (historical data)
--
create table pointValues (
  id bigint not null identity,
  dataPointId int not null,
  dataType int not null,
  pointValue float,
  ts bigint not null,
  primary key (id)
);
alter table pointValues add constraint pointValuesFk1 foreign key (dataPointId) references dataPoints(id) on delete cascade;
create index pointValuesIdx1 on pointValues (ts, dataPointId);
create index pointValuesIdx2 on pointValues (dataPointId, ts);

create table pointValueAnnotations (
  pointValueId bigint not null,
  textPointValueShort nvarchar(128),
  textPointValueLong ntext,
  sourceType smallint,
  sourceId int
);
alter table pointValueAnnotations add constraint pointValueAnnotationsFk1 foreign key (pointValueId) 
  references pointValues(id) on delete cascade;


--
--
-- Watch list
--
create table watchLists (
  id int not null identity,
  xid nvarchar(50) not null,
  userId int not null,
  name nvarchar(50),
  factoryId int not null,
  primary key (id)
);
alter table watchLists add constraint watchListsUn1 unique (xid);
alter table watchLists add constraint watchListsFk1 foreign key (userId) references users(id) on delete cascade;

create table watchListPoints (
  watchListId int not null,
  dataPointId int not null,
  sortOrder int not null
);
alter table watchListPoints add constraint watchListPointsFk1 foreign key (watchListId) references watchLists(id) on delete cascade;
alter table watchListPoints add constraint watchListPointsFk2 foreign key (dataPointId) references dataPoints(id);

create table watchListUsers (
  watchListId int not null,
  userId int not null,
  accessType int not null,
  primary key (watchListId, userId)
);
alter table watchListUsers add constraint watchListUsersFk1 foreign key (watchListId) references watchLists(id);
alter table watchListUsers add constraint watchListUsersFk2 foreign key (userId) references users(id) on delete cascade;


--
--
-- Point event detectors
--
create table pointEventDetectors (
  id int not null identity,
  xid nvarchar(50) not null,
  alias nvarchar(255),
  dataPointId int not null,
  detectorType int not null,
  alarmLevel int not null,
  stateLimit float,
  duration int,
  durationType int,
  binaryState char(1),
  multistateState int,
  changeCount int,
  alphanumericState nvarchar(128),
  weight float,
  primary key (id)
);
alter table pointEventDetectors add constraint pointEventDetectorsUn1 unique (xid, dataPointId);
alter table pointEventDetectors add constraint pointEventDetectorsFk1 foreign key (dataPointId) 
  references dataPoints(id);

--
--
-- Events
--
create table events (
  id int not null identity,
  typeId int not null,
  typeRef1 int not null,
  typeRef2 int not null,
  activeTs bigint not null,
  rtnApplicable char(1) not null,
  rtnTs bigint,
  rtnCause int,
  alarmLevel int not null,
  message ntext,
  ackTs bigint,
  ackUserId int,
  alternateAckSource int,
  scopeId int NULL,
  emailHandler int,
  primary key (id)
);
alter table events add constraint eventsFk1 foreign key (ackUserId) references users(id);
--------------------------------------------
---事件临时表 (针对邮件)
CREATE TABLE eventTemp(
	id int IDENTITY(1,1) primary key NOT NULL,
	uid int NOT NULL,
	emailAddress varchar(255)  NOT NULL,
	ts bigint NOT NULL,

) 
---------------------------------------------



create table userEvents (
  eventId int not null,
  userId int not null,
  silenced char(1) not null,
  primary key (eventId, userId)
);
alter table userEvents add constraint userEventsFk1 foreign key (eventId) references events(id) on delete cascade;
alter table userEvents add constraint userEventsFk2 foreign key (userId) references users(id);


--
--
-- Event handlers
--
create table eventHandlers (
  id int not null identity,
  xid nvarchar(50) not null,
  alias nvarchar(255),
  
  -- Event type, see events
  eventTypeId int not null,
  eventTypeRef1 int not null,
  eventTypeRef2 int not null,
  
  data image not null,
  scopeId int not null,
  disableChange char(1) not null,
  primary key (id)
);
alter table eventHandlers add constraint eventHandlersUn1 unique (xid);



--
--
-- Scheduled events
--
create table scheduledEvents (
  id int not null identity,
  xid nvarchar(50) not null,
  alias nvarchar(255),
  alarmLevel int not null,
  scheduleType int not null,
  returnToNormal char(1) not null,
  disabled char(1) not null,
  activeYear int,
  activeMonth int,
  activeDay int,
  activeHour int,
  activeMinute int,
  activeSecond int,
  activeCron nvarchar(25),
  inactiveYear int,
  inactiveMonth int,
  inactiveDay int,
  inactiveHour int,
  inactiveMinute int,
  inactiveSecond int,
  inactiveCron nvarchar(25),
  scopeId int ,
  primary key (id)
);
alter table scheduledEvents add constraint scheduledEventsUn1 unique (xid);


--
--
-- Point Hierarchy
--
create table pointHierarchy (
  id int not null identity,
  parentId int,
  name nvarchar(100),
  scopeId int ,
  primary key (id)
);

--
--
-- Compound events detectors
--
create table compoundEventDetectors (
  id int not null identity,
  xid nvarchar(50) not null,
  name nvarchar(100),
  alarmLevel int not null,
  returnToNormal char(1) not null,
  disabled char(1) not null,
  conditionText nvarchar(256) not null,
  scopeId int not null,
  primary key (id)
);
alter table compoundEventDetectors add constraint compoundEventDetectorsUn1 unique (xid);


--
--
-- Reports
--
create table reports (
  id int not null identity,
  userId int not null,
  name nvarchar(100) not null,
  data image not null,
  factoryId int not null,
  primary key (id)
);
alter table reports add constraint reportsFk1 foreign key (userId) references users(id) on delete cascade;

create table reportInstances (
  id int not null identity,
  userId int not null,
  name nvarchar(100) not null,
  includeEvents int not null,
  includeUserComments char(1) not null,
  reportStartTime bigint not null,
  reportEndTime bigint not null,
  runStartTime bigint,
  runEndTime bigint,
  recordCount int,
  preventPurge char(1),
  primary key (id)
);
alter table reportInstances add constraint reportInstancesFk1 foreign key (userId) references users(id) on delete cascade;

create table reportInstancePoints (
  id int not null identity,
  reportInstanceId int not null,
  dataSourceName nvarchar(40) not null,
  pointName nvarchar(100) not null,
  dataType int not null,
  startValue nvarchar(4000),
  textRenderer image,
  colour nvarchar(6),
  consolidatedChart char(1),
  primary key (id)
);
alter table reportInstancePoints add constraint reportInstancePointsFk1 foreign key (reportInstanceId) 
  references reportInstances(id) on delete cascade;

create table reportInstanceData (
  pointValueId bigint not null,
  reportInstancePointId int not null,
  pointValue float,
  ts bigint not null,
  primary key (pointValueId, reportInstancePointId)
);
alter table reportInstanceData add constraint reportInstanceDataFk1 foreign key (reportInstancePointId) 
  references reportInstancePoints(id) on delete cascade;

create table reportInstanceDataAnnotations (
  pointValueId bigint not null,
  reportInstancePointId int not null,
  textPointValueShort nvarchar(128),
  textPointValueLong ntext,
  sourceValue nvarchar(128),
  primary key (pointValueId, reportInstancePointId)
);
alter table reportInstanceDataAnnotations add constraint reportInstanceDataAnnotationsFk1 
  foreign key (pointValueId, reportInstancePointId) references reportInstanceData(pointValueId, reportInstancePointId) 
  on delete cascade;

create table reportInstanceEvents (
  eventId int not null,
  reportInstanceId int not null,
  typeId int not null,
  typeRef1 int not null,
  typeRef2 int not null,
  activeTs bigint not null,
  rtnApplicable char(1) not null,
  rtnTs bigint,
  rtnCause int,
  alarmLevel int not null,
  message ntext,
  ackTs bigint,
  ackUsername nvarchar(40),
  alternateAckSource int,
  primary key (eventId, reportInstanceId)
);
alter table reportInstanceEvents add constraint reportInstanceEventsFk1 foreign key (reportInstanceId)
  references reportInstances(id) on delete cascade;

create table reportInstanceUserComments (
  reportInstanceId int not null,
  username nvarchar(40),
  commentType int not null,
  typeKey int not null,
  ts bigint not null,
  commentText nvarchar(1024) not null
);
alter table reportInstanceUserComments add constraint reportInstanceUserCommentsFk1 foreign key (reportInstanceId)
  references reportInstances(id) on delete cascade;


--
--
-- Publishers
--
create table publishers (
  id int not null identity,
  xid nvarchar(50) not null,
  data image not null,
  primary key (id)
);
alter table publishers add constraint publishersUn1 unique (xid);


--
--
-- Point links
--
create table pointLinks (
  id int not null identity,
  xid nvarchar(50) not null,
  sourcePointId int not null,
  targetPointId int not null,
  script ntext,
  eventType int not null,
  disabled char(1) not null,
  scopeId int not null,
  primary key (id)
);
alter table pointLinks add constraint pointLinksUn1 unique (xid);


--
--
-- Maintenance events
--
create table maintenanceEvents (
  id int not null identity,
  xid nvarchar(50) not null,
  dataSourceId int not null,
  alias nvarchar(255),
  alarmLevel int not null,
  scheduleType int not null,
  disabled char(1) not null,
  activeYear int,
  activeMonth int,
  activeDay int,
  activeHour int,
  activeMinute int,
  activeSecond int,
  activeCron nvarchar(25),
  inactiveYear int,
  inactiveMonth int,
  inactiveDay int,
  inactiveHour int,
  inactiveMinute int,
  inactiveSecond int,
  inactiveCron nvarchar(25),
  primary key (id)
);
alter table maintenanceEvents add constraint maintenanceEventsUn1 unique (xid);
alter table maintenanceEvents add constraint maintenanceEventsFk1 foreign key (dataSourceId) references dataSources(id);


 -- 行业表：工厂行业的分类信息
create table trade(
id int identity(1,1) not null , 
tradename nvarchar(50)	not null, 
description	nvarchar(200), 
primary key(id)
); 

 
 -- 权限表:每个操作都是一种权限
create table ACTION( 
id int identity(1,1) not null ,
actionname	nvarchar(50) not null, 
description	nvarchar(200),	
url	nvarchar(200) not null,
primary key(id)
);

-- 角色表:多个权限的集合
create table role (
id int identity(1,1) not null ,
rolename nvarchar(20)not null,
description	nvarchar(200), 
scopeType int not null,
primary key(id)
); 



-- 角色权限表:为role表和action表的中间表(多对多关系)
create table role_action(
rid		INT		NOT NULL,
aid		INT	 	NOT NULL,
DATE		BIGINT		NOT NULL,
PRIMARY KEY(rid,aid)
);

ALTER TABLE role_action ADD CONSTRAINT role_action_rid FOREIGN KEY(rid)  REFERENCES role(id);
ALTER TABLE role_action ADD CONSTRAINT role_action_aid FOREIGN KEY(aid)  REFERENCES ACTION(id);

-- 用户角色表:为user表和role表的中间表(多对多关系)
create table user_role(
uid		INT		NOT NULL,
rid		INT		NOT NULL,
DATE		BIGINT		NOT NULL,
defaultRole int not null,
PRIMARY KEY (uid,rid)
);


ALTER TABLE user_role ADD CONSTRAINT user_role_uid FOREIGN KEY(uid)  REFERENCES users(id);
ALTER TABLE user_role ADD CONSTRAINT user_role_rid FOREIGN KEY(rid) REFERENCES role(id);


-- 范围表:存放区域、子区域、工厂的基本信息
create table scope( 
id int 	identity(1,1) NOT NULL,
scopename	VARCHAR(50)	NOT NULL ,
address		VARCHAR(200)		,
lon			FLOAT		NOT NULL ,
lat			FLOAT		NOT NULL ,
enlargenum	INT			NOT NULL ,
description	VARCHAR(200)		,
parentid	INT					,
scopetype	INT			NOT NULL ,
tradeid 	INT 				,
PRIMARY KEY(id)
);


ALTER TABLE scope ADD CONSTRAINT scope_parentid FOREIGN KEY(parentid)  REFERENCES scope(id);
ALTER TABLE scope ADD CONSTRAINT scope_tradeid FOREIGN KEY(tradeid)  REFERENCES trade(id);
-- 规定区域类型编号为1,子区域类型编号为2,工厂类型编号为3 



-- 用户所属表:记录此用户属于那个范围的
create table user_scope (
uid		INT		NOT NULL,
scopeid		INT		NOT NULL,
isHomeScope int not null,
PRIMARY KEY(uid,scopeid) 	
);


ALTER TABLE user_scope ADD CONSTRAINT user_scope_uid FOREIGN KEY(uid)  REFERENCES users(id);
ALTER TABLE user_scope ADD CONSTRAINT user_scope_scopeid FOREIGN KEY(scopeid)  REFERENCES scope(id);
 

-- 空压机型号表：记录空压机的所有型号
create table aircompressor_type(
id INT	identity(1,1) NOT NULL ,
typename	VARCHAR(50)	NOT NULL,
description	VARCHAR(200)		,
type int not null,
PRIMARY KEY(id)
);

 



-- 空压机属性表:记录所有类型的空压机的所有属性
create table aircompressor_attr(
id		INT		NOT NULL 	identity(1,1), 
attrname	VARCHAR(50)	NOT NULL,
description	VARCHAR(200),
PRIMARY KEY(id)
);




-- 空压机表：记录空压机的基本信息
create table aircompressor( 
id		INT		NOT NULL 	identity(1,1),
xid varchar(50)  not null,
acname		VARCHAR(50)	NOT NULL,
actid		INT		NOT NULL,
OFFSET		INT		NOT NULL,
factoryid	INT		NOT NULL,
type int not null,
PRIMARY KEY(id)
);

ALTER TABLE aircompressor ADD CONSTRAINT aircompressor_actid FOREIGN KEY(actid)  REFERENCES aircompressor_type(id);
ALTER TABLE aircompressor ADD CONSTRAINT aircompressor_factoryid FOREIGN KEY(factoryid)  REFERENCES scope(id);


-- 统计参数表：记录统计信息需要的参数
create table statisticsParam( 
id		INT		NOT NULL 	identity(1,1),
paramname	VARCHAR(50)	NOT NULL,
dataType	int not null,
useType int not null,
PRIMARY KEY(id)
); 
--------------------------------------------------------------------
--统计参数配置
create table statisticsConfiguration(
	spid int NOT NULL,
	acpaid int NOT NULL
) 
;
ALTER TABLE statisticsConfiguration  WITH CHECK ADD  CONSTRAINT acpaidFk1 FOREIGN KEY(acpaid)
REFERENCES aircompressor_attr (id);


ALTER TABLE statisticsConfiguration  WITH CHECK ADD  CONSTRAINT statisticsparamFK FOREIGN KEY(spid)
REFERENCES statisticsParam (id);
----------------------------------------------------------------------


create table aircompressor_type_attr(
	id int IDENTITY(1,1)  primary key NOT NULL,
	actid int NOT NULL,
	acaid int NOT NULL,
	data image NOT NULL,
);

ALTER TABLE aircompressor_type_attr  WITH CHECK ADD  CONSTRAINT aircompressor_type_attr_acaid FOREIGN KEY(acaid)
REFERENCES aircompressor_attr (id);

ALTER TABLE aircompressor_type_attr  WITH CHECK ADD  CONSTRAINT aircompressor_type_attr_actid FOREIGN KEY(actid)
REFERENCES aircompressor_type (id);
----------------------------------------------------------


-- 空压机成员表：记录空压机每个属性对应的数据点
create table aircompressor_members( 
acid		INT		NOT NULL,
acaid		INT		, 
spid		INT		,
dpid		INT		NOT NULL,
PRIMARY KEY(acid)
);

ALTER TABLE aircompressor_members ADD CONSTRAINT aircompressor_members_acid FOREIGN KEY(acid)  REFERENCES aircompressor(id);
ALTER TABLE aircompressor_members ADD CONSTRAINT aircompressor_members_acaid FOREIGN KEY(acaid)  REFERENCES aircompressor_attr(id);
--ALTER TABLE aircompressor_members ADD CONSTRAINT aircompressor_members_spid FOREIGN KEY(spid)  REFERENCES 
--statistics_param(id);
ALTER TABLE aircompressor_members ADD CONSTRAINT aircompressor_members_dpid FOREIGN KEY(dpid)  REFERENCES datapoints(id);

-- 压缩空气系统表：记录空压机系统的基本信息
create table aircompressor_system( 
id		INT		NOT NULL 	identity(1,1),
xid		VARCHAR(50)	NOT NULL,
systemname	VARCHAR(50)	NOT NULL,
factoryid	INT		NOT NULL,
PRIMARY KEY(id)
);

ALTER TABLE aircompressor_system ADD CONSTRAINT aircompressor_system_factoryid FOREIGN KEY(factoryid)  REFERENCES scope(id);
-- 工厂类型编号为3（在scope表中默认加条件 scopetype=3） 

 
-- 压缩空气系统成员表:记录空压机系统下的所有空压机以及统计需要的对应的参数
create table aircompressor_system_members( 
acsid		INT		NOT NULL,
membertype	INT		NOT NULL,
memberid	INT		NOT NULL,
spid		INT				,
PRIMARY KEY(acsid,membertype,memberid)
);
ALTER TABLE aircompressor_system_members ADD CONSTRAINT aircompressor_system_members_acsid FOREIGN KEY(acsid)  REFERENCES aircompressor_system(id);
ALTER TABLE aircompressor_system_members ADD CONSTRAINT aircompressor_system_members_spid  FOREIGN KEY(spid)  REFERENCES statisticsparam (id); 
-- 规定membertype 为空压机是值为0,为数据点时候值为1



--scope的事件处理器配置表
create table scopeSendSetting(
id int IDENTITY(1,1) primary key NOT NULL,
scopeId int NOT NULL,
data image NOT NULL,
) ;

--统计脚本
create table statisticsScript(
id int IDENTITY(1,1) NOT NULL,
xid varchar(50)   NOT NULL,
name varchar(50)   NOT NULL,
disabled char(1)   NOT NULL,
conditionText text   NOT NULL,
startTs bigint NOT NULL,
PRIMARY KEY(id)
);
--定时统计
create table scheduledStatistic(
	id int IDENTITY(1,1) NOT NULL,
	scriptId int NOT NULL,
	value float NOT NULL,
	ts bigint NOT NULL,
	unitType int NOT NULL,
	unitId int NOT NULL,
	date varchar(50) NULL,
	primary key (id)
);
ALTER TABLE scheduledStatistic  WITH CHECK ADD  CONSTRAINT scheduledStatistic_scriptId FOREIGN KEY(scriptId)
REFERENCES statisticsScript (id);
--统计运行状态表
create table statisticRTStatus(
	id int identity(1,1) NOT NULL,
	scriptId int NOT NULL,
	startTs bigint NOT NULL,
	endTs bigint NOT NULL,
	stopTs varchar(50) NOT NULL,
	statisticType int NOT NULL,
	primary key(id)
) ;
ALTER TABLE statisticRTStatus  WITH CHECK ADD  CONSTRAINT FK_statisticRTStatus_statisticsScript FOREIGN KEY(scriptId)
REFERENCES statisticsScript (id);

