# Sources: https://makefiletutorial.com/, https://www.baeldung.com/jni, https://www.youtube.com/watch?v=-_JiA7ldmvg

MAIN_NAME=MandelbrotSetNativeGenerator
OUTPUT_NAME=MandelbrotSet
TARGET_DIR=../../../target
JNI_INCLUDE_DIR_1='${JDK_HOME}'/include/win32
JNI_INCLUDE_DIR_2='${JDK_HOME}'/include

# GCC Oprions
# See https://stackoverflow.com/questions/27891478/error-when-compiling-in-cygwin-error-unknown-type-name-int64-jni-h
# INT_64_WORKAROUND = -D__int64=int64_t
# CC = g++

# all-cc:
# 	${CC} -c ${INT_64_WORKAROUND} -I${JNI_INCLUDE_DIR_1} -I${JNI_INCLUDE_DIR_2} ${MAIN_NAME}.cpp ${MAIN_NAME}.o
# 	${CC} -shared -o ${TARGET_DIR}/${OUTPUT_NAME} ${MAIN_NAME}.o -Wl,--add-stdcall-alias
# 	rm ${MAIN_NAME}.o

# MSVC Options
MSVC_DIR = 'C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Tools\MSVC\14.30.30705'
CL = ${MSVC_DIR}/bin/Hostx64/x64/cl.exe
MS_INCLUDE_DIR_1 ='C:\Program Files (x86)\Windows Kits\10\Include\10.0.19041.0\ucrt\include'

all:
	${CL}  -I${MS_INCLUDE_DIR_1} -I${JNI_INCLUDE_DIR_1} -I${JNI_INCLUDE_DIR_2} -MD -LD ${MAIN_NAME}.cpp -Fe${OUTPUT_NAME}.dll
	mv ${OUTPUT_NAME} ${TARGET_DIR}

# $(VC_IncludePath);$(WindowsSDK_IncludePath);