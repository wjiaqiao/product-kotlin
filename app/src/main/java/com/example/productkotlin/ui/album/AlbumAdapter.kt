package com.example.productkotlin.ui.album

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.productkotlin.base.ImagePath
import com.example.productkotlin.databinding.ItemAlbumImageBinding
import com.jiaqiao.product.ext.load
import com.jiaqiao.product.widget.adapter.ProductBaseBindingAdapter

class AlbumAdapter : ProductBaseBindingAdapter<ImagePath, ItemAlbumImageBinding>() {


    override fun convert(bind: ItemAlbumImageBinding, item: ImagePath, holder: BaseViewHolder) {
        bind.iv.load(item.path)
    }

}