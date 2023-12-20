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
* 호출 테스트 앱 : https://github.com/choi-hyeseong/MindSnapCall

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

<details>
    <summary style="font-weight:bold; text-decoration:underline">펼치기</summary>

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

맨 처음 onCreate() 메소드에서 MainViewModel의 isFirstJoined를 체크하여 튜토리얼이 진행되지 않은경우 튜토리얼 프래그먼트로 전환하고, 튜토리얼을
완료한경우 갤러리 프래그먼트로 전환합니다.
<br/><br/>
***

* MainViewModel

```kotlin
@HiltViewModel
class MainViewModel @Inject constructor(getUserFirstJoined: GetUserFirstJoined) : ViewModel() {

    private val firstJoinLiveData: LiveData<Boolean> =
        MutableLiveData(getUserFirstJoined.isFirstJoined())

    fun isFirstJoined(): LiveData<Boolean> {
        return firstJoinLiveData
    }
} 
```

해당 뷰모델에서는 로드시 유저의 튜토리얼 여부를 LiveData의 형태로 가져와 View에서 관측할 수 있게 firstJoinLiveData를 제공합니다.

위 액티비티에서 볼 수 있듯이 뷰에서는 뷰모델의 LiveData를 observe하여 최초로 관측하거나, 값이 변경될때 이를 읽어올 수 있습니다. 또한, LiveData의 특성상
화면 회전과 같은 상황에서 다시 onCreate될때 저장된 값을 전달 해 주어 데이터를 보존할 수 있습니다.
<br/><br/>
***

* GetUserFirstJoined

```kotlin
class GetUserFirstJoined(private val userRepository: UserRepository) {

    fun isFirstJoined(): Boolean {
        return userRepository.isFirstJoined()
    }
}
```

해당 유스케이스는 유저 레포지토리에서 유저가 튜토리얼을 완료했는지(첫 접속인지 아닌지)를 반환합니다.
<br/><br/>
***

* UserRepository

```kotlin
interface UserRepository {
    //유저가 튜토리얼을 완료했는지 확인하는 레포지토리
    fun isFirstJoined(): Boolean

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

이때 액티비티는 직접적으로 레포지토리에 접근하여 정보를 얻는것이 아닌, 유스케이스를 거쳐 데이터에 접근하므로 추후 레포지토리의 메소드나 구현체가 변경되더라도 유스케이스 내부의
코드만 수정하면 되니 액티비티의 코드를 수정할 필요가 없다는 장점이 있습니다.

유저 또한 User 클래스를 만들어 Model로 사용할 수 있으나, 튜토리얼 여부 하나만 담고 있기엔 애매해서 따로 구성하지 않았습니다. 추후 추가적인 데이터의 저장, 관리가
필요할경우 User 모델을 생성하는것도 좋다고 생각합니다.
<br/><br/>
***

* ActivityCallBack

```kotlin
interface ActivityCallback {

    fun navigateToTutorial()

    fun navigateToCoach()

    //튜토리얼 완료해서 프래그먼트 이동
    fun navigateToGallery()

    fun navigateToResult(prompt: String, artStyle: ArtStyle)

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

    //...
}
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
<br/><br/>
***

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
    inner class FragmentAdapter(tutorialFragment: TutorialFragment) :
        FragmentStateAdapter(tutorialFragment) {

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
<br/><br/>
***

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

튜토리얼의 마지막 프래그먼트입니다. 버튼을 클릭하면 뷰모델의 saveTutorialEnd 메소드를 호출하는것과 위의 MainActivity가 구현한 ActivityCallback을
사용하는 모습을 확인할 수 있습니다.</br>
다만, 버튼을 클릭할때 프래그먼트를 전환하는것이 아닌 ViewModel의 tutorialLiveData를 관측하여 프래그먼트를 전환합니다. <br/>

이는 뷰모델에서 유저 데이터의 저장이 비동기로 이루어지고 (현재는 동기식), 이후에 화면의 전환이 발생되어야 하는데 Fragment에서 바로 전환할경우 순서가 서로 뒤바뀌어 진행될
수 있어 위와 같은 방식을 택했고, 또한 MVVM 패턴에서도 Fragment에서 직접적으로 접근하는것 보단 livedata를 이용하는것이 좀더 적합하다고 생각되어 진행하였습니다.
<br/><br/>
***

* CoachMarkViewModel

```kotlin
@HiltViewModel
class CoachMarkViewModel @Inject constructor(private val saveUserVisited: SaveUserVisited) :
    ViewModel() {

    val tutorialLiveData: MutableLiveData<Boolean> = MutableLiveData()

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
<br/><br/>
***

##### 갤러리

* GalleryFragment

```kotlin
class GalleryFragment : Fragment() {

    private var imageLayoutBinding: ImageLayoutBinding? = null
    private var callback: ActivityCallback? = null
    private val viewModel: GalleryViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = GalleryLayoutBinding.inflate(layoutInflater, container, false)


        view.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(GridSpacingItemDecoration(2, 16, true))
        }



        view.btnNextPage.setOnClickListener {
            callback?.navigateToPrompt()
        }
        //이미지 로드/갱신시 adapter 갱신
        viewModel.imageLiveData.observe(viewLifecycleOwner) { image ->
            view.recyclerView.adapter = ImageAdapter(this, image)
        }

        viewModel.intentLiveData.observe(viewLifecycleOwner) {
            startActivity(it)
        }

        initChildView(view)
        return view.root
    }

    private fun onImageClick(image: Image) {
        imageLayoutBinding?.apply {
            share.setOnClickListener {
                viewModel.shareImage(image.fileName)
            }
        }
    }

    inner class ImageAdapter(
        private val context: Fragment,
        private val images: List<Image>,
    ) : RecyclerView.Adapter<ImageViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val imageView = ImageView(context.requireContext())
            return ImageViewHolder(imageView)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            holder.bind(images[position])
        }

        override fun getItemCount(): Int {
            return images.size
        }

    }

    inner class ImageViewHolder(private val imageView: ImageView) :
        RecyclerView.ViewHolder(imageView) {
        fun bind(image: Image) {
            val screenWidth = requireContext().resources.displayMetrics.widthPixels
            val imageSize = screenWidth / 2 - 64 // 전체 가로 화면의 1/2 크기로 설정

            val params = LayoutParams(
                imageSize,
                imageSize
            )
            imageView.layoutParams = params
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setImageBitmap(image.bitmap)
            imageView.setOnClickListener {
                onImageClick(image)
            }
        }
    }
}
```

GalleryFragment는 ViewModel의 Image 모델 리스트를 LiveData를 이용하여 읽어와 RecyclerView에 등록하는 부분과, Intent
LiveData를 이용하여 이미지를 공유하는 부분으로 나뉘어 있습니다.

<br/>

***

* GalleryViewModel

```kotlin
@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getAllImages: GetAllImages,
    private val shareImage: ShareImage
) : ViewModel() {

    val imageLiveData: MutableLiveData<List<Image>> = MutableLiveData()
    val intentLiveData: MutableLiveData<Intent> = MutableLiveData()

    init {
        getAllImage()
    }

    private fun getAllImage() {
        CoroutineScope(Dispatchers.IO).launch {
            imageLiveData.postValue(getAllImages.getAllImages())
        }
    }

    fun shareImage(fileName: String) {
        intentLiveData.value = shareImage.shareImage(fileName)
    }
}
```

해당 뷰모델에서는 갤러리로부터 저장된 Image 모델을 GetAllImage라는 유스케이스를 이용하여 불러오고, 클릭한 이미지 모델의 파일 이름을 이용하여 ShareImage
유스케이스를 사용,
이미지를 공유할 수 있는 인텐트를 반환받을 수 있습니다. 이를 LiveData에 담아 뷰에서 사용할 수 있게 가공합니다.<br/>

GetAllImages 유스케이스는 비동기적으로 진행되는 중단함수로 선언되어 있어 Coroutine Scope를 열어 비동기적으로 처리를 하였습니다.<br/><br/>
이때 Intent또한 뷰에서 처리하는것 대신 ViewModel의 LiveData를 이용하여 실질적인 모델 데이터의 접근을 뷰모델에게 위임하는쪽으로 구성했습니다.
<br/>

***

* Image

```kotlin
data class Image(val fileName: String, val bitmap: Bitmap)
```

* UseCase

```kotlin
package com.home.mindsnap.usecase

class ShareImage(private val galleryRepository: GalleryRepository) {

    fun shareImage(fileName: String): Intent {
        return galleryRepository.shareImage(fileName)
    }
}

class GetAllImages(private val galleryRepository: GalleryRepository) {

    suspend fun getAllImages(): List<Image> {
        return galleryRepository.getAllImages()
    }
}
```

이미지 모델은 저장된 이미지의 파일명 (확장자 포함)과 실제 이미지 데이터인 비트맵을 갖고 있습니다. 이를 디코딩하여 ImageView에 등록됩니다.<br/>
이미지를 불러오는 GetAllImages의 메소드는 suspend (중단)함수로 선언되어 비동기적으로 진행되므로 파일을 읽어오는데 시간이 소요되더라도 메인 UI를 지연시키지 않아
안전하게 이미지를 불러오는 작업을 수행할 수 있습니다.

<br/>

***

* GalleryRepository

```kotlin
interface GalleryRepository {
    fun isImageExists(fileName: String): Boolean
    suspend fun saveImage(image: Bitmap, fileName: String)
    suspend fun getAllImages(): List<Image>
    fun shareImage(fileName: String): Intent
}

class LocalGalleryRepository(private val galleryDao: GalleryDao) : GalleryRepository {

    override suspend fun saveImage(image: Bitmap, fileName: String) {
        //로컬에 저장
        galleryDao.saveImage(image, fileName)
    }

    override suspend fun getAllImages(): List<Image> {
        return galleryDao.getAllImages()
    }

    override fun shareImage(fileName: String): Intent {
        return galleryDao.shareImage(fileName)
    }

    override fun isImageExists(fileName: String): Boolean {
        return galleryDao.isImageExists(fileName)
    }
}
```

* LocalGalleryDao

```kotlin
class LocalGalleryDao(private val context: Context) : GalleryDao {
    //내부 저장소 접근을 위한 ApplicationContext 접근
    override suspend fun getAllImages(): List<Image> {
        val dir = context.filesDir
        val result = ArrayList<Image>()
        dir.listFiles()?.let {
            for (file: File in it) {
                readImage(file.absolutePath)?.let { image ->
                    result.add(
                        Image(
                            file.name, image))
                } //확장자 제거후 입력 -> 확장자 포함에서 입력
            }
        }
        return result
    }

    override suspend fun saveImage(image: Bitmap, fileName: String) {
        withContext(Dispatchers.IO) {
            val output = context.openFileOutput("$fileName.jpg", Context.MODE_PRIVATE)
            image.compress(Bitmap.CompressFormat.JPEG, 100, output)
            output.close()
        }
    }

    private fun readImage(filePath: String): Bitmap? {
        return BitmapFactory.decodeFile(filePath)
    }

    override fun shareImage(fileName: String): Intent {
        val name = if (fileName.contains(".")) fileName else fileName.plus(".jpg")
        return Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    context,
                    "com.home.mindsnap.fileprovider",
                    File(context.filesDir, name)))
        }
    }

    override fun isImageExists(fileName: String): Boolean {
        //filename도 동일하게 확장자 제거
        return context.fileList().map { it.split(".")[0] }
            .find { it.equals(fileName.split(".")[0], ignoreCase = true) } != null
    }
}
```

레포지토리와 실질적인 데이터 접근을 위한 Dao 객체입니다. 앱 내부 저장소 이용을 위해 ApplicationContext를 이용하였습니다. <br/>
이미지를 저장, 불러오는 과정에서 사용되는 메소드는 suspend로 선언, 별개의 쓰레드에서 동작하여 ANR을 방지하였습니다. <br/>

이때 ShareImage 유스케이스에서 사용되는 Intent를 해당 레포지토리에서 생성하는데, 저장하고 있는 이미지의 url를 인텐트에 담아 다른 앱을 호출할때 사용할 수 있게
하였습니다.

<br/>

***

##### 프롬프트

* PromptFragment

```kotlin
class PromptFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        arguments?.apply {
            val prompt = getString(PROMPT, "")
            val artStyle = ArtStyle.fromString(getString(ARTSTYLE, ""))
            viewModel.setParameter(prompt, artStyle)
        }

        bind.generateBtn.setOnClickListener {
            viewModel.setParameter(bind.promptInput.text.toString(), null) //프롬프트 갱신
            viewModel.generate()
        }

        viewModel.promptLiveData.observe(viewLifecycleOwner) {
            bind.promptInput.setText(it)
        }

        viewModel.artStyleLiveData.observe(viewLifecycleOwner) {
            bind.recyclerView3.adapter = ArtStyleAdapter(it)
        }

        viewModel.responseLiveData.observe(viewLifecycleOwner) {
            it.getContent()?.let { data ->
                if (data.isResourceMessage())
                    Toast.makeText(
                        requireContext(),
                        data.getResourceMessage(requireContext()),
                        Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.dataLiveData.observe(viewLifecycleOwner) {
            callback?.navigateToResult(it.first, it.second)
        }

        return bind.root
    }
}
```

프래그먼트 생성시 argument를 통해 프롬프트와 화풍을 읽어오고 이를 뷰와 뷰모델에 반영합니다. 이는 암시적 인텐트 혹은 딥링크, 결과 화면에서 뒤로가기 같은 상황에서
argument가 설정될 수 있습니다.

이 외에도 화풍과 프롬프트 입력창에서 LiveData를 관측하는데, 이는 입력받은 argument를 직접적으로 뷰에서 설정하는것이 아닌 뷰모델이 값을 전달받고 LiveData로
다시 뷰에게 전달하여 뷰의 일관성을 유지하고자 했습니다. (뷰만 갱신되고 뷰모델은 갱신 안되는 등..)

또한, Toast등의 알림을 표시하기 위한 ResponseLiveData가 있는데, 이는 String형이 아닌 StringData형식의 새로운 클래스의 형태로 응답을 반환합니다.
이는 추후에 5. Error handling에서 설명드리겠습니다.

추가로 CoachMarkFragment처럼 프래그먼트의 전환에 사용되었던 LiveData가 있는데, 이때는 프롬프트와 화풍 파라미터를 전달하기 위해 Boolean형 대신 Pair<
String, ArtStyle>의 형태로 값을 반환합니다.

<br/>

***

* PromptViewModel

```kotlin
@HiltViewModel
class PromptViewModel @Inject constructor() : ViewModel() {

    //intent로 받은 프롬프트와 스타일.
    private var prompt: String = ""
    private var artStyle: ArtStyle = ArtStyle.NONE

    val artStyleLiveData: MutableLiveData<ArtStyle> = MutableLiveData()
    val promptLiveData: MutableLiveData<String> = MutableLiveData()
    val responseLiveData: MutableLiveData<Event<StringData>> = MutableLiveData()
    val dataLiveData: MutableLiveData<Pair<String, ArtStyle>> = MutableLiveData() //프래그먼트 전환.

    fun setParameter(prompt: String?, artStyle: ArtStyle?) {
        prompt?.let {
            this.prompt = it
            notifyPromptUpdate()
        }
        artStyle?.let {
            this.artStyle = it
            notifyArtStyleUpdate()
        }
    }

    fun generate() {
        if (prompt.isEmpty())
            responseLiveData.value = Event(StringData(R.string.prompt_empty, null))
        else
            dataLiveData.value = Pair(prompt, artStyle)
    }

    private fun notifyPromptUpdate() {
        promptLiveData.value = prompt
    }

    private fun notifyArtStyleUpdate() {
        artStyleLiveData.value = artStyle
    }
}
```

setParameter 메소드를 이용해 뷰의 데이터를 뷰모델로 전달하고 뷰를 다시 갱신하는 메소드를 확인할 수 있습니다.

<br/>

***

##### 결과

* ResultFragment

```kotlin
class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind = ResultLayoutBinding.inflate(layoutInflater, container, false)
        viewModel.loadingLiveData.observe(viewLifecycleOwner) { loading ->
            if (loading)
                dialog?.show()
            else
                dialog?.dismiss() //dismiss로 해야 ui 안가리고 작동됨.
        }

        viewModel.resultImage.observe(viewLifecycleOwner) { image ->
            bind.resultImage.setImageBitmap(image)
        }

        viewModel.shareLiveData.observe(viewLifecycleOwner) {
            startActivity(it)
        }

        bind.resultShare.setOnClickListener {
            viewModel.shareImage()
        }

        bind.resultDownload.setOnClickListener {
            viewModel.saveImage()
        }

        bind.resultRetry.setOnClickListener {
            viewModel.retryPrompt()
        }

        arguments?.apply {
            val prompt = getString(PROMPT, "")
            val artStyle = ArtStyle.fromString(getString(ARTSTYLE, ""))
            if (prompt.isNullOrEmpty()) {
                Log.e(LOG_HEADER, "prompt is null.")
                callback?.requestFinish()
            }
            viewModel.setParameter(prompt, artStyle)
            viewModel.generateImage()
        }

        return bind.root
    }
}
```

위 프롬프트 화면과 동일하게 argument를 받는 모습을 확인할 수 있습니다. 이때, 최종 결과창은 프롬프트와 화풍 데이터 모두 존재해야 하므로 데이터가 없을경우 콜백의
finish를 호출하여 액티비티를 종료합니다.

그 외에 LiveData의 Image 모델을 관측하여 ImageView에 띄우고, 현재 로딩중인지 여부를 확인하여 Dialog를 보여주고 로딩이 끝난경우 없애주는 형식으로 작동하고
있습니다.
<br/> GalleryFragment와 동일하게 Intent LiveData를 관측하여 이미지 공유를 구현하는 모습을 볼 수 있습니다.

<br/>

***

* ResultViewModel

```kotlin
@HiltViewModel
class ResultViewModel @Inject constructor(
    private val generateImage: GenerateImage,
    private val saveLocalImage: SaveLocalImage,
    private val existImage: ExistImage,
    private val shareImage: ShareImage,
    private val promptGenerator: PromptGenerator
) : ViewModel() {

    val resultImage: MutableLiveData<Bitmap> = MutableLiveData()
    val loadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val toastLiveData: MutableLiveData<Event<StringData>> = MutableLiveData()
    val textLiveData: MutableLiveData<String> = MutableLiveData()
    val shareLiveData: MutableLiveData<Intent> = MutableLiveData()
    val retryLiveData: MutableLiveData<Pair<String, ArtStyle>> =
        MutableLiveData() //retry 버튼 클릭시 vm에 있는 데이터 반환.

    private val fileName: String by lazy {
        promptGenerator.generate(prompt, artStyle) //fileName 가져올때는 prompt와 style 존재
    }


    //intent로 받은 프롬프트와 스타일.
    private var prompt: String = ""
    private var artStyle: ArtStyle = ArtStyle.NONE
    fun generateImage() {
        CoroutineScope(Dispatchers.IO).launch {
            loadingLiveData.postValue(true)
            resultImage.postValue(generateImage.generateImage(prompt, artStyle))
            loadingLiveData.postValue(false)
        }
    }

    fun shareImage() {
        if (existImage.existImage(fileName))
            shareLiveData.value = shareImage.shareImage(fileName)
        else
            toastLiveData.postValue(Event(StringData(R.string.before_download, null)))
    }

    fun saveImage() {
        CoroutineScope(Dispatchers.IO).launch {
            resultImage.value?.let {
                saveLocalImage.saveImage(it, fileName)
                toastLiveData.postValue(Event(StringData(R.string.save_complete, null)))
            }
        }
    }

    fun setParameter(prompt: String, artStyle: ArtStyle) {
        this.prompt = prompt
        this.artStyle = artStyle
        notifyPromptChange(prompt)
    }

    fun retryPrompt() {
        retryLiveData.value = Pair(prompt, artStyle)
    }
    private fun notifyPromptChange(prompt: String) {
        textLiveData.value = prompt
    }
}
```

이미지를 생성하는 generateImage는 비동기적으로 처리됩니다. 해당 유스케이스를 호출하기 전에 loading LiveData를 true로 설정해 Dialog를 띄운 후,
유스케이스를 호출한 결과를 Image LiveData에 담아 전달한뒤, loading LiveData를 false로 지정해 Dialog를 숨깁니다.

저장되는 이미지의 파일명은 프롬프트의 내용과 화풍을 토대로 결정됩니다. (lazy로 설정되어 최종 공유, 저장시 사용됨)

<br/>

***

* UseCase

```kotlin
class ExistImage(private val galleryRepository: GalleryRepository) {
    fun existImage(fileName: String): Boolean {
        return galleryRepository.isImageExists(fileName)
    }
}
class SaveLocalImage(private val galleryRepository: GalleryRepository) {
    suspend fun saveImage(image: Bitmap, fileName: String) {
        galleryRepository.saveImage(image, fileName)
    }
}
```

* Repository

```kotlin
interface ImageGenRepository {
    suspend fun generateImage(prompt: String, style: ArtStyle): Bitmap
}

class OpenAIImageGenRepository(private val genDao: ImageGenDao) :
    ImageGenRepository {
    override suspend fun generateImage(prompt: String, style: ArtStyle): Bitmap {
        return genDao.getImage(prompt, style)
    }
}

class OpenAIImageGenDao(private val generator: PromptGenerator,
                        private val service: OpenAIGenService?,
                        private val bitmapGenerator: BitmapGenerator) : ImageGenDao {
    //test를 위한 bitmapfactory 분리
    override suspend fun getImage(prompt: String, style: ArtStyle): Bitmap {
        val response =
            service!!.generateImage(ImageRequest(prompt = generator.generate(prompt, style)))
        return decodeImage(response)
    }

    //BitmapFactory는 static 메소드.. mock 오류가 나서 따로 런타입 테스트 거쳐서 잘 작동하는거 확인함.
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun decodeImage(response: ImageResponse): Bitmap {
        val data = response.data[0]
        return if (data.url != null) {
            val connection = URL(data.url).openConnection()
            connection.connect()
            val input = connection.getInputStream()
            bitmapGenerator.decodeStream(input)
        }
        else {
            //url이 null이면 json은 not null
            val decodedString: ByteArray = Base64.decode(data.jsonData!!)
            bitmapGenerator.decodeByte(decodedString)
        }
    }
}
```

실질적인 이미지 생성 레포지토리는 OpenAI의 Dall-e를 사용하는 OpenAiImageGenRepository입니다.<br/>
해당 클래스는 OpenAi에 프롬프트와 화풍을 토대로 요청을 전송하며, 이를 디코딩하여 반환합니다. 이 과정 또한 비동기로 처리됩니다.
응답의 전송 및 처리 과정은 Retrofit에서 자세히 설명하겠습니다.

PromptGenerator는 입력받은 프롬프트와 화풍객체를 이용하여 문자열 형태의 프롬프트를 생성해주는 클래스입니다.

<br/> 레포지토리 패턴을 사용하여 추후 Midjourney, StableDiffusion등의 다른 서비스를 사용하더라도 해당 인터페이스를 구현하여 사용하면 되므로 전체적으로 수정할 필요가 없다는 점이 좋았습니다.

***

</details>

### 💬 다른 애플리케이션과 상호작용

* implicit intent

![1_a1gETOhKFWKjxqlfCEn7FQ](https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/dc7106b7-837d-4789-ace4-423452656270)

암시적 인텐트는 Intent의 Action을 이용하여 다른 앱이 자신을 호출, 실행할 수 있도록 하는 방식입니다.
<br/>이를 이용하여 다른 애플리케이션에서도 프롬프트와 화풍을 입력하고 인텐트를 호출하여 이미지 생성 앱을 호출할 수 있습니다.

* deep link

![image](https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/77b066bd-89c9-43ac-a409-589742f970e3)

딥링크또한 암시적 인텐트와 유사한 개념입니다. 다만 암시적 인텐트는 다른 앱 내에서 Intent의 action을 지정하고 startActivity를 이용하여 호출하는 반면, 딥
링크는 url를 클릭하면 앱을 실행할 수 있는 방식입니다.

* Manifests

```xml

<activity android:windowSoftInputMode="adjustNothing" android:name=".MainActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <action android:name="intent.IMAGE_GENERATE" />
        <category android:name="android.intent.category.LAUNCHER" />
        <category android:name="android.intent.category.DEFAULT" /> <!--DEFAULT 카테고리가 있어야 호출가능-->
    </intent-filter>

    <intent-filter android:autoVerify="true"> <!--For DeepLink-->
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="intent" android:host="genimage" />
    </intent-filter>
</activity>
```

위 intent filter는 implicit intent, 아래의 intent filter는 deep link를 위한 필터입니다.<br/>
따라서 intent.IMAGE_GENERATE의 action이 지정된 인텐트와 intent://로 시작하는 딥링크 주소는 위 앱으로 연결됩니다.

* MainActivity

```kotlin
if (tutorial) {
    supportFragmentManager.beginTransaction().replace(R.id.frame, WelcomeFragment()).commit()
}
else {
    //튜토리얼 완료되었을때만 인텐트 핸들링
    //implicit intent
    if (intent.hasExtra(PROMPT)) {
        navigateToResult(
            intent.getStringExtra(PROMPT)!!,
            ArtStyle.fromString(intent.getStringExtra(ARTSTYLE) ?: "")
        ) //has check, art style은 빈값이 올 수 있음
    }
    else if (intent.data != null) {
        //deep link
        // am start -W -a android.intent.action.VIEW -d "intent://genimage?prompt=a dog&artstyle=3D" com.home.mindsnap 로 호출가능
        val uri = intent.data!!
        val prompt = uri.getQueryParameter(PROMPT.lowercase())
        val artStyle = uri.getQueryParameter(ARTSTYLE.lowercase()) ?: ""
        if (prompt != null)
            navigateToResult(prompt, ArtStyle.fromString(artStyle))
        else {
            Log.w(LOG_HEADER, "Deep link doesn't contain prompt parameter.")
            navigateToGallery() //좀더 깔끔하게 안되나..
        }
    }
    else
        navigateToGallery()

}
```

위 딥링크, 암시적 인텐트에 의해 MainActivity가 실행된 후, 튜토리얼이 완료된경우 위 요청이 처리됩니다. (튜토리얼 미 완료시 튜토리얼 화면 이동)
<br/>
암시적 인텐트의 경우 intent 필드에 값이 존재하므로 인텐트에 prompt extra와 화풍 extra값이 존재하면 최종 결과화면으로 이동합니다.
<br/>deeplink의 경우 intent://genimage?prompt=a dog&artstyle=3D 와 같이 url에 데이터를 담아 전달하기 때문에 url 파라미터의
prompt와 화풍을 읽어온 뒤 값이 존재하면 최종 결과 화면으로 이동하게 됩니다.

위 두가지 방식에서 프롬프트가 존재하지 않을경우 기존 절차대로 갤러리 화면으로 이동하게 되며, 화풍 데이터는 없을경우 미 지정된 상태로 설정되기 때문에 존재하지 않아도 됩니다.

## 4. 라이브러리

### ✔ Coroutine

<img width="709" alt="coroutine" src="https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/264a134b-1efb-4f0c-ad26-98d342daacf1">

코루틴은 suspend(중단)과 resume(재시작)을 할 수 있는 함수를 생성하는 라이브러리 입니다.<br/>
이는 light-weight thread로써 메인 쓰레드를 블락하지 않고, 다른 쓰레드를 통해 여러가지 무거운 작업들을 처리하며 안전하게<br/>
결과값을 리턴하고, 메소드를 수행할 수 있습니다.

* GalleryDao

```kotlin
    override suspend fun getAllImages(): List<Image> {
    val dir = context.filesDir
    val result = ArrayList<Image>()
    dir.listFiles()?.let {
        for (file: File in it) {
            readImage(file.absolutePath)?.let { image ->
                result.add(
                    Image(
                        file.name, image))
            } //확장자 제거후 입력 -> 확장자 포함에서 입력
        }
    }
    return result
}

private fun readImage(filePath: String): Bitmap? {
    return BitmapFactory.decodeFile(filePath)
}
```

* GalleryViewModel

```kotlin
private fun getAllImage() {
    CoroutineScope(Dispatchers.IO).launch {
        imageLiveData.postValue(getAllImages.getAllImages())
    }
}
```

GalleryDao에서 사용되는 suspend 함수입니다. 내부 저장소에서 File 객체를 가져오고, 이를 BitmapFactory를 통해 bitmap으로 읽어오는데, 이때
FileIO와, 비트맵 디코딩은 상당한 시간이 소요될 수 있으므로, suspend 함수로 선언하여 코루틴 스코프 내에서만 처리되도록 하였습니다.
<br/> 다만, suspend 함수라도 스코프 선언시 혹은 withContext 메소드를 사용하여 Dispatcher.Main을 사용할경우 메인 쓰레드에서 동작할 수 있어
메인쓰레드를 블락하고, 동기적으로 작동할 수 있습니다.<br/>
<br/> 이를 위해 IO 쓰레드로 코루틴 스코프를 열고, 해당 스코프 내에서 withContext에 주의하여 해당 메소드를 호출한 뒤 LiveData에 값을 담아 전달하였습니다.
이때, LiveData의 값을 비동기적으로 설정할 수 있는 postValue 메소드를 사용하여 메인쓰레드가 아니더라도 값을 변경할 수 있게 하여 안전하게 절차를 진행할 수
있었습니다.

***

### 📱Retrofit

![0_dzNrYLSkCA2xjfpq](https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/b56c1451-5b3e-4de3-abaa-50a579e1e2d8)

Retrofit은 웹 통신을 위한 라이브러리 입니다. 기존 OkHttp 라이브러리를 한단계 더 추상화한 것으로, 좀더 쉽고 명확하게 웹 요청과 응답을 보내고, 처리할 수 있습니다.

<br/>기존 OkHttp, HttpConnection과 같은 '저 수준'의 방식에서는 웹 요청시 body에 데이터를 넣기 위해선 ObjectWriter등을 사용해 String,
Map등을 직접 json object로 변경한뒤 입력해주어야 했습니다.
<br/>또한, 응답으로 받은 json body또한 실제 클래스 / 액티비티에서 사용할 수 있도록 ObjectMapper를 이용해 객체로 변경해야 했습니다.<br/>
이 과정에서 코드가 지저분해지고, Json을 읽고 쓰는 과정에서 또한 크래쉬등과 같은 문제가 발생했습니다.<br/>

이를 Retrofit에서는 API Interface를 제공하고, Json 맵핑 과정을 클래스만 선언하면 자동으로 변환해주어 상당히 처리하기 좋았습니다.

* OpenAiGenService

````kotlin
interface OpenAIGenService {

    //body 붙여야 request body 인식
    // request 요청시 영어로 올바른 문장을 써서 보내야 잘 인식함.
    @POST("images/generations")
    suspend fun generateImage(@Body request: ImageRequest): ImageResponse
}
````

OpenAI에 이미지 생성 요청을 보내기 위한 서비스 인터페이스 입니다. 기존 OkHttp에서 URL을 설정하고, Response 콜백을 등록하는 과정 필요 없이 함수만 생성하고
이를 직접적으로 사용하면 되서 상당히 간결화 되었습니다.

* OpenAI Request / Response

```kotlin
package com.home.mindsnap.repository.image.dao.openai.request


data class ImageRequest(
    //request dto. serialized name 미 지정시 json 컨버팅중 오류 발생
    @SerializedName("model")
    val model: String = "dall-e-2",
    @SerializedName("prompt")
    val prompt: String,
    @SerializedName("n")
    val n: Int = 1,
    @SerializedName("size")
    val size: String = "1024x1024",
    @SerializedName("response_format")
    val responseFormat: String = "b64_json" //url도 가능
)

data class ImageResponse(@SerializedName("created") val created: Long,
                         @SerializedName("data") val data: List<ImageDataResponse>)

data class ImageDataResponse(@SerializedName("b64_json") val jsonData: String?,
                             @SerializedName("url") val url: String?)
```

위와 같이 기존에 일일히 json으로 맵핑해야 했던 body또한 data class를 이용하여 깔끔하게 생성하고 불러울 수 있습니다.

* NetworkModule

```kotlin
    @Provides
@Singleton
fun provideOpenAIService(retrofit: Retrofit): OpenAIGenService {
    return retrofit.create(OpenAIGenService::class.java)
}
```

이렇게 생성된 인터페이스는 Retrofit 객체를 이용하여 실제 구현체가 생성되고, 저 함수를 호출할경우 API 요청이 보내지게 됩니다.
<br/>이때 웹 요청또한 비동기로 진행되어야 하므로 suspend 함수를 사용하여 코루틴 스코프 내에서만 호출되도록 하였으며, 실제 호출시 IO Dispatcher에서 수행되게
됩니다.
***

### 🗡️ Hilt

![1_MA45ld5TZbYFlpowpVjimg (1)](https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/0e292ad1-2e26-4126-b5ad-720f6a734f71)

Hilt는 기존 DI 라이브러리인 Dagger를 기반으로 제작된 라이브러리 입니다.
<br/>DI란 Dependency Injection으로, 해당 클래스내에서 필요한 객체가 생성되고 같이 소멸되는 Composition방식이 아닌, 생성자의 필드로써 입력되는
Aggresigation의 방식입니다. <br/>

이는 기존 클래스에 필요한 서브 객체 (ViewModel에서는 UseCase, UseCase는 Repository..) 등등을 주입하는 과정으로써, 필요한 객체를 Module에서
생성하고, 이를 필요한 다른 모듈, 객체의 생성자에 주입해 객체를 재사용하고, 결합도를 낮춰 유지보수에 용이합니다.
<br/>즉, 객체의 생성을 Hilt에게 위임해 관리할 수 있습니다.

![a1b8656d7fc17b7d](https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/b62b1b15-6c44-4aa0-82d7-3895bbf3716f)

한가지 예를 들자면, 유저의 튜토리얼 여부를 저장하는 SaveUserVisited 유스케이스와 유저의 튜토리얼 여부를 반환하는 GetUserFirstJoined 유스케이스는
UserRepository에 접근하여 정보를 가져오고 수정합니다.

* UseCase

```kotlin
class GetUserFirstJoined(private val userRepository: UserRepository) {

    fun isFirstJoined(): Boolean {
        return userRepository.isFirstJoined()
    }
}
class SaveUserVisited(private val userRepository: UserRepository) {

    fun saveVisited() {
        userRepository.saveVisit()
    }
}
```

이때, 두 유스케이스는 동일한 레포지토리를 참조해야 하며, 다른 레포지토리를 참조할경우 예상하지 못한 오류가 발생할 수 있습니다.
<br/>이를 해결하기 위해 UserRepository를 싱글톤으로 선언해야 합니다.

* UserRepository - fix

```kotlin
class UserRepository {
    companion object {
        val instance = UserRepository()
    }
}
UserRepository.instance 
```

다만, 위 방식은 레포지토리와 유스케이스가 강하게 결합하여, 추후 사용하는 DB나 레포지토리에 변경이 생길경우 수정하기 까다로워질 수 있습니다.<br/>
이때 hilt의 DI를 이용하면 싱글톤 선언 없이 같은 객체를 두 유스케이스에 주입할 수 있습니다.

* DaoModule

```kotlin
@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    private val PREFERENCE = "IMAGE_GEN"

    @Provides
    @Singleton
    fun provideImageGenDao(promptGenerator: PromptGenerator,
                           openAIGenService: OpenAIGenService,
                           bitmapGenerator: BitmapGenerator): ImageGenDao {
        return OpenAIImageGenDao(promptGenerator, openAIGenService, bitmapGenerator)
    }

    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
    }
}

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return PreferenceUserRepository(userDao)
    }

}
```

* UseCaseModule

```kotlin
@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    //만약 리팩토링 한다면 각 레포지토리 - dao별로 묶어서 모듈 구성하면 좋을듯

    @Provides
    @Singleton
    fun provideGetUserFirstJoined(userRepository: UserRepository): GetUserFirstJoined {
        return GetUserFirstJoined(userRepository)
    }

    @Provides
    @Singleton
    fun provideSaveUserVisited(userRepository: UserRepository): SaveUserVisited {
        return SaveUserVisited(userRepository)
    }

}
```

* MainViewModel

```kotlin
//HiltViewModel을 이용한 Module 주입
@HiltViewModel
class MainViewModel @Inject constructor(getUserFirstJoined: GetUserFirstJoined) : ViewModel() {

    private val firstJoinLiveData: LiveData<Boolean> =
        MutableLiveData(getUserFirstJoined.isFirstJoined())

    fun isFirstJoined(): LiveData<Boolean> {
        //이렇게 해야 매 rotation시 로드 안함
        return firstJoinLiveData
    }

}
```

* MainActivity

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ActivityCallback {

    private val viewModel: MainViewModel by viewModels() //hilt viewmodel 생성 위임
}
```

hilt의 Module을 선언하고, 객체를 제공할 수 있는 Provide annotation을 작성한 함수를 선언해, 해당 객체를 생성할 수 있습니다.
<br/>이렇게 생성된 Dao는 Repository의 생성 함수의 파라미터로 들어가고, 이 레포지토리 또한 유스케이스 생성 함수의 파라미터로 들어가 구성됩니다.
<br/>

이를 통해 싱글톤 선언 없이 안전하게 객체를 주입할 수 있고, 나중에 Repository가 변경되더라도 Module의 생성 함수의 파라미터를 변경하여 쉽게 수정할 수 있습니다.
<br/>추가로, 생성자 DI를 이용한 구조 덕분에 밑에 언급될 Mock을 이용한 테스트 객체를 생성하고, 이를 주입해 단위 테스트에도 적합한 구조를 만들 수 있습니다.
***
### 🛠️ MockK
![img1 daumcdn](https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/a321efe8-ca46-4cd3-9469-78cd6c7896e9)

MockK은 테스트용 더미 객체를 생성하고, 이를 이용하여 단위 테스트를 할 수 있는 프레임워크입니다.
<br/>기존 안드로이드 앱을 테스트 하기 위해선 가상머신을 키고, 로그를 찍어서 원하는 flow대로 동작하는지 확인해야 했었습니다.
<br/>다만, 런타임이 아니더라도 테스트할 수 있는 코드를 가상머신을 키고 테스트하면서 상당한 시간이 걸리는 부분이 있습니다.

<br/>추가로, 코드를 수정했는데 다른 부분이 고장나는 경우도 많아서 런타임 도중 처음부터 다시 다 테스트 하기엔 상당한 시간이 소요된다는 점도 한몫했습니다.

이를 MockK 라이브러리와 junit을 이용하여 조금이나마 해결할 수 있었습니다.
* LocalGalleryRepositoryTest
```kotlin
class LocalGalleryRepositoryTest {
    lateinit var repository: GalleryRepository

    @Before
    fun init() {
        val dao = mockk<GalleryDao>()
        coEvery { dao.getAllImages() } returns listOf(mockk<Image>(), mockk<Image>())
        repository = LocalGalleryRepository(dao)
        //2개의 리스트 반환
    }

    @Test
    fun TEST_GET_ALL_IMAGE() {
        runBlocking {
            assertEquals(repository.getAllImages().size, 2)
        }
    }
}
```
갤러리 레포지토리 테스트 클래스입니다. 실제 가상머신에서 데이터를 불러오고, 저장되는지 확인해야 할 수 있습니다 <br/>
하지만, ViewModel - UseCase - Repository - Dao 순으로 요청의 위임이 진행되는데, 이 과정에서 요청된 자료가 잘못 호출되는지 여부를 확인 하기 위해선 테스트가 필요합니다. 위 테스트를 통해 Repository에서 Dao로 요청이 전송되고, 해당 요청을 통해 정상적으로 값을 반환하는 지 확인할 수 있습니다. 

* OpenAiImageGenDaoTest
```kotlin

class OpenAIImageGenDaoTest {

    lateinit var dao: OpenAIImageGenDao
    lateinit var response: ImageResponse
    lateinit var promptGenerator: PromptGenerator
    lateinit var service: OpenAIGenService
    @Before
    fun init() {
        promptGenerator = PromptGenerator()
        service = mockk<OpenAIGenService>()
        val bitmapGenerator = mockk<BitmapGenerator>()

        dao = OpenAIImageGenDao(promptGenerator, service, bitmapGenerator)
        every { bitmapGenerator.decodeByte(any()) } returns mockk()
        every { bitmapGenerator.decodeStream(any()) } returns mockk()


    }

    @Test
    fun TEST_URL_TO_BITMAP() {

        val data = ImageDataResponse(
            null,
            "https://img.gigglehd.com/gg/files/attach/images/158/293/986/006/5b89d99dce8ad12da73d6513da4e1d42.JPG")
        response = ImageResponse(java.lang.System.currentTimeMillis(), listOf(data))
        runBlocking {
            assertNotNull(dao.decodeImage(response))
        }
    }

    @Test
    fun TEST_JSON_TO_BITMAP() {
        val data = ImageDataResponse(
            "/9j/4AAQSkZJRgABAQEAYABgAAD/4RBmRXhpZgAATU0AKgAAAAgAAwESAAMAAAABAAEAAIdpAAQAAAABAAAIPuocAAcAAAgMAAAAMgAAAAAc6gAAAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB6hwABwAACAwAAAhQAAAAABzqAAAACAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD/4QjdaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49J++7vycgaWQ9J1c1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCc/Pg0KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyI+PHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIi8+PC94OnhtcG1ldGE+DQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg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",
            null)
        response = ImageResponse(java.lang.System.currentTimeMillis(), listOf(data))
        runBlocking {
            assertNotNull(dao.getImage("asdf", ArtStyle.NONE))
        }

    }
}
```

이러한 점을 들어 웹 요청 또한 정상적으로 위임이 진행되어 요청이 잘 보내지는지 확인할 수 있습니다.

이때, 실제로 요청을 보내고 응답을 받을 수 없으니 '모의 객체'라는것을 생성해 테스트를 수행할 수 있습니다.
<br/>모의 객체는 해당 객체의 필드, 메소드를 테스트 조건에 맞게 수정할 수 있으며 mockk메소드로 생성할 수 있습니다.

위 테스트에서 every 조건과 any() 를 통해 어떤 요청이 들어와도 모의 객체를 반환한다는 점을 확인할 수 있습니다.

최종적으로 assertNotNull, assert문을 통해 테스트하는 객체의 결과값이 예상한 결과값과 같을경우 해당 테스트는 성공적으로 수행되며, 테스트를 통과하지 못했을경우 해당 객체의 문제를 해결하는 방식으로 이를 수행할 수 있습니다.
<br/>추가로, 기존 테스트 프레임워크는 코틀린과 coroutine의 suspend메소드를 지원하지 않는데, MockK라이브러리의 coEvery{} 조건등을 통해 테스트를 정상적으로 수행할 수 있습니다.

이를 통해 모델 객체부터, 외부 액티비티까지 문제를 검증하며 개발을 수행해나가는 TDD (Test-Driven-Development) 테스트 주도 개발의 형태로 개발을 수행할 수 있습니다. 다만, 아직까진 테스트에 익숙하지 않아 모든  객체의 테스트를 생성하기 어려워 나머지는 런타임 테스트로 수행했다는점이 좀 아쉬웠습니다.

* StringDataTest
```kotlin
class StringDataTest {


    @Test
    fun CHECK_IS_RESOURCE() {
        val data = StringData(15, null)
        assertTrue(data.isResourceMessage())
    }

    @Test
    fun CHECK_IS_NOT_RESOURCE() {
        val data = StringData(message = "테스트")
        assertFalse(data.isResourceMessage())
    }

    @Test
    fun TEST_GET_STRING_NO_PARAM() {
        val data = StringData(15, null)
        val context = mockk<Context>()

        every { context.getString(any()) } returns ""
        data.getResourceMessage(context)
        verify(exactly = 1) { context.getString(any()) } 
    }

    @Test
    fun TEST_GET_STRING_PARAM() {
        val data = StringData(15, null, "배고파")
        val context = mockk<Context>()

        every { context.getString(any(), any()) } returns ""
        data.getResourceMessage(context)
        verify(exactly = 1) { context.getString(any(), any()) } //verify는 다 수행하고 맨 마지막에..
    }
}
```
![스크린샷 2023-12-18 145104](https://github.com/choi-hyeseong/mobile_project_7/assets/114974288/0298da98-3d53-444a-a82f-8876d91e3a7f)

***
## 5. Error handling

#### · 화면 회전시 첫 화면으로 돌아가는 문제

안드로이드 앱 내에서는 화면 회전시 onDestory와 onCreate를 거쳐 앱의 lifecycle이 변경됩니다. 이때, 기존에 저장되지 않은 데이터는 제거되는 경우가 있습니다.<br/>
이를 방지하기위해 SavedInstanceState, ViewModel등을 이용해 데이터를 저장하고 복원하는 과정을 거칩니다.

프래그먼트를 관리하는 FragmentManager는 화면 회전시 기존 저장된 프래그먼트를 다시 불러오는 과정을 거쳐 따로 데이터를 저장하고 불러 올  필요 없이 스스로 복구를 진행합니다. 

* MainActivity
```kotlin
onCreate() {
  viewModel.isFirstJoined().observe(this) { tutorial ->
    if (tutorial)
      supportFragmentManager.beginTransaction().replace(R.id.frame, WelcomeFragment())
        .commit()
        ...
  }
}
```
초기에는 이를 알지 못해 onCreate에서 바로 viewModel을 observe해서 프래그먼트를 바꿔주는 작업을 수행했습니다. 이 때문에 이미지 생성을 하는 도중에 화면 회전이 발생하면 초기 화면으로 돌아가는 문제가 있었습니다.

```kotlin
 if (savedInstanceState == null) {
  //최초 한번만 인식해서 화면 회전은 무시
  viewModel.isFirstJoined().observe(this) { tutorial ->
  }
}
```
위에서 언급한것처럼 프래그먼트 매니저는 화면 회전시 기존 뷰를 복구하므로 최초로 activity가 초기화 됐을때인 savedInstanceState가 null일때만 프래그먼트를 전환하도록 하여 해결하였습니다.
***
#### · Hilt 적용 문제

hilt는 프록시 객체를 생성해 의존성 주입을 담당하기 때문에 기존 코틀린 컴파일 도구인 kapt의 의존성이 필요합니다. 이 과정에서 코틀린 버전과 호환성 문제가 발생해 오류가 발생했습니다.

이는 hilt와 kotlin, kapt 버전을 서로 호환되게 맞추어 해결하였습니다.
***
#### · Junit Test 코틀린 미지원

단위 테스트 도구인 Junit을 사용하기 위해 Test클래스를 사용하고자 했으나 작동하지 않았습니다.
<br/>코틀린에서는 자바 기반의 Junit이 작동하지 않는다는것을 알게 되었고, 호환되는 MockK을 사용해 해결하였습니다.
***
#### · ViewModel Context 접근

MVVM 패턴에서는 뷰의 기능을 처리하는 Activity / Fragment와 실제적인 모델의 기능을 처리하는 ViewModel 간의 상호작용으로 이루어집니다.

이는 뷰와 모델간의 결합도를 떨어트리기 좋은 구조이지만, 실제 모델의 기능을 처리할때 Context의 기능이 필요한 경우가 있습니다.<br/>

예를 들어 API서버로 요청을 보냈을때 오류가 발생했을경우 Toast등을 사용해서 유저에게 문제가 발생한것을 알려줄 필요가 있습니다.

이때 Toast를 사용하기 위해선 Context 객체가 필요한데, ViewModel에서는 이를 사용할 수 없습니다. <br/>
ViewModel에서 Context에 접근하는것은 메모리 누수등의 위험성이 있고, 뷰를 담당하는 Context를 뷰모델에서 접근하면 모델과의 결합성이 커질 수 있기 때문입니다.

이를 해결하기 위해 LiveData를 사용하였습니다.

메모리 누수를 걱정할 필요 없는 AndroidViewModel이라는 객체를 상속받는 방법도 있으나, ViewModel에서는 Context의 기능을 사용하지 않는것이 좋을것 같아 다음과 같이 처리하였습니다.

```kotlin
 val responseLiveData : MutableLiveData<String> = MutableLiveData()
```
ViewModel의 LiveData를 사용하여 실제 Context에 접근하지 않고, 내부 데이터만 뷰로 전달해 뷰가 이를 실행하는 방식입니다.

LiveData의 특성상 observe를 수행하면 마지막으로 설정된 값을 다시 notify하게 됩니다.

이때, LiveData<String>으로 설정이 되어 있고, 이를 Toast로 호출하게 된다면
```kotlin
viewModel.liveData.observe() {
    Toast~ 
}
```
매 화면 회전시 Toast가 발생하는 문제가 있습니다. Toast는 한번 호출된 이후 다시 호출되서는 안되기 때문에 Event 클래스를 만들어 감쌌습니다.

* Event
```kotlin
open class Event<T>(private val content : T) {
    //toast와 같은 1회성 이벤트를 livedata에 넣을경우 화면 회전등과 같이 갱신시 지속적으로 호출되는 문제 해결하는 클래스
    var isHandled : Boolean = false
        private set

    fun getContent() : T? {
        if (isHandled)
            return null
        else {
            isHandled = true
            return content
        }
    }

    fun getContentForce() : T = content
}

```
Event 클래스의 getContent를 하면 이미 값을 가져온경우 null을 리턴하고, 값을 가져오지 않은경우 값을 반환한 뒤 isHandled 변수를 true로 변경해 다시 값을 가져오지 못하게 설정합니다.

추가로 이렇게 전달되는 String을 문자열로 하드코딩 하는 대신 string.xml을 이용해서 리소스 id를 받게 하고, 언어 호환성을 높일 수 있는 방법또한 생각해보았습니다.
* StringData
```kotlin
class StringData(private val resID : Int = -1, val message : String?, private vararg val param : String) {

  fun isResourceMessage() : Boolean {
    return resID != -1
  }

  fun getResourceMessage(context: Context) : String {
    return if (!isResourceMessage())
      ""
    else
      if (param.isEmpty()) {
        context.getString(resID)
      }
      else {
        context.getString(resID, param)
      }
  }
}
```
string.xml의 문자열 리소스는 리소스 id를 이용하여 접근할 수 있습니다. 리소스 id로부터 문자열을 가져오기 위해선 Context 객체가 필요한데,<br/>
이는 isResourceMessage로 리소스 메시지 여부를 확인하고 getREsourceMessage를 통해 Context를 받아 문자열을 가져올 수 있게 하였습니다.

```kotlin
val toastLiveData = MutableLiveData<Event<StringData>>()
```

* ResultFragment
```kotlin
 viewModel.toastLiveData.observe(viewLifecycleOwner) { event ->
            event.getContent()?.let {
              //Toast가 처음 처리 되는 경우  
              val message =
                    if (it.isResourceMessage()) it.getResourceMessage(requireContext()) else it.message
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
```

따라서 ViewModel이나 LiveData에서 Context에 접근할 필요 없이 로직을 처리할 수 있었습니다.
***

#### · 내부 파일 공유
앱에서 생성된 이미지는 앱 내부 저장소에 저장됩니다. (LocalGalleryRepository)
<br/> 이렇게 생성된 이미지를 공유하기 위해선 Intent에 담아서 요청해야 했습니다.

```kotlin

import java.io.FileOutputStream

override fun shareImage(fileName: String): Intent {
  return Intent(Intent.ACTION_SEND).apply {
    type = "image/*"
    putExtra(
      Intent.EXTRA_STREAM,
      FileOutputStream(fileName)
    )
  }
}
```
Intent의 Action_Send와 Extra에 이미지 stream을 담아 이미지 공유를 요청할 수 있습니다.
<br/>하지만 위 메소드는 오류가 발생했습니다.

> android.os.TransactionTooLargeException : data parcel size 1002388 bytes

컴포넌트간 데이터 공유를 할때 사용되는 Intent의 Extra로 이미지 객체를 전송하기엔 너무 크다는 오류였습니다.

이를 해결하기 위해 Extra에 이미지 객체를 넣는것이 아닌, URI을 넣어 직접 외부에서 참조 할 수 있도록 하였습니다.
```kotlin
putExtra(Intent.EXTRA_STREAM, File(fileName).toURI())
```

하지만 이것도 오류가 발생했습니다.

> java.lang.Throwable: file:// Uri exposed

api 24 이후로는 file:// 로 시작하는 uri를 직접 사용할 수 없다는 이유였습니다.

이를 해결하기 위해 이미지를 앱 내부가 아닌 갤러리 앱에서 사용할 수 있도록 저장 위치를 바꾸는 방법과, 다른 공유 방식을 선택하는것이였습니다.

다행히, 앱 컴포넌트중 하나인 ContentProvider의 구현체, FileProvider를 이용하여 해결할 수 있었습니다.

* Manifests
```xml
        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="com.home.mindsnap.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
```
* LocalGalleryDao
```kotlin
return Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    context,
                    "com.home.mindsnap.fileprovider",
                    File(context.filesDir, name)))
        }
```
다음과 같이 FileProvider를 이용, 해당 Provider의 uri를 이용하여 공유하니 정상적으로 이미지를 전달할 수 있었습니다.
<br/>이때 context가 필요했는데, 위의 **ViewModel에서 context 접근** 단락에서 언급했다 시피 View와 관련된 Context를 모델과 연결시키는 것은 좋지 않았습니다.

하지만, Intent 생성을 위해 context의 접근이 필요했고, 이를 해결하기 위해 전역 Context로 메모리 누수를 걱정할 필요 없는 ApplicationContext를 Hilt로 주입하여 사용하였습니다.

```kotlin
  @Provides
    @Singleton
    fun provideGalleryDao(@ApplicationContext context: Context) : GalleryDao {
        return LocalGalleryDao(context)
    }
```
만약 이 또한 Context랑 분리시키기 위해선 Intent를 LiveData로 공유하는것이 아닌, Extra에 들어갈 FileProvider로 생성한 Uri를 LiveData에 담아 뷰로 전달하고, 뷰는 이를 인텐트로 가공하여 전달할 수 있게하면 좋을것 같다고 생각했습니다.

```kotlin
val shareLiveData = mutableLiveData<URI>()
shareLiveData.value = FileProvider~

shareLiveData.observe() {
    Intent(Action_Send).apply {
        putExtra(Stream, it)
    }
}

```

#### · Context Mock 문제

마지막 Error Handling 입니다.<br/>
테스트 도중 Context의 메소드를 사용하기 위해 모의 객체를 생성할 필요가 있었습니다.
<br/> 하지만 기존에 사용하던 모의 객체 생성 라이브러인 Mockito는 Context가 Mock됐다며 테스트가 수행되지 않았습니다.

```kotlin
  @Test
    fun TEST_GET_STRING_NO_PARAM() {
        val data = StringData(15, null)
        val context = mockk<Context>()

        every { context.getString(any()) } returns ""
        data.getResourceMessage(context)
        verify(exactly = 1) { context.getString(any()) } 
    }
```
이를 해결할 방법을 찾던중, suspend 함수도 테스트할 수 있는 MockK라이브러리를 찾아냈고 이를 이용해 해결하였습니다. 