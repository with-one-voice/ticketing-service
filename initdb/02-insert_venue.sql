-- venue_db에 생성되는 p_venues 테이블에 값 넣는 쿼리문

INSERT INTO p_venues (venue_id, name, location, description, total_seat_count,
                      created_at, created_by, updated_at, updated_by, deleted_at, deleted_by)
VALUES
-- 1. 고척 스카이돔
('ec9c1e3e-15a6-49a1-85fc-1f017b8f1012', '고척 스카이돔', '서울특별시 구로구 경인로 430 (고척동)',
 '국내 최초의 돔형 야구장이자 대형 콘서트 및 스포츠 이벤트가 열리는 복합 문화 공간입니다.',
 16500, now(), NULL, now(), NULL, NULL, NULL),

-- 2. KSPO DOME
('31f2b964-61ae-4e94-8496-7c6f56c07f99', 'KSPO DOME (올림픽 체조경기장)', '서울특별시 송파구 올림픽로 424',
 '1988 서울올림픽 체조 경기장으로 시작되어, 현재는 국내외 아티스트들의 대형 콘서트 및 팬미팅이 열리는 대표적인 실내 공연장입니다.',
 15000, now(), NULL, now(), NULL, NULL, NULL),

-- 3. 부산 벡스코 오디토리움
('a3a574bc-f07e-4bb5-893a-6c97eac0dca5', '부산 벡스코 오디토리움', '부산광역시 해운대구 APEC로 55',
 '국제 전시 및 문화행사가 활발히 열리는 벡스코 내 다목적 공연장으로, 클래식부터 K-POP까지 다양한 공연이 열립니다.',
 4000, now(), NULL, now(), NULL, NULL, NULL),

-- 4. 대구 EXCO 컨벤션홀
('5b87f2a1-9ce3-438a-b6c1-fb7582b0e248', '대구 EXCO 컨벤션홀', '대구광역시 북구 엑스코로 10',
 '대구의 대표 전시장 내 위치한 실내 공연장으로, 콘서트, 전시, 포럼 등 다양한 이벤트가 개최됩니다.',
 3500, now(), NULL, now(), NULL, NULL, NULL),

-- 5. 인천 아트센터 대공연장
('cbf7bfea-c611-4b0f-bd0e-dad1e5f77e3f', '인천 아트센터 대공연장', '인천광역시 연수구 아트센터대로 222',
 '고품격 클래식 공연과 뮤지컬이 열리는 인천 대표 문화공간으로, 음향과 무대 시설이 뛰어난 공연장입니다.',
 2800, now(), NULL, now(), NULL, NULL, NULL);
