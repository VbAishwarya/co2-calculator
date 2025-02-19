#!/bin/bash

startCity=""
endCity=""
transportationMode=""

while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    --start)
    startCity="$2"
    shift
    shift
    ;;
    --end)
    endCity="$2"
    shift
    shift
    ;;
    --transportation-method)
    transportationMethod="$2"
    shift
    shift
    ;;
    *)
    shift
    ;;
esac
done
echo "start city: $startCity"
echo "end city: $endCity"
java -jar target/co2-calculator-1.0-SNAPSHOT.jar --start "$startCity" --end "$endCity" --transportation-method $transportationMethod

read -p "Press [Enter] key to close the terminal..."