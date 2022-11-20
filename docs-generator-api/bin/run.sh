#!/bin/sh

# Setting default JVM opts
if [ -z "$JAVA_XMS" ]; then
      JAVA_XMS=64m
fi
if [ -z "$JAVA_XMX" ]; then
      JAVA_XMX=128m
fi
if [ -z "$JAVA_MAX_META" ]; then
      JAVA_MAX_META=128m
fi

# Starting app with PID 1
exec java -Xms$JAVA_XMS -Xmx$JAVA_XMX -XX:MaxMetaspaceSize=$JAVA_MAX_META $JAVA_OPTS ${JAVA_OPTS} -Dloader.path=config -cp /app/resources/:./config:/app/config:/app/classes/:/app/libs/* "org.springframework.boot.loader.PropertiesLauncher" "$@"
