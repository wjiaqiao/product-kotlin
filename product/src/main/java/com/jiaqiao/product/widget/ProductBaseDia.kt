package com.jiaqiao.product.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.jiaqiao.product.base.ProductBaseAct
import com.jiaqiao.product.ext.*

/**
 * 基础dialog类
 * */
open abstract class ProductBaseDia<VB : ViewBinding>(
    @NonNull context: Context,
    @StyleRes themeResId: Int = 0,
    initLoadVM: Boolean = false,
) : Dialog(context, themeResId) {

    //取消标识符
    private var cancelableBol: Boolean = true

    //点击外部取消标识符
    private var canceledOnTouchOutsideBol: Boolean = true

    //ViewBinding的对象
    lateinit var mViewBind: VB

    //dialog依附的AppCompatActivity对象
    private var contextAc: AppCompatActivity? = null

    //布局的基础类
    private val rootView by lazy {
        LinearLayout(context).also {
            it.fitsSystemWindows = true
            it.setBackgroundColor(Color.TRANSPARENT)
            it.click {
                if (isShowing && cancelableBol && canceledOnTouchOutsideBol) {
                    dismiss()
                }
            }
        }
    }

    /**
     * dialog的背景透明度
     * */
    open fun dimAmount(): Float {
        return 0.4f
    }

    /**
     * rootview在dialog中的位置
     * */
    open fun gravity(): Int {
        return Gravity.CENTER
    }

    /**
     * rootview在dialog中的宽度
     * */
    open fun width(): Int {
        return ViewGroup.LayoutParams.MATCH_PARENT
    }

    /**
     * rootview在dialog中的高度
     * */
    open fun height(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    /**
     * 是否启用状态栏字体自适应
     * */
    open fun autoStatusBarFont(): Boolean {
        return true
    }

    init {
        if (context is AppCompatActivity) {
            contextAc = context
        }
        if (context is ProductBaseAct) {
            context.add(this)
        } else if (context is AppCompatActivity) {
            context.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        if (isShowing) {
                            dismiss()
                        }
                        context.lifecycle?.removeObserver(this)
                    }
                }
            })
        }
        if (initLoadVM) {
            initViewBind()
        }
    }

    /**
     * 创建ViewBinding
     * 利用反射 根据泛型得到 ViewBinding
     */
    private fun initViewBind() {
        mViewBind = createViewBinding()
    }


    //初始化view方法
    abstract fun initView()

    //load重复加载view或刷新控件的方法
    open fun loadView() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::mViewBind.isInitialized) {
            initViewBind()
        }
        setContentView(rootView)
        window?.let { win ->
            try {
                // 去除部分机型顶部蓝色条
                val dividerID =
                    context.resources.getIdentifier("android:id/titleDivider", null, null)
                findViewById<View>(dividerID)?.setBackgroundColor(Color.TRANSPARENT)
            } catch (thr: Throwable) {
            }

            win.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val lp = win.attributes
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.MATCH_PARENT
            //兼容刘海屏
            if (Build.VERSION.SDK_INT >= 28) {
                lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
            win.attributes = lp
            //取消状态栏
            win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //dialog内容延伸至状态栏
            val options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            win.decorView.systemUiVisibility = options

            win.setDimAmount(dimAmount())
        }

        rootView.removeAllViews()
        rootView.addView(mViewBind.root)

        rootView.gravity = gravity()
        mViewBind.root.layoutParams?.let {
            if (it.width < 0) {
                mViewBind.root.setWidth(width())
            }
            if (it.height < 0) {
                mViewBind.root.setHeight(height())
            }
        }
        mViewBind.root.isClickable = true
        mViewBind.root.isFocusable = true
        initView()
    }

    override fun onStart() {
        super.onStart()
        contextAc?.let {
            if (autoStatusBarFont()) {
                if (it.isStatusBarBlackFont()) {
                    window?.statusBarBlackFont()
                } else if (it.isStatusBarWhiteFont()) {
                    window?.isStatusBarWhiteFont()
                }
            }
            return@let
        }
        loadView()
    }

    override fun show() {
        if (!isShowing) {
            super.show()
        }
    }

    override fun setCancelable(flag: Boolean) {
        super.setCancelable(flag)
        cancelableBol = flag
    }

    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        super.setCanceledOnTouchOutside(cancel)
        if (cancel && !canceledOnTouchOutsideBol) {
            canceledOnTouchOutsideBol = true
        }
        canceledOnTouchOutsideBol = cancel
    }

}