# ✈️ 서울시 여행 자동 일정 플래너

사용자가 입력한 서울시 내 여행지와 일정에 따라 최적의 여행 일정을 자동으로 생성해주는 서비스입니다.

<br>

## 🔸 프로젝트 기능 및 설계

- `회원가입 기능`
    - 소셜 로그인 API : 사용자는 소셜 로그인 API를 통해 회원 가입이 가능합니다.
    - 중복 허용 금지 : 회원가입 시 아이디 중복을 금지하며, Spring JPA로 DB 내 아이디를 조회하고, 중복 여부를 확인합니다.

<br>

- `로그인 기능`
    - 로그인 : 사용자는 로그인을 할 수 있으며, 로그인 시 회원가입 때 사용한 아이디와 패스워드가 일치해야합니다.
    - 소셜 로그인 : Spring Security와 Spring OAuth2 클라이언트를 통해 구현하며, 엑세스 토큰을 요청하여 소셜 API로부터 사용자를 인증하고 세션을 생성합니다.

<br>

- `여행지 및 일정 입력`
  - 사용자 입력
    - 입력 기능 : 사용자는 여행 기간과 여행지를 입력할 수 있습니다.
    
    - 검색 기능 : 사용자가 검색창에 여행지를 입력하면 해당 지역의 인기 여행지와 식당 데이터를 검색할 수 있으며 ElasticSearch을 사용하여 구현합니다.
  
    - 여행지명 기반 지역 정보 획득 : 사용자가 검색창에 입력한 여행지명을 통해 해당 지역의 좌표(위도 및 경도)를 구글 Places API를 사용하여 얻습니다.
    
    - 인근 지역의 범위 설정 : 얻은 좌표를 기준으로 반경 5Km 내의 인기 관광지와 식당을 검색합니다.
    
    - 인기 여행지 및 식당 데이터 수집 : 구글 Places API를 통해 수집된 데이터는 리뷰 평점에 따라 정렬됩니다.
    
    - ElasticSearch : 서울시 공공 데이터 API와 구글 Places API를 통해 수집하여 ElasticSearch에 인덱싱합니다. 이를 위해 데이터를 JSON 형태로 변환하여 인덱스에 저장합니다.
    
        <br> 
  
  - 데이터 수집 및 저장
    - 서울시 여행지와 식당 데이터 수집은 공공 데이터 API(서울 열린 데이터 광장 Open API)를 사용하고, 위치 및 리뷰 평점 정보는 구글 Places API를 통해 호출합니다.

    <br>
    
  - 경유지 선택
    - 사용자가 반드시 포함해야 하는 경유지를 선택하면 검색한 여행지와는 다른 엔티티 구조로 저장하며 최종 일정을 생성 시 경유지와 함께 포함하여 경로를 생성합니다.
    - 경유지는 별도로 관리되어 최종 일정 생성 시 포함됩니다. 다익스트라 알고리즘을 사용하여 여행지와 식당 데이터를 노드로 정의하고, 각 장소 간의 거리를 엣지로 정의하여 경유지를 포함한 경로를 생성합니다.

    <br>
  
  - 외부 API
    - 서울 열린데이터 광장 API : 오픈 API 인증키를 신청하여 발급받고, 서울시 관광 명소 및 식당에 대한 정보를 수집합니다. 
    
    <br>

    - **서울시 관광 식당 인허가 정보 API 저장 필드**
      - 업소명 (BPLCNM)
      - 영업상태명 (TRDSTATENM)
      - 상세영업상태명 (DTLSTATENM)
      - 도로명 전체 주소 (RDNWHLADDR)
      - 좌표 (X, Y)

    <br>

    - **서울시 관광 명소 정보 API 저장 필드**
      - 관광명소 이름 (POST_SJ)
      - 주소 (ADDRESS)
      - 도로명 주소 (NEW_ADDRESS)

    <br>
  
    - Google Places API : Google Cloud에서 Places API 인증키를 발급 받아 위치 및 리뷰 평점 정보를 수집합니다.
    
    <br>

    - **Google Places API 저장 필드**
      - 장소명 (name)
      - 포맷된 주소 (formatted_address)
      - 위도 (lat)
      - 경도 (lng)
      - 장소의 평균 리뷰 평점 (rating)
      - 리뷰 리스트 (reviews)

<br>

- `체크리스트`
  - 준비물 구분 : 여행지에 알맞는 필요한 준비물을 체크 방식으로 구분합니다.
  - 체크리스트 CRUD : 체크리스트 항목에 대한 생성, 조회, 수정, 삭제 기능을 구현합니다.
  - 진행률 표시 : 체크리스트 항목의 준비 완료 진행률을 표시합니다.
  - 메모 작성 기능 : 추가 메모를 작성할 수 있습니다.(예약 번호, 여행지 관련 정보 등)

<br>

- `회원 관리`
  - 기본 회원 정보 : 기본 회원 정보를 표시합니다.
  - 회원 정보 CRUD : 회원 정보에 대한 생성, 조회, 수정, 삭제 기능을 구현합니다.
  - 여행 히스토리 : 사용자가 최종 선택한 일정을 여행 히스토리에 저장합니다. 히스토리에 저장하는 데이터는 최종 선택하는 경우에만 저장되고, 사용자가 최종 일정을 선택하지 않으면 일정은 저장되지 않고 사라집니다.
  - 즐겨찾기 기능 : 여행 히스토리에서 일정에 즐겨찾기 표시 기능을 구현합니다.

<br>

- `알림`
  - 일정 알림 : 일정 시작 및 종료 알림을 여행 일정 ±1일 00시 기준으로 Spring Scheduler를 통해 사용자의 이메일로 전송합니다.
  - 체크리스트 알림 : 체크리스트 항목에 미완료된 내용도 같이 포함하여 전송합니다.

<br>

- `데이터 동기화`
  - ElasticSearch에 저장된 데이터와 외부 API에서 제공하는 데이터를 주기적으로 비교하여 최신성을 유지합니다. 
  - API 호출 결과와 ES에 저장된 데이터가 다를 경우, 유효성 검사를 통해 주기적(1일 1회)으로 외부 API를 호출하여 최신화를 유지합니다.

<br>

- `데이터 흐름`
  - 사용자가 특정 여행지를 검색할 때, 먼저 ElasticSearch에서 데이터를 검색합니다.
  - ElasticSearch에 데이터가 존재하면, 이를 반환합니다.
  - ElasticSearch에 데이터가 없으면, 외부 API를 호출하여 데이터를 수집합니다.
  - 수집된 데이터를 ElasticSearch에 인덱싱합니다.
  - 사용자는 최신 데이터를 기반으로 검색 결과를 받습니다.

<br>

## 🔸 ERD
![erd](https://github.com/user-attachments/assets/689836dd-8cde-459a-8e46-dfff1eaa1e2a)

<br>

### 🔹 Tech Stack
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
<img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"> 
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white">
<img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=CSS3&logoColor=white">
<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=black">
<img src="https://img.shields.io/badge/-ElasticSearch-005571?style=for-the-badge&logo=elasticsearch&logoColor=white">
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>
