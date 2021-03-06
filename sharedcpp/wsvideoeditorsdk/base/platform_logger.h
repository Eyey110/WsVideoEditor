#pragma once

#include "android_logger.h"

#define LOG_TAG2 "wsvideoeditor"
#define LOG_TAGE "lullaby"

#define LOGD(...) do { whensunset::wsvideoeditor::android_logger::LogPrint(ANDROID_LOG_DEBUG, LOG_TAG2, __VA_ARGS__); } while (0)
#define LOGI(...) do { whensunset::wsvideoeditor::android_logger::LogPrint(ANDROID_LOG_INFO, LOG_TAG2, __VA_ARGS__); } while (0)
#define LOGW(...) do { whensunset::wsvideoeditor::android_logger::LogPrint(ANDROID_LOG_WARN, LOG_TAG2, __VA_ARGS__); } while (0)
#define LOGE(...) do { whensunset::wsvideoeditor::android_logger::LogPrint(ANDROID_LOG_ERROR, LOG_TAG2, __VA_ARGS__); } while (0)

#define ELOGD(...) do { whensunset::wsvideoeditor::android_logger::LogPrint(ANDROID_LOG_DEBUG, LOG_TAGE, __VA_ARGS__); } while (0)
#define ELOGI(...) do { whensunset::wsvideoeditor::android_logger::LogPrint(ANDROID_LOG_INFO, LOG_TAGE, __VA_ARGS__); } while (0)
#define ELOGW(...) do { whensunset::wsvideoeditor::android_logger::LogPrint(ANDROID_LOG_WARN, LOG_TAGE, __VA_ARGS__); } while (0)
#define ELOGE(...) do { whensunset::wsvideoeditor::android_logger::LogPrint(ANDROID_LOG_ERROR, LOG_TAGE, __VA_ARGS__); } while (0)

#define XASSERT(cond) do { if (!(cond)) { LOGE( "assert(" #cond ") fail in %s():%d, will abort!", __FUNCTION__, __LINE__); abort(); } } while (0)