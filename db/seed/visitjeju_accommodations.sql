-- VisitJeju one-time normalized accommodation seed
-- Generated at: 2026-07-15T05:31:04.189Z
-- No external API identifiers or credentials are stored.
-- Re-running is idempotent by the existing name + address pair.
SET NAMES utf8mb4;
START TRANSACTION;
INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '선스토리', 'CAMPING', 'EAST', '제주특별자치도 서귀포시 표선면 표선백사로60번길 72', 33.3163300, 126.8377000, '표선에 위치한 나무를 많이 사용해 지은 모던한 분위기의 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ae9defa3-a914-49e8-bb5d-0d58b5aeab58.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '선스토리' AND address = '제주특별자치도 서귀포시 표선면 표선백사로60번길 72');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '어라운드폴리', 'CAMPING', 'EAST', '제주특별자치도 서귀포시 성산읍 서성일로 433', 33.4155451, 126.8357525, '중산간에 자리한, 오늘의 제주를 살아보는 마을형 스테이', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202603/14/889e6387-6538-41a0-b2be-8fbb4f3262fb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '어라운드폴리' AND address = '제주특별자치도 서귀포시 성산읍 서성일로 433');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 베스트힐 글램핑 & 펜션', 'CAMPING', 'EAST', '제주특별자치도 제주시 조천읍 남조로 2109-36', 33.4276176, 126.6665803, '글램핑이 있는 자연 속 팬션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e704ad5d-a6a1-4367-8e28-4e3b3b4cc508.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 베스트힐 글램핑 & 펜션' AND address = '제주특별자치도 제주시 조천읍 남조로 2109-36');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주야자원', 'CAMPING', 'EAST', '제주특별자치도 서귀포시 표선면 풍천로273번길 143-13', 33.3608877, 126.8245549, '2만여평의 야자나무 식물원에 자리한 독립 별장형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4f72474a-f96e-4717-99c5-c0feb841f1c7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주야자원' AND address = '제주특별자치도 서귀포시 표선면 풍천로273번길 143-13');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주캠핑올레', 'CAMPING', 'EAST', '제주도 서귀포시 표선면 토산망동로 15', 33.3101270, 126.7816500, '표선에 위치한 무지개 컨셉의 컨테이너 하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/02ee2c1d-2ea3-4773-a8dd-8f8de4044c53.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주캠핑올레' AND address = '제주도 서귀포시 표선면 토산망동로 15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '플레이스 캠프 제주', 'CAMPING', 'EAST', '제주특별자치도 서귀포시 성산읍 동류암로 20', 33.4499440, 126.9183800, '다채로운 즐거움을 머금은 공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/32d633b1-4775-49ec-bb73-a7f5a347fc10.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '플레이스 캠프 제주' AND address = '제주특별자치도 서귀포시 성산읍 동류암로 20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '게으른소나기', 'GUESTHOUSE', 'EAST', '제주 제주시 구좌읍 계룡길 45-6', 33.5388300, 126.8338700, '약 100년 전통의 제주 돌집을 리모델링한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/853d3d26-ca2f-4213-8e9a-27d12ae0acee.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '게으른소나기' AND address = '제주 제주시 구좌읍 계룡길 45-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '꽃하루방게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 온평포구로62번길 24', 33.4003140, 126.9029500, '제주는 꽃보다 아름다워', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9f9478d3-f493-4a95-9604-509308133aea.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '꽃하루방게스트하우스' AND address = '제주특별자치도 서귀포시 성산읍 온평포구로62번길 24');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '꿈꾸는섬 게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 조천읍 조천3길 27-1', 33.5359500, 126.6349500, '아날로그 감성의 카페와 함께 있는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b3b3bc04-290c-4108-bca2-e99cc40b2604.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '꿈꾸는섬 게스트하우스' AND address = '제주특별자치도 제주시 조천읍 조천3길 27-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '나무이야기 하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 표선면 민속해안로 32', 33.3255768, 126.7999334, '표선에 위치한 바다가 보이는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/78f1616e-7575-459d-92cd-85c8384170e4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '나무이야기 하우스' AND address = '제주특별자치도 서귀포시 표선면 민속해안로 32');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '노낭게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 섭지코지로 62', 33.4376400, 126.9217300, '나누며 소통하는 따뜻한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e7c35361-5de6-43f0-9bde-c93909c00c5e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '노낭게스트하우스' AND address = '제주특별자치도 서귀포시 성산읍 섭지코지로 62');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '놀멍쉬멍고르멍 게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 동복남4길 5-4', 33.5506479, 126.7130053, '*저희 민박형 펜션은 조용하고 청정지역 동복리에 위치하고 있으며 신축건물로 깨끗합니다 * 원룸형 9평, 2동으로되어 각동 최대4인이 숙박할수있고 * 부모님 동반하여 2동 모두사용하셔서 오붓하게 우리 가족들만 지낼수있답니다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1e7acd2b-43d6-48df-8599-c1a7cd2b866d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '놀멍쉬멍고르멍 게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 동복남4길 5-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '다노이게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 동복로 51', 33.5531955, 126.7117668, '제주시 구좌읍 동복리에 위치한 다노이숙소&카페', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2ff269f8-ab72-445f-b783-eb048fe17e0d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '다노이게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 동복로 51');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '다락마마', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 상하도길 46-16', 33.5171550, 126.8662957, '구좌읍에 위치한 독채형(다락방) 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/29/fbadc56d-c457-40fb-aaa9-3a7c96565438.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '다락마마' AND address = '제주특별자치도 제주시 구좌읍 상하도길 46-16');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '달집스테이', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 종달논길 32-3', 33.4936870, 126.9010540, '농가주택을 개조한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/37da2830-0e3c-447f-87a2-7aa80194588b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '달집스테이' AND address = '제주특별자치도 제주시 구좌읍 종달논길 32-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '도시락게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 성산중앙로 48', 33.4639050, 126.9341900, '옛날 도시락 형태의 아침식사를 즐길 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/cab1589f-5c03-4980-98d5-89615b17a641.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '도시락게스트하우스' AND address = '제주특별자치도 서귀포시 성산읍 성산중앙로 48');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '동촌하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 종달논길 61-4', 33.4913500, 126.8999400, '올레1코스 중간지점이자 21코스 끝지점에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/384ec14e-75d1-4a2a-97c3-02e71f39d568.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '동촌하우스' AND address = '제주특별자치도 제주시 구좌읍 종달논길 61-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '두베하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 상하도길 46-12', 33.5169220, 126.8665540, '조용하고 편안한 우리 집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2b48f6ab-40d7-484d-825a-e343bb9db5c5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '두베하우스' AND address = '제주특별자치도 제주시 구좌읍 상하도길 46-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라라게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 조천읍 교래4길 74', 33.4180631, 126.6534847, '여성 전용 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202309/01/89960e0e-4c8a-44db-b123-c5b32be01d1a.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라라게스트하우스' AND address = '제주특별자치도 제주시 조천읍 교래4길 74');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라메종베니', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 신양로122번길 4-6', 33.4397954, 126.8990639, '축복받은 집이라는 의미를 담고 있는 신양해수욕장과 섭지코지 앞 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/615daaec-abb4-435e-8408-d78e8f67cb5b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라메종베니' AND address = '제주특별자치도 서귀포시 성산읍 신양로122번길 4-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '레프트핸더 스테이', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 상도북2길 17-12', 33.5201200, 126.8649600, '구좌에 위치하며 평등을 추구하는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/804371a8-4b4f-4021-8179-a6e37df797c2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '레프트핸더 스테이' AND address = '제주특별자치도 제주시 구좌읍 상도북2길 17-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '로지게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 조천읍 신흥관전길 58', 33.5454980, 126.6489300, '조천에 위치한 조용한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0a257892-ec70-46ae-bdc7-7c6c46a8c6f3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '로지게스트하우스' AND address = '제주특별자치도 제주시 조천읍 신흥관전길 58');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '메이풀하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 조천읍 곱은달서길 165', 33.5011023, 126.6616581, '각종 휴양레포츠를 즐길수 있는 함덕해수욕장과 섭지코지, 성산일출봉과 근접한 북제주군에 위치한 메이풀유스호스텔입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9c99123f-6592-4234-a9f5-c618e89d8eaf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '메이풀하우스' AND address = '제주특별자치도 제주시 조천읍 곱은달서길 165');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '메종드오조락', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 오조로 140', 33.4579400, 126.8903000, '나를 비춰보는 즐거움이 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b0f08118-c970-4f3a-9df8-9ff521014c62.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '메종드오조락' AND address = '제주특별자치도 서귀포시 성산읍 오조로 140');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '몬딱게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 세화서길 7-31', 33.5189970, 126.8527760, '매일 밤, 별을 볼 수 있는 공간이 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/afca2f84-5ccb-4c67-af3d-75906ec5b90a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '몬딱게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 세화서길 7-31');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '물고기나무게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 중산간동로 4204-14', 33.3789020, 126.8409600, '카페와 공방이 함께 운영되는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9cfc303d-1d33-437f-a9e6-ef6cf40a7e7d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '물고기나무게스트하우스' AND address = '제주특별자치도 서귀포시 성산읍 중산간동로 4204-14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '미쓰홍당무하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 평대4길 20-1', 33.5292446, 126.8368205, '전통적 옛 돌집을 리모델링하여 시골 마을과 조화를 이룬 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a77dc4e9-5832-4334-84eb-cf01718a2b21.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '미쓰홍당무하우스' AND address = '제주특별자치도 제주시 구좌읍 평대4길 20-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '뱅디가름게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 서성일로1222번길 17', 33.4457170, 126.9098400, '아늑하고 편안한 우리집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/845fc4b9-74ea-4c3b-b7ac-5a3df1a445cd.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '뱅디가름게스트하우스' AND address = '제주특별자치도 서귀포시 성산읍 서성일로1222번길 17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '벼리게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 면수1길 27', 33.5238880, 126.8645600, '세화바다가 한눈에 들어오는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9c328833-fc7d-43c2-ae9b-8e2e4a633811.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '벼리게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 면수1길 27');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '보리게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 송당6길 38-1', 33.4682100, 126.7837600, '소원을 비는 마을에 위치한 조용한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2ed4abfc-286f-4fa3-ab87-f4bc9468b2ca.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '보리게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 송당6길 38-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '블랑게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 표선면 표선관정로43번길 76-31', 33.3335600, 126.8266450, '표선인근이면 어디든 픽업서비스가 가능한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2a53c800-ede6-4799-b016-bda333464b29.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '블랑게스트하우스' AND address = '제주특별자치도 서귀포시 표선면 표선관정로43번길 76-31');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '빠담빠담게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 표선면 돈오름로 198-4', 33.3296240, 126.8182700, '새로운 인연이 시작되는 쾌적한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/aeb03d71-b0d0-479e-96af-7323685fa2d8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '빠담빠담게스트하우스' AND address = '제주특별자치도 서귀포시 표선면 돈오름로 198-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '선흘 동백동산 에코촌 유스호스텔', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 조천읍 북흘로 376-9 (선흘리) 제주시 북흘로 376-9', 33.5168804, 126.7040937, '자연을 고려한 친환경 제로에너지 자립형 숙박시설로 청소년과 일반일들에게 숙박 편의와 여행정보를 제공하는 청소년 수련시설입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202404/09/4ddfd018-5a99-4fc2-9f62-454c4074889d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '선흘 동백동산 에코촌 유스호스텔' AND address = '제주특별자치도 제주시 조천읍 북흘로 376-9 (선흘리) 제주시 북흘로 376-9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '성산게스트하우스 킴스캐빈', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 시흥상동로53번길 88-54', 33.4744091, 126.8870595, '성산일출봉 근처 조용한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/49cd59aa-7e94-41a2-bf4e-c0e8adab21f4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '성산게스트하우스 킴스캐빈' AND address = '제주특별자치도 서귀포시 성산읍 시흥상동로53번길 88-54');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '수상한소금밭게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 종달동길 36-10', 33.4904440, 126.9025600, '구좌읍 종달리에 위치한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c974ea63-f347-43ef-8227-6d1d10f3542d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '수상한소금밭게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 종달동길 36-10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아프리카게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 조천읍 신흥로2길 33', 33.5489460, 126.6516600, '얽매이지 않고 자유로운 영혼들의 작은 아지트 같은 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f3d66843-2164-4f88-8c6e-06d1cbdf9004.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아프리카게스트하우스' AND address = '제주특별자치도 제주시 조천읍 신흥로2길 33');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '안녕김녕sea', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 구좌해안로 178', 33.5398407, 126.7465830, '김녕 요트항 바닷가에 예술적 감성의 건축디자인과 미니멀리즘 인테리어, 아름다운 오션뷰를 감상할 수 있는 게스트하우스.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201808/20/8775e251-84ca-48fd-9fd2-495bea5b8a65.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '안녕김녕sea' AND address = '제주특별자치도 제주시 구좌읍 구좌해안로 178');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '안녕프로젝트 게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 동복로2길 12', 33.5520211, 126.7124099, '제주시 구좌읍 동복리(올레19코스) 해안가 마을에 위치한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2425c403-54c0-434f-bc0b-94d04d8ce7e5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '안녕프로젝트 게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 동복로2길 12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '언니게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 대수길 10-12', 33.5364650, 126.8394240, '제주에 제대로 녹아들 수 있는 하룻밤', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/14d3932d-d662-4dd8-a502-5b14167e211a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '언니게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 대수길 10-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '에바다라이더하우스.공방', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 고성중앙로 7-8', 33.4445754, 126.9095025, '자전거 여행과 공방체험을 함께 할 수 있는 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fb492275-6677-4e45-9560-21c381d178a0.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '에바다라이더하우스.공방' AND address = '제주특별자치도 서귀포시 성산읍 고성중앙로 7-8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '여울목게스트하우스(월정리)', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 월정중길 51', 33.5587000, 126.7941060, '알록달록 자유로움이 묻어나는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e56ed546-7a83-43a0-a762-6fd35d482429.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '여울목게스트하우스(월정리)' AND address = '제주특별자치도 제주시 구좌읍 월정중길 51');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오름게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 충렬로 147-19', 33.5099100, 126.8466100, '제주 동부 세화리에 위치한, 시골집 같이 푸근한 오름게스트하우스입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202008/24/178918f4-1159-40cf-9c14-57c0f95ee40c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오름게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 충렬로 147-19');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '와락 게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 세평항로 45-4', 33.5275000, 126.8547100, '제주시 구좌읍 평대리와 세화리 경계에 위치한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/887c2c81-5107-449b-ae9d-4edf094d306a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '와락 게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 세평항로 45-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '우리두리게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 표선면 민속해안로119번길 10-1', 33.3074070, 126.8127700, '제주도 표선면에 위치한 컨테이너로 지어진 컬러풀한 이색숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a98dd285-6f21-450e-a680-2f8fbe010d55.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '우리두리게스트하우스' AND address = '제주특별자치도 서귀포시 표선면 민속해안로119번길 10-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '우리희망이 게스트하우스', 'GUESTHOUSE', 'EAST', '제주시 조천읍 신흥관전길 60-1', 33.5460170, 126.6495100, '함덕해수욕장 차로 5분거리, 한라산이 보이는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7eadba6e-9469-4036-8c14-3052bb3c518a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '우리희망이 게스트하우스' AND address = '제주시 조천읍 신흥관전길 60-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '유월그리고열두마루', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 한동로1길 38', 33.5356980, 126.8254600, '구좌읍 한동리에 위치한 조용하고 단정한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6ea2d5ba-16e5-4b5b-9ddb-41ad6000a442.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '유월그리고열두마루' AND address = '제주특별자치도 제주시 구좌읍 한동로1길 38');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '이디하우스n카페', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 해녀박물관길 33-2', 33.5239870, 126.8620200, '세화 해변이 보이는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/bae74f65-f059-463f-80bc-7853880b04b3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '이디하우스n카페' AND address = '제주특별자치도 제주시 구좌읍 해녀박물관길 33-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '일출언덕신산게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 환해장성로121번길 21', 33.3842860, 126.8799433, '제주도 시골 마을에 위치해 있으며 일출을 볼 수 있는, 편안하고 안락한 가족적인 분위기의 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0e2169cb-9dae-40fe-a6eb-2c18843ee533.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '일출언덕신산게스트하우스' AND address = '제주특별자치도 서귀포시 성산읍 환해장성로121번길 21');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주도 게스트하우스 파티 바랑쉬 성산점', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 환해장성로 496', 33.4009910, 126.9037231, '제주바다가 펼쳐진 오션뷰와 펍이 있는 루프탑 게스트하우스.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201807/24/dc13252f-918e-4a24-b3fe-1ff74472f23e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주도 게스트하우스 파티 바랑쉬 성산점' AND address = '제주특별자치도 서귀포시 성산읍 환해장성로 496');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주의시간은느리다', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 상도로 30-4', 33.5119970, 126.8642350, '천천히가는 제주의 시간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/334d1608-ec91-4147-a2ad-672192603c11.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주의시간은느리다' AND address = '제주특별자치도 제주시 구좌읍 상도로 30-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '종달스토리', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 종달논길 61-7', 33.4917500, 126.9002100, '즐거움과 휴식이 있는 게스트 하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6362af92-d946-4570-bcce-b73c0e75ac1d.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '종달스토리' AND address = '제주특별자치도 제주시 구좌읍 종달논길 61-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '코시롱게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 성산중앙로37번길 1', 33.4631900, 126.9330700, '성산에 있는 젊은 에너지의 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/322e5685-143d-4d88-889a-902b53245d2f.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '코시롱게스트하우스' AND address = '제주특별자치도 서귀포시 성산읍 성산중앙로37번길 1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '탐탐56', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 상하도길 56', 33.5156600, 126.8667100, '탐라를 탐하다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fc43e733-9495-4916-92e5-f44d80876823.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '탐탐56' AND address = '제주특별자치도 제주시 구좌읍 상하도길 56');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '펠리스타운', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 시흥하동로50번길 23', 33.4744640, 126.9080372, '성산 일출봉에서 3km 떨어진 서귀포 해변의 인근에 있는 아늑한 호스텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8d106058-54f5-44df-90ea-987051e9474e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '펠리스타운' AND address = '제주특별자치도 서귀포시 성산읍 시흥하동로50번길 23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '포비네오두막게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 행원로4길 26', 33.5340986, 126.7954364, '아늑하고도 자유로운 곳, 포비네 오두막', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/08602b95-b950-40da-af2f-0f07fb08c387.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '포비네오두막게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 행원로4길 26');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '표선이레하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 표선면 표선당포로 9', 33.3257520, 126.8436500, '표선해비치해변 앞 아늑한 펜션&게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/604e4822-86cb-42f7-a568-b29569a28306.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '표선이레하우스' AND address = '제주특별자치도 서귀포시 표선면 표선당포로 9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하얀언덕', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 표선면 민속해안로 351', 33.3114780, 126.8340300, '올레 4코스에 자리한 펜션 & 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ca3bba7d-e56e-41d1-9068-3a021b5e4199.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하얀언덕' AND address = '제주특별자치도 서귀포시 표선면 민속해안로 351');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '함덕산티아고게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 조천읍 신촌9길 47-2', 33.5388870, 126.6224700, '바다와 맞닿은 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/70f1c6be-1d46-4a67-994b-9037583ffdc6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '함덕산티아고게스트하우스' AND address = '제주특별자치도 제주시 조천읍 신촌9길 47-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '헌치 게스트하우스', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 비자림로 1627 (송당리) 제주 오석심 공예 명장관', 33.4561819, 126.7673413, '비자림 숲 옆에 위치해 있는 게스트하우스. 한지 공예 상설 전시장과 카페가 마련되어 있어 자연과 문화를 모두 즐길 수 있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/19/ed63289a-0f21-457e-a3cc-5b4004d8080f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '헌치 게스트하우스' AND address = '제주특별자치도 제주시 구좌읍 비자림로 1627 (송당리) 제주 오석심 공예 명장관');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '히든스테이', 'GUESTHOUSE', 'EAST', '제주특별자치도 서귀포시 성산읍 성산중앙로40번길 12', 33.4630168, 126.9344317, '성산일출봉 앞 마을 가운데에 위치한 쾌적하고 휴식이 있는 공간을 지향하는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202208/31/c9faa0b4-720b-4e9c-b848-3d84623d27ef.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '히든스테이' AND address = '제주특별자치도 서귀포시 성산읍 성산중앙로40번길 12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'Oh 세화', 'GUESTHOUSE', 'EAST', '제주특별자치도 제주시 구좌읍 세송로 8-11', 33.5169400, 126.8595500, '세화리에 위치한 깔끔한 펜션&게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/eeb4a1ef-2b01-4491-8459-757663c09c6e.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'Oh 세화' AND address = '제주특별자치도 제주시 구좌읍 세송로 8-11');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'ZeZuZip', 'GUESTHOUSE', 'EAST', '제주 특별자치도 서귀포시 성산읍 삼달로 235-17', 33.3719860, 126.8581160, '성산읍 삼달리라는 작은 시골 마을에 있는 땅콩주택 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/75e82721-45c6-4013-a39b-d7ea7c52426a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'ZeZuZip' AND address = '제주 특별자치도 서귀포시 성산읍 삼달로 235-17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '뉴그린모텔', 'HOTEL', 'EAST', '제주특별자치도 서귀포시 표선면 표선백사로 119', 33.3231785, 126.8445353, '표선해수욕장 바로 앞에 위치한 실속형 숙박업체', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9f7678cb-3ad7-44c8-9fec-e964761dda5c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '뉴그린모텔' AND address = '제주특별자치도 서귀포시 표선면 표선백사로 119');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라마다 제주 함덕 호텔', 'HOTEL', 'EAST', '제주특별자치도 제주시 조천읍 신북로 470', 33.5386700, 126.6458400, '함덕서우봉해변 앞에 위치한 세계적 체인 브랜드 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/5ead2b80-0809-443e-8bfb-02f10779174f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라마다 제주 함덕 호텔' AND address = '제주특별자치도 제주시 조천읍 신북로 470');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '리시온호텔', 'HOTEL', 'EAST', '제주특별자치도 서귀포시 표선면 표선동서로 263', 33.3271865, 126.8337289, '서귀포 표선 시내 중심부에 있어 교통이 편리하고, 호텔 안에 편의점이 있어서 편의성이 좋은 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202208/31/9579811f-eb73-43ba-ad28-4aa5b9560b9b.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '리시온호텔' AND address = '제주특별자치도 서귀포시 표선면 표선동서로 263');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '메이더호텔', 'HOTEL', 'EAST', '제주특별자치도 제주시 구좌읍 일주동로 1626', 33.5517026, 126.7067636, '아름다운 구좌에 있는 메이더호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8d35d04e-d1f2-46dc-aada-3ee52193def1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '메이더호텔' AND address = '제주특별자치도 제주시 구좌읍 일주동로 1626');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '미도모텔', 'HOTEL', 'EAST', '제주특별자치도 서귀포시 성산읍 성산등용로 1', 33.4641323, 126.9336220, '성산포에 위치한 미도모텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/13/ea21cee2-ef22-4fc1-9b83-2dc81624051e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '미도모텔' AND address = '제주특별자치도 서귀포시 성산읍 성산등용로 1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '브리즈베이호텔', 'HOTEL', 'EAST', '제주특별자치도 서귀포시 성산읍 성산등용로 42', 33.4665400, 126.9307600, '성산일출봉 근처에 위치한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6d1445f6-14c0-4612-9e57-59dbcabfbc1f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '브리즈베이호텔' AND address = '제주특별자치도 서귀포시 성산읍 성산등용로 42');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '블루마운틴호텔', 'HOTEL', 'EAST', '제주특별자치도 서귀포시 성산읍 일출로 297', 33.4637856, 126.9343976, '블루마운틴 호텔은 UNESCO 세계자연유산으로 등재된 성산일출봉과 걸어서 3분 거리에 위치하고 있는 제주의 하늘과 바다, 태양을 담고 있는 호텔이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4d3ac45d-a613-468f-99d1-90111de454c9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '블루마운틴호텔' AND address = '제주특별자치도 서귀포시 성산읍 일출로 297');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아띠랑스 풀앤스파 호텔', 'HOTEL', 'EAST', '제주특별자치도 서귀포시 성산읍 해맞이해안로 2614', 33.4775550, 126.9093863, '성산포에 위치한 아띠랑스 호텔&풀빌라', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/be45750a-9d35-4e86-b2cc-cfdc846bf395.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아띠랑스 풀앤스파 호텔' AND address = '제주특별자치도 서귀포시 성산읍 해맞이해안로 2614');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아이미제주비치호텔 함덕', 'HOTEL', 'EAST', '제주특별자치도 제주시 조천읍 조함해안로 474', 33.5432725, 126.6637722, '아름다운 제주 함덕 해변을 테라스에서 느낄 수 있는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/2f32f096-22e1-4a9f-8372-47ad2d13b6a4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아이미제주비치호텔 함덕' AND address = '제주특별자치도 제주시 조천읍 조함해안로 474');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '에코랜드호텔', 'HOTEL', 'EAST', '제주특별자치도 제주시 조천읍 번영로 1278-169', 33.4617075, 126.6703845, '제주 자연 속에서 즐기는 색다른 경험을 선사하는 에코랜드호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202410/08/40f9e7dd-575c-4e53-b872-b6785bdd77f5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '에코랜드호텔' AND address = '제주특별자치도 제주시 조천읍 번영로 1278-169');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오션그랜드호텔', 'HOTEL', 'EAST', '제주특별자치도 제주시 조천읍 조함해안로 490', 33.5425600, 126.6652400, '함덕해수욕장과 서우봉이 내려다 보이는 관광1급호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/15d2f934-d647-4129-9c31-4aabe57c02b9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오션그랜드호텔' AND address = '제주특별자치도 제주시 조천읍 조함해안로 490');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주도한달살기', 'HOTEL', 'EAST', '제주특별자치도 제주시 조천읍 조천9길 10', 33.5368278, 126.6368713, '제주 유일의 실제 한달살기 전용 생활형 숙박시설', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/36ef71eb-de12-463d-aa44-21ef979a0b72.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주도한달살기' AND address = '제주특별자치도 제주시 조천읍 조천9길 10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주성산골든튤립호텔', 'HOTEL', 'EAST', '제주특별자치도 서귀포시 성산읍 일출로 31', 33.4479486, 126.9162124, '객실에서 유네스코 자연유산 성산일출봉을 볼 수 있는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0e07413f-ae95-4fc0-b199-3bd064443516.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주성산골든튤립호텔' AND address = '제주특별자치도 서귀포시 성산읍 일출로 31');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '코업시티호텔 성산', 'HOTEL', 'EAST', '제주특별자치도 서귀포시 성산읍 성산등용로 28', 33.4659601, 126.9319623, '성산일출봉과 오름을 조망할 수 있는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/c237ba70-9213-454c-b16a-5188414ea8ad.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '코업시티호텔 성산' AND address = '제주특별자치도 서귀포시 성산읍 성산등용로 28');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔아로하', 'HOTEL', 'EAST', '제주특별자치도 서귀포시 성산읍 성산등용로 40', 33.4663800, 126.9310400, '성산 바다를 가장 가까운 곳에서 볼 수 있는 클래식한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/cc6d34bf-0729-47c7-84c4-8e21b8b930e0.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔아로하' AND address = '제주특별자치도 서귀포시 성산읍 성산등용로 40');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '누울', 'OTHER', 'EAST', '제주특별자치도 제주시 구좌읍 대수길 10-18', 33.5363990, 126.8391514, '느슨한 풍경을 잇대다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202211/14/a100ea98-173c-46f8-b6a7-ad6ecb8764b7.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '누울' AND address = '제주특별자치도 제주시 구좌읍 대수길 10-18');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '세난포레스트', 'OTHER', 'EAST', '제주특별자치도 제주시 조천읍 중산간동로 883-31 (와산리) 2층', 33.4936941, 126.6781291, '최대 4인 사용할 수 있도록 설계된 유럽풍 렌탈하우스이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202310/04/186538cd-eae7-480e-9325-1e8887384289.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '세난포레스트' AND address = '제주특별자치도 제주시 조천읍 중산간동로 883-31 (와산리) 2층');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '세화리움', 'OTHER', 'EAST', '제주특별자치도 제주시 구좌읍 해녀박물관길 29', 33.5233540, 126.8624000, '세화리의 정취를 느낄 수 있는 ‘세화스러운’ 휴식 공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c8730a70-64a4-4c4e-aa71-34483b2c7c1b.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '세화리움' AND address = '제주특별자치도 제주시 구좌읍 해녀박물관길 29');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '소노벨제주', 'OTHER', 'EAST', '제주특별자치도 제주시 조천읍 신북로 577 소노벨제주', 33.5412860, 126.6714860, '소노벨 제주는 제주공항에서 불과 30분 거리에 위치한 청정한 해수욕장을 갖춘 함덕에 속해있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202503/12/fead2fa7-36f6-482a-89da-4eb409ca815c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '소노벨제주' AND address = '제주특별자치도 제주시 조천읍 신북로 577 소노벨제주');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '임진고택', 'OTHER', 'EAST', '제주특별자치도 제주시 구좌읍 상도로 16-13', 33.5133127, 126.8634849, '구석구석 깃든 위풍당당함', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202211/14/825d6e15-0fcd-41a2-bba2-020b0d9c710e.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '임진고택' AND address = '제주특별자치도 제주시 구좌읍 상도로 16-13');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 와싱톤', 'OTHER', 'EAST', '제주특별자치도 서귀포시 성산읍 신천서로50번길 22-56', 33.3419311, 126.8488963, '제주, 섬 안의 작은 유럽, 제주와싱톤은 자연과 함께 편안함과 휴식, 그리고 즐거움을 위한 모든 것이 준비된 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/23527245-f990-404e-abed-23af3d024c00.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 와싱톤' AND address = '제주특별자치도 서귀포시 성산읍 신천서로50번길 22-56');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '토리코티지X하시시박', 'OTHER', 'EAST', '제주특별자치도 제주시 조천읍 조천북6길 23', 33.5422556, 126.6388695, '아늑하게 담은 제주의 풍광', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202211/14/00bbec63-927b-4abd-af79-7f512dfd9e69.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '토리코티지X하시시박' AND address = '제주특별자치도 제주시 조천읍 조천북6길 23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '가비오타펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 해맞이해안로 2030 가동', 33.4880673, 126.8804954, '하도리해수욕장이 한 눈에 내다보이는 전객실 전망 좋은 오션뷰펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/01/b6f1a040-bb3f-47f3-829f-909d1d1ca8b0.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '가비오타펜션' AND address = '제주특별자치도 제주시 구좌읍 해맞이해안로 2030 가동');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '건축학개론펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 신풍하동로 224-33', 33.3584180, 126.8598200, '표선교차로 인근 독채로 이용할 수 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fa3f744e-27cc-40d4-8cfd-9e26cd9a6dbc.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '건축학개론펜션' AND address = '제주특별자치도 서귀포시 성산읍 신풍하동로 224-33');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '게으르게펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 신북로 410', 33.5434000, 126.6541900, '함덕해수욕장에 가까이 위치한 가족형 독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d412448d-a16d-4015-97ba-13bbae0f0846.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '게으르게펜션' AND address = '제주특별자치도 제주시 조천읍 신북로 410');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '고망난돌민박', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 일주동로 5464', 33.3531040, 126.8578950, '일주도로와 접하고 제주도올레3코스 중간지점에 위치한 전원민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c6e93d24-4d5b-4be7-be0a-da6e2bee62a5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '고망난돌민박' AND address = '제주특별자치도 서귀포시 성산읍 일주동로 5464');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '고성2119 애견펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 서성일로1136번길 10-1', 33.4440658, 126.8995516, '고성2119펜션 위플레이독은 제주 성산포 중심에 있는 가족펜션이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/0cf0cd0a-e362-4af3-87ff-2014daf95210.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '고성2119 애견펜션' AND address = '제주특별자치도 서귀포시 성산읍 서성일로1136번길 10-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '공감민박', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 수산서남로 75', 33.4472106, 126.8774412, '풋감이불이 주는 편안함을 느껴보세요.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202105/14/5a0fdd62-c660-4838-87fc-aadd178a2cc8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '공감민박' AND address = '제주특별자치도 서귀포시 성산읍 수산서남로 75');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '구름밭하우스', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 문주란로 142', 33.5149840, 126.8817700, '제주도 아름다운 동부바다와 해안도로가 한눈에 보이는 그곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a0214722-5d4a-4a7e-a390-7c606d5ef45d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '구름밭하우스' AND address = '제주특별자치도 제주시 구좌읍 문주란로 142');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '그레이스힐링', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 시흥상동로53번길 88-29', 33.4737774, 126.8874341, '제주 동쪽 최고의 풀빌라펜션... ‘그레이스 힐링’', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202407/24/b9fe7802-9288-4cf6-a041-cec8c18d32d3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '그레이스힐링' AND address = '제주특별자치도 서귀포시 성산읍 시흥상동로53번길 88-29');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '그림그리는펜션', 'PENSION', 'EAST', '제주도 서귀포시 성산읍 금백조로131번길 181', 33.4312200, 126.8439200, '성산인근에서는 픽업서비스가 가능한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6e5dc17c-91a3-44e1-9614-d25282fbfbeb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '그림그리는펜션' AND address = '제주도 서귀포시 성산읍 금백조로131번길 181');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '기분좋은 민박', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 선유로 36', 33.5359300, 126.7721730, '김녕에 위치한 바다 전망 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/19/533d040a-1dfa-497d-a6a7-b51c64028e05.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '기분좋은 민박' AND address = '제주특별자치도 제주시 구좌읍 선유로 36');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '김녕몽생이', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 김녕로1길 35-14', 33.5572050, 126.7482150, '제주시 구좌에 위치한 제주돌집 독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/47a91217-02b5-4b00-94c0-7cc04e0245b2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '김녕몽생이' AND address = '제주특별자치도 제주시 구좌읍 김녕로1길 35-14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '김녕한옥 용암정원', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 김녕로19길 42-6', 33.5570760, 126.7514300, '조선시대에 지어진 제주 한옥을 살린 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/80c6685b-981e-44e3-837a-ac71c6956880.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '김녕한옥 용암정원' AND address = '제주특별자치도 제주시 구좌읍 김녕로19길 42-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '꿈꾸는정원', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 함덕서4길 47', 33.5389274, 126.6654131, '함덕 서우봉해변에 위치한 독채 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/db66f412-c362-4e2e-acaa-9837191544d7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '꿈꾸는정원' AND address = '제주특별자치도 제주시 조천읍 함덕서4길 47');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '나무사이로햇살이', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 중산간동로 2281', 33.4711150, 126.7862400, '편백나무로 둘러쌓여 클래식한 나무집과 소소한 손길이 조화로운 독채민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/bd5d1f67-c7fd-439b-9a6f-f9d0386aeb1e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '나무사이로햇살이' AND address = '제주특별자치도 제주시 구좌읍 중산간동로 2281');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '나무아래독채펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 송당서길 51-1', 33.4668850, 126.7694500, '오름의 마을 송당리에 위치한 자연속의 나무아래독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/05547080-a040-4f46-9710-79aedef01200.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '나무아래독채펜션' AND address = '제주특별자치도 제주시 구좌읍 송당서길 51-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '남촌풀하우스', 'PENSION', 'EAST', '제주도 서귀포시 표선면 표선동서로 139', 33.3169980, 126.8282550, '표선에 위치한 은은한 황토의 향기와 원목내음이 가득한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7447f072-1edd-4b38-9911-c2b0c793c423.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '남촌풀하우스' AND address = '제주도 서귀포시 표선면 표선동서로 139');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '눈먼고래', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 조천7길 19-12', 33.5385320, 126.6345400, '제주다움을 간직한 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/147c9a07-b272-4760-86f9-331b0dc6976b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '눈먼고래' AND address = '제주특별자치도 제주시 조천읍 조천7길 19-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '뉴그린펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 표선백사로 121', 33.3262440, 126.8366600, '객실에서 바로 바다 수평선에서 떠오르는 일출을 감상할 수 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6d2768b2-dd7b-4797-ba29-92a0a53594ca.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '뉴그린펜션' AND address = '제주특별자치도 서귀포시 표선면 표선백사로 121');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '다올제민박', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 하도7길 9', 33.5162100, 126.8854200, '농가주택을 편백나무로 새롭게 리모델링한 쾌적하고 아늑한 쉼터', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/20549b5a-ca04-4d82-bded-c43dc8b90afa.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '다올제민박' AND address = '제주특별자치도 제주시 구좌읍 하도7길 9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '달빛고운펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 월정7길 52-1', 33.5553170, 126.7963800, '월정리에 위치한 편백나무 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7d1bfcba-1586-4311-8b2d-f78bb90e9e03.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '달빛고운펜션' AND address = '제주특별자치도 제주시 구좌읍 월정7길 52-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '달콤한아침', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 행원로 35-9', 33.5521400, 126.7990500, '각각 별도의 비밀번호 현관문, 개별 욕실로 구성된 독채형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/72f0056f-0c57-4936-8137-7e2fc9ccb996.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '달콤한아침' AND address = '제주특별자치도 제주시 구좌읍 행원로 35-9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '담제', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 행원로4길 21-4', 33.5516728, 126.8064723, '싱그러운 제주를 담은 제주독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/deb2ef34-4bed-409d-a1f6-ec9c8e4b58ec.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '담제' AND address = '제주특별자치도 제주시 구좌읍 행원로4길 21-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더갤러리펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 남조로 1717-24', 33.4221500, 126.6753600, '하나의 객실이 갤러리가 되는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ed31cb3d-ad2b-4592-aa60-86ec27dfde11.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더갤러리펜션' AND address = '제주특별자치도 제주시 조천읍 남조로 1717-24');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더들집 오름', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 동복로1길 23-3', 33.5543980, 126.7129600, '제주 바다를 더 가까이, 더 자세히 보고싶다면', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8a9245a6-6b96-4716-bbf4-6dc6fc0f2301.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더들집 오름' AND address = '제주특별자치도 제주시 구좌읍 동복로1길 23-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더쉼팡스파&풀빌라', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 세성로 67-2', 33.3122342, 126.8036169, '조경의 아름다움을 뽐내는 공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/87703798-19b8-4678-863e-292e4b7cfb29.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더쉼팡스파&풀빌라' AND address = '제주특별자치도 서귀포시 표선면 세성로 67-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더하우스펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 해맞이해안로 2060', 33.5096018, 126.9029970, '종달리해안도로 초입에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/cd9286b3-7645-4a22-81e3-231feb906013.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더하우스펜션' AND address = '제주특별자치도 제주시 구좌읍 해맞이해안로 2060');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '돌집조앤정', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 세화합전1길 7', 33.4895966, 126.8316644, '제주도 동쪽 구좌읍 세화리 예쁜 마을의 원룸과 독채형의 B & B 형 농가 돌집 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/13/9773ec4a-073f-45e6-8ad6-6a72ee7435a6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '돌집조앤정' AND address = '제주특별자치도 제주시 구좌읍 세화합전1길 7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '동박생이', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 월정1길 89', 33.5584200, 126.7929150, '동백새를 떠올리며 지은 돌집이자 한옥 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/445d1f9f-06f3-496a-ac2d-9d9ebf1a4e8e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '동박생이' AND address = '제주특별자치도 제주시 구좌읍 월정1길 89');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '동백동산펜션', 'PENSION', 'EAST', '제주시 조천읍 북흘로 378', 33.5162540, 126.7061200, '전 객실 편백나무 내장된 곶자왈 속 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/aecf07c6-d9c0-4737-88e1-7dc5801db4d0.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '동백동산펜션' AND address = '제주시 조천읍 북흘로 378');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '드헤브펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 문주란로1길 74-24', 33.5241700, 126.8833540, '세련되고 고급스러운 인테리어가 인상적인 신축펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3f01fefc-0e44-42dd-80cc-e63b7d0f8c4b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '드헤브펜션' AND address = '제주특별자치도 제주시 구좌읍 문주란로1길 74-24');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '로그빌리지', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 일주동로 5944', 33.3267400, 126.8206600, '제주 삼나무를 자연 그대로의 모습으로 지은 그림같은 공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d150c0ca-399e-45b9-ad85-46656d233486.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '로그빌리지' AND address = '제주특별자치도 서귀포시 표선면 일주동로 5944');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '로즈비치', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 조함해안로 162', 33.5447774, 126.6501640, '왼쪽은 올레18코스, 오른쪽은 올레19코스가 있는 로즈비치펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f1828870-3017-4db5-864c-112266f5dc52.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '로즈비치' AND address = '제주특별자치도 제주시 조천읍 조함해안로 162');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '루하우스', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 민속해안로119번길 8', 33.3072620, 126.8128400, '올레길 4코스에 위치한 독채 돌집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/af796fc9-02f6-4725-baff-06c8ea855b10.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '루하우스' AND address = '제주특별자치도 서귀포시 표선면 민속해안로119번길 8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '마빈과 르마빈 펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 일주동로 1532', 33.5489689, 126.6975528, '제주를 관광하기에 편리한 위치에있는 펜션마빈은 함덕해수욕장 인접에 있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/87ab66f2-926e-463b-9514-19048dbeecba.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '마빈과 르마빈 펜션' AND address = '제주특별자치도 제주시 조천읍 일주동로 1532');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '명상가의집', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 중산간동로 1513', 33.4949687, 126.7211439, '명상가의집은 제주의 숲에서 내면의 고요를 찾는 명상가와 예술가를 위한 숙박 공간이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202301/09/131ec1ad-2101-46f4-ad88-328f16dce007.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '명상가의집' AND address = '제주특별자치도 제주시 조천읍 중산간동로 1513');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '명월 민박', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 독자봉로60번길 182 (난산리)', 33.3982580, 126.8706307, '난산 명월 민박입니다! 하루 살기, 일주일 살기, 보름 살기, 한달 살기 모두 가능합니다!', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202503/05/0e40977f-7c99-4dd8-85ac-8ff6502ddaaa.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '명월 민박' AND address = '제주특별자치도 서귀포시 성산읍 독자봉로60번길 182 (난산리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '무이비엔', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 한동로4길 3', 33.5350760, 126.8234100, '‘매우 좋은’이라는 뜻에 걸맞은 무이비엔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/110fe461-ea48-435a-a7e5-585e89a6fd38.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '무이비엔' AND address = '제주특별자치도 제주시 구좌읍 한동로4길 3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바다파파', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 신촌10길 7', 33.5351405, 126.6258231, '제주도 바닷가 앞, 가족펜션과 농장을 운영하는 바다파파입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/26fac5ca-5412-48d4-aec1-cddd4aa1c97b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바다파파' AND address = '제주특별자치도 제주시 조천읍 신촌10길 7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바람돌이하우스민박', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 행원로5길 35-5', 33.5562800, 126.8047300, '바람과 돌이 머무는 집, 바람돌이 하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e9e7b118-439e-4875-83d6-c586226e73e1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바람돌이하우스민박' AND address = '제주특별자치도 제주시 구좌읍 행원로5길 35-5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '발리인제주', 'PENSION', 'EAST', '제주도 제주시 구좌읍 달맞이해안로 2326', 33.4933970, 126.9089800, '성산일출봉과 우도가 한눈에 보이는 커플전용 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/af001b62-c09f-41b3-b195-367d6ac5ef63.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '발리인제주' AND address = '제주도 제주시 구좌읍 달맞이해안로 2326');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '밧돌펜션하우스', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 비자림로 1771', 33.4764906, 126.7882841, '18개의 오름을 품고 있는 구좌읍 송당리에 위치한 독채, 커플 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202307/07/a919921e-4aa2-4774-86db-87e353ff1f70.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '밧돌펜션하우스' AND address = '제주특별자치도 제주시 구좌읍 비자림로 1771');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '밭담사이', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 종달로1길 115-1', 33.4880673, 126.8804954, '제주 동쪽 끝 마을 종달리, 해변과 80m 떨어진 자연속에 어울러진 소나무와 수국 꽃으로 어우러진 집. 편백 내장재 사용 피톤치트 발산 이용자가 편한 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202005/06/db473db3-936d-42d9-989f-865f0110ba09.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '밭담사이' AND address = '제주특별자치도 제주시 구좌읍 종달로1길 115-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '배목수집', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 세송로 43', 33.5145870, 126.8589100, '세화 바다가 보이는 마당 넓은 집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d4399736-3c6a-4d68-86ba-87fd4bddff3e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '배목수집' AND address = '제주특별자치도 제주시 구좌읍 세송로 43');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '별똥별채집소', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 하도13길 51-1', 33.5112899, 126.8919993, '하늘과 별과 바다가 가까운 독채민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201907/04/e76a0a0a-03f7-4e97-a977-77ea542ed320.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '별똥별채집소' AND address = '제주특별자치도 제주시 구좌읍 하도13길 51-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '볕들이펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 일출로 284-4', 33.4627122, 126.9351852, '성산일출봉 바로 아래에 위치하여 어디에서나 아침에 눈을뜨면 성산일출봉의 경관을 한눈에 볼수 있게 건축된 볕들이 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8f1e795f-4bc5-4f11-b56a-d89b8c092998.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '볕들이펜션' AND address = '제주특별자치도 서귀포시 성산읍 일출로 284-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '보물섬', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 한도로242번길 10-11', 33.4663540, 126.9315700, '성산일출봉 위치에 근처에 위치한 합리적인 가격의 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8f88002f-d934-48d6-befc-09b4d8f45a88.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '보물섬' AND address = '제주특별자치도 서귀포시 성산읍 한도로242번길 10-11');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '북촌플레이스', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 일주동로 1435', 33.5455800, 126.6881700, '깔끔한 인테리어와 탁드인 바다 조망이 돋보이는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e00aee3e-e46e-4ce1-8569-d7c799ee7cfb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '북촌플레이스' AND address = '제주특별자치도 제주시 조천읍 일주동로 1435');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '블루마린', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 민속해안로 349', 33.3113480, 126.8338700, '표선 올레 4코스에 위치한 푸른 바다 전망이 멋진 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/cf0bbf6f-6c65-4dab-b53d-5b3e54b191dc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '블루마린' AND address = '제주특별자치도 서귀포시 표선면 민속해안로 349');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '비오하우스', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 월정3길 53-5', 33.5561200, 126.7946100, '비오하우스는 월정리해변 근처에 위치한 펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8309179c-4776-4be3-8be9-37754f6da1e3.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '비오하우스' AND address = '제주특별자치도 제주시 구좌읍 월정3길 53-5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '비자낭달집', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 비자림로 2279', 33.4960353, 126.8123422, '천년의 숲 비자림에서 힐링하다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201805/23/fa7246ef-5d58-4cf9-93f0-000f9ddea62e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '비자낭달집' AND address = '제주특별자치도 제주시 구좌읍 비자림로 2279');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '비젠빌리지', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 하도9길 72', 33.5115550, 126.8907400, '숙박을 넘어 복합 문화 공간을 제공하다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d82adf16-9132-4acd-a6d2-ae3820f2f838.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '비젠빌리지' AND address = '제주특별자치도 제주시 구좌읍 하도9길 72');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '빌라배알로', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 환해장성로 941', 33.4348560, 126.9163740, '조용한 마을의 집 한 채를 통째로 빌리다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/caf78ea5-d95d-40a9-8057-4073cc36e374.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '빌라배알로' AND address = '제주특별자치도 서귀포시 성산읍 환해장성로 941');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '사랑이꽃피는민박', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 김녕로19길 12', 33.5582139, 126.7373381, '김녕마을 안쪽에 위치한 세계지질공원 지질 테마 숙소 ''지오하우스''', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/72584ba8-44c3-4390-8eeb-3f7f3e5545f3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '사랑이꽃피는민박' AND address = '제주특별자치도 제주시 구좌읍 김녕로19길 12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '성산아침노을펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 한도로255번길 14-1', 33.4667192, 126.9350943, '성산일출봉과 우도를 끼고 있는 바다 정면에 위치해 있어 바다 위로 떠오르는 일출을 펜션에서 볼 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7e5149e2-c5ad-44e2-91b8-96f9c103aa69.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '성산아침노을펜션' AND address = '제주특별자치도 서귀포시 성산읍 한도로255번길 14-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '성산우리집 펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 한도로255번길 9', 33.4669770, 126.9344586, '성산일출봉과 우도 근처에 위치한 성산일출봉펜션 성산 우리집입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201908/30/0db2d01e-2df6-4a3d-b1df-3acb68b729db.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '성산우리집 펜션' AND address = '제주특별자치도 서귀포시 성산읍 한도로255번길 9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '성산풀하우스', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 환해장성로121번길 21-4', 33.3842543, 126.8797266, '성산일출봉, 우도가 코 앞에 있는 28평형 독채형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201907/04/20c15795-bf49-40ea-9a08-3d2f05d60172.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '성산풀하우스' AND address = '제주특별자치도 서귀포시 성산읍 환해장성로121번길 21-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '세화맨션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 세화7길 17-10', 33.5232400, 126.8607600, '이국적인 세화 해변이 한눈에 들어오는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5d4e49c4-1d07-43f5-ac7d-efd9425762e1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '세화맨션' AND address = '제주특별자치도 제주시 구좌읍 세화7길 17-10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '소라담하우스', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 신촌남8길 10', 33.5301815, 126.6225493, '한가족만을 위한 독채형 단독펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b42ba078-f905-4ea4-b444-321fbaa74075.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '소라담하우스' AND address = '제주특별자치도 제주시 조천읍 신촌남8길 10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '소랑풀빌라', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 곱은달남길 218', 33.4925730, 126.6717500, '휴식과 레저가 어우러진 꿈의 풀빌라', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a86df5b0-02bb-432b-942c-60cf23c6ecdf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '소랑풀빌라' AND address = '제주특별자치도 제주시 조천읍 곱은달남길 218');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '솔민박', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 민속해안로 227-39', 33.3225332, 126.8228665, '제주독채 가족펜션 솔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201911/27/e4eb56d7-f9de-4c1c-8cf1-b0fd6a00a2e0.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '솔민박' AND address = '제주특별자치도 서귀포시 표선면 민속해안로 227-39');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '수필하우스', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 종달논길 50-1', 33.4929540, 126.8996050, '전통 제주돌집을 리모델링한 B&B형 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b35f83fc-b0fb-4f94-96a3-a702e5389185.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '수필하우스' AND address = '제주특별자치도 제주시 구좌읍 종달논길 50-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '수하리839', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 온평애향로 21', 33.4077450, 126.8999100, '제주의 풍경이 한눈에 들어오는 수하리839', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7d9dc718-f7ae-477e-bb22-3dba6eacfabf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '수하리839' AND address = '제주특별자치도 서귀포시 성산읍 온평애향로 21');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '숲골 독채펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 선교로 211-19', 33.4844500, 126.7024700, '산, 계곡, 호수 등이 가까이 위치하여 각종 놀이시설과 즐길거리가 다양한 제주독채펜션 숲골입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/07977a31-01d8-49f8-a238-8f331ee4295f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '숲골 독채펜션' AND address = '제주특별자치도 제주시 조천읍 선교로 211-19');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스코리아청굴물', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 김녕로1길 78', 33.5577758, 126.7514764, '제주도 옛 민가의 모습을 그대로 살린 숙소 제주지오브랜드 공식 숙소로 지정되어있다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/976ef80b-ed1e-4607-bab0-fab2718e5a1b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스코리아청굴물' AND address = '제주특별자치도 제주시 구좌읍 김녕로1길 78');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스테이느릇', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 번영로 2300-79', 33.4153837, 126.7522781, '자연 속 로컬 감성 가득한 독채형 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202507/03/f1899595-04a6-4eec-95fa-6f23a1cea1cc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스테이느릇' AND address = '제주특별자치도 서귀포시 표선면 번영로 2300-79');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스테이제주이음', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 하천로 37', 33.3450673, 126.8423898, '귤나무정원과 돌담 그리고 자쿠지가 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/11/d037000d-961f-4254-a4d4-bb66a9638098.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스테이제주이음' AND address = '제주특별자치도 서귀포시 표선면 하천로 37');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '썬레이크빌제주', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 한도로 49', 33.4675020, 126.9134076, '성산일출봉 근처에 위치해 있으며, 우수관광사업체로 등록되어 있는 썬레이크빌 휴양펜션 입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a71be6ca-e19d-49c1-8746-f0affd261ebb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '썬레이크빌제주' AND address = '제주특별자치도 서귀포시 성산읍 한도로 49');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '쏠레민박', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 성산등용로 96-1', 33.4701230, 126.9301400, '성산일출봉 및 우도 인근에 위치한 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/686ce014-9195-453f-8e47-67111860b993.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '쏠레민박' AND address = '제주특별자치도 서귀포시 성산읍 성산등용로 96-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아모르하우스 오션뷰펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 해맞이해안로 2060-18', 33.5087800, 126.9018900, '다양한 체험과 편안함을 한번에, 아모르하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5f20df80-2caa-4bea-a80d-3a11c2aff6de.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아모르하우스 오션뷰펜션' AND address = '제주특별자치도 제주시 구좌읍 해맞이해안로 2060-18');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아일랜드펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 금백조로131번길 176', 33.4311796, 126.8443512, '성산일출봉 인근에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e6d979d9-fb5e-475e-b7d0-8b7bf57fcd4d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아일랜드펜션' AND address = '제주특별자치도 서귀포시 성산읍 금백조로131번길 176');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아침해변', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 조함해안로 76-9', 33.5439418, 126.6380689, '제주의 아름다운 바다, 바람, 그리고 자연으로의 초대.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/57227089-d1d0-48be-a800-8d249248be07.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아침해변' AND address = '제주특별자치도 제주시 조천읍 조함해안로 76-9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아쿠아뷰티크', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 해맞이해안로 522', 33.5569228, 126.8015660, '월정리 해변 바로 앞에 있는 미니 풀빌라', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1d9098d3-cb25-4199-8be1-141373a21908.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아쿠아뷰티크' AND address = '제주특별자치도 제주시 구좌읍 해맞이해안로 522');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '애견펜션하젠(하젠타운)', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 토산중앙로 140', 33.3179415, 126.7744574, '표선의 잔디 정원에서 즐기는 프라이빗한 휴식', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202509/08/2119c96e-e5c1-4209-917e-7f1cf5a9a05b.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '애견펜션하젠(하젠타운)' AND address = '제주특별자치도 서귀포시 표선면 토산중앙로 140');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '야호비치하우스', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 조함해안로 582', 33.5314060, 126.6663700, '함덕 서우봉 해변에 위치한 감각적인 디자인과 해변 경치가 매력적인 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/938af916-bfdb-401d-877f-5bd2c1612e84.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '야호비치하우스' AND address = '제주특별자치도 제주시 조천읍 조함해안로 582');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '언덕위에빨간지붕', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 난고로499번길 17', 33.4049563, 126.8720230, '성산읍 난산리 마을 앞 동산 맨 위에 위치해있다. 언덕 위에 위치해있어 조용하면서도 남쪽과 동쪽으로 바다전망이 뛰어나고, 성산일출봉도 볼 수 있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/05/842bcf59-6b29-458a-b054-a8b77a238187.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '언덕위에빨간지붕' AND address = '제주특별자치도 서귀포시 성산읍 난고로499번길 17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '에이지하우스', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 돈오름로23번길 36-46', 33.3357594, 126.8060933, '세화리에 위치한 친환경 목조 건축물 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6684eee3-3d8b-4af0-a5b4-f86ffa812184.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '에이지하우스' AND address = '제주특별자치도 서귀포시 표선면 돈오름로23번길 36-46');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오션스카이', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 덕행로 450-15', 33.5340986, 126.7954364, '국내 유일의 개인 별장현 펜션단지로 한집당 200여 평의 대지 위에 설계된 별장 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6f9c095b-614d-4ff3-b506-86cbbd72aa63.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오션스카이' AND address = '제주특별자치도 제주시 구좌읍 덕행로 450-15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오션하도더힐링하우스', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 하도7길 130-108 (하도리)', 33.5162960, 126.8984323, '제주공항에서 1시간거리인 제주 동부지역 하도리 해수욕장 인근에 위치한 신축 2층 단독주택 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202311/14/7faf80b5-2ee8-4947-8dc9-e9b277c01dfd.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오션하도더힐링하우스' AND address = '제주특별자치도 제주시 구좌읍 하도7길 130-108 (하도리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오조리 일출스테이', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 오조로 42', 33.4579160, 126.9159900, '성산일출봉의 해돋이를 사랑하는 사람들과 함께 따뜻한 침실에서 볼 수 있는 오조리bnb', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9f9d9194-6a42-49bb-8d34-db37cffb02f1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오조리 일출스테이' AND address = '제주특별자치도 서귀포시 성산읍 오조로 42');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '온더스톤펜션 제주성산점', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 해맞이해안로 2746', 33.4700329, 126.9178447, '온더스톤펜션 제주성산점은 제주의 명소인 성산일출봉, 우도, 섭지코지를 한 곳에서 볼 수 있는 바다에 위치해있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/dcaa7440-b8a3-4e50-b744-7d39a35adc77.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '온더스톤펜션 제주성산점' AND address = '제주특별자치도 서귀포시 성산읍 해맞이해안로 2746');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '우연한동', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 한동북1길 39', 33.5416534, 126.8289162, '제주 동쪽마을 구좌에 위치한 제주 독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/03/3378ea25-8e9c-422a-bb89-ab08f8de65ba.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '우연한동' AND address = '제주특별자치도 제주시 구좌읍 한동북1길 39');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '월정소랑 제주독채펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 월정1길 79-3', 33.5576440, 126.7928600, '소랑(사랑)이 한가득', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/00b9c414-e0f1-4fa2-b1a8-ed06cb8583ef.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '월정소랑 제주독채펜션' AND address = '제주특별자치도 제주시 구좌읍 월정1길 79-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '월정에비뉴풀빌라', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 해맞이해안로 486', 33.5545847, 126.7982658, '에메랄드 빛깔이 아름다운 월정리 해변 앞, 오션뷰와 넓은 인피니티풀을 갖추고 있는 풀빌라와 전문 강사들로 구성된 서핑클럽, 누구나 즐길 수 있는 비치 펍과 레스토랑, 워크샵을 위한', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202111/05/6bb7ebfd-1307-4f6c-bac1-4ed21d374b83.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '월정에비뉴풀빌라' AND address = '제주특별자치도 제주시 구좌읍 해맞이해안로 486');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '월정플레이스', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 월정1길 54-34', 33.5547523, 126.7916765, '월정해안도로와 도보로 1~2분 거리인 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/02/1c4099c9-ce72-4794-bb47-0c0a573ff25d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '월정플레이스' AND address = '제주특별자치도 제주시 구좌읍 월정1길 54-34');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '이안재', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 종달로5길 35-1 (종달리)', 33.4880673, 126.8804954, '제주 종달리에 위치한 200년 옛집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/19/8d28dcca-9191-458f-90a9-1195145c4a55.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '이안재' AND address = '제주특별자치도 제주시 구좌읍 종달로5길 35-1 (종달리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '일출썬펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 성산중앙로65번길 6-2', 33.4655081, 126.9344914, '옥상에서 성산의 일출을 감상할 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/64c89c0c-b25b-4ef8-a80f-97c7e2741fe3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '일출썬펜션' AND address = '제주특별자치도 서귀포시 성산읍 성산중앙로65번길 6-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '자넷앤캐시', 'PENSION', 'EAST', '제주시 구좌읍 평대9길 20-1', 33.5257530, 126.8507000, '완벽한 프라이빗 키즈펜션, 한팀만 사용가능한 독체펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c1a9a4c1-edad-4217-a767-ad1d0cf2cf7a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '자넷앤캐시' AND address = '제주시 구좌읍 평대9길 20-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '잠비하우스', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 월정1길 54-11', 33.5554850, 126.7935100, '일본의 맛 이찌마루와 소박한 독채펜션 잠비 하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b2a7572f-0ccf-4b57-8b73-d1cedbbc3074.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '잠비하우스' AND address = '제주특별자치도 제주시 구좌읍 월정1길 54-11');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주가좋아서펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 비자림로 1569', 33.4509119, 126.7677935, '비자림로 인근에 위치해, 편안하고 조용하게 쉬고 갈 수 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201808/17/0cd2d949-dbdc-4806-8d93-d9165de902b3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주가좋아서펜션' AND address = '제주특별자치도 제주시 구좌읍 비자림로 1569');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주구도', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 중산간동로 648 (대흘리)', 33.4982639, 126.6548970, '창 너머 제주의 사계절을 간직한 곳...대흘리에 위치한 프라이빗한 풀빌라', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202402/21/39ae95b0-555c-48e8-906f-fe5a870d990a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주구도' AND address = '제주특별자치도 제주시 조천읍 중산간동로 648 (대흘리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주도 레인보우', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 종달로 68-2', 33.4920723, 126.8959814, '바다와 기생화산(지미오름)이 인접한 조용한 어촌마을에 있는 제주도 레인보우', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0e3868f8-064f-4017-98bc-089da30f80fa.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주도 레인보우' AND address = '제주특별자치도 제주시 구좌읍 종달로 68-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주독채펜션자쿠지감성숙소구르미별동', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 세화합전1길 34-12', 33.5126319, 126.8623001, '제주 신상 가족& 커플 &자쿠지&노천탕 &스누피가든&성산일출봉&비자림&비밀의 숲 인근 단독 독채 숙소이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202212/06/d054864a-810f-48a6-94f7-6f59eb1f25f8.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주독채펜션자쿠지감성숙소구르미별동' AND address = '제주특별자치도 제주시 구좌읍 세화합전1길 34-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주바다펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 해맞이해안로 2324', 33.4935340, 126.9089660, '성산일출봉과 우도가 내다보이는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3f507718-53e3-40ea-be2d-7591d13c3d86.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주바다펜션' AND address = '제주특별자치도 제주시 구좌읍 해맞이해안로 2324');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주밭담숲', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 김녕남8길 55-64', 33.5433328, 126.7617664, '제주 속의 제주 화산 흙집 숙소. 강한 항균력과 원적외선 효과를 느껴보자', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202301/04/0f79940e-1f82-43a3-bb5e-c992a3ad674b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주밭담숲' AND address = '제주특별자치도 제주시 구좌읍 김녕남8길 55-64');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주블루앤씨펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 서성일로 188-18', 33.4027582, 126.8148563, '성산읍에 위치한 제주블루앤씨펜션은 Blue & Sea처럼 바다처럼 푸른 건물이 인상적이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/0438cd91-7605-47ce-82a7-3060889e8329.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주블루앤씨펜션' AND address = '제주특별자치도 서귀포시 성산읍 서성일로 188-18');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주빌레성 통나무휴양펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 온평포구로 42', 33.4016100, 126.8994400, '자연그대로 통나무집의 명품 제주빌레성 통나무 휴양펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/25cf58a3-d771-4d4c-b5bb-23eae80f9633.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주빌레성 통나무휴양펜션' AND address = '제주특별자치도 서귀포시 성산읍 온평포구로 42');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주산내들', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 풍천로273번길 143-4', 33.3607250, 126.8237460, '야자수를 비롯한 크고 작은 꽃과 나무가 가득한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/04b7fc23-4bfa-4807-bedf-fd49eaa9dc4f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주산내들' AND address = '제주특별자치도 서귀포시 표선면 풍천로273번길 143-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주소풍', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 월정1길 23', 33.5539975, 126.7905923, '제주 월정리의 바다와 바람, 별을 느낄수 있는 조용하고 아늑한 공간을 제공한다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202004/16/0c176f00-2971-4291-aedb-f6269e79c4f5.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주소풍' AND address = '제주특별자치도 제주시 구좌읍 월정1길 23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주신촌돌집', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 신촌5길 30', 33.5366480, 126.6209000, '돌담이 정겨운 집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3ef53971-6acb-4496-a464-fb7f54db95d5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주신촌돌집' AND address = '제주특별자치도 제주시 조천읍 신촌5길 30');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주오션뷰펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 덕행로 276', 33.5259865, 126.7883327, '제주 동쪽 구좌읍에 위치한 조용하고 깨끗한 가족펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202301/27/34288fd6-7c9b-432c-aa31-7c36441e4ada.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주오션뷰펜션' AND address = '제주특별자치도 제주시 구좌읍 덕행로 276');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주올레돔 풀빌라 독채펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 온평포구로54번길 54', 33.3984783, 126.8995668, '제주 성산일출봉 가까운 감성펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202104/26/7224c389-f294-4419-bd83-785d9739297a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주올레돔 풀빌라 독채펜션' AND address = '제주특별자치도 서귀포시 성산읍 온평포구로54번길 54');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주올레하우스팬션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 덕행로 450-23', 33.5341000, 126.7954300, '행원리 자연속에 위치한 펜션 야외 수영장, 바베큐 구비', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f8563ec2-cf8f-44bb-91b6-e36ab0cc5683.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주올레하우스팬션' AND address = '제주특별자치도 제주시 구좌읍 덕행로 450-23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주와요펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 조천남4길 4', 33.5329839, 126.6386315, '함덕해수욕장 및 올레길 근처에 있는 즐거움이 가득한 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4487d797-56e0-4887-9c28-1cd4fa6b90ae.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주와요펜션' AND address = '제주특별자치도 제주시 조천읍 조천남4길 4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주코기네', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 조함해안로 76-34', 33.5437620, 126.6397860, '제주바다가 보이는 오직 한팀만을 위한 독채형 민박, 애견동반가능', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2f6eb7eb-8b1b-4dd4-beee-a45b494e5bc6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주코기네' AND address = '제주특별자치도 제주시 조천읍 조함해안로 76-34');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주통나무휴양펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 번영로 3328', 33.3512467, 126.8233233, '고급 제주산 삼나무 통나무집으로 주인이 직접 지은 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202007/01/1415cf91-5a08-4625-8a73-2a1627f7fcff.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주통나무휴양펜션' AND address = '제주특별자치도 서귀포시 표선면 번영로 3328');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주파도소리', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 해맞이해안로 1950', 33.5150151, 126.9004638, '청정자연지역 명품제주에 걸맞은 친절하고 편안한 숙박시설', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/865949da-5374-486b-a752-5b370f27612d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주파도소리' AND address = '제주특별자치도 제주시 구좌읍 해맞이해안로 1950');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주필하우스', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 일주동로 1415', 33.5450480, 126.6864200, '창 밖으로 푸른 바다를 볼 수 있는 전망을 가진 가족형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f20966b7-08b7-47bb-958e-b9f7a346d510.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주필하우스' AND address = '제주특별자치도 제주시 조천읍 일주동로 1415');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '조앤리키즈하우스', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 병문로 160-4 (오조리) 3호', 33.4592015, 126.9027005, '제주도 동쪽 성산일출봉 근처 대가족 여행을 위한 프라이빗 독채 펜션 (8인 가족 숙박가능)', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202512/05/585ea5d3-9752-4e12-ad28-eccd60ee21ec.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '조앤리키즈하우스' AND address = '제주특별자치도 서귀포시 성산읍 병문로 160-4 (오조리) 3호');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '조천댁', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 조천7길 19-17', 33.5383782, 126.6344741, '조천 마을에 위치한 노천탕이 있는 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201810/24/40cd6ea6-9082-47e9-a3e6-e93822da9893.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '조천댁' AND address = '제주특별자치도 제주시 조천읍 조천7길 19-17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '조천스테이', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 조천9길 30-1', 33.5382920, 126.6373900, '제주 전통 집 구조를 살린 현대적 감각의 톡채 렌트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201805/23/84a194f9-ec13-4bf3-9c6b-26354a549d94.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '조천스테이' AND address = '제주특별자치도 제주시 조천읍 조천9길 30-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '초', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 풍천로27번길 33-3', 33.3458100, 126.8492360, '성산읍 신천리에 위치한 Book store & Vacation 컨셉의 렌탈하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7f96b356-f883-4627-8180-0b9c8e9e2f1e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '초' AND address = '제주특별자치도 서귀포시 성산읍 풍천로27번길 33-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '코너스톤 스테이', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 조와로 27 (조천리) 코너스톤 스테이', 33.5331698, 126.6357375, '코너스톤 스테이는 제주시 조천읍에 위치한 민박집이다. 제주의 90년대 양옥집을 깨끗하게 단장하여 여행 오신 분들의 편안한 쉼을 위한 공간으로 내어드리고 있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202501/24/4ad5431c-7103-4d7e-895d-12ab9809ba85.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '코너스톤 스테이' AND address = '제주특별자치도 제주시 조천읍 조와로 27 (조천리) 코너스톤 스테이');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '클로버비앤비', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 시흥상동로 109', 33.4782050, 126.8958421, '우도와 바다, 성산일출봉이 한눈에 보이는 올레1코스 시작점에 위치한 클로버비앤비', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f02d4a6c-5439-4e9a-8e23-b3654ea11460.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '클로버비앤비' AND address = '제주특별자치도 서귀포시 성산읍 시흥상동로 109');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '탐라누리', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 금백조로 68', 33.4398313, 126.8658054, '서귀포시 성산읍 수산 자연생태마을에 위치한 민박집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f7843a8c-ddd6-4a6a-9bd9-9a76238f95a1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '탐라누리' AND address = '제주특별자치도 서귀포시 성산읍 금백조로 68');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '토마토하우스', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 성산등용로 96-7', 33.4698532, 126.9305924, '성산일출봉, 섭지코지 등 볼거리와 성산항 수산시장, 잠수함, 낚시배 등의 체험관광을 도보 5분 거리에서 즐길 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0bc7b715-5cc6-4df0-8764-edc836b4e0c1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '토마토하우스' AND address = '제주특별자치도 서귀포시 성산읍 성산등용로 96-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '패밀리아펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 번영로 3340', 33.3504691, 126.8235669, '유럽풍 복층형 독채 휴양펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/821a6dc7-a3cd-4284-a1d1-dcacc7f6c646.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '패밀리아펜션' AND address = '제주특별자치도 서귀포시 표선면 번영로 3340');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '편운산장', 'PENSION', 'EAST', '제주도 서귀포시 성산읍 서성일로 898-38', 33.4374500, 126.8751750, '황토로 지어진 전통 펜션과 식당을 겸한 산장', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5c17750a-8bd9-4ef0-bd6d-c8c58e19e3cf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '편운산장' AND address = '제주도 서귀포시 성산읍 서성일로 898-38');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '포지타노인제주', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 신천서로 27-5', 33.3429821, 126.8511337, '성산에 위치한 커플펜션 포지타노인제주펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/31ea3cbb-8dac-42e2-9473-3335da17eb58.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '포지타노인제주' AND address = '제주특별자치도 서귀포시 성산읍 신천서로 27-5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '푸르미르 펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 한도로 207', 33.4681900, 126.9300500, '푸르미르펜션은 성산포와 우도 그리고 섭지코지 근처에 위치 해 있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/66ae5b50-49f5-49d9-9720-93b18430cd56.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '푸르미르 펜션' AND address = '제주특별자치도 서귀포시 성산읍 한도로 207');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '피디스테이션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 평대10길 45-46', 33.5243749, 126.8423829, '제주 동쪽 작은 마을 안, 마음이 쉬어갈 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202005/13/b4c1b975-8225-49ab-93e4-163eda7ab79c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '피디스테이션' AND address = '제주특별자치도 제주시 구좌읍 평대10길 45-46');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하늘오름펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 중산간동로 1909', 33.4814988, 126.7512310, '하늘오름펜션은 제주의 아름다운 정경을 바라볼 수 있는 곳에 위치하고 있으며, 드넓게 펼쳐진 푸른 한라산과 제주 오름의 절경과 저녁에는 붉은빛이 아름다운 낙조를 볼 수 있는 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/fcb617be-5402-4665-8d0f-88aaca921375.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하늘오름펜션' AND address = '제주특별자치도 제주시 구좌읍 중산간동로 1909');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하늘을달리다', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 문주란로5길 25', 33.5208780, 126.8871700, '예스러움이 돋보이는 정겨운 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f62560e2-c2ad-495e-b890-385b04dc7fda.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하늘을달리다' AND address = '제주특별자치도 제주시 구좌읍 문주란로5길 25');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하도리보통날', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 하도1길 33', 33.5205650, 126.8788150, '하도리에 위치한 커플 전용 감성민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ca567d52-ebde-44ad-90d6-d32a0b9fddc1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하도리보통날' AND address = '제주특별자치도 제주시 구좌읍 하도1길 33');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하도리일번지', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 해맞이해안로 1860', 33.5218430, 126.8987600, '하도리 1번지에 있는 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/90fb73c2-3c76-4176-bfb4-e68768792cca.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하도리일번지' AND address = '제주특별자치도 제주시 구좌읍 해맞이해안로 1860');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하도하도 3200', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 면수1길 38', 33.5247145, 126.8642701, '제주의 돌담 집을 리모델링한 무인 렌탈하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201810/24/2a469717-9b9b-48fc-bd09-1b44070da326.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하도하도 3200' AND address = '제주특별자치도 제주시 구좌읍 면수1길 38');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하도하도1929', 'PENSION', 'EAST', '제주시 구좌읍 하도리 하도서문길 2', 33.5275080, 126.8814100, '제주의 돌집과 현대적 디자인이 더해진 독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b1ef6dc2-c64a-4d1e-b955-31e378fdfbeb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하도하도1929' AND address = '제주시 구좌읍 하도리 하도서문길 2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하루앤하루', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 조천7길 12', 33.5374600, 126.6355100, '새하얀 박공지붕과 제주돌집의 아름다운 조화', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6cb96fca-bb1f-4a1a-9f9e-ec04dcb9e317.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하루앤하루' AND address = '제주특별자치도 제주시 조천읍 조천7길 12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하이제인', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 별방길 113 (하도리)', 33.5272037, 126.8797585, '제주 감성 독채, 커플 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202501/31/627f085b-859b-4a8c-8589-366f5b40c09e.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하이제인' AND address = '제주특별자치도 제주시 구좌읍 별방길 113 (하도리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '함덕삼다펜션', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 조함해안로 488', 33.5427361, 126.6649530, '"제주 최고의 바다풍경" 시원한 바닷 바람이 아침 잠을 깨우는 곳.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3efb230b-f087-4554-977f-83ab42a70021.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '함덕삼다펜션' AND address = '제주특별자치도 제주시 조천읍 조함해안로 488');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해뜨는집', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 한도로 137', 33.4691308, 126.9226464, '강중훈 시인이 운영하는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202307/14/fc8e485b-ba7c-4473-9854-1ff9cf15c2da.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해뜨는집' AND address = '제주특별자치도 서귀포시 성산읍 한도로 137');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해뜨는초록마을', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 섭지코지로25번길 8 (고성리) 해뜨는초록마을펜', 33.4394539, 126.9203672, '성산에 위치해 제주의 편안함과 여유로움을 느낄 수 있는 자연휴양형펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202409/25/9e290d7d-e0c0-4b7a-b633-f25fe7230da2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해뜨는초록마을' AND address = '제주특별자치도 서귀포시 성산읍 섭지코지로25번길 8 (고성리) 해뜨는초록마을펜');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해마지펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 일주동로 5431', 33.3545979, 126.8612189, '제주도 최고의 일출명소 해마지펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/45e4eeca-c10f-429a-b2a9-18cf7424a2f8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해마지펜션' AND address = '제주특별자치도 서귀포시 성산읍 일주동로 5431');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해비치 바람의집', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 민속해안로 381', 33.3124730, 126.8366800, '표선해수욕장 해비치 바다 올레길4코스에 위치한 표선펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/85a49544-ee6d-4e56-a536-4592e9776a48.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해비치 바람의집' AND address = '제주특별자치도 서귀포시 표선면 민속해안로 381');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해일월', 'PENSION', 'EAST', '제주특별자치도 제주시 구좌읍 김녕항3길 26-29', 33.5398407, 126.7465830, '독채 렌탈 하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ada53dea-f35a-4eb8-8731-da9415bb70fa.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해일월' AND address = '제주특별자치도 제주시 구좌읍 김녕항3길 26-29');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '핸즈인제주', 'PENSION', 'EAST', '제주시 구좌읍 비자숲길 13', 33.4954260, 126.8121500, '걸어서 비자림에 갈수 있는 프로방스 스타일의 소박한 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/cad6818e-8981-4b13-a6d0-15321b9bafa5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '핸즈인제주' AND address = '제주시 구좌읍 비자숲길 13');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '헤세드하우스카페&펜션', 'PENSION', 'EAST', '제주특별자치도 서귀포시 표선면 가마행남로 42', 33.3053725, 126.8049995, '올레 4번길과 가마포구, 매오름을 즐길 수 있는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/5f5a3262-7d56-4487-8146-5985c92113dc.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '헤세드하우스카페&펜션' AND address = '제주특별자치도 서귀포시 표선면 가마행남로 42');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '휴양펜션 스위스마을 Swiss Village Pension', 'PENSION', 'EAST', '제주특별자치도 제주시 조천읍 와산서1길 32 401~405동', 33.4938544, 126.6759996, '조천읍 와산리에 위치한 농촌 관광 공동체 마을', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202302/06/98f2c9a0-d54c-4ad1-8a46-c6212e9eadb2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '휴양펜션 스위스마을 Swiss Village Pension' AND address = '제주특별자치도 제주시 조천읍 와산서1길 32 401~405동');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '휴일기록', 'PENSION', 'EAST', '제주특별자치도 서귀포시 성산읍 시흥하동로50번길 15', 33.4742568, 126.9068554, '제주의 아름다운 풍광을 담고있는 동쪽 시흥리에 위치, 가족, 연인과 프라이빗한 휴식을 누릴 수 있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/11/827ccca1-251b-42be-ad9b-1325937917bf.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '휴일기록' AND address = '제주특별자치도 서귀포시 성산읍 시흥하동로50번길 15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더포그레이스호텔앤리조트', 'RESORT', 'EAST', '제주특별자치도 서귀포시 성산읍 해맞이해안로 2670', 33.4736223, 126.9107438, '객실에서 우도와 성산일출봉을 조망할 수 있는 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201812/17/dabebc06-0584-4ddd-86cd-493121e46f99.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더포그레이스호텔앤리조트' AND address = '제주특별자치도 서귀포시 성산읍 해맞이해안로 2670');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '봄 그리고 가을리조트', 'RESORT', 'EAST', '제주특별자치도 서귀포시 성산읍 해맞이해안로 2660', 33.4738934, 126.9108755, '성산일출봉과 우도가 보이는 해맞이 해안로에 위치한 가족형 호텔 ＆ 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/22/5635ef29-7896-44b5-876e-2e15bbdd1e4f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '봄 그리고 가을리조트' AND address = '제주특별자치도 서귀포시 성산읍 해맞이해안로 2660');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '블라제리조트', 'RESORT', 'EAST', '제주특별자치도 서귀포시 표선면 녹산로 274', 33.3791145, 126.7469589, '단체 숙박 가능한 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/432184d0-f156-4065-8ec5-1b86815d03f1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '블라제리조트' AND address = '제주특별자치도 서귀포시 표선면 녹산로 274');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '소노캄제주', 'RESORT', 'EAST', '제주특별자치도 서귀포시 표선면 일주동로 6347-17', 33.3056347, 126.7921182, '소노캄 제주는 자연친화적 리조트로 주변 자연 경관과 조화를 이루도록 8층 높이로 설계되어 외국 휴양지의 편안한 느낌을 선사한다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202301/06/7a59a681-3224-4079-8da3-b84ab99600ac.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '소노캄제주' AND address = '제주특별자치도 서귀포시 표선면 일주동로 6347-17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아름다운리조트', 'RESORT', 'EAST', '제주특별자치도 서귀포시 성산읍 해맞이해안로 2644', 33.4758300, 126.9099100, '올레1코스의 중심에 위치하여 오른쪽으로는 성산일출봉과 우도가, 왼쪽으로는 종달리해안도로가 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/11eed429-493b-4b5e-a842-39c75630cf43.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아름다운리조트' AND address = '제주특별자치도 서귀포시 성산읍 해맞이해안로 2644');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오션스퀘어 리조트', 'RESORT', 'EAST', '서귀포시 환해장성로 667', 33.4131930, 126.9088600, '성산에 위치한 여유로운 휴식공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b6a22abb-c6c9-441c-8764-455077b062f7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오션스퀘어 리조트' AND address = '서귀포시 환해장성로 667');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '코코 프리미어 리조트', 'RESORT', 'EAST', '제주특별자치도 서귀포시 성산읍 환해장성로 231 (신산리, 오션 갤러리 프리미어 빌라스 2단지)', 33.3878898, 126.8895994, '제주 동쪽 성산읍 해안도로에 위치한 힐링 스테이', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202511/28/073bcfab-801a-4af2-a0b9-d52bd5bf834a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '코코 프리미어 리조트' AND address = '제주특별자치도 서귀포시 성산읍 환해장성로 231 (신산리, 오션 갤러리 프리미어 빌라스 2단지)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '크리스마스리조트', 'RESORT', 'EAST', '제주특별자치도 제주시 구좌읍 해맞이해안로 2030-8', 33.5091405, 126.8999502, '하도해수욕장 인근에 위치한 크리스마스리조트는 독채 펜션으로 다양한 부대시설이 있어 편안하고 즐거운 하루를 즐기기 최적의 장소다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/40ee124c-3e9c-4a6f-a01e-f673af259a77.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '크리스마스리조트' AND address = '제주특별자치도 제주시 구좌읍 해맞이해안로 2030-8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해돋는마을', 'RESORT', 'EAST', '제주특별자치도 서귀포시 성산읍 한도로 116-1', 33.4681830, 126.9203552, '해돋는 마을은 우도와 일출봉이 눈앞에 보이는 곳에 위치한 콘도형 민박입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/01847aaa-6d57-43b5-aa77-275b1cdb2f26.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해돋는마을' AND address = '제주특별자치도 서귀포시 성산읍 한도로 116-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해비치 호텔앤드리조트 제주', 'RESORT', 'EAST', '제주특별자치도 서귀포시 표선면 민속해안로 537', 33.3234500, 126.8447800, '제주의 푸른 바다와 자연을 만끽할 수 있는 표선에 위치한 호텔&리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202508/20/dbd32719-13d7-4489-bda4-36ace5bb22cd.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해비치 호텔앤드리조트 제주' AND address = '제주특별자치도 서귀포시 표선면 민속해안로 537');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '휘닉스 아일랜드', 'RESORT', 'EAST', '제주특별자치도 서귀포시 성산읍 섭지코지로 107', 33.4305760, 126.9277700, '섭지코지 주변에 위치한 자연친화적인 해양종합리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/7026ae01-0cc3-4e6d-ad74-549d8e3de64e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '휘닉스 아일랜드' AND address = '제주특별자치도 서귀포시 성산읍 섭지코지로 107');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '휘닉스 아일랜드 제주', 'RESORT', 'EAST', '제주특별자치도 서귀포시 성산읍 섭지코지로 107', 33.4300919, 126.9272996, '휘닉스 아일랜드는 제주의 자연을 그대로 보존하면서 사람과 건축, 문화가 어우러지도록 개발된 자연 친화적인 리조트입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202604/09/f7bf9526-fb29-49d0-ba67-11a584485c05.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '휘닉스 아일랜드 제주' AND address = '제주특별자치도 서귀포시 성산읍 섭지코지로 107');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주자연인펜션글램핑', 'CAMPING', 'NORTH', '제주특별자치도 제주시 아란서길 110', 33.4683293, 126.5385409, '식사+글램핑+숙박+야외공연 까지 들을 수 있는 아름다운 공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1dcf0112-912d-44dd-9644-ad1e52e4c888.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주자연인펜션글램핑' AND address = '제주특별자치도 제주시 아란서길 110');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '간드락게스트하우스', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 간월동로 12', 33.4853213, 126.5439807, '홈스테이 스타일의 친구집 같은 편안한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c60cbab3-2508-4f1c-b52c-4229b81885e2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '간드락게스트하우스' AND address = '제주특별자치도 제주시 간월동로 12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '게스트하우스7', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 용문로 168 (용담 2동) 2층', 33.5089200, 126.5112000, '제주 공항 근처 깨끗한 호텔형 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/07/e0e94a62-032e-4a83-8990-9feca8b9c0af.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '게스트하우스7' AND address = '제주특별자치도 제주시 용문로 168 (용담 2동) 2층');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '또랑게스트하우스', 'GUESTHOUSE', 'NORTH', '제주 제주시 한북로 338', 33.4599720, 126.5376800, '또랑게스트하우스는 공항에서 20분,신제주16분의 거리에 서귀포로 넘어가는 길쪽에 위치해 있다. 주변 관광지로는 한라산(성판악,관음사),사려니숲길,산굼부리,제주절물자연휴양림,마방목지,신비의도로,러브랜드,제주별빛누리공원,김경숙해바라기,제주도립미술관,한라수목원,제주미니랜드,등등 가까이 위치해 있다. ​​​​​​​또한 한라산 가시는 분들께는 김밥, 물을 서비스하고, 픽업을 돕고 있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a060052d-f47c-4e30-b49f-745de1be2ba0.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '또랑게스트하우스' AND address = '제주 제주시 한북로 338');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '란펜션 &게스트하우스', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 용마서1길 19-2', 33.5155248, 126.5045608, '걸어서 5분거리에 용두암이 위치한 란펜션은 앞에는바다 뒤에는 한라산자락이 멀리보이고 차로5분거리에 이마트가위치해있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5e93555d-3bd0-4b3e-a8f3-53008531b583.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '란펜션 &게스트하우스' AND address = '제주특별자치도 제주시 용마서1길 19-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '루미수다게스트하우스', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 중앙로 192', 33.5020700, 126.5279540, '끊임없는 웃음과 수다로 사랑의 인연이 맺어가는 집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c26d0b53-fdcc-4045-989b-6d920c011291.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '루미수다게스트하우스' AND address = '제주특별자치도 제주시 중앙로 192');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '린든호스텔', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 서광로 278', 33.4999689, 126.5262754, '자연과의 자유로운 생활을 꿈꾸며 시작한 호스텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/244e77ab-d060-4e5e-9f18-ada60e35b367.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '린든호스텔' AND address = '제주특별자치도 제주시 서광로 278');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '미라클게스트하우스', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 서해안로 346-9', 33.5095940, 126.4820000, '제주공항 근처에 위치한 펜션과 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/51ca01c6-f241-4d26-bb14-d2e359ee1f82.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '미라클게스트하우스' AND address = '제주특별자치도 제주시 서해안로 346-9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '미소게스트하우스', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 관덕로 41', 33.5134120, 126.5238300, '원도심속에 편안한 잠자리를 제공하는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/522fbb31-7d12-4cf7-809b-19927948ad8e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '미소게스트하우스' AND address = '제주특별자치도 제주시 관덕로 41');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '비안', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 조천읍 중산간동로 1197-16', 33.4777030, 126.7135540, '조용한 제주 중산간 마을 선흘리에 있는 독채형 B&B', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/43690c56-6b02-4488-b5f0-534c91691761.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '비안' AND address = '제주특별자치도 제주시 조천읍 중산간동로 1197-16');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '비지터게스트하우스', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 오현길 85', 33.5123600, 126.5252150, '50년 역사가 전해져오는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/dbca6cca-18c2-481f-84a3-78f0bb7c5fd7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '비지터게스트하우스' AND address = '제주특별자치도 제주시 오현길 85');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '삼다호스텔', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 서해안로 456-6', 33.5182080, 126.4889400, '삼다호텔은 용담 해안도로에 위치한 펜션형 호텔입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6360af00-9a0d-41f2-8cf8-049b01368872.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '삼다호스텔' AND address = '제주특별자치도 제주시 서해안로 456-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '숨게스트하우스 제주공항점', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 서광로5길 2-2', 33.5004078, 126.5147104, '제주공항 5분거리에 있는 뚜벅이 여행자들의 최고 휴식처', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/48a7e269-1f89-41ae-b75e-80b6a9958093.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '숨게스트하우스 제주공항점' AND address = '제주특별자치도 제주시 서광로5길 2-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주공항 게스트하우스 예스준', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 서해안로 232 (도두일동)', 33.5085520, 126.4708035, '제주공항 5분 도두 무지개 해안도로에 위치한 예스준 게스트하우스. 바다뷰 객실과 바다뷰 루프탑이 있어 제주힐링여행에 딱 좋은 게스트하우스.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/17/de8265e6-858d-4bb7-a56d-e77bbe6a4592.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주공항 게스트하우스 예스준' AND address = '제주특별자치도 제주시 서해안로 232 (도두일동)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주마실게스트하우스', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 서광로2길 11-16', 33.4988783, 126.5136184, '제주터미널 인근의 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c7ac1ab3-4eb8-4c58-9d01-d178157afdb3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주마실게스트하우스' AND address = '제주특별자치도 제주시 서광로2길 11-16');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '펜션 에덴호스텔', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 서해안로 456-12', 33.5184357, 126.4893095, '제주국제공항으로부터 차로 7~8분 거리에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/78884c14-6c9f-4307-9416-777860f184ee.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '펜션 에덴호스텔' AND address = '제주특별자치도 제주시 서해안로 456-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'YOU&I게스트하우스', 'GUESTHOUSE', 'NORTH', '제주특별자치도 제주시 광양8길 1-2', 33.4997710, 126.5291961, '제주공항근처에 있고 교통이 편리하고 깨끗하며 안락한 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2c137840-1965-4b90-a5e3-50271b39bdc7.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'YOU&I게스트하우스' AND address = '제주특별자치도 제주시 광양8길 1-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '가이아호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 연동7길 4', 33.4894172, 126.4906421, '바오젠거리 내에 위치하여, 관광 및 쇼핑을 편리하게 다닐수 있는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/bc6cfed1-eca4-45ca-9e02-b41f56f10880.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '가이아호텔' AND address = '제주특별자치도 제주시 연동7길 4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '갤러리 호텔 비앤비', 'HOTEL', 'NORTH', '제주특별자치도 제주시 삼무로3길 48', 33.4905780, 126.4907460, '제주국제공항에서 가까운 편리한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/36011b0d-3d85-43b7-be17-b7893da83319.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '갤러리 호텔 비앤비' AND address = '제주특별자치도 제주시 삼무로3길 48');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '골든파크호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 서광로2길 9', 33.4992330, 126.5130500, '제주국제공항과 제주시외버스터미널 근처에 위치한 숙박업소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/4830504b-ef0d-4936-bd88-f2e85d9c7bcd.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '골든파크호텔' AND address = '제주특별자치도 제주시 서광로2길 9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '그라벨호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 일주서로 7316', 33.4923190, 126.4284746, '조약돌에 부딪히는 파도소리가 들려오는 여류롭고 평화로운 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f2a172a8-09c5-4f4d-b30d-63a40f298e9f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '그라벨호텔' AND address = '제주특별자치도 제주시 일주서로 7316');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '늘송파크텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 원노형5길 22', 33.4875793, 126.4832153, '공항과 인접하며, 시내중심지에 위치한 제주도 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e4754848-539e-4795-b7b3-d0c26e57092b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '늘송파크텔' AND address = '제주특별자치도 제주시 원노형5길 22');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '다이아몬드 호텔 제주', 'HOTEL', 'NORTH', '제주특별자치도 제주시 신대로12길 42', 33.4889189, 126.4922938, '제주시 연동에 위치해 공항과 가까운 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/76817eac-31d2-467f-9437-70aa493f26b6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '다이아몬드 호텔 제주' AND address = '제주특별자치도 제주시 신대로12길 42');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더레드관광호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 동광로6길 10', 33.5010293, 126.5319039, '푸른 바다와 아름다운 한라산이 한눈에 보이는 경관이 좋은 관광호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a3394703-c0d6-49b4-b707-88ff11560195.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더레드관광호텔' AND address = '제주특별자치도 제주시 동광로6길 10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더오크라호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 신광로4길 38', 33.4891105, 126.4861229, '공항 인근, 신제주 연동 번화가에 위치한 가성비 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202302/24/2fe45e0d-fce7-4b63-9d8c-5003e8855710.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더오크라호텔' AND address = '제주특별자치도 제주시 신광로4길 38');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '디셈버호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 삼무로1길 15', 33.4903086, 126.4897335, '다양한 부대시설이 준비되어 있는 제주도 2성급 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201902/12/80e170ec-8d61-4c13-94f3-ef320d9644c8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '디셈버호텔' AND address = '제주특별자치도 제주시 삼무로1길 15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라마다제주시티홀', 'HOTEL', 'NORTH', '제주특별자치도 제주시 중앙로 304', 33.4934657, 126.5343138, '제주 시내에 위치하여 관광, 쇼핑, 비지니스에 최적화된 세계적 브랜드의 체인 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/2c86a260-118d-4de2-ae5d-6c58d1e98550.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라마다제주시티홀' AND address = '제주특별자치도 제주시 중앙로 304');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '롯데시티호텔 제주', 'HOTEL', 'NORTH', '제주특별자치도 제주시 도령로 83', 33.4909740, 126.4869700, '간결하며 실용적인 공간, 자연스러움과 편안함을 품은 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ebb31bf9-731d-4a76-9b51-c6f707d8fa52.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '롯데시티호텔 제주' AND address = '제주특별자치도 제주시 도령로 83');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '맥스호텔제주', 'HOTEL', 'NORTH', '제주특별자치도 제주시 연동12길 4', 33.4853318, 126.4930596, '2019년 오픈한 라이프스타일 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202011/12/6678cd95-16ac-4fb9-aeb9-fa869de5bcb3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '맥스호텔제주' AND address = '제주특별자치도 제주시 연동12길 4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '메이저호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 사장3길 36', 33.4830253, 126.4911069, '제주시 연동에 위치한 객실수42개의 모던한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/06/664a8976-78d2-41e7-9e72-a67811e4957f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '메이저호텔' AND address = '제주특별자치도 제주시 사장3길 36');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '메종 글래드 제주', 'HOTEL', 'NORTH', '제주특별자치도 제주시 노연로 80', 33.4847560, 126.4887100, '교통과 문화의 중심 신제주에 위치한 특1급 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/6eacb5aa-1383-4844-b7e9-40a1c0ec3371.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '메종 글래드 제주' AND address = '제주특별자치도 제주시 노연로 80');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '베스트웨스턴 제주 호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 도령로 27', 33.4877970, 126.4818950, '제주 시내에 위치한 세계 최대 체인브랜드 비지니스 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/25a9172c-cc28-445a-b442-c38c8c3dab05.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '베스트웨스턴 제주 호텔' AND address = '제주특별자치도 제주시 도령로 27');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '블랙샌즈호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 선사로 8', 33.5219994, 126.5873890, '삼양 검은모래 해변이 가까이에 있는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fb3a1f48-4577-4f28-a75c-b91f69ce58c2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '블랙샌즈호텔' AND address = '제주특별자치도 제주시 선사로 8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '삼해인관광호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 도령로 108', 33.4912935, 126.4896228, '천년의 아름다움을 간직한 바다로의 초대, 삼해인', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/baab7233-316e-4fda-a17d-455697b0f4c4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '삼해인관광호텔' AND address = '제주특별자치도 제주시 도령로 108');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스마일모텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 연동13길 42', 33.4861576, 126.4943880, '공항 근처에 있는 편안한 모텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/db869dc3-0d3a-458c-9d65-48cdc95ff239.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스마일모텔' AND address = '제주특별자치도 제주시 연동13길 42');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스타즈호텔 헤리티지', 'HOTEL', 'NORTH', '제주특별자치도 제주시 관덕로 26', 33.5128159, 126.5219322, '제주의 자연과 생활을 한눈에 경험할 수 있는, 내집같은 편안한 서비스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/64eabacf-50b8-4fcb-a7fb-8d226b0258f8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스타즈호텔 헤리티지' AND address = '제주특별자치도 제주시 관덕로 26');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '신강남모텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 서광로3길 2', 33.5004243, 126.5136081, '제주시외버스 터미널 바로 앞에 있는 모텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d51c304f-6da0-4f8f-87d7-d289d6413bb3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '신강남모텔' AND address = '제주특별자치도 제주시 서광로3길 2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '신라스테이 제주', 'HOTEL', 'NORTH', '제주특별자치도 제주시 노연로 100', 33.4855800, 126.4909700, '제주시내에 위치한 호텔신라의 프리미엄 비지니스 호텔 브랜드', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/15f4cdbd-acc8-4873-8b78-958db0a733d2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '신라스테이 제주' AND address = '제주특별자치도 제주시 노연로 100');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '신라스테이 플러스 이호테우', 'HOTEL', 'NORTH', '제주특별자치도 제주시 연대마을길 76', 33.4927867, 126.4227730, '이국적인 이호테우의 풍경을 바라보며 신라스테이의 가치를 경험할 수 있는 레저형 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202405/24/deee87a4-06b2-414a-9a33-58c32abaf90b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '신라스테이 플러스 이호테우' AND address = '제주특별자치도 제주시 연대마을길 76');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '신신호텔 제주공항', 'HOTEL', 'NORTH', '제주특별자치도 제주시 도령로 124 (연동, 신신호텔 제주공항) 신신호텔 제주공항', 33.4922698, 126.4906496, '제주국제공항 인근에 위치한 가성비 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202402/07/d0e0aa81-8f1c-47f0-9681-35063d9394e8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '신신호텔 제주공항' AND address = '제주특별자치도 제주시 도령로 124 (연동, 신신호텔 제주공항) 신신호텔 제주공항');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '심스호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 임항로 249', 33.5178257, 126.5396293, '사라봉과 제주항이 내려다 보이며 아름다운 낙조를 조망할 수 있는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/4f24fe64-9458-48c7-bc39-6dc4f8cc590f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '심스호텔' AND address = '제주특별자치도 제주시 임항로 249');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '씨앤호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 일주서로 7490', 33.4936717, 126.4470180, '제주 공항 15분 거리, 이호해수욕장 도보 10분 거리에 위치한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e81f17df-16ff-4a38-8d07-1fa4d2902c8a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '씨앤호텔' AND address = '제주특별자치도 제주시 일주서로 7490');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '씨제이 관광호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 광양8길 24', 33.4997873, 126.5271555, '제주의 아름다운 추억을 위한 공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8a4d2078-a206-48c4-b98f-a86beb93f0e7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '씨제이 관광호텔' AND address = '제주특별자치도 제주시 광양8길 24');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아스타호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 서사로 129', 33.5008810, 126.5204994, '제주공항 및 여객선터미널로부터 10분 이내 거리인 제주시내 중심가에 위치하고있어 매우 편리한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/2b068621-ae28-4f05-a877-f1bbfa81f074.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아스타호텔' AND address = '제주특별자치도 제주시 서사로 129');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아이존호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 신광로4길 18', 33.4890384, 126.4882686, '신제주 중심가에 위치한 먹을거리와 즐길거리가 가까이에 있는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/89516c7a-3520-4659-9c62-5e284d8e7ac9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아이존호텔' AND address = '제주특별자치도 제주시 신광로4길 18');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '에스호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 오일장중길 26', 33.4954340, 126.4715100, '제주오일장과 가깝고 활주로와 바다가 내려다보이는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/54271d4d-41c3-4de7-aea1-625d2ff64a2e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '에스호텔' AND address = '제주특별자치도 제주시 오일장중길 26');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '엘린호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 은남1길 4', 33.4893299, 126.4954791, '제주공항 5분 거리, 시내 중심가에 위치하고 있어 관광지, 관공서, 쇼핑, 문화 등 주변 시설을 이용하기에 편리한 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/13/f9828a01-839a-4313-b488-2ebb22bf955e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '엘린호텔' AND address = '제주특별자치도 제주시 은남1길 4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '엘앤엘 센트럴호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 노연로 24', 33.4859312, 126.4828144, '공항에서 5분거리 품격있고 편안한 아름다운 휴식처', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b5c298ff-9f82-4d8e-9170-a159c3c1d434.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '엘앤엘 센트럴호텔' AND address = '제주특별자치도 제주시 노연로 24');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '엘앤엘제주호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 삼무로 12 (연동)', 33.4897230, 126.4878600, '제주도심 한가운데에 위치한 "빛나는 보석" Amber Hotel', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/f9cc6454-5866-4734-b92a-429830bcdeea.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '엘앤엘제주호텔' AND address = '제주특별자치도 제주시 삼무로 12 (연동)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오렌지트리 호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 관덕로8길 7-12', 33.5122997, 126.5243863, '구제주의 중심에 위치한 호텔과 카페를 겸한 편안하고 유니크한 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4688cb0c-7c06-4522-8e45-95587b114200.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오렌지트리 호텔' AND address = '제주특별자치도 제주시 관덕로8길 7-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오로라호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 논세길 51', 33.4894470, 126.4421364, '에메랄드 빛 바다와 황금해변, 명품가족호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5cbf6b8f-ec97-4a79-aca5-7e9981c57c80.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오로라호텔' AND address = '제주특별자치도 제주시 논세길 51');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오션스위츠 제주호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 탑동해안로 74', 33.5182300, 126.5231700, '아름다운 제주의 바다와 바로 인접해 있으며 편리한 교통의 요지에 위치한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6787bc58-ce55-4466-ad94-943b35da76f4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오션스위츠 제주호텔' AND address = '제주특별자치도 제주시 탑동해안로 74');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오션패밀리호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 일주서로 7531', 33.4952562, 126.4510884, '제주공항에 인접한 교통의 요충지에 있는 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/14edfc70-ae1e-4554-935d-457acf26c04b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오션패밀리호텔' AND address = '제주특별자치도 제주시 일주서로 7531');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '올레관광호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 서광로 254', 33.4995300, 126.5237200, '깔끔하고 편리한 제주 올레관광호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/09760580-e5d7-4d5f-a624-e3d7dff8e294.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '올레관광호텔' AND address = '제주특별자치도 제주시 서광로 254');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제니아관광호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 은남1길 25', 33.4891300, 126.4931640, '고객을 가족처럼 따뜻하고 편안히 맞이하는 비지니스형 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/63ce8cec-aa98-4477-afb1-c44142fd4789.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제니아관광호텔' AND address = '제주특별자치도 제주시 은남1길 25');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 퍼시픽호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 서사로 20', 33.5106545, 126.5191158, '공항과 가까운 용담에 위치한 특급호텔 퍼시픽호텔입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/bcd34de9-3d48-4f2a-8a13-4e658f3df2ad.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 퍼시픽호텔' AND address = '제주특별자치도 제주시 서사로 20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 호텔 더블유 탑동', 'HOTEL', 'NORTH', '제주특별자치도 제주시 임항로 24', 33.5163159, 126.5273156, '안락하고 편안한 여행이 될 수 있는 HOTEL W', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a6bb8eeb-eda0-4217-9c5b-3e5dca597e1d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 호텔 더블유 탑동' AND address = '제주특별자치도 제주시 임항로 24');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 호텔 더원', 'HOTEL', 'NORTH', '제주특별자치도 제주시 사장3길 33', 33.4827145, 126.4913490, '삼다의 섬 제주에서 돌처럼 굳건하게, 여인처럼 부드럽게, 바다처럼 신속한 서비스를 약속하는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/dd8db481-44e7-4b68-8564-2013c41b10a7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 호텔 더원' AND address = '제주특별자치도 제주시 사장3길 33');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주갤럭시호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 남성로 33', 33.5031446, 126.5126490, '제주시내 교통이 편리한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/92d31a7f-ebf7-49dd-b2af-4399ea435310.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주갤럭시호텔' AND address = '제주특별자치도 제주시 남성로 33');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주로얄호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 신대로12길 45', 33.4884950, 126.4917500, '안락한 휴식을 제공하는 제주로얄호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/44788878-79ae-4e26-a0d9-a8a15b42aac6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주로얄호텔' AND address = '제주특별자치도 제주시 신대로12길 45');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주메이플호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 원노형3길 41', 33.4865192, 126.4817631, '제주시내 중심가에 위치한 교통이 편리한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7ecf14eb-da4e-4267-873e-55868c6516ea.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주메이플호텔' AND address = '제주특별자치도 제주시 원노형3길 41');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주스위트모텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 삼무로3길 9', 33.4901961, 126.4949390, '깔끔하고 고급스러운 인테리어와 쾌적한 서비스로 고객여러분을 맞이하는 스위트 모텔입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/44a8e39f-5630-4b57-9bc9-0b3dffeb5b23.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주스위트모텔' AND address = '제주특별자치도 제주시 삼무로3길 9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주썬호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 삼무로 67', 33.4901102, 126.4940270, '제주시내 중심가에 위치한 고품격 휴식처', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202506/18/75dbe907-6944-483f-8a8d-935b6de62445.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주썬호텔' AND address = '제주특별자치도 제주시 삼무로 67');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주오리엔탈호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 탑동로 47', 33.5172617, 126.5199138, '제주국제공항에서 약 10분, 제주항에서 5분 거리에 위치한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9ff948d5-0168-441a-b836-607a0da8e91d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주오리엔탈호텔' AND address = '제주특별자치도 제주시 탑동로 47');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주인호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 신산마을길 20', 33.4918750, 126.4458500, '교통적으로 뛰어난 위치에 있는 실용적인 관광호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/86859b6c-f349-406d-a0d9-8931408baa07.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주인호텔' AND address = '제주특별자치도 제주시 신산마을길 20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주팔레스호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 탑동로 9', 33.5168631, 126.5237566, '제주국제공항에서 차로 10분거리인 탑동에 위치한 제주팔레스호텔입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8e4ae181-a42e-4929-82be-e2a526141644.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주팔레스호텔' AND address = '제주특별자치도 제주시 탑동로 9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '파크사이드호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 도령로 126', 33.4923991, 126.4908271, '신제주 중심에 위치한 깔끔한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/23cbf962-6c2f-4783-a4ae-377f8392e41a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '파크사이드호텔' AND address = '제주특별자치도 제주시 도령로 126');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '팜파스호텔 제주', 'HOTEL', 'NORTH', '제주특별자치도 제주시 서해안로 368-3 (용담삼동, ciel bleu 호텔) 팜파스 호텔 제주', 33.5115783, 126.4847587, '제주 시내에 위치한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202312/29/2ee6cb79-8781-43c2-b43c-76a46363ff59.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '팜파스호텔 제주' AND address = '제주특별자치도 제주시 서해안로 368-3 (용담삼동, ciel bleu 호텔) 팜파스 호텔 제주');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '펄호텔제주', 'HOTEL', 'NORTH', '제주특별자치도 제주시 신대로20길 49', 33.4834750, 126.4912640, '제주시에 위치한 관광호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201904/22/b6ff3f16-635e-408b-a40d-3325ebaf8ebc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '펄호텔제주' AND address = '제주특별자치도 제주시 신대로20길 49');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '포시즌호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 원노형3길 19', 33.4830980, 126.4772312, '세련되고 특별한 디자인의 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d0c03215-73e7-4f97-834c-e50a0020a1cc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '포시즌호텔' AND address = '제주특별자치도 제주시 원노형3길 19');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하니크라운호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 삼성로 10', 33.5049614, 126.5273789, '하니크라운호텔은 쾌적하고 안락하게 잘 꾸며진 객실과 훌륭한 편의시설, 넓은 주차장을 완비하고 있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4ccb8faf-ed0f-4ead-9ac5-39fe205898f3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하니크라운호텔' AND address = '제주특별자치도 제주시 삼성로 10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해마호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 삼무로 28', 33.4897057, 126.4896117, '제주시내에 위치한 심플하고 우아한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/8af01c4b-8155-44d4-a321-33d7169863a3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해마호텔' AND address = '제주특별자치도 제주시 삼무로 28');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 서해안로 526', 33.5197387, 126.4952520, '공항에서 5분, 제주항에서 10분인 거리에 있으며, 주변관광지인 용담해안도로가 있어 시원한 바다풍경을 보기에 좋은 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2ba7a5e8-b2e9-4a91-8cd8-772992d3ce11.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해호텔' AND address = '제주특별자치도 제주시 서해안로 526');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔 리젠트마린', 'HOTEL', 'NORTH', '제주특별자치도 제주시 서부두2길 20', 33.5176240, 126.5271100, '제주 탑동 광장 앞, 제주 최대의 횟집 단지인 ''서부두 명품횟집거리''옆에 위치한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/732af6f2-d951-4f35-82d3-04b58c4b528a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔 리젠트마린' AND address = '제주특별자치도 제주시 서부두2길 20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔 스카이파크 제주1호점', 'HOTEL', 'NORTH', '제주특별자치도 제주시 삼무로 48', 33.4894322, 126.4918781, '공항에서 10분 거리로 누웨모루거리와 가까운 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202305/19/272339a7-0239-4b64-a617-cc12429a0937.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔 스카이파크 제주1호점' AND address = '제주특별자치도 제주시 삼무로 48');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔 휘슬락 제주', 'HOTEL', 'NORTH', '제주특별자치도 제주시 서부두2길 26', 33.5173865, 126.5275849, '신비로운 제주의 바다가 한 폭의 그림으로 기억되는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/47915447-10a3-4864-8181-680f67e84180.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔 휘슬락 제주' AND address = '제주특별자치도 제주시 서부두2길 26');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔난타', 'HOTEL', 'NORTH', '제주특별자치도 제주시 선돌목동길 56-26', 33.4452930, 126.5479200, '호텔난타는 200개가 넘는 객실을 보유한 4성급 호텔이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c2f1bbac-65fa-4b46-ad20-94f4570bbf11.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔난타' AND address = '제주특별자치도 제주시 선돌목동길 56-26');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔레오', 'HOTEL', 'NORTH', '제주특별자치도 제주시 삼무로 14', 33.4897350, 126.4883200, '220여 점의 조각과 그림이 전시된 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/ef13376b-21ce-4029-a0d0-1146911e78e8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔레오' AND address = '제주특별자치도 제주시 삼무로 14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔샬롬제주', 'HOTEL', 'NORTH', '제주특별자치도 제주시 동광로 34', 33.5019300, 126.5326840, '호텔 샬롬제주는 제주국제공항에서 차로 10분거리, 제주시청에서 200m, 제주시외버스터미널에서 3분거리에 위치 해 있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fab51b99-7de3-42fd-87a7-0e41fa407e96.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔샬롬제주' AND address = '제주특별자치도 제주시 동광로 34');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔시리우스', 'HOTEL', 'NORTH', '제주특별자치도 제주시 도령로 133', 33.4933987, 126.4909571, '제주공항에서 가장 가까운 특급호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/11/4ccb0590-84a9-4416-9e21-6d9b03310e7e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔시리우스' AND address = '제주특별자치도 제주시 도령로 133');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔제이엠', 'HOTEL', 'NORTH', '제주특별자치도 제주시 삼무로1길 10-7', 33.4903668, 126.4888158, '신제주 도심 속 교통이 편리한 곳에 위치한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/023c89bd-2ce0-4754-9142-84352bf91ca1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔제이엠' AND address = '제주특별자치도 제주시 삼무로1길 10-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔컬리넌제주', 'HOTEL', 'NORTH', '제주특별자치도 제주시 사장1길 26', 33.4853573, 126.4928515, '제주의 중심 연동에 위치한 BOUTIQUE&BUSINESS 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/a42dd45a-b3c1-477b-95bf-c9ee40b3cdd6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔컬리넌제주' AND address = '제주특별자치도 제주시 사장1길 26');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔펄리플러스', 'HOTEL', 'NORTH', '제주특별자치도 제주시 도두봉 2길 2', 33.5054100, 126.4699700, '공항근처 제주시 도두동에 위치한 가족호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/02/442f9f9d-2f9d-4aff-9f24-94312678efe9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔펄리플러스' AND address = '제주특별자치도 제주시 도두봉 2길 2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔W', 'HOTEL', 'NORTH', '제주특별자치도 제주시 은남4길 42', 33.4865472, 126.4913557, '안락하고 편안한 여행이 될 수 있는 제주시내의 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fece189a-adc2-4dd3-b368-af1377e279e3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔W' AND address = '제주특별자치도 제주시 은남4길 42');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'ATnoon호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 은남1길 8', 33.4893618, 126.4950589, '제주도심 중심가에 위치한 비지니스 관광호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c8d8b8f3-d1fb-4913-bc86-a0eaa2a95e7d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'ATnoon호텔' AND address = '제주특별자치도 제주시 은남1길 8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'K관광호텔', 'HOTEL', 'NORTH', '제주특별자치도 제주시 연동3길 10', 33.4890764, 126.4879086, '제주시내 상권에 위치하고 있는 모던한 이미지의 관광호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/31a089f1-8e44-4482-bd09-999cc40cc3fb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'K관광호텔' AND address = '제주특별자치도 제주시 연동3길 10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '가까이에파도소리', 'PENSION', 'NORTH', '제주도 제주시 외도2동 연대마을길 44', 33.4948580, 126.4249500, '가까이서 들리는 파도소리와 함께 낭만을 즐길 수 있는 바닷가 예쁜 집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/31eb7290-c157-4e17-bbc8-2dcc77d40347.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '가까이에파도소리' AND address = '제주도 제주시 외도2동 연대마을길 44');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '골든비치', 'PENSION', 'NORTH', '제주도 제주시 서해안로 486-3', 33.5186230, 126.4912500, '공항과 가까운 해안도로에 위치한 휴양형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/03215146-56c2-4e43-933d-c11a8684b39f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '골든비치' AND address = '제주도 제주시 서해안로 486-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '그린나래', 'PENSION', 'NORTH', '제주도 서귀포시 성산읍 서성일로 615', 33.4278400, 126.8509000, '성산일출봉이 한눈에 보이는곳에 자리잡은 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/cb7f0078-cd1b-49aa-b8ad-68a79162b4da.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '그린나래' AND address = '제주도 서귀포시 성산읍 서성일로 615');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '꿈꾸는바다펜션', 'PENSION', 'NORTH', '제주도 제주시 도두항서길 65', 33.5041580, 126.4637760, '푸른 바다와 한라산을 만끽하며 여정을 풀 수 있는 최적의 장소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c7da3798-9fc1-4217-ae88-4eab893b28f6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '꿈꾸는바다펜션' AND address = '제주도 제주시 도두항서길 65');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '낭만펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 도두5길 28', 33.5048108, 126.4720660, '공항과 가깝고 올레길 17코스에 위치해있는 낭만제주펜션은 주변에 이호테우해변 용두암 곽지해변이있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/98e3ffbe-2823-439e-9aa0-d38bdac7c5b7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '낭만펜션' AND address = '제주특별자치도 제주시 도두5길 28');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '달빛바다펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 어영길 64-12', 33.5174180, 126.4987800, '달빛펜션은 제주국제공항에서 5분거리에 위치하였으며 모든 객실에서 바다 조망이 가능하다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/13b57f17-4aad-404e-a82c-ca4dcc274756.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '달빛바다펜션' AND address = '제주특별자치도 제주시 어영길 64-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더오라', 'PENSION', 'NORTH', '제주특별자치도 제주시 동성길 36-1 3층', 33.4968978, 126.5095169, '제주 시내에 오소록이 숨어있는 농어촌민박 더오라스테이', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202104/27/a7f43631-d7dd-4ab6-a0c4-9ff2b2669c65.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더오라' AND address = '제주특별자치도 제주시 동성길 36-1 3층');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '돌꽃펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 서해안로 572-1', 33.5189566, 126.4988573, '아름다운 제주의 해안도로 근처에 위치하고 있으며 심플하고 깔끔한 원룸형 펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202011/02/9ddb0106-c80c-4c04-96a6-11ad7ee68293.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '돌꽃펜션' AND address = '제주특별자치도 제주시 서해안로 572-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '리버풀펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 도두봉6길 13', 33.5080500, 126.4720700, '도두봉과 인접한 리버풀펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/66157835-46a7-4eb9-826c-17760cbe324d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '리버풀펜션' AND address = '제주특별자치도 제주시 도두봉6길 13');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '리틀해녀', 'PENSION', 'NORTH', '제주특별자치도 제주시 용마로3길 10-7', 33.5147421, 126.5056688, '리틀해녀 북스테이, 가족과 함께 용담해안도로 독채민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202201/25/28374255-3ef5-4a96-a72d-0cd1fb453a8e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '리틀해녀' AND address = '제주특별자치도 제주시 용마로3길 10-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '마니주펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 서해안로 248', 33.5089545, 126.4722988, '관광지와 가까운 마니주펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9574d60b-5eff-4887-98a3-a4247b28c257.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '마니주펜션' AND address = '제주특별자치도 제주시 서해안로 248');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '몽돌바당', 'PENSION', 'NORTH', '제주특별자치도 제주시 내도3길 10', 33.4946440, 126.4396000, '은은한 나무향을 느낄 수 있는 목조 팬션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/48914531-2a2f-4871-9161-dab6f5ea1173.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '몽돌바당' AND address = '제주특별자치도 제주시 내도3길 10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '미래하우스', 'PENSION', 'NORTH', '제주특별자치도 제주시 서해안로 224-10', 33.5081334, 126.4711466, '일출과 월출을 함께 볼 수 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/220ce31d-e1dd-4636-81de-82490b1a3ab1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '미래하우스' AND address = '제주특별자치도 제주시 서해안로 224-10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바다드림펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 어영길 18-9', 33.5181481, 126.4949383, '제주공항 근처 바다가 보이는 바다드림 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8e8620bd-eb8b-443a-9633-61b940d81750.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바다드림펜션' AND address = '제주특별자치도 제주시 어영길 18-9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바다추억', 'PENSION', 'NORTH', '제주특별자치도 제주시 용해로 27', 33.5147440, 126.5029450, '다양함과 편리함으로 무장한 바다 추억 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/288b9661-65e9-40a5-8cef-9ef43c1cb6c7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바다추억' AND address = '제주특별자치도 제주시 용해로 27');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바다향기펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 서흘길 24', 33.5275634, 126.5889207, '검은모래해수욕장 앞에 위치해 있는 조용한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9dd3b46d-7d7c-4546-a38d-c283d9c14270.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바다향기펜션' AND address = '제주특별자치도 제주시 서흘길 24');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '별마로펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 항골남길 32-16', 33.4982100, 126.4601700, '아름다운 별이 보이는 야경과 한라산이 한눈에 보이는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/433ea5b7-a6f1-469d-96d4-2507e25ce2a5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '별마로펜션' AND address = '제주특별자치도 제주시 항골남길 32-16');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '부르네스테이', 'PENSION', 'NORTH', '제주특별자치도 제주시 서해안로 376', 33.5121500, 126.4844000, '브루네스테이는 서해안로에 위치한 펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/efbe2bbe-5534-41d8-a979-4d531460b84a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '부르네스테이' AND address = '제주특별자치도 제주시 서해안로 376');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '비아제주Stay', 'PENSION', 'NORTH', '제주특별자치도 제주시 삼무로9길 11', 33.4926870, 126.4949400, '비아 cafe&stay는 공항에서 버스로 15분, 차로 10~15분 거리에 위치 해 있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/780351c2-8aef-4535-b213-e3b6434c443e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '비아제주Stay' AND address = '제주특별자치도 제주시 삼무로9길 11');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '비치조아', 'PENSION', 'NORTH', '제주특별자치도 제주시 서흘길 7', 33.5268068, 126.5876413, '제주 시내에 위치한 바다가 보이는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/49ae88ef-8dce-4ae8-a556-bfe6f77f9d2b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '비치조아' AND address = '제주특별자치도 제주시 서흘길 7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '빌림', 'PENSION', 'NORTH', '제주특별자치도 제주시 오래물길 23', 33.5036700, 126.4677700, '오늘 하루 행복을 빌리는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2ebb0de1-1792-4561-b897-d611842c73df.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '빌림' AND address = '제주특별자치도 제주시 오래물길 23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '쁘띠제주빅터', 'PENSION', 'NORTH', '제주특별자치도 제주시 서해안로 338', 33.5099678, 126.4813246, '쁘띠제주빅터펜션은 도두 해안도로에서 신상 객실과 오션뷰 바베큐를 즐길 수 있는 펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8d681f99-018a-4b16-a781-8065642e16eb.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '쁘띠제주빅터' AND address = '제주특별자치도 제주시 서해안로 338');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '사랑터울펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 어영길 25', 33.5186026, 126.4959110, '레포츠공원근처 / 용두암해안도로변에 위치해 있음', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/96d4228f-a6a5-4759-aa87-7c7339a6577d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '사랑터울펜션' AND address = '제주특별자치도 제주시 어영길 25');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '섬타임즈제주', 'PENSION', 'NORTH', '제주특별자치도 제주시 연대마을길 66-1', 33.4932940, 126.4238600, '창밖에 펼쳐지는 원담의 풍경이 아름다운 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0e1c14ac-b332-4888-ad2a-a88b6361169c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '섬타임즈제주' AND address = '제주특별자치도 제주시 연대마을길 66-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스토리하우스펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 도리중길 37', 33.5019633, 126.4632175, '전객실 바다전망으로 제주바다가 한눈에 보이는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2a773c57-4125-42c9-b9ae-effe332c2d14.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스토리하우스펜션' AND address = '제주특별자치도 제주시 도리중길 37');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '실크로드', 'PENSION', 'NORTH', '제주특별자치도 제주시 용해로 21-4', 33.5147723, 126.5032299, '제주시내 용담해안도로에 위치한 바다가 보이는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8e9418b5-1722-413c-a684-b07f7ff75de7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '실크로드' AND address = '제주특별자치도 제주시 용해로 21-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '썬베이펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 서흘길 14', 33.5269440, 126.5883635, '삼양검은모래 해수욕장 앞에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0c6d111c-8010-4436-993b-ef016f454bed.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '썬베이펜션' AND address = '제주특별자치도 제주시 서흘길 14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아리스토캣펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 서해안로 518', 33.5198300, 126.4944700, '일몰과 야경이 인상적인 해안도로의 휴식처', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ecfc0669-3c2b-4094-93a4-33960049e39e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아리스토캣펜션' AND address = '제주특별자치도 제주시 서해안로 518');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아림민박', 'PENSION', 'NORTH', '제주특별자치도 제주시 서해안로 456-8', 33.5179022, 126.4891045, '제주시내 용담해안도로에 위치한 바다가 보이는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1a76af76-071c-4d87-bec9-e95dd4619640.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아림민박' AND address = '제주특별자치도 제주시 서해안로 456-8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '예다움휴양펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 백포북길 25', 33.5029118, 126.4589403, '예쁘고 정다움이 깃든 팬션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a57df206-7b88-49d1-998c-ae9d7cfdeb72.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '예다움휴양펜션' AND address = '제주특별자치도 제주시 백포북길 25');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '용두암빌리지', 'PENSION', 'NORTH', '제주특별자치도 제주시 용마서1길 27-5', 33.5161990, 126.5051476, '제주공항 5분거리 용담해안도로에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/faf965ee-1e95-4d6d-bda7-7503cbb3dcbe.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '용두암빌리지' AND address = '제주특별자치도 제주시 용마서1길 27-5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '이디완', 'PENSION', 'NORTH', '제주특별자치도 제주시 백포서3길 1', 33.5016805, 126.4574350, '제주 전통 독채 돌담집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202301/06/a797bc64-4eda-4198-818b-ed18907b48b0.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '이디완' AND address = '제주특별자치도 제주시 백포서3길 1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '이어도펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 도두항서5길 3', 33.5045692, 126.4640453, '공항에서 10분거리에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6b7de1c7-adfc-420c-8693-f66316700e41.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '이어도펜션' AND address = '제주특별자치도 제주시 도두항서5길 3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '절물길펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 명림로 10-1', 33.4914049, 126.5949431, '텃밭과 마당이 있는 전원주택형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3f558224-746b-48c6-abb7-f3aea28f48f6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '절물길펜션' AND address = '제주특별자치도 제주시 명림로 10-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 해피펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 월대1길 4', 33.4941100, 126.4337800, '해피펜션은 올레길 17코스 해안도로를 따라 위치해있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/eef21172-a13b-49ba-8645-617303a1c0fd.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 해피펜션' AND address = '제주특별자치도 제주시 월대1길 4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주쉼표', 'PENSION', 'NORTH', '제주특별자치도 제주시 서해안로 504', 33.5194453, 126.4930010, '바쁜 세상 걸음을 멈추고 따뜻한 햇살, 포근한 휴식을 느낄 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fdfde412-e918-4e1c-a643-1565c65836c6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주쉼표' AND address = '제주특별자치도 제주시 서해안로 504');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주오다', 'PENSION', 'NORTH', '제주특별자치도 제주시 서해안로 452', 33.5177400, 126.4878545, '바다를 가득 채우는 객실창이 있는 레지던스 호텔형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/15e1ac67-6df5-4fd6-b3b1-88a66631a2dd.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주오다' AND address = '제주특별자치도 제주시 서해안로 452');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '테마하우스', 'PENSION', 'NORTH', '제주특별자치도 제주시 백포북길 24', 33.5024275, 126.4586378, '파도가 웃음으로 반겨주는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f40035ac-dfd8-4ac6-8ec1-4c95030f1d2b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '테마하우스' AND address = '제주특별자치도 제주시 백포북길 24');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '펜션해다미', 'PENSION', 'NORTH', '제주특별자치도 제주시 서해안로 502', 33.5194025, 126.4927413, '용담해안도로에 위치해있는 콘도형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/85a7a26a-451a-4b9d-ad36-81258d3977ce.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '펜션해다미' AND address = '제주특별자치도 제주시 서해안로 502');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '푸른달', 'PENSION', 'NORTH', '제주특별자치도 제주시 진북길 13', 33.5244166, 126.5656417, '화북 바닷가 인근 감성적인 독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202511/20/d4fb2a97-7166-4bf2-89b6-547d4d7cafb1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '푸른달' AND address = '제주특별자치도 제주시 진북길 13');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '플로라펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 도리로 78-2', 33.4994903, 126.4602395, '제주공항에서 10분거리에 위치한 플로라펜션은 고급 건축물에만 사용하는 자재를 사용한 펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1d0b585d-b8fd-4fee-9a7c-d7f1458bdb7c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '플로라펜션' AND address = '제주특별자치도 제주시 도리로 78-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하늘타리펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 연대마을길 59', 33.4938535, 126.4245732, '전객실 바닷가 전망이 가능한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f509b8d8-2728-48b4-b5b9-67d3422d02a3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하늘타리펜션' AND address = '제주특별자치도 제주시 연대마을길 59');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해변의집', 'PENSION', 'NORTH', '제주특별자치도 제주시 서해안로 224-1', 33.5082890, 126.4708210, '용두암과 이호해수욕장사이 해안도로변에 위치한 해변의집 펜션은 주변에 제주해수피아가 있으며 유명먹거리식당도 많이 있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/cfaed659-9c87-42de-a7be-9efdb4fde873.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해변의집' AND address = '제주특별자치도 제주시 서해안로 224-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해안풍경펜션', 'PENSION', 'NORTH', '제주특별자치도 제주시 신사수2길 6', 33.5087966, 126.4750685, '공항과 이호테우해변 중간지점에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/cd3ce85e-6875-4b23-9a16-4d5a36ca1040.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해안풍경펜션' AND address = '제주특별자치도 제주시 신사수2길 6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '그랜드 하얏트 제주', 'RESORT', 'NORTH', '제주특별자치도 제주시 노연로 12', 33.4852786, 126.4814609, '공항에서 10분거리의 제주 드림타워 복합리조트에 위치한 아시아 태평양 최대 규모의 그랜드 하얏트 호텔로 1600개의 객실과 스위트, 14개의 레스토랑과 바, 스파, 야외풀데크 보유', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202104/22/19914c5f-4476-4b05-836c-61a6330975fe.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '그랜드 하얏트 제주' AND address = '제주특별자치도 제주시 노연로 12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '그림리조트', 'RESORT', 'NORTH', '제주특별자치도 제주시 서해안로 620-1 (용담삼동)', 33.5163040, 126.5036099, '푸른바다를 정원으로 들인 아름다운 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/cb52e1eb-78b6-4628-8011-6cadeff5ddcf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '그림리조트' AND address = '제주특별자치도 제주시 서해안로 620-1 (용담삼동)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라헨느리조트', 'RESORT', 'NORTH', '제주특별자치도 제주시 명림로 384', 33.4522614, 126.6152771, '한라산과 바다를 조망으로 골프와 휴양을 동시에 즐길 수 있는 골프리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/2194a25b-c5a4-4b74-a2bb-5d2bbb660014.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라헨느리조트' AND address = '제주특별자치도 제주시 명림로 384');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '비스타리조트', 'RESORT', 'NORTH', '제주특별자치도 제주시 서해안로 74', 33.5030980, 126.4576200, '통유리로 바라보는 제주 바다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5342b93d-dda4-4556-a1e0-5a7cfdffb994.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '비스타리조트' AND address = '제주특별자치도 제주시 서해안로 74');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '썬비치리조트', 'RESORT', 'NORTH', '제주특별자치도 제주시 서해안로 114', 33.5028321, 126.4615190, '용두암 해안도로에 위치해 있는 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8373f197-9be1-4660-8107-025c0f3e11b4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '썬비치리조트' AND address = '제주특별자치도 제주시 서해안로 114');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아모렉스리조트', 'RESORT', 'NORTH', '제주특별자치도 제주시 서해안로 216', 33.5074916, 126.4702754, '가족형 호텔, 아모렉스 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/89e49c6b-1f64-4075-9870-6d06063f9e43.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아모렉스리조트' AND address = '제주특별자치도 제주시 서해안로 216');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '엠버퓨어힐 호텔&리조트 제주', 'RESORT', 'NORTH', '제주특별자치도 제주시 1100로 2671-30', 33.4264438, 126.4870326, '한라산의 숨겨진 빛나는 보석이라 불리는 이곳은 해발 520m 지대에 위치해 제주 자연을 온전히 담은 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/5d2c70c3-c436-4a8e-ba5f-7b31964517f9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '엠버퓨어힐 호텔&리조트 제주' AND address = '제주특별자치도 제주시 1100로 2671-30');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주나인리조트', 'RESORT', 'NORTH', '제주특별자치도 제주시 해안마을북길 14-5', 33.4650239, 126.4537337, '가족, 모임, 세미나, 워크숍 등 단체 여행객들이 이용하기 좋은 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/eed4a8bd-774f-4d5c-aaaa-80b0eac4bbf0.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주나인리조트' AND address = '제주특별자치도 제주시 해안마을북길 14-5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '캠퍼트리 호텔앤리조트', 'RESORT', 'NORTH', '제주특별자치도 제주시 해안마을서4길 100', 33.4284365, 126.4673892, '해안동 무수천 인근에 위한 단독형 빌라 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a6df218a-b109-475c-b58d-3310ef529f35.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '캠퍼트리 호텔앤리조트' AND address = '제주특별자치도 제주시 해안마을서4길 100');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '한화리조트 제주', 'RESORT', 'NORTH', '제주특별자치도 제주시 명림로 575-107 (회천동, 한화리조트) 한화리조트 제주', 33.4489935, 126.6374120, '‘휴식’ ‘멍패커’ ‘워케이션’ 모두가 가능한 한화리조트 제주', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/3052901a-34c9-4d99-abbf-ec50eaabfcd3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '한화리조트 제주' AND address = '제주특별자치도 제주시 명림로 575-107 (회천동, 한화리조트) 한화리조트 제주');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '돌고래하우스(캠핑장)', 'CAMPING', 'SOUTH', '제주특별자치도 서귀포시 이어도로 826-58', 33.2327420, 126.4995000, '바다가 보이는 2층 독채펜션 각 편의시설 구비', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/50da1ab0-02e9-4774-9d64-1d38c627f5be.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '돌고래하우스(캠핑장)' AND address = '제주특별자치도 서귀포시 이어도로 826-58');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '서귀포호텔 카라반', 'CAMPING', 'SOUTH', '제주특별자치도 서귀포시 상예로 319', 33.2731910, 126.3947296, '서귀포 호텔 내 위치한 자연 속 청정 쉼터.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201807/23/45de17a5-0327-40d2-bab1-002de56cad96.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '서귀포호텔 카라반' AND address = '제주특별자치도 서귀포시 상예로 319');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주그믐', 'CAMPING', 'SOUTH', '제주특별자치도 서귀포시 예래해안로 179-23', 33.2361999, 126.3833462, '제주 예래바다가 보이는 독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/a60d4984-7a35-486a-a066-af656b46ff09.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주그믐' AND address = '제주특별자치도 서귀포시 예래해안로 179-23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '캠파제주', 'CAMPING', 'SOUTH', '제주특별자치도 서귀포시 516로 574-23', 33.2983919, 126.6011084, '캠파제주는 해질녘 노을이 아름다운 곳, 서귀포 자연 속에서 카라반 캠핑과 글램핑을 즐길 수 있는 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202212/09/e2799df7-755d-4aa2-8fb7-4ec30954b0d6.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '캠파제주' AND address = '제주특별자치도 서귀포시 516로 574-23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '팜핑제주', 'CAMPING', 'SOUTH', '제주특별자치도 서귀포시 중산간서로400번길 105', 33.2688268, 126.4730466, '카라반 캠핑과 감귤따기 체험을 같이 할 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/53f683da-6e00-4096-9758-30adb86552fd.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '팜핑제주' AND address = '제주특별자치도 서귀포시 중산간서로400번길 105');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '가름게스트하우스', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 법환하로9번길 10', 33.2365318, 126.5124066, '여행이라는 또 하나의 공동체가 시작하는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5d6b77e1-8ccf-47d5-89bf-03409b14ff15.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '가름게스트하우스' AND address = '제주특별자치도 서귀포시 법환하로9번길 10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '구덕게스트하우스', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 솔동산로22번길 20', 33.2440867, 126.5624219, '하이킹 배낭전문 숙소인 제주하이킹 inn', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/db8bf4e1-bfcc-42fa-8c87-8fdc33c73643.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '구덕게스트하우스' AND address = '제주특별자치도 서귀포시 솔동산로22번길 20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '나무이야기', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 남원읍 남한로21번길 9', 33.2808477, 126.7166153, '올래5코스 시작점에 위치한 편백나무와 삼나무 등 나무로 내부 인테리어가 된 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c757ce54-2232-4b1b-935a-94d0cf3cd40c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '나무이야기' AND address = '제주특별자치도 서귀포시 남원읍 남한로21번길 9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '나무이야기 게스트하우스 남원점', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 남원읍 남한로21번길 9', 33.2808500, 126.7166100, '나무이야기게스트하우스 2호점', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1b26ca1e-8c1d-4105-a63f-20adb65a9ade.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '나무이야기 게스트하우스 남원점' AND address = '제주특별자치도 서귀포시 남원읍 남한로21번길 9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '넙빌레하우스', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로2번길 28', 33.2692220, 126.6487600, '위미리에 위치한 조용하고 전망 좋은 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/56c8078a-5511-4924-9661-e76f0c0a26c6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '넙빌레하우스' AND address = '제주특별자치도 서귀포시 남원읍 태위로2번길 28');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더블루 제주', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 칠십리로 479', 33.2419500, 126.6017800, '서귀포 보목동에 위치한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ba1ee939-e529-4afa-bf62-b7b1ce0d15b2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더블루 제주' AND address = '제주특별자치도 서귀포시 칠십리로 479');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '몬딱쉼터', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 토평로 137', 33.2844099, 126.5796743, '친환경 유기농 체험농장과 함께하는 콘도형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/603f870c-3d6d-4c23-8328-7ef02f0d9dcc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '몬딱쉼터' AND address = '제주특별자치도 서귀포시 토평로 137');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '미도호스텔', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 동문동로 13-1', 33.2499770, 126.5664200, '서귀포 시내에 위치한 마이크로네이션 호스텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/76c7fc35-f595-41b4-9965-70651751abb0.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '미도호스텔' AND address = '제주특별자치도 서귀포시 동문동로 13-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '민중각 게스트하우스', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 서문로28번길 4', 33.2506750, 126.5587815, '저렴한 비용의 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a91706f0-a9eb-419a-974b-d568c196e9df.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '민중각 게스트하우스' AND address = '제주특별자치도 서귀포시 서문로28번길 4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '백패커스홈', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 중정로 24-7', 33.2470391, 126.5590304, '친환경 편백나무로 만든 서귀포에 위치한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/19d8dd4e-5cee-4088-a84f-f36fe454fe0f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '백패커스홈' AND address = '제주특별자치도 서귀포시 중정로 24-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '볼몽지엥하우스', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 보목포로1번길 10', 33.2419780, 126.6059950, '따뜻한 추억을 선물할 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/42f6dc90-a79d-4e2c-a8ed-ddf8742febc0.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '볼몽지엥하우스' AND address = '제주특별자치도 서귀포시 보목포로1번길 10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스쿠버스토리', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 태평로536번길 17', 33.2663197, 126.5671727, '스쿠버다이빙 체험 프로그램을 운영하는 게스트하우스이다. 다이빙 자격증 취득 가능하다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201907/04/92b5893c-4740-4def-98ac-e8918e653210.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스쿠버스토리' AND address = '제주특별자치도 서귀포시 태평로536번길 17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오빌하우스', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 남원읍 공천포로11번길 15', 33.2999950, 126.6297939, '올레 6코스 시작점에 위치하여 바다와 한라산 전경을 모두 감상할 수 있는 펜션형 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/330c7386-da98-4578-979b-2837eff81ece.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오빌하우스' AND address = '제주특별자치도 서귀포시 남원읍 공천포로11번길 15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '올레스테이', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 중정로 22', 33.2474043, 126.5587322, '제주 올레 여행자센터는 제주 여행자들의 베이스캠프입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b6853756-ef27-409a-bfbf-e99e89170fc3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '올레스테이' AND address = '제주특별자치도 서귀포시 중정로 22');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '외돌개나라', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 남성로 122', 33.2639577, 126.5507293, '모든 객실에서 바다 전망이 보이며, 제주올레 7코스 시작점에 위치한 아름다운 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ef75c42c-c37e-4363-af41-76f01f4798f1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '외돌개나라' AND address = '제주특별자치도 서귀포시 남성로 122');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '용산제주유스호스텔', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 이어도로 297', 33.2438633, 126.4513018, '취사 기능이 갖춰진 리조트 타입의 숙박시설이다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202306/27/a732efdf-58c9-4065-a01d-355d3c111d18.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '용산제주유스호스텔' AND address = '제주특별자치도 서귀포시 이어도로 297');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주와일드', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 논짓물로 28', 33.2387885, 126.3873810, '업무공간과 휴식 공간이 함께 있는 제주 워케이션 센터', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/9b4b3603-e41b-41d7-8d18-656348cbc1f6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주와일드' AND address = '제주특별자치도 서귀포시 논짓물로 28');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하마다', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 남원읍 일주동로 6863', 33.2922889, 126.7393618, '서귀포 한적한 바닷가 마을에 위치한 여성 전용 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201810/15/a6c90765-b394-49a6-ba24-dd42b4cd1a19.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하마다' AND address = '제주특별자치도 서귀포시 남원읍 일주동로 6863');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하얀도화지', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 하예로44번길 1', 33.2452784, 126.3892422, '서귀포시 중문권 전객실 바다전망 가능하며 화초가 많은 민박집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4fb74a8c-55de-4b53-ae52-68586f1d8b89.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하얀도화지' AND address = '제주특별자치도 서귀포시 하예로44번길 1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '흰고래게스트하우스', 'GUESTHOUSE', 'SOUTH', '제주특별자치도 서귀포시 이어도로1066번길 26', 33.2440570, 126.5237050, '서귀포 시내에 위치한 아기자기한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/bac91e01-ef59-40d3-b8b8-73a27808d23d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '흰고래게스트하우스' AND address = '제주특별자치도 서귀포시 이어도로1066번길 26');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '골드원 호텔 & 스위트', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 이어도로 1032', 33.2441806, 126.5224316, '서귀포혁신도시 바닷가에 위치한 궁전같은 귀족적인 정취를 가진 특급호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/372dd4bd-9e5b-4269-be2a-d5f67ec4cb77.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '골드원 호텔 & 스위트' AND address = '제주특별자치도 서귀포시 이어도로 1032');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '그랜드메르호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 논짓물로 45', 33.2395829, 126.3863727, '제주국제컨벤션센터까지 차량 9분! 중문관광단지와 인접한 편안하게 쉬기 좋은 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201812/06/19394054-1fc9-4841-969f-6d5637af7de6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '그랜드메르호텔' AND address = '제주특별자치도 서귀포시 논짓물로 45');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더 시에나 프리모', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 용흥로66번길 158-7', 33.2615620, 126.4944700, '이탈리아 토스카나 지방을 모티브로 한 자연 휴양형 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9bc0b340-9348-4829-844d-da96128b214a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더 시에나 프리모' AND address = '제주특별자치도 서귀포시 용흥로66번길 158-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '데이즈호텔 제주서귀포오션', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 동홍로 7', 33.2492258, 126.5675720, '서귀포 남단의 월드와이드호텔로 280여개의 객실과 연회장을 보유하고 있으며, 이중섭거리, 올레시장 등을 도보로 즐길 수 있는 합리적인 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/dd13df73-0949-4449-8e07-e4c761e53025.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '데이즈호텔 제주서귀포오션' AND address = '제주특별자치도 서귀포시 동홍로 7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '디아일랜드블루호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 태평로431번길 3 (서귀동, 디 아일랜드 블루 오피스텔) 디아일랜드블루호텔', 33.2458510, 126.5658459, '디아일랜드블루와 함께 수많은 볼거리&놀거리&먹을거리로 제주를 만끽하세요!', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202409/20/43814a26-6cc3-4ae9-ace9-7b08050fd1fd.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '디아일랜드블루호텔' AND address = '제주특별자치도 서귀포시 태평로431번길 3 (서귀동, 디 아일랜드 블루 오피스텔) 디아일랜드블루호텔');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '런호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 태평로431번길 20', 33.2471600, 126.5662600, '수려한 경관을 배경으로 서귀포 도심에 자리 잡은 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f5fd9695-d654-4f26-9c99-fffbe2c733c1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '런호텔' AND address = '제주특별자치도 서귀포시 태평로431번길 20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '롯데호텔 제주', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 중문관광로72번길 35', 33.2484577, 126.4105487, '사계절 온수풀과 이국적인 풍경이 매혹적인 리조트형 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202301/04/34dcc258-615b-485e-942a-8f2cf49e454e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '롯데호텔 제주' AND address = '제주특별자치도 서귀포시 중문관광로72번길 35');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '모멘토하우스', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 태평로439번길 21', 33.2465385, 126.5650235, '이중섭거리에 위치하고 있는 모멘토 호텔은 트리플스타일의 큰 방과 두 개의 침대, 아름다운 서귀포 바다풍경이 보이는 발코니가 있는 호텔입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7bc42dfb-a5fd-474e-808e-31271dc18890.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '모멘토하우스' AND address = '제주특별자치도 서귀포시 태평로439번길 21');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바다해호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 천지연로 3-1', 33.2414130, 126.5657700, '수익금의 일부를 기부하는 제주착한호텔 소속 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/10396d4b-6f53-473c-adb3-394ecd5ad433.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바다해호텔' AND address = '제주특별자치도 서귀포시 천지연로 3-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '밸류호텔 서귀포(VALUE HOTEL Seogwipo JS)', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 김정문화로 51', 33.2514209, 126.5103816, '서귀포시에 위치한 깔끔한 객실과 편리한 교통을 자랑하는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/bf6d4980-2093-4adf-871c-1cbdec14f022.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '밸류호텔 서귀포(VALUE HOTEL Seogwipo JS)' AND address = '제주특별자치도 서귀포시 김정문화로 51');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '베니키아 중문호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 천제연로 166', 33.2521468, 126.4220672, '서귀포시 중문동에 위치해 좋은 접근성을 자랑하는 모던한 감각의 호텔입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d9710d59-c927-4ce3-a40c-37ded90b9108.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '베니키아 중문호텔' AND address = '제주특별자치도 서귀포시 천제연로 166');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '부영CC관광호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 남원읍 남조로 960', 33.3508829, 126.6980129, '쾌적하고 편안한 삶과 휴식을 얻을 수 있는 부영관광호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a510f17b-d692-4c81-901f-857f0fdfd9b9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '부영CC관광호텔' AND address = '제주특별자치도 서귀포시 남원읍 남조로 960');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '블룸호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 중문관광로72번길 53', 33.2814107, 126.4111118, '중문단지 중심에 위치한 휴식과 비즈니스가 함께하는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/9e440916-f52c-421e-bafe-271f4051396c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '블룸호텔' AND address = '제주특별자치도 서귀포시 중문관광로72번길 53');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '블룸호텔 제주', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 중문관광로72번길 53', 33.2497631, 126.4082711, '중문단지 내에 위치한 경제적이면서 깔끔한 3성급 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202503/29/a95ae9e9-86d7-4fc7-af26-9286a574af5d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '블룸호텔 제주' AND address = '제주특별자치도 서귀포시 중문관광로72번길 53');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '비스타케이호텔월드컵', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 김정문화로41번길 10-6', 33.2519369, 126.5098771, '교통이 편리한 가성비 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/d0582b61-bc7e-4c7b-9dbd-dda5a4e2c3eb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '비스타케이호텔월드컵' AND address = '제주특별자치도 서귀포시 김정문화로41번길 10-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '서귀포칼호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 칠십리로 242', 33.2465300, 126.5817400, '칠십리 해안가에 위치한 자연 친화형 패밀리 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e0328a92-5e73-44dd-a575-a4b949c8e0cf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '서귀포칼호텔' AND address = '제주특별자치도 서귀포시 칠십리로 242');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '서귀포힐즈호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 신서로48번길 50', 33.2528563, 126.5102317, '새롭게 만나는 서귀포 힐즈호텔에서 안락함과 여유를 선사하겠습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202208/04/a547f3c3-7e1e-4753-b154-2d9e55092f59.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '서귀포힐즈호텔' AND address = '제주특별자치도 서귀포시 신서로48번길 50');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스위트호텔 제주', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 중문관광로72번길 67', 33.2492910, 126.4082711, '중문관광단지 내에 위치한 휴양형 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fa9d9c0f-e2ef-4ea3-8cf9-35500137c334.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스위트호텔 제주' AND address = '제주특별자치도 서귀포시 중문관광로72번길 67');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '신신호텔 제주오션', 'HOTEL', 'SOUTH', '제주 서귀포시 중앙로 14 신신호텔 제주오션', 33.2459813, 126.5623574, '신신호텔 제주오션은 아름다운 자연의 청정도 제주를 배경으로 한 최상의 휴양지이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202402/07/10882c47-ff22-4355-af1d-602de950ba8f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '신신호텔 제주오션' AND address = '제주 서귀포시 중앙로 14 신신호텔 제주오션');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '신신호텔 제주월드컵', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 서호중로 55 (서호동) 신신호텔 제주월드컵', 33.2545600, 126.5192299, '비즈니스와 휴양을 동시에 충족시키는 호텔로 젊은 층에게 특히 더 사랑 받는 트렌디한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202402/07/15b2f54f-b2bc-4748-a993-5b0641ddd583.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '신신호텔 제주월드컵' AND address = '제주특별자치도 서귀포시 서호중로 55 (서호동) 신신호텔 제주월드컵');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '신신호텔 천지연', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 서문로 42-1 (서귀동) 신신호텔 천지연', 33.2495146, 126.5576159, '제주 문화를 느낄 수 있는 서귀포에 위치한 최고의 스포츠 전지 훈련의 요람', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202402/07/9d144e85-3eeb-4576-8b1b-89ac774975eb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '신신호텔 천지연' AND address = '제주특별자치도 서귀포시 서문로 42-1 (서귀동) 신신호텔 천지연');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '썬라이즈호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 중문관광로 310', 33.2474985, 126.4305878, '중문관광단지에 위치한 강렬한 태양을 담은 포근한 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/cda970b6-b25d-47ba-86bb-9104c7ede252.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '썬라이즈호텔' AND address = '제주특별자치도 서귀포시 중문관광로 310');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '씨앤비 호텔 코지앤부띠끄', 'HOTEL', 'SOUTH', '제주툭별자치도 서귀포시 중문관광로 288', 33.2458300, 126.4295040, '중문에 위치한 음악공연장이 있는 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/aee46205-a572-458a-95c1-79ba4de000ff.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '씨앤비 호텔 코지앤부띠끄' AND address = '제주툭별자치도 서귀포시 중문관광로 288');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '엠스테이호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 태평로353번길 14', 33.2470170, 126.5606400, '서귀포시에 위치한 국내 토종 호텔 브랜드', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/06464cf5-2d02-4819-ad5a-21b8f7f94efc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '엠스테이호텔' AND address = '제주특별자치도 서귀포시 태평로353번길 14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '이스턴호텔제주', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 서호중로 65', 33.2784400, 126.5169900, '비즈니스와 휴양을 동시에 즐길 수 있는 서귀포시에 위치한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/338c2435-8daf-494f-ae40-0b7e946db3ce.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '이스턴호텔제주' AND address = '제주특별자치도 서귀포시 서호중로 65');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '일레인호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 일주서로 660', 33.2546584, 126.4392868, '중문관광단지 근처의 경치가 아름다운 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/6a1176f3-d70e-441b-88dd-8f9d209527ed.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '일레인호텔' AND address = '제주특별자치도 서귀포시 일주서로 660');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 빠레브호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 김정문화로 15', 33.2524864, 126.5066594, '호텔 빠레브는 순수 제주 토착자본으로 건립 되었으며, 서귀포를 대표하는 특1급 호텔로서 자부심과 책임감을 가지고 제주의 관광 서비스 산업을 선도하고자 함.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202002/04/ab2fc963-4c2d-405e-8787-6a6b04d54383.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 빠레브호텔' AND address = '제주특별자치도 서귀포시 김정문화로 15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주나인부띠끄호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 솔동산로10번길 23', 33.2426258, 126.5629447, '서귀포내 관광지와의 접근성이 좋은 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/992bbb65-4695-4edf-8e02-bdd8dd594029.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주나인부띠끄호텔' AND address = '제주특별자치도 서귀포시 솔동산로10번길 23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주신라호텔', 'HOTEL', 'SOUTH', '제주도 서귀포시 중문관광로 72번길 75', 33.2475600, 126.4081200, '파스텔 톤의 인테리어, 세계 유명 작가들의 예술작품과 아열대 정원이 어우러진 리조트 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202502/24/2e99fe95-a579-432b-8194-5d85b86ec4e6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주신라호텔' AND address = '제주도 서귀포시 중문관광로 72번길 75');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '체이슨호텔 더 스마일', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 신서로48번길 50', 33.2528570, 126.5102300, '2017년 오픈하여 서귀포에 자리잡은 디자인 호텔 브랜드 ''체이슨호텔 더 스마일''', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c927b0c8-d86a-47b1-a076-0bc20ef03a33.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '체이슨호텔 더 스마일' AND address = '제주특별자치도 서귀포시 신서로48번길 50');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '파르나스 호텔 제주', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 중문관광로72번길 100 (색달동)', 33.2814107, 126.4111118, '럭셔리한 휴식과 최고급 서비스를 경험할 수 있는 제주도의 명품 호텔', '', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '파르나스 호텔 제주' AND address = '제주특별자치도 서귀포시 중문관광로72번길 100 (색달동)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '파밀리아호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 대포동서로 63-20', 33.2408800, 126.4382500, '가족과 함께 머물고 싶은 곳, 파밀리아', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a8c137eb-db07-4346-b329-6232102d8cbf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '파밀리아호텔' AND address = '제주특별자치도 서귀포시 대포동서로 63-20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '파인힐호텔', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 부두로 44', 33.2413280, 126.5644681, '푸른바다와 천혜의 자연이 살아 숨쉬는 아름다운섬 PINE HILL호텔에 찾아주셔서 감사합니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4d006f2e-2349-4d6e-a865-278aef8e0f16.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '파인힐호텔' AND address = '제주특별자치도 서귀포시 부두로 44');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '파크 선샤인 제주', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 남성중로 135', 33.2435819, 126.5535596, '서귀포 접근성이 좋은 특 2급 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f448107f-0a2b-483c-9f3f-782d2a948516.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '파크 선샤인 제주' AND address = '제주특별자치도 서귀포시 남성중로 135');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '풍경호텔(풍경관광호텔)', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 남성중로 147', 33.2639577, 126.5507293, '올레 6코스에 위치하고 있으며 주변 풍경이 아름다운 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c6c418d6-1f8d-4229-af73-c41cc12fbd45.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '풍경호텔(풍경관광호텔)' AND address = '제주특별자치도 서귀포시 남성중로 147');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '프롬에이치 엘렌호텔 제주 서귀포점', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 이어도로 152 (대포동)', 33.2398003, 126.4385613, '엘렌 호텔은 제주 바다가 한폭의 그림처럼 펼쳐져 있는 서귀포시 중문 대포포구에 위치하고 있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202504/13/4136b8c9-9f09-4862-af9b-ca9103ffa45a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '프롬에이치 엘렌호텔 제주 서귀포점' AND address = '제주특별자치도 서귀포시 이어도로 152 (대포동)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '한옥호텔 한라궁', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 정방연로 6', 33.2484300, 126.5783500, '독특한 한옥 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/40b8e809-8288-45cb-aec3-f765380a7dce.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '한옥호텔 한라궁' AND address = '제주특별자치도 서귀포시 정방연로 6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '헤이 서귀포', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 태평로 363', 33.2464235, 126.5602247, '여가를 위한 라이프스타일 브랜드 heyy가 선보이는 두번째 호텔 heyy', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/d245eb13-d299-4fc1-a886-e2ed4e79c28c.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '헤이 서귀포' AND address = '제주특별자치도 서귀포시 태평로 363');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔 더본 제주', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 색달로 18', 33.2594337, 126.4063059, '자연과 조화를 이루며 다양한 다이닝 옵션과 함께 여행의 피로를 잊게 해주는 특별한 공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a91efee2-68a6-47c6-af44-1e9fa1d4c375.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔 더본 제주' AND address = '제주특별자치도 서귀포시 색달로 18');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔 라벤다제주', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 중정로 1', 33.2476960, 126.5566900, '호텔 수익의 일부를 기부하는 제주착한호텔 소속 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0fdf8cdb-50e8-404c-aaef-814120b54fc5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔 라벤다제주' AND address = '제주특별자치도 서귀포시 중정로 1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔 서귀피안 서귀포본점', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 보목로64번길 178 호텔서귀피안', 33.2444320, 126.5905456, '보목동에 위치한 넓은 창으로 보이는 파란 바다와 야자수가 인상적인 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201907/15/4bbb5d1e-a900-45f2-8e7e-44a288595acc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔 서귀피안 서귀포본점' AND address = '제주특별자치도 서귀포시 보목로64번길 178 호텔서귀피안');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔 징크', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 중정로 11', 33.2477769, 126.5575908, '제주올레여행자센터 맞은 편인 서귀포 구시가지의 중심에 있어 관광객, 비즈니스 기업인들을 위한 최적의 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202209/19/6c0b0320-68d8-4664-a8b2-eb0e602f2a89.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔 징크' AND address = '제주특별자치도 서귀포시 중정로 11');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔 화인 제주', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 칠십리로 87', 33.2405660, 126.5650900, '서귀포시 5성급 호텔로서 서비스와 시설면에서 최고급을 지향', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b3fdcbba-5293-41bd-853b-6e5b328a2fc2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔 화인 제주' AND address = '제주특별자치도 서귀포시 칠십리로 87');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔브릿지 서귀포', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 태평로 436', 33.2455925, 126.5666523, '우수한 가성비로 즐기는 아름다운 바다 경치', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202603/09/c0e1d104-10e9-4594-ac63-2672270ad4a7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔브릿지 서귀포' AND address = '제주특별자치도 서귀포시 태평로 436');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔캘리포니아', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 김정문화로27번길 14-4', 33.2528754, 126.5084426, '맛있는 조식과 쾌적하고 깨끗한 룸이 항상 마련되어있는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/6bfe6503-1a8b-4e5c-a677-c8a9538b955a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔캘리포니아' AND address = '제주특별자치도 서귀포시 김정문화로27번길 14-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '히든 클리프 호텔 & 네이쳐', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 예래해안로 542', 33.2543640, 126.4031500, '원시림 속에 위치하는 국내 최장 인피니티풀이 있는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/c3a0d6b3-33cb-459a-b060-5c284d22483e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '히든 클리프 호텔 & 네이쳐' AND address = '제주특별자치도 서귀포시 예래해안로 542');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'BK호텔제주', 'HOTEL', 'SOUTH', '제주특별자치도 서귀포시 칠십리로91번길 12', 33.2411080, 126.5643000, '서귀포시 서귀동에 위치한 신축 부티크 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/bb770459-25cd-4a59-8401-3b3775ab259c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'BK호텔제주' AND address = '제주특별자치도 서귀포시 칠십리로91번길 12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'WE호텔 제주', 'HOTEL', 'SOUTH', '제주도 서귀포시 1100로 453-95(회수동, 위호텔)', 33.2862170, 126.4442700, 'WE호텔의 5성 서비스와 함께 WE병원의 헬스케어 서비스를 제공하며 모든 투숙객에게 웰니스 프로그램을 무료로 제공', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202305/22/b6143dc0-5dc7-43b7-b732-8eaf2c8e8bcf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'WE호텔 제주' AND address = '제주도 서귀포시 1100로 453-95(회수동, 위호텔)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더그랜드섬오름', 'OTHER', 'SOUTH', '제주특별자치도 서귀포시 막숙포로 118', 33.2327311, 126.5099541, '가장 환상적인 제주 바다와 가장 아름다운 올레길을 누리는 곳 더 그랜드 섬오름', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/4391b939-4a9c-46e0-bc20-221ddbae7638.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더그랜드섬오름' AND address = '제주특별자치도 서귀포시 막숙포로 118');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '먼슬리브 인 제주', 'OTHER', 'SOUTH', '제주특별자치도 서귀포시 색달로72번길 20 (색달동)', 33.2592521, 126.4103574, '제주 애견 동반 숙소 먼슬리브 인 제주입니다. 저희는 서귀포 중문에 위치한 제주 애견 동반 한달살이 숙소를 운영하고 있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202501/31/087f0dfe-f7f0-4835-b0e0-9e9e41d3278a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '먼슬리브 인 제주' AND address = '제주특별자치도 서귀포시 색달로72번길 20 (색달동)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '부영주택 제주부영청소년수련원', 'OTHER', 'SOUTH', '제주특별자치도 서귀포시 중문상로17번길 52-17', 33.2377549, 126.4250671, '청소년들을 위한 단체 숙박 및 수련활동 프로그램 진행을 위한 청소년수련시설', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201904/18/95f19a6d-11dc-427f-918f-0a94f2613db7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '부영주택 제주부영청소년수련원' AND address = '제주특별자치도 서귀포시 중문상로17번길 52-17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '케니스테이 제주 서귀포', 'OTHER', 'SOUTH', '제주특별자치도 서귀포시 동문로 42', 33.2502118, 126.5651212, '가성비 좋은 깨끗하고 편안한 휴식처, 케니스토리 인 서귀포', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201910/18/2f2934d1-5c13-4514-8f8b-315ed157bd08.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '케니스테이 제주 서귀포' AND address = '제주특별자치도 서귀포시 동문로 42');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '포트에비뉴', 'OTHER', 'SOUTH', '제주특별자치도 서귀포시 대포동서로 63-19', 33.2407904, 126.4385392, '포트에비뉴 안내 제주의 아름다운 자연경관과 지리적 조건을 갖춘 국제적 휴양지 중문관광단지에 위치한 포트에비뉴는 주요 관광지와 인접해 있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202603/17/bf940d10-cff6-439f-bb4a-de3df7f6eb0e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '포트에비뉴' AND address = '제주특별자치도 서귀포시 대포동서로 63-19');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '갯바위펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 하예하동로 37', 33.2335467, 126.3727955, '중문관광단지에서 차로 10분거리인 난드르에 위치해 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3cb4e2d9-8a42-499c-be1d-6139f3c9a2bd.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '갯바위펜션' AND address = '제주특별자치도 서귀포시 하예하동로 37');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '굿데이 펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남성로115번길 2', 33.2539385, 126.5595922, '다양한 형태의 객실과 전객실 바다를 전망할 수 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4986cd40-4984-43ba-9745-a537142b0e5d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '굿데이 펜션' AND address = '제주특별자치도 서귀포시 남성로115번길 2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '그린사이드', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 천제연로 51', 33.2568179, 126.4115206, '중문관광단지 입구에 위치한 정원이 아름다운 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/11dc9905-5837-4fb9-ac7a-3182310511f8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '그린사이드' AND address = '제주특별자치도 서귀포시 천제연로 51');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '그림같은집', 'PENSION', 'SOUTH', '제주도 서귀포시 법환상로22번길 71', 33.2472100, 126.5175100, '서귀포에 위치한 단정한 정원이 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/413fedcd-063e-4d0b-ae79-fc964070a8b2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '그림같은집' AND address = '제주도 서귀포시 법환상로22번길 71');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '꿈꾸는 노마드', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 선반로 54', 33.2615636, 126.4944700, '노마드적 여행자들을 위한 쉼터', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/19/61efeae5-cf0b-418f-91ad-bb97623442f6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '꿈꾸는 노마드' AND address = '제주특별자치도 서귀포시 선반로 54');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '끄라비펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 토평로148번길 28', 33.2844100, 126.5796700, '서귀포 섶섬, 문섬, 지귀도가 한눈에 내려다보이는 바다 전망이 뛰어난 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3c243f9b-6968-4782-9a4d-bceb8da2b46b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '끄라비펜션' AND address = '제주특별자치도 서귀포시 토평로148번길 28');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '남쪽나라빌', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 천제연로 133-1', 33.2541120, 126.4198760, '제주 천제연폭포 앞에 위치한 남쪽나라빌', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/75524068-0a2b-4c0d-9456-926c6786a61e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '남쪽나라빌' AND address = '제주특별자치도 서귀포시 천제연로 133-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '늘해랑펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 분토왓로174번길 34-1', 33.2639577, 126.5507293, '서귀포월드컵경기장과 가까운 전원주택 느낌의 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/05d9b981-0044-4acf-9474-244949b3cf08.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '늘해랑펜션' AND address = '제주특별자치도 서귀포시 분토왓로174번길 34-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '다솜펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 이어도로 139', 33.2404923, 126.4366628, '중문관광단지 옆, 최적의 위치에 자리잡고 있는 가족휴양지 다솜펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/dabad183-a609-425f-9a54-72400850e2ed.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '다솜펜션' AND address = '제주특별자치도 서귀포시 이어도로 139');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더비비스제주', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 이어도로 760 (강정동) 더비비스제주', 33.2355622, 126.4962549, '푸르른 야자수와 발끝에 걸린 수평선 하늘과 맞닿은 에메랄드빛 바다. 프리미엄 오션뷰 숙소에 일상에 지친 여러분을 초대 합니다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202511/06/7fcef79c-85cc-4b75-9745-b6e3cbeaaa9a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더비비스제주' AND address = '제주특별자치도 서귀포시 이어도로 760 (강정동) 더비비스제주');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더아름다운펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 일주서로 632', 33.2538393, 126.4420593, '서귀포의 편안하고 분위기 좋은 콘도형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fcc24f5c-6422-4b83-8ca1-08a6ffe867f8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더아름다운펜션' AND address = '제주특별자치도 서귀포시 일주서로 632');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '돌담한길', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 한신로 55', 33.3352599, 126.6681117, '제주도 서귀포시 남원읍은 제주에서도 날씨가 좋은 곳으로 알려진 마을이다. 봄철에는 제주특산물로 유명한 고사리축제가 열리고, 감귤이 익어가는 풍경이 아름다워 ‘귤림추색’(감귤이 익어가는 가을풍경)이라고 불릴 정도. 제주도민들도 남원읍은 살기 좋은 고장이라 한다고. 돌담한길펜션은 72년 전통가옥을 현대식으로 리모델링해 내 집처럼 편안한 독채형 펜션이다. 대형 숙박시설이 아니라 단 2개의 객실만 운영하고 있는 소박한 제주 살림집스러운데 실내에 들어서면 70년 전 천장 서까래가 그대로 드러나 옛 분위기가 물씬 풍기고, 안방에는 편백나무 침대가 건강한 수면을 도와주고 창밖으로는 감귤밭이 한눈에 들어온다. 겉은 옛집이지만 화장실, 주방, 거실 등 모든 이용시설은 이용하기 편리한 현대식으로 개조했다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202010/07/dfce190e-0e27-41e3-93a6-65161a89f2b5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '돌담한길' AND address = '제주특별자치도 서귀포시 남원읍 한신로 55');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '동산나라펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 이어도로 164', 33.2392371, 126.4393518, '여러 명소들과 접근성이 좋은 교통의 요지에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/891a547f-e6a4-412c-a932-56afb9102e16.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '동산나라펜션' AND address = '제주특별자치도 서귀포시 이어도로 164');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '두리안펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로 551-3 두리안펜션', 33.2759215, 126.7057667, '서귀포시 남원읍에 위치하며, 올레길 5코스, 큰엉해안경승지, 코코몽에코파크, 대발이파크 바로앞에 있으며, 넓은 잔디정원과 야자수가 편안한 힐링환경을 제공하는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201907/17/16b9fbc3-d4cb-4bde-b6a8-519c65bb1bb4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '두리안펜션' AND address = '제주특별자치도 서귀포시 남원읍 태위로 551-3 두리안펜션');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '뜨레향 펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 하례망장포로 27-8 (하례리) 뜨레향 펜션', 33.3420367, 126.5884214, '제주도 남쪽의 바닷가 망장포 부근에 주인 부부가 직접 지은 펜션이다. 파도소리가 들리는 펜션 테라스에는 감귤 정원이 조성되어 있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/05/8016d2d4-6a76-41b7-aa0a-e6890a1637ad.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '뜨레향 펜션' AND address = '제주특별자치도 서귀포시 남원읍 하례망장포로 27-8 (하례리) 뜨레향 펜션');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '뜨리바다펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로510번길 31-16', 33.2715279, 126.6997188, '남원 큰엉 해안경승지가 정원이 되는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3a2f0589-9914-4ff3-ae13-9c17c3770e4e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '뜨리바다펜션' AND address = '제주특별자치도 서귀포시 남원읍 태위로510번길 31-16');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라임오렌지빌', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 칠십리로 332', 33.2462212, 126.5869422, '제주의 드넓은 바다와 제주만의 자연을 가득 담은 이곳은 다양한 컨셉의 객실 타입과 함께 다양한 볼거리를 가득 담은 이색적인 펜션이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/4415bb22-8f91-47e1-b93a-56accd079f00.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라임오렌지빌' AND address = '제주특별자치도 서귀포시 칠십리로 332');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라포즈펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 중산간서로 459', 33.2629300, 126.4661100, '서귀포 중문에서 가까운 모던하고 깔끔한 풀빌라형, 복층형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4176f0df-1871-4d42-b5cc-46447711c601.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라포즈펜션' AND address = '제주특별자치도 서귀포시 중산간서로 459');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '레지나펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 이어도로 789', 33.2368350, 126.4995600, '바다와 한라산 조망이 가능한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9934ba36-76d0-43dd-994f-450c45e9b960.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '레지나펜션' AND address = '제주특별자치도 서귀포시 이어도로 789');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '리빙스톤펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 법환로58번길 37', 33.2418584, 126.5102416, '리빙스톤펜션은 서귀포 바다와 범섬, 섶섬, 문섬을 한눈에 볼 수 있는 전망을 보유한 펜션이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/6c70d5d7-d15b-49e1-9299-842be1cb71a8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '리빙스톤펜션' AND address = '제주특별자치도 서귀포시 법환로58번길 37');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '마녀의언덕 펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 예래해안로 207', 33.2342186, 126.3844947, '올레길과 낚시 포인트로 유명한 큰코지 바로 앞에 위치한 이곳은 영화 ‘마녀’의 촬영지로 유명한 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/93e94993-edc3-4881-9b73-40eb18f3f5f7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '마녀의언덕 펜션' AND address = '제주특별자치도 서귀포시 예래해안로 207');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '메모리펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 염돈로 93-14', 33.2615636, 126.4944700, '서귀포 시내권에 위치한 메모리 인 제주 펜션은 피톤치드 가득한 제주산 편백나무로 만들어진 펜션.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8afac688-3daa-4f29-b00e-b911bb7ad874.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '메모리펜션' AND address = '제주특별자치도 서귀포시 염돈로 93-14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '물댄동산 통나무펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로 551-5', 33.2762767, 126.7055972, '남원 영화박물관과 해안경승지 맞은편에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c3139ad4-8702-4554-9329-095a4267ef2a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '물댄동산 통나무펜션' AND address = '제주특별자치도 서귀포시 남원읍 태위로 551-5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '미소가펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로44번길 2-1', 33.2725070, 126.6523740, '전통적인 돌담 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/12/5c4ae1ec-b3b8-43cd-bb24-e51061a5b198.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '미소가펜션' AND address = '제주특별자치도 서귀포시 남원읍 태위로44번길 2-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '밀레니엄빌', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 보목로64번길 196', 33.2452066, 126.5899924, '겨울에도 꽃이 피는 제주도에서 제일 따뜻한 화훼단지 옆에 위치한 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/aea27dd6-a077-47ac-8c9a-af32dad0087f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '밀레니엄빌' AND address = '제주특별자치도 서귀포시 보목로64번길 196');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바다산책', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 남태해안로 11-14', 33.2755419, 126.7107131, '남원큰엉까지 이어지는 해안절벽산책로가 매력적인 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4ae409b6-2e87-4775-a9e2-0fc873fa7751.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바다산책' AND address = '제주특별자치도 서귀포시 남원읍 남태해안로 11-14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바다위올레펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 이어도로 826-13', 33.2347260, 126.5031360, '넓은 바다가 한눈에', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5529954f-1bcf-41d5-9e90-adee9c3a67c7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바다위올레펜션' AND address = '제주특별자치도 서귀포시 이어도로 826-13');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바닷가하얀집', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 태평로120번길 29-2', 33.2419106, 126.5361713, '객실에서 금방이라도 손에 잡힐듯한 바다가 펼쳐지는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/72bcc684-9cfd-4417-8484-00aeba02dfa5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바닷가하얀집' AND address = '제주특별자치도 서귀포시 태평로120번길 29-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '방울풍뎅이 하우스', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 위미중앙로 274-45', 33.2701030, 126.6738740, '남원읍 위미리 바닷가 앞에 위치한 가족, 단체 단위 손님을 위한 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/97ba5d6b-e21f-4057-be6e-1d26a39446ee.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '방울풍뎅이 하우스' AND address = '제주특별자치도 서귀포시 남원읍 위미중앙로 274-45');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '범섬풍경펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 속골로 26', 33.2421460, 126.5297300, '전객실 바다 전망이 가능하며 올레 7코스에 인접한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c3690240-5409-47c2-8949-9287dc4d98cb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '범섬풍경펜션' AND address = '제주특별자치도 서귀포시 속골로 26');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '브릭216', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 이어도로 216', 33.2408370, 126.4434600, '초원의 조랑말과 멀리 보이는 바다의 조화', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/47a4d1ed-78dd-4795-92b1-89132200aec2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '브릭216' AND address = '제주특별자치도 서귀포시 이어도로 216');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '블란디야', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 법환로 15', 33.2366458, 126.5137243, '청정제주의 깨끗한 이미지를 연상케하는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e157e376-0c01-42e5-aea8-96a8dbef779d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '블란디야' AND address = '제주특별자치도 서귀포시 법환로 15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '비레이지', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 위미해안로129번길 17', 33.2704970, 126.6516820, '올레5코스 아름다운 마을 위미리의 감귤나무 사이에 위치한 신축 B&B', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b4cbb741-cfa0-47d6-89e3-d4b81cdb9f10.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '비레이지' AND address = '제주특별자치도 서귀포시 남원읍 위미해안로129번길 17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '비스비제주 앤 팜빌리지', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 속골로 29-10', 33.2427503, 126.5279492, '프라이빗 모던 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8df7e72e-a63f-485d-9747-4a48d8d51b73.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '비스비제주 앤 팜빌리지' AND address = '제주특별자치도 서귀포시 속골로 29-10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '빌라비 하우스 펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 색달로64번길 51', 33.2814107, 126.4111118, '운영자가 직접 설곔 치 시공한 숙소로 고급스러운 익스테리어와 인테리어를 자랑하는 빌라비 하우스는 최고급 마감 및 구스이불로 쾌적하고 편안한 잠자리를 제공한다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202101/19/8611706b-89c0-42d2-a9e8-aad3d7b46cd8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '빌라비 하우스 펜션' AND address = '제주특별자치도 서귀포시 색달로64번길 51');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '서귀포귤림성', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 일주동로 8941', 33.2504829, 126.5367012, '농림부지정 휴양펜션 서귀포 귤림성', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c6adbf29-9ac2-43a3-848d-3a98773a3faf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '서귀포귤림성' AND address = '제주특별자치도 서귀포시 일주동로 8941');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '서귀포통나무집', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 칠십리로503번길 1', 33.2423359, 126.6042263, '시멘트를 전혀 사용하지 않고 나무벽체, 황토바닥으로 시공된 자연친화적 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a105c6c6-d947-45d6-9173-eac0101618bb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '서귀포통나무집' AND address = '제주특별자치도 서귀포시 칠십리로503번길 1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '서부민박', 'PENSION', 'SOUTH', '제주도 서귀포시 이어도로 602', 33.2352640, 126.4807050, '제주도 서귀포시 강정마을에 위치한 서민적인 가격의 민박집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/38422fca-7f3d-4ea9-a1e7-70be91041c7d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '서부민박' AND address = '제주도 서귀포시 이어도로 602');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '성게돌 펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 쇠소깍로 186', 33.2506837, 126.6187196, '하효 쇠소깍 인근에 위치한 펜션.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201808/02/8f8c3d9b-c4cc-406b-ac93-8cd56caaf121.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '성게돌 펜션' AND address = '제주특별자치도 서귀포시 쇠소깍로 186');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '셋째날', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 하신상로 273-7', 33.3306923, 126.5673572, '서귀포에 한라산과 섶섭 바다가 시원하게 펼쳐 보이고,감귤밭이 딸려 있는 오픈2년된 신축펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f708a2db-d04e-4c3c-869d-7675c1f80e8c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '셋째날' AND address = '제주특별자치도 서귀포시 하신상로 273-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '소랑호젠펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로 545', 33.2757070, 126.7054200, '잔디정원과 아늑한 분위기의 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/34c350bf-6816-4d29-8e67-f49417256174.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '소랑호젠펜션' AND address = '제주특별자치도 서귀포시 남원읍 태위로 545');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '소풍BnB', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 호근남로 82', 33.2515937, 126.5323378, '귤나무로 둘러쌓인 조용한 쉼터, 2인실,3인실, 커플룸, 조식포함(서귀포)', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f667cf68-1109-4907-946f-84e4faa1faa7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '소풍BnB' AND address = '제주특별자치도 서귀포시 호근남로 82');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '솔바람풍경소리', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태수로143번길 132-37', 33.2904470, 126.7241740, '자연의 경치와 낭만이 있는 펜션으로 가족이나 연인 친구끼리 편안한 휴식을 할 수 있는 휴양펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/631213ce-555c-40db-8a3a-8eb0bedad503.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '솔바람풍경소리' AND address = '제주특별자치도 서귀포시 남원읍 태수로143번길 132-37');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '쇠소깍섬도리왓펜션', 'PENSION', 'SOUTH', '서귀포시 효돈순환로 217-3', 33.2570400, 126.6215300, '서귀포 하효마을, 쇠소깍에서 도보로 3분 거리에 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/aee49184-e605-4855-a422-3ffb38afd9c9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '쇠소깍섬도리왓펜션' AND address = '서귀포시 효돈순환로 217-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '수키하우스', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 도순남로 29', 33.2505144, 126.4742572, '제주도 서귀포 도순동에 위치한 조용한 마을의 쉬어가는 민박입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202605/24/549ae6e7-b890-44af-b022-bc430e4ded20.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '수키하우스' AND address = '제주특별자치도 서귀포시 도순남로 29');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스테이낭낭', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 분토왓로 67-23 (서홍동) 스테이낭낭', 33.2716304, 126.5529268, '한라산자락에 포근히 안긴 프라이빗 독채 스테이', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202504/13/9f7cacbe-f50b-4bdf-8f21-4139e502b1fc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스테이낭낭' AND address = '제주특별자치도 서귀포시 분토왓로 67-23 (서홍동) 스테이낭낭');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스테이어 리틀롱거', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 동홍로262번길 62 (동홍동)', 33.2695514, 126.5726854, '자연이 어우러진 시내권 숙소로써 넓은 정원과 귤꽃향기 가득한 힐링 스팟입니다. 겨울에는 동백 여름앤 수국 봄엔 유채꽃이 만개하는 아름다운 숙소입니다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202502/10/66c52766-0455-4f34-bb72-c8f1b26fcae9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스테이어 리틀롱거' AND address = '제주특별자치도 서귀포시 동홍로262번길 62 (동홍동)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '시루네펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 검은여로 79', 33.2504605, 126.5807376, '시루네펜션은 서귀포시 토평동에 위치한 조용하고 한적한 펜션이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202212/09/e8d2c1a5-1f96-4107-a356-4be60bf9c5b3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '시루네펜션' AND address = '제주특별자치도 서귀포시 검은여로 79');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아란치아 펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 염돈로 65', 33.2545830, 126.4919422, '서귀포의 중심 강정마을에 위치한 아란치아 펜션은 정남향의 좋은 채광을 시작으로 테라스에서 시원하게 뻗어있는 서귀포 바다를 볼 수 있는 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/97bc5015-5028-4b23-91cb-4915526b5ef9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아란치아 펜션' AND address = '제주특별자치도 서귀포시 염돈로 65');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아침의향기', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 색달중앙로 122', 33.2656780, 126.4144090, '제주에서 즐기는 고품격 서비스, 아침의 향기와 함께 하세요', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3843a80e-a9ee-4440-a1f4-863603431fe0.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아침의향기' AND address = '제주특별자치도 서귀포시 색달중앙로 122');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아토하우스', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 신흥앞동산로 84', 33.3372650, 126.7408450, '인생의 특별한 경험을 할 수 있는 곳 ''아토하우스''', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/747be7b9-ce0c-4a6a-ad31-d69cbb55af71.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아토하우스' AND address = '제주특별자치도 서귀포시 남원읍 신흥앞동산로 84');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '애견동반 상추네', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 검은여로 81-3', 33.2505357, 126.5807475, '펜션 지킴이 상추가 손님을 맞이하는 이곳은 넓은 잔디밭과 깔끔한 숙소가 눈길을 끄는 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/8715ba64-3d3f-457f-9a2e-6c376e004984.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '애견동반 상추네' AND address = '제주특별자치도 서귀포시 검은여로 81-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '에리두', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 대포복개로 37', 33.2419260, 126.4346420, '시내, 관광지와 가깝지만 조용한 대포동에 자리해 완벽한 휴식을 취할 수 있는 B&B', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/79b3ec63-5dd7-404c-9e56-367374f3f2bb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '에리두' AND address = '제주특별자치도 서귀포시 대포복개로 37');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '여행스케치', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 일주서로 694', 33.2555333, 126.4360843, '중문관광단지 인근 가족같은 분위기의 깨끗하고 저렴한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/bb49e7d9-bc80-449e-8f45-7436d2a613ec.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '여행스케치' AND address = '제주특별자치도 서귀포시 일주서로 694');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '예이츠산장', 'PENSION', 'SOUTH', '서귀포시 남원읍 516로 918', 33.3254660, 126.6012800, '남원 중산간, 공기 맑은 한라산 밑자락에 위치한 산장', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/23/141f5c89-80ed-424d-bffa-1e8cb78b6dc1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '예이츠산장' AND address = '서귀포시 남원읍 516로 918');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '옛마을펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 검은여로 80', 33.2507973, 126.5817291, '옛마을펜션은 서귀포시 토평동에 위치한 조용하고 한적한 펜션이다. 객실을 편백나무로 시공해 건강에 유해하지 않게 지은 이곳은 모든 가구도 원목을 이용해 숲속 오두막 느낌을 선사한다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202212/09/c63ffa4d-ae38-47fe-860d-265a16e82e5e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '옛마을펜션' AND address = '제주특별자치도 서귀포시 검은여로 80');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오션트리', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 월드컵로 188', 33.2330519, 126.5055822, '스쿠버다이빙을 즐길 수 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/39db09b5-6ad4-47e5-af8b-88b504449e29.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오션트리' AND address = '제주특별자치도 서귀포시 월드컵로 188');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '올리버스테이', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로852번길 22-1 (태흥리)', 33.2826047, 126.7376569, '대형견도 함께 행복하게 지낼 수 있는 대형견 동반 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202507/02/07e2c915-aa9b-48ad-a5da-51f041f9a332.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '올리버스테이' AND address = '제주특별자치도 서귀포시 남원읍 태위로852번길 22-1 (태흥리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '올리수펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 중문로5번길 16', 33.2514303, 126.4348243, '중문관광단지 인근에 위치한 깔끔한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201907/04/fb8da7d9-ad39-48cb-92c0-873a4c90e4c1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '올리수펜션' AND address = '제주특별자치도 서귀포시 중문로5번길 16');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '위미모루왓펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 위미항구로 51 (위미리)', 33.3060336, 126.6580882, '짧은 여행부터 한달살이, 일년살이를 위한 공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201810/26/bf9c1e9e-af6b-4625-bd0d-b31f62722b3b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '위미모루왓펜션' AND address = '제주특별자치도 서귀포시 남원읍 위미항구로 51 (위미리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '유러하우스', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 막숙포로 136', 33.2318082, 126.5087800, '빨간지붕, 하얀건물, 테라스가 그림같은 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/74987390-6f56-4288-9b6a-db03666a7530.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '유러하우스' AND address = '제주특별자치도 서귀포시 막숙포로 136');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '장원민박', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 천제연로 49', 33.2569568, 126.4111595, '중문관광단지와 올레8코스중간에 위치한 부담없고 아늑한 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201907/04/07871785-7241-4071-af06-d95883496cad.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '장원민박' AND address = '제주특별자치도 서귀포시 천제연로 49');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제니빌펜션', 'PENSION', 'SOUTH', '제주도 서귀포시 남원읍 태위로 604-15', 33.2764630, 126.7124940, '이국적인 디자인의 건물과 정원의 조화가 바다의 매력을 더하는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b04eb8b8-04dd-4944-af12-35a30cbc8c5f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제니빌펜션' AND address = '제주도 서귀포시 남원읍 태위로 604-15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제이제이하우스', 'PENSION', 'SOUTH', '제주도 서귀포시 중산간서로 241', 33.2607700, 126.4855350, '산방산, 마라도, 여미지식물원 등이 10분 이내에 가까운 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8a6ec4c3-d28f-4abd-b8f9-e6027c07a820.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제이제이하우스' AND address = '제주도 서귀포시 중산간서로 241');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 소담 펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 막숙포로 146', 33.2314832, 126.5080699, '올레7코스 흰돌밑 바로 앞에 위치한 모던펜션은 전 객실 바다전망이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c3dd5953-5c32-4e3e-bf42-2405310e6968.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 소담 펜션' AND address = '제주특별자치도 서귀포시 막숙포로 146');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 순진한가 펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 법환상로 65', 33.2406100, 126.5100250, '한폭의 그림 같은 서귀포 앞바다와 범섬을 바라보며 편안한 휴식을', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c18aee90-ea7e-4afd-b3d2-f53baa8836b9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 순진한가 펜션' AND address = '제주특별자치도 서귀포시 법환상로 65');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주도작은집 작은별', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 남한로153번길 42', 33.2881765, 126.7092595, '제주귤밭정원 독채스테이', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202508/07/a6e0671f-7cbc-4abe-8b14-a0e9fdc88b4f.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주도작은집 작은별' AND address = '제주특별자치도 서귀포시 남원읍 남한로153번길 42');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주락', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 월드컵로 203', 33.2316060, 126.5063100, '범섬이 정면으로 보이고 올레길을 끼고 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/97709b75-c452-4b81-81c2-04fe605737b5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주락' AND address = '제주특별자치도 서귀포시 월드컵로 203');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주목화휴양펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로 485', 33.2751664, 126.6990735, '이국적인 느낌의 제주목화휴양펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/52bda08c-a354-4090-90af-cdf48e2d9ef1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주목화휴양펜션' AND address = '제주특별자치도 서귀포시 남원읍 태위로 485');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주사랑', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 태평로442번길 21', 33.2443141, 126.5682209, '서귀포항과 가까운 도미토리 겸 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/00331d39-0e05-48a2-b03a-937a245e6648.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주사랑' AND address = '제주특별자치도 서귀포시 태평로442번길 21');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주샘모루펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 염돈로28번길 11', 33.2530980, 126.4949300, '샘이 솟는 언덕에 위치한 독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a2c6a6a2-225b-4b3e-86fb-c5c8cb56b119.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주샘모루펜션' AND address = '제주특별자치도 서귀포시 염돈로28번길 11');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주서울민박', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로 970', 33.2877269, 126.7483291, '제주의 아름다운 바다가 있는 서귀포 태흥리에 위치한 독채렌탈하우스입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c11e879a-94f2-4a71-9f8f-131d832369fe.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주서울민박' AND address = '제주특별자치도 서귀포시 남원읍 태위로 970');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주스테이비우다', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 색달중앙로121번길 45', 33.2651183, 126.4092475, '제주 남부 중문지역에 위치하며, 비우다, 채우다, 나누다, 품은 으로 나누어진 복합문화공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8b73c14a-1c40-4108-9641-684229759f4d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주스테이비우다' AND address = '제주특별자치도 서귀포시 색달중앙로121번길 45');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주아이니', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로894번길 20-30', 33.2835544, 126.7405017, '제주에서도 가장 따뜻한 남쪽마을(남원읍)에 위치한, 350평 대지에 마당과 물놀이시설이 갖춰진 공동주택형 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202208/31/6692196b-7cbe-4cd5-b640-1539447547ec.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주아이니' AND address = '제주특별자치도 서귀포시 남원읍 태위로894번길 20-30');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주애견동반감성숙소 르페도라', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 신례중앙로 6', 33.2891920, 126.6294100, '한라산이 보이는 1000평 잔디운동장에서 반려견과 함께 하세요', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202411/13/0ab2c798-4410-4af3-9e62-d3787c2a0f79.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주애견동반감성숙소 르페도라' AND address = '제주특별자치도 서귀포시 남원읍 신례중앙로 6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주에코스위츠', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 중문상로 207-13', 33.2655370, 126.4278600, '제주 에코 스위츠 펜션은 트립 어드바이져에서 선정한 2014-2016 트레블러스 초이스부분에서 B&B(Bed and Breakfast, 3성급 이상의 숙박시설)부분에서 수상하였다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0ed7d9d5-124e-4e90-a4a5-b478d7084fab.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주에코스위츠' AND address = '제주특별자치도 서귀포시 중문상로 207-13');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주영숙', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태수로26번길 17', 33.2922889, 126.7393618, '붉은벽돌과 귤나무정원 속의 시골호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201807/13/4b07bb5f-2f03-4ff6-a725-ab45590f36c8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주영숙' AND address = '제주특별자치도 서귀포시 남원읍 태수로26번길 17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주은빌레', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 한신로 247-3', 33.3116589, 126.7167925, '가족이 운영해서 더욱 깨끗하게 관리되는 재방문 손님이 많은 남원 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/11/880111d1-4234-4649-8e76-68039cb205f7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주은빌레' AND address = '제주특별자치도 서귀포시 남원읍 한신로 247-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주잔잔', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 칠십리로485번길 15-17', 33.2426363, 126.6009490, '잔잔한 휴식을 취하다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/77d2dbe6-7163-4627-80f9-e000d67a2dc8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주잔잔' AND address = '제주특별자치도 서귀포시 칠십리로485번길 15-17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주카사블랑카펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 학수암로 107', 33.2703972, 126.5279035, '서귀포시 남쪽 중앙에 위치하며 워크샵, 엠티 등 다수의 숙박에 적합한 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/19/36308122-8169-4f94-80de-5e88c11e9d4a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주카사블랑카펜션' AND address = '제주특별자치도 서귀포시 학수암로 107');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주파인비치펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로 976-10', 33.2922889, 126.7393618, '조용한 시골 어촌에 위치하여 매일 점심 시간대에 열리는 수산물 경매 과정을 볼 수 있고 주 특산물인 싱싱한 당일 바리 옥돔을 구매할 수 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/056a79ee-a512-4a26-bbc4-5854d2494011.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주파인비치펜션' AND address = '제주특별자치도 서귀포시 남원읍 태위로 976-10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주팡숑예래', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 예래해안로 357', 33.2409655, 126.3958822, '제주바다의 풍경이 그림같이 펼쳐진 곳!', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/94424b89-3e80-447c-a6ab-2da0877f19bf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주팡숑예래' AND address = '제주특별자치도 서귀포시 예래해안로 357');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주펜션향림원', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 칠십리로394번길 8-3', 33.2437437, 126.5932198, '서귀포 시내와 가까워 이용이 편리하고 저렴한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a108a9e6-6747-4ed7-b722-86f41579c6fc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주펜션향림원' AND address = '제주특별자치도 서귀포시 칠십리로394번길 8-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주해나루민박', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 신서로98번길 19-1 (강정동) 제주해나루', 33.2615636, 126.4944700, '서귀포시 신시가지에 위치한 바다전망 풀옵션 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201810/25/6f719f81-93c1-4109-a6a8-54b660ab3c13.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주해나루민박' AND address = '제주특별자치도 서귀포시 신서로98번길 19-1 (강정동) 제주해나루');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '중문그린힐통나무펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 천제연로 103-36', 33.2814107, 126.4111118, '중문관광단지와 도보 5분거리에 위치한 천연원목 통나무독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a30b67b6-0b47-4bd4-b81e-2516262d4d83.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '중문그린힐통나무펜션' AND address = '제주특별자치도 서귀포시 천제연로 103-36');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '중문빌리지펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 예래로 270', 33.2427000, 126.3852500, '원룸형 레저하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a0216d0d-7c24-45bb-8452-98c0d9245ea9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '중문빌리지펜션' AND address = '제주특별자치도 서귀포시 예래로 270');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '지삿개풍경', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 이어도로 150', 33.2397580, 126.4382600, '지삿개 전망이 보이는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0c683c52-cadd-4ee8-a402-26edfbf1516e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '지삿개풍경' AND address = '제주특별자치도 서귀포시 이어도로 150');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '채우리네펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 검은여로 67', 33.2495927, 126.5813354, '채우리네펜션은 반려동물과 함께 즐거운 하룻밤을 보낼 수 있는 반려견 동반 펜션이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202212/15/793ccd6f-fe7a-472f-9d85-765da47da7cc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '채우리네펜션' AND address = '제주특별자치도 서귀포시 검은여로 67');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '청재설헌', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 인정오름로 135-18', 33.2891483, 126.5718271, '청재설헌은 ‘Bed and Breakfast’라는 슬로건을 추구한다. 각종 비타민이나 영양식을 추구하는 현대인은 이해 할지 모르겠다. 하지만 우리네 어머니들이 늘 말씀하시던 한결같은 말 중에 “건강하려면 단잠을 자고 제 때 끼니를 잘 먹어야한다”고 하셨다. 2000년 11월 처음 문을 연 청재설헌은 더도 덜도 없이 그 말 그대로를 실천하는 내츄럴 숙박시설이다. ''Bed''는 잘 자는 것, ‘Breakfast’는 아침식사를 하는 것을 말한다. 청재설헌에서는 주인과 객이 함께 아침식사를 하는 곳이다. 호화스러운 호텔이나 여타 숙박시설과 달리 청재설헌 건물은 소박하게 지어졌다. 서귀포 토평동 중산간 자락, 한라산 남사면이라 어느 곳이든 한라산 정상이 한눈에 보이는 지역의 특성을 고려한 듯, 숙소 앞 정원에서도 건물 지붕 뒤로 한라산을 가리지 않는다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202011/03/09c2a083-88e5-492c-b9c8-689766a5ca93.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '청재설헌' AND address = '제주특별자치도 서귀포시 인정오름로 135-18');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '콴도제주', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로151번길 14-12', 33.3060340, 126.6580900, '위미리 감귤밭 사이 평화로운 쉼터', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/71668c62-498c-42a8-9734-a53ff2982363.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '콴도제주' AND address = '제주특별자치도 서귀포시 남원읍 태위로151번길 14-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '투데이제주펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 예래로 267-2', 33.2423033, 126.3859064, '넓고 아름다운 정원이 있는 별장', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d1295ccc-83b4-4e96-9191-3872588b7880.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '투데이제주펜션' AND address = '제주특별자치도 서귀포시 예래로 267-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '파도봐펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 남태해안로 141', 33.2783351, 126.7202180, '전객실 오션뷰~아름다운 풍경과 낭만이 있는 펜션..서귀포 파도봐펜션 입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202010/19/061fedba-a9e1-40f6-bf2e-6a3aadc469b1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '파도봐펜션' AND address = '제주특별자치도 서귀포시 남원읍 남태해안로 141');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '팜힐펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 예래로 107', 33.2524249, 126.3964014, '서귀포 중문관광단지 옆에 위치한 이국적인 분위기의 콘도형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7ee43a8f-e364-4dd8-b1b2-826f5c1e704a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '팜힐펜션' AND address = '제주특별자치도 서귀포시 예래로 107');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '펜션 수망가라', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태수로 667-11', 33.3508829, 126.6980129, '서귀포 수망리 펜션 수망가라 신축 펜션, 최고급 풀옵션 침구, 직접 가꾼 무공해 싱싱한 야채 서비스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201909/10/ea728f8a-2d81-4abb-9f65-e5de14415be3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '펜션 수망가라' AND address = '제주특별자치도 서귀포시 남원읍 태수로 667-11');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '펜션머물다', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 논짓물로48번길 16-6', 33.2416337, 126.3875957, '모던하고 심플한 인테리어가 돋보이는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b04eb9b2-b797-4c32-ad62-915c96ebdded.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '펜션머물다' AND address = '제주특별자치도 서귀포시 논짓물로48번길 16-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '펜션연리', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 논짓물로 50', 33.2404221, 126.3860267, '서귀포 중문에 전망 좋은 곳에 위치한 아름다운 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/329c76ef-a8c3-4b2a-9b3e-8e8bc3883088.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '펜션연리' AND address = '제주특별자치도 서귀포시 논짓물로 50');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '펜션형민박꿈꾸는숲', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 태평로205번길 43-7', 33.2474700, 126.5399400, '바다가 한눈에 보이는 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5d83984d-79ea-49b1-a5bc-8f84cf9047b3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '펜션형민박꿈꾸는숲' AND address = '제주특별자치도 서귀포시 태평로205번길 43-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '포시즌펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 칠십리로285번길 3', 33.2844099, 126.5796743, '전객실 바다전망과 서귀포 시내에서 5분거리에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2221c8b5-ad86-4f5a-8d23-df93631b1665.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '포시즌펜션' AND address = '제주특별자치도 서귀포시 칠십리로285번길 3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '포유펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 서의로 40-6', 33.2959968, 126.7109459, '남원에 위치한 감귤과수원 및 연못이 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/07380618-9dd7-4fe3-bcad-de8f9a16af2d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '포유펜션' AND address = '제주특별자치도 서귀포시 남원읍 서의로 40-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '푸른바다펜션', 'PENSION', 'SOUTH', '제주도 서귀포시 남성로128번길 37-10', 33.2419170, 126.5576550, '정원 앞 섬들이 반겨주는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/af2c5b12-f7da-4acf-b403-7046f59e3c8b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '푸른바다펜션' AND address = '제주도 서귀포시 남성로128번길 37-10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하늘정원펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 칠십리로394번길 8-4', 33.2439840, 126.5932900, '서귀포 칼호텔에서 차로 3분 거리 내에 위치한 하늘정원펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fe21b140-b210-4566-9164-62409e829581.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하늘정원펜션' AND address = '제주특별자치도 서귀포시 칠십리로394번길 8-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '한라앤탐 펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 위미중앙로300번길 8', 33.2729847, 126.6742609, '제주 전통 돌담집의 느낌을 제대로 살린 공간으로 서까래는 그대로 살리고 현대식으로 리모델링되어 이색적이며, 객실에서 창 밖으로 바라보이는 감귤밭 풍경이 따스함을 전해주는 펜션이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/40cd4012-35fb-4032-b6c2-accfb527008e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '한라앤탐 펜션' AND address = '제주특별자치도 서귀포시 남원읍 위미중앙로300번길 8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '한라하이츠', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 고근산로202번길 94', 33.2686727, 126.5182746, '펜션 주위로 난 올레 7-1코스와 서귀포 바다를 한 눈에 즐길 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a108a1f7-37c3-4023-bb4d-b8bc8231ffd7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '한라하이츠' AND address = '제주특별자치도 서귀포시 고근산로202번길 94');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해뜨는집펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 남원읍 하신로 31', 33.3420367, 126.5884214, '한라산과 바다를 전부 조망하는 단독형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/463eb8e4-d301-4ba5-8db2-faadbb4e986a.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해뜨는집펜션' AND address = '제주특별자치도 서귀포시 남원읍 하신로 31');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해성펜션', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 천제연로 131-8', 33.2542072, 126.4196588, '중문 천제연폭포에 위치한 원룸식 콘도형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a9294ff6-c1b0-4614-b90b-cc7ee8e9a8bb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해성펜션' AND address = '제주특별자치도 서귀포시 천제연로 131-8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호도하우스', 'PENSION', 'SOUTH', '제주특별자치도 서귀포시 막숙포로 134', 33.2319569, 126.5089298, '바람이 머물고 가는 올레 7코스에 위치한 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1c198a60-1db2-4819-b626-1d871195db84.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호도하우스' AND address = '제주특별자치도 서귀포시 막숙포로 134');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '그랜드 조선 제주', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 중문관광로72번길 60', 33.2498464, 126.4050769, '조선호텔앤리조트가 선보이는 독자 브랜드 호텔로, 조선호텔 100년 전통의 노하우와 고전적인 우아함을 현대적인 감각으로 재해석한 럭셔리 리조트 호텔입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202107/15/cc4500df-9057-40f7-88f6-4d1dc91eac3b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '그랜드 조선 제주' AND address = '제주특별자치도 서귀포시 중문관광로72번길 60');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '금호리조트제주', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로 522-12', 33.2729640, 126.7014800, '바다와 한라산 전망을 동시에 누리는 Seaside Resort', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/058c0664-698f-45a8-9f06-d984c84934b9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '금호리조트제주' AND address = '제주특별자치도 서귀포시 남원읍 태위로 522-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '기린빌라리조트 제주', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 남원읍 서성로 427 (위미리)', 33.3027060, 126.6568975, '2022년에 설립된 기린빌라리조트는 신비로운 한라산의 웅장한 기운을 품은 제주도 섬의 청정 숲 속 산마루에 자리하고 있습니다. 프라이빗하고 편안한 보금자리와 머무르기만 해도 힐링이', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202411/29/19b6cd7f-00eb-4de8-8e2c-5e87a86565a7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '기린빌라리조트 제주' AND address = '제주특별자치도 서귀포시 남원읍 서성로 427 (위미리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '담앤루리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 이어도로343번길 63', 33.2435151, 126.4352216, '서귀포시 대포동에 위치한 담앤루리조트입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/017acd07-0761-4712-bfc1-83e07aa9bda5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '담앤루리조트' AND address = '제주특별자치도 서귀포시 이어도로343번길 63');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더베이 제주 리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 문필로35번길 46', 33.2411800, 126.5934750, '올레6코스와 소천지가 앞에 있는 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b3652534-08f4-4afe-9cf2-599343a7b776.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더베이 제주 리조트' AND address = '제주특별자치도 서귀포시 문필로35번길 46');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '더큐브리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 솔오름로105번길 24', 33.2869233, 126.5668626, '천혜의 자연경관을 품은 서귀포에 위치한 더큐브리조트 제주', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202006/12/6650dc40-2527-4855-8bf2-32b4b2cb0274.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '더큐브리조트' AND address = '제주특별자치도 서귀포시 솔오름로105번길 24');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '돈내코힐 리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 돈내코로 180', 33.3005940, 126.5851100, '한라산 조망과 노천 스파가 가능한 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e29db189-0ef4-4d57-9a4a-44f9f95ba5e7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '돈내코힐 리조트' AND address = '제주특별자치도 서귀포시 돈내코로 180');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라오체리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 이어도로 763 (강정동)', 33.2360570, 126.4967900, '소각족이 이용하기 좋은 디럭스 객실, 친구들이나 가족이 이용하기좋은 스위트 객실, 두 가족 이상이 함께 사용할 수 있는 복층구조의 단독형 그랜드스위트로 구성되어 있는 라오체 리조트입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/cc56b3b9-51e0-40a4-8bcb-f229a132095d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라오체리조트' AND address = '제주특별자치도 서귀포시 이어도로 763 (강정동)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '레이크힐스 제주 리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 존제로 24', 33.2674383, 126.3769731, '골프장과 함께하는 제주에서 즐기는 최고급 별장 문화', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0f0f305f-c278-46be-ae15-96eda81f1978.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '레이크힐스 제주 리조트' AND address = '제주특별자치도 서귀포시 존제로 24');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '마린포트리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 이어도로 744', 33.2354436, 126.4948834, '제주의 아름다운 자연환경과 어우러지는 휴식의 공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/96523398-b834-46ec-92fb-03b6b13ff41b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '마린포트리조트' AND address = '제주특별자치도 서귀포시 이어도로 744');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '벙커호텔앤리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 중산간동로8183번길 16', 33.2612932, 126.5427690, '서귀포시 중산간동로에 위치하여 제주의 아름다운 한라산과 바다를 모두 접할 수 있는, 자연과 도시를 같이 느낄 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202207/12/8e8873c1-43e2-450d-ab71-09fe0b773b40.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '벙커호텔앤리조트' AND address = '제주특별자치도 서귀포시 중산간동로8183번길 16');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '베이힐 풀앤빌라', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 예래로 424', 33.2451200, 126.3852800, '서귀포시 하예동에 위치한 빌라형 레지던스 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201901/28/06cac570-4133-4b54-876e-a0ffaaee1888.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '베이힐 풀앤빌라' AND address = '제주특별자치도 서귀포시 예래로 424');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '벨룸리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 516로277번길 45 (토평동) 벨룸리조트', 33.2795677, 126.5814390, '제주도 프라이빗 리조트 독채 숙소 및 사계절 온수풀', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202410/10/d936bc0d-c491-4622-b748-51348211d50b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '벨룸리조트' AND address = '제주특별자치도 서귀포시 516로277번길 45 (토평동) 벨룸리조트');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '브리지스튜디오', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 이어도로 738', 33.2352943, 126.4942854, '조식을 포함한 카페, 라운지, 바베큐, 바다전망대 등 다양한 편의시설을 갖추고 고객님들을 맞이하고있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2fa783bd-4051-44cb-ad37-fb362fb1c3f4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '브리지스튜디오' AND address = '제주특별자치도 서귀포시 이어도로 738');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '블루펄리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 태평로92번길 35', 33.2412000, 126.5319536, '외돌개, 중문관광단지, 주상절리대, 천지연폭포 등 유명 관광지와 접근성이 좋은 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ed39ce35-a4e5-4184-a8c0-d370402def53.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '블루펄리조트' AND address = '제주특별자치도 서귀포시 태평로92번길 35');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '샤모니리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 중산간서로 68', 33.2633130, 126.5043950, '한라산을 등지고 서귀포 앞바다를 바라보면 자리한 가족&단체형 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d05dfb79-e715-49dc-80bb-ff2a88644a1b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '샤모니리조트' AND address = '제주특별자치도 서귀포시 중산간서로 68');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '솔동산스테이', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 솔동산로10번길 45', 33.2408330, 126.5626750, '서귀포시 관광 미항에 위치하고 있는 가족형 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e7a4b3fe-dffc-4041-b56b-bbd8ca978244.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '솔동산스테이' AND address = '제주특별자치도 서귀포시 솔동산로10번길 45');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스프링데일골프앤리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 남원읍 서성로 459', 33.3060340, 126.6580900, '한라산이 북서풍을 막아주는 따뜻한 위미리에 위치해 일년 내내 플레이가 가능한 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6d054576-38cd-4ef4-9aa9-dfc72d9793a4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스프링데일골프앤리조트' AND address = '제주특별자치도 서귀포시 남원읍 서성로 459');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '씨사이드아덴', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 중문관광로 124', 33.2512908, 126.4129373, '제주 중문의 청정바다를 품은 고품격 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202002/04/ec396357-41ed-42ae-b57c-7a0291c54a86.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '씨사이드아덴' AND address = '제주특별자치도 서귀포시 중문관광로 124');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '씨에스호텔앤리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 중문관광로 198', 33.2429050, 126.4204300, '현대와 전통이 어우러진 단독 별장형 객실 호텔앤리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/9cb9d282-0877-450c-b263-516d91c5afe1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '씨에스호텔앤리조트' AND address = '제주특별자치도 서귀포시 중문관광로 198');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '씨오르리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 이어도로 989', 33.2422008, 126.5191098, '서귀포 앞바다의 절경을 간직한 리조트로 바다 풍경과 함께 아침을 열수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202012/16/f4013b16-d7b8-4004-b733-525dae48f7e7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '씨오르리조트' AND address = '제주특별자치도 서귀포시 이어도로 989');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아트빌라스 제주', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 색달중앙로252번길 124', 33.2893348, 126.4152061, '제주의 자연을 모티브로 세계 유명 건축가들이 모여 건축한 고급 리조트', 'https://api.cdn.visitjeju.net/thumbnail/photomng/imgpath/202404/29/c1ae480d-67ae-479a-a446-864f930da31d.JPG', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아트빌라스 제주' AND address = '제주특별자치도 서귀포시 색달중앙로252번길 124');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '알앤비타운', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 분토왓로174번길 45', 33.2798730, 126.5518126, '가슴 한 가득 상쾌한 공기와 푸른바다의 아름다움을 만날 수 있는 RNBTOWN', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/12a3956d-6062-4a7c-8944-aed67bef9f1c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '알앤비타운' AND address = '제주특별자치도 서귀포시 분토왓로174번길 45');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '야크마을', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 색달중앙로 162', 33.2688261, 126.4158647, '제주의 자연 속에서 새로운 영감을 전하고, 삶의 균형을 회복할 수 있는 관광 휴양단지', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202301/06/4564a00b-ddea-439f-adf5-fdf194d95794.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '야크마을' AND address = '제주특별자치도 서귀포시 색달중앙로 162');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '올레요리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 이어도로 720', 33.2615636, 126.4944700, '제주 올레 7코스에 위치한 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/90815c70-45b6-42c2-bad3-0f0d40a9f9e6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '올레요리조트' AND address = '제주특별자치도 서귀포시 이어도로 720');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '월드컵리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 월드컵로45번길 40', 33.2443580, 126.5110860, '서귀포의 중심에 자리잡고 있으며, 앞으로는 바다에 떠 있는 범섬과 뒤로는 한라산을 조망할 수 있고 맑은 새소리를 들으며 추억을 남길 수 있는 쾌적한 휴양리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c080002f-bfe2-4198-a81f-080f3fde45e7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '월드컵리조트' AND address = '제주특별자치도 서귀포시 월드컵로45번길 40');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주도아이랑 파미유리조트 키즈가족펜션', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 이어도로 826-6', 33.2615636, 126.4944700, '파미유는 불어로 가족이란 뜻으로 가족을 대하는 마음으로 서비스를 제공하는 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/00b152e8-f993-4cd2-a7a4-f56e6adaad9d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주도아이랑 파미유리조트 키즈가족펜션' AND address = '제주특별자치도 서귀포시 이어도로 826-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주부영호텔엔리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 중문관광로 222', 33.2412366, 126.4244920, '중문관광단지에 위치한 제주지역 최대의 컨벤션 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c099710e-693b-4524-b94a-4c3e45ed057c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주부영호텔엔리조트' AND address = '제주특별자치도 서귀포시 중문관광로 222');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주아이브리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 산록남로 1966-34', 33.3017900, 126.5095900, '중산간에 위치한 통나무 펜션 & 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b6695c46-fd8a-495c-a04c-64b9ea322f00.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주아이브리조트' AND address = '제주특별자치도 서귀포시 산록남로 1966-34');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주엠리조트 제주서귀포', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 중산간서로157번길 28-6', 33.2572843, 126.4951274, '편백나무의 향과 편안함을 느낄 수 있으며, 다양한 부대시설로 가족/단체여행객에게 인기가 많다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201909/30/41693d84-bf01-409f-8387-54fb22968bad.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주엠리조트 제주서귀포' AND address = '제주특별자치도 서귀포시 중산간서로157번길 28-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '중문통나무펜션리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 중산간서로594번길 7-8', 33.2633327, 126.4510473, '150년 수령의 시베리아 바이칼 호수에서 온 최고의 수나무로 지어진 통나무 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1d7ba82f-a824-4190-a2bd-0718002da792.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '중문통나무펜션리조트' AND address = '제주특별자치도 서귀포시 중산간서로594번길 7-8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '중문훼미리리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 소보리당로164번길 83', 33.2659029, 126.3837498, '중문관광단지가 가깝고 관광하기에 교통이 편리한 가족호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d14d6afd-3b93-49e7-a71a-f3172e65d002.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '중문훼미리리조트' AND address = '제주특별자치도 서귀포시 소보리당로164번길 83');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '켄싱턴리조트 서귀포', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 이어도로 684 (켄싱턴리조트)', 33.2335718, 126.4893750, '아름다운 자연경관을 가진 제주 서귀포 지역에 위치한 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201907/23/40216361-f0dd-4238-a1a4-93bdf0bb72c5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '켄싱턴리조트 서귀포' AND address = '제주특별자치도 서귀포시 이어도로 684 (켄싱턴리조트)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '켄싱턴리조트 제주중문', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 중문관광로72번길 29-29', 33.2814107, 126.4111118, '#제주도 #켄싱턴리조트제주중문 #가족호텔 #패밀리리조트 #전객실오션뷰 #제주도숙소 #힐링여행 #제주도숙소추천 #정원 #산책로', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/1d5397e5-c4e9-4c83-bc0e-26cfe797a328.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '켄싱턴리조트 제주중문' AND address = '제주특별자치도 서귀포시 중문관광로72번길 29-29');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '클럽이에스 제주리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 1100로 501', 33.2514303, 126.4348243, '도시와 문명에 지친 이들에게 때 묻지 않은 자연 속 편안한 휴식을 드리기 위해 설립된 휴양 전문 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202008/10/654e5939-8941-4999-96cb-4baca1adb670.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '클럽이에스 제주리조트' AND address = '제주특별자치도 서귀포시 1100로 501');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '태흥리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 남원읍 태위로 988-6', 33.2884750, 126.7501140, '바다가 한눈에 보이는 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5aedc4e2-887e-43e6-865a-61ed54a8a4c2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '태흥리조트' AND address = '제주특별자치도 서귀포시 남원읍 태위로 988-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '팜밸리리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 중산간서로 193', 33.2615636, 126.4944700, '서귀포시에 위치한 개인정원과 개인수영장을 갖춘 럭셔리 풀빌라 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/52fa2c91-77a7-46b5-93e1-4c752f3bae8a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '팜밸리리조트' AND address = '제주특별자치도 서귀포시 중산간서로 193');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '포트애비뉴', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 대포동서로 63-19', 33.2407899, 126.4386215, '제주 중문의 자연 속에 위치한 리조트형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202208/31/b9a4178c-6042-482e-86c5-38ef632f4218.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '포트애비뉴' AND address = '제주특별자치도 서귀포시 대포동서로 63-19');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '휴마루리조트', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 중산간동로8183번길 16', 33.2612932, 126.5427422, '문섬, 섶섬, 범섬이 보이는 바다와 한라산 조망이가능한 휴향형 휴마루리조트 입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6fb40508-1d1b-404f-923c-75f61ebb0aee.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '휴마루리조트' AND address = '제주특별자치도 서귀포시 중산간동로8183번길 16');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'JW 메리어트 제주 리조트 & 스파', 'RESORT', 'SOUTH', '제주특별자치도 서귀포시 태평로 152', 33.2665145, 126.5323308, '세계 최고의 리조트 디자이너가 만든 ''JW메리어트 제주 리조트''', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202407/11/1b3a233e-fddd-4be0-80a9-71248311b85f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'JW 메리어트 제주 리조트 & 스파' AND address = '제주특별자치도 서귀포시 태평로 152');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '누구하나', 'CAMPING', 'WEST', '제주특별자치도 제주시 애월읍 수산서4길 3 (수산리) 누구하나', 33.4675496, 126.3862275, '애월읍에 위치한 카라반이 있는 빨간벽돌집, 그린스테이 Greenstay 누구하나', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202303/22/0ceb7559-07f3-4aaf-a6c4-dae5263b2821.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '누구하나' AND address = '제주특별자치도 제주시 애월읍 수산서4길 3 (수산리) 누구하나');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오크하우스', 'CAMPING', 'WEST', '제주특별자치도 제주시 애월읍 하가로 13', 33.4661432, 126.3582215, '일상을 벗어나 제주에서 숙박과 캠핑을 동시에 즐길수 있는 이색공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202203/04/cefe4e69-1357-4748-a197-c3c5ac847c2d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오크하우스' AND address = '제주특별자치도 제주시 애월읍 하가로 13');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '유수암캠핑장', 'CAMPING', 'WEST', '제주특별자치도 제주시 애월읍 하소로 684-25', 33.4223140, 126.3939899, '유수암캠핑장은 애월읍 유수암의 한적한 공간에 위치한 캠핑장이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202212/15/65bbc2ae-81d0-4a42-980b-84d00e73148b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '유수암캠핑장' AND address = '제주특별자치도 제주시 애월읍 하소로 684-25');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 노꼬메 펜션 캠핑장', 'CAMPING', 'WEST', '제주특별자치도 제주시 애월읍 유수암평화10길 175-78 (유수암리)', 33.4102354, 126.4128650, '제주 애월리 노꼬메오름 자락의 조용하고 쾌적한 숲속 낙원, 아름다운 석양과 푸른 바다를 껴안은 힐링캠프', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202501/24/89e32620-1b7e-4323-86d7-11a4ccc91a79.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 노꼬메 펜션 캠핑장' AND address = '제주특별자치도 제주시 애월읍 유수암평화10길 175-78 (유수암리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '캠핑트리펜션', 'CAMPING', 'WEST', '제주특별자치도 제주시 애월읍 광상로 115', 33.4108019, 126.4331640, '애월읍에 위치한 자연이 숨쉬는 통나무 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202104/22/1727d58d-3165-4268-9451-b41690d5242e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '캠핑트리펜션' AND address = '제주특별자치도 제주시 애월읍 광상로 115');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '고불락하우스', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 애월읍 고내로7길 45-12', 33.4667400, 126.3366550, '골목에 위치한 숨바꼭질 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/dab97919-0b99-4ccf-8178-8d7c9eb76a80.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '고불락하우스' AND address = '제주특별자치도 제주시 애월읍 고내로7길 45-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '곰씨비씨게스트하우스', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 안덕면 난드르로21번길 13-7', 33.2341420, 126.3671340, '정겨운 개나리같은 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/dc22efd4-0ec2-42d8-9bf7-68c80768e790.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '곰씨비씨게스트하우스' AND address = '제주특별자치도 서귀포시 안덕면 난드르로21번길 13-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '구구호스텔', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 안덕면 일주서로 1613-83', 33.2499784, 126.3411414, '제주 남쪽, 하루 단 7인을 위한 평화로운 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202606/10/507b786d-44f1-4aa4-9de2-7bcd82ab9391.JPG', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '구구호스텔' AND address = '제주특별자치도 서귀포시 안덕면 일주서로 1613-83');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '금서방네이층집', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 안덕면 덕수회관로 61번길 1', 33.2538260, 126.3102800, '덕수리 마을의 금 서방네 이층집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2f2243a7-f26e-49bc-818c-06bb39a8ee28.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '금서방네이층집' AND address = '제주특별자치도 서귀포시 안덕면 덕수회관로 61번길 1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '꿀잠자리', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 대정읍 최남단해안로30번길 14', 33.2181240, 126.2509600, '모슬포항 내 위치한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0fbc75e7-66a3-41df-87a8-8ef76d63d2f3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '꿀잠자리' AND address = '제주특별자치도 서귀포시 대정읍 최남단해안로30번길 14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '나무처럼 게스트하우스', 'GUESTHOUSE', 'WEST', '제주시 한림읍 홍수암로 3-1', 33.3851200, 126.2265300, '협재 근처의 카페&펍이 있는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7bdfacc4-797a-497f-bc72-ebd100ba3b8f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '나무처럼 게스트하우스' AND address = '제주시 한림읍 홍수암로 3-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '네모스테이', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 한림읍 한림로 394', 33.3971609, 126.2447593, '협재에 위치한 프리미엄 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/340d8479-c265-4b37-817e-418d828a979c.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '네모스테이' AND address = '제주특별자치도 제주시 한림읍 한림로 394');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '뉴코리아유스호스텔', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 애월읍 하소로 769-15', 33.4168955, 126.3982975, '청소년 및 일반단체가 공유할 수 있는 건전한 단체활동 공간 숙박시설', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ce810fcb-37fe-455e-8c80-ae0de9e9f5bc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '뉴코리아유스호스텔' AND address = '제주특별자치도 제주시 애월읍 하소로 769-15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '늘푸른 쉼 스테이', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 대정읍 하모백사로 29', 33.2142200, 126.2591600, '해모 해변 인근에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/91c07f60-b90d-4421-a031-af3d66e5e090.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '늘푸른 쉼 스테이' AND address = '제주특별자치도 서귀포시 대정읍 하모백사로 29');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '레몬트리게스트하우스', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 대정읍 하모항구로 70', 33.2196040, 126.2513900, '4개 국어가 가능한 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a2bb8be1-3841-4a64-9871-850d9d236154.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '레몬트리게스트하우스' AND address = '제주특별자치도 서귀포시 대정읍 하모항구로 70');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '루시드봉봉', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 대정읍 상모로 200-8', 33.2260250, 126.2673700, '밤하늘 달빛 아래 반짝이는 문화가 있는 게스트 하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/bf24a976-7db0-496f-b871-78c8437e5a03.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '루시드봉봉' AND address = '제주특별자치도 서귀포시 대정읍 상모로 200-8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '마이테르유스호스텔', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 애월읍 애원로 474-29', 33.4290068, 126.3475723, '애월읍에 위치한 숙박시설이자 복합청소년 교류공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2ae9ddea-e767-4251-bd31-1c5e5fc6001f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '마이테르유스호스텔' AND address = '제주특별자치도 제주시 애월읍 애원로 474-29');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '버튼포레스트', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 한경면 고조로 311-8', 33.3195720, 126.2092200, '나눔으로 마음이 따뜻해지는 집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/90447a2c-0aa1-4fb6-88c7-f3f18a0b84bd.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '버튼포레스트' AND address = '제주특별자치도 제주시 한경면 고조로 311-8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '북마크게스트하우스', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 대정읍 일과대수로11번길 16-8', 33.2387350, 126.2315800, '제주 서쪽 노을이 예쁜 조용한 시골 마을에 위치한 제주 전통 돌집을 개조하여 책을 테마로 꾸며진 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b43eeaeb-bb6b-43cf-bf3f-d68d96be97c5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '북마크게스트하우스' AND address = '제주특별자치도 서귀포시 대정읍 일과대수로11번길 16-8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '뿌리', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 한경면 용수1길 3', 33.3205785, 126.1778439, '스님이라 불리는 사장님', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a32392d6-0b4f-48b6-b971-46202381d95c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '뿌리' AND address = '제주특별자치도 제주시 한경면 용수1길 3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '알랑가게스트하우스', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 안덕면 사계남로153번길 2', 33.2288607, 126.3065651, '관광지와 가깝고 조망이 좋은 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/344d2503-b715-481d-b115-cb36cf2e51bc.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '알랑가게스트하우스' AND address = '제주특별자치도 서귀포시 안덕면 사계남로153번길 2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '인제주게스트하우스', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 한림읍 명월성로 137-14', 33.3960900, 126.2645200, '멋스러운 한적함이 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0b246c42-5dca-4855-ae37-3a04044b23bf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '인제주게스트하우스' AND address = '제주특별자치도 제주시 한림읍 명월성로 137-14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주게스트하우스 하쿠나마타타', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 애월읍 고내북동길 17', 33.4673040, 126.3408050, '제주시 애월 올레 15코스 종점, 16코스 시작점에 위치한 게스트 하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/33c64ac5-ea19-41b3-b934-260f4f20b8c9.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주게스트하우스 하쿠나마타타' AND address = '제주특별자치도 제주시 애월읍 고내북동길 17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주도 애월 파티하는 오누 게스트하우스', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 애월읍 애월로2길 7', 33.4628600, 126.3141400, '소등이 없는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f75b04ba-5d02-4c1a-b38c-85b707d1fa9e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주도 애월 파티하는 오누 게스트하우스' AND address = '제주특별자치도 제주시 애월읍 애월로2길 7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주도 조용한 게스트하우스 산방산점', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 안덕면 덕수회관로87번길 34', 33.2533852, 126.3048762, '1인 여행객만 예약 가능한 1인 전용 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/11/f6a23600-88bb-4090-a351-910f9ad19c7e.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주도 조용한 게스트하우스 산방산점' AND address = '제주특별자치도 서귀포시 안덕면 덕수회관로87번길 34');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주도게스트하우스 협재정류장', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 한림읍 한림로 391', 33.3973869, 126.2441639, '제주도 감성한옥에서 즐기는 여행자들의 휴식과 소통을 위한 전통적인 방식의 게스트하우스입니다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202604/21/39be0f0c-af2f-4c40-96b1-34f2f5476bf8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주도게스트하우스 협재정류장' AND address = '제주특별자치도 제주시 한림읍 한림로 391');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주소소', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 애월읍 소길남길 190-18', 33.4170900, 126.3749850, '동물체험까지 가능한 다재다능 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/71ebccbe-a950-4517-a810-b6a60da65184.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주소소' AND address = '제주특별자치도 제주시 애월읍 소길남길 190-18');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주유스호스텔', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 애월읍 방축길 128', 33.3857737, 126.3261090, '인성교육및 야외 활동을 위한 단체 맞춤형 유스호스텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201909/04/4275a728-e1b5-4716-a672-255ecf267441.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주유스호스텔' AND address = '제주특별자치도 제주시 애월읍 방축길 128');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '지니코티지', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 한경면 수동1길 15', 33.3381000, 126.2363400, '물들지 않은 자연 그대로의 느림이 있는 작은 별장', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0a3471c0-37f8-488c-8935-8cad4f23e284.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '지니코티지' AND address = '제주특별자치도 제주시 한경면 수동1길 15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '초록초록', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 한림읍 명재로 17', 33.3807357, 126.2482491, '협재해수욕장에 위치한 게스트하우스 & 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/17d5e7c1-89ff-4489-8483-6da0b04e3a6e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '초록초록' AND address = '제주특별자치도 제주시 한림읍 명재로 17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '코즈게스트하우스', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 안덕면 동광로100번길 20 (동광리) 코즈게스트하우스', 33.3045040, 126.3384700, '제주 서쪽 중심에 위치한 호텔식 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/01/ca9d2ca4-a66b-43e0-afef-e4a4ed078807.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '코즈게스트하우스' AND address = '제주특별자치도 서귀포시 안덕면 동광로100번길 20 (동광리) 코즈게스트하우스');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '탱자탱자게스트하우스', 'GUESTHOUSE', 'WEST', '제주특별자치도 제주시 애월읍 애월로9길 69', 33.4662000, 126.3183700, '365일 재미가 있는 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3f2342f9-218b-44ca-aa15-eac07a2f606f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '탱자탱자게스트하우스' AND address = '제주특별자치도 제주시 애월읍 애월로9길 69');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하랑게스트하우스', 'GUESTHOUSE', 'WEST', '제주특별자치도 서귀포시 대정읍 일과로13번길 1', 33.2328000, 126.2437000, '모슬포항에서 도보로 10~15분 거리에 위치 해 있으며 전용 카페가 완비 된 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d6061796-4253-4fd0-b0d6-3b8c733c9a83.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하랑게스트하우스' AND address = '제주특별자치도 서귀포시 대정읍 일과로13번길 1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '활엽수 게스트하우스', 'GUESTHOUSE', 'WEST', '제주 서귀포시 상모대서로 20번길 44', 33.2253460, 126.2613140, '제주의 옛집을 주인 부부가 손수 고쳐 만든 작은 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3e33d31b-f618-48bc-b5ae-9ea79c44d2ba.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '활엽수 게스트하우스' AND address = '제주 서귀포시 상모대서로 20번길 44');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '금능 이큐 스테이 호텔', 'HOTEL', 'WEST', '제주특별자치도 제주시 한림읍 금능남1길 6-1', 33.3854206, 126.2298086, '금능 이큐 스테이 호텔은 금능해수욕장 5분거리에 위치하여 반려동물과 물놀이도 즐기고 캠핑 감성까지 느낄 수 있는 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/4c1834fc-00f4-4e18-90a3-34704cd896bc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '금능 이큐 스테이 호텔' AND address = '제주특별자치도 제주시 한림읍 금능남1길 6-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '나비스호텔', 'HOTEL', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 885', 33.4862269, 126.3916298, '애월 해안도로 및 올레길 16코스에 위치한 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/70a97017-1f22-4f8b-a524-f7d6057468ef.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '나비스호텔' AND address = '제주특별자치도 제주시 애월읍 애월해안로 885');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '다인오세아노 호텔', 'HOTEL', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 394', 33.4724976, 126.3505747, '제주도 애월의 아름다운 바다와 올레길을 품고있는 다인오세아노호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201807/17/55e67d0c-d348-405f-a19e-cbecf32a15ee.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '다인오세아노 호텔' AND address = '제주특별자치도 제주시 애월읍 애월해안로 394');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '디아넥스 호텔', 'HOTEL', 'WEST', '제주특별자치도 서귀포시 안덕면 산록남로762번길 71', 33.3034740, 126.3920600, '드넓은 제주의 자연을 품은 컨템포러리 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202503/04/4a0fbc34-e9eb-4e71-826f-cf873e613fbc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '디아넥스 호텔' AND address = '제주특별자치도 서귀포시 안덕면 산록남로762번길 71');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라림부띠끄호텔', 'HOTEL', 'WEST', '제주특별자치도 서귀포시 안덕면 대평로 39', 33.2369300, 126.3630450, '현대적인 인테리어와 환상적인 제주 바다 전망을 즐길 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d60a8449-4e46-4278-8228-095e0eded86a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라림부띠끄호텔' AND address = '제주특별자치도 서귀포시 안덕면 대평로 39');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '리치호텔', 'HOTEL', 'WEST', '제주특별자치도 제주시 애월읍 고내로13길 100', 33.4721680, 126.3490700, '제주 리치 호텔은 곽지과물해변에서 차로 15분 거리에 있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/52641214-46ec-45c9-b616-4a87c45690be.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '리치호텔' AND address = '제주특별자치도 제주시 애월읍 고내로13길 100');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '머큐어 앰배서더 제주', 'HOTEL', 'WEST', '제주특별자치도 서귀포시 안덕면 한창로 365', 33.2928278, 126.3476318, '아름다운 제주 곶자왈에 둘러싸인 친환경 그린키 호텔, ''머큐어 앰배서더 제주''', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202408/20/d2a6328a-8e45-4a30-a7c9-b911a1131d47.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '머큐어 앰배서더 제주' AND address = '제주특별자치도 서귀포시 안덕면 한창로 365');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '베니키아호텔제주', 'HOTEL', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 554-6', 33.4777407, 126.3624077, '애월하귀 해안도로 중심지에 위치한 내 집에 온 것처럼 편안한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1ca1d3de-bb1a-4c25-9714-f1fefa819372.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '베니키아호텔제주' AND address = '제주특별자치도 제주시 애월읍 애월해안로 554-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '산방산호텔', 'HOTEL', 'WEST', '제주특별자치도 서귀포시 대정읍 일주서로 2093', 33.2495906, 126.2932647, '산방산과 송악산을 아우르는 사계해안과 가파도, 마라도 선착장이 있는 모슬포에 인접한 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/453d26b0-2252-4c43-b06b-d1a7c374550d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '산방산호텔' AND address = '제주특별자치도 서귀포시 대정읍 일주서로 2093');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '수앤수가족호텔', 'HOTEL', 'WEST', '제주시 애월읍 부룡수길 60', 33.4663350, 126.3688900, '다양한 객실과 부대시설을 갖춘 가족형 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/f1e873e6-4a61-42e6-ae7d-64b52be1703d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '수앤수가족호텔' AND address = '제주시 애월읍 부룡수길 60');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '씨스테이호텔', 'HOTEL', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 255', 33.4675271, 126.3391440, '애월읍 해안도로 고내포구에 위치한 씨스테이 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ea9670dc-4680-42b1-9b12-85ae9a75e726.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '씨스테이호텔' AND address = '제주특별자치도 제주시 애월읍 애월해안로 255');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '애월 베누스 무인호텔', 'HOTEL', 'WEST', '제주특별자치도 제주시 애월읍 유수암평화5길 92', 33.4205930, 126.4063000, '베누스무인텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a5e37690-f693-41fa-8f2c-88f68c8ebafd.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '애월 베누스 무인호텔' AND address = '제주특별자치도 제주시 애월읍 유수암평화5길 92');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '에쿠스모텔', 'HOTEL', 'WEST', '제주특별자치도 서귀포시 안덕면 화순중앙로54번길 5', 33.2472150, 126.3353650, '객실과 욕실을 넓게 꾸민 가족단위 관광모텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5dfc3fd4-431f-476c-97c0-6c8a4a589faa.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '에쿠스모텔' AND address = '제주특별자치도 서귀포시 안덕면 화순중앙로54번길 5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '유니호텔제주', 'HOTEL', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 656', 33.4794113, 126.3732685, '제주 유니호텔은 제주에서 가장 아릅답기로 유명한 애월 해안도로에서만 느끼실 수 있는 석양을 모티브로 하여 제주의 푸른바다와 아름다운 한라산이 마주보고 있는 구엄리에 2015년 3월 개관하였습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/638509a5-b742-4f9c-a1e1-9bff8b4a0625.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '유니호텔제주' AND address = '제주특별자치도 제주시 애월읍 애월해안로 656');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주시드니호텔', 'HOTEL', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 943', 33.4836770, 126.3958360, '애월해안도로 곽지해변 인접 오션뷰 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ffa1e856-64e0-4078-8f15-d236fe2f8727.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주시드니호텔' AND address = '제주특별자치도 제주시 애월읍 애월해안로 943');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주항공우주호텔', 'HOTEL', 'WEST', '제주도 서귀포시 안덕면 녹차분재로 216', 33.3021550, 126.2946850, '곶자왈과 푸른 녹차 밭 전경이 한 눈에', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c37962fb-25f5-46e5-9829-cc7305a6408c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주항공우주호텔' AND address = '제주도 서귀포시 안덕면 녹차분재로 216');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '코자호텔', 'HOTEL', 'WEST', '제주특별자치도 서귀포시 대정읍 최남단해안로 44', 33.2166491, 126.2522091, '제주 천연 화산 해수 스파를 테마로 한 부티끄 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/19/6ad779cc-7f72-496d-81c0-f687735d7e5b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '코자호텔' AND address = '제주특별자치도 서귀포시 대정읍 최남단해안로 44');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '코쿤호텔', 'HOTEL', 'WEST', '제주특별자치도 제주시 애월읍 중엄3길 50', 33.4779996, 126.3716952, '관광호텔로 객실이 30개를 보유하고 있으며 전 객실 바다전망 리모델링 완료.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/ba65380e-aa3a-4020-be91-1eaa9d092a3f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '코쿤호텔' AND address = '제주특별자치도 제주시 애월읍 중엄3길 50');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '핀크스 포도호텔', 'HOTEL', 'WEST', '제주특별자치도 서귀포시 안덕면 산록남로 863', 33.3159980, 126.3876400, '알알이 맺힌 제주의 아름다움', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202503/05/16c8533a-d400-42fe-bf0b-99dbc6fa84ff.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '핀크스 포도호텔' AND address = '제주특별자치도 서귀포시 안덕면 산록남로 863');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '호텔 스카브로', 'HOTEL', 'WEST', '제주특별자치도 서귀포시 예래해안로 15', 33.2333750, 126.3701510, '서귀포시 하예동에 위치하고 있는 스파&풀빌라 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/0810225c-0d81-4eb9-9725-555f590f3e48.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '호텔 스카브로' AND address = '제주특별자치도 서귀포시 예래해안로 15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'Hi제주호텔', 'HOTEL', 'WEST', '제주특별자치도 제주시 한림읍 일주서로 5125', 33.3917167, 126.2539423, '바베큐, 수영장 등 다양한 부대시설이 갖춰져 있는 호텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/0686c892-2723-4a99-8ccc-f153a6efce4a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'Hi제주호텔' AND address = '제주특별자치도 제주시 한림읍 일주서로 5125');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '곁겹', 'OTHER', 'WEST', '제주특별자치도 제주시 한림읍 한림로 224-6', 33.3860279, 126.2319063, '일상의 곁에서 쌓은 추억의 겹', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202211/14/4b6bb911-bd87-4891-bf5c-72f1dbcab5db.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '곁겹' AND address = '제주특별자치도 제주시 한림읍 한림로 224-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '고산별곡', 'OTHER', 'WEST', '제주특별자치도 제주시 한경면 고산로2길 6-3', 33.3028612, 126.1803361, '살다 보면 한 번쯤, 자발적 유배', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202211/14/b1ebb691-986f-4ea5-9487-d66bfc55b9cc.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '고산별곡' AND address = '제주특별자치도 제주시 한경면 고산로2길 6-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '빌라사계', 'OTHER', 'WEST', '제주특별자치도 서귀포시 안덕면 사계북로 157-20', 33.2397811, 126.3046261, '농도 짙은 쉼을 제공하는 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202211/14/cd96d154-a106-471c-8730-b7afc684fef4.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '빌라사계' AND address = '제주특별자치도 서귀포시 안덕면 사계북로 157-20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '산방산파크텔', 'OTHER', 'WEST', '제주특별자치도 서귀포시 안덕면 화순서서로 63', 33.2564742, 126.3292550, '산방산과 바다의 경치가 한눈에 보이는 중문에 위치한 제주 산방산 정원이다. 바쁜일상을 잠시 잊고, 여러분의 행복한 추억과 행복을 남겨주는 휴식처이다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202301/11/20f63fae-f940-4edb-96ff-69b611c810b4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '산방산파크텔' AND address = '제주특별자치도 서귀포시 안덕면 화순서서로 63');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '썬 웰니스 캠프', 'OTHER', 'WEST', '제주특별자치도 제주시 애월읍 광령평화7길 24 (광령리)', 33.4034138, 126.4508866, '제주도에서 18년동안 힐링 & 다이어트 프로그램을 진행하고 있는 웰니스 캠프', 'https://api.cdn.visitjeju.net/thumbnail/photomng/imgpath/202405/09/f8ca7d7c-e195-4ad6-9913-46cc368488ac.jpg', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '썬 웰니스 캠프' AND address = '제주특별자치도 제주시 애월읍 광령평화7길 24 (광령리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아템파우제', 'OTHER', 'WEST', '제주특별자치도 제주시 한림읍 월림8길 21', 33.3503941, 126.2576876, '세 가족이 즐길 수 있는 2층 단독주택', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/05/0ce29e27-f3af-432a-953c-18b9be407857.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아템파우제' AND address = '제주특별자치도 제주시 한림읍 월림8길 21');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '에메랄드하우스', 'OTHER', 'WEST', '제주특별자치도 서귀포시 안덕면 신화역사로 545-66', 33.3154941, 126.3488060, '제주의 드넓은 자연을 제대로 즐길 수 있는 이곳은 반려동물과 동반하는 여행이라면 더할 나위 없는 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/897443c1-aa2c-4419-be38-16da66a5b1e6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '에메랄드하우스' AND address = '제주특별자치도 서귀포시 안덕면 신화역사로 545-66');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오롯 바이 에퀴녹스', 'OTHER', 'WEST', '제주특별자치도 제주시 한림읍 금능길 75', 33.3890150, 126.2309630, '금능 해수욕장을 품고 있는 금능리에 위치한 오롯 바이 에퀴녹스는 반려동물과 즐거운 추억을 만들기에 좋은 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/492cdb63-5b53-4ada-84d5-854bd3079d4f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오롯 바이 에퀴녹스' AND address = '제주특별자치도 제주시 한림읍 금능길 75');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '옵데가 바이 에퀴녹스', 'OTHER', 'WEST', '제주특별자치도 제주시 한림읍 협재10길 4', 33.3985800, 126.2466933, '옵데가 바이 에퀴녹스는 협재해수욕장을 도보로 이용할 수 있는 곳에 있어 반려동물과 물놀이를 즐기고 함께 하룻밤을 편안하게 보낼 수 있는 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/9632f00b-1da9-4157-ba03-81f7945122a4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '옵데가 바이 에퀴녹스' AND address = '제주특별자치도 제주시 한림읍 협재10길 4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '월령지헌', 'OTHER', 'WEST', '제주특별자치도 제주시 한림읍 월령1길 13-5', 33.3754283, 126.2156947, '자연의 질감을 품은 달의 집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202211/14/240e7c82-b919-4a8a-9f77-dd7e0b35e509.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '월령지헌' AND address = '제주특별자치도 제주시 한림읍 월령1길 13-5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주애설레다', 'OTHER', 'WEST', '제주특별자치도 제주시 한경면 고조로 492-4', 33.3294047, 126.2216605, '제주에서 노을이 가장 아름다운 서쪽마을 한경면에 위치한 제주애설레다는 노란빛 외벽의 인트로한 느낌을 담은 제주 감성숙소다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/7d18f0e0-bdab-4ef7-bef2-9d59b306c6e6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주애설레다' AND address = '제주특별자치도 제주시 한경면 고조로 492-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '강남하우스펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 대정읍 형제해안로 316', 33.2071950, 126.2903500, '낭만과 추억이 가득한 아름다운 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7d90ba92-ee0d-476c-a563-b76a9b69c903.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '강남하우스펜션' AND address = '제주특별자치도 서귀포시 대정읍 형제해안로 316');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '거드락지민박', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 한림로 350', 33.3944780, 126.2413940, '단 둘만을 위한 소라집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c71bacd8-fa12-4fc5-8404-2c9ff85a5991.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '거드락지민박' AND address = '제주특별자치도 제주시 한림읍 한림로 350');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '구름비낭펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 판포2길 6-14', 33.3634307, 126.1976976, '제주 전통집의 재해석, 온전히 나만의 휴식공간이 되는 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/62122d36-a966-4ba2-8a6c-a727602748ac.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '구름비낭펜션' AND address = '제주특별자치도 제주시 한경면 판포2길 6-14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '그리하오', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 주가흘길 34 (저지리)', 33.3210213, 126.2744803, '제주오름과 한라산 그리고 귤밭을 보며 머무는 휴식 스테이''그리하오''', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202509/19/0eb8d237-7087-4790-b7d2-d21a2e60d9c7.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '그리하오' AND address = '제주특별자치도 제주시 한경면 주가흘길 34 (저지리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '글라라의집', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계남로 211', 33.2320940, 126.3087000, '산방산과 용머리해안 근처 2015년 최우수 지오하우스(지질체험숙소)', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/45ece939-cf58-4b3d-867b-c80ea0bccfd6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '글라라의집' AND address = '제주특별자치도 서귀포시 안덕면 사계남로 211');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '꽃머채 펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 소길남길 202-4', 33.4194584, 126.3835180, '애월 소길에 위치한 아름다운 정원이 있는 통나무집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e29f7d3a-3a90-4ab9-8e9d-6c251eff9556.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '꽃머채 펜션' AND address = '제주특별자치도 제주시 애월읍 소길남길 202-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '꽃향기바다소리', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 판조로 29', 33.3656000, 126.2066300, '조용하고 아늑한 꽃향기가 날리는 바다소리 독채형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/afa81c25-487e-478b-8717-1447f80dbe22.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '꽃향기바다소리' AND address = '제주특별자치도 제주시 한경면 판조로 29');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '끌림 토리', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 고내로7길 10-2', 33.4641300, 126.3388440, '옛 제주 집의 정서와 향기를 보존한 곳', '', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '끌림 토리' AND address = '제주특별자치도 제주시 애월읍 고내로7길 10-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '나인스파빌', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 하광로 518', 33.4454230, 126.4303404, '먼 바다가 보이며 지대가 높아 탁트인 시원한 전망과 주변 수목과 돌담으로 어우러져 조용하고, 건물 내/외부가 모던하고 깔끔한 컨셉의 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4316a0aa-1b56-46e1-913c-e39524ec4a7e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '나인스파빌' AND address = '제주특별자치도 제주시 애월읍 하광로 518');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '난드르통나무집', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 대평로 36', 33.2370870, 126.3639164, '친환경 난드르통나무집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8becb506-d442-4832-9a04-4d637003d519.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '난드르통나무집' AND address = '제주특별자치도 서귀포시 안덕면 대평로 36');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '노을과원담', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 한경해안로 282', 33.3318117, 126.1662152, '노을이 멋진 신창풍차해안도로와 가까운 아담한 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/939a1aa2-6099-4255-9b4e-766f314c5134.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '노을과원담' AND address = '제주특별자치도 제주시 한경면 한경해안로 282');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '노을담은뜨락', 'PENSION', 'WEST', '제주특별자치도 서귀포시 대정읍 노을해안로 416', 33.2702640, 126.2004199, '환상적인 제주바다의 전망을 한눈에 담을 수 있는곳.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c7c77239-ee07-47ad-9df7-9f015c12ab6a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '노을담은뜨락' AND address = '제주특별자치도 서귀포시 대정읍 노을해안로 416');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '느르왓', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 고산중길 23', 33.4996220, 126.5311900, '제주도 자연석으로 지어진 돌집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201809/04/74c8b4a6-2306-4119-9f33-a7c3c9d79d01.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '느르왓' AND address = '제주특별자치도 제주시 한경면 고산중길 23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '다래산장펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 납읍로10길 49-32', 33.4305880, 126.3479160, '애월읍에 위치한 황토와 통나무로 지어진 산장', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2a81a369-6221-4b9d-bb03-1cd6f093b477.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '다래산장펜션' AND address = '제주특별자치도 제주시 애월읍 납읍로10길 49-32');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '단빌리지민박', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 고락로 100', 33.3021310, 126.1901501, '1인실부터 가족실까지 다양한 크기의 게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1923268f-73fc-4bdb-9674-b1fccfc3a0ea.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '단빌리지민박' AND address = '제주특별자치도 제주시 한경면 고락로 100');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '달자펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 738', 33.4835320, 126.3799440, '바다 위를 달리는 자전거', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/17/30bde26f-47cc-4391-bfd3-2254bbb29230.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '달자펜션' AND address = '제주특별자치도 제주시 애월읍 애월해안로 738');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '달코롬', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 덕수서로35번길 18-9', 33.2548777, 126.3030495, '귤따기 무료체험숙소, 프라이빗, 감성숙소, 서귀포숙소, 산방산, 오설록, 사계해변 숙소.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202604/25/de583724-11a9-4043-9f0e-d1b2356061e2.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '달코롬' AND address = '제주특별자치도 서귀포시 안덕면 덕수서로35번길 18-9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '달콤한소금만들기', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계로 100', 33.2393511, 126.2999471, '산방산과 용머리해안이 가까운 목조 주택', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7fa9ce2b-00d5-439a-a44c-7eb7a25386a7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '달콤한소금만들기' AND address = '제주특별자치도 서귀포시 안덕면 사계로 100');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '돌담민박', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 하귀12길 18', 33.4861200, 126.4092200, '낚시 체험도 가능한 제주 토속 민박집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202302/13/b2adfc02-8e90-4dba-b863-7ba984330ce5.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '돌담민박' AND address = '제주특별자치도 제주시 애월읍 하귀12길 18');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '동막골펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 서광서문로 55-7', 33.2835700, 126.3232040, '제주를 담은 아름다운 정원이 있는 동막골게스트하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6ff84a98-975c-4670-9fc2-d48d2eaf6ac1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '동막골펜션' AND address = '제주특별자치도 서귀포시 안덕면 서광서문로 55-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '디어마이프렌즈 제주', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 청수로 14', 33.3203758, 126.2452952, '디어마이프렌즈는 조용하면서도 한적한 청수리 마을에 위치하여 반려동물과 오래오래 함께하고, 행복한 시간을 보낼 수 있도록 반려동물을 위한 사장님의 따뜻한 마음의 손길이 펜션 곳곳에 닿아있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/17/696ac351-b934-4991-bbb9-1e4b052be944.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '디어마이프렌즈 제주' AND address = '제주특별자치도 제주시 한경면 청수로 14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '디오션힐 펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계북로 208', 33.2367000, 126.3089400, '산방산에 인접한 바다조망이 좋은 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a23adc31-12fb-4077-a66e-31645df89f3c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '디오션힐 펜션' AND address = '제주특별자치도 서귀포시 안덕면 사계북로 208');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '또오크라', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 수산북길 31', 33.4699637, 126.3907147, '''다시 오겠다''는 뜻의 제주어로 이름 지어진 한라산이 보이는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f981c8b9-b8a4-4ca4-9fe0-0918ee9717e8.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '또오크라' AND address = '제주특별자치도 제주시 애월읍 수산북길 31');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라신비', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 한림해안로 548', 33.4378031, 126.2755954, '귀덕 해안도로에 위치한 오션뷰 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/06/32af3ced-11e1-492d-972c-ac45bc233831.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라신비' AND address = '제주특별자치도 제주시 한림읍 한림해안로 548');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라이온힐펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 평화로 507', 33.2813513, 126.3191078, '산방산과 송악해변이 아름답게 펼쳐지는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/65c1281c-07b3-4851-aced-46bbc9db612c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라이온힐펜션' AND address = '제주특별자치도 서귀포시 안덕면 평화로 507');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라파로마휴양펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 협재로 62', 33.3923816, 126.2473264, '제주 협재해수욕장 인근에 위치하여 바다 전망을 즐길 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b0b0cbc0-8bd9-4808-af19-f4c2edfd7800.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라파로마휴양펜션' AND address = '제주특별자치도 제주시 한림읍 협재로 62');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '로그맨하우스', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 소길남길 179', 33.4194584, 126.3835180, '제주경마장쪽에 위치해 있는 로그맨하우스 펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7e036091-a0fa-4906-8f0c-2761fa27cfd1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '로그맨하우스' AND address = '제주특별자치도 제주시 애월읍 소길남길 179');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '로그밸리', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 소길남길 190-19', 33.4169117, 126.3756972, '무료승마체험을 할 수 있는 통나무집 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0b53f905-d549-4fc8-957d-2ed3c5a926d3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '로그밸리' AND address = '제주특별자치도 제주시 애월읍 소길남길 190-19');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '루시드엠 펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계북로 120', 33.2368500, 126.3005500, '루시드엠은 사계해안도로에 위치해있으며 산방산 전망이 가능한 리조트다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5f14cdb3-762c-4b03-b87e-7d17b07bac63.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '루시드엠 펜션' AND address = '제주특별자치도 서귀포시 안덕면 사계북로 120');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '리벤시아 스테이 제주', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 월령안길 5-4', 33.3770103, 126.2153865, '3대째 거주해온 전통가옥을 현대적으로 리모데릴ㅇ하여 전통의 아름다움과 현대적 편리함을 동시에 제공하는 감성숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202504/17/2ab9db65-3ab8-40a7-a868-26e534c1844e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '리벤시아 스테이 제주' AND address = '제주특별자치도 제주시 한림읍 월령안길 5-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '마눌펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 고내봉서2길 73', 33.4493108, 126.3325117, '스몰 웨딩과 감귤 체험 그리고 아이들이 넓은 잔디밭을 뛰어놀 수 있는 이색적인 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201910/24/c859be45-cd08-4ec2-a2ba-865eb07cc4cc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '마눌펜션' AND address = '제주특별자치도 제주시 애월읍 고내봉서2길 73');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '마레카펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 금능1길 26', 33.3880393, 126.2248884, '제주도 독채펜션 마레카는 제주의 역사를 간직한 제주의 농가주택인 돌담집으로 된 독채입니다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4eb7fe5b-f821-4c4b-ad2f-5bd1cba48c2a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '마레카펜션' AND address = '제주특별자치도 제주시 한림읍 금능1길 26');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '머무름펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 명재로 61-5', 33.3945611, 126.2533684, '여기, 아름다운 제주에서 자연과 함께 편히 머무를 수 있는 민박이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202006/25/5df51059-d697-4bb3-8926-cb58ca7eed13.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '머무름펜션' AND address = '제주특별자치도 제주시 한림읍 명재로 61-5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '모물펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 곽지9길 4', 33.4523500, 126.3082900, '포근함 그리고 쉼', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8fe0614b-4861-4107-86bb-fa9b003ce705.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '모물펜션' AND address = '제주특별자치도 제주시 애월읍 곽지9길 4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '몬떼뷰 스테이', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계남로 75-9', 33.2301700, 126.3018100, '산방산과 아름다운 사계 바다 조망이 좋은 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e4d63035-9027-4c43-aab9-e79b39302aae.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '몬떼뷰 스테이' AND address = '제주특별자치도 서귀포시 안덕면 사계남로 75-9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '몽마르뜨펜션', 'PENSION', 'WEST', '제주도 제주시 애월읍 애월해안로 384-23', 33.4710300, 126.3506100, '하귀해안도로 근방에 위치한 전 객실 바다전망인 프로방스풍 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/10feb870-a313-4b05-9ce1-73d19f3ca527.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '몽마르뜨펜션' AND address = '제주도 제주시 애월읍 애월해안로 384-23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '물메랑', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 수산북길 3', 33.4721057, 126.3913346, '수산저수지와 한라산과 바다의 아름다운 경치가 한 눈에 들어오는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9db5eaf2-b62a-4c8c-9d12-d64e0038338b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '물메랑' AND address = '제주특별자치도 제주시 애월읍 수산북길 3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '미르빌펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계로114번길 87', 33.2460023, 126.3060521, '자연과 함께하는 휴양형펜션 & 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3d9fc491-3f25-45c4-8c63-bdfae6d0a220.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '미르빌펜션' AND address = '제주특별자치도 서귀포시 안덕면 사계로114번길 87');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '미선이네집', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계남로153번길 32-4', 33.2311619, 126.3060242, '제주도 산방산 유채꽃밭에 위치한 제주도 가족 여행을 위한 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202603/12/3b04a316-8a6f-4c96-bd2a-eb73fe273782.jpg', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '미선이네집' AND address = '제주특별자치도 서귀포시 안덕면 사계남로153번길 32-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '민박집섶낭', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 협재2길 41', 33.3939620, 126.2390984, '협재해수욕장 인근에서 한적한 전원생활을 즐길 수 있는 2층 별채 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201901/18/f3337ec8-39d5-4590-bbc3-7868992f707e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '민박집섶낭' AND address = '제주특별자치도 제주시 한림읍 협재2길 41');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바다스케치', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계남로216번길 52', 33.2350000, 126.3099800, '직접 설계하고 지은 바다 위 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/aa25c1dc-5045-40b6-b28c-635c2dc56bb5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바다스케치' AND address = '제주특별자치도 서귀포시 안덕면 사계남로216번길 52');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바다와자전거', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 고내로9길 42', 33.4666671, 126.3382031, '애월해안로 올레길 15코스 종점 16코스 시작점에 있는 농어촌민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201910/28/9a13a93b-8589-4d19-962c-2ab8d4b414aa.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바다와자전거' AND address = '제주특별자치도 제주시 애월읍 고내로9길 42');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바다의향기', 'PENSION', 'WEST', '제주특별자치도 서귀포시 대정읍 송악관광로177번길 254-6', 33.2130528, 126.2922680, '낭만과 추억이 가득한 아름다운 바다의 향기 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a3902d24-8a73-42c9-ab86-62142b91687b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바다의향기' AND address = '제주특별자치도 서귀포시 대정읍 송악관광로177번길 254-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바다풍경펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 923', 33.4839152, 126.3939136, '애월 해변 근교에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/48797475-9233-4f03-b349-a6ac7b800cbe.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바다풍경펜션' AND address = '제주특별자치도 제주시 애월읍 애월해안로 923');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바닷가하우스펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 대정읍 형제해안로 254', 33.2122165, 126.2921599, '산방산과 송악산의 맑은 공기가 숨쉬는 마을', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/09e8485e-0427-4fa3-83c7-1ad26a9cb2b5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바닷가하우스펜션' AND address = '제주특별자치도 서귀포시 대정읍 형제해안로 254');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바당정원펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 판조로 23', 33.3660110, 126.2065075, '제주바다가 한눈에 펼쳐진 공간, 2층독채형', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5965cef5-f6fe-4515-8e52-4b5b1ffaa23a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바당정원펜션' AND address = '제주특별자치도 제주시 한경면 판조로 23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바사팬스파', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 한경해안로 448-6', 33.3402680, 126.1683515, '저녁 노을을 보며 스파를 즐길 수 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f1a00fda-6afd-4c68-b0d9-d628bc851c1d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바사팬스파' AND address = '제주특별자치도 제주시 한경면 한경해안로 448-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '바산올레 애견동반 펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 대평로 29', 33.2365893, 126.3639804, '중문관광단지와 화순해수욕장 중간에 위치한 바산올레펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/26c9987c-dc22-407f-b20b-4baf66231503.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '바산올레 애견동반 펜션' AND address = '제주특별자치도 서귀포시 안덕면 대평로 29');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '발로바쉬', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 일주서로 6066', 33.4521196, 126.3093209, '전 객실이 곽지해수욕장을 바라보는 전망이 좋은 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5a23a252-b58a-4ebb-bb96-6e5d02e56a18.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '발로바쉬' AND address = '제주특별자치도 제주시 애월읍 일주서로 6066');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '보로미 민박', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 고성서3길 20-1', 33.4652669, 126.4118502, '가족이 머물기에 적합한 넓은 숙소입니다. 편안한 휴식을 보낼 수 있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202201/17/2ed43fc2-e9e6-44f5-abf5-71c45a973b76.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '보로미 민박' AND address = '제주특별자치도 제주시 애월읍 고성서3길 20-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '봉아저씨네민박', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 명상로 8-4', 33.3747438, 126.2846078, '협재해수욕장 인근에 위치한 40여년 된 돌집을 현대식 감각으로 재구성한 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/17/d55312d4-afed-4a94-9b53-6df3284cb9f3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '봉아저씨네민박' AND address = '제주특별자치도 제주시 한림읍 명상로 8-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '브라운캐빈', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 소길남길 190-40', 33.4154800, 126.3752700, '애월의 작은 숲속 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f363d238-e198-4010-b84a-dafe1c36ec58.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '브라운캐빈' AND address = '제주특별자치도 제주시 애월읍 소길남길 190-40');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '사계 해담은스파펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계북로 157-15', 33.2394018, 126.3039897, '용머리 웅회환 북태평양 바다전망이 펼쳐진 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/16aabb3f-8f75-4ec2-8c83-41ffc465aa26.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '사계 해담은스파펜션' AND address = '제주특별자치도 서귀포시 안덕면 사계북로 157-15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '사계여행민박', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 산방로 262', 33.2349693, 126.3078602, '산방산 기슭 300m 아래 올레 10코스에 자리한 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/227e6e33-46b5-4c9d-8c9e-fe0d84130ef3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '사계여행민박' AND address = '제주특별자치도 서귀포시 안덕면 산방로 262');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '사노롱', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 옹포6길 3', 33.4031597, 126.2523624, '제주의 옛스러움을 간직한 독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202511/20/a13f5486-f792-4e35-84d7-3732f4496683.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '사노롱' AND address = '제주특별자치도 제주시 한림읍 옹포6길 3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '사면초가펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 판조로 62', 33.3630800, 126.2080300, '넓은 정원과 제주의 옛 멋을 간직하여 네츄럴과 모던의 조화가 매력적인 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a4d994a4-28f7-4f75-b6e0-d0043f0536bf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '사면초가펜션' AND address = '제주특별자치도 제주시 한경면 판조로 62');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '산방산에펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계북로41번길 151', 33.2461360, 126.2969700, '단독형 휴양펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2194cf45-dbe9-4f1c-8c1f-2bda01216658.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '산방산에펜션' AND address = '제주특별자치도 서귀포시 안덕면 사계북로41번길 151');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '삼다행복돌집', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 수산2길 8-7', 33.4702528, 126.3931973, '제주도 돌집 숙소입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202604/13/ac1fd2ab-5bd7-49ce-b833-ca6b3d46e273.jpg', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '삼다행복돌집' AND address = '제주특별자치도 제주시 애월읍 수산2길 8-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '서귀포 늘바다 애견동반펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 대평로 38', 33.2372470, 126.3636731, '100이상 수령의 소나무로 지어진 통나무별장', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7b3e1e0a-f665-4282-b6fc-90f635cc3467.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '서귀포 늘바다 애견동반펜션' AND address = '제주특별자치도 서귀포시 안덕면 대평로 38');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '소낭재', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 협재로 181-57', 33.3834100, 126.2528900, '숲 속 펜션, 소낭재', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/86daa85e-4b76-4f5e-b4b5-fd5e05305c86.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '소낭재' AND address = '제주특별자치도 제주시 한림읍 협재로 181-57');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '소풍이야기', 'PENSION', 'WEST', '제주시 애월읍 애월해안로 832', 33.4886670, 126.3874050, '애월해안도로에 위치한 바다전망 좋은 커플펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0f0952f1-fbd1-454c-b050-fa2cfc4733df.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '소풍이야기' AND address = '제주시 애월읍 애월해안로 832');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '수원성민박', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 수원7길 47-3', 33.4282335, 126.2664469, '편백나무 향이 가득한 힐링 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202511/18/2b9291d7-b87e-46c2-b9b7-88f71ea3e313.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '수원성민박' AND address = '제주특별자치도 제주시 한림읍 수원7길 47-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '쉴띠펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 한창로 102-1', 33.2725145, 126.3655401, '제주도 방언으로 ''쉬는 곳''을 의미하는 쉴띠', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/05258697-bf86-4021-b543-8e53fc307dee.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '쉴띠펜션' AND address = '제주특별자치도 서귀포시 안덕면 한창로 102-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '쉴만한물가펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 월령안길 28', 33.3762170, 126.2141340, '월령리 바닷가에 위치한 카페&민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/51b8ae34-b12d-4728-b067-babf5c9faf5a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '쉴만한물가펜션' AND address = '제주특별자치도 제주시 한림읍 월령안길 28');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '스테이달하', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 신엄안1길 20', 33.4731413, 126.3656123, '애월에 위치해 있으며 너른 잔디밭과 야외에 노천탕, 복층 구조로 만들어진 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201810/24/8862d99b-6a6a-4868-955c-af166cafc1f4.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '스테이달하' AND address = '제주특별자치도 제주시 애월읍 신엄안1길 20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '슬로우리제주 민박', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 엄수로 77', 33.4697730, 126.3822308, '애월에 위치한 독채 민박집.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201904/29/1e278317-6536-4527-8fe0-6e563840bbfe.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '슬로우리제주 민박' AND address = '제주특별자치도 제주시 애월읍 엄수로 77');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '시땅', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 고내북동길 47', 33.4688700, 126.3427500, '예쁜 정원과 건물이 매력적인 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201805/23/b89c8a4c-d5b7-462d-bd9c-05e82f44323d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '시땅' AND address = '제주특별자치도 제주시 애월읍 고내북동길 47');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '시월애', 'PENSION', 'WEST', '제주특별자치도 서귀포시 대정읍 무릉사장로 154', 33.2574721, 126.1911790, '바다가 가깝고 잔디마당이 있는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1c27b0c3-fd74-4a35-856b-e84f5e610cc9.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '시월애' AND address = '제주특별자치도 서귀포시 대정읍 무릉사장로 154');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아뜨네통나무펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 상하귀길 91', 33.4757330, 126.4163508, '상쾌한 자연 속에서 느끼는 제주의 풍경 제주공항 근처 아뜨네통나무펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/bbd1618a-c2b1-43cd-ab30-54765b8a75fe.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아뜨네통나무펜션' AND address = '제주특별자치도 제주시 애월읍 상하귀길 91');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아르포레 스테이', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 상귀장수물길 58-41', 33.4490259, 126.4014713, '예술과 자연이 만나는 특별한 공간, 아르포레 스테이', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202605/10/7ec713f1-d412-4955-9dbe-1f60b99a2079.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아르포레 스테이' AND address = '제주특별자치도 제주시 애월읍 상귀장수물길 58-41');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아마빌레', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 909', 33.4847294, 126.3926699, '공항에서 차로 15분 거리의 애월해안도로에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/94b5bb67-46df-4096-bec3-a5aec061ace1.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아마빌레' AND address = '제주특별자치도 제주시 애월읍 애월해안로 909');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아올키즈펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계중앙로 13-15', 33.2301400, 126.2980800, '부모와 아이가 즐거운 키즈 프라이빗 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c24c8434-49b9-4aa4-b5fc-7cf7b3d09d98.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아올키즈펜션' AND address = '제주특별자치도 서귀포시 안덕면 사계중앙로 13-15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '아이랑 키즈풀빌라', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 고내로7길 34', 33.4656000, 126.3375600, '아이와 함께하기 좋은 독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6f08393b-1d2f-464b-9be8-8661ad458ade.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '아이랑 키즈풀빌라' AND address = '제주특별자치도 제주시 애월읍 고내로7길 34');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '안트레 펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 한림중앙로 154-7', 33.4022520, 126.2697800, '넓은 정원과 포근한 객실을 가진 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/160df30d-11f6-4ad7-8951-b019f9670abb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '안트레 펜션' AND address = '제주특별자치도 제주시 한림읍 한림중앙로 154-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '애월감성숙소샤인히얼60', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 중산간서로 5397', 33.4321500, 126.3206576, '힐링과 예술이 공존하는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202511/19/83072e3b-cbe9-4c33-8d9d-341a2d8b78c0.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '애월감성숙소샤인히얼60' AND address = '제주특별자치도 제주시 애월읍 중산간서로 5397');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '애월포구풍경', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 애월로11길 18', 33.4659400, 126.3208400, '제주시 애월읍 애월리에 위치한 가족펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/39447315-af40-4f7e-8db0-350e1e7a70d3.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '애월포구풍경' AND address = '제주특별자치도 제주시 애월읍 애월로11길 18');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '애월하타', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 곽지남3길 21-1', 33.4450242, 126.3114644, '100평 잔디정원에서 우리만의 시간을 보낼 수 있는 애월하타입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202412/10/aafcbf16-44ca-48bc-a52b-cd11c10679d6.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '애월하타' AND address = '제주특별자치도 제주시 애월읍 곽지남3길 21-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '애월해안누리펜션(구 하얀둥지펜션)', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 298', 33.4704594, 126.3416719, '황토로 빗은 애월해안도로에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201907/04/8a9b1931-9843-4c1e-93a2-7a9cfa5f83e3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '애월해안누리펜션(구 하얀둥지펜션)' AND address = '제주특별자치도 제주시 애월읍 애월해안로 298');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '야스라기펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 상귀서길 82-34', 33.4635276, 126.4045277, '네덜란드산 원목으로 설계되어 통나무의 은은한 향이 그대로 살아있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/52f6eaf9-2882-4175-b967-d0d8d50bbeba.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '야스라기펜션' AND address = '제주특별자치도 제주시 애월읍 상귀서길 82-34');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '엄블랑펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 형제해안로132번길 15-24', 33.2209524, 126.2923419, '지질의 특성을 체험할 수 있도록 하는 지질테마 숙소인 지오하우스 엄블랑펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/acb7aeec-44b6-4ae3-829b-86262dd2096c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '엄블랑펜션' AND address = '제주특별자치도 서귀포시 안덕면 형제해안로132번길 15-24');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '에너벨리민박', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 한림로 356', 33.3948825, 126.2418944, '맛있는 음식점과 전망좋은 카페를 같이 운영하는 콘도형 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201907/04/095b2841-0e09-4a1d-a2c9-3cc9243cfe9e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '에너벨리민박' AND address = '제주특별자치도 제주시 한림읍 한림로 356');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '엘쯔타운 독채펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 유수암평화길 136', 33.4094410, 126.4118394, '숲에 둘러싸인 신축펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/11f50ce6-5c23-4c18-a9ef-40cc5b031280.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '엘쯔타운 독채펜션' AND address = '제주특별자치도 제주시 애월읍 유수암평화길 136');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '옛살라비펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 광령8길 47', 33.4408969, 126.4345309, '제주공항과 15분거리, 올래16코스 종점, 17코스 시작점에 위치한 옛살라비 펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0946415d-f7a8-4c8e-9e5d-baa9506511be.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '옛살라비펜션' AND address = '제주특별자치도 제주시 애월읍 광령8길 47');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오비두스 스테이', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 신엄안3길 138', 33.4731869, 126.3535996, '애월 해안도로 산책길에서 한적한 산책을 즐길 수 있는 바닷가 앞 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2a9378eb-1e8c-4995-ba7e-a0d86c6390f2.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오비두스 스테이' AND address = '제주특별자치도 제주시 애월읍 신엄안3길 138');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오시록헌 AM', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 금능6길 8', 33.3877900, 126.2319800, '아늑하다, 오시록헌', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c291194f-8382-40cc-a5d5-b173ea5fefe4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오시록헌 AM' AND address = '제주특별자치도 제주시 한림읍 금능6길 8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '오후여섯시펜션 &카페', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 곽지9길 10', 33.4521596, 126.3077543, '제주 애월 곽지해수욕장 인근에 위치한 펜션.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201904/03/97a39f80-14cc-4d84-b492-4a9bc08a3871.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '오후여섯시펜션 &카페' AND address = '제주특별자치도 제주시 애월읍 곽지9길 10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '올레프로방스펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 광령평화2길 170-23', 33.3701721, 126.4819697, '청정지역인 제주도에서도 생태보존지역에 근접해 맑은 공기와 탁 트인 경관을 자랑하는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/5655b7dd-cd40-4e5e-bc4b-9d26ea22f1b9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '올레프로방스펜션' AND address = '제주특별자치도 제주시 애월읍 광령평화2길 170-23');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '용수차경 돌품', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 용수2길 7 (용수리) 용수 2길 7', 33.3223568, 126.1696030, '사람을 품었던 제주의 돌집을 다시 한 번 더 품은 돌을 품은 집, 용수차경입니다', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202304/06/d49a2b13-5c72-4c74-b0bf-ba72f05c776d.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '용수차경 돌품' AND address = '제주특별자치도 제주시 한경면 용수2길 7 (용수리) 용수 2길 7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '우연히 행복해지다 펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 한림로 224', 33.3866166, 126.2317794, '금능해수욕장 근처에 위치한 제주도펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201909/27/b361b266-392e-4fc4-8142-fb6669eabfaa.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '우연히 행복해지다 펜션' AND address = '제주특별자치도 제주시 한림읍 한림로 224');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '월령채', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 월령3길 27-5', 33.3777664, 126.2160381, '한림읍 14올레 선인장 군락지 월령포구에 위치한 프라이빗 감성팬션 월령채', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202207/22/98d4691a-e570-487c-b627-10cffdc94315.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '월령채' AND address = '제주특별자치도 제주시 한림읍 월령3길 27-5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '웨스티하우스', 'PENSION', 'WEST', '제주도 서귀포시 안덕면 일주서로 1488번길9', 33.2416340, 126.3671900, '자연과 어우러진 수공식 통나무집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8731bebc-df31-4ce6-8a2c-af3a5c568532.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '웨스티하우스' AND address = '제주도 서귀포시 안덕면 일주서로 1488번길9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '이디살래', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 일주서로 2027', 33.2496260, 126.3013200, '가족과 함께라면, 이디살래', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b1e6e835-9324-4272-a0cf-e359da33e0ea.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '이디살래' AND address = '제주특별자치도 서귀포시 안덕면 일주서로 2027');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '이리로스테이', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 광령평화2길 142-19', 33.4322775, 126.4329879, '친환경적 목조주택으로 만들어진 휴양형 고급펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b6ec4a43-4075-4fe1-8071-d757abbb5752.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '이리로스테이' AND address = '제주특별자치도 제주시 애월읍 광령평화2길 142-19');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '이쁜새펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 협재로 68-5', 33.3922895, 126.2472499, '차로 1분 거리에 협재 해수욕장이 있는 전객실 바다전망의 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/57bd142d-21d5-4e42-bfac-36d982fe93fb.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '이쁜새펜션' AND address = '제주특별자치도 제주시 한림읍 협재로 68-5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '잇츠힐펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 788', 33.4870400, 126.3840068, '해안도로 바로 앞에 위치한 잇츠빌 펜션은 앞으로는 바다전경이 뒤로는 한라산을 배경으로 사계절 다채로운 풍경이 있습니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/62244935-52f1-46c1-b117-cab937cfc058.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '잇츠힐펜션' AND address = '제주특별자치도 제주시 애월읍 애월해안로 788');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제이앤클로이', 'PENSION', 'WEST', '제주특별자치도 서귀포시 대정읍 무릉중앙로 203', 33.2571800, 126.1888400, '별과 바다를 보면서 즐길 수 있는 스파 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/56cece65-b8d7-4252-9d02-efda2d9f9e4a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제이앤클로이' AND address = '제주특별자치도 서귀포시 대정읍 무릉중앙로 203');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제이펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 화순서서로64번길 20', 33.2553028, 126.3311699, '안덕면 화순리 곶자알 생태숲 인근에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/924ed9ce-d95f-4eda-8523-5000d85fd1ed.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제이펜션' AND address = '제주특별자치도 서귀포시 안덕면 화순서서로64번길 20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 또올레 펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 판포2길 44', 33.3608375, 126.2014235, '맑고 신선한 제주의 공기와 함께, 일상탈출의 여유가 느껴지는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0736d55c-e0e2-41f7-b880-3b5fa603daaf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 또올레 펜션' AND address = '제주특별자치도 제주시 한경면 판포2길 44');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 마제스티펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 옹포2길 12', 33.3996800, 126.2498000, '협재에 위치하여 비양도가 잘 보이는 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a51104ca-f627-4861-a825-16fd55327090.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 마제스티펜션' AND address = '제주특별자치도 제주시 한림읍 옹포2길 12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 벨루가', 'PENSION', 'WEST', '제주특별자치도 서귀포시 대정읍 영락하동로85번길 41-1', 33.2562288, 126.2115638, '돌고래 서식지로 유명한 서남쪽 조용한 시골마을에 위치한 제주도 개인 온수풀빌라 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/15/cfb1a791-608b-4e97-8d76-d47344df20cf.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 벨루가' AND address = '제주특별자치도 서귀포시 대정읍 영락하동로85번길 41-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 브라이튼 서귀포점', 'PENSION', 'WEST', '제주특별자치도 서귀포시 대정읍 도원로3번길 9 (신도리)', 33.2774721, 126.1711174, '제주도 프라이빗 독채 풀빌라 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202601/08/d144b8f2-7583-4b9d-bc57-3aea86236b39.jpg', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 브라이튼 서귀포점' AND address = '제주특별자치도 서귀포시 대정읍 도원로3번길 9 (신도리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주 키즈앤돌핀 펜션', 'PENSION', 'WEST', '제주도 서귀포시 대정읍 노을해안로 638', 33.2707560, 126.1760000, '올레 12코스의 중간지점. 노을이 아름답게 걸리는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/46894d4a-4c50-4a6f-870f-b4a5601766f7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주 키즈앤돌핀 펜션' AND address = '제주도 서귀포시 대정읍 노을해안로 638');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주29애월풀빌라', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 중산간서로 5409', 33.4327740, 126.3217200, '야외 수영장 및 골프연습시설 등 다양한 부대시설을 갖춘 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/818a32af-c2cc-4ceb-9769-2e62cd8c6b80.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주29애월풀빌라' AND address = '제주특별자치도 제주시 애월읍 중산간서로 5409');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주금산펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 납읍로2길 40', 33.4323430, 126.3380600, '제주 올레 15코스 중간지점의 최대 경관인 납읍리 금산공원 앞에 위치한 펜션형 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f277fe24-2c82-49fd-9300-6915851feeff.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주금산펜션' AND address = '제주특별자치도 제주시 애월읍 납읍로2길 40');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주마로펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 한림로 414-2', 33.3983880, 126.2464000, '마음가는대로, 마로', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/44651103-ca53-4f07-a5eb-126eea0ce83c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주마로펜션' AND address = '제주특별자치도 제주시 한림읍 한림로 414-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주별바람펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 한림해안로 500', 33.4346670, 126.2725769, '제주한림해안로에 위치한 곳으로 전객실 바다전망이 가능한 펜션형민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/59003d64-3652-48d6-bb75-100a504ca148.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주별바람펜션' AND address = '제주특별자치도 제주시 한림읍 한림해안로 500');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주비치펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 384-12', 33.4718940, 126.3495201, '전 객실이 제주의 바다와 노을을 한눈에 볼 수 있는 구조도 되어 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fa4ab624-ea6d-42c2-8e86-50bf6e392ad9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주비치펜션' AND address = '제주특별자치도 제주시 애월읍 애월해안로 384-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주애견전문 엔젤하우스', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 신엄안3길 95', 33.4722525, 126.3578503, '제주 애월에 있는 애견동반가능한 넓은 정원의 아름다운 펜션이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202212/21/d76319c6-a251-4cac-a69b-587415660c84.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주애견전문 엔젤하우스' AND address = '제주특별자치도 제주시 애월읍 신엄안3길 95');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주애견펜션 쉼멍스테이', 'PENSION', 'WEST', '제주특별자치도 서귀포시 대정읍 신영로 16-14', 33.2237511, 126.2458989, '쉼멍스테이는 반려동물과 함께 편안하게 하룻밤을 보낼 수 있는 반려견 동반 펜션이다.', '', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주애견펜션 쉼멍스테이' AND address = '제주특별자치도 서귀포시 대정읍 신영로 16-14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주애단비 마주', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 어도봉남길 4', 33.4179975, 126.3019411, '한경면에 위치한 애견 동반 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201810/22/14f270ab-b867-4f5c-b21e-bbc1048ec6ce.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주애단비 마주' AND address = '제주특별자치도 제주시 한림읍 어도봉남길 4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주애물들다', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 고조로 492-8', 33.3291135, 126.2213991, '제주애물들다는 제주의 석양을 잔뜩 느낄 수 있는 감성펜션이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/f55ef052-f4e3-436f-b70a-989d94a80d7c.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주애물들다' AND address = '제주특별자치도 제주시 한경면 고조로 492-8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주애빛나다', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 고조로 492-6', 33.3294047, 126.2216605, '신비로운 자연과 함께 제주에 빛나는 시간, 노을이 아름다운 한경면에 위치한 민트색 독채펜션 제주애빛나다는 제주 자연을 그대로 느낄 수 있는 곳이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202308/18/97ee07f3-9e89-4591-bf72-3456e687d2d4.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주애빛나다' AND address = '제주특별자치도 제주시 한경면 고조로 492-6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주엔펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 대정읍 노을해안로 614', 33.2695742, 126.1779554, '연인들이 꿈과 사랑을 간직하고 가는 곳 설레임 가득한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c18a31f0-a0eb-4d1e-83a2-8dd772674ec9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주엔펜션' AND address = '제주특별자치도 서귀포시 대정읍 노을해안로 614');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주이야기펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 유수암평화길 170-2', 33.4289000, 126.4095500, '유수암에 위치한 넓은 정원이 인상적인 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/74c10b55-009c-4030-ac42-686f967646da.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주이야기펜션' AND address = '제주특별자치도 제주시 애월읍 유수암평화길 170-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주조이랜드', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 애원로 116-5', 33.4547384, 126.3251781, '모든 객실이 통나무로된 독채로구성되어있고 전나무를 사용하여 월동에 강하며 나무특유의 향이나며 아토피가 있는 아이들, 새집 증후군때매 고생하는분들에게 추천합니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/66001091-6cb5-4471-95b8-7260e3fe6d59.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주조이랜드' AND address = '제주특별자치도 제주시 애월읍 애원로 116-5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주프렌즈', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 판포2길 35', 33.3614954, 126.2008492, '바다가 보이는 그림같이 예쁜 집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/88e8acf8-648b-4c97-879e-10c99c0a44a6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주프렌즈' AND address = '제주특별자치도 제주시 한경면 판포2길 35');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주하늘바다 오션뷰펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 수원15길 31 (수원리)', 33.4332671, 126.2728882, '제주의 넓고 푸르른 바다와 한라산의 백록담이 보이는 오션뷰 민박형 펜선이자 안전인증 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202406/17/485ec4a0-30a7-4f9c-a5a4-b6956cdaed22.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주하늘바다 오션뷰펜션' AND address = '제주특별자치도 제주시 한림읍 수원15길 31 (수원리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주해조대펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 한림로 339', 33.3807358, 126.2482491, '시원하게 뻥 뚫린 바다 전망대에서 협재 해수욕장을 감상할 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8f9a2c2f-7d2b-4abb-b2e5-73ceac8c473a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주해조대펜션' AND address = '제주특별자치도 제주시 한림읍 한림로 339');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '중문햇빛펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 쇠물도로 14', 33.2696174, 126.3699416, '각각의 동으로 구성돼 독립적으로 사용할 수 있는 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/b2f8d161-3c02-4e9a-9976-f87a4ad4e902.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '중문햇빛펜션' AND address = '제주특별자치도 서귀포시 안덕면 쇠물도로 14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '쥬빌리펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 소기왓로 84-4', 33.2423579, 126.3693253, '올레 8코스와 9코스 지역에 위치한 따뜻한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/8776d7c4-6913-4e63-b012-9ba12e1be629.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '쥬빌리펜션' AND address = '제주특별자치도 서귀포시 안덕면 소기왓로 84-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '청수곶', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 대한로 800-8', 33.2973928, 126.2444295, '곶자왈과 한라산을 한눈에... ‘청수곶’', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202407/23/8e08775f-e618-495f-9af0-bad419d430ad.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '청수곶' AND address = '제주특별자치도 제주시 한경면 대한로 800-8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '초록새록', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 중엄1길 37-22', 33.4996220, 126.5311900, '애월읍 신엄리에 위치한 통나무펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/43d245e9-9d5f-490c-9b22-bc7c51a1abad.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '초록새록' AND address = '제주특별자치도 제주시 애월읍 중엄1길 37-22');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '캐빈타운', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 가문동상2길 29-3', 33.4849359, 126.3910124, '아름답고 편안함을 제공하는 단체 여행객 전용 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/55b2dfad-a8de-44ee-a1bc-fa4b626c4005.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '캐빈타운' AND address = '제주특별자치도 제주시 애월읍 가문동상2길 29-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '캐슬드한림', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 강구로 95-7', 33.4091439, 126.2780097, '사생활 보호를 위한 높은 담장으로 둘러쌓인 독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202310/13/f56f4968-34fa-4e4f-a358-1c81f6ea1061.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '캐슬드한림' AND address = '제주특별자치도 제주시 한림읍 강구로 95-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '코코스펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 협재2길 26', 33.3807358, 126.2482491, '협재해수욕장 도보4분거리에 있고 야자수와 수영장이 멋진 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1a526139-2e9d-42e0-a317-befc1435dcbc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '코코스펜션' AND address = '제주특별자치도 제주시 한림읍 협재2길 26');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '코쿤협재펜션', 'PENSION', 'WEST', '제주도 제주시 한림읍 협재로 33', 33.3952700, 126.2470500, '협재 해수욕장에서 5분거리에 위치한 넓은 잔디 정원을 갖춘 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f8644282-8599-42f6-841f-b4ef09b2fb48.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '코쿤협재펜션' AND address = '제주도 제주시 한림읍 협재로 33');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '킹스통나무', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 도치돌길 279-1', 33.4108192, 126.3644577, '애월 경마공원 근처에 위치한 통나무 독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/9136cdd6-d5fd-412e-928e-67ce8675fe02.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '킹스통나무' AND address = '제주특별자치도 제주시 애월읍 도치돌길 279-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '토토하우스', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 광령자종길 14', 33.3701721, 126.4819697, '제주 고품격 가족 및 키즈 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201812/16/13f3c817-70b8-4316-b2a6-2c1b0591b3cc.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '토토하우스' AND address = '제주특별자치도 제주시 애월읍 광령자종길 14');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '통나무파크', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 도치돌길 303', 33.4098961, 126.3665965, '숲 속 통나무집의 매력을 느낄 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/18340431-d99b-4177-82c2-2e4c8b40d43b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '통나무파크' AND address = '제주특별자치도 제주시 애월읍 도치돌길 303');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '틸다하우스', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 중산간서로 3367-1', 33.3109000, 126.2372213, '아이와 함께 제주의 자연을 체험하며 아이와 함께 할 수 있는 가족펜션.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201905/09/d59281a5-8ca4-4231-97e7-73436be4f5ec.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '틸다하우스' AND address = '제주특별자치도 제주시 한경면 중산간서로 3367-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '파더하우스', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 고하상로 88', 33.4572880, 126.3466068, '제주도 애월읍 하가리 마을 안에 위치한 아늑한 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f9c03c16-63b6-46da-a8d7-f7c2216b86f9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '파더하우스' AND address = '제주특별자치도 제주시 애월읍 고하상로 88');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '파미에애월', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 고성남서길 39-15', 33.4321847, 126.4217324, '가족의 집처럼 편안하게 머무길 바라... ‘파미에애월’', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202407/24/d9466782-d43b-47c0-85f0-553b41a1862d.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '파미에애월' AND address = '제주특별자치도 제주시 애월읍 고성남서길 39-15');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '파크빌리지', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 상귀서길 82-12', 33.4651885, 126.4053912, '잣나무 낙엽송으로 내부 인테리어를 한 자연 웰빙테마의 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/519c1d9b-7dc4-424d-823d-9b28c66de3ab.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '파크빌리지' AND address = '제주특별자치도 제주시 애월읍 상귀서길 82-12');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '포구민박', 'PENSION', 'WEST', '제주시 애월읍 하귀12길 20-1', 33.4863500, 126.4089700, '애월읍 하귀 민박, 포구 앞 아기자기한 독채민박(인생샷 포토존)', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201810/31/a7de559d-602b-404d-b4f7-c99cac803c34.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '포구민박' AND address = '제주시 애월읍 하귀12길 20-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '폴라리스펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 유수암동3길 45', 33.4380950, 126.4084240, '가족 단위로 머물기 좋은 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/272719b2-2d9f-4ac2-baf7-b52c9ebec9a6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '폴라리스펜션' AND address = '제주특별자치도 제주시 애월읍 유수암동3길 45');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '풍차와 해넘이', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 두조로 8 (두모리)', 33.3539347, 126.1878700, '신창풍차해안도로 인근, 가족과 함께 노을을 즐길 수 있는 가족친화 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202509/21/bed3c0e4-3dc1-4b26-9a00-319320f15654.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '풍차와 해넘이' AND address = '제주특별자치도 제주시 한경면 두조로 8 (두모리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '풍차와노을 독채펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한경면 한경해안로 474-26', 33.4996220, 126.5311900, '제주 신창리 바닷가에 위치한 가족독채펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/1679225f-6342-46a9-a00c-6c7ed565d5a9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '풍차와노을 독채펜션' AND address = '제주특별자치도 제주시 한경면 한경해안로 474-26');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '프레리아 커플 독채펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 유수암평화5길 34-29', 33.4253669, 126.4050815, '유수암리에 위치하며 한적한 휴식만을 위한 공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/df99e77a-9273-4798-8100-717525a59804.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '프레리아 커플 독채펜션' AND address = '제주특별자치도 제주시 애월읍 유수암평화5길 34-29');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '플라마펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 장원길 34-29', 33.4129700, 126.2790000, '한림항과 협재해수욕장 근처에 위치 한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a34b5be5-3491-4ac0-a22f-7a6785dff171.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '플라마펜션' AND address = '제주특별자치도 제주시 한림읍 장원길 34-29');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하귤소담', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 광령북1길 67', 33.4034138, 126.4508866, '하귤나무가 있는 감성 독채 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202511/19/1d269e45-3e85-4c25-af92-3232a84788fe.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하귤소담' AND address = '제주특별자치도 제주시 애월읍 광령북1길 67');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하녹펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 하광로 181-1', 33.4790040, 126.4164050, '애월읍 하귀리에 위치한 하녹펜션은 제주에 흔치 않은 한국 전통방식으로 지어진 한옥펜션이다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/934423b5-7c46-4d09-aad1-bc704facda65.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하녹펜션' AND address = '제주특별자치도 제주시 애월읍 하광로 181-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하늘고래블루', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 협재1길 59-4', 33.3994060, 126.2461500, '전통 돌집을 활용한 독채형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/fe549944-ab75-46ea-93da-ebf8d28ec7f6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하늘고래블루' AND address = '제주특별자치도 제주시 한림읍 협재1길 59-4');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하늘만큼 땅만큼 펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 가문동상4길 5', 33.4832683, 126.3942235, '넓고 쾌적한 객실 전체가 오션뷰로, 다양한 시설과 1200평 규모의 넓은 공간에 포토존이 구비되어 있는 숙소', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/e9d7cd23-89bf-4b8c-a610-9d329835db95.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하늘만큼 땅만큼 펜션' AND address = '제주특별자치도 제주시 애월읍 가문동상4길 5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하늘조각휴양펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계북로 75', 33.2355460, 126.2959547, '자연, 조형미가 함께하는 편안함과 휴식, 그리고 즐거움을 위한 모든 것이 마련된 곳입니다. 그 어디에서도 없었던 특별한 시간 하늘정원에서 경험해 보시기 바랍니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/6bd2906b-57e7-4165-9af5-807ece6e9f42.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하늘조각휴양펜션' AND address = '제주특별자치도 서귀포시 안덕면 사계북로 75');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하바다 독채 통나무집', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 대평감산로 86', 33.2414400, 126.3649500, '중문관광단지에서 차로 10분거리에 위치한 독채 복층 바다전망 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e506b7c0-baed-43b5-aeb5-57e5ff3c7214.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하바다 독채 통나무집' AND address = '제주특별자치도 서귀포시 안덕면 대평감산로 86');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '하이디펜션', 'PENSION', 'WEST', '제주특별자치도 서귀포시 안덕면 사계북로41번길 142 (사계리) 하이디펜션', 33.2454599, 126.2973600, '한라산, 산방산, 단산, 용머리와 푸른 바다를 한눈에 담을 수 있는 여행자들의 쉼터', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/02/14daeeca-4a4e-412e-9f55-0e3e97069928.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '하이디펜션' AND address = '제주특별자치도 서귀포시 안덕면 사계북로41번길 142 (사계리) 하이디펜션');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '한담풍경하우스', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 애월로 6', 33.4594051, 126.3115167, '애월에 위치한 한담해변 인근에 위치한 펜션.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201905/17/dd310250-fb90-40e1-9801-9e5728077323.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '한담풍경하우스' AND address = '제주특별자치도 제주시 애월읍 애월로 6');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '한림 퐁낭하우스 펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 대림12길 60-4 (대림리)', 33.4252987, 126.2770824, '한림에 위치한 안전인증민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201812/18/5136cfe2-c668-4448-b431-d8edf5768b4f.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '한림 퐁낭하우스 펜션' AND address = '제주특별자치도 제주시 한림읍 대림12길 60-4 (대림리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해변산책펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 곽지9길 7', 33.4519929, 126.3080920, '북유럽 풍의 인테리어, 애월더썬셋 몽상드애월과 같은 유명카페와 가까운 위치의 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/e69d8656-46b5-493d-8386-0052737bf0b9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해변산책펜션' AND address = '제주특별자치도 제주시 애월읍 곽지9길 7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해아래펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 번대동길 5', 33.4782640, 126.3923700, '제주도 하늘 아래에 있는 해아래 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3011255b-6859-41db-a348-50c96836942b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해아래펜션' AND address = '제주특별자치도 제주시 애월읍 번대동길 5');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해오름정원', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 상하귀길 98-1', 33.4750229, 126.4167515, '제주바다와 자연, 유명관광지, 공항과 근접한 거리에 위치해 이동이 편리한 해오름정원펜션입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/3f865954-4111-45bf-82bf-886007def890.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해오름정원' AND address = '제주특별자치도 제주시 애월읍 상하귀길 98-1');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해울독채펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 명재로 100-45', 33.3924810, 126.2411472, '골프연습장, 수영장, 편의점 등의 부대시설을 갖춘 편리한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a007e64b-7e9f-4ffd-b26e-ae3ad57e1e04.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해울독채펜션' AND address = '제주특별자치도 제주시 한림읍 명재로 100-45');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해피데이', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 한림로 423', 33.3993265, 126.2468661, '모든 방에서 협재바다와 비양도가 한 눈에 바라다 보이는 협재 해피데이민박!!', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/ea7da3ca-0b26-40bb-82b8-85a5dfed2f8b.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해피데이' AND address = '제주특별자치도 제주시 한림읍 한림로 423');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '해피데이펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 한림로 423', 33.3993265, 126.2468661, '모든 방에서 협재바다와 비양도가 한 눈에 바라다 보이는 민박', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/90a424c0-8f80-40ca-bfba-505bbfdde09e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '해피데이펜션' AND address = '제주특별자치도 제주시 한림읍 한림로 423');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '허브인휴양펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 귀덕6길 92-17', 33.4356437, 126.2902931, '상쾌한 허브향을 즐길 수 있는 애월해안도로에 근접한 휴양펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/09144105-1b4f-4e49-ae4f-224629313f6d.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '허브인휴양펜션' AND address = '제주특별자치도 제주시 한림읍 귀덕6길 92-17');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '헤리티지', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 하광로 530', 33.4441450, 126.4296200, '한라산,바다 전망의 예쁜 빨간 벽돌 외관의 휴식처', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/bdc02e5b-fcb7-4d47-abe4-c04767056486.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '헤리티지' AND address = '제주특별자치도 제주시 애월읍 하광로 530');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '협재 하얀집', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 협재1길 9', 33.3965751, 126.2425147, '대문 앞 용천수 웅덩이를 질길 수 있는 한림에 위치한 민박집', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/f7d803f7-1b1d-463f-a5b5-bb31ea12fc02.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '협재 하얀집' AND address = '제주특별자치도 제주시 한림읍 협재1길 9');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '협재해오름펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 한림읍 옹포2길 37-28 (옹포리) 협재해오름펜션', 33.3979570, 126.2535498, '해가 뜨고 질 때까지 행복한 기억만 남을 수 있도록, 해오름펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201811/06/e821ce5d-9e33-4163-b7ff-55daaf9ed337.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '협재해오름펜션' AND address = '제주특별자치도 제주시 한림읍 옹포2길 37-28 (옹포리) 협재해오름펜션');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '혼디펜션', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 신엄안3길 20', 33.4742823, 126.3646948, '애월해안도로 인근 신엄리 마을 안에 위치한 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201808/17/5061870a-01c8-40b5-845a-4d082982b515.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '혼디펜션' AND address = '제주특별자치도 제주시 애월읍 신엄안3길 20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'CS빌리지', 'PENSION', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 532-7', 33.4784500, 126.3603700, '제주 바다가 훤히 보이는 애월의 유럽풍 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/a5018e1e-cf3e-45c2-8832-34a3c8d9669a.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'CS빌리지' AND address = '제주특별자치도 제주시 애월읍 애월해안로 532-7');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'WITH US', 'PENSION', 'WEST', '제주시 애월읍 애월로 7길 24-3', 33.4658430, 126.3176040, '모던하게 개조된 제주 농가 주택, 렌탈하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/79136ec4-ae91-452e-97ae-5ae60c786c3f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'WITH US' AND address = '제주시 애월읍 애월로 7길 24-3');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '다인리조트', 'RESORT', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 400-9 (고내리) 다인리조트', 33.4713567, 126.3510024, '제주도 애월의 아름다운 바다와 올레길을 품고 있는 다인리조트입니다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202407/03/dc13cd0e-0da5-4bc6-b48f-e42422db485e.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '다인리조트' AND address = '제주특별자치도 제주시 애월읍 애월해안로 400-9 (고내리) 다인리조트');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '담모라 호텔앤리조트', 'RESORT', 'WEST', '제주특별자치도 서귀포시 안덕면 일주서로 1975-11 (사계리)', 33.2497388, 126.3060973, '슬로우 라이프 스테이 담모라', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202408/08/9069f359-e428-4f76-9483-448a67323179.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '담모라 호텔앤리조트' AND address = '제주특별자치도 서귀포시 안덕면 일주서로 1975-11 (사계리)');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '라온호텔앤리조트', 'RESORT', 'WEST', '제주특별자치도 제주시 한림읍 금능남로 127', 33.3760556, 126.2441268, '비양도와 에메랄드빛 협재바다가 보이는 제주도 서부의 명품 휴양단지 라온호텔앤리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/dc17399d-d773-41bc-b760-e2dc70c41f20.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '라온호텔앤리조트' AND address = '제주특별자치도 제주시 한림읍 금능남로 127');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '랜딩관 제주신화월드 호텔앤리조트', 'RESORT', 'WEST', '제주특별자치도 서귀포시 안덕면 신화역사로304번길 38', 33.3066601, 126.3188970, '스마트하게 누리는 최상의 서비스, 랜딩 리조트.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201903/06/4c23963b-d156-428c-bee1-f9d211a9f0e3.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '랜딩관 제주신화월드 호텔앤리조트' AND address = '제주특별자치도 서귀포시 안덕면 신화역사로304번길 38');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '루체빌', 'RESORT', 'WEST', '제주특별자치도 서귀포시 안덕면 산록남로 786', 33.3189900, 126.3852500, '숲속에 앉아 있는 듯한 휴식 공간', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/28b8b099-01c7-47ab-91d5-d23aca068cef.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '루체빌' AND address = '제주특별자치도 서귀포시 안덕면 산록남로 786');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '메리어트관 제주신화월드 호텔앤리조트', 'RESORT', 'WEST', '제주특별자치도 서귀포시 안덕면 신화역사로304번길 38', 33.3077182, 126.3176417, '글로벌 브랜드의 럭셔리 웰빙 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201903/06/4a6b49f3-d1d1-4d18-892e-38e640b79a32.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '메리어트관 제주신화월드 호텔앤리조트' AND address = '제주특별자치도 서귀포시 안덕면 신화역사로304번길 38');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '블랙스톤 제주', 'RESORT', 'WEST', '제주특별자치도 제주시 한림읍 한창로 925-122', 33.3386600, 126.3124300, '제주 원시림 속에서 27홀의 역동적인 드라마가 펼쳐지는 골프앤리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202604/30/84bf5af9-1016-4228-a3ff-cde6e36f466f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '블랙스톤 제주' AND address = '제주특별자치도 제주시 한림읍 한창로 925-122');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '서머셋 제주신화월드 호텔앤리조트', 'RESORT', 'WEST', '제주특별자치도 서귀포시 안덕면 신화역사로304번길 89', 33.3027495, 126.3172963, '3대가 함께하는 프리미엄 패밀리 리조트, 이젠 사랑하는 반려견과도 함께하세요!', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202311/01/016e3ffb-a811-4229-ac2f-271e2411caaa.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '서머셋 제주신화월드 호텔앤리조트' AND address = '제주특별자치도 서귀포시 안덕면 신화역사로304번길 89');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '신화관 제주신화월드 호텔 앤 리조트', 'RESORT', 'WEST', '제주특별자치도 서귀포시 안덕면 신화역사로304번길 38 신화관 제주신화월드 호텔 앤 리조트', 33.3062278, 126.3160186, '제주만의 낭만 가득한 프리미엄 휴양 리조트, 신화(Shinhwa)', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202205/30/fd57c115-646d-48c8-9ede-ea367b91ce12.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '신화관 제주신화월드 호텔 앤 리조트' AND address = '제주특별자치도 서귀포시 안덕면 신화역사로304번길 38 신화관 제주신화월드 호텔 앤 리조트');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '썬앤문리조트', 'RESORT', 'WEST', '제주특별자치도 서귀포시 안덕면 사계남로 102', 33.2273780, 126.3011099, '썬앤문 리조트는 제주도 사계 해변 근처에 자리 잡고 있다. 이곳은 제주 최초의 천연리조트로 천연염색의 장인이 지은 공간으로도 유명하다. 현무암으로 둘러싸인 외관과 천연소재를 사용한 이불로 건강한 리조트의 느낌을 준다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201809/03/713054ab-a539-4bec-a046-afffca220c16.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '썬앤문리조트' AND address = '제주특별자치도 서귀포시 안덕면 사계남로 102');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '에코그린리조트', 'RESORT', 'WEST', '제주특별자치도 제주시 한림읍 귀덕6길 94', 33.4359287, 126.2915445, '제주 귀덕리에 위치한 복합형 테마 리조트 & 풀빌라', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202401/12/78f4833d-d8a0-494d-b04b-5b1231dc8f82.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '에코그린리조트' AND address = '제주특별자치도 제주시 한림읍 귀덕6길 94');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '올레리조트', 'RESORT', 'WEST', '제주 제주시 애월읍 애월해안로 454-10 454-12, 올레리조트', 33.4756942, 126.3558489, '오래오래 머물고 싶은 리조트 올레리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202206/15/1b884afc-841b-4982-88a2-8dd48f619742.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '올레리조트' AND address = '제주 제주시 애월읍 애월해안로 454-10 454-12, 올레리조트');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '웨이브 제주 호텔&리조트', 'RESORT', 'WEST', '제주특별자치도 제주시 한림읍 한림로 77', 33.3813630, 126.2183300, '협재해수욕장 및 한림공원과 가까운 곳에 위치한 이국적인 풍경의 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/d2d484b6-230d-40e1-bb61-ec287b5fa183.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '웨이브 제주 호텔&리조트' AND address = '제주특별자치도 제주시 한림읍 한림로 77');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주댕댕이파크 리조트', 'RESORT', 'WEST', '제주특별자치도 제주시 애월읍 광령평화2길 116-25', 33.4034138, 126.4508866, '자연 속에서 반려동물과 추억을 쌓는 힐링 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202509/08/784803f4-f8d0-45c1-8d50-926d31d1bb2f.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주댕댕이파크 리조트' AND address = '제주특별자치도 제주시 애월읍 광령평화2길 116-25');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주리조트', 'RESORT', 'WEST', '제주특별자치도 제주시 애월읍 가문동상4길 11', 33.4831162, 126.3929955, '제주시 애월해안도로에 위치한 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201807/30/3a2ad41e-fb6f-4303-a8ac-8d5515444fb5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주리조트' AND address = '제주특별자치도 제주시 애월읍 가문동상4길 11');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주머무리', 'RESORT', 'WEST', '제주특별자치도 서귀포시 안덕면 평화로 437-10', 33.2769758, 126.3134998, '깨끗하고 편안한 숙소, 조용하고 자연친화적인 단독콘도형 숙박시설, 가깝고 다양한 관광볼거리, 편안한 공간, 매우 조용하고 한적한 곳으로 힐링할 수 있는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202012/22/f8520603-580b-40ca-abf2-8ff9f48e5adf.webp', FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주머무리' AND address = '제주특별자치도 서귀포시 안덕면 평화로 437-10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주토비스콘도', 'RESORT', 'WEST', '제주특별자치도 제주시 애월읍 애월로1길 8', 33.4630730, 126.3117479, '제주 토비스는 ''애월''의 한담해변(패들보드, 서핑, 스쿠버, 카약)에 있는 전통있는 휴양콘도이다. 올래길이나 라이딩코스와 이어져 있고 전지훈련, 수학여행, 기업 연수 등의 편리한 입지조건을 갖추고 있다.', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/202003/23/d2c566fe-e928-4223-9dcf-f921e1461a59.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주토비스콘도' AND address = '제주특별자치도 제주시 애월읍 애월로1길 8');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '제주파크랜드', 'RESORT', 'WEST', '제주특별자치도 제주시 애월읍 상귀서길 82-20', 33.4648517, 126.4048595, '넓은 정원과 직접 체험할 수 있는 농장, 현대식 시설과 호텔식 서비스를 갖춘 제주파크랜드', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/c649a01d-3c4a-4257-a082-17341581b2ab.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '제주파크랜드' AND address = '제주특별자치도 제주시 애월읍 상귀서길 82-20');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '천상의노을', 'RESORT', 'WEST', '제주특별자치도 제주시 애월읍 애월해안로 424', 33.4738614, 126.3532006, '집 앞 도로가 올레16코스인 산책하기 좋은 작은 리조트형 펜션', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/39c0e9e8-cd42-4886-a970-5ec2252842b6.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '천상의노을' AND address = '제주특별자치도 제주시 애월읍 애월해안로 424');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '카이리조트', 'RESORT', 'WEST', '제주특별자치도 제주시 한림읍 협재2길 10', 33.3922148, 126.2409676, '한림공원과 협재해수욕장 근처에 위치한 호텔식 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7ed3c731-acb9-4c27-a10f-4909a2c620d7.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '카이리조트' AND address = '제주특별자치도 제주시 한림읍 협재2길 10');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '켄싱턴리조트 제주 한림점', 'RESORT', 'WEST', '제주특별자치도 제주시 한림읍 한림해안로 530', 33.4358200, 126.2745300, '제주시 서부, 한림해안로에 위치한 50개 객실의 소형리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/4758a647-7b71-4b6d-8286-91396fd9b897.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '켄싱턴리조트 제주 한림점' AND address = '제주특별자치도 제주시 한림읍 한림해안로 530');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '타미우스골프앤빌리지', 'RESORT', 'WEST', '제주특별자치도 제주시 애월읍 화전길 201', 33.3689588, 126.3505941, '일본 자연주의 철학자이자 코스설계자 가토슌수케의 코스디자인이 풍경화처럼 아름다운 골프장', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201912/20/f9ad3fd2-de71-4547-bf25-638a709f02c9.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '타미우스골프앤빌리지' AND address = '제주특별자치도 제주시 애월읍 화전길 201');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '테디밸리 골프앤리조트', 'RESORT', 'WEST', '제주특별자치도 서귀포시 안덕면 한창로 365', 33.2934470, 126.3489252, '아름다운 곶자왈 숲에서 느끼는 여유있는 골프텔', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/799e5b03-d210-40c3-ad57-e7fe626519c5.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '테디밸리 골프앤리조트' AND address = '제주특별자치도 서귀포시 안덕면 한창로 365');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT '흰수염고래리조트', 'RESORT', 'WEST', '제주특별자치도 제주시 애월읍 일주서로 6818', 33.4748840, 126.3805100, '아이들의 호기심을 자극하고 즐거움을 선사하는 곳', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/7f635a1b-8006-4e8b-b569-cc18cce96152.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = '흰수염고래리조트' AND address = '제주특별자치도 제주시 애월읍 일주서로 6818');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'UTS제주골프빌리지', 'RESORT', 'WEST', '제주도 제주시 애월읍 광령평화2길 170-2', 33.4309230, 126.4344000, '골퍼를 위한 미국식 목조 세컨하우스', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/0e7e4e03-80ae-4d0b-9d64-0cc3cac1237f.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'UTS제주골프빌리지' AND address = '제주도 제주시 애월읍 광령평화2길 170-2');

INSERT INTO accommodations (name, accommodation_type, region, address, latitude, longitude, description, thumbnail_url, parking_yn, use_yn)
SELECT 'Y리조트제주', 'RESORT', 'WEST', '제주특별자치도 서귀포시 안덕면 화순중앙로124번길 75', 33.2416500, 126.3336640, '화순금모레 해변과 가까운 리조트', 'https://api.cdn.visitjeju.net/photomng/thumbnailpath/201804/30/2a4034f1-2115-4ef6-a90a-f8d163ceb825.webp', TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM accommodations WHERE name = 'Y리조트제주' AND address = '제주특별자치도 서귀포시 안덕면 화순중앙로124번길 75');

COMMIT;
