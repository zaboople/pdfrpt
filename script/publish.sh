#!/bin/bash -e
repo="pdfrpt"

cd $(dirname $0)/..
ant dist

cd ../zaboople.github.generate/lib/external-$repo
echo
echo "Current directory: "$(pwd)
echo -n "WARNING: I am about to do an rm -rf. Is that okay? "
read answer
if [[ $answer == y* ]]; then
  rm -rf *
fi

echo
cd ../../../$repo
echo "Current directory: "$(pwd)
echo -n "I am about to do a huge cp -r. Is that okay? "
read answer
if [[ $answer == y* ]]; then
  cp -r dist/site/* ../zaboople.github.generate/lib/external-$repo
  ant clean
fi

echo
cd ../zaboople.github.generate
git status
echo "Change to "$(pwd)" to finish publish"