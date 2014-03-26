# IF we're on the classes server, reset the CS4351 variable to the right place
if [ $HOME=/classes/cs4351/$LSUUSERNAME ]; then
  export CS4351=/classes/cs4351/cs4351_bau/pub
fi

export PROG=chap5
export TIGER=${CS4351}/tiger
export CLASSPATH=.:..:${CS4351}/classes/${PROG}:${CS4351}/classes