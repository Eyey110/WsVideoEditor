//
// Created by Eyey on 2020/8/13.
//

#ifndef ANDROID_ANDROID_WS_PLAYER_H
#define ANDROID_ANDROID_WS_PLAYER_H

#include "jni_helper.h"
#include <jni.h>

namespace whensunset {
    namespace wsvideoeditor {
        class WsPlayerAndroid  {

        public:

            WsPlayerAndroid(jobject j_ws_media_player);

            virtual ~WsPlayerAndroid();

            void Init(jobject j_ws_media_player);

            void callPostEvent(jint what, jint arg1, jint arg2, jobject obj);

        private:
            GlobalRef<jobject> j_object_ws_media_player_;
            GlobalRef<jclass> j_class_ws_media_player_;
            jmethodID  j_method_id_post_event_;

            void Release();
        };
    }
}


#endif //ANDROID_ANDROID_WS_PLAYER_H
