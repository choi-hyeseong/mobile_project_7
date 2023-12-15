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

* 기존 Model - View - Controller (MVC)는 컨트롤러가 뷰와 모델과 강한 결합을, Model - View - Presenter (MVP)의 경우
  Presenter가 View와 강한 결합을 갖고 있어 두 패턴은 향후 코드 수정과 같은 유지보수에 어려움이 따를 수 있습니다.
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

![스크린샷 2023-12-15 232027](https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/eb45e8b2-7461-4ab0-bc7f-364d8169233f)

* MVVM과 Clean Architecture를 사용하여 클래스를 구성하였습니다.<br/>

프래그먼트 기반으로 제작되어 하나의 액티비티에 여러개의 프래그먼트가 전환되는 구조로, 기존 여러개의 액티비티로 제작된 앱들에 비해
프로세스간 통신(IPC)없이 서로 데이터를 주고 받기 수월하고, UI 또한 상황에 맞게 쉽게 변경할 수 있는 구조입니다.

* 위 클래스들은 용도에 따라 다음과 같이 분리할 수 있습니다.

##### 최초 액티비티

* View (MainActivity)
* ViewModel (MainViewModel)
* Repository (UserRepository)
* UseCase(GetUserFirstJoined)

##### 튜토리얼

* View (TutorialFragment)
* ViewModel (CoachMarkViewModel)
* Repository (UserRepository)
* UseCase(SaveUserVisited)

##### 갤러리

* View (GalleryFragment)
* ViewModel (GalleryViewModel)
* Repository (GalleryRepository)
* UseCase (GetAllImages, ShareImages, ExistImage)
* Model (Image)

#### 프롬프트

* View (PromptFragment)
* ViewModel (PromptViewModel)

#### 결과

* View (ResultFragment)
* ViewModel (ResultViewModel)
* Repository (GalleryRepository, ImageGenRepository)
* UseCase (GenerateImage, SaveImage, ShareImage, ExistImage)
* Model (Image)

### 🖥️ 동작 과정

##### 액티비티
* MainActivity

```kotlin
  viewModel.isFirstJoined().observe(this) { tutorial ->
    if (tutorial)
        supportFragmentManager.beginTransaction().replace(R.id.frame, WelcomeFragment()).commit()
    else
        navigateToGallery()
}
```
맨 처음 onCreate() 메소드에서 MainViewModel의 isFirstJoined를 체크하여 튜토리얼이 진행되지 않은경우 튜토리얼 프래그먼트로 전환하고, 튜토리얼을 완료한경우 갤러리 프래그먼트로 전환합니다.

* MainViewModel
``` kotlin
@HiltViewModel
class MainViewModel @Inject constructor(getUserFirstJoined: GetUserFirstJoined) : ViewModel() {

    private val firstJoinLiveData : LiveData<Boolean> = MutableLiveData(getUserFirstJoined.isFirstJoined())

    fun isFirstJoined() : LiveData<Boolean> {
        return firstJoinLiveData
    }
} 
```
해당 뷰모델에서는 로드시 유저의 튜토리얼 여부를 LiveData의 형태로 가져와 View에서 관측할 수 있게 firstJoinLiveData를 제공합니다.

위 액티비티에서 볼 수 있듯이 뷰에서는 뷰모델의 LiveData를 observe하여 최초로 관측하거나, 값이 변경될때 이를 읽어올 수 있습니다. 또한, LiveData의 특성상 화면 회전과 같은 상황에서 다시 onCreate될때 저장된 값을 전달 해 주어 데이터를 보존할 수 있습니다.
* GetUserFirstJoined
```kotlin
class GetUserFirstJoined(private val userRepository: UserRepository) {

    fun isFirstJoined() : Boolean {
        return userRepository.isFirstJoined()
    }
}
```
해당 유스케이스는 유저 레포지토리에서 유저가 튜토리얼을 완료했는지(첫 접속인지 아닌지)를 반환합니다.
* UserRepository
```kotlin
interface UserRepository {
    //유저가 튜토리얼을 완료했는지 확인하는 레포지토리
    fun isFirstJoined() : Boolean

    fun saveVisit() //튜토리얼 완료

}

class PreferenceUserRepository(private val userDao: UserDao) : UserRepository {
  override fun isFirstJoined(): Boolean {
    return userDao.isFirstJoined()
  }

  override fun saveVisit() {
    return userDao.saveVisit()
  }
}

private const val JOIN = "FIRST_JOIN"
class PreferenceUserDao(private val preferences: SharedPreferences) : UserDao {

  override fun isFirstJoined(): Boolean {
    return preferences.getBoolean(JOIN, true) //preference에 저장된 값이 없을경우 true
  }

  override fun saveVisit() {
    preferences.edit().putBoolean(JOIN, false).apply()
  }
}
```
유저 레포지토리는 내부 저장소인 SharedPreferences를 사용하는 구현체를 이용하여 유저의 튜토리얼 여부를 관리할 수 있습니다.<br/>

이때 액티비티는 직접적으로 레포지토리에 접근하여 정보를 얻는것이 아닌, 유스케이스를 거쳐 데이터에 접근하므로 추후 레포지토리의 메소드나 구현체가 변경되더라도 유스케이스 내부의 코드만 수정하면 되니 액티비티의 코드를 수정할 필요가 없다는 장점이 있습니다.

유저 또한 User 클래스를 만들어 Model로 사용할 수 있으나, 튜토리얼 여부 하나만 담고 있기엔 애매해서 따로 구성하지 않았습니다. 추후 추가적인 데이터의 저장, 관리가 필요할경우 User 모델을 생성하는것도 좋다고 생각합니다.

* ActivityCallBack
``` kotlin
interface ActivityCallback {

    fun navigateToTutorial()

    fun navigateToCoach()

    //튜토리얼 완료해서 프래그먼트 이동
    fun navigateToGallery()

    fun navigateToResult(prompt : String, artStyle: ArtStyle)

    fun navigateToPrompt() {
        navigateToPrompt(null, null)
    }

    fun requestFinish()

    fun navigateToPrompt(prompt: String?, artStyle: ArtStyle?)
}

class MainActivity : AppCompatActivity(), ActivityCallback {
    override fun navigateToTutorial() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, TutorialFragment()).commit()
    }

    override fun navigateToGallery() {
        supportFragmentManager.beginTransaction().replace(R.id.frame, GalleryFragment()).commit()
    }

    ...
```
추후 프래그먼트에서 다른 화면으로 전환될때 사용되는 콜백 인터페이스 입니다.<br/>
프래그먼트가 액티비티에 접근하여 다른 화면으로 전환을 요청하는데 사용할 수 있습니다.
```kotlin
    private var callback: ActivityCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as ActivityCallback?
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
    
    view.btnNextPage.setOnClickListener {
        callback?.navigateToPrompt()
    }
```

기존에 프래그먼트에서 액티비티에 접근할때 requireActivity().supportFragmentManager...를 사용하는것 대신 콜백으로 캐스팅하여<br/>
callback?.navigate..의 형태로 좀더 편리하게 접근하고 재사용할 수 있겠습니다.
##### Tutorial
* TutorialFragment
```kotlin
class TutorialFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val bind = TutorialLayoutBinding.inflate(inflater, container, false)
        bind.pager.adapter = FragmentAdapter(this)
        return bind.root
    }

    //view pager를 위한 adapter (fragment)
    inner class FragmentAdapter(tutorialFragment: TutorialFragment) : FragmentStateAdapter(tutorialFragment) {

        private val fragments = listOf(FirstFragment(), SecondFragment(), ThirdFragment())

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

        /*
        bind 할 필요 없는 이유 -> 기존 RecyclerView의 item은 data class의 형태. 따라서 각 아이템별로 bind가 필요했음.
        fragment -> 각자 클래스로 형성됨. 리스너 등록 가능.
         */
    }
}
```
튜토리얼을 미완료 했을경우 전환 되는 WelcomeFragment에서 연결되는 프래그먼트입니다. (환영합니다 버튼 클릭시~)<br/>
단순한 튜토리얼 이미지를 가지고 있는 3개의 프래그먼트를 FragmentStateAdapter에 담아 ViewPager에 할당해줍니다.
* CoachMarkFragment
```kotlin
@AndroidEntryPoint
class CoachMarkFragment : Fragment() {

    private val viewModel: CoachMarkViewModel by viewModels()
    private var callback: ActivityCallback? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val bind = CoachLayoutBinding.inflate(layoutInflater, container, false)

        bind.btnStart.setOnClickListener {
            viewModel.saveTutorialEnded()
        }

        viewModel.tutorialLiveData.observe(viewLifecycleOwner) {
            callback?.navigateToGallery()
        }
      
        return bind.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as ActivityCallback?
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}
```
튜토리얼의 마지막 프래그먼트입니다. 버튼을 클릭하면 뷰모델의 saveTutorialEnd 메소드를 호출하는것과 위의 MainActivity가 구현한 ActivityCallback을 사용하는 모습을 확인할 수 있습니다.</br>
다만, 버튼을 클릭할때 프래그먼트를 전환하는것이 아닌 ViewModel의 tutorialLiveData를 관측하여 프래그먼트를 전환합니다. <br/>

이는 뷰모델에서 유저 데이터의 저장이 비동기로 이루어지고 (현재는 동기식), 이후에 화면의 전환이 발생되어야 하는데 Fragment에서 바로 전환할경우 순서가 서로 뒤바뀌어 진행될 수 있어 위와 같은 방식을 택했고, 또한 MVVM 패턴에서도 Fragment에서 직접적으로 접근하는것 보단 livedata를 이용하는것이 좀더 적합하다고 생각되어 진행하였습니다. 

* CoachMarkViewModel
```kotlin
@HiltViewModel
class CoachMarkViewModel @Inject constructor(private val saveUserVisited: SaveUserVisited): ViewModel() {

    val tutorialLiveData : MutableLiveData<Boolean> = MutableLiveData()

    fun saveTutorialEnded() {
        saveUserVisited.saveVisited()
        tutorialLiveData.value = true
    }
  
}
```
* SaveUserVisited
```kotlin
class SaveUserVisited(private val userRepository: UserRepository) {

    fun saveVisited() {
        userRepository.saveVisit()
    }
}
```
뷰모델에서는 버튼 클릭시 유저의 튜토리얼 여부를 유스케이스를 거쳐 저장하게 되고, 프래그먼트에게 화면 전환 여부를 LiveData에 담아 전달합니다.

##### 갤러리
* GalleryFragment



##### 다른 애플리케이션과 상호작용
* implicit intent
* deep link
## 4. 라이브러리

### ✔ Coroutine

### 📱Retrofit

### 🗡️ Hilt

### 🛠️ Mockk

## 5. Error handling