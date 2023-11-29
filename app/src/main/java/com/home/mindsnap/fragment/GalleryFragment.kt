package com.home.mindsnap.fragment

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.home.mindsnap.R

class ImageAdapter(
    private val context: Fragment,
    private val imageResources: Array<Int>,
    private val onImageClick: (Int) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = ImageView(context.requireContext())
        val screenWidth = context.resources.displayMetrics.widthPixels
        val imageSize = screenWidth / 2 - 64 // 전체 가로 화면의 1/2 크기로 설정

        val params = LinearLayout.LayoutParams(
            imageSize,
            imageSize
        )
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setOnClickListener {
            onImageClick(imageResources[imageView.tag as Int])
        }
        return ImageViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageView = holder.itemView as ImageView
        imageView.setImageResource(imageResources[position])
        imageView.tag = position
    }

    override fun getItemCount(): Int {
        return imageResources.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
public class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnNextPage: Button
    private lateinit var galleryTitle: TextView
    private lateinit var blurLayer: ImageView
    private lateinit var enlargedImageView: ImageView
    private lateinit var smallImagesContainer: LinearLayout

    private val imageResources = arrayOf(
        R.drawable.img_1,
        R.drawable.img_2,
        R.drawable.img_3,
        R.drawable.img_4,
        R.drawable.img_5,
        R.drawable.img_6,
        R.drawable.img_6  // ... 이미지 추가
    )

    private val smallImageViews = mutableListOf<ImageView>()
    private val imageSpacing = 16 // Image spacing

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        galleryTitle = view.findViewById(R.id.GalleryTitle)
        btnNextPage = view.findViewById(R.id.btnNextPage)
        smallImagesContainer = view.findViewById(R.id.smallImagesContainer)

        // Initialize blurLayer and enlargedImageView
        blurLayer = ImageView(requireContext())
        blurLayer.setBackgroundColor(Color.parseColor("#80000000")) // Semi-transparent gray background
        blurLayer.scaleType = ImageView.ScaleType.FIT_XY
        blurLayer.visibility = View.GONE
        val blurLayerParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        (view as FrameLayout).addView(blurLayer, blurLayerParams)

        enlargedImageView = ImageView(requireContext())
        enlargedImageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
        enlargedImageView.visibility = View.GONE
        val enlargedImageViewParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        (view).addView(enlargedImageView, enlargedImageViewParams)

        val horizontalScrollView = HorizontalScrollView(requireContext())
        smallImagesContainer = LinearLayout(requireContext())
        smallImagesContainer.orientation = LinearLayout.HORIZONTAL
        val smallImagesContainerParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        smallImagesContainerParams.gravity = Gravity.BOTTOM
        Log.d("kkang",ViewGroup.LayoutParams.MATCH_PARENT.toString())
        smallImagesContainerParams.bottomMargin = getScreenWidth() / 4

        horizontalScrollView.addView(smallImagesContainer)
        (view).addView(horizontalScrollView, smallImagesContainerParams)

        galleryTitle.text = "Gallery"
        val imageSpacing = 16
        val smallImageWidth = getScreenWidth() / 4
        val smallImageHeight = smallImageWidth

        blurLayer.setOnClickListener {
            blurLayer.visibility = View.GONE
            enlargedImageView.visibility = View.GONE
            smallImagesContainer.removeAllViews()
        }
        (blurLayer.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = ImageAdapter(this, imageResources, ::onImageClick)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, imageSpacing, true))


        for (i in imageResources.indices) {
            val imageView = ImageView(requireContext())
            imageView.setImageResource(imageResources[i])

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            params.weight = 1.0f
            params.setMargins(imageSpacing, imageSpacing, imageSpacing, imageSpacing)

            imageView.layoutParams = params

            smallImageViews.add(imageView)

            imageView.layoutParams = params
            imageView.setOnClickListener {
                enlargedImageView.setImageDrawable(imageView.drawable)
                enlargedImageView.visibility = View.VISIBLE
                blurLayer.visibility = View.VISIBLE
                smallImagesContainer.removeAllViews()

                for (otherImageView in smallImageViews) {
                    val otherSmallImage = ImageView(requireContext())
                    otherSmallImage.setImageDrawable(otherImageView.drawable)

                    val smallParams =
                        LinearLayout.LayoutParams(smallImageWidth, smallImageHeight)
                    smallParams.setMargins(imageSpacing, 0, imageSpacing, 20)
                    otherSmallImage.layoutParams = smallParams

                    val parent = otherSmallImage.parent as? ViewGroup
                    parent?.removeView(otherSmallImage)

                    smallImagesContainer.addView(otherSmallImage)

                    if (!smallImageViews.contains(otherImageView)) {
                        smallImageViews.add(otherImageView)
                    }

                    otherSmallImage.setOnClickListener {
                        enlargedImageView.setImageDrawable(otherImageView.drawable)
                        enlargedImageView.visibility = View.VISIBLE
                        blurLayer.visibility = View.VISIBLE
                    }
                }
            }

            smallImageViews.add(imageView)
        }

        btnNextPage.setOnClickListener {
            Toast.makeText(requireContext(), "Create Image", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.widthPixels
    }

    private fun getScreenHeight(): Int {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.heightPixels
    }

    private fun onImageClick(imageResource: Int) {
        enlargedImageView.setImageResource(imageResource)
        enlargedImageView.visibility = View.VISIBLE
        blurLayer.visibility = View.VISIBLE

        smallImagesContainer.removeAllViews()

        for (otherImageResource in imageResources) {
            val otherSmallImage = ImageView(requireContext())
            otherSmallImage.setImageResource(otherImageResource)

            val smallParams = LinearLayout.LayoutParams(getScreenWidth() / 4-32, getScreenWidth() / 4 - 32)
            smallParams.setMargins(0, 0, imageSpacing, 20)
            otherSmallImage.layoutParams = smallParams

            val parent = otherSmallImage.parent as? ViewGroup
            parent?.removeView(otherSmallImage)

            smallImagesContainer.addView(otherSmallImage)

            otherSmallImage.setOnClickListener {
                enlargedImageView.setImageResource(otherImageResource)
                enlargedImageView.visibility = View.VISIBLE
                blurLayer.visibility = View.VISIBLE
            }
        }
    }

    class GridSpacingItemDecoration(
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
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
}
