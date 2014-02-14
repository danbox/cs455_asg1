#!/bin/bash
#bash script to start up terminals and ssh to computers.
#These computers’ names are read from a file named comps.
gnome-terminal -x bash -c "ssh -t charleston.cs.colostate.edu 'echo 'helloworld!'; cd ~/cs455/asg1/cs455_asg1; java cs455.overlay.node.Registry 23456; bash '" &
sleep 2
for i in `cat ./comps`
do
 echo 'sshing to '${i}
 gnome-terminal -x bash -c "ssh -t ${i} 'echo 'hello world!'; cd ~/cs455/asg1/cs455_asg1; java cs455.overlay.node.MessagingNode charleston 23456; bash'" &
done
