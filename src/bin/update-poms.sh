#!/bin/sh

src=/home/projects/maven/repository-staging/pom-svn-repository
dest=/home/projects/maven/repository-staging/to-ibiblio/maven2

/usr/local/subversion/bin/svn update $src

./copy-updated-poms.sh