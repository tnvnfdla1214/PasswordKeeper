# PasswordKeeper - 비밀번호 저장소

중장년층을 위한 간편하고 안전한 비밀번호 관리 앱

## 프로젝트 개요

메모장처럼 간편하게 사용하면서도 AES256 암호화로 안전하게 비밀번호를 관리할 수 있는 Android 앱입니다.

## 기술 스택

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Database**: Room
- **Encryption**: AES256 (Android Keystore)
- **Authentication**: BiometricPrompt API

## 주요 기능

- 로컬 저장소 기반 (서버 없음)
- AES256 암호화
- 지문인식 / 마스터 비밀번호 인증
- 전체 필드 검색
- 원클릭 복사
- 스마트 정렬 (최근 사용 우선)

## 프로젝트 구조

```
app/
├── data/
│   ├── local/
│   │   ├── dao/           # Room DAO
│   │   ├── entity/        # Room Entity
│   │   └── AppDatabase.kt
│   └── repository/        # Repository 구현
├── domain/
│   ├── model/            # Domain 모델
│   ├── repository/       # Repository 인터페이스
│   └── usecase/          # Use Cases
├── presentation/
│   ├── ui/
│   │   ├── auth/        # 인증 화면
│   │   ├── home/        # 홈 화면
│   │   ├── detail/      # 상세 화면
│   │   ├── form/        # 추가/수정 화면
│   │   └── theme/       # Material3 테마
│   ├── viewmodel/       # ViewModels
│   └── navigation/      # Navigation
├── di/                  # Hilt 모듈
└── util/               # 유틸리티 (암호화, 생체인증)
```

## 빌드 방법

1. Android Studio 설치
2. 프로젝트 열기
3. Gradle Sync
4. Run

## 최소 요구사항

- Android API 26 (Android 8.0) 이상
- 지문 인식 센서 (선택)
