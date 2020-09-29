//
// Created by Eyey on 2020/8/13.
//

#include "android_ws_player.h"

namespace whensunset {
    namespace wsvideoeditor {

        WsPlayerAndroid::~WsPlayerAndroid() {

        }

        void WsPlayerAndroid::Init(jobject j_ws_media_player) {
            AttachCurrentThreadIfNeeded attached;
            JNIEnv *env = attached.jni();
            LocalRef<jclass> clazz{env,
                                   env->FindClass("com/whensunset/wsvideoeditorsdk/WsMediaPlayer")};
            jthrowable jexception = env->ExceptionOccurred();
            if (jexception) {
                env->ExceptionDescribe();
                env->DeleteLocalRef(jexception);
                assert(false);
            }
            j_class_ws_media_player_.reset(env, clazz());
            j_method_id_post_event_ = env->GetStaticMethodID(clazz(), "postEventFromNative", "(Ljava/lang/Object;IIILjava/lang/Object)V");
            assert(j_method_id_post_event_);
            j_object_ws_media_player_.reset(env, j_ws_media_player);
            assert(j_object_ws_media_player_());
        }

        void WsPlayerAndroid::callPostEvent(jint what, jint arg1, jint arg2,
                                            jobject obj) {
            AttachCurrentThreadIfNeeded attached;
            JNIEnv *env = attached.jni();
            env->CallStaticVoidMethod(j_class_ws_media_player_(), j_method_id_post_event_, j_object_ws_media_player_(),
                                      what, arg1, arg2, obj);
        }

        void WsPlayerAndroid::Release() {

        }

        WsPlayerAndroid::WsPlayerAndroid(jobject j_ws_media_player) {
            Init(j_ws_media_player);
        }

    }
}
