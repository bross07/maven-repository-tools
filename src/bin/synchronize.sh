#!/bin/sh

PID=$$
RUNNING=`ps -ef | grep synchronize.sh | grep -v 'sh -c' | grep -v grep | grep -v $PID`
if [ ! -z "$RUNNING" ]; then
  echo Sync already running... exiting
  echo $RUNNING
  exit 1
fi


TOOLS_BASE=/home/projects/maven/repository-tools
(
  cd $TOOLS_BASE/syncopate
  ./sync
  retval=$?; if [ $retval != 0 ]; then exit $retval; fi
)
retval=$?; if [ $retval != 0 ]; then exit $retval; fi

(
  cd $TOOLS_BASE/repoclean
  ./repoclean.sh synchronize.properties
  retval=$?; if [ $retval != 0 ]; then exit $retval; fi
)
retval=$?; if [ $retval != 0 ]; then exit $retval; fi


# get poms from svn and generate checksums
(
  cd /home/projects/maven/repository-staging/to-ibiblio/maven2
  svn export --force svn://svn.codehaus.org/maven/scm/repository/ .
  retval=$?; if [ $retval != 0 ]; then exit $retval; fi
  for f in `svn list -R svn://svn.codehaus.org/maven/scm/repository/ | grep .pom` ; do openssl md5 $f > $f.md5 ; openssl sha1 $f > $f.sha1; done
)
retval=$?; if [ $retval != 0 ]; then exit $retval; fi


(
  cd $TOOLS_BASE/ibiblio-sync
  ./synchronize-codehaus-to-ibiblio.sh
  retval=$?; if [ $retval != 0 ]; then exit $retval; fi
)
retval=$?; if [ $retval != 0 ]; then exit $retval; fi
