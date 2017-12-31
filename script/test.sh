cd $(dirname $0)/.. || exit 1
ant compile || exit 1
classpath=$(
  find lib -name '*.jar' |\
  gawk -v DELIM=';' 'BEGIN {first=1} {
    if (!first)
      printf(DELIM);
    first=false;
    printf($0);
  } END {print("");}'
)
echo "Testing..."
java -classpath "$classpath;build" org.tmotte.pdfrpt.test.Test "$@" && echo "DONE"