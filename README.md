# 📌 다예약 (Dayeyak)  
<img width="880" height="349" alt="image" src="https://github.com/user-attachments/assets/645f2d98-4a91-46b6-8acd-5b597256736e" />
</br>
</br>

## 🗂️ Project Overview
- **프로젝트 이름**: 다예약  
- **진행 기간**: 2025.09.01 ~ 2025.10.02  
- **프로젝트 설명**:  Micro Service Architecture 기반 **통합 예약 서비스**
- **배포 주소** : http://54.180.116.224:10700/alarms/
- **팀 노션** : [🔗](https://www.notion.so/teamsparta/4-2612dc3ef51480679e40c1af55c69c0d)





</br>

## ⚙️ Tech Stack
- <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>  
- <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/>  
- <img src="https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white"/>  
- <img src="https://img.shields.io/badge/SSE-FF6F00?style=for-the-badge&logo=signal&logoColor=white"/>  
- <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white"/>  
- <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white"/>  

</br>

## 🏗️ 프로젝트 구조
```bash
📦 alarm-service
 ┣ 📂 auth           # 사용자 인증/인가 처리
 ┣ 📂 config         # 환경설정, Kafka 설정
 ┣ 📂 domain.alarm   # 알람 도메인 (Entity, Repository, Service, Controller)
 ┣ 📂 exception      # 예외 처리 핸들러
 ┣ 📂 kafka          # Kafka Message를 위한 DTO
 ┣ 📂 utils          # 공통 유틸리티

```
</br>

##  🔑 알람 로직 흐름
1️⃣ ***Kafaka Message Consume***

  예약, 백오피스, 웨이팅 등 다른 서비스가 발행한 Kafka Message를 정해둔 topic을 통해 Consume

2️⃣***알람 DB 저장***

  Message Queue에 포함된 DTO객체를 기반으로 새로운 알람 데이터 생성/저장

3️⃣ ***SSE를 통한 실시간 알람 발송***
 
  Message Queue에 포함된 user id를 통해 미리 생성된 SSE Emitter에게 이벤트 발송

</br>

## 🎯 알람 플로우 차트
<img width="1013" height="606" alt="image" src="https://github.com/user-attachments/assets/8bb08877-4abd-469c-a38c-f7efe20f63ba" />

</br>
</br>

## 📡 알람 동작 방법 및 시연 영상
사용자가 SSE로 알람을 구독할 때 Last-Event-Id를 함께 넘기면 연결이 끊어진 구간 동안 발생했던 이벤트를 다시 받을 수 있습니다.

```
GET ip:port/alarms/subscribe?Last-Event-Id={id}
```

</br>

https://github.com/user-attachments/assets/ac8ec20b-aa9d-4f45-b9fe-0352b68befe4


</br>

