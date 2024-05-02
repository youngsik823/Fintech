# 계좌 시스템
CRUD를 활용한 계좌 관리 시스템 API
## [프로그램 주요 기능]
## 1.회원가입

- [x] 이름, 이메일, 휴대폰번호(11자리), 비밀번호를 입력해야 회원가입을 할 수 있다.(권한은 default = USER)
- [x] 이메일 및 휴대폰번호는 중복 불가하다.

## 2. 로그인

- [x] 회원가입 한 이메일과 비밀번호를 입력 시 로그인을 할 수 있다.
- [ ] 로그인 할 때 JWT를 활용한 인터셉터로 검증한 후 사용 가능하다.

## 3. 계좌 관리 (생성 / 삭제 / 계좌 조회)
- [ ] **생성 실패** : 사용자가 없는 경우, 계좌가 5개(사용자당 최대 보유 계좌) 인경우 실패를 응답한다.
- [ ] **생성 성공** : 회원가입 시 은행명(default = APPLE), 사용자명(이름), 계좌번호 (10자리 랜덤 숫자)  인경우 성공을 응답한다.
- [ ] **조회 실패** : 사용자가 없는 경우 실패 응답한다.
- [ ] **조회 성공** : 계좌 생성일 순으로 (계좌번호, 잔액, 사용자명(이름), 계좌상태 등) 정보를 JSON LIST 형식으로 응답한다.
- [ ] **삭제 실패** : 사용자 없는 경우, 사용자 이메일과 계좌 시용지가 다른 경우, 계좌가 이미 삭제 상태인 경우, 잔액이 있는 경우 실패 응답한다.
- [ ] **삭제 성공** : 삭제 시 데이터베이스에서 삭제되지 않고 삭제 여부가 저장된다.
- [ ] 다른 타 은행계좌는 이미 생성 되었다고 가정을 한다.

## 4. 거래 관리 및 내역 조회(수정 / 조회)
- [ ] **거래 실패** : 거래 금액이 사용자 잔액보다 큰 경우, 사용자가 없는 경우, 사용자 아이디와 계좌 사용자가 다른 경우, 계좌가 이미 삭제 상태인 경우 실패 응답한다.
- [ ] **거래 성공** : (계좌번호, 사용자명(이름), 거래금액 등) 정보를 JSON 형식으로 응답한다. 
- [ ] **내역조회 실패** : 거래 내역이 없는경우 실패 응답한다.
- [ ] **내역조회 성공** : 최신 순으로 정렬 및 paging 처리가 되며 페이지 당 7개씩 (계좌번호, 거래 금액, 거래 대상, 거래 일시 등) 정보를 확인 할 수 있다.

## [추가 구현하고 싶은 기능]
- [ ] 회원가입 시 이메일 인증 구현

## Tech Stack
<div>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
</div>

<div>
  <span style="font-size:24px;">라이브러리 : </span>
  <span style="font-size:24px;">Spring Web, Spring Data JPA, Lombok, Spring Security, JWT</span>
</div>

## [ERD 데이터 모델링]
![erd](https://github.com/youngsik823/Fintech/assets/109953656/25dc898b-0681-4d9d-bd4b-b1eeb5e3def1)


