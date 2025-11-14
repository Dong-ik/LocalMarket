#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
SAVED="`pwd`"
SCRIPT_DIR="`cd "$(dirname "$0")" && pwd`"
cd "$SAVED" >/dev/null
APP_HOME="$SCRIPT_DIR"
APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here.
DEFAULT_JVM_OPTS='-Xmx64m -Xms64m'
JAVA_OPTS=${JAVA_OPTS:=""}
GRADLE_OPTS=${GRADLE_OPTS:=""}

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

if [ -z "$JAVA_HOME" ] ; then
    JAVACMD="java"
else
    JAVACMD="$JAVA_HOME/bin/java"
fi

if [ ! -x "$JAVACMD" ] ; then
    echo "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
    exit 1
fi

CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS "-Dorg.gradle.appname=$APP_BASE_NAME" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
