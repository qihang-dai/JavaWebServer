#!/bin/bash
# Author: @miabelar F20

MAXWAIT=60

# go to the same directory as the script. Makes edge cases easier
cd "$(dirname "$0")"

if [[ ( $1 == "--help") ||  $1 == "-h" ]]  ; then
    echo "Usage: `basename $0` [-h] {make|lint|up|diff <method> <route> [data]|halt|clean}"
    echo "This script should be run in the same folder as pom.xml, src and www"
    echo "Options:"
    echo "-h or --help : displays the usage for the script"
    echo
    echo "{make|lint|up|diff <method> <route> [data]|halt|clean} : choose one of the six commands:"
    echo
    echo "make : takes src, www, and pom.xml and puts them in a new directory called spark_version."
    echo "This folder will contain the same files except with a Spark version of the WebServer"
    echo "This spark version is made by replacing the contents of WebServer with Spark imports and code."
    echo "As a result, this new code may not compile and you will have to run lint to ensure the new code compiles"
    echo
    echo "lint : Makes sure that the Spark version of the WebServer does not run into any compilation errors and can be run"
    echo
    echo "up : Launches the original server on port 45555 and the Spark server on port 45556."
    echo "Make sure that the WebServer class does not hard code the ports and accepts command line args"
    echo
    echo "diff <method> <route> [data]: Compares the output of the two servers. Parameters:"
    echo "method : The HTTP method. Examples: GET, POST, HEAD"
    echo "route : The route. Examples: /hello, /hi"
    echo "data : Optional. The data for the request. Follows the cURL data flag: https://curl.haxx.se/docs/manpage.html"
    echo "Examples: 'user=user1&pass=abcd' and 'user=user1' "
    echo "The diff command uses the diff command. Read more about it here: https://web.cs.dal.ca/~johnston/unix/diff.html"
    echo
    echo "halt : Stops both the original and Spark server"
    echo
    echo "clean : removes the spark_version folder"
    echo
    exit 0
fi

if [[ $1 == "make" ]]; then 
    if [ -d ./spark_version ] ; then
        echo "Directory already exists"
        exit 0
    fi
    isCommentInFile=$(cat ./src/main/java/edu/upenn/cis/cis455/WebServer.java | grep -c "Leave this comment for the Spark comparator tool")
    if [ $isCommentInFile -eq 0 ]; then
       echo "Error: You need to have the following comment in your WebServer.java: // ... and above here. Leave this comment for the Spark comparator tool";
       exit 0;
    fi
    # make folder and copy
    mkdir ./spark_version
    cp ./pom.xml ./spark_version
    if [ -d ./www ] ; then
        cp -r ./www ./spark_version/www
    fi
    cp -r ./src ./spark_version/src
    # go in and replace imports to spark
    sed -i 's@import static edu.upenn.cis.cis455.WebServiceFactory.*;@import static spark.Spark.*;\nimport spark.*;@g' ./spark_version/src/main/java/edu/upenn/cis/cis455/WebServer.java
    sed -i 's@import edu.upenn.cis.cis455.m2.interfaces@//import edu.upenn.cis.cis455.m2.interfaces@g' ./spark_version/src/main/java/edu/upenn/cis/cis455/WebServer.java
    # add an afterAfter filter for headers to match writeup
    sed -i '/Leave this comment for the Spark comparator tool/a afterAfter((req, res) -> {\n\t\t\tres.header("Content-Length", "" + res.body().length());\n\t\t\tres.header("Connection", "close");\n\t\t});' ./spark_version/src/main/java/edu/upenn/cis/cis455/WebServer.java
    exit 0
fi

if [[ $1 == "lint" ]]; then 
    if [ -d ./spark_version ] ; then
        cd ./spark_version
        mvn compile
        cd ..
    else
        echo "You need to run make"
    fi
    exit 0
fi

if [[ $1 == "up" ]]; then 
    if [ -d ./spark_version ] ; then
        fuser -k 45555/tcp  > /dev/null 2>&1
        fuser -k 45556/tcp  > /dev/null 2>&1
        # start the impl server
        eval "mvn install exec:java -Dexec.args='45555 $(pwd)/www' &" >/dev/null
        # wait for port to be listening
        # wait max for 10 seconds
        echo "Starting original server... (waiting max ${MAXWAIT} seconds)"
        original_server_counter=0
        while [ -z "$(lsof -t -i:45555)" ] && [ $original_server_counter -lt ${MAXWAIT} ]
        do
         sleep 1
         original_server_counter=$(( $original_server_counter + 1 ))
        done
        if [ $original_server_counter -eq ${MAXWAIT} ] ; then 
            echo "Original server could not start"
            fuser -k 45555/tcp  > /dev/null 2>&1
            fuser -k 45556/tcp  > /dev/null 2>&1
            exit 0
        fi
        echo "Original server running"
        # start the spark server
        cd ./spark_version
        echo "Starting spark server... (waiting max ${MAXWAIT} seconds)"
        eval "mvn install exec:java -Dexec.args='45556 $(pwd)/www' &" >/dev/null
        # wait for port to be listening
        # listen max for 10 seconds
        spark_server_counter=0
        while [ -z "$(lsof -t -i:45556)" ] && [ $spark_server_counter -lt ${MAXWAIT} ]
        do
         sleep 1
         spark_server_counter=$(( $spark_server_counter + 1 ))
        done
        if [ $spark_server_counter -eq ${MAXWAIT} ] ; then 
            echo "Spark server could not start. This could be transient. Please try again before looking into why the server did not start."
            fuser -k 45555/tcp  > /dev/null 2>&1
            fuser -k 45556/tcp  > /dev/null 2>&1
            exit 0
        fi
        echo "Spark server running"
    else
        echo "You need to run make"
    fi
    exit 0
fi

if [[ $1 == "diff" ]]; then 
    if [ ! -d ./spark_version ] ; then
        echo "You need to run make"
        exit 0
    fi
    if [ -z "$(lsof -t -i:45555)" ] ; then
        echo "Original server not running. You need to run ./spark_compare.sh up"
        exit 0
    fi
    if [ -z "$(lsof -t -i:45556)" ] ; then
        echo "Spark server not running. You need to run ./spark_compare.sh up"
        exit 0
    fi
    if [[ -z "$2" ||  -z "$3" ]]  ; then
        echo "You must provide a method and a route"
        exit 0
    else
        # convert to uppercase
        method=$(echo $2 | tr 'a-z' 'A-Z')
        route=$3
        # add the data flag if needed
        data="-d $4"
        if [ -z "$4" ]; then 
            data=""
        fi
        original_res=$(curl -X $method $data -i http://localhost:45555$route 2>/dev/null)
        spark_res=$(curl -X $method $data -i http://localhost:45556$route 2>/dev/null)
        # filter unnecessary headers
        original_res=$(echo "$original_res" | sed "/Server:/d")
        spark_res=$(echo "$spark_res" | sed "/Server:/d")
        original_res=$(echo "$original_res" | sed "/Date:/d")
        spark_res=$(echo "$spark_res" | sed "/Date:/d")
        # sort content
        sorted_original_res=$(sort <(echo "$original_res"))
        sorted_spark_res=$(sort <(echo "$spark_res"))

        contents_diff=$(diff <(echo "$sorted_original_res") <(echo "$sorted_spark_res"))
        echo ""
        if [ -z "$contents_diff" ]; then 
            echo "No difference between the two responses!"
        else
            echo "Differences (top is your implementation, bottom is Spark's):"
            echo ""
            echo "$contents_diff"
            echo ""
        fi
    fi
    exit 0
fi

if [[ $1 == "halt" || $1 == "clean" ]]; then 
    if [ -d ./spark_version ] ; then
        echo "Shutting down servers..."
        # sometimes weird output comes when killing the servers
        exec >> /dev/null
        exec 2>&1
        fuser -k 45555/tcp > /dev/null 2>&1
        fuser -k 45556/tcp > /dev/null 2>&1
    else
        echo "You need to run make"
    fi
fi

if [[ $1 == "clean" ]]; then 
    if [ -d ./spark_version ] ; then
        rm -rf ./spark_version
    fi
    exit 0
fi

if [[ $1 == "halt" ]]; then
    exit 0
fi

echo "Error: Unknown parameter. See usage using --help"
exit 0
