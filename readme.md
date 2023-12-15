# MindSnap
Image Generator from Prompt Application

## 1. 개요
### 📖 소개
문장으로부터 이미지를 생성하여 학습에 도움이 될 수 있도록 제작된 앱입니다.
<br/>Project : 2023-09 ~ 2023-12 

### 👤 팀원
* 최혜성 ([@choi-hyeseong](https://github.com/choi-hyeseong)) : API연동, 코드 리팩토링, 클래스 설계 및 코드 작성
* 양지웅 ([@Peppertunacan](https://github.com/Peppertunacan)) : UI설계 및 코드 작성
* 최성규 ([@choi-sunggyu](https://github.com/choi-sunggyu)) : UI설계 및 코드 작성
* 한채영 ([@chaeyoung11](https://github.com/chaeyoung11)) : 회의록 작성, 코드 작성

### 🌐 관련 링크
* 노션(회의록) : https://www.notion.so/7-23857ccb328740d181ad91d9309be9cc
* 피그마(UI) : https://www.figma.com/file/oSKBYcNnNNcsLSYmDcOWFp/MindSnap

## 2. 구조

### 🕸️ MVVM
![img1 daumcdn](https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/4a4588c6-cf02-4c21-bf41-7b8a68cb70c2)<br/>
* 기존 Model - View - Controller (MVC)는 컨트롤러가 뷰와 모델과 강한 결합을, Model - View - Presenter (MVP)의 경우 Presenter가 View와 강한 결합을 갖고 있어 두 패턴은 향후 코드 수정과 같은 유지보수에 어려움이 따를 수 있습니다. 
<br/>이때, View와 Model을 연결하는 ViewModel을 이용, 뷰와 모델간의 의존성을 낮추어 유지보수에 용이한 패턴입니다.
* 이는 관심사 분리, 테스트 주도 개발(TDD)를 메인으로 한 Clean Architecture와 함께 사용되곤 합니다.

##### Clean Architecture

![02(2)](https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/489f5934-554a-4e46-a462-c7568d3c62a9)
* 위 MVVM패턴과 함께 사용되어 테스트 용이성, 새로운 개발자가 합류해도 쉽게 코드를 이해하고 수정할 수 있는 구조로써 많이 애용되고 있습니다.
* 또한 오류가 발생해도 서로 결합도가 낮아 영향받는 코드가 적어 쉽게 수정할 수 있습니다.

### 🧍 Use Case
![스크린샷 2023-12-15 171616](https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/d78fc595-29da-41c2-b079-a6c998497777)
* 앱을 구성하는 3가지의 서비스 (갤러리, 유저 데이터, 이미지 생성)을 액터로 다음과 같은 유즈케이스를 도출했습니다.
  <br/><br/>
* 튜토리얼 여부를 관리하는 유저 서비스는 해당 정보를 가져오고, 저장할 수 있습니다.
* 이미지 생성 서비스는 프롬프트로부터 이미지를 생성하여 반환하는 기능을 갖고 있습니다.
* 갤러리 서비스는 해당 앱에서 생성된 이미지를 저장하고, 불러와 화면에 보여주고, 이를 공유하는 기능을 갖고 있습니다.

### 🏃 Activity Diagram
<img src="https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/f997cb77-1208-4146-96be-0ee72b9672a5" height="500"/>
* 위 유스케이스로부터 활동 다이어그램은 다음과 같습니다.

## 3. 상세 구현

### 🏢 클래스 구조

## 4. 라이브러리

### ✔ Coroutine

### 📱Retrofit

### 🗡️ Hilt

### 🛠️ Mockk

## 5. Error handling