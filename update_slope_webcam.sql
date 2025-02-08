USE ski_db;

TRUNCATE TABLE webcams;
TRUNCATE TABLE slopes;

-- 지산 리조트 슬로프 정보 (resort_id = 1)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (1, '레몬1', '초급', FALSE, FALSE, FALSE, 1),
       (1, '레몬1-1', '초급', FALSE, FALSE, FALSE, 1),
       (1, '오렌지2', '중급', FALSE, FALSE, FALSE, 2),
       (1, '오렌지3', '중급', FALSE, FALSE, FALSE, 2),
       (1, '뉴오렌지', '상급', FALSE, FALSE, FALSE, 2),
       (1, '블루5', '상급', FALSE, FALSE, FALSE, 3),
       (1, '실버6', '최상급', FALSE, FALSE, FALSE, 5),
       (1, '실버7', '중상급', FALSE, FALSE, FALSE, 5);

-- 곤지암 스키장 슬로프 정보 (resort_id = 2)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (2, '휘센', '초급', FALSE, FALSE, TRUE, 4),
       (2, '와이낫', '초중급', FALSE, FALSE, FALSE, 3),
       (2, '그램1', '상급', FALSE, FALSE, FALSE, 1),
       (2, '그램2', '중급', FALSE, FALSE, FALSE, 1),
       (2, 'CNP1', '초중급', FALSE, FALSE, FALSE, 1),
       (2, 'CNP2', '상급', FALSE, FALSE, FALSE, 5),
       (2, '씽큐1', '중상급', FALSE, FALSE, FALSE, 2),
       (2, '씽큐2', '초중급', FALSE, FALSE, FALSE, 5),
       (2, '씽큐3', '중상급', FALSE, FALSE, FALSE, 2),
       (2, '씽큐브릿지', '상급', FALSE, FALSE, FALSE, NULL);

-- 비발디파크 슬로프 정보 (resort_id = 3)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (3, '발라드', '초급', FALSE, FALSE, FALSE, 2),
       (3, '레게', '중상급', FALSE, FALSE, FALSE, NULL),
       (3, '클래식', '중상급', FALSE, FALSE, FALSE, NULL),
       (3, '펑키', '중상급', FALSE, FALSE, FALSE, NULL),
       (3, '테크노1', '중상급', FALSE, FALSE, FALSE, 5),
       (3, '테크노2', '상급', FALSE, FALSE, FALSE, 9),
       (3, '힙합1', '중상급', FALSE, FALSE, FALSE, 6),
       (3, '힙합2', '중상급', FALSE, FALSE, FALSE, 6),
       (3, '블루스', '초급', FALSE, FALSE, FALSE, 4),
       (3, '재즈', '중급', FALSE, FALSE, FALSE, 1);

-- 엘리시안 강촌 슬로프 정보 (resort_id = 4)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (4, '팬더', '초급', FALSE, FALSE, FALSE, 2),
       (4, '래빗', '초급', FALSE, FALSE, FALSE, 2),
       (4, '드래곤', '중급', FALSE, FALSE, FALSE, 1),
       (4, '호스', '중급', FALSE, FALSE, FALSE, 1),
       (4, '페가수스', '중급', FALSE, FALSE, FALSE, 3),
       (4, '제브라', '중급', FALSE, FALSE, FALSE, 3),
       (4, '디어', '중급', FALSE, FALSE, FALSE, NULL),
       (4, '퓨마', '중급', FALSE, FALSE, FALSE, 1),
       (4, '래퍼드', '상급', FALSE, FALSE, FALSE, 3),
       (4, '제규어', '상급', FALSE, FALSE, FALSE, 3);

-- 웰리힐리파크 슬로프 정보 (resort_id = 5)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (5, '알파1', '상급', FALSE, FALSE, TRUE, 2),
       (5, '알파2', '초급', FALSE, FALSE, FALSE, 2),
       (5, '알파3', '초급', FALSE, FALSE, FALSE, 2),
       (5, '브라보1', '중급', FALSE, FALSE, FALSE, NULL),
       (5, '브라보2', '중급', FALSE, FALSE, FALSE, NULL),
       (5, '델타1', '초급', FALSE, FALSE, FALSE, 4),
       (5, '델타2', '최상급', FALSE, FALSE, FALSE, 4),
       (5, '델타+', '중급', FALSE, FALSE, FALSE, 6),
       (5, '에코1', '최상급', FALSE, FALSE, FALSE, 5),
       (5, '에코2', '상급', FALSE, FALSE, FALSE, NULL),
       (5, '에코3', '최상급', FALSE, FALSE, FALSE, NULL),
       (5, '챌린지1', '상급', FALSE, FALSE, FALSE, 5),
       (5, '챌린지2', '상급', FALSE, FALSE, FALSE, NULL),
       (5, '챌린지3', '최상급', FALSE, FALSE, FALSE, NULL),
       (5, '챌린지4', '상급', FALSE, FALSE, FALSE, 5),
       (5, '챌린지5', '상급', FALSE, FALSE, FALSE, 5),
       (5, '스타 익스프레스1', '중급', FALSE, FALSE, FALSE, NULL),
       (5, '스타 익스프레스2', '초급', FALSE, FALSE, FALSE, 6);

-- 휘닉스파크 슬로프 정보 (resort_id = 6)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (6, '스패로우', '초급', FALSE, FALSE, FALSE, 1),
       (6, '펭귄', '초급', FALSE, FALSE, FALSE, 5),
       (6, '호크2', '초급', FALSE, FALSE, FALSE, NULL),
       (6, '도도(강습전용)', '초급', TRUE, TRUE, FALSE, 2),
       (6, '키위', '중급', FALSE, FALSE, FALSE, 3),
       (6, '파노라마', '중급', FALSE, FALSE, FALSE, NULL),
       (6, '호크', '중급', TRUE, TRUE, FALSE, 1),
       (6, '밸리', '중상급', FALSE, FALSE, FALSE, NULL),
       (6, '듀크(이상호)', '중상급', FALSE, FALSE, FALSE, 3),
       (6, '슬로프스타일(SS/SBS)', '중상급', FALSE, FALSE, FALSE, NULL),
       (6, '챔피언', '상급', FALSE, FALSE, FALSE, NULL),
       (6, '환타지', '상급', FALSE, FALSE, FALSE, NULL),
       (6, '디지', '최상급', FALSE, FALSE, FALSE, 5),
       (6, '모글(MO)', '최상급', FALSE, FALSE, FALSE, NULL),
       (6, '파라다이스', '최상급', FALSE, FALSE, FALSE, NULL),
       (6, '익스트림파크(킥커존)', '파크', FALSE, FALSE, FALSE, NULL),
       (6, '익스트림파크(파크존)', '파크', FALSE, FALSE, FALSE, NULL),
       (6, '하이파이브(HP)', '파크', FALSE, FALSE, FALSE, NULL);

-- 하이윈 스키장 슬로프 정보 (resort_id = 7)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (7, '제우스 1', '초급', FALSE, FALSE, FALSE, NULL),
       (7, '제우스 2', '초급', FALSE, FALSE, FALSE, 1),
       (7, '제우스 3', '초급', FALSE, FALSE, FALSE, 13),
       (7, '제우스 3-1', '초급', FALSE, FALSE, FALSE, NULL),
       (7, '빅토리아 1', '상급', FALSE, FALSE, FALSE, 12),
       (7, '빅토리아 2', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '빅토리아 3', '최상급', FALSE, FALSE, FALSE, 9),
       (7, '헤라 1', '중급', FALSE, FALSE, FALSE, NULL),
       (7, '헤라 2', '중상급', FALSE, FALSE, FALSE, 2),
       (7, '헤라 3', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '아폴로 1', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '아폴로 2', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '아폴로 3', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '아폴로 4', '상급', FALSE, FALSE, FALSE, 15),
       (7, '아폴로 5', '최상급', FALSE, FALSE, FALSE, NULL),
       (7, '아폴로 6', '상급', FALSE, FALSE, FALSE, NULL),
       (7, '아테나 1', '초급', FALSE, FALSE, FALSE, 4),
       (7, '아테나 2', '중급', FALSE, FALSE, FALSE, 6),
       (7, '아테나 3', '초급', FALSE, FALSE, FALSE, NULL),
       (7, '아테나 3-1', '초급', FALSE, FALSE, FALSE, NULL);

-- 용평스키장 모나 슬로프 정보 (resort_id = 8)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (8, '옐로우', '초급', FALSE, FALSE, FALSE, 3),
       (8, '뉴옐로우', '초급', FALSE, FALSE, FALSE, 3),
       (8, '메가그린', '초중급', FALSE, FALSE, FALSE, NULL),
       (8, '핑크', '초중급', TRUE, TRUE, FALSE, 4),
       (8, '레드파라다이스', '중급', FALSE, FALSE, FALSE, NULL),
       (8, '골드파라다이스', '중급', FALSE, FALSE, FALSE, NULL),
       (8, '실버파라다이스', '중급', FALSE, FALSE, FALSE, NULL),
       (8, '레인보우파라다이스', '중급', FALSE, FALSE, FALSE, NULL),
       (8, '골드환타스틱', '중상급', FALSE, FALSE, FALSE, NULL),
       (8, '뉴레드', '상급', FALSE, FALSE, FALSE, NULL),
       (8, '블루', '상급', FALSE, FALSE, FALSE, NULL),
       (8, '골드밸리', '상급', FALSE, FALSE, FALSE, NULL),
       (8, '레인보우4', '상급', FALSE, FALSE, FALSE, NULL),
       (8, '레드', '최상급', FALSE, FALSE, FALSE, 5),
       (8, '실버', '최상급', FALSE, FALSE, FALSE, NULL),
       (8, '뉴골드', '최상급', FALSE, FALSE, FALSE, NULL),
       (8, '레인보우 1', '최상급', FALSE, FALSE, FALSE, NULL),
       (8, '레인보우 2', '최상급', FALSE, FALSE, FALSE, NULL),
       (8, '레인보우 3', '최상급', FALSE, FALSE, FALSE, NULL),
       (8, '드래곤파크(터레인파크)', '익스트림', FALSE, FALSE, FALSE, NULL);

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

-- 오투리조트 슬로프 정보 (resort_id = 11)
INSERT INTO slopes (resort_id, `name`, difficulty, is_day_operating, is_night_operating, is_late_night_operating,
                    webcam_number)
VALUES (11, '드림슬로프 1', '초급', FALSE, FALSE, FALSE, NULL),
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
       (11, '하프파이프', '파크', FALSE, FALSE, FALSE, NULL);

-- 웹캠 정보 insert
INSERT INTO webcams (resort_id, `name`, number, description, url)
-- 지산 웹캠 오픈 준비중 : https://www.jisanresort.co.kr/m/ski/slopes/webcam.asp
VALUES (1, '레몬 탑승장', 1, '초급', NULL),
       (1, '오렌지 탑승장', 2, '초급', NULL),
       (1, '뉴오렌지 탑승장', 3, '초급', NULL),
       (1, '블루 탑승장', 4, '중급', NULL),
       (1, '5번 슬로프', 5, '상급', NULL),
       (1, '실버 탑승장', 6, '중급', NULL),
       (1, '지산 전경', 7, NULL, NULL),

       (2, '정상 휴게소', 1, '정상', 'http://konjiam.live.cdn.cloudn.co.kr/konjiam/cam01.stream/playlist.m3u8'),
       (2, '정상부 슬로프', 2, NULL, 'http://konjiam.live.cdn.cloudn.co.kr/konjiam/cam02.stream/playlist.m3u8'),
       (2, '중간 슬로프', 3, NULL, 'http://konjiam.live.cdn.cloudn.co.kr/konjiam/cam05.stream/playlist.m3u8'),
       (2, '초중급 베이스', 4, NULL, 'http://konjiam.live.cdn.cloudn.co.kr/konjiam/cam03.stream/playlist.m3u8'),
       (2, '중상급 베이스', 5, NULL, 'http://konjiam.live.cdn.cloudn.co.kr/konjiam/cam04.stream/playlist.m3u8'),

       -- 비발디 : 이미지로 url 전달됨
       (3, '재즈', 1, NULL, NULL),
       (3, '발라드', 2, NULL, NULL),
       (3, '블루스', 4, NULL, NULL),
       (3, '테크노', 5, NULL, NULL),
       (3, '힙합', 6, NULL, NULL),
       (3, '슬로프 전경', 8, NULL, NULL),
       (3, '스키장 정상', 9, NULL, NULL),

       -- 유튜브 웹캠
       (4, '알프하우스', 1, '정상', NULL),
       (4, '서브하우스', 2, '초급 (A,B,A1 리프트)', NULL),
       (4, '스키하우스', 3, '중상급 (C,D,E리프트)', NULL),

       (5, '알파', 2, NULL, 'https://live.wellihillipark.com/wellihillipark/_definst_/cam02.stream/playlist.m3u8'),
       (5, '베이스', 3, NULL, 'https://live.wellihillipark.com/wellihillipark/_definst_/cam03.stream/playlist.m3u8'),
       (5, '리조트 전경', 4, NULL, 'https://live.wellihillipark.com/wellihillipark/_definst_/cam04.stream/playlist.m3u8'),
       (5, '정상광장', 5, NULL, 'https://live.wellihillipark.com/wellihillipark/_definst_/cam05.stream/playlist.m3u8'),
       (5, '패밀리', 6, NULL, 'https://live.wellihillipark.com/wellihillipark/_definst_/cam06.stream/playlist.m3u8'),
       (5, '워터플래닛', 7, NULL, 'https://live.wellihillipark.com/wellihillipark/_definst_/cam07.stream/playlist.m3u8'),

       (6, '호크,스패로우', 1, NULL, 'https://streaming.phoenixhnr.co.kr/hls/yh_02.m3u8'),
       (6, '도도', 2, NULL, 'https://streaming.phoenixhnr.co.kr/hls/sp_01.m3u8'),
       (6, '불새마루', 3, NULL, 'https://streaming.phoenixhnr.co.kr/hls/ht_01.m3u8'),
       (6, '베이스', 4, NULL, 'https://streaming.phoenixhnr.co.kr/hls/bc_02.m3u8'),
       (6, '펭귄', 5, NULL, 'https://streaming.phoenixhnr.co.kr/hls/bc_01.m3u8'),
       (6, '스노우 빌리지', 6, NULL, 'https://streaming.phoenixhnr.co.kr/hls/yh_01.m3u8'),

       (7, '제우스2번 슬로프 입구', 1, NULL, 'http://59.30.12.195:1935/live/_definst_/ch1.stream/playlist.m3u8'),
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

       (8, '발왕산 스카이워크', 1, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam14.stream/playlist.m3u8'),
       (8, '발왕산 천년주목숲길(동쪽)', 2, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam15.stream/chunklist.m3u8'),
       (8, '옐로우 슬로프', 3, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam03.stream/chunklist.m3u8'),
       (8, '핑크 슬로프', 4, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam07.stream/playlist.m3u8'),
       (8, '베이스전경/레드 슬로프', 5, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam10.stream/chunklist.m3u8'),
       (8, '모나용평진입로', 6, NULL, 'https://live.yongpyong.co.kr/Ycam1/cam05.stream/chunklist.m3u8'),

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

       (10, '베이직 슬로프', 1, '초급', 'https://rtsp.me/embed/KQhdNabr/'),
       (10, '쥬피터 슬로프', 2, '중급', NULL),
       (10, '우라누스 슬로프', 3, '중급', NULL),
       (10, '슬로프 광장', 4, '하단 (에덴,벨리,아담 리프트)', 'https://rtsp.me/embed/Y93378YZ/'),

       (11, '으뜸마루', 1, NULL, NULL),
       (11, '버금마루', 2, NULL, NULL);
