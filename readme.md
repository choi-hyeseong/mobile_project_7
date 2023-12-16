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
<br/>deeplink의 경우 intent://genimage?prompt=a dog&artstyle=3D 와 같이 url에 데이터를 담아 전달하기 때문에 url 파라미터의 prompt와 화풍을 읽어온 뒤 값이 존재하면 최종 결과 화면으로 이동하게 됩니다.

위 두가지 방식에서 프롬프트가 존재하지 않을경우 기존 절차대로 갤러리 화면으로 이동하게 되며, 화풍 데이터는 없을경우 미 지정된 상태로 설정되기 때문에 존재하지 않아도 됩니다.

## 4. 라이브러리

### ✔ Coroutine

### 📱Retrofit

### 🗡️ Hilt

### 🛠️ Mockk

## 5. Error handling