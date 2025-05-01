# 프로젝트 명: One Ticket

## 프로젝트 소개

**실시간 예매 처리를 위한 MSA 기반 티켓팅 시스템**

- With-One-Voice는 사용자들에게 원활하고 안정적인 티켓 구매 경험을 제공하기 위해 설계된 대규모 티켓팅 서비스 입니다.

## 프로젝트 목표

### 1. 실시간 트래픽 처리 경험

- 공연/영화/이벤트 등의 인기 티켓에 대해 **대량 트래픽 상황에서도 안정적인 예매 기능 제공**
- 동시성 제어, 선착순 처리, 비정상 요청 방지 등 **현실성 있는 로직 구현**

### 2. MSA 아키텍처 설계 및 구현

- 사용자 인증, 공연 정보, 좌석 관리, 예매 처리, 결제, 알림 등을 **도메인 단위로 분리**
- 각 마이크로서비스는 독립적으로 배포 가능하고, 서로 느슨하게 연결되도록 설계

### 3. 비동기 메시징 기반 이벤트 처리

- Kafka 또는 Redis Stream 등을 통해 서비스 간 **비동기 이벤트 흐름 구현**
- 예매 성공 → 결제 요청 → 알림 전송 등 일련의 흐름을 메시지 기반으로 처리하여 **확장성과 복원력 확보**

### **4. 정합성 있는 동시성 제어 로직 구현**

- 다수의 사용자가 동시에 동일 좌석을 예매하려 할 때 발생하는 문제를 해결
- Redis의 분산 락, 좌석 예약 상태 캐싱 등을 활용하여 **데이터 정합성 보장**

### 5. 장애 대응 및 복구 설계 경험

- 예매 중 결제 실패 시 롤백 처리
- 보상 트랜잭션 또는 SAGA 패턴을 활용한 분산 트랜잭션 처리 설계

## 인프라 설계도

- 물리적 구조

![infra1](https://github.com/user-attachments/assets/cedf0412-f475-41db-8410-0b3deeec44fb)

- 논리적 구조

![infra2](https://github.com/user-attachments/assets/9959d1d3-8538-4032-a4ad-0c41ee4f66ca)

## 주요 기능

- 사용자
    - 회원가입, 로그인 (JWT 기반)

- 공연
    - 공연 정보 등록 및 조회
    - 공연 회차 생성 시,  좌석 생성 kafaka event 생성

- 좌석
    - 실시간 좌석 조회
    - 좌석 선점 기능 (Redisson 분산락)

- 예매
    - 티켓 생성 및 상태 관리 (WAITING_PAYMENT, COMPLETED 등)
    - 중복 예매 방지 로직

- 결제
    - KakaoPay 연동 통한 결제 요청/승인
    - 결제 실패 시 티켓 자동 취소

- 알림
    - 예매 성공/실패 시 Email or Discord 알림 전송
    - Kafka 이벤트 기반 비동기 전송

- 모니터링
    - Prometheus + Grafana로 트래픽, 지표 시각화
    - Loki + Promtail로 중앙 집중형 로그 수집
 
## 적용 기술

## 기술적 의사 결정

- Redis vs DB-based Lock
  - DB 락은 분산 환경에서 신뢰 어려움 → Redis로 대체
  - Redisson 사용으로 분산 환경에서 안전한 락 확보

- Kafka vs RabbitMQ
  - Kafka 선택 이유: 대량 메시지 처리에 강함, 로그 재처리 가능
  - RabbitMQ는 메시지 순서 보장이 필요하거나 라우팅이 중요할 경우 고려 가능

- GitHub Actions vs Jenkins
  - GitHub Actions는 GitHub과의 통합성과 관리 편의성 면에서 우위
  - 도메인별로 분리된 CI 파이프라인
  - Docker 빌드 + Kubernetes 배포 자동화

## 트러블 슈팅

## CONTRIBUTORS



<h3 align="center">✨ Tech Stack ✨</h3>
<div align="center">

  <!-- Frontend -->
  <img src="https://img.shields.io/badge/TypeScript-3178C6.svg?style=for-the-badge&logo=typescript&logoColor=white"/>
  <img src="https://img.shields.io/badge/React-61DAFB.svg?style=for-the-badge&logo=react&logoColor=black"/>

  <!-- Backend -->
  <img src="https://img.shields.io/badge/Java-ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20Security-6DB33F.svg?style=for-the-badge&logo=springsecurity&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20WebFlux-6DB33F.svg?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-59666C.svg?style=for-the-badge&logo=hibernate&logoColor=white"/>

  <!-- MSA / DevOps -->
  <img src="https://img.shields.io/badge/Spring%20Cloud-6DB33F.svg?style=for-the-badge&logo=spring&logoColor=white"/>
  <img src="https://img.shields.io/badge/Eureka-4FC08D.svg?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Feign-0052CC.svg?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Docker-2496ED.svg?style=for-the-badge&logo=docker&logoColor=white"/>
  <img src="https://img.shields.io/badge/GitHub%20Actions-2088FF.svg?style=for-the-badge&logo=githubactions&logoColor=white"/>
  <img src="https://img.shields.io/badge/Kubernetes-326CE5.svg?style=for-the-badge&logo=kubernetes&logoColor=white"/>
  <img src="https://img.shields.io/badge/Kops-326CE5.svg?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Helm-0F1689.svg?style=for-the-badge&logo=helm&logoColor=white"/>
  <img src="https://img.shields.io/badge/ArgoCD-FB7A24.svg?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/AWS-232F3E.svg?style=for-the-badge&logo=amazonaws&logoColor=white"/>

  <!-- DB / Cache -->
  <img src="https://img.shields.io/badge/PostgreSQL-4169E1.svg?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Redis-DC382D.svg?style=for-the-badge&logo=redis&logoColor=white"/>

  <!-- Messaging -->
  <img src="https://img.shields.io/badge/Kafka-231F20.svg?style=for-the-badge&logo=apachekafka&logoColor=white"/>
  <img src="https://img.shields.io/badge/SMTP-FF8C00.svg?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Discord-5865F2.svg?style=for-the-badge&logo=discord&logoColor=white"/>

  <!-- Monitoring -->
  <img src="https://img.shields.io/badge/Prometheus-E6522C.svg?style=for-the-badge&logo=prometheus&logoColor=white"/>
  <img src="https://img.shields.io/badge/Grafana-F46800.svg?style=for-the-badge&logo=grafana&logoColor=white"/>
  <img src="https://img.shields.io/badge/JMeter-D22128.svg?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Loki-0E5C89.svg?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Zipkin-000000.svg?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/ArgoCD_UI-FB7A24.svg?style=for-the-badge"/>

  <!-- Collaboration -->
  <img src="https://img.shields.io/badge/GitHub-181717.svg?style=for-the-badge&logo=github&logoColor=white"/>
  <img src="https://img.shields.io/badge/Notion-000000.svg?style=for-the-badge&logo=notion&logoColor=white"/>
  <img src="https://img.shields.io/badge/Swagger-85EA2D.svg?style=for-the-badge&logo=swagger&logoColor=black"/>

</div>

