package com.home.mindsnap.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.mindsnap.ActivityCallback
import com.home.mindsnap.databinding.GalleryLayoutBinding
import com.home.mindsnap.databinding.ImageLayoutBinding
import com.home.mindsnap.model.Image
import com.home.mindsnap.repository.gallery.LocalGalleryRepository
import com.home.mindsnap.repository.gallery.dao.LocalGalleryDao
import com.home.mindsnap.usecase.GetAllImages
import com.home.mindsnap.usecase.ShareImage
import com.home.mindsnap.viewmodel.GalleryViewModel

class GalleryFragment : Fragment() {

    private lateinit var blurLayer: ImageView
    private var imageLayoutBinding: ImageLayoutBinding? = null
    private var callback: ActivityCallback? = null
    private val viewModel by lazy {
        val repo = LocalGalleryRepository(LocalGalleryDao(requireContext()))
        GalleryViewModel(GetAllImages(repo), ShareImage(repo))
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as ActivityCallback?
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
        imageLayoutBinding = null //memory 누수 문제 예방
    }

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
            imageLayoutBinding?.recyclerView2?.adapter = SmallImageAdapter(this, image)
        }

        viewModel.intentLiveData.observe(viewLifecycleOwner) {
            startActivity(it)
        }

        initChildView(view)
        return view.root
    }

    private fun initChildView(view: GalleryLayoutBinding) {
        // ummm...
        // Initialize blurLayer and enlargedImageView

        blurLayer = ImageView(requireContext()).apply {
            setBackgroundColor(Color.parseColor("#80000000")) // Semi-transparent gray background
            scaleType = ImageView.ScaleType.FIT_XY
            visibility = View.GONE

            setOnClickListener {
                visibility = View.GONE
                imageLayoutBinding?.root?.visibility = View.INVISIBLE
            }
        }.also {
            view.root.addView(
                it, FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                ))
        }

        val bind = ImageLayoutBinding.inflate(layoutInflater).apply {
            root.visibility = View.INVISIBLE
            recyclerView2.apply {
                setHasFixedSize(true)
                layoutManager =
                    GridLayoutManager(requireContext(), 1, LinearLayoutManager.HORIZONTAL, false)
            }
        }
        imageLayoutBinding = bind
        view.root.addView(bind.root)
    }

    private fun onImageClick(image: Image) {
        imageLayoutBinding?.apply {
            root.visibility = View.VISIBLE
            imageView.setImageBitmap(image.bitmap)
            share.setOnClickListener {
                viewModel.shareImage(image.fileName)
            }
        }
        blurLayer.visibility = View.VISIBLE
    }

    inner class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount
                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            }
            else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
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

    inner class SmallImageAdapter(
        private val context: Fragment,
        private val images: List<Image>,
    ) : RecyclerView.Adapter<SmallImageViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallImageViewHolder {
            val imageView = ImageView(context.requireContext()).apply {
                layoutParams = LayoutParams(200,200).also {
                    it.setMargins(10,0,10,0)
                }
                scaleType = ImageView.ScaleType.CENTER_CROP

            }
            return SmallImageViewHolder(imageView)
        }

        override fun onBindViewHolder(holder: SmallImageViewHolder, position: Int) {
            holder.bind(images[position])
        }

        override fun getItemCount(): Int {
            return images.size
        }

    }

    inner class SmallImageViewHolder(private val imageView: ImageView) :
        RecyclerView.ViewHolder(imageView) {
        fun bind(image: Image) {
            imageView.apply {
                setImageBitmap(image.bitmap)
                setOnClickListener {
                    imageLayoutBinding?.imageView?.setImageBitmap(image.bitmap)
                    imageLayoutBinding?.share?.setOnClickListener {
                        viewModel.shareImage(image.fileName)
                    }
                }
            }
        }
    }
}
