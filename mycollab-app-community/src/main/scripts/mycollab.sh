#!/bin/sh

# -----------------------------------------------------------------------------
# Control Script for the MyCollab Server
#
# Environment Variable Prerequisites
#
#   MYCOLLAB_HOME   May point at your MyCollab "build" directory.
#   MYCOLLAB_OUT    (Optional) Full path to a file where stdout and stderr
#                   will be redirected.
#                   Default is $CATALINA_BASE/logs/catalina.out
#   MYCOLLAB_PORT   Port of server to allow user access to server
#   MYCOLLAB_OPTS   (Optional) Java runtime options used when the "start",
#                   "stop" command is executed.
#                   Include here and not in JAVA_OPTS all options, that should
#                   only be used by MyCollab itself, not by the stop process,
#                   the version command etc.
#                   Examples are heap size, GC logging, JMX ports etc.
#
#   JAVA_HOME       Must point at your Java Development Kit installation.
#                   Required to run the with the "debug" argument.
#
#   MYCOLLAB_PID    (Optional) Path of the file which should contains the pid
#                   of the catalina startup java process, when start (fork) is
#                   used
# -----------------------------------------------------------------------------

export MYCOLLAB_OPTS="-Xms756m -Xmx1024m -XX:NewSize=256m -XX:MaxPermSize=256m -XX:+DisableExplicitGC"
export MYCOLLAB_PORT=8080

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false
darwin=false
os400=false
case "`uname`" in
CYGWIN*) cygwin=true;;
Darwin*) darwin=true;;
OS400*) os400=true;;
esac

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# Only set MYCOLLAB_HOME if not already set
[ -z "$MYCOLLAB_HOME" ] && MYCOLLAB_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

if [ -z "$MYCOLLAB_OUT" ] ; then
  MYCOLLAB_OUT="$MYCOLLAB_HOME"/logs/mycollab.out
fi

echo $MYCOLLAB_HOME
echo Log $MYCOLLAB_OUT

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$MYCOLLAB_HOME" ] && MYCOLLAB_HOME=`cygpath --unix "$MYCOLLAB_HOME"`
fi

# For OS400
if $os400; then
  # Set job priority to standard for interactive (interactive - 6) by using
  # the interactive priority - 6, the helper threads that respond to requests
  # will be running at the same priority as interactive jobs.
  COMMAND='chgjob job('$JOBNAME') runpty(6)'
  system $COMMAND

  # Enable multi threading
  export QIBM_MULTI_THREADED=Y
fi

# Bugzilla 37848: When no TTY is available, don't output to console
have_tty=0
if [ "`tty`" != "not a tty" ]; then
    have_tty=1
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  JAVA_HOME=`cygpath --absolute --windows "$JAVA_HOME"`
  MYCOLLAB_HOME=`cygpath --absolute --windows "$MYCOLLAB_HOME"`
fi

# Set standard commands for invoking Java.
if [ -z "$JAVA_HOME" ] ; then
  _RUNJAVA=java
else
  _RUNJAVA="$JAVA_HOME"/bin/java
fi  

# ----- Execute The Requested Command -----------------------------------------

# Bugzilla 37848: only output this if we have a TTY
if [ $have_tty -eq 1 ]; then
  echo "Using MYCOLLAB_HOME:   $MYCOLLAB_HOME"
  if [ "$1" = "debug" ] ; then
    echo "Using JAVA_HOME:       $JAVA_HOME"
  fi
  
  if [ ! -z "$MYCOLLAB_PID" ]; then
    echo "Using MYCOLLAB_PID:    $MYCOLLAB_PID"
  fi
fi



if [ "$1" = "start" ] ; then

  if [ ! -z "$MYCOLLAB_PID" ]; then
    if [ -f "$MYCOLLAB_PID" ]; then
      if [ -s "$MYCOLLAB_PID" ]; then
        echo "Existing PID file found during start."
        if [ -r "$MYCOLLAB_PID" ]; then
          PID=`cat "$MYCOLLAB_PID"`
          ps -p $PID >/dev/null 2>&1
          if [ $? -eq 0 ] ; then
            echo "MyCollab appears to still be running with PID $PID. Start aborted."
            exit 1
          else
            echo "Removing/clearing stale PID file."
            rm -f "$MYCOLLAB_PID" >/dev/null 2>&1
            if [ $? != 0 ]; then
              if [ -w "$MYCOLLAB_PID" ]; then
                cat /dev/null > "$MYCOLLAB_PID"
              else
                echo "Unable to remove or clear stale PID file. Start aborted."
                exit 1
              fi
            fi
          fi
        else
          echo "Unable to read PID file. Start aborted."
          exit 1
        fi
      else
        rm -f "$MYCOLLAB_PID" >/dev/null 2>&1
        if [ $? != 0 ]; then
          if [ ! -w "$MYCOLLAB_PID" ]; then
            echo "Unable to remove or write to empty PID file. Start aborted."
            exit 1
          fi
        fi
      fi
    fi
  fi

  shift
  touch "$MYCOLLAB_OUT"
  cd ..
  eval \"$_RUNJAVA\" $MYCOLLAB_OPTS \
      -jar $MYCOLLAB_HOME/runner.jar --port $MYCOLLAB_PORT --stop-port 8079 --stop-key esoftheadsecretkey  
 ####>> "$MYCOLLAB_OUT" 2>&1 "&"

  if [ ! -z "$MYCOLLAB_PID" ]; then
    echo $! > "$MYCOLLAB_PID"
  fi

elif [ "$1" = "stop" ] ; then

  shift

  SLEEP=5
  if [ ! -z "$1" ]; then
    echo $1 | grep "[^0-9]" >/dev/null 2>&1
    if [ $? -gt 0 ]; then
      SLEEP=$1
      shift
    fi
  fi

  FORCE=0
  if [ "$1" = "-force" ]; then
    shift
    FORCE=1
  fi

  if [ ! -z "$MYCOLLAB_PID" ]; then
    if [ -f "$MYCOLLAB_PID" ]; then
      if [ -s "$MYCOLLAB_PID" ]; then
        kill -0 `cat "$MYCOLLAB_PID"` >/dev/null 2>&1
        if [ $? -gt 0 ]; then
          echo "PID file found but no matching process was found. Stop aborted."
          exit 1
        fi
      else
        echo "PID file is empty and has been ignored."
      fi
    else
      echo "\$MYCOLLAB_PID was set but the specified file does not exist. Is MyCollab running? Stop aborted."
      exit 1
    fi
  fi

  cd ..
  eval \"$_RUNJAVA\" -jar $MYCOLLAB_HOME/runner.jar  --stop-port 8079 --stop-key esoftheadsecretkey --stop 

  if [ ! -z "$MYCOLLAB_PID" ]; then
    if [ -f "$MYCOLLAB_PID" ]; then
      while [ $SLEEP -ge 0 ]; do
        kill -0 `cat "$MYCOLLAB_PID"` >/dev/null 2>&1
        if [ $? -gt 0 ]; then
          rm -f "$MYCOLLAB_PID" >/dev/null 2>&1
          if [ $? != 0 ]; then
            if [ -w "$MYCOLLAB_PID" ]; then
              cat /dev/null > "$MYCOLLAB_PID"
            else
              echo "MyCollab stopped but the PID file could not be removed or cleared."
            fi
          fi
          break
        fi
        if [ $SLEEP -gt 0 ]; then
          sleep 1
        fi
        if [ $SLEEP -eq 0 ]; then
          if [ $FORCE -eq 0 ]; then
            echo "MyCollab did not stop in time. PID file was not removed."
          fi
        fi
        SLEEP=`expr $SLEEP - 1 `
      done
    fi
  fi

  KILL_SLEEP_INTERVAL=5
  if [ $FORCE -eq 1 ]; then
    if [ -z "$MYCOLLAB_PID" ]; then
      echo "Kill failed: \$MYCOLLAB_PID not set"
    else
      if [ -f "$MYCOLLAB_PID" ]; then
        PID=`cat "$MYCOLLAB_PID"`
        echo "Killing MyCollab with the PID: $PID"
        kill -9 $PID
        while [ $KILL_SLEEP_INTERVAL -ge 0 ]; do
            kill -0 `cat "$MYCOLLAB_PID"` >/dev/null 2>&1
            if [ $? -gt 0 ]; then
                rm -f "$MYCOLLAB_PID" >/dev/null 2>&1
                if [ $? != 0 ]; then
                    echo "MyCollab was killed but the PID file could not be removed."
                fi
                break
            fi
            if [ $KILL_SLEEP_INTERVAL -gt 0 ]; then
                sleep 1
            fi
            KILL_SLEEP_INTERVAL=`expr $KILL_SLEEP_INTERVAL - 1 `
        done
        if [ $KILL_SLEEP_INTERVAL -gt 0 ]; then
            echo "MyCollab has not been killed completely yet. The process might be waiting on some system call or might be UNINTERRUPTIBLE."
        fi
      fi
    fi
  fi

else

  echo "Usage: mycollab.sh ( commands ... )"
  echo "commands:"

  echo "  start             Start MyCollab in a separate window"
  echo "  stop              Stop MyCollab, waiting up to 5 seconds for the process to end"
  echo "  stop -force       Stop MyCollab, wait up to 5 seconds and then use kill -KILL if still running"
  echo "Note: Waiting for the process to end and use of the -force option require that \$MYCOLLAB_PID is defined"
  exit 1

fi