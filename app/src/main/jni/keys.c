#include <jni.h>

JNIEXPORT jstring JNICALL
Java_at_ac_tuwien_mns_group3_mnsg3e3_di_ServiceModule_getMlsApiKey(JNIEnv *env, jobject instance) {

 return (*env)->  NewStringUTF(env, "dGVzdA==");
}
