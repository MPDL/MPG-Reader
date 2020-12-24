#!/bin/bash
while true; do
    response=$(curl http://dev-mpgreader.mpdl.mpg.de \
        --write-out %{http_code} \
        --silent \
        --output /dev/null \
    )
    Date=`date +%Y-%m-%d::%H:%M:%S`
    if test "$response" -ge 200 && test "$response" -le 299
    then
        echo $Date' Reader api is alive'
    else
        echo $Date' Reader api is not working'
        # send out alert email or msg
    fi
    sleep 30
done
