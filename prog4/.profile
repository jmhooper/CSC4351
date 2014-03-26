if [ "${HOME}" = "/classes/cs4351/${LSUUSERNAME}" ]; then
  # If we're on the classes server, use the CS4351 folder provided there
  export CS4351=/classes/cs4351/cs4351_bau/pub
else
  # Otherwise, use the folder that is downloaded by the setup script
  export CS4351=${PROGDIR}/CS4351
fi

export PROG=chap6
export TIGER=${CS4351}/tiger
export CLASSPATH=.:..:${CS4351}/classes/${PROG}:${CS4351}/classes