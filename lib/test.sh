rm -f sample.pdf &&\
 ant compile &&\
 source lib/classpath.sh &&\
 echo 'Creating pdf...' &&\
 java  -mx20m org.tmotte.pdfrpt.test.Test "$@" sample.pdf &&\
 echo 'pdf complete.' &&\
 /c/Program\ Files/Adobe/Acrobat\ 7.0/Reader/AcroRd32.exe  sample.pdf &&\
 rm -f sample.pdf