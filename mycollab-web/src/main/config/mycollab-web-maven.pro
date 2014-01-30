-dontshrink
-dontoptimize
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-ignorewarnings


-keep class com.esofthead.mycollab.**.domain.* {
    public private protected *;
}

-keep class com.esofthead.mycollab.**.domain.criteria.* {
    public private protected *;
}

-keep class com.esofthead.mycollab.**.dao.* {
    public private protected *;
}

-keep class com.esofthead.mycollab.**.service.* {
    public private protected *;
}

-keep class com.esofthead.mycollab.**.service.ibatis.* {
    public private protected *;
}

-keep class com.esofthead.mycollab.**.servlet.* {
    public private protected *;
}

# Keep - Applications. Keep all application classes, along with their 'main'
# methods.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Also keep - Database drivers. Keep all implementations of java.sql.Driver.
-keep class * extends java.sql.Driver

# Also keep - Swing UI L&F. Keep all extensions of javax.swing.plaf.ComponentUI,
# along with the special 'createUI' method.
-keep class * extends javax.swing.plaf.ComponentUI {
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent);
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}
