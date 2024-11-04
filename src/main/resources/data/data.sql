INSERT INTO Brand (brand_name, created_at, updated_at)
VALUES ('A', now(), now()),
       ('B', now(), now()),
       ('C', now(), now()),
       ('D', now(), now()),
       ('E', now(), now()),
       ('F', now(), now()),
       ('G', now(), now()),
       ('H', now(), now()),
       ('I', now(), now());

INSERT INTO Category (category_name, created_at, updated_at)
VALUES ('TOP', now(), now()),
       ('OUTER', now(), now()),
       ('PANTS', now(), now()),
       ('SNEAKERS', now(), now()),
       ('BAG', now(), now()),
       ('HAT', now(), now()),
       ('SOCKS', now(), now()),
       ('ACCESSORY', now(), now());


INSERT INTO Product (brand_id, category_id, price, created_at, updated_at)
VALUES (1, 1, 11200, NOW(), NOW()),
       (1, 2, 5500, NOW(), NOW()),
       (1, 3, 4200, NOW(), NOW()),
       (1, 4, 9000, NOW(), NOW()),
       (1, 5, 2000, NOW(), NOW()),
       (1, 6, 1700, NOW(), NOW()),
       (1, 7, 1800, NOW(), NOW()),
       (1, 8, 2300, NOW(), NOW());

INSERT INTO Product (brand_id, category_id, price, created_at, updated_at)
VALUES (2, 1, 10500, NOW(), NOW()),
       (2, 2, 5900, NOW(), NOW()),
       (2, 3, 3800, NOW(), NOW()),
       (2, 4, 9100, NOW(), NOW()),
       (2, 5, 2100, NOW(), NOW()),
       (2, 6, 2000, NOW(), NOW()),
       (2, 7, 2000, NOW(), NOW()),
       (2, 8, 2200, NOW(), NOW());

INSERT INTO Product (brand_id, category_id, price, created_at, updated_at)
VALUES (3, 1, 10000, NOW(), NOW()), -- 상의
       (3, 2, 6200, NOW(), NOW()),  -- 아우터
       (3, 3, 3300, NOW(), NOW()),  -- 바지
       (3, 4, 9200, NOW(), NOW()),  -- 스니커즈
       (3, 5, 2200, NOW(), NOW()),  -- 가방
       (3, 6, 1900, NOW(), NOW()),  -- 모자
       (3, 7, 2200, NOW(), NOW()),  -- 양말
       (3, 8, 2100, NOW(), NOW());

-- 브랜드 D
INSERT INTO Product (brand_id, category_id, price, created_at, updated_at)
VALUES (4, 1, 10100, NOW(), NOW()), -- 상의
       (4, 2, 5100, NOW(), NOW()),  -- 아우터
       (4, 3, 3000, NOW(), NOW()),  -- 바지
       (4, 4, 9500, NOW(), NOW()),  -- 스니커즈
       (4, 5, 2500, NOW(), NOW()),  -- 가방
       (4, 6, 1500, NOW(), NOW()),  -- 모자
       (4, 7, 2400, NOW(), NOW()),  -- 양말
       (4, 8, 2000, NOW(), NOW());
-- 액세서리

-- 브랜드 E
INSERT INTO Product (brand_id, category_id, price, created_at, updated_at)
VALUES (5, 1, 10700, NOW(), NOW()), -- 상의
       (5, 2, 5000, NOW(), NOW()),  -- 아우터
       (5, 3, 3800, NOW(), NOW()),  -- 바지
       (5, 4, 9900, NOW(), NOW()),  -- 스니커즈
       (5, 5, 2300, NOW(), NOW()),  -- 가방
       (5, 6, 1800, NOW(), NOW()),  -- 모자
       (5, 7, 2100, NOW(), NOW()),  -- 양말
       (5, 8, 2100, NOW(), NOW());
-- 액세서리

-- 브랜드 F
INSERT INTO Product (brand_id, category_id, price, created_at, updated_at)
VALUES (6, 1, 11200, NOW(), NOW()), -- 상의
       (6, 2, 7200, NOW(), NOW()),  -- 아우터
       (6, 3, 4000, NOW(), NOW()),  -- 바지
       (6, 4, 9300, NOW(), NOW()),  -- 스니커즈
       (6, 5, 2100, NOW(), NOW()),  -- 가방
       (6, 6, 1600, NOW(), NOW()),  -- 모자
       (6, 7, 2300, NOW(), NOW()),  -- 양말
       (6, 8, 1900, NOW(), NOW());
-- 액세서리

-- 브랜드 G
INSERT INTO Product (brand_id, category_id, price, created_at, updated_at)
VALUES (7, 1, 10500, NOW(), NOW()), -- 상의
       (7, 2, 5800, NOW(), NOW()),  -- 아우터
       (7, 3, 3900, NOW(), NOW()),  -- 바지
       (7, 4, 9000, NOW(), NOW()),  -- 스니커즈
       (7, 5, 2200, NOW(), NOW()),  -- 가방
       (7, 6, 1700, NOW(), NOW()),  -- 모자
       (7, 7, 2100, NOW(), NOW()),  -- 양말
       (7, 8, 2000, NOW(), NOW());
-- 액세서리

-- 브랜드 H
INSERT INTO Product (brand_id, category_id, price, created_at, updated_at)
VALUES (8, 1, 10800, NOW(), NOW()), -- 상의
       (8, 2, 6300, NOW(), NOW()),  -- 아우터
       (8, 3, 3100, NOW(), NOW()),  -- 바지
       (8, 4, 9700, NOW(), NOW()),  -- 스니커즈
       (8, 5, 2100, NOW(), NOW()),  -- 가방
       (8, 6, 1600, NOW(), NOW()),  -- 모자
       (8, 7, 2000, NOW(), NOW()),  -- 양말
       (8, 8, 2000, NOW(), NOW());
-- 액세서리

-- 브랜드 I
INSERT INTO Product (brand_id, category_id, price, created_at, updated_at)
VALUES (9, 1, 11400, NOW(), NOW()), -- 상의
       (9, 2, 6700, NOW(), NOW()),  -- 아우터
       (9, 3, 3200, NOW(), NOW()),  -- 바지
       (9, 4, 9500, NOW(), NOW()),  -- 스니커즈
       (9, 5, 2400, NOW(), NOW()),  -- 가방
       (9, 6, 1700, NOW(), NOW()),  -- 모자
       (9, 7, 1700, NOW(), NOW()),  -- 양말
       (9, 8, 2400, NOW(), NOW()); -- 액세서리


