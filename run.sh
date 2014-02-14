#!/bin/bash
#bash script to start up terminals and ssh to computers.
#These computers’ names are read from a file named comps.
for i in `cat ./comps`
do
 echo 'sshing to '${i}
 gnome-terminal -x bash -c "ssh -t ${i} 'echo 'hello world!'; pwd; bash'" &
done
