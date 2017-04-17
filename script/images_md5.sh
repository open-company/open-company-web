#!/bin/sh

cat /dev/null > /tmp/partial_$1.txt
for f in $(find ./target/public/img/ -type f)
do
  (stat -c %Y $f; echo $f; cat $f) | md5sum >> /tmp/partial_$1.txt
done

cat /tmp/partial_$1.txt | md5sum