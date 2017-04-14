#!/bin/sh

cat /dev/null > /tmp/img_md5.txt
for f in $(find ./target/public/img/ -type f)
do
  echo $f
  (stat -c %Y $f; echo $f; cat $f) | md5sum >> /tmp/img_md5.txt
done

cat /tmp/img_md5.txt | md5sum