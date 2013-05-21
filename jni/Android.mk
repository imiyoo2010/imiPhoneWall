LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := imiFunction
LOCAL_SRC_FILES := com_imiFirewall_Function.cpp

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)
