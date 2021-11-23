#!/bin/bash
mvn clean package

jarPath=./activeVsMon/target/activeVsMon-1.0.1-jar-with-dependencies.jar
outputFile=measures/res-final

accessMethods=(buffer actObject)

producersNumbs=(16 32)
consumersNumbs=(32 64)
bufferSizes=(100)

optNumbCoeffs=(600 750 900 1000 1500 2000 2500 3000 3500)
actionCostCoeffs=(1 30 60 100 150 200 300)
complReqOptNumbCoeffs=(10)
secondsOfMeasuring=10

for bufferSize in "${bufferSizes[@]}"; do
    for producersNumb in "${producersNumbs[@]}"; do
        for consumersNumb in "${consumersNumbs[@]}"; do
            for complReqOptNumbCoeff in "${complReqOptNumbCoeffs[@]}"; do
                 for actionCostCoeff in "${actionCostCoeffs[@]}"; do
	            for optNumbCoeff in "${optNumbCoeffs[@]}"; do
                      for accessMethd in "${accessMethods[@]}"; do

                            echo $accessMethd $producersNumb $consumersNumb $bufferSize $optNumbCoeff $complReqOptNumbCoeff $actionCostCoeff $secondsOfMeasuring
                            java -jar $jarPath \
                                  $accessMethd $producersNumb $consumersNumb $bufferSize $optNumbCoeff $complReqOptNumbCoeff $actionCostCoeff $secondsOfMeasuring \
                                  >> "$outputFile-$accessMethd.txt";

                      done
                  done
            done
        done
    done
done
done
