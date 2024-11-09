CREATE
USER IF NOT EXISTS 'master'@'%' IDENTIFIED BY 'master';
GRANT ALL PRIVILEGES ON *.* TO
'master'@'%';
FLUSH
PRIVILEGES;

CREATE
DATABASE IF NOT EXISTS ski_db;

USE
ski_db;

DROP TABLE IF EXISTS current_weather;
DROP TABLE IF EXISTS daily_weather;
DROP TABLE IF EXISTS hourly_weather;
DROP TABLE IF EXISTS snow_quality_votes;
DROP TABLE IF EXISTS webcams;
DROP TABLE IF EXISTS slopes;
DROP TABLE IF EXISTS ski_resorts;

-- 스키장 정보 테이블
CREATE TABLE ski_resorts
(
    resort_id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`                     VARCHAR(255) NOT NULL,
    status                     VARCHAR(255) NOT NULL COMMENT '운영중, 운영종료, 예정',
    opening_date               DATE NULL,
    closing_date               DATE NULL,
    open_slopes                INT          NOT NULL DEFAULT 0,
    total_slopes               INT          NOT NULL DEFAULT 0,
    day_operating_hours        VARCHAR(50) NULL COMMENT '주간 운영시간',
    night_operating_hours      VARCHAR(50) NULL COMMENT '야간 운영시간',
    late_night_operating_hours VARCHAR(50) NULL COMMENT '심야 운영시간',
    dawn_operating_hours       VARCHAR(50) NULL COMMENT '새벽 운영시간',
    midnight_operating_hours   VARCHAR(50) NULL COMMENT '자정 운영시간',
    snowfall_time              VARCHAR(50) NULL COMMENT '정설 시간',
    x_coordinate               VARCHAR(10)  NOT NULL COMMENT '위도 매핑 값',
    y_coordinate               VARCHAR(10)  NOT NULL COMMENT '경도 매핑 값',
    detailed_area_code         VARCHAR(10) NULL COMMENT '예보 구역 코드(기온 예보에 사용)',
    broad_area_code            VARCHAR(10) NULL COMMENT '광역 지역 코드(육상 예보에 사용)',
    created_at                 DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                 DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 현재 날씨 테이블
CREATE TABLE current_weather
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    resort_id   BIGINT       NOT NULL UNIQUE,
    temperature INT          NOT NULL,
    max_temp    INT          NOT NULL,
    min_temp    INT          NOT NULL,
    feels_like  INT          NOT NULL,
    description VARCHAR(255) NOT NULL,
    `condition` VARCHAR(255) NOT NULL COMMENT '맑음, 구름많음, 흐림, 비, 비/눈, 눈',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (resort_id) REFERENCES ski_resorts (resort_id)
);

-- 시간대별 날씨 테이블
CREATE TABLE hourly_weather
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    resort_id            BIGINT       NOT NULL,
    forecast_time        DATETIME     NOT NULL,
    temperature          INT          NOT NULL,
    precipitation_chance INT          NOT NULL,
    `condition`          VARCHAR(255) NOT NULL COMMENT '맑음, 흐림, 흐리고 비, 비, 눈, 안개',
    created_at           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (resort_id) REFERENCES ski_resorts (resort_id)
);

-- 주간 날씨 테이블
CREATE TABLE daily_weather
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    resort_id            BIGINT       NOT NULL,
    forecast_date        DATE         NOT NULL,
    day_of_week          VARCHAR(10)  NOT NULL,
    d_day                INT          NOT NULL COMMENT '오늘(0), 내일(1), 모레(2)',
    precipitation_chance INT          NOT NULL COMMENT '강수확률',
    max_temp             INT          NOT NULL,
    min_temp             INT          NOT NULL,
    `condition`          VARCHAR(255) NOT NULL COMMENT '맑음, 흐림, 흐리고 비, 비, 눈, 안개',
    created_at           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (resort_id) REFERENCES ski_resorts (resort_id)
);

-- 설질 투표 테이블
CREATE TABLE snow_quality_votes
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    resort_id   BIGINT   NOT NULL,
    is_positive BOOLEAN  NOT NULL,
    voted_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (resort_id) REFERENCES ski_resorts (resort_id)
);

-- 슬로프 정보 테이블
CREATE TABLE slopes
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    resort_id               BIGINT       NOT NULL,
    `name`                  VARCHAR(255) NOT NULL,
    webcam_number           INT NULL,
    difficulty              VARCHAR(255) NOT NULL COMMENT '초급, 중급, 중상급, 최상급, 파크',
    is_day_operating        BOOLEAN               DEFAULT FALSE,
    is_night_operating      BOOLEAN               DEFAULT FALSE,
    is_late_night_operating BOOLEAN               DEFAULT FALSE,
    is_dawn_operating       BOOLEAN               DEFAULT FALSE,
    is_midnight_operating   BOOLEAN               DEFAULT FALSE,
    created_at              DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (resort_id) REFERENCES ski_resorts (resort_id)
);

-- 웹캠 정보 테이블
CREATE TABLE webcams
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    resort_id   BIGINT   NOT NULL,
    `name`      VARCHAR(255) NULL,
    number      INT NULL,
    description VARCHAR(255) NULL,
    url         VARCHAR(255) NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (resort_id) REFERENCES ski_resorts (resort_id)
);

-- 스키장 정보
INSERT INTO ski_resorts (`name`, status, opening_date, closing_date, open_slopes, total_slopes, day_operating_hours,
                         night_operating_hours, late_night_operating_hours, dawn_operating_hours,
                         midnight_operating_hours, snowfall_time, x_coordinate, y_coordinate, detailed_area_code,
                         broad_area_code)
VALUES ('지산 리조트', '예정', STR_TO_DATE('2024.12.04', '%Y.%m.%d'), NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, '69',
        '119', '11B20701', '11B00000'),
       ('곤지암 스키장', '예정', STR_TO_DATE('2024.12.03', '%Y.%m.%d'), NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, '69',
        '119', '11B20702', '11B00000'),
       ('비발디파크', '예정', STR_TO_DATE('2024.11.25', '%Y.%m.%d'), NULL, 0, 0, '08:30~16:30', '18:30~22:30', NULL, NULL,
        NULL, '16:30~18:30', '72', '129', '11D10302', '11D10000'),
       ('엘리시안 강촌', '예정', STR_TO_DATE('2024.11.30', '%Y.%m.%d'), NULL, 0, 0, '09:00~17:00', '18:30~24:00', '18:30~03:00',
        NULL, NULL, '17:00~18:30', '71', '132', '11D10301', '11D10000'),
       ('웰리힐리파크', '예정', STR_TO_DATE('2024.11.30', '%Y.%m.%d'), NULL, 0, 0, '09:00~16:30', '18:30~22:30', '22:30~24:00',
        NULL, NULL, '16:30~18:30', '81', '126', '11D10402', '11D10000'),
       ('휘닉스파크', '예정', STR_TO_DATE('2024.11.22', '%Y.%m.%d'), NULL, 0, 0, NULL, NULL, NULL, NULL, NULL, '14:30~18:00',
        '84', '128', '11D10503', '11D10000'),
       ('하이원 스키장', '예정', STR_TO_DATE('2024.12.06', '%Y.%m.%d'), NULL, 0, 0, '09:00~16:00', '18:00~22:00', NULL, NULL,
        NULL, '16:00~18:00', '92', '120', '11D10502', '11D10000'),
       ('용평스키장 모나', '예정', STR_TO_DATE('2024.11.22', '%Y.%m.%d'), NULL, 0, 0, '09:00~17:00', '19:00~22:00', NULL, NULL,
        NULL, '17:00~19:00', '89', '130', '11D20201', '11D20000'),
       ('무주덕유산', '예정', STR_TO_DATE('2024.12.06', '%Y.%m.%d'), NULL, 0, 0, '07:00~16:00', '18:00~21:00', NULL, NULL,
        NULL, '16:30~18:30', '75', '93', '11F10302', '11F10000'),
       ('에덴벨리(양산)', '예정', STR_TO_DATE('2024.11.23', '%Y.%m.%d'), NULL, 0, 0, NULL, NULL, NULL, NULL, NULL,
        '17:30~19:00', '95', '80', '11H20102', '11H20000'),
       ('오투리조트', '예정', STR_TO_DATE('2024.11.29', '%Y.%m.%d'), NULL, 0, 0, '09:30~16:30', '18:00~21:30', NULL, NULL,
        NULL, '16:30~18:00', '95', '119', '11D20301', '11D20000');

-- 하이윈 스키장 슬로프 정보 (resort_id = 7)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (7, '제우스슬로프 1', '초급', FALSE, FALSE, FALSE, NULL),
       (7, '제우스슬로프 2', '초급', FALSE, FALSE, FALSE, 1),
       (7, '제우스슬로프 3', '초급', FALSE, FALSE, FALSE, 13),
       (7, '제우스슬로프 3-1', '초급', FALSE, FALSE, FALSE, NULL),
       (7, '빅토리아슬로프 1', '상급', FALSE, FALSE, FALSE, 12),
       (7, '빅토리아슬로프 2', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '빅토리아슬로프 3', '최상급', FALSE, FALSE, FALSE, 9),
       (7, '헤라슬로프 1', '중급', FALSE, FALSE, FALSE, NULL),
       (7, '헤라슬로프 2', '중상급', FALSE, FALSE, FALSE, 2),
       (7, '헤라슬로프 3', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '아폴로슬로프 1', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '아폴로슬로프 2', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '아폴로슬로프 3', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '아폴로슬로프 4', '상급', FALSE, FALSE, FALSE, 15),
       (7, '아폴로슬로프 5', '최상급', FALSE, FALSE, FALSE, NULL),
       (7, '아폴로슬로프 6', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '아테나슬로프 1', '초급', FALSE, FALSE, FALSE, 4),
       (7, '아테나슬로프 2', '중급', FALSE, FALSE, FALSE, 6),
       (7, '아테나슬로프 3', '초급', FALSE, FALSE, FALSE, NULL),
       (7, '아테나슬로프 3-1', '초급', FALSE, FALSE, FALSE, NULL);

-- 곤지암 스키장 슬로프 정보 (resort_id = 2)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (2, '휘센슬로프', '초급', FALSE, FALSE, TRUE, NULL),
       (2, '와이낫슬로프', '초중급', FALSE, FALSE, FALSE, NULL),
       (2, '그램슬로프1', '상급', FALSE, FALSE, FALSE, NULL),
       (2, '그램슬로프 2', '중급', FALSE, FALSE, FALSE, NULL),
       (2, '와이낫슬로프 1', '초중급', FALSE, FALSE, FALSE, 5),
       (2, 'CNP슬로프 1', '초중급', FALSE, FALSE, FALSE, 2),
       (2, 'CNP슬로프 2', '상급', FALSE, FALSE, FALSE, NULL),
       (2, '씽큐리프트 1', '중상급', FALSE, FALSE, FALSE, 2),
       (2, '씽큐리프트 2', '초중급', FALSE, FALSE, FALSE, 5),
       (2, '씽큐리프트 3', '중상급', FALSE, FALSE, FALSE, NULL),
       (2, '씽큐브릿지', '상급', FALSE, FALSE, FALSE, NULL);

-- 지산 리조트 슬로프 정보 (resort_id = 1)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (1, '레몬슬로프 1', '초급', FALSE, FALSE, FALSE, 1),
       (1, '레몬슬로프 1-1', '초급', FALSE, FALSE, FALSE, NULL),
       (1, '오렌지슬로프 2', '중급', FALSE, FALSE, FALSE, 2),
       (1, '오렌지슬로프 3', '중급', FALSE, FALSE, FALSE, 3),
       (1, '뉴오렌지슬로프', '상급', FALSE, FALSE, FALSE, NULL),
       (1, '블루슬로프 5', '상급', FALSE, FALSE, FALSE, 4),
       (1, '실버슬로프 6', '최상급', FALSE, FALSE, FALSE, 5),
       (1, '실버슬로프 7', '중상급', FALSE, FALSE, FALSE, 6);

-- 용평스키장 모나 슬로프 정보 (resort_id = 8)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (8, '골드 밸리', '상급', FALSE, FALSE, FALSE, 4),
       (8, '골드 파라다이스', '중급', FALSE, FALSE, FALSE, NULL),
       (8, '뉴골드슬로프', '중급', FALSE, FALSE, FALSE, 4),
       (8, '골드 환타스틱', '중급', FALSE, FALSE, FALSE, 4),
       (8, '레드 파라다이스', '중급', FALSE, FALSE, FALSE, 6),
       (8, '레드슬로프', '최상급', FALSE, FALSE, FALSE, NULL),
       (8, '뉴레드슬로프', '상급', FALSE, FALSE, FALSE, 6),
       (8, '블루슬로프', '상급', FALSE, FALSE, FALSE, NULL),
       (8, '핑크슬로프', '중급', FALSE, FALSE, FALSE, 7),
       (8, '옐로우슬로프', '초급', FALSE, FALSE, FALSE, NULL),
       (8, '뉴 옐로우 슬로프', '초급', FALSE, FALSE, FALSE, 8),
       (8, '실버 파라다이스', '중급', FALSE, FALSE, FALSE, NULL),
       (8, '레인보우 파라다이스', '중급', FALSE, FALSE, FALSE, 1),
       (8, '레인보우 1', '최상급', FALSE, FALSE, FALSE, 3),
       (8, '레인보우 2', '최상급', FALSE, FALSE, FALSE, 3),
       (8, '레인보우 3', '최상급', FALSE, FALSE, FALSE, 3),
       (8, '레인보우 4', '상급', FALSE, FALSE, FALSE, 3),
       (8, '메가 그린', '중급', FALSE, FALSE, FALSE, 5),
       (8, '드래곤 파크', '파크', FALSE, FALSE, FALSE, NULL);

-- 휘닉스파크 슬로프 정보 (resort_id = 6)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (6, '스패로우', '초급', FALSE, FALSE, FALSE, 6),
       (6, '펭귄슬로프', '초급', FALSE, FALSE, FALSE, 3),
       (6, '호크슬로프 1', '중급', FALSE, FALSE, FALSE, NULL),
       (6, '키위슬로프', '중급', FALSE, FALSE, FALSE, 2),
       (6, '파노라마', '중급', FALSE, FALSE, FALSE, 4),
       (6, '밸리슬로프', '중급', FALSE, FALSE, FALSE, 5),
       (6, '호크슬로프 2', '중급', FALSE, FALSE, FALSE, NULL),
       (6, '듀크슬로프', '중급', FALSE, FALSE, FALSE, 2),
       (6, '슬로프스타일', '중급', FALSE, FALSE, FALSE, 3),
       (6, '챔피언슬로프', '상급', FALSE, FALSE, FALSE, 7),
       (6, '환타지슬로프', '상급', FALSE, FALSE, FALSE, NULL),
       (6, '디지슬로프', '최상급', FALSE, FALSE, FALSE, 5),
       (6, '모글슬로프', '최상급', FALSE, FALSE, FALSE, NULL),
       (6, '파라다이스', '최상급', FALSE, FALSE, FALSE, NULL),
       (6, '익스트림 파크', '파크', FALSE, FALSE, FALSE, NULL);

-- 무주덕유산 슬로프 정보 (resort_id = 9)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (9, '이스턴슬로프', '초급', FALSE, FALSE, TRUE, 7),
       (9, '서역기행', '초급', FALSE, FALSE, FALSE, 5),
       (9, '스피츠 하단', '초급', FALSE, FALSE, FALSE, 8),
       (9, '웨스턴슬로프', '중급', FALSE, FALSE, FALSE, NULL),
       (9, '썬다운슬로프', '중급', FALSE, FALSE, FALSE, 6),
       (9, '실크로드', '중급', FALSE, FALSE, FALSE, 2),
       (9, '루키힐슬로프', '중급', FALSE, FALSE, FALSE, NULL),
       (9, '터보슬로프', '중급', FALSE, FALSE, FALSE, 6),
       (9, '커넥션슬로프', '중급', FALSE, FALSE, FALSE, NULL),
       (9, '미뉴에트', '상급', FALSE, FALSE, FALSE, 3),
       (9, '프리웨이', '상급', FALSE, FALSE, FALSE, 5),
       (9, '야마가슬로프', '상급', FALSE, FALSE, FALSE, 5),
       (9, '파노라마', '상급', FALSE, FALSE, FALSE, NULL),
       (9, '레이더스 하단', '상급', FALSE, FALSE, FALSE, NULL),
       (9, '모차르트', '상급', FALSE, FALSE, FALSE, 3),
       (9, '왈츠슬로프', '상급', FALSE, FALSE, FALSE, NULL),
       (9, '알레그로', '상급', FALSE, FALSE, FALSE, 1),
       (9, '레이더스 상단', '최상급', FALSE, FALSE, FALSE, 4),
       (9, '폴카슬로프', '최상급', FALSE, FALSE, FALSE, 1),
       (9, '카덴자슬로프', '최상급', FALSE, FALSE, FALSE, 1);

-- 웰리힐리파크 슬로프 정보 (resort_id = 5)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (5, '알파슬로프 1', '파크', FALSE, FALSE, TRUE, 5),
       (5, '알파슬로프 2', '초급', FALSE, FALSE, FALSE, 4),
       (5, '알파슬로프 3', '초급', FALSE, FALSE, FALSE, 4),
       (5, '브라보슬로프 1', '중급', FALSE, FALSE, FALSE, NULL),
       (5, '브라보슬로프 2', '중급', FALSE, FALSE, FALSE, NULL),
       (5, '델타슬로프 1', '초급', FALSE, FALSE, FALSE, 3),
       (5, '델타슬로프 +', '초급', FALSE, FALSE, FALSE, 1),
       (5, '에코슬로프 1', '최상급', FALSE, FALSE, FALSE, NULL),
       (5, '에코슬로프 2', '상급', FALSE, FALSE, FALSE, NULL),
       (5, '에코슬로프 3', '최상급', FALSE, FALSE, FALSE, NULL),
       (5, '첼린지슬로프 1', '상급', FALSE, FALSE, FALSE, NULL),
       (5, '첼린지슬로프 2', '상급', FALSE, FALSE, FALSE, NULL),
       (5, '첼린지슬로프 3', '최상급', FALSE, FALSE, FALSE, NULL),
       (5, '첼린지슬로프 4', '상급', FALSE, FALSE, FALSE, NULL),
       (5, '첼린지슬로프 5', '상급', FALSE, FALSE, FALSE, NULL),
       (5, '스타 익스프레스1', '중급', FALSE, FALSE, FALSE, NULL),
       (5, '스타 익스프레스2', '초급', FALSE, FALSE, FALSE, 1),
       (5, '하프파이프', '파크', FALSE, FALSE, FALSE, 2);

-- 엘리시안 강촌 슬로프 정보 (resort_id = 4)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (4, '팬더슬로프', '초급', FALSE, FALSE, TRUE, 2),
       (4, '래빗슬로프', '초급', FALSE, FALSE, FALSE, 2),
       (4, '드래곤슬로프', '중급', FALSE, FALSE, FALSE, 1),
       (4, '호스슬로프', '중급', FALSE, FALSE, FALSE, 1),
       (4, '페가수스', '중급', FALSE, FALSE, FALSE, 3),
       (4, '제브라슬로프', '중급', FALSE, FALSE, FALSE, 3),
       (4, '디어슬로프', '중급', FALSE, FALSE, FALSE, NULL),
       (4, '퓨마슬로프', '중급', FALSE, FALSE, FALSE, 1),
       (4, '래퍼드슬로프', '상급', FALSE, FALSE, FALSE, 3),
       (4, '제규어슬로프', '상급', FALSE, FALSE, FALSE, 3);

-- 비발디파크 슬로프 정보 (resort_id = 3)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (3, '발라드슬로프', '초급', FALSE, FALSE, FALSE, 6),
       (3, '블루스슬로프', '초급', FALSE, FALSE, FALSE, 9),
       (3, '힙합슬로프', '중상급', FALSE, FALSE, FALSE, 4),
       (3, '클래식슬로프', '중상급', FALSE, FALSE, FALSE, 3),
       (3, '레게슬로프', '중급', FALSE, FALSE, FALSE, NULL),
       (3, '재즈슬로프', '중급', FALSE, FALSE, FALSE, 8),
       (3, '펑키슬로프', '상급', FALSE, FALSE, FALSE, 5),
       (3, '테크노슬로프', '상급', FALSE, FALSE, FALSE, 7),
       (3, '락슬로프', '최상급', FALSE, FALSE, FALSE, NULL),
       (3, '익스트림 파크', '파크', FALSE, FALSE, FALSE, NULL);

-- 오투리조트 슬로프 정보 (resort_id = 11)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (11, '드림슬로프 1', '초급', FALSE, FALSE, FALSE, 3),
       (11, '드림슬로프 2', '초급', FALSE, FALSE, FALSE, NULL),
       (11, '헤드슬로프', '중급', FALSE, FALSE, FALSE, NULL),
       (11, '해피슬로프', '중급', FALSE, FALSE, FALSE, 1),
       (11, '글로리슬로프 1', '상급', FALSE, FALSE, FALSE, NULL),
       (11, '글로리슬로프 2', '상급', FALSE, FALSE, FALSE, NULL),
       (11, '글로리슬로프 3', '상급', FALSE, FALSE, FALSE, 1),
       (11, '패션슬로프 1', '최상급', FALSE, FALSE, FALSE, 1),
       (11, '패션슬로프 2', '최상급', FALSE, FALSE, FALSE, NULL),
       (11, '챌린지슬로프 1', '최상급', FALSE, FALSE, FALSE, 2),
       (11, '챌린지슬로프 2', '최상급', FALSE, FALSE, FALSE, 2),
       (11, '챌린지슬로프 3', '최상급', FALSE, FALSE, FALSE, 2),
       (11, '하프파이프', '파크', FALSE, FALSE, FALSE, 4);

-- 에덴벨리(양산) 슬로프 정보 (resort_id = 10)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (10, '베이직슬로프', '초급', FALSE, FALSE, FALSE, 1),
       (10, '메인슬로프', '초급', FALSE, FALSE, FALSE, 1),
       (10, '쥬피터슬로프', '중급', FALSE, FALSE, FALSE, 2),
       (10, '새턴슬로프', '중급', FALSE, FALSE, FALSE, NULL),
       (10, '우라누스', '중급', FALSE, FALSE, FALSE, 3),
       (10, '머큐리슬로프', '상급', FALSE, FALSE, FALSE, NULL),
       (10, '비너스슬로프', '상급', FALSE, FALSE, FALSE, 1);

-- 웹캠 정보 insert
INSERT INTO webcams (resort_id, `name`, number, description, url)
VALUES (7, '제우스2번 슬로프 입구', 1, NULL, 'http://59.30.12.195:1935/live/_definst_/ch1.stream/playlist.m3u8'),
       (7, '헤라2번 슬로프 입구', 2, NULL, 'http://59.30.12.195:1935/live/_definst_/ch2.stream/playlist.m3u8'),
       (7, '하이원 탑', 3, '정상', 'http://59.30.12.195:1935/live/_definst_/ch3.stream/playlist.m3u8'),
       (7, '아테나1번 슬로프', 4, '슬로프 하단', 'http://59.30.12.195:1935/live/_definst_/ch4.stream/playlist.m3u8'),
       (7, '마운틴 허브 베이스', 5, '허브', 'http://59.30.12.195:1935/live/_definst_/ch5.stream/playlist.m3u8'),
       (7, '아테나2번 슬로프', 6, NULL, 'http://59.30.12.195:1935/live/_definst_/ch6.stream/playlist.m3u8'),
       (7, '마운틴 베이스', 7, '허브', 'http://59.30.12.195:1935/live/_definst_/ch7.stream/playlist.m3u8'),
       (7, '아테나2번 슬로프 하단', 8, NULL, 'http://59.30.12.195:1935/live/_definst_/ch8.stream/playlist.m3u8'),
       (7, '빅토리아 상단', 9, NULL, 'http://59.30.12.195:1935/live/_definst_/ch9.stream/playlist.m3u8'),
       (7, '제우스 2번', 10, NULL, 'http://59.30.12.195:1935/live/_definst_/ch10.stream/playlist.m3u8'),
       (7, '밸리 허브 베이스', 11, '쉼터', 'http://59.30.12.195:1935/live/_definst_/ch11.stream/playlist.m3u8'),
       (7, '빅토리아1번 슬로프', 12, NULL, 'http://59.30.12.195:1935/live/_definst_/ch12.stream/playlist.m3u8'),
       (7, '제우스3번 슬로프', 13, NULL, 'http://59.30.12.195:1935/live/_definst_/ch13.stream/playlist.m3u8'),
       (7, '제우스3번 중단부', 14, NULL, 'http://59.30.12.195:1935/live/_definst_/ch14.stream/playlist.m3u8'),
       (7, '아폴로4번 중단부', 15, NULL, 'http://59.30.12.195:1935/live/_definst_/ch15.stream/playlist.m3u8'),
       (7, '아폴로 베이스', 16, NULL, 'http://59.30.12.195:1935/live/_definst_/ch16.stream/playlist.m3u8'),
       (7, '제우스3번 하단', 17, NULL, 'http://59.30.12.195:1935/live/_definst_/ch17.stream/playlist.m3u8'),
       (7, '밸리 베이스', 18, NULL, 'http://59.30.12.195:1935/live/_definst_/ch18.stream/playlist.m3u8'),

       (2, '정상 휴게소', 1, '정상', 'http://konjiam.live.cdn.cloudn.co.kr/konjiam/cam01.stream/playlist.m3u8'),
       (2, '정상부 슬로프', 2, NULL, 'http://konjiam.live.cdn.cloudn.co.kr/konjiam/cam02.stream/playlist.m3u8'),
       (2, '중간 슬로프', 3, NULL, 'http://konjiam.live.cdn.cloudn.co.kr/konjiam/cam05.stream/playlist.m3u8'),
       (2, '초중급 베이스', 4, NULL, 'http://konjiam.live.cdn.cloudn.co.kr/konjiam/cam03.stream/playlist.m3u8'),
       (2, '중상급 베이스', 5, NULL, 'http://konjiam.live.cdn.cloudn.co.kr/konjiam/cam04.stream/playlist.m3u8'),

       (8, '레인보우 정상', 1, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam05.stream/chunklist.m3u8'),
       (8, '골드슬로프 정상', 2, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam15.stream/chunklist.m3u8'),
       (8, '레인보우 전경', 3, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam10.stream/chunklist.m3u8'),
       (8, '골드 슬로프 전경', 4, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam14.stream/chunklist.m3u8'),
       (8, '메가그린 슬로프', 5, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam07.stream/chunklist.m3u8'),
       (8, '레드 슬로프', 6, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam03.stream/chunklist.m3u8'),

       (4, '알프하우스', 1, '정상', NULL),
       (4, '서브하우스', 2, '초급 (A,B,A1 리프트)', NULL),
       (4, '스키하우스', 3, '중상급 (C,D,E리프트)', NULL),

       (6, '스키베이스', 1, NULL, NULL),
       (6, '불새마루존', 2, NULL, NULL),
       (6, '팽귄 슬로프', 3, NULL, NULL),
       (6, '파노라마', 4, NULL, NULL),
       (6, '몽블랑 정상', 5, NULL, NULL),
       (6, '스페로우', 6, NULL, NULL),
       (6, '챔피온', 7, NULL, NULL),

       (1, '레몬 탑승장', 1, '초급', NULL),
       (1, '오렌지 탑승장', 2, '초급', NULL),
       (1, '뉴오렌지 탑승장', 3, '초급', NULL),
       (1, '블루 탑승장', 4, '중급', NULL),
       (1, '5번 슬로프', 5, '상급', NULL),
       (1, '실버 탑승장', 6, '중급', NULL),
       (1, '지산 전경', 7, NULL, NULL),

       (3, '스키월드 정상', 1, NULL, NULL),
       (3, '테크노 상단', 2, NULL, NULL),
       (3, '클래식', 3, NULL, NULL),
       (3, '힙합', 4, NULL, NULL),
       (3, '펑키', 5, NULL, NULL),
       (3, '발라드', 6, NULL, NULL),
       (3, '테크노하단', 7, NULL, NULL),
       (3, '재즈', 8, NULL, NULL),
       (3, '블루스', 9, NULL, NULL),
       (3, '슬로프 전경', 10, NULL, NULL),

       (5, '정상광장', 1, NULL, 'https://live.wellihillipark.com/wellihillipark/_definst_/cam03.stream/playlist.m3u8'),
       (5, '하프파이프', 2, NULL, 'https://live.wellihillipark.com/wellihillipark/_definst_/cam04.stream/playlist.m3u8'),
       (5, '패밀리슬로프', 3, NULL, 'https://live.wellihillipark.com/wellihillipark/_definst_/cam05.stream/playlist.m3u8'),
       (5, '야외광장', 4, NULL, 'https://live.wellihillipark.com/wellihillipark/_definst_/cam06.stream/playlist.m3u8'),
       (5, 'A1/A3 슬로프', 5, NULL, 'https://live.wellihillipark.com/wellihillipark/_definst_/cam07.stream/playlist.m3u8'),

       (10, '베이직 슬로프', 1, '초급', 'https://www.edenvalley.co.kr/CS/cam_pop1.asp'),
       (10, '쥬피터 슬로프', 2, '중급', NULL),
       (10, '우라누스 슬로프', 3, '중급', NULL),
       (10, '슬로프 광장', 4, '하단 (에덴,벨리,아담 리프트)', NULL),

       (9, '설천상단 슬로프', 1, '최상급', 'http://muju.live.cdn.cloudn.co.kr/mujuresort/_definst_/cam06.stream/playlist.m3u8'),
       (9, '설천봉 정상', 2, '최상급', 'http://muju.live.cdn.cloudn.co.kr/mujuresort/_definst_/cam07.stream/playlist.m3u8'),
       (9, '모차르트, 미뉴에트', 3, '최상급 하단',
        'http://muju.live.cdn.cloudn.co.kr/mujuresort/_definst_/cam08.stream/playlist.m3u8'),
       (9, '만선봉 정상', 4, '상급', 'http://muju.live.cdn.cloudn.co.kr/mujuresort/_definst_/cam02.stream/playlist.m3u8'),
       (9, '하이디 하우스', 5, '전등급', 'http://muju.live.cdn.cloudn.co.kr/mujuresort/_definst_/cam03.stream/playlist.m3u8'),
       (9, '서역기행, 썬다운', 6, '중상급', 'http://muju.live.cdn.cloudn.co.kr/mujuresort/_definst_/cam04.stream/playlist.m3u8'),
       (9, '만선 하우스', 7, '하단 (크루지,요트, 보트 리프트)',
        'http://muju.live.cdn.cloudn.co.kr/mujuresort/_definst_/cam01.stream/playlist.m3u8'),
       (9, '설천 하우스', 8, '하단 (코러스 에코 리프트)',
        'http://muju.live.cdn.cloudn.co.kr/mujuresort/_definst_/cam05.stream/playlist.m3u8'),

       (11, '으뜸마루', 1, NULL, NULL),
       (11, '버금마루', 2, NULL, NULL);


-- 현재 날씨 정보 insert
INSERT INTO current_weather (resort_id, temperature, max_temp, min_temp, feels_like, description, `condition`)
VALUES (1, 3, 5, -2, 0, '맑은 날씨', '맑음'),
       (2, 2, 4, -3, -1, '흐린 날씨', '흐림'),
       (3, 1, 3, -4, -3, '눈이 오는 날씨', '눈'),
       (4, 0, 2, -5, -4, '비가 오는 날씨', '비'),
       (5, -1, 1, -6, -5, '안개 낀 날씨', '안개'),
       (6, -2, 0, -7, -6, '맑은 날씨', '맑음'),
       (7, -3, -1, -8, -7, '눈이 오는 날씨', '눈'),
       (8, -4, -2, -9, -8, '흐리고 비', '흐리고 비'),
       (9, -5, -3, -10, -9, '눈이 오는 날씨', '눈'),
       (10, -6, -4, -11, -10, '맑은 날씨', '맑음'),
       (11, -7, -5, -12, -11, '안개 낀 날씨', '안개');

-- 주간 날씨 정보 insert
INSERT INTO daily_weather (resort_id, forecast_date, day_of_week, precipitation_chance, max_temp, min_temp,
                           `condition`, d_day)
VALUES (1, CURDATE(), '월요일', 10, 5, -2, '맑음', 0),
       (1, CURDATE() + INTERVAL 1 DAY, '화요일', 20, 6, -1, '흐림', 1),
       (1, CURDATE() + INTERVAL 2 DAY, '수요일', 30, 4, -3, '흐리고 비', 2),

       (2, CURDATE(), '월요일', 15, 4, -3, '흐림', 0),
       (2, CURDATE() + INTERVAL 1 DAY, '화요일', 25, 5, -2, '흐리고 비', 1),
       (2, CURDATE() + INTERVAL 2 DAY, '수요일', 35, 3, -4, '눈', 2),

       (3, CURDATE(), '월요일', 20, 3, -4, '눈', 0),
       (3, CURDATE() + INTERVAL 1 DAY, '화요일', 30, 2, -5, '비', 1),
       (3, CURDATE() + INTERVAL 2 DAY, '수요일', 40, 1, -6, '흐림', 2),

       (4, CURDATE(), '월요일', 5, 2, -5, '맑음', 0),
       (4, CURDATE() + INTERVAL 1 DAY, '화요일', 10, 1, -6, '맑음', 1),
       (4, CURDATE() + INTERVAL 2 DAY, '수요일', 15, 0, -7, '흐림', 2),

       (5, CURDATE(), '월요일', 12, 1, -6, '흐림', 0),
       (5, CURDATE() + INTERVAL 1 DAY, '화요일', 22, 0, -7, '비', 1),
       (5, CURDATE() + INTERVAL 2 DAY, '수요일', 32, -1, -8, '안개', 2),

       (6, CURDATE(), '월요일', 18, 0, -7, '맑음', 0),
       (6, CURDATE() + INTERVAL 1 DAY, '화요일', 28, -1, -8, '맑음', 1),
       (6, CURDATE() + INTERVAL 2 DAY, '수요일', 38, -2, -9, '흐림', 2),

       (7, CURDATE(), '월요일', 25, -1, -8, '눈', 0),
       (7, CURDATE() + INTERVAL 1 DAY, '화요일', 35, -2, -9, '비', 1),
       (7, CURDATE() + INTERVAL 2 DAY, '수요일', 45, -3, -10, '눈', 2),

       (8, CURDATE(), '월요일', 8, -2, -9, '맑음', 0),
       (8, CURDATE() + INTERVAL 1 DAY, '화요일', 18, -3, -10, '맑음', 1),
       (8, CURDATE() + INTERVAL 2 DAY, '수요일', 28, -4, -11, '흐림', 2),

       (9, CURDATE(), '월요일', 22, -3, -10, '흐림', 0),
       (9, CURDATE() + INTERVAL 1 DAY, '화요일', 32, -4, -11, '흐리고 비', 1),
       (9, CURDATE() + INTERVAL 2 DAY, '수요일', 42, -5, -12, '눈', 2),

       (10, CURDATE(), '월요일', 15, -4, -11, '맑음', 0),
       (10, CURDATE() + INTERVAL 1 DAY, '화요일', 25, -5, -12, '맑음', 1),
       (10, CURDATE() + INTERVAL 2 DAY, '수요일', 35, -6, -13, '흐림', 2),

       (11, CURDATE(), '월요일', 18, -5, -12, '안개', 0),
       (11, CURDATE() + INTERVAL 1 DAY, '화요일', 28, -6, -13, '안개', 1),
       (11, CURDATE() + INTERVAL 2 DAY, '수요일', 38, -7, -14, '흐림', 2);

INSERT INTO hourly_weather (resort_id, forecast_time, temperature, precipitation_chance, `condition`)
VALUES (1, '2024-10-04 08:00:00', -2, 20, '맑음'),
       (1, '2024-10-04 10:00:00', 0, 30, '흐림'),
       (1, '2024-10-04 12:00:00', 2, 40, '흐리고 비'),
       (1, '2024-10-04 14:00:00', 0, 50, '비'),
       (1, '2024-10-04 16:00:00', -2, 60, '눈'),
       (1, '2024-10-04 18:00:00', -4, 70, '눈'),
       (1, '2024-10-04 20:00:00', -6, 80, '흐림'),
       (1, '2024-10-04 22:00:00', -8, 90, '안개'),
       (1, '2024-10-05 00:00:00', -10, 80, '맑음'),
       (1, '2024-10-05 02:00:00', -12, 70, '맑음');

