#!/bin/bash
mvn clean package

jarPath=./activeVsMon/target/activeVsMon-1.0.1-jar-with-dependencies.jar
outputFile=measures/res

accessMethods=(buffer actObject)

workersNumb=(3 6 10 12)
bufferSizes=(100 1000 10000)

optNumbCoeffs=(10 50 100 150 200 250)
actionCostCoeffs=(1 10 20 50 80)
complReqOptNumbCoeffs=(10 50 100 200);
secondsOfMeasuring=6

for bufferSize in "${bufferSizes[@]}"; do
    for producersNumb in "${workersNumb[@]}"; do
        for consumersNumb in "${workersNumb[@]}"; do
            for optNumbCoeff in "${optNumbCoeffs[@]}"; do
                 for complReqOptNumbCoeff in "${complReqOptNumbCoeffs[@]}"; do
                   for actionCostCoeff in "${actionCostCoeffs[@]}"; do
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
