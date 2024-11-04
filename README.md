# 무신사 가격 비교 API

## 개요

이 프로젝트는 제품, 브랜드, 카테고리의 가격 비교를 위한 API를 제공합니다. 사용자는 브랜드 및 제품에 대해 CRUD 작업을 수행할 수 있으며, 카테고리별 최저가 및 최고가 상품을 조회하고, 특정 카테고리의 가격
비교를 할 수 있습니다. 이 프로젝트는 **Spring Boot**로 구현되었으며 RESTful API 아키텍처를 따릅니다.

---

## 구현 범위

이 구현은 다음과 같은 기능을 포함합니다:

- **브랜드 관리**: 브랜드 추가, 수정 및 삭제.
- **카테고리 관리**: 모든 카테고리 조회.
- **제품 관리**: 제품 추가, 수정 및 삭제.
- **가격 비교**:
    - 모든 카테고리에서 최저가 제품 조회.
    - 모든 카테고리에서 최저가 브랜드 조회.
    - 특정 카테고리의 가격 비교(최저가/최고가) 조회.

### 서비스 계층

1. **BrandService**
    - `saveOrUpdateBrand(BrandRequest brandRequest)`: 브랜드 추가 또는 업데이트. ID가 있으면 해당 브랜드를 업데이트하고, 없으면 새로 생성합니다.
    - `deleteBrand(Long id)`: ID로 브랜드 삭제. 브랜드가 없으면 예외를 발생시킵니다.

2. **PriceService**
    - `getCategoryPriceComparison(Category category)`: 특정 카테고리의 최저가 및 최고가 상품을 조회합니다. `MinMaxPriceEntity`에서 먼저 조회하고 없으면
      `ProductRepository`에서 직접 조회합니다.
    - `getMinMaxPrices()`: 모든 브랜드와 카테고리에 대한 최소/최대 가격 정보를 반환합니다.

3. **ProductService**
    - `getCheapestPriceOfAllCategory()`: 모든 카테고리에서 최저가 상품을 조회합니다. `MinMaxPriceEntity`에서 먼저 조회하고 없으면 전체 상품에서 계산합니다.
    - `getCheapestBrandForAllCategories()`: 모든 카테고리에 대해 최저가를 제공하는 브랜드를 조회합니다.
    - `saveOrUpdateProduct(ProductRequest productRequest)`: 제품 추가 또는 업데이트. ID가 있으면 해당 제품을 업데이트하고, 없으면 새로 생성합니다.
    - `deleteProduct(Long productId)`: ID로 제품 삭제. 제품이 없으면 예외를 발생시킵니다.

---

## 코드 빌드, 테스트, 실행 방법

### 사전 준비 사항

프로젝트를 빌드하고 실행하기 전에 다음 소프트웨어가 설치되어 있어야 합니다:

- Java 17 이상
- Gradle (빌드 도구)
- H2 데이터베이스 (인메모리 DB)

### 빌드 방법

1. 저장소를 클론:
   ```bash
   git clone https://github.com/jeonjhong/mss.git
   ```

2. Gradle로 프로젝트 빌드
    ```bash
   ./gradlew clean build
   ```
3. 유닛 테스트 실행:
    ```bash
    ./gradlew test
   ```
4. 어플리케이션 실행
   4.1
    ``` bash
   ./gradlew bootRun
   ``` 
   4.2
    ``` bash
   java -jar build/libs/musinsa-price-comparison.jar
   ``` 