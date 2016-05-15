-- HSQLDB
DROP TABLE USERS IF EXISTS CASCADE;
DROP TABLE ROLES IF EXISTS CASCADE;
DROP TABLE USERS_ROLES IF EXISTS CASCADE;

-- H2
--DROP TABLE IF EXISTS USERS CASCADE;
--DROP TABLE IF EXISTS ROLES CASCADE;
--DROP TABLE IF EXISTS USERS_ROLES CASCADE;

CREATE TABLE USERS (ID INT, USERNAME VARCHAR(20) CHECK (USERNAME <> ''), PASSWORD VARCHAR(20) CHECK (PASSWORD <> ''));
ALTER TABLE USERS ADD CONSTRAINT USERS_PK PRIMARY KEY (ID);
ALTER TABLE USERS ADD CONSTRAINT USERS_NAME_UNQ UNIQUE (USERNAME);
INSERT INTO USERS (ID, USERNAME, PASSWORD) VALUES (1, 'quickstartUser', 'quickstartPwd1!');
INSERT INTO USERS (ID, USERNAME, PASSWORD) VALUES (2, 'guest', 'guestPwd1!');

CREATE TABLE ROLES (ID INT, NAME VARCHAR(20));
ALTER TABLE ROLES ADD CONSTRAINT ROLES_PK PRIMARY KEY (ID);
ALTER TABLE ROLES ADD CONSTRAINT ROLES_NAME_UNQ UNIQUE (NAME);
INSERT INTO ROLES (ID, NAME) VALUES (1, 'quickstarts');
INSERT INTO ROLES (ID, NAME) VALUES (2, 'guest');

CREATE TABLE USERS_ROLES (USER_ID INT, ROLE_ID INT);
ALTER TABLE USERS_ROLES ADD CONSTRAINT USERS_ROLES_UNQ UNIQUE (USER_ID, ROLE_ID);
INSERT INTO USERS_ROLES (USER_ID, ROLE_ID) VALUES (1,1);
INSERT INTO USERS_ROLES (USER_ID, ROLE_ID) VALUES (2,2);


--CREATE SEQUENCE OBJECT_SEQ AS BIGINT START WITH 10 INCREMENT BY 10;
-- HSQLDB
--insert into USERS (ID, USERNAME) VALUES(NEXT VALUE FOR USER_SEQ, 'USER 1');

-- H2
--insert into USERS (ID, USERNAME) VALUES(USER_SEQ.NEXTVAL, 'USER 1');