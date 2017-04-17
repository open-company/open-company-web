#!/bin/sh

cat /dev/null > $1
for f in $(find ./target/public/img/ -type f)
do
  (stat -c %Y $f; echo $f; cat $f) | md5sum >> $1
done

cat $1 | md5sum