#!/bin/sh

for f in $(find ./target/public/img/ -type f)
do
  echo $f >> /tmp/partial_$1.txt
  (stat -c %Y $f; echo $f; cat $f) | md5sum >> /tmp/partial_$1.txt
done

cat /tmp/partial_$1.txt | md5sum