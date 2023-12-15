
#viewbinding
-keep class * extends androidx.viewbinding.ViewBinding {*;}
-keep class * implements androidx.viewbinding.ViewBinding {*;}

#dialog
-keep class * extends android.app.Dialog {*;}

#协程
-keep class androidx.lifecycle.PLifeKtKt {*;}

#工具库
-keep class com.jiaqiao.product.** {*;}