DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

## SETTINGS FOR ANDROTEST
export DISPLAY=:0

# export ANDROID_HOME=$DIR/android-sdk-linux
# export PATH=$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools/20.0.0:$PATH

### DYNODROID
export SDK_INSTALL=$ANDROID_HOME

### ACTEVE
export Z3_BIN=$DIR/tools/z3/bin/z3
export JavaHeap=512m
export A3T_DIR=$DIR/tools/acteve

### SWIFTHAND
export ADK_ROOT=$ANDROID_HOME
export ADK_LIB=$ADK_ROOT/tools/lib

# GUI RIPPER
# export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64


